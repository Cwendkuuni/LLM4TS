package com.oceanbase.odc.service.schedule.job;

import static org.mockito.Mockito.*;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.JobDetailBuilder;

@SuppressWarnings("unchecked")
public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;

    private PartitionPlanTaskService partitionPlanTaskService;

    private PartitionPlanService partitionPlanService;

    private ScheduleService scheduleService;

    private ApplicationContext applicationContext;

    @Before
    public void setup() {
        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        partitionPlanService = mock(PartitionPlanService.class);
        scheduleService = mock(ScheduleService.class);
        applicationContext = mock(ApplicationContext.class);
        SpringContextUtil.setApplicationContext(applicationContext);
        when(applicationContext.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
        when(applicationContext.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
        when(applicationContext.getBean(ScheduleService.class)).thenReturn(scheduleService);

        partitionPlanJob = new PartitionPlanJob();
    }

    @Test
    public void testExecute() throws Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        String json = JsonUtils.toJson(jobParameters);
        scheduleEntity.setJobParametersJson(json);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);

        TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
        tablePartitionPlan.setId(1L);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(tablePartitionPlan);

        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);
        when(applicationContext.getBean(StdSchedulerFactory.class)).thenReturn(new StdSchedulerFactory());

        JobExecutionContext context = new JobExecutionContext();
        context.setMergedJobDataMap(jobParameters.toJobDataMap());
        context.setScheduler(mock(context.getScheduler().getClass()));

        partitionPlanJob.execute(context);

        verify(partitionPlanTaskService, times(1))
                .executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
    }

    @Test
    public void testExecute_whenScheduleIsNotFound() throws Exception {
        Long scheduleId = 1L;

        when(scheduleService.nullSafeGetById(scheduleId)).thenThrow(new Exception());

        JobExecutionContext context = new JobExecutionContext();
        context.setMergedJobDataMap(new PartitionPlanJobParameters().toJobDataMap());
        context.setScheduler(mock(context.getScheduler().getClass()));

        partitionPlanJob.execute(context);

        verify(partitionPlanTaskService, never()).executePartitionPlan(anyLong(), anyList());
    }

    @Test
    public void testExecute_whenDatabasePartitionPlanIsNotConfigured() throws Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        String json = JsonUtils.toJson(jobParameters);
        scheduleEntity.setJobParametersJson(json);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(false);

        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);

        JobExecutionContext context = new JobExecutionContext();
        context.setMergedJobDataMap(jobParameters.toJobDataMap());
        context.setScheduler(mock(context.getScheduler().getClass()));

        partitionPlanJob.execute(context);

        verify(partitionPlanTaskService, never()).executePartitionPlan(anyLong(), anyList());
    }

    @Test
    public void testExecute_whenTablePartitionPlansIsEmpty() throws Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        String json = JsonUtils.toJson(jobParameters);
        scheduleEntity.setJobParametersJson(json);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);

        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(new ArrayList<>());

        JobExecutionContext context = new JobExecutionContext();
        context.setMergedJobDataMap(jobParameters.toJobDataMap());
        context.setScheduler(mock(context.getScheduler().getClass()));

        partitionPlanJob.execute(context);

        verify(partitionPlanTaskService, never()).executePartitionPlan(anyLong(), anyList());
    }

    @Test
    public void testInterrupt() {
        PartitionPlanJob partitionPlanJobSpy = spy(partitionPlanJob);
        partitionPlanJobSpy.interrupt();

        verify(partitionPlanJobSpy, times(1)).interrupt();
    }
}