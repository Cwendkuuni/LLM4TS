import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.metadata.tasks.TaskModel;
import com.netflix.conductor.core.metadata.tasks.TaskModel.Status;
import com.netflix.conductor.core.metadata.workflow.WorkflowModel;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.QueueTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.AsyncResult;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class WorkflowSweeperTest {

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSweepAsync() throws ExecutionException, InterruptedException {
        String workflowId = "testWorkflowId";
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        completableFuture.complete(null);
        when(workflowExecutor.decide(anyString())).thenReturn(getTestWorkflowModel());
        when(queueDAO.remove(anyString(), anyString()))
                .thenReturn(true);
        when(workflowExecutor.decide(anyString()))
                .thenReturn(getTestTerminalWorkflowModel());
        when(queueDAO.setUnackTimeout(anyString(), anyString(), anyLong()))
                .thenReturn(true);

        AsyncResult<Void> asyncResult = (AsyncResult<Void>) workflowSweeper.sweepAsync(workflowId)
                .get();

        assertEquals(null, asyncResult.getResult());
        Mockito.verify(workflowExecutor, Mockito.times(1))
                .decide(workflowId);
        Mockito.verify(queueDAO, Mockito.times(1))
                .remove(DECIDER_QUEUE, workflowId);
        Mockito.verify(queueDAO, Mockito.times(1))
                .setUnackTimeout(DECIDER_QUEUE, workflowId, 5000L);
    }

    @Test
    public void testSweep() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = getTestWorkflowModel();
        when(workflowExecutor.decide(anyString())).thenReturn(workflowModel);
        when(queueDAO.remove(anyString(), anyString()))
                .thenReturn(true);
        when(workflowExecutor.decide(anyString()))
                .thenReturn(getTestTerminalWorkflowModel());
        when(queueDAO.setUnackTimeout(anyString(), anyString(), anyLong()))
                .thenReturn(true);

        workflowSweeper.sweep(workflowId);

        Mockito.verify(workflowExecutor, Mockito.times(1))
                .decide(workflowId);
        Mockito.verify(queueDAO, Mockito.times(1))
                .remove(DECIDER_QUEUE, workflowId);
        Mockito.verify(queueDAO, Mockito.times(1))
                .setUnackTimeout(DECIDER_QUEUE, workflowId, 5000L);
    }

    @Test
    public void testUnack() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = getTestWorkflowModel();
        long workflowOffsetTimeout = 45;
        when(queueDAO.setUnackTimeout(anyString(), anyString(), anyLong()))
                .thenReturn(true);

        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        Mockito.verify(queueDAO, Mockito.times(1))
                .setUnackTimeout(DECIDER_QUEUE, workflowId, 60L);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;
        long range = workflowOffsetTimeout / 3;
        long jitter = new Random().nextInt((int) (2 * range + 1)) - range;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        Assertions.assertTrue(result >= (workflowOffsetTimeout - range)
                && result <= (workflowOffsetTimeout + range));
    }

    private WorkflowModel getTestWorkflowModel() {
        return new WorkflowModel()
                .setWorkflowId("testWorkflowId")
                .setWorkflowName("testWorkflowName")
                .setStatus(Status.IN_PROGRESS)
                .setTasks(getTestTasks());
    }

    private WorkflowModel getTestTerminalWorkflowModel() {
        return new WorkflowModel()
                .setWorkflowId("testWorkflowId")
                .setWorkflowName("testWorkflowName")
                .setStatus(Status.TERMINAL);
    }

    private TaskModel[] getTestTasks() {
        TaskDef taskDef = new TaskDef()
                .setName("testTaskName")
                .setType(TaskType.TASK_TYPE_SCHEDULED)
                .setPollTimeoutSeconds(10);
        return new TaskModel[] {
                new TaskModel()
                        .setTaskId("testTaskId")
                        .setStatus(Status.IN_PROGRESS)
                        .setTaskType(TaskType.TASK_TYPE_SCHEDULED)
                        .setTaskDef(taskDef)
        };
    }
}