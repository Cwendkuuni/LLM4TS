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
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Configuration
@ComponentScan("com.netflix.conductor.core.execution")
public class AsyncSystemTaskExecutorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSystemTaskExecutorTest.class);

    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;
    private ExecutionDAOFacade executionDAOFacade;
    private QueueDAO queueDAO;
    private MetadataDAO metadataDAO;
    private WorkflowExecutor workflowExecutor;
    private WorkflowSystemTask workflowSystemTask;

    @Before
    public void setup() {
        ApplicationContext context = new AnnotationConfigApplicationContext(this.getClass());
        asyncSystemTaskExecutor = context.getBean(AsyncSystemTaskExecutor.class);
        executionDAOFacade = mock(ExecutionDAOFacade.class);
        queueDAO = mock(QueueDAO.class);
        metadataDAO = mock(MetadataDAO.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        workflowSystemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.executionDAOFacade = executionDAOFacade;
        asyncSystemTaskExecutor.queueDAO = queueDAO;
        asyncSystemTaskExecutor.metadataDAO = metadataDAO;
        asyncSystemTaskExecutor.workflowExecutor = workflowExecutor;

        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(1);
        taskExecutor.initialize();
        asyncSystemTaskExecutor.taskExecutor = taskExecutor;
    }

    @Test
    public void testExecute() throws InterruptedException {
        String taskId = "123";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(queueDAO.get(anyString(), anyString())).thenReturn(taskModel);
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyInt())).thenReturn(true);
        when(queueDAO.remove(anyString(), anyString())).thenReturn(true);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(workflowSystemTask, times(1)).isAsyncComplete(taskModel);
        verify(workflowSystemTask, times(1)).start(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(workflowSystemTask, times(1)).execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(executionDAOFacade, times(1)).updateTask(any(TaskModel.class));
        verifyNoMoreInteractions(workflowSystemTask, executionDAOFacade, queueDAO);
    }

    @Test
    public void testExecute_taskNotFound() throws InterruptedException {
        String taskId = "123";

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(workflowSystemTask, times(1)).isAsyncComplete(any(TaskModel.class));
        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verifyNoMoreInteractions(workflowSystemTask, executionDAOFacade, queueDAO);
    }

    @Test
    public void testExecute_terminalState() throws InterruptedException {
        String taskId = "123";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskModel.Status.FAILED);

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(workflowSystemTask, times(1)).isAsyncComplete(any(TaskModel.class));
        verify(queueDAO, times(1)).remove(anyString(), anyString());
        verifyNoMoreInteractions(workflowSystemTask, executionDAOFacade, queueDAO);
    }

    @Test
    public void testExecute_postponeQuietly() throws InterruptedException {
        String taskId = "123";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskModel.Status.SCHEDULED);

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);
        when(queueDAO.get(anyString(), anyString())).thenReturn(taskModel);
        when(queueDAO.postpone(anyString(), anyString(), anyInt(), anyInt())).thenReturn(false);
        doNothing().when(asyncSystemTaskExecutor).postponeQuietly(anyString(), any(TaskModel.class));

        asyncSystemTaskExecutor.execute(workflowSystemTask, taskId);

        verify(workflowSystemTask, times(1)).isAsyncComplete(taskModel);
        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
        verify(asyncSystemTaskExecutor, times(1)).postponeQuietly(anyString(), any(TaskModel.class));
        verifyNoMoreInteractions(workflowSystemTask, executionDAOFacade, queueDAO);
    }

    @Test
    public void testPostponeQuietly() throws InterruptedException {
        String queueName = "testQueue";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("123");
        taskModel.setStatus(TaskModel.Status.SCHEDULED);

        asyncSystemTaskExecutor.postponeQuietly(queueName, taskModel);

        verify(queueDAO, times(1)).postpone(anyString(), anyString(), anyInt(), anyInt());
        verifyNoMoreInteractions(workflowSystemTask, executionDAOFacade, queueDAO);
    }

    @Test
    public void testLoadTaskQuietly_taskFound() throws InterruptedException {
        String taskId = "123";
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(taskModel);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertEquals(taskModel, result);
        verify(executionDAOFacade, times(1)).getTaskModel(anyString());
        verifyNoMoreInteractions(workflowSystemTask, executionDAOFacade, queueDAO);
    }

    @Test
    public void testLoadTaskQuietly_taskNotFound() throws InterruptedException {
        String taskId = "123";

        when(executionDAOFacade.getTaskModel(anyString())).thenReturn(null);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNotNull(result);
        verify(executionDAOFacade, times(1)).getTaskModel(anyString());
        verifyNoMoreInteractions(workflowSystemTask, executionDAOFacade, queueDAO);
    }

    public static class WorkflowSystemTaskSpy extends WorkflowSystemTask {

        public WorkflowSystemTaskSpy(String name) {
            super(name);
        }

        @Override
        public boolean isAsyncComplete(TaskModel taskModel) {
            return super.isAsyncComplete(taskModel);
        }

        @Override
        public void start(WorkflowModel workflowModel, TaskModel taskModel, WorkflowExecutor workflowExecutor) {
            super.start(workflowModel, taskModel, workflowExecutor);
        }

        @Override
        public void execute(WorkflowModel workflowModel, TaskModel taskModel, WorkflowExecutor workflowExecutor) {
            super.execute(workflowModel, taskModel, workflowExecutor);
        }
    }
}