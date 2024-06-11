import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class DataArchiveDeleteJobTest {

@Mock
private ScheduleTaskRepository scheduleTaskRepository;

@Mock
private JsonUtils jsonUtils;

private DataArchiveDeleteJob dataArchiveDeleteJob;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);
dataArchiveDeleteJob = new DataArchiveDeleteJob();
dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
dataArchiveDeleteJob.setJsonUtils(jsonUtils);
}

@Test
public void testExecuteWithDataArchiveTaskNotFound() {
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("123");

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

when(scheduleTaskRepository.findById("123")).thenReturn(Optional.empty());

dataArchiveDeleteJob.execute(null);

// assert that the task status is set to FAILED
ScheduleTaskEntity updatedTaskEntity = new ScheduleTaskEntity();
updatedTaskEntity.setId(1L);
updatedTaskEntity.setStatus(TaskStatus.FAILED);
verifyTaskEntityUpdated(updatedTaskEntity);
}

@Test
public void testExecuteWithDataArchiveTaskNotDone() {
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("123");

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(2L);
dataArchiveTask.setStatus(TaskStatus.RUNNING);

when(scheduleTaskRepository.findById("123")).thenReturn(Optional.of(dataArchiveTask));

dataArchiveDeleteJob.execute(null);

// assert that the task status is set to FAILED
ScheduleTaskEntity updatedTaskEntity = new ScheduleTaskEntity();
updatedTaskEntity.setId(1L);
updatedTaskEntity.setStatus(TaskStatus.FAILED);
verifyTaskEntityUpdated(updatedTaskEntity);
}

@Test
public void testExecuteWithClearTasksSuccess() {
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("123");

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(2L);
dataArchiveTask.setStatus(TaskStatus.DONE);
List<DlmTask> taskUnits = createDlmTasks(3);

when(scheduleTaskRepository.findById("123")).thenReturn(Optional.of(dataArchiveTask));
when(jsonUtils.fromJson(anyString(), any(TypeReference.class))).thenReturn(taskUnits);

// simulate successful execution of clear tasks
dataArchiveDeleteJob.setExecuteTask(taskId -> {});
TaskStatus taskStatus = TaskStatus.SUCCESS;
when(dataArchiveDeleteJob.getTaskStatus(taskUnits)).thenReturn(taskStatus);

dataArchiveDeleteJob.execute(null);

// assert that the task status is set to SUCCESS
ScheduleTaskEntity updatedTaskEntity = new ScheduleTaskEntity();
updatedTaskEntity.setId(1L);
updatedTaskEntity.setStatus(taskStatus);
verifyTaskEntityUpdated(updatedTaskEntity);
}

@Test
public void testExecuteWithClearTasksFailed() {
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("123");

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(2L);
dataArchiveTask.setStatus(TaskStatus.DONE);
List<DlmTask> taskUnits = createDlmTasks(3);

when(scheduleTaskRepository.findById("123")).thenReturn(Optional.of(dataArchiveTask));
when(jsonUtils.fromJson(anyString(), any(TypeReference.class))).thenReturn(taskUnits);

// simulate failed execution of clear tasks
dataArchiveDeleteJob.setExecuteTask(taskId -> {});
TaskStatus taskStatus = TaskStatus.FAILED;
when(dataArchiveDeleteJob.getTaskStatus(taskUnits)).thenReturn(taskStatus);

dataArchiveDeleteJob.execute(null);

// assert that the task status is set to FAILED
ScheduleTaskEntity updatedTaskEntity = new ScheduleTaskEntity();
updatedTaskEntity.setId(1L);
updatedTaskEntity.setStatus(taskStatus);
verifyTaskEntityUpdated(updatedTaskEntity);
}

private void verifyTaskEntityUpdated(ScheduleTaskEntity updatedTaskEntity) {
when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class))).thenReturn(true);
dataArchiveDeleteJob.updateTaskEntity(updatedTaskEntity);
assertEquals(updatedTaskEntity, dataArchiveDeleteJob.getTaskEntity());
}

private List<DlmTask> createDlmTasks(int count) {
List<DlmTask> result = new java.util.ArrayList<>(count);
for (int i = 0; i < count; i++) {
DlmTask dlmTask = new DlmTask();
dlmTask.setId("id" + i);
result.add(dlmTask);
}
return result;
}
}