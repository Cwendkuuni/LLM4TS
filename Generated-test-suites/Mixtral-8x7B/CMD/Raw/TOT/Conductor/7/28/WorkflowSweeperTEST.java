import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.metadata.tasks.TaskModel;
import com.netflix.conductor.core.metadata.tasks.TaskModel.Status;
import com.netflix.conductor.core.metadata.workflow.WorkflowDef;
import com.netflix.conductor.core.metadata.workflow.WorkflowModel;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.TaskRunnerDAO;
import com.netflix.conductor.mock.MockWorkflowExecutor;
import com.netflix.conductor.mock.MockWorkflowRepairService;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@EnableAsync
@Component
public class WorkflowSweeperTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowSweeperTest.class);
    private WorkflowSweeper workflowSweeper;
    private QueueDAO queueDAO;
    private WorkflowExecutor workflowExecutor;
    private WorkflowRepairService workflowRepairService;
    private ConductorProperties properties;
    private TaskRunnerDAO taskRunnerDAO;

    @Before
    public void setup() {
        queueDAO = mock(QueueDAO.class);
        workflowExecutor = mock(MockWorkflowExecutor.class);
        workflowRepairService = mock(MockWorkflowRepairService.class);
        properties = new ConductorProperties();
        taskRunnerDAO = mock(TaskRunnerDAO.class);

        workflowSweeper =
                new WorkflowSweeper(
                        workflowExecutor,
                        Optional.of(workflowRepairService),
                        properties,
                        queueDAO,
                        taskRunnerDAO);
    }

    @After
    public void teardown() {}

    @Test
    public void sweepAsyncTest() throws Exception {
        String workflowId = "testWorkflow";

        when(queueDAO.get(DECIDER_QUEUE, workflowId))
                .thenReturn(
                        new TaskModel()
                                .setStatus(Status.IN_PROGRESS)
                                .setWorkflowId(workflowId)
                                .setTaskType(TaskType.TASK_TYPE_DECIDER));

        workflowSweeper.sweepAsync(workflowId);

        Mockito.verify(queueDAO, Mockito.times(1))
                .setUnackTimeout(DECIDER_QUEUE, workflowId, 60000);
    }

    @Test
    public void sweepAsyncNotFoundExceptionTest() throws Exception {
        String workflowId = "testWorkflow";

        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(null);

        workflowSweeper.sweepAsync(workflowId);

        Mockito.verify(queueDAO, Mockito.times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void sweepAsyncNonTerminalWorkflowTest() throws Exception {
        String workflowId = "testWorkflow";

        WorkflowModel workflowModel = new WorkflowModel().setStatus(Status.IN_PROGRESS);

        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        workflowSweeper.sweepAsync(workflowId);

        Mockito.verify(queueDAO, Mockito.times(1))
                .setUnackTimeout(DECIDER_QUEUE, workflowId, 60000);
    }

    @Test
    public void sweepAsyncTerminalWorkflowTest() throws Exception {
        String workflowId = "testWorkflow";

        WorkflowModel workflowModel = new WorkflowModel().setStatus(Status.COMPLETED);

        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        workflowSweeper.sweepAsync(workflowId);

        Mockito.verify(queueDAO, Mockito.times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void unackWorkflowModelNotNullTest() throws Exception {
        String workflowId = "testWorkflow";
        WorkflowModel workflowModel =
                new WorkflowModel()
                        .setWorkflowId(workflowId)
                        .setStatus(Status.IN_PROGRESS)
                        .setTasks(
                                Collections.singletonList(
                                        new TaskModel()
                                                .setStatus(Status.IN_PROGRESS)
                                                .setTaskType(TaskType.TASK_TYPE_DECIDER)));

        workflowSweeper.unack(workflowModel, 60);

        Mockito.verify(queueDAO, Mockito.times(1))
                .setUnackTimeout(DECIDER_QUEUE, workflowId, 60000);
    }

    @Test
    public void unackWorkflowModelScheduledTest() throws Exception {
        String workflowId = "testWorkflow";
        WorkflowModel workflowModel =
                new WorkflowModel()
                        .setWorkflowId(workflowId)
                        .setStatus(Status.SCHEDULED)
                        .setTasks(
                                Collections.singletonList(
                                        new TaskModel()
                                                .setStatus(Status.SCHEDULED)
                                                .setTaskType(TaskType.TASK_TYPE_DECIDER)));

        workflowSweeper.unack(workflowModel, 60);

        Mockito.verify(queueDAO, Mockito.times(1))
                .setUnackTimeout(DECIDER_QUEUE, workflowId, 60000);
    }

    @Test
    public void workflowOffsetWithJitterTest() throws Exception {
        long workflowOffsetTimeout = 45;

        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        // result should be between [30, 60]
        org.junit.Assert.assertTrue(result >= 30 && result <= 60);
    }
}