import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.QuartzScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.quartz.CronTriggerBean;
import org.springframework.scheduling.quartz.JobDetailBean;
import org.springframework.test.util.ReflectionTestUtils;

@SuppressWarnings("unchecked")
public class DataArchiveDeleteJobTest {

private DataArchiveDeleteJob dataArchiveDeleteJob;
private JobExecutionContext jobExecutionContext;
private ScheduleTaskEntity scheduleTaskEntity;
private DataArchiveClearParameters dataArchiveClearParameters;
private List<DlmTask> taskUnits;

@Before
public void setUp() {
dataArchiveDeleteJob = new DataArchiveDeleteJob();

// Initialize JobExecutionContext
jobExecutionContext = mock(JobExecutionContext.class);

// Initialize ScheduleTaskEntity
scheduleTaskEntity = mock(ScheduleTaskEntity.class);

// Initialize DataArchiveClearParameters
dataArchiveClearParameters = new DataArchiveClearParameters();

// Initialize taskUnits
taskUnits = mock(List.class);

// Autowired QuartzScheduler and ScheduleTaskRepository are not used in this test,
// so we mock them to avoid NullPointerException
ReflectionTestUtils.setField(dataArchiveDeleteJob, "scheduler", mock(QuartzScheduler.class));
ReflectionTestUtils.setField(dataArchiveDeleteJob, "scheduleTaskRepository", mock(ScheduleTaskRepository.class));
}

@Test
public void testExecute_dataArchiveTaskNotFound() throws JobExecutionException {
// Set up DataArchiveClearParameters with a non-existing data archive task ID
dataArchiveClearParameters.setDataArchiveTaskId("non-existing-task-id");
ReflectionTestUtils.setField(dataArchiveDeleteJob, "scheduleTaskRepository",
mock(ScheduleTaskRepository.class));
when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
.thenReturn(Optional.empty());

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).findById(dataArchiveClearParameters.getDataArchiveTaskId());
verify(scheduleTaskRepository, times(1))
.updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
verifyNoMoreInteractions(scheduleTaskRepository);
}

@Test
public void testExecute_dataArchiveTaskNotFinished() throws JobExecutionException {
// Set up ScheduleTaskEntity with a status different than DONE
scheduleTaskEntity.setStatus(TaskStatus.RUNNING);
when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
.thenReturn(Optional.of(scheduleTaskEntity));

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).findById(dataArchiveClearParameters.getDataArchiveTaskId());
verify(scheduleTaskRepository, times(1))
.updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
verifyNoMoreInteractions(scheduleTaskRepository);
}

@Test
public void testExecute_prepareTasksForClear() throws JobExecutionException {
// Set up ScheduleTaskEntity with a status of DONE and a result JSON containing taskUnits
String taskUnitsJson = "[\n"
+ " {\n"
+ " \"id\": 1,\n"
+ " \"name\": \"task1\",\n"
+ " \"type\": \"full\",\n"
+ " \"source\": {\n"
+ " \"host\": \"host1\",\n"
+ " \"port\": 3306,\n"
+ " \"user\": \"user1\",\n"
+ " \"password\": \"password1\",\n"
+ " \"database\": \"database1\"\n"
+ " },\n"
+ " \"destination\": {\n"
+ " \"host\": \"host2\",\n"
+ " \"port\": 3307,\n"
+ " \"user\": \"user2\",\n"
+ " \"password\": \"password2\",\n"
+ " \"database\": \"database2\"\n"
+ " },\n"
+ " \"startTime\": 1686643200000,\n"
+ " \"endTime\": 1686729600000,\n"
+ " \"totalRows\": 10000,\n"
+ " \"errorRows\": 0,\n"
+ " \"status\": \"DONE\"\n"
+ " }\n"
+ "]";

scheduleTaskEntity.setStatus(TaskStatus.DONE);
when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
.thenReturn(Optional.of(scheduleTaskEntity));
when(scheduleTaskRepository.getById(scheduleTaskEntity.getId())).thenReturn(scheduleTaskEntity);
when(scheduleTaskEntity.getResultJson()).thenReturn(taskUnitsJson);

ReflectionTestUtils.setField(dataArchiveDeleteJob, "JsonUtils", new JsonUtils());

DlmTask dlmTask1 = new DlmTask();
dlmTask1.setId(1L);
dlmTask1.setJobType(JobType.DELETE);
dlmTask1.setStatus(TaskStatus.PREPARING);

when(JsonUtils.fromJson(taskUnitsJson, new TypeReference<List<DlmTask>>() {})).thenReturn(List.of(dlmTask1));

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).findById(dataArchiveClearParameters.getDataArchiveTaskId());
verify(scheduleTaskRepository, times(1)).getById(scheduleTaskEntity.getId());
verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.PREPARING);

// Verify that taskUnits are processed correctly
verify(dataArchiveDeleteJob, times(1)).executeTask(scheduleTaskEntity.getId(), List.of(dlmTask1));
}

@Test
public void testExecuteTask() {
// Test method with different inputs
}

@Test
public void testGetTaskStatus() {
// Test method with different inputs
}
}