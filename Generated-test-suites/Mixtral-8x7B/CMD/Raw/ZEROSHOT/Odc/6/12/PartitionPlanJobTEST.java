import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.ScheduleService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    private JobExecutionContext jobExecutionContext;

    @Before
    public void setUp() {
        jobExecutionContext = mock(JobExecutionContext.class);
    }

    @Test
    public void testExecuteWithScheduleEntityFound() throws SchedulerException {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(2L);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setFlowInstanceId(3L);
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        TablePartitionPlanEntity tablePartitionPlan1 = new TablePartitionPlanEntity();
        tablePartitionPlan1.setId(4L);
        tablePartitionPlans.add(tablePartitionPlan1);

        doReturn(scheduleEntity).when(scheduleService).nullSafeGetById(anyLong());
        doReturn(jobParameters).when(scheduleService).getJobParametersFromJson(anyString());
        doReturn(databasePartitionPlan).when(partitionPlanService).getDatabasePartitionPlanById(anyLong());
        doReturn(tablePartitionPlans).when(partitionPlanService)
                .getValidTablePlanByDatabasePartitionPlanId(anyLong());

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanTaskService, times(1)).executePartitionPlan(anyLong(), any());
        verify(scheduleService, times(1)).terminate(any(ScheduleEntity.class));
    }

    @Test
    public void testExecuteWithScheduleEntityNotFound() throws SchedulerException {
        Long scheduleId = 1L;

        doReturn(null).when(scheduleService).nullSafeGetById(anyLong());

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanTaskService, times(0)).executePartitionPlan(anyLong(), any());
        verify(scheduleService, times(0)).terminate(any(ScheduleEntity.class));
    }

    @Test
    public void testInterrupt() {
        doNothing().when(partitionPlanTaskService).interrupt();

        partitionPlanJob.interrupt();

        verify(partitionPlanTaskService, times(1)).interrupt();
    }
}