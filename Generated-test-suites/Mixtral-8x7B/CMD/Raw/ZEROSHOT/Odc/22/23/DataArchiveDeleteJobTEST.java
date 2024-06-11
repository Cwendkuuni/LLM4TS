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
import org.quartz.*;
import org.quartz.core.JobDetail;
import org.quartz.core.JobExecutor;
import org.quartz.core. scheduler.Scheduler;
import org.quartz.core.scheduler.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.simpl.RemoteScheduler;
import org.quartz.simpl.RAMJobStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

private Scheduler scheduler;
private DataArchiveDeleteJob dataArchiveDeleteJob;
private ScheduleTaskEntity taskEntity;
private DataArchiveClearParameters dataArchiveClearParameters;
private List<DlmTask> taskUnits;

@Before
public void setUp() throws SchedulerException {
scheduler = new StdSchedulerFactory().getScheduler();
scheduler.start();

dataArchiveDeleteJob = spy(new DataArchiveDeleteJob());

taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson("{}");
taskEntity.setResultJson("[]");
taskEntity.setStatus(TaskStatus.DONE);

dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId(2L);

taskUnits = new ArrayList<>();

doReturn(new RAMJobStore()).when(dataArchiveDeleteJob).getJobStore();
doReturn(scheduler).when(dataArchiveDeleteJob).getScheduler();
doReturn(mock(JobExecutionContext.class)).when(dataArchiveDeleteJob).createJobExecutionContext(any(), any());
doReturn(mock(JobExecutor.class)).when(dataArchiveDeleteJob).getJobExecutor();
doReturn(mock(RemoteScheduler.class)).when(dataArchiveDeleteJob).getRemoteScheduler();
}

@Test
public void testExecute_DataArchiveTaskNotFound() {
dataArchiveClearParameters.setDataArchiveTaskId(100L); // make it not found

doReturn(Optional.of(taskEntity)).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());

dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
}

@Test
public void testExecute_DataArchiveTaskNotDone() {
taskEntity.setStatus(TaskStatus.RUNNING);

doReturn(Optional.of(taskEntity)).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());

dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
}

@Test
public void testExecute_PrepareTasksForClear() throws Exception {
DlmTask dlmTask1 = new DlmTask();
dlmTask1.setId(1L);
dlmTask1.setJobType(JobType.DELETE);

DlmTask dlmTask2 = new DlmTask();
dlmTask2.setId(2L);
dlmTask2.setJobType(JobType.DELETE);

taskUnits.add(dlmTask1);
taskUnits.add(dlmTask2);

dataArchiveClearParameters.setDataArchiveTaskId(2L);
doReturn(Optional.of(taskEntity)).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());

JobDetail jobDetail = mock(JobDetail.class);
JobExecutionContext jobExecutionContext = new JobExecutionContext(scheduler, jobDetail, mock(Trigger.class), null, null);

doReturn(jobExecutionContext).when(dataArchiveDeleteJob).createJobExecutionContext(any(), any());

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(dataArchiveDeleteJob, times(1)).executeTask(taskEntity.getId(), taskUnits);
}

private void simulateTaskStatus(List<DlmTask> taskUnits, TaskStatus... statuses) {
for (int i = 0; i < taskUnits.size() && i < statuses.length; i++) {
taskUnits.get(i).setStatus(statuses[i]);
}
}

@Test
public void testExecute_UpdateTaskStatus() throws Exception {
DlmTask dlmTask1 = new DlmTask();
dlmTask1.setId(1L);
dlmTask1.setJobType(JobType.DELETE);

DlmTask dlmTask2 = new DlmTask();
dlmTask2.setId(2L);
dlmTask2.setJobType(JobType.DELETE);

taskUnits.add(dlmTask1);
taskUnits.add(dlmTask2);

dataArchiveClearParameters.setDataArchiveTaskId(2L);
doReturn(Optional.of(taskEntity)).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());

JobDetail jobDetail = mock(JobDetail.class);
JobExecutionContext jobExecutionContext = new JobExecutionContext(scheduler, jobDetail, mock(Trigger.class), null, null);

doReturn(jobExecutionContext).when(dataArchiveDeleteJob).createJobExecutionContext(any(), any());

simulateTaskStatus(taskUnits, TaskStatus.DONE, TaskStatus.FAILED);

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
}

}