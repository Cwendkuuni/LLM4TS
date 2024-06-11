import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

@SuppressWarnings("unchecked")
public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;

    private PartitionPlanTaskService partitionPlanTaskService;

    private PartitionPlanService partitionPlanService;

    private ScheduleService scheduleService;

    private JobExecutionContext jobExecutionContext;

    @Before
    public void setup() throws Exception {
        SpringContextUtil.clearBeans();

        partPlanServiceSpy = spy(new PartitionPlanService());
        SpringContextUtil.registerBean("partitionPlanService", partPlanServiceSpy);

        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        SpringContextUtil.registerBean("partitionPlanTaskService", partitionPlanTaskService);

        scheduleService = mock(ScheduleService.class);
        SpringContextUtil.registerBean("scheduleService", scheduleService);

        StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();
        schedulerFactory.initialize();
        jobExecutionContext = mock(JobExecutionContext.class);
        when(jobExecutionContext.getMergedJobDataMap()).thenReturn(new org.quartz.JobDataMap());
        when(jobExecutionContext.getScheduler()).thenReturn(schedulerFactory.getScheduler());

        partitionPlanJob = new PartitionPlanJob();
    }

    @Test
    public void testExecute() throws Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleEntity.getJobParametersJson()).thenReturn("{\"databasePartitionPlanId\": 123}");
        when(scheduleEntity.getId()).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(123L);
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(false);
        databasePartitionPlan.setFlowInstanceId("123");
        doReturn(databasePartitionPlan).when(partPlanServiceSpy).getDatabasePartitionPlanById(123L);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity());
        doReturn(tablePartitionPlans).when(partPlanServiceSpy)
                .getValidTablePlanByDatabasePartitionPlanId(123L);

        partitionPlanJob.execute(jobExecutionContext);

        verify(scheduleService, times(1)).terminate(scheduleEntity);
    }

    @Test
    public void testExecuteWithNoTablePartitionPlans() throws Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleEntity.getJobParametersJson()).thenReturn("{\"databasePartitionPlanId\": 123}");
        when(scheduleEntity.getId()).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(123L);
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);
        databasePartitionPlan.setFlowInstanceId("123");
        doReturn(databasePartitionPlan).when(partPlanServiceSpy).getDatabasePartitionPlanById(123L);

        List<TablePartitionPlanEntity> tablePartitionPlans = Collections.emptyList();
        doReturn(tablePartitionPlans).when(partPlanServiceSpy)
                .getValidTablePlanByDatabasePartitionPlanId(123L);

        partitionPlanJob.execute(jobExecutionContext);

        verify(scheduleService, times(0)).terminate(scheduleEntity);
    }

    @Test
    public void testExecuteWithInterrupted() throws Exception {
        doThrow(new UnExpectedException()).when(partitionPlanTaskService).executePartitionPlan(any(), any());

        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleEntity.getJobParametersJson()).thenReturn("{\"databasePartitionPlanId\": 123}");
        when(scheduleEntity.getId()).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(123L);
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);
        databasePartitionPlan.setFlowInstanceId("123");
        doReturn(databasePartitionPlan).when(partPlanServiceSpy).getDatabasePartitionPlanById(123L);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity());
        doReturn(tablePartitionPlans).when(partPlanServiceSpy)
                .getValidTablePlanByDatabasePartitionPlanId(123L);

        partitionPlanJob.execute(jobExecutionContext);

        verify(scheduleService, times(1)).terminate(scheduleEntity);
    }

    @Test
    public void testInterrupt() throws Exception {
        PartitionPlanJob partitionPlanJobSpy = spy(partitionPlanJob);
        partitionPlanJobSpy.interrupt();
    }

    private PartitionPlanService partPlanServiceSpy;
}