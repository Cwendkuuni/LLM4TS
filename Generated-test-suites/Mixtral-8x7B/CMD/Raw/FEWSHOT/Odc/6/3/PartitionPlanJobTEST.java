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
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.model.ScheduleJobParameters;
import com.oceanbase.odc.service.schedule.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.util.SpringContextUtilTest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.QuartzJobFactory;

public class PartitionPlanJobTest {

private PartitionPlanJob partitionPlanJob;

private PartitionPlanService partitionPlanServiceMock;

private PartitionPlanTaskService partitionPlanTaskServiceMock;

private ScheduleService scheduleServiceMock;

private ScheduleEntity scheduleEntityMock;

private JobExecutionContext jobExecutionContextMock;

private static final Long SCHEDULE_ID = 1L;

private static final String JOB_PARAMETERS_JSON = "{\"databasePartitionPlanId\": 1}";

@Before
public void setup() {
partitionPlanServiceMock = mock(PartitionPlanService.class);
partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
scheduleServiceMock = mock(ScheduleService.class);
SpringContextUtilTest.replaceSpringContextUtilBean(SpringContextUtil.class,
SpringContextUtilTest.SPRING_CONTEXT_UTIL_TEST_INSTANCE);
SpringContextUtilTest.addSpringContextUtilBean(PartitionPlanService.class,
partitionPlanServiceMock);
SpringContextUtilTest.addSpringContextUtilBean(PartitionPlanTaskService.class,
partitionPlanTaskServiceMock);
SpringContextUtilTest.addSpringContextUtilBean(ScheduleService.class,
scheduleServiceMock);

partitionPlanJob = new PartitionPlanJob();

scheduleEntityMock = new ScheduleEntity();
scheduleEntityMock.setId(SCHEDULE_ID);
scheduleEntityMock.setJobParametersJson(JOB_PARAMETERS_JSON);

jobExecutionContextMock = mock(JobExecutionContext.class);
when(jobExecutionContextMock.getMergedJobDataMap()
.getString(ScheduleTaskUtils.SCHEDULE_ID))
.thenReturn(SCHEDULE_ID.toString());
when(jobExecutionContextMock.getMergedJobDataMap()
.getString(ScheduleTaskUtils.JOB_PARAMETERS_JSON))
.thenReturn(JOB_PARAMETERS_JSON);
}

@Test
public void testExecute() throws SchedulerException {
PartitionPlanJobParameters partitionPlanJobParameters =
JsonUtils.fromJson(JOB_PARAMETERS_JSON, PartitionPlanJobParameters.class);
DatabasePartitionPlanEntity databasePartitionPlan =
new DatabasePartitionPlanEntity();
databasePartitionPlan.setConfigEnabled(false);
List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
tablePartitionPlans.add(new TablePartitionPlanEntity());

when(scheduleServiceMock.nullSafeGetById(SCHEDULE_ID))
.thenReturn(scheduleEntityMock);
when(partitionPlanServiceMock.getDatabasePartitionPlanById(
partitionPlanJobParameters.getDatabasePartitionPlanId()))
.thenReturn(databasePartitionPlan);
when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(
partitionPlanJobParameters.getDatabasePartitionPlanId()))
.thenReturn(tablePartitionPlans);

partitionPlanJob.execute(jobExecutionContextMock);

// Verify that the schedule is terminated if the database partition plan is disabled or there are no table partition
// plans
verifyScheduleTermination(SCHEDULE_ID);
}

@Test
public void testExecuteWithException() throws SchedulerException {
PartitionPlanJobParameters partitionPlanJobParameters =
JsonUtils.fromJson(JOB_PARAMETERS_JSON, PartitionPlanJobParameters.class);
DatabasePartitionPlanEntity databasePartitionPlan =
new DatabasePartitionPlanEntity();
databasePartitionPlan.setConfigEnabled(true);
List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
tablePartitionPlans.add(new TablePartitionPlanEntity());

when(scheduleServiceMock.nullSafeGetById(SCHEDULE_ID))
.thenReturn(scheduleEntityMock);
when(partitionPlanServiceMock.getDatabasePartitionPlanById(
partitionPlanJobParameters.getDatabasePartitionPlanId()))
.thenReturn(databasePartitionPlan);
when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(
partitionPlanJobParameters.getDatabasePartitionPlanId()))
.thenReturn(tablePartitionPlans);
when(partitionPlanTaskServiceMock.executePartitionPlan(
databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans))
.thenThrow(new RuntimeException("Create partition-plan database change task failed."));

partitionPlanJob.execute(jobExecutionContextMock);

// Verify that the schedule is not terminated if there is an exception while creating the partition-plan database
// change task
verifyScheduleNotTermination(SCHEDULE_ID);
}

@Test
public void testInterrupt() {
// Ensure that interrupt() throws an UnExpectedException
assertThrows(UnExpectedException.class, () -> partitionPlanJob.interrupt());
}

private void verifyScheduleTermination(Long scheduleId) throws SchedulerException {
verify(scheduleServiceMock, times(1))
.terminate(scheduleEntityMock);
}

private void verifyScheduleNotTermination(Long scheduleId) {
verify(scheduleServiceMock, never())
.terminate(scheduleEntityMock);
}
}