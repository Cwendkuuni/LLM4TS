import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import com.netflix.conductor.model.TaskModel.Status;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class WorkflowSweeperTest {

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private ConductorProperties properties;

    @Mock
    private Monitors monitors;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    private WorkflowContext workflowContext;
    private WorkflowModel workflowModel;
    private TaskModel taskModel;
    private TaskDef taskDef;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        workflowContext = new WorkflowContext("test-app-id");
        ReflectionTestUtils.setField(workflowSweeper, "workflowContext", workflowContext);

        workflowModel = new WorkflowModel();
        workflowModel.setWorkflowDefinition(new WorkflowDef());
        taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        taskDef = new TaskDef();
        taskDef.setPollTimeoutSeconds(10);
    }

    @Test
    public void testSweepAsync() throws Exception {
        String workflowId = "test-workflow-id";

        Async async = Mockito.spy(Async.class);
        ReflectionTestUtils.setField(workflowSweeper, "async", async);

        when(queueDAO.remove(DECIDER_QUEUE, workflowId)).thenReturn(true);

        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);
        future.get();

        verify(async, times(1)).execute(ArgumentMatchers.any(Runnable.class));
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweep() throws Exception {
        String workflowId = "test-workflow-id";

        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);
        doNothing().when(queueDAO).remove(DECIDER_QUEUE, workflowId);

        workflowSweeper.sweep(workflowId);

        verify(workflowExecutor, times(1)).decide(workflowId);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweepNotFoundException() throws Exception {
        String workflowId = "test-workflow-id";

        when(workflowExecutor.decide(workflowId)).thenThrow(new NotFoundException("Test"));
        doNothing().when(queueDAO).remove(DECIDER_QUEUE, workflowId);

        workflowSweeper.sweep(workflowId);

        verify(workflowExecutor, times(1)).decide(workflowId);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweepException() throws Exception {
        String workflowId = "test-workflow-id";

        when(workflowExecutor.decide(workflowId)).thenThrow(new RuntimeException("Test"));
        doNothing().when(queueDAO).remove(DECIDER_QUEUE, workflowId);

        workflowSweeper.sweep(workflowId);

        verify(workflowExecutor, times(1)).decide(workflowId);
        verify(queueDAO, never()).remove(DECIDER_QUEUE, workflowId);
        verify(monitors, times(1)).error(WorkflowSweeper.class.getSimpleName(), "sweep");
    }

    @Test
    public void testUnack() throws Exception {
        String workflowId = "test-workflow-id";
        long workflowOffsetTimeout = 60;

        workflowModel.setTasks(new TaskModel[]{taskModel});
        taskModel.setTaskType(TaskType.TASK_TYPE_WAIT);

        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        when(queueDAO.setUnackTimeout(DECIDER_QUEUE, workflowId, longArgumentCaptor.capture()))
                .thenReturn(true);

        workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

        assertEquals(workflowOffsetTimeout + taskModel.getWaitTimeout() + 1,
                longArgumentCaptor.getValue().longValue(), 0);
    }

    @Test
    public void testWorkflowOffsetWithJitter() throws Exception {
        long workflowOffsetTimeout = 60;

        long jitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(jitter >= workflowOffsetTimeout * 2 / 3 && jitter <= workflowOffsetTimeout * 4 / 3);
    }

    @After
    public void tearDown() {
        MockitoAnnotations.reset(workflowExecutor, queueDAO, workflowRepairService, properties,
                monitors);
    }
}