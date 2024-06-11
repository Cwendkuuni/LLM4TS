package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.test.PluggableFlowableTestCase;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.task.Task;
import org.flowable.task.service.delegate.DelegateTask;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

@PluggableFlowableTestCase
public class BaseRuntimeFlowableDelegateTest {

	@Mock
	private DelegateExecution delegateExecution;

	@Mock
	private DelegateTask delegateTask;

	@Mock
	private EventPublisher eventPublisher;

	@Mock
	private FlowableAdaptor flowableAdaptor;

	@Mock
	private FlowInstanceRepository flowInstanceRepository;

	private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		when(delegateExecution.getId()).thenReturn("executionId");
		when(delegateExecution.getProcessDefinitionId()).thenReturn("processDefinitionId");
		when(delegateExecution.getCurrentActivityId()).thenReturn("activityId");

		when(delegateTask.getExecution()).thenReturn(delegateExecution);
		when(delegateTask.getTaskDefinitionKey()).thenReturn("taskDefinitionKey");

		baseRuntimeFlowableDelegate = spy(new TestRuntimeFlowableDelegate());
		baseRuntimeFlowableDelegate.setEventPublisher(eventPublisher);
		baseRuntimeFlowableDelegate.setFlowableAdaptor(flowableAdaptor);
		baseRuntimeFlowableDelegate.setFlowInstanceRepository(flowInstanceRepository);
	}

	@Test
	public void testInitTargetTaskInstanceId() {
		ExecutionEntity executionEntity = new ExecutionEntity();
		executionEntity.setId("executionId");
		when(delegateExecution.getRuntimeExecution()).thenReturn(executionEntity);

		when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
				.thenReturn(Optional.of(Optional.of(1L)));

		when(flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L)).thenReturn(
				Optional.of(new FlowTaskInstance(1L, "taskDefinitionKey", 1L, null, null, null, null, null, null, null)));

		baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

		assertEquals(1L, baseRuntimeFlowableDelegate.getTargetTaskInstanceId().longValue());
		assertEquals(TaskType.SERVICE, baseRuntimeFlowableDelegate.getTaskType());
	}

	@Test(expected = ServiceTaskError.class)
	public void testInitTargetTaskInstanceId_ServiceTaskError() {
		ExecutionEntity executionEntity = new ExecutionEntity();
		executionEntity.setId("executionId");
		when(delegateExecution.getRuntimeExecution()).thenReturn(executionEntity);

		when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
				.thenReturn(Optional.empty());

		baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);
	}

	@Test
	public void testRun_preHandleThrowsException() throws Exception {
		when(delegateExecution.getRuntimeExecution()).thenThrow(new Exception("Exception in delegateExecution.getRuntimeExecution()"));

		baseRuntimeFlowableDelegate.run(delegateExecution);

		verify(delegateExecution, times(1)).getRuntimeExecution();
		verify(delegateTask, never()).setAssignee("assignee");
	}

	@Test
	public void testRun_callableThrowsException() throws Exception {
		Callable<String> callable = () -> {
			throw new RuntimeException("Exception in callable");
		};

		when(delegateExecution.getRuntimeExecution()).thenReturn(mock(ExecutionEntity.class));
		doThrow(new RuntimeException("Exception in callable")).when(baseRuntimeFlowableDelegate).callable(delegateExecution);

		baseRuntimeFlowableDelegate.run(delegateExecution);

		verify(delegateExecution, times(1)).getRuntimeExecution();
		verify(delegateTask, never()).setAssignee("assignee");
	}

	@Test
	public void testCallable() throws Exception {
		Callable<String> callable = () -> "result";

		when(delegateExecution.getRuntimeExecution()).thenReturn(mock(ExecutionEntity.class));
		String result = baseRuntimeFlowableDelegate.callable(delegateExecution);

		assertEquals("result", result);
		verify(delegateExecution, times(1)).getRuntimeExecution();
	}

	@Test
	public void testRun_setAssignee() throws Exception {
		when(delegateExecution.getRuntimeExecution()).thenReturn(mock(ExecutionEntity.class));
		doNothing().when(baseRuntimeFlowableDelegate).preHandle(delegateExecution);
		doReturn(callable(delegateExecution)).when(baseRuntimeFlowableDelegate).callable(delegateExecution);

		baseRuntimeFlowableDelegate.run(delegateExecution);

		verify(delegateTask, times(1)).setAssignee("assignee");
	}

	private Callable<String> callable(DelegateExecution delegateExecution) {
		return () -> "result";
	}

	@Test
	public void testIsDone() throws Exception {
		assertFalse(baseRuntimeFlowableDelegate.isDone());

		baseRuntimeFlowableDelegate.setDone(true);

		assertTrue(baseRuntimeFlowableDelegate.isDone());
	}

	@Test
	public void testGet_normal() throws Exception {
		when(delegateExecution.getRuntimeExecution()).thenReturn(mock(ExecutionEntity.class));
		doNothing().when(baseRuntimeFlowableDelegate).preHandle(delegateExecution);
		doReturn(callable(delegateExecution)).when(baseRuntimeFlowableDelegate).callable(delegateExecution);

		String result = baseRuntimeFlowableDelegate.get();

		assertEquals("result", result);
	}

	@Test(expected = InterruptedException.class)
	public void testGet_interrupted() throws Exception {
		when(delegateExecution.getRuntimeExecution()).thenReturn(mock(ExecutionEntity.class));
		doNothing().when(baseRuntimeFlowableDelegate).preHandle(delegateExecution);
		doReturn(callable(delegateExecution)).when(baseRuntimeFlowableDelegate).callable(delegateExecution);

		Thread.currentThread().interrupt();

		baseRuntimeFlowableDelegate.get();
	}

	@Test(expected = ExecutionException.class)
	public void testGet_executionException() throws Exception {
		when(delegateExecution.getRuntimeExecution()).thenReturn(mock(ExecutionEntity.class));
		doThrow(new RuntimeException("Exception in callable")).when(baseRuntimeFlowableDelegate).callable(delegateExecution);

		baseRuntimeFlowableDelegate.get();
	}

	@Test(expected = ExecutionException.class)
	public void testGet_executionException_interrupted() throws Exception {
		when(delegateExecution.getRuntimeExecution()).thenReturn(mock(ExecutionEntity.class));
		doThrow(new RuntimeException("Exception in callable")).when(baseRuntimeFlowableDelegate).callable(delegateExecution);

		Thread.currentThread().interrupt();

		baseRuntimeFlowableDelegate.get();
	}

	@Test
	public void testGet_timeout() throws Exception {
		ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		ScheduledFuture<?> future = scheduledExecutorService.schedule(() -> {
		}, 1, TimeUnit.SECONDS);

		when(delegateExecution.getRuntimeExecution()).thenReturn(mock(ExecutionEntity.class));
		doNothing().when(baseRuntimeFlowableDelegate).preHandle(delegateExecution);
		doReturn(callable(delegateExecution)).when(baseRuntimeFlowableDelegate).callable(delegateExecution);

		try {
			baseRuntimeFlowableDelegate.get(100, TimeUnit.MILLISECONDS);
		} finally {
			future.cancel(true);
			scheduledExecutorService.shutdownNow();
		}
	}

	private class TestRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {

		@Override
		protected Callable<String> initCallable(DelegateExecution execution) {
			return callable(execution);
		}

		@Override
		protected void preHandle(DelegateExecution execution) {
			delegateTask.setAssignee("assignee");
		}

	}

	private class TestExecutionListener implements ExecutionListener {

		@Override
		public void notify(DelegateExecution execution) {
			if (execution.getEventName().equals(Event.EVENTNAME_END)) {
				updateFlowInstanceStatus(FlowStatus.COMPLETED);
			}
		}

	}

}