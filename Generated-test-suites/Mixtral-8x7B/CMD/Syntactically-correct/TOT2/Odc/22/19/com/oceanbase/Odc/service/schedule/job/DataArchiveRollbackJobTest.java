import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.AbstractDlmJob;
import com.oceanbase.odc.utils.JsonUtils;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = DataArchiveRollbackJob.class)
public class DataArchiveRollbackJobTest extends AbstractDlmJob {

@Autowired
private ScheduleTaskRepository scheduleTaskRepository;

@MockBean
private JobExecutionContext jobExecutionContext;

private DataArchiveRollbackJob dataArchiveRollbackJob;

@Before
public void setup() {
dataArchiveRollbackJob = new DataArchiveRollbackJob();
dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
}

@Test
public void testExecute_Success() throws JobExecutionException {
// Arrange
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setJobName("DataArchiveRollbackJob");
taskEntity.setJobGroup("group");
taskEntity.setParametersJson("{\"dataArchiveTaskId\":10L}");

DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
rollbackParameters.setDataArchiveTaskId(10L);

when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));

List<DlmTask> taskUnits = new ArrayList<>();
DlmTask taskUnit = new DlmTask();
taskUnit.setSourceDatabaseId(1L);
taskUnit.setTargetDatabaseId(2L);
taskUnits.add(taskUnit);

String resultJson = JsonUtils.toJson(taskUnits);
when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
.thenReturn(Optional.of(new ScheduleTaskEntity().setResultJson(resultJson)));

// Act
dataArchiveRollbackJob.execute(jobExecutionContext);

// Assert
assertEquals(TaskStatus.DONE, scheduleTaskRepository.findById(1L).get().getStatus());
}

@Test
public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
// Arrange
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setJobName("DataArchiveRollbackJob");
taskEntity.setJobGroup("group");
taskEntity.setParametersJson("{\"dataArchiveTaskId\":10L}");

DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
rollbackParameters.setDataArchiveTaskId(10L);

when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));
when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
.thenReturn(Optional.ofNullable(null));

// Act and Assert
try {
dataArchiveRollbackJob.execute(jobExecutionContext);
} catch (JobExecutionException e) {
assertEquals("Data archive task not found, rollback task fast failed.", e.getMessage());
}
}

@Test
public void testExecute_FailedToUpdateStatus() throws JobExecutionException {
// Arrange
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setJobName("DataArchiveRollbackJob");
taskEntity.setJobGroup("group");
taskEntity.setParametersJson("{\"dataArchiveTaskId\":10L}");

DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
rollbackParameters.setDataArchiveTaskId(10L);

when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));
when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
.thenReturn(Optional.of(taskEntity));

when(scheduleTaskRepository.updateStatusById(anyLong(), any()))
.thenThrow(new RuntimeException());

// Act and Assert
try {
dataArchiveRollbackJob.execute(jobExecutionContext);
} catch (JobExecutionException e) {
assertEquals("Failed to update task status.", e.getMessage());
}
}

}