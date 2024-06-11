import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowModel.WorkflowStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.test.util.AsyncTestUtils;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WorkflowSweeperTest {

private WorkflowExecutor workflowExecutor;
private QueueDAO queueDAO;
private WorkflowSweeper workflowSweeper;
private ConductorProperties conductorProperties;

@Before
public void setup() {
workflowExecutor = Mockito.mock(WorkflowExecutor.class);
queueDAO = Mockito.mock(QueueDAO.class);
conductorProperties = Mockito.mock(ConductorProperties.class);

WorkflowContext workflowContext = new WorkflowContext(conductorProperties.getAppId());
WorkflowContext.set(workflowContext);

workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.empty(), conductorProperties, queueDAO);
}

@Test
public void sweepAsyncTest() throws ExecutionException, InterruptedException {
CompletableFuture<Void> future = workflowSweeper.sweepAsync("workflow1");
AsyncTestUtils.getFuture(future);
verify(workflowExecutor, times(1)).decide("workflow1");
}

@Test
public void sweepTest() {
WorkflowModel workflow = new WorkflowModel();
workflow.setStatus(WorkflowStatus.COMPLETED);

when(workflowExecutor.decide("workflow1")).thenReturn(workflow);

workflowSweeper.sweep("workflow1");
verify(queueDAO, times(1)).remove("decider_queue", "workflow1");
}

@Test
public void sweepNotFoundExceptionTest() {
when(workflowExecutor.decide("workflow1")).thenThrow(new NotFoundException("Workflow not found"));

workflowSweeper.sweep("workflow1");
verify(queueDAO, times(1)).remove("decider_queue", "workflow1");
}

@Test
public void unackWithWorkflowModelTest() {
WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setTasks(
Mockito.mock(java.util.Collection.class));

workflowSweeper.unack(workflowModel, 30);
verify(queueDAO, times(1)).setUnackTimeout("decider_queue", "workflow1", 30000);
}

@Test
public void unackWithoutWorkflowModelTest() {
workflowSweeper.unack(null, 30);
verify(queueDAO, times(1)).setUnackTimeout("decider_queue", "workflow1", 30000);
}

@Test
public void workflowOffsetWithJitterTest() {
long result1 = workflowSweeper.workflowOffsetWithJitter(45);
Assert.assertTrue(result1 >= 30 && result1 <= 60);

long result2 = workflowSweeper.workflowOffsetWithJitter(120);
Assert.assertTrue(result2 >= 80 && result2 <= 160);
}
}