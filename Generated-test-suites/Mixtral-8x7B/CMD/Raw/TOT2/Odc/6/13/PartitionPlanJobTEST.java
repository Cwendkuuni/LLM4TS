import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

@Mock
private PartitionPlanTaskService partitionPlanTaskService;

@Mock
private PartitionPlanService partitionPlanService;

@Mock
private ScheduleService scheduleService;

@Mock
private ScheduleTaskUtils scheduleTaskUtils;

@Mock
private SpringContextUtil springContextUtil;

@Mock
private JobExecutionContext jobExecutionContext;

@Mock
private ScheduleEntity scheduleEntity;

@Mock
private PartitionPlanJobParameters jobParameters;

@Mock
private DatabasePartitionPlanEntity databasePartitionPlan;

@InjectMocks
private PartitionPlanJob partitionPlanJob;

private List<TablePartitionPlanEntity> tablePartitionPlans;

@Before
public void setUp() {
tablePartitionPlans = new ArrayList<>();
TablePartitionPlanEntity tablePartitionPlanEntity = new TablePartitionPlanEntity();
tablePartitionPlans.add(tablePartitionPlanEntity);
}

@Test
public void testExecute_whenScheduleEntityIsNull_thenLogWarningAndReturn() {
when(scheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(1L);
when(scheduleService.nullSafeGetById(anyLong())).thenReturn(null);

partitionPlanJob.execute(jobExecutionContext);
}

@Test
public void testExecute_whenDatabasePartitionPlanIsDisableOrTablePartitionPlansIsEmpty_thenStopPartitionPlanJob() throws SchedulerException {
when(scheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(1L);
when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
when(jobEntity.getMergedJobDataMap().get("jobParameters")).thenReturn(jobParameters);
when(jobParameters.getDatabasePartitionPlanId()).thenReturn(1L);
when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
when(databasePartitionPlan.isConfigEnabled()).thenReturn(false);
when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

partitionPlanJob.execute(jobExecutionContext);

when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(new ArrayList<>());

partitionPlanJob.execute(jobExecutionContext);
}

@Test
public void testExecute_whenCreatePartitionPlanDatabaseChangeTaskFailed_thenLogWarning() {
when(scheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(1L);
when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
when(jobEntity.getMergedJobDataMap().get("jobParameters")).thenReturn(jobParameters);
when(jobParameters.getDatabasePartitionPlanId()).thenReturn(1L);
when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);
when(partitionPlanTaskService.executePartitionPlan(anyString(), any())).thenThrow(new RuntimeException());

partitionPlanJob.execute(jobExecutionContext);
}

@Test
public void testInterrupt_whenUnExpectedExceptionThrown_thenDoNothing() {
partitionPlanJob.interrupt();
}
}