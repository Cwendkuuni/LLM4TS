import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.test.PluggableFlowableTestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BaseRuntimeFlowableDelegateTest extends PluggableFlowableTestCase {

    private static final String PROCESS_DEFINITION_ID = "myProcess";
    private static final String ACTIVITY_ID = "myTask";
    private static final Long FLOW_INSTANCE_ID = 1L;
    private static final Long TARGET_TASK_INSTANCE_ID = 2L;
    private static final TaskType TASK_TYPE = TaskType.SERVICE;
    private static final FlowStatus FLOW_STATUS = FlowStatus.RUNNING;

    private BaseRuntimeFlowableDelegate<String> delegate;
    private DelegateExecution execution;
    private EventPublisher eventPublisher;
    private FlowInstanceRepository flowInstanceRepository;
    private FlowableAdaptor flowableAdaptor;
    private ExecutionStrategyConfig strategyConfig;

    @Before
    public void setUp() {
        // Initialize mocks
        eventPublisher = Mockito.mock(EventPublisher.class);
        flowInstanceRepository = Mockito.mock(FlowInstanceRepository.class);
        flowableAdaptor = Mockito.mock(FlowableAdaptor.class);

        // Initialize delegate
        delegate = new TestRuntimeFlowableDelegate(eventPublisher, flowInstanceRepository, flowableAdaptor);

        // Initialize execution
        execution = Mockito.mock(DelegateExecution.class);
        when(execution.getProcessDefinitionId()).thenReturn(PROCESS_DEFINITION_ID);
        when(execution.getCurrentActivityId()).thenReturn(ACTIVITY_ID);

        // Initialize strategyConfig
        strategyConfig = new ExecutionStrategyConfig();
        strategyConfig.setRetryIntervalMillis(1000);
        strategyConfig.setRetryTimes(3);

        // Initialize delegate
        delegate.setEventPublisher(eventPublisher);
        delegate.setFlowInstanceRepository(flowInstanceRepository);
        delegate.setFlowableAdaptor(flowableAdaptor);
        delegate.setStrategyConfig(strategyConfig);
    }

    @Test
    public void testInitialization() throws Exception {
        // Test initialization
        delegate.initTargetTaskInstanceId(execution);

        // Verify that flowableAdaptor.getFlowInstanceIdByProcessDefinitionId was called
        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(PROCESS_DEFINITION_ID);

        // Verify that flowableAdaptor.getTaskInstanceByActivityId was called
        ArgumentCaptor<String> activityIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> flowInstanceIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(flowableAdaptor, times(1))
                .getTaskInstanceByActivityId(activityIdCaptor.capture(), flowInstanceIdCaptor.capture());
        assertEquals(ACTIVITY_ID, activityIdCaptor.getValue());
        assertEquals(FLOW_INSTANCE_ID, flowInstanceIdCaptor.getValue());

        // Verify that delegate properties were set correctly
        assertEquals(FLOW_INSTANCE_ID, delegate.getFlowInstanceId());
        assertEquals(TARGET_TASK_INSTANCE_ID, delegate.getTargetTaskInstanceId());
        assertEquals(TASK_TYPE, delegate.getTaskType());
        assertEquals(strategyConfig, delegate.getStrategyConfig());
    }

    @Test
    public void testPreHandle() throws Exception {
        // Test preHandle
        delegate.preHandle(execution);

        // Verify that eventPublisher.publishEvent was called
        verify(eventPublisher, times(1)).publishEvent(any());
    }

    @Test
    public void testIsDone() throws Exception {
        // Test isDone
        assertFalse(delegate.isDone());

        // Set done to true and test again
        delegate.done = true;
        assertTrue(delegate.isDone());
    }

    @Test
    public void testGet() throws Exception {
        // Test get with no timeout
        Future<String> future = Mockito.mock(Future.class);
        when(future.get()).thenReturn("testResult");
        delegate.returnObject = future;

        String result = delegate.get();
        assertEquals("testResult", result);

        // Test get with timeout
        when(future.get(anyLong(), any())).thenThrow(new TimeoutException());

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> timeoutFuture = executor.submit(() -> delegate.get(1, TimeUnit.SECONDS));
        executor.shutdownNow();

        try {
            timeoutFuture.get();
        } catch (Exception e) {
            assertTrue(e instanceof ExecutionException);
            assertTrue(e.getCause() instanceof TimeoutException);
        }
    }

    @Test
    public void testUpdateFlowInstanceStatus() throws Exception {
        // Test updateFlowInstanceStatus
        delegate.updateFlowInstanceStatus(FLOW_STATUS);

        // Verify that flowInstanceRepository.updateStatusById was called
        verify(flowInstanceRepository, times(1)).updateStatusById(FLOW_INSTANCE_ID, FLOW_STATUS);
    }

    private class TestRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {

        public TestRuntimeFlowableDelegate(EventPublisher eventPublisher,
                                            FlowInstanceRepository flowInstanceRepository,
                                            FlowableAdaptor flowableAdaptor) {
            super();
            this.eventPublisher = eventPublisher;
            this.flowInstanceRepository = flowInstanceRepository;
            this.flowableAdaptor = flowableAdaptor;
        }

        @Override
        protected Callable<String> initCallable(DelegateExecution execution) {
            return null;
        }

        @Override
        protected void preHandle(DelegateExecution execution) {
            // Test preHandle implementation
        }

        @Override
        public void execute(DelegateExecution execution) {
            // Test execute implementation
        }
    }

}