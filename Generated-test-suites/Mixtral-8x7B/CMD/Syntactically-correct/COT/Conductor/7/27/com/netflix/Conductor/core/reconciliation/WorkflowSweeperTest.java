import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.metadata.workflow.WorkflowDefinition;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.metrics.Monitors;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.test.util.ReflectionTestUtils;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WorkflowSweeperTest {

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private ConductorProperties properties;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private Monitors monitors;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(workflowSweeper, "className", WorkflowSweeper.class.getSimpleName());
    }

    @After
    public void tearDown() {
        Mockito.reset(workflowExecutor, workflowRepairService, properties, queueDAO, monitors);
    }

    @Test
    public void testSweepAsync() throws Exception {
        // Given
        String workflowId = "test-workflow-id";
        when(queueDAO.remove(DECIDER_QUEUE, workflowId)).thenReturn(true);

        // When
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);

        // Then
        future.get();
        verify(workflowExecutor, times(1)).decide(workflowId);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweepAsyncException() throws Exception {
        // Given
        String workflowId = "test-workflow-id";
        when(queueDAO.remove(DECIDER_QUEUE, workflowId)).thenReturn(true);
        doThrow(new Exception("Test exception")).when(workflowExecutor).decide(workflowId);

        // When
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);

        // Then
        future.get();
        verify(workflowExecutor, times(1)).decide(workflowId);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
        verify(monitors, times(1)).error(WorkflowSweeper.class.getSimpleName(), "sweepAsync");
    }

    @Test
    public void testSweep() throws Exception {
        // Given
        String workflowId = "test-workflow-id";
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(Status.COMPLETED);

        WorkflowContext workflowContext = new WorkflowContext(properties.getAppId());
        WorkflowContext.set(workflowContext);

        when(workflowExecutor.decide(workflowId)).thenReturn(workflow);
        when(queueDAO.remove(DECIDER_QUEUE, workflowId)).thenReturn(true);

        // When
        workflowSweeper.sweep(workflowId);

        // Then
        verify(workflowExecutor, times(1)).decide(workflowId);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweepNotFoundException() throws Exception {
        // Given
        String workflowId = "test-workflow-id";

        WorkflowContext workflowContext = new WorkflowContext(properties.getAppId());
        WorkflowContext.set(workflowContext);

        when(workflowExecutor.decide(workflowId)).thenThrow(new NotFoundException("Test exception"));

        // When
        workflowSweeper.sweep(workflowId);

        // Then
        verify(workflowExecutor, times(1)).decide(workflowId);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
        verify(monitors, times(1)).error(WorkflowSweeper.class.getSimpleName(), "sweep");
    }

    @Test
    public void testSweepWorkflowNull() throws Exception {
        // Given
        String workflowId = "test-workflow-id";

        WorkflowContext workflowContext = new WorkflowContext(properties.getAppId());
        WorkflowContext.set(workflowContext);

        when(workflowExecutor.decide(workflowId)).thenReturn(null);

        // When
        workflowSweeper.sweep(workflowId);

        // Then
        verify(workflowExecutor, times(1)).decide(workflowId);
        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowId, anyLong());
    }

    @Test
    public void testSweepUnack() throws Exception {
        // Given
        String workflowId = "test-workflow-id";

        WorkflowContext workflowContext = new WorkflowContext(properties.getAppId());
        WorkflowContext.set(workflowContext);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowDefinition(new WorkflowDefinition());
        workflowModel.setTasks(
                TaskModel.builder().status(Status.IN_PROGRESS).taskType(TaskType.TASK_TYPE_WAIT).build()
                        .getTaskList()
        );

        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        // When
        workflowSweeper.sweep(workflowId);

        // Then
        verify(workflowExecutor, times(1)).decide(workflowId);
        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowId, anyLong());
    }

    @Test
    public void testSweepUnackScheduled() throws Exception {
        // Given
        String workflowId = "test-workflow-id";

        WorkflowContext workflowContext = new WorkflowContext(properties.getAppId());
        WorkflowContext.set(workflowContext);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowDefinition(new WorkflowDefinition());
        workflowModel.setTasks(
                TaskModel.builder().status(Status.SCHEDULED).build()
                        .getTaskList()
        );

        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        // When
        workflowSweeper.sweep(workflowId);

        // Then
        verify(workflowExecutor, times(1)).decide(workflowId);
        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowId, anyLong());
    }

    @Test
    public void testUnack() throws Exception {
        // Given
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowDefinition(new WorkflowDefinition());

        when(queueDAO.setUnackTimeout(DECIDER_QUEUE, ArgumentMatchers.anyString(), anyLong()))
                .thenReturn(true);

        // When
        workflowSweeper.unack(workflowModel, 5);

        // Then
        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, ArgumentMatchers.anyString(), anyLong());
    }

    @Test
    public void testWorkflowOffsetWithJitter() throws Exception {
        // When
        long result = workflowSweeper.workflowOffsetWithJitter(45);

        // Then
        long range = 45 / 3;
        long jitter = new Random().nextInt((int) (2 * range + 1)) - range;
        Assert.assertTrue(result >= (45 + jitter) && result <= (45 - jitter));
    }
}