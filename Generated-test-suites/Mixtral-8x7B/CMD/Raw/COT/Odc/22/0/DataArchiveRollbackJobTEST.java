import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringSprintfTriggerFactory;

@Slf4j
public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private Scheduler scheduler;
    private JobFactory jobFactory;

    @Before
    public void setup() throws Exception {
        SchedulerFactory schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory
                .setConfigLocation(new ClassPathResource("/quartz.properties"));

        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/application.properties"));
        schedulerFactory.setQuartzProperties(propertiesFactoryBean.getObject());

        schedulerFactory.afterPropertiesSet();
        scheduler = schedulerFactory.getScheduler();

        jobFactory = new AdaptableJobFactory();
        jobFactory.afterPropertiesSet();
        scheduler.setJobFactory(jobFactory);

        dataArchiveRollbackJob = new DataArchiveRollbackJob();
    }

    @Test
    public void testExecute() throws Exception {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setJobName("testJobName");
        taskEntity.setJobGroup("testJobGroup");
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
        taskEntity.setResultJson("[]");

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(2L);

        when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(taskEntity));

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getResult()).thenReturn(taskEntity);

        dataArchiveRollbackJob.execute(context);
    }

    @Test(expected = Exception.class)
    public void testExecuteWithDataArchiveTaskNotFound() throws Exception {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setJobName("testJobName");
        taskEntity.setJobGroup("testJobGroup");
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
        taskEntity.setResultJson("[]");

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(2L);

        when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.empty());

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getResult()).thenReturn(taskEntity);

        dataArchiveRollbackJob.execute(context);
    }

    @Test
    public void testExecuteWithPrepareTasks() throws Exception {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setJobName("testJobName");
        taskEntity.setJobGroup("testJobGroup");
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
        taskEntity.setResultJson("[{\"id\": 1, \"sourceDatabaseId\": 3, \"targetDatabaseId\": 4, \"jobType\": \"JOB_TYPE\"}]");

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(2L);

        when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(taskEntity));

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getResult()).thenReturn(taskEntity);

        dataArchiveRollbackJob.execute(context);
    }

    @Test
    public void testUpdateStatusById() throws Exception {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setStatus(TaskStatus.PREPARING);

        dataArchiveRollbackJob.updateStatusById(taskEntity.getId(), TaskStatus.FAILED);

        // Verify that updateStatusById was called with correct arguments
        // Add your verification logic here
    }

    @Test
    public void testExecuteTask() throws Exception {
        dataArchiveRollbackJob.executeTask(1L, createDlmTasks());

        // Verify that executeTask was called with correct arguments
        // Add your verification logic here
    }

    @Test
    public void testGetTaskStatus() throws Exception {
        List<DlmTask> dlmTasks = createDlmTasks();

        TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(dlmTasks);

        // Verify that getTaskStatus returns correct values based on dlmTasks statuses
        // Add your verification logic here
    }

    private List<DlmTask> createDlmTasks() {
        return List.of(
                new DlmTask()
                        .setId(1L)
                        .setSourceDatabaseId(2L)
                        .setTargetDatabaseId(3L)
                        .setJobType(JobType.MIGRATE)
                        .setStatus(TaskStatus.PREPARING),
                new DlmTask()
                        .setId(2L)
                        .setSourceDatabaseId(3L)
                        .setTargetDatabaseId(2L)
                        .setJobType(JobType.ROLLBACK)
                        .setStatus(TaskStatus.DONE));
    }
}