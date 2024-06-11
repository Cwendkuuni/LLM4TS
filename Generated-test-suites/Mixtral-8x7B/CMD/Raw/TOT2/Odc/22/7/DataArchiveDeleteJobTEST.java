import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private JobExecutionContext jobExecutionContext;
 private ScheduleTaskEntity scheduleTaskEntity;
 private ScheduleTaskEntity dataArchiveTask;
 private DataArchiveClearParameters dataArchiveClearParameters;
 private List<DlmTask> taskUnits;

 @Before
 public void setup() {
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 jobExecutionContext = mock(JobExecutionContext.class);
 scheduleTaskEntity = new ScheduleTaskEntity();
 dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveClearParameters = new DataArchiveClearParameters();
 taskUnits = new ArrayList<>();
 }

 @Test
 public void testExecute_whenDataArchiveTaskNotPresent_thenTaskStatusIsFailed() throws JobExecutionException {
 dataArchiveClearParameters.setDataArchiveTaskId("123");
 scheduleTaskEntity.setId("456");
 scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\":\"123\"}");
 when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.ofNullable(null));

 dataArchiveDeleteJob.execute(jobExecutionContext);

 assertEquals(scheduleTaskEntity.getStatus(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_whenDataArchiveTaskNotFinished_thenTaskStatusIsFailed() throws JobExecutionException {
 dataArchiveClearParameters.setDataArchiveTaskId("123");
 dataArchiveClearParameters.setDataArchiveTaskStatus(TaskStatus.RUNNING);
 scheduleTaskEntity.setId("456");
 scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\":\"123\"}");
 scheduleTaskEntity.setStatus(TaskStatus.PREPARING);
 when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.ofNullable(dataArchiveTask));

 dataArchiveDeleteJob.execute(jobExecutionContext);

 assertEquals(scheduleTaskEntity.getStatus(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_whenAllConditionsAreMet_thenTaskStatusIsSuccess() throws JobExecutionException {
 dataArchiveClearParameters.setDataArchiveTaskId("123");
 dataArchiveClearParameters.setDataArchiveTaskStatus(TaskStatus.DONE);
 scheduleTaskEntity.setId("456");
 scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\":\"123\"}");
 scheduleTaskEntity.setStatus(TaskStatus.PREPARING);
 dataArchiveTask.setId("123");
 dataArchiveTask.setStatus(TaskStatus.DONE);
 taskUnits.add(new DlmTask());
 when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.ofNullable(dataArchiveTask));

 dataArchiveDeleteJob.execute(jobExecutionContext);

 assertEquals(scheduleTaskEntity.getStatus(), TaskStatus.SUCCESS);
 }

 private AbstractDlmJob.TaskStatus getTaskStatus(List<DlmTask> taskUnits) {
 return AbstractDlmJob.TaskStatus.SUCCESS;
 }

 private void executeTask(String id, List<DlmTask> taskUnits) {
 // Mock implementation
 }

 private <T> T fromJson(String json, TypeReference<T> typeReference) {
 // Mock implementation
 return null;
 }
}