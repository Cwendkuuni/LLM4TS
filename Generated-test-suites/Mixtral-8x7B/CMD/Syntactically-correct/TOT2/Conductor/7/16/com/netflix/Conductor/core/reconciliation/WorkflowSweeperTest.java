import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.core.utils.TestUtils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestUtils.TestConfig.class})
public class WorkflowSweeperTest {

    @Autowired
    @Qualifier("queueDAO")
    private QueueDAO queueDAO;

    @Autowired
    private WorkflowExecutor workflowExecutor;

    private WorkflowSweeper workflowSweeper;
    private WorkflowRepairService workflowRepairService;
    private ConductorProperties conductorProperties;

    @Before
    public void setup() {
        conductorProperties = new ConductorProperties();
        workflowRepairService = Mockito.mock(WorkflowRepairService.class);
        workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), conductorProperties, queueDAO);
        workflowSweeper = spy(workflowSweeper);

        ReflectionTestUtils.setField(workflowSweeper, "sweeperExecutorName", "testSweeperExecutorName");
    }

    @After
    public void teardown() {
        queueDAO.clear();
    }

    @Test
    public void sweepAsync_test() throws Exception {
        String workflowId = "testWorkflowId";

        doNothing().when(workflowSweeper).sweep(workflowId);

        workflowSweeper.sweepAsync(workflowId).get();

        verify(workflowSweeper, times(1)).sweep(workflowId);
    }

    @Test
    public void sweep_test() throws Exception {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = createWorkflowModel(workflowId);
        queueDAO.add(DECIDER_QUEUE, workflowId, workflowModel);

        when(workflowExecutor.decide(workflowId)).thenReturn(workflowModel);

        workflowSweeper.sweep(workflowId);

        verify(workflowExecutor, times(1)).decide(workflowId);
        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void sweep_workflowNotFound_test() throws Exception {
        String workflowId = "testWorkflowId";

        when(workflowExecutor.decide(workflowId)).thenThrow(new NotFoundException("Not found"));

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void unack_test() throws Exception {
        String workflowId = "testWorkflowId";
        WorkflowModel workflowModel = createWorkflowModel(workflowId);
        queueDAO.add(DECIDER_QUEUE, workflowId, workflowModel);

        ReflectionTestUtils.setField(workflowSweeper, "workflowOffsetTimeout", 30);
        long delayedTime = workflowSweeper.workflowOffsetWithJitter(30);

        workflowSweeper.unack(workflowModel, delayedTime);

        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowId, (int) delayedTime * 1000);
    }

    @Test
    public void workflowOffsetWithJitter_test() throws Exception {
        long workflowOffsetTimeout = 30;
        long jitteredTime = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        Assert.assertTrue("Jittered time should be between " + (workflowOffsetTimeout / 3) * 2 + " and " + (workflowOffsetTimeout / 3) * 4,
                jitteredTime >= (workflowOffsetTimeout / 3) * 2 && jitteredTime <= (workflowOffsetTimeout / 3) * 4);
    }

    private WorkflowModel createWorkflowModel(String workflowId) {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(Status.IN_PROGRESS);
        workflowModel.setWorkflowId(workflowId);

        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskType(TaskType.TASK_TYPE_SCHEDULED);
        workflowModel.addTask(taskModel);

        return workflowModel;
    }
}