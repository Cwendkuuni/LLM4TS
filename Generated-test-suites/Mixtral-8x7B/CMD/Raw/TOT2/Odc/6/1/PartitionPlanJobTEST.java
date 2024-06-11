import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;
import org.junit.Before;
import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class PartitionPlanJobTest implements ApplicationContextAware {

 private ApplicationContext applicationContext;

 @Override
 public void setApplicationContext(ApplicationContext applicationContext) {
 this.applicationContext = applicationContext;
 }

 @Bean
 @Primary
 public Scheduler quartzScheduler() throws SchedulerException {
 Scheduler scheduler = new StdSchedulerFactory().getScheduler();
 scheduler.start();
 return scheduler;
 }

 @Configuration
 static class ContextConfiguration {

 @Bean
 public PartitionPlanService partitionPlanService() {
 return mock(PartitionPlanService.class);
 }

 @Bean
 public PartitionPlanTaskService partitionPlanTaskService() {
 return mock(PartitionPlanTaskService.class);
 }

 @Bean
 public ScheduleService scheduleService() {
 return mock(ScheduleService.class);
 }

 @Bean
 public JobDetail jobDetail() {
 JobDataMap jobDataMap = new JobDataMap();
 jobDataMap.put("partitionPlanJob", new PartitionPlanJob());
 return JobBuilder.newJob(OdcJob.class).withIdentity("PartitionPlanJob")
 .setJobData(jobDataMap).build();
 }

 @Bean
 public Trigger trigger() {
 return TriggerBuilder.newTrigger().forJob("PartitionPlanJob").withIdentity("PartitionPlanJobTrigger")
 .startNow().build();
 }
 }

 @Before
 public void setUp() throws SchedulerException {
 Scheduler scheduler = applicationContext.getBean(Scheduler.class);
 scheduler.scheduleJob(applicationContext.getBean(JobDetail.class),
 applicationContext.getBean(Trigger.class));
 }

 @Test
 public void testExecute() throws SchedulerException, UnExpectedException {
 PartitionPlanService partitionPlanService = applicationContext.getBean(PartitionPlanService.class);
 PartitionPlanTaskService partitionPlanTaskService = applicationContext.getBean(PartitionPlanTaskService.class);
 ScheduleService scheduleService = applicationContext.getBean(ScheduleService.class);

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(new PartitionPlanJobParameters()));

 when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
 when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(new DatabasePartitionPlanEntity());
 when(partitionPlanService
 .getValidTablePlanByDatabasePartitionPlanId(1L))
 .thenReturn(Collections.singletonList(new TablePartitionPlanEntity()));

 PartitionPlanJob partitionPlanJob = applicationContext.getBean(PartitionPlanJob.class);
 JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);
 when(jobExecutionContext.getMergedJobDataMap().get("scheduleId")).thenReturn(1L);

 partitionPlanJob.execute(jobExecutionContext);

 verify(partitionPlanTaskService, times(1))
 .executePartitionPlan(eq(new DatabasePartitionPlanEntity().getFlowInstanceId()), anyList());
 }

 @Test
 public void testInterrupt() throws SchedulerException {
 PartitionPlanJob partitionPlanJob = applicationContext.getBean(PartitionPlanJob.class);

 try {
 partitionPlanJob.interrupt();
 } catch (UnExpectedException e) {
 // Expected
 }
 }

 @Test
 public void testPartitionPlanServiceThrowException()
 throws SchedulerException, UnExpectedException {
 PartitionPlanService partitionPlanService = applicationContext.getBean(PartitionPlanService.class);
 PartitionPlanTaskService partitionPlanTaskService = applicationContext.getBean(PartitionPlanTaskService.class);
 ScheduleService scheduleService = applicationContext.getBean(ScheduleService.class);

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(new PartitionPlanJobParameters()));

 when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
 when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenThrow(new RuntimeException());

 PartitionPlanJob partitionPlanJob = applicationContext.getBean(PartitionPlanJob.class);
 JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);
 when(jobExecutionContext.getMergedJobDataMap().get("scheduleId")).thenReturn(1L);

 partitionPlanJob.execute(jobExecutionContext);

 verify(partitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());
 }

 @Test
 public void testScheduleServiceTerminateSchedule() throws SchedulerException, UnExpectedException {
 PartitionPlanService partitionPlanService = applicationContext.getBean(PartitionPlanService.class);
 PartitionPlanTaskService partitionPlanTaskService = applicationContext.getBean(PartitionPlanTaskService.class);
 ScheduleService scheduleService = applicationContext.getBean(ScheduleService.class);

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(new PartitionPlanJobParameters()));

 when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
 when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(new DatabasePartitionPlanEntity());
 when(partitionPlanService
 .getValidTablePlanByDatabasePartitionPlanId(1L))
 .thenReturn(Collections.emptyList());

 PartitionPlanJob partitionPlanJob = applicationContext.getBean(PartitionPlanJob.class);
 JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);
 when(jobExecutionContext.getMergedJobDataMap().get("scheduleId")).thenReturn(1L);

 partitionPlanJob.execute(jobExecutionContext);

 verify(scheduleService, times(1)).terminate(scheduleEntity);
 }
}