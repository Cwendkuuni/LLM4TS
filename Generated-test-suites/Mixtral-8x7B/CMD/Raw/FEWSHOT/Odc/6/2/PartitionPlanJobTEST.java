import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;

@ContextConfiguration("classpath:applicationContext.xml")
public class PartitionPlanJobTest extends AbstractJUnit4SpringContextTests {

 private PartitionPlanJob partitionPlanJob;

 private PartitionPlanService partitionPlanServiceMock;

 private PartitionPlanTaskService partitionPlanTaskServiceMock;

 private ScheduleService scheduleServiceMock;

 private JobExecutionContext jobExecutionContextMock;

 private ScheduleEntity scheduleEntityMock;

 private PartitionPlanJobParameters partitionPlanJobParametersMock;

 private DatabasePartitionPlanEntity databasePartitionPlanEntityMock;

 private List<TablePartitionPlanEntity> tablePartitionPlansMock;

 @Before
 public void setup() {
 partitionPlanServiceMock = mock(PartitionPlanService.class);
 partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
 scheduleServiceMock = mock(ScheduleService.class);
 jobExecutionContextMock = mock(JobExecutionContext.class);
 scheduleEntityMock = new ScheduleEntity();

 ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanServiceMock);
 ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanTaskService", partitionPlanTaskServiceMock);
 ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", scheduleServiceMock);
 }

 @Test
 public void testExecute() throws Exception {
 when(ScheduleTaskUtils.getScheduleId(jobExecutionContextMock)).thenReturn(1L);
 when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntityMock);

 partitionPlanJobParametersMock = new PartitionPlanJobParameters();
 partitionPlanJobParametersMock.setDatabasePartitionPlanId(1L);
 String json = JsonUtils.toJson(partitionPlanJobParametersMock);
 scheduleEntityMock.setJobParametersJson(json);

 databasePartitionPlanEntityMock = new DatabasePartitionPlanEntity();
 databasePartitionPlanEntityMock.setFlowInstanceId("flowInstanceId");
 databasePartitionPlanEntityMock.setConfigEnabled(true);
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L))
 .thenReturn(databasePartitionPlanEntityMock);

 tablePartitionPlansMock = new ArrayList<>();
 TablePartitionPlanEntity tablePartitionPlanEntityMock = new TablePartitionPlanEntity();
 tablePartitionPlansMock.add(tablePartitionPlanEntityMock);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L))
 .thenReturn(tablePartitionPlansMock);

 partitionPlanJob = new PartitionPlanJob();
 partitionPlanJob.execute(jobExecutionContextMock);

 verify(partitionPlanTaskServiceMock, times(1))
 .executePartitionPlan(databasePartitionPlanEntityMock.getFlowInstanceId(),
 tablePartitionPlansMock);
 }

 @Test
 public void testExecuteWithScheduleNotFound() throws Exception {
 when(ScheduleTaskUtils.getScheduleId(jobExecutionContextMock)).thenReturn(1L);
 when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(null);

 partitionPlanJob = new PartitionPlanJob();
 partitionPlanJob.execute(jobExecutionContextMock);

 verify(partitionPlanTaskServiceMock, never())
 .executePartitionPlan(anyString(), anyList());
 }

 @Test
 public void testExecuteWithDatabasePartitionPlanDisabled() throws Exception {
 when(ScheduleTaskUtils.getScheduleId(jobExecutionContextMock)).thenReturn(1L);
 when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntityMock);

 partitionPlanJobParametersMock = new PartitionPlanJobParameters();
 partitionPlanJobParametersMock.setDatabasePartitionPlanId(1L);
 String json = JsonUtils.toJson(partitionPlanJobParametersMock);
 scheduleEntityMock.setJobParametersJson(json);

 databasePartitionPlanEntityMock = new DatabasePartitionPlanEntity();
 databasePartitionPlanEntityMock.setFlowInstanceId("flowInstanceId");
 databasePartitionPlanEntityMock.setConfigEnabled(false);
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L))
 .thenReturn(databasePartitionPlanEntityMock);

 tablePartitionPlansMock = new ArrayList<>();
 TablePartitionPlanEntity tablePartitionPlanEntityMock = new TablePartitionPlanEntity();
 tablePartitionPlansMock.add(tablePartitionPlanEntityMock);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L))
 .thenReturn(tablePartitionPlansMock);

 partitionPlanJob = new PartitionPlanJob();
 partitionPlanJob.execute(jobExecutionContextMock);

 verify(partitionPlanTaskServiceMock, never())
 .executePartitionPlan(anyString(), anyList());
 }

 @Test
 public void testExecuteWithNoTablePartitionPlans() throws Exception {
 when(ScheduleTaskUtils.getScheduleId(jobExecutionContextMock)).thenReturn(1L);
 when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntityMock);

 partitionPlanJobParametersMock = new PartitionPlanJobParameters();
 partitionPlanJobParametersMock.setDatabasePartitionPlanId(1L);
 String json = JsonUtils.toJson(partitionPlanJobParametersMock);
 scheduleEntityMock.setJobParametersJson(json);

 databasePartitionPlanEntityMock = new DatabasePartitionPlanEntity();
 databasePartitionPlanEntityMock.setFlowInstanceId("flowInstanceId");
 databasePartitionPlanEntityMock.setConfigEnabled(true);
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L))
 .thenReturn(databasePartitionPlanEntityMock);

 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(new ArrayList<>());

 partitionPlanJob = new PartitionPlanJob();
 partitionPlanJob.execute(jobExecutionContextMock);

 verify(partitionPlanTaskServiceMock, never())
 .executePartitionPlan(anyString(), anyList());
 }

 @Test
 public void testExecuteWithSchedulerException() throws Exception {
 when(ScheduleTaskUtils.getScheduleId(jobExecutionContextMock)).thenReturn(1L);
 when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntityMock);

 partitionPlanJobParametersMock = new PartitionPlanJobParameters();
 partitionPlanJobParametersMock.setDatabasePartitionPlanId(1L);
 String json = JsonUtils.toJson(partitionPlanJobParametersMock);
 scheduleEntityMock.setJobParametersJson(json);

 databasePartitionPlanEntityMock = new DatabasePartitionPlanEntity();
 databasePartitionPlanEntityMock.setFlowInstanceId("flowInstanceId");
 databasePartitionPlanEntityMock.setConfigEnabled(true);
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L))
 .thenReturn(databasePartitionPlanEntityMock);

 tablePartitionPlansMock = new ArrayList<>();
 TablePartitionPlanEntity tablePartitionPlanEntityMock = new TablePartitionPlanEntity();
 tablePartitionPlansMock.add(tablePartitionPlanEntityMock);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L))
 .thenReturn(tablePartitionPlansMock);

 doThrow(new SchedulerException("SchedulerException")).when(scheduleServiceMock).terminate(scheduleEntityMock);

 partitionPlanJob = new PartitionPlanJob();
 partitionPlanJob.execute(jobExecutionContextMock);

 verify(partitionPlanTaskServiceMock, never())
 .executePartitionPlan(anyString(), anyList());
 }

 @Test
 public void testExecuteWithException() throws Exception {
 when(ScheduleTaskUtils.getScheduleId(jobExecutionContextMock)).thenReturn(1L);
 when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntityMock);

 partitionPlanJobParametersMock = new PartitionPlanJobParameters();
 partitionPlanJobParametersMock.setDatabasePartitionPlanId(1L);
 String json = JsonUtils.toJson(partitionPlanJobParametersMock);
 scheduleEntityMock.setJobParametersJson(json);

 databasePartitionPlanEntityMock = new DatabasePartitionPlanEntity();
 databasePartitionPlanEntityMock.setFlowInstanceId("flowInstanceId");
 databasePartitionPlanEntityMock.setConfigEnabled(true);
 when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L))
 .thenReturn(databasePartitionPlanEntityMock);

 tablePartitionPlansMock = new ArrayList<>();
 TablePartitionPlanEntity tablePartitionPlanEntityMock = new TablePartitionPlanEntity();
 tablePartitionPlansMock.add(tablePartitionPlanEntityMock);
 when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L))
 .thenReturn(tablePartitionPlansMock);

 doThrow(new Exception("Exception")).when(partitionPlanTaskServiceMock)
 .executePartitionPlan(databasePartitionPlanEntityMock.getFlowInstanceId(), tablePartitionPlansMock);

 partitionPlanJob = new PartitionPlanJob();
 partitionPlanJob.execute(jobExecutionContextMock);

 verify(partitionPlanTaskServiceMock, times(1))
 .executePartitionPlan(databasePartitionPlanEntityMock.getFlowInstanceId(),
 tablePartitionPlansMock);
 }

 @Test
 public void testInterrupt() throws Exception {
 partitionPlanJob = new PartitionPlanJob();
 partitionPlanJob.interrupt();
 }

}