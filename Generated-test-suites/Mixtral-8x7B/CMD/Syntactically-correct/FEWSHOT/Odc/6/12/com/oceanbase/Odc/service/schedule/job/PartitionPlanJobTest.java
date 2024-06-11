import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleEntity;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
import com.oceanbase.odc.service.schedule.job.PartitionPlanJobParameters;

@ContextConfiguration(classes = { PartitionPlanJobTestConfig.class })
public class PartitionPlanJobTest extends QuartzJobBean {

private PartitionPlanJob partitionPlanJob;

@Autowired
private ScheduleService scheduleService;

@Autowired
private PartitionPlanTaskService partitionPlanTaskService;

@Autowired
private PartitionPlanService partitionPlanService;

@Before
public void setUp() {
partitionPlanJob = new PartitionPlanJob();
ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", scheduleService);
ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanTaskService", partitionPlanTaskService);
ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanService);
}

@Test
public void testExecute() throws SchedulerException {
JobExecutionContext context = mock(JobExecutionContext.class);
PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
jobParameters.setDatabasePartitionPlanId(1L);
when(context.getMergedJobDataMap().get("scheduleEntity")).thenReturn(jobParameters);
DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
databasePartitionPlan.setFlowInstanceId("123");
databasePartitionPlan.setConfigEnabled(true);
when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
.thenReturn(databasePartitionPlan);
List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
tablePartitionPlans.add(tablePartitionPlan);
when(partitionPlanService
.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
.thenReturn(tablePartitionPlans);
partitionPlanJob.execute(context);
verify(scheduleService, times(1)).terminate(any(ScheduleEntity.class));
verify(partitionPlanTaskService, times(1)).executePartitionPlan(eq("123"), eq(tablePartitionPlans));
}

@Test
public void testExecuteWhenDatabasePartitionPlanIsNotConfigEnabled()
throws SchedulerException {
JobExecutionContext context = mock(JobExecutionContext.class);
PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
jobParameters.setDatabasePartitionPlanId(1L);
when(context.getMergedJobDataMap().get("scheduleEntity")).thenReturn(jobParameters);
DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
databasePartitionPlan.setFlowInstanceId("123");
databasePartitionPlan.setConfigEnabled(false);
when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
.thenReturn(databasePartitionPlan);
List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
tablePartitionPlans.add(tablePartitionPlan);
when(partitionPlanService
.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
.thenReturn(tablePartitionPlans);
partitionPlanJob.execute(context);
verify(scheduleService, times(0)).terminate(any(ScheduleEntity.class));
verify(partitionPlanTaskService, times(0)).executePartitionPlan(eq("123"), eq(tablePartitionPlans));
}

@Test
public void testExecuteWhenNoTablePartitionPlans() throws SchedulerException {
JobExecutionContext context = mock(JobExecutionContext.class);
PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
jobParameters.setDatabasePartitionPlanId(1L);
when(context.getMergedJobDataMap().get("scheduleEntity")).thenReturn(jobParameters);
DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
databasePartitionPlan.setFlowInstanceId("123");
databasePartitionPlan.setConfigEnabled(true);
when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
.thenReturn(databasePartitionPlan);
List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
when(partitionPlanService
.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
.thenReturn(tablePartitionPlans);
partitionPlanJob.execute(context);
verify(scheduleService, times(1)).terminate(any(ScheduleEntity.class));
verify(partitionPlanTaskService, times(0)).executePartitionPlan(eq("123"), eq(tablePartitionPlans));
}

@Test
public void testInterrupt() {
PartitionPlanJob partitionPlanJobSpy = spy(partitionPlanJob);
partitionPlanJobSpy.interrupt();
verify(partitionPlanJobSpy, times(1)).interrupt();
}
}

class PartitionPlanJobTestConfig {

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
public JobExecutionContext jobExecutionContext() {
return mock(JobExecutionContext.class);
}

@Bean
public PartitionPlanJob partitionPlanJob() {
return new PartitionPlanJob();
}

}