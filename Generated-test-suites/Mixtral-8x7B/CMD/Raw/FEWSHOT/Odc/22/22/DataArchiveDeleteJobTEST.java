import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.util.JobThread;
import com.oceanbase.tools.migrator.common.enums.JobType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

@Slf4j
public class DataArchiveDeleteJobTest {

private DataArchiveDeleteJob dataArchiveDeleteJob;
private ScheduleTaskRepository scheduleTaskRepository;
private JobExecutionContext jobExecutionContext;
private JobThread jobThread;
private DataArchiveClearParameters dataArchiveClearParameters;
private ScheduleTaskEntity taskEntity;
private List<DlmTask> taskUnits;
private Optional<ScheduleTaskEntity> dataArchiveTaskOption;

@Before
public void setup() {
dataArchiveDeleteJob = spy(new DataArchiveDeleteJob());
scheduleTaskRepository = mock(ScheduleTaskRepository.class);
jobExecutionContext = mock(JobExecutionContext.class);
jobThread = mock(JobThread.class);
dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId(1L);

taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson("{\"dataArchiveTaskId\":1}");
taskEntity.setStatus(TaskStatus.DONE);
taskEntity.setResultJson("[]");

dataArchiveTaskOption = Optional.of(taskEntity);

taskUnits = new ArrayList<>();
DlmTask taskUnit1 = new DlmTask();
taskUnit1.setId(1L);
taskUnit1.setJobType(JobType.DELETE);
taskUnit1.setStatus(TaskStatus.PREPARING);
taskUnits.add(taskUnit1);

doReturn(jobThread).when(dataArchiveDeleteJob).getCurrentThread();
doReturn(scheduleTaskRepository).when(dataArchiveDeleteJob).getScheduleTaskRepository();
doReturn(taskEntity).when(jobExecutionContext).getResult();
doReturn(dataArchiveClearParameters).when(dataArchiveDeleteJob).getParameters(anyString(), any(TypeReference.class));
doReturn(dataArchiveTaskOption).when(scheduleTaskRepository).findById(anyLong());
}

@Test
public void testExecute_whenDataArchiveTaskNotFound_thenUpdateStatusByIdToFailed() throws Exception {
// Arrange
taskEntity.setStatus(TaskStatus.RUNNING);

// Act
dataArchiveDeleteJob.execute(jobExecutionContext);

// Assert
verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
}

@Test
public void testExecute_whenDataArchiveTaskNotDone_thenUpdateStatusByIdToFailed() throws Exception {
// Arrange
taskEntity.setStatus(TaskStatus.PREPARING);

// Act
dataArchiveDeleteJob.execute(jobExecutionContext);

// Assert
verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
}

@Test
public void testExecute_whenDataArchiveTaskFoundAndDone_thenPrepareTasksForClear() throws Exception {
// Arrange

// Act
dataArchiveDeleteJob.execute(jobExecutionContext);

// Assert
verify(scheduleTaskRepository, times(0)).updateStatusById(anyLong(), any(TaskStatus.class));
assertNotNull(taskUnits.get(0).getId());
assertEquals(JobType.DELETE, taskUnits.get(0).getJobType());
assertEquals(TaskStatus.PREPARING, taskUnits.get(0).getStatus());
}

@Test
public void testExecute_whenTaskUnitsPrepared_thenExecuteTask() throws Exception {
// Arrange
when(scheduleTaskRepository.findById(anyLong())).thenReturn(dataArchiveTaskOption);

// Act
dataArchiveDeleteJob.execute(jobExecutionContext);

// Assert
verify(dataArchiveDeleteJob, times(1)).executeTask(anyLong(), anyList());
}

@Test
public void testExecute_whenTaskUnitsExecuted_thenUpdateStatusById() throws Exception {
// Arrange
when(scheduleTaskRepository.findById(anyLong())).thenReturn(dataArchiveTaskOption);

// Act
dataArchiveDeleteJob.execute(jobExecutionContext);

// Assert
verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
}

@Test
public void testExecute_whenTaskUnitsExecutedFailed_thenUpdateStatusByIdToFailed() throws Exception {
// Arrange
when(scheduleTaskRepository.findById(anyLong())).thenReturn(dataArchiveTaskOption);
doReturn(TaskStatus.FAILED).when(dataArchiveDeleteJob).getTaskStatus(anyList());

// Act
dataArchiveDeleteJob.execute(jobExecutionContext);

// Assert
verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
}

@Test
public void testGetParameters_whenParametersJsonIsEmpty_thenReturnNull() throws Exception {
// Arrange
taskEntity.setParametersJson("");

// Act
DataArchiveClearParameters result = dataArchiveDeleteJob.getParameters(taskEntity.getParametersJson(),
DataArchiveClearParameters.class);

// Assert
assertNull(result);
}

@Test
public void testGetParameters_whenParametersJsonIsNotNull_thenReturnParameters() throws Exception {
// Arrange

// Act
DataArchiveClearParameters result = dataArchiveDeleteJob.getParameters(taskEntity.getParametersJson(),
DataArchiveClearParameters.class);

// Assert
assertNotNull(result);
assertEquals(1, result.getDataArchiveTaskId().longValue());
}

@Test
public void testGetTaskStatus_whenTaskUnitsIsEmpty_thenReturnNull() throws Exception {
// Arrange

// Act
TaskStatus result = dataArchiveDeleteJob.getTaskStatus(new ArrayList<>());

// Assert
assertNull(result);
}

@Test
public void testGetTaskStatus_whenTaskUnitsIsNotNull_thenReturnTaskStatus() throws Exception {
// Arrange

// Act
TaskStatus result = dataArchiveDeleteJob.getTaskStatus(taskUnits);

// Assert
assertEquals(TaskStatus.PREPARING, result);
}
}