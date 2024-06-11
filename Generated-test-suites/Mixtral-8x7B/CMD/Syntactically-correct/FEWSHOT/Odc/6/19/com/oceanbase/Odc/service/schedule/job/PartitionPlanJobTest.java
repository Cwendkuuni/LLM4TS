import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleEntity;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

@org.junit.runner.RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

@Autowired
private ApplicationContext applicationContext;

private PartitionPlanJob partitionPlanJob;

@Before
public void setUp() {
partitionPlanJob = new PartitionPlanJob();
ReflectionTestUtils.setField(partitionPlanJob, "applicationContext", applicationContext);
}

@Test
public void testExecute() throws Exception {
JobExecutionContext context = mock(JobExecutionContext.class);
Long scheduleId = 1L;
ScheduleEntity scheduleEntity = new ScheduleEntity();
scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\": \"1\"}");
when(context.getMergedJobDataMap().get("scheduleId")).thenReturn(scheduleId);
when(applicationContext.getBean(ScheduleService.class)).thenReturn(mock(ScheduleService.class));
when(applicationContext.getBean(PartitionPlanService.class))
.thenReturn(mock(PartitionPlanService.class));
when(applicationContext.getBean(PartitionPlanTaskService.class))
.thenReturn(mock(PartitionPlanTaskService.class));
when(applicationContext.getBean(SpringContextUtil.class)).thenReturn(mock(SpringContextUtil.class));
PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
jobParameters.setDatabasePartitionPlanId(1L);
DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
databasePartitionPlan.setFlowInstanceId("1");
databasePartitionPlan.setConfigEnabled(true);
List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
tablePartitionPlans.add(tablePartitionPlan);
when(applicationContext.getBean(ScheduleService.class).nullSafeGetById(scheduleId))
.thenReturn(scheduleEntity);
when(applicationContext.getBean(PartitionPlanService.class)
.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
.thenReturn(databasePartitionPlan);
when(applicationContext.getBean(PartitionPlanService.class)
.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
.thenReturn(tablePartitionPlans);
partitionPlanJob.execute(context);
verify(applicationContext.getBean(PartitionPlanTaskService.class)
.executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans), times(1));
}

@Test
public void testExecuteWhenScheduleNotFound() throws Exception {
JobExecutionContext context = mock(JobExecutionContext.class);
Long scheduleId = 1L;
when(context.getMergedJobDataMap().get("scheduleId")).thenReturn(scheduleId);
when(applicationContext.getBean(ScheduleService.class)).thenReturn(mock(ScheduleService.class));
when(applicationContext.getBean(PartitionPlanService.class))
.thenReturn(mock(PartitionPlanService.class));
when(applicationContext.getBean(PartitionPlanTaskService.class))
.thenReturn(mock(PartitionPlanTaskService.class));
when(applicationContext.getBean(SpringContextUtil.class)).thenReturn(mock(SpringContextUtil.class));
when(applicationContext.getBean(ScheduleService.class).nullSafeGetById(scheduleId))
.thenThrow(new SchedulerException("Schedule not found"));
partitionPlanJob.execute(context);
}

@Test
public void testExecuteWhenPartitionPlanIsNotConfigured() throws Exception {
JobExecutionContext context = mock(JobExecutionContext.class);
Long scheduleId = 1L;
ScheduleEntity scheduleEntity = new ScheduleEntity();
scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\": \"1\"}");
when(context.getMergedJobDataMap().get("scheduleId")).thenReturn(scheduleId);
when(applicationContext.getBean(ScheduleService.class)).thenReturn(mock(ScheduleService.class));
when(applicationContext.getBean(PartitionPlanService.class))
.thenReturn(mock(PartitionPlanService.class));
when(applicationContext.getBean(PartitionPlanTaskService.class))
.thenReturn(mock(PartitionPlanTaskService.class));
when(applicationContext.getBean(SpringContextUtil.class)).thenReturn(mock(SpringContextUtil.class));
PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
jobParameters.setDatabasePartitionPlanId(1L);
DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
databasePartitionPlan.setFlowInstanceId("1");
databasePartitionPlan.setConfigEnabled(false);
List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
tablePartitionPlans.add(tablePartitionPlan);
when(applicationContext.getBean(ScheduleService.class).nullSafeGetById(scheduleId))
.thenReturn(scheduleEntity);
when(applicationContext.getBean(PartitionPlanService.class)
.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
.thenReturn(databasePartitionPlan);
when(applicationContext.getBean(PartitionPlanService.class)
.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
.thenReturn(tablePartitionPlans);
partitionPlanJob.execute(context);
verify(applicationContext.getBean(PartitionPlanTaskService.class)
.executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans), never());
}

@Test
public void testExecuteWhenNoTablePartitionPlan() throws Exception {
JobExecutionContext context = mock(JobExecutionContext.class);
Long scheduleId = 1L;
ScheduleEntity scheduleEntity = new ScheduleEntity();
scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\": \"1\"}");
when(context.getMergedJobDataMap().get("scheduleId")).thenReturn(scheduleId);
when(applicationContext.getBean(ScheduleService.class)).thenReturn(mock(ScheduleService.class));
when(applicationContext.getBean(PartitionPlanService.class))
.thenReturn(mock(PartitionPlanService.class));
when(applicationContext.getBean(PartitionPlanTaskService.class))
.thenReturn(mock(PartitionPlanTaskService.class));
when(applicationContext.getBean(SpringContextUtil.class)).thenReturn(mock(SpringContextUtil.class));
PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
jobParameters.setDatabasePartitionPlanId(1L);
DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
databasePartitionPlan.setFlowInstanceId("1");
databasePartitionPlan.setConfigEnabled(true);
List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
when(applicationContext.getBean(ScheduleService.class).nullSafeGetById(scheduleId))
.thenReturn(scheduleEntity);
when(applicationContext.getBean(PartitionPlanService.class)
.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
.thenReturn(databasePartitionPlan);
when(applicationContext.getBean(PartitionPlanService.class)
.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
.thenReturn(tablePartitionPlans);
partitionPlanJob.execute(context);
verify(applicationContext.getBean(PartitionPlanTaskService.class)
.executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans), never());
}

@Test(expected = UnExpectedException.class)
public void testInterrupt() throws Exception {
partitionPlanJob.interrupt();
}
}