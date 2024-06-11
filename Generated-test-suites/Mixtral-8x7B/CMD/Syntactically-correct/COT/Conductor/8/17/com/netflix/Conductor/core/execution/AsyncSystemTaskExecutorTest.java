import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ContextConfiguration(classes = AsyncSystemTaskExecutorTest.Config.class)
public class AsyncSystemTaskExecutorTest {

    @Configuration
    @ComponentScan("com.netflix.conductor.core.execution")
    static class Config {
    }

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private QueueUtils queueUtils;

    @Mock
    private WorkflowSystemTask workflowSystemTask;

    @Mock
    private Logger logger;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        asyncSystemTaskExecutor.setLogger(logger);
    }

    @Test
    public void testExecute_GivenTaskModelAndSystemTask_WhenTaskExists_ThenPersistResults() {
        // Arrange
        String taskId = "taskId";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        // Act
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // Assert
        verify(executionDAOFacade, times(1)).updateTask(taskModel);
        verifyNoMoreInteractions(executionDAOFacade);
    }

    @Test
    public void testExecute_GivenTaskModelAndSystemTask_WhenTaskDoesNotExist_ThenLogError() {
        // Arrange
        String taskId = "taskId";

        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());

        // Act
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        // Assert
        verify(logger, times(1)).error(anyString(), anyString(), any(RuntimeException.class));
        verifyNoMoreInteractions(executionDAOFacade);
    }

    @Test
    public void testPostponeQuietly_GivenQueueNameAndTaskModel_WhenNoException_ThenPostponeTask() {
        // Arrange
        String queueName = "queueName";
        TaskModel taskModel = new TaskModel();

        // Act
        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

        // Assert
        verify(queueDAO, times(1)).postpone(queueName, taskModel.getTaskId(),
                taskModel.getWorkflowPriority(), queueTaskMessagePostponeSecs);
        verifyNoMoreInteractions(queueDAO);
    }

    @Test
    public void testPostponeQuietly_GivenQueueNameAndTaskModel_WhenException_ThenLogError() {
        // Arrange
        String queueName = "queueName";
        TaskModel taskModel = new TaskModel();

        doThrow(new RuntimeException()).when(queueDAO).postpone(queueName,
                taskModel.getTaskId(), taskModel.getWorkflowPriority(), queueTaskMessagePostponeSecs);

        // Act
        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

        // Assert
        verify(logger, times(1)).error(anyString(), anyString(), any(RuntimeException.class));
        verifyNoMoreInteractions(queueDAO);
    }

    @Test
    public void testLoadTaskQuietly_GivenTaskId_WhenTaskExists_ThenReturnTaskModel() {
        // Arrange
        String taskId = "taskId";
        TaskModel taskModel = new TaskModel();

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(taskModel);

        // Act
        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        // Assert
        assertEquals(taskModel, result);
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verifyNoMoreInteractions(executionDAOFacade);
    }

    @Test
    public void testLoadTaskQuietly_GivenTaskId_WhenTaskDoesNotExist_ThenReturnNull() {
        // Arrange
        String taskId = "taskId";

        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());

        // Act
        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        // Assert
        assertNotNull(result);
        verify(executionDAOFacade, times(1)).getTaskModel(taskId);
        verifyNoMoreInteractions(executionDAOFacade);
    }
}