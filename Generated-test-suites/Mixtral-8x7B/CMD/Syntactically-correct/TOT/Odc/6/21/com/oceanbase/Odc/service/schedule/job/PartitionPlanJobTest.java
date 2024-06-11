import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskServiceImpl;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanServiceImpl;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleServiceImpl;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class PartitionPlanJobTest {

    @MockBean
    private ScheduleService scheduleService;

    @MockBean
    private PartitionPlanTaskService partitionPlanTaskService;

    @MockBean
    private PartitionPlanService partitionPlanService;

    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setUp() {
        partitionPlanJob = new PartitionPlanJob();
    }

    @Test
    public void testExecuteWhenScheduleExists() throws Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

        JobExecutionContext context = new JobExecutionContext();
        context.put("scheduleId", scheduleId);

        partitionPlanJob.execute(context);

        // Assert that the schedule entity is returned correctly when it exists
        assertEquals(scheduleEntity, ScheduleTaskUtils.getSchedule(context));
    }

    @Test
    public void testExecuteWhenScheduleDoesNotExist() throws Exception {
        Long scheduleId = 1L;
        when(scheduleService.nullSafeGetById(scheduleId)).thenThrow(new Exception());

        JobExecutionContext context = new JobExecutionContext();
        context.put("scheduleId", scheduleId);

        partitionPlanJob.execute(context);

        // Assert that a warning message is logged when the schedule does not exist
        assertEquals("Schedule not found,scheduleId=1", context.getResult());
    }

    @Test
    public void testExecuteWhenPartitionPlanIsDisabledOrNoTablesToProcess() throws Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(false);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(jobParameters));
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        JobExecutionContext context = new JobExecutionContext();
        context.put("scheduleId", scheduleId);

        partitionPlanJob.execute(context);

        // Assert that the partition plan job is stopped and the schedule is terminated
        assertTrue(context.isJobInterrupted());
        assertEquals(SchedulerException.class, context.getResult().getClass());
    }

    @Test
    public void testExecuteWhenPartitionPlanIsExecutedCorrectly() throws Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);
        databasePartitionPlan.setFlowInstanceId("12345");
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(jobParameters));
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity());
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        JobExecutionContext context = new JobExecutionContext();
        context.put("scheduleId", scheduleId);

        partitionPlanJob.execute(context);

        // Assert that the partition plan task service is executed correctly
        verify(partitionPlanTaskService).executePartitionPlan("12345", tablePartitionPlans);
    }

    @Test
    public void testExecuteWhenExceptionIsThrown() throws Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);
        databasePartitionPlan.setFlowInstanceId("12345");
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(jobParameters));
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity());
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);
        when(partitionPlanTaskService.executePartitionPlan("12345", tablePartitionPlans)).thenThrow(new Exception());

        JobExecutionContext context = new JobExecutionContext();
        context.put("scheduleId", scheduleId);

        partitionPlanJob.execute(context);

        // Assert that the exception is caught and logged
        assertEquals("Create partition-plan database change task failed.", context.getResult());
    }
}