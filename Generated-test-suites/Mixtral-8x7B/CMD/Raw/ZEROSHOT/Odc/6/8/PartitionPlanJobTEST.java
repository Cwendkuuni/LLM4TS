package com.oceanbase.odc.service.schedule.job;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;

    private PartitionPlanTaskService partitionPlanTaskServiceMock;

    private PartitionPlanService partitionPlanServiceMock;

    private ScheduleService scheduleServiceMock;

    private ScheduleTaskUtils scheduleTaskUtilsMock;

    private SpringContextUtil springContextUtilMock;

    private JobExecutionContext jobExecutionContextMock;

    @Before
    public void setUp() {
        partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
        partitionPlanServiceMock = mock(PartitionPlanService.class);
        scheduleServiceMock = mock(ScheduleService.class);
        scheduleTaskUtilsMock = mock(ScheduleTaskUtils.class);
        springContextUtilMock = mock(SpringContextUtil.class);

        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanTaskService",
                partitionPlanTaskServiceMock);
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanServiceMock);
        ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", scheduleServiceMock);

        jobExecutionContextMock = mock(JobExecutionContext.class);

        when(springContextUtilMock.getBean(PartitionPlanTaskService.class))
                .thenReturn(partitionPlanTaskServiceMock);
        when(springContextUtilMock.getBean(PartitionPlanService.class))
                .thenReturn(partitionPlanServiceMock);
        when(springContextUtilMock.getBean(ScheduleService.class))
                .thenReturn(scheduleServiceMock);
        when(springContextUtilMock.getBean(ScheduleTaskUtils.class))
                .thenReturn(scheduleTaskUtilsMock);
    }

    @Test
    public void testExecute() throws Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\": 100}");

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(100L);
        databasePartitionPlan.setConfigEnabled(true);

        TablePartitionPlanEntity tablePartitionPlan1 = new TablePartitionPlanEntity();
        tablePartitionPlan1.setId(101L);

        TablePartitionPlanEntity tablePartitionPlan2 = new TablePartitionPlanEntity();
        tablePartitionPlan2.setId(102L);

        List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(tablePartitionPlan1, tablePartitionPlan2);

        when(scheduleTaskUtilsMock.getScheduleId(jobExecutionContextMock)).thenReturn(scheduleId);
        when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(tablePartitionPlans);
        doNothing().when(partitionPlanTaskServiceMock)
                .executePartitionPlan(anyString(), any(List.class));

        partitionPlanJob.execute(jobExecutionContextMock);

        verify(partitionPlanTaskServiceMock)
                .executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
    }

    @Test
    public void testExecute_scheduleNotFound() throws Exception {
        Long scheduleId = 1L;

        when(scheduleTaskUtilsMock.getScheduleId(jobExecutionContextMock)).thenReturn(scheduleId);
        when(scheduleServiceMock.nullSafeGetById(anyLong())).thenThrow(new Exception());

        partitionPlanJob.execute(jobExecutionContextMock);

        verify(partitionPlanTaskServiceMock).executePartitionPlan(anyString(), anyList());
    }

    @Test
    public void testExecute_databasePartitionPlanNotConfigured() throws Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\": 100}");

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(100L);
        databasePartitionPlan.setConfigEnabled(false);

        List<TablePartitionPlanEntity> tablePartitionPlans = Collections.emptyList();

        when(scheduleTaskUtilsMock.getScheduleId(jobExecutionContextMock)).thenReturn(scheduleId);
        when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(jobExecutionContextMock);

        verify(partitionPlanTaskServiceMock).executePartitionPlan(anyString(), anyList());
    }

    @Test
    public void testExecute_noTablePartitionPlans() throws Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\": 100}");

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(100L);
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = Collections.emptyList();

        when(scheduleTaskUtilsMock.getScheduleId(jobExecutionContextMock)).thenReturn(scheduleId);
        when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(jobExecutionContextMock);

        verify(partitionPlanTaskServiceMock).executePartitionPlan(anyString(), anyList());
    }

    @Test
    public void testInterrupt() throws Exception {
        when(partitionPlanServiceMock.interruptPartitionPlan(anyString()))
                .thenThrow(new UnExpectedException());

        partitionPlanJob.interrupt();

        verify(partitionPlanServiceMock).interruptPartitionPlan(anyString());
    }
}