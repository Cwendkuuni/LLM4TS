package com.oceanbase.odc.service.schedule.job;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;
import java.util.List;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.matchers.EverythingMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.test.util.ReflectionTestUtils;

public class DataArchiveRollbackJobTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataArchiveRollbackJobTest.class);

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private ScheduleTaskRepository scheduleTaskRepository;
    private JobExecutionContext jobExecutionContext;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = spy(new DataArchiveRollbackJob());
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        jobExecutionContext = mock(JobExecutionContext.class);

        ReflectionTestUtils.setField(dataArchiveRollbackJob, "scheduleTaskRepository", scheduleTaskRepository);
    }

    @After
    public void tearDown() {
        dataArchiveRollbackJob = null;
        scheduleTaskRepository = null;
        jobExecutionContext = null;
    }

    @Test
    public void testExecute_whenDataArchiveTaskNotFound_thenUpdateTaskStatusToFailedAndReturn()
            throws JobExecutionException {
        // Arrange
        long taskId = 1L;
        DataArchiveRollbackParameters rollbackParameters = createDataArchiveRollbackParameters(taskId);
        ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);
        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskRepository.findById(taskId)).thenReturn(Optional.empty());

        // Act
        dataArchiveRollbackJob.execute(jobExecutionContext);

        // Assert
        verify(scheduleTaskRepository, times(1))
                .updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_whenDataArchiveTaskFound_thenPrepareTasksForRollbackAndUpdateTaskStatus()
            throws JobExecutionException {
        // Arrange
        long taskId = 1L;
        DataArchiveRollbackParameters rollbackParameters = createDataArchiveRollbackParameters(taskId);
        ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);
        Optional<ScheduleTaskEntity> dataArchiveTaskOptional = Optional.of(scheduleTaskEntity);
        List<DlmTask> dlmTasks = createDlmTasks();

        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskRepository.findById(taskId)).thenReturn(dataArchiveTaskOptional);
        when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class)))
                .thenReturn(true);
        when(JsonUtils.fromJson(anyString(), eq(DataArchiveRollbackParameters.class)))
                .thenReturn(rollbackParameters);
        when(JsonUtils.fromJson(anyString(), any(new TypeReference<List<DlmTask>>() {})))
                .thenReturn(dlmTasks);

        doNothing().when(dataArchiveRollbackJob).executeTask(anyLong(), any());

        // Act
        dataArchiveRollbackJob.execute(jobExecutionContext);

        // Assert
        verify(dataArchiveRollbackJob, times(1)).executeTask(anyLong(), any());
        verify(scheduleTaskRepository, times(1))
                .updateStatusById(anyLong(), eq(TaskStatus.PREPARING));
    }

    @Test
    public void testExecuteTask_whenTaskUnitsAreEmpty_thenReturn() {
        // Arrange
        long taskId = 1L;
        List<DlmTask> dlmTasks = createDlmTasks();

        doReturn(null).when(dataArchiveRollbackJob)
                .findDataArchiveTaskById(anyLong());

        // Act
        dataArchiveRollbackJob.executeTask(taskId, dlmTasks);

        // Assert
        verify(dataArchiveRollbackJob, times(0))
                .findDataArchiveTaskById(anyLong());
        verify(scheduleTaskRepository, times(0))
                .updateStatusById(anyLong(), any(TaskStatus.class));
    }

    @Test
    public void testExecuteTask_whenTaskUnitsAreNotEmpty_thenUpdateTaskStatus() {
        // Arrange
        long taskId = 1L;
        List<DlmTask> dlmTasks = createDlmTasks();
        ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);

        when(dataArchiveRollbackJob.findDataArchiveTaskById(anyLong()))
                .thenReturn(scheduleTaskEntity);

        // Act
        dataArchiveRollbackJob.executeTask(taskId, dlmTasks);

        // Assert
        verify(dataArchiveRollbackJob, times(1))
                .findDataArchiveTaskById(anyLong());
        verify(scheduleTaskRepository, times(dlmTasks.size()))
                .updateStatusById(anyLong(), any(TaskStatus.class));
    }

    private DataArchiveRollbackParameters createDataArchiveRollbackParameters(long taskId) {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(taskId);
        return rollbackParameters;
    }

    private List<DlmTask> createDlmTasks() {
        DlmTask dlmTask1 = new DlmTask();
        DlmTask dlmTask2 = new DlmTask();

        dlmTask1.setId(1L);
        dlmTask1.setJobName("jobName");
        dlmTask1.setJobGroup("jobGroup");
        dlmTask1.setJobType(JobType.DATA_ARCHIVE);
        dlmTask1.setSourceDatabaseId(1L);
        dlmTask1.setTargetDatabaseId(2L);
        dlmTask1.setStatus(TaskStatus.PREPARING);

        dlmTask2.setId(2L);
        dlmTask2.setJobName("jobName");
        dlmTask2.setJobGroup("jobGroup");
        dlmTask2.setJobType(JobType.DATA_ARCHIVE);
        dlmTask2.setSourceDatabaseId(2L);
        dlmTask2.setTargetDatabaseId(1L);
        dlmTask2.setStatus(TaskStatus.PREPARING);

        return List.of(dlmTask1, dlmTask2);
    }
}