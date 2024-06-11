import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.execution.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.TestSystemTask;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.WorkerTask;
import com.netflix.conductor.core.metadata.tasks.TaskDef;
import com.netflix.conductor.core.metadata.tasks.TaskType;
import com.netflix.conductor.core.model.Task;
import com.netflix.conductor.core.model.Workflow;
import com.netflix.conductor.core.utils.QueueUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

 @Mock
 private ExecutionDAOFacade executionDAOFacade;

 @Mock
 private QueueDAO queueDAO;

 @Mock
 private MetadataDAO metadataDAO;

 @InjectMocks
 private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

 @Before
 public void init() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testExecute_whenTaskDoesNotExist_thenLogErrorAndRemoveFromQueue() {
 // Arrange
 WorkflowSystemTask systemTask = new TestSystemTask();
 String taskId = "123";

 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

 // Act
 asyncSystemTaskExecutor.execute(systemTask, taskId);

 // Assert
 verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
 }

 @Test
 public void testExecute_whenTaskIsCompleted_thenRemoveFromQueue() {
 // Arrange
 WorkflowSystemTask systemTask = new TestSystemTask();
 String taskId = "123";
 Task task = new Task();
 task.setStatus(Task.Status.COMPLETED);

 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

 // Act
 asyncSystemTaskExecutor.execute(systemTask, taskId);

 // Assert
 verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
 }

 @Test
 public void testExecute_whenTaskExceedsInProgressLimit_thenPostpone() {
 // Arrange
 WorkflowSystemTask systemTask = new TestSystemTask();
 String taskId = "123";
 Task task = new Task();
 task.setStatus(Task.Status.SCHEDULED);
 task.setTaskDefinitionName("test");
 when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

 // Act
 asyncSystemTaskExecutor.execute(systemTask, taskId);

 // Assert
 verify(queueDAO, times(1)).postpone(anyString(), eq(taskId));
 }

 @Test
 public void testExecute_whenTaskExceedsRateLimitPerFrequency_thenPostpone() {
 // Arrange
 WorkflowSystemTask systemTask = new TestSystemTask();
 String taskId = "123";
 Task task = new Task();
 task.setStatus(Task.Status.SCHEDULED);
 task.setTaskDefinitionName("test");
 when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(true);

 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

 // Act
 asyncSystemTaskExecutor.execute(systemTask, taskId);

 // Assert
 verify(queueDAO, times(1)).postpone(anyString(), eq(taskId));
 }

 @Test
 public void testExecute_whenTaskIsExecuted_thenUpdateTask() {
 // Arrange
 WorkflowSystemTask systemTask = new TestSystemTask();
 String taskId = "123";
 Task task = new Task();
 task.setStatus(Task.Status.SCHEDULED);
 task.setTaskDefinitionName("test");

 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

 // Act
 asyncSystemTaskExecutor.execute(systemTask, taskId);

 // Assert
 verify(executionDAOFacade, times(1)).updateTask(task);
 }

 @Test
 public void testExecute_whenTaskIsCancelled_thenRemoveFromQueue() {
 // Arrange
 WorkflowSystemTask systemTask = new TestSystemTask();
 String taskId = "123";
 Task task = new Task();
 task.setStatus(Task.Status.CANCELED);

 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

 // Act
 asyncSystemTaskExecutor.execute(systemTask, taskId);

 // Assert
 verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
 }

 @Test
 public void testExecute_whenWorkflowIsCompleted_thenRemoveFromQueue() {
 // Arrange
 WorkflowSystemTask systemTask = new TestSystemTask();
 String taskId = "123";
 Task task = new Task();
 task.setStatus(Task.Status.IN_PROGRESS);
 Workflow workflow = new Workflow();
 workflow.setStatus(Workflow.Status.COMPLETED);

 when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

 // Act
 asyncSystemTaskExecutor.execute(systemTask, taskId);

 // Assert
 verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
 }

 @Test
 public void testExecute_whenWorkflowIsTerminal_thenSetTaskToCancelled() {
 // Arrange
 WorkflowSystemTask systemTask = new TestSystemTask();
 String taskId = "123";
 Task task = new Task();
 task.setStatus(Task.Status.IN_PROGRESS);
 Workflow workflow = new Workflow();
 workflow.setStatus(Workflow.Status.FAILED);

 when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

 // Act
 asyncSystemTaskExecutor.execute(systemTask, taskId);

 // Assert
 assertEquals(Task.Status.CANCELED, task.getStatus());
 }

 @Test
 public void testExecute_whenTaskIsAsyncCompleteAndNotScheduled_thenRemoveFromQueue() {
 // Arrange
 WorkflowSystemTask systemTask = new TestSystemTask();
 systemTask.setAsyncComplete(true);
 String taskId = "123";
 Task task = new Task();
 task.setStatus(Task.Status.IN_PROGRESS);
 TaskDef taskDef = new TaskDef();
 taskDef.setTaskType(TaskType.WORKFLOW_TASK);
 when(executionDAOFacade.getTaskDef(taskDef.getName())).thenReturn(taskDef);

 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

 // Act
 asyncSystemTaskExecutor.execute(systemTask, taskId);

 // Assert
 verify(queueDAO, times(1)).remove(anyString(), eq(taskId));
 }

 @Test
 public void testExecute_whenTaskIsNotAsyncComplete_thenPostpone() {
 // Arrange
 WorkflowSystemTask systemTask = new TestSystemTask();
 systemTask.setAsyncComplete(false);
 String taskId = "123";
 Task task = new Task();
 task.setStatus(Task.Status.IN_PROGRESS);
 TaskDef taskDef = new TaskDef();
 taskDef.setTaskType(TaskType.WORKFLOW_TASK);
 when(executionDAOFacade.getTaskDef(taskDef.getName())).thenReturn(taskDef);

 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

 // Act
 asyncSystemTaskExecutor.execute(systemTask, taskId);

 // Assert
 verify(queueDAO, times(1)).postpone(anyString(), eq(taskId));
 }

 @Test
 public void testExecute_whenTaskIsCompleted_thenDecideWorkflow() {
 // Arrange
 WorkflowSystemTask systemTask = new TestSystemTask();
 String taskId = "123";
 Task task = new Task();
 task.setStatus(Task.Status.COMPLETED);

 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

 // Act
 asyncSystemTaskExecutor.execute(systemTask, taskId);

 // Assert
 verify(executionDAOFacade, times(1)).decide(anyString());
 }
}