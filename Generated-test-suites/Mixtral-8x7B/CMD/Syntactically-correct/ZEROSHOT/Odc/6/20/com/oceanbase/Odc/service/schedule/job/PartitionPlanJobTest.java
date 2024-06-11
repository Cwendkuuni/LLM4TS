import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private JobExecutionContext context;

    @Mock
    private ScheduleEntity scheduleEntity;

    @Mock
    private PartitionPlanJobParameters jobParameters;

    @Mock
    private DatabasePartitionPlanEntity databasePartitionPlan;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanTaskService", partitionPlanTaskService);
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanService);
        ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", scheduleService);
    }

    @Test
    public void testExecute_scheduleNotFound() throws SchedulerException {
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(1L);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(null);

        partitionPlanJob.execute(context);

        verify(log, times(1)).warn(anyString(), any());
        verify(scheduleService, times(0)).terminate(any());
    }

    @Test
    public void testExecute_databasePartitionPlanNotEnabled() throws SchedulerException {
        Long scheduleId = 1L;
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(jobParameters.getDatabasePartitionPlanId()).thenReturn(1L);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(false);

        partitionPlanJob.execute(context);

        verify(log, times(1)).info(anyString(), anyLong());
        verify(scheduleService, times(1)).terminate(any());
    }

    @Test
    public void testExecute_tablePartitionPlansEmpty() throws SchedulerException {
        Long scheduleId = 1L;
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(jobParameters.getDatabasePartitionPlanId()).thenReturn(1L);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
        when(partitionPlanService
                .getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(new ArrayList<>());

        partitionPlanJob.execute(context);

        verify(log, times(1)).info(anyString(), anyLong());
        verify(scheduleService, times(1)).terminate(any());
    }

    @Test
    public void testExecute_executePartitionPlanSuccess() throws Exception {
        Long scheduleId = 1L;
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(jobParameters.getDatabasePartitionPlanId()).thenReturn(1L);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(mock(TablePartitionPlanEntity.class));
        when(partitionPlanService
                .getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(tablePartitionPlans);

        doNothing().when(partitionPlanTaskService)
                .executePartitionPlan(anyString(), any());

        partitionPlanJob.execute(context);

        verify(log, times(0)).info(anyString(), anyLong());
        verify(scheduleService, times(0)).terminate(any());
        verify(partitionPlanTaskService, times(1))
                .executePartitionPlan(anyString(), any());
    }

    @Test
    public void testExecute_executePartitionPlanFailed() throws Exception {
        Long scheduleId = 1L;
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(jobParameters.getDatabasePartitionPlanId()).thenReturn(1L);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(mock(TablePartitionPlanEntity.class));
        when(partitionPlanService
                .getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(tablePartitionPlans);

        when(partitionPlanTaskService.executePartitionPlan(anyString(), any()))
                .thenThrow(new Exception("Create partition-plan database change task failed."));

        partitionPlanJob.execute(context);

        verify(log, times(1)).warn(anyString(), any());
        verify(scheduleService, times(0)).terminate(any());
    }

    @Test
    public void testInterrupt() {
        partitionPlanJob.interrupt();

        verify(log, times(1)).warn(anyString(), any());
    }
}