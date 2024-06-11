import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.model.TaskModel;
import com.netflix.conductor.common.model.WorkflowModel;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel.Status;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class WorkflowSweeperTest {

    private static final String WORKFLOW_ID = "workflow-123";
    private static final String WORKFLOW_NAME = "test-workflow";

    private WorkflowSweeper workflowSweeper;
    private WorkflowExecutor workflowExecutor;
    private QueueDAO queueDAO;
    private WorkflowRepairService workflowRepairService;
    private ConductorProperties conductorProperties;

    @Before
    public void setup() {
        workflowExecutor = mock(WorkflowExecutor.class);
        queueDAO = mock(QueueDAO.class);
        workflowRepairService = mock(WorkflowRepairService.class);
        conductorProperties = mock(ConductorProperties.class);

        when(conductorProperties.getAppId()).thenReturn("test-app");
        when(conductorProperties.getWorkflowOffsetTimeout()).thenReturn(Optional.of(10L));
        when(conductorProperties.getSchedulerConfiguration().getSweeperExecutorName()).thenReturn("test-sweeper");

        workflowSweeper = new WorkflowSweeper(
                workflowExecutor,
                Optional.ofNullable(workflowRepairService),
                conductorProperties,
                queueDAO
        );
    }

    @Test
    public void testSweepAsync() throws ExecutionException, InterruptedException {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.initialize();

        workflowSweeper.setTaskExecutor(executor);

        ListenableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);

        future.get();

        verify(workflowExecutor, times(1)).decide(WORKFLOW_ID);
    }

    @Test
    public void testSweep() throws Exception {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(Status.FAILED);
        workflowModel.setWorkflowId(WORKFLOW_ID);
        workflowModel.setWorkflowName(WORKFLOW_NAME);

        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(workflowModel);

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO, times(1)).remove(Utils.DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweepNotFoundException() throws Exception {
        doThrow(new NotFoundException("Workflow not found")).when(workflowExecutor).decide(WORKFLOW_ID);

        workflowSweeper.sweep(WORKFLOW_ID);

        verify(queueDAO, times(1)).remove(Utils.DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testUnack() throws Exception {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(Status.IN_PROGRESS);
        workflowModel.setWorkflowId(WORKFLOW_ID);
        workflowModel.setWorkflowName(WORKFLOW_NAME);
        workflowModel.setTasks(
                TaskModel.list(
                        TaskModel.builder()
                                .status(Status.SCHEDULED)
                                .taskType(TaskType.TASK_TYPE_SCHEDULED)
                                .build()
                )
        );

        when(queueDAO.setUnackTimeout(Utils.DECIDER_QUEUE, WORKFLOW_ID, 120000L)).thenReturn(true);

        workflowSweeper.unack(workflowModel, 120);

        verify(queueDAO, times(1)).setUnackTimeout(Utils.DECIDER_QUEUE, WORKFLOW_ID, 120000L);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(result >= (30 + workflowOffsetTimeout) && result <= (60 + workflowOffsetTimeout));
    }
}