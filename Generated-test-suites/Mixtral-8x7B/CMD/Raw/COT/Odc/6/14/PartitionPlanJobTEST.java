import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
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
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private SpringContextUtil springContextUtil;

    @Mock
    private ScheduleTaskUtils scheduleTaskUtils;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanTaskService",
                partitionPlanTaskService);
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanService);
        ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", scheduleService);
    }

    @Test
    public void testExecute_ScheduleNotFound() throws Exception {
        when(scheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(1L);
        when(scheduleService.nullSafeGetById(anyLong())).thenThrow(new Exception("Schedule not found"));

        partitionPlanJob.execute(jobExecutionContext);

        verify(log, times(1)).warn("Schedule not found,scheduleId={}", 1L);
        verify(partitionPlanTaskService, never()).executePartitionPlan(any(), any());
        verify(scheduleService, times(1)).terminate(any());
    }

    @Test
    public void testExecute_InvalidDatabasePartitionPlan() throws Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        String json = JsonUtils.toJson(jobParameters);
        scheduleEntity.setJobParametersJson(json);

        when(scheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(null);

        partitionPlanJob.execute(jobExecutionContext);

        verify(log, times(1))
                .info("database partition plan is disable or no table need to process,start to stop partition-plan job,scheduleId={}",
                        scheduleId);
        verify(scheduleService, times(1)).terminate(any());
        verify(partitionPlanTaskService, never()).executePartitionPlan(any(), any());
    }

    @Test
    public void testExecute_ValidDatabasePartitionPlan() throws Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        String json = JsonUtils.toJson(jobParameters);
        scheduleEntity.setJobParametersJson(json);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
        tablePartitionPlans.add(tablePartitionPlan);

        when(scheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        when(partitionPlanService
                .getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(jobExecutionContext);

        verify(log, never())
                .info("database partition plan is disable or no table need to process,start to stop partition-plan job,scheduleId={}",
                        scheduleId);
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(any(), any());
        verify(scheduleService, never()).terminate(any());
    }

    @Test
    public void testExecute_ExceptionDuringPartitionPlanCreation() throws Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        String json = JsonUtils.toJson(jobParameters);
        scheduleEntity.setJobParametersJson(json);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
        tablePartitionPlans.add(tablePartitionPlan);

        when(scheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        when(partitionPlanService
                .getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(tablePartitionPlans);
        doThrow(new Exception("Create partition-plan database change task failed"))
                .when(partitionPlanTaskService)
                .executePartitionPlan(any(), any());

        partitionPlanJob.execute(jobExecutionContext);

        verify(log, times(1))
                .warn("Create partition-plan database change task failed.", any());
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(any(), any());
        verify(scheduleService, never()).terminate(any());
    }

    @Test
    public void testInterrupt() throws SchedulerException, ExecutionException {
        doThrow(new UnExpectedException()).when(partitionPlanJob).interrupt();

        partitionPlanJob.interrupt();

        verify(log, times(1)).error("Interrupt error: {}", any());
    }

    @Test
    public void testGetJobName() {
        String jobName = partitionPlanJob.getJobName();

        assert "PartitionPlanJob".equals(jobName);
    }
}