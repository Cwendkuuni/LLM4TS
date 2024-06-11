import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class DataArchiveDeleteJobTest {

@MockBean
private ScheduleTaskRepository scheduleTaskRepository;

@Autowired
private DataArchiveDeleteJob dataArchiveDeleteJob;

private JobExecutionContext jobExecutionContext;

@Before
public void setup() {
jobExecutionContext = mock(JobExecutionContext.class);
}

@Test
public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("invalid_task_id");

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
.thenReturn(Optional.empty());

dataArchiveDeleteJob.execute(jobExecutionContext);

assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
}

@Test
public void testExecute_DataArchiveTaskNotDone() throws JobExecutionException {
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("valid_task_id");

ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(2L);
dataArchiveTask.setStatus(TaskStatus.RUNNING);
dataArchiveTask.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
.thenReturn(Optional.of(dataArchiveTask));

dataArchiveDeleteJob.execute(jobExecutionContext);

assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
}

@Test
public void testExecute_ClearTasksSuccess() throws JobExecutionException {
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("valid_task_id");

ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(2L);
dataArchiveTask.setStatus(TaskStatus.DONE);
dataArchiveTask.setResultJson(JsonUtils.toJson(getDlmTasks()));
dataArchiveTask.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
.thenReturn(Optional.of(dataArchiveTask));

when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class)))
.thenReturn(true);

dataArchiveDeleteJob.execute(jobExecutionContext);

assertEquals(TaskStatus.DONE, taskEntity.getStatus());
}

private List<DlmTask> getDlmTasks() {
List<DlmTask> dlmTasks = JsonUtils.fromJson("[\n" +
" {\n" +
" \"id\": 1,\n" +
" \"jobId\": 1,\n" +
" \"jobName\": \"jobName\",\n" +
" \"jobGroup\": \"jobGroup\",\n" +
" \"tenant\": 1,\n" +
" \"database\": 1,\n" +
" \"table\": 1,\n" +
" \"status\": \"PREPARING\",\n" +
" \"jobType\": \"DELETE\",\n" +
" \"taskType\": \"FULL\",\n" +
" \"taskId\": \"taskId\",\n" +
" \"startTime\": 0,\n" +
" \"endTime\": 0,\n" +
" \"cost\": 0\n" +
" },\n" +
" {\n" +
" \"id\": 2,\n" +
" \"jobId\": 1,\n" +
" \"jobName\": \"jobName\",\n" +
" \"jobGroup\": \"jobGroup\",\n" +
" \"tenant\": 1,\n" +
" \"database\": 1,\n" +
" \"table\": 1,\n" +
" \"status\": \"PREPARING\",\n" +
" \"jobType\": \"DELETE\",\n" +
" \"taskType\": \"FULL\",\n" +
" \"taskId\": \"taskId\",\n" +
" \"startTime\": 0,\n" +
" \"endTime\": 0,\n" +
" \"cost\": 0\n" +
" }]"
, new TypeReference<List<DlmTask>>() {});
return dlmTasks;
}
}