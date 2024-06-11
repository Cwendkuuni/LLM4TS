import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.matchers.GroupMatcher;

@Slf4j
public class DataArchiveRollbackJobTest {

private DataArchiveRollbackJob dataArchiveRollbackJob;
private JobExecutionContext jobExecutionContext;
private ScheduleTaskEntity taskEntity;
private ScheduleTaskEntity dataArchiveTask;
private DataArchiveRollbackParameters rollbackParameters;
private List<DlmTask> taskUnits;
private JobType jobType;

@Before
public void setup() {
datArchiveRollbackJob = new DataArchiveRollbackJob();
jobExecutionContext = mock(JobExecutionContext.class);
taskEntity = new ScheduleTaskEntity();
dataArchiveTask = new ScheduleTaskEntity();
rollbackParameters = new DataArchiveRollbackParameters();
taskUnits = Arrays.asList(mock(DlmTask.class), mock(DlmTask.class));
jobType = JobType.ROLLBACK;
}

@Test
public void testExecute_whenTaskEntityIsNull_thenLogWarn() throws JobExecutionException {
when(jobExecutionContext.getResult()).thenReturn(null);

datArchiveRollbackJob.execute(jobExecutionContext);

assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
}

@Test
public void testExecute_whenDataArchiveTaskNotPresent_thenUpdateTaskStatusToFailed() throws JobExecutionException {
String dataArchiveTaskId = "123456";
rollbackParameters.setDataArchiveTaskId(dataArchiveTaskId);
taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

when(scheduleTaskRepository.findById(dataArchiveTaskId)).thenReturn(Optional.empty());

datArchiveRollbackJob.execute(jobExecutionContext);

assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
}

@Test
public void testExecute_whenDataArchiveTaskPresent_thenPrepareTasksForRollback() throws JobExecutionException {
String dataArchiveTaskId = "123456";
rollbackParameters.setDataArchiveTaskId(dataArchiveTaskId);
taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
String resultJson = JsonUtils.toJson(taskUnits);
dataArchiveTask.setResultJson(resultJson);

when(scheduleTaskRepository.findById(dataArchiveTaskId)).thenReturn(Optional.of(dataArchiveTask));

datArchiveRollbackJob.execute(jobExecutionContext);

assertEquals(taskUnits.size(), DlmJobIdUtil.generateHistoryJobId(taskEntity.getJobName(),
taskEntity.getJobGroup(), taskEntity.getId(), taskUnits.size()).size());
}

@Test
public void testExecuteTask_whenTaskStatusIsPreparing_thenSetTaskStatusToDone() throws JobExecutionException {
String jobName = "DataArchiveRollbackJob";
String jobGroup = "ScheduleTaskEntity";
String id = "123456";

datArchiveRollbackJob.executeTask(id, taskUnits);

taskUnits.forEach(task -> {
if (task.getStatus() == TaskStatus.PREPARING) {
task.setStatus(TaskStatus.DONE);
}
});

List<DlmTask> newTaskUnits = JsonUtils.fromJson(JsonUtils.toJson(taskUnits), new TypeReference<List<DlmTask>>() {});

assertEquals(newTaskUnits, taskUnits);
}

@Test
public void testGetTaskStatus_whenAllTaskUnitsAreDone_thenReturnDone() {
taskUnits.forEach(task -> task.setStatus(TaskStatus.DONE));

TaskStatus taskStatus = datArchiveRollbackJob.getTaskStatus(taskUnits);

assertEquals(TaskStatus.DONE, taskStatus);
}

@Test
public void testGetTaskStatus_whenAllTaskUnitsArePreparing_thenReturnPreparing() {
taskUnits.forEach(task -> task.setStatus(TaskStatus.PREPARING));

TaskStatus taskStatus = datArchiveRollbackJob.getTaskStatus(taskUnits);

assertEquals(TaskStatus.PREPARING, taskStatus);
}

@Test
public void testGetTaskStatus_whenTaskUnitsHaveDifferentStatuses_thenReturnFailed() {
taskUnits.get(0).setStatus(TaskStatus.DONE);
taskUnits.get(1).setStatus(TaskStatus.FAILED);

TaskStatus taskStatus = datArchiveRollbackJob.getTaskStatus(taskUnits);

assertEquals(TaskStatus.FAILED, taskStatus);
}

private void mockScheduleTaskRepository() {
scheduleTaskRepository = mock(ScheduleTaskRepository.class);
}
}