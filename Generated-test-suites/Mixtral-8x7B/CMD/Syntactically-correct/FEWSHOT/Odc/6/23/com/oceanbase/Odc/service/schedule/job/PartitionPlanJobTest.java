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
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SuppressWarnings("unused")
public class PartitionPlanJobTest {

@Autowired
private ApplicationContext applicationContext;

private PartitionPlanJob partitionPlanJob;

private PartitionPlanTaskService partitionPlanTaskService;

private PartitionPlanService partitionPlanService;

private ScheduleService scheduleService;

private JobExecutionContext jobExecutionContext;

private ScheduleEntity scheduleEntity;

private PartitionPlanJobParameters jobParameters;

private DatabasePartitionPlanEntity databasePartitionPlan;

private List<TablePartitionPlanEntity> tablePartitionPlans;

private ScheduledExecutorService scheduledExecutorService;

private SchedulerException schedulerException;

private ThreadPoolTaskExecutor threadPoolTaskExecutor;

@Before
public void setup() {
partitionPlanTaskService = mock(PartitionPlanTaskService.class);
partitionPlanService = mock(PartitionPlanService.class);
scheduleService = mock(ScheduleService.class);
scheduledExecutorService = mock(ScheduledExecutorService.class);
schedulerException = mock(SchedulerException.class);
threadPoolTaskExecutor = mock(ThreadPoolTaskExecutor.class);

partitionPlanJob = new PartitionPlanJob();
partitionPlanJob.setPartitionPlanTaskService(partitionPlanTaskService);
partitionPlanJob.setPartitionPlanService(partitionPlanService);
partitionPlanJob.setScheduleService(scheduleService);

SpringContextUtil.setApplicationContext(applicationContext);
SpringContextUtil.setScheduledExecutorService(scheduledExecutorService);
SpringContextUtil.setSchedulerException(schedulerException);
SpringContextUtil.setThreadPoolTaskExecutor(threadPoolTaskExecutor);

jobExecutionContext = mock(JobExecutionContext.class);
scheduleEntity = mock(ScheduleEntity.class);
jobParameters = new PartitionPlanJobParameters();
databasePartitionPlan = new DatabasePartitionPlanEntity();
tablePartitionPlans = new ArrayList<>();
}

@Test
public void testExecute() throws Exception {
when(jobExecutionContext.getMergedJobDataMap().get(ScheduleTaskUtils.SCHEDULE_ID)).thenReturn(1L);
when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
when(scheduleEntity.getJobParametersJson()).thenReturn("{\"databasePartitionPlanId\":1}");
when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L))
.thenReturn(tablePartitionPlans);
when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
when(tablePartitionPlans.isEmpty()).thenReturn(false);

partitionPlanJob.execute(jobExecutionContext);

// Add assertions here based on the expected behavior
}

@Test
public void testInterrupt() {
try {
partitionPlanJob.interrupt();
} catch (UnExpectedException e) {
// Expected behavior
}
}
}