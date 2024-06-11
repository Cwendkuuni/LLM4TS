import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
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
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    @Mock
    private SpringContextUtil springContextUtil;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    private final Long SCHEDULE_ID = 1L;

    private final String JOB_PARAMETERS_JSON = "{\"databasePartitionPlanId\": 1}";

    @Before
    public void setup() {
        ReflectionTestUtils.setField(partitionPlanJob, "springContextUtil", springContextUtil);
    }

    @Test
    public void testExecute_scheduleNotFound() throws Exception {
        when(scheduleEntity.getId()).thenReturn(SCHEDULE_ID);
        when(scheduleTaskUtils.getScheduleId(context)).thenReturn(SCHEDULE_ID);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(null);

        partitionPlanJob.execute(context);

        verify(scheduleService, times(1)).nullSafeGetById(anyLong());
        verify(partitionPlanTaskService, never()).executePartitionPlan(any(), any());
        verify(scheduleService, never()).terminate(any());
    }

    @Test
    public void testExecute_databasePartitionPlanIsDisableOrNoTableNeedToProcess() throws Exception {
        when(scheduleEntity.getId()).thenReturn(SCHEDULE_ID);
        when(scheduleTaskUtils.getScheduleId(context)).thenReturn(SCHEDULE_ID);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(JsonUtils.fromJson(JOB_PARAMETERS_JSON,
                new TypeReference<PartitionPlanJobParameters>() {})).thenReturn(
                new PartitionPlanJobParameters(1L, false));
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(
                new DatabasePartitionPlanEntity(1L, false, null, null, null));
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(Collections.emptyList());

        partitionPlanJob.execute(context);

        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(anyLong());
        verify(partitionPlanService, times(1))
                .getValidTablePlanByDatabasePartitionPlanId(anyLong());
        verify(partitionPlanTaskService, never()).executePartitionPlan(any(), any());
        verify(scheduleService, times(1)).terminate(any());
    }

    @Test
    public void testExecute_executePartitionPlanSuccess() throws Exception {
        when(scheduleEntity.getId()).thenReturn(SCHEDULE_ID);
        when(scheduleTaskUtils.getScheduleId(context)).thenReturn(SCHEDULE_ID);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(JsonUtils.fromJson(JOB_PARAMETERS_JSON,
                new TypeReference<PartitionPlanJobParameters>() {})).thenReturn(
                new PartitionPlanJobParameters(1L, true));
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong()))
                .thenReturn(new DatabasePartitionPlanEntity(1L, true, null, null, null));
        when(
                partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(getTablePartitionPlanEntities());
        when(scheduleTaskUtils.getScheduleId(context)).thenReturn(SCHEDULE_ID);

        partitionPlanJob.execute(context);

        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(anyLong());
        verify(partitionPlanService, times(1))
                .getValidTablePlanByDatabasePartitionPlanId(anyLong());
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(any(), any());
        verify(scheduleService, never()).terminate(any());
    }

    @Test
    public void testExecute_executePartitionPlanFailed() throws Exception {
        when(scheduleEntity.getId()).thenReturn(SCHEDULE_ID);
        when(scheduleTaskUtils.getScheduleId(context)).thenReturn(SCHEDULE_ID);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(JsonUtils.fromJson(JOB_PARAMETERS_JSON,
                new TypeReference<PartitionPlanJobParameters>() {})).thenReturn(
                new PartitionPlanJobParameters(1L, true));
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong()))
                .thenReturn(new DatabasePartitionPlanEntity(1L, true, null, null, null));
        when(
                partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(getTablePartitionPlanEntities());
        when(scheduleTaskUtils.getScheduleId(context)).thenReturn(SCHEDULE_ID);
        doThrow(new Exception("Create partition-plan database change task failed."))
                .when(partitionPlanTaskService)
                .executePartitionPlan(any(), any());

        partitionPlanJob.execute(context);

        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(anyLong());
        verify(partitionPlanService, times(1))
                .getValidTablePlanByDatabasePartitionPlanId(anyLong());
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(any(), any());
        verify(scheduleService, never()).terminate(any());
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() throws Exception {
        partitionPlanJob.interrupt();
    }

    private List<TablePartitionPlanEntity> getTablePartitionPlanEntities() {
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity(1L, null, null, null, null, null, null));
        return tablePartitionPlans;
    }
}