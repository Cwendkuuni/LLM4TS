package com.oceanbase.odc.service.schedule.job;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@SuppressWarnings("unchecked")
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
    public void setup() {
        partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
        partitionPlanServiceMock = mock(PartitionPlanService.class);
        scheduleServiceMock = mock(ScheduleService.class);

        SpringContextUtil.put(PartitionPlanTaskService.class, partitionPlanTaskServiceMock);
        SpringContextUtil.put(PartitionPlanService.class, partitionPlanServiceMock);
        SpringContextUtil.put(ScheduleService.class, scheduleServiceMock);

        partitionPlanJob = new PartitionPlanJob();

        jobExecutionContextMock = mock(JobExecutionContext.class);

        scheduleEntityMock = new ScheduleEntity();
        scheduleEntityMock.setId(1L);
        scheduleEntityMock.setJobParametersJson("{}");

        partitionPlanJobParametersMock = new PartitionPlanJobParameters();
        partitionPlanJobParametersMock.setDatabasePartitionPlanId(1L);

        databasePartitionPlanMock = new DatabasePartitionPlanEntity();
        databasePartitionPlanMock.setFlowInstanceId("flowInstanceId");
        databasePartitionPlanMock.setConfigEnabled(true);

        tablePartitionPlansMock = new ArrayList<>();
        tablePartitionPlansMock.add(new TablePartitionPlanEntity());

        when(jobExecutionContextMock.getMergedJobDataMap().get(ScheduleTaskUtils.SCHEDULE_ID))
                .thenReturn(scheduleEntityMock.getId());
        when(jobExecutionContextMock.getMergedJobDataMap().get(ScheduleTaskUtils.SCHEDULE))
                .thenReturn(scheduleEntityMock);
        when(JsonUtils.fromJson(anyString(), any()))
                .thenReturn(partitionPlanJobParametersMock);
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong()))
                .thenReturn(databasePartitionPlanMock);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(tablePartitionPlansMock);
        doNothing().when(scheduleServiceMock).terminate(any(ScheduleEntity.class));
    }

    @Test
    public void testExecute_scheduleNotFound() throws SchedulerException, UnExpectedException {
        when(scheduleEntityMock.getId()).thenReturn(null);

        partitionPlanJob.execute(jobExecutionContextMock);

        // Assert that the partition plan job was stopped
        // ...
    }

    @Test
    public void testExecute_databasePartitionPlanDisabled()
            throws SchedulerException, UnExpectedException {
        databasePartitionPlanMock.setConfigEnabled(false);

        partitionPlanJob.execute(jobExecutionContextMock);

        // Assert that the partition plan job was stopped
        // ...
    }

    @Test
    public void testExecute_noTablePartitionPlans()
            throws SchedulerException, UnExpectedException {
        tablePartitionPlansMock.clear();

        partitionPlanJob.execute(jobExecutionContextMock);

        // Assert that the partition plan job was stopped
        // ...
    }

    @Test
    public void testExecute_partitionPlanTaskServiceThrowsException()
            throws SchedulerException, UnExpectedException {
        when(partitionPlanTaskServiceMock.executePartitionPlan(anyString(), anyList()))
                .thenThrow(new RuntimeException("Test exception"));

        partitionPlanJob.execute(jobExecutionContextMock);

        // Assert that the partition plan job was stopped
        // ...
    }

    @Test
    public void testInterrupt() throws SchedulerException, UnExpectedException {
        when(scheduleEntityMock.getId()).thenReturn(1L);
        doNothing().when(scheduleServiceMock).terminate(any(ScheduleEntity.class));

        partitionPlanJob.interrupt();

        // Assert that the partition plan job was stopped
        // ...
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt_scheduleNotFound() throws SchedulerException, UnExpectedException {
        when(scheduleEntityMock.getId()).thenReturn(null);

        partitionPlanJob.interrupt();
    }
}