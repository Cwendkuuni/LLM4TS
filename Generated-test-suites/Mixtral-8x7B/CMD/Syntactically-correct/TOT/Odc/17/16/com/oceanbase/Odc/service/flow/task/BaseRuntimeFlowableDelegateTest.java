import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.test.PluggableFlowableTestCase;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BaseRuntimeFlowableDelegateTest extends PluggableFlowableTestCase {

    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;
    private DelegateExecution delegateExecution;
    private FlowableAdaptor flowableAdaptor;
    private EventPublisher eventPublisher;
    private FlowInstanceRepository flowInstanceRepository;

    @Before
    public void setUp() {
        baseRuntimeFlowableDelegate = spy(new TestBaseRuntimeFlowableDelegate());
        delegateExecution = mock(DelegateExecution.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        eventPublisher = mock(EventPublisher.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);

        baseRuntimeFlowableDelegate.setFlowableAdaptor(flowableAdaptor);
        baseRuntimeFlowableDelegate.setEventPublisher(eventPublisher);
        baseRuntimeFlowableDelegate.setFlowInstanceRepository(flowInstanceRepository);
    }

    @Test
    public void testRunSuccess() throws Exception {
        when(delegateExecution.getCurrentActivityId()).thenReturn("testActivityId");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("testProcessDefinitionId"))
                .thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId("testActivityId", 1L))
                .thenReturn(Optional.of(mock(FlowTaskInstance.class)));
        when(flowableAdaptor.getTaskInstanceByActivityId("nonexistent", 1L))
                .thenReturn(Optional.empty());

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenAnswer(invocation -> Executors.newSingleThreadExecutor().submit(() -> 1L).get());

        ArgumentCaptor<String> activityIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<FlowTaskInstance> taskInstanceCaptor = ArgumentCaptor.forClass(FlowTaskInstance.class);

        baseRuntimeFlowableDelegate.run(delegateExecution);

        verify(flowableAdaptor, times(1))
                .getFlowInstanceIdByProcessDefinitionId("testProcessDefinitionId");
        verify(flowableAdaptor, times(1))
                .getTaskInstanceByActivityId(activityIdCaptor.capture(), taskInstanceCaptor.capture());
        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(taskInstanceCaptor.getValue(), times(1)).bindServiceTask(baseRuntimeFlowableDelegate);

        assertEquals("testActivityId", activityIdCaptor.getValue());
    }

    @Test
    public void testRunFailure() throws Exception {
        when(delegateExecution.getCurrentActivityId()).thenThrow(new RuntimeException("Test exception"));

        try {
            baseRuntimeFlowableDelegate.run(delegateExecution);
            fail("Expected exception was not thrown");
        } catch (Exception e) {
            assertEquals("Test exception", e.getMessage());
        }
    }

    @Test
    public void testIsDone() {
        assertTrue(baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        Future<String> future = baseRuntimeFlowableDelegate.get();
        assertEquals("Test result", future.get());
    }

    @Test(expected = ExecutionException.class)
    public void testGetException() throws InterruptedException, ExecutionException, TimeoutException {
        when(delegateExecution.getCurrentActivityId()).thenThrow(new RuntimeException("Test exception"));
        baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS);
    }

    @Test
    public void testUpdateFlowInstanceStatusSuccess() {
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class)))
                .thenReturn(true);

        assertTrue(baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED));
    }

    @Test
    public void testUpdateFlowInstanceStatusFailure() {
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class)))
                .thenReturn(false);

        assertFalse(baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED));
    }

    private class TestBaseRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {

        @Override
        protected void preHandle(DelegateExecution delegateExecution) {
            // Do nothing
        }

        @Override
        protected Callable<String> initCallable(DelegateExecution delegateExecution) {
            return () -> "Test result";
        }

    }

}