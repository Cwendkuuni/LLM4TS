import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleEntity;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;

@org.junit.runner.RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @Autowired
    private PartitionPlanService partitionPlanServiceMock;

    @Autowired
    private PartitionPlanTaskService partitionPlanTaskServiceMock;

    @Autowired
    private ScheduleService scheduleServiceMock;

    private PartitionPlanJob partPlanJob;

    private JobExecutionContext jobContextMock;

    private ScheduleEntity scheduleEntity;

    private PartitionPlanJobParameters jobParameters;

    private DatabasePartitionPlanEntity databasePartitionPlan;

    private List<TablePartitionPlanEntity> tablePartitionPlans;

    @Before
    public void setup() {
        partPlanJob = new PartitionPlanJob();
        partPlanJob.setPartitionPlanService(partitionPlanServiceMock);
        partPlanJob.setPartitionPlanTaskService(partitionPlanTaskServiceMock);
        partPlanJob.setScheduleService(scheduleServiceMock);

        jobContextMock = mock(JobExecutionContext.class);

        scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);

        jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(jobParameters));

        databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setFlowInstanceId("123");
        databasePartitionPlan.setConfigEnabled(true);

        tablePartitionPlans = new ArrayList<>();
        TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
        tablePartitionPlans.add(tablePartitionPlan);

        ReflectionTestUtils.setField(partPlanJob, "springContextUtil", new SpringContextUtil());
    }

    @Test
    public void testExecute() throws Exception {
        when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntity);
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        partPlanJob.execute(jobContextMock);

        verify(scheduleServiceMock, times(1)).terminate(scheduleEntity);
    }

    @Test
    public void testInterrupt() throws Exception {
        partPlanJob.interrupt();
    }

    @Test
    public void testExecute_DatabasePartitionPlanIsDisable() throws Exception {
        databasePartitionPlan.setConfigEnabled(false);

        when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntity);
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        partPlanJob.execute(jobContextMock);

        verify(scheduleServiceMock, times(0)).terminate(scheduleEntity);
    }

    @Test
    public void testExecute_TablePartitionPlansIsEmpty() throws Exception {
        tablePartitionPlans.clear();

        when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntity);
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        partPlanJob.execute(jobContextMock);

        verify(scheduleServiceMock, times(0)).terminate(scheduleEntity);
    }

    @Test
    public void testExecute_ExecutePartitionPlanSuccess() throws Exception {
        when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntity);
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        partPlanJob.execute(jobContextMock);

        verify(partitionPlanTaskServiceMock, times(1))
                .executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
    }

    @Test
    public void testExecute_ExecutePartitionPlanFailed() throws Exception {
        when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntity);
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        doThrow(new Exception()).when(partitionPlanTaskServiceMock)
                .executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);

        partPlanJob.execute(jobContextMock);

        verify(partitionPlanTaskServiceMock, times(1))
                .executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
    }

    @Test
    public void testExecute_ScheduleNotFoun() throws Exception {
        when(scheduleServiceMock.nullSafeGetById(1L)).thenThrow(new Exception());

        partPlanJob.execute(jobContextMock);

        verify(scheduleServiceMock, times(0)).terminate(scheduleEntity);
    }

    @Test
    public void testExecute_ScheduleTerminate() throws Exception {
        when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntity);
        doNothing().when(scheduleServiceMock).terminate(scheduleEntity);

        partPlanJob.execute(jobContextMock);

        verify(scheduleServiceMock, times(1)).terminate(scheduleEntity);
    }
}