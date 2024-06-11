package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

@SuppressWarnings("rawtypes")
public class BaseRuntimeFlowableDelegateTest implements ApplicationEventPublisherAware {

    private BaseRuntimeFlowableDelegate baseRuntimeFlowableDelegate;

    private DelegateExecution delegateExecution;

    private FlowableAdaptor flowableAdaptor;

    private EventPublisher eventPublisher;

    private FlowInstanceRepository flowInstanceRepository;

    private ApplicationEventPublisher applicationEventPublisher;

    @Before
    public void setup() {
        delegateExecution = mock(DelegateExecution.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        eventPublisher = mock(EventPublisher.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);

        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate() {

            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test Result";
            }

            @Override
            protected void preHandle(DelegateExecution execution) {
                // Do nothing
            }
        };
        baseRuntimeFlowableDelegate.setFlowableAdaptor(flowableAdaptor);
        baseRuntimeFlowableDelegate.setEventPublisher(eventPublisher);
        baseRuntimeFlowableDelegate.setFlowInstanceRepository(flowInstanceRepository);
        baseRuntimeFlowableDelegate.setApplicationEventPublisher(applicationEventPublisher);
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        String processDefinitionId = "Test Process";
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId)).thenReturn(1L);

        String activityId = "Test Activity";
        Execution execution = mock(Execution.class);
        when(delegateExecution.getCurrentActivityId()).thenReturn(activityId);
        when(delegateExecution.getProcessDefinitionId()).thenReturn(processDefinitionId);
        when(delegateExecution.getExecution()).thenReturn(execution);

        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(anyString(), longArgumentCaptor.capture());
        Long flowInstanceId = longArgumentCaptor.getValue();
        assertEquals(1L, flowInstanceId.longValue());

        assertEquals("Test Activity", baseRuntimeFlowableDelegate.getActivityId());
        assertEquals(1L, baseRuntimeFlowableDelegate.getTargetTaskInstanceId().longValue());
        assertEquals(TaskType.SERVICE_TASK, baseRuntimeFlowableDelegate.getTaskType());
        assertEquals(null, baseRuntimeFlowableDelegate.getFlowInstanceId());
        assertEquals(null, baseRuntimeFlowableDelegate.getStrategyConfig());
    }

    @Test
    public void testExecuteWithException() {
        when(delegateExecution.getCurrentActivityId()).thenThrow(new RuntimeException("Test Exception"));

        baseRuntimeFlowableDelegate.execute(delegateExecution);

        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(eventPublisher, times(1)).removeEventListener(any(TaskInstanceCreatedListener.class));
        verify(eventPublisher, times(1)).removeEventListener(any(ActiveTaskStatisticsListener.class));
        verify(flowableAdaptor, never()).getFlowInstanceIdByProcessDefinitionId(anyString());
        verify(delegateExecution, times(1)).getExecution();
        verify(delegateExecution, times(1)).getProcessDefinitionId();
        verify(delegateExecution, times(1)).getCurrentActivityId();
    }

    @Test
    public void testIsDone() {
        assertTrue(baseRuntimeFlowableDelegate.isDone());

        baseRuntimeFlowableDelegate.setDone(false);
        assertFalse(baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        when(delegateExecution.getCurrentActivityId()).thenReturn("Test Activity");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(getFlowTaskInstance());

        String result = baseRuntimeFlowableDelegate.get();

        assertEquals("Test Result", result);

        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(anyString());
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(anyString(), anyLong());
        verify(delegateExecution, times(1)).getExecution();
        verify(delegateExecution, times(1)).getProcessDefinitionId();
        verify(delegateExecution, times(1)).getCurrentActivityId();
        verify(flowInstanceRepository, times(1)).updateStatusById(anyLong(), any(FlowStatus.class));
    }

    @Test
    public void testGetWithTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        when(delegateExecution.getCurrentActivityId()).thenReturn("Test Activity");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(getFlowTaskInstance());

        baseRuntimeFlowableDelegate.get(0, TimeUnit.MILLISECONDS);

        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(anyString());
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(anyString(), anyLong());
        verify(delegateExecution, times(1)).getExecution();
        verify(delegateExecution, times(1)).getProcessDefinitionId();
        verify(delegateExecution, times(1)).getCurrentActivityId();
        verify(flowInstanceRepository, never()).updateStatusById(anyLong(), any(FlowStatus.class));
    }

    @Test(expected = ServiceTaskError.class)
    public void testExceptionInInitCallable() throws Exception {
        when(delegateExecution.getCurrentActivityId()).thenReturn("Test Activity");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(getFlowTaskInstance());

        BaseRuntimeFlowableDelegate testDelegate = Mockito.spy(
                new BaseRuntimeFlowableDelegate() {

                    @Override
                    protected Callable<String> initCallable(DelegateExecution execution) {
                        throw new RuntimeException("Test Exception");
                    }

                    @Override
                    protected void preHandle(DelegateExecution execution) {
                        // Do nothing
                    }
                });

        testDelegate.setFlowableAdaptor(flowableAdaptor);
        testDelegate.setEventPublisher(eventPublisher);
        testDelegate.setFlowInstanceRepository(flowInstanceRepository);
        testDelegate.setApplicationEventPublisher(applicationEventPublisher);

        testDelegate.get();
    }

    private FlowTaskInstance getFlowTaskInstance() {
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);
        when(flowTaskInstance.getId()).thenReturn(1L);
        when(flowTaskInstance.getTaskType()).thenReturn(TaskType.SERVICE_TASK);
        when(flowTaskInstance.getStrategyConfig()).thenReturn(new ExecutionStrategyConfig());
        return flowTaskInstance;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}