import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
public class DataArchiveDeleteJobTest {

@MockBean
private scheduleTaskRepository scheduleTaskRepository;

@Autowired
private DataArchiveDeleteJob dataArchiveDeleteJob;

private JobExecutionContext jobExecutionContext;

@Before
public void setup() {
JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
Mockito.when(context.getResult()).thenReturn(new ScheduleTaskEntity());
jobExecutionContext = context;
}

@Test
public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("123");

Mockito.when(scheduleTaskRepository.findById("123"))
.thenReturn(Optional.of(new ScheduleTaskEntity()));

dataArchiveDeleteJob.execute(jobExecutionContext);

Mockito.verify(scheduleTaskRepository).updateStatusById(Mockito.anyString(), TaskStatus.FAILED);
}

@Test
public void testExecute_DataArchiveTaskNotDone() throws JobExecutionException {
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("123");

ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId("123");
dataArchiveTask.setStatus(TaskStatus.RUNNING);

Mockito.when(scheduleTaskRepository.findById("123"))
.thenReturn(Optional.of(dataArchiveTask));

dataArchiveDeleteJob.execute(jobExecutionContext);

Mockito.verify(scheduleTaskRepository).updateStatusById(Mockito.anyString(), TaskStatus.FAILED);
}

@Test
public void testExecute_PrepareTasksForClear() throws JobExecutionException {
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("123");

ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId("123");
dataArchiveTask.setStatus(TaskStatus.DONE);

List<DlmTask> taskUnits = createDlmTasks();

Mockito.when(scheduleTaskRepository.findById("123"))
.thenReturn(Optional.of(dataArchiveTask));

Mockito.when(JsonUtils.fromJson(Mockito.anyString(), Mockito.eq(DataArchiveClearParameters.class)))
.thenReturn(dataArchiveClearParameters);

Mockito.when(JsonUtils.fromJson(Mockito.anyString(), Mockito.eq(new TypeReference<List<DlmTask>>() {})))
.thenReturn(taskUnits);

dataArchiveDeleteJob.execute(jobExecutionContext);

// Verify tasks are updated with correct job id, job type, and status
for (DlmTask task : taskUnits) {
Assert.assertEquals(DlmJobIdUtil.generateHistoryJobId(jobExecutionContext.getJobDetail().getName(),
jobExecutionContext.getJobDetail().getGroup(), jobExecutionContext.getJobRunTime().getJobId(), taskUnits.size()),
task.getId());
Assert.assertEquals(JobType.DELETE, task.getJobType());
Assert.assertEquals(TaskStatus.PREPARING, task.getStatus());
}
}

private List<DlmTask> createDlmTasks() {
// Create and return a sample list of DlmTask
return null;
}
}