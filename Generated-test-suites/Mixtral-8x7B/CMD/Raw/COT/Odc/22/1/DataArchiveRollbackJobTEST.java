import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.AbstractDlmJob;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class DataArchiveRollbackJobTest {

private DataArchiveRollbackJob dataArchiveRollbackJob;
private ScheduleTaskRepository scheduleTaskRepository;
private JobExecutionContext jobExecutionContext;

@Before
public void setUp() {
dataArchiveRollbackJob = new DataArchiveRollbackJob();
scheduleTaskRepository = mock(ScheduleTaskRepository.class);
jobExecutionContext = mock(JobExecutionContext.class);
}

@Test
public void testExecute() throws Exception {
// Arrange
String parametersJson = "{\"dataArchiveTaskId\": 1}";
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(2L);
taskEntity.setParametersJson(parametersJson);
DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
rollbackParameters.setDataArchiveTaskId(1L);
when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));
List<DlmTask> taskUnits = new ArrayList<>();
taskUnits.add(new DlmTask());
String resultJson = JsonUtils.toJson(taskUnits);
ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setResultJson(resultJson);
when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));
TypeReference<List<DlmTask>> typeReference = new TypeReference<List<DlmTask>>() {};
when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), typeReference)).thenReturn(taskUnits);

// Act
dataArchiveRollbackJob.execute(jobExecutionContext);

// Assert
verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
}

@Test
public void testExecuteWithDataArchiveTaskNotFound() throws Exception {
// Arrange
when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

// Act and Assert
assertThrows(IllegalArgumentException.class, () -> dataArchiveRollbackJob.execute(jobExecutionContext));
verify(scheduleTaskRepository, times(0)).updateStatusById(anyLong(), any(TaskStatus.class));
}

@Test
public void testExecuteWithNullParametersJson() throws Exception {
// Arrange
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setParametersJson(null);
when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));

// Act and Assert
assertThrows(IllegalArgumentException.class, () -> dataArchiveRollbackJob.execute(jobExecutionContext));
verify(scheduleTaskRepository, times(0)).updateStatusById(anyLong(), any(TaskStatus.class));
}

@Test
public void testExtractTaskUnits() throws Exception {
// Arrange
List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());
String resultJson = JsonUtils.toJson(taskUnits);
ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setResultJson(resultJson);
TypeReference<List<DlmTask>> typeReference = new TypeReference<List<DlmTask>>() {};

// Act
List<DlmTask> extractedTaskUnits = dataArchiveRollbackJob.extractTaskUnits(dataArchiveTask, typeReference);

// Assert
assertEquals(taskUnits, extractedTaskUnits);
}

@Test
public void testExtractTaskUnitsWithNullResultJson() throws Exception {
// Arrange
ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setResultJson(null);
TypeReference<List<DlmTask>> typeReference = new TypeReference<List<DlmTask>>() {};

// Act and Assert
assertThrows(IllegalArgumentException.class, () -> dataArchiveRollbackJob.extractTaskUnits(dataArchiveTask, typeReference));
}

@Test
public void testExtractTaskUnitsWithNullTypeReference() throws Exception {
// Arrange
ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setResultJson("{}");

// Act and Assert
assertThrows(IllegalArgumentException.class, () -> dataArchiveRollbackJob.extractTaskUnits(dataArchiveTask, null));
}

@Test
public void testGenerateHistoryJobId() {
// Arrange
String jobName = "jobName";
String jobGroup = "jobGroup";
Long taskEntityId = 3L;
int taskUnitsSize = 4;

// Act
String historyJobId = dataArchiveRollbackJob.generateHistoryJobId(jobName, jobGroup, taskEntityId, taskUnitsSize);

// Assert
assertTrue(historyJobId.startsWith(jobName));
assertTrue(historyJobId.contains(jobGroup));
assertTrue(historyJobId.contains(String.valueOf(taskEntityId)));
assertTrue(historyJobId.contains(String.valueOf(taskUnitsSize)));
}

@Test
public void testGetTaskStatus() throws Exception {
// Arrange
List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());
DlmTask firstTask = taskUnits.get(0);
firstTask.setStatus(TaskStatus.PREPARING);
DlmTask secondTask = taskUnits.get(1);
secondTask.setStatus(TaskStatus.DONE);

// Act
TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

// Assert
assertEquals(TaskStatus.DONE, taskStatus);
}

@Test
public void testGetTaskStatusWithAllTasksFailed() throws Exception {
// Arrange
List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());
DlmTask firstTask = taskUnits.get(0);
firstTask.setStatus(TaskStatus.FAILED);
DlmTask secondTask = taskUnits.get(1);
secondTask.setStatus(TaskStatus.FAILED);

// Act
TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

// Assert
assertEquals(TaskStatus.FAILED, taskStatus);
}

@Test
public void testGetTaskStatusWithNoTasksPrepared() throws Exception {
// Arrange
List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());
DlmTask firstTask = taskUnits.get(0);
firstTask.setStatus(TaskStatus.DONE);
DlmTask secondTask = taskUnits.get(1);
secondTask.setStatus(TaskStatus.DONE);

// Act
TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

// Assert
assertEquals(TaskStatus.DONE, taskStatus);
}

@Test
public void testGetTaskStatusWithNoTasks() throws Exception {
// Act
TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(new ArrayList<>());

// Assert
assertEquals(TaskStatus.DONE, taskStatus);
}
}