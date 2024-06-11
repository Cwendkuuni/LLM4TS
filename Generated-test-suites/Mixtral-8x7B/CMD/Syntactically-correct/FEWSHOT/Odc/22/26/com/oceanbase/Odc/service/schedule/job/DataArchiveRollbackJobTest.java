import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz. jobs.junit.methods.TestDescription;
import org.quartz.scheduler.JobDetail;
import org.quartz.scheduler.JobKey;
import org.quartz.scheduler.Scheduler;
import org.quartz.scheduler.SchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataArchiveRollbackJobTest {

private static final Logger LOGGER = LoggerFactory.getLogger(DataArchiveRollbackJobTest.class);

private DataArchiveRollbackJob dataArchiveRollbackJob;

private JobExecutionContext jobExecutionContext;

private ScheduleTaskEntity taskEntity;

private ScheduleTaskEntity dataArchiveTask;

private List<DlmTask> taskUnits;

private DataArchiveRollbackParameters rollbackParameters;

private Scheduler scheduler;

private ScheduleTaskRepository scheduleTaskRepository;

@Before
public void setUp() throws Exception {
 taskUnits = new ArrayList<>();
 taskUnits.add(new DlmTask());
 taskUnits.add(new DlmTask());
rollbackParameters = new DataArchiveRollbackParameters();
rollbackParameters.setDataArchiveTaskId(1L);
dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(2L);
dataArchiveTask.setResultJson("[]");
dataArchiveTask.setParametersJson(JsonUtils.toJson(rollbackParameters));
taskEntity = new ScheduleTaskEntity();
taskEntity.setId(3L);
taskEntity.setResult(dataArchiveTask);
taskEntity.setJobName("testJob");
taskEntity.setJobGroup("testGroup");
scheduleTaskRepository = mock(ScheduleTaskRepository.class);
when(scheduleTaskRepository.findById(eq(1L))).thenReturn(Optional.of(dataArchiveTask));
when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class))).thenReturn(true);
dataArchiveRollbackJob = new DataArchiveRollbackJob();
dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
jobExecutionContext = mock(JobExecutionContext.class);
when(jobExecutionContext.getResult()).thenReturn(taskEntity);
scheduler = mock(Scheduler.class);
when(jobExecutionContext.getScheduler()).thenReturn(scheduler);
JobKey jobKey = new JobKey("testJob", "testGroup");
when(scheduler.checkExists(jobKey)).thenReturn(true);
JobDetail jobDetail = mock(JobDetail.class);
when(jobDetail.getKey()).thenReturn(jobKey);
when(scheduler.getJobDetail(jobKey)).thenReturn(jobDetail);
}

@Test
@TestDescription("Test successful execution of DataArchiveRollbackJob")
public void testExecuteSuccess() throws JobExecutionException {
when(scheduleTaskRepository.findById(eq(rollbackParameters.getDataArchiveTaskId())))
.thenReturn(Optional.of(dataArchiveTask));
dataArchiveRollbackJob.execute(jobExecutionContext);
verify(scheduleTaskRepository, times(1))
.updateStatusById(eq(taskEntity.getId()), eq(TaskStatus.DONE));
}

@Test
@TestDescription("Test failure of DataArchiveRollbackJob due to non-existent data archive task")
public void testExecuteFailure() throws JobExecutionException {
when(scheduleTaskRepository.findById(eq(rollbackParameters.getDataArchiveTaskId())))
.thenReturn(Optional.empty());
dataArchiveRollbackJob.execute(jobExecutionContext);
verify(scheduleTaskRepository, times(1))
.updateStatusById(eq(taskEntity.getId()), eq(TaskStatus.FAILED));
}

@Test
@TestDescription("Test successful preparation of task units for rollback")
public void testPrepareTasksForRollback() throws Exception {
dataArchiveRollbackJob.prepareTasksForRollback(taskUnits, 3L, "testJob", "testGroup");
DlmTask firstTask = taskUnits.get(0);
assertEquals(DlmJobIdUtil.generateHistoryJobId("testJob", "testGroup", 3L, 2), firstTask.getId());
assertEquals(firstTask.getSourceDatabaseId(), firstTask.getTargetDatabaseId());
assertEquals(firstTask.getJobType(), JobType.ROLLBACK);
}

@Test
@TestDescription("Test successful execution of task units")
public void testExecuteTask() throws Exception {
dataArchiveRollbackJob.executeTask(3L, taskUnits);
verify(scheduleTaskRepository, times(1))
.updateStatusById(eq(3L), any(TaskStatus.class));
}

@Test
@TestDescription("Test successful determination of task status")
public void testGetTaskStatus() throws Exception {
List<TaskStatus> taskStatuses = new ArrayList<>();
taskStatuses.add(TaskStatus.DONE);
taskStatuses.add(TaskStatus.FAILED);
TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits, taskStatuses);
assertEquals(TaskStatus.FAILED, taskStatus);
}
}