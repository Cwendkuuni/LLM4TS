import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.utils.AbstractDlmJob;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

@SuppressWarnings("unchecked")
public class DataArchiveDeleteJobTest {

@Mock
private ScheduleTaskRepository scheduleTaskRepository;

@Mock
private JobExecutionContext jobExecutionContext;

@Mock
private JsonUtils jsonUtils;

@InjectMocks
private DataArchiveDeleteJob dataArchiveDeleteJob;

private ScheduleTaskEntity taskEntity;

private DataArchiveClearParameters dataArchiveClearParameters;

private List<DlmTask> taskUnits;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);

taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
taskEntity.setResultJson("[]");
taskEntity.setStatus(TaskStatus.DONE);

dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId(2L);

taskUnits = new ArrayList<>();
taskUnits.add(new DlmTask());
}

@Test
public void testExecute_whenDataArchiveTaskNotFound_thenUpdateTaskStatusToFailedAndReturn() {
dataArchiveDeleteJob = spy(dataArchiveDeleteJob);

doReturn(Optional.of(taskEntity)).when(scheduleTaskRepository).findById(anyLong());
doReturn(Optional.empty()).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
}

@Test
public void testExecute_whenDataArchiveTaskNotFinished_thenUpdateTaskStatusToFailedAndReturn() {
dataArchiveDeleteJob = spy(dataArchiveDeleteJob);

doReturn(Optional.of(taskEntity)).when(scheduleTaskRepository).findById(anyLong());
doReturn(Optional.of(taskEntity)).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());
doReturn(TaskStatus.RUNNING).when(scheduleTaskRepository).getTaskStatus(taskEntity);

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
}

@Test
public void testExecute_whenDataArchiveTaskFinished_thenPrepareTasksForClearAndExecute() throws Exception {
dataArchiveDeleteJob = spy(dataArchiveDeleteJob);

DataArchiveClearParameters dataArchiveClearParametersSpy = spy(dataArchiveClearParameters);
doReturn(taskUnits).when(jsonUtils).fromJson(anyString(), any(TypeReference.class));

doReturn(Optional.of(taskEntity)).when(scheduleTaskRepository).findById(anyLong());
doReturn(Optional.of(taskEntity)).when(scheduleTaskRepository).findById(dataArchiveClearParametersSpy.getDataArchiveTaskId());
doReturn(TaskStatus.DONE).when(scheduleTaskRepository).getTaskStatus(taskEntity);

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(dataArchiveClearParametersSpy, times(1)).getDataArchiveTaskId();
verify(jsonUtils, times(1)).fromJson(taskEntity.getParametersJson(), DataArchiveClearParameters.class);
verify(jsonUtils, times(1)).fromJson(taskEntity.getResultJson(), new TypeReference<List<DlmTask>>() {});
verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.PREPARING);
verify(scheduleTaskRepository, times(1)).createTasks(taskUnits);
}

@Test
public void testGetTaskStatus_whenAllTasksAreDone_thenReturnDone() {
dataArchiveDeleteJob = spy(dataArchiveDeleteJob);

DlmTask dlmTask1 = new DlmTask();
dlmTask1.setStatus(TaskStatus.DONE);

DlmTask dlmTask2 = new DlmTask();
dlmTask2.setStatus(TaskStatus.DONE);

taskUnits.add(dlmTask1);
taskUnits.add(dlmTask2);

TaskStatus taskStatus = dataArchiveDeleteJob.getTaskStatus(taskUnits);

assertEquals(TaskStatus.DONE, taskStatus);
}

@Test
public void testGetTaskStatus_whenAnyTaskIsNotDone_thenReturnPreparing() {
dataArchiveDeleteJob = spy(dataArchiveDeleteJob);

DlmTask dlmTask1 = new DlmTask();
dlmTask1.setStatus(TaskStatus.DONE);

DlmTask dlmTask2 = new DlmTask();
dlmTask2.setStatus(TaskStatus.FAILED);

taskUnits.add(dlmTask1);
taskUnits.add(dlmTask2);

TaskStatus taskStatus = dataArchiveDeleteJob.getTaskStatus(taskUnits);

assertEquals(TaskStatus.PREPARING, taskStatus);
}

@Test
public void testExecuteTask_whenCalled_thenDoNothing() {
dataArchiveDeleteJob = spy(dataArchiveDeleteJob);

doNothing().when(dataArchiveDeleteJob).executeTask(anyLong(), any());

dataArchiveDeleteJob.executeTask(1L, taskUnits);

verify(dataArchiveDeleteJob, times(1)).executeTask(1L, taskUnits);
}
}