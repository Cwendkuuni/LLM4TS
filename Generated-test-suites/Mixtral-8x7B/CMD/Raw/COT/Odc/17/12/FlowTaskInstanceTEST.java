import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowNodeType;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest {

    private static final Long ORGANIZATION_ID = 1L;
    private static final Long FLOW_INSTANCE_ID = 2L;
    private static final Long ID = 3L;
    private static final Long TARGET_TASK_ID = 4L;
    private static final FlowNodeType NODE_TYPE = FlowNodeType.SERVICE_TASK;
    private static final FlowTaskExecutionStrategy STRATEGY = FlowTaskExecutionStrategy.MANUAL;
    private static final Integer WAIT_EXEC_EXPIRE_INTERVAL_SECONDS = 30;
    private static final Integer EXECUTION_TIME = 10;
    private static final TaskType TASK_TYPE = TaskType.DATABASE_TASK;

    private FlowTaskInstance flowTaskInstance;

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    private SequenceInstanceRepository sequenceInstanceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    @Mock
    private TaskService taskService;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private RuntimeDelegateMapper runtimeDelegateMapper;

    @Mock
    private EventPublisher eventPublisher;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        flowTaskInstance = new FlowTaskInstance(TASK_TYPE, ORGANIZATION_ID, FLOW_INSTANCE_ID,
                new ExecutionStrategyConfig(STRATEGY, WAIT_EXEC_EXPIRE_INTERVAL_SECONDS, EXECUTION_TIME),
                true, true, runtimeDelegateMapper, flowableAdaptor, eventPublisher, taskService,
                nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);
    }

    @Test
    public void testGetCoreFlowableElementType() {
        FlowableElementType flowableElementType = flowTaskInstance.getCoreFlowableElementType();
        assertEquals(FlowableElementType.SERVICE_TASK, flowableElementType);
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
        when(serviceTaskInstanceRepository.batchCreate(anyList())).thenReturn(Collections.singletonList(new ServiceTaskInstanceEntity()));

        List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskInstanceRepository);
        assertEquals(1, result.size());
        assertEquals(ID, result.get(0).getId());
    }

    @Test
    public void testCreate() {
        when(serviceTaskInstanceRepository.save(any())).thenReturn(new ServiceTaskInstanceEntity());

        flowTaskInstance.create();

        verify(serviceTaskInstanceRepository, times(1)).save(any());
    }

    @Test
    public void testUpdate() {
        when(serviceTaskInstanceRepository.update(any())).thenReturn(1);

        flowTaskInstance.update();

        verify(serviceTaskInstanceRepository, times(1)).update(any());
    }

    @Test
    public void testDelete() {
        flowTaskInstance.delete(ID);

        verify(serviceTaskInstanceRepository, times(1)).deleteById(ID);
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> delegate = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(delegate);

        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testIsPresentOnThisMachine() {
        BaseRuntimeFlowableDelegate<?> delegate = mock(BaseRuntimeFlowableDelegate.class);
        when(delegate.getTargetTaskInstanceId()).thenReturn(ID);

        flowTaskInstance.bindServiceTask(delegate);
        assertEquals(true, flowTaskInstance.isPresentOnThisMachine());

        flowTaskInstance.dealloc();
        assertEquals(false, flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testDealloc() {
        ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);
        flowTaskInstance.setListener(listener);

        flowTaskInstance.dealloc();

        verify(eventPublisher, times(1)).removeEventListener(listener);
    }

    @Test
    public void testAlloc() {
        ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);

        flowTaskInstance.alloc();

        verify(eventPublisher, times(1)).addEventListener(listener);
    }

    @Test
    public void testComplete() {
        flowTaskInstance.complete(false);

        verify(flowableAdaptor, times(1)).getFlowableElementByType(ID, NODE_TYPE, FlowableElementType.USER_TASK);
        verify(flowableAdaptor, times(1))
                .getProcessInstanceIdByFlowInstanceId(FLOW_INSTANCE_ID);
        verify(taskService, times(1)).createTaskQuery();
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();

        verify(flowTaskInstance, times(1)).complete(false);
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();

        verify(flowTaskInstance, times(1)).complete(true);
    }

    @Test
    public void testCancel() {
        BaseRuntimeFlowableDelegate<?> delegate = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(delegate);

        flowTaskInstance.cancel(true);

        verify(delegate, times(1)).cancel(true);
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<?> delegate = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(delegate);

        when(delegate.isDone()).thenReturn(true);

        assertEquals(true, flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> delegate = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(delegate);

        when(delegate.get()).thenReturn("result");

        assertEquals("result", flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> delegate = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(delegate);

        when(delegate.get(1, TimeUnit.SECONDS)).thenThrow(new TimeoutException("Timeout"));

        try {
            flowTaskInstance.getResult(1, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            assertEquals("Timeout", e.getMessage());
        }
    }

    @Test
    public void testMapToTaskEntity() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(TASK_TYPE, ORGANIZATION_ID, FLOW_INSTANCE_ID,
                new ExecutionStrategyConfig(STRATEGY, WAIT_EXEC_EXPIRE_INTERVAL_SECONDS, EXECUTION_TIME),
                true, true, runtimeDelegateMapper, flowableAdaptor, eventPublisher, taskService,
                nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);

        ServiceTaskInstanceEntity entity = FlowTaskInstance.mapToTaskEntity(flowTaskInstance);

        assertEquals(ORGANIZATION_ID, entity.getOrganizationId());
        assertEquals(FlowNodeStatus.PENDING, entity.getStatus());
        assertEquals(FLOW_INSTANCE_ID, entity.getFlowInstanceId());
        assertEquals(true, entity.isStartEndpoint());
        assertEquals(true, entity.isEndEndpoint());
        assertEquals(STRATEGY, entity.getStrategy());
        assertEquals(TASK_TYPE, entity.getTaskType());
        assertEquals(null, entity.getTargetTaskId());
        assertEquals(WAIT_EXEC_EXPIRE_INTERVAL_SECONDS, entity.getWaitExecExpireIntervalSeconds());
        assertEquals(EXECUTION_TIME, entity.getExecutionTime());
    }
}