import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

private DataArchiveDeleteJob dataArchiveDeleteJob;
private JobExecutionContext jobExecutionContext;
private ScheduleTaskRepository scheduleTaskRepository;

@Before
public void setup() {
dataArchiveDeleteJob = new DataArchiveDeleteJob();
scheduleTaskRepository = mock(ScheduleTaskRepository.class);
dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);

jobExecutionContext = mock(JobExecutionContext.class);
when(jobExecutionContext.getResult()).thenReturn(mock(ScheduleTaskEntity.class));
}

@Test
public void testExecute_whenDataArchiveTaskNotFound_shouldUpdateTaskStatusToFailed() throws JobExecutionException {
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId(1L);

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
.thenReturn(Optional.empty());

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository).updateStatusById(eq(taskEntity.getId()), eq(TaskStatus.FAILED));
}

@Test
public void testExecute_whenDataArchiveTaskNotDone_shouldUpdateTaskStatusToFailed() throws JobExecutionException {
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId(1L);

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
taskEntity.setStatus(TaskStatus.RUNNING);

ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(2L);
dataArchiveTask.setStatus(TaskStatus.DONE);

when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
.thenReturn(Optional.of(dataArchiveTask));

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository).updateStatusById(eq(taskEntity.getId()), eq(TaskStatus.FAILED));
}

@Test
public void testExecute_whenPrepareTasksForClear_shouldSetCorrectJobIdAndJobType() throws JobExecutionException {
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId(1L);

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(2L);
dataArchiveTask.setStatus(TaskStatus.DONE);

List<DlmTask> taskUnits = new ArrayList<>();
DlmTask taskUnit1 = new DlmTask();
DlmTask taskUnit2 = new DlmTask();
taskUnits.add(taskUnit1);
taskUnits.add(taskUnit2);

when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
.thenReturn(Optional.of(dataArchiveTask));

dataArchiveDeleteJob.execute(jobExecutionContext);

for (int i = 0; i < taskUnits.size(); i++) {
DlmTask taskUnit = taskUnits.get(i);
String expectedJobId = DlmJobIdUtil.generateHistoryJobId(
dataArchiveDeleteJob.getJobName(),
dataArchiveDeleteJob.getJobGroup(),
taskEntity.getId(),
i + 1
);

verify(taskUnit, times(1)).setId(expectedJobId);
verify(taskUnit, times(1)).setJobType(JobType.DELETE);
verify(taskUnit, times(1)).setStatus(TaskStatus.PREPARING);
}
}

@Test
public void testExecute_whenPrepareTasksForClear_shouldExecuteTask() throws JobExecutionException {
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId(1L);

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(2L);
dataArchiveTask.setStatus(TaskStatus.DONE);

List<DlmTask> taskUnits = new ArrayList<>();
DlmTask taskUnit1 = new DlmTask();
DlmTask taskUnit2 = new DlmTask();
taskUnits.add(taskUnit1);
taskUnits.add(taskUnit2);

when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
.thenReturn(Optional.of(dataArchiveTask));

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(dataArchiveDeleteJob, times(1)).executeTask(eq(taskEntity.getId()), anyList());
}

@Test
public void testExecute_whenPrepareTasksForClear_shouldUpdateTaskStatus() throws JobExecutionException {
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId(1L);

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(2L);
dataArchiveTask.setStatus(TaskStatus.DONE);

List<DlmTask> taskUnits = new ArrayList<>();
DlmTask taskUnit1 = new DlmTask();
DlmTask taskUnit2 = new DlmTask();
taskUnits.add(taskUnit1);
taskUnits.add(taskUnit2);

TaskStatus expectedTaskStatus = TaskStatus.PREPARING;

when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
.thenReturn(Optional.of(dataArchiveTask));

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1))
.updateStatusById(eq(taskEntity.getId()), eq(expectedTaskStatus));
}

private void assertTaskUnits(String json, List<DlmTask> expectedTaskUnits) throws Exception {
List<DlmTask> taskUnits = JsonUtils.fromJson(json, new TypeReference<List<DlmTask>>() {});
JSONAssert.assertEquals(
JsonUtils.toJson(expectedTaskUnits), JsonUtils.toJson(taskUnits), JSONCompareMode.LENIENT);
}
}