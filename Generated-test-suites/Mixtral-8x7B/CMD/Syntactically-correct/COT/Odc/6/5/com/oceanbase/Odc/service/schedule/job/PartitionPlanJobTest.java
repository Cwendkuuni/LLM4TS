package com.oceanbase.odc.service.schedule.job;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
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
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.QuartzJobFactory;

@SuppressWarnings("unchecked")
public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;

    private PartitionPlanService partitionPlanService;

    private PartitionPlanTaskService partitionPlanTaskService;

    private ScheduleService scheduleService;

    private SpringContextUtil springContextUtil;

    private ScheduleTaskUtils scheduleTaskUtils;

    private JobExecutionContext jobExecutionContext;

    @Before
    public void setUp() throws Exception {
        partitionPlanService = mock(PartitionPlanService.class);
        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        scheduleService = mock(ScheduleService.class);
        springContextUtil = mock(SpringContextUtil.class);
        scheduleTaskUtils = mock(ScheduleTaskUtils.class);

        when(springContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
        when(springContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
        when(springContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);

        QuartzJobFactory quartzJobFactory = mock(QuartzJobFactory.class);
        when(quartzJobFactory.newJob(QuartzJobBean.class, null)).thenReturn(new QuartzJobBean() {
        });

        jobExecutionContext = mock(JobExecutionContext.class);
        when(jobExecutionContext.getMergedJobDataMap()).thenReturn(new org.quartz.JobDataMap());
        when(jobExecutionContext.getScheduler().getJobDetail(any())).thenReturn(null);
        when(jobExecutionContext.getScheduler().getJobDetail(any(), any())).thenReturn(null);
        when(jobExecutionContext.getTrigger().getJobKey()).thenReturn(jobExecutionContext.getJobDetail()
                .getKey());
        when(jobExecutionContext.getJobDetail().getJobClass()).thenReturn(PartitionPlanJob.class);
        when(jobExecutionContext.getTrigger().getKey().getName()).thenReturn("partitionPlanJobTrigger");
        when(jobExecutionContext.getTrigger().getKey()
                .getGroup()).thenReturn(QuartzJobBean.DEFAULT_JOB_GROUP);

        doNothing().when(scheduleTaskUtils).scheduleJob(any(ScheduleEntity.class), any(Trigger.class));

        doThrow(new UnExpectedException()).when(partitionPlanJob).interrupt();

        partitionPlanJob = spy(new PartitionPlanJob());
        partitionPlanJob.partitionPlanTaskService = partitionPlanTaskService;
        partitionPlanJob.partitionPlanService = partitionPlanService;
        partitionPlanJob.scheduleService = scheduleService;
        partitionPlanJob.springContextUtil = springContextUtil;
        partitionPlanJob.scheduleTaskUtils = scheduleTaskUtils;
    }

    @Test
    public void testExecute_scheduleNotFound() throws SchedulerException {
        Long scheduleId = 1L;
        when(scheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(scheduleId)).thenThrow(new SchedulerException(""));

        partitionPlanJob.execute(jobExecutionContext);

        verify(log, times(1)).warn("Schedule not found,scheduleId={}", scheduleId);
        verify(partitionPlanTaskService, never()).executePartitionPlan(anyLong(), any());
    }

    @Test
    public void testExecute_databasePartitionPlanNotFound() throws SchedulerException {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        String json = JsonUtils.toJson(jobParameters);
        when(scheduleEntity.getJobParametersJson()).thenReturn(json);

        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(null);

        partitionPlanJob.execute(jobExecutionContext);

        verify(log, times(1))
                .info("database partition plan is disable or no table need to process,start to stop partition-plan job,scheduleId={}",
                        scheduleId);
        verify(scheduleService, times(1)).terminate(scheduleEntity);
        verify(partitionPlanTaskService, never()).executePartitionPlan(anyLong(), any());
    }

    @Test
    public void testExecute_tablePartitionPlansEmpty() throws SchedulerException {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        String json = JsonUtils.toJson(jobParameters);
        when(scheduleEntity.getJobParametersJson()).thenReturn(json);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);

        List<TablePartitionPlanEntity> emptyTablePartitionPlans = new ArrayList<>();
        when(partitionPlanService
                .getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(emptyTablePartitionPlans);

        partitionPlanJob.execute(jobExecutionContext);

        verify(log, times(1))
                .info("database partition plan is disable or no table need to process,start to stop partition-plan job,scheduleId={}",
                        scheduleId);
        verify(scheduleService, times(1)).terminate(scheduleEntity);
        verify(partitionPlanTaskService, never()).executePartitionPlan(anyLong(), any());
    }

    @Test
    public void testExecute_validTablePartitionPlans() throws SchedulerException {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        String json = JsonUtils.toJson(jobParameters);
        when(scheduleEntity.getJobParametersJson()).thenReturn(json);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity());
        when(partitionPlanService
                .getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(jobExecutionContext);

        verify(scheduleService, never()).terminate(scheduleEntity);
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(),
                tablePartitionPlans);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() throws UnExpectedException {
        partitionPlanJob.interrupt();
    }

    @Test
    public void testGetScheduler() throws SchedulerException {
        when(jobExecutionContext.getScheduler()).thenReturn(mock(org.quartz.Scheduler.class));
        partitionPlanJob.getScheduler(jobExecutionContext);
    }

    @Test
    public void testGetJobDetail() {
        partitionPlanJob.getJobDetail(jobExecutionContext);
    }

    @Test
    public void testGetTrigger() {
        partitionPlanJob.getTrigger(jobExecutionContext);
    }
}