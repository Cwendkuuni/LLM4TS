
import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob job;
    private JobExecutionContext context;
    private ScheduleTaskEntity taskEntity;
    private ScheduleTaskEntity dataArchiveTask;
    private DataArchiveRollbackParameters rollbackParameters;
    private List<DlmTask> taskUnits;
    private ScheduleTaskRepository scheduleTaskRepository;
    private DataArchiveService dataArchiveService;

    @Before
    public void setUp() {
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        dataArchiveService = mock(DataArchiveService.class);
        job = new DataArchiveRollbackJob(scheduleTaskRepository, dataArchiveService);
        context = mock(JobExecutionContext.class);
        taskEntity = createMockScheduleTaskEntity();
        dataArchiveTask = createMockScheduleTaskEntity();
        rollbackParameters = createMockDataArchiveRollbackParameters();
        taskUnits = createMockDlmTaskList();
    }

    @Test
    public void testExecuteSuccess() throws Exception {
        String parametersJson = JsonUtils.toJson(rollbackParameters);
        taskEntity.setParametersJson(parametersJson);
        String resultJson = JsonUtils.toJson(taskUnits);
        dataArchiveTask.setResultJson(resultJson);
        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(dataArchiveTask);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(dataArchiveTaskOption);
        when(dataArchiveService.executeTask(anyLong(), anyList())).thenReturn(true);
        when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class))).thenReturn(true);
        replayAll();

        job.execute(context);

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<TaskStatus> statusCaptor = ArgumentCaptor.forClass(TaskStatus.class);
        verify(scheduleTaskRepository).updateStatusById(idCaptor.capture(), statusCaptor.capture());
        verifyNoMoreInteractions(scheduleTaskRepository, dataArchiveService);
        assertEquals(rollbackParameters.getDataArchiveTaskId(), idCaptor.getValue());
        assertEquals(TaskStatus.DONE, statusCaptor.getValue());
    }

    @Test
    public void testExecuteFailure() throws Exception {
        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.empty();
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(dataArchiveTaskOption);
        when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class))).thenReturn(true);
        replayAll();

        job.execute(context);

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<TaskStatus> statusCaptor = ArgumentCaptor.forClass(TaskStatus.class);
        verify(scheduleTaskRepository).updateStatusById(idCaptor.capture(), statusCaptor.capture());
        verifyNoMoreInteractions(scheduleTaskRepository, dataArchiveService);
        assertEquals(taskEntity.getId(), idCaptor.getValue());
        assertEquals(TaskStatus.FAILED, statusCaptor.getValue());
    }

    @Test
    public void testExecuteTaskFailure() throws Exception {
        String parametersJson = JsonUtils.toJson(rollbackParameters);
        taskEntity.setParametersJson(parametersJson);
        String resultJson = JsonUtils.toJson(taskUnits);
        dataArchiveTask.setResultJson(resultJson);
        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(dataArchiveTask);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(dataArchiveTaskOption);
        when(dataArchiveService.executeTask(anyLong(), anyList())).thenReturn(false);
        when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class))).thenReturn(true);
        replayAll();

        job.execute(context);

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<TaskStatus> statusCaptor = ArgumentCaptor.forClass(TaskStatus.class);
        verify(scheduleTaskRepository).updateStatusById(idCaptor.capture(), statusCaptor.capture());
        verifyNoMoreInteractions(scheduleTaskRepository, dataArchiveService);
        assertEquals(taskEntity.getId(), idCaptor.getValue());
        assertEquals(TaskStatus.FAILED, statusCaptor.getValue());
    }

    private JobExecutionContext createMockJobExecutionContext() {
        return mock(JobExecutionContext.class);
    }

    private ScheduleTaskEntity createMockScheduleTaskEntity() {
        ScheduleTaskEntity entity = mock(ScheduleTaskEntity.class);
        when(entity.getId()).thenReturn(1L);
        when(entity.getJobName()).thenReturn("testJobName");
        when(entity.getJobGroup()).thenReturn("testJobGroup");
        when(entity.getParametersJson()).thenReturn(null);
        when(entity.getResultJson()).thenReturn(null);
        return entity;
    }

    private DataArchiveRollbackParameters createMockDataArchiveRollbackParameters() {
        DataArchiveRollbackParameters parameters = new DataArchiveRollbackParameters();
        parameters.setDataArchiveTaskId(2L);
        return parameters;
    }

    private List<DlmTask> createMockDlmTaskList() {
        DlmTask task1 = new DlmTask();
        task1.setId(3L);
        DlmTask task2 = new DlmTask();
        task2.setId(4L);
        return List.of(task1, task2);
    }
}
