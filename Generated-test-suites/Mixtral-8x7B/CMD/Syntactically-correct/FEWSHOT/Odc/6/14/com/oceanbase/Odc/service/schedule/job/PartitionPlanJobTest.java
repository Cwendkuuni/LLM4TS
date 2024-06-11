import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;

@org.springframework.test.context.ContextConfiguration(classes = { PartitionPlanJobTestConfig.class })
public class PartitionPlanJobTest {

private PartitionPlanJob partitionPlanJob;

@Autowired
private ScheduleService scheduleService;

@Autowired
private PartitionPlanTaskService partitionPlanTaskService;

@Autowired
private PartitionPlanService partitionPlanService;

@Autowired
private ScheduleTaskUtils scheduleTaskUtils;

@Autowired
private SpringContextUtil springContextUtil;

@Autowired
private JsonUtils jsonUtils;

@Before
public void setup() {
partitionPlanJob = new PartitionPlanJob();
ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", scheduleService);
ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanTaskService", partitionPlanTaskService);
ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanService);
ReflectionTestUtils.setField(partitionPlanJob, "scheduleTaskUtils", scheduleTaskUtils);
ReflectionTestUtils.setField(partitionPlanJob, "springContextUtil", springContextUtil);
ReflectionTestUtils.setField(partitionPlanJob, "jsonUtils", jsonUtils);
}

@Test
public void testExecute() throws Exception {
Long scheduleId = 1L;
ScheduleEntity scheduleEntity = new ScheduleEntity();
scheduleEntity.setId(scheduleId);
ReflectionTestUtils.setField(scheduleTaskUtils, "scheduleService", scheduleService);
when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
ReflectionTestUtils.setField(partitionPlanJob, "jsonUtils", jsonUtils);
String json = jsonUtils.toJson(jobParameters);
scheduleEntity.setJobParametersJson(json);
DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
databasePartitionPlan.setConfigEnabled(true);
ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanService);
when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
List<TablePartitionPlanEntity> tablePartitionPlans = mock(List.class);
when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);
ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanTaskService", partitionPlanTaskService);
partitionPlanJob.execute(mock(JobExecutionContext.class));
verify(partitionPlanTaskService, times(1)).executePartitionPlan(anyString(), anyList());
}

@Test
public void testExecuteWhenScheduleNotFound() throws Exception {
Long scheduleId = 1L;
ReflectionTestUtils.setField(scheduleTaskUtils, "scheduleService", scheduleService);
when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(null);
partitionPlanJob.execute(mock(JobExecutionContext.class));
verify(scheduleService, times(1)).nullSafeGetById(scheduleId);
verify(partitionPlanTaskService, times(0)).executePartitionPlan(anyString(), anyList());
}

@Test
public void testExecuteWhenDatabasePartitionPlanIsNotConfigEnabled() throws Exception {
Long scheduleId = 1L;
ScheduleEntity scheduleEntity = new ScheduleEntity();
scheduleEntity.setId(scheduleId);
ReflectionTestUtils.setField(scheduleTaskUtils, "scheduleService", scheduleService);
when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
ReflectionTestUtils.setField(partitionPlanJob, "jsonUtils", jsonUtils);
String json = jsonUtils.toJson(jobParameters);
scheduleEntity.setJobParametersJson(json);
DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
databasePartitionPlan.setConfigEnabled(false);
ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanService);
when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
List<TablePartitionPlanEntity> tablePartitionPlans = mock(List.class);
when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);
ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanTaskService", partitionPlanTaskService);
partitionPlanJob.execute(mock(JobExecutionContext.class));
verify(partitionPlanTaskService, times(0)).executePartitionPlan(anyString(), anyList());
}

@Test
public void testExecuteWhenTablePartitionPlansIsEmpty() throws Exception {
Long scheduleId = 1L;
ScheduleEntity scheduleEntity = new ScheduleEntity();
scheduleEntity.setId(scheduleId);
ReflectionTestUtils.setField(scheduleTaskUtils, "scheduleService", scheduleService);
when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
ReflectionTestUtils.setField(partitionPlanJob, "jsonUtils", jsonUtils);
String json = jsonUtils.toJson(jobParameters);
scheduleEntity.setJobParametersJson(json);
DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
databasePartitionPlan.setConfigEnabled(true);
ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanService);
when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
List<TablePartitionPlanEntity> tablePartitionPlans = mock(List.class);
when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);
when(tablePartitionPlans.isEmpty()).thenReturn(true);
ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanTaskService", partitionPlanTaskService);
partitionPlanJob.execute(mock(JobExecutionContext.class));
verify(partitionPlanTaskService, times(0)).executePartitionPlan(anyString(), anyList());
}

@Test
public void testInterrupt() throws Exception {
partitionPlanJob.interrupt();
}
}

@Configuration
@ComponentScan("com.oceanbase")
class PartitionPlanJobTestConfig {

@Bean
public ScheduleEntity scheduleEntity() {
return new ScheduleEntity();
}

@Bean
public ScheduleService scheduleService() {
return mock(ScheduleService.class);
}

@Bean
public PartitionPlanService partitionPlanService() {
return mock(PartitionPlanService.class);
}

@Bean
public PartitionPlanTaskService partitionPlanTaskService() {
return mock(PartitionPlanTaskService.class);
}

@Bean
public ScheduleTaskUtils scheduleTaskUtils() {
return new ScheduleTaskUtils();
}

@Bean
public SpringContextUtil springContextUtil() {
return new SpringContextUtil();
}

@Bean
public JsonUtils jsonUtils() {
return new JsonUtils();
}
}