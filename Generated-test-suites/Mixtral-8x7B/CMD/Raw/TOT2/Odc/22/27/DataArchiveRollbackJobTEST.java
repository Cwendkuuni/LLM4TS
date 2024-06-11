import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.scheduler.GroupedScheduler;
import org.quartz.scheduler.Scheduler;
import org.quartz.scheduler.SchedulerFactory;
import org.quartz.scheduler.SimpleTrigger;
import org.quartz.simpl.SimpleScheduleBuilder;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.quartz.utils.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {

@Autowired
private ApplicationContext applicationContext;

private DataArchiveRollbackJob dataArchiveRollbackJob;
private Scheduler scheduler;
private Logger logger = LoggerFactory.getLogger(DataArchiveRollbackJobTest.class);

@Configuration
static class ContextConfig {

@Bean
public JobFactory jobFactory() {
return new SpringBeanJobFactory();
}

@Bean
public SchedulerFactory schedulerFactory() {
return new SchedulerFactoryBean();
}
}

@Before
public void setUp() throws Exception {
dataArchiveRollbackJob = applicationContext.getBean(DataArchiveRollbackJob.class);
SchedulerFactory schedulerFactory = applicationContext.getBean(SchedulerFactory.class);
scheduler = schedulerFactory.getScheduler();
scheduler.start();
}

// Test for execute method
@Test
public void testExecute() throws Exception {
// Prepare mocked objects
JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setJobName("testJobName");
taskEntity.setJobGroup("testJobGroup");
taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
taskEntity.setResultJson("[]");
when(jobExecutionContext.getResult()).thenReturn(taskEntity);

DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
rollbackParameters.setDataArchiveTaskId(2L);
when(jobExecutionContext.getMergedJobDataMap().get(any())).thenReturn(rollbackParameters);

Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(taskEntity);
when(scheduleTaskRepository.findById(anyLong())).thenReturn(dataArchiveTaskOption);

List<DlmTask> taskUnits = Collections.singletonList(new DlmTask());
when(scheduleTaskRepository.updateStatusById(anyLong(), any())).thenReturn(true);
when(JsonUtils.fromJson(anyString(), any(TypeReference.class))).thenReturn(taskUnits);

// Call the method to test
dataArchiveRollbackJob.execute(jobExecutionContext);

// Assertions
assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
}

// Test for edge case - data archive task not found
@Test
public void testExecute_dataArchiveTaskNotFound() throws Exception {
// Prepare mocked objects
JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setJobName("testJobName");
taskEntity.setJobGroup("testJobGroup");
taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
taskEntity.setResultJson("[]");
when(jobExecutionContext.getResult()).thenReturn(taskEntity);

DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
rollbackParameters.setDataArchiveTaskId(2L);
when(jobExecutionContext.getMergedJobDataMap().get(any())).thenReturn(rollbackParameters);

Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.empty();
when(scheduleTaskRepository.findById(anyLong())).thenReturn(dataArchiveTaskOption);

// Call the method to test
dataArchiveRollbackJob.execute(jobExecutionContext);

// Assertions
assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
assertEquals("Data archive task not found,rollback task fast failed.dataArchiveTaskId=2", logger.getLastLogEvent().getMessage());
}

// Test for exception handling - JsonUtils.fromJson fails
@Test(expected = JobExecutionException.class)
public void testExecute_JsonUtilsFromJsonFails() throws Exception {
// Prepare mocked objects
JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setJobName("testJobName");
taskEntity.setJobGroup("testJobGroup");
taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
taskEntity.setResultJson("[]");
when(jobExecutionContext.getResult()).thenReturn(taskEntity);

DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
rollbackParameters.setDataArchiveTaskId(2L);
when(jobExecutionContext.getMergedJobDataMap().get(any())).thenReturn(rollbackParameters);

Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(taskEntity);
when(scheduleTaskRepository.findById(anyLong())).thenReturn(dataArchiveTaskOption);

when(JsonUtils.fromJson(anyString(), any(TypeReference.class))).thenThrow(new RuntimeException());

// Call the method to test
dataArchiveRollbackJob.execute(jobExecutionContext);
}

// Helper method to create a job and trigger
private void scheduleJob(String jobName, String jobGroup, Class jobClass) throws SchedulerException {
JobDetail jobDetail = JobBuilder.newJob(jobClass)
.withIdentity(jobName, jobGroup)
.build();

SimpleTrigger trigger = TriggerBuilder.newTrigger()
.withIdentity(jobName, jobGroup)
.startNow()
.withSchedule(SimpleScheduleBuilder.simpleSchedule()
.withIntervalInSeconds(1)
.repeatForever())
.build();

scheduler.scheduleJob(jobDetail, trigger);
}

// Helper method to delete a job and trigger
private void deleteJob(String jobName, String jobGroup) throws SchedulerException {
scheduler.unscheduleJob(TriggerKey.triggerKey(jobName, jobGroup));
scheduler.deleteJob(JobKey.jobKey(jobName, jobGroup));
}
}