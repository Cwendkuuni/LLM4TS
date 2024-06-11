import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.QuartzJobMethodInvoker;
import org.springframework.test.util.ReflectionTestUtils;

public class DataArchiveDeleteJobTest {

private DataArchiveDeleteJob dataArchiveDeleteJob;
private JobExecutionContext jobExecutionContext;
private ScheduleTaskEntity taskEntity;
private ScheduleTaskEntity dataArchiveTask;
private DataArchiveClearParameters dataArchiveClearParameters;
private List<DlmTask> taskUnits;
private JobType jobType;

@Before
public void setUp() {
dataArchiveDeleteJob = spy(new DataArchiveDeleteJob());

jobExecutionContext = mock(JobExecutionContext.class);

taskEntity = new ScheduleTaskEntity();
ReflectionTestUtils.setField(dataArchiveDeleteJob, "scheduleTaskRepository",
mock(com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository.class));

dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId(1L);

taskUnits = Arrays.asList(new DlmTask(), new DlmTask());

doReturn(Optional.of(dataArchiveTask)).when(dataArchiveDeleteJob).findDataArchiveTaskById(any());
doReturn(taskUnits).when(dataArchiveDeleteJob).prepareTasksForClear(any(), any());
doReturn(JobType.DELETE).when(dataArchiveDeleteJob).getJobType(any());

}

@Test
public void testExecute_dataArchiveTaskNotFound() {
dataArchiveDeleteJob.execute(jobExecutionContext);

verify(dataArchiveDeleteJob, times(1)).findDataArchiveTaskById(1L);
verify(dataArchiveDeleteJob, times(1)).updateStatusById(any(), any());
verify(dataArchiveDeleteJob, times(0)).prepareTasksForClear(any(), any());
verify(dataArchiveDeleteJob, times(0)).executeTask(any(), any());
verify(dataArchiveDeleteJob, times(0)).getJobType(any());
}

@Test
public void testExecute_dataArchiveTaskNotDone() {
dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setStatus(TaskStatus.PREPARING);

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(dataArchiveDeleteJob, times(1)).findDataArchiveTaskById(1L);
verify(dataArchiveDeleteJob, times(1)).updateStatusById(any(), any());
verify(dataArchiveDeleteJob, times(0)).prepareTasksForClear(any(), any());
verify(dataArchiveDeleteJob, times(0)).executeTask(any(), any());
verify(dataArchiveDeleteJob, times(0)).getJobType(any());
}

@Test
public void testExecute_prepareTasksForClearSuccess() {
dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setStatus(TaskStatus.DONE);

doReturn(taskUnits).when(dataArchiveDeleteJob).prepareTasksForClear(taskEntity, dataArchiveClearParameters);

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(dataArchiveDeleteJob, times(1)).findDataArchiveTaskById(1L);
verify(dataArchiveDeleteJob, times(1)).prepareTasksForClear(any(), any());
verify(dataArchiveDeleteJob, times(1)).executeTask(any(), any());
verify(dataArchiveDeleteJob, times(1)).getJobType(any());
verify(dataArchiveDeleteJob, times(1)).updateStatusById(any(), any());
}

private Optional<ScheduleTaskEntity> findDataArchiveTaskById(Long dataArchiveTaskId) {
// Mock implementation
return Optional.ofNullable(dataArchiveTask);
}

private List<DlmTask> prepareTasksForClear(ScheduleTaskEntity taskEntity,
DataArchiveClearParameters dataArchiveClearParameters) {
// Mock implementation
return taskUnits;
}

private JobType getJobType(ScheduleTaskEntity taskEntity) {
// Mock implementation
return jobType;
}

private void executeTask(Long taskId, List<DlmTask> taskUnits) {
// Mock implementation
}

private TaskStatus getTaskStatus(List<DlmTask> taskUnits) {
// Mock implementation
return TaskStatus.SUCCESS;
}

private void updateStatusById(Long id, TaskStatus status) {
// Mock implementation
}
}