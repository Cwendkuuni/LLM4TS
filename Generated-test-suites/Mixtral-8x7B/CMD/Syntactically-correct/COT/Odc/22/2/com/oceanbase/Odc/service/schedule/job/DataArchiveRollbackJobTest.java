import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.utils.AbstractDlmJob;
import com.oceanbase.tools.migrator.common.enums.JobType;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private JobExecutionContext context;

    @Mock
    private Thread jobThread;

    @Mock
    private ScheduleTaskEntity taskEntity;

    @Mock
    private DataArchiveRollbackParameters rollbackParameters;

    @Mock
    private DlmTask dlmTask;

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setup() {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("{}");
        when(JsonUtils.fromJson(anyString(), eq(DataArchiveRollbackParameters.class)))
                .thenReturn(rollbackParameters);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));
        when(taskEntity.getResultJson()).thenReturn("[]");
        when(JsonUtils.fromJson(anyString(), any(TypeReference.class))).thenReturn(List.of(dlmTask));
        when(DlmJobIdUtil.generateHistoryJobId(anyString(), anyString(), anyLong(), anyInt()))
                .thenReturn(1L);
    }

    @Test
    public void testExecute_dataArchiveTaskNotFound() throws JobExecutionException {
        when(rollbackParameters.getDataArchiveTaskId()).thenReturn(1L);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.FAILED);
    }

    @Test
    public void testExecute_dataArchiveTaskFound() throws JobExecutionException {
        when(rollbackParameters.getDataArchiveTaskId()).thenReturn(1L);
        doNothing().when(dataArchiveRollbackJob).executeTask(1L, List.of(dlmTask));

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.DONE);
    }

    @Test
    public void testExecute_dataArchiveTaskFound_taskStatusPreparing() throws JobExecutionException {
        when(rollbackParameters.getDataArchiveTaskId()).thenReturn(1L);
        when(dlmTask.getStatus()).thenReturn(TaskStatus.PREPARING);

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.PREPARING);
    }

    @Test
    public void testExecute_dataArchiveTaskFound_taskStatusDone() throws JobExecutionException {
        when(rollbackParameters.getDataArchiveTaskId()).thenReturn(1L);
        when(dlmTask.getStatus()).thenReturn(TaskStatus.DONE);

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.DONE);
    }

    @Test
    public void testExecute_dataArchiveTaskFound_taskStatusFailed() throws JobExecutionException {
        when(rollbackParameters.getDataArchiveTaskId()).thenReturn(1L);
        when(dlmTask.getStatus()).thenReturn(TaskStatus.FAILED);

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.FAILED);
    }

    @Test
    public void testExecute_dataArchiveTaskFound_taskStatusRunning() throws JobExecutionException {
        when(rollbackParameters.getDataArchiveTaskId()).thenReturn(1L);
        when(dlmTask.getStatus()).thenReturn(TaskStatus.RUNNING);

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.RUNNING);
    }

    @Test
    public void testExecute_dataArchiveTaskFound_taskStatusSucceeded() throws JobExecutionException {
        when(rollbackParameters.getDataArchiveTaskId()).thenReturn(1L);
        when(dlmTask.getStatus()).thenReturn(TaskStatus.SUCCEEDED);

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.SUCCEEDED);
    }

    @Test
    public void testExecute_dataArchiveTaskFound_taskStatusUnknown() throws JobExecutionException {
        when(rollbackParameters.getDataArchiveTaskId()).thenReturn(1L);
        when(dlmTask.getStatus()).thenReturn(TaskStatus.UNKNOWN);

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.UNKNOWN);
    }

    @Test
    public void testExecute_dataArchiveTaskFound_updateStatusByIdNotCalled() throws JobExecutionException {
        when(rollbackParameters.getDataArchiveTaskId()).thenReturn(1L);
        doNothing().when(dataArchiveRollbackJob).executeTask(1L, List.of(dlmTask));
        doNothing().when(scheduleTaskRepository).updateStatusById(anyLong(), any(TaskStatus.class));

        dataArchiveRollbackJob.execute(context);

        verify(scheduleTaskRepository, never()).updateStatusById(1L, TaskStatus.FAILED);
    }

    @Test(expected = JobExecutionException.class)
    public void testExecute_JsonUtilsFromJsonThrowsException() throws JobExecutionException {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("{}");
        when(JsonUtils.fromJson(anyString(), eq(DataArchiveRollbackParameters.class)))
                .thenThrow(new RuntimeException());

        dataArchiveRollbackJob.execute(context);
    }

    @Test(expected = JobExecutionException.class)
    public void testExecute_scheduleTaskRepositoryFindByIdThrowsException()
            throws JobExecutionException {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("{}");
        when(scheduleTaskRepository.findById(anyLong())).thenThrow(new RuntimeException());

        dataArchiveRollbackJob.execute(context);
    }

    @Test(expected = JobExecutionException.class)
    public void testExecute_JsonUtilsFromJsonTaskUnitsThrowsException() throws JobExecutionException {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("{}");
        when(JsonUtils.fromJson(anyString(), any(TypeReference.class))).thenThrow(new RuntimeException());

        dataArchiveRollbackJob.execute(context);
    }

    @Test(expected = JobExecutionException.class)
    public void testExecute_DlmJobIdUtilGenerateHistoryJobIdThrowsException()
            throws JobExecutionException {
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("{}");
        when(JsonUtils.fromJson(anyString(), any(TypeReference.class)))
                .thenReturn(List.of(dlmTask));
        when(DlmJobIdUtil.generateHistoryJobId(anyString(), anyString(), anyLong(), anyInt()))
                .thenThrow(new RuntimeException());

        dataArchiveRollbackJob.execute(context);
    }
}