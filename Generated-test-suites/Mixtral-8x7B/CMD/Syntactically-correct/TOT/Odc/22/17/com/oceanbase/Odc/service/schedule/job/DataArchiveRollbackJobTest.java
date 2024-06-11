import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.scheduler.JobKey;
import org.quartz.scheduler.Scheduler;
import org.quartz.scheduler.SchedulerException;
import org.quartz.scheduler.SchedulerFactory;
import org.quartz.spi.OperableTrigger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {

private DataArchiveRollbackJob dataArchiveRollbackJob;
private ScheduleTaskRepository scheduleTaskRepository;
private Scheduler scheduler;

@Before
public void setUp() throws SchedulerException {
SchedulerFactory schedulerFactory = mock(SchedulerFactory.class);
scheduler = mock(Scheduler.class);
when(schedulerFactory.getScheduler()).thenReturn(scheduler);

scheduleTaskRepository = mock(ScheduleTaskRepository.class);

dataArchiveRollbackJob = new DataArchiveRollbackJob(scheduleTaskRepository);
}

@Test
public void testExecuteWithValidTaskEntityAndDataArchiveRollbackParameters() throws JobExecutionException, SchedulerException {
JobExecutionContext context = mock(JobExecutionContext.class);

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setJobName("DataArchiveRollbackJob");
taskEntity.setJobGroup("group1");
taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");

DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
rollbackParameters.setDataArchiveTaskId(2L);

when(context.getMergedJobDataMap().get(anyString())).thenReturn(taskEntity);

List<DlmTask> taskUnits = new ArrayList<>();
DlmTask taskUnit = new DlmTask();
taskUnit.setSourceDatabaseId(3L);
taskUnit.setTargetDatabaseId(4L);
taskUnit.setJobType(JobType.ARCHIVE);
taskUnit.setStatus(TaskStatus.PREPARING);
taskUnits.add(taskUnit);

String jsonTaskUnits = JsonUtils.toJson(taskUnits);
when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(new ScheduleTaskEntity(2L, jsonTaskUnits)));

dataArchiveRollbackJob.execute(context);

OperableTrigger trigger = mock(OperableTrigger.class);
when(trigger.getKey()).thenReturn(JobKey.jobKey("DataArchiveRollbackJob", "group1"));
when(scheduler.getTrigger(JobKey.jobKey("DataArchiveRollbackJob", "group1"))).thenReturn(trigger);

dataArchiveRollbackJob.updateTaskStatus(1L, TaskStatus.DONE);
}

@Test
public void testExecuteWithInvalidTaskEntity() throws JobExecutionException, SchedulerException {
JobExecutionContext context = mock(JobExecutionContext.class);

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setJobName("DataArchiveRollbackJob");
taskEntity.setJobGroup("group1");
taskEntity.setParametersJson("{\"dataArchiveTaskId\": \"invalidId\"}");

when(context.getMergedJobDataMap().get(anyString())).thenReturn(taskEntity);

dataArchiveRollbackJob.execute(context);

OperableTrigger trigger = mock(OperableTrigger.class);
when(trigger.getKey()).thenReturn(JobKey.jobKey("DataArchiveRollbackJob", "group1"));
when(scheduler.getTrigger(JobKey.jobKey("DataArchiveRollbackJob", "group1"))).thenReturn(trigger);

dataArchiveRollbackJob.updateTaskStatus(1L, TaskStatus.FAILED);
}

@Test
public void testExecuteWithNullTaskUnits() throws JobExecutionException, SchedulerException {
JobExecutionContext context = mock(JobExecutionContext.class);

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setJobName("DataArchiveRollbackJob");
taskEntity.setJobGroup("group1");
taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");

DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
rollbackParameters.setDataArchiveTaskId(2L);

when(context.getMergedJobDataMap().get(anyString())).thenReturn(taskEntity);

when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(new ScheduleTaskEntity(2L, null)));

dataArchiveRollbackJob.execute(context);

OperableTrigger trigger = mock(OperableTrigger.class);
when(trigger.getKey()).thenReturn(JobKey.jobKey("DataArchiveRollbackJob", "group1"));
when(scheduler.getTrigger(JobKey.jobKey("DataArchiveRollbackJob", "group1"))).thenReturn(trigger);

dataArchiveRollbackJob.updateTaskStatus(1L, TaskStatus.FAILED);
}

@Test
public void testExecuteWithNullSourceDatabaseId() throws JobExecutionException, SchedulerException {
JobExecutionContext context = mock(JobExecutionContext.class);

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setJobName("DataArchiveRollbackJob");
taskEntity.setJobGroup("group1");
taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");

DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
rollbackParameters.setDataArchiveTaskId(2L);

when(context.getMergedJobDataMap().get(anyString())).thenReturn(taskEntity);

List<DlmTask> taskUnits = new ArrayList<>();
DlmTask taskUnit = new DlmTask();
taskUnit.setSourceDatabaseId(null);
taskUnit.setTargetDatabaseId(4L);
taskUnit.setJobType(JobType.ARCHIVE);
taskUnit.setStatus(TaskStatus.PREPARING);
taskUnits.add(taskUnit);

String jsonTaskUnits = JsonUtils.toJson(taskUnits);
when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(new ScheduleTaskEntity(2L, jsonTaskUnits)));

dataArchiveRollbackJob.execute(context);

OperableTrigger trigger = mock(OperableTrigger.class);
when(trigger.getKey()).thenReturn(JobKey.jobKey("DataArchiveRollbackJob", "group1"));
when(scheduler.getTrigger(JobKey.jobKey("DataArchiveRollbackJob", "group1"))).thenReturn(trigger);

dataArchiveRollbackJob.updateTaskStatus(1L, TaskStatus.FAILED);
}

@Test
public void testExecuteWithNullTargetDatabaseId() throws JobExecutionException, SchedulerException {
JobExecutionContext context = mock(JobExecutionContext.class);

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setJobName("DataArchiveRollbackJob");
taskEntity.setJobGroup("group1");
taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");

DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
rollbackParameters.setDataArchiveTaskId(2L);

when(context.getMergedJobDataMap().get(anyString())).thenReturn(taskEntity);

List<DlmTask> taskUnits = new ArrayList<>();
DlmTask taskUnit = new DlmTask();
taskUnit.setSourceDatabaseId(3L);
taskUnit.setTargetDatabaseId(null);
taskUnit.setJobType(JobType.ARCHIVE);
taskUnit.setStatus(TaskStatus.PREPARING);
taskUnits.add(taskUnit);

String jsonTaskUnits = JsonUtils.toJson(taskUnits);
when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(new ScheduleTaskEntity(2L, jsonTaskUnits)));

dataArchiveRollbackJob.execute(context);

OperableTrigger trigger = mock(OperableTrigger.class);
when(trigger.getKey()).thenReturn(JobKey.jobKey("DataArchiveRollbackJob", "group1"));
when(scheduler.getTrigger(JobKey.jobKey("DataArchiveRollbackJob", "group1"))).thenReturn(trigger);

dataArchiveRollbackJob.updateTaskStatus(1L, TaskStatus.FAILED);
}
}