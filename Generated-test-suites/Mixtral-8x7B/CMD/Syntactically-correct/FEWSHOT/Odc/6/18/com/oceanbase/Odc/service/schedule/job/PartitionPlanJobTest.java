import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

public class PartitionPlanJobTest {

@Configuration
@ImportResource("classpath:spring-context.xml")
static class ContextConfiguration {
}

ApplicationContext applicationContext;

PartitionPlanJob partitionPlanJob;

PartitionPlanService partitionPlanService;

PartitionPlanTaskService partitionPlanTaskService;

ScheduleService scheduleService;

@Before
public void setUp() {
applicationContext = new AnnotationConfigApplicationContext(ContextConfiguration.class);

partitionPlanService = mock(PartitionPlanService.class);

partitionPlanTaskService = mock(PartitionPlanTaskService.class);

scheduleService = mock(ScheduleService.class);

partitionPlanJob = new PartitionPlanJob() {
@Override
protected ApplicationContext getApplicationContext() {
return applicationContext;
}
};

partitionPlanJob.partitionPlanService = partitionPlanService;

partitionPlanJob.partitionPlanTaskService = partitionPlanTaskService;

partitionPlanJob.scheduleService = scheduleService;
}

@Test
public void testExecute() throws Exception {
// Create a dummy scheduleEntity
ScheduleEntity scheduleEntity = new ScheduleEntity();
scheduleEntity.setId(UUID.randomUUID().getLeastSignificantBits());
scheduleEntity.setJobParametersJson(
"{\"databasePartitionPlanId\": \"123\", \"someOtherParam\": \"test\"}");

when(scheduleService.nullSafeGetById(scheduleEntity.getId()))
.thenReturn(scheduleEntity);

// Create dummy jobParameters
PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
jobParameters.setDatabasePartitionPlanId(123L);

when(partitionPlanService.getDatabasePartitionPlanById(123L))
.thenReturn(new DatabasePartitionPlanEntity());

when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(123L))
.thenReturn(Collections.emptyList());

when(scheduleService.terminate(scheduleEntity))
.thenReturn(true);

// Create dummy context
JobExecutionContext context = mock(JobExecutionContext.class);
when(context.getMergedJobDataMap().get(ScheduleTaskUtils.SCHEDULE_ID))
.thenReturn(scheduleEntity.getId());

partitionPlanJob.execute(context);

// TODO: Add assertions for method execution
}

@Test
public void testInterrupt() {
// TODO: Implement test for interrupt method
}

@Test
public void testGetApplicationContext() {
// TODO: Implement test for getApplicationContext method
}
}