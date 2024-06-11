import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.simpl.RAMJobStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class DataArchiveDeleteJobTest {

private static final Logger log = LoggerFactory.getLogger(DataArchiveDeleteJobTest.class);

private Scheduler scheduler;
private ApplicationContext applicationContext;

@Before
public void setup() throws SchedulerException {
scheduler = new StdSchedulerFactory().getScheduler();
scheduler.setJobStore(new RAMJobStore());
scheduler.start();

applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
}

// Test method: execute
@Test
public void testExecute_TypicalUseCase() throws SchedulerException, JobExecutionException {
// Prepare test data
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setJobName("DataArchiveDeleteJob");
taskEntity.setJobGroup("group1");
taskEntity.setStatus(TaskStatus.DONE);
taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
String parametersJson = "{\"dataArchiveTaskId\": 2, \"startTs\": 1686627200000, \"endTs\": 1686627200000}";
DataArchiveClearParameters dataArchiveClearParameters = JsonUtils.fromJson(parametersJson, DataArchiveClearParameters.class);
ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(2L);
dataArchiveTask.setStatus(TaskStatus.DONE);
dataArchiveTask.setResultJson("[{\"id\": 3, \"jobType\": \"DELETE\", \"status\": \"PREPARING\"}]");
List<DlmTask> taskUnits = JsonUtils.fromJson("[{\"id\": 3, \"jobType\": \"DELETE\", \"status\": \"PREPARING\"}]",
new TypeReference<List<DlmTask>>() {});

// Mock dependencies
ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
.thenReturn(Optional.of(dataArchiveTask));
when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.FAILED))
.thenReturn(true);

DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);

// Execute test
JobDetail jobDetail = newJob(DataArchiveDeleteJob.class)
.withIdentity(taskEntity.getJobName(), taskEntity.getJobGroup())
.build();
jobDetail.getJobDataMap().put("scheduleTaskRepository", scheduleTaskRepository);

SimpleTrigger trigger = newTrigger()
.withIdentity(taskEntity.getJobName(), taskEntity.getJobGroup())
.startNow()
.build();

scheduler.scheduleJob(jobDetail, trigger);
scheduler.triggerJob(jobDetail.getKey());

verify(scheduleTaskRepository, times(1)).findById(dataArchiveClearParameters.getDataArchiveTaskId());
verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
}

// Test method: execute
@Test(expected = JobExecutionException.class)
public void testExecute_TaskEntityNull() throws SchedulerException, JobExecutionException {
// Prepare test data
ScheduleTaskEntity taskEntity = null;
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId(1L);
ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(1L);
dataArchiveTask.setStatus(TaskStatus.DONE);
dataArchiveTask.setResultJson("[]");
List<DlmTask> taskUnits = JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {});

// Mock dependencies
ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
.thenReturn(Optional.of(dataArchiveTask));
DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);

// Execute test
JobDetail jobDetail = newJob(DataArchiveDeleteJob.class)
.withIdentity(taskEntity.getJobName(), taskEntity.getJobGroup())
.build();
jobDetail.getJobDataMap().put("scheduleTaskRepository", scheduleTaskRepository);

SimpleTrigger trigger = newTrigger()
.withIdentity(taskEntity.getJobName(), taskEntity.getJobGroup())
.startNow()
.build();

scheduler.scheduleJob(jobDetail, trigger);
scheduler.triggerJob(jobDetail.getKey());
}

// Test method: execute
@Test(expected = JobExecutionException.class)
public void testExecute_DataArchiveTaskNotFound() throws SchedulerException, JobExecutionException {
// Prepare test data
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setJobName("DataArchiveDeleteJob");
taskEntity.setJobGroup("group1");
taskEntity.setStatus(TaskStatus.DONE);
taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
DataArchiveClearParameters dataArchiveClearParameters = JsonUtils.fromJson(taskEntity.getParametersJson(),
DataArchiveClearParameters.class);
ScheduleTaskEntity dataArchiveTask = null;

// Mock dependencies
ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
.thenReturn(Optional.of(dataArchiveTask));
DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);

// Execute test
JobDetail jobDetail = newJob(DataArchiveDeleteJob.class)
.withIdentity(taskEntity.getJobName(), taskEntity.getJobGroup())
.build();
jobDetail.getJobDataMap().put("scheduleTaskRepository", scheduleTaskRepository);

SimpleTrigger trigger = newTrigger()
.withIdentity(taskEntity.getJobName(), taskEntity.getJobGroup())
.startNow()
.build();

scheduler.scheduleJob(jobDetail, trigger);
scheduler.triggerJob(jobDetail.getKey());
}

// Test method: execute
@Test(expected = JobExecutionException.class)
public void testExecute_DataArchiveTaskNotDone() throws SchedulerException, JobExecutionException {
// Prepare test data
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setJobName("DataArchiveDeleteJob");
taskEntity.setJobGroup("group1");
taskEntity.setStatus(TaskStatus.DONE);
taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
DataArchiveClearParameters dataArchiveClearParameters = JsonUtils.fromJson(taskEntity.getParametersJson(),
DataArchiveClearParameters.class);
ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(2L);
dataArchiveTask.setStatus(TaskStatus.RUNNING);
dataArchiveTask.setResultJson("[]");
List<DlmTask> taskUnits = JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {});

// Mock dependencies
ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
.thenReturn(Optional.of(dataArchiveTask));
when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.FAILED))
.thenReturn(true);
DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);

// Execute test
JobDetail jobDetail = newJob(DataArchiveDeleteJob.class)
.withIdentity(taskEntity.getJobName(), taskEntity.getJobGroup())
.build();
jobDetail.getJobDataMap().put("scheduleTaskRepository", scheduleTaskRepository);

SimpleTrigger trigger = newTrigger()
.withIdentity(taskEntity.getJobName(), taskEntity.getJobGroup())
.startNow()
.build();

scheduler.scheduleJob(jobDetail, trigger);
scheduler.triggerJob(jobDetail.getKey());
}

// Test method: executeTask
@Test
public void testExecuteTask() throws SchedulerException {
// Prepare test data
Long taskId = 1L;
List<DlmTask> taskUnits =
JsonUtils.fromJson("[{\"id\": 3, \"jobType\": \"DELETE\", \"status\": \"PREPARING\"}]",
new TypeReference<List<DlmTask>>() {});

// Mock dependencies
ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);

// Execute test
dataArchiveDeleteJob.executeTask(taskId, taskUnits);

verify(scheduleTaskRepository, times(1)).updateStatusById(taskId, TaskStatus.RUNNING);
}

// Test method: getTaskStatus
@Test
public void testGetTaskStatus() {
// Prepare test data
List<DlmTask> taskUnits =
JsonUtils.fromJson("[{\"id\": 3, \"jobType\": \"DELETE\", \"status\": \"PREPARING\"}]",
new TypeReference<List<DlmTask>>() {});

// Execute test
TaskStatus taskStatus = DataArchiveDeleteJob.getTaskStatus(taskUnits);

assert(taskStatus == TaskStatus.RUNNING);
}

// Test method: getJobKey
@Test
public void testGetJobKey() {
// Prepare test data
String jobName = "DataArchiveDeleteJob";
String jobGroup = "group1";

// Execute test
JobKey jobKey = DataArchiveDeleteJob.getJobKey(jobName, jobGroup);

assert(jobKey.getName().equals(jobName));
assert(jobKey.getGroup().equals(jobGroup));
}

// Test method: getDlmJobId
@Test
public void testGetDlmJobId() {
// Prepare test data
String jobName = "DataArchiveDeleteJob";
String jobGroup = "group1";
Long taskId = 1L;
int taskUnitsSize = 10;

// Execute test
String dlmJobId = DataArchiveDeleteJob.getDlmJobId(jobName, jobGroup, taskId, taskUnitsSize);

assert(dlmJobId.contains(jobName));
assert(dlmJobId.contains(jobGroup));
assert(dlmJobId.contains(String.valueOf(taskId)));
assert(dlmJobId.contains(String.valueOf(taskUnitsSize)));
}
}