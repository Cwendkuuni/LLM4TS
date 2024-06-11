package com.oceanbase.odc.service.schedule.job;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Slf4j
public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;
    private JobExecutionContext jobExecutionContext;
    private ScheduleTaskRepository scheduleTaskRepository;
    private MongoTemplate mongoTemplate;

    @Before
    public void setUp() {
        dataArchiveDeleteJob = spy(new DataArchiveDeleteJob());
        jobExecutionContext = mock(JobExecutionContext.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        mongoTemplate = mock(MongoTemplate.class);

        dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
        dataArchiveDeleteJob.setMongoTemplate(mongoTemplate);
    }

    @Test
    public void testExecute_whenDataArchiveTaskNotFound_shouldUpdateTaskStatusToFailed() throws JobExecutionException {
        // Arrange
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("taskId");
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        doReturn(Optional.empty()).when(dataArchiveDeleteJob).findDataArchiveTaskById("taskId");

        // Act
        dataArchiveDeleteJob.execute(jobExecutionContext);

        // Assert
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_whenDataArchiveTaskNotFinished_shouldUpdateTaskStatusToFailed() throws JobExecutionException {
        // Arrange
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("taskId");
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.RUNNING);

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        doReturn(Optional.of(dataArchiveTask)).when(dataArchiveDeleteJob).findDataArchiveTaskById("taskId");

        // Act
        dataArchiveDeleteJob.execute(jobExecutionContext);

        // Assert
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void testExecute_whenTasksForClearPrepared_shouldExecuteTasks() throws JobExecutionException {
        // Arrange
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("taskId");
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.DONE);
        dataArchiveTask.setResultJson("[{\"id\": \"1\", \"name\": \"test\"}]");

        List<DlmTask> taskUnits = JsonUtils.fromJson("[{\"id\": \"1\", \"name\": \"test\"}]",
                new TypeReference<List<DlmTask>>() {});

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        doReturn(Optional.of(dataArchiveTask)).when(dataArchiveDeleteJob).findDataArchiveTaskById("taskId");
        when(dataArchiveDeleteJob.prepareTasksForClear(any(), anyList())).thenReturn(taskUnits);
        dataArchiveDeleteJob.executeTask = mock(DataArchiveDeleteJob::executeTask);

        // Act
        dataArchiveDeleteJob.execute(jobExecutionContext);

        // Assert
        verify(dataArchiveDeleteJob, times(1)).executeTask(taskEntity.getId(), taskUnits);
        verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), any(TaskStatus.class));
    }

    private Optional<ScheduleTaskEntity> findDataArchiveTaskById(String taskId) {
        ScheduleTaskEntity task = new ScheduleTaskEntity();
        task.setId("123");
        return Optional.of(task);
    }

    private List<DlmTask> prepareTasksForClear(String dataArchiveTaskId, List<DlmTask> taskUnits) {
        return taskUnits;
    }

    private void executeTask(String taskId, List<DlmTask> taskUnits) {
    }

    private TaskStatus getTaskStatus(List<DlmTask> taskUnits) {
        return TaskStatus.DONE;
    }
}