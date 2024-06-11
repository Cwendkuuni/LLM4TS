import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
    private JobExecutionContext context;

    @Mock
    private ScheduleEntity scheduleEntity;

    @Mock
    private PartitionPlanJobParameters jobParameters;

    @Mock
    private DatabasePartitionPlanEntity databasePartitionPlan;

    @Before
    public void setup() {
        when(SpringContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
        when(SpringContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
        when(SpringContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);

        when(context.getMergedJobDataMap().get(anyString())).thenReturn(scheduleEntity);
        when(scheduleEntity.getId()).thenReturn(1L);

        when(jobParameters.getDatabasePartitionPlanId()).thenReturn(1L);

        when(databasePartitionPlan.getId()).thenReturn(1L);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);

        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity());
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(tablePartitionPlans);
    }

    @Test
    public void testExecuteWithScheduleNotFound() throws Exception {
        when(scheduleService.nullSafeGetById(anyLong())).thenThrow(new Exception("Schedule not found"));

        partitionPlanJob.execute(context);

        verify(log, times(1)).warn("Schedule not found,scheduleId={}", 1L);
        verify(partitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());
        verify(scheduleService, times(1)).terminate(scheduleEntity);
    }

    @Test
    public void testExecuteWithConfigDisabled() throws Exception {
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(false);

        partitionPlanJob.execute(context);

        verify(log, times(1)).info(
                "database partition plan is disable or no table need to process,start to stop partition-plan job,scheduleId={}",
                1L);
        verify(partitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());
        verify(scheduleService, times(1)).terminate(scheduleEntity);
    }

    @Test
    public void testExecuteWithEmptyTablePartitionPlans() throws Exception {
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(new ArrayList<>());

        partitionPlanJob.execute(context);

        verify(log, times(1)).info(
                "database partition plan is disable or no table need to process,start to stop partition-plan job,scheduleId={}",
                1L);
        verify(partitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());
        verify(scheduleService, times(1)).terminate(scheduleEntity);
    }

    @Test
    public void testExecute() throws Exception {
        partitionPlanJob.execute(context);

        verify(partitionPlanTaskService, times(1))
                .executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), anyList());
        verify(scheduleService, never()).terminate(scheduleEntity);
    }

    @Test
    public void testInterrupt() throws Exception {
        partitionPlanJob.interrupt();

        verify(log, times(1)).warn("Interrupt not supported for PartitionPlanJob.");
    }
}