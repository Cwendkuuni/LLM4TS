import com.netflix.conductor.core.config.TestConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.core.utils.TestUtils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class WorkflowSweeperTest {

    private static final String WORKFLOW_ID = "workflow-123";
    private static final String WORKFLOW_NAME = "test-workflow";

    private WorkflowSweeper workflowSweeper;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private TestConductorProperties properties;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(properties.getAppId()).thenReturn("test-app");

        when(queueDAO.getUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID))
                .thenReturn(1000L)
                .thenThrow(new RuntimeException("Test exception"));

        doNothing().when(queueDAO).remove(DECIDER_QUEUE, WORKFLOW_ID);

        workflowSweeper =
                new WorkflowSweeper(
                        workflowExecutor,
                        Optional.of(workflowRepairService),
                        properties,
                        queueDAO);
    }

    @Test
    public void testSweepAsync() throws Exception {
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);
        future.get();

        // Verify that sweep method was called
        // ...
    }

    @Test
    public void testSweepNotFoundException() throws Exception {
        when(queueDAO.getUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(null);

        workflowSweeper.sweep(WORKFLOW_ID);

        // Verify that queueDAO.remove was called
        // ...
    }

    @Test(expected = RuntimeException.class)
    public void testSweepException() throws Exception {
        when(queueDAO.getUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID)).thenThrow(new RuntimeException("Test exception"));

        workflowSweeper.sweep(WORKFLOW_ID);

        // Verify that no exception is expected, mockito will check for the exception type
    }

    @Test
    public void testUnack() throws Exception {
        WorkflowModel workflowModel = TestUtils.createWorkflowModel(WORKFLOW_ID, WORKFLOW_NAME);
        workflowModel.setStatus(Status.IN_PROGRESS);

        when(queueDAO.getUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID)).thenReturn(1000L);

        workflowSweeper.unack(workflowModel, 30);

        // Verify that queueDAO.setUnackTimeout was called with correct parameters
        // ...
    }

    @Test
    public void testWorkflowOffsetWithJitter() throws Exception {
        long workflowOffsetTimeout = 45;
        long actual = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);
        long range = workflowOffsetTimeout / 3;
        long jitter = new Random().nextInt((int) (2 * range + 1)) - range;
        long expected = workflowOffsetTimeout + jitter;

        assertEquals(expected, actual);
    }
}