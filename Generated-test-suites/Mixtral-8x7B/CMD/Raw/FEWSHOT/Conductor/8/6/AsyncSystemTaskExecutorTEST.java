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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Component
public class AsyncSystemTaskExecutorTest {

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    private Logger logger;

    private WorkflowSystemTask workflowSystemTask;

    private TaskModel taskModel;

    private WorkflowModel workflowModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        logger = LoggerFactory.getLogger(AsyncSystemTaskExecutor.class);
        asyncSystemTaskExecutor =
                new AsyncSystemTaskExecutor(
                        executionDAOFacade, queueDAO, metadataDAO, new ConductorProperties(), mock(ExecutorService.class));

        workflowSystemTask = mock(WorkflowSystemTask.class);
        taskModel = mock(TaskModel.class);
        workflowModel = mock(WorkflowModel.class);

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(workflowModel.toShortString()).thenReturn("workflowModelToString");
    }

    @Test
    public void testExecute() {
        asyncSystemTaskExecutor.execute(workflowSystemTask, "taskId");
    }

    @Test
    public void testLoadTaskQuietly() {
        asyncSystemTaskExecutor.loadTaskQuietly("taskId");
    }

    @Test
    public void testPostponeQuietly() {
        asyncSystemTaskExecutor.postponeQuietly("queueName", taskModel);
    }

    @Test
    public void testGetQueueName() {
        String queueName = QueueUtils.getQueueName(taskModel);
        assertNotNull(queueName);
    }

    @Test
    public void testExceedsInProgressLimit() {
        when(executionDAOFacade.exceedsInProgressLimit(taskModel)).thenReturn(true);
        asyncSystemTaskExecutor.exceedsInProgressLimit(taskModel);
    }

    @Test
    public void testExceedsRateLimitPerFrequency() {
        when(executionDAOFacade.exceedsRateLimitPerFrequency(
                        taskModel, metadataDAO.getTaskDef(taskModel.getTaskDefName())))
                .thenReturn(true);
        asyncSystemTaskExecutor.exceedsRateLimitPerFrequency(taskModel, metadataDAO.getTaskDef("defName"));
    }

    @Test
    public void testDecide() {
        asyncSystemTaskExecutor.decide("workflowId");
    }
}