

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class DataArchiveDeleteJobTest {

    @Autowired
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @MockBean
    private AbstractDlmJob abstractDlmJob;

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    private JobExecutionContext jobExecutionContext;

    @Before
    public void setUp() {
        jobExecutionContext = Mockito.mock(JobExecutionContext.class);
    }

    @Test
    public void testExecute_dataArchiveTaskNotFound() throws JobExecutionException {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).findById(1L);
        verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.FAILED);
    }

    @Test
    public void testExecute_dataArchiveTaskNotFinished() throws JobExecutionException {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setStatus(TaskStatus.RUNNING);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).findById(1L);
        verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.FAILED);
    }

    @Test
    public void testExecute_prepareTasksForClearSuccess() throws JobExecutionException {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setStatus(TaskStatus.DONE);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
        taskEntity.setResultJson("[]");

        List<DlmTask> taskUnits = JsonUtils.fromJson("[" +
                "{\"id\": 1, \"jobType\": \"DELETE\", \"status\": \"PREPARING\", \"taskType\": \"TASK_TYPE\"}," +
                "{\"id\": 2, \"jobType\": \"DELETE\", \"status\": \"PREPARING\", \"taskType\": \"TASK_TYPE\"}" +
                "]", new TypeReference<List<DlmTask>>() {});

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
        when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.PREPARING)).thenReturn(1);
        when(JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);
        when(DlmJobIdUtil.generateHistoryJobId("jobName", "jobGroup", 1L, 2)).thenReturn("historyJobId");

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).findById(1L);
        verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.PREPARING);
        verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.SUCCESS);
    }

    @Test
    public void testExecute_prepareTasksForClearFailure() throws JobExecutionException {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setStatus(TaskStatus.DONE);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
        taskEntity.setResultJson("[]");

        List<DlmTask> taskUnits = JsonUtils.fromJson("[" +
                "{\"id\": 1, \"jobType\": \"DELETE\", \"status\": \"PREPARING\", \"taskType\": \"TASK_TYPE\"}," +
                "{\"id\": 2, \"jobType\": \"DELETE\", \"status\": \"PREPARING\", \"taskType\": \"TASK_TYPE\"}" +
                "]", new TypeReference<List<DlmTask>>() {});

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
        when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.PREPARING)).thenReturn(1);
        when(JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);
        when(DlmJobIdUtil.generateHistoryJobId("jobName", "jobGroup", 1L, 2)).thenReturn("historyJobId");
        when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenReturn(1);

        doThrow(new RuntimeException()).when(abstractDlmJob).executeTask(1L, taskUnits);

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).findById(1L);
        verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.PREPARING);
        verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.FAILED);
    }
}