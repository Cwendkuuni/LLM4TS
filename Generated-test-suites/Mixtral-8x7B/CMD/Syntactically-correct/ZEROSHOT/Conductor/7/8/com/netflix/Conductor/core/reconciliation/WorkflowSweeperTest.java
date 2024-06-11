import com.netflix.conductor.core.reconciliation.WorkflowSweeper;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.test.util.AsyncTestUtils;

import java.time.Instant;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class WorkflowSweeperTest {

@Mock
private WorkflowExecutor workflowExecutor;

@Mock
private QueueDAO queueDAO;

@InjectMocks
private WorkflowSweeper workflowSweeper;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);
}

@Test
public void sweepAsyncTest() {
AsyncTestUtils.assertTrueEventually(
() -> {
workflowSweeper.sweepAsync("workflowId");
verify(workflowSweeper, times(1)).sweep("workflowId");
}
);
}

@Test
public void sweepTest() {
WorkflowContext workflowContext = new WorkflowContext("appId");
WorkflowContext.set(workflowContext);
WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setStatus(TaskModel.Status.COMPLETED);
doReturn(workflowModel)
.when(workflowExecutor)
.decide(anyString());
doNothing()
.when(queueDAO)
.remove(anyString(), anyString());

workflowSweeper.sweep("workflowId");

verify(workflowExecutor, times(1)).decide("workflowId");
verify(queueDAO, times(1)).remove(DECIDER_QUEUE, "workflowId");
}

@Test
public void unackTest() {
WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setTasks(
TaskModel.builder().status(TaskModel.Status.IN_PROGRESS).build());
doReturn(workflowModel)
.when(workflowExecutor)
.decide(anyString());
doNothing()
.when(queueDAO)
.setUnackTimeout(anyString(), anyString(), anyLong());

workflowSweeper.unack(workflowModel, 30);

verify(queueDAO, times(1))
.setUnackTimeout(DECIDER_QUEUE, "workflowId", 31 * 1000);
}

@Test
public void workflowOffsetWithJitterTest() {
long workflowOffsetTimeout = 45;
long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

long expectedMin = workflowOffsetTimeout - (workflowOffsetTimeout / 3);
long expectedMax = workflowOffsetTimeout + (workflowOffsetTimeout / 3);

assertTrue(result >= expectedMin && result <= expectedMax);
}
}