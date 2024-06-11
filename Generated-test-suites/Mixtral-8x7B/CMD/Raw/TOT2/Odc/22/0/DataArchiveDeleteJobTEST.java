import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.TriggerBuilder;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz. jobs.testing.CompleteRunnableJob;
import org.quartz.scheduler.JobDetail;
import org.quartz.scheduler.Scheduler;
import org.quartz.scheduler.SchedulerFactory;
import org.quartz.simpl.SimpleTrigger;
import org.quartz.simpl.SimpleTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.quartz.TriggerBuilder.newTrigger;

public class DataArchiveDeleteJobTest {

private static final Logger log = LoggerFactory.getLogger(DataArchiveDeleteJobTest.class);
private Scheduler scheduler;
private DataArchiveDeleteJob dataArchiveDeleteJob;

@Before
public void setup() throws Exception {
SchedulerFactory schedulerFactory = new org.quartz.impl.StdSchedulerFactory();
scheduler = schedulerFactory.getScheduler();
scheduler.start();
dataArchiveDeleteJob = new DataArchiveDeleteJob();
}

private JobExecutionContext createJobExecutionContext(ScheduleTaskEntity taskEntity) {
JobDetail jobDetail = JobBuilder.newJob(DataArchiveDeleteJob.class)
.withIdentity("testJob", "testGroup")
.build();
jobDetail.getJobDataMap().put("taskEntity", taskEntity);

SimpleTrigger simpleTrigger = newTrigger()
.withIdentity("testTrigger", "testGroup")
.startNow()
.build();

return new JobExecutionContext(jobDetail, simpleTrigger);
}

private ScheduleTaskEntity createScheduleTaskEntity() {
ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
scheduleTaskEntity.setId(1L);
scheduleTaskEntity.setJobName("testJob");
scheduleTaskEntity.setJobGroup("testGroup");
scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\":10}");
scheduleTaskEntity.setStatus(TaskStatus.PREPARING);
scheduleTaskEntity.setResultJson("[]");
return scheduleTaskEntity;
}

private List<DlmTask> createDlmTasks() {
List<DlmTask> dlmTasks = new ArrayList<>();
DlmTask dlmTask1 = new DlmTask();
dlmTask1.setId(1L);
dlmTask1.setJobType(JobType.DELETE);
dlmTask1.setStatus(TaskStatus.PREPARING);

DlmTask dlmTask2 = new DlmTask();
dlmTask2.setId(2L);
dlmTask2.setJobType(JobType.DELETE);
dlmTask2.setStatus(TaskStatus.PREPARING);

dlmTasks.add(dlmTask1);
dlmTasks.add(dlmTask2);
return dlmTasks;
}

private DataArchiveClearParameters createDataArchiveClearParameters() {
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId(10L);
return dataArchiveClearParameters;
}

@Test
public void testExecute() throws Exception {
ScheduleTaskEntity taskEntity = createScheduleTaskEntity();

dataArchiveDeleteJob.execute(createJobExecutionContext(taskEntity));

// Check if the task status is updated to FAILED when data archive task not found
ScheduleTaskEntity updatedTaskEntity =
scheduleTaskRepository.findById(taskEntity.getId()).get();
assertEquals(TaskStatus.FAILED, updatedTaskEntity.getStatus());

// Check if the task status is updated to FAILED when data archive task is not done
taskEntity.setStatus(TaskStatus.RUNNING);
taskEntity.setResultJson("[]");
dataArchiveDeleteJob.execute(createJobExecutionContext(taskEntity));
updatedTaskEntity = scheduleTaskRepository.findById(taskEntity.getId()).get();
assertEquals(TaskStatus.FAILED, updatedTaskEntity.getStatus());

// Check if the tasks are prepared and executed when data archive task is done
taskEntity.setStatus(TaskStatus.DONE);
taskEntity.setResultJson(JsonUtils.toJson(createDlmTasks()));
dataArchiveDeleteJob.execute(createJobExecutionContext(taskEntity));
updatedTaskEntity = scheduleTaskRepository.findById(taskEntity.getId()).get();
List<DlmTask> preparedTasks =
JsonUtils.fromJson(updatedTaskEntity.getResultJson(), new TypeReference<List<DlmTask>>() {
});
assertEquals(4, preparedTasks.size());
assertEquals(DlmJobIdUtil.generateHistoryJobId(taskEntity.getJobName(), taskEntity.getJobGroup(),
taskEntity.getId(), 2), preparedTasks.get(1).getId());
assertEquals(JobType.DELETE, preparedTasks.get(1).getJobType());
assertEquals(TaskStatus.PREPARING, preparedTasks.get(1).getStatus());
}

private void addJobToScheduler(JobDetail jobDetail) throws Exception {
scheduler.addJob(jobDetail, true);
}

private void triggerJob(String jobName, String jobGroup) throws Exception {
JobKey jobKey = new JobKey(jobName, jobGroup);
scheduler.triggerJob(jobKey);
}

@Test
public void testJobExecution() throws Exception {
JobDetail jobDetail = JobBuilder.newJob(DataArchiveDeleteJob.class)
.withIdentity("testJob", "testGroup")
.build();
addJobToScheduler(jobDetail);

SimpleTrigger simpleTrigger = newTrigger()
.withIdentity("testTrigger", "testGroup")
.startNow()
.build();
scheduler.scheduleJob(simpleTrigger);

SimpleScheduleBuilder scheduleBuilder =
SimpleScheduleBuilder.simpleSchedule()
.withIntervalInSeconds(1)
.repeatForever();

SimpleTriggerImpl trigger =
new SimpleTriggerImpl("testTrigger", "testGroup",
new TriggerKey("testTrigger", "testGroup"),
null,
null,
scheduleBuilder.build(), 1, TimeUnit.SECONDS);
scheduler.scheduleJob(trigger);

triggerJob("testJob", "testGroup");
Thread.sleep(5000);
}

}