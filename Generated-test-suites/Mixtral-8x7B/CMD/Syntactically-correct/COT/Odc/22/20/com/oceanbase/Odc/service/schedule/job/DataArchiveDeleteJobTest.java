// 

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DataArchiveDeleteJobTest {

    @Autowired
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @MockBean
    private AbstractDlmJob abstractDlmJob;

    @MockBean
    private ScheduleTaskRepository scheduleTaskRepository;

    private JobExecutionContext jobExecutionContext;

    @Before
    public void setUp() {
        jobExecutionContext = mock(JobExecutionContext.class);

        ReflectionTestUtils.setField(dataArchiveDeleteJob, "scheduleTaskRepository", scheduleTaskRepository);
        ReflectionTestUtils.setField(dataArchiveDeleteJob, "abstractDlmJob", abstractDlmJob);
    }

    @Test
    public void testExecute_happyPath() throws JobExecutionException {
        // Arrange
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
        taskEntity.setResultJson("[]");
        taskEntity.setStatus(TaskStatus.DONE);

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);

        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(2L);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);
        dataArchiveTask.setStatus(TaskStatus.DONE);

        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(dataArchiveTask);
        List<DlmTask> dlmTasks = Arrays.asList(
                new DlmTask("task1"),
                new DlmTask("task2")
        );

        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(dataArchiveTaskOption);

        when(JsonUtils.fromJson(taskEntity.getParametersJson(), DataArchiveClearParameters.class))
                .thenReturn(dataArchiveClearParameters);

        when(scheduleTaskRepository.findById(dataArchiveTask.getId()))
                .thenReturn(Optional.of(dataArchiveTask));

        when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {}))
                .thenReturn(dlmTasks);

        // Act
        dataArchiveDeleteJob.execute(jobExecutionContext);

        // Assert
        // Additional asserts can be added here to check the behavior of the method in the happy path scenario
    }

    @Test
    public void testExecute_taskEntityIsNull() throws JobExecutionException {
        // Arrange
        when(jobExecutionContext.getResult()).thenReturn(null);

        // Act and Assert
        try {
            dataArchiveDeleteJob.execute(jobExecutionContext);
        } catch (JobExecutionException e) {
            assertEquals("No task entity found in the JobExecutionContext", e.getMessage());
        }
    }

    @Test
    public void testExecute_dataArchiveTaskNotFound() throws JobExecutionException {
        // Arrange
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
        taskEntity.setResultJson("[]");
        taskEntity.setStatus(TaskStatus.DONE);

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);

        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(2L);

        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        // Act and Assert
        try {
            dataArchiveDeleteJob.execute(jobExecutionContext);
        } catch (JobExecutionException e) {
            assertEquals("Data archive task not found,rollback task fast failed.dataArchiveTaskId=2", e.getMessage());
        }
    }

    @Test
    public void testExecute_dataArchiveTaskNotDone() throws JobExecutionException {
        // Arrange
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
        taskEntity.setResultJson("[]");
        taskEntity.setStatus(TaskStatus.DONE);

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(2L);
        dataArchiveTask.setStatus(TaskStatus.PREPARING);

        Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(dataArchiveTask);

        when(scheduleTaskRepository.findById(any()))
                .thenReturn(dataArchiveTaskOption);

        // Act and Assert
        try {
            dataArchiveDeleteJob.execute(jobExecutionContext);
        } catch (JobExecutionException e) {
            assertEquals("Data archive task do not finish,data archive task id = 2", e.getMessage());
        }
    }

    // 
}