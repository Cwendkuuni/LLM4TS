import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParametersSerde;
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

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private ScheduleTaskUtils scheduleTaskUtils;

    @Mock
    private SpringContextUtil springContextUtil;

    @Mock
    private PartitionPlanJobParametersSerde partitionPlanJobParametersSerde;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    private JobExecutionContext jobExecutionContext;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanTaskService", partitionPlanTaskService);
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanService);
        ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", scheduleService);

        jobExecutionContext = mock(JobExecutionContext.class);
        when(scheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(1L);
    }

    @Test
    public void testExecute_ScheduleNotFound() throws Exception {
        when(scheduleService.nullSafeGetById(anyLong())).thenThrow(new Exception("Schedule not found"));

        partitionPlanJob.execute(jobExecutionContext);

        verify(scheduleService, times(1)).nullSafeGetById(anyLong());
        verify(partitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());
    }

    @Test
    public void testExecute_PartitionPlanDisabledOrNoTablesToProcess() throws Exception {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("{}");

        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(false);

        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(jobExecutionContext);

        verify(scheduleService, times(1)).nullSafeGetById(anyLong());
        verify(partitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());
    }

    @Test
    public void testExecute_ExceptionDuringExecution() throws Exception {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("{}");

        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);

        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity());
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(tablePartitionPlans);

        when(partitionPlanTaskService.executePartitionPlan(anyString(), anyList())).thenThrow(new Exception("Test exception"));

        partitionPlanJob.execute(jobExecutionContext);

        verify(scheduleService, times(1)).nullSafeGetById(anyLong());
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(anyString(), anyList());
    }

    @Test
    public void testInterrupt() throws SchedulerException {
        doThrow(new SchedulerException("Test exception")).when(scheduleService).terminate(any(ScheduleEntity.class));

        partitionPlanJob.interrupt();

        verify(scheduleService, times(1)).terminate(any(ScheduleEntity.class));
    }

    @Test
    public void testPartitionPlanJobParametersDeserialize() throws Exception {
        String json = "{\"databasePartitionPlanId\": 123}";
        PartitionPlanJobParameters expected = new PartitionPlanJobParameters();
        expected.setDatabasePartitionPlanId(123L);

        when(partitionPlanJobParametersSerde.deserialize(json)).thenReturn(expected);

        PartitionPlanJobParameters actual =
                ReflectionTestUtils.invokeMethod(partitionPlanJob, "partitionPlanJobParametersDeserialize", json);

        assertEquals(expected, actual);
    }
}