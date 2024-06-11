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
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@Component
public class AsyncSystemTaskExecutorTest {

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private QueueUtils queueUtils;

    @Mock
    private WorkflowExecutor workflowExecutor;

    private TaskModel task;
    private WorkflowModel workflow;
    private ConcurrentHashMap<String, TaskModel> taskMap;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        task = new TaskModel();
        task.setTaskId("task-1");
        task.setTaskType("taskType");
        task.setWorkflowInstanceId("workflowInstanceId");
        task.setStatus(TaskModel.Status.SCHEDULED);

        workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.Status.RUNNING);

        taskMap = new ConcurrentHashMap<>();
        taskMap.put(task.getTaskId(), task);

        when(executionDAOFacade.getTaskModel(task.getTaskId())).thenReturn(task);

        when(queueUtils.getQueueName(task)).thenReturn("queueName");

        when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(new WorkflowSystemTask());

        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(false);

        when(executionDAOFacade.exceedsRateLimitPerFrequency(
                task,
                new WorkflowSystemTask()))
                .thenReturn(false);
    }

    @Test
    public void testExecute() {
        asyncSystemTaskExecutor.execute(new WorkflowSystemTask(), task.getTaskId());

        Mockito.verify(executionDAOFacade, Mockito.times(1))
                .updateTask(task);
    }

    @Test
    public void testPostponeQuietly() {
        asyncSystemTaskExecutor.postponeQuietly("queueName", task);

        Mockito.verify(queueDAO, Mockito.times(1))
                .postpone("queueName",
                        task.getTaskId(),
                        task.getWorkflowPriority(),
                        asyncSystemTaskExecutor.queueTaskMessagePostponeSecs);
    }

    @Test
    public void testLoadTaskQuietly() {
        asyncSystemTaskExecutor.loadTaskQuietly(task.getTaskId());

        Mockito.verify(executionDAOFacade, Mockito.times(1))
                .getTaskModel(task.getTaskId());
    }
}