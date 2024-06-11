package com.netflix.conductor.core.execution;

import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTask;
import com.netflix.conductor.core.model.TaskModel;
import com.netflix.conductor.core.model.WorkflowModel;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@ComponentScan("com.netflix.conductor.core.execution")
@PropertySource("classpath:application.properties")
public class AsyncSystemTaskExecutorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutorTest.class);

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private WorkflowExecutor workflowExecutor;

    @Before
    public void setup() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AsyncSystemTaskExecutorTest.class);
        executionDAOFacade = context.getBean(ExecutionDAOFacade.class);
        queueDAO = context.getBean(QueueDAO.class);
        metadataDAO = context.getBean(MetadataDAO.class);
        workflowExecutor = context.getBean(WorkflowExecutor.class);

        asyncSystemTaskExecutor = new AsyncSystemTaskExecutor(
                executionDAOFacade,
                queueDAO,
                metadataDAO,
                new ConductorProperties(),
                workflowExecutor
        );

        ReflectionTestUtils.setField(asyncSystemTaskExecutor, "systemTaskCallbackTime", 1000L);
        ReflectionTestUtils.setField(asyncSystemTaskExecutor, "queueTaskMessagePostponeSecs", 500L);
    }

    @Test
    public void testExecute() {
        WorkflowSystemTask systemTask = Mockito.mock(WorkflowSystemTask.class);
        String taskId = "123";
        TaskModel task = createTaskModel(taskId, "SCHEDULED");

        Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        Mockito.when(queueDAO.get(QueueUtils.getQueueName(task), taskId))
                .thenReturn(Collections.singletonList(task));

        // Test when task is completed
        ReflectionTestUtils.setField(task, "status", TaskModel.Status.COMPLETED);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        Mockito.verify(queueDAO, Mockito.times(1)).remove(QueueUtils.getQueueName(task), taskId);

        // Test when task is not found
        Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        Mockito.verify(queueDAO, Mockito.times(0)).remove(QueueUtils.getQueueName(task), taskId);

        // Test when task is in terminal state
        ReflectionTestUtils.setField(task, "status", TaskModel.Status.FAILED);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        Mockito.verify(queueDAO, Mockito.times(1)).remove(QueueUtils.getQueueName(task), taskId);

        // Test when task is not in terminal state
        ReflectionTestUtils.setField(task, "status", TaskModel.Status.IN_PROGRESS);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        Mockito.verify(queueDAO, Mockito.times(0)).remove(QueueUtils.getQueueName(task), taskId);

        // Test when task is postponed
        ReflectionTestUtils.setField(task, "status", TaskModel.Status.SCHEDULED);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        Mockito.verify(queueDAO, Mockito.times(1)).postpone(
                QueueUtils.getQueueName(task),
                taskId,
                task.getWorkflowPriority(),
                asyncSystemTaskExecutor.getSystemTaskCallbackTime()
        );

        // Test when task is cancelled
        ReflectionTestUtils.setField(task, "status", TaskModel.Status.CANCELED);
        asyncSystemTaskExecutor.execute(systemTask, taskId);
        Mockito.verify(queueDAO, Mockito.times(1)).remove(QueueUtils.getQueueName(task), taskId);
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "testQueue";
        TaskModel task = createTaskModel("123", "SCHEDULED");
        asyncSystemTaskExecutor.postponeQuietly(queueName, task);
        Mockito.verify(queueDAO, Mockito.times(1)).postpone(
                queueName,
                task.getTaskId(),
                task.getWorkflowPriority(),
                asyncSystemTaskExecutor.getQueueTaskMessagePostponeSecs()
        );
    }

    @Test
    public void testLoadTaskQuietly() {
        String taskId = "123";
        TaskModel task = createTaskModel(taskId, "SCHEDULED");
        Mockito.when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        Assert.assertEquals(task, asyncSystemTaskExecutor.loadTaskQuietly(taskId));
        Mockito.verify(executionDAOFacade, Mockito.times(1)).getTaskModel(taskId);
    }

    private TaskModel createTaskModel(String taskId, String status) {
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.valueOf(status));
        task.setWorkflowInstanceId("1234");
        task.setTaskDefName("testTask");
        task.setWorkflowPriority(1);
        task.setQueue(QueueUtils.getQueueName(task));
        return task;
    }
}

@Component
class ConductorProperties {

    private final ConcurrentHashMap<String, Object> properties = new ConcurrentHashMap<>();

    public long getSystemTaskWorkerCallbackDuration() {
        return (long) properties.getOrDefault("systemTaskWorkerCallbackDuration", 1000L);
    }

    public long getTaskExecutionPostponeDuration() {
        return (long) properties.getOrDefault("taskExecutionPostponeDuration", 500L);
    }
}

@Component
class WorkflowExecutor {

    public void decide(String workflowId) {
    }
}