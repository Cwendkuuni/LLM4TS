import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParametersSerde;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private ScheduleEntity scheduleEntity;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    private PartitionPlanJobParametersSerde serde = new PartitionPlanJobParametersSerde();

    @Before
    public void setup() {
        ReflectionTestUtils.setField(partitionPlanJob, "scheduler", mock(org.quartz.Scheduler.class));
    }

    @Test
    public void testExecute_scheduleNotFound() throws Exception {
        when(scheduleService.nullSafeGetById(anyLong())).thenThrow(new Exception());

        partitionPlanJob.execute(jobExecutionContext);

        verify(log, times(1)).warn("Schedule not found,scheduleId={}", anyLong());
        verify(partitionPlanTaskService, never()).executePartitionPlan(any(), any());
    }

    @Test
    public void testExecute_databasePartitionPlanNotFound() throws Exception {
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn("{}");

        partitionPlanJob.execute(jobExecutionContext);

        verify(log, times(1)).warn("Database partition plan not found,scheduleId={}", anyLong());
        verify(partitionPlanTaskService, never()).executePartitionPlan(any(), any());
    }

    @Test
    public void testExecute_noTablePartitionPlans() throws Exception {
        PartitionPlanJobParameters parameters = new PartitionPlanJobParameters();
        parameters.setDatabasePartitionPlanId(1L);
        String json = serde.serialize(parameters);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn(json);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(new DatabasePartitionPlanEntity());

        partitionPlanJob.execute(jobExecutionContext);

        verify(log, times(1))
                .info("database partition plan is disable or no table need to process,start to stop partition-plan job,scheduleId={}",
                        anyLong());
        verify(scheduleService, times(1)).terminate(any(ScheduleEntity.class));
        verify(partitionPlanTaskService, never()).executePartitionPlan(any(), any());
    }

    @Test
    public void testExecute_executePartitionPlanSuccess() throws Exception {
        PartitionPlanJobParameters parameters = new PartitionPlanJobParameters();
        parameters.setDatabasePartitionPlanId(1L);
        String json = serde.serialize(parameters);
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity());
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn(json);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        when(partitionPlanService
                .getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(jobExecutionContext);

        verify(log, never()).warn(anyString());
        verify(scheduleService, never()).terminate(any(ScheduleEntity.class));
        verify(partitionPlanTaskService, times(1))
                .executePartitionPlan(anyLong(), any(List.class));
    }

    @Test
    public void testExecute_executePartitionPlanFailure() throws Exception {
        PartitionPlanJobParameters parameters = new PartitionPlanJobParameters();
        parameters.setDatabasePartitionPlanId(1L);
        String json = serde.serialize(parameters);
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity());
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn(json);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        when(partitionPlanService
                .getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);
        doThrow(new Exception("Something went wrong")).when(partitionPlanTaskService)
                .executePartitionPlan(anyLong(), any(List.class));

        partitionPlanJob.execute(jobExecutionContext);

        verify(log, times(1)).warn("Create partition-plan database change task failed.", any());
        verify(scheduleService, never()).terminate(any(ScheduleEntity.class));
        verify(partitionPlanTaskService, times(1))
                .executePartitionPlan(anyLong(), any(List.class));
    }

    @Test
    public void testInterrupt() {
        doThrow(new UnExpectedException()).when(partitionPlanJob).interrupt();

        assertEquals(partitionPlanJob.interrupt(), new UnExpectedException());
    }

    @Test
    public void testGetJobGroupNames() {
        List<String> groupNames = partitionPlanJob.getJobGroupNames();

        assertEquals(groupNames.size(), 1);
        assertEquals(groupNames.get(0), "PartitionPlanJobGroup");
    }

    @Test
    public void testGetJobNames() {
        List<String> jobNames = partitionPlanJob.getJobNames();

        assertEquals(jobNames.size(), 1);
        assertEquals(jobNames.get(0), "PartitionPlanJob");
    }

    @Test
    public void testDeleteJobs_noJobs() throws SchedulerException {
        when(scheduleService.getAllSchedules()).thenReturn(new ArrayList<>());

        partitionPlanJob.deleteJobs();

        verify(scheduleService, never()).deleteJob(any(), any());
    }

    @Test
    public void testDeleteJobs_deleteJobSuccess() throws SchedulerException {
        List<ScheduleEntity> scheduleEntities = new ArrayList<>();
        scheduleEntities.add(new ScheduleEntity());
        scheduleEntities.add(new ScheduleEntity());
        when(scheduleService.getAllSchedules()).thenReturn(scheduleEntities);
        when(scheduleService.deleteJob(any(), any())).thenReturn(true);

        partitionPlanJob.deleteJobs();

        verify(scheduleService, times(2)).deleteJob(any(), any());
    }

    @Test
    public void testDeleteJobs_deleteJobFailure() throws SchedulerException {
        List<ScheduleEntity> scheduleEntities = new ArrayList<>();
        scheduleEntities.add(new ScheduleEntity());
        scheduleEntities.add(new ScheduleEntity());
        when(scheduleService.getAllSchedules()).thenReturn(scheduleEntities);
        when(scheduleService.deleteJob(any(), any())).thenReturn(false);

        partitionPlanJob.deleteJobs();

        verify(scheduleService, times(2)).deleteJob(any(), any());
    }
}