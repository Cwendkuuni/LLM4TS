package com.oceanbase.odc.service.schedule.job;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

@SuppressWarnings("unchecked")
public class PartitionPlanJobTest {

    private static final String SCHEDULE_ID = "123";
    private static final String JOB_PARAMETERS_JSON = "{\"databasePartitionPlanId\": 456}";
    private static final Long DATABASE_PARTITION_PLAN_ID = 456L;
    private static final String FLOW_INSTANCE_ID = "789";
    private static final List<TablePartitionPlanEntity> TABLE_PARTITION_PLANS = new ArrayList<>();
    private static final ScheduleEntity SCHEDULE_ENTITY = new ScheduleEntity();
    private static final DatabasePartitionPlanEntity DATABASE_PARTITION_PLAN = new DatabasePartitionPlanEntity();

    static {
        TABLE_PARTITION_PLANS.add(new TablePartitionPlanEntity());
        SCHEDULE_ENTITY.setId(Long.parseLong(SCHEDULE_ID));
        SCHEDULE_ENTITY.setJobParametersJson(JOB_PARAMETERS_JSON);
        DATABASE_PARTITION_PLAN.setId(DATABASE_PARTITION_PLAN_ID);
        DATABASE_PARTITION_PLAN.setFlowInstanceId(FLOW_INSTANCE_ID);
    }

    private PartitionPlanJob partitionPlanJob;
    private PartitionPlanTaskService partitionPlanTaskServiceMock;
    private PartitionPlanService partitionPlanServiceMock;
    private ScheduleService scheduleServiceMock;
    private JobExecutionContext jobExecutionContextMock;
    private ApplicationContext applicationContextMock;

    @Before
    public void setup() {
        partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
        partitionPlanServiceMock = mock(PartitionPlanService.class);
        scheduleServiceMock = mock(ScheduleService.class);
        jobExecutionContextMock = mock(JobExecutionContext.class);
        applicationContextMock = mock(ApplicationContext.class);

        when(jobExecutionContextMock.getMergedJobDataMap().get(QuartzJobBean.JOB_DATA_MAP_KEY)).thenReturn(
                new HashMap<>());
        when(jobExecutionContextMock.getMergedJobDataMap().get(QuartzJobBean.JOB_DATA_MAP_KEY))
                .get(ScheduleTaskUtils.SCHEDULE_ID_KEY)
                .thenReturn(SCHEDULE_ID);

        when(applicationContextMock.getBean(PartitionPlanTaskService.class))
                .thenReturn(partitionPlanTaskServiceMock);
        when(applicationContextMock.getBean(PartitionPlanService.class))
                .thenReturn(partitionPlanServiceMock);
        when(applicationContextMock.getBean(ScheduleService.class))
                .thenReturn(scheduleServiceMock);

        SpringContextUtil.setApplicationContext(applicationContextMock);

        partitionPlanJob = new PartitionPlanJob();
    }

    @Test
    public void testExecute_scheduleNotFound() throws Exception {
        when(scheduleServiceMock.nullSafeGetById(anyLong())).thenThrow(new Exception());

        partitionPlanJob.execute(jobExecutionContextMock);

        verify(partitionPlanTaskServiceMock, never()).executePartitionPlan(any(), any());
        verify(scheduleServiceMock, times(1)).nullSafeGetById(anyLong());
    }

    @Test
    public void testExecute_databasePartitionPlanDisabledOrNoTables() throws Exception {
        when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(SCHEDULE_ENTITY);

        when(partitionPlanServiceMock.getDatabasePartitionPlanById(DATABASE_PARTITION_PLAN_ID))
                .thenReturn(DATABASE_PARTITION_PLAN);

        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(DATABASE_PARTITION_PLAN_ID))
                .thenReturn(new ArrayList<>());

        partitionPlanJob.execute(jobExecutionContextMock);

        verify(partitionPlanTaskServiceMock, never()).executePartitionPlan(any(), any());
        verify(scheduleServiceMock, times(1)).terminate(SCHEDULE_ENTITY);
    }

    @Test
    public void testExecute_executePartitionPlanSuccessful() throws Exception {
        when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(SCHEDULE_ENTITY);

        when(partitionPlanServiceMock.getDatabasePartitionPlanById(DATABASE_PARTITION_PLAN_ID))
                .thenReturn(DATABASE_PARTITION_PLAN);

        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(DATABASE_PARTITION_PLAN_ID))
                .thenReturn(TABLE_PARTITION_PLANS);

        doNothing().when(partitionPlanTaskServiceMock).executePartitionPlan(FLOW_INSTANCE_ID, TABLE_PARTITION_PLANS);

        partitionPlanJob.execute(jobExecutionContextMock);

        verify(partitionPlanTaskServiceMock, times(1)).executePartitionPlan(FLOW_INSTANCE_ID, TABLE_PARTITION_PLANS);
        verify(scheduleServiceMock, never()).terminate(SCHEDULE_ENTITY);
    }

    @Test
    public void testExecute_executePartitionPlanFails() throws Exception {
        when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(SCHEDULE_ENTITY);

        when(partitionPlanServiceMock.getDatabasePartitionPlanById(DATABASE_PARTITION_PLAN_ID))
                .thenReturn(DATABASE_PARTITION_PLAN);

        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(DATABASE_PARTITION_PLAN_ID))
                .thenReturn(TABLE_PARTITION_PLANS);

        doThrow(new Exception()).when(partitionPlanTaskServiceMock).executePartitionPlan(FLOW_INSTANCE_ID,
                TABLE_PARTITION_PLANS);

        partitionPlanJob.execute(jobExecutionContextMock);

        verify(partitionPlanTaskServiceMock, times(1)).executePartitionPlan(FLOW_INSTANCE_ID, TABLE_PARTITION_PLANS);
        verify(scheduleServiceMock, never()).terminate(SCHEDULE_ENTITY);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() throws Exception {
        doThrow(new UnExpectedException()).when(partitionPlanJob).interrupt();
        partitionPlanJob.interrupt();
    }

    @Test
    public void testPartitionPlanJob_correctInitialization() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();

        PartitionPlanTaskService partitionPlanTaskService = partitionPlanJob.getPartitionPlanTaskService();
        PartitionPlanService partitionPlanService = partitionPlanJob.getPartitionPlanService();
        ScheduleService scheduleService = partitionPlanJob.getScheduleService();

        assert partitionPlanTaskService != null;
        assert partitionPlanService != null;
        assert scheduleService != null;
    }
}