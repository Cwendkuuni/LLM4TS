import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.test.PluggableFlowableTestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BaseRuntimeFlowableDelegateTest extends PluggableFlowableTestCase {

    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;
    private DelegateExecution delegateExecution;
    private FlowableAdaptor flowableAdaptor;
    private EventPublisher eventPublisher;
    private FlowInstanceRepository flowInstanceRepository;
    private RetryExecutor retryExecutor;

    @Before
    public void setUp() {
        baseRuntimeFlowableDelegate = mock(BaseRuntimeFlowableDelegate.class);
        delegateExecution = mock(DelegateExecution.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        eventPublisher = mock(EventPublisher.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);
        retryExecutor = mock(RetryExecutor.class);

        when(baseRuntimeFlowableDelegate.getFlowableAdaptor()).thenReturn(flowableAdaptor);
        when(baseRuntimeFlowableDelegate.getEventPublisher()).thenReturn(eventPublisher);
        when(baseRuntimeFlowableDelegate.getFlowInstanceRepository()).thenReturn(flowInstanceRepository);
        when(baseRuntimeFlowableDelegate.getRetryExecutor()).thenReturn(retryExecutor);

        when(delegateExecution.getCurrentActivityId()).thenReturn("testActivityId");
        when(delegateExecution.getProcessDefinitionId()).thenReturn("testProcessDefinitionId");

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenReturn(Optional.of(1L));

        ArgumentCaptor<TaskInstanceCreatedListener> taskInstanceCreatedListenerCaptor =
                ArgumentCaptor.forClass(TaskInstanceCreatedListener.class);

        when(eventPublisher.addEventListener(any(TaskInstanceCreatedListener.class)))
                .thenAnswer(invocation -> {
                    TaskInstanceCreatedListener listener = invocation.getArgument(0);
                    listener.onTaskInstanceCreated(null);
                    return null;
                });

        when(eventPublisher.getListeners(TaskInstanceCreatedListener.class)).thenReturn(
                taskInstanceCreatedListenerCaptor.capture());

        ArgumentCaptor<ActiveTaskStatisticsListener> activeTaskStatisticsListenerCaptor =
                ArgumentCaptor.forClass(ActiveTaskStatisticsListener.class);

        when(eventPublisher.addEventListener(any(ActiveTaskStatisticsListener.class)))
                .thenAnswer(invocation -> {
                    ActiveTaskStatisticsListener listener = invocation.getArgument(0);
                    listener.onActiveTaskStatistics(null);
                    return null;
                });

        when(eventPublisher.getListeners(ActiveTaskStatisticsListener.class)).thenReturn(
                activeTaskStatisticsListenerCaptor.capture());

        baseRuntimeFlowableDelegate.init(eventPublisher);
    }

    @Test
    public void testInitTargetTaskInstanceId_Success() {
        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

        verify(flowableAdaptor, times(1))
                .getFlowInstanceIdByProcessDefinitionId(delegateExecution.getProcessDefinitionId());
        verify(flowableAdaptor, times(1))
                .getTaskInstanceByActivityId(delegateExecution.getCurrentActivityId(), 1L);

        assertEquals(1L, baseRuntimeFlowableDelegate.getTargetTaskInstanceId());
        assertEquals("testActivityId", baseRuntimeFlowableDelegate.getActivityId());
        assertEquals(TaskType.SERVICE_TASK, baseRuntimeFlowableDelegate.getTaskType());
        assertNotNull(baseRuntimeFlowableDelegate.getStrategyConfig());
    }

    @Test
    public void testInitTargetTaskInstanceId_Exception() {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(ServiceTaskError.class, () ->
                baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution));

        verify(flowableAdaptor, times(1))
                .getFlowInstanceIdByProcessDefinitionId(delegateExecution.getProcessDefinitionId());
        verify(flowableAdaptor, never())
                .getTaskInstanceByActivityId(delegateExecution.getCurrentActivityId(), anyLong());

        assertFalse(baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet_Success() throws InterruptedException, ExecutionException, TimeoutException {
        when(baseRuntimeFlowableDelegate.call()).thenReturn("testResult");

        Future<String> future = baseRuntimeFlowableDelegate;

        String result = future.get();

        assertEquals("testResult", result);
        assertTrue(future.isDone());
    }

    @Test
    public void testGet_Timeout() throws InterruptedException, ExecutionException {
        when(baseRuntimeFlowableDelegate.call()).thenReturn("testResult");

        Future<String> future = baseRuntimeFlowableDelegate;

        assertThrows(TimeoutException.class, () -> future.get(1, TimeUnit.MILLISECONDS));
        assertTrue(future.isDone());
    }

    @Test
    public void testGet_Exception() throws InterruptedException {
        when(baseRuntimeFlowableDelegate.call()).thenThrow(new RuntimeException("testException"));

        Future<String> future = baseRuntimeFlowableDelegate;

        assertThrows(ExecutionException.class, () -> future.get());
        assertTrue(future.isDone());
    }

    @Test
    public void testPreHandle() throws Exception {
        ResettableDelegateExecution resettableDelegateExecution = new ResettableDelegateExecution(delegateExecution);

        baseRuntimeFlowableDelegate.preHandle(resettableDelegateExecution);

        reset(delegateExecution);
        resettableDelegateExecution.reset();

        verify(delegateExecution, times(1)).setVariable(anyString(), any());
    }

    @Test
    public void testRun_InitializationException() throws Exception {
        ResettableDelegateExecution resettableDelegateExecution = new ResettableDelegateExecution(delegateExecution);

        when(delegateExecution.getCurrentActivityId()).thenThrow(new RuntimeException("testException"));

        baseRuntimeFlowableDelegate.run(resettableDelegateExecution);

        verify(flowableAdaptor, never())
                .getFlowInstanceIdByProcessDefinitionId(delegateExecution.getProcessDefinitionId());
        verify(flowableAdaptor, never())
                .getTaskInstanceByActivityId(delegateExecution.getCurrentActivityId(), 1L);

        verify(eventPublisher, times(1))
                .publishEvent(any(ServiceTaskStartedEvent.class));
        verify(eventPublisher, times(1))
                .removeEventListener(any(TaskInstanceCreatedListener.class));
        verify(eventPublisher, times(1))
                .removeEventListener(any(ActiveTaskStatisticsListener.class));

        assertTrue(baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testRun_ExecutionException() throws Exception {
        ResettableDelegateExecution resettableDelegateExecution = new ResettableDelegateExecution(delegateExecution);

        when(baseRuntimeFlowableDelegate.initCallable(any(DelegateExecution.class)))
                .thenThrow(new RuntimeException("testException"));

        assertThrows(RuntimeException.class, () -> baseRuntimeFlowableDelegate.run(resettableDelegateExecution));

        verify(flowableAdaptor, times(1))
                .getFlowInstanceIdByProcessDefinitionId(delegateExecution.getProcessDefinitionId());
        verify(flowableAdaptor, times(1))
                .getTaskInstanceByActivityId(delegateExecution.getCurrentActivityId(), 1L);

        verify(eventPublisher, times(1))
                .publishEvent(any(ServiceTaskStartedEvent.class));
        verify(eventPublisher, times(1))
                .removeEventListener(any(TaskInstanceCreatedListener.class));
        verify(eventPublisher, times(1))
                .removeEventListener(any(ActiveTaskStatisticsListener.class));

        assertTrue(baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testExecute() throws Exception {
        ResettableDelegateExecution resettableDelegateExecution = new ResettableDelegateExecution(delegateExecution);

        baseRuntimeFlowableDelegate.execute(resettableDelegateExecution);

        reset(delegateExecution);
        resettableDelegateExecution.reset();

        verify(flowableAdaptor, times(1))
                .getFlowInstanceIdByProcessDefinitionId(delegateExecution.getProcessDefinitionId());
        verify(flowableAdaptor, times(1))
                .getTaskInstanceByActivityId(delegateExecution.getCurrentActivityId(), 1L);

        verify(eventPublisher, times(1))
                .publishEvent(any(ServiceTaskStartedEvent.class));
        verify(eventPublisher, times(1))
                .removeEventListener(any(TaskInstanceCreatedListener.class));
        verify(eventPublisher, times(1))
                .removeEventListener(any(ActiveTaskStatisticsListener.class));

        assertTrue(baseRuntimeFlowableDelegate.isDone());
    }

    private static class ResettableDelegateExecution extends DelegateExecution {

        private DelegateExecution delegateExecution;

        private ResettableDelegateExecution(DelegateExecution delegateExecution) {
            this.delegateExecution = delegateExecution;
        }

        void reset() {
            delegateExecution = mock(DelegateExecution.class);
            when(delegateExecution.getCurrentActivityId()).thenReturn("testActivityId");
            when(delegateExecution.getProcessDefinitionId()).thenReturn("testProcessDefinitionId");
        }

        @Override
        public boolean equals(Object o) {
            return delegateExecution.equals(o);
        }

        @Override
        public int hashCode() {
            return delegateExecution.hashCode();
        }

        @Override
        public <T> T getVariable(String variableName) {
            return delegateExecution.getVariable(variableName);
        }

        @Override
        public <T> T getVariable(String variableName, Class<T> type) {
            return delegateExecution.getVariable(variableName, type);
        }

        @Override
        public void setVariable(String variableName, Object variableValue) {
            delegateExecution.setVariable(variableName, variableValue);
        }

        @Override
        public <T> void setVariable(String variableName, T variableValue) {
            delegateExecution.setVariable(variableName, variableValue);
        }

        @Override
        public String getId() {
            return delegateExecution.getId();
        }

        @Override
        public String getProcessDefinitionId() {
            return delegateExecution.getProcessDefinitionId();
        }

        @Override
        public String getCurrentActivityId() {
            return delegateExecution.getCurrentActivityId();
        }

        @Override
        public String getProcessInstanceId() {
            return delegateExecution.getProcessInstanceId();
        }

        @Override
        public Object getEngineServices() {
            return delegateExecution.getEngineServices();
        }

        @Override
        public Object getProcessEngineConfiguration() {
            return delegateExecution.getProcessEngineConfiguration();
        }
    }

}