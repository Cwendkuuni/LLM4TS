import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Test
    public void testExecute() throws Exception {
        JobExecutionContext context = mock(JobExecutionContext.class);
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        String jobParametersJson = "{\"databasePartitionPlanId\":1}";
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        when(JsonUtils.fromJson(jobParametersJson, PartitionPlanJobParameters.class))
                .thenReturn(jobParameters);
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);
        when(partitionPlanService.getDatabasePartitionPlanById(1L))
                .thenReturn(databasePartitionPlan);
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
        tablePartitionPlan.setId(1L);
        tablePartitionPlans.add(tablePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L))
                .thenReturn(tablePartitionPlans);
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanTaskService",
                partitionPlanTaskService);
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanService);
        ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", scheduleService);
        partitionPlanJob.execute(context);
    }

    @Test
    public void testExecuteWhenScheduleNotFound() throws Exception {
        JobExecutionContext context = mock(JobExecutionContext.class);
        Long scheduleId = 1L;
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(null);
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanTaskService",
                partitionPlanTaskService);
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanService);
        ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", scheduleService);
        partitionPlanJob.execute(context);
    }

    @Test
    public void testExecuteWhenDatabasePartitionPlanIsDisabled() throws Exception {
        JobExecutionContext context = mock(JobExecutionContext.class);
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        String jobParametersJson = "{\"databasePartitionPlanId\":1}";
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        when(JsonUtils.fromJson(jobParametersJson, PartitionPlanJobParameters.class))
                .thenReturn(jobParameters);
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(false);
        when(partitionPlanService.getDatabasePartitionPlanById(1L))
                .thenReturn(databasePartitionPlan);
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L))
                .thenReturn(tablePartitionPlans);
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanTaskService",
                partitionPlanTaskService);
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanService);
        ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", scheduleService);
        partitionPlanJob.execute(context);
    }

    @Test
    public void testExecuteWhenNoTablePartitionPlans() throws Exception {
        JobExecutionContext context = mock(JobExecutionContext.class);
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        String jobParametersJson = "{\"databasePartitionPlanId\":1}";
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        when(JsonUtils.fromJson(jobParametersJson, PartitionPlanJobParameters.class))
                .thenReturn(jobParameters);
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);
        when(partitionPlanService.getDatabasePartitionPlanById(1L))
                .thenReturn(databasePartitionPlan);
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L))
                .thenReturn(tablePartitionPlans);
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanTaskService",
                partitionPlanTaskService);
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanService);
        ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", scheduleService);
        partitionPlanJob.execute(context);
    }

    @Test(expected = SchedulerException.class)
    public void testTerminate() throws Exception {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
        ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", scheduleService);
        partitionPlanJob.interrupt();
    }

    @Test
    public void testJsonUtils() throws Exception {
        String json = "{\"key\":\"value\"}";
        PartitionPlanJobParameters parameters =
                JsonUtils.fromJson(json, new TypeReference<PartitionPlanJobParameters>() {
                });
        assertEquals("value", parameters.getKey());
    }
}