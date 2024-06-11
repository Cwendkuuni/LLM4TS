import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.events.EventDefinition;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.core.utils.TestUtils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.TaskDAO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.scheduling.annotation.AsyncResult;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private TaskDAO taskDAO;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    private WorkflowModel workflowModel;

    @Before
    public void setup() {
        workflowModel = TestUtils.createWorkflowModel();
    }

    @Test
    public void testWorkflowSweeperInitialization() {
        assertNotNull(workflowSweeper);
    }

    @Test
    public void testSweepAsync() throws ExecutionException, InterruptedException {
        String workflowId = "testWorkflowId";
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        completableFuture.complete(null);
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);
        doAnswer(invocation -> completableFuture).when(workflowSweeper).sweepAsync(workflowId);

        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);
        result.get();

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    @Test
    public void testSweepAsyncWithNotFoundException() throws ExecutionException, InterruptedException {
        String workflowId = "testWorkflowId";
        when(workflowExecutor.decide(workflowId)).thenThrow(new NotFoundException("Not found"));

        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);
        result.get();

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    @Test
    public void testSweepAsyncWithException() throws ExecutionException, InterruptedException {
        String workflowId = "testWorkflowId";
        when(workflowExecutor.decide(workflowId)).thenThrow(new RuntimeException("Error"));

        CompletableFuture<Void> result = workflowSweeper.sweepAsync(workflowId);
        result.get();

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    @Test
    public void testSweep() {
        String workflowId = "testWorkflowId";
        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);
        doNothing().when(workflowSweeper).sweep(workflowId);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
        verify(workflowExecutor, times(1)).decide(workflowId);
        verify(workflowSweeper, times(1)).sweep(workflowId);
    }

    @Test
    public void testSweepWithNotFoundException() {
        String workflowId = "testWorkflowId";
        when(workflowExecutor.decide(workflowId)).thenThrow(new NotFoundException("Not found"));

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    @Test
    public void testSweepWithException() {
        String workflowId = "testWorkflowId";
        when(workflowExecutor.decide(workflowId)).thenThrow(new RuntimeException("Error"));

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
        verify(workflowExecutor, times(1)).decide(workflowId);
    }

    @Test
    public void testUnack() {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = TestUtils.createWorkflowModel();

        doNothing().when(queueDAO).setUnackTimeout(DECIDER_QUEUE, workflowId, anyLong());

        workflowSweeper.unack(workflowModel, 10L);

        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowId, 10L);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;
        long jitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        assertTrue(jitter >= -(workflowOffsetTimeout / 3) && jitter <= (workflowOffsetTimeout / 3));
    }
}