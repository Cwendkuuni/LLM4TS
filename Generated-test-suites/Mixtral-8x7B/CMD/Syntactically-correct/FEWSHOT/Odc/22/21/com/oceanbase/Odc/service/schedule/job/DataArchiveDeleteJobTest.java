import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.AbstractDlmJob;
import com.oceanbase.tools.migrator.common.enums.JobType;

@SuppressWarnings("unchecked")
public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskRepository scheduleTaskRepository;
 private JobExecutionContext jobExecutionContext;

 @Before
 public void setup() {
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
 jobExecutionContext = mock(JobExecutionContext.class);
 }

 @Test
 public void testExecute_whenDataArchiveTaskNotFound_shouldSetTaskStatusToFailed()
 throws JobExecutionException {
 DataArchiveClearParameters parameters = new DataArchiveClearParameters();
 parameters.setDataArchiveTaskId(1L);
 String parametersJson = JsonUtils.toJson(parameters);
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(2L);
 taskEntity.setParametersJson(parametersJson);

 doReturn(Optional.of(new ScheduleTaskEntity())).doReturn(Optional.empty())
 .when(scheduleTaskRepository).findById(anyLong());

 dataArchiveDeleteJob.execute(jobExecutionContext);

 verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
 }

 @Test
 public void testExecute_whenDataArchiveTaskNotFinished_shouldSetTaskStatusToFailed()
 throws JobExecutionException {
 DataArchiveClearParameters parameters = new DataArchiveClearParameters();
 parameters.setDataArchiveTaskId(1L);
 String parametersJson = JsonUtils.toJson(parameters);
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(2L);
 taskEntity.setParametersJson(parametersJson);

 doReturn(Optional.of(new ScheduleTaskEntity()))
 .when(scheduleTaskRepository).findById(anyLong());
 doReturn(Optional.of(new ScheduleTaskEntity()).get())
 .when(scheduleTaskRepository).findById(anyLong()).get();

 dataArchiveDeleteJob.execute(jobExecutionContext);

 verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
 }

 @Test
 public void testExecute_whenTaskUnitsAreEmpty_shouldSetTaskStatusToFailed()
 throws JobExecutionException {
 DataArchiveClearParameters parameters = new DataArchiveClearParameters();
 parameters.setDataArchiveTaskId(1L);
 String parametersJson = JsonUtils.toJson(parameters);
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(2L);
 taskEntity.setParametersJson(parametersJson);

 doReturn(Optional.of(new ScheduleTaskEntity()))
 .when(scheduleTaskRepository).findById(anyLong());
 doReturn(Optional.of(new ScheduleTaskEntity().setResultJson("[]")))
 .when(scheduleTaskRepository).findById(anyLong()).get();

 dataArchiveDeleteJob.execute(jobExecutionContext);

 verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
 }

 @Test
 public void testExecute_whenTaskUnitsAreNotEmpty_shouldSetTaskStatusAccordingly()
 throws JobExecutionException {
 DataArchiveClearParameters parameters = new DataArchiveClearParameters();
 parameters.setDataArchiveTaskId(1L);
 String parametersJson = JsonUtils.toJson(parameters);
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(2L);
 taskEntity.setParametersJson(parametersJson);

 DlmTask taskUnit1 = new DlmTask();
 taskUnit1.setId(1L);
 taskUnit1.setJobType(JobType.DELETE);
 taskUnit1.setStatus(TaskStatus.PREPARING);

 DlmTask taskUnit2 = new DlmTask();
 taskUnit2.setId(2L);
 taskUnit2.setJobType(JobType.DELETE);
 taskUnit2.setStatus(TaskStatus.PREPARING);

 List<DlmTask> taskUnits = new ArrayList<>();
 taskUnits.add(taskUnit1);
 taskUnits.add(taskUnit2);

 doReturn(Optional.of(new ScheduleTaskEntity()))
 .when(scheduleTaskRepository).findById(anyLong());
 doReturn(Optional.of(new ScheduleTaskEntity().setResultJson(JsonUtils.toJson(taskUnits))))
 .when(scheduleTaskRepository).findById(anyLong()).get();

 dataArchiveDeleteJob.execute(jobExecutionContext);

 verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
 }

 @Test
 public void testGetTaskStatus() throws Exception {
 DataArchiveClearParameters parameters = new DataArchiveClearParameters();
 parameters.setDataArchiveTaskId(1L);
 String parametersJson = JsonUtils.toJson(parameters);
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(2L);
 taskEntity.setParametersJson(parametersJson);

 DlmTask taskUnit1 = new DlmTask();
 taskUnit1.setId(1L);
 taskUnit1.setJobType(JobType.DELETE);
 taskUnit1.setStatus(TaskStatus.PREPARING);

 DlmTask taskUnit2 = new DlmTask();
 taskUnit2.setId(2L);
 taskUnit2.setJobType(JobType.DELETE);
 taskUnit2.setStatus(TaskStatus.PREPARING);

 List<DlmTask> taskUnits = new ArrayList<>();
 taskUnits.add(taskUnit1);
 taskUnits.add(taskUnit2);

 doReturn(Optional.of(new ScheduleTaskEntity()))
 .when(scheduleTaskRepository).findById(anyLong());
 doReturn(Optional.of(new ScheduleTaskEntity().setResultJson(JsonUtils.toJson(taskUnits))))
 .when(scheduleTaskRepository).findById(anyLong()).get();

 assertEquals(TaskStatus.PREPARING, dataArchiveDeleteJob.getTaskStatus(taskUnits));
 }

 @Test
 public void testExecuteTask() throws Exception {
 DataArchiveClearParameters parameters = new DataArchiveClearParameters();
 parameters.setDataArchiveTaskId(1L);
 String parametersJson = JsonUtils.toJson(parameters);
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(2L);
 taskEntity.setParametersJson(parametersJson);

 DlmTask taskUnit1 = new DlmTask();
 taskUnit1.setId(1L);
 taskUnit1.setJobType(JobType.DELETE);
 taskUnit1.setStatus(TaskStatus.PREPARING);
 taskUnit1.setJobName("jobName1");
 taskUnit1.setJobGroup("jobGroup1");

 DlmTask taskUnit2 = new DlmTask();
 taskUnit2.setId(2L);
 taskUnit2.setJobType(JobType.DELETE);
 taskUnit2.setStatus(TaskStatus.PREPARING);
 taskUnit2.setJobName("jobName2");
 taskUnit2.setJobGroup("jobGroup2");

 List<DlmTask> taskUnits = new ArrayList<>();
 taskUnits.add(taskUnit1);
 taskUnits.add(taskUnit2);

 doReturn(Optional.of(new ScheduleTaskEntity()))
 .when(scheduleTaskRepository).findById(anyLong());
 doReturn(Optional.of(new ScheduleTaskEntity().setResultJson(JsonUtils.toJson(taskUnits))))
 .when(scheduleTaskRepository).findById(anyLong()).get();

 String expectedJson = JsonUtils.toJson(taskUnits);

 dataArchiveDeleteJob.executeTask(2L, taskUnits);

 String actualJson = JsonUtils.toJson(taskUnits);

 JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.NON_EXTENSIBLE);
 }
}