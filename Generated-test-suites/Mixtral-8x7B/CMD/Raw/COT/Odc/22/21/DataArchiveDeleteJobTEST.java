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

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private JobExecutionContext jobExecutionContext;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ScheduleTaskEntity scheduleTaskEntity;
    private DataArchiveClearParameters dataArchiveClearParameters;
    private List<DlmTask> taskUnits;

    @Before
    public void setup() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();

        jobExecutionContext = mock(JobExecutionContext.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);

        scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
        scheduleTaskEntity.setResultJson("[]");
        scheduleTaskEntity.setStatus(TaskStatus.DONE);

        dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(2L);

        taskUnits = new ArrayList<>();
        DlmTask task1 = new DlmTask();
        task1.setId(1L);
        task1.setJobType(JobType.DELETE);
        task1.setStatus(TaskStatus.PREPARING);
        taskUnits.add(task1);
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() throws Exception {
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.empty());

        dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).findById(dataArchiveClearParameters.getDataArchiveTaskId());
        verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_DataArchiveTaskNotDone() throws Exception {
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));

        dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).findById(dataArchiveClearParameters.getDataArchiveTaskId());
        verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_Success() throws Exception {
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(scheduleTaskEntity));

        when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.PREPARING)).thenReturn(true);
        when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenReturn(false);

        when(JsonUtils.fromJson(anyString(), any(TypeReference.class))).thenReturn(taskUnits);

        dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).findById(dataArchiveClearParameters.getDataArchiveTaskId());
        verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.PREPARING);
        verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.DONE);
    }
}