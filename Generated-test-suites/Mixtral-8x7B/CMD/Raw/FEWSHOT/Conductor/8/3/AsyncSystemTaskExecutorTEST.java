package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.Mockito.*;

@Component
public class AsyncSystemTaskExecutorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutor.class);

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);

        ConductorProperties conductorProperties = new ConductorProperties();
        conductorProperties.setSystemTaskWorkerCallbackDuration(60);
        conductorProperties.setTaskExecutionPostponeDuration(60);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                conductorProperties,
                mock(WorkflowExecutor.class)
        );
    }

    @Test
    public void execute_taskNotFound_shouldLogErrorAndReturn() {
        String taskId = "task-1";
        TaskModel task = null;

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(LOGGER).error("TaskId: {} could not be found while executing {}", taskId, any());
        verifyNoMoreInteractions(executionDAOFacade, queueDAO);
    }

    @Test
    public void execute_taskAlreadyCompleted_shouldRemoveFromQueueAndReturn() {
        String taskId = "task-1";
        TaskModel task = createTask(taskId, TaskModel.Status.COMPLETED);
        String queueName = QueueUtils.getQueueName(task);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO).remove(queueName, taskId);
        verifyNoMoreInteractions(executionDAOFacade, queueDAO);
    }

    @Test
    public void execute_taskExceedsInProgressLimit_shouldPostponeAndReturn() {
        String taskId = "task-1";
        TaskModel task = createTask(taskId, TaskModel.Status.SCHEDULED);
        String queueName = QueueUtils.getQueueName(task);

        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO).postpone(queueName, taskId, task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
        verifyNoMoreInteractions(executionDAOFacade, queueDAO);
    }

    @Test
    public void execute_taskExceedsRateLimit_shouldPostponeAndReturn() {
        String taskId = "task-1";
        TaskModel task = createTask(taskId, TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.exceedsRateLimitPerFrequency(task, any())).thenReturn(true);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(queueDAO).postpone(queueName, taskId, task.getWorkflowPriority(), queueTaskMessagePostponeSecs);
        verifyNoMoreInteractions(executionDAOFacade, queueDAO);
    }

    @Test
    public void execute_workflowCompleted_shouldRemoveTaskAndDecideWorkflow() {
        String taskId = "task-1";
        TaskModel task = createTask(taskId, TaskModel.Status.IN_PROGRESS);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.WorkflowStatus.COMPLETED);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(executionDAOFacade).updateTask(task);
        verify(queueDAO).remove(queueName, taskId);
        verify(workflowExecutor).decide(task.getWorkflowInstanceId());
    }

    @Test
    public void execute_taskExecuted_shouldUpdateTaskAndPostpone() {
        String taskId = "task-1";
        TaskModel task = createTask(taskId, TaskModel.Status.IN_PROGRESS);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.WorkflowStatus.RUNNING);

        asyncSystemTaskExecutor.execute(mock(WorkflowSystemTask.class), taskId);

        verify(executionDAOFacade).updateTask(task);
        verify(queueDAO).postpone(queueName, taskId, task.getWorkflowPriority(), systemTaskCallbackTime);
    }

    private TaskModel createTask(String taskId, TaskModel.Status status) {
        ConcurrentHashMap<String, Object> input = new ConcurrentHashMap<>();
        input.put("key", "value");

        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setTaskType("type");
        task.setWorkflowInstanceId("workflow-1");
        task.setStatus(status);
        task.setInput(input);
        task.setWorkflowPriority(1);

        return task;
    }
}