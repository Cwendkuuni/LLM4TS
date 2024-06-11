import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;
import org.junit.Before;
import org.junit.Test;
import org.quartz.*;
import org.quartz.core.JobDetail;
import org.quartz.core. scheduler.ClassLoadHelper;
import org.quartz.core.scheduler.SchedulerFactoryImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

private PartitionPlanJob partitionPlanJob;

private PartitionPlanTaskService partitionPlanTaskService;

private PartitionPlanService partitionPlanService;

private ScheduleService scheduleService;

private JobDetail jobDetail;

private Scheduler scheduler;

@Before
public void setup() throws SchedulerException {
ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
partitionPlanTaskService = mock(PartitionPlanTaskService.class);
partitionPlanService = mock(PartitionPlanService.class);
scheduleService = mock(ScheduleService.class);
SpringContextUtil.putBean(PartitionPlanTaskService.class, partitionPlanTaskService);
SpringContextUtil.putBean(PartitionPlanService.class, partitionPlanService);
SpringContextUtil.putBean(ScheduleService.class, scheduleService);

jobDetail = JobBuilder.newJob(PartitionPlanJob.class)
.withIdentity("PartitionPlanJob", "group1").build();

SchedulerFactory schedulerFactory = new StdSchedulerFactory();
scheduler = schedulerFactory.getScheduler();
scheduler.start();
scheduler.addJob(jobDetail, true);

partitionPlanJob = new PartitionPlanJob();
}

@Test
public void testExecute() throws SchedulerException, JobExecutionException, UnExpectedException {
ScheduleEntity scheduleEntity = new ScheduleEntity();
scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\":1}");
when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
jobParameters.setDatabasePartitionPlanId(1L);
DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
databasePartitionPlan.setConfigEnabled(true);
when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(new TablePartitionPlanEntity(), new TablePartitionPlanEntity());
when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

partitionPlanJob.execute(jobDetail.getJobDataMap());

verify(partitionPlanTaskService, times(1))
.executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
verify(scheduleService, times(1)).terminate(scheduleEntity);
}

@Test
public void testExecuteWithNullScheduleEntity() throws SchedulerException, JobExecutionException {
when(scheduleService.nullSafeGetById(anyLong())).thenReturn(null);

partitionPlanJob.execute(jobDetail.getJobDataMap());

verify(partitionPlanTaskService, times(0)).executePartitionPlan(anyString(), anyList());
verify(scheduleService, times(0)).terminate(any(ScheduleEntity.class));
}

@Test
public void testExecuteWithDatabasePartitionPlanDisabled() throws SchedulerException, JobExecutionException {
ScheduleEntity scheduleEntity = new ScheduleEntity();
scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\":1}");
when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
jobParameters.setDatabasePartitionPlanId(1L);
DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
databasePartitionPlan.setConfigEnabled(false);
when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(new TablePartitionPlanEntity(), new TablePartitionPlanEntity());
when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

partitionPlanJob.execute(jobDetail.getJobDataMap());

verify(partitionPlanTaskService, times(0)).executePartitionPlan(anyString(), anyList());
verify(scheduleService, times(1)).terminate(scheduleEntity);
}

@Test
public void testExecuteWithNoTablePartitionPlans() throws SchedulerException, JobExecutionException {
ScheduleEntity scheduleEntity = new ScheduleEntity();
scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\":1}");
when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
jobParameters.setDatabasePartitionPlanId(1L);
DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
databasePartitionPlan.setConfigEnabled(true);
when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
List<TablePartitionPlanEntity> tablePartitionPlans = Collections.emptyList();
when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

partitionPlanJob.execute(jobDetail.getJobDataMap());

verify(partitionPlanTaskService, times(0)).executePartitionPlan(anyString(), anyList());
verify(scheduleService, times(1)).terminate(scheduleEntity);
}

@Test
public void testInterrupt() {
assertFalse(partitionPlanJob.interrupt());
}
}