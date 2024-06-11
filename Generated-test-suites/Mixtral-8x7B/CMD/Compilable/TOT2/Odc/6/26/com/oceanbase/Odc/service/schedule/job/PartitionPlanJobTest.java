package com.oceanbase.odc.service.schedule.job;
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
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PartitionPlanJobTest {

private PartitionPlanJob partitionPlanJob;

private PartitionPlanService partitionPlanService;

private PartitionPlanTaskService partitionPlanTaskService;

private ScheduleService scheduleService;

private JobExecutionContext jobExecutionContext;

@Before
public void setup() {
ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
partitionPlanService = applicationContext.getBean(PartitionPlanService.class);
partitionPlanTaskService = applicationContext.getBean(PartitionPlanTaskService.class);
scheduleService = applicationContext.getBean(ScheduleService.class);

partitionPlanJob = new PartitionPlanJob();

jobExecutionContext = mock(JobExecutionContext.class);
when(jobExecutionContext.getMergedJobDataMap()).thenReturn(mock(org.quartz.JobDataMap.class));
}

@Test
public void testExecute_ValidParameters() throws Exception {
Long scheduleId = 1L;
ScheduleEntity scheduleEntity = new ScheduleEntity();
scheduleEntity.setId(scheduleId);
scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\": \"123\"}");

when(jobExecutionContext.getMergedJobDataMap().get("scheduleEntity")).thenReturn(scheduleEntity);

DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
databasePartitionPlan.setId(123L);
databasePartitionPlan.setConfigEnabled(true);
when(partitionPlanService.getDatabasePartitionPlanById(123L)).thenReturn(databasePartitionPlan);

List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
tablePartitionPlan.setId(1L);
tablePartitionPlans.add(tablePartitionPlan);
when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(123L)).thenReturn(tablePartitionPlans);

partitionPlanJob.execute(jobExecutionContext);

// Add assertions here to verify the expected behavior
}

@Test
public void testExecute_ScheduleNotFound() throws Exception {
Long scheduleId = 1L;
ScheduleEntity scheduleEntity = new ScheduleEntity();
scheduleEntity.setId(scheduleId);

when(jobExecutionContext.getMergedJobDataMap().get("scheduleEntity")).thenReturn(scheduleEntity);

partitionPlanJob.execute(jobExecutionContext);

// Add assertions here to verify the expected behavior
}

@Test
public void testExecute_DatabasePartitionPlanDisabled() throws Exception {
Long scheduleId = 1L;
ScheduleEntity scheduleEntity = new ScheduleEntity();
scheduleEntity.setId(scheduleId);
scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\": \"123\"}");

when(jobExecutionContext.getMergedJobDataMap().get("scheduleEntity")).thenReturn(scheduleEntity);

DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
databasePartitionPlan.setId(123L);
databasePartitionPlan.setConfigEnabled(false);
when(partitionPlanService.getDatabasePartitionPlanById(123L)).thenReturn(databasePartitionPlan);

List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(123L)).thenReturn(tablePartitionPlans);

partitionPlanJob.execute(jobExecutionContext);

// Add assertions here to verify the expected behavior
}

@Test
public void testExecute_NoTablePartitionPlans() throws Exception {
Long scheduleId = 1L;
ScheduleEntity scheduleEntity = new ScheduleEntity();
scheduleEntity.setId(scheduleId);
scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\": \"123\"}");

when(jobExecutionContext.getMergedJobDataMap().get("scheduleEntity")).thenReturn(scheduleEntity);

DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
databasePartitionPlan.setId(123L);
databasePartitionPlan.setConfigEnabled(true);
when(partitionPlanService.getDatabasePartitionPlanById(123L)).thenReturn(databasePartitionPlan);

List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(123L)).thenReturn(tablePartitionPlans);

partitionPlanJob.execute(jobExecutionContext);

// Add assertions here to verify the expected behavior
}

@Test
public void testInterrupt() throws Exception {
partitionPlanJob.interrupt();

// Add assertions here to verify the expected behavior
}

}