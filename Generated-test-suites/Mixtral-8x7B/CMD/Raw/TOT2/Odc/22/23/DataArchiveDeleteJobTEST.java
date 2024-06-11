import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

@Mock
private ScheduleTaskRepository scheduleTaskRepository;

@InjectMocks
private DataArchiveDeleteJob dataArchiveDeleteJob;

private JobExecutionContext jobExecutionContext;

@Before
public void setup() {
jobExecutionContext = mock(JobExecutionContext.class);
}

@Test
public void testExecute_whenDataArchiveTaskNotFound_shouldUpdateTaskStatusToFailed() {
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("123");

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

when(jobExecutionContext.getResult()).thenReturn(taskEntity);

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.FAILED);
}

@Test
public void testExecute_whenDataArchiveTaskNotFinished_shouldUpdateTaskStatusToFailed() {
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("123");

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(2L);
dataArchiveTask.setStatus(TaskStatus.RUNNING);

when(jobExecutionContext.getResult()).thenReturn(taskEntity);
when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
.thenReturn(Optional.of(dataArchiveTask));

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.FAILED);
}

@Test
public void testExecute_whenTaskUnitsPrepared_shouldExecuteTasksAndUpdateTaskStatus() {
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("123");
dataArchiveClearParameters.setTaskUnits(
"[{\"id\":\"1\",\"jobName\":\"testJob\",\"jobGroup\":\"testGroup\",\"parameters\":{},\"status\":\"PREPARING\"}]");

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(2L);
dataArchiveTask.setStatus(TaskStatus.DONE);
dataArchiveTask.setResultJson("[{\"id\":\"1\",\"jobName\":\"testJob\",\"jobGroup\":" +
"\n\"testGroup\",\"parameters\":{},\"status\":\"PREPARING\"}]");

List<DlmTask> taskUnits = JsonUtils.fromJson("[{\"id\":\"1\",\"jobName\":\"testJob\"" +
"\n,\"jobGroup\":\"testGroup\",\"parameters\":{},\"status\":\"PREPARING\"}]",
new TypeReference<List<DlmTask>>() {
});

when(jobExecutionContext.getResult()).thenReturn(taskEntity);
when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
.thenReturn(Optional.of(dataArchiveTask));
when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class)))
.thenReturn(1);

doNothing().when(dataArchiveDeleteJob).executeTask(anyLong(), anyList());

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.PREPARING);
verify(dataArchiveDeleteJob, times(1)).executeTask(1L, taskUnits);
verify(scheduleTaskRepository, times(1)).updateStatusById(1L, any(TaskStatus.class));
}

@Test
public void testExecuteTask_whenTaskUnitsNotEmpty_shouldSetIdsAndJobTypeAndExecuteTasks() {
List<DlmTask> taskUnits = JsonUtils.fromJson("[{\"id\":\"1\",\"jobName\":\"testJob\"" +
"\n,\"jobGroup\":\"testGroup\",\"parameters\":{},\"status\":\"PREPARING\"}]",
new TypeReference<List<DlmTask>>() {
});

dataArchiveDeleteJob.executeTask(1L, taskUnits);

DlmTask taskUnit = taskUnits.get(0);

assertEquals(DlmJobIdUtil.generateHistoryJobId("testJob", "testGroup", 1L, 1), taskUnit.getId());
assertEquals(JobType.DELETE, taskUnit.getJobType());
assertEquals(TaskStatus.PREPARING, taskUnit.getStatus());

verify(dataArchiveDeleteJob, times(1)).executeDlmTask(taskUnit);
}

private TaskStatus getTaskStatus(List<DlmTask> taskUnits) {
// Assume all tasks are successful
return TaskStatus.SUCCESS;
}

private void executeDlmTask(DlmTask taskUnit) {
// Mock execution of DLM task
}
}