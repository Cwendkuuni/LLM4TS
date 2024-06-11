import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.dao.TaskModelDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class AsyncSystemTaskExecutorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutorTest.class);

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

    private ApplicationContext applicationContext;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(QueueDAOImpl.class);
        applicationContext.refresh();
        queueDAO = applicationContext.getBean(QueueDAO.class);

        ReflectionTestUtils.setField(asyncSystemTaskExecutor, "queueDAO", queueDAO);
        ReflectionTestUtils.setField(asyncSystemTaskExecutor, "executionDAOFacade", executionDAOFacade);
        ReflectionTestUtils.setField(asyncSystemTaskExecutor, "metadataDAO", metadataDAO);
        ReflectionTestUtils.setField(asyncSystemTaskExecutor, "workflowExecutor", workflowExecutor);

        when(metadataDAO.getTaskDef(anyString())).thenReturn(new WorkflowSystemTask("testTaskDef"));
    }

    @Test
    public void execute_TypicalUseCase() throws Exception {
        TaskModel task = new TaskModel();
        task.setTaskId("123");
        task.setWorkflowInstanceId("workflow-123");
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskDefName("testTaskDef");
        task.setRateLimitPerFrequency(1);

        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.WorkflowStatus.IN_PROGRESS);

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(new WorkflowSystemTask("testTask"), "123");

        Mockito.verify(queueDAO, Mockito.times(1)).remove(anyString(), anyString());
        Mockito.verify(executionDAOFacade, Mockito.times(1)).updateTask(any(TaskModel.class));
        Mockito.verify(workflowExecutor, Mockito.times(1)).decide("workflow-123");
    }

    @Test
    public void execute_TaskNotFound() throws Exception {
        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);

        asyncSystemTaskExecutor.execute(new WorkflowSystemTask("testTask"), "123");

        Mockito.verify(queueDAO, Mockito.times(0)).remove(anyString(), anyString());
        Mockito.verify(executionDAOFacade, Mockito.times(0)).updateTask(any(TaskModel.class));
        Mockito.verify(workflowExecutor, Mockito.times(0)).decide(anyString());
    }

    @Test
    public void execute_TaskAlreadyCompleted() throws Exception {
        TaskModel task = new TaskModel();
        task.setTaskId("123");
        task.setWorkflowInstanceId("workflow-123");
        task.setStatus(TaskModel.Status.COMPLETED);
        task.setTaskDefName("testTaskDef");

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);

        asyncSystemTaskExecutor.execute(new WorkflowSystemTask("testTask"), "123");

        Mockito.verify(queueDAO, Mockito.times(1)).remove(anyString(), anyString());
        Mockito.verify(executionDAOFacade, Mockito.times(0)).updateTask(any(TaskModel.class));
        Mockito.verify(workflowExecutor, Mockito.times(0)).decide(anyString());
    }

    @Test
    public void execute_TaskExceedsInProgressLimit() throws Exception {
        TaskModel task = new TaskModel();
        task.setTaskId("123");
        task.setWorkflowInstanceId("workflow-123");
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskDefName("testTaskDef");
        task.setRateLimitPerFrequency(1);

        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.WorkflowStatus.IN_PROGRESS);

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(executionDAOFacade.exceedsInProgressLimit(any(TaskModel.class)))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(new WorkflowSystemTask("testTask"), "123");

        Mockito.verify(queueDAO, Mockito.times(1)).postpone(
                anyString(),
                anyString(),
                anyInt(),
                anyLong()
        );
    }

    @Test
    public void execute_TaskExceedsRateLimitPerFrequency() throws Exception {
        TaskModel task = new TaskModel();
        task.setTaskId("123");
        task.setWorkflowInstanceId("workflow-123");
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskDefName("testTaskDef");
        task.setRateLimitPerFrequency(1);

        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.WorkflowStatus.IN_PROGRESS);

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(
                any(TaskModel.class),
                any(WorkflowSystemTask.class)
        )).thenReturn(true);

        asyncSystemTaskExecutor.execute(new WorkflowSystemTask("testTask"), "123");

        Mockito.verify(queueDAO, Mockito.times(1)).postpone(
                anyString(),
                anyString(),
                anyInt(),
                anyLong()
        );
    }

    @Test
    public void execute_WorkflowCompleted() throws Exception {
        TaskModel task = new TaskModel();
        task.setTaskId("123");
        task.setWorkflowInstanceId("workflow-123");
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskDefName("testTaskDef");
        task.setRateLimitPerFrequency(1);

        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.WorkflowStatus.COMPLETED);

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(anyString(), anyBoolean())).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(new WorkflowSystemTask("testTask"), "123");

        Mockito.verify(queueDAO, Mockito.times(1)).remove(anyString(), anyString());
        Mockito.verify(executionDAOFacade, Mockito.times(1)).updateTask(any(TaskModel.class));
        Mockito.verify(workflowExecutor, Mockito.times(1)).decide("workflow-123");
    }

    @Test
    public void postponeQuietly_Success() throws Exception {
        String queueName = "test-queue";
        TaskModel task = new TaskModel();
        task.setTaskId("123");

        queueDAO.postpone(queueName, task.getTaskId(), task.getWorkflowPriority(), 10);

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);
    }

    @Test
    public void postponeQuietly_Exception() throws Exception {
        String queueName = "test-queue";
        TaskModel task = new TaskModel();
        task.setTaskId("123");

        Mockito.doThrow(new Exception("test exception")).when(queueDAO).postpone(
                queueName,
                task.getTaskId(),
                task.getWorkflowPriority(),
                10
        );

        asyncSystemTaskExecutor.postponeQuietly(queueName, task);

        Mockito.verify(queueDAO, Mockito.times(1)).postpone(
                queueName,
                task.getTaskId(),
                task.getWorkflowPriority(),
                10
        );
    }

    @Test
    public void loadTaskQuietly_Success() throws Exception {
        String taskId = "123";

        TaskModel task = new TaskModel();
        task.setTaskId("123");

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        Mockito.verify(executionDAOFacade, Mockito.times(1)).getTaskModel(taskId);
        assert task.equals(result);
    }

    @Test
    public void loadTaskQuietly_Exception() throws Exception {
        String taskId = "123";

        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new Exception("test exception"));

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        Mockito.verify(executionDAOFacade, Mockito.times(1)).getTaskModel(taskId);
        assert result == null;
    }
}