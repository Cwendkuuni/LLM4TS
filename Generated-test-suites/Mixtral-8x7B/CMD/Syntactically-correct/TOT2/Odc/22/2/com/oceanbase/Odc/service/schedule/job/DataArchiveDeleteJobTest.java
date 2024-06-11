import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.AbstractDlmJob;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.core.task.TaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class DataArchiveDeleteJobTest {

@Mock
private ScheduleTaskRepository scheduleTaskRepository;

@Mock
private TaskExecutor taskExecutor;

@Mock
private JobExecutionContext jobExecutionContext;

private DataArchiveDeleteJob dataArchiveDeleteJob;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);
dataArchiveDeleteJob = new DataArchiveDeleteJob();
dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
dataArchiveDeleteJob.setTaskExecutor(taskExecutor);
}

@Test
public void testExecute_whenTaskEntityIsNull_thenTaskStatusIsFailed() throws JobExecutionException {
dataArchiveDeleteJob.execute(null);
}

@Test
public void testExecute_whenDataArchiveTaskNotFound_thenTaskStatusIsFailed() throws JobExecutionException {
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("123");
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.empty());

dataArchiveDeleteJob.execute(jobExecutionContext);

assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
}

@Test
public void testExecute_whenDataArchiveTaskIsNotDone_thenTaskStatusIsFailed() throws JobExecutionException {
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("123");
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId("123");
dataArchiveTask.setStatus(TaskStatus.RUNNING);

when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(dataArchiveTask));

dataArchiveDeleteJob.execute(jobExecutionContext);

assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
}

@Test
public void testExecute_whenDataArchiveTaskIsDone_thenTaskUnitsArePreparedAndTaskStatusIsUpdated() throws JobExecutionException {
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("123");
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId("123");
dataArchiveTask.setStatus(TaskStatus.DONE);

List<DlmTask> taskUnits = new ArrayList<>();
DlmTask taskUnit = new DlmTask();
taskUnit.setId("456");
taskUnits.add(taskUnit);

String resultJson = JsonUtils.toJson(taskUnits);
dataArchiveTask.setResultJson(resultJson);

when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(dataArchiveTask));

doNothing().when(taskExecutor).execute(any(Runnable.class));

dataArchiveDeleteJob.execute(jobExecutionContext);

assertEquals(TaskStatus.PREPARING, taskUnits.get(0).getStatus());
assertEquals(TaskStatus.PREPARING, taskEntity.getStatus());
}

private List<DlmTask> createTaskUnits() {
List<DlmTask> taskUnits = new ArrayList<>();
DlmTask taskUnit = new DlmTask();
taskUnit.setId("456");
taskUnits.add(taskUnit);
return taskUnits;
}

private ScheduleTaskEntity createTaskEntity() {
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("123");
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
return taskEntity;
}
}