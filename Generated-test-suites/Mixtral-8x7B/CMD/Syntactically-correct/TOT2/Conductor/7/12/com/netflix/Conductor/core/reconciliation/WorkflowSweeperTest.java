import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel.WorkflowStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class WorkflowSweeperTest {

    @Autowired
    private WorkflowSweeper workflowSweeper;

    @MockBean
    private WorkflowExecutor workflowExecutor;

    @MockBean
    private QueueDAO queueDAO;

    @MockBean
    private WorkflowRepairService workflowRepairService;

    private WorkflowModel workflowModel;

    @BeforeEach
    public void setUp() {
        workflowModel = new WorkflowModel();
        workflowModel.setWorkflowId("workflow-id-1");
        workflowModel.setStatus(WorkflowStatus.RUNNING);
        workflowModel.setTasks(Collections.singletonList(new TaskModel()));
    }

    @Test
    public void sweepAsync_shouldCallSweepMethod() throws Exception {
        AsyncResult<Void> asyncResult = new AsyncResult<>(null);
        when(workflowExecutor.decide(any())).thenReturn(workflowModel);

        workflowSweeper.sweepAsync("workflow-id-1").get();

        verify(workflowExecutor, times(1)).decide(any());
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, "workflow-id-1");
    }

    @Test
    public void sweepAsync_shouldHandleNotFoundException() throws Exception {
        when(workflowExecutor.decide(any())).thenThrow(new NotFoundException("Workflow not found"));

        workflowSweeper.sweepAsync("workflow-id-1").get();

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, "workflow-id-1");
    }

    @Test
    public void sweep_shouldCallDecideMethod() throws Exception {
        when(workflowExecutor.decide(any())).thenReturn(workflowModel);

        workflowSweeper.sweep("workflow-id-1");

        verify(workflowExecutor, times(1)).decide(any());
    }

    @Test
    public void sweep_shouldHandleNotFoundException() throws Exception {
        when(workflowExecutor.decide(any())).thenThrow(new NotFoundException("Workflow not found"));

        workflowSweeper.sweep("workflow-id-1");

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, "workflow-id-1");
    }

    @Test
    public void unack_shouldSetUnackTimeoutWithCorrectValues() {
        ArgumentCaptor<String> workflowIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> timeoutCaptor = ArgumentCaptor.forClass(Long.class);

        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_WAIT);
        taskModel.setWaitTimeout(10);

        workflowModel.setTasks(Collections.singletonList(taskModel));

        workflowSweeper.unack(workflowModel, 60);

        verify(queueDAO, times(1)).setUnackTimeout(eq(DECIDER_QUEUE), workflowIdCaptor.capture(),
                timeoutCaptor.capture());

        Assertions.assertEquals("workflow-id-1", workflowIdCaptor.getValue());
        Assertions.assertEquals(11_000L, timeoutCaptor.getValue().longValue());
    }

    @Test
    public void workflowOffsetWithJitter_shouldReturnValueBetweenOneThirdAndTwoThirdsOfInput() {
        long input = 45;
        long result = workflowSweeper.workflowOffsetWithJitter(input);

        long lowerBound = (2 * input) / 3 - 1;
        long upperBound = (2 * input) / 3 + 1;

        Assertions.assertTrue(result >= lowerBound && result <= upperBound);
    }
}