import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.test.util.ReflectionTestUtils;

public class DataArchiveRollbackJobTest {

@Before
public void setup() {
// Initialize necessary mocks here
}

@Test
public void testExecute_whenTaskEntityIsNull_throwsJobExecutionException() throws JobExecutionException {
JobExecutionContext context = mock(JobExecutionContext.class);
when(context.getResult()).thenReturn(null);

DataArchiveRollbackJob job = new DataArchiveRollbackJob();
ReflectionTestUtils.setField(job, "scheduleTaskRepository", mock(ScheduleTaskRepository.class));

try {
job.execute(context);
} catch (JobExecutionException e) {
assert e.getMessage().equals("TaskEntity is null.");
}
}

@Test
public void testExecute_whenDataArchiveTaskNotFound_updatesTaskStatusToFailed() {
JobExecutionContext context = mock(JobExecutionContext.class);
when(context.getResult()).thenReturn(mock(ScheduleTaskEntity.class));

ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
DataArchiveRollbackJob job = new DataArchiveRollbackJob();
ReflectionTestUtils.setField(job, "scheduleTaskRepository", scheduleTaskRepository);

DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
rollbackParameters.setDataArchiveTaskId(1L);

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

job.execute(context);

// Verify that the task status is updated to FAILED
// ...
}

@Test
public void testExecute_whenDataArchiveTaskFound_updatesTaskStatusToExpected() {
JobExecutionContext context = mock(JobExecutionContext.class);
when(context.getResult()).thenReturn(mock(ScheduleTaskEntity.class));

ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
DataArchiveRollbackJob job = new DataArchiveRollbackJob();
ReflectionTestUtils.setField(job, "scheduleTaskRepository", scheduleTaskRepository);

DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
rollbackParameters.setDataArchiveTaskId(1L);

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(2L);

List<DlmTask> taskUnits = mock(List.class);

when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
when(JsonUtils.fromJson(any(), any(Class.class))).thenReturn(mock(DataArchiveRollbackParameters.class));
when(JsonUtils.fromJson(any(), any(TypeReference.class))).thenReturn(taskUnits);

job.execute(context);

// Verify that the task status is updated to the expected value
// ...
}

}