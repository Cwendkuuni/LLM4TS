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
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.quartz.DateBuilder.evenMinuteDateAfter;

public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;
    private PartitionPlanTaskService partitionPlanTaskService;
    private PartitionPlanService partitionPlanService;
    private ScheduleService scheduleService;
    private JobExecutionContext jobExecutionContext;
    private ApplicationContext applicationContext;

    @Before
    public void setUp() {
        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        partitionPlanService = mock(PartitionPlanService.class);
        scheduleService = mock(ScheduleService.class);

        applicationContext = mock(ApplicationContext.class);
        when(applicationContext.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
        when(applicationContext.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
        when(applicationContext.getBean(ScheduleService.class)).thenReturn(scheduleService);

        SpringContextUtil.setApplicationContext(applicationContext);

        partitionPlanJob = new PartitionPlanJob();

        jobExecutionContext = mock(JobExecutionContext.class);
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger", "group")
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInMinutes(1)
                        .repeatForever())
                .build();

        when(jobExecutionContext.getTrigger()).thenReturn(trigger);
        when(jobExecutionContext.getMergedJobDataMap()).thenReturn(new org.quartz.JobDataMap());
        when(jobExecutionContext.getScheduler().getSchedulerInstanceId())
                .thenReturn("some valid Scheduler Instance Id");
        when(jobExecutionContext.getScheduler().getTimeZone()).thenReturn(TimeZone.getDefault());
    }

    @Test
    public void testExecute() throws Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleEntity.getId()).thenReturn(scheduleId);
        when(scheduleEntity.getJobParametersJson()).thenReturn("{}");
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);

        DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
        when(databasePartitionPlan.getFlowInstanceId()).thenReturn("123");
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(mock(TablePartitionPlanEntity.class));
        when(partitionPlanService
                .getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanTaskService, times(1))
                .executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
    }

    @Test
    public void testExecute_scheduleNotFound() throws Exception {
        Long scheduleId = 1L;

        when(scheduleService.nullSafeGetById(scheduleId)).thenThrow(new Exception());

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanTaskService, never())
                .executePartitionPlan(anyString(), anyList());
    }

    @Test
    public void testExecute_databasePartitionPlanDisabled() throws Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleEntity.getId()).thenReturn(scheduleId);
        when(scheduleEntity.getJobParametersJson()).thenReturn("{}");
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);

        DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(false);
        when(databasePartitionPlan.getFlowInstanceId()).thenReturn("123");
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanTaskService, never())
                .executePartitionPlan(anyString(), anyList());
    }

    @Test
    public void testExecute_noTablePartitionPlans() throws Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleEntity.getId()).thenReturn(scheduleId);
        when(scheduleEntity.getJobParametersJson()).thenReturn("{}");
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);

        DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
        when(databasePartitionPlan.getFlowInstanceId()).thenReturn("123");
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);

        when(partitionPlanService
                .getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(Collections.emptyList());

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanTaskService, never())
                .executePartitionPlan(anyString(), anyList());
    }

    @Test
    public void testInterrupt() {
        assertThrows(UnExpectedException.class, () -> partitionPlanJob.interrupt());
    }

    private SimpleScheduleBuilder simpleSchedule() {
        return SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInMinutes(1)
                .repeatForever();
    }

}