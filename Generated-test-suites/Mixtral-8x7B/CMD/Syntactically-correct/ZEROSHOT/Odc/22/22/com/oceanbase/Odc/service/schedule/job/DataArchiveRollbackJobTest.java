import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {

 @Mock
 private AbstractDlmJob abstractDlmJob;

 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;

 private DataArchiveRollbackJob dataArchiveRollbackJob;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 dataArchiveRollbackJob.setAbstractDlmJob(abstractDlmJob);
 dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
 }

 @Test
 public void testExecute() {
 // Arrange
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

 Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(taskEntity);
 when(scheduleTaskRepository.findById(1L)).thenReturn(dataArchiveTaskOption);

 List<DlmTask> taskUnits = JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {});
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenReturn(null);
 when(JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);

 // Act
 dataArchiveRollbackJob.execute(null);

 // Assert
 verify(scheduleTaskRepository, times(1)).findById(1L);
 verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.FAILED);
 }

 @Test
 public void testExecuteWithDataArchiveTaskNotFound() {
 // Arrange
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

 Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.ofNullable(null);
 when(scheduleTaskRepository.findById(1L)).thenReturn(dataArchiveTaskOption);

 // Act
 dataArchiveRollbackJob.execute(null);

 // Assert
 verify(scheduleTaskRepository, times(1)).findById(1L);
 verify(scheduleTaskRepository, never()).updateStatusById(1L, TaskStatus.FAILED);
 }

 @Test
 public void testExecuteTask() {
 // Arrange
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);

 List<DlmTask> taskUnits = JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {});

 // Act
 dataArchiveRollbackJob.executeTask(1L, taskUnits);

 // Assert
 verify(abstractDlmJob, times(1)).executeTask(1L, taskUnits);
 }

 @Test
 public void testGetTaskStatus() {
 // Arrange
 List<DlmTask> taskUnits = JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {});

 // Act
 TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

 // Assert
 assertEquals(TaskStatus.DONE, taskStatus);
 }
}