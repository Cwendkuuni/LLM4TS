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

    @Mock
    private ScheduleEntity scheduleEntity;

    @Mock
    private ScheduleTaskUtils scheduleTaskUtils;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanTaskService", partitionPlanTaskService);
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanService);
        ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", scheduleService);
    }

    @Test
    public void testExecute_ScheduleNotFound() throws Exception {
        when(scheduleTaskUtils.getScheduleId(context)).thenReturn(1L);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(null);

        partitionPlanJob.execute(context);

        verify(log, times(1)).warn("Schedule not found,scheduleId={}", 1L);
        verifyNoMoreInteractions(partitionPlanService, partitionPlanTaskService);
    }

    @Test
    public void testExecute_DatabasePartitionPlanNotFound() throws Exception {
        Long scheduleId = 1L;
        when(scheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn("{}");
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(null);

        partitionPlanJob.execute(context);

        verify(log, times(1))
                .info("database partition plan is disable or no table need to process,start to stop partition-plan job,scheduleId={}",
                        scheduleId);
        verify(scheduleService, times(1)).terminate(scheduleEntity);
        verifyNoMoreInteractions(partitionPlanService, partitionPlanTaskService);
    }

    @Test
    public void testExecute_NoTablePartitionPlans() throws Exception {
        Long scheduleId = 1L;
        Long databasePartitionPlanId = 2L;
        when(scheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(new PartitionPlanJobParameters(databasePartitionPlanId, "{}")));
        when(partitionPlanService.getDatabasePartitionPlanById(databasePartitionPlanId)).thenReturn(new DatabasePartitionPlanEntity());
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(databasePartitionPlanId))
                .thenReturn(new ArrayList<>());

        partitionPlanJob.execute(context);

        verify(log, times(1))
                .info("database partition plan is disable or no table need to process,start to stop partition-plan job,scheduleId={}",
                        scheduleId);
        verify(scheduleService, times(1)).terminate(scheduleEntity);
        verifyNoMoreInteractions(partitionPlanService, partitionPlanTaskService);
    }

    @Test
    public void testExecute_PartitionPlanSuccess() throws Exception {
        Long scheduleId = 1L;
        Long databasePartitionPlanId = 2L;
        when(scheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(new PartitionPlanJobParameters(databasePartitionPlanId, "{}")));
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setFlowInstanceId("flowInstanceId");
        databasePartitionPlan.setConfigEnabled(true);
        when(partitionPlanService.getDatabasePartitionPlanById(databasePartitionPlanId)).thenReturn(databasePartitionPlan);
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity());
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(databasePartitionPlanId))
                .thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(context);

        verifyNoMoreInteractions(scheduleService);
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
    }

    @Test
    public void testExecute_ExceptionInPartitionPlan() throws Exception {
        Long scheduleId = 1L;
        when(scheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn("{}");

        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(new DatabasePartitionPlanEntity());
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(new ArrayList<>());

        doThrow(new Exception("test exception")).when(partitionPlanTaskService).executePartitionPlan(anyString(), anyList());

        partitionPlanJob.execute(context);

        verify(log, times(1)).warn("Create partition-plan database change task failed.", new Exception("test exception"));
        verifyNoMoreInteractions(scheduleService);
        verifyNoMoreInteractions(partitionPlanService);
    }

    @Test
    public void testInterrupt() {
        try {
            partitionPlanJob.interrupt();
        } catch (UnExpectedException e) {
            assertEquals("Interrupt should not be supported.", e.getMessage());
        }
    }
}