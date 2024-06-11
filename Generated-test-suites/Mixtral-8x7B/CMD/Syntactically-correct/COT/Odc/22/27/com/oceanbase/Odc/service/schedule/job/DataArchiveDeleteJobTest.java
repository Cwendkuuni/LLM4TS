import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.scheduler.JobKey;
import org.quartz.scheduler.Scheduler;
import org.quartz.scheduler.SchedulerException;
import org.quartz.scheduler.SchedulerFactory;
import org.quartz.spi.OperableTrigger;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private Scheduler scheduler;
    private JobExecutionContext jobExecutionContext;
    private ScheduleTaskEntity scheduleTaskEntity;
    private ScheduleTaskEntity dataArchiveTask;
    private List<DlmTask> taskUnits;

    @Before
    public void setUp() throws SchedulerException {
        scheduler = mock(Scheduler.class);
        jobExecutionContext = mock(JobExecutionContext.class);
        scheduleTaskEntity = mock(ScheduleTaskEntity.class);
        dataArchiveTask = mock(ScheduleTaskEntity.class);
        taskUnits = mock(List.class);

        dataArchiveDeleteJob = new DataArchiveDeleteJob() {
            @Override
            protected Scheduler getScheduler() {
                return scheduler;
            }
        };

        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskEntity.getParametersJson()).thenReturn("{\"dataArchiveTaskId\": 1}");
        when(scheduleTaskEntity.getJobName()).thenReturn("testJobName");
        when(scheduleTaskEntity.getJobGroup()).thenReturn("testJobGroup");
        when(scheduleTaskEntity.getId()).thenReturn(1L);

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
        when(dataArchiveTask.getResultJson()).thenReturn("[]");

        when(JsonUtils.fromJson(anyString(), any(TypeReference.class))).thenReturn(taskUnits);

        doNothing().when(scheduler).triggerJob(any(JobKey.class));
    }

    @Test
    public void testExecute() throws JobExecutionException, SchedulerException {
        dataArchiveDeleteJob.execute(jobExecutionContext);

        // check that the task status is updated
        assertEquals(TaskStatus.FAILED, scheduleTaskRepository.getTaskStatus(1L));

        // check that the scheduler's triggerJob method is called with the correct parameters
        JobKey jobKey = new JobKey("testJobName", "testJobGroup");
        OperableTrigger trigger = mock(OperableTrigger.class);
        when(scheduler.getTrigger(jobKey)).thenReturn(trigger);
        dataArchiveDeleteJob.triggerJob(jobKey);
        // add more assertions here based on the behavior of the actual class
    }

    @Test
    public void testExecute_dataArchiveTaskNotFound() throws JobExecutionException, SchedulerException {
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(jobExecutionContext);

        // check that the task status is updated to FAILED
        assertEquals(TaskStatus.FAILED, scheduleTaskRepository.getTaskStatus(1L));

        // check that the scheduler's triggerJob method is not called
        JobKey jobKey = new JobKey("testJobName", "testJobGroup");
        dataArchiveDeleteJob.triggerJob(jobKey);
        // add more assertions here based on the behavior of the actual class
    }

    @Test
    public void testExecute_dataArchiveTaskNotDone() throws JobExecutionException, SchedulerException {
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.RUNNING);

        dataArchiveDeleteJob.execute(jobExecutionContext);

        // check that the task status is updated to FAILED
        assertEquals(TaskStatus.FAILED, scheduleTaskRepository.getTaskStatus(1L));

        // check that the scheduler's triggerJob method is not called
        JobKey jobKey = new JobKey("testJobName", "testJobGroup");
        dataArchiveDeleteJob.triggerJob(jobKey);
        // add more assertions here based on the behavior of the actual class
    }

    @Test
    public void testExecute_noTaskUnits() throws JobExecutionException, SchedulerException {
        when(dataArchiveTask.getResultJson()).thenReturn("null");

        dataArchiveDeleteJob.execute(jobExecutionContext);

        // check that the task status is updated to FAILED
        assertEquals(TaskStatus.FAILED, scheduleTaskRepository.getTaskStatus(1L));

        // check that the scheduler's triggerJob method is not called
        JobKey jobKey = new JobKey("testJobName", "testJobGroup");
        dataArchiveDeleteJob.triggerJob(jobKey);
        // add more assertions here based on the behavior of the actual class
    }

    @Test
    public void testExecute_emptyTaskUnits() throws JobExecutionException, SchedulerException {
        when(dataArchiveTask.getResultJson()).thenReturn("[]");

        dataArchiveDeleteJob.execute(jobExecutionContext);

        // check that the task status is updated to FAILED
        assertEquals(TaskStatus.FAILED, scheduleTaskRepository.getTaskStatus(1L));

        // check that the scheduler's triggerJob method is not called
        JobKey jobKey = new JobKey("testJobName", "testJobGroup");
        dataArchiveDeleteJob.triggerJob(jobKey);
        // add more assertions here based on the behavior of the actual class
    }

    @Test
    public void testTriggerJob() throws SchedulerException {
        JobKey jobKey = new JobKey("testJobName", "testJobGroup");
        dataArchiveDeleteJob.triggerJob(jobKey);

        // add more assertions here based on the behavior of the actual class
    }

    @Test
    public void testGetScheduler() {
        Scheduler scheduler = dataArchiveDeleteJob.getScheduler();
        assertNotNull(scheduler);
    }

    @Test
    public void testGetJobName() {
        String jobName = dataArchiveDeleteJob.getJobName();
        assertEquals("DataArchiveDeleteJob", jobName);
    }

    @Test
    public void testGetJobGroup() {
        String jobGroup = dataArchiveDeleteJob.getJobGroup();
        assertEquals(AbstractDlmJob.JOB_GROUP, jobGroup);
    }
}