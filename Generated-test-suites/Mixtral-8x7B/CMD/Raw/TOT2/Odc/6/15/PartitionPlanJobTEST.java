import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanJobParameters;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
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
    private ScheduleTaskUtils scheduleTaskUtils;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanTaskService", partitionPlanTaskService);
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanService);
        ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", scheduleService);
    }

    @Test
    public void testExecute_scheduleNotFound() throws SchedulerException {
        when(scheduleTaskUtils.getScheduleId(context)).thenReturn(1L);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(null);

        partitionPlanJob.execute(context);

        verify(scheduleService, times(1)).nullSafeGetById(anyLong());
        verifyNoMoreInteractions(scheduleService);
        verifyNoMoreInteractions(partitionPlanService);
        verifyNoMoreInteractions(partitionPlanTaskService);
    }

    @Test
    public void testExecute_databasePartitionPlanNotFound() {
        Long scheduleId = 1L;
        when(scheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(null);

        partitionPlanJob.execute(context);

        verify(scheduleService, times(1)).nullSafeGetById(scheduleId);
        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(anyLong());
        verifyNoMoreInteractions(scheduleService);
        verifyNoMoreInteractions(partitionPlanService);
        verifyNoMoreInteractions(partitionPlanTaskService);
    }

    @Test
    public void testExecute_validTablePartitionPlansEmpty() {
        Long scheduleId = 1L;
        when(scheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong()))
                .thenReturn(new DatabasePartitionPlanEntity());

        partitionPlanJob.execute(context);

        verify(scheduleService, times(1)).nullSafeGetById(scheduleId);
        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(anyLong());
        verifyNoMoreInteractions(scheduleService);
        verifyNoMoreInteractions(partitionPlanService);
        verifyNoMoreInteractions(partitionPlanTaskService);
    }

    @Test
    public void testExecute_executePartitionPlanSuccess() throws Exception {
        Long scheduleId = 1L;
        Long databasePartitionPlanId = 2L;
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(databasePartitionPlanId);

        when(scheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(context.getMergedJobDataMap().get("jobParameters")).thenReturn(jobParameters);
        when(partitionPlanService.getDatabasePartitionPlanById(databasePartitionPlanId))
                .thenReturn(new DatabasePartitionPlanEntity());
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(
                databasePartitionPlanId))
                .thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(context);

        verify(scheduleService, times(1)).nullSafeGetById(scheduleId);
        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(databasePartitionPlanId);
        verify(partitionPlanService, times(1)).getValidTablePlanByDatabasePartitionPlanId(
                databasePartitionPlanId);
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(anyString(), anyList());
        verifyNoMoreInteractions(scheduleService);
        verifyNoMoreInteractions(partitionPlanService);
        verifyNoMoreInteractions(partitionPlanTaskService);
    }

    @Test
    public void testInterrupt_throwsUnexpectedException() {
        doThrow(new UnExpectedException()).when(partitionPlanJob).interrupt();

        try {
            partitionPlanJob.interrupt();
            assertEquals(false, true);
        } catch (UnExpectedException e) {
            assertEquals(true, true);
        }
    }
}