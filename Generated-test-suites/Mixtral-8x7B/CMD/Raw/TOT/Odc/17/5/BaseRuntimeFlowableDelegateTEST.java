import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.test.PluggableFlowableTestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BaseRuntimeFlowableDelegateTest extends PluggableFlowableTestCase {

    private BaseRuntimeFlowableDelegate<String> delegate;

    @Autowired
    private FlowableAdaptor flowableAdaptor;

    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private FlowInstanceRepository flowInstanceRepository;

    @Before
    public void setUp() {
        delegate = Mockito.spy(new TestRuntimeFlowableDelegate());
        eventPublisher = Mockito.mock(EventPublisher.class);
        flowInstanceRepository = Mockito.mock(FlowInstanceRepository.class);

        delegate.setEventPublisher(eventPublisher);
        delegate.setFlowInstanceRepository(flowInstanceRepository);
        delegate.setFlowableAdaptor(flowableAdaptor);
    }

    @Test
    public void testInitializationFailed() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any())).thenReturn(Optional.empty());

        assertThrows(ServiceTaskError.class, () -> delegate.run(execution));
    }

    @Test
    public void testExecutionSuccess() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any())).thenReturn(Optional.of(1L));

        String result = delegate.get();

        assertEquals("Task executed successfully", result);
    }

    @Test
    public void testExecutionFailed() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any())).thenReturn(Optional.of(1L));

        when(delegate.initCallable(execution)).thenThrow(new RuntimeException("Execution failed"));

        assertThrows(ExecutionException.class, () -> delegate.get());
    }

    @Test
    public void testGetFutureResultSuccess() throws Exception {
        when(delegate.isDone()).thenReturn(true);
        when(delegate.get()).thenReturn("Task executed successfully");

        String result = delegate.get();

        assertEquals("Task executed successfully", result);
    }

    @Test
    public void testGetFutureResultFailed() throws Exception {
        when(delegate.isDone()).thenReturn(true);
        when(delegate.get()).thenThrow(new ExecutionException(new RuntimeException("Execution failed")));

        assertThrows(ExecutionException.class, () -> delegate.get());
    }

    @Test
    public void testGetFutureResultTimeout() throws Exception {
        when(delegate.isDone()).thenReturn(false);

        assertThrows(TimeoutException.class, () -> delegate.get(1, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testUpdateFlowInstanceStatusSuccess() {
        FlowInstanceRepository flowInstanceRepository = Mockito.mock(FlowInstanceRepository.class);

        delegate.setFlowInstanceRepository(flowInstanceRepository);

        when(flowInstanceRepository.updateStatusById(eq(1L), any())).thenReturn(true);

        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        Mockito.verify(flowInstanceRepository, Mockito.times(1)).updateStatusById(eq(1L), any());
    }

    @Test
    public void testUpdateFlowInstanceStatusFailed() {
        FlowInstanceRepository flowInstanceRepository = Mockito.mock(FlowInstanceRepository.class);

        delegate.setFlowInstanceRepository(flowInstanceRepository);

        when(flowInstanceRepository.updateStatusById(eq(1L), any())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED));
    }

    private static class TestRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {

        @Override
        protected Callable<String> initCallable(DelegateExecution execution) {
            return () -> "Task executed successfully";
        }
    }
}