import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskServiceImpl;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskStatus;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskStatus.Status;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskStatusService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskStatusServiceImpl;
import com.oceanbase.odc.service.quartz.QuartzManager;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleServiceImpl;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

@SuppressWarnings("unchecked")
public class PartitionPlanJobTest {

    private PartitionPlanJob sut;

    private PartitionPlanTaskService partitionPlanTaskService;

    private PartitionPlanService partitionPlanService;

    private ScheduleService scheduleService;

    private PartitionPlanTaskStatusService partitionPlanTaskStatusService;

    private QuartzManager quartzManager;

    @Before
    public void setUp() {
        partitionPlanTaskService = mock(PartitionPlanTaskServiceImpl.class);
        partitionPlanService = mock(PartitionPlanService.class);
        scheduleService = mock(ScheduleServiceImpl.class);
        partitionPlanTaskStatusService = mock(PartitionPlanTaskStatusServiceImpl.class);
        quartzManager = mock(QuartzManager.class);

        sut = new PartitionPlanJob() {
            @Override
            public void interrupt() {
                // do nothing
            }
        };
        sut.setPartitionPlanTaskService(partitionPlanTaskService);
        sut.setPartitionPlanService(partitionPlanService);
        sut.setScheduleService(scheduleService);
        sut.setPartitionPlanTaskStatusService(partitionPlanTaskStatusService);
        sut.setQuartzManager(quartzManager);
    }

    @Test
    public void testExecute_schedule_not_found() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getMergedJobDataMap().get(ScheduleTaskUtils.SCHEDULE_ID_KEY)).thenReturn("1");
        when(scheduleService.nullSafeGetById(1L)).thenReturn(null);

        sut.execute(context);

        // schedule not found, so nothing should happen
    }

    @Test
    public void testExecute_database_partition_plan_disabled() throws Exception {
        JobExecutionContext context = mock(JobExecutionContext.class);
        Long scheduleId = 1L;
        when(context.getMergedJobDataMap().get(ScheduleTaskUtils.SCHEDULE_ID_KEY)).thenReturn(scheduleId);
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleEntity.getStatus()).thenReturn(ScheduleStatus.RUNNING);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        PartitionPlanJobParameters jobParameters = mock(PartitionPlanJobParameters.class);
        when(jobParameters.getDatabasePartitionPlanId()).thenReturn(1L);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(jobParameters));
        DatabasePartitionPlanEntity databasePartitionPlan =
                mock(DatabasePartitionPlanEntity.class);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(false);
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);

        sut.execute(context);

        // database partition plan is disabled, so stop the partition-plan job
        Assert.assertEquals(1, quartzManager.getTriggersByJobKey(context.getJobDetail().getKey())
                .size());
        TriggerKey triggerKey = new TriggerKey(context.getJobDetail().getKey().getName(),
                context.getJobDetail().getKey().getGroup());
        Trigger trigger = quartzManager.getTrigger(triggerKey);
        Assert.assertEquals(CompletedExecutionInstruction.DELETE, trigger.getCompletionInstruction());
    }

    @Test
    public void testExecute_no_table_found() throws Exception {
        JobExecutionContext context = mock(JobExecutionContext.class);
        Long scheduleId = 1L;
        when(context.getMergedJobDataMap().get(ScheduleTaskUtils.SCHEDULE_ID_KEY)).thenReturn(scheduleId);
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleEntity.getStatus()).thenReturn(ScheduleStatus.RUNNING);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        PartitionPlanJobParameters jobParameters = mock(PartitionPlanJobParameters.class);
        when(jobParameters.getDatabasePartitionPlanId()).thenReturn(1L);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(jobParameters));
        DatabasePartitionPlanEntity databasePartitionPlan =
                mock(DatabasePartitionPlanEntity.class);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        when(
                partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L))
                        .thenReturn(tablePartitionPlans);

        sut.execute(context);

        // no table found, so stop the partition-plan job
        Assert.assertEquals(1, quartzManager.getTriggersByJobKey(context.getJobDetail().getKey())
                .size());
        TriggerKey triggerKey = new TriggerKey(context.getJobDetail().getKey().getName(),
                context.getJobDetail().getKey().getGroup());
        Trigger trigger = quartzManager.getTrigger(triggerKey);
        Assert.assertEquals(CompletedExecutionInstruction.DELETE, trigger.getCompletionInstruction());
    }

    @Test
    public void testExecute_partition_plan_success() throws Exception {
        JobExecutionContext context = mock(JobExecutionContext.class);
        Long scheduleId = 1L;
        when(context.getMergedJobDataMap().get(ScheduleTaskUtils.SCHEDULE_ID_KEY)).thenReturn(scheduleId);
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleEntity.getStatus()).thenReturn(ScheduleStatus.RUNNING);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        PartitionPlanJobParameters jobParameters = mock(PartitionPlanJobParameters.class);
        when(jobParameters.getDatabasePartitionPlanId()).thenReturn(1L);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(jobParameters));
        DatabasePartitionPlanEntity databasePartitionPlan =
                mock(DatabasePartitionPlanEntity.class);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        TablePartitionPlanEntity tablePartitionPlan1 = mock(TablePartitionPlanEntity.class);
        when(tablePartitionPlan1.isConfigEnabled()).thenReturn(true);
        TablePartitionPlanEntity tablePartitionPlan2 = mock(TablePartitionPlanEntity.class);
        when(tablePartitionPlan2.isConfigEnabled()).thenReturn(true);
        List<TablePartitionPlanEntity> tablePartitionPlans =
                List.of(tablePartitionPlan1, tablePartitionPlan2);
        when(
                partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L))
                        .thenReturn(tablePartitionPlans);

        when(partitionPlanTaskService.executePartitionPlan(1L, tablePartitionPlans))
                .thenReturn(List.of(new PartitionPlanTaskStatus(1L, Status.SUCCESS)));

        sut.execute(context);

        // partition plan success, so do nothing
        Assert.assertEquals(1, quartzManager.getTriggersByJobKey(context.getJobDetail().getKey())
                .size());
        TriggerKey triggerKey = new TriggerKey(context.getJobDetail().getKey().getName(),
                context.getJobDetail().getKey().getGroup());
        Trigger trigger = quartzManager.getTrigger(triggerKey);
        Assert.assertEquals(CompletedExecutionInstruction.NOOP, trigger.getCompletionInstruction());
    }

    @Test
    public void testExecute_partition_plan_failure() throws Exception {
        JobExecutionContext context = mock(JobExecutionContext.class);
        Long scheduleId = 1L;
        when(context.getMergedJobDataMap().get(ScheduleTaskUtils.SCHEDULE_ID_KEY)).thenReturn(scheduleId);
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleEntity.getStatus()).thenReturn(ScheduleStatus.RUNNING);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        PartitionPlanJobParameters jobParameters = mock(PartitionPlanJobParameters.class);
        when(jobParameters.getDatabasePartitionPlanId()).thenReturn(1L);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(jobParameters));
        DatabasePartitionPlanEntity databasePartitionPlan =
                mock(DatabasePartitionPlanEntity.class);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        TablePartitionPlanEntity tablePartitionPlan1 = mock(TablePartitionPlanEntity.class);
        when(tablePartitionPlan1.isConfigEnabled()).thenReturn(true);
        TablePartitionPlanEntity tablePartitionPlan2 = mock(TablePartitionPlanEntity.class);
        when(tablePartitionPlan2.isConfigEnabled()).thenReturn(true);
        List<TablePartitionPlanEntity> tablePartitionPlans =
                List.of(tablePartitionPlan1, tablePartitionPlan2);
        when(
                partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L))
                        .thenReturn(tablePartitionPlans);

        when(partitionPlanTaskService.executePartitionPlan(1L, tablePartitionPlans))
                .thenReturn(List.of(new PartitionPlanTaskStatus(1L, Status.FAILURE)));

        sut.execute(context);

        // partition plan failure, so stop the partition-plan job
        Assert.assertEquals(1, quartzManager.getTriggersByJobKey(context.getJobDetail().getKey())
                .size());
        TriggerKey triggerKey = new TriggerKey(context.getJobDetail().getKey().getName(),
                context.getJobDetail().getKey().getGroup());
        Trigger trigger = quartzManager.getTrigger(triggerKey);
        Assert.assertEquals(CompletedExecutionInstruction.DELETE, trigger.getCompletionInstruction());
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() throws Exception {
        sut.interrupt();
    }
}