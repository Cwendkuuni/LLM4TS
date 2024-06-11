package com.netflix.conductor.core.reconciliation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.metadata.workflow.WorkflowDefinition;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.events.EventService;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.ReconciliationTaskDAO;
import com.netflix.conductor.metrics.Monitors;

public class WorkflowSweeperTest {

@Mock
private ConductorProperties properties;

@Mock
private WorkflowExecutor workflowExecutor;

@Mock
private WorkflowRepairService workflowRepairService;

@Mock
private QueueDAO queueDAO;

@Mock
private EventService eventService;

@Mock
private ReconciliationTaskDAO reconciliationTaskDAO;

@Mock
private ThreadPoolTaskExecutor sweeperExecutor;

@InjectMocks
private WorkflowSweeper workflowSweeper;

@Before
public void init() {
MockitoAnnotations.initMocks(this);
}

@Test
public void sweepAsyncTest() throws Exception {
AsyncResult<Void> asyncResult = new AsyncResult<>(null);
AsyncTestUtils.await(asyncResult);

verify(workflowSweeper, times(1)).sweepAsync(any());
}

@Test
public void sweepAsyncExceptionTest() throws Exception {
AsyncTestUtils.setException(new RuntimeException("Test exception"));

AsyncResult<Void> asyncResult = new AsyncResult<>(null);
asyncResult.setResult(workflowSweeper.sweepAsync("testWorkflowId"));

AsyncTestUtils.await(asyncResult);

verify(workflowSweeper, times(1)).sweepAsync(any());
}

@Test
public void sweepAsyncSuccessTest() throws Exception {
AsyncResult<Void> asyncResult = new AsyncResult<>(null);
asyncResult.setResult(workflowSweeper.sweepAsync("testWorkflowId").get());

AsyncTestUtils.await(asyncResult);

verify(workflowSweeper, times(1)).sweepAsync(any());
}

@Test
public void sweepTest() throws Exception {
ArgumentCaptor<String> workflowIdCaptor = ArgumentCaptor.forClass(String.class);

when(queueDAO.getUnacknowledgedTasks(DECIDER_QUEUE, 1)).thenReturn(
"[{\"workflowId\":\"testWorkflowId\", \"taskType\": \"DECISION\", \"timeout\": 300, \"retryCount\": 0, \"status\": \"IN_PROGRESS\", \"startTime\": 1633038666361, \"endTime\": 0, \"correlationId\": null, \"input\": \"{}\", \"output\": null, \"retryInterval\": 0, \"taskId\": \"testTaskId\", \"definitionVersion\": 1, \"scheduleToStartTimeout\": 0, \"parentTaskId\": null, \"execution\": null}]"
);

when(workflowExecutor.decide(workflowIdCaptor.capture())).thenReturn(getWorkflowModel());

WorkflowModel capturedWorkflowModel = getWorkflowModel();
when(workflowExecutor.getWorkflow(capturedWorkflowModel.getWorkflowId()))
.thenReturn(Optional.of(capturedWorkflowModel));

doNothing().when(queueDAO).remove(DECIDER_QUEUE, workflowIdCaptor.capture());

workflowSweeper.sweep("testWorkflowId");

verify(queueDAO, times(1)).getUnacknowledgedTasks(DECIDER_QUEUE, 1);
verify(workflowExecutor, times(1)).decide(workflowIdCaptor.capture());
verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowIdCaptor.capture());

Assert.assertEquals("testWorkflowId", workflowIdCaptor.getValue());
Assert.assertEquals(Status.COMPLETED, capturedWorkflowModel.getStatus());
}

@Test
public void sweepNotFoundExceptionTest() throws Exception {
when(queueDAO.getUnacknowledgedTasks(DECIDER_QUEUE, 1)).thenReturn(null);

workflowSweeper.sweep("testWorkflowId");

verify(queueDAO, times(1)).getUnacknowledgedTasks(DECIDER_QUEUE, 1);
verify(queueDAO, times(1)).remove(DECIDER_QUEUE, "testWorkflowId");
}

@Test
public void sweepExceptionTest() throws Exception {
when(queueDAO.getUnacknowledgedTasks(DECIDER_QUEUE, 1)).thenReturn(
"[{\"workflowId\":\"testWorkflowId\", \"taskType\": \"DECISION\", \"timeout\": 300, \"retryCount\": 0, \"status\": \"IN_PROGRESS\", \"startTime\": 1633038666361, \"endTime\": 0, \"correlationId\": null, \"input\": \"{}\", \"output\": null, \"retryInterval\": 0, \"taskId\": \"testTaskId\", \"definitionVersion\": 1, \"scheduleToStartTimeout\": 0, \"parentTaskId\": null, \"execution\": null}]"
);

when(workflowExecutor.decide(any())).thenThrow(new NotFoundException("Test exception"));

workflowSweeper.sweep("testWorkflowId");

verify(queueDAO, times(1)).getUnacknowledgedTasks(DECIDER_QUEUE, 1);
verify(queueDAO, times(1)).remove(DECIDER_QUEUE, "testWorkflowId");
}

@Test
public void sweepNoTasksTest() throws Exception {
when(queueDAO.getUnacknowledgedTasks(DECIDER_QUEUE, 1)).thenReturn("");

workflowSweeper.sweep("testWorkflowId");

verify(queueDAO, times(1)).getUnacknowledgedTasks(DECIDER_QUEUE, 1);
verify(queueDAO, never()).remove(DECIDER_QUEUE, "testWorkflowId");
}

@Test
public void unackTest() throws Exception {
ArgumentCaptor<String> workflowIdCaptor = ArgumentCaptor.forClass(String.class);
ArgumentCaptor<Long> timeoutCaptor = ArgumentCaptor.forClass(Long.class);

when(queueDAO.getTask(DECIDER_QUEUE, "testWorkflowId")).thenReturn(
"[{\"workflowId\":\"testWorkflowId\", \"taskType\": \"DECISION\", \"timeout\": 300, \"retryCount\": 0, \"status\": \"IN_PROGRESS\", \"startTime\": 1633038666361, \"endTime\": 0, \"correlationId\": null, \"input\": \"{}\", \"output\": null, \"retryInterval\": 0, \"taskId\": \"testTaskId\", \"definitionVersion\": 1, \"scheduleToStartTimeout\": 0, \"parentTaskId\": null, \"execution\": null}]"
);

workflowSweeper.unack(getWorkflowModel(), 100);

verify(queueDAO, times(1)).getTask(DECIDER_QUEUE, workflowIdCaptor.capture());
verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowIdCaptor.capture(),
timeoutCaptor.capture());

Assert.assertEquals("testWorkflowId", workflowIdCaptor.getValue());
Assert.assertEquals(Long.valueOf(100000), timeoutCaptor.getValue());
}

@Test
public void unackNoTaskTest() throws Exception {
when(queueDAO.getTask(DECIDER_QUEUE, "testWorkflowId")).thenReturn("");

workflowSweeper.unack(getWorkflowModel(), 100);

verify(queueDAO, times(1)).getTask(DECIDER_QUEUE, "testWorkflowId");
verify(queueDAO, never()).setUnackTimeout(DECIDER_QUEUE, "testWorkflowId", 100000);
}

@Test
public void workflowOffsetWithJitterTest() throws Exception {
Assert.assertEquals(60, workflowSweeper.workflowOffsetWithJitter(30));
Assert.assertEquals(60, workflowSweeper.workflowOffsetWithJitter(60));
Assert.assertEquals(120, workflowSweeper.workflowOffsetWithJitter(180));
Assert.assertEquals(120, workflowSweeper.workflowOffsetWithJitter(120));
Assert.assertEquals(180, workflowSweeper.workflowOffsetWithJitter(270));
Assert.assertEquals(180, workflowSweeper.workflowOffsetWithJitter(180));
}

private WorkflowModel getWorkflowModel() {
WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setWorkflowId("testWorkflowId");
workflowModel.setWorkflowName("testWorkflowName");
workflowModel.setStatus(Status.IN_PROGRESS);
workflowModel.setTasks(
new TaskModel[] {
new TaskModel("testTaskId", TaskType.DECISION, Instant.now().toEpochMilli(), 0,
new TaskDef(),
"{}", null, "", 0, 300, 0,
null, null, Optional.empty(), null, null, null, null)
});
workflowModel.setWorkflowDefinition(
new WorkflowDefinition("testWorkflowName", Optional.empty(), Optional.empty(),
new TaskDef(), null, null, null, null, null));
return workflowModel;
}
}