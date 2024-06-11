import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.concurrent.Callable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

    @Mock
    private DelegateExecution delegateExecution;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private EventPublisher eventPublisher;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        baseRuntimeFlowableDelegate = spy(new TestRuntimeFlowableDelegate());
        baseRuntimeFlowableDelegate.flowableAdaptor = flowableAdaptor;
        baseRuntimeFlowableDelegate.eventPublisher = eventPublisher;
        baseRuntimeFlowableDelegate.flowInstanceRepository = flowInstanceRepository;
    }

    @Test
    public void testRun_initializationFailed() {
        doThrow(new RuntimeException("Initialization failed"))
                .when(baseRuntimeFlowableDelegate).initTargetTaskInstanceId(delegateExecution);

        assertThrows(ServiceTaskError.class, () -> baseRuntimeFlowableDelegate.run(delegateExecution));

        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(eventPublisher).removeEventListener(any(TaskInstanceCreatedListener.class));
        verify(eventPublisher).removeEventListener(any(ActiveTaskStatisticsListener.class));
    }

    @Test
    public void testRun_executionFailed() {
        doNothing().when(baseRuntimeFlowableDelegate).initTargetTaskInstanceId(delegateExecution);
        doThrow(new RuntimeException("Execution failed"))
                .when(baseRuntimeFlowableDelegate).preHandle(delegateExecution);

        assertThrows(RuntimeException.class, () -> baseRuntimeFlowableDelegate.run(delegateExecution));

        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(eventPublisher).removeEventListener(any(TaskInstanceCreatedListener.class));
        verify(eventPublisher).removeEventListener(any(ActiveTaskStatisticsListener.class));
    }

    @Test
    public void testRun_completed() throws Exception {
        doNothing().when(baseRuntimeFlowableDelegate).initTargetTaskInstanceId(delegateExecution);
        doNothing().when(baseRuntimeFlowableDelegate).preHandle(delegateExecution);

        Callable<String> callable = () -> "Test Result";
        when(delegateExecution.getCurrentActivityId()).thenReturn("testActivity");
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
                .thenReturn(Optional.of(new FlowTaskInstance()));

        baseRuntimeFlowableDelegate.run(delegateExecution);

        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(eventPublisher).removeEventListener(any(TaskInstanceCreatedListener.class));
        verify(eventPublisher).removeEventListener(any(ActiveTaskStatisticsListener.class));
        verify(baseRuntimeFlowableDelegate).preHandle(delegateExecution);
        verify(flowableAdaptor, times(2)).getTaskInstanceByActivityId(anyString(), anyLong());
        verify(flowInstanceRepository).updateStatusById(anyLong(), any(FlowStatus.class));
        assertEquals("Test Result", baseRuntimeFlowableDelegate.get());
    }

    @Test
    public void testGet_interruptedException() throws Exception {
        doNothing().when(baseRuntimeFlowableDelegate).initTargetTaskInstanceId(delegateExecution);
        doNothing().when(baseRuntimeFlowableDelegate).preHandle(delegateExecution);

        Callable<String> callable = () -> {
            Thread.currentThread().interrupt();
            return "Test Result";
        };
        when(delegateExecution.getCurrentActivityId()).thenReturn("testActivity");
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
                .thenReturn(Optional.of(new FlowTaskInstance()));

        baseRuntimeFlowableDelegate.run(delegateExecution);

        assertThrows(InterruptedException.class, baseRuntimeFlowableDelegate::get);
    }

    @Test
    public void testGet_executionException() throws Exception {
        doNothing().when(baseRuntimeFlowableDelegate).initTargetTaskInstanceId(delegateExecution);
        doThrow(new RuntimeException("Execution failed"))
                .when(baseRuntimeFlowableDelegate).preHandle(delegateExecution);

        when(delegateExecution.getCurrentActivityId()).thenReturn("testActivity");
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
                .thenReturn(Optional.of(new FlowTaskInstance()));

        baseRuntimeFlowableDelegate.run(delegateExecution);

        assertThrows(ExecutionException.class, baseRuntimeFlowableDelegate::get);
    }

    @Test
    public void testGet_timeoutException() throws Exception {
        doNothing().when(baseRuntimeFlowableDelegate).initTargetTaskInstanceId(delegateExecution);
        doNothing().when(baseRuntimeFlowableDelegate).preHandle(delegateExecution);

        when(delegateExecution.getCurrentActivityId()).thenReturn("testActivity");
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
                .thenReturn(Optional.of(new FlowTaskInstance()));

        baseRuntimeFlowableDelegate.run(delegateExecution);

        assertThrows(TimeoutException.class, () -> baseRuntimeFlowableDelegate.get(1, TimeUnit.MILLISECONDS));
    }

    private static class TestRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {

        @Override
        protected Callable<String> initCallable(DelegateExecution execution) {
            return () -> "Test Result";
        }

        @Override
        protected void preHandle(DelegateExecution execution) {
            // Do nothing
        }

        @Override
        public String get() throws InterruptedException, ExecutionException {
            PreConditions.validNotDone("BaseRuntimeFlowableDelegate", baseRuntimeFlowableDelegate);
            return super.get();
        }

        @Override
        public String get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            PreConditions.validNotDone("BaseRuntimeFlowableDelegate", baseRuntimeFlowableDelegate);
            return super.get(timeout, unit);
        }
    }
}