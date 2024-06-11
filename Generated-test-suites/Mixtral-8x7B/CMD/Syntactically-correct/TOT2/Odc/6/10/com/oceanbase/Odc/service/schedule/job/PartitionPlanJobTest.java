import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanJobParameters;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
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

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

 @Mock
 private ScheduleService scheduleService;

 @Mock
 private PartitionPlanTaskService partitionPlanTaskService;

 @Mock
 private PartitionPlanService partitionPlanService;

 @InjectMocks
 private PartitionPlanJob partitionPlanJob;

 private JobExecutionContext jobExecutionContext;

 @Before
 public void setup() {
 jobExecutionContext = mock(JobExecutionContext.class);
 ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", scheduleService);
 ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanTaskService", partitionPlanTaskService);
 ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanService);
 }

 @Test
 public void testExecute_scheduleNotFound() throws Exception {
 doReturn(null).when(scheduleService).nullSafeGetById(anyLong());
 partitionPlanJob.execute(jobExecutionContext);
 verify(scheduleService, times(1)).nullSafeGetById(anyLong());
 verify(partitionPlanTaskService, times(0)).executePartitionPlan(any(), any());
 }

 @Test
 public void testExecute_databasePartitionPlanNotConfigured() throws Exception {
 Long scheduleId = 1L;
 ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
 doReturn(scheduleEntity).when(scheduleService).nullSafeGetById(scheduleId);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(1L);
 doReturn(jobParameters).when(scheduleTaskUtils).getJobParameters(jobExecutionContext);
 DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
 doReturn(false).when(databasePartitionPlan).isConfigEnabled();
 doReturn(databasePartitionPlan).when(partitionPlanService).getDatabasePartitionPlanById(1L);
 List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
 doReturn(tablePartitionPlans).when(partitionPlanService)
 .getValidTablePlanByDatabasePartitionPlanId(1L);
 partitionPlanJob.execute(jobExecutionContext);
 verify(scheduleService, times(1)).nullSafeGetById(scheduleId);
 verify(partitionPlanTaskService, times(0)).executePartitionPlan(any(), any());
 }

 @Test
 public void testExecute_noTableToProcess() throws Exception {
 Long scheduleId = 1L;
 ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
 doReturn(scheduleEntity).when(scheduleService).nullSafeGetById(scheduleId);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(1L);
 doReturn(jobParameters).when(scheduleTaskUtils).getJobParameters(jobExecutionContext);
 DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
 doReturn(true).when(databasePartitionPlan).isConfigEnabled();
 doReturn(databasePartitionPlan).when(partitionPlanService).getDatabasePartitionPlanById(1L);
 List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
 doReturn(tablePartitionPlans).when(partitionPlanService)
 .getValidTablePlanByDatabasePartitionPlanId(1L);
 partitionPlanJob.execute(jobExecutionContext);
 verify(scheduleService, times(1)).nullSafeGetById(scheduleId);
 verify(partitionPlanTaskService, times(0)).executePartitionPlan(any(), any());
 }

 @Test
 public void testExecute_executePartitionPlanSuccess() throws Exception {
 Long scheduleId = 1L;
 ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
 doReturn(scheduleEntity).when(scheduleService).nullSafeGetById(scheduleId);
 PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
 jobParameters.setDatabasePartitionPlanId(1L);
 doReturn(jobParameters).when(scheduleTaskUtils).getJobParameters(jobExecutionContext);
 DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
 doReturn(true).when(databasePartitionPlan).isConfigEnabled();
 doReturn(databasePartitionPlan).when(partitionPlanService).getDatabasePartitionPlanById(1L);
 List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
 tablePartitionPlans.add(mock(TablePartitionPlanEntity.class));
 doReturn(tablePartitionPlans).when(partitionPlanService)
 .getValidTablePlanByDatabasePartitionPlanId(1L);
 doNothing().when(partitionPlanTaskService).executePartitionPlan(any(), any());
 partitionPlanJob.execute(jobExecutionContext);
 verify(scheduleService, times(1)).nullSafeGetById(scheduleId);
 verify(partitionPlanTaskService, times(1)).executePartitionPlan(any(), any());
 }

 @Test(expected = UnExpectedException.class)
 public void testInterrupt() throws Exception {
 partitionPlanJob.interrupt();
 }
}