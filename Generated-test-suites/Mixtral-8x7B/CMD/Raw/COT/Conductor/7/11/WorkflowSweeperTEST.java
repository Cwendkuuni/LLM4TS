import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.metadata.workflow.WorkflowDefinition;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.runners.RunWithConductor;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.events.EventProcessor;
import com.netflix.conductor.core.events.ListenerProcessor;
import com.netflix.conductor.core.execution.tasks.Task;
import com.netflix.conductor.core.execution.tasks.TaskRepository;
import com.netflix.conductor.core.exception.ConductorException;
import com.netflix.conductor.core.utils.ThreadNameUtil;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.TaskDAO;
import com.netflix.conductor.dao.TaskRunnerDAO;
import com.netflix.conductor.dao.WorkflowDAO;
import com.netflix.conductor.dao.MetadataDAO;
import com.netflix.conductor.metrics.Monitors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Instant;
import java.util.Optional;
import java.util.Random;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WorkflowSweeperTest {

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private ConductorProperties properties;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @BeforeEach
    public void setUp() {
        Monitors.reset();
    }

    @Test
    public void sweepAsyncTest() throws Exception {
        String workflowId = "test_workflow_1";
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);
        Assertions.assertNotNull(future);
        future.get();
    }

    @Test
    public void sweepTest() throws NotFoundException {
        String workflowId = "test_workflow_2";
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(Status.COMPLETED);

        WorkflowContext workflowContext = new WorkflowContext(properties.getAppId());
        WorkflowContext.set(workflowContext);

        when(queueDAO.remove(DECIDER_QUEUE, workflowId)).thenReturn(true);
        when(workflowExecutor.decide(workflowId)).thenReturn(workflow);

        workflowSweeper.sweep(workflowId);

        Mockito.verify(queueDAO, Mockito.times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void sweepNotFoundExceptionTest() throws NotFoundException {
        String workflowId = "test_workflow_3";

        when(queueDAO.remove(DECIDER_QUEUE, workflowId)).thenThrow(NotFoundException.class);

        workflowSweeper.sweep(workflowId);

        Mockito.verify(queueDAO, Mockito.times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void unackTest() throws ConductorException {
        String workflowId = "test_workflow_4";

        WorkflowModel workflowModel = new WorkflowModel();
        TaskModel taskModel1 = new TaskModel();
        taskModel1.setStatus(Status.IN_PROGRESS);
        TaskModel taskModel2 = new TaskModel();
        taskModel2.setStatus(Status.SCHEDULED);

        workflowModel.getTasks().add(taskModel1);
        workflowModel.getTasks().add(taskModel2);

        when(queueDAO.setUnackTimeout(DECIDER_QUEUE, workflowId, Mockito.anyLong())).thenReturn(true);

        workflowSweeper.unack(workflowModel, 30);

        Mockito.verify(queueDAO, Mockito.times(1)).setUnackTimeout(DECIDER_QUEUE, workflowId, 31);
    }

    @Test
    public void workflowOffsetWithJitterTest() {
        long workflowOffsetTimeout = 45;
        long jitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        Assertions.assertTrue(jitter >= (workflowOffsetTimeout / 3) * -1 && jitter <= (workflowOffsetTimeout / 3) * 2);
    }
}