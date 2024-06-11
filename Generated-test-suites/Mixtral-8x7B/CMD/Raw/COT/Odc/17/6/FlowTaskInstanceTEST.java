import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowableElementTypeImpl;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.TaskInstanceCreatedEvent;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FlowTaskInstanceTest {

    private static final FlowableElementType FLOWABLE_ELEMENT_TYPE = FlowableElementTypeImpl.of(FlowableElementType.SERVICE_TASK);
    private static final FlowNodeStatus FLOW_NODE_STATUS_PENDING = FlowNodeStatus.PENDING;
    private static final FlowNodeStatus FLOW_NODE_STATUS_EXECUTING = FlowNodeStatus.EXECUTING;
    private static final Long FLOW_INSTANCE_ID = 1L;
    private static final Long ORGANIZATION_ID = 1L;
    private static final boolean START_ENDPOINT = true;
    private static final boolean END_ENDPOINT = true;
    private static final Long TARGET_TASK_ID = 1L;
    private static final ExecutionStrategyConfig EXECUTION_STRATEGY_CONFIG = ExecutionStrategyConfig.builder()
            .strategy(FlowTaskExecutionStrategy.AUTO)
            .pendingExpireIntervalSeconds(0)
            .executionTime(Duration.ofSeconds(0))
            .build();
    private static final TaskType TASK_TYPE = TaskType.TASK_TYPE_1;
    private static final ServiceTaskInstanceEntity SERVICE_TASK_INSTANCE_ENTITY = ServiceTaskInstanceEntity.builder()
            .id(1L)
            .organizationId(ORGANIZATION_ID)
            .status(FLOW_NODE_STATUS_PENDING)
            .flowInstanceId(FLOW_INSTANCE_ID)
            .startEndpoint(START_ENDPOINT)
            .endEndpoint(END_ENDPOINT)
            .strategy(EXECUTION_STRATEGY_CONFIG.getStrategy())
            .taskType(TASK_TYPE.getValue())
            .targetTaskId(TARGET_TASK_ID)
            .build();

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    private SequenceInstanceRepository sequenceInstanceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private TaskService taskService;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private RuntimeDelegateMapper runtimeDelegateMapper;

    @Mock
    private BaseRuntimeFlowableDelegate baseRuntimeFlowableDelegate;

    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        flowTaskInstance = new FlowTaskInstance(FLOW_NODE_TYPE, ORGANIZATION_ID, FLOW_INSTANCE_ID,
                EXECUTION_STRATEGY_CONFIG, START_ENDPOINT, END_ENDPOINT, runtimeDelegateMapper,
                flowableAdaptor, eventPublisher, taskService, nodeInstanceEntityRepository, sequenceInstanceRepository,
                serviceTaskInstanceRepository);

        Mockito.when(runtimeDelegateMapper.map(TASK_TYPE))
                .thenReturn(baseRuntimeFlowableDelegate.getClass());
    }

    @Test
    public void testConstructor() {
        assertNotNull(flowTaskInstance);
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FLOWABLE_ELEMENT_TYPE, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
        List<ServiceTaskInstanceEntity> entities = Collections.singletonList(SERVICE_TASK_INSTANCE_ENTITY);

        when(serviceTaskInstanceRepository.batchCreate(any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        FlowTaskInstance.batchCreate(instances, serviceTaskInstanceRepository);

        ArgumentCaptor<List<ServiceTaskInstanceEntity>> entityArgumentCaptor =
                ArgumentCaptor.forClass(List.class);

        verify(serviceTaskInstanceRepository, times(1))
                .batchCreate(entityArgumentCaptor.capture());

        List<ServiceTaskInstanceEntity> capturedEntities = entityArgumentCaptor.getValue();

        for (int i = 0; i < capturedEntities.size(); i++) {
            assertEquals(entities.get(i), capturedEntities.get(i));
        }
    }

    @Test
    public void testCreate() {
        when(serviceTaskInstanceRepository.save(any()))
                .thenReturn(SERVICE_TASK_INSTANCE_ENTITY);

        flowTaskInstance.create();

        verify(serviceTaskInstanceRepository, times(1))
                .save(any(ServiceTaskInstanceEntity.class));

        assertEquals(SERVICE_TASK_INSTANCE_ENTITY.getId(), flowTaskInstance.getId());
        assertEquals(SERVICE_TASK_INSTANCE_ENTITY.getCreateTime(), flowTaskInstance.getCreateTime());
        assertEquals(SERVICE_TASK_INSTANCE_ENTITY.getUpdateTime(), flowTaskInstance.getUpdateTime());
    }

    @Test
    public void testUpdate() {
        when(serviceTaskInstanceRepository.update(any()))
                .thenReturn(1);

        flowTaskInstance.update();

        verify(serviceTaskInstanceRepository, times(1))
                .update(any(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testDelete() {
        Long instanceId = 1L;

        flowTaskInstance.delete(instanceId);

        verify(serviceTaskInstanceRepository, times(1))
                .deleteById(instanceId);
    }

    @Test
    public void testBindServiceTask() {
        when(baseRuntimeFlowableDelegate.getTargetTaskInstanceId())
                .thenReturn(flowTaskInstance.getId());

        flowTaskInstance.bindServiceTask(baseRuntimeFlowableDelegate);

        assertEquals(FLOW_NODE_STATUS_EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testIsPresentOnThisMachine() {
        when(baseRuntimeFlowableDelegate.getTargetTaskInstanceId())
                .thenReturn(flowTaskInstance.getId());

        flowTaskInstance.bindServiceTask(baseRuntimeFlowableDelegate);

        assertTrue(flowTaskInstance.isPresentOnThisMachine());

        flowTaskInstance.dealloc();

        assertFalse(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testDealloc() {
        flowTaskInstance.alloc();

        flowTaskInstance.dealloc();

        verify(eventPublisher, times(1))
                .removeEventListener(flowTaskInstance.listener);

        assertNull(flowTaskInstance.listener);
    }

    @Test
    public void testAlloc() {
        flowTaskInstance.alloc();

        verify(eventPublisher, times(1))
                .addEventListener(flowTaskInstance.listener);

        assertNotNull(flowTaskInstance.listener);
    }

    @Test
    public void testConfirmExecute() {
        when(baseRuntimeFlowableDelegate.getTargetTaskInstanceId())
                .thenReturn(flowTaskInstance.getId());

        flowTaskInstance.bindServiceTask(baseRuntimeFlowableDelegate);

        flowTaskInstance.confirmExecute();

        verify(baseRuntimeFlowableDelegate, times(1))
                .complete();
    }

    @Test
    public void testAbort() {
        when(baseRuntimeFlowableDelegate.getTargetTaskInstanceId())
                .thenReturn(flowTaskInstance.getId());

        flowTaskInstance.bindServiceTask(baseRuntimeFlowableDelegate);

        flowTaskInstance.abort();

        verify(baseRuntimeFlowableDelegate, times(1))
                .cancel();
    }

    @Test
    public void testCancel() {
        when(baseRuntimeFlowableDelegate.getTargetTaskInstanceId())
                .thenReturn(flowTaskInstance.getId());

        flowTaskInstance.bindServiceTask(baseRuntimeFlowableDelegate);

        flowTaskInstance.cancel(true);

        verify(baseRuntimeFlowableDelegate, times(1))
                .cancel(true);
    }

    @Test
    public void testIsDone() {
        when(baseRuntimeFlowableDelegate.getTargetTaskInstanceId())
                .thenReturn(flowTaskInstance.getId());

        flowTaskInstance.bindServiceTask(baseRuntimeFlowableDelegate);

        when(baseRuntimeFlowableDelegate.isDone())
                .thenReturn(true);

        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        when(baseRuntimeFlowableDelegate.getTargetTaskInstanceId())
                .thenReturn(flowTaskInstance.getId());

        flowTaskInstance.bindServiceTask(baseRuntimeFlowableDelegate);

        when(baseRuntimeFlowableDelegate.get())
                .thenReturn("result");

        assertEquals("result", flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout()
            throws InterruptedException, ExecutionException, TimeoutException {
        when(baseRuntimeFlowableDelegate.getTargetTaskInstanceId())
                .thenReturn(flowTaskInstance.getId());

        flowTaskInstance.bindServiceTask(baseRuntimeFlowableDelegate);

        when(baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS))
                .thenReturn("result");

        assertEquals("result", flowTaskInstance.getResult(1, TimeUnit.SECONDS));
    }

    @Test
    public void testGetTargetDelegateClass() {
        assertEquals(baseRuntimeFlowableDelegate.getClass(),
                flowTaskInstance.getTargetDelegateClass());
    }

    @Test
    public void testComplete() {
        flowTaskInstance.complete(true);

        verify(flowableAdaptor, times(1))
                .getFlowableElementByType(flowTaskInstance.getId(),
                        FlowNodeType.SERVICE_TASK,
                        FlowableElementType.USER_TASK);

        verify(flowableAdaptor, times(1))
                .getProcessInstanceIdByFlowInstanceId(flowTaskInstance.getFlowInstanceId());

        verify(taskService, times(1))
                .createTaskQuery()
                .processInstanceId(anyString())
                .list();

        verify(taskService, times(1))
                .complete(anyString(), anyMap());
    }

    @Test
    public void testMapToTaskEntity() {
        assertEquals(SERVICE_TASK_INSTANCE_ENTITY,
                FlowTaskInstance.mapToTaskEntity(flowTaskInstance));
    }

    @Test
    public void testValidNotExists() {
        when(serviceTaskInstanceRepository.existsById(flowTaskInstance.getId()))
                .thenReturn(false);

        assertThrows(IllegalStateException.class,
                flowTaskInstance::validNotExists);

        verify(serviceTaskInstanceRepository, times(1))
                .existsById(flowTaskInstance.getId());
    }

    @Test
    public void testValidExists() {
        when(serviceTaskInstanceRepository.existsById(flowTaskInstance.getId()))
                .thenReturn(true);

        assertThrows(IllegalStateException.class,
                flowTaskInstance::validExists);

        verify(serviceTaskInstanceRepository, times(1))
                .existsById(flowTaskInstance.getId());
    }

    @Test
    public void testResourceType() {
        assertEquals(ResourceType.ODC_FLOW_TASK_INSTANCE.name(),
                flowTaskInstance.resourceType());
    }

    @Test
    public void testGetTaskType() {
        assertEquals(TASK_TYPE, flowTaskInstance.getTaskType());
    }

    @Test
    public void testGetTargetTaskId() {
        assertEquals(TARGET_TASK_ID, flowTaskInstance.getTargetTaskId());
    }

    @Test
    public void testGetStrategyConfig() {
        assertEquals(EXECUTION_STRATEGY_CONFIG, flowTaskInstance.getStrategyConfig());
    }
}