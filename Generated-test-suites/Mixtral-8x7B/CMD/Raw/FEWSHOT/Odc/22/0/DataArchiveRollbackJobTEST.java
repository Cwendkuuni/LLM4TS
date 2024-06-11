import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataArchiveRollbackJobTest {

private static final Logger LOGGER = LoggerFactory.getLogger(DataArchiveRollbackJobTest.class);

private ScheduleTaskRepository scheduleTaskRepository;

private DataArchiveRollbackJob dataArchiveRollbackJob;

@Before
public void setUp() {
scheduleTaskRepository = mock(ScheduleTaskRepository.class);
dataArchiveRollbackJob = spy(new DataArchiveRollbackJob() {
@Override
protected Logger getLogger() {
return LOGGER;
}
});
doReturn(scheduleTaskRepository).when(dataArchiveRollbackJob).getScheduleTaskRepository();
}

@Test
public void testExecute_whenTaskEntityIsNull_thenLogsWarningAndUpdatesStatusToFailed() throws JobExecutionException {
JobExecutionContext context = mock(JobExecutionContext.class);
dataArchiveRollbackJob.execute(context);
verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
}

@Test
public void testExecute_whenDataArchiveTaskNotFound_thenLogsWarningAndUpdatesStatusToFailed() throws JobExecutionException {
JobExecutionContext context = mock(JobExecutionContext.class);
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
context.setResult(taskEntity);
doReturn(Optional.empty()).when(scheduleTaskRepository).findById(anyLong());
dataArchiveRollbackJob.execute(context);
verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
}

@Test
public void testExecute_whenDataArchiveTaskFound_thenPreparesTasksForRollbackAndExecutesThem() throws JobExecutionException {
JobExecutionContext context = mock(JobExecutionContext.class);
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson("{\"dataArchiveTaskId\": 10}");
context.setResult(taskEntity);
ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(10L);
dataArchiveTask.setResultJson("["
+ "{\"sourceDatabaseId\": 100, \"targetDatabaseId\": 200, \"jobType\": \"JOB_TYPE\", \"status\": \"STATUS\"},"
+ "{\"sourceDatabaseId\": 300, \"targetDatabaseId\": 400, \"jobType\": \"JOB_TYPE\", \"status\": \"STATUS\"}"
+ "]");
doReturn(Optional.of(dataArchiveTask)).when(scheduleTaskRepository).findById(anyLong());
List<DlmTask> taskUnits = new ArrayList<>();
taskUnits.add(DlmJobIdUtil.generateHistoryJobTask(1L, 1, 1, 100, 200, JobType.DATA_ARCHIVE, TaskStatus.PREPARING));
taskUnits.add(DlmJobIdUtil.generateHistoryJobTask(1L, 2, 2, 300, 400, JobType.DATA_ARCHIVE, TaskStatus.PREPARING));
doReturn(taskUnits).when(dataArchiveRollbackJob).jsonToDlmTasks(dataArchiveTask.getResultJson());
dataArchiveRollbackJob.execute(context);
verify(dataArchiveRollbackJob, times(1)).jsonToDlmTasks(dataArchiveTask.getResultJson());
verify(dataArchiveRollbackJob, times(1)).executeTask(anyLong(), any());
verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
}

private List<DlmTask> jsonToDlmTasks(String resultJson) {
List<DlmTask> dlmTasks = new ArrayList<>();
// Add your JsonUtils.fromJson logic here
return dlmTasks;
}

private void executeTask(Long taskId, List<DlmTask> dlmTasks) throws JobExecutionException {
// Add your executeTask logic here
}

private TaskStatus getTaskStatus(List<DlmTask> dlmTasks) {
// Add your getTaskStatus logic here
return TaskStatus.SUCCESS;
}

private Optional<ScheduleTaskEntity> findById(Long id) {
// Add your findById logic here
return Optional.empty();
}
}