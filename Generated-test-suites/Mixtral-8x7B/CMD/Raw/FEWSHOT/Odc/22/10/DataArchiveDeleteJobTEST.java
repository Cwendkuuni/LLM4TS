import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.MDC;

@Slf4j
public class DataArchiveDeleteJobTest {

private static final String TASK_ID = "123";
private static final String PARAMETERS_JSON = "{\"dataArchiveTaskId\": \"456\"}";
private static final String RESULT_JSON = "[{\"id\": \"789\", \"jobType\": \"DELETE\"}]";
private static final String TASK_STATUS = TaskStatus.PREPARING.toString();
private static final String MOCK_JOB_NAME = "mockJobName";
private static final String MOCK_JOB_GROUP = "mockJobGroup";
private static final String MOCK_TASK_ID = "mockTaskId";
private static final String MOCK_TASK_NAME = "mockTaskName";
private static final String MOCK_TASK_GROUP = "mockTaskGroup";

@Test
public void testExecute_whenTaskEntityIsNull_thenUpdateStatusByIdWithFailed() {
JobExecutionContext context = mock(JobExecutionContext.class);
when(context.getResult()).thenReturn(null);

DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
dataArchiveDeleteJob.execute(context);

assertEquals(TaskStatus.FAILED, scheduleTaskRepository.getStatusById(TASK_ID));
}

@Test
public void testExecute_whenDataArchiveTaskNotFound_thenUpdateStatusByIdWithFailed() {
JobExecutionContext context = mock(JobExecutionContext.class);
when(context.getResult()).thenReturn(new ScheduleTaskEntity());
scheduleTaskRepository = mock(ScheduleTaskRepository.class);
when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.empty());

DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
dataArchiveDeleteJob.scheduleTaskRepository = scheduleTaskRepository;
dataArchiveDeleteJob.execute(context);

verify(scheduleTaskRepository).updateStatusById(TASK_ID, TaskStatus.FAILED);
}

@Test
public void testExecute_whenDataArchiveTaskNotDone_thenUpdateStatusByIdWithFailed() {
JobExecutionContext context = mock(JobExecutionContext.class);
when(context.getResult()).thenReturn(new ScheduleTaskEntity());
scheduleTaskRepository = mock(ScheduleTaskRepository.class);
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId(TASK_ID);
ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(TASK_ID);
dataArchiveTask.setStatus(TaskStatus.RUNNING);
dataArchiveTask.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(dataArchiveTask));

DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
dataArchiveDeleteJob.scheduleTaskRepository = scheduleTaskRepository;
dataArchiveDeleteJob.execute(context);

verify(scheduleTaskRepository).updateStatusById(TASK_ID, TaskStatus.FAILED);
}

@Test
public void testExecute_whenPrepareTasksSuccessfully_thenUpdateStatusById() {
JobExecutionContext context = mock(JobExecutionContext.class);
when(context.getResult()).thenReturn(new ScheduleTaskEntity());
scheduleTaskRepository = mock(ScheduleTaskRepository.class);
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId(TASK_ID);
ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(TASK_ID);
dataArchiveTask.setStatus(TaskStatus.DONE);
dataArchiveTask.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(dataArchiveTask));
List<DlmTask> taskUnits = JsonUtils.fromJson(RESULT_JSON, new TypeReference<List<DlmTask>>() {});
when(scheduleTaskRepository.updateStatusById(anyString(), any(TaskStatus.class)))
.thenReturn(true);
DlmJobIdUtil dlmJobIdUtil = mock(DlmJobIdUtil.class);
when(dlmJobIdUtil.generateHistoryJobId(anyString(), anyString(), anyString(), anyInt()))
.thenReturn(MOCK_TASK_ID);
dataArchiveDeleteJob.dlmJobIdUtil = dlmJobIdUtil;

DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
dataArchiveDeleteJob.scheduleTaskRepository = scheduleTaskRepository;
dataArchiveDeleteJob.execute(context);

verify(scheduleTaskRepository).updateStatusById(TASK_ID, TaskStatus.PREPARING);
}

@Test
public void testExecute_whenPrepareTasksFailed_thenNotUpdateStatusById() {
JobExecutionContext context = mock(JobExecutionContext.class);
when(context.getResult()).thenReturn(new ScheduleTaskEntity());
scheduleTaskRepository = mock(ScheduleTaskRepository.class);
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId(TASK_ID);
ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(TASK_ID);
dataArchiveTask.setStatus(TaskStatus.DONE);
dataArchiveTask.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(dataArchiveTask));
List<DlmTask> taskUnits = JsonUtils.fromJson(RESULT_JSON, new TypeReference<List<DlmTask>>() {});
when(scheduleTaskRepository.updateStatusById(anyString(), any(TaskStatus.class)))
.thenReturn(false);

DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
dataArchiveDeleteJob.scheduleTaskRepository = scheduleTaskRepository;
dataArchiveDeleteJob.execute(context);

verify(scheduleTaskRepository, times(0)).updateStatusById(TASK_ID, TaskStatus.PREPARING);
}

@Test
public void testGetTaskStatus_whenTaskUnitsArePreparing_thenReturnPreparing() {
DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
List<DlmTask> taskUnits =
JsonUtils.fromJson("[{\"id\": \"123\", \"jobType\": \"DELETE\", \"status\": \"PREPARING\"}]",
new TypeReference<List<DlmTask>>() {});

assertEquals(TaskStatus.PREPARING, dataArchiveDeleteJob.getTaskStatus(taskUnits));
}

@Test
public void testGetTaskStatus_whenTaskUnitsAreDone_thenReturnDone() {
DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
List<DlmTask> taskUnits =
JsonUtils.fromJson("[{\"id\": \"123\", \"jobType\": \"DELETE\", \"status\": \"DONE\"}]",
new TypeReference<List<DlmTask>>() {});

assertEquals(TaskStatus.DONE, dataArchiveDeleteJob.getTaskStatus(taskUnits));
}

@Test
public void testGetTaskStatus_whenTaskUnitsHaveDifferentStatus_thenReturnPreparing() {
DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
List<DlmTask> taskUnits =
JsonUtils.fromJson(
"[{\"id\": \"123\", \"jobType\": \"DELETE\", \"status\": \"PREPARING\"}," +
"{\"id\": \"456\", \"jobType\": \"DELETE\", \"status\": \"DONE\"}]",
new TypeReference<List<DlmTask>>() {});

assertEquals(TaskStatus.PREPARING, dataArchiveDeleteJob.getTaskStatus(taskUnits));
}

@Test
public void testGetTaskStatus_whenTaskUnitsAreEmpty_thenReturnFailed() {
DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
List<DlmTask> taskUnits = JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {});

assertEquals(TaskStatus.FAILED, dataArchiveDeleteJob.getTaskStatus(taskUnits));
}
}