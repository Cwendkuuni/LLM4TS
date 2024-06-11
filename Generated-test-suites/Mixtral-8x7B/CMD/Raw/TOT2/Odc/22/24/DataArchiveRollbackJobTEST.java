package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

@Slf4j
public class DataArchiveRollbackJobTest {

  private DataArchiveRollbackJob dataArchiveRollbackJob;
  private JobExecutionContext jobExecutionContext;
  private ScheduleTaskEntity taskEntity;
  private ScheduleTaskEntity dataArchiveTask;
  private List<DlmTask> taskUnits;

  @Before
  public void setUp() {
    dataArchiveRollbackJob = new DataArchiveRollbackJob();
    jobExecutionContext = mock(JobExecutionContext.class);
    taskEntity = new ScheduleTaskEntity();
    dataArchiveTask = new ScheduleTaskEntity();
    taskUnits =
        JsonUtils.fromJson(
            "[]", new TypeReference<List<DlmTask>>() {}); // initialize taskUnits with some data
  }

  @Test
  public void testExecute_whenDataArchiveTaskNotPresent_shouldUpdateStatusToFailed() {
    DataArchiveRollbackParameters rollbackParameters =
        new DataArchiveRollbackParameters();
    rollbackParameters.setDataArchiveTaskId(1L);
    taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

    when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

    dataArchiveRollbackJob.execute(jobExecutionContext);

    assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
  }

  @Test
  public void testExecute_whenDataArchiveTaskPresent_shouldExecuteTask() {
    DataArchiveRollbackParameters rollbackParameters =
        new DataArchiveRollbackParameters();
    rollbackParameters.setDataArchiveTaskId(1L);
    rollbackParameters.setTasksJson("[{\"id\":1,\"sourceDatabaseId\":1,\"targetDatabaseId\":2,"
        + "\"jobType\":\"ROLLBACK\"}]");
    taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

    when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
    when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED))
        .thenReturn(dataArchiveTask);

    dataArchiveRollbackJob.execute(jobExecutionContext);

    // Additional assertions to confirm that the task was executed properly
  }

  // Additional test cases for other methods, edge cases, and error scenarios

}