import com.netflix.conductor.core.config.TestConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.AsyncResult;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class WorkflowSweeperTest {

    private static final String WORKFLOW_ID = "workflow-123";
    private static final String WORKFLOW_NAME = "test-workflow";
    private static final String TASK_TYPE = TaskType.TASK_TYPE_SCHEDULED.name();
    private static final String TASK_DEF_NAME = "test-task-def";
    private static final String DECIDER_QUEUE = "decider-queue";
    private static final String APP_ID = "test-app";

    private WorkflowSweeper workflowSweeper;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private Monitors monitors;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private ConductorProperties conductorProperties;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(conductorProperties.getAppId()).thenReturn(APP_ID);
        when(conductorProperties.getWorkflowOffsetTimeout()).thenReturn(Mono.just(60L));

        workflowSweeper = new WorkflowSweeper(
                workflowExecutor,
                Optional.of(workflowRepairService),
                conductorProperties,
                queueDAO
        );
    }

    @Test
    public void sweepAsyncTest() {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(getWorkflowModel());

        StepVerifier.create(workflowSweeper.sweepAsync(WORKFLOW_ID))
                .expectComplete()
                .verify();

        verify(workflowExecutor, times(1)).decide(WORKFLOW_ID);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void sweepTest() {
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(getWorkflowModel());

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(workflowExecutor, times(1)).decide(WORKFLOW_ID);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void unackTest() {
        WorkflowModel workflowModel = getWorkflowModel();

        when(queueDAO.setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, ArgumentMatchers.anyLong()))
                .thenReturn(Mono.empty());

        workflowSweeper.unack(workflowModel, 30);

        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, 30000);
    }

    @Test
    public void workflowOffsetWithJitterTest() {
        long workflowOffsetTimeout = 60;
        long jitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        Assertions.assertTrue(jitter >= (workflowOffsetTimeout - (workflowOffsetTimeout / 3))
                && jitter <= (workflowOffsetTimeout + (workflowOffsetTimeout / 3)));
    }

    private WorkflowModel getWorkflowModel() {
        return WorkflowModel.builder()
                .appId(APP_ID)
                .name(WORKFLOW_NAME)
                .id(WORKFLOW_ID)
                .status(TaskModel.Status.IN_PROGRESS)
                .tasks(List.of(getTaskModel()))
                .build();
    }

    private TaskModel getTaskModel() {
        return TaskModel.builder()
                .appId(APP_ID)
                .name(WORKFLOW_NAME)
                .id(UUID.randomUUID().toString())
                .status(TaskModel.Status.IN_PROGRESS)
                .taskType(TASK_TYPE)
                .taskDefinition(Optional.of(getTaskDef()))
                .build();
    }

    private TaskDef getTaskDef() {
        return TaskDef.builder()
                .name(TASK_DEF_NAME)
                .taskType(TASK_TYPE)
                .timeoutSeconds(10)
                .build();
    }
}