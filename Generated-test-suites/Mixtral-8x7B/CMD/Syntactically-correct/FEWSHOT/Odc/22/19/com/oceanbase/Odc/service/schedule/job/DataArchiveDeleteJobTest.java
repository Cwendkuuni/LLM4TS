import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.matchers.GroupMatcher;

@Slf4j
public class DataArchiveDeleteJobTest {

private DataArchiveDeleteJob dataArchiveDeleteJob;
private JobExecutionContext jobExecutionContext;
private ScheduleTaskEntity scheduleTaskEntity;
private ScheduleTaskEntity dataArchiveTask;
private DataArchiveClearParameters dataArchiveClearParameters;
private List<DlmTask> taskUnits;
private DlmTask taskUnit1;
private DlmTask taskUnit2;

@Before
public void setup() {
dataArchiveDeleteJob = new DataArchiveDeleteJob();
jobExecutionContext = mock(JobExecutionContext.class);
scheduleTaskEntity = mock(ScheduleTaskEntity.class);
dataArchiveTask = mock(ScheduleTaskEntity.class);
dataArchiveClearParameters = mock(DataArchiveClearParameters.class);
taskUnits = Arrays.asList(taskUnit1 = mock(DlmTask.class), taskUnit2 = mock(DlmTask.class));
}

@Test
public void testExecute_whenDataArchiveTaskNotFound_thenUpdateTaskStatusToFailed()
throws JobExecutionException {
when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn("123");
when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.empty());

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository).updateStatusById(anyString(), eq(TaskStatus.FAILED));
}

@Test
public void testExecute_whenDataArchiveTaskNotDone_thenUpdateTaskStatusToFailed()
throws JobExecutionException {
when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn("123");
when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(dataArchiveTask));
when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.RUNNING);

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository).updateStatusById(anyString(), eq(TaskStatus.FAILED));
}

@Test
public void testExecute_whenTaskUnitsAreNull_thenDoNothing()
throws JobExecutionException {
when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn("123");
when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(dataArchiveTask));
when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
when(dataArchiveTask.getResultJson()).thenReturn(null);

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository).updateStatusById(anyString(), eq(TaskStatus.DONE));
}

@Test
public void testExecute_whenTaskUnitsAreEmpty_thenDoNothing()
throws JobExecutionException {
when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn("123");
when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(dataArchiveTask));
when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
when(dataArchiveTask.getResultJson()).thenReturn("[]");

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository).updateStatusById(anyString(), eq(TaskStatus.DONE));
}

@Test
public void testExecute_whenTaskUnitsHaveMoreThanOneElement_thenExecuteTask()
throws JobExecutionException {
when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn("123");
when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(dataArchiveTask));
when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
when(dataArchiveTask.getResultJson()).thenReturn(JsonUtils.toJson(taskUnits));

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(executeTask).accept(anyString(), eq(taskUnits));
}

@Test
public void testExecute_whenTaskUnitsHaveOneElement_thenExecuteTask()
throws JobExecutionException {
when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn("123");
when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(dataArchiveTask));
when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
when(dataArchiveTask.getResultJson()).thenReturn(JsonUtils.toJson(taskUnit1));

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(executeTask).accept(anyString(), eq(Arrays.asList(taskUnit1)));
}

@Test
public void testGetTaskStatus_whenAllTasksAreDone_thenReturnDone() {
List<DlmTask> tasks = Arrays.asList(
taskUnit1 = createDlmTask("1", TaskStatus.DONE),
taskUnit2 = createDlmTask("2", TaskStatus.DONE)
);

assertEquals(TaskStatus.DONE, dataArchiveDeleteJob.getTaskStatus(tasks));
}

@Test
public void testGetTaskStatus_whenAllTasksAreFailed_thenReturnFailed() {
List<DlmTask> tasks = Arrays.asList(
taskUnit1 = createDlmTask("1", TaskStatus.FAILED),
taskUnit2 = createDlmTask("2", TaskStatus.FAILED)
);

assertEquals(TaskStatus.FAILED, dataArchiveDeleteJob.getTaskStatus(tasks));
}

@Test
public void testGetTaskStatus_whenAllTasksArePreparing_thenReturnPreparing() {
List<DlmTask> tasks = Arrays.asList(
taskUnit1 = createDlmTask("1", TaskStatus.PREPARING),
taskUnit2 = createDlmTask("2", TaskStatus.PREPARING)
);

assertEquals(TaskStatus.PREPARING, dataArchiveDeleteJob.getTaskStatus(tasks));
}

@Test
public void testGetTaskStatus_whenTasksHaveMixedStatus_thenReturnFailed() {
List<DlmTask> tasks = Arrays.asList(
taskUnit1 = createDlmTask("1", TaskStatus.DONE),
taskUnit2 = createDlmTask("2", TaskStatus.FAILED)
);

assertEquals(TaskStatus.FAILED, dataArchiveDeleteJob.getTaskStatus(tasks));
}

@Test
public void testGetTaskStatus_whenTasksHaveMixedStatusButNotFailed_thenReturnPreparing() {
List<DlmTask> tasks = Arrays.asList(
taskUnit1 = createDlmTask("1", TaskStatus.DONE),
taskUnit2 = createDlmTask("2", TaskStatus.PREPARING)
);

assertEquals(TaskStatus.PREPARING, dataArchiveDeleteJob.getTaskStatus(tasks));
}

private DlmTask createDlmTask(String id, TaskStatus taskStatus) {
DlmTask dlmTask = mock(DlmTask.class);
when(dlmTask.getId()).thenReturn(id);
when(dlmTask.getStatus()).thenReturn(taskStatus);
return dlmTask;
}

}