import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanJobParameters;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
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

    @Mock
    private JsonUtils jsonUtils;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        doNothing().when(springContextUtil).getBean(PartitionPlanTaskService.class);
        doNothing().when(springContextUtil).getBean(PartitionPlanService.class);
        doNothing().when(springContextUtil).getBean(ScheduleService.class);
    }

    @Test
    public void testExecute_scheduleIdFound() {
        Long scheduleId = 1L;
        when(scheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(jsonUtils.fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class))
                .thenReturn(new PartitionPlanJobParameters());
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(new DatabasePartitionPlanEntity());
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity());
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L))
                .thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(context);

        verify(scheduleEntity, times(1)).getJobParametersJson();
        verify(jsonUtils, times(1)).fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class);
        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(1L);
        verify(partitionPlanService, times(1))
                .getValidTablePlanByDatabasePartitionPlanId(1L);
        verify(partitionPlanTaskService, times(1))
                .executePartitionPlan(1L, tablePartitionPlans);
    }

    @Test
    public void testExecute_scheduleIdNotFound() {
        when(scheduleTaskUtils.getScheduleId(context)).thenReturn(1L);
        when(scheduleService.nullSafeGetById(1L)).thenThrow(new RuntimeException());

        partitionPlanJob.execute(context);

        verify(scheduleEntity, times(0)).getJobParametersJson();
        verify(jsonUtils, times(0)).fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class);
        verify(partitionPlanService, times(0)).getDatabasePartitionPlanById(1L);
        verify(partitionPlanService, times(0))
                .getValidTablePlanByDatabasePartitionPlanId(1L);
        verify(partitionPlanTaskService, times(0))
                .executePartitionPlan(1L, new ArrayList<>());
    }

    @Test
    public void testInterrupt() {
        doThrow(new UnExpectedException()).when(partitionPlanJob).interrupt();

        partitionPlanJob.interrupt();

        verify(partitionPlanJob, times(1)).interrupt();
    }

    @Test
    public void testExecute_partitionPlanTaskServiceThrowsException() {
        Long scheduleId = 1L;
        when(scheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(jsonUtils.fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class))
                .thenReturn(new PartitionPlanJobParameters());
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(new DatabasePartitionPlanEntity());
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity());
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L))
                .thenReturn(tablePartitionPlans);
        doThrow(new RuntimeException()).when(partitionPlanTaskService)
                .executePartitionPlan(1L, tablePartitionPlans);

        partitionPlanJob.execute(context);

        verify(scheduleEntity, times(1)).getJobParametersJson();
        verify(jsonUtils, times(1)).fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class);
        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(1L);
        verify(partitionPlanService, times(1))
                .getValidTablePlanByDatabasePartitionPlanId(1L);
        verify(partitionPlanTaskService, times(1))
                .executePartitionPlan(1L, tablePartitionPlans);
    }

    @Test
    public void testExecute_scheduleServiceTerminateThrowsSchedulerException()
            throws SchedulerException {
        Long scheduleId = 1L;
        when(scheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(jsonUtils.fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class))
                .thenReturn(new PartitionPlanJobParameters());
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(new DatabasePartitionPlanEntity());
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity());
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L))
                .thenReturn(tablePartitionPlans);
        doThrow(new SchedulerException("")).when(scheduleService).terminate(scheduleEntity);

        partitionPlanJob.execute(context);

        verify(scheduleEntity, times(1)).getJobParametersJson();
        verify(jsonUtils, times(1)).fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class);
        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(1L);
        verify(partitionPlanService, times(1))
                .getValidTablePlanByDatabasePartitionPlanId(1L);
        verify(partitionPlanTaskService, times(0))
                .executePartitionPlan(1L, tablePartitionPlans);
        verify(scheduleService, times(1)).terminate(scheduleEntity);
    }
}