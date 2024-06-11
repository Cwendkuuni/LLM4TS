import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution delegateExecution;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(baseRuntimeFlowableDelegate, "retryExecutor",
                RetryExecutor.builder().retryIntervalMillis(100).retryTimes(2).build());
    }

    @Test
    public void testRunWithInitializationException() throws Exception {
        when(delegateExecution.getCurrentActivityId()).thenReturn("testActivityId");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any()))
                .thenThrow(new ServiceTaskError(new Exception("Initialization error")));

        baseRuntimeFlowableDelegate.run(delegateExecution);

        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(flowInstanceRepository, never()).updateStatusById(any(Long.class), any(FlowStatus.class));
    }

    @Test
    public void testRunWithExceptionDuringExecution() throws Exception {
        when(delegateExecution.getCurrentActivityId()).thenReturn("testActivityId");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any()))
                .thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(any(), any()))
                .thenReturn(Optional.of(FlowTaskInstance.builder().id(1L).build()));
        when(flowableAdaptor.getFlowInstanceById(any()))
                .thenThrow(new ServiceTaskError(new Exception("Execution error")));

        baseRuntimeFlowableDelegate.run(delegateExecution);

        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(flowInstanceRepository, never()).updateStatusById(any(Long.class), any(FlowStatus.class));
    }

    @Test
    public void testIsDone() {
        assertTrue(baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        when(delegateExecution.getCurrentActivityId()).thenReturn("testActivityId");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any()))
                .thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(any(), any()))
                .thenReturn(Optional.of(FlowTaskInstance.builder().id(1L).build()));

        baseRuntimeFlowableDelegate.run(delegateExecution);

        Future<String> future = baseRuntimeFlowableDelegate;
        assertEquals("testResult", future.get());
    }

    @Test
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        when(delegateExecution.getCurrentActivityId()).thenReturn("testActivityId");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any()))
                .thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(any(), any()))
                .thenReturn(Optional.of(FlowTaskInstance.builder().id(1L).build()));

        baseRuntimeFlowableDelegate.run(delegateExecution);

        Future<String> future = baseRuntimeFlowableDelegate;

        assertThrows(TimeoutException.class, () -> future.get(1, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance flowTaskInstance = FlowTaskInstance.builder().id(1L).build();

        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);

        assertEquals(1L, ReflectionTestUtils.getField(baseRuntimeFlowableDelegate, "targetTaskInstanceId"));
        verify(flowTaskInstance, times(1)).bindServiceTask(baseRuntimeFlowableDelegate);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.FINISHED);

        verify(flowInstanceRepository, times(1))
                .updateStatusById(ReflectionTestUtils.getField(baseRuntimeFlowableDelegate, "flowInstanceId"),
                        FlowStatus.FINISHED);
    }

    @Test
    public void testInitCallable() {
        Callable<String> callable = baseRuntimeFlowableDelegate.initCallable(delegateExecution);

        assertFalse(callable.call().isEmpty());
        verify(delegateExecution, times(1)).setVariable(any(String.class), any());
    }

    @Test
    public void testPreHandle() throws Exception {
        when(delegateExecution.getCurrentActivityId()).thenReturn("testActivityId");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any()))
                .thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(any(), any()))
                .thenReturn(Optional.of(FlowTaskInstance.builder().id(1L).build()));

        baseRuntimeFlowableDelegate.preHandle(delegateExecution);

        verify(delegateExecution, times(1)).setVariable(eq("flowInstanceId"), eq(1L));
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        when(delegateExecution.getCurrentActivityId()).thenReturn("testActivityId");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any()))
                .thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(any(), any()))
                .thenReturn(Optional.of(FlowTaskInstance.builder().id(1L).build()));

        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

        assertEquals(1L, ReflectionTestUtils.getField(baseRuntimeFlowableDelegate, "targetTaskInstanceId"));
    }

    @Test
    public void testExecute() {
        EventPublisher eventPublisher = Mockito.mock(EventPublisher.class);
        baseRuntimeFlowableDelegate.setEventPublisher(eventPublisher);

        baseRuntimeFlowableDelegate.execute(delegateExecution);

        verify(eventPublisher, times(1)).removeEventListener(any());
    }
}