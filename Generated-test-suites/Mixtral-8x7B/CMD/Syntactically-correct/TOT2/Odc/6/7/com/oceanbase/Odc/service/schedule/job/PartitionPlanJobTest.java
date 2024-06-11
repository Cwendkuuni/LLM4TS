import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.model.ScheduleJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnexpectedException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class PartitionPlanJobTest {

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private JobExecutionContext context;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testExecuteTypicalCase() throws SchedulerException, UnexpectedException {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity());

        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(context.getMergedJobDataMap().get("scheduleJobParameters")).thenReturn(new ScheduleJobParameters());
        when(context.getMergedJobDataMap().get("scheduleJobParameters").getJobParameters()).thenReturn(jobParameters);
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(context);

        verify(partitionPlanTaskService, times(1)).executePartitionPlan(any(), any());
    }

    @Test
    public void testExecuteScheduleNotFound() throws SchedulerException, UnexpectedException {
        Long scheduleId = 1L;

        when(scheduleService.nullSafeGetById(scheduleId)).thenThrow(new SchedulerException(""));

        partitionPlanJob.execute(context);

        verify(partitionPlanTaskService, times(0)).executePartitionPlan(any(), any());
    }

    @Test
    public void testExecuteDatabasePartitionPlanDisabledOrNoTableToProcess() throws SchedulerException, UnexpectedException {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(false);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();

        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(context.getMergedJobDataMap().get("scheduleJobParameters")).thenReturn(new ScheduleJobParameters());
        when(context.getMergedJobDataMap().get("scheduleJobParameters").getJobParameters()).thenReturn(jobParameters);
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(context);

        verify(partitionPlanTaskService, times(0)).executePartitionPlan(any(), any());
    }

    @Test(expected = RuntimeException.class)
    public void testExecuteTerminateScheduleException() throws SchedulerException, UnexpectedException {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity());

        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(context.getMergedJobDataMap().get("scheduleJobParameters")).thenReturn(new ScheduleJobParameters());
        when(context.getMergedJobDataMap().get("scheduleJobParameters").getJobParameters()).thenReturn(jobParameters);
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);
        doThrow(new SchedulerException("")).when(scheduleService).terminate(scheduleEntity);

        partitionPlanJob.execute(context);
    }

    @Test
    public void testExecuteCreatePartitionPlanDatabaseChangeTaskFailed() throws SchedulerException, UnexpectedException {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity());

        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(context.getMergedJobDataMap().get("scheduleJobParameters")).thenReturn(new ScheduleJobParameters());
        when(context.getMergedJobDataMap().get("scheduleJobParameters").getJobParameters()).thenReturn(jobParameters);
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);
        doThrow(new UnexpectedException("")).when(partitionPlanTaskService).executePartitionPlan(any(), any());

        partitionPlanJob.execute(context);

        verify(scheduleService, times(1)).terminate(scheduleEntity);
    }

    @Test
    public void testInterrupt() {
        doThrow(new UnexpectedException("")).when(partitionPlanJob).interrupt();

        partitionPlanJob.interrupt();

        verify(partitionPlanJob, times(1)).interrupt();
    }
}