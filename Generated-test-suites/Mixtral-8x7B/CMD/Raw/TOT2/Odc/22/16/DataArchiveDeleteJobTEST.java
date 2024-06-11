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
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.TriggerBuilder;
import org.quartz.impl. matchers.GroupMatcher;
import org.quartz.impl. triggers.SimpleTriggerImpl;
import org.quartz.scheduler.Scheduler;
import org.quartz.scheduler.SchedulerFactory;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.quartz.spi.operators.JobStoreTX;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

private Scheduler scheduler;
private JobFactory jobFactory;
private ApplicationContext applicationContext;
private DataArchiveDeleteJob dataArchiveDeleteJob;
private JobExecutionContext jobExecutionContext;

@Before
public void setUp() throws Exception {
applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
jobFactory = applicationContext.getBean(JobFactory.class);
scheduler = applicationContext.getBean(Scheduler.class);
dataArchiveDeleteJob = new DataArchiveDeleteJob();
jobExecutionContext = mock(JobExecutionContext.class);
}

@Test
public void testExecute() throws JobExecutionException {
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("12345");
String parametersJson = JsonUtils.toJson(dataArchiveClearParameters);
taskEntity.setParametersJson(parametersJson);

when(jobExecutionContext.getResult()).thenReturn(taskEntity);

ScheduleTaskEntity dataArchiveTaskEntity = new ScheduleTaskEntity();
dataArchiveTaskEntity.setId("67890");
dataArchiveTaskEntity.setStatus(TaskStatus.DONE);
List<DlmTask> taskUnits = new ArrayList<>();
DlmTask dlmTask = new DlmTask();
dlmTask.setId("11111");
taskUnits.add(dlmTask);
String resultJson = JsonUtils.toJson(taskUnits);
dataArchiveTaskEntity.setResultJson(resultJson);

Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(dataArchiveTaskEntity);
when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
.thenReturn(dataArchiveTaskOption);

DlmTask dlmTaskSpy = spy(dlmTask);
List<DlmTask> taskUnitsSpy = new ArrayList<>();
taskUnitsSpy.add(dlmTaskSpy);
when(JsonUtils.fromJson(dataArchiveTaskEntity.getResultJson(), new TypeReference<List<DlmTask>>() {}))
.thenReturn(taskUnitsSpy);

when(DlmJobIdUtil.generateHistoryJobId(anyString(), anyString(), anyLong(), anyInt()))
.thenReturn("22222");

when(jobExecutionContext.getMergedJobDataMap())
.thenReturn(new org.quartz.JobDataMap());

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
verify(dlmTaskSpy, times(1)).setId("22222");
verify(dlmTaskSpy, times(1)).setJobType(JobType.DELETE);
verify(dlmTaskSpy, times(1)).setStatus(TaskStatus.PREPARING);
}

private void initMocks() {
scheduler = mock(Scheduler.class);
jobFactory = mock(JobFactory.class);
applicationContext = mock(ApplicationContext.class);
scheduleTaskRepository = mock(ScheduleTaskRepository.class);
dataArchiveDeleteJob = mock(DataArchiveDeleteJob.class);
jobExecutionContext = mock(JobExecutionContext.class);
}

private ScheduleTaskRepository scheduleTaskRepository;

private void initStubs() {
initMocks();
try {
SchedulerFactory schedulerFactory = new org.quartz.impl.StdSchedulerFactory();
schedulerFactory.initialize(new Properties());
scheduler = schedulerFactory.getScheduler();
StubJobStoreTX stubJobStoreTX = new StubJobStoreTX();
scheduler.setJobStore(stubJobStoreTX);
stubJobStoreTX.setScheduler(scheduler);
stubJobStoreTX.start();
stubJobStoreTX.scheduleJobs(scheduler, getJobKey(), getTrigger());

applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
jobFactory = applicationContext.getBean(JobFactory.class);
scheduler.setJobFactory(jobFactory);
scheduler.start();
} catch (Exception e) {
e.printStackTrace();
}
}

private JobKey getJobKey() {
return new JobKey("testJob", "testJobGroup");
}

private Trigger getTrigger() {
SimpleScheduleBuilder schedBuilder = SimpleScheduleBuilder.simpleSchedule()
.withIntervalInSeconds(60)
.repeatForever();
return TriggerBuilder.newTrigger()
.forJob(getJobKey())
.withIdentity(getTriggerKey(), "testJobGroup")
.withSchedule(schedBuilder)
.build();
}

private TriggerKey getTriggerKey() {
return new TriggerKey("testTrigger", "testJobGroup");
}

private void executeJob() throws Exception {
JobDetail jobDetail = JobBuilder.newJob(DataArchiveDeleteJob.class)
.withIdentity(getJobKey())
.build();
scheduler.addJob(jobDetail, true);
scheduler.start();
scheduler.triggerJob(getJobKey());
}
}

class StubJobStoreTX extends JobStoreTX {

@Override
public void scheduleJobs(Scheduler scheduler, List<? extends JobDetail> jobDetails, List<? extends Trigger> triggers)
throws SchedulerException {
super.scheduleJobs(scheduler, jobDetails, triggers);
}
}