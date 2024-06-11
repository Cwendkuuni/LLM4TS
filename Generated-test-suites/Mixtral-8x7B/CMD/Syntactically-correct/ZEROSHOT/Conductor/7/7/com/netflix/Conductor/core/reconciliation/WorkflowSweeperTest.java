import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.WorkflowContext;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.test.util.AsyncTestUtils;

import java.time.Instant;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class WorkflowSweeperTest {

@Mock
private WorkflowExecutor workflowExecutor;

@Mock
private QueueDAO queueDAO;

@Mock
private ConductorProperties properties;

@Mock
private WorkflowRepairService workflowRepairService;

private WorkflowSweeper workflowSweeper;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);

WorkflowContext workflowContext = new WorkflowContext("testAppId");
WorkflowContext.set(workflowContext);

when(properties.getAppId()).thenReturn("testAppId");
when(properties.getWorkflowOffsetTimeout()).thenReturn(Optional.of(60L));

workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
}

@Test
public void sweepAsyncTest() {
AsyncTestUtils.invokeAndAssert(() -> workflowSweeper.sweepAsync("testWorkflowId"), null, () -> {
verify(workflowExecutor, times(1)).decide(anyString());
verify(queueDAO, times(1)).remove(anyString(), anyString());
verifyNoMoreInteractions(workflowExecutor, queueDAO);
});
}

@Test
public void sweepTest() {
workflowSweeper.sweep("testWorkflowId");

verify(workflowExecutor, times(1)).decide(anyString());
verify(queueDAO, times(1)).remove(anyString(), anyString());
verifyNoMoreInteractions(workflowExecutor, queueDAO);
}

@Test
public void unackTest() {
WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setWorkflowDefinition(new WorkflowModel.WorkflowDefinition());
workflowModel.setTasks(
new TaskModel[]{
new TaskModel().setStatus(TaskModel.Status.IN_PROGRESS).setTaskType(TaskType.TASK_TYPE_TASK),
new TaskModel().setStatus(TaskModel.Status.SCHEDULED)
}
);

workflowSweeper.unack(workflowModel, 60);

verify(queueDAO, times(1)).setUnackTimeout(anyString(), anyString(), anyLong());
verifyNoMoreInteractions(queueDAO);
}

@Test
public void workflowOffsetWithJitterTest() {
long result1 = workflowSweeper.workflowOffsetWithJitter(45);
long result2 = workflowSweeper.workflowOffsetWithJitter(45);
long result3 = workflowSweeper.workflowOffsetWithJitter(45);

assertEquals(45, result1);
assertEquals(45, result2);
assertEquals(45, result3);

result1 = workflowSweeper.workflowOffsetWithJitter(120);
result2 = workflowSweeper.workflowOffsetWithJitter(120);
result3 = workflowSweeper.workflowOffsetWithJitter(120);

assertEquals(80, result1);
assertEquals(120, result2);
assertEquals(160, result3);
}
}