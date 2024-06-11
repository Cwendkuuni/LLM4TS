
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class PartitionPlanJobTest {

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() throws Exception {
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getMergedJobDataMap().get(ScheduleTaskUtils.SCHEDULE_ID)).thenReturn(1L);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        String json = "{\"databasePartitionPlanId\": 1}";
        scheduleEntity.setJobParametersJson(json);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity());
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);

        partitionPlanJob.execute(context);

        verify(partitionPlanTaskService).executePartitionPlan("flowInstanceId", tablePartitionPlans);
        verify(scheduleService).terminate(scheduleEntity);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() throws Exception {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.interrupt();
    }

    @Test
    public void testExecuteScheduleNotFound() throws Exception {
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getMergedJobDataMap().get(ScheduleTaskUtils.SCHEDULE_ID)).thenReturn(1L);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        String json = "{\"databasePartitionPlanId\": 1}";
        scheduleEntity.setJobParametersJson(json);

        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        partitionPlanJob.partitionPlanService = mock(PartitionPlanService.class);
        partitionPlanJob.scheduleService = mock(ScheduleService.class);

        when(scheduleService.nullSafeGetById(1L)).thenThrow(new Exception());

        partitionPlanJob.execute(context);

        verify(partitionPlanTaskService, never()).executePartitionPlan(anyString(), anyList());
        verify(scheduleService, never()).terminate(scheduleEntity);
    }
}
