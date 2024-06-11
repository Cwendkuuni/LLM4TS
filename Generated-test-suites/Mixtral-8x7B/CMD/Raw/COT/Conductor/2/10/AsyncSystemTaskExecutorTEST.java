import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.test.TestWorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.dao.TaskModelDAO;
import com.netflix.conductor.dao.metadata.TaskDefDAO;
import com.netflix.conductor.dao.metadata.WorkflowDefDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.workflow.WorkflowDef;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AsyncSystemTaskExecutorTest {

    private static final String TASK_ID = "123";
    private static final String WORKFLOW_ID = "workflow-123";
    private static final String TASK_TYPE = "test-task";
    private static final String QUEUE_NAME = QueueUtils.getQueueName(new TaskModel());

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private WorkflowExecutor workflowExecutor;
    private WorkflowDef workflowDef;

    @Before
    public void setUp() {
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAOImpl.class);
        metadataDAO = mock(MetadataDAO.class);
        TaskModelDAO taskModelDAO = mock(TaskModelDAO.class);
        TaskDefDAO taskDefDAO = mock(TaskDefDAO.class);
        WorkflowDefDAO workflowDefDAO = mock(WorkflowDefDAO.class);
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        when(applicationContext.getBean(TaskModelDAO.class)).thenReturn(taskModelDAO);
        when(applicationContext.getBean(TaskDefDAO.class)).thenReturn(taskDefDAO);
        when(applicationContext.getBean(WorkflowDefDAO.class)).thenReturn(workflowDefDAO);
        when(applicationContext.getBean(Executor.class)).thenReturn(new ThreadPoolTaskExecutor());

        workflowExecutor = new WorkflowExecutor(executionDAOFacade, metadataDAO, applicationContext);

        asyncSystemTaskExecutor =
                new AsyncSystemTaskExecutor(
                        executionDAOFacade, queueDAO, metadataDAO, new ConductorProperties(),
                        workflowExecutor);

        workflowDef =
                new WorkflowDef()
                        .setName(WORKFLOW_ID)
                        .setVersion(1)
                        .setTasks(Collections.singletonList(Map.of("type", TASK_TYPE)));
    }

    @Test
    public void testExecute_TaskNotFound() {
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(null);

        asyncSystemTaskExecutor.execute(new TestWorkflowSystemTask(), TASK_ID);

        Mockito.verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_TaskCompleted() {
        TaskModel taskModel = createTaskModel(TASK_ID, TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(taskModel);

        asyncSystemTaskExecutor.execute(new TestWorkflowSystemTask(), TASK_ID);

        Mockito.verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_TaskFailed() {
        TaskModel taskModel = createTaskModel(TASK_ID, TaskModel.Status.FAILED);
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(taskModel);

        asyncSystemTaskExecutor.execute(new TestWorkflowSystemTask(), TASK_ID);

        Mockito.verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_TaskScheduled() throws InterruptedException {
        TaskModel taskModel = createTaskModel(TASK_ID, TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(taskModel);
        when(executionDAOFacade.exceedsInProgressLimit(any(TaskModel.class)))
                .thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(
                any(TaskModel.class), anyString()))
                .thenReturn(false);

        CountDownLatch latch = new CountDownLatch(1);
        TestWorkflowSystemTask testWorkflowSystemTask =
                new TestWorkflowSystemTask() {
                    @Override
                    public void execute(WorkflowModel workflow, TaskModel task,
                                        WorkflowExecutor workflowExecutor) {
                        latch.countDown();
                    }
                };

        asyncSystemTaskExecutor.execute(testWorkflowSystemTask, TASK_ID);

        latch.await();
        Mockito.verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_TaskInProgress() {
        TaskModel taskModel = createTaskModel(TASK_ID, TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(taskModel);

        asyncSystemTaskExecutor.execute(new TestWorkflowSystemTask(), TASK_ID);

        // Task should stay in queue
        Mockito.verify(queueDAO, Mockito.never()).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_TaskAsyncComplete() {
        TaskModel taskModel = createTaskModel(TASK_ID, TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(taskModel);

        asyncSystemTaskExecutor.execute(new TestWorkflowSystemTask(true), TASK_ID);

        Mockito.verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_WorkflowCompleted() {
        TaskModel taskModel = createTaskModel(TASK_ID, TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, false))
                .thenReturn(new WorkflowModel().setStatus(WorkflowModel.State.COMPLETED));

        asyncSystemTaskExecutor.execute(new TestWorkflowSystemTask(), TASK_ID);

        Mockito.verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_WorkflowFailed() {
        TaskModel taskModel = createTaskModel(TASK_ID, TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, false))
                .thenReturn(new WorkflowModel().setStatus(WorkflowModel.State.FAILED));

        asyncSystemTaskExecutor.execute(new TestWorkflowSystemTask(), TASK_ID);

        Mockito.verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testExecute_Exception() {
        TaskModel taskModel = createTaskModel(TASK_ID, TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(taskModel);
        when(executionDAOFacade.getWorkflowModel(WORKFLOW_ID, false)).thenThrow(RuntimeException.class);

        asyncSystemTaskExecutor.execute(new TestWorkflowSystemTask(), TASK_ID);

        Mockito.verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
    }

    @Test
    public void testPostponeQuietly() {
        asyncSystemTaskExecutor.postponeQuietly(QUEUE_NAME, createTaskModel(TASK_ID,
                TaskModel.Status.IN_PROGRESS));

        Mockito.verify(queueDAO).postpone(QUEUE_NAME, TASK_ID, 0, queueTaskMessagePostponeSecs);
    }

    @Test
    public void testLoadTaskQuietly_TaskExists() {
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(createTaskModel(TASK_ID,
                TaskModel.Status.IN_PROGRESS));

        TaskModel taskModel = asyncSystemTaskExecutor.loadTaskQuietly(TASK_ID);

        assertNotNull(taskModel);
    }

    @Test
    public void testLoadTaskQuietly_TaskDoesNotExist() {
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(null);

        TaskModel taskModel = asyncSystemTaskExecutor.loadTaskQuietly(TASK_ID);

        assertNull(taskModel);
    }

    private TaskModel createTaskModel(String taskId, TaskModel.Status status) {
        return new TaskModel().setTaskId(taskId).setStatus(status);
    }
}