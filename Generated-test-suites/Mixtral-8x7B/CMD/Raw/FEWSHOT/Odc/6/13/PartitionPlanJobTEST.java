import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters.PartitionPlanJobType;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.model.ScheduleJobType;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

public class PartitionPlanJobTest {

@Before
public void setUp() throws Exception {
ApplicationContext applicationContext = mock(ApplicationContext.class);
SpringContextUtil.setApplicationContext(applicationContext);

PartitionPlanService partitionPlanService = mock(PartitionPlanService.class);
when(applicationContext.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);

PartitionPlanTaskService partitionPlanTaskService = mock(PartitionPlanTaskService.class);
when(applicationContext.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);

ScheduleService scheduleService = mock(ScheduleService.class);
when(applicationContext.getBean(ScheduleService.class)).thenReturn(scheduleService);

JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);
when(jobExecutionContext.getMergedJobDataMap().getString("scheduleId")).thenReturn("123");

Field field = PartitionPlanJob.class.getDeclaredField("scheduleService");
field.setAccessible(true);
field.set(null, scheduleService);

field = PartitionPlanJob.class.getDeclaredField("partitionPlanService");
field.setAccessible(true);
field.set(null, partitionPlanService);

field = PartitionPlanJob.class.getDeclaredField("partitionPlanTaskService");
field.setAccessible(true);
field.set(null, partitionPlanTaskService);
}

@Test
public void testExecute() throws Exception {
PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
partitionPlanJob.execute(null);
verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(anyLong());
verify(partitionPlanService, times(1))
.getValidTablePlanByDatabasePartitionPlanId(anyLong());
verify(partitionPlanTaskService, times(1))
.executePartitionPlan(anyLong(), any());
}

@Test
public void testExecuteWithNullSchedule() throws Exception {
Field field = PartitionPlanJob.class.getDeclaredField("scheduleService");
field.setAccessible(true);
PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
field.set(partitionPlanJob, null);
partitionPlanJob.execute(null);
verify(partitionPlanService, never()).getDatabasePartitionPlanById(anyLong());
verify(partitionPlanService, never())
.getValidTablePlanByDatabasePartitionPlanId(anyLong());
verify(partitionPlanTaskService, never())
.executePartitionPlan(anyLong(), any());
}

@Test
public void testExecuteWithException() throws Exception {
doThrow(new SchedulerException("test")).when(scheduleService).terminate(any(ScheduleEntity.class));
PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
partitionPlanJob.execute(null);
verify(scheduleService, times(1)).terminate(any(ScheduleEntity.class));
}

@Test
public void testInterrupt() throws Exception {
PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
partitionPlanJob.interrupt();
}

@Test
public void testDeserializeJobParameters() throws Exception {
PartitionPlanJobParameters partitionPlanJobParameters = new PartitionPlanJobParameters();
partitionPlanJobParameters.setDatabasePartitionPlanId(1L);
partitionPlanJobParameters.setJobType(PartitionPlanJobType.TABLE_PARTITION_PLAN);
ScheduleEntity scheduleEntity = new ScheduleEntity();
scheduleEntity.setJobParametersJson(JsonUtils.toJson(partitionPlanJobParameters));
PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
PartitionPlanJobParameters result =
partitionPlanJob.deserializeJobParameters(scheduleEntity.getJobParametersJson(),
ScheduleJobType.PARTITION_PLAN);
assertEquals(partitionPlanJobParameters.getDatabasePartitionPlanId(),
result.getDatabasePartitionPlanId());
assertEquals(partitionPlanJobParameters.getJobType(), result.getJobType());
}

@Test(expected = Exception.class)
public void testDeserializeJobParametersWithInvalidJson() throws Exception {
PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
partitionPlanJob.deserializeJobParameters("{\"invalid\": \"json\"}", ScheduleJobType.PARTITION_PLAN);
}

@Test
public void testDeserializeJobParametersWithNullJson() throws Exception {
PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
PartitionPlanJobParameters result =
partitionPlanJob.deserializeJobParameters(null, ScheduleJobType.PARTITION_PLAN);
assertNull(result);
}

@Test
public void testDeserializeJobParametersWithNullType() throws Exception {
PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
PartitionPlanJobParameters partitionPlanJobParameters = new PartitionPlanJobParameters();
partitionPlanJobParameters.setDatabasePartitionPlanId(1L);
partitionPlanJobParameters.setJobType(PartitionPlanJobType.TABLE_PARTITION_PLAN);
partitionPlanJob.deserializeJobParameters(JsonUtils.toJson(partitionPlanJobParameters), null);
}

@Test
public void testGetPartitionPlanEntities() throws Exception {
PartitionPlanService partitionPlanService = mock(PartitionPlanService.class);
PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
Field field = PartitionPlanJob.class.getDeclaredField("partitionPlanService");
field.setAccessible(true);
field.set(partitionPlanJob, partitionPlanService);
DatabasePartitionPlanEntity databasePartitionPlanEntity = new DatabasePartitionPlanEntity();
databasePartitionPlanEntity.setId(1L);
List<TablePartitionPlanEntity> tablePartitionPlanEntities = new ArrayList<>();
tablePartitionPlanEntities.add(new TablePartitionPlanEntity());
when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlanEntity);
when(partitionPlanService
.getValidTablePlanByDatabasePartitionPlanId(1L))
.thenReturn(tablePartitionPlanEntities);
List<Map<String, Object>> result = partitionPlanJob.getPartitionPlanEntities(1L);
verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(1L);
verify(partitionPlanService, times(1))
.getValidTablePlanByDatabasePartitionPlanId(1L);
assertEquals(2, result.size());
}

@Test
public void testGetPartitionPlanEntitiesWithEmptyTablePlanEntities() throws Exception {
PartitionPlanService partitionPlanService = mock(PartitionPlanService.class);
PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
Field field = PartitionPlanJob.class.getDeclaredField("partitionPlanService");
field.setAccessible(true);
field.set(partitionPlanJob, partitionPlanService);
DatabasePartitionPlanEntity databasePartitionPlanEntity = new DatabasePartitionPlanEntity();
databasePartitionPlanEntity.setId(1L);
when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlanEntity);
when(partitionPlanService
.getValidTablePlanByDatabasePartitionPlanId(1L))
.thenReturn(Collections.emptyList());
List<Map<String, Object>> result = partitionPlanJob.getPartitionPlanEntities(1L);
verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(1L);
verify(partitionPlanService, times(1))
.getValidTablePlanByDatabasePartitionPlanId(1L);
assertEquals(1, result.size());
}

@Test
public void testGetPartitionPlanEntitiesWithNullTablePlanEntities() throws Exception {
PartitionPlanService partitionPlanService = mock(PartitionPlanService.class);
PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
Field field = PartitionPlanJob.class.getDeclaredField("partitionPlanService");
field.setAccessible(true);
field.set(partitionPlanJob, partitionPlanService);
DatabasePartitionPlanEntity databasePartitionPlanEntity = new DatabasePartitionPlanEntity();
databasePartitionPlanEntity.setId(1L);
when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlanEntity);
when(partitionPlanService
.getValidTablePlanByDatabasePartitionPlanId(1L))
.thenReturn(null);
List<Map<String, Object>> result = partitionPlanJob.getPartitionPlanEntities(1L);
verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(1L);
verify(partitionPlanService, times(1))
.getValidTablePlanByDatabasePartitionPlanId(1L);
assertEquals(1, result.size());
}

@Test
public void testGetPartitionPlanEntitiesWithException() throws Exception {
PartitionPlanService partitionPlanService = mock(PartitionPlanService.class);
PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
Field field = PartitionPlanJob.class.getDeclaredField("partitionPlanService");
field.setAccessible(true);
field.set(partitionPlanJob, partitionPlanService);
when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenThrow(new Exception("test"));
List<Map<String, Object>> result = partitionPlanJob.getPartitionPlanEntities(1L);
verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(1L);
verify(partitionPlanService, never())
.getValidTablePlanByDatabasePartitionPlanId(1L);
assertEquals(0, result.size());
}

@Test
public void testGetScheduleEntityById() throws Exception {
ScheduleService scheduleService = mock(ScheduleService.class);
PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
Field field = PartitionPlanJob.class.getDeclaredField("scheduleService");
field.setAccessible(true);
field.set(partitionPlanJob, scheduleService);
ScheduleEntity scheduleEntity = new ScheduleEntity();
when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
ScheduleEntity result = partitionPlanJob.getScheduleEntityById(1L);
verify(scheduleService, times(1)).nullSafeGetById(1L);
assertEquals(scheduleEntity, result);
}

@Test
public void testGetScheduleEntityByIdWithException() throws Exception {
ScheduleService scheduleService = mock(ScheduleService.class);
PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
Field field = PartitionPlanJob.class.getDeclaredField("scheduleService");
field.setAccessible(true);
field.set(partitionPlanJob, scheduleService);
when(scheduleService.nullSafeGetById(1L)).thenThrow(new Exception("test"));
ScheduleEntity result = partitionPlanJob.getScheduleEntityById(1L);
verify(scheduleService, times(1)).nullSafeGetById(1L);
assertNull(result);
}

@Test
public void testIsPartitionPlanJobEnabled() throws Exception {
PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
databasePartitionPlan.setConfigEnabled(true);
List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
tablePartitionPlans.add(new TablePartitionPlanEntity());
when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
when(partitionPlanService
.getValidTablePlanByDatabasePartitionPlanId(1L))
.thenReturn(tablePartitionPlans);
assertTrue(partitionPlanJob.isPartitionPlanJobEnabled(1L));
}

@Test
public void testIsPartitionPlanJobEnabledWithNullPlan() throws Exception {
PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(null);
assertFalse(partitionPlanJob.isPartitionPlanJobEnabled(1L));
}

@Test
public void testIsPartitionPlanJobEnabledWithNoTablePlans() throws Exception {
PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
databasePartitionPlan.setConfigEnabled(true);
when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
when(partitionPlanService
.getValidTablePlanByDatabasePartitionPlanId(1L))
.thenReturn(Collections.emptyList());
assertFalse(partitionPlanJob.isPartitionPlanJobEnabled(1L));
}

@Test
public void testIsPartitionPlanJobEnabledWithDisabledConfig() throws Exception {
PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
databasePartitionPlan.setConfigEnabled(false);
List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
tablePartitionPlans.add(new TablePartitionPlanEntity());
when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
when(partitionPlanService
.getValidTablePlanByDatabasePartitionPlanId(1L))
.thenReturn(tablePartitionPlans);
assertFalse(partitionPlanJob.isPartitionPlanJobEnabled(1L));
}
}