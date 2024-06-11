package com.oceanbase.odc.service.schedule.job;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

@SuppressWarnings("unchecked")
public class PartitionPlanJobTest {

    private static final long SCHEDULE_ID = 1L;

    private static final String JOB_PARAMETERS_JSON = "{}";

    private static final String FLOW_INSTANCE_ID = "123456";

    private static final String DATABASE_PARTITION_PLAN_ID = "1001";

    private ApplicationContext applicationContext;

    private PartitionPlanService partitionPlanService;

    private PartitionPlanTaskService partitionPlanTaskService;

    private ScheduleService scheduleService;

    private PartitionPlanJob partitionPlanJob;

    private JobExecutionContext jobExecutionContext;

    @Before
    public void setup() {
        applicationContext = mock(ApplicationContext.class);
        partitionPlanService = mock(PartitionPlanService.class);
        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        scheduleService = mock(ScheduleService.class);
        SpringContextUtil.setApplicationContext(applicationContext);
        when(applicationContext.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
        when(applicationContext.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
        when(applicationContext.getBean(ScheduleService.class)).thenReturn(scheduleService);

        partitionPlanJob = new PartitionPlanJob();

        jobExecutionContext = mock(JobExecutionContext.class);
        when(jobExecutionContext.getMergedJobDataMap().getLong(ScheduleTaskUtils.SCHEDULE_ID_KEY))
                .thenReturn(SCHEDULE_ID);
        when(jobExecutionContext.getMergedJobDataMap().getString(ScheduleTaskUtils.JOB_PARAMETERS_JSON_KEY))
                .thenReturn(JOB_PARAMETERS_JSON);
    }

    @Test
    public void testExecute() throws Exception {
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(DATABASE_PARTITION_PLAN_ID);
        when(JsonUtils.fromJson(JOB_PARAMETERS_JSON, PartitionPlanJobParameters.class))
                .thenReturn(jobParameters);

        DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        when(partitionPlanService.getDatabasePartitionPlanById(DATABASE_PARTITION_PLAN_ID))
                .thenReturn(databasePartitionPlan);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        TablePartitionPlanEntity tablePartitionPlan = mock(TablePartitionPlanEntity.class);
        tablePartitionPlans.add(tablePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(DATABASE_PARTITION_PLAN_ID))
                .thenReturn(tablePartitionPlans);

        doNothing().when(partitionPlanTaskService).executePartitionPlan(FLOW_INSTANCE_ID, tablePartitionPlans);

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(DATABASE_PARTITION_PLAN_ID);
        verify(partitionPlanService, times(1))
                .getValidTablePlanByDatabasePartitionPlanId(DATABASE_PARTITION_PLAN_ID);
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(FLOW_INSTANCE_ID, tablePartitionPlans);
    }

    @Test
    public void testExecute_scheduleNotFound() throws Exception {
        when(scheduleService.nullSafeGetById(anyLong())).thenThrow(new Exception());

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanService, times(0)).getDatabasePartitionPlanById(anyLong());
        verify(partitionPlanService, times(0))
                .getValidTablePlanByDatabasePartitionPlanId(anyLong());
        verify(partitionPlanTaskService, times(0)).executePartitionPlan(anyString(), anyList());
    }

    @Test
    public void testExecute_databasePartitionPlanNotFound() throws Exception {
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(DATABASE_PARTITION_PLAN_ID);
        when(JsonUtils.fromJson(JOB_PARAMETERS_JSON, PartitionPlanJobParameters.class))
                .thenReturn(jobParameters);

        when(partitionPlanService.getDatabasePartitionPlanById(DATABASE_PARTITION_PLAN_ID))
                .thenThrow(new Exception());

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(DATABASE_PARTITION_PLAN_ID);
        verify(partitionPlanService, times(0))
                .getValidTablePlanByDatabasePartitionPlanId(DATABASE_PARTITION_PLAN_ID);
        verify(partitionPlanTaskService, times(0)).executePartitionPlan(anyString(), anyList());
    }

    @Test
    public void testExecute_noTablePartitionPlans() throws Exception {
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(DATABASE_PARTITION_PLAN_ID);
        when(JsonUtils.fromJson(JOB_PARAMETERS_JSON, PartitionPlanJobParameters.class))
                .thenReturn(jobParameters);

        DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        when(partitionPlanService.getDatabasePartitionPlanById(DATABASE_PARTITION_PLAN_ID))
                .thenReturn(databasePartitionPlan);

        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(DATABASE_PARTITION_PLAN_ID))
                .thenReturn(Collections.emptyList());

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(DATABASE_PARTITION_PLAN_ID);
        verify(partitionPlanService, times(1))
                .getValidTablePlanByDatabasePartitionPlanId(DATABASE_PARTITION_PLAN_ID);
        verify(partitionPlanTaskService, times(0)).executePartitionPlan(anyString(), anyList());
    }

    @Test
    public void testExecute_executePartitionPlanException() throws Exception {
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(DATABASE_PARTITION_PLAN_ID);
        when(JsonUtils.fromJson(JOB_PARAMETERS_JSON, PartitionPlanJobParameters.class))
                .thenReturn(jobParameters);

        DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        when(partitionPlanService.getDatabasePartitionPlanById(DATABASE_PARTITION_PLAN_ID))
                .thenReturn(databasePartitionPlan);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        TablePartitionPlanEntity tablePartitionPlan = mock(TablePartitionPlanEntity.class);
        tablePartitionPlans.add(tablePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(DATABASE_PARTITION_PLAN_ID))
                .thenReturn(tablePartitionPlans);

        doThrow(new Exception()).when(partitionPlanTaskService).executePartitionPlan(FLOW_INSTANCE_ID,
                tablePartitionPlans);

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(DATABASE_PARTITION_PLAN_ID);
        verify(partitionPlanService, times(1))
                .getValidTablePlanByDatabasePartitionPlanId(DATABASE_PARTITION_PLAN_ID);
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(FLOW_INSTANCE_ID, tablePartitionPlans);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() throws Exception {
        partitionPlanJob.interrupt();
    }
}