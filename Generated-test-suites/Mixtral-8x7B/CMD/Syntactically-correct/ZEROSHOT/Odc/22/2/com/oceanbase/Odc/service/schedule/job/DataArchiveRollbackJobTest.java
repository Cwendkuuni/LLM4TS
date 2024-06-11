import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {

 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;

 @Mock
 private AbstractDlmJob abstractDlmJob;

 @InjectMocks
 private DataArchiveRollbackJob dataArchiveRollbackJob;

 @Before
 public void init() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testExecute() {
 // Arrange
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setParametersJson("{}");
 taskEntity.setResultJson("[]");

 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(2L);

 List<DlmTask> taskUnits = JsonUtils.fromJson("[[{\"id\":1,\"sourceDatabaseId\":2,\"targetDatabaseId\":3}]]",
 new TypeReference<List<DlmTask>>() {});

 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));
 when(abstractDlmJob.executeTask(anyLong(), any())).thenReturn(true);
 doNothing().when(scheduleTaskRepository).updateStatusById(anyLong(), any(TaskStatus.class));

 // Act
 dataArchiveRollbackJob.execute(null);

 // Assert
 assertEquals(taskEntity.getStatus(), TaskStatus.DONE);
 }

 @Test
 public void testExecute_whenDataArchiveTaskNotFound_thenTaskStatusFailed() {
 // Arrange
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setParametersJson("{}");
 taskEntity.setResultJson("[]");

 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(2L);

 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

 // Act
 dataArchiveRollbackJob.execute(null);

 // Assert
 assertEquals(taskEntity.getStatus(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_whenExecuteTaskFailed_thenTaskStatusFailed() {
 // Arrange
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setParametersJson("{}");
 taskEntity.setResultJson("[]");

 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(2L);

 List<DlmTask> taskUnits = JsonUtils.fromJson("[[{\"id\":1,\"sourceDatabaseId\":2,\"targetDatabaseId\":3}]]",
 new TypeReference<List<DlmTask>>() {});

 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));
 when(abstractDlmJob.executeTask(anyLong(), any())).thenReturn(false);
 doNothing().when(scheduleTaskRepository).updateStatusById(anyLong(), any(TaskStatus.class));

 // Act
 dataArchiveRollbackJob.execute(null);

 // Assert
 assertEquals(taskEntity.getStatus(), TaskStatus.FAILED);
 }
}