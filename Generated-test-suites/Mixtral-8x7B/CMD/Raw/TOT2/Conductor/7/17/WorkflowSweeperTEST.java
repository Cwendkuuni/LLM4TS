import com.netflix.conductor.core.config.TestConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.common.runstatus.WorkflowRunStatus;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef.WorkflowStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class WorkflowSweeperTest {

    @Autowired
    private WorkflowSweeper workflowSweeper;

    @Autowired
    private TestConductorProperties testConductorProperties;

    @MockBean
    private QueueDAO queueDAO;

    @MockBean
    private WorkflowExecutor workflowExecutor;

    @MockBean
    private WorkflowRepairService workflowRepairService;

    private static final String WORKFLOW_ID = "workflow-123";
    private static final String WORKFLOW_NAME = "test-workflow";
    private static final String WAIT_TASK_TYPE = TaskType.TASK_TYPE_WAIT.name();
    private static final String HUMAN_TASK_TYPE = TaskType.TASK_TYPE_HUMAN.name();
    private static final String SCHEDULED_TASK_TYPE = TaskType.TASK_TYPE_SCHEDULED.name();

    @BeforeEach
    public void setup() {
        TestConductorProperties.set(testConductorProperties);
        WorkflowContext.set(new WorkflowContext(testConductorProperties.getAppId()));
    }

    @Test
    public void testSweepAsync() throws ExecutionException, InterruptedException {
        when(queueDAO.getUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID))
                .thenReturn(testConductorProperties.getWorkflowOffsetTimeout().toSeconds() * 1000);

        CompletableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);
        future.get();

        verify(queueDAO, times(1)).getUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweepAsyncNotFoundException() {
        when(queueDAO.getUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID))
                .thenThrow(new NotFoundException("Workflow not found"));

        CompletableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);

        verify(queueDAO, times(1)).getUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweepWithTerminalStatus() {
        WorkflowModel workflowModel =
                new WorkflowModel()
                        .setWorkflowId(WORKFLOW_ID)
                        .setWorkflowName(WORKFLOW_NAME)
                        .setStatus(WorkflowStatus.COMPLETED);

        when(workflowExecutor.decide(WORKFLOW_ID))
                .thenReturn(workflowModel);
        when(queueDAO.getUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID))
                .thenReturn(testConductorProperties.getWorkflowOffsetTimeout().toSeconds() * 1000);

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO, times(1)).getUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweepWithInProgressWaitOrHumanTask() {
        WorkflowModel workflowModel =
                new WorkflowModel()
                        .setWorkflowId(WORKFLOW_ID)
                        .setWorkflowName(WORKFLOW_NAME)
                        .setStatus(WorkflowStatus.IN_PROGRESS);

        TaskModel waitTask =
                new TaskModel()
                        .setTaskId("wait-task-123")
                        .setWorkflowId(WORKFLOW_ID)
                        .setStatus(TaskModel.Status.IN_PROGRESS)
                        .setTaskType(WAIT_TASK_TYPE);
        TaskModel humanTask =
                new TaskModel()
                        .setTaskId("human-task-123")
                        .setWorkflowId(WORKFLOW_ID)
                        .setStatus(TaskModel.Status.IN_PROGRESS)
                        .setTaskType(HUMAN_TASK_TYPE);

        workflowModel.setTasks(List.of(waitTask, humanTask));

        when(workflowExecutor.decide(WORKFLOW_ID))
                .thenReturn(workflowModel);
        when(queueDAO.getUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID))
                .thenReturn(testConductorProperties.getWorkflowOffsetTimeout().toSeconds() * 1000);

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO, never()).remove(DECIDER_QUEUE, WORKFLOW_ID);
        verify(queueDAO, times(1))
                .setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, 45000L); // 45 seconds
    }

    @Test
    public void testSweepWithScheduledTask() {
        WorkflowModel workflowModel =
                new WorkflowModel()
                        .setWorkflowId(WORKFLOW_ID)
                        .setWorkflowName(WORKFLOW_NAME)
                        .setStatus(WorkflowStatus.IN_PROGRESS);

        TaskModel scheduledTask =
                new TaskModel()
                        .setTaskId("scheduled-task-123")
                        .setWorkflowId(WORKFLOW_ID)
                        .setStatus(TaskModel.Status.SCHEDULED)
                        .setTaskType(SCHEDULED_TASK_TYPE);

        workflowModel.setTasks(List.of(scheduledTask));

        when(workflowExecutor.decide(WORKFLOW_ID))
                .thenReturn(workflowModel);
        when(queueDAO.getUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID))
                .thenReturn(testConductorProperties.getWorkflowOffsetTimeout().toSeconds() * 1000);

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO, never()).remove(DECIDER_QUEUE, WORKFLOW_ID);
        verify(queueDAO, times(1))
                .setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, 45000L); // 45 seconds
    }

    @Test
    public void testSweepWithNonExistentTask() {
        when(queueDAO.getUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID))
                .thenReturn(testConductorProperties.getWorkflowOffsetTimeout().toSeconds() * 1000);

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO, times(1)).getUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID);
        verify(queueDAO, never()).remove(DECIDER_QUEUE, WORKFLOW_ID);
        verify(queueDAO, times(1))
                .setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, 45000L); // 45 seconds
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel =
                new WorkflowModel()
                        .setWorkflowId(WORKFLOW_ID)
                        .setWorkflowName(WORKFLOW_NAME)
                        .setStatus(WorkflowStatus.IN_PROGRESS);

        TaskModel waitTask =
                new TaskModel()
                        .setTaskId("wait-task-123")
                        .setWorkflowId(WORKFLOW_ID)
                        .setStatus(TaskModel.Status.IN_PROGRESS)
                        .setTaskType(WAIT_TASK_TYPE);

        workflowModel.setTasks(List.of(waitTask));

        when(workflowExecutor.decide(WORKFLOW_ID))
                .thenReturn(workflowModel);

        workflowSweeper.unack(workflowModel, 45L);

        verify(queueDAO, times(1))
                .setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, 45000L); // 45 seconds
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long jitterValue = workflowSweeper.workflowOffsetWithJitter(45);

        Assertions.assertTrue(jitterValue >= 30 && jitterValue <= 60);
    }
}