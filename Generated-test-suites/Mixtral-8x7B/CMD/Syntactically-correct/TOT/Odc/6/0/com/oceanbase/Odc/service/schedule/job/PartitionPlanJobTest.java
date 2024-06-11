
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.PartitionPlanJob;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecuteValidScheduleId() {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity());

        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(mock(JobExecutionContext.class));

        verify(partitionPlanTaskService, times(1)).executePartitionPlan(anyString(), eq(tablePartitionPlans));
    }

    @Test
    public void testExecuteNullScheduleEntity() {
        Long scheduleId = 1L;

        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(null);

        partitionPlanJob.execute(mock(JobExecutionContext.class));

        verify(partitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());
    }

    @Test
    public void testInterrupt() {
        exception.expect(UnExpectedException.class);
        new PartitionPlanJob().interrupt();
    }

    @Test
    public void testConstructor() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        assertNotNull(partitionPlanJob.getPartitionPlanTaskService());
        assertNotNull(partitionPlanJob.getPartitionPlanService());
        assertNotNull(partitionPlanJob.getScheduleService());
    }
}
