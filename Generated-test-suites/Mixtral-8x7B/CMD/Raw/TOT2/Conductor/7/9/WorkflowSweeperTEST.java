import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("unittest")
public class WorkflowSweeperTest {

@Autowired
private ApplicationContext applicationContext;

@MockBean
private WorkflowExecutor workflowExecutor;

@MockBean
private WorkflowRepairService workflowRepairService;

@MockBean
private ConductorProperties properties;

@MockBean
private QueueDAO queueDAO;

private WorkflowSweeper workflowSweeper;

@BeforeEach
public void setup() {
workflowSweeper = applicationContext.getBean(WorkflowSweeper.class);
}

@Test
public void sweepAsyncTest() throws ExecutionException, InterruptedException {
// Arrange
String workflowId = "testWorkflow";
CompletableFuture<Void> future = new CompletableFuture<>();
when(queueDAO.remove(DECIDER_QUEUE, workflowId)).thenReturn(future);

// Act
CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId).get();

// Assert
Assertions.assertSame(future, result);
Mockito.verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
}

@Test
public void sweepTest() {
// Arrange
String workflowId = "testWorkflow";
WorkflowModel workflow = new WorkflowModel();
workflow.setStatus(Status.FAILED);
WorkflowContext workflowContext = new WorkflowContext(properties.getAppId());
WorkflowContext.set(workflowContext);

when(workflowExecutor.decide(workflowId)).thenReturn(workflow);
when(queueDAO.remove(DECIDER_QUEUE, workflowId)).thenReturn(null);

// Act
workflowSweeper.sweep(workflowId);

// Assert
Mockito.verify(workflowExecutor).decide(workflowId);
Mockito.verify(queueDAO).remove(DECIDER_QUEUE, workflowId);
}

@Test
public void unackTest() {
// Arrange
WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setWorkflowDefinition(new TaskDef());
workflowModel.setTasks(null);

long workflowOffsetTimeout = 45;

when(queueDAO.setUnackTimeout(anyString(), anyString(), anyLong()))
.thenReturn(null);

// Act
workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

// Assert
Mockito.verify(queueDAO)
.setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), workflowOffsetTimeout * 1000);
}

@Test
public void workflowOffsetWithJitterTest() {
// Arrange
long workflowOffsetTimeout = 45;

// Act
long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

// Assert
long range = workflowOffsetTimeout / 3;
long lowerBound = workflowOffsetTimeout - range;
long upperBound = workflowOffsetTimeout + (range * 2);
Assertions.assertTrue(result >= lowerBound && result <= upperBound);
}
}