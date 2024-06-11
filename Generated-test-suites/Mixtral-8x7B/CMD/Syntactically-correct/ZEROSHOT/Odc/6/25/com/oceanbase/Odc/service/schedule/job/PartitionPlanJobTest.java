package com.oceanbase.odc.service.schedule.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonUtils;
import com.oceanbase.odc.common.json.JsonConverter;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.service.ScheduleService;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class PartitionPlanJobTest {

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private SpringContextUtil springContextUtil;

    @Mock
    private ScheduleTaskUtils scheduleTaskUtils;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    private static final String DATABASE_PARTITION_PLAN_ID = "databasePartitionPlanId";
    private static final String FLOW_INSTANCE_ID = "flowInstanceId";
    private static final String JOB_PARAMETERS_JSON = "{\"databasePartitionPlanId\": \"%s\"}";
    private static final long SCHEDULE_ID = 1L;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExecute() throws JsonProcessingException, SchedulerException, UnExpectedException {
        // Prepare test data
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setFlowInstanceId(FLOW_INSTANCE_ID);
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity());

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters(DATABASE_PARTITION_PLAN_ID);
        String jobParametersJson = String.format(JOB_PARAMETERS_JSON, DATABASE_PARTITION_PLAN_ID);
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson(jobParametersJson);

        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(JsonUtils.fromJson(jobParametersJson, PartitionPlanJobParameters.class))
                .thenReturn(jobParameters);
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);
        when(springContextUtil.getBean(PartitionPlanTaskService.class))
                .thenReturn(partitionPlanTaskService);
        when(springContextUtil.getBean(PartitionPlanService.class))
                .thenReturn(partitionPlanService);
        when(springContextUtil.getBean(ScheduleService.class))
                .thenReturn(scheduleService);
        when(scheduleTaskUtils.getScheduleId(jobExecutionContext))
                .thenReturn(SCHEDULE_ID);

        // Execute the method
        partitionPlanJob.execute(jobExecutionContext);

        // Verify the results
        verify(partitionPlanTaskService, times(1))
                .executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
        verify(scheduleService, times(1)).terminate(scheduleEntity);
    }

    @Test
    public void testInterrupt() {
        // Execute the method
        partitionPlanJob.interrupt();

        // Verify that an UnExpectedException is thrown
        verify(springContextUtil, times(0)).getBean(any());
        verify(partitionPlanService, times(0)).getDatabasePartitionPlanById(anyString());
        verify(partitionPlanService, times(0))
                .getValidTablePlanByDatabasePartitionPlanId(anyString());
        verify(partitionPlanTaskService, times(0)).executePartitionPlan(anyString(), anyList());
    }
}