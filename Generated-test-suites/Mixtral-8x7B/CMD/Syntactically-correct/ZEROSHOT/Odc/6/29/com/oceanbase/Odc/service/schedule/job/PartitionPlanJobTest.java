import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SpringContextUtil.class, SchedulerFactoryBean.class})
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

    @Mock
    private ScheduleEntity scheduleEntity;

    @Mock
    private ApplicationContext applicationContext;

    private List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();

    private PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockStatic(SpringContextUtil.class);
        mockStatic(SchedulerFactoryBean.class);

        when(SpringContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
        when(SpringContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
        when(SpringContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);

        when(jobExecutionContext.getMergedJobDataMap().get("scheduleId")).thenReturn(1L);

        when(scheduleEntity.getId()).thenReturn(1L);
        when(scheduleEntity.getJobParametersJson()).thenReturn("{\"databasePartitionPlanId\": 1}");
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);

        jobParameters.setDatabasePartitionPlanId(1L);

        tablePartitionPlans.add(new TablePartitionPlanEntity());
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L))
                .thenReturn(tablePartitionPlans);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setFlowInstanceId("flowInstanceId");
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
    }

    @Test
    public void testExecute() throws SchedulerException {
        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanService, times(1))
                .getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId());
        verify(partitionPlanTaskService, times(1))
                .executePartitionPlan(databasePartitionPlans.get(0).getFlowInstanceId(), tablePartitionPlans);
    }

    @Test
    public void testInterrupt() {
        partitionPlanJob.interrupt();

        verify(partitionPlanTaskService, times(0)).executePartitionPlan(any(), any());
    }

    @Test
    public void testGetScheduleId() {
        Long scheduleId = ScheduleTaskUtils.getScheduleId(jobExecutionContext);

        assertEquals(new Long(1), scheduleId);
    }

    @Test
    public void testTerminateSchedule() throws SchedulerException {
        partitionPlanJob.terminateSchedule();

        verifyStatic(times(1));
        SchedulerFactoryBean.getDefaultScheduler().interrupt(any(JobDetailImpl.class));
    }
}