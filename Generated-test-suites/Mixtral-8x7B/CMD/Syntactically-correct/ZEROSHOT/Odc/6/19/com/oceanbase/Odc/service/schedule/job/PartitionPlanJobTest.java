import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
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
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    private static final Long SCHEDULE_ID = 1L;

    private static final String JOB_PARAMETERS_JSON = "{}";

    private static final String DATABASE_PARTITION_PLAN_ID = "1";

    private static final String FLOW_INSTANCE_ID = "1";

    private static final Logger LOGGER = LoggerFactory.getLogger(PartitionPlanJobTest.class);

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @Mock
    private ScheduleEntity scheduleEntity;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setUp() {
        when(jobExecutionContext.getMergedJobDataMap().getLong(ScheduleTaskUtils.SCHEDULE_ID)).thenReturn(SCHEDULE_ID);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JOB_PARAMETERS_JSON);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(getDatabasePartitionPlanEntity());
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(getTablePartitionPlanEntities());
    }

    @Test
    public void testExecute() throws Exception {
        partitionPlanJob.execute(jobExecutionContext);

        verify(scheduleService, times(1)).nullSafeGetById(SCHEDULE_ID);
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(FLOW_INSTANCE_ID, getTablePartitionPlanEntities());
    }

    @Test
    public void testExecuteWhenScheduleNotFound() throws Exception {
        when(scheduleService.nullSafeGetById(SCHEDULE_ID)).thenThrow(new Exception());

        partitionPlanJob.execute(jobExecutionContext);

        verify(scheduleService, times(1)).nullSafeGetById(SCHEDULE_ID);
        verify(partitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());
    }

    @Test
    public void testExecuteWhenPartitionPlanNotEnabled() throws Exception {
        when(getDatabasePartitionPlanEntity().isConfigEnabled()).thenReturn(false);

        partitionPlanJob.execute(jobExecutionContext);

        verify(scheduleService, times(1)).nullSafeGetById(SCHEDULE_ID);
        verify(partitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());
    }

    @Test
    public void testExecuteWhenNoTablePartitionPlan() throws Exception {
        when(getTablePartitionPlanEntities()).thenReturn(Collections.emptyList());

        partitionPlanJob.execute(jobExecutionContext);

        verify(scheduleService, times(1)).nullSafeGetById(SCHEDULE_ID);
        verify(partitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());
    }

    @Test
    public void testInterrupt() {
        assertThrows(UnExpectedException.class, () -> partitionPlanJob.interrupt());
    }

    private DatabasePartitionPlanEntity getDatabasePartitionPlanEntity() {
        DatabasePartitionPlanEntity databasePartitionPlanEntity = new DatabasePartitionPlanEntity();
        databasePartitionPlanEntity.setFlowInstanceId(FLOW_INSTANCE_ID);
        databasePartitionPlanEntity.setConfigEnabled(true);
        return databasePartitionPlanEntity;
    }

    private List<TablePartitionPlanEntity> getTablePartitionPlanEntities() {
        List<TablePartitionPlanEntity> tablePartitionPlanEntities = new ArrayList<>();
        // Add some TablePartitionPlanEntities to the list
        return tablePartitionPlanEntities;
    }
}