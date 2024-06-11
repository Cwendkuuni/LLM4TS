package com.oceanbase.odc.service.schedule.job;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.ScheduleJob;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PartitionPlanJobTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PartitionPlanJobTest.class);

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private SpringContextUtil springContextUtil;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @Mock
    private ScheduleEntity scheduleEntity;

    @Mock
    private Trigger trigger;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        partitionPlanJob = spy(partitionPlanJob);
    }

    @Test
    public void testExecute() throws Exception {
        long scheduleId = 1L;
        when(scheduleEntity.getId()).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(jobExecutionContext.getMergedJobDataMap().get(ScheduleJob.SCHEDULE_ID)).thenReturn(scheduleId);
        when(springContextUtil.getBean(PartitionPlanTaskService.class))
                .thenReturn(partitionPlanTaskService);
        when(springContextUtil.getBean(PartitionPlanService.class))
                .thenReturn(partitionPlanService);
        when(springContextUtil.getBean(ScheduleService.class))
                .thenReturn(scheduleService);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        long databasePartitionPlanId = 1L;
        jobParameters.setDatabasePartitionPlanId(databasePartitionPlanId);
        when(JsonUtils.fromJson(anyString(), PartitionPlanJobParameters.class))
                .thenReturn(jobParameters);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);
        when(partitionPlanService.getDatabasePartitionPlanById(databasePartitionPlanId))
                .thenReturn(databasePartitionPlan);

        TablePartitionPlanEntity tablePartitionPlan1 = new TablePartitionPlanEntity();
        TablePartitionPlanEntity tablePartitionPlan2 = new TablePartitionPlanEntity();
        List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(tablePartitionPlan1, tablePartitionPlan2);
        when(partitionPlanService
                .getValidTablePlanByDatabasePartitionPlanId(databasePartitionPlanId))
                .thenReturn(tablePartitionPlans);

        doNothing().when(partitionPlanTaskService)
                .executePartitionPlan(anyString(), any());

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanService).getDatabasePartitionPlanById(databasePartitionPlanId);
        verify(partitionPlanService)
                .getValidTablePlanByDatabasePartitionPlanId(databasePartitionPlanId);
        verify(partitionPlanTaskService)
                .executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
    }

    @Test
    public void testExecute_scheduleNotFound() throws Exception {
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(null);
        when(jobExecutionContext.getMergedJobDataMap().get(ScheduleJob.SCHEDULE_ID))
                .thenReturn(1L);

        partitionPlanJob.execute(jobExecutionContext);

        verify(scheduleService).nullSafeGetById(anyLong());
        verify(partitionPlanTaskService).executePartitionPlan(anyString(), any());
        verify(scheduleService).terminate(scheduleEntity);
    }

    @Test
    public void testExecute_databasePartitionPlanNotFound() throws Exception {
        long scheduleId = 1L;
        when(scheduleEntity.getId()).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(jobExecutionContext.getMergedJobDataMap().get(ScheduleJob.SCHEDULE_ID)).thenReturn(scheduleId);
        when(springContextUtil.getBean(PartitionPlanTaskService.class))
                .thenReturn(partitionPlanTaskService);
        when(springContextUtil.getBean(PartitionPlanService.class))
                .thenReturn(partitionPlanService);
        when(springContextUtil.getBean(ScheduleService.class))
                .thenReturn(scheduleService);

        when(JsonUtils.fromJson(anyString(), PartitionPlanJobParameters.class))
                .thenReturn(new PartitionPlanJobParameters());

        when(partitionPlanService.getDatabasePartitionPlanById(anyLong()))
                .thenReturn(null);

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanService).getDatabasePartitionPlanById(anyLong());
        verify(scheduleService).terminate(scheduleEntity);
    }

    @Test
    public void testExecute_noTablePartitionPlans() throws Exception {
        long scheduleId = 1L;
        when(scheduleEntity.getId()).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(jobExecutionContext.getMergedJobDataMap().get(ScheduleJob.SCHEDULE_ID)).thenReturn(scheduleId);
        when(springContextUtil.getBean(PartitionPlanTaskService.class))
                .thenReturn(partitionPlanTaskService);
        when(springContextUtil.getBean(PartitionPlanService.class))
                .thenReturn(partitionPlanService);
        when(springContextUtil.getBean(ScheduleService.class))
                .thenReturn(scheduleService);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        long databasePartitionPlanId = 1L;
        jobParameters.setDatabasePartitionPlanId(databasePartitionPlanId);
        when(JsonUtils.fromJson(anyString(), PartitionPlanJobParameters.class))
                .thenReturn(jobParameters);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);
        when(partitionPlanService.getDatabasePartitionPlanById(databasePartitionPlanId))
                .thenReturn(databasePartitionPlan);

        when(partitionPlanService
                .getValidTablePlanByDatabasePartitionPlanId(databasePartitionPlanId))
                .thenReturn(Arrays.asList());

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanService).getDatabasePartitionPlanById(databasePartitionPlanId);
        verify(partitionPlanService)
                .getValidTablePlanByDatabasePartitionPlanId(databasePartitionPlanId);
        verify(scheduleService).terminate(scheduleEntity);
    }

    @Test
    public void testExecute_interrupt() throws Exception {
        when(jobExecutionContext.getMergedJobDataMap().get(ScheduleJob.SCHEDULE_ID))
                .thenReturn(1L);

        doThrow(UnExpectedException.class).when(partitionPlanJob).interrupt();

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanJob).interrupt();
    }
}