
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
import org.springframework.stereotype.Component;

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
    private Logger logger;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() {
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        String taskId = "taskId";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        String queueName = QueueUtils.getQueueName(task);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        verify(queueDAO, times(1)).remove(queueName, taskId);
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "queueName";
        TaskModel task = new TaskModel();
        task.setTaskId("taskId");
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);
        verify(queueDAO, times(1)).postpone(queueName, "taskId", task.getWorkflowPriority(), 
                asyncSystemTaskExecutor.queueTaskMessagePostponeSecs);
    }

    @Test
    public void testLoadTaskQuietly_null() {
        String taskId = "taskId";
        when(executionDAOFacade.getTaskModel(taskId)).thenThrow(new RuntimeException());
        assertNull(asyncSystemTaskExecutor.loadTaskQuietly(taskId));
    }

}
