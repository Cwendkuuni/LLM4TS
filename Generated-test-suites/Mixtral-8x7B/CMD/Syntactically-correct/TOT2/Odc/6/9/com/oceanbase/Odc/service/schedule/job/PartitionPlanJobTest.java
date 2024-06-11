import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanJobParameters;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.quartz.SchedulerBuilder.create;
import static org.quartz.TriggerBuilder.newTrigger;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    private ScheduleEntity scheduleEntity;

    @Before
    public void setUp() {
        scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\": 1}");

        ReflectionTestUtils.setField(partitionPlanJob, "scheduler", create().build());

        when(jobExecutionContext.getMergedJobDataMap().get(ScheduleTaskUtils.SCHEDULE_ID))
                .thenReturn(scheduleEntity.getId());
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong()))
                .thenReturn(new DatabasePartitionPlanEntity());
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(Collections.emptyList());
        doNothing().when(scheduleService).terminate(scheduleEntity);
    }

    @Test
    public void testExecuteWithValidSchedule() throws SchedulerException {
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        ReflectionTestUtils.setField(scheduleEntity, "jobParametersJson",
                JsonUtils.toJson(jobParameters));

        JobDetailImpl jobDetail = new JobDetailImpl();
        jobDetail.setJobClass(PartitionPlanJob.class);
        jobDetail.setName("testJob");
        jobDetail.setGroup("testGroup");

        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L))
                .thenReturn(Arrays.asList(new TablePartitionPlanEntity(), new TablePartitionPlanEntity()));

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanTaskService, times(1)).executePartitionPlan(anyString(), anyList());
    }

    @Test
    public void testExecuteWithScheduleNotFound() {
        when(scheduleService.nullSafeGetById(anyLong())).thenThrow(new SchedulerException("Schedule not found"));

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());
    }

    @Test
    public void testExecuteWithDatabasePartitionPlanNotFound() {
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenThrow(
                new UnExpectedException());

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());
    }

    @Test
    public void testExecuteWithNoTablePartitionPlans() {
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(Collections.emptyList());

        partitionPlanJob.execute(jobExecutionContext);

        verify(scheduleService, times(1)).terminate(scheduleEntity);
        verify(partitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());
    }

}