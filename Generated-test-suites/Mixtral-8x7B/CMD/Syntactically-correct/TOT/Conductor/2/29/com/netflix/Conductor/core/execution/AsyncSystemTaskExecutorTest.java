
package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.model.workflow.WorkflowExecution;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Configuration
@ComponentScan("com.netflix.conductor.core.execution")
public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @InjectMocks
    private AsyncSystemTaskExecutor executor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecuteSuccess() {
        // Arrange
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);
        TaskModel task = new TaskModel();
        task.setTaskId("1");
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        task.setWorkflowInstanceId("workflow1");

        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);
        when(queueDAO.remove(anyString(), anyString())).thenReturn(true);

        // Act
        executor.execute(systemTask, task.getTaskId());

        // Assert
        verify(queueDAO).remove(anyString(), anyString());
        verify(executionDAOFacade).updateTask(task);
        assertEquals(TaskModel.Status.COMPLETED, task.getStatus());
    }

    @Test
    public void testPostponeQuietlyFailure() {
        // Arrange
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyInt()))
                .thenThrow(new RuntimeException("Task postponement failed"));

        // Act & Assert
        executor.postponeQuietly("queue1", new TaskModel());

        verify(queueDAO).postpone(anyString(), anyString(), anyInt(), anyInt());
        verify(LOGGER).error(anyString(), any(RuntimeException.class));
    }

    @Test
    public void testLoadTaskQuietlyFailure() {
        // Arrange
        when(executionDAOFacade.getTaskModel(anyString())).thenThrow(new RuntimeException("Task load failed"));

        // Act & Assert
        assertNull(executor.loadTaskQuietly("task1"));

        verify(executionDAOFacade).getTaskModel(anyString());
    }
}

