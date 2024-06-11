import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {

    @Mock
    private AbstractDlmJob abstractDlmJob;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private JobExecutionContext jobExecutionContext;

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dataArchiveRollbackJob = new DataArchiveRollbackJob(scheduleTaskRepository);
        dataArchiveRollbackJob.setAbstractDlmJob(abstractDlmJob);
    }

    @Test
    public void testExecute_validParameters_shouldRollbackTasks() {
        // Given
        String parametersJson = "{\"dataArchiveTaskId\":1}";
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson(parametersJson);

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);

        List<DlmTask> taskUnits = JsonUtils.createJsonList(new DlmTask(), new DlmTask());

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        when(JsonUtils.fromJson(parametersJson, DataArchiveRollbackParameters.class))
                .thenReturn(rollbackParameters);
        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);

        // When
        dataArchiveRollbackJob.execute(jobExecutionContext);

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
    }

    @Test
    public void testExecute_dataArchiveTaskNotFound_shouldFailTask() {
        // Given
        String parametersJson = "{\"dataArchiveTaskId\":1}";
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson(parametersJson);

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));

        // When
        dataArchiveRollbackJob.execute(jobExecutionContext);

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_taskUnitsIsEmpty_shouldFailTask() {
        // Given
        String parametersJson = "{\"dataArchiveTaskId\":1}";
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson(parametersJson);

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);

        List<DlmTask> taskUnits = JsonUtils.createJsonList();

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        when(JsonUtils.fromJson(parametersJson, DataArchiveRollbackParameters.class))
                .thenReturn(rollbackParameters);
        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);

        // When
        dataArchiveRollbackJob.execute(jobExecutionContext);

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_rollbackTasksSuccessfully_shouldUpdateTaskStatus() {
        // Given
        String parametersJson = "{\"dataArchiveTaskId\":1}";
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson(parametersJson);

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);

        List<DlmTask> taskUnits = JsonUtils.createJsonList(new DlmTask(), new DlmTask());

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        when(JsonUtils.fromJson(parametersJson, DataArchiveRollbackParameters.class))
                .thenReturn(rollbackParameters);
        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);
        when(dataArchiveRollbackJob.executeTask(taskEntity.getId(), taskUnits)).thenReturn(JobType.ROLLBACK);

        // When
        dataArchiveRollbackJob.execute(jobExecutionContext);

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.DONE);
    }

    @Test
    public void testExecute_rollbackTasksUnsuccessfully_shouldUpdateTaskStatus() {
        // Given
        String parametersJson = "{\"dataArchiveTaskId\":1}";
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson(parametersJson);

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);

        List<DlmTask> taskUnits = JsonUtils.createJsonList(new DlmTask(), new DlmTask());

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        when(JsonUtils.fromJson(parametersJson, DataArchiveRollbackParameters.class))
                .thenReturn(rollbackParameters);
        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);
        when(dataArchiveRollbackJob.executeTask(taskEntity.getId(), taskUnits)).thenReturn(JobType.PREPARE);

        // When
        dataArchiveRollbackJob.execute(jobExecutionContext);

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_exceptionWhileRollbackTasks_shouldUpdateTaskStatus() {
        // Given
        String parametersJson = "{\"dataArchiveTaskId\":1}";
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson(parametersJson);

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);

        List<DlmTask> taskUnits = JsonUtils.createJsonList(new DlmTask(), new DlmTask());

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        when(JsonUtils.fromJson(parametersJson, DataArchiveRollbackParameters.class))
                .thenReturn(rollbackParameters);
        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);
        doThrow(new RuntimeException()).when(dataArchiveRollbackJob).executeTask(taskEntity.getId(), taskUnits);

        // When
        dataArchiveRollbackJob.execute(jobExecutionContext);

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }
}