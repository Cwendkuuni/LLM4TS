package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

@SuppressWarnings("unchecked")
public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private ScheduleTaskRepository scheduleTaskRepository;
    private JobExecutionContext jobExecutionContext;
    private ScheduleTaskEntity taskEntity;
    private ScheduleTaskEntity dataArchiveTask;
    private List<DlmTask> taskUnits;

    @Before
    public void setup() {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        dataArchiveDeleteJob = new DataArchiveDeleteJob() {
            @Override
            protected ScheduleTaskRepository scheduleTaskRepository() {
                return scheduleTaskRepository;
            }
        };
        taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
        dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);
        dataArchiveTask.setResultJson("[{\"id\": \"1\", \"jobName\": \"test\", \"jobGroup\": \"test\", \"status\": \"DONE\"}]");
        taskUnits = new ArrayList<>();
        taskUnits.add(new DlmTask());
        taskUnits.get(0).setId("1");
        taskUnits.get(0).setJobType(JobType.DATA_ARCHIVE);
        taskUnits.get(0).setStatus(TaskStatus.DONE);
    }

    @Test
    public void testExecute() throws JobExecutionException, SchedulerException {
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        jobExecutionContext = mock(JobExecutionContext.class);
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(jobExecutionContext.getMergedJobDataMap().get(anyString())).thenReturn(taskEntity);
        JobKey jobKey = new JobKey("test", "test");
        when(jobExecutionContext.getJobDetail().getKey()).thenReturn(jobKey);
        when(jobExecutionContext.getMergedJobDataMap().getString(anyString())).thenReturn("test");
        when(jobExecutionContext.getScheduler().getTrigger(jobKey)).thenReturn(mock(Trigger.class));

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
    }

    @Test
    public void testExecute_whenDataArchiveTaskNotFound() throws JobExecutionException {
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());
        jobExecutionContext = mock(JobExecutionContext.class);
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(jobExecutionContext.getMergedJobDataMap().get(anyString())).thenReturn(taskEntity);
        JobKey jobKey = new JobKey("test", "test");
        when(jobExecutionContext.getJobDetail().getKey()).thenReturn(jobKey);
        when(jobExecutionContext.getMergedJobDataMap().getString(anyString())).thenReturn("test");
        when(jobExecutionContext.getScheduler().getTrigger(jobKey)).thenReturn(mock(Trigger.class));

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_whenDataArchiveTaskNotFinished() throws JobExecutionException {
        dataArchiveTask.setStatus(TaskStatus.RUNNING);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        jobExecutionContext = mock(JobExecutionContext.class);
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(jobExecutionContext.getMergedJobDataMap().get(anyString())).thenReturn(taskEntity);
        JobKey jobKey = new JobKey("test", "test");
        when(jobExecutionContext.getJobDetail().getKey()).thenReturn(jobKey);
        when(jobExecutionContext.getMergedJobDataMap().getString(anyString())).thenReturn("test");
        when(jobExecutionContext.getScheduler().getTrigger(jobKey)).thenReturn(mock(Trigger.class));

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_whenPrepareTasksForClear() throws JobExecutionException {
        dataArchiveTask.setStatus(TaskStatus.DONE);
        DataArchiveClearParameters dataArchiveClearParameters =
                JsonUtils.fromJson(taskEntity.getParametersJson(), DataArchiveClearParameters.class);
        dataArchiveClearParameters.setDataArchiveTaskId(dataArchiveTask.getId());
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class))).thenReturn(null);
        when(JsonUtils.fromJson(anyString(), any(TypeReference.class))).thenReturn(taskUnits);
        when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class))).thenReturn(null);

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(2)).updateStatusById(anyLong(), any(TaskStatus.class));
        assertEquals(DlmJobIdUtil.generateHistoryJobId(jobExecutionContext.getJobDetail().getKey().getName(),
                jobExecutionContext.getJobDetail().getKey().getGroup(), taskEntity.getId(), 1),
                taskUnits.get(0).getId());
        assertEquals(JobType.DELETE, taskUnits.get(0).getJobType());
        assertEquals(TaskStatus.PREPARING, taskUnits.get(0).getStatus());
    }

    @Test
    public void testExecute_whenGetTaskStatus() throws JobExecutionException {
        dataArchiveTask.setStatus(TaskStatus.DONE);
        DataArchiveClearParameters dataArchiveClearParameters =
                JsonUtils.fromJson(taskEntity.getParametersJson(), DataArchiveClearParameters.class);
        dataArchiveClearParameters.setDataArchiveTaskId(dataArchiveTask.getId());
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        when(JsonUtils.fromJson(anyString(), any(TypeReference.class))).thenReturn(taskUnits);

        TaskStatus taskStatus = dataArchiveDeleteJob.getTaskStatus(taskUnits);

        assertEquals(TaskStatus.PREPARING, taskStatus);
    }

    @Test
    public void testGetTaskStatus_whenTaskStatusIsDone() throws JobExecutionException {
        taskUnits.get(0).setStatus(TaskStatus.DONE);
        when(JsonUtils.fromJson(anyString(), any(TypeReference.class))).thenReturn(taskUnits);

        TaskStatus taskStatus = dataArchiveDeleteJob.getTaskStatus(taskUnits);

        assertEquals(TaskStatus.DONE, taskStatus);
    }

    @Test
    public void testGetTaskStatus_whenTaskStatusIsFailed() throws JobExecutionException {
        taskUnits.get(0).setStatus(TaskStatus.FAILED);
        when(JsonUtils.fromJson(anyString(), any(TypeReference.class))).thenReturn(taskUnits);

        TaskStatus taskStatus = dataArchiveDeleteJob.getTaskStatus(taskUnits);

        assertEquals(TaskStatus.FAILED, taskStatus);
    }

    @Test
    public void testGetTaskStatus_whenTaskStatusIsRunning() throws JobExecutionException {
        taskUnits.get(0).setStatus(TaskStatus.RUNNING);
        when(JsonUtils.fromJson(anyString(), any(TypeReference.class))).thenReturn(taskUnits);

        TaskStatus taskStatus = dataArchiveDeleteJob.getTaskStatus(taskUnits);

        assertEquals(TaskStatus.RUNNING, taskStatus);
    }
}