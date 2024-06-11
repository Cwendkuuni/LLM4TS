import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.metadata.tasks.TaskDef;
import com.netflix.conductor.core.metadata.tasks.TaskType;
import com.netflix.conductor.core.metadata.tasks.TaskModel;
import com.netflix.conductor.core.metadata.workflow.WorkflowDef;
import com.netflix.conductor.core.metadata.workflow.WorkflowModel;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.common.config.TestConductorConfig;
import com.netflix.conductor.common.utils.Utils;
import com.netflix.conductor.model.TaskEvent;
import com.netflix.conductor.model.TaskModel.Status;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WorkflowSweeperTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowSweeperTest.class);

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private ConductorProperties properties;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(workflowSweeper, "sweeperExecutorName", "testExecutor");
        when(properties.getAppId()).thenReturn("testApp");
    }

    @After
    public void tearDown() {
        TestConductorConfig.clearThreadContext();
    }

    @Test
    public void testWorkflowSweeperInitialization() {
        assertNotNull(workflowSweeper);
        assertNotNull(workflowSweeper.getProperties());
        assertNotNull(workflowSweeper.getQueueDAO());
        assertNotNull(workflowSweeper.getWorkflowExecutor());
        assertNotNull(workflowSweeper.getWorkflowRepairService());
    }

    @Test
    public void testSweepAsync() throws ExecutionException, InterruptedException {
        String workflowId = "testWorkflow";

        when(queueDAO.getUnackTimeout(DECIDER_QUEUE, workflowId)).thenReturn(100L);

        @SuppressWarnings("unchecked")
        ListenableFuture<Void> future = (ListenableFuture<Void>) spy(workflowSweeper).sweepAsync(workflowId);

        verify(workflowSweeper, times(1)).sweep(workflowId);

        ListenableFutureCallback<Void> callback = new ListenableFutureCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                LOGGER.info("sweepAsync completed successfully");
            }

            @Override
            public void onFailure(Throwable ex) {
                LOGGER.info("sweepAsync failed", ex);
            }
        };

        future.addCallback(callback);
        future.get();
    }

    @Test
    public void testSweepNotFoundException() {
        String workflowId = "testWorkflow";

        when(queueDAO.getUnackTimeout(DECIDER_QUEUE, workflowId)).thenReturn(100L);
        doThrow(new NotFoundException("Workflow not found")).when(workflowExecutor).decide(workflowId);

        @SuppressWarnings("unchecked")
        ListenableFuture<Void> future = (ListenableFuture<Void>) spy(workflowSweeper).sweepAsync(workflowId);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
        verify(workflowExecutor, times(1)).decide(workflowId);
        verify(workflowSweeper, never()).unack(any(WorkflowModel.class), anyLong());
        verify(queueDAO, never()).setUnackTimeout(anyString(), anyString(), anyLong());

        future.get();
    }

    @Test
    public void testSweepUnack() throws InterruptedException, ExecutionException {
        String workflowId = "testWorkflow";

        when(queueDAO.getUnackTimeout(DECIDER_QUEUE, workflowId)).thenReturn(100L);
        when(workflowExecutor.decide(workflowId)).thenReturn(createWorkflowModel());
        when(queueDAO.remove(DECIDER_QUEUE, workflowId)).thenReturn(true);

        @SuppressWarnings("unchecked")
        ListenableFuture<Void> future = (ListenableFuture<Void>) spy(workflowSweeper).sweepAsync(workflowId);

        verify(queueDAO, times(1)).getUnackTimeout(DECIDER_QUEUE, workflowId);
        verify(workflowExecutor, times(1)).decide(workflowId);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
        verify(queueDAO, never()).setUnackTimeout(anyString(), anyString(), anyLong());

        future.get();
    }

    @Test
    public void testSweepUnackWithJitter() throws InterruptedException, ExecutionException {
        String workflowId = "testWorkflow";

        when(queueDAO.getUnackTimeout(DECIDER_QUEUE, workflowId)).thenReturn(100L);
        when(workflowExecutor.decide(workflowId)).thenReturn(createWorkflowModel());
        when(queueDAO.remove(DECIDER_QUEUE, workflowId)).thenReturn(false);

        @SuppressWarnings("unchecked")
        ListenableFuture<Void> future = (ListenableFuture<Void>) spy(workflowSweeper).sweepAsync(workflowId);

        ArgumentCaptor<Long> argument = ArgumentCaptor.forClass(Long.class);
        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowId, argument.capture());

        long jitter = argument.getValue() - 100L;
        assertTrue((2 / 3D) * 100D <= jitter && jitter <= (4 / 3D) * 100D);

        future.get();
    }

    @Test
    public void testUnack() throws InterruptedException, ExecutionException {
        WorkflowModel workflowModel = createWorkflowModel();

        workflowSweeper.unack(workflowModel, 100L);

        ArgumentCaptor<Long> argument = ArgumentCaptor.forClass(Long.class);
        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowModel.getWorkflowId(), argument.capture());
        assertEquals(100L, (long) argument.getValue());
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long original = 45L;
        long result = workflowSweeper.workflowOffsetWithJitter(original);

        long range = original / 3;
        long jitter = result - original;
        assertTrue((-range) <= jitter && jitter <= range);
    }

    private WorkflowModel createWorkflowModel() {
        return new WorkflowModel("testWorkflow", "testDefinition", Status.IN_PROGRESS, Instant.now().toEpochMilli(),
                new WorkflowDef(), null, null, null);
    }

}