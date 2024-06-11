package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.engine.runtime.Execution;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.stubbing.Answer;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> delegateSpy;
    private DelegateExecution executionMock;
    private FlowInstanceRepository flowInstanceRepositoryMock;
    private FlowableAdaptor flowableAdaptorMock;
    private EventPublisher eventPublisherMock;
    private RetryExecutor retryExecutorMock;

    @Before
    public void setup() {
        executionMock = mock(DelegateExecution.class);
        flowInstanceRepositoryMock = mock(FlowInstanceRepository.class);
        flowableAdaptorMock = mock(FlowableAdaptor.class);
        eventPublisherMock = mock(EventPublisher.class);
        retryExecutorMock = mock(RetryExecutor.class);

        delegateSpy = spy(new TestRuntimeFlowableDelegate());
        doReturn(flowInstanceRepositoryMock).when(delegateSpy).getFlowInstanceRepository();
        doReturn(flowableAdaptorMock).when(delegateSpy).getFlowableAdaptor();
        doReturn(eventPublisherMock).when(delegateSpy).getEventPublisher();
        doReturn(retryExecutorMock).when(delegateSpy).getRetryExecutor();
    }

    @After
    public void teardown() {
        CommandContextUtil.resetCommandContext();
    }

    @Test
    public void testInitTargetTaskInstanceId() throws Exception {
        String processDefinitionId = "testProcess";
        Long flowInstanceId = 1L;
        String activityId = "testActivity";
        Long taskInstanceId = 10L;

        when(executionMock.getProcessDefinitionId()).thenReturn(processDefinitionId);
        when(flowableAdaptorMock.getFlowInstanceIdByProcessDefinitionId(processDefinitionId))
                .thenReturn(Optional.of(flowInstanceId));
        when(flowableAdaptorMock.getTaskInstanceByActivityId(activityId, flowInstanceId))
                .thenReturn(Optional.of(
                        new FlowTaskInstance(taskInstanceId, activityId, FlowStatus.CREATED, null, null,
                                null, null, null, null)));

        delegateSpy.initTargetTaskInstanceId(executionMock);

        assertEquals(delegateSpy.getFlowInstanceId(), flowInstanceId);
        assertEquals(delegateSpy.getTargetTaskInstanceId(), taskInstanceId);
        assertEquals(delegateSpy.getTaskType(), null);
        assertEquals(delegateSpy.getStrategyConfig(), null);

        verify(flowableAdaptorMock, times(1))
                .getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        verify(flowableAdaptorMock, times(1))
                .getTaskInstanceByActivityId(activityId, flowInstanceId);
    }

    @Test
    public void testRunWithException() throws Exception {
        when(executionMock.getCurrentActivityId()).thenReturn("testActivity");
        doThrow(new Exception("Test exception")).when(delegateSpy).preHandle(executionMock);

        delegateSpy.run(executionMock);

        verify(eventPublisherMock, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(eventPublisherMock, times(1)).removeEventListener(any(TaskInstanceCreatedListener.class));
        verify(eventPublisherMock, times(1)).removeEventListener(any(ActiveTaskStatisticsListener.class));
    }

    @Test
    public void testRunWithoutException() throws Exception {
        when(executionMock.getCurrentActivityId()).thenReturn("testActivity");
        when(executionMock.getId()).thenReturn("testExecutionId");

        Callable<String> callable = () -> "Test result";
        doReturn(callable).when(delegateSpy).initCallable(executionMock);

        delegateSpy.run(executionMock);

        verify(eventPublisherMock, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(eventPublisherMock, times(1)).removeEventListener(any(TaskInstanceCreatedListener.class));
        verify(eventPublisherMock, times(1)).removeEventListener(any(ActiveTaskStatisticsListener.class));
    }

    @Test
    public void testExecute() {
        delegateSpy.execute(executionMock);

        verify(eventPublisherMock, times(1)).removeEventListener(any(TaskInstanceCreatedListener.class));
        verify(eventPublisherMock, times(1)).removeEventListener(any(ActiveTaskStatisticsListener.class));
    }

    @Test
    public void testIsDone() {
        assertTrue(delegateSpy.isDone());
    }

    @Test
    public void testGetThrowsInterruptedException() throws Exception {
        when(executionMock.getCurrentActivityId()).thenReturn("testActivity");
        Callable<String> callable = () -> "Test result";
        doReturn(callable).when(delegateSpy).initCallable(executionMock);

        delegateSpy.run(executionMock);

        assertNull(delegateSpy.get(0, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testGetThrowsExecutionException() throws Exception {
        when(executionMock.getCurrentActivityId()).thenReturn("testActivity");
        doThrow(new Exception("Test exception")).when(delegateSpy).preHandle(executionMock);

        delegateSpy.run(executionMock);

        try {
            delegateSpy.get();
        } catch (ExecutionException e) {
            assertNotNull(e.getCause());
            assertEquals(e.getCause().getMessage(), "Test exception");
        }
    }

    @Test
    public void testGetNoException() throws Exception {
        when(executionMock.getCurrentActivityId()).thenReturn("testActivity");
        Callable<String> callable = () -> "Test result";
        doReturn(callable).when(delegateSpy).initCallable(executionMock);

        delegateSpy.run(executionMock);

        assertEquals(delegateSpy.get(), "Test result");
    }

    @Test
    public void testBindToFlowTaskInstance() {
        Long taskInstanceId = 10L;
        FlowTaskInstance taskInstance = new FlowTaskInstance(taskInstanceId, "testActivity", FlowStatus.CREATED,
                null, null, null, null, null, null);

        delegateSpy.bindToFlowTaskInstance(taskInstance);

        assertEquals(taskInstance.getServiceTask(), delegateSpy);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        delegateSpy.updateFlowInstanceStatus(FlowStatus.RUNNING);

        verify(flowInstanceRepositoryMock, times(1)).updateStatusById(anyLong(), eq(FlowStatus.RUNNING));
    }

    @Test
    public void testInitCallable() throws Exception {
        when(executionMock.getCurrentActivityId()).thenReturn("testActivity");

        Callable<String> callable = delegateSpy.initCallable(executionMock);

        assertNotNull(callable);
    }

    private class TestRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {

        @Override
        protected Callable<String> initCallable(DelegateExecution execution) {
            return () -> "Test result";
        }

        @Override
        protected void preHandle(DelegateExecution execution) {}

        @Override
        public FlowInstanceRepository getFlowInstanceRepository() {
            return flowInstanceRepositoryMock;
        }

        @Override
        public FlowableAdaptor getFlowableAdaptor() {
            return flowableAdaptorMock;
        }

        @Override
        public EventPublisher getEventPublisher() {
            return eventPublisherMock;
        }

        @Override
        public RetryExecutor getRetryExecutor() {
            return retryExecutorMock;
        }
    }
}