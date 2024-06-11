import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.TriggerBuilder;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.quartz.scheduler.Scheduler;
import org.quartz.scheduler.SchedulerFactory;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.quartz.spi.operators.JobStoreTX;
import org.quartz.spi.operators.SchedulerOperator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {

private Scheduler scheduler;

@Before
public void setUp() throws Exception {
SchedulerFactory schedulerFactory = new SchedulerFactory();
schedulerFactory.initialize(createProperties());
scheduler = schedulerFactory.getScheduler();
scheduler.start();
}

private java.util.Properties createProperties() {
java.util.Properties props = new java.util.Properties();
props.put("org.quartz.scheduler.instanceName", "DataArchiveRollbackJobTest");
props.put("org.quartz.scheduler.instanceId", "AUTO");
props.put("org.quartz.scheduler.skipUpdateCheck", "true");
props.put("org.quartz.jobStore.class", JobStoreTX.class.getName());
props.put("org.quartz.jobStore.misfireThreshold", "60000");
props.put("org.quartz.jobStore.isClustered", "false");
props.put("org.quartz.jobStore.clusterCheckinInterval", "15000");
props.put("org.quartz.jobStore.maxMisfiresToHandleAtATime", "1");
props.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
props.put("org.quartz.threadPool.threadCount", "3");
props.put("org.quartz.threadPool.threadPriority", "5");
return props;
}

@Test
public void testExecute() throws Exception {
ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
SchedulerFactoryBean schedulerFactoryBean = context.getBean(SchedulerFactoryBean.class);
JobFactory jobFactory = new AdaptableJobFactory();
jobFactory.setApplicationContext(context);
schedulerFactoryBean.setJobFactory(jobFactory);
Scheduler scheduler = schedulerFactoryBean.getScheduler();
scheduler.start();
DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
rollbackParameters.setDataArchiveTaskId(1L);
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);
when(jobExecutionContext.getMergedJobDataMap().get("scheduleTaskEntity")).thenReturn(taskEntity);
DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();
dataArchiveRollbackJob.setScheduler(scheduler);
dataArchiveRollbackJob.setScheduleTaskRepository(mock(ScheduleTaskRepository.class));
dataArchiveRollbackJob.execute(jobExecutionContext);
}

@Test
public void testExecuteWithDataArchiveTaskNotFound() throws Exception {
ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
SchedulerFactoryBean schedulerFactoryBean = context.getBean(SchedulerFactoryBean.class);
JobFactory jobFactory = new AdaptableJobFactory();
jobFactory.setApplicationContext(context);
schedulerFactoryBean.setJobFactory(jobFactory);
Scheduler scheduler = schedulerFactoryBean.getScheduler();
scheduler.start();
DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
rollbackParameters.setDataArchiveTaskId(1L);
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);
when(jobExecutionContext.getMergedJobDataMap().get("scheduleTaskEntity")).thenReturn(taskEntity);
when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());
DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();
dataArchiveRollbackJob.setScheduler(scheduler);
dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
dataArchiveRollbackJob.execute(jobExecutionContext);
}

@Test
public void testExecuteWithRollbackTasksPrepared() throws Exception {
ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
SchedulerFactoryBean schedulerFactoryBean = context.getBean(SchedulerFactoryBean.class);
JobFactory jobFactory = new AdaptableJobFactory();
jobFactory.setApplicationContext(context);
schedulerFactoryBean.setJobFactory(jobFactory);
Scheduler scheduler = schedulerFactoryBean.getScheduler();
scheduler.start();
DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
rollbackParameters.setDataArchiveTaskId(1L);
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);
when(jobExecutionContext.getMergedJobDataMap().get("scheduleTaskEntity")).thenReturn(taskEntity);
List<DlmTask> taskUnits = new ArrayList<>();
DlmTask taskUnit = new DlmTask();
taskUnit.setId(1L);
taskUnit.setStatus(TaskStatus.DONE);
taskUnits.add(taskUnit);
when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));
when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenReturn(null);
when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(taskEntity));
when(scheduleTaskRepository.updateStatusById(2L, TaskStatus.FAILED)).thenReturn(null);
ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(2L);
dataArchiveTask.setResultJson(JsonUtils.toJson(taskUnits));
when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
.thenReturn(Optional.of(dataArchiveTask));
DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();
dataArchiveRollbackJob.setScheduler(scheduler);
dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
dataArchiveRollbackJob.execute(jobExecutionContext);
}

}