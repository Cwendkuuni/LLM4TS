package com.oceanbase.odc.service.schedule.job;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unchecked")
public class PartitionPlanJobTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PartitionPlanJobTest.class);

    private PartitionPlanJob partitionPlanJob;

    private PartitionPlanService partitionPlanService;

    private PartitionPlanTaskService partitionPlanTaskService;

    private ScheduleService scheduleService;

    private SpringContextUtil springContextUtil;

    private ScheduleTaskUtils scheduleTaskUtils;

    private JobExecutionContext jobExecutionContext;

    @Before
    public void setUp() {
        partitionPlanService = mock(PartitionPlanService.class);
        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        scheduleService = mock(ScheduleService.class);
        springContextUtil = mock(SpringContextUtil.class);
        scheduleTaskUtils = mock(ScheduleTaskUtils.class);

        when(springContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
        when(springContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
        when(springContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);

        partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.setPartitionPlanTaskService(partitionPlanTaskService);
        partitionPlanJob.setPartitionPlanService(partitionPlanService);
        partitionPlanJob.setScheduleService(scheduleService);
        partitionPlanJob.setSpringContextUtil(springContextUtil);
        partitionPlanJob.setScheduleTaskUtils(scheduleTaskUtils);

        jobExecutionContext = mock(JobExecutionContext.class);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        try {
            when(scheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(scheduleEntity.getId());
        } catch (SchedulerException e) {
            LOGGER.warn("Failed to get scheduleId from jobExecutionContext.", e);
        }
    }

    @Test
    public void testExecute_scheduleEntityNull() throws Exception {
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(null);

        partitionPlanJob.execute(jobExecutionContext);

        verify(scheduleService).terminate(any(ScheduleEntity.class));
    }

    @Test
    public void testExecute_validPartitionPlan() throws Exception {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        String jobParametersJson = JsonUtils.toJson(jobParameters);
        scheduleEntity.setJobParametersJson(jobParametersJson);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);

        TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(tablePartitionPlan);

        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(tablePartitionPlans);
        doNothing().when(partitionPlanTaskService).executePartitionPlan(
                anyString(), anyList());

        partitionPlanJob.execute(jobExecutionContext);

        verify(scheduleService).terminate(scheduleEntity);
        verify(partitionPlanTaskService).executePartitionPlan(
                databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
    }

    @Test
    public void testExecute_invalidPartitionPlan() throws Exception {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        String jobParametersJson = JsonUtils.toJson(jobParameters);
        scheduleEntity.setJobParametersJson(jobParametersJson);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(false);

        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong()))
                .thenReturn(databasePartitionPlan);

        partitionPlanJob.execute(jobExecutionContext);

        verify(scheduleService).terminate(scheduleEntity);
    }

    @Test
    public void testExecute_parseJobParametersJsonException() throws Exception {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);

        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);

        when(jobExecutionContext.getMergedJobDataMap().getString("jobParameters")).thenReturn("invalidJson");

        partitionPlanJob.execute(jobExecutionContext);

        verify(scheduleService).terminate(scheduleEntity);
    }

    @Test
    public void testExecute_JsonProcessingException() throws Exception {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        String jobParametersJson = JsonUtils.toJson(jobParameters);
        scheduleEntity.setJobParametersJson(jobParametersJson);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);

        TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(tablePartitionPlan);

        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(tablePartitionPlans);
        doThrow(JsonProcessingException.class)
                .when(partitionPlanTaskService).executePartitionPlan(
                        anyString(), anyList());

        partitionPlanJob.execute(jobExecutionContext);

        verify(scheduleService).terminate(scheduleEntity);
    }

    @Test
    public void testExecute_JsonMappingException() throws Exception {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        String jobParametersJson = JsonUtils.toJson(jobParameters);
        scheduleEntity.setJobParametersJson(jobParametersJson);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);

        TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(tablePartitionPlan);

        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(tablePartitionPlans);
        doThrow(JsonMappingException.class)
                .when(partitionPlanTaskService).executePartitionPlan(
                        anyString(), anyList());

        partitionPlanJob.execute(jobExecutionContext);

        verify(scheduleService).terminate(scheduleEntity);
    }

    @Test
    public void testExecute_SchedulerException() throws Exception {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        String jobParametersJson = JsonUtils.toJson(jobParameters);
        scheduleEntity.setJobParametersJson(jobParametersJson);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);

        TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(tablePartitionPlan);

        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(tablePartitionPlans);
        doThrow(SchedulerException.class)
                .when(scheduleService).terminate(any(ScheduleEntity.class));

        partitionPlanJob.execute(jobExecutionContext);

        verify(scheduleService).terminate(scheduleEntity);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() throws Exception {
        partitionPlanJob.interrupt();
    }
}