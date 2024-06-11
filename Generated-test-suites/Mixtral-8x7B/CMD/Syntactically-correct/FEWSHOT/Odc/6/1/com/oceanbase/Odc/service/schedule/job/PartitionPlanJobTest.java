import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@SuppressWarnings("unchecked")
public class PartitionPlanJobTest {

 @Autowired
 private PartitionPlanService partitionPlanService;

 @Autowired
 private PartitionPlanTaskService partitionPlanTaskService;

 @Autowired
 private ScheduleService scheduleService;

 @Autowired
 private ScheduleTaskUtils scheduleTaskUtils;

 @Autowired
 private SpringContextUtil springContextUtil;

 @Autowired
 private SchedulerFactoryBean schedulerFactoryBean;

 private PartitionPlanJob partitionPlanJob;

 private JobExecutionContext jobExecutionContext;

 private ScheduleEntity scheduleEntity;

 private DatabasePartitionPlanEntity databasePartitionPlan;

 private List<TablePartitionPlanEntity> tablePartitionPlans;

 private PartitionPlanJobParameters partitionPlanJobParameters;

 @Before
 public void setup() throws SchedulerException {
 partitionPlanJob = new PartitionPlanJob();

 jobExecutionContext = mock(JobExecutionContext.class);

 scheduleEntity = mock(ScheduleEntity.class);

 databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);

 tablePartitionPlans = new ArrayList<>();

 partitionPlanJobParameters = mock(PartitionPlanJobParameters.class);

 when(scheduleEntity.getJobParametersJson()).thenReturn("{\"databasePartitionPlanId\": 1}");
 when(JsonUtils.fromJson(anyString(), any(TypeReference.class)))
 .thenReturn(partitionPlanJobParameters);
 when(partitionPlanService.getDatabasePartitionPlanById(anyLong()))
 .thenReturn(databasePartitionPlan);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
 .thenReturn(tablePartitionPlans);

 // Make sure the Scheduler is running
 schedulerFactoryBean.start();
 }

 @Test
 public void testExecute_ScheduleNotFound() {
 doThrow(new SchedulerException("")).when(scheduleTaskUtils).getScheduleId(jobExecutionContext);

 partitionPlanJob.execute(jobExecutionContext);

 verify(scheduleService, times(1)).nullSafeGetById(anyLong());
 verify(log, times(1)).warn(anyString(), anyLong());
 verify(partitionPlanService, never()).getDatabasePartitionPlanById(anyLong());
 }

 @Test
 public void testExecute_ScheduleConfigDisabled() throws Exception {
 when(databasePartitionPlan.isConfigEnabled()).thenReturn(false);

 scheduleEntity.setId(1L);
 scheduleTaskUtils.setScheduleId(jobExecutionContext, 1L);

 partitionPlanJob.execute(jobExecutionContext);

 verify(scheduleService, times(1)).terminate(scheduleEntity);
 }

 @Test
 public void testExecute_NoTablePlans() throws Exception {
 when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
 when(tablePartitionPlans.isEmpty()).thenReturn(true);

 scheduleEntity.setId(1L);
 scheduleTaskUtils.setScheduleId(jobExecutionContext, 1L);

 partitionPlanJob.execute(jobExecutionContext);

 verify(scheduleService, times(1)).terminate(scheduleEntity);
 }

 @Test
 public void testExecute_ExecutePartitionPlanSuccess() throws Exception {
 when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
 when(tablePartitionPlans.isEmpty()).thenReturn(false);

 scheduleEntity.setId(1L);
 scheduleTaskUtils.setScheduleId(jobExecutionContext, 1L);

 doNothing().when(partitionPlanTaskService).executePartitionPlan(anyString(), anyList());

 partitionPlanJob.execute(jobExecutionContext);

 verify(partitionPlanTaskService, times(1)).executePartitionPlan(anyString(), anyList());
 }

 @Test
 public void testExecute_ExecutePartitionPlanException() throws Exception {
 when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
 when(tablePartitionPlans.isEmpty()).thenReturn(false);

 scheduleEntity.setId(1L);
 scheduleTaskUtils.setScheduleId(jobExecutionContext, 1L);

 doThrow(new ExecutionException(new Exception("test")))
 .when(partitionPlanTaskService).executePartitionPlan(anyString(), anyList());

 partitionPlanJob.execute(jobExecutionContext);

 verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(anyLong());
 verify(partitionPlanService, times(1))
 .getValidTablePlanByDatabasePartitionPlanId(anyLong());
 }

 @Test
 public void testInterrupt() {
 assertThrows(UnExpectedException.class, () -> partitionPlanJob.interrupt());
 }
}