import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.events.EventService;
import com.netflix.conductor.core.events.queue.Queue;
import com.netflix.conductor.core.events.queue.QueueManager;
import com.netflix.conductor.core.utils.DeadLetterUtils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.TaskDAO;
import com.netflix.conductor.dao.WorkflowDAO;
import com.netflix.conductor.metrics.Monitors;

public class WorkflowSweeperTest {

@Mock
private WorkflowExecutor workflowExecutor;

@Mock
private QueueDAO queueDAO;

@Mock
private TaskDAO taskDAO;

@Mock
private WorkflowDAO workflowDAO;

@Mock
private EventService eventService;

@Mock
private QueueManager queueManager;

@Mock
private DeadLetterUtils deadLetterUtils;

private WorkflowSweeper workflowSweeper;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);

workflowSweeper = new WorkflowSweeper(workflowExecutor, null, new ConductorProperties(), queueDAO,
taskDAO, workflowDAO, eventService, queueManager, deadLetterUtils);
}

@Test
public void testSweepAsync() throws Exception {
// Arrange
CompletableFuture<Void> future = workflowSweeper.sweepAsync("workflow1");

// Assert
Assert.assertNotNull(future);
Assert.assertTrue(future.isCompletedExceptionally() == false);
Assert.assertTrue(future.isCancelled() == false);
Assert.assertTrue(future.isDone() == true);
}

@Test
public void testSweep() throws Exception {
// Arrange
WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setStatus(Status.IN_PROGRESS);

WorkflowContext workflowContext = new WorkflowContext("appId");
WorkflowContext.set(workflowContext);

when(workflowExecutor.decide(anyString())).thenReturn(workflowModel);

// Act
workflowSweeper.sweep("workflow1");

// Assert
verify(queueDAO, times(1)).remove(anyString(), anyString());
verify(queueDAO, never()).setUnackTimeout(anyString(), anyString(), anyLong());
}

@Test
public void testSweepNotFoundException() throws Exception {
// Arrange
WorkflowModel workflowModel = null;

WorkflowContext workflowContext = new WorkflowContext("appId");
WorkflowContext.set(workflowContext);

when(workflowExecutor.decide(anyString())).thenThrow(new NotFoundException("Workflow not found"));

// Act
workflowSweeper.sweep("workflow1");

// Assert
verify(queueDAO, times(1)).remove(anyString(), anyString());
verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), anyLong());
}

@Test
public void testUnack() throws Exception {
// Arrange
TaskModel taskModel1 = new TaskModel();
taskModel1.setTaskType(TaskType.TASK_TYPE_SCHEDULED);
taskModel1.setStatus(Status.SCHEDULED);

TaskModel taskModel2 = new TaskModel();
taskModel2.setTaskType(TaskType.TASK_TYPE_IN_PROGRESS);
taskModel2.setStatus(Status.IN_PROGRESS);

WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setTasks(new TaskModel[] { taskModel1, taskModel2 });
workflowModel.setWorkflowDefinition(new WorkflowDef());

WorkflowContext workflowContext = new WorkflowContext("appId");
WorkflowContext.set(workflowContext);

when(workflowExecutor.decide(anyString())).thenReturn(workflowModel);

// Act
workflowSweeper.sweep("workflow1");

// Assert
verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), anyLong());
}

@Test
public void testWorkflowOffsetWithJitter() throws Exception {
// Arrange
long workflowOffsetTimeout = 45;

// Act
long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

// Assert
Assert.assertTrue(result >= (workflowOffsetTimeout - (workflowOffsetTimeout / 3))
&& result <= (workflowOffsetTimeout + (workflowOffsetTimeout / 3)));
}
}