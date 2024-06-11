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
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

 @Mock
 private ScheduleService scheduleService;

 @Mock
 private PartitionPlanService partitionPlanService;

 @Mock
 private PartitionPlanTaskService partitionPlanTaskService;

 @Mock
 private JobExecutionContext context;

 @InjectMocks
 private PartitionPlanJob partitionPlanJob;

 private ScheduleEntity scheduleEntity;

 private PartitionPlanJobParameters jobParameters;

 private DatabasePartitionPlanEntity databasePartitionPlan;

 private List<TablePartitionPlanEntity> tablePartitionPlans;

 @Before
 public void setup() {
 scheduleEntity = mock(ScheduleEntity.class);
 jobParameters = mock(PartitionPlanJobParameters.class);
 databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
 tablePartitionPlans = new ArrayList<>();
 tablePartitionPlans.add(mock(TablePartitionPlanEntity.class));
 }

 @Test
 public void testExecute_ScheduleNotFound() throws SchedulerException {
 when(scheduleService.nullSafeGetById(anyLong())).thenThrow(new SchedulerException("Schedule not found"));
 partitionPlanJob.execute(context);
 assertEquals(0, partitionPlanTaskService.executePartitionPlan(anyString(), any()));
 }

 @Test
 public void testExecute_DatabasePartitionPlanNotFound() {
 when(scheduleEntity.getJobParametersJson()).thenReturn("{}");
 when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(null);
 partitionPlanJob.execute(context);
 assertEquals(0, partitionPlanTaskService.executePartitionPlan(anyString(), any()));
 }

 @Test
 public void testExecute_NoTableToProcess() {
 when(scheduleEntity.getJobParametersJson()).thenReturn("{}");
 when(partitionPlanService.getDatabasePartitionPlanById(anyLong()))
 .thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
 .thenReturn(new ArrayList<>());
 partitionPlanJob.execute(context);
 assertEquals(0, partitionPlanTaskService.executePartitionPlan(anyString(), any()));
 }

 @Test
 public void testExecute_PartitionPlanSuccess() throws Exception {
 when(scheduleEntity.getJobParametersJson())
 .thenReturn(JsonUtils.toJson(jobParameters));
 when(partitionPlanService.getDatabasePartitionPlanById(anyLong()))
 .thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
 .thenReturn(tablePartitionPlans);
 doNothing().when(partitionPlanTaskService).executePartitionPlan(anyString(), any());
 partitionPlanJob.execute(context);
 }

 @Test(expected = UnExpectedException.class)
 public void testInterrupt() {
 partitionPlanJob.interrupt();
 }
}