import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private AbstractDlmJob abstractDlmJob;

    private JobExecutionContext jobExecutionContext;

    @Before
    public void setup() {
        jobExecutionContext = mock(JobExecutionContext.class);
    }

    @Test
    public void testExecute_whenDataArchiveTaskNotFound_thenUpdateTaskStatusToFailed() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("1234");

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_whenDataArchiveTaskNotFinished_thenUpdateTaskStatusToFailed() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("1234");

        ScheduleTaskEntity dataArchiveTaskEntity = new ScheduleTaskEntity();
        dataArchiveTaskEntity.setId(1L);
        dataArchiveTaskEntity.setStatus(TaskStatus.RUNNING);
        dataArchiveTaskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(2L);
        scheduleTaskEntity.setResultJson("[]");

        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTaskEntity));

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_whenDataArchiveTaskFoundAndFinished_thenExecuteTasksAndUpdateStatus() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("1234");

        ScheduleTaskEntity dataArchiveTaskEntity = new ScheduleTaskEntity();
        dataArchiveTaskEntity.setId(1L);
        dataArchiveTaskEntity.setStatus(TaskStatus.DONE);
        dataArchiveTaskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
        dataArchiveTaskEntity.setResultJson("[]");

        List<DlmTask> dlmTasks = JsonUtils.fromJson("[{}]", new TypeReference<List<DlmTask>>() {});

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(2L);

        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTaskEntity));

        when(JsonUtils.fromJson(dataArchiveTaskEntity.getResultJson(),
                new TypeReference<List<DlmTask>>() {})).thenReturn(dlmTasks);

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(abstractDlmJob, times(1)).executeTask(anyLong(), anyList());
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
    }

    @Test
    public void testExecute_whenExceptionOccursInExecuteTask_thenUpdateTaskStatusToFailed() {
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("1234");

        ScheduleTaskEntity dataArchiveTaskEntity = new ScheduleTaskEntity();
        dataArchiveTaskEntity.setId(1L);
        dataArchiveTaskEntity.setStatus(TaskStatus.DONE);
        dataArchiveTaskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
        dataArchiveTaskEntity.setResultJson("[]");

        List<DlmTask> dlmTasks = JsonUtils.fromJson("[{}]", new TypeReference<List<DlmTask>>() {});

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(2L);

        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTaskEntity));

        when(JsonUtils.fromJson(dataArchiveTaskEntity.getResultJson(),
                new TypeReference<List<DlmTask>>() {})).thenReturn(dlmTasks);

        doThrow(new RuntimeException("test exception")).when(abstractDlmJob).executeTask(anyLong(), anyList());

        dataArchiveDeleteJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testGetTaskStatus_whenAllTasksAreSuccess_thenReturnSuccess() {
        List<DlmTask> dlmTasks = createDlmTasks(5, TaskStatus.SUCCESS);

        TaskStatus taskStatus = dataArchiveDeleteJob.getTaskStatus(dlmTasks);

        assertEquals(TaskStatus.SUCCESS, taskStatus);
    }

    @Test
    public void testGetTaskStatus_whenAnyTaskIsFailed_thenReturnFailed() {
        List<DlmTask> dlmTasks = createDlmTasks(5, TaskStatus.SUCCESS);
        dlmTasks.set(2, createDlmTask(TaskStatus.FAILED));

        TaskStatus taskStatus = dataArchiveDeleteJob.getTaskStatus(dlmTasks);

        assertEquals(TaskStatus.FAILED, taskStatus);
    }

    @Test
    public void testGetTaskStatus_whenAllTasksAreRunning_thenReturnRunning() {
        List<DlmTask> dlmTasks = createDlmTasks(5, TaskStatus.RUNNING);

        TaskStatus taskStatus = dataArchiveDeleteJob.getTaskStatus(dlmTasks);

        assertEquals(TaskStatus.RUNNING, taskStatus);
    }

    private List<DlmTask> createDlmTasks(int count, TaskStatus status) {
        List<DlmTask> dlmTasks = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            dlmTasks.add(createDlmTask(status));
        }
        return dlmTasks;
    }

    private DlmTask createDlmTask(TaskStatus status) {
        DlmTask dlmTask = new DlmTask();
        dlmTask.setId("test_id_" + System.currentTimeMillis());
        dlmTask.setJobType(JobType.DELETE);
        dlmTask.setStatus(status);
        return dlmTask;
    }
}