import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.impl.JobExecutionContextImpl;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

 @Mock
 private PartitionPlanTaskService partitionPlanTaskService;

 @Mock
 private PartitionPlanService partitionPlanService;

 @Mock
 private ScheduleService scheduleService;

 @Mock
 private JsonUtils jsonUtils;

 @Mock
 private ScheduleTaskUtils scheduleTaskUtils;

 @Mock
 private SpringContextUtil springContextUtil;

 @InjectMocks
 private PartitionPlanJob partitionPlanJob;

 private JobExecutionContext context;

 private ScheduleEntity scheduleEntity;

 private DatabasePartitionPlanEntity databasePartitionPlan;

 private List<TablePartitionPlanEntity> tablePartitionPlans;

 private PartitionPlanJobParameters jobParameters;

 @Before
 public void setup() {
 context = new JobExecutionContextImpl();
 scheduleEntity = new ScheduleEntity();
 databasePartitionPlan = new DatabasePartitionPlanEntity();
 tablePartitionPlans = new ArrayList<>();
 jobParameters = new PartitionPlanJobParameters();

 ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanTaskService", partitionPlanTaskService);
 ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanService);
 ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", scheduleService);
 ReflectionTestUtils.setField(partitionPlanJob, "jsonUtils", jsonUtils);
 ReflectionTestUtils.setField(partitionPlanJob, "scheduleTaskUtils", scheduleTaskUtils);
 ReflectionTestUtils.setField(partitionPlanJob, "springContextUtil", springContextUtil);
 }

 @Test
 public void testExecute() throws Exception {
 when(scheduleTaskUtils.getScheduleId(context)).thenReturn(1L);
 when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
 when(jsonUtils.fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class))
 .thenReturn(jobParameters);
 when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);
 when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
 when(tablePartitionPlans.isEmpty()).thenReturn(false);

 partitionPlanJob.execute(context);

 verify(partitionPlanTaskService, times(1)).executePartitionPlan(anyString(), anyList());
 }

 @Test
 public void testExecuteWhenScheduleNotFound() throws Exception {
 when(scheduleTaskUtils.getScheduleId(context)).thenReturn(1L);
 when(scheduleService.nullSafeGetById(1L)).thenThrow(new Exception());

 partitionPlanJob.execute(context);

 verify(partitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());
 }

 @Test
 public void testExecuteWhenDatabasePartitionPlanIsDisable() throws Exception {
 when(scheduleTaskUtils.getScheduleId(context)).thenReturn(1L);
 when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
 when(jsonUtils.fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class))
 .thenReturn(jobParameters);
 when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);
 when(databasePartitionPlan.isConfigEnabled()).thenReturn(false);

 partitionPlanJob.execute(context);

 verify(partitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());
 }

 @Test
 public void testExecuteWhenNoTableToProcess() throws Exception {
 when(scheduleTaskUtils.getScheduleId(context)).thenReturn(1L);
 when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
 when(jsonUtils.fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class))
 .thenReturn(jobParameters);
 when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);
 when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
 when(tablePartitionPlans.isEmpty()).thenReturn(true);

 partitionPlanJob.execute(context);

 verify(partitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());
 }

 @Test
 public void testInterrupt() throws SchedulerException {
 partitionPlanJob.interrupt();
 scheduleService.terminate(scheduleEntity);
 }
}