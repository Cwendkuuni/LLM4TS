package com.netflix.conductor.core.reconciliation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
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
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.util.ReflectionTestUtils;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.events.queue.QueueDAO;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.model.TaskModel.Status;

@EnableAsync
public class WorkflowSweeperTest {

 @Mock
 private WorkflowExecutor workflowExecutor;

 @Mock
 private QueueDAO queueDAO;

 @Mock
 private WorkflowRepairService workflowRepairService;

 @Mock
 private ConductorProperties conductorProperties;

 @InjectMocks
 private WorkflowSweeper workflowSweeper;

 @Before
 public void init() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void sweepAsyncTest() throws Exception {
 // Given
 String workflowId = "workflow-1";

 // When
 ReflectionTestUtils.setField(workflowSweeper, "sweeperExecutor",
 mock(ThreadPoolTaskExecutor.class));
 doNothing().when(workflowSweeper).sweep(anyString());

 // Then
 workflowSweeper.sweepAsync(workflowId);
 verify(workflowSweeper, times(1)).sweep(workflowId);
 }

 @Test
 public void sweepTest() throws Exception {
 // Given
 String workflowId = "workflow-1";
 WorkflowModel workflow = getWorkflowModel();
 when(workflowExecutor.decide(workflowId)).thenReturn(workflow);
 when( conductorProperties.getAppId()).thenReturn("conductor");

 // When
 WorkflowContext workflowContext = mock(WorkflowContext.class);
 when(workflowContext.getAppId()).thenReturn("conductor");
 ReflectionTestUtils.setField(workflowSweeper, "queueDAO", queueDAO);
 ReflectionTestUtils.setField(workflowSweeper, "workflowExecutor", workflowExecutor);
 ReflectionTestUtils.setField(workflowSweeper, "conductorProperties", conductorProperties);
 WorkflowContext.set(workflowContext);

 // Then
 workflowSweeper.sweep(workflowId);
 verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
 }

 @Test
 public void sweepTestNotFoundException() throws Exception {
 // Given
 String workflowId = "workflow-1";
 when(workflowExecutor.decide(workflowId)).thenThrow(new NotFoundException("Not found"));

 // When
 WorkflowContext workflowContext = mock(WorkflowContext.class);
 when(workflowContext.getAppId()).thenReturn("conductor");
 ReflectionTestUtils.setField(workflowSweeper, "queueDAO", queueDAO);
 ReflectionTestUtils.setField(workflowSweeper, "workflowExecutor", workflowExecutor);
 ReflectionTestUtils.setField(workflowSweeper, "conductorProperties", conductorProperties);
 WorkflowContext.set(workflowContext);

 // Then
 workflowSweeper.sweep(workflowId);
 verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
 }

 @Test
 public void sweepTestException() throws Exception {
 // Given
 String workflowId = "workflow-1";
 when(workflowExecutor.decide(workflowId)).thenThrow(new Exception("Error"));

 // When
 WorkflowContext workflowContext = mock(WorkflowContext.class);
 when(workflowContext.getAppId()).thenReturn("conductor");
 ReflectionTestUtils.setField(workflowSweeper, "queueDAO", queueDAO);
 ReflectionTestUtils.setField(workflowSweeper, "workflowExecutor", workflowExecutor);
 ReflectionTestUtils.setField(workflowSweeper, "conductorProperties", conductorProperties);
 WorkflowContext.set(workflowContext);

 // Then
 workflowSweeper.sweep(workflowId);
 verify(queueDAO, never()).remove(DECIDER_QUEUE, workflowId);
 }

 @Test
 public void unackTest() throws Exception {
 // Given
 WorkflowModel workflowModel = getWorkflowModel();
 TaskModel taskModel1 = getTaskModel(Status.IN_PROGRESS, TaskType.TASK_TYPE_TASK);
 TaskModel taskModel2 = getTaskModel(Status.SCHEDULED, TaskType.TASK_TYPE_WAIT);
 workflowModel.setTasks(taskModel1, taskModel2);

 // When
 ReflectionTestUtils.setField(workflowSweeper, "queueDAO", queueDAO);

 // Then
 workflowSweeper.unack(workflowModel, 45);
 verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), 60000);
 }

 @Test
 public void workflowOffsetWithJitterTest() throws Exception {
 // Given

 // When
 WorkflowSweeper spyWorkflowSweeper = spy(workflowSweeper);

 // Then
 Assert.assertTrue(spyWorkflowSweeper.workflowOffsetWithJitter(45) >= 30);
 Assert.assertTrue(spyWorkflowSweeper.workflowOffsetWithJitter(45) <= 60);
 }

 private WorkflowModel getWorkflowModel() {
 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setStatus(Status.IN_PROGRESS);
 workflowModel.setWorkflowId("workflow-1");
 workflowModel.setWorkflowName("workflow-name");
 workflowModel.setWorkflowDefinition(getWorkflowDef());
 return workflowModel;
 }

 private WorkflowDef getWorkflowDef() {
 WorkflowDef workflowDef = new WorkflowDef();
 workflowDef.setTimeoutSeconds(45);
 return workflowDef;
 }

 private TaskModel getTaskModel(Status status, TaskType taskType) {
 TaskModel taskModel = new TaskModel();
 taskModel.setStatus(status);
 taskModel.setTaskType(taskType);
 return taskModel;
 }
}