package com.oceanbase.odc.service.schedule.job;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;

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

@SuppressWarnings("unchecked")
public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;

    private PartitionPlanTaskService partitionPlanTaskService;

    private PartitionPlanService partitionPlanService;

    private ScheduleService scheduleService;

    private ApplicationContext applicationContext;

    private JobExecutionContext jobExecutionContext;

    private ScheduleEntity scheduleEntity;

    private PartitionPlanJobParameters jobParameters;

    private DatabasePartitionPlanEntity databasePartitionPlan;

    private List<TablePartitionPlanEntity> tablePartitionPlans;

    @Before
    public void setUp() throws Exception {
        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        partitionPlanService = mock(PartitionPlanService.class);
        scheduleService = mock(ScheduleService.class);
        applicationContext = mock(ApplicationContext.class);
        jobExecutionContext = mock(JobExecutionContext.class);
        SpringContextUtil.setApplicationContext(applicationContext);

        when(applicationContext.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
        when(applicationContext.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
        when(applicationContext.getBean(ScheduleService.class)).thenReturn(scheduleService);

        scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\":1}");

        jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);

        databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setFlowInstanceId("flowInstanceId");
        databasePartitionPlan.setConfigEnabled(true);

        tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity());

        partitionPlanJob = new PartitionPlanJob();
    }

    @Test
    public void testExecute() throws Exception {
        when(SpringContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);
        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
        when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class))
                .thenReturn(jobParameters);
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L))
                .thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanTaskService).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(),
                tablePartitionPlans);
        verify(scheduleService).terminate(scheduleEntity);
    }

    @Test
    public void testExecuteWithException() throws Exception {
        when(SpringContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);
        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
        when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class))
                .thenReturn(jobParameters);
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L))
                .thenReturn(tablePartitionPlans);

        doThrow(new Exception()).when(partitionPlanTaskService).executePartitionPlan(
                databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanTaskService).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(),
                tablePartitionPlans);
        verify(scheduleService, never()).terminate(scheduleEntity);
    }

    @Test
    public void testInterrupt() {
        try {
            partitionPlanJob.interrupt();
            fail("Expected UnExpectedException");
        } catch (UnExpectedException e) {
            // expected
        }
    }

    @Test
    public void testGetPartitionPlanTaskService() {
        PartitionPlanTaskService result = partitionPlanJob.getPartitionPlanTaskService();
        assertEquals(partitionPlanTaskService, result);
    }

    @Test
    public void testGetPartitionPlanService() {
        PartitionPlanService result = partitionPlanJob.getPartitionPlanService();
        assertEquals(partitionPlanService, result);
    }

    @Test
    public void testGetScheduleService() {
        ScheduleService result = partitionPlanJob.getScheduleService();
        assertEquals(scheduleService, result);
    }

    @Test
    public void testGetApplicationContext() {
        ApplicationContext result = partitionPlanJob.getApplicationContext();
        assertEquals(applicationContext, result);
    }

    @Test
    public void testGetJobExecutionContext() {
        JobExecutionContext result = partitionPlanJob.getJobExecutionContext();
        assertEquals(jobExecutionContext, result);
    }
}