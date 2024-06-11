import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@Slf4j
public class DataArchiveDeleteJobTest {

@Mock
private JsonUtils jsonUtils;

@Mock
private ScheduleTaskRepository scheduleTaskRepository;

@Mock
private DlmJobIdUtil dlmJobIdUtil;

@InjectMocks
private DataArchiveDeleteJob dataArchiveDeleteJob;

@Before
public void init() {
MockitoAnnotations.initMocks(this);
}

@Test
public void testExecute_DataArchiveTaskNotFound() {
// Arrange
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson("{\"dataArchiveTaskId\": 10}");

DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId(10L);

when(jsonUtils.fromJson(anyString(), any(Class.class))).thenReturn(dataArchiveClearParameters);
when(scheduleTaskRepository.findById(10L)).thenReturn(Optional.empty());

// Act
dataArchiveDeleteJob.execute(null);

// Assert
assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
}

@Test
public void testExecute_DataArchiveTaskNotFinished() {
// Arrange
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson("{\"dataArchiveTaskId\": 10}");

DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId(10L);

ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(10L);
dataArchiveTask.setStatus(TaskStatus.RUNNING);

when(jsonUtils.fromJson(anyString(), any(Class.class))).thenReturn(dataArchiveClearParameters);
when(scheduleTaskRepository.findById(10L)).thenReturn(Optional.of(dataArchiveTask));

// Act
dataArchiveDeleteJob.execute(null);

// Assert
assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
}

@Test
public void testExecute_ClearTasksSuccess() {
// Arrange
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson("{\"dataArchiveTaskId\": 10}");

DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId(10L);
dataArchiveClearParameters.setTaskUnits(Arrays.asList("task1", "task2"));

ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(10L);
dataArchiveTask.setStatus(TaskStatus.DONE);

DlmTask task1 = new DlmTask();
task1.setId("task1");
task1.setJobType(JobType.DELETE);
task1.setStatus(TaskStatus.PREPARING);

DlmTask task2 = new DlmTask();
task2.setId("task2");
task2.setJobType(JobType.DELETE);
task2.setStatus(TaskStatus.PREPARING);

List<DlmTask> taskUnits = Arrays.asList(task1, task2);

when(jsonUtils.fromJson(anyString(), any(Class.class))).thenReturn(dataArchiveClearParameters);
when(scheduleTaskRepository.findById(10L)).thenReturn(Optional.of(dataArchiveTask));
when(jsonUtils.fromJson(anyString(), new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);
when(dlmJobIdUtil.generateHistoryJobId(anyString(), anyString(), anyLong(), anyInt()))
.thenReturn("historyJobId");

// Act
dataArchiveDeleteJob.execute(null);

// Assert
assertEquals(TaskStatus.DONE, taskEntity.getStatus());
}

@Test
public void testExecute_ClearTasksFailed() {
// Arrange
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson("{\"dataArchiveTaskId\": 10}");

DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId(10L);
dataArchiveClearParameters.setTaskUnits(Arrays.asList("task1", "task2"));

ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(10L);
dataArchiveTask.setStatus(TaskStatus.DONE);

DlmTask task1 = new DlmTask();
task1.setId("task1");
task1.setJobType(JobType.DELETE);
task1.setStatus(TaskStatus.FAILED);

DlmTask task2 = new DlmTask();
task2.setId("task2");
task2.setJobType(JobType.DELETE);
task2.setStatus(TaskStatus.FAILED);

List<DlmTask> taskUnits = Arrays.asList(task1, task2);

when(jsonUtils.fromJson(anyString(), any(Class.class))).thenReturn(dataArchiveClearParameters);
when(scheduleTaskRepository.findById(10L)).thenReturn(Optional.of(dataArchiveTask));
when(jsonUtils.fromJson(anyString(), new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);

// Act
dataArchiveDeleteJob.execute(null);

// Assert
assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
}

@Test
public void testGetTaskStatus_AllTasksSucceeded() {
// Arrange
List<DlmTask> taskUnits = Arrays.asList(
new DlmTask().setId("task1").setJobType(JobType.DELETE).setStatus(TaskStatus.DONE),
new DlmTask().setId("task2").setJobType(JobType.DELETE).setStatus(TaskStatus.DONE)
);

// Act
TaskStatus taskStatus = dataArchiveDeleteJob.getTaskStatus(taskUnits);

// Assert
assertEquals(TaskStatus.DONE, taskStatus);
}

@Test
public void testGetTaskStatus_AnyTaskFailed() {
// Arrange
List<DlmTask> taskUnits = Arrays.asList(
new DlmTask().setId("task1").setJobType(JobType.DELETE).setStatus(TaskStatus.DONE),
new DlmTask().setId("task2").setJobType(JobType.DELETE).setStatus(TaskStatus.FAILED)
);

// Act
TaskStatus taskStatus = dataArchiveDeleteJob.getTaskStatus(taskUnits);

// Assert
assertEquals(TaskStatus.FAILED, taskStatus);
}
}