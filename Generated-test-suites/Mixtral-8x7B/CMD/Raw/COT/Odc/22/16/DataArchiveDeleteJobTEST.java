import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.MockitoJpaRepository;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private AbstractDlmJob abstractDlmJob;
    private JpaRepository<ScheduleTaskEntity, Long> scheduleTaskRepository;
    private JobExecutionContext jobExecutionContext;

    @Before
    public void setUp() {
        scheduleTaskRepository = mock(MockitoJpaRepository.class);
        abstractDlmJob = mock(AbstractDlmJob.class);
        dataArchiveDeleteJob = new DataArchiveDeleteJob(scheduleTaskRepository, abstractDlmJob);
        jobExecutionContext = mock(JobExecutionContext.class);
    }

    @Test
    public void testExecute_whenDataArchiveTaskNotFound_thenUpdateStatusToFailed() throws JobExecutionException {
        DataArchiveClearParameters parameters = new DataArchiveClearParameters();
        parameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(parameters));

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository).updateStatusById(eq(1L), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_whenDataArchiveTaskNotDone_thenUpdateStatusToFailed() throws JobExecutionException {
        DataArchiveClearParameters parameters = new DataArchiveClearParameters();
        parameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(parameters));
        taskEntity.setStatus(TaskStatus.RUNNING);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);
        dataArchiveTask.setStatus(TaskStatus.DONE);

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository).updateStatusById(eq(1L), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_whenTaskUnitsIsNull_thenDoNothing() throws JobExecutionException {
        DataArchiveClearParameters parameters = new DataArchiveClearParameters();
        parameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(parameters));
        taskEntity.setStatus(TaskStatus.DONE);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);
        dataArchiveTask.setStatus(TaskStatus.DONE);

        List<DlmTask> taskUnits = null;

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
        when(JsonUtils.fromJson(anyString(), eq(new TypeReference<List<DlmTask>>() {})))
                .thenReturn(taskUnits);

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, never()).updateStatusById(eq(1L), any());
    }

    @Test
    public void testExecute_whenEverythingIsFine_thenExecuteTaskAndUpdateStatus() throws JobExecutionException {
        DataArchiveClearParameters parameters = new DataArchiveClearParameters();
        parameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(parameters));
        taskEntity.setStatus(TaskStatus.DONE);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);
        dataArchiveTask.setStatus(TaskStatus.DONE);

        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setId(1001L);

        DlmTask dlmTask2 = new DlmTask();
        dlmTask2.setId(1002L);

        List<DlmTask> taskUnits = List.of(dlmTask1, dlmTask2);

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
        when(JsonUtils.fromJson(anyString(), eq(new TypeReference<List<DlmTask>>() {}))).thenReturn(taskUnits);

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(abstractDlmJob).executeTask(eq(1L), eq(taskUnits));
        verify(scheduleTaskRepository).updateStatusById(eq(1L), any());
    }
}