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
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Mock
    private ScheduleEntity scheduleEntity;

    @Mock
    private PartitionPlanJobParameters jobParameters;

    @Mock
    private DatabasePartitionPlanEntity databasePartitionPlan;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        doNothing().when(scheduleService).terminate(any());
    }

    @Test
    public void testExecute() throws Exception {
        when(jobParameters.getDatabasePartitionPlanId()).thenReturn(1L);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn("{\"databasePartitionPlanId\":1}");
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(Arrays.asList(mock(TablePartitionPlanEntity.class)));
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
        when(jobExecutionContext.getMergedJobDataMap().get(ScheduleTaskUtils.SCHEDULE_ID)).thenReturn(1L);

        partitionPlanJob.execute(jobExecutionContext);

        assertEquals(1, partitionPlanTaskService.executePartitionPlanCalls.size());
    }

    @Test
    public void testInterrupt() {
        try {
            partitionPlanJob.interrupt();
        } catch (UnExpectedException e) {
            assertEquals("Interrupt should not be called.", e.getMessage());
        }
    }
}