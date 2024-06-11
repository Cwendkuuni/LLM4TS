import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PartitionPlanJobTest {

@Mock
PartitionPlanTaskService partitionPlanTaskService;

@Mock
PartitionPlanService partitionPlanService;

@Mock
ScheduleService scheduleService;

@InjectMocks
PartitionPlanJob partitionPlanJob;

private ScheduleEntity scheduleEntity;
private PartitionPlanJobParameters jobParameters;
private DatabasePartitionPlanEntity databasePartitionPlan;
private List<TablePartitionPlanEntity> tablePartitionPlans;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);

scheduleEntity = new ScheduleEntity();
scheduleEntity.setId(1L);
scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\": 1}");

jobParameters = new PartitionPlanJobParameters();
jobParameters.setDatabasePartitionPlanId(1L);

databasePartitionPlan = new DatabasePartitionPlanEntity();
databasePartitionPlan.setFlowInstanceId("123");
databasePartitionPlan.setConfigEnabled(true);

tablePartitionPlans = new ArrayList<>();
tablePartitionPlans.add(new TablePartitionPlanEntity());

when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class))
.thenReturn(jobParameters);
when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);
}

@Test
public void testExecute() {
partitionPlanJob.execute(null);

verify(partitionPlanTaskService, times(1)).executePartitionPlan("123", tablePartitionPlans);
verify(scheduleService, times(1)).terminate(scheduleEntity);
}

@Test
public void testInterrupt() {
partitionPlanJob.interrupt();

verify(partitionPlanTaskService, times(0)).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(),
tablePartitionPlans);
verify(scheduleService, times(0)).terminate(scheduleEntity);
}

@Test
public void testGetJobType() {
assertEquals(PartitionPlanJob.JobType.PARTITION_PLAN, partitionPlanJob.getJobType());
}
}