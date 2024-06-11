import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.TaskModel;
import com.netflix.conductor.dao.WorkflowModel;
import com.netflix.conductor.metrics.Monitors;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Component
public class AsyncSystemTaskExecutorTest {

 @Mock
 private ExecutionDAOFacade executionDAOFacade;
 @Mock
 private QueueDAO queueDAO;
 @Mock
 private MetadataDAO metadataDAO;
 @Mock
 private QueueUtils queueUtils;
 @Mock
 private Monitors monitors;
 @Mock
 private WorkflowExecutor workflowExecutor;
 @Mock
 private WorkflowSystemTask workflowSystemTask;
 private Logger logger;
 @InjectMocks
 private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
 private TaskModel taskModel;
 private WorkflowModel workflowModel;
 private ConcurrentMap<String, TaskModel> taskModelMap;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 logger = LoggerFactory.getLogger(AsyncSystemTaskExecutorTest.class);
 taskModel = new TaskModel();
 workflowModel = new WorkflowModel();
 taskModelMap = new ConcurrentHashMap<>();
 taskModel.setTaskId("task-123");
 taskModel.setWorkflowInstanceId("workflow-123");
 workflowModel.setStatus(WorkflowModel.WorkflowStatus.IN_PROGRESS);
 when(executionDAOFacade.getTaskModel(taskModel.getTaskId()))
 .thenReturn(taskModel);
 when(executionDAOFacade.getWorkflowModel(
 taskModel.getWorkflowInstanceId(),
 false))
 .thenReturn(workflowModel);
 when(queueUtils.getQueueName(taskModel)).thenReturn("test-queue");
 taskModelMap.put(taskModel.getTaskId(), taskModel);
 }

 @Test
 public void testExecute() {
 asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());
 }

 @Test
 public void testExecuteTaskCompleted() {
 taskModel.setStatus(TaskModel.Status.COMPLETED);
 asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());
 assertEquals(TaskModel.Status.COMPLETED, taskModel.getStatus());
 }

 @Test
 public void testExecuteTaskCancelled() {
 taskModel.setStatus(TaskModel.Status.CANCELED);
 asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());
 assertEquals(TaskModel.Status.CANCELED, taskModel.getStatus());
 }

 @Test
 public void testExecuteTaskFailed() {
 taskModel.setStatus(TaskModel.Status.FAILED);
 asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());
 assertEquals(TaskModel.Status.FAILED, taskModel.getStatus());
 }

 @Test
 public void testExecuteTaskScheduled() {
 taskModel.setStatus(TaskModel.Status.SCHEDULED);
 asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());
 assertEquals(TaskModel.Status.IN_PROGRESS, taskModel.getStatus());
 }

 @Test
 public void testExecuteTaskPostpone() {
 asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel.getTaskId());
 }
}