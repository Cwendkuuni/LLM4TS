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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.test.util.ReflectionTestUtils;

@SuppressWarnings("unchecked")
public class DataArchiveDeleteJobTest {

private DataArchiveDeleteJob dataArchiveDeleteJob;
private JobExecutionContext jobExecutionContext;
private ScheduleTaskEntity taskEntity;
private ScheduleTaskEntity dataArchiveTask;
private DataArchiveClearParameters dataArchiveClearParameters;
private List<DlmTask> taskUnits;

@Before
public void setup() {
dataArchiveDeleteJob = new DataArchiveDeleteJob();
ReflectionTestUtils.setField(dataArchiveDeleteJob, "scheduleTaskRepository",
mock(ScheduleTaskRepository.class));

jobExecutionContext = mock(JobExecutionContext.class);
taskEntity = new ScheduleTaskEntity();
dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveTask = new ScheduleTaskEntity();
taskUnits = Arrays.asList(new DlmTask(), new DlmTask());

when(jobExecutionContext.getMergedJobDataMap().get("scheduleTask")).thenReturn(taskEntity);
when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters));
when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
.thenReturn(Optional.of(dataArchiveTask));
when(dataArchiveTask.getResultJson()).thenReturn(JsonUtils.toJson(taskUnits));
when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
}

@Test
public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
ReflectionTestUtils.setField(dataArchiveDeleteJob, "scheduleTaskRepository",
mock(ScheduleTaskRepository.class));

when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
.thenReturn(Optional.empty());

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1))
.updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
}

@Test
public void testExecute_DataArchiveTaskNotFinished() throws JobExecutionException {
dataArchiveTask.setStatus(TaskStatus.RUNNING);

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1))
.updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
}

@Test
public void testExecute_Success() throws JobExecutionException {
dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.DONE);
}

private ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);

@Test
public void executeTask_Success() {
dataArchiveDeleteJob.executeTask(1L, taskUnits);

verify(scheduleTaskRepository, times(1))
.saveAll(taskUnits.stream().map(task -> {
task.setId(DlmJobIdUtil.generateHistoryJobId("jobName", "jobGroup", 1L, 2));
task.setJobType(JobType.DELETE);
task.setStatus(TaskStatus.PREPARING);
return task;
}).collect(Collectors.toList()));
}

@Test
public void getTaskStatus_Success() {
when(scheduleTaskRepository.findAll(
Example.of(new ScheduleTaskEntity().setJobName("jobName").setJobGroup("jobGroup").setStatus(TaskStatus.PREPARING)),
GroupMatcher.groupStartsWith("group")))
.thenReturn(taskUnits);

TaskStatus taskStatus = dataArchiveDeleteJob.getTaskStatus(taskUnits);

assertEquals(TaskStatus.PREPARING, taskStatus);
}

private QuartzJobBean.JobCompletionNotification jobCompletionNotification =
mock(QuartzJobBean.JobCompletionNotification.class);

@Test
public void notifyJobStore_Success() throws Exception {
dataArchiveDeleteJob.notifyJobStore(jobCompletionNotification, null, null, false);

verify(jobCompletionNotification, times(1)).notifyJobStore(JobExecutionException.class, false, false);
}
}