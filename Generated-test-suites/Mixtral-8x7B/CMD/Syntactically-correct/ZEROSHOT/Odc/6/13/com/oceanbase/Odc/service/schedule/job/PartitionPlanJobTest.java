import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PartitionPlanJobTest {

private PartitionPlanJob partitionPlanJob;

private ApplicationContext applicationContext;

private PartitionPlanService partitionPlanService;

private PartitionPlanTaskService partitionPlanTaskService;

private ScheduleService scheduleService;

private JobExecutionContext jobExecutionContext;

@Before
public void setUp() throws IOException, SchedulerException {
applicationContext = PropertiesLoaderUtils.loadProperties("classpath:application.properties");
StdSchedulerFactory factory = new StdSchedulerFactory();
factory.initialize(applicationContext);

partitionPlanService = mock(PartitionPlanService.class);
partitionPlanTaskService = mock(PartitionPlanTaskService.class);
scheduleService = mock(ScheduleService.class);

when(SpringContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
when(SpringContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
when(SpringContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);

partitionPlanJob = new PartitionPlanJob();

jobExecutionContext = mock(JobExecutionContext.class);
when(jobExecutionContext.getMergedJobDataMap().get("scheduleId")).thenReturn(1L);
}

@Test
public void testExecute() throws SchedulerException, UnExpectedException {
ScheduleEntity scheduleEntity = new ScheduleEntity();
scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\":1}");

DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
databasePartitionPlan.setConfigEnabled(true);
databasePartitionPlan.setFlowInstanceId(1L);

List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(new TablePartitionPlanEntity(), new TablePartitionPlanEntity());

when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);

partitionPlanJob.execute(jobExecutionContext);

Mockito.verify(partitionPlanTaskService, Mockito.times(1))
.executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
}

@Test
public void testExecuteWhenDatabasePartitionPlanIsDisable() throws SchedulerException, UnExpectedException {
ScheduleEntity scheduleEntity = new ScheduleEntity();
scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\":1}");

DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
databasePartitionPlan.setConfigEnabled(false);

List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(new TablePartitionPlanEntity(), new TablePartitionPlanEntity());

when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);

partitionPlanJob.execute(jobExecutionContext);

Mockito.verify(partitionPlanTaskService, Mockito.times(0))
.executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
}

@Test
public void testExecuteWhenNoTableNeedToProcess() throws SchedulerException, UnExpectedException {
ScheduleEntity scheduleEntity = new ScheduleEntity();
scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\":1}");

DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
databasePartitionPlan.setConfigEnabled(true);

List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList();

when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);

partitionPlanJob.execute(jobExecutionContext);

Mockito.verify(partitionPlanTaskService, Mockito.times(0))
.executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
}

@Test
public void testInterrupt() throws UnExpectedException {
PartitionPlanJob partitionPlanJobSpy = Mockito.spy(partitionPlanJob);

partitionPlanJobSpy.interrupt();

Mockito.verify(partitionPlanJobSpy, Mockito.times(1)).interrupt();
}
}