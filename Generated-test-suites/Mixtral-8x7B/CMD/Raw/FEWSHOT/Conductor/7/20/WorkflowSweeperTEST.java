package com.netflix.conductor.core.reconciliation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.Instant;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.util.ReflectionTestUtils;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.events.queue.QueueDAO;
import com.netflix.conductor.core.utils.ExecutionUtils;
import com.netflix.conductor.model.WorkflowStatus;

@EnableAsync
public class WorkflowSweeperTest {

 @Mock
 private WorkflowExecutor workflowExecutor;

 @Mock
 private QueueDAO queueDAO;

 @Mock
 private WorkflowRepairService workflowRepairService;

 @Mock
 private ConductorProperties properties;

 @InjectMocks
 private WorkflowSweeper workflowSweeper = spy(new WorkflowSweeper(null, null, null, null));

 @Before
 public void init() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testSweepAsync() throws Exception {
 // Test that sweepAsync invokes sweep method
 ReflectionTestUtils.setField(workflowSweeper, "taskExecutor", new ThreadPoolTaskExecutor());
 doNothing().when(workflowSweeper).sweep(any());
 workflowSweeper.sweepAsync("workflowId");
 verify(workflowSweeper, times(1)).sweep("workflowId");
 }

 @Test
 public void testSweep() throws Exception {
 // Test sweep method for successful execution
 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setStatus(WorkflowStatus.COMPLETED);
 doReturn(workflowModel).when(workflowExecutor).decide(any());
 doNothing().when(queueDAO).remove(any(), any());

 workflowSweeper.sweep("workflowId");
 verify(queueDAO, times(1)).remove(eq(DECIDER_QUEUE), eq("workflowId"));
 }

 @Test
 public void testSweepNotFoundException() throws Exception {
 // Test sweep method for NotFoundException
 doThrow(new NotFoundException("Workflow not found")).when(workflowExecutor).decide(any());

 workflowSweeper.sweep("workflowId");
 verify(queueDAO, times(1)).remove(eq(DECIDER_QUEUE), eq("workflowId"));
 }

 @Test
 public void testUnackSuccess() throws Exception {
 // Test unack method for successful execution
 TaskModel taskModel = new TaskModel();
 taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setTasks(new TaskModel[] { taskModel });
 ReflectionTestUtils.setField(workflowModel, "workflowDefinition", new Object());
 long postponeDurationSeconds = 60;
 doReturn(postponeDurationSeconds).when(workflowSweeper)
 .workflowOffsetWithJitter(anyLong());
 doNothing().when(queueDAO).setUnackTimeout(any(), any(), anyLong());

 workflowSweeper.unack(workflowModel, 45);

 verify(queueDAO, times(1)).setUnackTimeout(eq(DECIDER_QUEUE), eq("workflowId"),
 eq(postponeDurationSeconds * 1000L));
 }

 @Test
 public void testUnackScheduledTask() throws Exception {
 // Test unack method for scheduled task
 TaskModel taskModel = new TaskModel();
 taskModel.setStatus(TaskModel.Status.SCHEDULED);
 TaskDef taskDef = new TaskDef();
 taskDef.setPollTimeoutSeconds(30);
 taskModel.setTaskDefinition(Optional.of(taskDef));
 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setTasks(new TaskModel[] { taskModel });
 long postponeDurationSeconds = 31;
 doReturn(postponeDurationSeconds).when(workflowSweeper)
 .workflowOffsetWithJitter(anyLong());
 doNothing().when(queueDAO).setUnackTimeout(any(), any(), anyLong());

 workflowSweeper.unack(workflowModel, 45);

 verify(queueDAO, times(1)).setUnackTimeout(eq(DECIDER_QUEUE), eq("workflowId"),
 eq(postponeDurationSeconds * 1000L));
 }

 @Test
 public void testWorkflowOffsetWithJitter() throws Exception {
 // Test workflowOffsetWithJitter method
 long workflowOffsetTimeout = 45;
 long jitter = 10;
 long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

 Assert.assertTrue(result >= (workflowOffsetTimeout - jitter)
 && result <= (workflowOffsetTimeout + jitter));
 }
}