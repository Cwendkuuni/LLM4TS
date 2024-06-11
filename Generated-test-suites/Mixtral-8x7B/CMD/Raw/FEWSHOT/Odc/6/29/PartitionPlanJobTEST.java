import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class PartitionPlanJobTest {

private PartitionPlanService partitionPlanService;
private PartitionPlanTaskService partitionPlanTaskService;
private ScheduleService scheduleService;
private JobExecutionContext jobExecutionContext;
private ApplicationContext applicationContext;
private PartitionPlanJob partitionPlanJob;
private ScheduleEntity scheduleEntity;
private PartitionPlanJobParameters jobParameters;
private DatabasePartitionPlanEntity databasePartitionPlan;
private List<TablePartitionPlanEntity> tablePartitionPlans;

@Before
public void setUp() {
applicationContext = new AnnotationConfigApplicationContext();
partitionPlanService = mock(PartitionPlanService.class);
partitionPlanTaskService = mock(PartitionPlanTaskService.class);
scheduleService = mock(ScheduleService.class);
jobExecutionContext = mock(JobExecutionContext.class);
applicationContext.register(PartitionPlanJob.class);
applicationContext.refresh();
partitionPlanJob = applicationContext.getBean(PartitionPlanJob.class);
scheduleEntity = new ScheduleEntity();
scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\": 1}");
jobParameters = new PartitionPlanJobParameters();
jobParameters.setDatabasePartitionPlanId(1L);
databasePartitionPlan = new DatabasePartitionPlanEntity();
databasePartitionPlan.setConfigEnabled(true);
databasePartitionPlan.setFlowInstanceId("123");
tablePartitionPlans = new ArrayList<>();
tablePartitionPlans.add(new TablePartitionPlanEntity());
}

@Test
public void testExecute() throws Exception {
when(ScheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(1L);
when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class))
.thenReturn(jobParameters);
when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
when(partitionPlanService
.getValidTablePlanByDatabasePartitionPlanId(1L))
.thenReturn(tablePartitionPlans);
doNothing().when(partitionPlanTaskService)
.executePartitionPlan(anyString(), anyList());
partitionPlanJob.execute(jobExecutionContext);
}

@Test
public void testExecuteWithScheduleNotFound() throws Exception {
when(ScheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(1L);
when(scheduleService.nullSafeGetById(1L)).thenThrow(new Exception());
partitionPlanJob.execute(jobExecutionContext);
}

@Test
public void testExecuteWithDatabasePartitionPlanDisabled() throws Exception {
when(ScheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(1L);
when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class))
.thenReturn(jobParameters);
databasePartitionPlan.setConfigEnabled(false);
when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
when(partitionPlanService
.getValidTablePlanByDatabasePartitionPlanId(1L))
.thenReturn(tablePartitionPlans);
partitionPlanJob.execute(jobExecutionContext);
}

@Test
public void testExecuteWithNoTablePartitionPlans() throws Exception {
when(ScheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(1L);
when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class))
.thenReturn(jobParameters);
databasePartitionPlan.setConfigEnabled(true);
when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
when(partitionPlanService
.getValidTablePlanByDatabasePartitionPlanId(1L))
.thenReturn(new ArrayList<>());
partitionPlanJob.execute(jobExecutionContext);
}

@Test
public void testExecuteWithException() throws Exception {
when(ScheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(1L);
when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class))
.thenReturn(jobParameters);
databasePartitionPlan.setConfigEnabled(true);
when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
when(partitionPlanService
.getValidTablePlanByDatabasePartitionPlanId(1L))
.thenReturn(tablePartitionPlans);
when(partitionPlanTaskService.executePartitionPlan(anyString(), anyList()))
.thenThrow(new Exception());
partitionPlanJob.execute(jobExecutionContext);
}

@Test
public void testInterrupt() {
partitionPlanJob.interrupt();
}
}