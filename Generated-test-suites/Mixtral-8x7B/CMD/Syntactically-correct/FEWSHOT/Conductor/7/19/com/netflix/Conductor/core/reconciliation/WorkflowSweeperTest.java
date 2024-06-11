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
import org.springframework.scheduling.annotation.Async;
import org.springframework.test.util.ReflectionTestUtils;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.model.QueueEvent;

public class WorkflowSweeperTest {

 @Mock
 private WorkflowExecutor workflowExecutor;

 @Mock
 private QueueDAO queueDAO;

 @Mock
 private ConductorProperties properties;

 @InjectMocks
 private WorkflowSweeper workflowSweeper = spy(new WorkflowSweeper(null, null, properties, queueDAO));

 @Before
 public void init() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testSweepAsync() {

 // Given
 String workflowId = "workflow-123";

 doNothing().when(workflowSweeper).sweep(workflowId);

 // When
 workflowSweeper.sweepAsync(workflowId).join();

 // Then
 verify(workflowSweeper, times(1)).sweep(workflowId);
 }

 @Test
 public void testSweep_workflowNotFound() {

 // Given
 String workflowId = "workflow-123";

 doReturn(null).when(workflowExecutor).decide(workflowId);
 doNothing().when(queueDAO).remove(Utils.DECIDER_QUEUE, workflowId);

 // When
 workflowSweeper.sweep(workflowId);

 // Then
 verify(workflowExecutor, times(1)).decide(workflowId);
 verify(queueDAO, times(1)).remove(Utils.DECIDER_QUEUE, workflowId);
 }

 @Test
 public void testSweep_workflowFoundTerminalStatus() {

 // Given
 String workflowId = "workflow-123";

 WorkflowModel workflow = new WorkflowModel();
 workflow.setStatus(Status.COMPLETED);

 doReturn(workflow).when(workflowExecutor).decide(workflowId);
 doNothing().when(queueDAO).remove(Utils.DECIDER_QUEUE, workflowId);

 // When
 workflowSweeper.sweep(workflowId);

 // Then
 verify(workflowExecutor, times(1)).decide(workflowId);
 verify(queueDAO, times(1)).remove(Utils.DECIDER_QUEUE, workflowId);
 }

 @Test
 public void testSweep_workflowFoundInProgress() {

 // Given
 String workflowId = "workflow-123";

 WorkflowModel workflow = new WorkflowModel();
 workflow.addTask(new TaskModel(null, null, null, null, null, Status.IN_PROGRESS, null));

 doReturn(workflow).when(workflowExecutor).decide(workflowId);

 ReflectionTestUtils.setField(workflowSweeper, "workflowOffsetTimeout", 45);

 doNothing().when(queueDAO).setUnackTimeout(eq(Utils.DECIDER_QUEUE), eq(workflowId), anyLong());

 // When
 workflowSweeper.sweep(workflowId);

 // Then
 verify(workflowExecutor, times(1)).decide(workflowId);
 verify(queueDAO, times(1)).setUnackTimeout(eq(Utils.DECIDER_QUEUE), eq(workflowId), anyLong());
 }

 @Test
 public void testSweep_workflowFoundNonTerminalStatus() {

 // Given
 String workflowId = "workflow-123";

 WorkflowModel workflow = new WorkflowModel();
 workflow.setStatus(Status.SCHEDULED);

 doReturn(workflow).when(workflowExecutor).decide(workflowId);

 ReflectionTestUtils.setField(workflowSweeper, "workflowOffsetTimeout", 45);

 doNothing().when(queueDAO).setUnackTimeout(eq(Utils.DECIDER_QUEUE), eq(workflowId), anyLong());

 // When
 workflowSweeper.sweep(workflowId);

 // Then
 verify(workflowExecutor, times(1)).decide(workflowId);
 verify(queueDAO, times(1)).setUnackTimeout(eq(Utils.DECIDER_QUEUE), eq(workflowId), anyLong());
 }

 @Test
 public void testUnack() {

 // Given
 String workflowId = "workflow-123";
 WorkflowModel workflow = new WorkflowModel();
 workflow.setWorkflowDefinition(new WorkflowModel.WorkflowDefinition());

 ReflectionTestUtils.setField(workflowSweeper, "workflowOffsetTimeout", 45);

 doNothing().when(queueDAO).setUnackTimeout(eq(Utils.DECIDER_QUEUE), eq(workflowId), anyLong());

 // When
 workflowSweeper.unack(workflow, 60L);

 // Then
 verify(queueDAO, times(1)).setUnackTimeout(eq(Utils.DECIDER_QUEUE), eq(workflowId), anyLong());
 }

 @Test
 public void testWorkflowOffsetWithJitter() {

 // Given
 long workflowOffsetTimeout = 45;

 // When
 long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

 // Then
 Assert.assertTrue("Result should be greater than " + (2 * workflowOffsetTimeout / 3),
 result >= (2 * workflowOffsetTimeout / 3));
 Assert.assertTrue("Result should be less than " + (4 * workflowOffsetTimeout / 3),
 result <= (4 * workflowOffsetTimeout / 3));
 }
}