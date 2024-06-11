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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveDeleteJobTest {

@Mock
ScheduleTaskRepository scheduleTaskRepository;

DataArchiveDeleteJob dataArchiveDeleteJob;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);
dataArchiveDeleteJob = new DataArchiveDeleteJob();
dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
}

@Test
public void testExecuteTypicalUseCase() {
JobExecutionContext context = mock(JobExecutionContext.class);
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
String parametersJson = "{\"dataArchiveTaskId\":\"123\"}";
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("123");
taskEntity.setParametersJson(parametersJson);
ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId("456");
dataArchiveTask.setStatus(TaskStatus.DONE);
List<DlmTask> taskUnits = new ArrayList<>();
DlmTask dlmTask = new DlmTask();
dlmTask.setId("789");
taskUnits.add(dlmTask);
String resultJson = JsonUtils.toJson(taskUnits);
dataArchiveTask.setResultJson(resultJson);
when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(dataArchiveTask));
when(context.getResult()).thenReturn(taskEntity);
dataArchiveDeleteJob.execute(context);
assertEquals(TaskStatus.PREPARING, taskUnits.get(0).getStatus());
}

@Test
public void testExecuteDataArchiveTaskNotFound() {
JobExecutionContext context = mock(JobExecutionContext.class);
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
String parametersJson = "{\"dataArchiveTaskId\":\"123\"}";
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("123");
taskEntity.setParametersJson(parametersJson);
when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.empty());
when(context.getResult()).thenReturn(taskEntity);
dataArchiveDeleteJob.execute(context);
assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
}

@Test
public void testExecuteDataArchiveTaskNotDone() {
JobExecutionContext context = mock(JobExecutionContext.class);
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
String parametersJson = "{\"dataArchiveTaskId\":\"123\"}";
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("123");
taskEntity.setParametersJson(parametersJson);
ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId("456");
dataArchiveTask.setStatus(TaskStatus.RUNNING);
List<DlmTask> taskUnits = new ArrayList<>();
DlmTask dlmTask = new DlmTask();
dlmTask.setId("789");
taskUnits.add(dlmTask);
String resultJson = JsonUtils.toJson(taskUnits);
dataArchiveTask.setResultJson(resultJson);
when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(dataArchiveTask));
when(context.getResult()).thenReturn(taskEntity);
dataArchiveDeleteJob.execute(context);
assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
}

@Test
public void testExecuteTaskUnitsEmpty() {
JobExecutionContext context = mock(JobExecutionContext.class);
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
String parametersJson = "{\"dataArchiveTaskId\":\"123\"}";
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("123");
taskEntity.setParametersJson(parametersJson);
ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId("456");
dataArchiveTask.setStatus(TaskStatus.DONE);
List<DlmTask> taskUnits = new ArrayList<>();
String resultJson = JsonUtils.toJson(taskUnits);
dataArchiveTask.setResultJson(resultJson);
when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(dataArchiveTask));
when(context.getResult()).thenReturn(taskEntity);
dataArchiveDeleteJob.execute(context);
assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
}
}