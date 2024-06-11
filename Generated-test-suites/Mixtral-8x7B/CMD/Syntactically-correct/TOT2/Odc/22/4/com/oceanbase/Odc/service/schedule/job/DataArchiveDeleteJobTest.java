import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.scheduler.GroupedScheduler;
import org.quartz.scheduler.Scheduler;
import org.quartz.scheduler.SchedulerException;
import org.quartz.scheduler.SchedulerFactory;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.quartz.spi.operators.DataArchiveDeleteJobFactory;
import org.quartz.spi.operators.ScheduleTaskRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class DataArchiveDeleteJobTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataArchiveDeleteJobTest.class);
    private static final String TASK_NAME = "testTask";
    private static final String TASK_GROUP = "testGroup";
    private static final String PARAMETERS_JSON = "{\"dataArchiveTaskId\": \"123\"}";
    private static final String RESULT_JSON = "[{\"id\": \"456\", \"jobType\": \"DELETE\"}]";
    private static final String DATA_ARCHIVE_TASK_ID = "123";
    private static final Long TASK_ID = 1L;
    private static final String JOB_NAME = "testJobName";
    private static final String JOB_GROUP = "testJobGroup";
    private static final JobFactory JOB_FACTORY = new DataArchiveDeleteJobFactory();

    @Autowired
    private ScheduleTaskRepositoryImpl scheduleTaskRepository;

    private Scheduler scheduler;

    @Before
    public void setUp() throws SchedulerException {
        SchedulerFactory schedulerFactory = new org.quartz.impl.StdSchedulerFactory();
        scheduler = schedulerFactory.getScheduler();
        scheduler.setJobFactory(JOB_FACTORY);
        scheduler.start();
    }

    // Helper method to create a JobExecutionContext
    private JobExecutionContext createJobExecutionContext(ScheduleTaskEntity taskEntity) {
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getMergedJobDataMap().getString("parametersJson")).thenReturn(taskEntity.getParametersJson());
        when(context.getMergedJobDataMap().getString("result")).thenReturn(taskEntity.getResultJson());
        when(context.getResult()).thenReturn(taskEntity);
        return context;
    }

    // Test method for execute(JobExecutionContext context)
    @Test
    public void testExecute() throws SchedulerException {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(TASK_ID);
        taskEntity.setName(TASK_NAME);
        taskEntity.setGroup(TASK_GROUP);
        taskEntity.setParametersJson(PARAMETERS_JSON);
        taskEntity.setResultJson(RESULT_JSON);

        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(DATA_ARCHIVE_TASK_ID);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);
        dataArchiveTask.setStatus(TaskStatus.DONE);

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId("456");
        taskUnits.add(dlmTask);

        when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(Optional.of(dataArchiveTask));

        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);

        JobExecutionContext context = createJobExecutionContext(taskEntity);

        dataArchiveDeleteJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(TASK_ID, TaskStatus.FAILED);
        verify(scheduleTaskRepository, times(1)).findById(DATA_ARCHIVE_TASK_ID);
        verifyNoMoreInteractions(scheduleTaskRepository);
    }

    // Test method for extractAndListAllPublicMethods()
    @Test
    public void testExtractAndListAllPublicMethods() {
        Class<DataArchiveDeleteJob> clazz = DataArchiveDeleteJob.class;
        assertEquals(1, ReflectionUtils.getAllDeclaredMethods(clazz).length);
    }

    // Test method for getTaskStatus(List<DlmTask> taskUnits)
    @Test
    public void testGetTaskStatus() {
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setStatus(TaskStatus.DONE);
        DlmTask dlmTask2 = new DlmTask();
        dlmTask2.setStatus(TaskStatus.FAILED);
        taskUnits.add(dlmTask1);
        taskUnits.add(dlmTask2);

        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);

        TaskStatus taskStatus = dataArchiveDeleteJob.getTaskStatus(taskUnits);

        assertEquals(TaskStatus.FAILED, taskStatus);
    }

    // Test method for executeTask(Long taskId, List<DlmTask> taskUnits)
    @Test
    public void testExecuteTask() throws SchedulerException {
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setId("456");
        dlmTask1.setJobType(JobType.DELETE);
        dlmTask1.setStatus(TaskStatus.PREPARING);
        taskUnits.add(dlmTask1);

        when(scheduler.getJobDetail(anyString(), anyString())).thenReturn(null);
        when(scheduler.getTrigger(anyString(), anyString())).thenReturn(null);

        Scheduler mockScheduler = mock(Scheduler.class);
        when(mockScheduler.getJobDetail(anyString(), anyString())).thenReturn(null);
        when(mockScheduler.getTrigger(anyString(), anyString())).thenReturn(null);

        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.setScheduler(mockScheduler);
        dataArchiveDeleteJob.executeTask(TASK_ID, taskUnits);

        verify(mockScheduler, times(1)).scheduleJob(any(org.quartz.JobDetail.class), any(org.quartz.Trigger.class));
        verifyNoMoreInteractions(mockScheduler);
    }

    // Test method for rollbackTask(Long taskId)
    @Test
    public void testRollbackTask() throws SchedulerException {
        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.setScheduler(scheduler);

        dataArchiveDeleteJob.rollbackTask(TASK_ID);

        GroupedScheduler groupedScheduler =
                (GroupedScheduler) scheduler.getSchedulerMetaData().getSchedulerInstance(JOB_NAME, JOB_GROUP);
        List<String> jobNames = groupedScheduler.getJobNames();

        assertEquals(0, jobNames.size());
    }
}