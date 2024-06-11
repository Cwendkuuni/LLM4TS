
package com.oceanbase.odc.service.schedule.job;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;

public class PartitionPlanJobTest {

    private PartitionPlanService partitionPlanService = mock(PartitionPlanService.class);

    private PartitionPlanTaskService partitionPlanTaskService = mock(PartitionPlanTaskService.class);

    private ScheduleService scheduleService = mock(ScheduleService.class);

    @Test
    public void testExecute() throws Exception {
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getMergedJobDataMap().get(ScheduleTaskUtils.SCHEDULE_ID)).thenReturn(1L);

        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);

        PartitionPlanJobParameters jobParameters = mock(PartitionPlanJobParameters.class);
        when(jobParameters.getDatabasePartitionPlanId()).thenReturn(1L);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(jobParameters));

        DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
        when(databasePartitionPlan.getFlowInstanceId()).thenReturn("123");
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.setPartitionPlanService(partitionPlanService);
        partitionPlanJob.setPartitionPlanTaskService(partitionPlanTaskService);
        partitionPlanJob.setScheduleService(scheduleService);

        partitionPlanJob.execute(context);

        verify(partitionPlanTaskService).executePartitionPlan("123", tablePartitionPlans);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.interrupt();
    }

    @Test
    public void testConstructor() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();

        assertNotNull(partitionPlanJob.getPartitionPlanTaskService());
        assertNotNull(partitionPlanJob.getPartitionPlanService());
        assertNotNull(partitionPlanJob.getScheduleService());
    }
}
