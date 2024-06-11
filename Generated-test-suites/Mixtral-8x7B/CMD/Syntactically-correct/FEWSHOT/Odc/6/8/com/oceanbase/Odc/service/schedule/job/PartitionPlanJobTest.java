import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

@SuppressWarnings("unchecked")
public class PartitionPlanJobTest {

 private PartitionPlanJob partitionPlanJob;

 private PartitionPlanService partitionPlanService;

 private PartitionPlanTaskService partitionPlanTaskService;

 private ScheduleService scheduleService;

 private ScheduleEntity scheduleEntity;

 private ApplicationContext applicationContext;

 private JobExecutionContext jobExecutionContext;

 @Before
 public void setup() {
 partitionPlanService = mock(PartitionPlanService.class);
 partitionPlanTaskService = mock(PartitionPlanTaskService.class);
 scheduleService = mock(ScheduleService.class);
 applicationContext = mock(ApplicationContext.class);
 jobExecutionContext = mock(JobExecutionContext.class);

 when(SpringContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
 when(SpringContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
 when(SpringContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);
 when(SpringContextUtil.getBean(ApplicationContext.class)).thenReturn(applicationContext);
 when(jobExecutionContext.getMergedJobDataMap().get(QuartzJobBean.MERGED_JOB_DATA_MAP_KEY)).thenReturn(
 null);

 partitionPlanJob = new PartitionPlanJob();
 }

 @Test
 public void testExecuteScheduleEntityFound() throws Exception {
 Long scheduleId = 1L;
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setConfigEnabled(true);
 List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
 tablePartitionPlans.add(new TablePartitionPlanEntity());
 tablePartitionPlans.add(new TablePartitionPlanEntity());
 when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
 when(JsonUtils.fromJson(anyString(), any(Class.class))).thenReturn(new PartitionPlanJobParameters());
 when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
 .thenReturn(tablePartitionPlans);

 partitionPlanJob.execute(jobExecutionContext);

 // Check if scheduleEntity is passed to partitionPlanTaskService.executePartitionPlan
 doNothing().when(partitionPlanTaskService).executePartitionPlan(anyString(), anyList());
 partitionPlanJob.execute(jobExecutionContext);
 verify(partitionPlanTaskService).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(),
 tablePartitionPlans);
 }

 @Test
 public void testExecuteScheduleEntityNotFound() throws Exception {
 Long scheduleId = 1L;
 when(scheduleService.nullSafeGetById(anyLong())).thenReturn(null);

 partitionPlanJob.execute(jobExecutionContext);

 verify(log).warn("Schedule not found,scheduleId={}", scheduleId);
 }

 @Test
 public void testExecuteDatabasePartitionPlanDisabled() throws Exception {
 Long scheduleId = 1L;
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setConfigEnabled(false);
 List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
 tablePartitionPlans.add(new TablePartitionPlanEntity());
 tablePartitionPlans.add(new TablePartitionPlanEntity());
 when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
 when(JsonUtils.fromJson(anyString(), any(Class.class))).thenReturn(new PartitionPlanJobParameters());
 when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
 .thenReturn(tablePartitionPlans);

 partitionPlanJob.execute(jobExecutionContext);

 verify(scheduleService).terminate(scheduleEntity);
 }

 @Test
 public void testExecuteNoTablePartitionPlans() throws Exception {
 Long scheduleId = 1L;
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setConfigEnabled(true);
 List<TablePartitionPlanEntity> tablePartitionPlans = Collections.emptyList();
 when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
 when(JsonUtils.fromJson(anyString(), any(Class.class))).thenReturn(new PartitionPlanJobParameters());
 when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
 .thenReturn(tablePartitionPlans);

 partitionPlanJob.execute(jobExecutionContext);

 verify(scheduleService).terminate(scheduleEntity);
 }

 @Test
 public void testExecuteScheduleEntityTerminate() throws Exception {
 Long scheduleId = 1L;
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setConfigEnabled(true);
 List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
 tablePartitionPlans.add(new TablePartitionPlanEntity());
 tablePartitionPlans.add(new TablePartitionPlanEntity());
 when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
 when(JsonUtils.fromJson(anyString(), any(Class.class))).thenReturn(new PartitionPlanJobParameters());
 when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
 .thenReturn(tablePartitionPlans);
 doThrow(new SchedulerException("")).when(scheduleService).terminate(scheduleEntity);

 partitionPlanJob.execute(jobExecutionContext);

 verify(log).warn("Stop partition plan job failed.", any(SchedulerException.class));
 }

 @Test
 public void testExecuteJsonUtilsFromJsonThrowsException() throws Exception {
 Long scheduleId = 1L;
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setConfigEnabled(true);
 List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
 tablePartitionPlans.add(new TablePartitionPlanEntity());
 tablePartitionPlans.add(new TablePartitionPlanEntity());
 when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
 when(JsonUtils.fromJson(anyString(), any(Class.class))).thenThrow(new RuntimeException(""));
 when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
 .thenReturn(tablePartitionPlans);

 partitionPlanJob.execute(jobExecutionContext);

 verify(log).error("Parse PartitionPlanJobParameters from json failed.", any(RuntimeException.class));
 }

 @Test
 public void testExecutePartitionPlanTaskServiceThrowsException() throws Exception {
 Long scheduleId = 1L;
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setConfigEnabled(true);
 List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
 tablePartitionPlans.add(new TablePartitionPlanEntity());
 tablePartitionPlans.add(new TablePartitionPlanEntity());
 when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
 when(JsonUtils.fromJson(anyString(), any(Class.class))).thenReturn(new PartitionPlanJobParameters());
 when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
 .thenReturn(tablePartitionPlans);
 doThrow(new RuntimeException("")).when(partitionPlanTaskService).executePartitionPlan(anyString(), anyList());

 partitionPlanJob.execute(jobExecutionContext);

 verify(log).warn("Create partition-plan database change task failed.", any(RuntimeException.class));
 }

 @Test
 public void testInterruptThrowsUnExpectedException() {
 doThrow(new UnExpectedException()).when(partitionPlanJob).interrupt();

 assertThrows(UnExpectedException.class, () -> partitionPlanJob.interrupt());
 }

 @Test
 public void testInterruptNoException() {
 doNothing().when(partitionPlanJob).interrupt();

 partitionPlanJob.interrupt();
 }

 @Test
 public void testGetPartitionPlanService() {
 PartitionPlanService result = partitionPlanJob.getPartitionPlanService();

 assertNotNull(result);
 }

 @Test
 public void testGetPartitionPlanTaskService() {
 PartitionPlanTaskService result = partitionPlanJob.getPartitionPlanTaskService();

 assertNotNull(result);
 }

 @Test
 public void testGetScheduleService() {
 ScheduleService result = partitionPlanJob.getScheduleService();

 assertNotNull(result);
 }

 @Test
 public void testToString() {
 String result = partitionPlanJob.toString();

 assertFalse(result.isEmpty());
 }

 @Test
 public void testGetScheduleId() {
 when(ScheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(1L);

 Long result = partitionPlanJob.getScheduleId();

 assertEquals(result, 1L);
 }

 @Test
 public void testPartitionPlanJobParametersFromJson() throws Exception {
 String json = "{\"databasePartitionPlanId\": 1}";
 PartitionPlanJobParameters result =
 JsonUtils.fromJson(json, new TypeReference<PartitionPlanJobParameters>() {});

 assertNotNull(result);
 assertEquals(result.getDatabasePartitionPlanId(), 1L);
 }

 @Test
 public void testPartitionPlanJobParametersFromJsonNull() throws Exception {
 String json = null;
 PartitionPlanJobParameters result =
 JsonUtils.fromJson(json, new TypeReference<PartitionPlanJobParameters>() {});

 assertNull(result);
 }

 @Test
 public void testPartitionPlanJobParametersFromJsonEmpty() throws Exception {
 String json = "";
 PartitionPlanJobParameters result =
 JsonUtils.fromJson(json, new TypeReference<PartitionPlanJobParameters>() {});

 assertNull(result);
 }
}