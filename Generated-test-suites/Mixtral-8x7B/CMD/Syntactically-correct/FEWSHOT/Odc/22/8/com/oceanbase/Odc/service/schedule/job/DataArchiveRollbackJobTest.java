import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.QuartzJobBeanJobDetail;

public class DataArchiveRollbackJobTest {

private DataArchiveRollbackJob dataArchiveRollbackJob;

@Before
public void setup() {
dataArchiveRollbackJob = new DataArchiveRollbackJob();
}

@Test
public void testExecute() throws JobExecutionException {
// Prepare mocked dependencies
JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);
ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
rollbackParameters.setDataArchiveTaskId(1L);
when(taskEntity.getParametersJson()).thenReturn("{}");
when(jobExecutionContext.getResult()).thenReturn(taskEntity);

// Prepare mocked method calls
List<DlmTask> taskUnits = Arrays.asList(
new DlmTask().setId(1L).setSourceDatabaseId(1L).setTargetDatabaseId(2L).setJobType(JobType.ARCHIVE)
.setStatus(TaskStatus.PREPARING),
new DlmTask().setId(2L).setSourceDatabaseId(2L).setTargetDatabaseId(3L).setJobType(JobType.ARCHIVE)
.setStatus(TaskStatus.PREPARING)
);
String taskUnitsJson = JsonUtils.toJson(taskUnits);
when(taskEntity.getResultJson()).thenReturn(taskUnitsJson);
Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(taskEntity);
when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
.thenReturn(dataArchiveTaskOption);

// Call the method under test
dataArchiveRollbackJob.execute(jobExecutionContext);

// Assert the results
assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
}

@Test
public void testExecuteTask() {
// Prepare test data
Long taskId = 1L;
List<DlmTask> taskUnits = Arrays.asList(
new DlmTask().setId(1L).setSourceDatabaseId(1L).setTargetDatabaseId(2L).setJobType(JobType.ARCHIVE)
.setStatus(TaskStatus.PREPARING),
new DlmTask().setId(2L).setSourceDatabaseId(2L).setTargetDatabaseId(3L).setJobType(JobType.ARCHIVE)
.setStatus(TaskStatus.PREPARING)
);

// Call the method under test
dataArchiveRollbackJob.executeTask(taskId, taskUnits);

// Assert the results
// Add your assertions here
}

@Test
public void testGetTaskStatus() {
// Prepare test data
List<DlmTask> taskUnits = Arrays.asList(
new DlmTask().setId(1L).setSourceDatabaseId(1L).setTargetDatabaseId(2L).setJobType(JobType.ARCHIVE)
.setStatus(TaskStatus.PREPARING),
new DlmTask().setId(2L).setSourceDatabaseId(2L).setTargetDatabaseId(3L).setJobType(JobType.ARCHIVE)
.setStatus(TaskStatus.PREPARING)
);

// Call the method under test
TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

// Assert the results
// Add your assertions here
assertEquals(TaskStatus.PREPARING, taskStatus);
}

private QuartzJobBeanJobDetail createJobDetail(DataArchiveRollbackJob job, String jobName, String jobGroup) {
QuartzJobBeanJobDetail jobDetail = new QuartzJobBeanJobDetail();
jobDetail.setJobClass(DataArchiveRollbackJob.class);
jobDetail.setJobDataAsMap(QuartzJobBean.createSerializableMap(job));
jobDetail.setKey(jobGroup, jobName);
jobDetail.setDescription("Test job");
return jobDetail;
}

private List<ScheduleTaskEntity> createTaskEntities(int numTasks) {
List<ScheduleTaskEntity> taskEntities =
new ArrayList<ScheduleTaskEntity>(numTasks);
for (int i = 0; i < numTasks; i++) {
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setJobName("testJobName" + i);
taskEntity.setJobGroup("testJobGroup" + i);
taskEntity.setParametersJson("{}");
taskEntity.setResultJson("{}");
taskEntities.add(taskEntity);
}
return taskEntities;
}
}