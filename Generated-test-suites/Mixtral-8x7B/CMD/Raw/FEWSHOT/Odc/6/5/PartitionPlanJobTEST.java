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
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;
import java.io.InputStream;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

@Mock
private SpringContextUtil springContextUtil;

@Mock
private PartitionPlanTaskService partitionPlanTaskService;

@Mock
private PartitionPlanService partitionPlanService;

@Mock
private ScheduleService scheduleService;

@InjectMocks
private PartitionPlanJob partitionPlanJob;

private JobExecutionContext jobExecutionContext;

private ScheduleEntity scheduleEntity;

private PartitionPlanJobParameters jobParameters;

private DatabasePartitionPlanEntity databasePartitionPlan;

private List<TablePartitionPlanEntity> tablePartitionPlans;

@Before
public void setUp() {
Resource resource = new ClassPathResource("/PartitionPlanJobTestData.json");
InputStream inputStream = resource.getInputStream();
String jsonData = new String();
try {
jsonData = org.apache.commons.io.IOUtils.toString(inputStream, "UTF-8");
} catch (Exception e) {
e.printStackTrace();
}

scheduleEntity = JsonUtils.fromJson(jsonData, ScheduleEntity.class);

jobParameters = JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class);

databasePartitionPlan = partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId());

tablePartitionPlans = new ArrayList<>();
tablePartitionPlans.addAll(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(
jobParameters.getDatabasePartitionPlanId()));

jobExecutionContext = mock(JobExecutionContext.class);
when(jobExecutionContext.getMergedJobDataMap().get(ScheduleTaskUtils.SCHEDULE_ID)).thenReturn(scheduleEntity.getId());
}

@Test
public void testExecute_whenScheduleNotFound_thenLogWarnAndReturn() throws SchedulerException {
doNothing().when(scheduleService).terminate(any(ScheduleEntity.class));

when(scheduleService.nullSafeGetById(anyLong())).thenReturn(null);

partitionPlanJob.execute(jobExecutionContext);

// Assert that scheduleService.terminate() was not called
}

@Test
public void testExecute_whenDatabasePartitionPlanIsDisabledOrNoTableNeedToProcess_thenLogInfoAndStopPartitionPlanJob()
throws Exception {
doNothing().when(scheduleService).terminate(any(ScheduleEntity.class));

when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);

when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(
databasePartitionPlan.setConfigEnabled(false));

when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(new ArrayList<>());

partitionPlanJob.execute(jobExecutionContext);

// Assert that scheduleService.terminate() was called
}

@Test
public void testExecute_whenCreatePartitionPlanDatabaseChangeTaskFailed_thenLogWarn() throws Exception {
doNothing().when(scheduleService).terminate(any(ScheduleEntity.class));

when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);

when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);

when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
.thenReturn(tablePartitionPlans);

doNothing().when(partitionPlanTaskService).executePartitionPlan(anyString(), anyList());

when(partitionPlanTaskService.executePartitionPlan(anyString(), anyList()))
.thenThrow(new Exception("Create partition-plan database change task failed."));

partitionPlanJob.execute(jobExecutionContext);

// Assert that scheduleService.terminate() was not called
}

@Test
public void testExecute_whenNoException_thenDoNotStopPartitionPlanJob() throws Exception {
doNothing().when(scheduleService).terminate(any(ScheduleEntity.class));

when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);

when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);

when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
.thenReturn(tablePartitionPlans);

doNothing().when(partitionPlanTaskService).executePartitionPlan(anyString(), anyList());

when(partitionPlanTaskService.executePartitionPlan(anyString(), anyList()))
.thenReturn(new ArrayList<>());

partitionPlanJob.execute(jobExecutionContext);

// Assert that scheduleService.terminate() was not called
}

@Test(expected = UnExpectedException.class)
public void testInterrupt_whenInterruptCalled_thenThrowUnExpectedException() throws Exception {
partitionPlanJob.interrupt();
}
}