import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
import com.oceanbase.odc.service.schedule.job.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.model.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.service.ScheduleService;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.impl.JobExecutionContextImpl;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private ScheduleTaskUtils scheduleTaskUtils;

    @Mock
    private SpringContextUtil springContextUtil;

    private JobExecutionContext jobExecutionContext;

    @Before
    public void setUp() {
        jobExecutionContext = new JobExecutionContextImpl();
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanTaskService", partitionPlanTaskService);
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanService);
        ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", scheduleService);
    }

    @Test
    public void testExecute_scheduleNotFound() throws SchedulerException {
        when(scheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(1L);
        when(scheduleService.nullSafeGetById(anyLong())).thenThrow(new SchedulerException("Schedule not found"));

        partitionPlanJob.execute(jobExecutionContext);

        verify(log, times(1)).warn("Schedule not found,scheduleId={}", 1L);
        verifyNoMoreInteractions(partitionPlanService, partitionPlanTaskService, scheduleService);
    }

    @Test
    public void testExecute_databasePartitionPlanNotConfiguredOrNoTables() throws Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        String json = JsonUtils.toJson(jobParameters);
        scheduleEntity.setJobParametersJson(json);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(false);

        when(scheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(Arrays.asList());

        partitionPlanJob.execute(jobExecutionContext);

        verify(log, times(1))
                .info("database partition plan is disable or no table need to process,start to stop partition-plan job,scheduleId={}",
                        scheduleId);
        verify(scheduleService, times(1)).terminate(scheduleEntity);
        verifyNoMoreInteractions(partitionPlanService, partitionPlanTaskService, scheduleService);
    }

    @Test
    public void testExecute_exceptionDuringPartitionPlanTaskServiceExecute() throws Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        String json = JsonUtils.toJson(jobParameters);
        scheduleEntity.setJobParametersJson(json);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(new TablePartitionPlanEntity());

        when(scheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(tablePartitionPlans);
        when(partitionPlanTaskService.executePartitionPlan(anyLong(), anyList()))
                .thenThrow(new Exception("Create partition-plan database change task failed."));

        partitionPlanJob.execute(jobExecutionContext);

        verify(log, times(1))
                .warn("Create partition-plan database change task failed.", any(Exception.class));
        verifyNoMoreInteractions(partitionPlanService, partitionPlanTaskService, scheduleService);
    }

    @Test
    public void testInterrupt() {
        partitionPlanJob.interrupt();

        verify(log, times(1)).warn("Interrupt not supported for this job.");
    }
}