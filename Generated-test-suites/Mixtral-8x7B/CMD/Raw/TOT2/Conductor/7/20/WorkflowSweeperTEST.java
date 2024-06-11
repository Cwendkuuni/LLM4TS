import com.netflix.conductor.core.config.TestConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.common.runstatus.WorkflowRunStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.AsyncResult;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class WorkflowSweeperTest {

@Mock
private QueueDAO queueDAO;

@Mock
private WorkflowExecutor workflowExecutor;

@Mock
private WorkflowRepairService workflowRepairService;

@Mock
private ConductorProperties conductorProperties;

@InjectMocks
private WorkflowSweeper workflowSweeper;

@Mock
private TestConductorProperties testConductorProperties;

@BeforeEach
public void setup() {
MockitoAnnotations.initMocks(this);
}

// Helper method to create a WorkflowModel
private WorkflowModel createWorkflowModel(String workflowId, String status) {
return new WorkflowModel()
.setWorkflowId(workflowId)
.setStatus(WorkflowRunStatus.valueOf(status));
}

// Helper method to create a TaskModel
private TaskModel createTaskModel(String taskId, String status) {
return new TaskModel()
.setTaskId(taskId)
.setStatus(Status.valueOf(status));
}

@Test
public void sweepAsyncTest() throws Exception {
// Given
String workflowId = "workflow-001";
CompletableFuture<Void> completableFuture = new CompletableFuture<>();
when(queueDAO.remove(DECIDER_QUEUE, workflowId)).thenReturn(completableFuture);

// When
workflowSweeper.sweepAsync(workflowId).get();

// Then
Mockito.verify(queueDAO, Mockito.times(1)).remove(DECIDER_QUEUE, workflowId);
}

@Test
public void sweepAsyncExceptionTest() throws Exception {
// Given
String workflowId = "workflow-001";
when(queueDAO.remove(DECIDER_QUEUE, workflowId))
.thenThrow(new Exception("Queue removal failed"));

// When
workflowSweeper.sweepAsync(workflowId).get();

// Then
Mockito.verify(queueDAO, Mockito.times(1)).remove(DECIDER_QUEUE, workflowId);
}

@Test
public void sweepTest() throws Exception {
// Given
String workflowId = "workflow-001";
WorkflowModel workflowModel = createWorkflowModel(workflowId, "COMPLETED");
List<TaskModel> taskModels = Collections.singletonList(createTaskModel(workflowId, "IN_PROGRESS"));

when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);
when(queueDAO.getTasksByWorkflowIdAndStatus(DECIDER_QUEUE, workflowId, Status.IN_PROGRESS.name()))
.thenReturn(taskModels);

// When
workflowSweeper.sweep(workflowId);

// Then
Mockito.verify(queueDAO, Mockito.times(1)).remove(DECIDER_QUEUE, workflowId);
}

@Test
public void sweepNotFoundExceptionTest() throws Exception {
// Given
String workflowId = "workflow-001";
when(workflowExecutor.decide(workflowId))
.thenThrow(new NotFoundException("Workflow not found for id: " + workflowId));

// When
workflowSweeper.sweep(workflowId);

// Then
Mockito.verify(queueDAO, Mockito.times(1)).remove(DECIDER_QUEUE, workflowId);
}

@Test
public void sweepUnackTest() throws Exception {
// Given
String workflowId = "workflow-001";
WorkflowModel workflowModel = createWorkflowModel(workflowId, "IN_PROGRESS");

when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);
when(queueDAO.getTasksByWorkflowIdAndStatus(DECIDER_QUEUE, workflowId, Status.IN_PROGRESS.name()))
.thenReturn(Collections.emptyList());

// When
workflowSweeper.sweep(workflowId);

// Then
Mockito.verify(queueDAO, Mockito.times(1)).setUnackTimeout(
DECIDER_QUEUE, workflowId, anyLong());
}

@Test
public void workflowOffsetWithJitterTest() {
// Given
long workflowOffsetTimeout = 45_000;

// When
long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

// Then
long minValue = workflowOffsetTimeout - (workflowOffsetTimeout / 3);
long maxValue = workflowOffsetTimeout + (workflowOffsetTimeout / 3);
Assertions.assertTrue(result >= minValue && result <= maxValue);
}
}