import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.model.tasks.TaskModel;
import com.netflix.conductor.common.model.workflow.WorkflowModel;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.events.EventQueue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WorkflowSweeperTest {

    private static final String WORKFLOW_ID = "workflow-1";
    private static final String WORKFLOW_NAME = "workflow-name";
    private static final String TASK_DEF_NAME = "test-task-def";
    private static final String TASK_ID = "task-1";
    private static final String WAIT_TASK_TYPE = TaskType.TASK_TYPE_WAIT.name();
    private static final String HUMAN_TASK_TYPE = TaskType.TASK_TYPE_HUMAN.name();
    private static final String EVENT_QUEUE = EventQueue.QUEUE_NAME;
    private static final String APP_ID = "test-app";
    private static final String DECIDER_QUEUE = "decider-queue";

    @Mock
    private ConductorProperties properties;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    private WorkflowSweeper workflowSweeper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WorkflowContext workflowContext = new WorkflowContext(APP_ID);
        WorkflowContext.set(workflowContext);

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.initialize();

        workflowSweeper = new WorkflowSweeper(
                workflowExecutor,
                Optional.of(workflowRepairService),
                properties,
                queueDAO
        ) {
            @Override
            public ListenableFuture<Void> sweepAsync(String workflowId) {
                return new AsyncResult<>(super.sweepAsync(workflowId).join());
            }
        };
        workflowSweeper.setAsyncExecutor(executor);
    }

    @Test
    public void testSweepAsync() throws Exception {
        Mockito.when(queueDAO.remove(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(true);

        ListenableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);
        future.get();

        Mockito.verify(queueDAO, Mockito.times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweepNotFoundException() throws Exception {
        Mockito.when(queueDAO.remove(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(false);

        ListenableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);
        future.get();

        Mockito.verify(queueDAO, Mockito.times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(WORKFLOW_ID);
        workflowModel.setWorkflowName(WORKFLOW_NAME);

        TaskModel taskModel1 = new TaskModel();
        taskModel1.setTaskId(TASK_ID);
        taskModel1.setStatus(Status.IN_PROGRESS);
        taskModel1.setTaskType(WAIT_TASK_TYPE);
        taskModel1.setWaitTimeout(45);

        List<TaskModel> tasks = new ArrayList<>();
        tasks.add(taskModel1);
        workflowModel.setTasks(tasks);

        Mockito.when(properties.getWorkflowOffsetTimeout()).thenReturn(Optional.of(45));

        workflowSweeper.unack(workflowModel, 45);

        Mockito.verify(queueDAO, Mockito.times(1))
                .setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, 60000);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;
        long jitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        long lowerBound = workflowOffsetTimeout - (workflowOffsetTimeout / 3);
        long upperBound = workflowOffsetTimeout + (workflowOffsetTimeout / 3);

        Assertions.assertTrue(jitter >= lowerBound && jitter <= upperBound);
    }
}