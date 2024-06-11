import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.TestContextUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.quartz.DateBuilder.evenMinuteDate;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

public class PartitionPlanJobTest {

private Scheduler scheduler;

@Autowired
private ApplicationContext applicationContext;

@Autowired
private ScheduleService scheduleService;

@Autowired
private PartitionPlanService partitionPlanService;

@Autowired
private PartitionPlanTaskService partitionPlanTaskService;

@Before
public void setUp() throws SchedulerException {
scheduler = new StdSchedulerFactory().getScheduler();
scheduler.start();
TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
}

@After
public void tearDown() throws SchedulerException {
scheduler.shutdown();
}

@Test
public void testExecute() throws SchedulerException, UnExpectedException {
// Arrange
ScheduleEntity scheduleEntity = new ScheduleEntity();
scheduleEntity.setName("testPartitionPlanJob");
scheduleEntity.setStartTime(evenMinuteDate(0));
scheduleEntity.setJobClass("com.oceanbase.odc.service.schedule.job.PartitionPlanJob");
PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
jobParameters.setDatabasePartitionPlanId(1L);
scheduleEntity.setJobParametersJson(JsonUtils.toJson(jobParameters));
scheduleEntity = scheduleService.save(scheduleEntity);

// Act
scheduler.triggerJob(new JobKey(scheduleEntity.getJobClass(), scheduleEntity.getName()));

// Assert
Thread.sleep(5000); // Give some time for the job to execute
DatabasePartitionPlanEntity databasePartitionPlan = partitionPlanService.getDatabasePartitionPlanById(1L);
List<TablePartitionPlanEntity> tablePartitionPlans =
partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L);
assertEquals(databasePartitionPlan.getFlowInstanceId(),
partitionPlanTaskService.getLastExecutedFlowInstanceId());
assertEquals(tablePartitionPlans.size(), partitionPlanTaskService.getLastExecutedTableCount());
}

// Other test methods for edge cases and exception handling scenarios
}