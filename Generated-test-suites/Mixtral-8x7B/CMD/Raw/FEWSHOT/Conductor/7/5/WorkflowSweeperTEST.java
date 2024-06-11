package com.netflix.conductor.core.reconciliation;

import static org.mockito.Mockito.*;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.metadata.tasks.TaskUtils;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel.Status;
import java.time.Instant;
import java.util.Optional;
import java.util.Random;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.StringUtils;

@EnableAsync
public class WorkflowSweeperTest {

@Mock private WorkflowExecutor workflowExecutor;
@Mock private QueueDAO queueDAO;
@Mock private WorkflowRepairService workflowRepairService;
@Mock private ConductorProperties properties;

@InjectMocks private WorkflowSweeper workflowSweeper;

private static final Logger LOGGER =
LoggerFactory.getLogger(WorkflowSweeperTest.class);

@Before
public void init() {
MockitoAnnotations.initMocks(this);

// Initialize the logger to use a mock handler to capture log messages
ReflectionTestUtils.setField(
workflowSweeper, "LOGGER", LoggerFactory.getLogger(WorkflowSweeperTest.class));
}

@Test
public void testSweepAsync() {
// Verify that sweepAsync uses the SWEEPER_EXECUTOR_NAME
verify(workflowSweeper, times(1)).sweepAsync("workflowId");
}

@Test
public void testSweep() {
String workflowId = "workflowId";

// Mock the call to workflowExecutor.decide() to return a WorkflowModel
WorkflowModel workflow = new WorkflowModel();
workflow.setStatus(Status.COMPLETED);
workflow.setWorkflowId(workflowId);

when(workflowExecutor.decide(workflowId))
.thenReturn(workflow);

// Verify that queueDAO.remove() is called with the correct parameters
workflowSweeper.sweep(workflowId);
verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
}

@Test
public void testSweepWithNotFoundException() {
String workflowId = "workflowId";

// Mock the call to workflowExecutor.decide() to throw a NotFoundException
when(workflowExecutor.decide(workflowId))
.thenThrow(new NotFoundException("Workflow not found"));

// Verify that queueDAO.remove() is called with the correct parameters
workflowSweeper.sweep(workflowId);
verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
}

@Test
public void testSweepWithException() {
String workflowId = "workflowId";

// Mock the call to workflowExecutor.decide() to throw an Exception
when(workflowExecutor.decide(workflowId))
.thenThrow(new RuntimeException("Error deciding workflow"));

workflowSweeper.sweep(workflowId);

// Verify that Monitors.error() was called with the correct parameters
verify(Monitors, times(1)).error(WorkflowSweeper.class.getSimpleName(), "sweep");
}

@Test
public void testUnack() {
String workflowId = "workflowId";

// Mock the call to workflowExecutor.decide() to return a WorkflowModel with tasks
WorkflowModel workflow = new WorkflowModel();
workflow.setWorkflowId(workflowId);
workflow.setTasks(
TaskUtils.createTasksList(
new TaskModel(workflowId, TaskType.TASK_TYPE_SCHEDULED, Status.SCHEDULED, 1),
new TaskModel(workflowId, TaskType.TASK_TYPE_IN_PROGRESS, Status.IN_PROGRESS, 2))
);

// Set the poll timeout for the second task to 10 seconds
ReflectionTestUtils.setField(
workflow.getTasks().get(1), "pollTimeoutSeconds", 10);

// Set the timeout for the workflow to 20 seconds
ReflectionTestUtils.setField(workflow, "timeoutSeconds", 20);

// Call unack() with the workflow
workflowSweeper.unack(workflow, 30);

// Verify that queueDAO.setUnackTimeout() is called with the correct parameters
verify(queueDAO, times(1))
.setUnackTimeout(DECIDER_QUEUE, workflowId, 30000);
}

@Test
public void testWorkflowOffsetWithJitter() {
long workflowOffsetTimeout = 45;
long jitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

// Verify that jitter is between -(1/3) and +(2/3) of workflowOffsetTimeout
Assert.assertTrue(
jitter >= -workflowOffsetTimeout / 3 && jitter <= 2 * workflowOffsetTimeout / 3);
}
}