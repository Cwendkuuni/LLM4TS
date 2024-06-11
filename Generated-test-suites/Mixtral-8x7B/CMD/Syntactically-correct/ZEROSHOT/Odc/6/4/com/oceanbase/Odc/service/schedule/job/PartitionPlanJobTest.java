import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

private PartitionPlanJob partitionPlanJob;

private PartitionPlanService partitionPlanService;

private PartitionPlanTaskService partitionPlanTaskService;

private ScheduleService scheduleService;

private ApplicationContext applicationContext;

private JobExecutionContext jobExecutionContext;

@Before
public void setup() throws Exception {
partitionPlanService = mock(PartitionPlanService.class);
partitionPlanTaskService = mock(PartitionPlanTaskService.class);
scheduleService = mock(ScheduleService.class);
applicationContext = mock(ApplicationContext.class);
jobExecutionContext = mock(JobExecutionContext.class);

PartitionPlanJobParameters partitionPlanJobParameters = new PartitionPlanJobParameters();
partitionPlanJobParameters.setDatabasePartitionPlanId(1L);

ScheduleEntity scheduleEntity = new ScheduleEntity();
scheduleEntity.setJobParametersJson(JsonUtils.toJson(partitionPlanJobParameters));
scheduleEntity.setId(1L);
scheduleEntity.setFireInstanceId("123");

when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);

when(SpringContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
when(SpringContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
when(SpringContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);
when(SpringContextUtil.getBean(ApplicationContext.class)).thenReturn(applicationContext);

partitionPlanJob = new PartitionPlanJob();
}

@Test
public void testExecute() throws Exception {
DatabasePartitionPlanEntity databasePartitionPlanEntity = new DatabasePartitionPlanEntity();
databasePartitionPlanEntity.setFlowInstanceId("456");
databasePartitionPlanEntity.setConfigEnabled(true);

List<TablePartitionPlanEntity> tablePartitionPlanEntities = new ArrayList<>();
TablePartitionPlanEntity tablePartitionPlanEntity = new TablePartitionPlanEntity();
tablePartitionPlanEntities.add(tablePartitionPlanEntity);

when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlanEntity);
when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
.thenReturn(tablePartitionPlanEntities);

SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
asyncTaskExecutor.setConcurrencyLimit(1);

when(applicationContext.getBean(SchedulerFactoryBean.class)).thenReturn(new SchedulerFactoryBean());
when(applicationContext.getBean(SimpleAsyncTaskExecutor.class)).thenReturn(asyncTaskExecutor);

partitionPlanJob.execute(jobExecutionContext);

verify(partitionPlanTaskService, times(1))
.executePartitionPlan(databasePartitionPlanEntity.getFlowInstanceId(), tablePartitionPlanEntities);
}

@Test
public void testExecuteNoDatabasePartitionPlan() throws Exception {
when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(null);

partitionPlanJob.execute(jobExecutionContext);

verify(partitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());
}

@Test
public void testExecuteNoTablePartitionPlan() throws Exception {
DatabasePartitionPlanEntity databasePartitionPlanEntity = new DatabasePartitionPlanEntity();
databasePartitionPlanEntity.setFlowInstanceId("456");
databasePartitionPlanEntity.setConfigEnabled(true);

when(partitionPlanService.getDatabasePartitionPlanById(anyLong()))
.thenReturn(databasePartitionPlanEntity);
when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
.thenReturn(new ArrayList<>());

partitionPlanJob.execute(jobExecutionContext);

verify(partitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());
}

@Test
public void testExecuteDatabasePartitionPlanDisabled() throws Exception {
DatabasePartitionPlanEntity databasePartitionPlanEntity = new DatabasePartitionPlanEntity();
databasePartitionPlanEntity.setFlowInstanceId("456");
databasePartitionPlanEntity.setConfigEnabled(false);

when(partitionPlanService.getDatabasePartitionPlanById(anyLong()))
.thenReturn(databasePartitionPlanEntity);

partitionPlanJob.execute(jobExecutionContext);

verify(partitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());
}

@Test
public void testInterrupt() throws Exception {
doThrow(new UnExpectedException()).when(partitionPlanTaskService).interrupt();

partitionPlanJob.interrupt();

verify(partitionPlanTaskService, times(1)).interrupt();
}
}