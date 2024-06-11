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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;
    private PartitionPlanService partitionPlanService;
    private PartitionPlanTaskService partitionPlanTaskService;
    private ScheduleService scheduleService;
    private JobExecutionContext jobExecutionContext;
    private Scheduler scheduler;
    private JobDetail jobDetail;
    private JobDataMap jobDataMap;

    @Before
    public void setUp() throws SchedulerException {
        partitionPlanService = mock(PartitionPlanService.class);
        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        scheduleService = mock(ScheduleService.class);
        SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanService);
        SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskService);
        SpringContextUtil.setBean(ScheduleService.class, scheduleService);

        jobExecutionContext = mock(JobExecutionContext.class);
        scheduler = StdSchedulerFactory.getDefaultScheduler();
        jobDetail = JobBuilder.newJob(PartitionPlanJob.class).withIdentity("testJob").build();
        jobDataMap = new JobDataMap();
        jobDataMap.put("scheduleId", 1L);
        jobDetail.setJobDataMap(jobDataMap);
        scheduler.addJob(jobDetail, true);
        scheduler.start();

        partitionPlanJob = new PartitionPlanJob();
    }

    @After
    public void tearDown() throws SchedulerException {
        scheduler.shutdown();
    }

    @Test
    public void testExecute() throws Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(jobParameters));

        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);

        List<TablePartitionPlanEntity> tablePartitionPlans = Collections.singletonList(new TablePartitionPlanEntity());
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanTaskService).executePartitionPlan(anyString(), anyList());
    }

    @Test
    public void testExecute_whenScheduleNotFound() throws Exception {
        Long scheduleId = 1L;
        when(scheduleService.nullSafeGetById(anyLong())).thenThrow(new Exception());

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());
    }

    @Test
    public void testExecute_whenDatabasePartitionPlanDisabled() throws Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(jobParameters));

        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(false);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());
    }

    @Test
    public void testExecute_whenNoTableToProcess() throws Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(jobParameters));

        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);

        List<TablePartitionPlanEntity> tablePartitionPlans = Collections.emptyList();
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());
    }

    @Test
    public void testExecute_whenExceptionThrown() throws Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(jobParameters));

        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);

        List<TablePartitionPlanEntity> tablePartitionPlans = Collections.singletonList(new TablePartitionPlanEntity());
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(tablePartitionPlans);

        doThrow(new Exception()).when(partitionPlanTaskService).executePartitionPlan(anyString(), anyList());

        partitionPlanJob.execute(jobExecutionContext);

        verify(scheduleService).terminate(scheduleEntity);
    }

    @Test
    public void testInterrupt() {
        assertFalse(partitionPlanJob.interrupt());
    }
}