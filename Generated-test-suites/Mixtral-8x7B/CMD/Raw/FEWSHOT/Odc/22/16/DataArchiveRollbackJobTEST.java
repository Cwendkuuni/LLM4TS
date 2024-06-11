import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.impl.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataArchiveRollbackJobTest {

private static final Logger log = LoggerFactory.getLogger(DataArchiveRollbackJobTest.class);
private DataArchiveRollbackJob dataArchiveRollbackJob;
private JobExecutionContext jobExecutionContext;
private ScheduleTaskEntity taskEntity;
private DataArchiveRollbackParameters rollbackParameters;
private List<DlmTask> taskUnits;
private ScheduleTaskEntity dataArchiveTask;
private Optional<ScheduleTaskEntity> dataArchiveTaskOption;

@Before
public void setUp() {
dataArchiveRollbackJob = new DataArchiveRollbackJob();
jobExecutionContext = mock(JobExecutionContext.class);
taskEntity = mock(ScheduleTaskEntity.class);
rollbackParameters = new DataArchiveRollbackParameters();
taskUnits = mock(List.class);
dataArchiveTask = mock(ScheduleTaskEntity.class);
dataArchiveTaskOption = Optional.of(dataArchiveTask);
}

@Test
public void testExecute() throws JobExecutionException {
when(jobExecutionContext.getResult()).thenReturn(taskEntity);
when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(rollbackParameters));
when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
.thenReturn(dataArchiveTaskOption);
when(dataArchiveTask.getResultJson()).thenReturn(JsonUtils.toJson(taskUnits));
when(taskUnits.size()).thenReturn(10);

dataArchiveRollbackJob.execute(jobExecutionContext);

// Add your assertions here
}

@Test
public void testExecute_dataArchiveTaskNotFound() throws JobExecutionException {
when(jobExecutionContext.getResult()).thenReturn(taskEntity);
when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(rollbackParameters));
when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
.thenReturn(Optional.empty());

dataArchiveRollbackJob.execute(jobExecutionContext);

// Add your assertions here
}

@Test
public void testExecute_prepareTasksForRollback() throws JobExecutionException {
when(jobExecutionContext.getResult()).thenReturn(taskEntity);
when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(rollbackParameters));
when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
.thenReturn(dataArchiveTaskOption);
when(dataArchiveTask.getResultJson()).thenReturn(JsonUtils.toJson(taskUnits));
when(taskUnits.size()).thenReturn(10);
DlmTask taskUnit = new DlmTask();
when(taskUnit.getId()).thenReturn(1L);
when(taskUnits.get(0)).thenReturn(taskUnit);

dataArchiveRollbackJob.execute(jobExecutionContext);

// Add your assertions here
}

private void setUpExecuteTaskAndGetTaskStatus(List<DlmTask> taskUnits)
throws JobExecutionException {
when(jobExecutionContext.getResult()).thenReturn(taskEntity);
when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(rollbackParameters));
when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
.thenReturn(dataArchiveTaskOption);
when(dataArchiveTask.getResultJson()).thenReturn(JsonUtils.toJson(taskUnits));
when(taskUnits.size()).thenReturn(10);
}

@Test
public void testExecuteTaskAndGetTaskStatus_allTasksSucceeded()
throws JobExecutionException {
List<DlmTask> taskUnits = createTaskUnits(10, true);
setUpExecuteTaskAndGetTaskStatus(taskUnits);

TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

assertEquals(TaskStatus.SUCCEEDED, taskStatus);
}

@Test
public void testExecuteTaskAndGetTaskStatus_someTasksFailed()
throws JobExecutionException {
List<DlmTask> taskUnits = createTaskUnits(10, true);
taskUnits.set(5, createFailedTaskUnit());
setUpExecuteTaskAndGetTaskStatus(taskUnits);

TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

assertEquals(TaskStatus.PARTIALLY_FAILED, taskStatus);
}

@Test
public void testExecuteTaskAndGetTaskStatus_allTasksFailed()
throws JobExecutionException {
List<DlmTask> taskUnits = createTaskUnits(10, true);
for (int i = 0; i < taskUnits.size(); i++) {
taskUnits.set(i, createFailedTaskUnit());
}
setUpExecuteTaskAndGetTaskStatus(taskUnits);

TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

assertEquals(TaskStatus.FAILED, taskStatus);
}

@Test
public void testExecuteTaskAndGetTaskStatus_tasksNotStarted()
throws JobExecutionException {
List<DlmTask> taskUnits = createTaskUnits(10, false);
setUpExecuteTaskAndGetTaskStatus(taskUnits);

TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

assertEquals(TaskStatus.CREATED, taskStatus);
}

private List<DlmTask> createTaskUnits(int count, boolean started) {
List<DlmTask> taskUnits = mock(List.class);
for (int i = 0; i < count; i++) {
DlmTask taskUnit = mock(DlmTask.class);
when(taskUnit.isStarted()).thenReturn(started);
taskUnits.add(taskUnit);
}
return taskUnits;
}

private DlmTask createFailedTaskUnit() {
DlmTask taskUnit = mock(DlmTask.class);
when(taskUnit.isStarted()).thenReturn(true);
when(taskUnit.isSucceeded()).thenReturn(false);
return taskUnit;
}
}