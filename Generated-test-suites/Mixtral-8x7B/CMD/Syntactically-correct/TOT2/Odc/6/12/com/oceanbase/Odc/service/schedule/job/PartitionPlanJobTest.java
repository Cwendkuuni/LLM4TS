import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParametersBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

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
        when(SpringContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);
        when(SpringContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
        when(SpringContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);

        jobParameters = new PartitionPlanJobParametersBuilder().build();
        databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setFlowInstanceId("flowInstanceId");
        tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity());

        scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(jobParameters));
        scheduleEntity.setId(1L);
    }

    // Test for execute method
    @Test
    public void testExecute() throws Exception {
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(context);

        verify(partitionPlanTaskService, times(1))
                .executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
    }

    // Test for interrupt method
    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }

    // Edge case: Schedule not found
    @Test
    public void testExecuteWithScheduleNotFound() throws Exception {
        when(scheduleService.nullSafeGetById(anyLong())).thenThrow(new Exception());

        partitionPlanJob.execute(context);

        verify(partitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());
    }

    // Edge case: Database partition plan is disabled or no table needs to process
    @Test
    public void testExecuteWithDatabasePartitionPlanDisabledOrNoTable() throws Exception {
        databasePartitionPlan.setConfigEnabled(false);
        tablePartitionPlans.clear();

        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(context);

        verify(partitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());
        verify(scheduleService, times(1)).terminate(scheduleEntity);
    }

    // Exception handling: SchedulerException in termin
}