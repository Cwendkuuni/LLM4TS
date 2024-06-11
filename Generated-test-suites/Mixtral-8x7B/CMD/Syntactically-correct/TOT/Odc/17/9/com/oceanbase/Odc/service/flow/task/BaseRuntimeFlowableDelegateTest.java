import org.flowable.engine. delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Captor
    private ArgumentCaptor<ServiceTaskStartedEvent> serviceTaskStartedEventArgumentCaptor;

    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        ReflectionTestUtils.setField(flowableAdaptor, "eventPublisher", eventPublisher);

        baseRuntimeFlowableDelegate = new TestRuntimeFlowableDelegate();
        ReflectionTestUtils.setField(baseRuntimeFlowableDelegate, "flowableAdaptor", flowableAdaptor);
        ReflectionTestUtils.setField(baseRuntimeFlowableDelegate, "eventPublisher", applicationEventPublisher);
        ReflectionTestUtils.setField(baseRuntimeFlowableDelegate, "flowInstanceRepository", flowInstanceRepository);
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        String processDefinitionId = "processDefinitionId";
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId)).thenReturn(1L);

        String activityId = "activityId";
        when(flowableAdaptor.getTaskInstanceByActivityId(activityId, 1L)).thenReturn(Optional.of(new FlowTaskInstance()));

        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

        assertEquals("1", baseRuntimeFlowableDelegate.getFlowInstanceId());
        assertEquals(activityId, baseRuntimeFlowableDelegate.getActivityId());
        assertEquals(TaskType.SERVICE_TASK, baseRuntimeFlowableDelegate.getTaskType());
        assertNotNull(baseRuntimeFlowableDelegate.getStrategyConfig());
    }

    @Test
    public void testPreHandle() throws Exception {
        baseRuntimeFlowableDelegate.preHandle(delegateExecution);

        verify(delegateExecution, times(1)).setVariable("someVariableKey", "someVariableValue");
    }

    @Test
    public void testRun_initializationFailed() throws Exception {
        when(delegateExecution.getCurrentActivityId()).thenThrow(new Exception("Initialization failure"));

        assertThrows(Exception.class, () -> baseRuntimeFlowableDelegate.run(delegateExecution));

        verify(eventPublisher, times(1)).publishEvent(serviceTaskStartedEventArgumentCaptor.capture());
        ServiceTaskStartedEvent serviceTaskStartedEvent = serviceTaskStartedEventArgumentCaptor.getValue();
        assertEquals(baseRuntimeFlowableDelegate, serviceTaskStartedEvent.getServiceTask());
        assertFalse(serviceTaskStartedEvent.isSuccess());
    }

    @Test
    public void testRun_executionFailed() throws Exception {
        when(delegateExecution.getCurrentActivityId()).thenReturn("activityId");

        Callable<String> callable = () -> {
            throw new RuntimeException("Execution failure");
        };
        ReflectionTestUtils.setField(baseRuntimeFlowableDelegate, "callable", callable);

        assertThrows(Exception.class, () -> baseRuntimeFlowableDelegate.run(delegateExecution));

        verify(eventPublisher, times(1)).publishEvent(serviceTaskStartedEventArgumentCaptor.capture());
        ServiceTaskStartedEvent serviceTaskStartedEvent = serviceTaskStartedEventArgumentCaptor.getValue();
        assertEquals(baseRuntimeFlowableDelegate, serviceTaskStartedEvent.getServiceTask());
        assertFalse(serviceTaskStartedEvent.isSuccess());
    }

    @Test
    public void testRun_success() throws Exception {
        when(delegateExecution.getCurrentActivityId()).thenReturn("activityId");

        Callable<String> callable = () -> "result";
        ReflectionTestUtils.setField(baseRuntimeFlowableDelegate, "callable", callable);

        String result = baseRuntimeFlowableDelegate.run(delegateExecution);

        assertEquals("result", result);

        verify(eventPublisher, times(1)).publishEvent(serviceTaskStartedEventArgumentCaptor.capture());
        ServiceTaskStartedEvent serviceTaskStartedEvent = serviceTaskStartedEventArgumentCaptor.getValue();
        assertEquals(baseRuntimeFlowableDelegate, serviceTaskStartedEvent.getServiceTask());
        assertTrue(serviceTaskStartedEvent.isSuccess());
    }

    @Test
    public void testIsDone() {
        baseRuntimeFlowableDelegate.setDone(true);
        assertTrue(baseRuntimeFlowableDelegate.isDone());

        baseRuntimeFlowableDelegate.setDone(false);
        assertFalse(baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet_notDone() throws InterruptedException, ExecutionException {
        assertThrows(InterruptedException.class, () -> baseRuntimeFlowableDelegate.get());
    }

    @Test
    public void testGet_doneWithResult() throws InterruptedException, ExecutionException {
        Callable<String> callable = () -> "result";
        ReflectionTestUtils.setField(baseRuntimeFlowableDelegate, "callable", callable);
        baseRuntimeFlowableDelegate.setDone(true);

        String result = baseRuntimeFlowableDelegate.get();

        assertEquals("result", result);
    }

    @Test
    public void testGet_doneWithoutResult() throws InterruptedException, ExecutionException {
        baseRuntimeFlowableDelegate.setDone(true);

        assertThrows(ExecutionException.class, () -> baseRuntimeFlowableDelegate.get());
    }

    @Test
    public void testGet_timeout() throws InterruptedException, ExecutionException, TimeoutException {
        Callable<String> callable = () -> {
            Thread.sleep(1000);
            return "result";
        };
        ReflectionTestUtils.setField(baseRuntimeFlowableDelegate, "callable", callable);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(callable);

        String result = future.get(500, TimeUnit.MILLISECONDS);

        assertEquals("result", result);
    }

    private class TestRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {

        @Override
        protected Callable<String> initCallable(DelegateExecution execution) {
            return () -> "result";
        }

        @Override
        protected void preHandle(DelegateExecution execution) {
            execution.setVariable("someVariableKey", "someVariableValue");
        }

        @Override
        protected void updateFlowInstanceStatus(@NonNull FlowStatus flowStatus) {
            // No-op
        }

        @Override
        public void bindToFlowTaskInstance(@NonNull FlowTaskInstance taskInstance) {
            // No-op
        }
    }

}