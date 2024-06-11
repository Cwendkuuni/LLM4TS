package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Component
public class AsyncSystemTaskExecutorTest {

    private final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutorTest.class);

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

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() throws ExecutionException, InterruptedException {
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
        String taskId = "task-1";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(queueUtils.getQueueName(task)).thenReturn("queue-1");
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        verify(workflowSystemTask, times(1)).start(any(), any(), any());
    }

    @Test
    public void testExecuteWithTerminalTask() {
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
        String taskId = "task-1";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.FAILED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        verify(queueDAO, times(1)).remove(any(), any());
    }

    @Test
    public void testExecuteWithPostponeTask() {
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
        String taskId = "task-1";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(queueUtils.getQueueName(task)).thenReturn("queue-1");
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        verify(queueDAO, times(1)).postpone(any(), any(), any(), any());
    }

    @Test
    public void testExecuteWithInProgressTask() {
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
        String taskId = "task-1";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        verify(workflowSystemTask, times(1)).execute(any(), any(), any());
    }

    @Test
    public void testExecuteWithException() {
        WorkflowSystemTask workflowSystemTask = mock(WorkflowSystemTask.class);
        String taskId = "task-1";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        doThrow(new RuntimeException("Test exception")).when(workflowSystemTask).execute(any(), any(), any());
        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);
        verify(executionDAOFacade, times(1)).updateTask(any());
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "queue-1";
        TaskModel task = new TaskModel();
        task.setTaskId("task-1");
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);
        verify(queueDAO, times(1)).postpone(any(), any(), any(), any());
    }

    @Test
    public void testLoadTaskQuietly() {
        String taskId = "task-1";
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(new TaskModel());
        TaskModel task = asyncSystemTaskExecutor.loadTaskQuietly(taskId);
        assertEquals(new TaskModel(), task);
    }
}