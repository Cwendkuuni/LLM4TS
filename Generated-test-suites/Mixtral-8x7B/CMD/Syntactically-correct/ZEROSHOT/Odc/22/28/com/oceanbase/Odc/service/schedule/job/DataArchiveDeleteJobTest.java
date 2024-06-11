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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DataArchiveDeleteJobTest {

@Mock
private ScheduleTaskRepository scheduleTaskRepository;

private DataArchiveDeleteJob dataArchiveDeleteJob;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);

dataArchiveDeleteJob = spy(new DataArchiveDeleteJob() {
@Override
public ScheduleTaskRepository getScheduleTaskRepository() {
return scheduleTaskRepository;
}
});
}

@Test
public void testExecute_dataArchiveTaskNotFound() {
// Given
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("task-1");

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

// When
dataArchiveDeleteJob.execute(null);

// Then
verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), TaskStatus.FAILED);
}

@Test
public void testExecute_dataArchiveTaskNotDone() {
// Given
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("task-1");

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(1L);
dataArchiveTask.setStatus(TaskStatus.RUNNING);

doReturn(Optional.of(dataArchiveTask)).when(scheduleTaskRepository).findById(anyLong());

// When
dataArchiveDeleteJob.execute(null);

// Then
verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), TaskStatus.FAILED);
}

@Test
public void testExecute_clearTasksSuccess() {
// Given
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("task-1");

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(1L);
dataArchiveTask.setStatus(TaskStatus.DONE);

List<DlmTask> taskUnits = createDlmTasks(2);

doReturn(Optional.of(dataArchiveTask)).when(scheduleTaskRepository).findById(anyLong());

doReturn(taskUnits).when(dataArchiveDeleteJob).findDataArchiveTasks(anyLong());

// When
dataArchiveDeleteJob.execute(null);

// Then
verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), TaskStatus.SUCCESS);
}

@Test
public void testExecute_clearTasksFailed() {
// Given
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("task-1");

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(1L);
dataArchiveTask.setStatus(TaskStatus.DONE);

List<DlmTask> taskUnits = createDlmTasks(2);

doReturn(Optional.of(dataArchiveTask)).when(scheduleTaskRepository).findById(anyLong());

doReturn(taskUnits).when(dataArchiveDeleteJob).findDataArchiveTasks(anyLong());

doReturn(TaskStatus.FAILED).when(dataArchiveDeleteJob).getTaskStatus(any());

// When
dataArchiveDeleteJob.execute(null);

// Then
verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), TaskStatus.FAILED);
}

private List<DlmTask> createDlmTasks(int count) {
List<DlmTask> taskUnits = mock(new TypeReference<List<DlmTask>>() {});

for (int i = 0; i < count; i++) {
DlmTask taskUnit = new DlmTask();
taskUnit.setId("task-" + (i + 1));
taskUnit.setJobType(JobType.DELETE);
taskUnit.setStatus(TaskStatus.PREPARING);

taskUnits.add(taskUnit);
}

return taskUnits;
}
}