import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.scheduler.Scheduler;
import org.quartz.scheduler.SchedulerException;
import org.quartz.scheduler.SchedulerFactory;
import org.quartz.simpl.SimpleSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PartitionPlanJobTest.class);

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @Mock
    private Scheduler scheduler;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    private ScheduleEntity scheduleEntity;

    private PartitionPlanJobParameters jobParameters;

    private DatabasePartitionPlanEntity databasePartitionPlanEntity;

    private List<TablePartitionPlanEntity> tablePartitionPlans;

    @Before
    public void setUp() throws Exception {
        scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\":1}");

        jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);

        databasePartitionPlanEntity = new DatabasePartitionPlanEntity();
        databasePartitionPlanEntity.setId(1L);
        databasePartitionPlanEntity.setFlowInstanceId(UUID.randomUUID().toString());
        databasePartitionPlanEntity.setConfigEnabled(true);

        tablePartitionPlans = new ArrayList<>();
        TablePartitionPlanEntity tablePartitionPlanEntity = new TablePartitionPlanEntity();
        tablePartitionPlanEntity.setId(1L);
        tablePartitionPlans.add(tablePartitionPlanEntity);

        when(jobExecutionContext.getMergedJobDataMap().get(ScheduleTaskUtils.SCHEDULE_ID)).thenReturn(1L);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(JsonUtils.fromJson(any(), any())).thenReturn(jobParameters);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlanEntity);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(tablePartitionPlans);
        when(scheduler.getJobDetail(any(), any())).thenReturn(jobExecutionContext.getJobDetail());
        when(scheduler.getTrigger(any(), any())).thenReturn(getTrigger());
    }

    @Test
    public void testExecute() throws Exception {
        when(partitionPlanTaskService.executePartitionPlan(any(), any())).thenReturn(null);

        partitionPlanJob.execute(jobExecutionContext);

        assertEquals(databasePartitionPlanEntity.getFlowInstanceId(),
                partitionPlanTaskService.executePartitionPlan(databasePartitionPlanEntity.getFlowInstanceId(),
                        tablePartitionPlans).getFlowInstanceId());
    }

    @Test
    public void testInterrupt() throws Exception {
        doNothing().when(partitionPlanTaskService).interrupt(any());

        partitionPlanJob.interrupt();

        partitionPlanTaskService.interrupt(any());
    }

    private Trigger getTrigger() {
        return TriggerBuilder.newTrigger()
                .withIdentity("trigger", "group")
                .startNow()
                .build();
    }

    public static void main(String[] args) throws SchedulerException {
        SchedulerFactory schedulerFactory = new SimpleSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.start();
    }
}