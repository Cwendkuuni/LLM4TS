import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.impl.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskServiceImpl;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleEntity;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
import com.oceanbase.odc.service.schedule.job.PartitionPlanJobParameters;

@SuppressWarnings("unchecked")
public class PartitionPlanJobTest extends QuartzJobBean {

 private PartitionPlanJob partitionPlanJob;

 private PartitionPlanTaskService partitionPlanTaskService;

 private PartitionPlanService partitionPlanService;

 private ScheduleService scheduleService;

 private JobExecutionContext jobExecutionContext;

 @Before
 public void setup() {
 partitionPlanTaskService = mock(PartitionPlanTaskServiceImpl.class);
 partitionPlanService = mock(PartitionPlanService.class);
 scheduleService = mock(ScheduleService.class);

 partitionPlanJob = new PartitionPlanJob();

 SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskService);
 SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanService);
 SpringContextUtil.setBean(ScheduleService.class, scheduleService);

 jobExecutionContext = mock(JobExecutionContext.class);
 }

 @Test
 public void testExecute_ScheduleNotFound() throws JobExecutionException, SchedulerException {
 ScheduleTaskUtils.setScheduleId(jobExecutionContext, 1L);

 when(scheduleService.nullSafeGetById(1L)).thenReturn(null);

 partitionPlanJob.execute(jobExecutionContext);

 verify(scheduleService, times(1)).nullSafeGetById(1L);
 verify(log, times(1))
 .warn("Schedule not found,scheduleId={}", 1L);
 verifyNoMoreInteractions(partitionPlanTaskService, scheduleService);
 }

 @Test
 public void testExecute_DatabasePartitionPlanNotFound() throws JobExecutionException, SchedulerException {
 ScheduleTaskUtils.setScheduleId(jobExecutionContext, 1L);

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\": 1}");

 when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
 when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(null);

 partitionPlanJob.execute(jobExecutionContext);

 verify(scheduleService, times(1)).nullSafeGetById(1L);
 verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(1L);
 verify(log, times(1))
 .warn("Database partition plan not found,databasePartitionPlanId={}", 1);
 verifyNoMoreInteractions(partitionPlanTaskService, scheduleService);
 }

 @Test
 public void testExecute_NoTablePartitionPlans() throws JobExecutionException, SchedulerException {
 ScheduleTaskUtils.setScheduleId(jobExecutionContext, 1L);

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\": 1}");

 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setConfigEnabled(true);

 when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
 when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);

 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(new ArrayList<>());

 partitionPlanJob.execute(jobExecutionContext);

 verify(scheduleService, times(1)).nullSafeGetById(1L);
 verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(1L);
 verify(partitionPlanService, times(1))
 .getValidTablePlanByDatabasePartitionPlanId(1L);
 verify(log, times(1))
 .info("database partition plan is disable or no table need to process,start to stop partition-plan job,scheduleId={}",
 1);

 verify(scheduleService, times(1)).terminate(scheduleEntity);
 verifyNoMoreInteractions(partitionPlanTaskService, scheduleService);
 }

 @Test
 public void testExecute() throws JobExecutionException, SchedulerException {
 ScheduleTaskUtils.setScheduleId(jobExecutionContext, 1L);

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\": 1}");

 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setConfigEnabled(true);

 List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
 tablePartitionPlans.add(new TablePartitionPlanEntity());

 when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
 when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);

 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

 partitionPlanJob.execute(jobExecutionContext);

 verify(scheduleService, times(1)).nullSafeGetById(1L);
 verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(1L);
 verify(partitionPlanService, times(1))
 .getValidTablePlanByDatabasePartitionPlanId(1L);
 verify(partitionPlanTaskService, times(1)).executePartitionPlan(1L, tablePartitionPlans);
 verifyNoMoreInteractions(partitionPlanTaskService, scheduleService);
 }

 @Test(expected = UnExpectedException.class)
 public void testInterrupt() throws UnExpectedException {
 partitionPlanJob.interrupt();
 }
}