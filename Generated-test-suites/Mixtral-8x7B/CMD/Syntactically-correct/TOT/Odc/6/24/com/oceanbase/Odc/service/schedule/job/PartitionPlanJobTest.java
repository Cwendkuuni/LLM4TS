
import static org.mockito.Mockito.*;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters.DatabasePartitionPlanJobParameters;
import org.junit.Before;
import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

    private static final long SCHEDULE_ID = 1L;

    private static final long DATABASE_PARTITION_PLAN_ID = 2L;

    private static final String JOB_PARAMETERS_JSON = "{}";

    private PartitionPlanJob partitionPlanJob;

    private JobExecutionContext jobExecutionContext;

    private ScheduleEntity scheduleEntity;

    private PartitionPlanService partitionPlanService;

    private PartitionPlanTaskService partitionPlanTaskService;

    private ScheduleService scheduleService;

    @Before
    public void setUp() {
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        ReflectionTestUtils.setField(SpringContextUtil.class, "applicationContext", applicationContext);

        partitionPlanService = mock(PartitionPlanService.class);
        ReflectionTestUtils.setField(partitionPlanService, "applicationContext", applicationContext);

        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        ReflectionTestUtils.setField(partitionPlanTaskService, "applicationContext", applicationContext);

        scheduleService = mock(ScheduleService.class);
        ReflectionTestUtils.setField(scheduleService, "applicationContext", applicationContext);

        scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(SCHEDULE_ID);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(DATABASE_PARTITION_PLAN_ID);

        String jobParametersJson = JsonUtils.toJson(jobParameters);
        scheduleEntity.setJobParametersJson(jobParametersJson);

        jobExecutionContext = mock(JobExecutionContext.class);
        when(jobExecutionContext.getMergedJobDataMap().get("scheduleId")).thenReturn(scheduleEntity.getId());
        when(jobExecutionContext.getMergedJobDataMap().get("scheduleEntity")).thenReturn(scheduleEntity);

        partitionPlanJob = new PartitionPlanJob();
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanService);
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanTaskService", partitionPlanTaskService);
        ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", scheduleService);
    }

    @Test
    public void testExecuteWithValidScheduleEntity() throws Exception {
        when(partitionPlanService.getDatabasePartitionPlanById(DATABASE_PARTITION_PLAN_ID)).thenReturn(
                new DatabasePartitionPlanEntity());

        List<TablePartitionPlanEntity> tablePartitionPlans =
                Arrays.asList(new TablePartitionPlanEntity(), new TablePartitionPlanEntity());

        when(partitionPlanService
                .getValidTablePlanByDatabasePartitionPlanId(DATABASE_PARTITION_PLAN_ID))
                .thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanTaskService).executePartitionPlan(anyLong(), anyList());
    }

    @Test
    public void testExecuteWithNullScheduleId() {
        when(jobExecutionContext.getMergedJobDataMap().get("scheduleId")).thenReturn(null);

        partitionPlanJob.execute(jobExecutionContext);

        verify(scheduleService).terminate(any(ScheduleEntity.class));
    }

    @Test
    public void testExecuteWithInvalidDatabasePartitionPlanId() throws Exception {
        when(partitionPlanService.getDatabasePartitionPlanById(null)).thenThrow(new Exception());

        partitionPlanJob.execute(jobExecutionContext);

        verify(scheduleService).terminate(any(ScheduleEntity.class));
    }

    @Test
    public void testInterrupt() throws Exception {
        doThrow(new UnExpectedException()).when(partitionPlanJob).interrupt();

        partitionPlanJob.interrupt();
    }
}
