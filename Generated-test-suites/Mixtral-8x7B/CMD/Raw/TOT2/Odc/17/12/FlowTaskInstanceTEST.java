import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedEvent;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowInstance;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FlowTaskInstanceTest {

    @Mock
    private TaskService taskService;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepository;

    @Mock
    private SequenceInstanceRepository sequenceInstanceRepository;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private BaseRuntimeFlowableDelegate<? extends BaseRuntimeFlowableDelegate> baseRuntimeFlowableDelegate;

    @Mock
    private RuntimeDelegateMapper runtimeDelegateMapper;

    @Mock
    private EventPublisher eventPublisher;

    private ExecutorService executorService;

    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        executorService = Executors.newSingleThreadExecutor();

        when(runtimeDelegateMapper.map(TaskType.GENERIC_TASK)).thenReturn(baseRuntimeFlowableDelegate.getClass());

        flowTaskInstance = new FlowTaskInstance(
                TaskType.GENERIC_TASK,
                1L,
                1L,
                ExecutionStrategyConfig.builder()
                        .strategy(FlowTaskExecutionStrategy.MANUAL)
                        .pendingExpireIntervalSeconds(3600)
                        .executionTime(10)
                        .build(),
                true,
                true,
                runtimeDelegateMapper,
                new FlowableAdaptorImpl(),
                eventPublisher,
                taskService,
                serviceTaskRepository,
                sequenceInstanceRepository
        );
    }

    @Test
    public void testExtractMethods() {
        // Extract and list all the public methods including their signatures
        Map<String, List<String>> methodSignatures = new HashMap<>();
        methodSignatures.put("FlowTaskInstance", List.of(
                "public FlowTaskInstance(ServiceTaskInstanceEntity entity, RuntimeDelegateMapper convertor, FlowableAdaptor flowableAdaptor, EventPublisher eventPublisher, TaskService taskService, NodeInstanceEntityRepository nodeRepository, SequenceInstanceRepository sequenceRepository, ServiceTaskInstanceRepository serviceTaskRepository)",
                "public FlowTaskInstance(TaskType taskType, Long organizationId, Long flowInstanceId, ExecutionStrategyConfig strategyConfig, boolean startEndpoint, boolean endEndPoint, RuntimeDelegateMapper convertor, FlowableAdaptor flowableAdaptor, EventPublisher eventPublisher, TaskService taskService, NodeInstanceEntityRepository nodeRepository, SequenceInstanceRepository sequenceRepository, ServiceTaskInstanceRepository serviceTaskRepository)",
                "public FlowableElementType getCoreFlowableElementType()",
                "public static List<FlowTaskInstance> batchCreate(List<FlowTaskInstance> instances, ServiceTaskInstanceRepository serviceTaskInstanceRepository)",
                "public void create()",
                "public void update()",
                "public void delete()",
                "public void bindServiceTask(BaseRuntimeFlowableDelegate<?> target)",
                "public boolean isPresentOnThisMachine()",
                "public void dealloc()",
                "public void alloc()",
                "public void confirmExecute()",
                "public void abort()",
                "public void cancel(boolean mayInterruptIfRunning) throws InterruptedException, ExecutionException",
                "public boolean isDone()",
                "public Object getResult() throws InterruptedException, ExecutionException",
                "public Object getResult(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException",
                "public String resourceType()",
                "public Class<? extends BaseRuntimeFlowableDelegate<?>> getTargetDelegateClass()"
        ));

        assertEquals(methodSignatures, MethodSignatures.of(FlowTaskInstance.class));
    }

    @Test
    public void testCanCreate() {
        // Test for create() method
        flowTaskInstance.create();
        verify(serviceTaskRepository, times(1)).save(any(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testCanUpdate() {
        // Test for update() method
        Long id = 1L;
        flowTaskInstance.setId(id);
        flowTaskInstance.update();
        verify(serviceTaskRepository, times(1)).update(any(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testCanDelete() {
        // Test for delete() method
        Long id = 1L;
        flowTaskInstance.setId(id);
        flowTaskInstance.delete(id);
        verify(serviceTaskRepository, times(1)).deleteById(id);
    }

    @Test
    public void testCanBindServiceTask() {
        // Test for bindServiceTask() method
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        assertEquals(target, flowTaskInstance.targetTaskHandle);
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testCanConfirmExecute() throws Exception {
        // Test for confirmExecute() method
        FlowInstance flowInstance = Mockito.mock(FlowInstance.class);
        when(flowableAdaptor.getFlowInstanceById(flowTaskInstance.getFlowInstanceId()))
                .thenReturn(flowInstance);
        when(flowInstance.getStatus())
                .thenReturn(FlowNodeStatus.EXECUTING);

        flowTaskInstance.confirmExecute();
        verify(serviceTaskRepository, times(1)).complete(flowTaskInstance.getId());
    }

    @Test
    public void testCanAbort() throws Exception {
        // Test for abort() method
        FlowInstance flowInstance = Mockito.mock(FlowInstance.class);
        when(flowableAdaptor.getFlowInstanceById(flowTaskInstance.getFlowInstanceId()))
                .thenReturn(flowInstance);
        when(flowInstance.getStatus())
                .thenReturn(FlowNodeStatus.EXECUTING);

        flowTaskInstance.abort();
        verify(serviceTaskRepository, times(1)).abort(flowTaskInstance.getId());
    }

    @Test
    public void testCanCancel() throws Exception {
        // Test for cancel() method
        FlowInstance flowInstance = Mockito.mock(FlowInstance.class);
        when(flowableAdaptor.getFlowInstanceById(flowTaskInstance.getFlowInstanceId()))
                .thenReturn(flowInstance);
        when(flowInstance.getStatus())
                .thenReturn(FlowNodeStatus.EXECUTING);

        Future<?> future = executorService.submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        flowTaskInstance.targetTaskHandle = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            public Object execute() throws Exception {
                return future.get();
            }
        };

        flowTaskInstance.cancel(true);
        assertTrue(future.isCancelled());
    }

    @Test
    public void testIsDone() throws Exception {
        // Test for isDone() method
        FlowInstance flowInstance = Mockito.mock(FlowInstance.class);
        when(flowableAdaptor.getFlowInstanceById(flowTaskInstance.getFlowInstanceId()))
                .thenReturn(flowInstance);
        when(flowInstance.getStatus())
                .thenReturn(FlowNodeStatus.EXECUTING);

        Future<?> future = executorService.submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        flowTaskInstance.targetTaskHandle = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            public Object execute() throws Exception {
                return future.get();
            }
        };

        assertFalse(flowTaskInstance.isDone());
        future.get();
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws Exception {
        // Test for getResult() method
        FlowInstance flowInstance = Mockito.mock(FlowInstance.class);
        when(flowableAdaptor.getFlowInstanceById(flowTaskInstance.getFlowInstanceId()))
                .thenReturn(flowInstance);
        when(flowInstance.getStatus())
                .thenReturn(FlowNodeStatus.EXECUTING);

        Future<?> future = executorService.submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Result";
        });

        flowTaskInstance.targetTaskHandle = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            public Object execute() throws Exception {
                return future.get();
            }
        };

        assertEquals("Result", flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws Exception {
        // Test for getResult(long timeout, TimeUnit unit) method
        FlowInstance flowInstance = Mockito.mock(FlowInstance.class);
        when(flowableAdaptor.getFlowInstanceById(flowTaskInstance.getFlowInstanceId()))
                .thenReturn(flowInstance);
        when(flowInstance.getStatus())
                .thenReturn(FlowNodeStatus.EXECUTING);

        Future<?> future = executorService.submit(() -> {
            try {
                Thread.sleep(1100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Result";
        });

        flowTaskInstance.targetTaskHandle = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            public Object execute() throws Exception {
                return future.get();
            }
        };

        assertThrows(TimeoutException.class, () -> flowTaskInstance.getResult(1000, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testCanGetTargetDelegateClass() {
        // Test for getTargetDelegateClass() method
        assertEquals(baseRuntimeFlowableDelegate.getClass(), flowTaskInstance.getTargetDelegateClass());
    }

    @Test
    public void testCanMapToTaskEntity() {
        // Test for mapToTaskEntity() method
        ServiceTaskInstanceEntity entity = flowTaskInstance.mapToTaskEntity();
        assertNotNull(entity);
        assertEquals(flowTaskInstance.getId(), entity.getId());
        assertEquals(flowTaskInstance.getOrganizationId(), entity.getOrganizationId());
        assertEquals(flowTaskInstance.getStatus(), entity.getStatus());
        assertEquals(flowTaskInstance.getFlowInstanceId(), entity.getFlowInstanceId());
        assertEquals(flowTaskInstance.isStartEndpoint(), entity.isStartEndpoint());
        assertEquals(flowTaskInstance.isEndEndpoint(), entity.isEndEndpoint());
        assertEquals(flowTaskInstance.strategyConfig.getStrategy(), entity.getStrategy());
        assertEquals(flowTaskInstance.getTaskType(), entity.getTaskType());
        assertEquals(flowTaskInstance.strategyConfig.getPendingExpireIntervalSeconds(), entity.getWaitExecExpireIntervalSeconds());
        assertEquals(flowTaskInstance.strategyConfig.getExecutionTime(), entity.getExecutionTime());
    }

    @Test
    public void testCanBatchCreate() {
        // Test for batchCreate() method
        List<FlowTaskInstance> instances = new ArrayList<>();
        instances.add(flowTaskInstance);
        instances.add(flowTaskInstance);
        instances.add(flowTaskInstance);

        when(serviceTaskRepository.batchCreate(any()))
                .thenReturn(Collections.singletonList(flowTaskInstance.mapToTaskEntity()));

        List<FlowTaskInstance> result =
                FlowTaskInstance.batchCreate(instances, serviceTaskRepository);

        assertEquals(3, result.size());
    }
}