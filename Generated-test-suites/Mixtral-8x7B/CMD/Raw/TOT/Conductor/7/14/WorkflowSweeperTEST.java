import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.model.tasks.TaskModel;
import com.netflix.conductor.common.model.workflow.WorkflowModel;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.events.EventExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutorImpl;
import com.netflix.conductor.core.service.QueueService;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.TaskDAO;
import com.netflix.conductor.dao.WorkflowDAO;
import com.netflix.conductor.metrics.Monitors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.*;

public class WorkflowSweeperTest {

    private WorkflowSweeper workflowSweeper;
    private WorkflowExecutor workflowExecutor;
    private QueueDAO queueDAO;
    private TaskDAO taskDAO;
    private WorkflowDAO workflowDAO;
    private EventExecutor eventExecutor;
    private QueueService queueService;

    @BeforeEach
    public void setup() {
        workflowExecutor = mock(WorkflowExecutorImpl.class);
        queueDAO = mock(QueueDAO.class);
        taskDAO = mock(TaskDAO.class);
        workflowDAO = mock(WorkflowDAO.class);
        eventExecutor = mock(EventExecutor.class);
        queueService = mock(QueueService.class);

        Monitors monitors = mock(Monitors.class);

        ReflectionTestUtils.setField(workflowExecutor, "workflowDAO", workflowDAO);
        ReflectionTestUtils.setField(workflowExecutor, "taskDAO", taskDAO);
        ReflectionTestUtils.setField(workflowExecutor, "eventExecutor", eventExecutor);
        ReflectionTestUtils.setField(workflowExecutor, "queueService", queueService);

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.initialize();

        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.empty(), new ConductorProperties(), queueDAO);
        ReflectionTestUtils.setField(workflowSweeper, "taskExecutor", executor);
    }

    @Test
    public void sweepAsyncTest() throws ExecutionException, InterruptedException {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = createWorkflowModel(workflowId);

        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(workflowModel);
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);

        future.get();

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void sweepAsyncNotFoundExceptionTest() throws ExecutionException, InterruptedException {
        String workflowId = "testWorkflowId";

        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenThrow(new NotFoundException("Test exception"));

        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);

        future.get();

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void sweepAsyncExceptionTest() throws ExecutionException, InterruptedException {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = createWorkflowModel(workflowId);

        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(workflowModel);
        doThrow(new RuntimeException("Test exception")).when(workflowExecutor).decide(workflowId);

        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);

        future.get();

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void sweepTest() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = createWorkflowModel(workflowId);

        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(workflowModel);
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void sweepNotFoundExceptionTest() {
        String workflowId = "testWorkflowId";

        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenThrow(new NotFoundException("Test exception"));

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void sweepExceptionTest() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = createWorkflowModel(workflowId);

        when(queueDAO.get(DECIDER_QUEUE, workflowId)).thenReturn(workflowModel);
        doThrow(new RuntimeException("Test exception")).when(workflowExecutor).decide(workflowId);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void unackTest() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = createWorkflowModel(workflowId);

        when(queueDAO.getUnackTimeout(DECIDER_QUEUE, workflowId)).thenReturn(1000L);

        workflowSweeper.unack(workflowModel, 60L);

        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowId, 61000L);
    }

    @Test
    public void workflowOffsetWithJitterTest() {
        long input = 45;
        long result = workflowSweeper.workflowOffsetWithJitter(input);

        Assertions.assertTrue(result >= (input - (input / 3)) && result <= (input + (input / 3)));
    }

    private WorkflowModel createWorkflowModel(String workflowId) {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setWorkflowName("testWorkflowName");
        workflowModel.setStatus(Status.IN_PROGRESS);

        List<TaskModel> tasks = new ArrayList<>();
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("testTaskId");
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_SCHEDULED);
        tasks.add(taskModel);
        workflowModel.setTasks(tasks);

        return workflowModel;
    }
}