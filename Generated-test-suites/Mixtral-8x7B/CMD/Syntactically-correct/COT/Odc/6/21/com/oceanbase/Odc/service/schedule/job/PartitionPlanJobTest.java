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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
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
    private JobExecutionContext context;

    @Mock
    private ScheduleEntity scheduleEntity;

    @Mock
    private ScheduleTaskUtils scheduleTaskUtils;

    @Mock
    private SpringContextUtil springContextUtil;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    private PartitionPlanJobParameters jobParameters;

    private DatabasePartitionPlanEntity databasePartitionPlan;

    private List<TablePartitionPlanEntity> tablePartitionPlans;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanTaskService", partitionPlanTaskService);
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanService);
        ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", scheduleService);

        jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);

        databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setFlowInstanceId("flowInstanceId");
        databasePartitionPlan.setConfigEnabled(false);

        tablePartitionPlans = new ArrayList<>();
        TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
        tablePartitionPlans.add(tablePartitionPlan);

        when(springContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
        when(springContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
        when(springContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);
        when(scheduleTaskUtils.getScheduleId(context)).thenReturn(1L);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn("jobParametersJson");
        when(JsonUtils.fromJson(any(), any())).thenReturn(jobParameters);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        when(partitionPlanService
                .getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(tablePartitionPlans);
        doNothing().when(partitionPlanTaskService).executePartitionPlan(any(), any());
    }

    @Test
    public void testExecute() throws SchedulerException, UnExpectedException {
        partitionPlanJob.execute(context);

        assertEquals("flowInstanceId",
                ReflectionTestUtils.getField(partitionPlanJob, "databasePartitionPlan").getFlowInstanceId());
        assertEquals(1,
                ReflectionTestUtils.getField(partitionPlanJob, "tablePartitionPlans").size());
    }

    @Test
    public void testInterrupt() {
        assertThrows(UnExpectedException.class, () -> partitionPlanJob.interrupt());
    }

    @Test
    public void testExecute_scheduleNotFound() throws SchedulerException, UnExpectedException {
        when(scheduleService.nullSafeGetById(anyLong())).thenThrow(new SchedulerException(""));

        partitionPlanJob.execute(context);

        assertFalse(ReflectionTestUtils.getField(partitionPlanJob, "databasePartitionPlan").isConfigEnabled());
        assertEquals(0, ReflectionTestUtils.getField(partitionPlanJob, "tablePartitionPlans").size());
    }

    @Test
    public void testExecute_databasePartitionPlanNotFound() throws SchedulerException, UnExpectedException {
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(null);

        partitionPlanJob.execute(context);

        assertFalse(ReflectionTestUtils.getField(partitionPlanJob, "databasePartitionPlan").isConfigEnabled());
        assertEquals(0, ReflectionTestUtils.getField(partitionPlanJob, "tablePartitionPlans").size());
    }

    @Test
    public void testExecute_tablePartitionPlansEmpty() throws SchedulerException, UnExpectedException {
        databasePartitionPlan.setConfigEnabled(true);
        tablePartitionPlans.clear();

        partitionPlanJob.execute(context);

        assertFalse(ReflectionTestUtils.getField(partitionPlanJob, "databasePartitionPlan").isConfigEnabled());
        assertEquals(0, ReflectionTestUtils.getField(partitionPlanJob, "tablePartitionPlans").size());
    }

    @Test
    public void testExecute_executePartitionPlanException() throws SchedulerException, UnExpectedException {
        doThrow(new Exception("")).when(partitionPlanTaskService).executePartitionPlan(any(), any());

        partitionPlanJob.execute(context);

        assertFalse(ReflectionTestUtils.getField(partitionPlanJob, "databasePartitionPlan").isConfigEnabled());
        assertEquals(0, ReflectionTestUtils.getField(partitionPlanJob, "tablePartitionPlans").size());
    }
}