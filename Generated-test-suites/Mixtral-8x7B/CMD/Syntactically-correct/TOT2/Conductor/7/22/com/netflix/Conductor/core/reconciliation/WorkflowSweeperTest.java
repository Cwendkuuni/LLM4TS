import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.metadata.tasks.TaskModel;
import com.netflix.conductor.core.metadata.workflow.WorkflowModel;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.test.util.ReflectionTestUtils;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
public void setup() {
MockitoAnnotations.initMocks(this);
ReflectionTestUtils.setField(workflowSweeper, "sweeperExecutorName", "sweeperExecutorName");
}

@Test
public void sweepAsync_whenWorkflowIdIsProvided_thenSweepMethodIsCalled() {
CompletableFuture<Void> future = workflowSweeper.sweepAsync("workflowId");
verify(workflowSweeper).sweep("workflowId");
future.join();
}

@Test
public void sweep_whenWorkflowIsFound_thenItUnacksTheWorkflow() {
WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setStatus(WorkflowStatus.IN_PROGRESS);
when(workflowExecutor.decide(anyString())).thenReturn(workflowModel);
workflowSweeper.sweep("workflowId");
verify(queueDAO, times(1)).setUnackTimeout(ArgumentMatchers.eq(DECIDER_QUEUE), ArgumentMatchers.eq("workflowId"), ArgumentMatchers.anyLong());
}

@Test
public void sweep_whenWorkflowIsNotFound_thenItRemovesWorkflowFromDeciderQueue() {
workflowSweeper.sweep("workflowId");
verify(queueDAO, times(1)).remove(ArgumentMatchers.eq(DECIDER_QUEUE), ArgumentMatchers.eq("workflowId"));
}

@Test
public void unack_whenTaskIsInProgressAndWaitTask_thenItSetsUnackTimeoutWithJitter() {
TaskModel taskModel = new TaskModel();
taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
taskModel.setTaskType(TaskType.TASK_TYPE_WAIT);
WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setTasks(new TaskModel[]{taskModel});
ReflectionTestUtils.setField(workflowSweeper, "workflowOffsetTimeout", 45);
workflowSweeper.unack(workflowModel, 45);
verify(queueDAO, times(1)).setUnackTimeout(ArgumentMatchers.eq(DECIDER_QUEUE), ArgumentMatchers.eq("workflowId"), ArgumentMatchers.anyLong());
}

@Test
public void workflowOffsetWithJitter_whenWorkflowOffsetTimeoutIs45_thenReturnsValueBetween30And60() {
long result = workflowSweeper.workflowOffsetWithJitter(45);
Assert.assertTrue(result >= 30 && result <= 60);
}
}