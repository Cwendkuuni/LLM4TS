import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsyncSystemTaskExecutorTest {

	@Mock
	private ExecutionDAOFacade executionDAOFacade;

	@Mock
	private QueueDAO queueDAO;

	@Mock
	private MetadataDAO metadataDAO;

	@Mock
	private ConductorProperties conductorProperties;

	@Mock
	private WorkflowExecutor workflowExecutor;

	@InjectMocks
	private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

	private TaskModel task;
	private WorkflowModel workflow;
	private WorkflowSystemTask systemTask;

	@BeforeEach
	void setUp(){
		task = new TaskModel();
		task.setTaskId("task-id");
		task.setWorkflowInstanceId("workflow-instance-id");
		task.setTaskDefName("task-def-name");
		task.setStatus(TaskModel.Status.SCHEDULED);
		task.setPriority(1);
		task.setPollCount(0);

		workflow = new WorkflowModel();
		workflow.setStatus(WorkflowModel.WorkflowStatus.RUNNING);
		workflow.setId("workflow-id");

		systemTask = mock(WorkflowSystemTask.class);

		ReflectionTestUtils.setField(asyncSystemTaskExecutor, "queueTaskMessagePostponeSecs", 10);
		ReflectionTestUtils.setField(asyncSystemTaskExecutor, "systemTaskCallbackTime", 10);
	}

	// Test cases for execute method
	@Test
	public void testExecute_TaskDoesNotExist_LogsErrorAndRemovesFromQueue() {
		// Arrange
		String taskId = "task-1";
		when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

		// Act
		asyncSystemTaskExecutor.execute(systemTask, taskId);

		// Assert
		verify(LOGGER).error(eq("TaskId: " + taskId + " could not be found while executing " + systemTask), anyString());
		verify(queueDAO).remove(eq("test-task-type"), eq(taskId));
	}

	@Test
	public void testExecute_TaskIsCompleted_RemovesFromQueue() {
		// Arrange
		String taskId = "task-1";
		task.setTaskType("test-task-type");
		task.setStatus(TaskModel.Status.COMPLETED);
		when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

		// Act
		asyncSystemTaskExecutor.execute(systemTask, taskId);

		// Assert
		verify(queueDAO).remove(eq("test-task-type"), eq(taskId));
	}

	@Test
	public void testExecute_TaskExceedsInProgressLimit_PostponesTask() {
		// Arrange
		String taskId = "task-1";
		task.setTaskType("test-task-type");
		task.setStatus(TaskModel.Status.SCHEDULED);
		task.setInProgressCount(conductorProperties.getConcurrentTaskExecutionLimit());
		when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

		// Act
		asyncSystemTaskExecutor.execute(systemTask, taskId);

		// Assert
		verify(queueDAO).postpone(eq("test-task-type"), eq(taskId), anyInt(), eq(queueTaskMessagePostponeSecs));
	}

	// Add more test cases for other scenarios

}