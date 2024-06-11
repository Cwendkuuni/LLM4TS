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
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

 private ApplicationContext applicationContext;
 private PartitionPlanService partitionPlanService;
 private PartitionPlanTaskService partitionPlanTaskService;
 private ScheduleService scheduleService;
 private JobDetail jobDetail;
 private JobDataMap jobDataMap;
 private JobExecutionContext jobExecutionContext;
 private PartitionPlanJob partitionPlanJob;
 private Scheduler scheduler;

 @Before
 public void setup() throws SchedulerException {
 applicationContext = mock(ApplicationContext.class);
 partitionPlanService = mock(PartitionPlanService.class);
 partitionPlanTaskService = mock(PartitionPlanTaskService.class);
 scheduleService = mock(ScheduleService.class);

 ReflectionTestUtils.setField(partitionPlanTaskService, "applicationContext", applicationContext);
 ReflectionTestUtils.setField(partitionPlanService, "applicationContext", applicationContext);
 ReflectionTestUtils.setField(scheduleService, "applicationContext", applicationContext);

 when(applicationContext.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
 when(applicationContext.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
 when(applicationContext.getBean(ScheduleService.class)).thenReturn(scheduleService);

 jobDataMap = new JobDataMap();
 jobExecutionContext = new JobExecutionContext(jobDetail, jobDataMap);

 Properties properties = new Properties();
 properties.setProperty("org.quartz.scheduler.instanceName", "MyScheduler");
 properties.setProperty("org.quartz.scheduler.instanceId", "AUTO");
 properties.setProperty("org.quartz.scheduler.rmi.export", "false");
 properties.setProperty("org.quartz.scheduler.rmi.proxy", "false");
 properties.setProperty("org.quartz.scheduler.wrapJobExecutionInUserTransaction", "false");

 scheduler = new StdSchedulerFactory(properties).getScheduler();
 scheduler.start();

 partitionPlanJob = new PartitionPlanJob();
 ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanTaskService", partitionPlanTaskService);
 ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanService);
 ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", scheduleService);
 }

 @Test
 public void testExecute() throws SchedulerException, JobExecutionException {
 Long scheduleId = 1L;
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(scheduleId);
 scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\": \"123\"}");
 when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setConfigEnabled(true);
 when(partitionPlanService.getDatabasePartitionPlanById(123L)).thenReturn(databasePartitionPlan);

 TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
 List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
 tablePartitionPlans.add(tablePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(123L)).thenReturn(tablePartitionPlans);

 jobDataMap.put("scheduleId", scheduleId);

 scheduler.addJob(jobDetail, true);
 scheduler.triggerJob(jobDetail.getKey());

 verify(partitionPlanTaskService, times(1))
 .executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
 }

 @Test
 public void testExecute_scheduleNotExist() throws SchedulerException, JobExecutionException {
 Long scheduleId = 1L;
 when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(null);

 jobDataMap.put("scheduleId", scheduleId);

 scheduler.addJob(jobDetail, true);
 scheduler.triggerJob(jobDetail.getKey());

 verify(partitionPlanTaskService, never()).executePartitionPlan(anyLong(), anyList());
 }

 @Test
 public void testExecute_databasePartitionPlanNotExist() throws SchedulerException, JobExecutionException {
 Long scheduleId = 1L;
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(scheduleId);
 scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\": \"123\"}");
 when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

 when(partitionPlanService.getDatabasePartitionPlanById(123L)).thenReturn(null);

 jobDataMap.put("scheduleId", scheduleId);

 scheduler.addJob(jobDetail, true);
 scheduler.triggerJob(jobDetail.getKey());

 verify(partitionPlanTaskService, never()).executePartitionPlan(anyLong(), anyList());
 }

 @Test
 public void testExecute_tablePartitionPlansEmpty() throws SchedulerException, JobExecutionException {
 Long scheduleId = 1L;
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(scheduleId);
 scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\": \"123\"}");
 when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setConfigEnabled(true);
 when(partitionPlanService.getDatabasePartitionPlanById(123L)).thenReturn(databasePartitionPlan);

 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(123L)).thenReturn(new ArrayList<>());

 jobDataMap.put("scheduleId", scheduleId);

 scheduler.addJob(jobDetail, true);
 scheduler.triggerJob(jobDetail.getKey());

 verify(partitionPlanTaskService, never()).executePartitionPlan(anyLong(), anyList());
 }

 @Test
 public void testExecute_databasePartitionPlanConfigDisabled() throws SchedulerException, JobExecutionException {
 Long scheduleId = 1L;
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(scheduleId);
 scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\": \"123\"}");
 when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setConfigEnabled(false);
 when(partitionPlanService.getDatabasePartitionPlanById(123L)).thenReturn(databasePartitionPlan);

 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(123L)).thenReturn(new ArrayList<>());

 jobDataMap.put("scheduleId", scheduleId);

 scheduler.addJob(jobDetail, true);
 scheduler.triggerJob(jobDetail.getKey());

 verify(partitionPlanTaskService, never()).executePartitionPlan(anyLong(), anyList());
 }

 @Test
 public void testInterrupt() {
 assertThrows(UnExpectedException.class, () -> partitionPlanJob.interrupt());
 }

 @Test
 public void testConstructor() {
 PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
 assertNotNull(partitionPlanJob);
 }

 @Test
 public void testGetPartitionPlanTaskService() {
 PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
 assertNotNull(partitionPlanJob.getPartitionPlanTaskService());
 }

 @Test
 public void testGetPartitionPlanService() {
 PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
 assertNotNull(partitionPlanJob.getPartitionPlanService());
 }

 @Test
 public void testGetScheduleService() {
 PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
 assertNotNull(partitionPlanJob.getScheduleService());
 }
}