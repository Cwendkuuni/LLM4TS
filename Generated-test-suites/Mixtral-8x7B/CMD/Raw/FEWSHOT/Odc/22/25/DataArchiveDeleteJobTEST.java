import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.core.JobDetail;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.scheduler.Scheduler;
import org.quartz.scheduler.SchedulerException;
import org.quartz.spi.OperableTrigger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataArchiveDeleteJobTest {

private DataArchiveDeleteJob dataArchiveDeleteJob;
private JobExecutionContext jobExecutionContext;
private ScheduleTaskEntity scheduleTaskEntity;
private ScheduleTaskEntity dataArchiveTask;
private DataArchiveClearParameters dataArchiveClearParameters;
private List<DlmTask> taskUnits;
private Scheduler scheduler;

@Before
public void setUp() throws SchedulerException {
dataArchiveDeleteJob = new DataArchiveDeleteJob();

// Initialize JobExecutionContext
jobExecutionContext = mock(JobExecutionContext.class);

// Initialize ScheduleTaskEntity
scheduleTaskEntity = mock(ScheduleTaskEntity.class);

// Initialize DataArchiveClearParameters
dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("123456");

// Initialize List<DlmTask>
taskUnits = new ArrayList<>();

// Initialize Scheduler
scheduler = mock(Scheduler.class);
when(jobExecutionContext.getScheduler()).thenReturn(scheduler);
}

@Test
public void testExecute_DataArchiveTaskNotFound_UpdateTaskStatusToFailed() throws JobExecutionException, SchedulerException {
// Setup
dataArchiveClearParameters.setDataArchiveTaskId("invalid_id");
when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
.thenReturn(Optional.ofNullable(null));

// Run
dataArchiveDeleteJob.execute(jobExecutionContext);

// Verify
verify(scheduleTaskRepository, times(1))
.updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
}

@Test
public void testExecute_DataArchiveTaskNotDone_UpdateTaskStatusToFailed() throws JobExecutionException, SchedulerException {
// Setup
dataArchiveTask = mock(ScheduleTaskEntity.class);
when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.RUNNING);

when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
.thenReturn(Optional.of(dataArchiveTask));

// Run
dataArchiveDeleteJob.execute(jobExecutionContext);

// Verify
verify(scheduleTaskRepository, times(1))
.updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
}

@Test
public void testExecute_PrepareTasksForClearAndExecuteTask_UpdateTaskStatus() throws JobExecutionException, SchedulerException {
// Setup
dataArchiveTask = mock(ScheduleTaskEntity.class);
when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);

when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
.thenReturn(Optional.of(dataArchiveTask));

// Initialize List<DlmTask>
DlmTask taskUnit1 = new DlmTask();
taskUnit1.setId("1");
taskUnits.add(taskUnit1);

DlmTask taskUnit2 = new DlmTask();
taskUnit2.setId("2");
taskUnits.add(taskUnit2);

// Run
dataArchiveDeleteJob.execute(jobExecutionContext);

// Verify
verify(scheduleTaskRepository, times(1))
.updateStatusById(scheduleTaskEntity.getId(), TaskStatus.PREPARING);

verify(scheduler, times(1))
.triggerJob(any(JobDetail.class), any(OperableTrigger.class));

verify(scheduler, times(1))
.deleteJob(anyString(), anyString());
}
}