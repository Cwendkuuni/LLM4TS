import com.oceanbase.odc.core.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableElementType;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@Slf4j
public class FlowTaskInstanceTest {

    @Mock
    private TaskService taskService;

    @Mock
    private NodeInstanceEntityRepository nodeRepository;

    @Mock
    private SequenceInstanceRepository sequenceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepository;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private RuntimeDelegateMapper delegateConvertor;

    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setUp() {
        when(delegateConvertor.map(TaskType.TEST)).thenReturn(Mockito.mock(BaseRuntimeFlowableDelegate.class));

        flowTaskInstance = new FlowTaskInstance(
                TaskType.TEST,
                1L,
                1L,
                ExecutionStrategyConfig.builder()
                        .strategy(FlowTaskExecutionStrategy.AUTO)
                        .pendingExpireIntervalSeconds(60)
                        .executionTime(Duration.ofSeconds(5))
                        .build(),
                true,
                false,
                delegateConvertor,
                flowableAdaptor,
                eventPublisher,
                taskService,
                nodeRepository,
                sequenceRepository,
                serviceTaskRepository
        );
    }

    @After
    public void tearDown() {
        Mockito.verifyNoMoreInteractions(taskService, nodeRepository, sequenceRepository, serviceTaskRepository,
                flowableAdaptor, eventPublisher, delegateConvertor);
    }

    @Test
    public void testConstructor() {
        // Check default constructor values
        Assert.assertEquals(FlowNodeType.SERVICE_TASK, flowTaskInstance.getNodeType());
        Assert.assertNull(flowTaskInstance.getId());
        Assert.assertNull(flowTaskInstance.getCreateTime());
        Assert.assertNull(flowTaskInstance.getUpdateTime());
        Assert.assertFalse(flowTaskInstance.isStartEndpoint());
        Assert.assertFalse(flowTaskInstance.isEndEndPoint());

        // Check constructor with arguments
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setId(1L);
        entity.setOrganizationId(1L);
        entity.setStatus(FlowNodeStatus.PENDING);
        entity.setFlowInstanceId(1L);
        entity.setCreateTime(Instant.now().toEpochMilli());
        entity.setUpdateTime(Instant.now().toEpochMilli());
        entity.setStartEndpoint(true);
        entity.setEndEndpoint(false);
        flowTaskInstance = new FlowTaskInstance(entity, delegateConvertor, flowableAdaptor, eventPublisher, taskService,
                nodeRepository, sequenceRepository, serviceTaskRepository);
        Assert.assertEquals(1L, flowTaskInstance.getId().longValue());
        Assert.assertNotNull(flowTaskInstance.getCreateTime());
        Assert.assertNotNull(flowTaskInstance.getUpdateTime());
        Assert.assertTrue(flowTaskInstance.isStartEndpoint());
        Assert.assertFalse(flowTaskInstance.isEndEndPoint());
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
        when(serviceTaskRepository.batchCreate(any())).thenReturn(
                Collections.singletonList(new ServiceTaskInstanceEntity()));

        List<FlowTaskInstance> createdInstances = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
        Assert.assertEquals(1, createdInstances.size());
        Assert.assertNotNull(createdInstances.get(0).getId());
    }

    @Test
    public void testCreate() {
        when(serviceTaskRepository.save(any())).thenReturn(new ServiceTaskInstanceEntity());

        flowTaskInstance.create();

        verify(serviceTaskRepository, times(1)).save(any());
    }

    @Test
    public void testUpdate() {
        flowTaskInstance.setStatus(FlowNodeStatus.EXECUTING);
        when(serviceTaskRepository.update(any())).thenReturn(1);

        flowTaskInstance.update();

        verify(serviceTaskRepository, times(1)).update(any());
    }

    @Test
    public void testDelete() {
        flowTaskInstance.setId(1L);

        flowTaskInstance.delete(serviceTaskRepository);

        verify(serviceTaskRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> delegate = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(delegate);

        verify(flowTaskInstance, times(1)).setStatus(FlowNodeStatus.EXECUTING);
        verify(delegate, times(1)).bindTaskInstance(anyLong());
    }

    @Test
    public void testDealloc() {
        ServiceTaskStartedListener listener = Mockito.mock(ServiceTaskStartedListener.class);
        flowTaskInstance.listener = listener;

        flowTaskInstance.dealloc();

        verify(eventPublisher, times(1)).removeEventListener(listener);
        Assert.assertNull(flowTaskInstance.listener);
    }

    @Test
    public void testAlloc() {
        ServiceTaskStartedListener listener = Mockito.mock(ServiceTaskStartedListener.class);
        flowTaskInstance.listener = null;

        flowTaskInstance.alloc();

        verify(eventPublisher, times(1)).addEventListener(listener);
        Assert.assertNotNull(flowTaskInstance.listener);
    }

    @Test
    public void testComplete() throws ExecutionException, InterruptedException {
        BaseRuntimeFlowableDelegate<?> delegate = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.targetTaskHandle = delegate;

        flowTaskInstance.complete(false);

        verify(delegate, times(1)).cancel(false);
        verify(taskService, times(1)).complete(anyString(), any());
    }

    @Test
    public void testConfirmExecute() throws ExecutionException, InterruptedException {
        BaseRuntimeFlowableDelegate<?> delegate = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.targetTaskHandle = delegate;

        flowTaskInstance.confirmExecute();

        verify(delegate, times(1)).cancel(false);
    }

    @Test
    public void testAbort() throws ExecutionException, InterruptedException {
        BaseRuntimeFlowableDelegate<?> delegate = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.targetTaskHandle = delegate;

        flowTaskInstance.abort();

        verify(delegate, times(1)).cancel(true);
    }

    @Test
    public void testCancel() throws ExecutionException, InterruptedException {
        BaseRuntimeFlowableDelegate<?> delegate = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.targetTaskHandle = delegate;

        flowTaskInstance.cancel(true);

        verify(delegate, times(1)).cancel(true);
    }

    @Test
    public void testIsDone() throws ExecutionException, InterruptedException {
        BaseRuntimeFlowableDelegate<?> delegate = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.targetTaskHandle = delegate;

        when(delegate.isDone()).thenReturn(true);

        Assert.assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws ExecutionException, InterruptedException {
        BaseRuntimeFlowableDelegate<?> delegate = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.targetTaskHandle = delegate;

        when(delegate.get()).thenReturn("Test Result");

        Assert.assertEquals("Test Result", flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws ExecutionException, InterruptedException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> delegate = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.targetTaskHandle = delegate;

        when(delegate.get(1, TimeUnit.SECONDS)).thenThrow(new TimeoutException());

        Assert.assertThrows(TimeoutException.class, () -> flowTaskInstance.getResult(1, TimeUnit.SECONDS));
    }

    @Test
    public void testIsPresentOnThisMachine() {
        BaseRuntimeFlowableDelegate<?> delegate = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.targetTaskHandle = delegate;
        when(delegate.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());

        Assert.assertTrue(flowTaskInstance.isPresentOnThisMachine());

        flowTaskInstance.targetTaskHandle = null;
        Assert.assertFalse(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testResourceType() {
        Assert.assertEquals("ODC_FLOW_TASK_INSTANCE", flowTaskInstance.resourceType());
    }

    @Test
    public void testGetTargetDelegateClass() {
        Class<? extends BaseRuntimeFlowableDelegate<?>> targetDelegateClass = flowTaskInstance.getTargetDelegateClass();
        Assert.assertNotNull(targetDelegateClass);
    }

    @Test
    public void testMapToTaskEntity() {
        ServiceTaskInstanceEntity entity = FlowTaskInstance.mapToTaskEntity(flowTaskInstance);

        Assert.assertNotNull(entity);
        Assert.assertEquals(flowTaskInstance.getOrganizationId(), entity.getOrganizationId());
        Assert.assertEquals(flowTaskInstance.getStatus(), entity.getStatus());
        Assert.assertEquals(flowTaskInstance.getFlowInstanceId(), entity.getFlowInstanceId());
        Assert.assertEquals(flowTaskInstance.isStartEndpoint(), entity.isStartEndpoint());
        Assert.assertEquals(flowTaskInstance.isEndEndPoint(), entity.isEndEndpoint());
        Assert.assertEquals(flowTaskInstance.strategyConfig.getStrategy(), entity.getStrategy());
        Assert.assertEquals(flowTaskInstance.getTaskType(), entity.getTaskType());
        Assert.assertEquals(flowTaskInstance.strategyConfig.getPendingExpireIntervalSeconds(), entity.getWaitExecExpireIntervalSeconds());
        Assert.assertEquals(flowTaskInstance.strategyConfig.getExecutionTime().toSeconds(), entity.getExecutionTime());
    }

    @Test
    public void testValidNotExists() {
        when(serviceTaskRepository.existsById(flowTaskInstance.getId())).thenReturn(false);

        flowTaskInstance.validNotExists();

        verify(serviceTaskRepository, times(1)).existsById(flowTaskInstance.getId());
    }

    @Test
    public void testValidNotExistsException() {
        when(serviceTaskRepository.existsById(flowTaskInstance.getId())).thenReturn(true);

        Assert.assertThrows(IllegalStateException.class, flowTaskInstance::validNotExists);
    }

    @Test
    public void testValidExists() {
        when(serviceTaskRepository.existsById(flowTaskInstance.getId())).thenReturn(true);

        flowTaskInstance.validExists();

        verify(serviceTaskRepository, times(1)).existsById(flowTaskInstance.getId());
    }

    @Test
    public void testValidExistsException() {
        when(serviceTaskRepository.existsById(flowTaskInstance.getId())).thenReturn(false);

        Assert.assertThrows(IllegalStateException.class, flowTaskInstance::validExists);
    }

    @Test
    public void testFindTasksByProcessInstanceId() {
        List<Task> tasks = Collections.singletonList(Mockito.mock(Task.class));
        when(taskService.createTaskQuery().processInstanceId(anyString())
                .list()).thenReturn(tasks);

        List<Task> result = flowTaskInstance.findTasksByProcessInstanceId("1", taskService);

        Assert.assertEquals(tasks, result);
        verify(taskService, times(1)).createTaskQuery().processInstanceId(anyString()).list();
    }

    @Test
    public void testExecuteNode() throws ExecutionException, InterruptedException {
        List<FlowableElement> elements = Collections.singletonList(Mockito.mock(FlowableElement.class));
        when(flowableAdaptor.getFlowableElementByType(anyLong(), any(), any()))
                .thenReturn(elements);

        Optional<String> optional = Optional.of("1");
        when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(anyLong()))
                .thenReturn(optional);

        flowTaskInstance.executeNode("1", flowableAdaptor);

        verify(flowableAdaptor, times(1)).getFlowableElementByType(anyLong(), any(), any());
        verify(flowableAdaptor, times(1)).getProcessInstanceIdByFlowInstanceId(anyLong());
        verify(taskService, times(1)).complete(anyString(), any());
    }

    @Test
    public void testExecuteNodeWhenNoTasks() throws ExecutionException, InterruptedException {
        List<FlowableElement> elements = Collections.emptyList();
        when(flowableAdaptor.getFlowableElementByType(anyLong(), any(), any()))
                .thenReturn(elements);

        flowTaskInstance.executeNode("1", flowableAdaptor);

        verify(flowableAdaptor, times(1)).getFlowableElementByType(anyLong(), any(), any());
        verify(taskService, never()).complete(anyString(), any());
    }
}