import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private ScheduleTaskEntity scheduleTaskEntity;
    private JobExecutionContext jobExecutionContext;
    private Scheduler scheduler;
    private MethodInvokingJobDetailFactoryBean jobDetailFactoryBean;
    private JobKey jobKey;

    @Before
    public void setUp() throws SchedulerException {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("testJobName");
        scheduleTaskEntity.setJobGroup("testJobGroup");
        scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\":10}");
        scheduleTaskEntity.setResultJson("[]");

        jobExecutionContext = mock(JobExecutionContext.class);
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);

        scheduler = mock(Scheduler.class);
        jobDetailFactoryBean = mock(MethodInvokingJobDetailFactoryBean.class);
        jobKey = new JobKey("testJobName", "testJobGroup");

        when(jobDetailFactoryBean.getJobClass()).thenReturn(DataArchiveRollbackJob.class);
        when(jobDetailFactoryBean.getJobDataMap()).thenReturn(jobExecutionContext.getMergedJobDataMap());
        when(scheduler.checkExists(jobKey)).thenReturn(true);
    }

    @Test
    public void testExecuteSuccess() throws JobExecutionException, SchedulerException {
        List<DlmTask> dlmTasks = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId(1L);
        dlmTask.setSourceDatabaseId(2L);
        dlmTask.setTargetDatabaseId(3L);
        dlmTask.setJobType(JobType.ROLLBACK);
        dlmTasks.add(dlmTask);

        when(scheduleTaskRepository.findById(eq(10L))).thenReturn(Optional.of(new ScheduleTaskEntity()));

        when(jobExecutionContext.getScheduler()).thenReturn(scheduler);

        when(scheduleTaskRepository.updateStatusById(eq(1L), any(TaskStatus.class)))
                .thenReturn(1);

        when(scheduleTaskRepository.findById(eq(1L))).thenReturn(Optional.of(scheduleTaskEntity));

        when(jobExecutionContext.getMergedJobDataMap()).thenReturn(jobDetailFactoryBean.getJobDataMap());

        when(scheduler.triggerJob(jobKey)).thenReturn(true);

        when(scheduleTaskRepository.executeTask(eq(1L), anyList())).thenReturn(1);

        when(scheduleTaskRepository.getTaskStatus(anyList())).thenReturn(TaskStatus.SUCCESS);

        DataArchiveRollbackParameters rollbackParameters = JsonUtils.fromJson(scheduleTaskEntity.getParametersJson(),
                DataArchiveRollbackParameters.class);

        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        Assert.assertEquals(TaskStatus.SUCCESS, scheduleTaskEntity.getStatus());
    }

    @Test
    public void testExecuteFailed() throws JobExecutionException, SchedulerException {
        when(scheduleTaskRepository.findById(eq(10L))).thenReturn(Optional.of(new ScheduleTaskEntity()));

        when(jobExecutionContext.getScheduler()).thenReturn(scheduler);

        when(scheduleTaskRepository.updateStatusById(eq(1L), any(TaskStatus.class)))
                .thenReturn(1);

        when(scheduleTaskRepository.findById(eq(1L))).thenReturn(Optional.of(scheduleTaskEntity));

        when(jobExecutionContext.getMergedJobDataMap()).thenReturn(jobDetailFactoryBean.getJobDataMap());

        when(scheduler.triggerJob(jobKey)).thenReturn(true);

        when(scheduleTaskRepository.executeTask(eq(1L), anyList())).thenReturn(1);

        when(scheduleTaskRepository.getTaskStatus(anyList())).thenReturn(TaskStatus.FAILED);

        DataArchiveRollbackParameters rollbackParameters = JsonUtils.fromJson(scheduleTaskEntity.getParametersJson(),
                DataArchiveRollbackParameters.class);

        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        Assert.assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
    }

    @Test
    public void testExecuteDataArchiveTaskNotFound() throws JobExecutionException, SchedulerException {
        when(scheduleTaskRepository.findById(eq(10L))).thenReturn(Optional.ofNullable(null));

        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        Assert.assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
    }

    @Test
    public void testExecuteSchedulerException() throws JobExecutionException, SchedulerException {
        when(scheduleTaskRepository.findById(eq(10L))).thenReturn(Optional.of(new ScheduleTaskEntity()));

        when(jobExecutionContext.getScheduler()).thenThrow(new SchedulerException("test"));

        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        Assert.assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
    }

    private Scheduler scheduleTaskRepository;

    static class ScheduleTaskRepository {

        public Optional<ScheduleTaskEntity> findById(Long id) {
            return Optional.ofNullable(null);
        }

        public int updateStatusById(Long id, TaskStatus status) {
            return 1;
        }

        public int executeTask(Long id, List<DlmTask> dlmTasks) {
            return 1;
        }

        public TaskStatus getTaskStatus(List<DlmTask> dlmTasks) {
            return TaskStatus.SUCCESS;
        }
    }
}