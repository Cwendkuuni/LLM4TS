package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.ExecutionStrategyConfig;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableService;
import com.oceanbase.odc.service.flow.FlowableTaskService;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution delegateExecution;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private FlowableService flowableService;

    @Mock
    private FlowableTaskService flowableTaskService;

    @Mock
    private Execution execution;

    @Mock
    private RetryExecutor retryExecutor;

    @Mock
    private Callable<Integer> callable;

    private BaseRuntimeFlowableDelegate<Integer> baseRuntimeFlowableDelegate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // Spy on the delegate to track calls to methods
        baseRuntimeFlowableDelegate = spy(new BaseRuntimeFlowableDelegate<Integer>() {
            @Override
            protected Callable<Integer> initCallable(DelegateExecution execution) {
                return callable;
            }

            @Override
            protected void preHandle(DelegateExecution execution) {
                // Do nothing
            }
        });

        // Set up mock dependencies
        baseRuntimeFlowableDelegate.flowableAdaptor = flowableAdaptor;
        baseRuntimeFlowableDelegate.eventPublisher = eventPublisher;
        baseRuntimeFlowableDelegate.flowInstanceRepository = flowInstanceRepository;
        baseRuntimeFlowableDelegate.retryExecutor = retryExecutor;

        // Set up behavior of mocks
        when(delegateExecution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        when(delegateExecution.getCurrentActivityId()).thenReturn("activityId");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenReturn(Optional.of(Optional.of(1L)));
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
                .thenReturn(Optional.of(createFlowTaskInstance()));
        when(flowableTaskService.getExecutionById(1L)).thenReturn(execution);
        when(execution.getProcessInstanceId()).thenReturn("processInstanceId");
        when(execution.getId()).thenReturn("executionId");
        when(retryExecutor.run(any(), any())).thenAnswer(invocation -> {
            Callable<Object> callable = invocation.getArgument(0);
            return Optional.of(callable.call());
        });

        // Create a TaskExecutor mock that can be used to schedule tasks
        ExecutorService taskExecutor = Executors.newSingleThreadExecutor();
        doAnswer(invocation -> {
            Runnable command = invocation.getArgument(0);
            taskExecutor.execute(command);
            return null;
        }).when(baseRuntimeFlowableDelegate).submitToTaskExecutor(any(), any());
    }

    @Test
    public void testInitTargetTaskInstanceId() throws Exception {
        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

        ArgumentCaptor<Callable<Object>> callableCaptor = ArgumentCaptor.forClass(Callable.class);
        verify(retryExecutor, times(1)).run(callableCaptor.capture(), any());

        Callable<Object> capturedCallable = callableCaptor.getValue();
        assertTrue(capturedCallable instanceof Callable);

        Object result = capturedCallable.call();
        assertEquals(1L, result);

        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(anyString());
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(anyString(), anyLong());
    }

    @Test
    public void testPreHandle() throws Exception {
        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);
        baseRuntimeFlowableDelegate.preHandle(delegateExecution);

        verify(delegateExecution, times(1)).setVariable(anyString(), any());
    }

    @Test
    public void testRunWithoutException() throws Exception {
        doNothing().when(baseRuntimeFlowableDelegate).preHandle(delegateExecution);
        when(callable.call()).thenReturn(42);

        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);
        Integer result = baseRuntimeFlowableDelegate.call();

        assertEquals(Integer.valueOf(42), result);

        verify(delegateExecution, times(1)).setVariable(anyString(), any());
        verify(flowableTaskService, times(1)).completeTask(anyLong());
    }

    @Test
    public void testRunWithException() throws Exception {
        doThrow(new RuntimeException("Test exception")).when(baseRuntimeFlowableDelegate).preHandle(delegateExecution);

        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

        try {
            baseRuntimeFlowableDelegate.call();
            assertTrue(false);
        } catch (Exception e) {
            verify(delegateExecution, never()).setVariable(anyString(), any());
            verify(flowableTaskService, never()).completeTask(anyLong());
            assertTrue(e instanceof RuntimeException);
            assertEquals("Test exception", e.getMessage());
        }
    }

    @Test
    public void testIsDone() throws Exception {
        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);
        assertTrue(baseRuntimeFlowableDelegate.isDone());

        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);
        assertFalse(baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet() throws Exception {
        doNothing().when(baseRuntimeFlowableDelegate).preHandle(delegateExecution);
        when(callable.call()).thenReturn(42);

        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);
        Integer result = baseRuntimeFlowableDelegate.get();

        assertEquals(Integer.valueOf(42), result);
    }

    @Test
    public void testGetWithInterruptedException() throws Exception {
        doNothing().when(baseRuntimeFlowableDelegate).preHandle(delegateExecution);
        when(callable.call()).thenThrow(new InterruptedException("Test interrupted exception"));

        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

        try {
            baseRuntimeFlowableDelegate.get();
            assertTrue(false);
        } catch (InterruptedException e) {
            verify(delegateExecution, never()).setVariable(anyString(), any());
            verify(flowableTaskService, never()).completeTask(anyLong());
            assertEquals("Test interrupted exception", e.getMessage());
        }
    }

    @Test
    public void testGetWithExecutionException() throws Exception {
        doThrow(new RuntimeException("Test exception")).when(baseRuntimeFlowableDelegate).preHandle(delegateExecution);

        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

        try {
            baseRuntimeFlowableDelegate.get();
            assertTrue(false);
        } catch (ExecutionException e) {
            verify(delegateExecution, never()).setVariable(anyString(), any());
            verify(flowableTaskService, never()).completeTask(anyLong());
            assertTrue(e.getCause() instanceof RuntimeException);
            assertEquals("Test exception", e.getCause().getMessage());
        }
    }

    @Test
    public void testGetWithTimeoutException() throws Exception {
        doNothing().when(baseRuntimeFlowableDelegate).preHandle(delegateExecution);
        when(callable.call()).thenThrow(new RuntimeException("Test exception"));

        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

        try {
            baseRuntimeFlowableDelegate.get(1, TimeUnit.MICROSECONDS);
            assertTrue(false);
        } catch (TimeoutException e) {
            verify(delegateExecution, never()).setVariable(anyString(), any());
            verify(flowableTaskService, never()).completeTask(anyLong());
            assertEquals("Could not execute task successfully within the given timeout of 1 MICROSECONDS",
                    e.getMessage());
        }
    }

    @Test
    public void testSubmitToTaskExecutor() throws Exception {
        Runnable runnable = Mockito.mock(Runnable.class);
        TaskExecutor taskExecutor = Mockito.mock(TaskExecutor.class);

        baseRuntimeFlowableDelegate.submitToTaskExecutor(runnable, taskExecutor);

        verify(taskExecutor, times(1)).execute(runnable);
    }

    private FlowTaskInstance createFlowTaskInstance() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        flowTaskInstance.setId(1L);
        flowTaskInstance.setTaskType(TaskType.NORMAL);
        flowTaskInstance.setStrategyConfig(new ExecutionStrategyConfig());
        return flowTaskInstance;
    }
}