import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Result;
import org.quartz.spi.JobDataMap;
import org.quartz.spi.TriggerFiredBundle;
import org.slf4j.Logger;

@Slf4j
public class DataArchiveDeleteJobTest {

private DataArchiveDeleteJob dataArchiveDeleteJob;
private JobExecutionContext jobExecutionContext;
private ScheduleTaskEntity scheduleTaskEntity;
private DataArchiveClearParameters dataArchiveClearParameters;
private Optional<ScheduleTaskEntity> dataArchiveTaskOption;
private ScheduleTaskEntity dataArchiveTask;
private List<DlmTask> taskUnits;
private TypeReference<List<DlmTask>> typeReference;
private DlmJobIdUtil dlmJobIdUtil;
private JsonUtils jsonUtils;
private scheduleTaskRepository scheduleTaskRepository;

@Before
public void setup() {
dataArchiveDeleteJob = spy(new DataArchiveDeleteJob());
jobExecutionContext = mock(JobExecutionContext.class);
scheduleTaskEntity = mock(ScheduleTaskEntity.class);
dataArchiveClearParameters = mock(DataArchiveClearParameters.class);
dataArchiveTaskOption = Optional.of(scheduleTaskEntity);
dataArchiveTask = mock(ScheduleTaskEntity.class);
taskUnits = Arrays.asList(mock(DlmTask.class), mock(DlmTask.class));
typeReference = new TypeReference<List<DlmTask>>() {};
dlmJobIdUtil = mock(DlmJobIdUtil.class);
jsonUtils = mock(JsonUtils.class);
scheduleTaskRepository = mock(scheduleTaskRepository.class);

dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
dataArchiveDeleteJob.setJsonUtils(jsonUtils);
dataArchiveDeleteJob.setDlmJobIdUtil(dlmJobIdUtil);

doReturn(taskUnits).when(jsonUtils).fromJson(anyString(), any(TypeReference.class));
doReturn(dataArchiveTaskOption).when(scheduleTaskRepository).findById(anyString());
doReturn(dataArchiveTask).when(dataArchiveTaskOption).get();
doReturn(TaskStatus.DONE).when(dataArchiveTask).getStatus();
doReturn(JsonUtils.toJson(taskUnits)).when(dataArchiveTask).getResultJson();
}

@Test
public void testExecuteWithNullTaskEntity() throws JobExecutionException {
dataArchiveDeleteJob.execute(null);
verify(scheduleTaskRepository, times(0)).updateStatusById(anyString(), any(TaskStatus.class));
}

@Test
public void testExecuteWithNullParametersJson() throws JobExecutionException {
when(scheduleTaskEntity.getParametersJson()).thenReturn(null);

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
}

@Test
public void testExecuteWithNullDataArchiveTaskId() throws JobExecutionException {
when(scheduleTaskEntity.getParametersJson()).thenReturn("{}");

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
}

@Test
public void testExecuteWithNotFoundDataArchiveTask() throws JobExecutionException {
when(scheduleTaskEntity.getParametersJson()).thenReturn("{\"dataArchiveTaskId\":\"invalid_id\"}");

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
}

@Test
public void testExecuteWithNotDoneDataArchiveTask() throws JobExecutionException {
when(scheduleTaskEntity.getParametersJson())
.thenReturn("{\"dataArchiveTaskId\":\"valid_id\", \"status\":\"PROCESSING\"}");

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
}

@Test
public void testExecuteWithSuccess() throws JobExecutionException {
when(scheduleTaskEntity.getParametersJson())
.thenReturn("{\"dataArchiveTaskId\":\"valid_id\", \"status\":\"DONE\"}");

JobDataMap jobDataMap = mock(JobDataMap.class);
TriggerFiredBundle bundle = mock(TriggerFiredBundle.class);
when(bundle.getJobDetail().getJobDataMap()).thenReturn(jobDataMap);
when(jobDataMap.getString(DataArchiveDeleteJob.JOB_NAME)).thenReturn("testJob");
when(jobDataMap.getString(DataArchiveDeleteJob.JOB_GROUP)).thenReturn("testGroup");

dataArchiveDeleteJob.execute(bundle);

verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.DONE);
}

@Test
public void testExecuteTask() throws JobExecutionException {
doReturn(Result.SUCCESS).when(dataArchiveDeleteJob).executeTask(anyString(), anyList());

dataArchiveDeleteJob.executeTask(scheduleTaskEntity.getId(), taskUnits);

verify(dataArchiveDeleteJob, times(1)).executeTask(scheduleTaskEntity.getId(), taskUnits);
}

@Test
public void testGetTaskStatus() throws JobExecutionException {
doReturn(TaskStatus.DONE).when(dataArchiveDeleteJob).getTaskStatus(anyList());

assertEquals(TaskStatus.DONE, dataArchiveDeleteJob.getTaskStatus(taskUnits));
}

@Test
public void testGetTaskStatusWithException() throws JobExecutionException {
doThrow(new JobExecutionException("test exception")).when(dataArchiveDeleteJob).getTaskStatus(anyList());

try {
dataArchiveDeleteJob.getTaskStatus(taskUnits);
} catch (JobExecutionException e) {
assertEquals("test exception", e.getMessage());
}
}

@Test
public void testGetLogger() {
assertEquals(log, DataArchiveDeleteJob.getLogger());
}

@Test
public void testGetJobName() {
assertEquals(DataArchiveDeleteJob.JOB_NAME, dataArchiveDeleteJob.getJobName());
}

@Test
public void testGetJobGroup() {
assertEquals(DataArchiveDeleteJob.JOB_GROUP, dataArchiveDeleteJob.getJobGroup());
}
}