import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class DataArchiveDeleteJobTest extends AbstractDlmJobTest {

 @MockBean
 private ScheduleTaskRepository scheduleTaskRepository;

 private DataArchiveDeleteJob dataArchiveDeleteJob;

 @Before
 public void setUp() {
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
 }

 @Test
 public void testExecute() throws JobExecutionException {
 JobExecutionContext jobExecutionContext = createJobExecutionContext();
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setStatus(TaskStatus.DONE);
 taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
 taskEntity.setResultJson("[]");

 when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(taskEntity));

 dataArchiveDeleteJob.execute(jobExecutionContext);
 }

 @Test
 public void testExecute_whenDataArchiveTaskNotFound_thenJobFailed() throws JobExecutionException {
 JobExecutionContext jobExecutionContext = createJobExecutionContext();
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setStatus(TaskStatus.DONE);
 taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
 taskEntity.setResultJson("[]");

 when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.empty());

 dataArchiveDeleteJob.execute(jobExecutionContext);

 assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
 }

 @Test
 public void testExecute_whenDataArchiveTaskNotFinished_thenJobFailed() throws JobExecutionException {
 JobExecutionContext jobExecutionContext = createJobExecutionContext();
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setStatus(TaskStatus.RUNNING);
 taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
 taskEntity.setResultJson("[]");

 when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(taskEntity));

 dataArchiveDeleteJob.execute(jobExecutionContext);

 assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
 }

 @Test
 public void testExecute_whenPrepareTasksForClear_thenJobPrepareTasks() throws JobExecutionException {
 JobExecutionContext jobExecutionContext = createJobExecutionContext();
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setStatus(TaskStatus.DONE);
 taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
 taskEntity.setResultJson("[]");

 DlmTask dlmTask1 = new DlmTask();
 dlmTask1.setId(1L);
 dlmTask1.setJobType(JobType.DELETE);
 DlmTask dlmTask2 = new DlmTask();
 dlmTask2.setId(2L);
 dlmTask2.setJobType(JobType.DELETE);

 when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(taskEntity));
 when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class)))
 .thenReturn(1);
 when(jsonUtils.fromJson(anyString(), any(TypeReference.class)))
 .thenReturn(List.of(dlmTask1, dlmTask2));

 dataArchiveDeleteJob.execute(jobExecutionContext);

 assertEquals(2, taskEntity.getTaskUnits().size());
 assertEquals(JobType.DELETE, taskEntity.getTaskUnits().get(0).getJobType());
 }

 private JobExecutionContext createJobExecutionContext() {
 JobExecutionContext jobExecutionContext = new JobExecutionContext();
 jobExecutionContext.setResult(new ScheduleTaskEntity());
 return jobExecutionContext;
 }
}