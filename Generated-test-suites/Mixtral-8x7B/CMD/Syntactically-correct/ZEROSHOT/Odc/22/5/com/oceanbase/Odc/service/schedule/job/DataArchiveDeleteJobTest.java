import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;

 @Mock
 private AbstractDlmJob abstractDlmJob;

 @InjectMocks
 private DataArchiveDeleteJob dataArchiveDeleteJob;

 @Before
 public void init() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testExecuteSuccess() {
 // Prepare test data
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setStatus(TaskStatus.DONE);
 String parametersJson = "{\"dataArchiveTaskId\": 2}";
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(2L);
 dataArchiveClearParameters.setParameters("test parameters");
 taskEntity.setParametersJson(parametersJson);

 List<DlmTask> taskUnits = JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {});

 when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(new ScheduleTaskEntity()));
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenReturn(true);
 when(abstractDlmJob.executeTask(anyLong(), any())).thenReturn(true);

 // Execute test method
 dataArchiveDeleteJob.execute(taskEntity);

 // Verify results
 verify(scheduleTaskRepository, times(1)).findById(2L);
 verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.FAILED);
 verify(abstractDlmJob, times(1)).executeTask(1L, taskUnits);
 }

 @Test
 public void testExecuteFailed() {
 // Prepare test data
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setStatus(TaskStatus.DONE);
 String parametersJson = "{\"dataArchiveTaskId\": 2}";
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(2L);
 dataArchiveClearParameters.setParameters("test parameters");
 taskEntity.setParametersJson(parametersJson);

 List<DlmTask> taskUnits = JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {});

 when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(new ScheduleTaskEntity()));
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenReturn(true);
 when(abstractDlmJob.executeTask(anyLong(), any())).thenReturn(false);

 // Execute test method
 dataArchiveDeleteJob.execute(taskEntity);

 // Verify results
 verify(scheduleTaskRepository, times(1)).findById(2L);
 verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.FAILED);
 verify(abstractDlmJob, times(1)).executeTask(1L, taskUnits);
 }

 @Test
 public void testExecuteDataArchiveTaskNotFound() {
 // Prepare test data
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setStatus(TaskStatus.DONE);
 String parametersJson = "{\"dataArchiveTaskId\": 2}";
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(2L);
 dataArchiveClearParameters.setParameters("test parameters");
 taskEntity.setParametersJson(parametersJson);

 List<DlmTask> taskUnits = JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {});

 when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.ofNullable(null));
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenReturn(true);

 // Execute test method
 dataArchiveDeleteJob.execute(taskEntity);

 // Verify results
 verify(scheduleTaskRepository, times(1)).findById(2L);
 verify(scheduleTaskRepository, never()).updateStatusById(1L, TaskStatus.FAILED);
 verify(abstractDlmJob, never()).executeTask(anyLong(), any());
 }

 @Test
 public void testExecuteDataArchiveTaskNotDone() {
 // Prepare test data
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setStatus(TaskStatus.RUNNING);
 String parametersJson = "{\"dataArchiveTaskId\": 2}";
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(2L);
 dataArchiveClearParameters.setParameters("test parameters");
 taskEntity.setParametersJson(parametersJson);

 List<DlmTask> taskUnits = JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {});

 when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(new ScheduleTaskEntity()));
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenReturn(true);

 // Execute test method
 dataArchiveDeleteJob.execute(taskEntity);

 // Verify results
 verify(scheduleTaskRepository, times(1)).findById(2L);
 verify(scheduleTaskRepository, never()).updateStatusById(1L, TaskStatus.FAILED);
 verify(abstractDlmJob, never()).executeTask(anyLong(), any());
 }

 @Test
 public void testExecuteUpdateStatusByIdFailed() {
 // Prepare test data
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setStatus(TaskStatus.DONE);
 String parametersJson = "{\"dataArchiveTaskId\": 2}";
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(2L);
 dataArchiveClearParameters.setParameters("test parameters");
 taskEntity.setParametersJson(parametersJson);

 List<DlmTask> taskUnits = JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {});

 when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(new ScheduleTaskEntity()));
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenReturn(false);

 // Execute test method
 dataArchiveDeleteJob.execute(taskEntity);

 // Verify results
 verify(scheduleTaskRepository, times(1)).findById(2L);
 verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.FAILED);
 verify(abstractDlmJob, never()).executeTask(anyLong(), any());
 }

 @Test
 public void testExecuteTaskReturnFalse() {
 // Prepare test data
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setStatus(TaskStatus.DONE);
 String parametersJson = "{\"dataArchiveTaskId\": 2}";
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(2L);
 dataArchiveClearParameters.setParameters("test parameters");
 taskEntity.setParametersJson(parametersJson);

 List<DlmTask> taskUnits = JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {});

 when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(new ScheduleTaskEntity()));
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenReturn(true);
 when(abstractDlmJob.executeTask(anyLong(), any())).thenReturn(false);

 // Execute test method
 dataArchiveDeleteJob.execute(taskEntity);

 // Verify results
 verify(scheduleTaskRepository, times(1)).findById(2L);
 verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.FAILED);
 verify(abstractDlmJob, times(1)).executeTask(1L, taskUnits);
 }

 @Test
 public void testExecuteTaskReturnTrue() {
 // Prepare test data
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setStatus(TaskStatus.DONE);
 String parametersJson = "{\"dataArchiveTaskId\": 2}";
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(2L);
 dataArchiveClearParameters.setParameters("test parameters");
 taskEntity.setParametersJson(parametersJson);

 List<DlmTask> taskUnits = JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {});

 when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(new ScheduleTaskEntity()));
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenReturn(true);
 when(abstractDlmJob.executeTask(anyLong(), any())).thenReturn(true);

 // Execute test method
 dataArchiveDeleteJob.execute(taskEntity);

 // Verify results
 verify(scheduleTaskRepository, times(1)).findById(2L);
 verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.FAILED);
 verify(abstractDlmJob, times(1)).executeTask(1L, taskUnits);
 }

 @Test
 public void testDlmJobIdUtilGenerateHistoryJobId() {
 // Prepare test data
 String jobName = "testJobName";
 String jobGroup = "testJobGroup";
 Long taskId = 1L;
 Integer taskUnitsSize = 10;

 // Execute test method
 String result = DlmJobIdUtil.generateHistoryJobId(jobName, jobGroup, taskId, taskUnitsSize);

 // Verify results
 assertEquals(jobName + "_" + taskId + "_" + 1, result);
 }

 @Test
 public void testGetTaskStatusSuccess() {
 // Prepare test data
 List<DlmTask> taskUnits = JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {});

 // Execute test method
 TaskStatus result = dataArchiveDeleteJob.getTaskStatus(taskUnits);

 // Verify results
 assertEquals(TaskStatus.DONE, result);
 }

 @Test
 public void testGetTaskStatusFailed() {
 // Prepare test data
 List<DlmTask> taskUnits = JsonUtils.fromJson("[{\"status\": \"FAILED\"}]",
 new TypeReference<List<DlmTask>>() {});

 // Execute test method
 TaskStatus result = dataArchiveDeleteJob.getTaskStatus(taskUnits);

 // Verify results
 assertEquals(TaskStatus.FAILED, result);
 }

 @Test
 public void testGetTaskStatusRunning() {
 // Prepare test data
 List<DlmTask> taskUnits = JsonUtils.fromJson("[{\"status\": \"RUNNING\"}]",
 new TypeReference<List<DlmTask>>() {});

 // Execute test method
 TaskStatus result = dataArchiveDeleteJob.getTaskStatus(taskUnits);

 // Verify results
 assertEquals(TaskStatus.RUNNING, result);
 }

 @Test
 public void testGetTaskStatusPreparing() {
 // Prepare test data
 List<DlmTask> taskUnits = JsonUtils.fromJson("[{\"status\": \"PREPARING\"}]",
 new TypeReference<List<DlmTask>>() {});

 // Execute test method
 TaskStatus result = dataArchiveDeleteJob.getTaskStatus(taskUnits);

 // Verify results
 assertEquals(TaskStatus.PREPARING, result);
 }
}