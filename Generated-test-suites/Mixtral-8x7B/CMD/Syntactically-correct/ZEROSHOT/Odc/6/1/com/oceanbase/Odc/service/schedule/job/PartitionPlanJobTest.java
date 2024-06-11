import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    private ScheduleTaskUtils scheduleTaskUtils;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanTaskService", partitionPlanTaskService);
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanService);
        ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", scheduleService);
        ReflectionTestUtils.setField(partitionPlanJob, "scheduleTaskUtils", scheduleTaskUtils);
    }

    @Test
    public void testExecuteScheduleNotFound() throws SchedulerException {
        when(scheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(1L);
        when(scheduleService.nullSafeGetById(anyLong())).thenThrow(new SchedulerException(""));

        partitionPlanJob.execute(jobExecutionContext);

        verify(scheduleService, times(1)).nullSafeGetById(anyLong());
        verify(scheduleTaskUtils, times(1)).getScheduleId(jobExecutionContext);
    }

    @Test
    public void testExecuteDatabasePartitionPlanDisabled() throws SchedulerException, Exception {
        when(scheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(1L);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(createScheduleEntity());
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(createDatabasePartitionPlanEntity());
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(createTablePartitionPlanEntities());

        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanService);

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(anyLong());
        verify(partitionPlanService, times(1))
                .getValidTablePlanByDatabasePartitionPlanId(anyLong());
    }

    @Test
    public void testExecuteNoTablePartitionPlan() throws SchedulerException, Exception {
        when(scheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(1L);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(createScheduleEntity());
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(createDatabasePartitionPlanEntity());
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(new ArrayList<>());

        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanService);

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(anyLong());
        verify(partitionPlanService, times(1))
                .getValidTablePlanByDatabasePartitionPlanId(anyLong());
    }

    @Test
    public void testExecutePartitionPlanSuccess() throws SchedulerException, Exception {
        when(scheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(1L);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(createScheduleEntity());
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(createDatabasePartitionPlanEntity());
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(createTablePartitionPlanEntities());

        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanService);

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(anyLong());
        verify(partitionPlanService, times(1))
                .getValidTablePlanByDatabasePartitionPlanId(anyLong());
        verify(partitionPlanTaskService, times(1))
                .executePartitionPlan(anyString(), any(List.class));
    }

    @Test
    public void testInterrupt() throws Exception {
        partitionPlanJob.interrupt();

        verify(partitionPlanTaskService, times(0)).executePartitionPlan(anyString(), any(List.class));
    }

    private ScheduleEntity createScheduleEntity() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("{}");
        return scheduleEntity;
    }

    private DatabasePartitionPlanEntity createDatabasePartitionPlanEntity() {
        DatabasePartitionPlanEntity databasePartitionPlanEntity = new DatabasePartitionPlanEntity();
        databasePartitionPlanEntity.setConfigEnabled(false);
        databasePartitionPlanEntity.setFlowInstanceId("123456");
        return databasePartitionPlanEntity;
    }

    private List<TablePartitionPlanEntity> createTablePartitionPlanEntities() {
        TablePartitionPlanEntity tablePartitionPlanEntity1 = new TablePartitionPlanEntity();
        tablePartitionPlanEntity1.setEnabled(true);
        TablePartitionPlanEntity tablePartitionPlanEntity2 = new TablePartitionPlanEntity();
        tablePartitionPlanEntity2.setEnabled(false);

        return List.of(tablePartitionPlanEntity1, tablePartitionPlanEntity2);
    }
}