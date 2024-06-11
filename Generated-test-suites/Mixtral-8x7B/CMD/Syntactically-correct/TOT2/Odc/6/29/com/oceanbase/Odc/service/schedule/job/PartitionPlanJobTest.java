import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @Mock
    private ScheduleTaskUtils scheduleTaskUtils;

    @Mock
    private SpringContextUtil springContextUtil;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanTaskService", partitionPlanTaskService);
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanService);
        ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", scheduleService);
    }

    @Test
    public void testExecute_ScheduleNotFound() throws Exception {
        Long scheduleId = 1L;
        when(scheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(scheduleId)).thenThrow(new Exception("Schedule not found"));

        partitionPlanJob.execute(jobExecutionContext);

        verify(log).warn("Schedule not found,scheduleId={}", scheduleId);
        verifyNoMoreInteractions(partitionPlanTaskService, partitionPlanService, scheduleService);
    }

    @Test
    public void testExecute_DatabasePartitionPlanNotFound() throws Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        String jobParametersJson = JsonUtils.toJson(jobParameters);
        scheduleEntity.setJobParametersJson(jobParametersJson);

        when(scheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenThrow(new Exception("Database partition plan not found"));

        partitionPlanJob.execute(jobExecutionContext);

        verify(log).warn("Database partition plan not found");
        verifyNoMoreInteractions(partitionPlanTaskService, partitionPlanService, scheduleService);
    }

    @Test
    public void testExecute_NoTableForPartitionPlan() throws Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        String jobParametersJson = JsonUtils.toJson(jobParameters);
        scheduleEntity.setJobParametersJson(jobParametersJson);
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);

        when(scheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanService
                .getValidTablePlanByDatabasePartitionPlanId(1L))
                .thenReturn(Collections.emptyList());

        partitionPlanJob.execute(jobExecutionContext);

        verify(log).info("database partition plan is disable or no table need to process,start to stop partition-plan job,scheduleId={}",
                scheduleId);
        verify(scheduleService).terminate(scheduleEntity);
        verifyNoMoreInteractions(partitionPlanTaskService, partitionPlanService, scheduleService);
    }

    @Test
    public void testExecute_PartitionPlanSuccess() throws Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        String jobParametersJson = JsonUtils.toJson(jobParameters);
        scheduleEntity.setJobParametersJson(jobParametersJson);
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity());

        when(scheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanService
                .getValidTablePlanByDatabasePartitionPlanId(1L))
                .thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanTaskService).executePartitionPlan(1L, tablePartitionPlans);
        verifyNoMoreInteractions(partitionPlanTaskService, partitionPlanService, scheduleService);
    }

    @Test
    public void testInterrupt() throws Exception {
        partitionPlanJob.interrupt();

        verify(log).warn("Interrupt is not supported for this job.");
        verifyNoMoreInteractions(partitionPlanTaskService, partitionPlanService, scheduleService);
    }
}