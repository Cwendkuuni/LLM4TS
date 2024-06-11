import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.AsyncSystemTaskExecutor;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Configuration
@ComponentScan("com.netflix.conductor")
@ImportResource("classpath:/conductor-context.xml")
@PropertySource("classpath:application.properties")
public class AsyncSystemTaskExecutorTest {

    private static final String TASK_ID = "123";
    private static final String WORKFLOW_ID = "workflow-123";
    private static final String TASK_TYPE = "TASK_TYPE";
    private static final String QUEUE_NAME = "QUEUE_NAME";

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private WorkflowExecutor workflowExecutor;
    private Logger logger;
    private ApplicationContext applicationContext;
    private Environment environment;

    @Before
    public void setup() {
        applicationContext = new AnnotationConfigApplicationContext(AsyncSystemTaskExecutorTest.class);
        executionDAOFacade = Mockito.mock(ExecutionDAOFacade.class);
        queueDAO = Mockito.mock(QueueDAO.class);
        metadataDAO = Mockito.mock(MetadataDAO.class);
        workflowExecutor = Mockito.mock(WorkflowExecutor.class);
        logger = Mockito.mock(Logger.class);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                createConductorProperties(),
                workflowExecutor
        ) {
            @Override
            protected Logger getLogger() {
                return logger;
            }
        };

        asyncSystemTaskExecutor.setApplicationContext(applicationContext);
    }

    @Test
    public void testExecute_TaskNotFound() {
        TaskModel task = null;

        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(task);

        asyncSystemTaskExecutor.execute(createWorkflowSystemTask(), TASK_ID);

        verify(logger, times(1))
                .error("TaskId: {} could not be found while executing {}", TASK_ID, any());
        verify(queueDAO, times(1))
                .remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_TaskCompleted() {
        TaskModel task = createTaskModel(TaskModel.Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(task);

        asyncSystemTaskExecutor.execute(createWorkflowSystemTask(), TASK_ID);

        verify(queueDAO, times(1))
                .remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_TaskCancelled() {
        TaskModel task = createTaskModel(TaskModel.Status.CANCELED);

        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(task);

        asyncSystemTaskExecutor.execute(createWorkflowSystemTask(), TASK_ID);

        verify(queueDAO, times(1))
                .remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_TaskInProgress() {
        TaskModel task = createTaskModel(TaskModel.Status.IN_PROGRESS);

        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(task);

        asyncSystemTaskExecutor.execute(createWorkflowSystemTask(), TASK_ID);

        verify(queueDAO, times(1))
                .remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_TaskScheduled_ConcurrentExecutionLimitExceeded() {
        TaskModel task = createTaskModel(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(createWorkflowSystemTask(), TASK_ID);

        verify(queueDAO, times(1))
                .postpone(QUEUE_NAME, TASK_ID, task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
        verify(logger, times(1))
                .warn("Concurrent Execution limited for {}:{}", TASK_ID, task.getTaskDefName());
    }

    @Test
    public void testExecute_TaskScheduled_RateLimitExceeded() {
        TaskModel task = createTaskModel(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, Mockito.any()))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(createWorkflowSystemTask(), TASK_ID);

        verify(queueDAO, times(1))
                .postpone(QUEUE_NAME, TASK_ID, task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
        verify(logger, times(1))
                .warn("RateLimit Execution limited for {}:{}, limit:{}",
                        TASK_ID, task.getTaskDefName(), task.getRateLimitPerFrequency());
    }

    @Test
    public void testExecute_TaskScheduled_WorkflowCompleted() {
        TaskModel task = createTaskModel(TaskModel.Status.SCHEDULED);
        WorkflowModel workflow = createWorkflowModel(WorkflowModel.Status.COMPLETED);

        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, Mockito.anyBoolean()))
                .thenReturn(workflow);

        asyncSystemTaskExecutor.execute(createWorkflowSystemTask(), TASK_ID);

        verify(queueDAO, times(1))
                .remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_TaskScheduled() {
        TaskModel task = createTaskModel(TaskModel.Status.SCHEDULED);
        WorkflowModel workflow = createWorkflowModel(WorkflowModel.Status.RUNNING);

        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(task);
        when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, Mockito.anyBoolean()))
                .thenReturn(workflow);

        asyncSystemTaskExecutor.execute(createWorkflowSystemTask(), TASK_ID);

        verify(queueDAO, times(1))
                .postpone(QUEUE_NAME, TASK_ID, task.getWorkflowPriority(), task.getCallbackAfterSeconds());
        verify(workflowExecutor, times(1))
                .decide(WORKFLOW_ID);
    }

    @Test
    public void testExecute_TaskAsyncComplete() {
        TaskModel task = createTaskModel(TaskModel.Status.IN_PROGRESS);
        task.setEndTime(System.currentTimeMillis());

        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(task);

        asyncSystemTaskExecutor.execute(createWorkflowSystemTask(), TASK_ID);

        verify(queueDAO, times(1))
                .remove(QUEUE_NAME, TASK_ID);
        verify(workflowExecutor, times(1))
                .decide(WORKFLOW_ID);
    }

    @Test
    public void testExecute_TaskException() {
        TaskModel task = createTaskModel(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(task);

        asyncSystemTaskExecutor.execute(createWorkflowSystemTaskWithException(), TASK_ID);

        verify(logger, times(1))
                .error("Error executing system task - {}, with id: {}",
                        createWorkflowSystemTaskWithException(), TASK_ID);
    }

    @Test
    public void testPostponeQuietly_Success() {
        TaskModel task = createTaskModel(TaskModel.Status.SCHEDULED);

        asyncSystemTaskExecutor.postponeQuietly(QUEUE_NAME, task);

        verify(queueDAO, times(1))
                .postpone(QUEUE_NAME, TASK_ID, task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
    }

    @Test
    public void testPostponeQuietly_Exception() {
        TaskModel task = createTaskModel(TaskModel.Status.SCHEDULED);

        when(queueDAO.postpone(QUEUE_NAME, TASK_ID, task.getWorkflowPriority(), queueTaskMessagePostponeSecs))
                .thenThrow(new RuntimeException());

        asyncSystemTaskExecutor.postponeQuietly(QUEUE_NAME, task);

        verify(logger, times(1))
                .error("Error postponing task: {} in queue: {}", TASK_ID, QUEUE_NAME);
    }

    @Test
    public void testLoadTaskQuietly_Success() {
        asyncSystemTaskExecutor.loadTaskQuietly(TASK_ID);

        verify(executionDAOFacade, times(1))
                .getTaskModel(TASK_ID);
    }

    @Test
    public void testLoadTaskQuietly_Exception() {
        when(executionDAOFacade.getTaskModel(TASK_ID))
                .thenThrow(new RuntimeException());

        assertNull(asyncSystemTaskExecutor.loadTaskQuietly(TASK_ID));
    }

    private ConductorProperties createConductorProperties() {
        ConductorProperties conductorProperties = new ConductorProperties();
        conductorProperties.setSystemTaskWorkerCallbackDuration(10);
        conductorProperties.setTaskExecutionPostponeDuration(10);
        return conductorProperties;
    }

    private WorkflowSystemTask createWorkflowSystemTask() {
        return createWorkflowSystemTask(null);
    }

    private WorkflowSystemTask createWorkflowSystemTaskWithException() {
        WorkflowSystemTask workflowSystemTask = createWorkflowSystemTask(Exception.class);
        when(workflowSystemTask.execute(any(), any(), any()))
                .thenThrow(new RuntimeException());
        return workflowSystemTask;
    }

    private WorkflowSystemTask createWorkflowSystemTask(Class<? extends Throwable> exceptionClass) {
        return Mockito.mock(WorkflowSystemTask.class,
                invocation -> {
                    if (exceptionClass != null && "execute".equals(invocation.getMethod().getName())) {
                        throw exceptionClass.getConstructor().newInstance();
                    }
                    return null;
                });
    }

    private TaskModel createTaskModel(TaskModel.Status status) {
        return createTaskModel(status, 0, 0, 0, 0, 0, 0, 0, 0, null);
    }

    private TaskModel createTaskModel(
            TaskModel.Status status,
            long taskId,
            long workflowInstanceId,
            String taskDefName,
            String callbackUrl,
            long startTime,
            long endTime,
            long pollCount,
            long callbackAfterSeconds,
            WorkflowModel workflowModel) {

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(String.valueOf(taskId));
        taskModel.setWorkflowInstanceId(String.valueOf(workflowInstanceId));
        taskModel.setTaskDefName(taskDefName);
        taskModel.setCallbackUrl(callbackUrl);
        taskModel.setStartTime(startTime);
        taskModel.setEndTime(endTime);
        taskModel.setPollCount(pollCount);
        taskModel.setCallbackAfterSeconds(callbackAfterSeconds);
        taskModel.setStatus(status);
        taskModel.setWorkflowPriority(1);
        taskModel.setReasonForIncompletion(null);
        taskModel.setInput(new HashMap<>());
        taskModel.setOutput(new HashMap<>());
        taskModel.setWorkflowModel(workflowModel);
        return taskModel;
    }

    private WorkflowModel createWorkflowModel(WorkflowModel.Status status) {
        return createWorkflowModel(status, "workflow-id", 0, 0, null);
    }

    private WorkflowModel createWorkflowModel(
            WorkflowModel.Status status,
            String workflowInstanceId,
            long version,
            long tasksCount,
            Map<String, Object> input) {

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setStatus(status);
        workflowModel.setWorkflowInstanceId(workflowInstanceId);
        workflowModel.setVersion(version);
        workflowModel.setTasksCount(tasksCount);
        workflowModel.setInput(input);
        return workflowModel;
    }
}