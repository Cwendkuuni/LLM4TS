import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.dao.QueueDAOImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AsyncSystemTaskExecutorTest {

 @Mock
 private ExecutionDAOFacade executionDAOFacade;
 @Mock
 private QueueDAO queueDAO;
 @Mock
 private MetadataDAO metadataDAO;
 @Mock
 private WorkflowExecutor workflowExecutor;
 private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

 @Before
 public void init() {
 MockitoAnnotations.initMocks(this);
 asyncSystemTaskExecutor =
 new AsyncSystemTaskExecutor(
 executionDAOFacade, queueDAO, metadataDAO, new ConductorProperties(), workflowExecutor);
 }

 @Test
 public void testExecute() {
 WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
 String taskId = "123";
 TaskModel taskModel = new TaskModel();
 taskModel.setWorkflowInstanceId("workflowId");
 taskModel.setTaskId(taskId);
 taskModel.setStatus(TaskModel.Status.SCHEDULED);

 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);
 when(queueDAO.get(any(), any())).thenReturn(taskModel);

 asyncSystemTaskExecutor.execute(systemTask, taskId);

 verify(systemTask, times(1)).start(any(), any(), any());
 verify(executionDAOFacade, times(1)).updateTask(taskModel);
 }

 @Test
 public void testExecuteWhenTaskNotFound() {
 WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
 String taskId = "123";

 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

 asyncSystemTaskExecutor.execute(systemTask, taskId);

 verify(systemTask, never()).start(any(), any(), any());
 verify(executionDAOFacade, times(0)).updateTask(any());
 }

 @Test
 public void testExecuteWithTerminalStatus() {
 WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
 String taskId = "123";
 TaskModel taskModel = new TaskModel();
 taskModel.setWorkflowInstanceId("workflowId");
 taskModel.setTaskId(taskId);
 taskModel.setStatus(TaskModel.Status.COMPLETED);

 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

 asyncSystemTaskExecutor.execute(systemTask, taskId);

 verify(systemTask, never()).start(any(), any(), any());
 verify(queueDAO, times(1)).remove(any(), any());
 }

 @Test
 public void testExecuteWhenTaskExceedsInProgressLimit() {
 WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
 String taskId = "123";
 TaskModel taskModel = new TaskModel();
 taskModel.setWorkflowInstanceId("workflowId");
 taskModel.setTaskId(taskId);
 taskModel.setStatus(TaskModel.Status.SCHEDULED);

 when(executionDAOFacade.exceedsInProgressLimit(taskModel))
 .thenReturn(true);

 asyncSystemTaskExecutor.execute(systemTask, taskId);

 verify(systemTask, never()).start(any(), any(), any());
 verify(queueDAO, times(1)).postpone(
 any(), any(), anyInt(), anyLong());
 }

 @Test
 public void testExecuteWhenTaskExceedsRateLimitPerFrequency() {
 WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
 String taskId = "123";
 TaskModel taskModel = new TaskModel();
 taskModel.setWorkflowInstanceId("workflowId");
 taskModel.setTaskId(taskId);
 taskModel.setStatus(TaskModel.Status.SCHEDULED);

 when(executionDAOFacade.exceedsRateLimitPerFrequency(taskModel, any()))
 .thenReturn(true);

 asyncSystemTaskExecutor.execute(systemTask, taskId);

 verify(systemTask, never()).start(any(), any(), any());
 verify(queueDAO, times(1)).postpone(
 any(), any(), anyInt(), anyLong());
 }

 @Test
 public void testExecuteWithWorkflowCompletion() {
 WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
 String taskId = "123";
 TaskModel taskModel = new TaskModel();
 taskModel.setWorkflowInstanceId("workflowId");
 taskModel.setTaskId(taskId);
 taskModel.setStatus(TaskModel.Status.SCHEDULED);

 WorkflowModel workflowModel = mock(WorkflowModel.class);
 when(workflowModel.getStatus())
 .thenReturn(WorkflowModel.WorkflowStatus.COMPLETED);

 when(executionDAOFacade.getWorkflowModel(any(), anyBoolean()))
 .thenReturn(workflowModel);

 asyncSystemTaskExecutor.execute(systemTask, taskId);

 verify(systemTask, never()).start(any(), any(), any());
 verify(queueDAO, times(1)).remove(any(), any());
 }

 @Test
 public void testExecuteWithWorkflowInProgress() {
 WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
 String taskId = "123";
 TaskModel taskModel = new TaskModel();
 taskModel.setWorkflowInstanceId("workflowId");
 taskModel.setTaskId(taskId);
 taskModel.setStatus(TaskModel.Status.SCHEDULED);

 WorkflowModel workflowModel = mock(WorkflowModel.class);
 when(workflowModel.getStatus())
 .thenReturn(WorkflowModel.WorkflowStatus.IN_PROGRESS);

 when(executionDAOFacade.getWorkflowModel(any(), anyBoolean()))
 .thenReturn(workflowModel);

 asyncSystemTaskExecutor.execute(systemTask, taskId);

 verify(systemTask, times(1)).execute(any(), any(), any());
 }

 @Test
 public void testExecuteWithWorkflowFailed() {
 WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
 String taskId = "123";
 TaskModel taskModel = new TaskModel();
 taskModel.setWorkflowInstanceId("workflowId");
 taskModel.setTaskId(taskId);
 taskModel.setStatus(TaskModel.Status.SCHEDULED);

 WorkflowModel workflowModel = mock(WorkflowModel.class);
 when(workflowModel.getStatus())
 .thenReturn(WorkflowModel.WorkflowStatus.FAILED);

 when(executionDAOFacade.getWorkflowModel(any(), anyBoolean()))
 .thenReturn(workflowModel);

 asyncSystemTaskExecutor.execute(systemTask, taskId);

 verify(systemTask, never()).start(any(), any(), any());
 verify(queueDAO, times(1)).remove(any(), any());
 }

 @Test
 public void testExecuteWithWorkflowCancelled() {
 WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
 String taskId = "123";
 TaskModel taskModel = new TaskModel();
 taskModel.setWorkflowInstanceId("workflowId");
 taskModel.setTaskId(taskId);
 taskModel.setStatus(TaskModel.Status.SCHEDULED);

 WorkflowModel workflowModel = mock(WorkflowModel.class);
 when(workflowModel.getStatus())
 .thenReturn(WorkflowModel.WorkflowStatus.CANCELED);

 when(executionDAOFacade.getWorkflowModel(any(), anyBoolean()))
 .thenReturn(workflowModel);

 asyncSystemTaskExecutor.execute(systemTask, taskId);

 verify(systemTask, never()).start(any(), any(), any());
 verify(queueDAO, times(1)).remove(any(), any());
 }

 @Test
 public void testPostponeQuietly() {
 String queueName = "queue";
 String taskId = "123";
 TaskModel taskModel = new TaskModel();
 taskModel.setTaskId(taskId);

 asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

 verify(queueDAO, times(1)).postpone(queueName, taskId, anyInt(), anyLong());
 }

 @Test
 public void testLoadTaskQuietly() {
 String taskId = "123";

 TaskModel taskModel = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

 verify(executionDAOFacade, times(1)).getTaskModel(taskId);
 }

 @Test
 public void testLoadTaskQuietlyWhenTaskNotFound() {
 String taskId = "123";

 when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

 TaskModel taskModel = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

 verify(executionDAOFacade, times(1)).getTaskModel(taskId);
 assertEquals(null, taskModel);
 }
}