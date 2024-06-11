import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.dao.TaskModel;
import com.netflix.conductor.dao.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.concurrent.ExecutionException;
import static org.junit.Assert.*;
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
 @Mock
 private WorkflowSystemTask workflowSystemTask;
 @Mock
 private TaskModel taskModel;
 @Mock
 private WorkflowModel workflowModel;

 private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 asyncSystemTaskExecutor =
 new AsyncSystemTaskExecutor(
 executionDAOFacade, queueDAO, metadataDAO, new ConductorProperties(), workflowExecutor);
 }

 @Test
 public void execute_taskModelIsNull_shouldLogErrorAndReturn() throws ExecutionException {
 asyncSystemTaskExecutor.execute(workflowSystemTask, null);
 verify(LOGGER).error(anyString(), anyString(), anyString());
 }

 @Test
 public void execute_taskModelIsTerminal_shouldRemoveFromQueue()
 throws ExecutionException, InterruptedException {
 when(taskModel.getStatus()).thenReturn(TaskModel.Status.COMPLETED);
 asyncSystemTaskExecutor.execute(workflowSystemTask, "123");
 verify(queueDAO).remove(anyString(), anyString());
 }

 @Test
 public void execute_taskModelExceedsInProgressLimit_shouldPostpone()
 throws ExecutionException, InterruptedException {
 when(executionDAOFacade.exceedsInProgressLimit(any())).thenReturn(true);
 asyncSystemTaskExecutor.execute(workflowSystemTask, "123");
 verify(queueDAO).postpone(anyString(), anyString(), anyInt());
 }

 @Test
 public void execute_taskModelExceedsRateLimitPerFrequency_shouldPostpone()
 throws ExecutionException, InterruptedException {
 when(executionDAOFacade.exceedsRateLimitPerFrequency(any(), any())).thenReturn(true);
 asyncSystemTaskExecutor.execute(workflowSystemTask, "123");
 verify(queueDAO).postpone(anyString(), anyString(), anyInt());
 }

 @Test
 public void execute_workflowIsCompleted_shouldCancelTaskModel()
 throws ExecutionException, InterruptedException {
 when(workflowModel.getStatus()).thenReturn(WorkflowModel.WorkflowStatus.COMPLETED);
 asyncSystemTaskExecutor.execute(workflowSystemTask, "123");
 verify(taskModel).setStatus(TaskModel.Status.CANCELED);
 }

 @Test
 public void execute_workflowIsTerminal_shouldRemoveFromQueue()
 throws ExecutionException, InterruptedException {
 when(workflowModel.getStatus()).thenReturn(WorkflowModel.WorkflowStatus.FAILED);
 asyncSystemTaskExecutor.execute(workflowSystemTask, "123");
 verify(queueDAO).remove(anyString(), anyString());
 }

 @Test
 public void execute_taskIsScheduled_shouldIncrementPollCount()
 throws ExecutionException, InterruptedException {
 when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
 asyncSystemTaskExecutor.execute(workflowSystemTask, "123");
 verify(taskModel).incrementPollCount();
 }

 @Test
 public void execute_taskIsScheduledAndAsyncComplete_shouldRemoveFromQueue()
 throws ExecutionException, InterruptedException {
 when(taskModel.getStatus()).thenReturn(TaskModel.Status.SCHEDULED);
 when(workflowSystemTask.isAsyncComplete(any())).thenReturn(true);
 asyncSystemTaskExecutor.execute(workflowSystemTask, "123");
 verify(queueDAO).remove(anyString(), anyString());
 }

 @Test
 public void execute_taskIsInProgress_shouldExecuteSystemTask()
 throws ExecutionException, InterruptedException {
 when(taskModel.getStatus()).thenReturn(TaskModel.Status.IN_PROGRESS);
 asyncSystemTaskExecutor.execute(workflowSystemTask, "123");
 verify(workflowSystemTask).execute(any(), any(), any());
 }

 @Test
 public void execute_taskExecutionCompleted_shouldDecideWorkflow()
 throws ExecutionException, InterruptedException {
 asyncSystemTaskExecutor.execute(workflowSystemTask, "123");
 verify(workflowExecutor).decide("123");
 }

 @Test
 public void postponeQuietly_shouldPostponeTaskInQueue()
 throws ExecutionException, InterruptedException {
 asyncSystemTaskExecutor.postponeQuietly("test-queue", taskModel);
 verify(queueDAO).postpone(eq("test-queue"), eq(taskModel.getTaskId()), anyInt());
 }

 @Test
 public void loadTaskQuietly_shouldReturnTaskModelFromExecutionDAO()
 throws ExecutionException, InterruptedException {
 when(executionDAOFacade.getTaskModel("123")).thenReturn(taskModel);
 TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly("123");
 assertEquals(result, taskModel);
 }
}