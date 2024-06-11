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
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AsyncSystemTaskExecutorTest {

    private static final String TASK_ID = "123";
    private static final String WORKFLOW_ID = "456";
    private static final String QUEUE_NAME = "test-queue";
    private static final String TASK_TYPE = "test-task";

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Captor
    private ArgumentCaptor<TaskModel> taskModelCaptor;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private WorkflowSystemTask workflowSystemTask;
    private Logger logger;

    @Before
    public void setUp() {
        logger = LoggerFactory.getLogger(AsyncSystemTaskExecutor.class);
        asyncSystemTaskExecutor =
                new AsyncSystemTaskExecutor(
                        executionDAOFacade, queueDAO, metadataDAO, new ConductorProperties(),
                        workflowExecutor);
        workflowSystemTask = mock(WorkflowSystemTask.class);
    }

    @Test
    public void testExecute_TaskDoesNotExist() {
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(null);

        asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

        verify(queueDAO).remove(TASK_TYPE, TASK_ID);
        verifyNoMoreInteractions(executionDAOFacade);
        verifyNoMoreInteractions(metadataDAO);
        verifyNoMoreInteractions(workflowExecutor);
    }

    @Test
    public void testExecute_TaskIsCompleted() {
        TaskModel task = createTaskModel(TaskModel.Status.COMPLETED);
        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(task);

        asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

        verify(queueDAO).remove(QUEUE_NAME, TASK_ID);
        verifyNoMoreInteractions(executionDAOFacade);
        verifyNoMoreInteractions(metadataDAO);
        verifyNoMoreInteractions(workflowExecutor);
    }

    @Test
    public void testExecute_TaskIsScheduled_TaskExecutionLimitExceeded() {
        TaskModel task = createTaskModel(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

        verify(queueDAO).postpone(QUEUE_NAME, TASK_ID, task.getWorkflowPriority(),
                queueTaskMessagePostponeSecs);
        verifyNoMoreInteractions(executionDAOFacade);
        verifyNoMoreInteractions(metadataDAO);
        verifyNoMoreInteractions(workflowExecutor);
    }

    @Test
    public void testExecute_TaskIsScheduled_RateLimitExceeded() {
        TaskModel task = createTaskModel(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, task))
                .thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

        verify(queueDAO).postpone(QUEUE_NAME, TASK_ID, task.getWorkflowPriority(),
                queueTaskMessagePostponeSecs);
        verifyNoMoreInteractions(executionDAOFacade);
        verifyNoMoreInteractions(metadataDAO);
        verifyNoMoreInteractions(workflowExecutor);
    }

    @Test
    public void testExecute_TaskIsInProgress() throws Exception {
        TaskModel task = createTaskModel(TaskModel.Status.IN_PROGRESS);

        when(executionDAOFacade.getTaskModel(TASK_ID)).thenReturn(task);

        asyncSystemTaskExecutor.execute(workflowSystemTask, TASK_ID);

        verify(workflowSystemTask).execute(any(WorkflowModel.class), taskModelCaptor.capture(),
                any(WorkflowExecutor.class));
        TaskModel capturedTask = taskModelCaptor.getValue();
        assertThat(capturedTask, notNullValue());
        assertThat(capturedTask.getId(), equalTo(TASK_ID));

        verifyNoMoreInteractions(executionDAOFacade);
        verifyNoMoreInteractions(queueDAO);
        verifyNoMoreInteractions(metadataDAO);
    }

    private TaskModel createTaskModel(TaskModel.Status status) {
        return TaskModel.builder()
                .id(TASK_ID)
                .status(status)
                .workflowInstanceId(WORKFLOW_ID)
                .queue(QUEUE_NAME)
                .taskType(TASK_TYPE)
                .callbackAfterSeconds(0)
                .build();
    }
}

@Component
class ConductorProperties {

    public long getSystemTaskWorkerCallbackDuration() {
        return 0;
    }

    public long getTaskExecutionPostponeDuration() {
        return 0;
    }
}