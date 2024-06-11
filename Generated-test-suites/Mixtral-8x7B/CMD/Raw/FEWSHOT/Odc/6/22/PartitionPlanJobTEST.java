import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PartitionPlanJobTest {

private PartitionPlanJob partitionPlanJob;

@Before
public void setUp() {
PartitionPlanService partitionPlanService = mock(PartitionPlanService.class);
PartitionPlanTaskService partitionPlanTaskService = mock(PartitionPlanTaskService.class);
ScheduleService scheduleService = mock(ScheduleService.class);

partitionPlanJob = new PartitionPlanJob();
ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanService);
ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanTaskService", partitionPlanTaskService);
ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", scheduleService);
}

@Test
public void testExecute() throws Exception {
Long scheduleId = 1L;
ScheduleEntity scheduleEntity = new ScheduleEntity();
scheduleEntity.setId(scheduleId);
scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\": 2}");

PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
jobParameters.setDatabasePartitionPlanId(2L);

DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
databasePartitionPlan.setConfigEnabled(true);

List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
tablePartitionPlans.add(tablePartitionPlan);

when(partitionPlanService.getDatabasePartitionPlanById(2L)).thenReturn(databasePartitionPlan);
when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(2L)).thenReturn(tablePartitionPlans);
when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

partitionPlanJob.execute(mock(JobExecutionContext.class));

Assert.assertEquals(1, scheduleService.getSchedules(GroupMatcher.groupContains("partitionPlan")).size());
}

@Test
public void testExecuteWithJobParametersException() throws Exception {
Long scheduleId = 1L;
ScheduleEntity scheduleEntity = new ScheduleEntity();
scheduleEntity.setId(scheduleId);
scheduleEntity.setJobParametersJson("invalid_json");

when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

partitionPlanJob.execute(mock(JobExecutionContext.class));

Assert.assertEquals(0, scheduleService.getSchedules(GroupMatcher.groupContains("partitionPlan")).size());
}

@Test
public void testExecuteWithNullSchedule() throws Exception {
Long scheduleId = 1L;
when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(null);

partitionPlanJob.execute(mock(JobExecutionContext.class));

Assert.assertEquals(0, scheduleService.getSchedules(GroupMatcher.groupContains("partitionPlan")).size());
}

@Test
public void testExecuteWithPartitionPlanServiceException() throws Exception {
Long scheduleId = 1L;
ScheduleEntity scheduleEntity = new ScheduleEntity();
scheduleEntity.setId(scheduleId);
scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\": 2}");

PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
jobParameters.setDatabasePartitionPlanId(2L);

DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
databasePartitionPlan.setConfigEnabled(true);

List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
TablePartitionPlanEntity tablePartitionPlan = new
TablePartitionPlanEntity();
tablePartitionPlans.add(tablePartitionPlan);

when(partitionPlanService.getDatabasePartitionPlanById(2L)).thenThrow(new Exception("test exception"));
when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

partitionPlanJob.execute(mock(JobExecutionContext.class));

Assert.assertEquals(0, scheduleService.getSchedules(GroupMatcher.groupContains("partitionPlan")).size());
}

@Test
public void testExecuteWithPartitionPlanTaskServiceException() throws Exception {
Long scheduleId = 1L;
ScheduleEntity scheduleEntity = new ScheduleEntity();
scheduleEntity.setId(scheduleId);
scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\": 2}");

PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
jobParameters.setDatabasePartitionPlanId(2L);

DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
databasePartitionPlan.setConfigEnabled(true);

List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
tablePartitionPlans.add(tablePartitionPlan);

when(partitionPlanService.getDatabasePartitionPlanById(2L)).thenReturn(databasePartitionPlan);
when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(2L)).thenReturn(tablePartitionPlans);
when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
when(partitionPlanTaskService.executePartitionPlan(1L, tablePartitionPlans)).thenThrow(new Exception("test exception"));

partitionPlanJob.execute(mock(JobExecutionContext.class));

Assert.assertEquals(0, scheduleService.getSchedules(GroupMatcher.groupContains("partitionPlan")).size());
}

@Test
public void testInterrupt() throws SchedulerException, UnExpectedException {
partitionPlanJob.interrupt();

Assert.assertEquals(0, scheduleService.getSchedules(GroupMatcher.groupContains("partitionPlan")).size());
}
}