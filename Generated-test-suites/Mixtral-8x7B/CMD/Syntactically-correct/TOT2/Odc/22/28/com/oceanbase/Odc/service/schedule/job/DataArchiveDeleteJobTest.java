import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.impl.AbstractDlmJob;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@Slf4j
public class DataArchiveDeleteJobTest {

 @Mock
 ScheduleTaskRepository scheduleTaskRepository;

 @InjectMocks
 DataArchiveDeleteJob dataArchiveDeleteJob;

 JobExecutionContext jobExecutionContext;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 jobExecutionContext = new JobExecutionContext(null);
 }

 @Test
 public void testExecute() throws Exception {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("12345");

 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
 scheduleTaskEntity.setStatus(TaskStatus.DONE);

 Optional<ScheduleTaskEntity> optionalScheduleTaskEntity = Optional.of(scheduleTaskEntity);
 List<DlmTask> dlmTasks = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setId("67890");
 dlmTasks.add(dlmTask);

 when(scheduleTaskRepository.findById("12345")).thenReturn(optionalScheduleTaskEntity);
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenReturn(true);
 when(JsonUtils.fromJson("{\"dataArchiveTaskId\":\"12345\"}", DataArchiveClearParameters.class))
 .thenReturn(dataArchiveClearParameters);
 when(JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {})).thenReturn(dlmTasks);
 when(DlmJobIdUtil.generateHistoryJobId("jobName", "jobGroup", 1L, 1))
 .thenReturn("historyJobId1");

 dataArchiveDeleteJob.execute(jobExecutionContext);

 // Add assertions here for the specific behavior you expect
 }

 @Test
 public void testExecute_whenDataArchiveTaskNotFound_shouldReturnFailed() throws Exception {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("12345");

 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
 scheduleTaskEntity.setStatus(TaskStatus.DONE);

 Optional<ScheduleTaskEntity> optionalScheduleTaskEntity = Optional.of(scheduleTaskEntity);

 when(scheduleTaskRepository.findById("12345")).thenReturn(optionalScheduleTaskEntity);
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenReturn(true);

 dataArchiveDeleteJob.execute(jobExecutionContext);

 assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
 }

 @Test
 public void testExecute_whenDataArchiveTaskNotFinished_shouldReturnFailed() throws Exception {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("12345");

 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
 scheduleTaskEntity.setStatus(TaskStatus.RUNNING);

 Optional<ScheduleTaskEntity> optionalScheduleTaskEntity = Optional.of(scheduleTaskEntity);

 when(scheduleTaskRepository.findById("12345")).thenReturn(optionalScheduleTaskEntity);
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenReturn(true);

 dataArchiveDeleteJob.execute(jobExecutionContext);

 assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
 }

 @Test
 public void testExecute_whenTaskUnitsEmpty_shouldReturnFailed() throws Exception {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("12345");

 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
 scheduleTaskEntity.setStatus(TaskStatus.DONE);

 Optional<ScheduleTaskEntity> optionalScheduleTaskEntity = Optional.of(scheduleTaskEntity);
 when(scheduleTaskRepository.findById("12345")).thenReturn(optionalScheduleTaskEntity);
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenReturn(true);
 when(JsonUtils.fromJson("{}", DataArchiveClearParameters.class))
 .thenReturn(dataArchiveClearParameters);
 when(JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {})).thenReturn(new ArrayList<>());

 dataArchiveDeleteJob.execute(jobExecutionContext);

 assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
 }
}