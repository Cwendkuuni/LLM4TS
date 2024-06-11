import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

 @Mock
 private AbstractDlmJob.ScheduleTaskRepository scheduleTaskRepository;

 @InjectMocks
 private DataArchiveRollbackJob dataArchiveRollbackJob;

 private JobExecutionContext jobExecutionContext;

 @Before
 public void setup() {
 jobExecutionContext = mock(JobExecutionContext.class);
 ReflectionTestUtils.setField(dataArchiveRollbackJob, "jobThread", Thread.currentThread());
 }

 @Test
 public void testExecuteSuccess() {
 // Prepare mock data
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setJobName("dataArchiveRollbackJob");
 taskEntity.setJobGroup("group1");
 taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
 taskEntity.setResultJson("[]");

 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(2L);

 when(jobExecutionContext.getResult()).thenReturn(taskEntity);
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));

 List<DlmTask> taskUnits = JsonUtils.fromJson("[[{\"id\": 3,\"sourceDatabaseId\": 4,\"targetDatabaseId\": 5}]]",
 new TypeReference<List<DlmTask>>() {});

 when(JsonUtils.fromJson(anyString(), eq(new TypeReference<List<DlmTask>>() {})))
 .thenReturn(taskUnits);

 // Call the method under test
 dataArchiveRollbackJob.execute(jobExecutionContext);

 // Verify the results
 verify(scheduleTaskRepository, times(1))
 .updateStatusById(eq(taskEntity.getId()), eq(TaskStatus.DONE));
 }

 @Test
 public void testExecuteFailure() {
 // Prepare mock data
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setJobName("dataArchiveRollbackJob");
 taskEntity.setJobGroup("group1");
 taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
 taskEntity.setResultJson("[]");

 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(2L);

 when(jobExecutionContext.getResult()).thenReturn(taskEntity);
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

 // Call the method under test
 dataArchiveRollbackJob.execute(jobExecutionContext);

 // Verify the results
 verify(scheduleTaskRepository, times(1))
 .updateStatusById(eq(taskEntity.getId()), eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecuteTask() {
 // Prepare mock data
 List<DlmTask> taskUnits = JsonUtils.fromJson("[[{\"id\": 3,\"sourceDatabaseId\": 4,\"targetDatabaseId\": 5}]]",
 new TypeReference<List<DlmTask>>() {});

 // Call the method under test
 dataArchiveRollbackJob.executeTask(1L, taskUnits);

 // Verify the results
 verify(scheduleTaskRepository, times(1)).save(any(ScheduleTaskEntity.class));
 }

 @Test
 public void testGetTaskStatus() {
 // Prepare mock data
 List<DlmTask> taskUnits = JsonUtils.fromJson("[[{\"id\": 3,\"sourceDatabaseId\": 4,\"targetDatabaseId\": 5}]]",
 new TypeReference<List<DlmTask>>() {});

 when(JsonUtils.getTaskStatus(taskUnits)).thenReturn(TaskStatus.DONE);

 // Call the method under test
 TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

 // Verify the results
 assertEquals(TaskStatus.DONE, taskStatus);
 }
}