import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.metadata.tasks.TaskModel;
import com.netflix.conductor.core.metadata.workflow.WorkflowModel;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.AsyncResult;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

public class WorkflowSweeperTest {

    private static final String WORKFLOW_ID = "workflow-1";
    private static final String WORKFLOW_NAME = "workflow-name";
    private static final String TASK_TYPE = TaskType.TASK_TYPE_SIMPLE_TASK.name();
    private static final String TASK_DEF_NAME = "test-task-def";
    private static final String DECIDER_QUEUE = "decider";
    private static final String APP_ID = "test-app";

    private WorkflowSweeper workflowSweeper;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        workflowSweeper = new WorkflowSweeper(workflowExecutor,
                Optional.of(workflowRepairService),
                new ConductorProperties(),
                queueDAO);

        Mockito.when(queueDAO.remove(DECIDER_QUEUE, WORKFLOW_ID))
                .thenReturn(Mono.empty());

        Mockito.when(queueDAO.setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, ArgumentMatchers.anyLong()))
                .thenReturn(Mono.empty());
    }

    @Test
    public void sweepAsyncTest() {
        Mockito.when(workflowExecutor.decide(WORKFLOW_ID))
                .thenReturn(Mono.just(createWorkflowModel()));

        StepVerifier.create(workflowSweeper.sweepAsync(WORKFLOW_ID))
                .verifyComplete();

        Mockito.verify(queueDAO, Mockito.times(1))
                .remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void sweepAsyncNotFoundExceptionTest() {
        Mockito.when(workflowExecutor.decide(WORKFLOW_ID))
                .thenReturn(Mono.error(new NotFoundException("Workflow not found for id: " + WORKFLOW_ID)));

        StepVerifier.create(workflowSweeper.sweepAsync(WORKFLOW_ID))
                .verifyComplete();

        Mockito.verify(queueDAO, Mockito.times(1))
                .remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void sweepAsyncExceptionTest() {
        Mockito.when(workflowExecutor.decide(WORKFLOW_ID))
                .thenReturn(Mono.error(new RuntimeException("Unexpected error")));

        StepVerifier.create(workflowSweeper.sweepAsync(WORKFLOW_ID))
                .expectErrorMessage("Unexpected error")
                .verify();
    }

    @Test
    public void sweepTerminalWorkflowTest() {
        WorkflowModel workflowModel = createWorkflowModel();
        workflowModel.setStatus(WorkflowStatus.COMPLETED);

        Mockito.when(workflowExecutor.decide(WORKFLOW_ID))
                .thenReturn(Mono.just(workflowModel));

        workflowSweeper.sweep(WORKFLOW_ID);

        Mockito.verify(queueDAO, Mockito.times(1))
                .remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void sweepNonTerminalWorkflowTest() {
        WorkflowModel workflowModel = createWorkflowModel();

        Mockito.when(workflowExecutor.decide(WORKFLOW_ID))
                .thenReturn(Mono.just(workflowModel));

        workflowSweeper.sweep(WORKFLOW_ID);

        Mockito.verify(queueDAO, Mockito.times(1))
                .setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, ArgumentMatchers.anyLong());
    }

    @Test
    public void unackTest() {
        WorkflowModel workflowModel = createWorkflowModel();

        workflowSweeper.unack(workflowModel, 30);

        Mockito.verify(queueDAO, Mockito.times(1))
                .setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, 30 * 1000);
    }

    @Test
    public void workflowOffsetWithJitterTest() {
        long workflowOffsetTimeout = 45;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        Assertions.assertTrue(result >= workflowOffsetTimeout - (workflowOffsetTimeout / 3) &&
                result <= workflowOffsetTimeout + (workflowOffsetTimeout / 3));
    }

    private WorkflowModel createWorkflowModel() {
        return WorkflowModel.builder()
                .appId(APP_ID)
                .workflowId(WORKFLOW_ID)
                .workflowName(WORKFLOW_NAME)
                .status(WorkflowStatus.RUNNING)
                .tasks(List.of(
                        TaskModel.builder()
                                .taskId(UUID.randomUUID().toString())
                                .workflowId(WORKFLOW_ID)
                                .status(TaskModel.Status.IN_PROGRESS)
                                .taskType(TASK_TYPE)
                                .build()))
                .build();
    }
}