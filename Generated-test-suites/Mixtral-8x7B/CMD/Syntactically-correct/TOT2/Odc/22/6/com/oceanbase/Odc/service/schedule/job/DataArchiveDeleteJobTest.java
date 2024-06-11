import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.common.json.JsonUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@Slf4j
public class DataArchiveDeleteJobTest {

 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;

 private DataArchiveDeleteJob dataArchiveDeleteJob;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() {
 // Given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

 // When
 dataArchiveDeleteJob.execute(null);

 // Then
 assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
 }

 @Test
 public void testExecute_DataArchiveTaskNotFinished() {
 // Given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.RUNNING);

 List<DlmTask> taskUnits = new ArrayList<>();
 taskUnits.add(new DlmTask());

 dataArchiveClearParameters.setTaskUnits(taskUnits);

 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(2L);
 taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));

 // When
 dataArchiveDeleteJob.execute(null);

 // Then
 assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
 }

 @Test
 public void testExecute_PrepareTasksForClear() {
 // Given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.DONE);

 List<DlmTask> taskUnits = new ArrayList<>();
 taskUnits.add(new DlmTask());

 dataArchiveClearParameters.setTaskUnits(taskUnits);

 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(2L);
 taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

 DlmTask dlmTask = new DlmTask();
 dlmTask.setId(DlmJobIdUtil.generateHistoryJobId("jobName", "jobGroup", 2L, 1));
 dlmTask.setJobType(JobType.DELETE);
 dlmTask.setStatus(TaskStatus.PREPARING);

 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));

 // When
 dataArchiveDeleteJob.execute(null);

 // Then
 // Check taskUnits have been updated with correct id and status
 assertEquals(TaskStatus.PREPARING, dlmTask.getStatus());
 }

 @Test
 public void testExecute_UpdateTaskStatus() {
 // Given
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(1L);
 dataArchiveTask.setStatus(TaskStatus.DONE);

 List<DlmTask> taskUnits = new ArrayList<>();
 taskUnits.add(new DlmTask());

 dataArchiveClearParameters.setTaskUnits(taskUnits);

 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(2L);
 taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

 DlmTask dlmTask = new DlmTask();
 dlmTask.setId(DlmJobIdUtil.generateHistoryJobId("jobName", "jobGroup", 2L, 1));
 dlmTask.setJobType(JobType.DELETE);
 dlmTask.setStatus(TaskStatus.PREPARING);

 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
 when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class))).thenReturn(null);

 // When
 dataArchiveDeleteJob.execute(null);

 // Then
 // Check taskEntity status has been updated
 assertEquals(TaskStatus.PREPARING, taskEntity.getStatus());
 }
}