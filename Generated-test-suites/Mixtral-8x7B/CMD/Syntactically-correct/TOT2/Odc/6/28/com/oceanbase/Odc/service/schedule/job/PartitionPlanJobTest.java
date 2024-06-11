import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    private ScheduleEntity scheduleEntity;

    private PartitionPlanJobParameters jobParameters;

    private DatabasePartitionPlanEntity databasePartitionPlan;

    private List<TablePartitionPlanEntity> tablePartitionPlans;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanTaskService", partitionPlanTaskService);
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanService);
        ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", scheduleService);

        scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\": 1}");

        jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);

        databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setFlowInstanceId("flowInstanceId");
        databasePartitionPlan.setConfigEnabled(true);

        tablePartitionPlans = new ArrayList<>();
        TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
        tablePartitionPlans.add(tablePartitionPlan);
    }

    @Test
    public void testExecute_whenScheduleNotFound_shouldLogWarningAndReturn() throws Exception {
        doThrow(new SchedulerException("Schedule not found")).when(scheduleService).nullSafeGetById(anyLong());

        partitionPlanJob.execute(context);

        verify(scheduleService, times(1)).nullSafeGetById(anyLong());
        verify(log, times(1)).warn(anyString(), anyLong());
        verifyNoMoreInteractions(partitionPlanService, partitionPlanTaskService);
    }

    @Test
    public void testExecute_whenDatabasePartitionPlanIsDisabledOrNoTableToProcess_shouldStopPartitionPlanJob()
            throws Exception {
        databasePartitionPlan.setConfigEnabled(false);
        tablePartitionPlans.clear();

        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(context);

        verify(scheduleService, times(1)).nullSafeGetById(anyLong());
        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(anyLong());
        verify(partitionPlanService, times(1))
                .getValidTablePlanByDatabasePartitionPlanId(anyLong());
        verify(scheduleService, times(1)).terminate(scheduleEntity);
        verifyNoMoreInteractions(partitionPlanTaskService);
    }

    @Test
    public void testExecute_whenExceptionOccurs_shouldLogWarning() throws Exception {
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(tablePartitionPlans);
        doThrow(new Exception("Create partition-plan database change task failed"))
                .when(partitionPlanTaskService)
                .executePartitionPlan(anyString(), anyList());

        partitionPlanJob.execute(context);

        verify(scheduleService, times(1)).nullSafeGetById(anyLong());
        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(anyLong());
        verify(partitionPlanService, times(1))
                .getValidTablePlanByDatabasePartitionPlanId(anyLong());
        verify(partitionPlanTaskService, times(1))
                .executePartitionPlan(anyString(), anyList());
        verify(log, times(1)).warn(anyString(), any());
    }

    @Test
    public void testInterrupt() {
        assertThrows(UnExpectedException.class, () -> partitionPlanJob.interrupt());
    }
}