import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PartitionPlanJobTest {

@Mock
private PartitionPlanTaskService partitionPlanTaskService;

@Mock
private PartitionPlanService partitionPlanService;

@Mock
private ScheduleService scheduleService;

@InjectMocks
private PartitionPlanJob partitionPlanJob;

private JobExecutionContext jobExecutionContext;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);
jobExecutionContext = mock(JobExecutionContext.class);
}

@Test
public void testExecuteWithScheduleNotFound() throws SchedulerException {
doReturn(null).when(scheduleService).nullSafeGetById(anyLong());
partitionPlanJob.execute(jobExecutionContext);
verify(scheduleService, times(1)).terminate(any(ScheduleEntity.class));
}

@Test
public void testExecuteWithDatabasePartitionPlanDisabled() {
PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
jobParameters.setDatabasePartitionPlanId(1L);
DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
databasePartitionPlan.setConfigEnabled(false);
doReturn(jobParameters).when(jobExecutionContext).getMergedJobDataMap().get(PartitionPlanJob.JOB_PARAMETERS);
doReturn(databasePartitionPlan).when(partitionPlanService).getDatabasePartitionPlanById(anyLong());
doReturn(Arrays.asList()).when(partitionPlanService)
.getValidTablePlanByDatabasePartitionPlanId(anyLong());
partitionPlanJob.execute(jobExecutionContext);
verify(scheduleService, times(1)).terminate(any(ScheduleEntity.class));
}

@Test
public void testExecuteWithNoTablePartitionPlans() {
PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
jobParameters.setDatabasePartitionPlanId(1L);
DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
databasePartitionPlan.setConfigEnabled(true);
doReturn(jobParameters).when(jobExecutionContext).getMergedJobDataMap().get(PartitionPlanJob.JOB_PARAMETERS);
doReturn(databasePartitionPlan).when(partitionPlanService).getDatabasePartitionPlanById(anyLong());
doReturn(Arrays.asList()).when(partitionPlanService)
.getValidTablePlanByDatabasePartitionPlanId(anyLong());
partitionPlanJob.execute(jobExecutionContext);
verify(scheduleService, times(1)).terminate(any(ScheduleEntity.class));
}

@Test
public void testExecuteWithException() {
PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
jobParameters.setDatabasePartitionPlanId(1L);
DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
databasePartitionPlan.setConfigEnabled(true);
doReturn(jobParameters).when(jobExecutionContext).getMergedJobDataMap().get(PartitionPlanJob.JOB_PARAMETERS);
doReturn(databasePartitionPlan).when(partitionPlanService).getDatabasePartitionPlanById(anyLong());
List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(
new TablePartitionPlanEntity(),
new TablePartitionPlanEntity());
doReturn(tablePartitionPlans).when(partitionPlanService)
.getValidTablePlanByDatabasePartitionPlanId(anyLong());
doThrow(new RuntimeException()).when(partitionPlanTaskService).executePartitionPlan(
anyString(), anyList());
partitionPlanJob.execute(jobExecutionContext);
verify(scheduleService, times(0)).terminate(any(ScheduleEntity.class));
}

@Test
public void testInterrupt() {
PartitionPlanJob partitionPlanJobSpy = spy(new PartitionPlanJob());
doNothing().when(partitionPlanJobSpy).interrupt();
partitionPlanJobSpy.interrupt();
verify(partitionPlanJobSpy, times(1)).interrupt();
}
}