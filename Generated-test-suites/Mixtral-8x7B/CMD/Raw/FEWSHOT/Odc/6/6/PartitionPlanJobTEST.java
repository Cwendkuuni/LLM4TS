import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters.DatabasePartitionPlanId;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters.FlowInstanceId;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @Mock
    private SpringContextUtil mockSpringContextUtil;

    @Mock
    private ScheduleService mockScheduleService;

    @Mock
    private ScheduleEntity mockScheduleEntity;

    @Mock
    private PartitionPlanService mockPartitionPlanService;

    @Mock
    private PartitionPlanTaskService mockPartitionPlanTaskService;

    @Mock
    private ScheduleTaskUtils mockScheduleTaskUtils;

    @Mock
    private JobExecutionContext mockJobExecutionContext;

    @Mock
    private JsonUtils mockJsonUtils;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    private List<TablePartitionPlanEntity> tablePartitionPlans;

    private PartitionPlanJobParameters jobParameters;

    private DatabasePartitionPlanEntity databasePartitionPlan;

    @Before
    public void setup() {
        tablePartitionPlans = new ArrayList<>();
        jobParameters = new PartitionPlanJobParameters();
        databasePartitionPlan = new DatabasePartitionPlanEntity();
    }

    @Test
    public void testExecute() throws Exception {
        Long scheduleId = 1L;
        when(mockScheduleTaskUtils.getScheduleId(mockJobExecutionContext)).thenReturn(scheduleId);
        when(mockScheduleService.nullSafeGetById(scheduleId)).thenReturn(mockScheduleEntity);
        when(mockJsonUtils.fromJson(mockScheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class))
                .thenReturn(jobParameters);
        when(mockPartitionPlanService.getDatabasePartitionPlanById(1L))
                .thenReturn(databasePartitionPlan);
        when(mockPartitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L))
                .thenReturn(tablePartitionPlans);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
        when(tablePartitionPlans.isEmpty()).thenReturn(false);

        partitionPlanJob.execute(mockJobExecutionContext);
    }

    @Test
    public void testExecuteWithScheduleNotFound() throws Exception {
        Long scheduleId = 1L;
        when(mockScheduleTaskUtils.getScheduleId(mockJobExecutionContext)).thenReturn(scheduleId);
        when(mockScheduleService.nullSafeGetById(scheduleId)).thenThrow(new Exception());

        partitionPlanJob.execute(mockJobExecutionContext);
    }

    @Test
    public void testExecuteWithDatabasePartitionPlanDisabled() throws Exception {
        Long scheduleId = 1L;
        when(mockScheduleTaskUtils.getScheduleId(mockJobExecutionContext)).thenReturn(scheduleId);
        when(mockScheduleService.nullSafeGetById(scheduleId)).thenReturn(mockScheduleEntity);
        when(mockJsonUtils.fromJson(mockScheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class))
                .thenReturn(jobParameters);
        when(mockPartitionPlanService.getDatabasePartitionPlanById(1L))
                .thenReturn(databasePartitionPlan);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(false);

        partitionPlanJob.execute(mockJobExecutionContext);
    }

    @Test
    public void testExecuteWithNoTablePartitionPlans() throws Exception {
        Long scheduleId = 1L;
        when(mockScheduleTaskUtils.getScheduleId(mockJobExecutionContext)).thenReturn(scheduleId);
        when(mockScheduleService.nullSafeGetById(scheduleId)).thenReturn(mockScheduleEntity);
        when(mockJsonUtils.fromJson(mockScheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class))
                .thenReturn(jobParameters);
        when(mockPartitionPlanService.getDatabasePartitionPlanById(1L))
                .thenReturn(databasePartitionPlan);
        when(mockPartitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L))
                .thenReturn(new ArrayList<>());

        partitionPlanJob.execute(mockJobExecutionContext);
    }

    @Test
    public void testExecuteWithException() throws Exception {
        Long scheduleId = 1L;
        when(mockScheduleTaskUtils.getScheduleId(mockJobExecutionContext)).thenReturn(scheduleId);
        when(mockScheduleService.nullSafeGetById(scheduleId)).thenReturn(mockScheduleEntity);
        when(mockJsonUtils.fromJson(mockScheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class))
                .thenReturn(jobParameters);
        when(mockPartitionPlanService.getDatabasePartitionPlanById(1L))
                .thenReturn(databasePartitionPlan);
        when(mockPartitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L))
                .thenReturn(tablePartitionPlans);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
        when(tablePartitionPlans.isEmpty()).thenReturn(false);
        when(mockPartitionPlanTaskService.executePartitionPlan(1L, tablePartitionPlans))
                .thenThrow(new Exception());

        partitionPlanJob.execute(mockJobExecutionContext);
    }

    @Test
    public void testInterrupt() throws Exception {
        when(mockPartitionPlanTaskService.isInterrupted()).thenReturn(true);
        partitionPlanJob.interrupt();
    }

    @Test(expected = UnExpectedException.class)
    public void testInterruptWithException() throws Exception {
        when(mockPartitionPlanTaskService.isInterrupted()).thenReturn(false);
        partitionPlanJob.interrupt();
    }
}