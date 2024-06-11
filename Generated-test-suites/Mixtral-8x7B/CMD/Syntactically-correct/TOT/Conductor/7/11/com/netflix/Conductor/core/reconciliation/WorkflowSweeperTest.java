import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.metadata.tasks.TaskModel;
import com.netflix.conductor.core.metadata.tasks.TaskModel.Status;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.QueueTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import static org.mockito.Mockito.*;

@EnableAsync
public class WorkflowSweeperTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowSweeperTest.class);

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private ConductorProperties properties;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        ReflectionTestUtils.setField(workflowSweeper, "className", "WorkflowSweeperTest");

        Executor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.initialize();
        ReflectionTestUtils.setField(workflowSweeper, "sweeperExecutor", executor);
    }

    @Test
    public void sweepAsyncTest() throws ExecutionException, InterruptedException {
        String workflowId = "testWorkflowId";

        when(queueDAO.getUnackTimeout(DECIDER_QUEUE, workflowId)).thenReturn(1000L);

        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);

        future.get();

        verify(queueDAO, times(1)).getUnackTimeout(DECIDER_QUEUE, workflowId);
        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowId, 1000L);

        Mockito.verifyNoMoreInteractions(queueDAO);
    }

    @Test
    public void sweepTest() throws ExecutionException, InterruptedException {
        String workflowId = "testWorkflowId";
        String workflowDefinitionName = "testWorkflowDefinition";
        String taskId = "testTaskId";
        String taskType = TaskType.TASK_TYPE_SCHEDULED.name();
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setTaskType(TaskType.TASK_TYPE_SCHEDULED);
        taskModel.setStatus(Status.SCHEDULED);
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setWorkflowName(workflowDefinitionName);
        workflowModel.setTasks(new TaskModel[]{taskModel});

        when(properties.getAppId()).thenReturn("testAppId");
        when(queueDAO.getUnackTimeout(DECIDER_QUEUE, workflowId)).thenReturn(1000L);
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).getUnackTimeout(DECIDER_QUEUE, workflowId);
        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowId, 1000L);
        verify(workflowExecutor, times(1)).decide(workflowId);

        Mockito.verifyNoMoreInteractions(queueDAO, workflowExecutor);
    }

    @Test
    public void unackTest() {
        String workflowId = "testWorkflowId";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("testTaskId");
        taskModel.setStatus(Status.SCHEDULED);
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setTasks(new TaskModel[]{taskModel});

        when(queueDAO.setUnackTimeout(DECIDER_QUEUE, workflowId, 1000L)).thenReturn(null);

        workflowSweeper.unack(workflowModel, 10);

        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowId, 1000L);

        Mockito.verifyNoMoreInteractions(queueDAO);
    }

    @Test
    public void workflowOffsetWithJitterTest() {
        long workflowOffsetTimeout = 45;
        long range = workflowOffsetTimeout / 3;
        long jitter = new Random().nextInt((int) (2 * range + 1)) - range;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        Assert.assertTrue(result >= (workflowOffsetTimeout - range) && result <= (workflowOffsetTimeout + range));
    }

    @Test
    public void givenWorkflowNotExists_whenSweep_thenRemoveFromDeciderQueue() {
        String workflowId = "nonexistent-workflow";

        doThrow(new NotFoundException("Workflow not found")).when(workflowExecutor).decide(workflowId);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void givenTerminalWorkflow_whenSweep_thenRemoveFromDeciderQueue() {
        String workflowId = "test-terminal-workflow";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setStatus(Status.COMPLETED);

        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void givenNonTerminalWorkflow_whenSweep_thenSetUnackTimeoutWithPostponeDuration() {
        String workflowId = "test-non-terminal-workflow";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setStatus(Status.IN_PROGRESS);

        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);
        when(queueDAO.setUnackTimeout(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyLong()))
                .thenReturn(null);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowId, ArgumentMatchers.anyLong());
    }

    @Test
    public void givenNullWorkflow_whenSweep_thenSetUnackTimeoutWithPostponeDuration() {
        String workflowId = "test-null-workflow";

        when(workflowExecutor.decide(workflowId)).thenReturn(null);
        when(queueDAO.setUnackTimeout(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyLong()))
                .thenReturn(null);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowId, ArgumentMatchers.anyLong());
    }

    @Test
    public void givenWorkflowWithInProgressTasks_whenUnack_thenSetUnackTimeoutWithPostponeDuration() {
        String workflowId = "test-workflow-in-progress";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setStatus(Status.IN_PROGRESS);

        TaskModel taskModel1 = new TaskModel();
        taskModel1.setTaskId("test-task-1");
        taskModel1.setStatus(Status.IN_PROGRESS);
        TaskModel taskModel2 = new TaskModel();
        taskModel2.setTaskId("test-task-2");
        taskModel2.setStatus(Status.SCHEDULED);

        workflowModel.setTasks(new TaskModel[]{taskModel1, taskModel2});

        when(queueDAO.setUnackTimeout(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyLong()))
                .thenReturn(null);

        workflowSweeper.unack(workflowModel, 10);

        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowId, ArgumentMatchers.eq(11L));
    }

    @Test
    public void givenWorkflowWithScheduledTasks_whenUnack_thenSetUnackTimeoutWithPostponeDuration() {
        String workflowId = "test-workflow-scheduled";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setStatus(Status.IN_PROGRESS);

        TaskModel taskModel1 = new TaskModel();
        taskModel1.setTaskId("test-task-1");
        taskModel1.setStatus(Status.SCHEDULED);
        TaskModel taskModel2 = new TaskModel();
        taskModel2.setTaskId("test-task-2");
        taskModel2.setStatus(Status.SCHEDULED);

        workflowModel.setTasks(new TaskModel[]{taskModel1, taskModel2});

        when(queueDAO.setUnackTimeout(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyLong()))
                .thenReturn(null);

        workflowSweeper.unack(workflowModel, 10);

        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowId, ArgumentMatchers.eq(11L));
    }

    @Test
    public void givenWorkflowWithTimeoutSeconds_whenUnack_thenSetUnackTimeoutWithPostponeDuration() {
        String workflowId = "test-workflow-timeout-seconds";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setWorkflowDefinition(new WorkflowDefinitionTO("test-workflow-timeout-seconds", 20, "1.0", ""));
        workflowModel.setStatus(Status.IN_PROGRESS);

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("test-task");
        taskModel.setStatus(Status.SCHEDULED);

        workflowModel.setTasks(new TaskModel[]{taskModel});

        when(queueDAO.setUnackTimeout(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyLong()))
                .thenReturn(null);

        workflowSweeper.unack(workflowModel, 10);

        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowId, ArgumentMatchers.eq(21L));
    }

    @Test
    public void givenWorkflowWithTaskDefinition_whenUnack_thenSetUnackTimeoutWithPostponeDuration() {
        String workflowId = "test-workflow-task-definition";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setStatus(Status.IN_PROGRESS);

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("test-task");
        taskModel.setStatus(Status.SCHEDULED);
        taskModel.setTaskDefinition(new TaskDef("test-task-definition", "1.0", TaskType.TASK_TYPE_SCHEDULED, 10));

        workflowModel.setTasks(new TaskModel[]{taskModel});

        when(queueDAO.setUnackTimeout(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyLong()))
                .thenReturn(null);

        workflowSweeper.unack(workflowModel, 10);

        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowId, ArgumentMatchers.eq(11L));
    }

    @Test
    public void givenWorkflowWithWaitTimeout_whenUnack_thenSetUnackTimeoutWithPostponeDuration() {
        String workflowId = "test-workflow-wait-timeout";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setStatus(Status.IN_PROGRESS);

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("test-task");
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_WAIT.name());
        taskModel.setWaitTimeout(10);

        workflowModel.setTasks(new TaskModel[]{taskModel});

        when(queueDAO.setUnackTimeout(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyLong()))
                .thenReturn(null);

        workflowSweeper.unack(workflowModel, 10);

        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowId, ArgumentMatchers.eq(11L));
    }

    private class WorkflowRepairService {

        public void verifyAndRepairWorkflowTasks(String workflowId) {
            // Mock implementation
        }
    }

    private class WorkflowDefinitionTO {

        private String name;
        private int timeoutSeconds;
        private String version;
        private String description;

        public WorkflowDefinitionTO(String name, int timeoutSeconds, String version, String description) {
            this.name = name;
            this.timeoutSeconds = timeoutSeconds;
            this.version = version;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public int getTimeoutSeconds() {
            return timeoutSeconds;
        }
    }

    private class QueueTO {

        private String name;

        public QueueTO(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}