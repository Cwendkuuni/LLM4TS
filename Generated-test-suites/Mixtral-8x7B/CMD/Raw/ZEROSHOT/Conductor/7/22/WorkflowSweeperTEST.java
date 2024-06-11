package com.netflix.conductor.core.reconciliation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.AsyncResult;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.run.Workflow;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.QueueTO;
import com.netflix.conductor.model.QueueTO.QueueType;

public class WorkflowSweeperTest {

	private static final String WORKFLOW_ID = "workflow-123";
	private static final String WORKFLOW_NAME = "test-workflow";
	private static final String DECIDER_QUEUE = "decider";
	private static final long WORKFLOW_OFFSET_TIMEOUT = 45;
	private static final long JITTER = 15;
	private static final long POSTPONE_DURATION_SECONDS = 61;

	@Mock
	private WorkflowExecutor workflowExecutor;

	@Mock
	private ConductorProperties properties;

	@Mock
	private QueueDAO queueDAO;

	@Mock
	private WorkflowRepairService workflowRepairService;

	private WorkflowSweeper workflowSweeper;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		when(properties.getWorkflowOffsetTimeout()).thenReturn(Optional.of(WORKFLOW_OFFSET_TIMEOUT));

		workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
		workflowSweeper = spy(workflowSweeper);

		doNothing().when(workflowSweeper).unack(any(WorkflowModel.class), anyLong());
	}

	@Test
	public void testSweepAsync() {
		AsyncResult<Void> asyncResult = new AsyncResult<>(null);
		when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(createWorkflowModel());
		when(workflowSweeper.sweepAsync(WORKFLOW_ID)).thenReturn(asyncResult);

		workflowSweeper.sweepAsync(WORKFLOW_ID);

		verify(workflowSweeper, times(1)).sweep(WORKFLOW_ID);
	}

	@Test
	public void testSweep() {
		ArgumentCaptor<QueueTO> argCaptor = ArgumentCaptor.forClass(QueueTO.class);

		when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(createWorkflowModel());
		when(queueDAO.remove(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(true);

		workflowSweeper.sweep(WORKFLOW_ID);

		verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
		verify(workflowSweeper, times(1)).unack(any(WorkflowModel.class), anyLong());
		verify(queueDAO, times(1)).setUnackTimeout(argCaptor.capture(), anyString(), anyLong());

		QueueTO queueTO = argCaptor.getValue();
		assert(queueTO.getQueueType() == QueueType.DECIDER);
		assert(queueTO.getQueueName().equals(DECIDER_QUEUE));
		assert(queueTO.getWorkflowId().equals(WORKFLOW_ID));
		assert(queueTO.getUnackTimeout() == POSTPONE_DURATION_SECONDS * 1000);
	}

	@Test
	public void testUnack() {
		ArgumentCaptor<QueueTO> argCaptor = ArgumentCaptor.forClass(QueueTO.class);
		WorkflowModel workflowModel = createWorkflowModel();

		workflowSweeper.unack(workflowModel, POSTPONE_DURATION_SECONDS);

		verify(queueDAO, times(1)).setUnackTimeout(argCaptor.capture(), anyString(), anyLong());

		QueueTO queueTO = argCaptor.getValue();
		assert(queueTO.getQueueType() == QueueType.DECIDER);
		assert(queueTO.getQueueName().equals(DECIDER_QUEUE));
		assert(queueTO.getWorkflowId().equals(WORKFLOW_ID));
		assert(queueTO.getUnackTimeout() == POSTPONE_DURATION_SECONDS * 1000);
	}

	@Test
	public void testWorkflowOffsetWithJitter() {
		assert(workflowSweeper.workflowOffsetWithJitter(WORKFLOW_OFFSET_TIMEOUT) >= (WORKFLOW_OFFSET_TIMEOUT - JITTER)
				&& workflowSweeper.workflowOffsetWithJitter(WORKFLOW_OFFSET_TIMEOUT) <= (WORKFLOW_OFFSET_TIMEOUT + JITTER));
	}

	private WorkflowModel createWorkflowModel() {
		WorkflowDef workflowDef = new WorkflowDef();
		workflowDef.setTimeoutSeconds(60);
		WorkflowModel workflowModel = new WorkflowModel();
		workflowModel.setWorkflowDefinition(workflowDef);
		workflowModel.setWorkflowId(WORKFLOW_ID);
		workflowModel.setWorkflowName(WORKFLOW_NAME);
		workflowModel.setStatus(TaskModel.Status.IN_PROGRESS);
		workflowModel.setCreatedTime(Instant.now().toEpochMilli());
		TaskModel taskModel = new TaskModel();
		taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
		taskModel.setTaskType(TaskType.TASK_TYPE_TASK);
		taskModel.setResponseTimeoutSeconds(60);
		workflowModel.addTask(taskModel);
		return workflowModel;
	}
}