import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
public class DataArchiveDeleteJobTest {

    @SpyBean
    private AbstractDlmJob abstractDlmJob;

    @SpyBean
    private ScheduleTaskRepository scheduleTaskRepository;

    @Autowired
    private JobScheduler jobScheduler;

    private JobExecutionContext jobExecutionContext;

    @Before
    public void setup() throws Exception {
        jobExecutionContext = jobScheduler.createJobExecutionContext(DataArchiveDeleteJob.class,
                new SimpleTriggerImpl("trigger1", "group1", SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(10).repeatForever()).getFireTimeAfter(new java.util.Date()));
        jobExecutionContext.setMerge(true);
        jobExecutionContext.put(JobExecutionContext.JOB_KEY, new JobKey("jobKey", "group1"));
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("taskId");
        String parametersJson = JsonUtils.toJson(dataArchiveClearParameters);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson(parametersJson);
        taskEntity.setStatus(TaskStatus.QUEUED);

        doReturn(Optional.empty()).when(scheduleTaskRepository).findById(eq(1L));

        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository).updateStatusById(eq(1L), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_DataArchiveTaskNotDone() throws JobExecutionException {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("taskId");
        String parametersJson = JsonUtils.toJson(dataArchiveClearParameters);
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setParametersJson(parametersJson);
        dataArchiveTask.setStatus(TaskStatus.RUNNING);

        doReturn(Optional.of(dataArchiveTask)).when(scheduleTaskRepository).findById(eq(1L));

        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository).updateStatusById(eq(1L), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_PrepareTasksForClear() throws JobExecutionException {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("taskId");
        String parametersJson = JsonUtils.toJson(dataArchiveClearParameters);
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setParametersJson(parametersJson);
        dataArchiveTask.setStatus(TaskStatus.DONE);

        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setId("id1");
        dlmTask1.setJobType(JobType.DELETE);
        dlmTask1.setStatus(TaskStatus.PREPARING);

        DlmTask dlmTask2 = new DlmTask();
        dlmTask2.setId("id2");
        dlmTask2.setJobType(JobType.DELETE);
        dlmTask2.setStatus(TaskStatus.PREPARING);

        List<DlmTask> taskUnits = new ArrayList<>();
        taskUnits.add(dlmTask1);
        taskUnits.add(dlmTask2);

        doReturn(Optional.of(dataArchiveTask)).when(scheduleTaskRepository).findById(eq(1L));
        doReturn(taskUnits).when(abstractDlmJob).prepareTasksForClear(any(ScheduleTaskEntity.class));

        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository).updateStatusById(eq(1L), any(TaskStatus.class));
        verify(jobScheduler).addJobs(eq(taskUnits), any(List.class));
    }
}