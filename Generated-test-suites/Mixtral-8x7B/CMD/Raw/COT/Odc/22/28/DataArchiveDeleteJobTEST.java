package com.oceanbase.odc.service.schedule.job;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.QuartzJobMetadata;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@SuppressWarnings("unchecked")
public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private JobExecutionContext jobExecutionContext;
    private ScheduleTaskEntity taskEntity;
    private SchedulerFactoryBean schedulerFactoryBean;
    private QuartzJobMetadata quartzJobMetadata;
    private QuartzJobBean quartzJobBean;

    @Before
    public void setup() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
        schedulerFactoryBean = mock(SchedulerFactoryBean.class);
        quartzJobMetadata = mock(QuartzJobMetadata.class);
        quartzJobBean = mock(QuartzJobBean.class);

        jobExecutionContext = mock(JobExecutionContext.class);
        taskEntity = mock(ScheduleTaskEntity.class);

        when(jobExecutionContext.getMergedJobDataMap().get("scheduleTaskEntity")).thenReturn(taskEntity);

        dataArchiveDeleteJob.setSchedulerFactoryBean(schedulerFactoryBean);
        dataArchiveDeleteJob.setQuartzJobMetadata(quartzJobMetadata);
        dataArchiveDeleteJob.setQuartzJobBean(quartzJobBean);
    }

    @Test
    public void testExecute_NoDataArchiveTaskFound() throws JobExecutionException {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("taskId");
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters));

        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskNotDone() throws JobExecutionException {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("taskId");
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId("dataArchiveTaskId");
        dataArchiveTask.setStatus(TaskStatus.RUNNING);

        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_ClearTasksSuccess() throws JobExecutionException {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("dataArchiveTaskId");
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId("dataArchiveTaskId");
        dataArchiveTask.setStatus(TaskStatus.DONE);

        List<DlmTask> taskUnits = createTaskUnits();

        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(dataArchiveTask));
        when(JsonUtils.fromJson(anyString(), any(TypeReference.class))).thenReturn(taskUnits);

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), TaskStatus.COMPLETED);
    }

    @Test
    public void testExecute_ClearTasksFailed() throws JobExecutionException {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("dataArchiveTaskId");
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId("dataArchiveTaskId");
        dataArchiveTask.setStatus(TaskStatus.DONE);

        List<DlmTask> taskUnits = createTaskUnits();
        taskUnits.get(0).setStatus(TaskStatus.FAILED);

        when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(dataArchiveTask));
        when(JsonUtils.fromJson(anyString(), any(TypeReference.class))).thenReturn(taskUnits);

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), TaskStatus.FAILED);
    }

    private List<DlmTask> createTaskUnits() {
        return List.of(
                new DlmTask()
                        .setId("taskId")
                        .setJobType(JobType.DELETE)
                        .setStatus(TaskStatus.PREPARING));
    }
}