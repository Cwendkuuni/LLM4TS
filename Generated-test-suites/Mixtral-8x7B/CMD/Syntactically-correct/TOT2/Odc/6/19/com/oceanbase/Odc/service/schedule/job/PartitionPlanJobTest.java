import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private SpringContextUtil springContextUtil;

    @Mock
    private JobExecutionContext jobExecutionContext;

    private ScheduleEntity scheduleEntity;

    private PartitionPlanJobParameters partitionPlanJobParameters;

    private DatabasePartitionPlanEntity databasePartitionPlanEntity;

    private List<TablePartitionPlanEntity> tablePartitionPlanEntities;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanTaskService", partitionPlanTaskService);
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanService);
        ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", scheduleService);

        scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\":1}");

        partitionPlanJobParameters = new PartitionPlanJobParameters();
        partitionPlanJobParameters.setDatabasePartitionPlanId(1L);

        databasePartitionPlanEntity = new DatabasePartitionPlanEntity();
        databasePartitionPlanEntity.setId(1L);
        databasePartitionPlanEntity.setFlowInstanceId(UUID.randomUUID().toString());
        databasePartitionPlanEntity.setConfigEnabled(true);

        tablePartitionPlanEntities = new ArrayList<>();
        tablePartitionPlanEntities.add(new TablePartitionPlanEntity());
    }

    @Test
    public void testExecute_scheduleNotFound() throws SchedulerException {
        when(scheduleService.nullSafeGetById(anyLong())).thenThrow(new SchedulerException(""));

        partitionPlanJob.execute(jobExecutionContext);

        verify(scheduleService, times(1)).nullSafeGetById(anyLong());
        verify(partitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());
    }

    @Test
    public void testExecute_databasePartitionPlanDisabled() {
        databasePartitionPlanEntity.setConfigEnabled(false);

        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlanEntity);

        partitionPlanJob.execute(jobExecutionContext);

        verify(scheduleService, times(1)).nullSafeGetById(anyLong());
        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(anyLong());
        verify(partitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());
    }

    @Test
    public void testExecute_noTablePartitionPlan() {
        tablePartitionPlanEntities.clear();

        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlanEntity);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(tablePartitionPlanEntities);

        partitionPlanJob.execute(jobExecutionContext);

        verify(scheduleService, times(1)).nullSafeGetById(anyLong());
        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(anyLong());
        verify(partitionPlanService, times(1))
                .getValidTablePlanByDatabasePartitionPlanId(anyLong());
        verify(partitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());
    }

    @Test
    public void testExecute_executePartitionPlanSuccess() {
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong()))
                .thenReturn(databasePartitionPlanEntity);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(tablePartitionPlanEntities);

        partitionPlanJob.execute(jobExecutionContext);

        verify(scheduleService, times(1)).nullSafeGetById(anyLong());
        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(anyLong());
        verify(partitionPlanService, times(1))
                .getValidTablePlanByDatabasePartitionPlanId(anyLong());
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(anyString(), anyList());
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}