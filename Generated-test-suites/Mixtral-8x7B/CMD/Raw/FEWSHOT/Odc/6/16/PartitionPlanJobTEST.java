import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.GroupMatcher;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;

    private PartitionPlanTaskService partitionPlanTaskServiceMock;

    private PartitionPlanService partitionPlanServiceMock;

    private ScheduleService scheduleServiceMock;

    private JobExecutionContext jobExecutionContextMock;

    private ScheduleEntity scheduleEntityMock;

    private PartitionPlanJobParameters partitionPlanJobParametersMock;

    private DatabasePartitionPlanEntity databasePartitionPlanMock;

    private List<TablePartitionPlanEntity> tablePartitionPlansMock;

    @Before
    public void setUp() {
        partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
        partitionPlanServiceMock = mock(PartitionPlanService.class);
        scheduleServiceMock = mock(ScheduleService.class);
        jobExecutionContextMock = mock(JobExecutionContext.class);
        scheduleEntityMock = mock(ScheduleEntity.class);
        partitionPlanJobParametersMock = mock(PartitionPlanJobParameters.class);
        databasePartitionPlanMock = mock(DatabasePartitionPlanEntity.class);
        tablePartitionPlansMock = mock(List.class);

        partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.partitionPlanTaskService = partitionPlanTaskServiceMock;
        partitionPlanJob.partitionPlanService = partitionPlanServiceMock;
        partitionPlanJob.scheduleService = scheduleServiceMock;
    }

    @Test
    public void testExecute_ScheduleNotFound() throws SchedulerException {
        when(ScheduleTaskUtils.getScheduleId(jobExecutionContextMock)).thenReturn(1L);
        when(scheduleServiceMock.nullSafeGetById(1L)).thenThrow(new SchedulerException("not found"));

        partitionPlanJob.execute(jobExecutionContextMock);

        // assertions
        // no exception is expected
    }

    @Test
    public void testExecute_DatabasePartitionPlanNotConfigEnabled() throws SchedulerException {
        when(ScheduleTaskUtils.getScheduleId(jobExecutionContextMock)).thenReturn(1L);
        when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntityMock);
        when(scheduleEntityMock.getJobParametersJson()).thenReturn("{\"databasePartitionPlanId\":1}");
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlanMock);
        when(databasePartitionPlanMock.isConfigEnabled()).thenReturn(false);

        partitionPlanJob.execute(jobExecutionContextMock);

        // assertions
        // no exception is expected
    }

    @Test
    public void testExecute_NoTablePartitionPlans() throws SchedulerException {
        when(ScheduleTaskUtils.getScheduleId(jobExecutionContextMock)).thenReturn(1L);
        when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntityMock);
        when(scheduleEntityMock.getJobParametersJson()).thenReturn("{\"databasePartitionPlanId\":1}");
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(
                databasePartitionPlanMock);
        when(databasePartitionPlanMock.isConfigEnabled()).thenReturn(true);
        when(
                partitionPlanService
                        .getValidTablePlanByDatabasePartitionPlanId(1L))
                .thenReturn(Collections.emptyList());

        partitionPlanJob.execute(jobExecutionContextMock);

        // assertions
        // no exception is expected
    }

    @Test
    public void testExecute_PartitionPlanSuccess() throws Exception {
        Long scheduleId = 1L;
        when(ScheduleTaskUtils.getScheduleId(jobExecutionContextMock)).thenReturn(scheduleId);
        when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntityMock);
        when(scheduleEntityMock.getJobParametersJson()).thenReturn("{\"databasePartitionPlanId\":1}");
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlanMock);
        when(databasePartitionPlanMock.isConfigEnabled()).thenReturn(true);
        when(
                partitionPlanService
                        .getValidTablePlanByDatabasePartitionPlanId(1L))
                .thenReturn(tablePartitionPlansMock);
        when(tablePartitionPlansMock.size()).thenReturn(1);

        partitionPlanJob.execute(jobExecutionContextMock);

        // assertions
        verifyExecutePartitionPlanIsCalled();
    }

    @Test
    public void testInterrupt() {
        assertThrows(UnExpectedException.class, () -> partitionPlanJob.interrupt());
    }

    private void verifyExecutePartitionPlanIsCalled() throws SchedulerException, Exception {
        when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntityMock);
        when(scheduleEntityMock.getJobParametersJson()).thenReturn("{\"databasePartitionPlanId\":1}");
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(
                databasePartitionPlanMock);
        when(databasePartitionPlanMock.isConfigEnabled()).thenReturn(true);
        when(
                partitionPlanService
                        .getValidTablePlanByDatabasePartitionPlanId(1L))
                .thenReturn(tablePartitionPlansMock);
        when(tablePartitionPlansMock.size()).thenReturn(1);

        partitionPlanJob.execute(jobExecutionContextMock);

        // assertions
        verify(partitionPlanTaskServiceMock, times(1))
                .executePartitionPlan(databasePartitionPlanMock.getFlowInstanceId(),
                        tablePartitionPlansMock);
    }
}