import com.netflix.conductor.core.dal.ExecutionDAOFacade;
import com.netflix.conductor.core.dal.MetadataDAO;
import com.netflix.conductor.core.dal.QueueDAO;
import com.netflix.conductor.core.execution.model.WorkflowModel;
import com.netflix.conductor.core.execution.model.WorkflowSystemTask;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTaskTest;
import com.netflix.conductor.core.execution.tasks.WorkflowSystemTaskTest.TestWorkflowSystemTask;
import com.netflix.conductor.core.utils.QueueUtils;
import com.netflix.conductor.dao.QueueDAOImpl;
import com.netflix.conductor.dao.TaskModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

@org.junit.jupiter.api.ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class AsyncSystemTaskExecutorTest {

 @Mock
 private ExecutionDAOFacade executionDAOFacade;
 @Mock
 private QueueDAO queueDAO;
 @Mock
 private MetadataDAO metadataDAO;
 @Mock
 private QueueUtils queueUtils;

 @InjectMocks
 private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

 private TaskModel taskModel;
 private WorkflowModel workflowModel;
 private WorkflowSystemTask workflowSystemTask;
 private TestWorkflowSystemTask testWorkflowSystemTask;

 @BeforeEach
 public void init() {
 MockitoAnnotations.openMocks(this);

 applicationContext =
 new AnnotationConfigApplicationContext(TestConfig.class);
 asyncSystemTaskExecutor = applicationContext.getBean(AsyncSystemTaskExecutor.class);

 workflowModel = new WorkflowModel();
 workflowModel.setStatus(WorkflowModel.WorkflowStatus.RUNNING);

 taskModel = new TaskModel();
 taskModel.setWorkflowInstanceId("workflowInstanceId");
 taskModel.setStatus(TaskModel.Status.SCHEDULED);

 workflowSystemTask = new WorkflowSystemTask() {
 @Override
 public void execute(WorkflowModel workflow, TaskModel task, WorkflowExecutor workflowExecutor) {}
 };

 testWorkflowSystemTask = new TestWorkflowSystemTask();
 }

 @Test
 public void execute_taskModelIsNull_logErrorAndReturn() {
 asyncSystemTaskExecutor.execute(workflowSystemTask, null);

 verify(queueDAO, times(1)).remove(QueueUtils.getQueueName(taskModel), taskModel.getTaskId());
 }

 @Test
 public void execute_taskModelIsTerminal_logInfoAndReturn() {
 taskModel.setStatus(TaskModel.Status.FAILED);

 asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel);

 verify(queueDAO, times(1)).remove(QueueUtils.getQueueName(taskModel), taskModel.getTaskId());
 }

 @Test
 public void execute_taskModelIsScheduledAndExceedsInProgressLimit_postponeTask() {
 when(executionDAOFacade.exceedsInProgressLimit(taskModel))
 .thenReturn(true);

 asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel);

 verify(queueUtils, times(1)).getQueueName(taskModel);
 verify(queueDAO, times(1)).postpone(
 QueueUtils.getQueueName(taskModel),
 taskModel.getTaskId(),
 taskModel.getWorkflowPriority(),
 queueTaskMessagePostponeSecs);
 }

 @Test
 public void execute_taskModelIsScheduledAndExceedsRateLimitPerFrequency_postponeTask() {
 when(executionDAOFacade.exceedsRateLimitPerFrequency(
 taskModel, metadataDAO.getTaskDef(taskModel.getTaskDefName())))
 .thenReturn(true);

 asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel);

 verify(queueUtils, times(1)).getQueueName(taskModel);
 verify(queueDAO, times(1)).postpone(
 QueueUtils.getQueueName(taskModel),
 taskModel.getTaskId(),
 taskModel.getWorkflowPriority(),
 queueTaskMessagePostponeSecs);
 }

 @Test
 public void execute_taskModelIsNotScheduledAndWorkflowIsTerminal_logInfoAndCleanup() {
 taskModel.setStatus(TaskModel.Status.IN_PROGRESS);
 workflowModel.setStatus(WorkflowModel.WorkflowStatus.FAILED);

 asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel);

 verify(queueDAO, times(1)).remove(QueueUtils.getQueueName(taskModel), taskModel.getTaskId());
 }

 @Test
 public void execute_taskModelIsScheduledAndWorkflowIsTerminal_logInfoAndCleanup() {
 taskModel.setStatus(TaskModel.Status.SCHEDULED);
 workflowModel.setStatus(WorkflowModel.WorkflowStatus.FAILED);

 asyncSystemTaskExecutor.execute(workflowSystemTask, taskModel);

 verify(queueDAO, times(1)).remove(QueueUtils.getQueueName(taskModel), taskModel.getTaskId());
 }

 @Test
 public void execute_taskModelIsScheduledAndWorkflowIsNotTerminal_executeSystemTask() {
 when(metadataDAO.getTaskDef(taskModel.getTaskDefName()))
 .thenReturn(
 new com.netflix.conductor.dao.TaskDef() {
 @Override
 public int getRateLimitPerFrequency() {
 return 0;
 }
 });

 asyncSystemTaskExecutor.execute(testWorkflowSystemTask, taskModel);

 verify(testWorkflowSystemTask, times(1)).execute(workflowModel, taskModel, any());
 }

 private static class TestConfig {
 @Bean
 public QueueDAO queueDAO() {
 return mock(QueueDAOImpl.class);
 }

 @Bean
 public ExecutionDAOFacade executionDAOFacade() {
 return mock(ExecutionDAOFacade.class);
 }

 @Bean
 public MetadataDAO metadataDAO() {
 return mock(MetadataDAO.class);
 }

 @Bean
 public QueueUtils queueUtils() {
 return mock(QueueUtils.class);
 }

 @Bean
 public AsyncSystemTaskExecutor asyncSystemTaskExecutor(
 ExecutionDAOFacade executionDAOFacade,
 QueueDAO queueDAO,
 MetadataDAO metadataDAO,
 ConductorProperties conductorProperties,
 WorkflowExecutor workflowExecutor) {
 return new AsyncSystemTaskExecutor(
 executionDAOFacade, queueDAO, metadataDAO, conductorProperties, workflowExecutor);
 }

 @Bean
 public WorkflowExecutor workflowExecutor() {
 return mock(WorkflowExecutor.class);
 }

 @Bean
 public ConductorProperties conductorProperties() {
 return new ConductorProperties() {
 @Override
 public TimeUnit getTimeUnit() {
 return TimeUnit.SECONDS;
 }

 @Override
 public int getSystemTaskWorkerCallbackDuration() {
 return 10;
 }

 @Override
 public int getTaskExecutionPostponeDuration() {
 return 10;
 }
 };
 }

 @Bean
 public ScheduledExecutorService scheduledExecutorService() {
 return new ScheduledThreadPoolExecutor(
 10,
 (Runnable command, long delay, TimeUnit unit) -> {
 Thread thread = new Thread(command, "test-scheduled-thread");
 thread.setDaemon(false);
 return thread;
 });
 }

 @Bean
 public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
 ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
 threadPoolTaskScheduler.setPoolSize(10);
 threadPoolTaskScheduler.setThreadNamePrefix("test-thread-pool-");
 threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(true);
 threadPoolTaskScheduler.setAwaitTerminationSeconds(60);
 threadPoolTaskScheduler.initialize();
 return threadPoolTaskScheduler;
 }

 @Bean
 public ApplicationContext applicationContext() {
 ApplicationContext context = new AnnotationConfigApplicationContext();
 context.register(TestConfig.class);
 context.refresh();
 return context;
 }
 }

 private ApplicationContext applicationContext;

 private static final long queueTaskMessagePostponeSecs = 10;
}