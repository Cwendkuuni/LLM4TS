import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.calendar.AnnualCalendar;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.JobDetailImpl;
import org.quartz.JobBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private AbstractDlmJob abstractDlmJob;
    private Scheduler scheduler;
    private JobExecutionContext jobExecutionContext;
    private ScheduleTaskEntity scheduleTaskEntity;
    private DataArchiveClearParameters dataArchiveClearParameters;
    private List<DlmTask> taskUnits;

    @Before
    public void setup() throws SchedulerException {
        scheduler = mock(Scheduler.class);
        abstractDlmJob = mock(AbstractDlmJob.class);
        dataArchiveDeleteJob = spy(new DataArchiveDeleteJob() {
            @Override
            protected AbstractDlmJob getAbstractDlmJob() {
                return abstractDlmJob;
            }
        });
        jobExecutionContext = mock(JobExecutionContext.class);
        scheduleTaskEntity = mock(ScheduleTaskEntity.class);
        dataArchiveClearParameters = new DataArchiveClearParameters();
        taskUnits = new ArrayList<>();
    }

    @Test
    public void testExecute_dataArchiveTaskNotFound() {
        dataArchiveClearParameters.setDataArchiveTaskId("123");
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_dataArchiveTaskNotDone() {
        dataArchiveClearParameters.setDataArchiveTaskId("123");
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.RUNNING);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_prepareTasksForClear() {
        dataArchiveClearParameters.setDataArchiveTaskId("123");
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.DONE);
        dataArchiveTask.setResultJson("[]");
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(dataArchiveDeleteJob, times(1)).executeTask(scheduleTaskEntity.getId(), taskUnits);
        verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.PREPARING);
    }

    @Test
    public void testExecuteTask_taskUnitsEmpty() {
        dataArchiveDeleteJob.executeTask(1L, new ArrayList<>());

        verify(scheduler, times(0)).scheduleJob(any(JobDetailImpl.class), any(CronTriggerImpl.class));
    }

    @Test
    public void testExecuteTask_taskUnitsNotEmpty() throws SchedulerException {
        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setId("1");
        dlmTask1.setJobType(JobType.DELETE);
        dlmTask1.setStatus(TaskStatus.PREPARING);

        DlmTask dlmTask2 = new DlmTask();
        dlmTask2.setId("2");
        dlmTask2.setJobType(JobType.DELETE);
        dlmTask2.setStatus(TaskStatus.PREPARING);

        taskUnits.add(dlmTask1);
        taskUnits.add(dlmTask2);

        JobDetailImpl jobDetail = JobBuilder.newJob(DataArchiveDeleteJob.class)
                .withIdentity("DataArchiveDeleteJob", "group1")
                .build();
        CronTriggerImpl cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity("DataArchiveDeleteJobTrigger", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * 1/1 * ? *"))
                .build();

        when(scheduler.scheduleJob(any(JobDetailImpl.class), any(CronTriggerImpl.class)))
                .thenReturn(true);

        dataArchiveDeleteJob.executeTask(1L, taskUnits);

        verify(scheduler, times(1)).scheduleJob(jobDetail, cronTrigger);
    }

    @Test
    public void testGetTaskStatus_taskUnitsAllFinishedSuccess() {
        taskUnits.add(new DlmTask());
        taskUnits.get(0).setStatus(TaskStatus.FINISHED);

        assertEquals(TaskStatus.FINISHED, dataArchiveDeleteJob.getTaskStatus(taskUnits));
    }

    @Test
    public void testGetTaskStatus_taskUnitsAllFinishedFailed() {
        taskUnits.add(new DlmTask());
        taskUnits.get(0).setStatus(TaskStatus.FAILED);

        assertEquals(TaskStatus.FAILED, dataArchiveDeleteJob.getTaskStatus(taskUnits));
    }

    @Test
    public void testGetTaskStatus_taskUnitsNotAllFinished() {
        taskUnits.add(new DlmTask());
        taskUnits.get(0).setStatus(TaskStatus.PREPARING);

        assertEquals(TaskStatus.PREPARING, dataArchiveDeleteJob.getTaskStatus(taskUnits));
    }
}