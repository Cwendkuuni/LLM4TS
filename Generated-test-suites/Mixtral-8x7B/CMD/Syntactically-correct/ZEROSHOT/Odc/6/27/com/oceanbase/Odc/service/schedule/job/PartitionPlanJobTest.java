import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private JobExecutionContext jobExecutionContext;

    private ScheduleEntity scheduleEntity;

    private PartitionPlanJobParameters jobParameters;

    private DatabasePartitionPlanEntity databasePartitionPlan;

    private List<TablePartitionPlanEntity> tablePartitionPlans;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(partitionPlanJob, "schedulerFactory", new StdSchedulerFactory());

        scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\":10}");

        jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(10L);

        databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setFlowInstanceId("flowInstanceId");
        databasePartitionPlan.setConfigEnabled(true);

        tablePartitionPlans = new ArrayList<>();
        TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
        tablePartitionPlans.add(tablePartitionPlan);
    }

    @Test
    public void testExecute() throws SchedulerException {
        Mockito.when(jobExecutionContext.getMergedJobDataMap().getLong("scheduleId")).thenReturn(1L);
        Mockito.when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        Mockito.when(JsonUtils.fromJson(anyString(), any())).thenReturn(jobParameters);
        Mockito.when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        Mockito.when(partitionPlanService
                .getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(tablePartitionPlans);
        Mockito.doNothing().when(partitionPlanTaskService)
                .executePartitionPlan(anyString(), anyList());

        partitionPlanJob.execute(jobExecutionContext);

        Mockito.verify(partitionPlanTaskService, Mockito.times(1))
                .executePartitionPlan(anyString(), anyList());
        Mockito.verify(scheduleService, Mockito.times(1)).terminate(scheduleEntity);
    }

    @Test
    public void testInterrupt() {
        Assert.assertThrows(UnExpectedException.class, () -> partitionPlanJob.interrupt());
    }

    @Test
    public void testCreatePartitionPlanJob() throws SchedulerException {
        Date startTime = new Date();
        Trigger trigger = TriggerBuilder.newTrigger()
                .startNow()
                .withIdentity("partitionPlanJob", "group1")
                .build();

        partitionPlanJob.createPartitionPlanJob("partitionPlanJob", "group1", startTime);

        Mockito.verify(partitionPlanJob.schedulerFactory).getScheduler().scheduleJob(any(), any());
    }
}