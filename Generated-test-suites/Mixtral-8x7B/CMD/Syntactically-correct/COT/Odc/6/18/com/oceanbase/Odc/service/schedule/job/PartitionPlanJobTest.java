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
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;

    private PartitionPlanTaskService partitionPlanTaskService;

    private PartitionPlanService partitionPlanService;

    private ScheduleService scheduleService;

    private Scheduler scheduler;

    private JobDetail jobDetail;

    private JobExecutionContext jobExecutionContext;

    @Before
    public void setUp() throws SchedulerException, IOException {
        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        partitionPlanService = mock(PartitionPlanService.class);
        scheduleService = mock(ScheduleService.class);

        scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();

        jobDetail = JobBuilder.newJob(PartitionPlanJob.class)
                .withIdentity("PartitionPlanJob", "group1")
                .build();

        jobExecutionContext = new JobExecutionContext(scheduler, jobDetail);

        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanTaskService", partitionPlanTaskService);
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanService);
        ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", scheduleService);
    }

    @Test
    public void testExecute() throws SchedulerException, JobExecutionException {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);

        String jobParametersJson = JsonUtils.toJson(jobParameters);
        scheduleEntity.setJobParametersJson(jobParametersJson);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setFlowInstanceId("flowInstanceId");
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(new TablePartitionPlanEntity(),
                new TablePartitionPlanEntity());

        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanService
                .getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanTaskService, times(1))
                .executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
    }

    @Test
    public void testExecuteWithScheduleNotFound() throws SchedulerException, JobExecutionException {
        Long scheduleId = 1L;

        when(scheduleService.nullSafeGetById(scheduleId)).thenThrow(new SchedulerException("Schedule not found"));

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());
    }

    @Test
    public void testExecuteWithDatabasePartitionPlanNotFound() throws SchedulerException,
            JobExecutionException {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);

        String jobParametersJson = JsonUtils.toJson(jobParameters);
        scheduleEntity.setJobParametersJson(jobParametersJson);

        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenThrow(new SchedulerException("Database partition plan not found"));

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());
    }

    @Test
    public void testExecuteWithNoTablesToProcess() throws SchedulerException, JobExecutionException {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);

        String jobParametersJson = JsonUtils.toJson(jobParameters);
        scheduleEntity.setJobParametersJson(jobParametersJson);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setFlowInstanceId("flowInstanceId");
        databasePartitionPlan.setConfigEnabled(false);

        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());
    }

    @Test
    public void testExecuteWithExceptionDuringPartitionPlanExecution() throws SchedulerException,
            JobExecutionException {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);

        String jobParametersJson = JsonUtils.toJson(jobParameters);
        scheduleEntity.setJobParametersJson(jobParametersJson);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setFlowInstanceId("flowInstanceId");
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(new TablePartitionPlanEntity(),
                new TablePartitionPlanEntity());

        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanService
                .getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);

        doThrow(new RuntimeException("Create partition-plan database change task failed"))
                .when(partitionPlanTaskService)
                .executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanTaskService, times(1))
                .executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() throws UnExpectedException {
        partitionPlanJob.interrupt();
    }

}