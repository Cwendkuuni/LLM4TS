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
public void setUp() {
dataArchiveDeleteJob = new DataArchiveDeleteJob();
jobExecutionContext = mock(JobExecutionContext.class);
taskEntity = mock(ScheduleTaskEntity.class);
dataArchiveTask = mock(ScheduleTaskEntity.class);
dataArchiveClearParameters = new DataArchiveClearParameters();
taskUnits = Arrays.asList(new DlmTask(), new DlmTask());

// Set up the necessary mocks
when(jobExecutionContext.getResult()).thenReturn(taskEntity);
when(taskEntity.getParametersJson()).thenReturn("parametersJson");
when(JsonUtils.fromJson(eq("parametersJson"), eq(DataArchiveClearParameters.class)))
.thenReturn(dataArchiveClearParameters);
when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn("dataArchiveTaskId");

when(scheduleTaskRepository.findById(eq("dataArchiveTaskId"))).thenReturn(Optional.of(dataArchiveTask));
when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
when(dataArchiveTask.getResultJson()).thenReturn("resultJson");
when(JsonUtils.fromJson(eq("resultJson"), eq(new TypeReference<List<DlmTask>>() {})))
.thenReturn(taskUnits);

// Set up the necessary reflection to bypass private method calls
ReflectionTestUtils.setField(dataArchiveDeleteJob, "scheduleTaskRepository",
mock(com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository.class));
}

@Test
public void testExecute_whenDataArchiveTaskNotFound_thenUpdateTaskStatusToFailed()
throws JobExecutionException {
// Arrange
when(scheduleTaskRepository.findById(eq("dataArchiveTaskId"))).thenReturn(Optional.empty());

// Act
dataArchiveDeleteJob.execute(jobExecutionContext);

// Assert
verify(scheduleTaskRepository).updateStatusById(eq(taskEntity.getId()), eq(TaskStatus.FAILED));
}

@Test
public void testExecute_whenDataArchiveTaskNotDone_thenUpdateTaskStatusToFailed()
throws JobExecutionException {
// Arrange
when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.RUNNING);

// Act
dataArchiveDeleteJob.execute(jobExecutionContext);

// Assert
verify(scheduleTaskRepository).updateStatusById(eq(taskEntity.getId()), eq(TaskStatus.FAILED));
}

@Test
public void testExecute_whenExecuteTaskSuccess_thenUpdateTaskStatusToMatchTaskStatus()
throws JobExecutionException {
// Arrange
TaskStatus expectedTaskStatus = TaskStatus.SUCCESS;
when(dataArchiveDeleteJob.executeTask(eq(taskEntity.getId()), eq(taskUnits)))
.thenReturn(expectedTaskStatus);

// Act
dataArchiveDeleteJob.execute(jobExecutionContext);

// Assert
verify(scheduleTaskRepository).updateStatusById(eq(taskEntity.getId()), eq(expectedTaskStatus));
}

@Test
public void testExecute_whenExecuteTaskFailed_thenUpdateTaskStatusToFailed()
throws JobExecutionException {
// Arrange
when(dataArchiveDeleteJob.executeTask(eq(taskEntity.getId()), eq(taskUnits)))
.thenReturn(TaskStatus.FAILED);

// Act
dataArchiveDeleteJob.execute(jobExecutionContext);

// Assert
verify(scheduleTaskRepository).updateStatusById(eq(taskEntity.getId()), eq(TaskStatus.FAILED));
}

private class TestQuartzJob extends QuartzJobBean {
@Override
protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
dataArchiveDeleteJob.execute(context);
}
}
}