import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.schedule.PartitionPlanJob;
import com.oceanbase.odc.service.schedule.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters.DatabasePartitionPlan;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class PartitionPlanJobTest {

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    private ScheduleEntity scheduleEntity;

    private DatabasePartitionPlanEntity databasePartitionPlanEntity;

    private List<TablePartitionPlanEntity> tablePartitionPlanEntities;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);

        databasePartitionPlanEntity = new DatabasePartitionPlanEntity();
        databasePartitionPlanEntity.setId(1L);
        databasePartitionPlanEntity.setConfigEnabled(true);

        tablePartitionPlanEntities = new ArrayList<>();
        TablePartitionPlanEntity tablePartitionPlanEntity = new TablePartitionPlanEntity();
        tablePartitionPlanEntity.setId(1L);
        tablePartitionPlanEntities.add(tablePartitionPlanEntity);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        String json = JsonUtils.toJson(jobParameters);
        scheduleEntity.setJobParametersJson(json);
    }

    @Test
    public void testExecute_success() throws Exception {
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlanEntity);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(tablePartitionPlanEntities);

        doNothing().when(partitionPlanTaskService).executePartitionPlan(anyString(), anyList());

        partitionPlanJob.execute(null);
    }
}