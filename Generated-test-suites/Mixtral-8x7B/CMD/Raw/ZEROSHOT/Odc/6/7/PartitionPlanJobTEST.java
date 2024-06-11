import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
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
import static org.quartz.SchedulerBuilder.create;
import static org.quartz.impl.StdSchedulerFactory.getDefaultScheduler;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    private JobExecutionContext jobExecutionContext;

    @Before
    public void setUp() {
        jobExecutionContext = mock(JobExecutionContext.class);

        ReflectionTestUtils.setField(partitionPlanJob, "scheduler", getDefaultScheduler());

        when(jobExecutionContext.getMergedJobDataMap().getLong("scheduleId")).thenReturn(1L);

        when(partitionPlanService.nullSafeGetById(anyLong())).thenReturn(getScheduleEntity());

        when(scheduleService.terminate(any(ScheduleEntity.class))).thenReturn(null);

        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(getDatabasePartitionPlan());

        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(getTablePartitionPlans());

        doNothing().when(partitionPlanTaskService).executePartitionPlan(anyString(), anyList());
    }

    @Test
    public void testExecute() throws SchedulerException {
        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanService, times(1)).nullSafeGetById(anyLong());
        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(anyLong());
        verify(partitionPlanService, times(1))
                .getValidTablePlanByDatabasePartitionPlanId(anyLong());
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(anyString(), anyList());
        verify(scheduleService, times(1)).terminate(any(ScheduleEntity.class));
    }

    @Test
    public void testInterrupt() {
        try {
            partitionPlanJob.interrupt();
        } catch (UnExpectedException e) {
            assertEquals(UnExpectedException.class, e.getClass());
        }

        verify(partitionPlanTaskService, never()).interrupt(anyString());
    }

    private ScheduleEntity getScheduleEntity() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson(getPartitionPlanJobParameters().toString());
        return scheduleEntity;
    }

    private PartitionPlanJobParameters getPartitionPlanJobParameters() {
        PartitionPlanJobParameters parameters = new PartitionPlanJobParameters();
        parameters.setDatabasePartitionPlanId(1L);
        return parameters;
    }

    private DatabasePartitionPlanEntity getDatabasePartitionPlan() {
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setFlowInstanceId(UUID.randomUUID().toString());
        databasePartitionPlan.setConfigEnabled(true);
        return databasePartitionPlan;
    }

    private List<TablePartitionPlanEntity> getTablePartitionPlans() {
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
        tablePartitionPlan.setId(1L);
        tablePartitionPlans.add(tablePartitionPlan);
        return tablePartitionPlans;
    }
}