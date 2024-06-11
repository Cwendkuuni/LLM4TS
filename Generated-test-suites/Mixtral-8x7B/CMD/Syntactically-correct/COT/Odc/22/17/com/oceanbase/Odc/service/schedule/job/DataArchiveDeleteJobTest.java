package com.oceanbase.odc.service.schedule.job;

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
import org.quartz.impl.JobExecutionContextImpl;
import org.slf4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;
import com.oceanbase.tools.migrator.common.exception.MigratorException;

@SuppressWarnings("unchecked")
public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;

    private ScheduleTaskRepository scheduleTaskRepository;

    private JobExecutionContext jobExecutionContext;

    private ScheduleTaskEntity taskEntity;

    private DataArchiveClearParameters dataArchiveClearParameters;

    private List<DlmTask> taskUnits;

    @Before
    public void setUp() {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        dataArchiveDeleteJob = new DataArchiveDeleteJob(scheduleTaskRepository);
        Logger logger = mock(Logger.class);
        dataArchiveDeleteJob.setLogger(logger);
        jobExecutionContext = new JobExecutionContextImpl();
        taskEntity = new ScheduleTaskEntity();
        dataArchiveClearParameters = new DataArchiveClearParameters();
        taskUnits = new ArrayList<>();
        JsonUtils jsonUtils = mock(JsonUtils.class);
        dataArchiveDeleteJob.setJsonUtils(jsonUtils);
        DlmJobIdUtil dlmJobIdUtil = mock(DlmJobIdUtil.class);
        dataArchiveDeleteJob.setDlmJobIdUtil(dlmJobIdUtil);
    }

    @Test
    public void testExecute_whenDataArchiveTaskNotFound_shouldUpdateTaskStatusToFailed()
            throws MigratorException {
        dataArchiveClearParameters.setDataArchiveTaskId("dataArchiveTaskId");
        when(scheduleTaskRepository.findById("dataArchiveTaskId")).thenReturn(Optional.empty());
        dataArchiveDeleteJob.execute(jobExecutionContext);
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_whenDataArchiveTaskNotDone_shouldUpdateTaskStatusToFailed()
            throws MigratorException {
        dataArchiveClearParameters.setDataArchiveTaskId("dataArchiveTaskId");
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.RUNNING);
        when(scheduleTaskRepository.findById("dataArchiveTaskId")).thenReturn(Optional.of(dataArchiveTask));
        dataArchiveDeleteJob.execute(jobExecutionContext);
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_whenDataArchiveTaskFoundAndDone_shouldExecuteTaskAndUpdateTaskStatus()
            throws MigratorException {
        dataArchiveClearParameters.setDataArchiveTaskId("dataArchiveTaskId");
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId("dataArchiveTaskId");
        dataArchiveTask.setStatus(TaskStatus.DONE);
        dataArchiveTask.setResultJson("[]");
        taskUnits.add(new DlmTask());
        when(scheduleTaskRepository.findById("dataArchiveTaskId")).thenReturn(Optional.of(dataArchiveTask));
        when(jsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);
        dataArchiveDeleteJob.execute(jobExecutionContext);
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.PREPARING);
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.RUNNING);
    }

    @Test
    public void testExecute_whenExceptionThrownWhileExecutingTask_shouldUpdateTaskStatusToFailed()
            throws MigratorException {
        dataArchiveClearParameters.setDataArchiveTaskId("dataArchiveTaskId");
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId("dataArchiveTaskId");
        dataArchiveTask.setStatus(TaskStatus.DONE);
        dataArchiveTask.setResultJson("[]");
        taskUnits.add(new DlmTask());
        when(scheduleTaskRepository.findById("dataArchiveTaskId")).thenReturn(Optional.of(dataArchiveTask));
        when(jsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);
        when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.PREPARING)).thenThrow(
                new MigratorException("test exception"));
        dataArchiveDeleteJob.execute(jobExecutionContext);
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }
}