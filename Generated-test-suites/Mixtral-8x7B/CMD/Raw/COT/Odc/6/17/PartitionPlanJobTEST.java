

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;

    private PartitionPlanService partitionPlanServiceMock;

    private PartitionPlanTaskService partitionPlanTaskServiceMock;

    private ScheduleService scheduleServiceMock;

    private ApplicationContext applicationContextMock;

    private JobExecutionContext jobExecutionContextMock;

    @Before
    public void setup() {
        partitionPlanServiceMock = mock(PartitionPlanService.class);
        partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
        scheduleServiceMock = mock(ScheduleService.class);
        applicationContextMock = mock(ApplicationContext.class);
        jobExecutionContextMock = mock(JobExecutionContext.class);

        when(SpringContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanServiceMock);
        when(SpringContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskServiceMock);
        when(SpringContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleServiceMock);

        partitionPlanJob = new PartitionPlanJob();
    }

    @Test
    public void testExecute_ScheduleEntityNotFound() throws SchedulerException, UnExpectedException {
        Long scheduleId = 1L;

        when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenThrow(new SchedulerException("Schedule not found"));

        partitionPlanJob.execute(jobExecutionContextMock);

        verify(log, times(1))
                .warn("Schedule not found,scheduleId={}", scheduleId);
        verify(scheduleServiceMock, times(1)).nullSafeGetById(scheduleId);
        verifyNoMoreInteractions(partitionPlanServiceMock, partitionPlanTaskServiceMock, scheduleServiceMock,
                applicationContextMock, jobExecutionContextMock);
    }

    @Test
    public void testExecute_DatabasePartitionPlanNotConfiguredOrNoTables()
            throws SchedulerException, UnExpectedException {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(jobParameters));
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(false);
        databasePartitionPlan.setFlowInstanceId("1");

        when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(new ArrayList<>());

        partitionPlanJob.execute(jobExecutionContextMock);

        verify(log, times(1))
                .info("database partition plan is disable or no table need to process,start to stop partition-plan job,scheduleId={}",
                        scheduleId);
        verify(scheduleServiceMock, times(1)).terminate(scheduleEntity);
        verifyNoMoreInteractions(partitionPlanServiceMock, partitionPlanTaskServiceMock, scheduleServiceMock,
                applicationContextMock, jobExecutionContextMock);
    }

    @Test
    public void testExecute_PartitionPlanTaskServiceThrowsException()
            throws SchedulerException, UnExpectedException {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(jobParameters));
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);
        databasePartitionPlan.setFlowInstanceId("1");
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity());

        when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);
        when(partitionPlanTaskServiceMock.executePartitionPlan(databasePartitionPlan.getFlowInstanceId(),
                tablePartitionPlans)).thenThrow(new RuntimeException("Create partition-plan database change task failed"));

        partitionPlanJob.execute(jobExecutionContextMock);

        verify(log, times(1))
                .warn("Create partition-plan database change task failed.", any(RuntimeException.class));
        verifyNoMoreInteractions(partitionPlanServiceMock, partitionPlanTaskServiceMock, scheduleServiceMock,
                applicationContextMock, jobExecutionContextMock);
    }

    @Test
    public void testInterrupt() throws UnExpectedException {
        partitionPlanJob.interrupt();

        verify(log, times(1)).warn("Interrupt is not supported for this job.");
        verifyNoMoreInteractions(partitionPlanServiceMock, partitionPlanTaskServiceMock, scheduleServiceMock,
                applicationContextMock, jobExecutionContextMock);
    }
}