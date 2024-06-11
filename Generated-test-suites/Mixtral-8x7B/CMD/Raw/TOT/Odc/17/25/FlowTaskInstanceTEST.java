import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowableEngine;
import com.oceanbase.odc.service.flow.FlowNodeInstance;
import com.oceanbase.odc.service.flow.FlowNodeInstanceTest;
import com.oceanbase.odc.service.flow.FlowNodeType;
import com.oceanbase.odc.service.flow.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowInstance;
import com.oceanbase.odc.service.flow.model.TaskInstanceCreatedEvent;
import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FlowTaskInstanceTest {

    private static final String FLOW_INSTANCE_ID = "123";
    private static final String ORGANIZATION_ID = "456";
    private static final String PROCESS_INSTANCE_ID = "789";
    private static final String TASK_NAME = "Test Task";
    private static final String TASK_TYPE = "Test Task Type";
    private static final Long TARGET_TASK_ID = 987L;
    private static final Duration WAIT_EXEC_EXPIRE_INTERVAL = Duration.ofSeconds(10);
    private static final Duration EXECUTION_TIME = Duration.ofSeconds(20);
    private static final FlowTaskExecutionStrategy STRATEGY = FlowTaskExecutionStrategy.MANUAL;
    private static final ExecutionStrategyConfig STRATEGY_CONFIG =
            new ExecutionStrategyConfig(STRATEGY, WAIT_EXEC_EXPIRE_INTERVAL.toSeconds(), EXECUTION_TIME.toSeconds());

    private FlowTaskInstance flowTaskInstance;

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    private SequenceInstanceRepository sequenceInstanceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

    @Mock
    private FlowableEngine flowableEngine;

    @Mock
    private TaskService taskService;

    @Mock
    private ServiceTaskStartedListener serviceTaskStartedListener;

    @Captor
    private ArgumentCaptor<TaskInstanceCreatedEvent> taskInstanceCreatedEventArgumentCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        FlowableAdaptor flowableAdaptor = new FlowableAdaptorImpl(flowableEngine);
        flowTaskInstance = new FlowTaskInstance(TASK_TYPE, ORGANIZATION_ID, FLOW_INSTANCE_ID, STRATEGY_CONFIG,
                true, false, flowableAdaptor.getRuntimeDelegateMapper(), flowableAdaptor,
                new EventPublisher(), taskService, nodeInstanceEntityRepository, sequenceInstanceRepository,
                serviceTaskInstanceRepository);
    }

    @Test
    public void testFlowTaskInstanceCreation() {
        // Test happy path
        flowTaskInstance.create();

        verify(serviceTaskInstanceRepository).save(any(ServiceTaskInstanceEntity.class));
        assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
        assertEquals(FLOW_INSTANCE_ID, flowTaskInstance.getFlowInstanceId());
        assertEquals(ORGANIZATION_ID, flowTaskInstance.getOrganizationId());
        assertEquals(TASK_TYPE, flowTaskInstance.getTaskType());
        assertEquals(STRATEGY, flowTaskInstance.getStrategy());
        assertEquals(WAIT_EXEC_EXPIRE_INTERVAL.toSeconds(), flowTaskInstance.getWaitExecExpireIntervalSeconds());
        assertEquals(EXECUTION_TIME.toSeconds(), flowTaskInstance.getExecutionTime());
        assertEquals(TARGET_TASK_ID, flowTaskInstance.getTargetTaskId());
        assertNotNull(flowTaskInstance.getId());
        assertNotNull(flowTaskInstance.getCreateTime());
        assertNotNull(flowTaskInstance.getUpdateTime());
    }

    @Test
    public void testMapToTaskEntity() {
        // Test happy path
        ServiceTaskInstanceEntity entity = flowTaskInstance.mapToTaskEntity(flowTaskInstance);

        assertEquals(ORGANIZATION_ID, entity.getOrganizationId());
        assertEquals(FlowNodeStatus.PENDING, entity.getStatus());
        assertEquals(FLOW_INSTANCE_ID, entity.getFlowInstanceId());
        assertEquals(true, entity.isStartEndpoint());
        assertEquals(false, entity.isEndEndpoint());
        assertEquals(STRATEGY, entity.getStrategy());
        assertEquals(TASK_TYPE, entity.getTaskType());
        assertEquals(TARGET_TASK_ID, entity.getTargetTaskId());
        assertEquals(WAIT_EXEC_EXPIRE_INTERVAL.toSeconds(), entity.getWaitExecExpireIntervalSeconds());
        assertEquals(EXECUTION_TIME.toSeconds(), entity.getExecutionTime());
    }

    @Test
    public void testBatchCreate() {
        // Test happy path
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
        List<ServiceTaskInstanceEntity> entities =
                FlowTaskInstance.batchCreate(instances, serviceTaskInstanceRepository);

        assertEquals(1, entities.size());
        assertEquals(flowTaskInstance.getId(), entities.get(0).getId());
        assertEquals(flowTaskInstance.getCreateTime(), entities.get(0).getCreateTime());
        assertEquals(flowTaskInstance.getUpdateTime(), entities.get(0).getUpdateTime());
    }

    @Test
    public void testUpdate() {
        // Test happy path
        flowTaskInstance.update();

        verify(serviceTaskInstanceRepository).update(any(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testValidNotExists() {
        when(serviceTaskInstanceRepository.existsById(flowTaskInstance.getId())).thenReturn(false);

        flowTaskInstance.validNotExists();

        verify(serviceTaskInstanceRepository).existsById(flowTaskInstance.getId());
    }

    @Test(expected = IllegalStateException.class)
    public void testValidNotExistsException() {
        when(serviceTaskInstanceRepository.existsById(flowTaskInstance.getId())).thenReturn(true);

        flowTaskInstance.validNotExists();
    }

    @Test
    public void testValidExists() {
        when(serviceTaskInstanceRepository.existsById(flowTaskInstance.getId())).thenReturn(true);

        flowTaskInstance.validExists();

        verify(serviceTaskInstanceRepository).existsById(flowTaskInstance.getId());
    }

    @Test(expected = IllegalStateException.class)
    public void testValidExistsException() {
        when(serviceTaskInstanceRepository.existsById(flowTaskInstance.getId())).thenReturn(false);

        flowTaskInstance.validExists();
    }

    @Test
    public void testDoDelete() {
        // Test happy path
        flowTaskInstance.doDelete(1L);

        verify(serviceTaskInstanceRepository).deleteById(1L);
    }

    @Test
    public void testBindServiceTask() {
        // Test happy path
        BaseRuntimeFlowableDelegate<?> targetTaskHandle = new BaseRuntimeFlowableDelegateTest();
        flowTaskInstance.bindServiceTask(targetTaskHandle);

        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
        assertEquals(targetTaskHandle, flowTaskInstance.getTargetTaskHandle());

        // Test already bound
        flowTaskInstance.bindServiceTask(targetTaskHandle);

        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
        assertEquals(targetTaskHandle, flowTaskInstance.getTargetTaskHandle());
    }

    @Test
    public void testDealloc() {
        // Test not allocated
        flowTaskInstance.dealloc();

        verify(serviceTaskStartedListener, never()).unregister();

        // Test allocated
        flowTaskInstance.alloc();
        flowTaskInstance.dealloc();

        verify(serviceTaskStartedListener).unregister();
    }

    @Test
    public void testAlloc() {
        // Test already allocated
        flowTaskInstance.alloc();

        verify(eventPublisher, never()).addEventListener(serviceTaskStartedListener);

        // Test not allocated
        flowTaskInstance.dealloc();
        flowTaskInstance.alloc();

        verify(eventPublisher).addEventListener(serviceTaskStartedListener);
    }

    @Test
    public void testComplete() {
        // Test manual strategy
        flowTaskInstance.setStrategy(FlowTaskExecutionStrategy.MANUAL);
        flowTaskInstance.complete(true);

        verify(taskService).complete(anyString(), anyMap());

        // Test auto strategy
        flowTaskInstance.setStrategy(FlowTaskExecutionStrategy.AUTO);
        flowTaskInstance.complete(true);

        verify(taskService, never()).complete(anyString(), anyMap());
    }

    @Test
    public void testCancel() throws ExecutionException, InterruptedException {
        // Test not present
        assertFalse(flowTaskInstance.isPresentOnThisMachine());
        flowTaskInstance.cancel(true);

        verify(targetTaskHandle, never()).cancel(anyBoolean());

        // Test present
        flowTaskInstance.bindServiceTask(new BaseRuntimeFlowableDelegateTest() {
            @Override
            public void execute() throws Exception {
                Thread.sleep(1000);
            }
        });
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
        flowTaskInstance.cancel(true);

        verify(targetTaskHandle).cancel(true);
    }

    @Test
    public void testIsDone() throws ExecutionException, InterruptedException {
        // Test not present
        assertFalse(flowTaskInstance.isPresentOnThisMachine());
        assertFalse(flowTaskInstance.isDone());

        // Test present and done
        flowTaskInstance.bindServiceTask(new BaseRuntimeFlowableDelegateTest() {
            @Override
            public void execute() throws Exception {
                Thread.sleep(1000);
            }
        });
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
        assertTrue(flowTaskInstance.isDone());

        // Test present and not done
        flowTaskInstance.bindServiceTask(new BaseRuntimeFlowableDelegateTest() {
            @Override
            public void execute() throws Exception {
                Thread.sleep(500);
            }
        });
        assertFalse(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws ExecutionException, InterruptedException, TimeoutException {
        // Test not present
        assertFalse(flowTaskInstance.isPresentOnThisMachine());
        assertNull(flowTaskInstance.getResult());

        // Test present and done
        flowTaskInstance.bindServiceTask(new BaseRuntimeFlowableDelegateTest() {
            @Override
            public void execute() throws Exception {
                Thread.sleep(1000);
            }
        });
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
        assertEquals("Result", flowTaskInstance.getResult());

        // Test present and not done
        flowTaskInstance.bindServiceTask(new BaseRuntimeFlowableDelegateTest() {
            @Override
            public void execute() throws Exception {
                Thread.sleep(500);
            }
        });
        assertNull(flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultTimeUnit()
            throws ExecutionException, InterruptedException, TimeoutException {
        // Test not present
        assertFalse(flowTaskInstance.isPresentOnThisMachine());
        assertNull(flowTaskInstance.getResult(1, TimeUnit.SECONDS));

        // Test present and done
        flowTaskInstance.bindServiceTask(new BaseRuntimeFlowableDelegateTest() {
            @Override
            public void execute() throws Exception {
                Thread.sleep(1000);
            }
        });
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
        assertEquals("Result", flowTaskInstance.getResult(1, TimeUnit.SECONDS));

        // Test present and not done
        flowTaskInstance.bindServiceTask(new BaseRuntimeFlowableDelegateTest() {
            @Override
            public void execute() throws Exception {
                Thread.sleep(500);
            }
        });
        assertNull(flowTaskInstance.getResult(1, TimeUnit.SECONDS));
    }

    @Test
    public void testGetTargetDelegateClass() {
        // Test present
        flowTaskInstance.bindServiceTask(new BaseRuntimeFlowableDelegateTest() {});
        assertEquals(BaseRuntimeFlowableDelegateTest.class,
                flowTaskInstance.getTargetDelegateClass());

        // Test not present
        assertFalse(flowTaskInstance.isPresentOnThisMachine());
        assertNull(flowTaskInstance.getTargetDelegateClass());
    }

    private class BaseRuntimeFlowableDelegateTest extends BaseRuntimeFlowableDelegate<Object> {

        @Override
        public String getTargetTaskInstanceId() {
            return FlowTaskInstanceTest.this.flowTaskInstance.getId();
        }

        @Override
        public void execute() throws Exception {
            Thread.sleep(1000);
        }

        @Override
        public Object getResult() {
            return "Result";
        }
    }

}