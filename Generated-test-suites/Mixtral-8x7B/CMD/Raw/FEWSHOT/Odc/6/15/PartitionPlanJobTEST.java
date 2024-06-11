import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;

@org.springframework.test.context.junit4.SpringJUnit4ClassRunner
public class PartitionPlanJobTest {

 private PartitionPlanJob partitionPlanJob;

 @Autowired
 private ScheduleService scheduleService;

 @Autowired
 private PartitionPlanService partitionPlanService;

 @Autowired
 private PartitionPlanTaskService partitionPlanTaskService;

 @Autowired
 private ScheduleTaskUtils scheduleTaskUtils;

 @Autowired
 private SpringContextUtil springContextUtil;

 @Before
 public void setup() {
 partitionPlanJob = new PartitionPlanJob();
 ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", scheduleService);
 ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanService);
 ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanTaskService", partitionPlanTaskService);
 ReflectionTestUtils.setField(partitionPlanJob, "scheduleTaskUtils", scheduleTaskUtils);
 ReflectionTestUtils.setField(partitionPlanJob, "springContextUtil", springContextUtil);
 }

 @Test
 public void testExecute() throws SchedulerException, JsonProcessingException {
 JobExecutionContext context = mock(JobExecutionContext.class);
 when(context.getMergedJobDataMap().get(ScheduleTaskUtils.SCHEDULE_ID)).thenReturn(1L);
 when(scheduleService.nullSafeGetById(anyLong())).thenReturn(new ScheduleEntity());
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(1L);
 when(springContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);
 when(springContextUtil.getBean(PartitionPlanService.class))
 .thenReturn(partitionPlanService);
 when(springContextUtil.getBean(PartitionPlanTaskService.class))
 .thenReturn(partitionPlanTaskService);
 when(springContextUtil.getBean(ScheduleTaskUtils.class)).thenReturn(scheduleTaskUtils);
 when(JsonUtils.fromJson(anyString(), any()))
 .thenReturn(jobParameters);
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setConfigEnabled(true);
 when(partitionPlanService.getDatabasePartitionPlanById(anyLong()))
 .thenReturn(databasePartitionPlan);
 List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
 TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
 tablePartitionPlan.setTableId(1L);
 tablePartitionPlans.add(tablePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
 .thenReturn(tablePartitionPlans);
 partitionPlanJob.execute(context);
 verify(partitionPlanTaskService, times(1))
 .executePartitionPlan(anyString(), anyList());
 }

 @Test
 public void testExecuteScheduleNotFound() throws SchedulerException, JsonProcessingException {
 JobExecutionContext context = mock(JobExecutionContext.class);
 when(context.getMergedJobDataMap().get(ScheduleTaskUtils.SCHEDULE_ID)).thenReturn(1L);
 when(scheduleService.nullSafeGetById(anyLong())).thenReturn(null);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(1L);
 when(springContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);
 when(springContextUtil.getBean(PartitionPlanService.class))
 .thenReturn(partitionPlanService);
 when(springContextUtil.getBean(PartitionPlanTaskService.class))
 .thenReturn(partitionPlanTaskService);
 when(springContextUtil.getBean(ScheduleTaskUtils.class)).thenReturn(scheduleTaskUtils);
 when(JsonUtils.fromJson(anyString(), any()))
 .thenReturn(jobParameters);
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setConfigEnabled(true);
 when(partitionPlanService.getDatabasePartitionPlanById(anyLong()))
 .thenReturn(databasePartitionPlan);
 List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
 TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
 tablePartitionPlan.setTableId(1L);
 tablePartitionPlans.add(tablePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
 .thenReturn(tablePartitionPlans);
 partitionPlanJob.execute(context);
 verify(partitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());
 }

 @Test
 public void testExecuteDatabasePartitionPlanDisable()
 throws SchedulerException, JsonProcessingException {
 JobExecutionContext context = mock(JobExecutionContext.class);
 when(context.getMergedJobDataMap().get(ScheduleTaskUtils.SCHEDULE_ID)).thenReturn(1L);
 when(scheduleService.nullSafeGetById(anyLong())).thenReturn(new ScheduleEntity());
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(1L);
 when(springContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);
 when(springContextUtil.getBean(PartitionPlanService.class))
 .thenReturn(partitionPlanService);
 when(springContextUtil.getBean(PartitionPlanTaskService.class))
 .thenReturn(partitionPlanTaskService);
 when(springContextUtil.getBean(ScheduleTaskUtils.class)).thenReturn(scheduleTaskUtils);
 when(JsonUtils.fromJson(anyString(), any()))
 .thenReturn(jobParameters);
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setConfigEnabled(false);
 when(partitionPlanService.getDatabasePartitionPlanById(anyLong()))
 .thenReturn(databasePartitionPlan);
 List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
 TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
 tablePartitionPlan.setTableId(1L);
 tablePartitionPlans.add(tablePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
 .thenReturn(tablePartitionPlans);
 partitionPlanJob.execute(context);
 verify(partitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());
 }

 @Test
 public void testExecuteNoTablePartitionPlan()
 throws SchedulerException, JsonProcessingException {
 JobExecutionContext context = mock(JobExecutionContext.class);
 when(context.getMergedJobDataMap().get(ScheduleTaskUtils.SCHEDULE_ID)).thenReturn(1L);
 when(scheduleService.nullSafeGetById(anyLong())).thenReturn(new ScheduleEntity());
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(1L);
 when(springContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);
 when(springContextUtil.getBean(PartitionPlanService.class))
 .thenReturn(partitionPlanService);
 when(springContextUtil.getBean(PartitionPlanTaskService.class))
 .thenReturn(partitionPlanTaskService);
 when(springContextUtil.getBean(ScheduleTaskUtils.class)).thenReturn(scheduleTaskUtils);
 when(JsonUtils.fromJson(anyString(), any()))
 .thenReturn(jobParameters);
 DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
 databasePartitionPlan.setConfigEnabled(true);
 when(partitionPlanService.getDatabasePartitionPlanById(anyLong()))
 .thenReturn(databasePartitionPlan);
 List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
 .thenReturn(tablePartitionPlans);
 partitionPlanJob.execute(context);
 verify(partitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());
 }

 @Test
 public void testInterrupt() {
 PartitionPlanJob partitionPlanJobSpy = spy(partitionPlanJob);
 doThrow(new UnExpectedException()).when(partitionPlanJobSpy).interrupt();
 partitionPlanJobSpy.interrupt();
 verify(partitionPlanJobSpy, times(1)).interrupt();
 }

 @Test
 public void testGetGroupNames() throws SchedulerException {
 when(scheduleTaskUtils.getScheduler().getJobGroupNames()).thenReturn(new String[] { "group1" });
 assertEquals(1, partitionPlanJob.getGroupNames().size());
 assertTrue(partitionPlanJob.getGroupNames().contains("group1"));
 }

 @Test
 public void testGetJobNames() throws SchedulerException {
 String groupName = "group1";
 when(scheduleTaskUtils.getScheduler().getJobNames(GroupMatcher.jobGroupEquals(groupName)))
 .thenReturn(new String[] { "job1" });
 assertEquals(1, partitionPlanJob.getJobNames(groupName).size());
 assertTrue(partitionPlanJob.getJobNames(groupName).contains("job1"));
 }

 @Test
 public void testDeleteJobs() throws SchedulerException {
 String groupName = "group1";
 doNothing().when(scheduleTaskUtils.getScheduler())
 .deleteJobs(GroupMatcher.jobGroupEquals(groupName));
 partitionPlanJob.deleteJobs(groupName);
 verify(scheduleTaskUtils.getScheduler(), times(1))
 .deleteJobs(GroupMatcher.jobGroupEquals(groupName));
 }

}