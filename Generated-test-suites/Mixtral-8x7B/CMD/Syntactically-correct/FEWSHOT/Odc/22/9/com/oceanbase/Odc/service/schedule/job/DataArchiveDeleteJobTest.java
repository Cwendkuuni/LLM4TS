import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
import org.quartz.impl.JobExecutionContextImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataArchiveDeleteJobTest {
private static final Logger log = LoggerFactory.getLogger(DataArchiveDeleteJob.class);
private DataArchiveDeleteJob dataArchiveDeleteJob;
private JobExecutionContext jobExecutionContext;
private ScheduleTaskEntity scheduleTaskEntity;
private DataArchiveClearParameters dataArchiveClearParameters;
private List<DlmTask> dlmTasks;

@Before
public void setup() {
dataArchiveDeleteJob = spy(new DataArchiveDeleteJob());
jobExecutionContext = mock(JobExecutionContextImpl.class);
scheduleTaskEntity = new ScheduleTaskEntity();
dataArchiveClearParameters = new DataArchiveClearParameters();
dlmTasks = Arrays.asList(mock(DlmTask.class), mock(DlmTask.class));
}

@Test
public void testExecute_DataArchiveTaskNotFound() {
doReturn(Optional.of(scheduleTaskEntity)).doReturn(Optional.empty()).when(dataArchiveDeleteJob)
.scheduleTaskRepositoryFindById(anyString());
dataArchiveDeleteJob.execute(jobExecutionContext);
verify(dataArchiveDeleteJob, times(1)).scheduleTaskRepositoryUpdateStatusById(anyString(), any(TaskStatus.class));
}

@Test
public void testExecute_DataArchiveTaskNotDone() {
doReturn(Optional.of(scheduleTaskEntity)).when(dataArchiveDeleteJob)
.scheduleTaskRepositoryFindById(anyString());
doReturn(TaskStatus.RUNNING).when(dataArchiveDeleteJob).getTaskStatus(anyList());
dataArchiveDeleteJob.execute(jobExecutionContext);
verify(dataArchiveDeleteJob, times(1)).scheduleTaskRepositoryUpdateStatusById(anyString(), any(TaskStatus.class));
}

@Test
public void testExecute_DataArchiveTaskDone_ExecuteTaskSuccess() {
doReturn(Optional.of(scheduleTaskEntity)).when(dataArchiveDeleteJob)
.scheduleTaskRepositoryFindById(anyString());
doReturn(TaskStatus.DONE).when(dataArchiveDeleteJob).getTaskStatus(anyList());
doReturn(JsonUtils.toJson(dlmTasks)).when(dataArchiveDeleteJob).executeTask(anyString(), anyList());
dataArchiveDeleteJob.execute(jobExecutionContext);
verify(dataArchiveDeleteJob, times(1)).scheduleTaskRepositoryUpdateStatusById(anyString(), any(TaskStatus.class));
}

@Test
public void testExecute_DataArchiveTaskDone_ExecuteTaskFailed() {
doReturn(Optional.of(scheduleTaskEntity)).when(dataArchiveDeleteJob)
.scheduleTaskRepositoryFindById(anyString());
doReturn(TaskStatus.DONE).when(dataArchiveDeleteJob).getTaskStatus(anyList());
doReturn(JsonUtils.toJson(dlmTasks)).when(dataArchiveDeleteJob).executeTask(anyString(), anyList());
doReturn(TaskStatus.FAILED).when(dataArchiveDeleteJob).getTaskStatus(anyList());
dataArchiveDeleteJob.execute(jobExecutionContext);
verify(dataArchiveDeleteJob, times(1)).scheduleTaskRepositoryUpdateStatusById(anyString(), any(TaskStatus.class));
}

private JobExecutionContext createJobExecutionContext(ScheduleTaskEntity task) {
jobExecutionContext = mock(JobExecutionContextImpl.class);
doReturn(task).when(jobExecutionContext).getResult();
return jobExecutionContext;
}

private Optional<ScheduleTaskEntity> scheduleTaskRepositoryFindById(String id) {
return dataArchiveDeleteJob.scheduleTaskRepository.findById(id);
}

private String executeTask(String id, List<DlmTask> dlmTasks) {
return JsonUtils.toJson(dlmTasks);
}

private TaskStatus getTaskStatus(List<DlmTask> dlmTasks) {
return TaskStatus.DONE;
}

private void scheduleTaskRepositoryUpdateStatusById(String id, TaskStatus status) {
dataArchiveDeleteJob.scheduleTaskRepository.updateStatusById(id, status);
}
}