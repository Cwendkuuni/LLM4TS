import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
import com.oceanbase.odc.service.schedule.job.OdcJob;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PartitionPlanJobTest {

 @Mock
 private PartitionPlanTaskService partitionPlanTaskService;

 @Mock
 private PartitionPlanService partitionPlanService;

 @Mock
 private ScheduleService scheduleService;

 @Mock
 private JobExecutionContext context;

 @Mock
 private ScheduleEntity scheduleEntity;

 @Mock
 private DatabasePartitionPlanEntity databasePartitionPlan;

 @InjectMocks
 private PartitionPlanJob partitionPlanJob;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testExecuteJobWithNoScheduleFound() {
 partitionPlanJob.execute(context);
 verify(log, times(1)).warn("Schedule not found,scheduleId={}", anyLong());
 }

 @Test
 public void testExecuteJobWithScheduleFound() throws Exception {
 when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
 when(scheduleEntity.getJobParametersJson()).thenReturn("{}");
 when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
 .thenReturn(new ArrayList<>());

 partitionPlanJob.execute(context);

 verify(log, times(1)).info(
 "database partition plan is disable or no table need to process,start to stop partition-plan job,scheduleId={}",
 anyLong());
 verify(scheduleService, times(1)).terminate(scheduleEntity);
 }

 @Test
 public void testExecuteJobWithTablePartitionPlans() throws Exception {
 when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
 when(scheduleEntity.getJobParametersJson()).thenReturn("{}");
 when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
 List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
 tablePartitionPlans.add(new TablePartitionPlanEntity());
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
 .thenReturn(tablePartitionPlans);

 partitionPlanJob.execute(context);

 verify(partitionPlanTaskService, times(1))
 .executePartitionPlan(anyString(), any(List.class));
 }

 @Test
 public void testInterrupt() {
 partitionPlanJob.interrupt();
 verify(log, never()).warn(anyString(), any(Throwable.class));
 }
}