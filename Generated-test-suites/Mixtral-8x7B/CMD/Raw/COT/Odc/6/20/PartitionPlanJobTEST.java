

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.matchers.GroupMatcher;
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
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private SpringContextUtil springContextUtil;

    @Mock
    private JobExecutionContext jobExecutionContext;

    private ScheduleEntity scheduleEntity;

    private PartitionPlanJobParameters partitionPlanJobParameters;

    private List<TablePartitionPlanEntity> tablePartitionPlans;

    private DatabasePartitionPlanEntity databasePartitionPlanEntity;

    @Before
    public void setUp() {
        scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\": \"123\"}");

        partitionPlanJobParameters = new PartitionPlanJobParameters();
        partitionPlanJobParameters.setDatabasePartitionPlanId(123L);

        tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity());

        databasePartitionPlanEntity = new DatabasePartitionPlanEntity();
        databasePartitionPlanEntity.setFlowInstanceId(UUID.randomUUID().toString());
        databasePartitionPlanEntity.setConfigEnabled(true);

        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanTaskService", partitionPlanTaskService);
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanService);
        ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", scheduleService);
        ReflectionTestUtils.setField(partitionPlanJob, "springContextUtil", springContextUtil);
    }

    @Test
    public void testExecuteWithValidData() throws Exception {
        when(springContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
        when(springContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
        when(springContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);

        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class))
                .thenReturn(partitionPlanJobParameters);

        when(partitionPlanService.getDatabasePartitionPlanById(partitionPlanJobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlanEntity);

        when(partitionPlanService
                .getValidTablePlanByDatabasePartitionPlanId(partitionPlanJobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanTaskService, times(1)).executePartitionPlan(
                databasePartitionPlanEntity.getFlowInstanceId(), tablePartitionPlans);
    }

    @Test
    public void testExecuteWithScheduleNotFound() throws Exception {
        when(springContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
        when(springContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
        when(springContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);

        when(scheduleService.nullSafeGetById(anyLong())).thenThrow(new Exception("Schedule not found"));

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanTaskService, times(0)).executePartitionPlan(any(), any());
    }

    @Test
    public void testExecuteWithNoTablePlans() throws Exception {
        when(springContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
        when(springContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
        when(springContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);

        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class))
                .thenReturn(partitionPlanJobParameters);

        when(partitionPlanService.getDatabasePartitionPlanById(partitionPlanJobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlanEntity);

        when(partitionPlanService
                .getValidTablePlanByDatabasePartitionPlanId(partitionPlanJobParameters.getDatabasePartitionPlanId()))
                .thenReturn(Collections.emptyList());

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanTaskService, times(0)).executePartitionPlan(any(), any());
    }

    @Test
    public void testExecuteWithPartitionPlanFailed() throws Exception {
        when(springContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
        when(springContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
        when(springContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);

        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class))
                .thenReturn(partitionPlanJobParameters);

        when(partitionPlanService.getDatabasePartitionPlanById(partitionPlanJobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlanEntity);

        when(partitionPlanService
                .getValidTablePlanByDatabasePartitionPlanId(partitionPlanJobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);

        Mockito.doThrow(new Exception("Create partition-plan database change task failed"))
                .when(partitionPlanTaskService).executePartitionPlan(any(), any());

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanTaskService, times(1)).executePartitionPlan(any(), any());
    }

    @Test
    public void testInterrupt() {
        assertThrows(UnExpectedException.class, () -> partitionPlanJob.interrupt());
    }

    @Test
    public void testGetJobGroup() {
        assertEquals("PartitionPlanJob", partitionPlanJob.getJobGroup());
    }

    @Test
    public void testGetJobName() {
        assertEquals("PartitionPlanJob", partitionPlanJob.getJobName());
    }

    @Test
    public void testGetTrigger() throws SchedulerException {
        Trigger trigger = TriggerBuilder.newTrigger().build();
        when(jobExecutionContext.getTrigger()).thenReturn(trigger);

        assertNotNull(partitionPlanJob.getTrigger(jobExecutionContext));
    }

    @Test
    public void testIsDurable() {
        assertTrue(partitionPlanJob.isDurable());
    }

    @Test
    public void testIsPersistJobDataAfterExecution() {
        assertTrue(partitionPlanJob.isPersistJobDataAfterExecution());
    }

    @Test
    public void testRequiresReclone() {
        assertFalse(partitionPlanJob.requiresReclone());
    }
}