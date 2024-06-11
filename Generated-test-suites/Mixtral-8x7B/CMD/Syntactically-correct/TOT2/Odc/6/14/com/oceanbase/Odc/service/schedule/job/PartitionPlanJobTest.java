import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanJobParameters;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PartitionPlanJobTest {

@Mock
private ScheduleService scheduleService;

@Mock
private PartitionPlanTaskService partitionPlanTaskService;

@Mock
private PartitionPlanService partitionPlanService;

@Mock
private JobExecutionContext jobExecutionContext;

@Mock
private ScheduleTaskUtils scheduleTaskUtils;

@Mock
private ScheduleEntity scheduleEntity;

@Mock
private PartitionPlanJobParameters jobParameters;

@Mock
private DatabasePartitionPlanEntity databasePartitionPlan;

@Mock
private TablePartitionPlanEntity tablePartitionPlanEntity;

@InjectMocks
private PartitionPlanJob partitionPlanJob;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);
}

@Test
public void testExecute_scheduleIdDoesNotExist() {
when(scheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(1L);
when(scheduleService.nullSafeGetById(anyLong())).thenReturn(null);

partitionPlanJob.execute(jobExecutionContext);

verify(scheduleService, times(1)).nullSafeGetById(anyLong());
verify(log, times(1)).warn("Schedule not found,scheduleId={}", 1L);
}

@Test
public void testExecute_databasePartitionPlanDoesNotExist() {
when(scheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(1L);
when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(null);

partitionPlanJob.execute(jobExecutionContext);

verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(anyLong());
verify(log, times(1)).warn("Database partition plan not found,databasePartitionPlanId={}",
anyLong());
}

@Test
public void testExecute_noTableNeedToProcess() {
when(scheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(1L);
when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
.thenReturn(new ArrayList<>());

partitionPlanJob.execute(jobExecutionContext);

verify(partitionPlanService, times(1)).getValidTablePlanByDatabasePartitionPlanId(anyLong());
verify(scheduleService, times(1)).terminate(scheduleEntity);
}

@Test
public void testExecute_executePartitionPlanSuccess() {
when(scheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(1L);
when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
.thenReturn(List.of(tablePartitionPlanEntity));

doNothing().when(partitionPlanTaskService).executePartitionPlan(anyString(), anyList());

partitionPlanJob.execute(jobExecutionContext);

verify(partitionPlanTaskService, times(1)).executePartitionPlan(anyString(), anyList());
}

@Test(expected = RuntimeException.class)
public void testExecute_executePartitionPlanFailed() {
when(scheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(1L);
when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
.thenReturn(List.of(tablePartitionPlanEntity));

doThrow(new RuntimeException()).when(partitionPlanTaskService).executePartitionPlan(anyString(), anyList());

partitionPlanJob.execute(jobExecutionContext);
}

@Test
public void testInterrupt() {
doThrow(new UnExpectedException()).when(partitionPlanJob).interrupt();

partitionPlanJob.interrupt();

verify(log, times(1)).warn("Unexpected exception was thrown", new UnExpectedException());
}
}