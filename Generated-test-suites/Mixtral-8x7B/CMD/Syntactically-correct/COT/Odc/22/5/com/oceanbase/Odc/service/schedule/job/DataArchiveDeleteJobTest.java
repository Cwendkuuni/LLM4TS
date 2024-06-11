import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.AbstractDlmJob;

@SpringBootTest
public class DataArchiveDeleteJobTest {

    @Autowired
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    private JobExecutionContext jobExecutionContext;

    @Before
    public void setup() {
        jobExecutionContext = mock(JobExecutionContext.class);
    }

    @Test
    public void testExecute_whenDataArchiveTaskNotFound_shouldSetTaskStatusToFailed() throws JobExecutionException {
        // Given
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("123");

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId("456");
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        // When
        dataArchiveDeleteJob.execute(jobExecutionContext);

        // Then
        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_whenDataArchiveTaskNotFinished_shouldSetTaskStatusToFailed()
            throws JobExecutionException {
        // Given
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("123");

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId("123");
        dataArchiveTask.setStatus(TaskStatus.RUNNING);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId("456");
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        // When
        dataArchiveDeleteJob.execute(jobExecutionContext);

        // Then
        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_whenAllConditionsMet_shouldExecuteTasks() throws JobExecutionException {
        // Given
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("123");

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId("123");
        dataArchiveTask.setStatus(TaskStatus.DONE);

        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setId("task1");
        DlmTask dlmTask2 = new DlmTask();
        dlmTask2.setId("task2");

        List<DlmTask> taskUnits = List.of(dlmTask1, dlmTask2);

        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId("456");
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
        taskEntity.setResultJson(JsonUtils.toJson(taskUnits));

        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(dataArchiveTask));

        // When
        dataArchiveDeleteJob.execute(jobExecutionContext);

        // Then
        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.PREPARING);
        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), any(TaskStatus.class));
    }

    // Additional test methods for other edge cases and exception handling scenarios
}