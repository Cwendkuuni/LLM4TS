package com.oceanbase.odc.service.schedule.job;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
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
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;

public class PartitionPlanJobTest {

  private PartitionPlanJob partitionPlanJob;
  private PartitionPlanTaskService partitionPlanTaskService;
  private PartitionPlanService partitionPlanService;
  private ScheduleService scheduleService;
  private JobExecutionContext jobExecutionContext;
  private SchedulerFactory schedulerFactory;
  private Logger logger;

  @Before
  public void setUp() throws Exception {
    logger = mock(Logger.class);
    partitionPlanTaskService = mock(PartitionPlanTaskService.class);
    partitionPlanService = mock(PartitionPlanService.class);
    scheduleService = mock(ScheduleService.class);
    SpringContextUtil springContextUtil = mock(SpringContextUtil.class);
    when(springContextUtil.getBean(PartitionPlanTaskService.class))
        .thenReturn(partitionPlanTaskService);
    when(springContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
    when(springContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);

    partitionPlanJob =
        spy(
            new PartitionPlanJob(
                springContextUtil, logger)); // create a spy of PartitionPlanJob class

    jobExecutionContext = mock(JobExecutionContext.class);
    when(jobExecutionContext.getMergedJobDataMap().get("scheduleId")).thenReturn(1L);

    schedulerFactory = new StdSchedulerFactory();
    schedulerFactory.initialize();
  }

  @After
  public void tearDown() throws Exception {
    schedulerFactory.getScheduler().shutdown();
  }

  @Test
  public void testExecute() throws Exception {
    ScheduleEntity scheduleEntity = new ScheduleEntity();
    scheduleEntity.setId(1L);
    scheduleEntity.setJobParametersJson(
        JsonUtils.toJson(
            new PartitionPlanJobParameters(
                1L,
                "flowInstanceId",
                Collections.singletonList(
                    new TablePartitionPlanEntity(
                        1L,
                        "databaseName",
                        "tableName",
                        "shardColumn",
                        "shardType",
                        "status",
                        "rangeValue",
                        "rangeType")))));

    when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);

    DatabasePartitionPlanEntity databasePartitionPlan =
        new DatabasePartitionPlanEntity(1L, "databaseName", true, "flowInstanceId");

    when(partitionPlanService.getDatabasePartitionPlanById(anyLong()))
        .thenReturn(databasePartitionPlan);

    List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
    tablePartitionPlans.add(
        new TablePartitionPlanEntity(
            1L,
            "databaseName",
            "tableName",
            "shardColumn",
            "shardType",
            "status",
            "rangeValue",
            "rangeType"));

    when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
        .thenReturn(tablePartitionPlans);

    doNothing().when(partitionPlanTaskService).executePartitionPlan(anyString(), anyList());

    partitionPlanJob.execute(jobExecutionContext);

    verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(anyLong());
    verify(partitionPlanService, times(1))
        .getValidTablePlanByDatabasePartitionPlanId(anyLong());
    verify(partitionPlanTaskService, times(1))
        .executePartitionPlan(anyString(), anyList());
  }

  @Test
  public void testInterrupt() throws Exception {
    doNothing().when(partitionPlanJob).interrupt();
    partitionPlanJob.interrupt();
    verify(partitionPlanJob, times(1)).interrupt();
  }

  @Test
  public void testExecuteWithScheduleNotFound() throws Exception {
    when(scheduleService.nullSafeGetById(anyLong())).thenReturn(null);

    partitionPlanJob.execute(jobExecutionContext);

    verify(logger, times(1))
        .warn("Schedule not found,scheduleId={}", 1L);
  }
}