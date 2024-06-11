import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.test.util.ReflectionTestUtils;

@SuppressWarnings("unchecked")
public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;

    private PartitionPlanService partitionPlanService;

    private PartitionPlanTaskService partitionPlanTaskService;

    private ScheduleService scheduleService;

    private SchedulerFactoryBean schedulerFactoryBean;

    @Before
    public void setup() {
        partitionPlanService = mock(PartitionPlanService.class);
        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        scheduleService = mock(ScheduleService.class);
        schedulerFactoryBean = mock(SchedulerFactoryBean.class);

        SpringContextUtil.setContext(schedulerFactoryBean);

        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", partitionPlanService);
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanTaskService", partitionPlanTaskService);
        ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", scheduleService);
    }

    @Test
    public void testExecute() throws Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(2L);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
        tablePartitionPlans.add(tablePartitionPlan);

        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class))
                .thenReturn(jobParameters);
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(
                        jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);
        when(schedulerFactoryBean.getScheduler().getJobDetail(
                        GroupMatcher.jobGroupEquals(PartitionPlanJob.JOB_GROUP),
                        GroupMatcher.anyJobGroup()))
                .thenReturn(null);

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getMergedJobDataMap().get(PartitionPlanJob.SCHEDULE_ID)).thenReturn(scheduleId);

        partitionPlanJob.execute(context);

        verify(partitionPlanTaskService).executePartitionPlan(
                databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);

        // Test for the case where the database partition plan is disabled or no table needs to be processed
        databasePartitionPlan.setConfigEnabled(false);
        tablePartitionPlans.clear();

        partitionPlanJob.execute(context);

        verify(scheduleService).terminate(scheduleEntity);
    }

    @Test
    public void testInterrupt() {
        assertThrows(UnExpectedException.class, () -> partitionPlanJob.interrupt());
    }
}