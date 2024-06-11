package com.netflix.conductor.core.reconciliation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.util.AsyncTestUtils;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.utils.TestUtils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.Status;

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
 private WorkflowSweeper workflowSweeper;

 @Before
 public void init() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void sweepAsyncTest() {
 AsyncTestUtils.getExecutor().execute(() -> workflowSweeper.sweepAsync("workflowId"));
 }

 @Test
 public void sweepTest() {
 WorkflowContext workflowContext = new WorkflowContext("appId");
 WorkflowContext.set(workflowContext);
 WorkflowModel workflowModel = TestUtils.createWorkflowModel();
 when(workflowExecutor.decide(anyString())).thenReturn(workflowModel);
 doNothing().when(queueDAO).remove(anyString(), anyString());
 workflowSweeper.sweep("workflowId");
 verify(workflowExecutor, times(1)).decide("workflowId");
 }

 @Test
 public void unackTest() {
 WorkflowModel workflowModel = TestUtils.createWorkflowModel();
 when(queueDAO.getUnackTimeout(anyString(), anyString())).thenReturn(100L);
 workflowSweeper.unack(workflowModel, 10);
 verify(queueDAO, times(1))
 .setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), 11000);
 }

 @Test
 public void workflowOffsetWithJitterTest() {
 long workflowOffsetTimeout = 45;
 long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
 Assert.assertTrue(result >= (2 * workflowOffsetTimeout / 3) && result <= (4 * workflowOffsetTimeout / 3));
 }

 @Test
 public void testSweep_whenWorkflowNotFound() {
 doNothing().when(queueDAO).remove(anyString(), anyString());
 workflowSweeper.sweep("not_found_workflowId");
 verify(queueDAO, times(1)).remove(DECIDER_QUEUE, "not_found_workflowId");
 }

 @Test
 public void testSweep_whenWorkflowRepairServiceIsNull() {
 WorkflowModel workflowModel = TestUtils.createWorkflowModel();
 when(workflowExecutor.decide(anyString())).thenReturn(workflowModel);
 doNothing().when(queueDAO).remove(anyString(), anyString());
 workflowSweeper = new WorkflowSweeper(workflowExecutor, null, properties, queueDAO);
 workflowSweeper.sweep("workflowId");
 verify(workflowExecutor, times(1)).decide("workflowId");
 }

 @Test
 public void testUnack_whenWorkflowModelIsNull() {
 workflowSweeper.unack(null, 10);
 }
}