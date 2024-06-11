import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
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
import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.QuartzJobFactory;
import org.springframework.test.util.ReflectionTestUtils;

public class PartitionPlanJobTest {

@Autowired
private PartitionPlanService partitionPlanService;

@Autowired
private PartitionPlanTaskService partitionPlanTaskService;

@Autowired
private ScheduleService scheduleService;

@Autowired
private SpringContextUtil springContextUtil;

@Autowired
private ScheduleTaskUtils scheduleTaskUtils;

@Autowired
private JsonUtils jsonUtils;

private PartitionPlanJob partitionPlanJob;

private JobExecutionContext jobExecutionContext;

@Before
public void setup() {
partitionPlanJob = new PartitionPlanJob();
ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanTaskService", partitionPlanTaskService);
ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanService);
ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", scheduleService);

jobExecutionContext = mock(JobExecutionContext.class);
ReflectionTestUtils.setField(partitionPlanJob, "springContextUtil", springContextUtil);
ReflectionTestUtils.setField(partitionPlanJob, "scheduleTaskUtils", scheduleTaskUtils);
ReflectionTestUtils.setField(partitionPlanJob, "jsonUtils", jsonUtils);
}

@Test
public void testExecute() throws Exception {
Long scheduleId = 1L;
ScheduleEntity scheduleEntity = createScheduleEntity(scheduleId);
when(scheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(scheduleId);
when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
String jobParametersJson = jsonUtils.toJson(jobParameters);
when(scheduleEntity.getJobParametersJson()).thenReturn(jobParametersJson);
DatabasePartitionPlanEntity databasePartitionPlan = createDatabasePartitionPlanEntity(1L);
when(partitionPlanService.getDatabasePartitionPlanById(anyLong()))
.thenReturn(databasePartitionPlan);
List<TablePartitionPlanEntity> tablePartitionPlans = createTablePartitionPlanEntities(1, 2);
when(partitionPlanService
.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
.thenReturn(tablePartitionPlans);

partitionPlanJob.execute(jobExecutionContext);

// add assertions here
}

private List<TablePartitionPlanEntity> createTablePartitionPlanEntities(long... ids) {
List<TablePartitionPlanEntity> entities = new ArrayList<>();
for (long id : ids) {
TablePartitionPlanEntity entity = new TablePartitionPlanEntity();
entity.setId(id);
entities.add(entity);
}
return entities;
}

private ScheduleEntity createScheduleEntity(long scheduleId) {
ScheduleEntity scheduleEntity = new ScheduleEntity();
scheduleEntity.setId(scheduleId);
scheduleEntity.setJobClass(PartitionPlanJob.class.getName());
return scheduleEntity;
}

private DatabasePartitionPlanEntity createDatabasePartitionPlanEntity(long id) {
DatabasePartitionPlanEntity entity = new DatabasePartitionPlanEntity();
entity.setId(id);
entity.setConfigEnabled(true);
return entity;
}

// other test methods

// test interrupt

// test other methods
}

class TestJobFactory extends QuartzJobFactory {

@Override
protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
return new PartitionPlanJob();
}
}

class TestQuartzJobBean extends QuartzJobBean {

private PartitionPlanJob partitionPlanJob;

@Override
protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
partitionPlanJob.execute(context);
}

public void setPartitionPlanJob(PartitionPlanJob partitionPlanJob) {
this.partitionPlanJob = partitionPlanJob;
}
}