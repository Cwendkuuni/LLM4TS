import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.RetryDAO;
import com.netflix.conductor.core.dal.SystemTaskQueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.dummy.DummySystemTask;
import com.netflix.conductor.core.executionengine.WorkflowExecutor;
import com.netflix.conductor.core.events.QueueUtils;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.model.workflow.WorkflowDef;
import com.netflix.conductor.core.utils.TestingUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AsyncSystemTaskExecutorTest {

@Mock
private ExecutionDAOFacade executionDAOFacade;

@Mock
private QueueDAO queueDAO;

@Mock
private MetadataDAO metadataDAO;

@Mock
private WorkflowExecutor workflowExecutor;

@InjectMocks
private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

private WorkflowSystemTask workflowSystemTask;

private TaskModel taskModel;

private WorkflowModel workflowModel;

private WorkflowDef workflowDef;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);

workflowSystemTask = new DummySystemTask();

taskModel = TestingUtils.createTaskModel();

workflowModel = TestingUtils.createWorkflowModel();

workflowDef = TestingUtils.createWorkflowDef();

ReflectionTestUtils.setField(asyncSystemTaskExecutor, "systemTaskCallbackTime", 10L);

ReflectionTestUtils.setField(asyncSystemTaskExecutor, "queueTaskMessagePostponeSecs", 5L);
}

@Test
public void testExecute_TaskDoesNotExist() {
String taskId = "123";

Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

Mockito.verify(queueDAO, Mockito.times(0)).remove(Mockito.anyString(), Mockito.anyString());
Mockito.verify(queueDAO, Mockito.times(0)).postpone(
Mockito.anyString(),
Mockito.anyString(),
Mockito.anyInt(),
Mockito.anyLong()
);
Mockito.verify(executionDAOFacade, Mockito.times(1)).updateTask(taskModel);
}

@Test
public void testExecute_TaskIsCompleted() {
String taskId = "123";

taskModel.setStatus(TaskModel.Status.COMPLETED);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

Mockito.verify(queueDAO, Mockito.times(1)).remove(Mockito.anyString(), MockId.equals(taskId));
Mockito.verify(queueDAO, Mockito.times(0)).postpone(
Mockito.anyString(),
Mockito.anyString(),
Mockito.anyInt(),
Mockito.anyLong()
);
Mockito.verify(executionDAOFacade, Mockito.times(1)).updateTask(taskModel);
}

@Test
public void testExecute_TaskIsCancelled() {
String taskId = "123";

taskModel.setStatus(TaskModel.Status.CANCELED);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

Mockito.verify(queueDAO, Mockito.times(0)).remove(Mockito.anyString(), Mockito.anyString());
Mockito.verify(queueDAO, Mockito.times(0)).postpone(
Mockito.anyString(),
Mockito.anyString(),
Mockito.anyInt(),
Mockito.anyLong()
);
Mockito.verify(executionDAOFacade, Mockito.times(1)).updateTask(taskModel);
}

@Test
public void testExecute_TaskIsScheduled_ExceedsInProgressLimit() {
String taskId = "123";

taskModel.setStatus(TaskModel.Status.SCHEDULED);

Mockito.when(executionDAOFacade.exceedsInProgressLimit(taskModel))
.thenReturn(true);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

Mockito.verify(queueDAO, Mockito.times(0)).remove(Mockito.anyString(), Mockito.anyString());
Mockito.verify(queueDAO, Mockito.times(1)).postpone(
Mockito.anyString(),
Mockito.anyString(),
Mockito.anyInt(),
Mockito.eq(5L)
);
Mockito.verify(executionDAOFacade, Mockito.times(1)).updateTask(taskModel);
}

@Test
public void testExecute_TaskIsScheduled_ExceedsRateLimitPerFrequency() {
String taskId = "123";

taskModel.setStatus(TaskModel.Status.SCHEDULED);

Mockito.when(executionDAOFacade.exceedsRateLimitPerFrequency(
taskModel,
Mockito.any(WorkflowDef.class)
))
.thenReturn(true);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

Mockito.verify(queueDAO, Mockito.times(0)).remove(Mockito.anyString(), Mockito.anyString());
Mockito.verify(queueDAO, Mockito.times(1)).postpone(
Mockito.anyString(),
Mockito.anyString(),
Mockito.anyInt(),
Mockito.eq(5L)
);
Mockito.verify(executionDAOFacade, Mockito.times(1)).updateTask(taskModel);
}

@Test
public void testExecute_TaskIsScheduled() {
String taskId = "123";

taskModel.setStatus(TaskModel.Status.SCHEDULED);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

Mockito.verify(queueDAO, Mockito.times(0)).remove(Mockito.anyString(), Mockito.anyString());
Mockito.verify(queueDAO, Mockito.times(1)).postpone(
Mockito.anyString(),
Mockito.anyString(),
Mockito.anyInt(),
Mockito.eq(10L)
);
Mockito.verify(executionDAOFacade, Mockito.times(1)).updateTask(taskModel);
}

@Test
public void testExecute_TaskIsInProgress() {
String taskId = "123";

taskModel.setStatus(TaskModel.Status.IN_PROGRESS);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

Mockito.verify(queueDAO, Mockito.times(0)).remove(Mockito.anyString(), Mockito.anyString());
Mockito.verify(queueDAO, Mockito.times(0)).postpone(
Mockito.anyString(),
Mockito.anyString(),
Mockito.anyInt(),
Mockito.anyLong()
);
Mockito.verify(executionDAOFacade, Mockito.times(1)).updateTask(taskModel);
}

@Test
public void testExecute_TaskIsCompleted_WorkflowIsCompleted() {
String taskId = "123";

taskModel.setStatus(TaskModel.Status.COMPLETED);
workflowModel.setStatus(WorkflowModel.Status.COMPLETED);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

Mockito.verify(queueDAO, Mockito.times(1)).remove(Mockito.anyString(), Mockito.anyString());
Mockito.verify(queueDAO, Mockito.times(0)).postpone(
Mockito.anyString(),
Mockito.anyString(),
Mockito.anyInt(),
Mockito.anyLong()
);
Mockito.verify(executionDAOFacade, Mockito.times(1)).updateTask(taskModel);
}

@Test
public void testExecute_WorkflowIsCompleted_TaskIsNotCompleted() {
String taskId = "123";

taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
workflowModel.setStatus(WorkflowModel.Status.COMPLETED);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

Mockito.verify(queueDAO, Mockito.times(0)).remove(Mockito.anyString(), Mockito.anyString());
Mockito.verify(queueDAO, Mockito.times(0)).postpone(
Mockito.anyString(),
Mockito.anyString(),
Mockito.anyInt(),
Mockito.anyLong()
);
Mockito.verify(executionDAOFacade, Mockito.times(1)).updateTask(taskModel);
}

@Test
public void testExecute_WorkflowIsCompleted_TaskIsScheduled() {
String taskId = "123";

taskModel.setStatus(TaskModel.Status.SCHEDULED);
workflowModel.setStatus(WorkflowModel.Status.COMPLETED);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

Mockito.verify(queueDAO, Mockito.times(0)).remove(Mockito.anyString(), Mockito.anyString());
Mockito.verify(queueDAO, Mockito.times(0)).postpone(
Mockito.anyString(),
Mockito.anyString(),
Mockito.anyInt(),
Mockito.anyLong()
);
Mockito.verify(executionDAOFacade, Mockito.times(1)).updateTask(taskModel);
}

@Test
public void testExecute_WorkflowIsNotCompleted() {
String taskId = "123";

workflowModel.setStatus(WorkflowModel.Status.STARTED);

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

Mockito.verify(queueDAO, Mockito.times(0)).remove(Mockito.anyString(), Mockito.anyString());
Mockito.verify(queueDAO, Mockito.times(0)).postpone(
Mockito.anyString(),
Mockito.anyString(),
Mockito.anyInt(),
Mockito.anyLong()
);
Mockito.verify(executionDAOFacade, Mockito.times(1)).updateTask(taskModel);
}

@Test
public void testExecute_Exception() {
String taskId = "123";

Mockito.doThrow(new RuntimeException("test exception"))
.when(workflowSystemTask)
.start(Mockito.any(WorkflowModel.class), Mockito.any(TaskModel.class), Mockito.any(WorkflowExecutor.class));

asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

Mockito.verify(queueDAO, Mockito.times(0)).remove(Mockito.anyString(), Mockito.anyString());
Mockito.verify(queueDAO, Mockito.times(0)).postpone(
Mockito.anyString(),
Mockito.anyString(),
Mockito.anyInt(),
Mockito.anyLong()
);
Mockito.verify(executionDAOFacade, Mockito.times(1)).updateTask(taskModel);
}

@Test
public void testPostponeQuietly_Exception() {
String queueName = "test-queue";
String taskId = "123";

Mockito.doThrow(new RuntimeException("test exception"))
.when(queueDAO)
.postpone(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyLong());

asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

Mockito.verify(queueDAO, Mockito.times(1))
.postpone(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyLong());
}

@Test
public void testLoadTaskQuietly_Exception() {
String taskId = "123";

Mockito.when(executionDAOFacade.getTaskModel(taskId))
.thenThrow(new RuntimeException("test exception"));

TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

Assert.assertNull(result);
}
}