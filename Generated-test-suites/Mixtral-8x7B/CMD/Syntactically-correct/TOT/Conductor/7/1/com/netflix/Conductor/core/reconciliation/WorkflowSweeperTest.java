import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.model.WorkflowModel;
import com.netflix.conductor.common.model.workflow.Task;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.events.WorkflowEventRepository;
import com.netflix.conductor.core.reconciliation.models.WorkflowEvent;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.TaskDAO;
import com.netflix.conductor.dao.WorkflowDAO;
import com.netflix.conductor.service.WorkflowService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.AsyncResult;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WorkflowSweeperTest {

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowEventRepository workflowEventRepository;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private TaskDAO taskDAO;

    @Mock
    private WorkflowDAO workflowDAO;

    @Mock
    private WorkflowService workflowService;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        workflowSweeper = spy(workflowSweeper);
    }

    @Test
    public void sweepAsyncTest() throws ExecutionException, InterruptedException {
        String workflowId = "test-workflow-id";
        CompletableFuture<Void> future = new CompletableFuture<>();
        when(workflowExecutor.decide(workflowId)).thenReturn(mock(WorkflowModel.class));
        doReturn(future).when(workflowSweeper).sweep(workflowId);

        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);

        assertTrue(result.isDone());
        result.get();
        verify(workflowSweeper, times(1)).sweep(workflowId);
    }

    @Test
    public void sweepTest() {
        String workflowId = "test-workflow-id";

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);
        workflowModel.setStatus(WorkflowModel.Status.COMPLETED);

        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void sweepNotFoundExceptionTest() {
        String workflowId = "test-workflow-id";

        when(workflowExecutor.decide(workflowId)).thenThrow(new NotFoundException("Test"));

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void unackTest() {
        String workflowId = "test-workflow-id";
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId(workflowId);

        Task task = new Task();
        task.setStatus(Task.Status.IN_PROGRESS);
        task.setTaskType(TaskType.TASK_TYPE_SCHEDULED);

        workflowModel.setTasks(Arrays.asList(task));

        when(queueDAO.setUnackTimeout(anyString(), anyString(), anyLong()))
                .thenReturn(true);
        workflowSweeper.unack(workflowModel, 45);

        verify(queueDAO, times(1)).setUnackTimeout(
                DECIDER_QUEUE, workflowId, 46 * 1000);
    }

    @Test
    public void workflowOffsetWithJitterTest() {
        long workflowOffsetTimeout = 45;
        long range = workflowOffsetTimeout / 3;
        long jitter = new Random().nextInt((int) (2 * range + 1)) - range;
        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(result >= (workflowOffsetTimeout - range)
                && result <= (workflowOffsetTimeout + range));
    }
}