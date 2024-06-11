import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private JobExecutionContext jobExecutionContext;
 private ScheduleTaskRepository scheduleTaskRepository;
 private List<DlmTask> taskUnits;
 private ScheduleTaskEntity scheduleTaskEntity;
 private DataArchiveRollbackParameters rollbackParameters;

 @Before
 public void setup() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 jobExecutionContext = mock(JobExecutionContext.class);
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 taskUnits = new ArrayList<>();
 scheduleTaskEntity = mock(ScheduleTaskEntity.class);
 rollbackParameters = new DataArchiveRollbackParameters();
 }

 @Test
 public void testExecuteSuccessful() throws Exception {
 when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
 rollbackParameters.setDataArchiveTaskId(1L);
 String jsonString = JsonUtils.toJson(taskUnits);
 when(scheduleTaskEntity.getParametersJson()).thenReturn(jsonString);
 when(scheduleTaskEntity.getResultJson()).thenReturn(jsonString);
 dataArchiveRollbackJob.execute(jobExecutionContext);
 verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
 }

 @Test
 public void testExecuteTaskNotFound() throws Exception {
 when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());
 dataArchiveRollbackJob.execute(jobExecutionContext);
 verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecuteInvalidJson() throws Exception {
 when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
 when(scheduleTaskEntity.getParametersJson()).thenReturn("invalid json");
 dataArchiveRollbackJob.execute(jobExecutionContext);
 verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecuteSuccessfulPrepareTask() throws Exception {
 when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
 rollbackParameters.setDataArchiveTaskId(1L);
 String jsonString = JsonUtils.toJson(taskUnits);
 when(scheduleTaskEntity.getParametersJson()).thenReturn(jsonString);
 when(scheduleTaskEntity.getResultJson()).thenReturn(jsonString);
 dataArchiveRollbackJob.execute(jobExecutionContext);
 verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.DONE));
 }

 @Test
 public void testExecuteFailedPrepareTask() throws Exception {
 when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
 rollbackParameters.setDataArchiveTaskId(1L);
 when(scheduleTaskEntity.getParametersJson()).thenReturn("invalid json");
 dataArchiveRollbackJob.execute(jobExecutionContext);
 verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecuteSuccessfulExecuteTask() throws Exception {
 when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
 rollbackParameters.setDataArchiveTaskId(1L);
 taskUnits.add(new DlmTask());
 String jsonString = JsonUtils.toJson(taskUnits);
 when(scheduleTaskEntity.getParametersJson()).thenReturn(jsonString);
 when(scheduleTaskEntity.getResultJson()).thenReturn(jsonString);
 dataArchiveRollbackJob.execute(jobExecutionContext);
 verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.DONE));
 }

 @Test
 public void testExecuteFailedExecuteTask() throws Exception {
 when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
 rollbackParameters.setDataArchiveTaskId(1L);
 taskUnits.add(null);
 String jsonString = JsonUtils.toJson(taskUnits);
 when(scheduleTaskEntity.getParametersJson()).thenReturn(jsonString);
 when(scheduleTaskEntity.getResultJson()).thenReturn(jsonString);
 dataArchiveRollbackJob.execute(jobExecutionContext);
 verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecuteSuccessfulUpdateStatus() throws Exception {
 when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
 rollbackParameters.setDataArchiveTaskId(1L);
 taskUnits.add(new DlmTask());
 String jsonString = JsonUtils.toJson(taskUnits);
 when(scheduleTaskEntity.getParametersJson()).thenReturn(jsonString);
 when(scheduleTaskEntity.getResultJson()).thenReturn(jsonString);
 dataArchiveRollbackJob.execute(jobExecutionContext);
 verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.DONE));
 }

 @Test
 public void testExecuteFailedUpdateStatus() throws Exception {
 when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
 rollbackParameters.setDataArchiveTaskId(1L);
 when(scheduleTaskEntity.getParametersJson()).thenReturn("invalid json");
 dataArchiveRollbackJob.execute(jobExecutionContext);
 verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
 }
}