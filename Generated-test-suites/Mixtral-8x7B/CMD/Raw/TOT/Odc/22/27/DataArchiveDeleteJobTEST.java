import static org.mockito.Mockito.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.utils.JsonUtils;
import com.oceanbase.tools.migrator.common.enums.JobType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import static org.quartz.utils.KeyGenerator.generateKey;
import static org.mockito.MockitoAnnotations.initMocks;

public class DataArchiveDeleteJobTest {
private ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);

@Before
public void setup() {
initMocks(this);
}

private JobExecutionContext createMockJobExecutionContext() throws JobExecutionException {
JobExecutionContext context = mock(JobExecutionContext.class);
context.setResult(new ScheduleTaskEntity());
return context;
}

private List<DlmTask> createDlmTaskList(int count) {
List<DlmTask> taskList = new ArrayList<>();
for (int i = 0; i < count; i++) {
DlmTask task = new DlmTask();
task.setId(Long.parseLong(String.valueOf(i)));
taskList.add(task);
}
return taskList;
}

@Test
public void testExecute_withValidParameters_shouldExecuteTasks() {
// Create a mock JobExecutionContext
JobExecutionContext context = createMockJobExecutionContext();

// Set up the necessary objects and data
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("123");
taskEntity.setId(1L);
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

context.setResult(taskEntity);

ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(2L);
dataArchiveTask.setStatus(TaskStatus.DONE);

List<DlmTask> taskUnits = createDlmTaskList(3);

when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
.thenReturn(Optional.of(dataArchiveTask));

when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.FAILED))
.thenReturn(null);

when(scheduleTaskRepository.findById(taskEntity.getId()))
.thenReturn(Optional.of(taskEntity));

when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.FAILED))
.thenReturn(null);

when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), any()))
.thenReturn(null);

// Call the method
DataArchiveDeleteJob job = new DataArchiveDeleteJob();
job.setScheduleTaskRepository(scheduleTaskRepository);
job.execute(context);

// Verify that the tasks were executed and the status was updated
verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.PREPARING);
verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.DONE);
}

@Test
public void testExecute_withNonExistentDataArchiveTask_shouldFailFast() {
// Create a mock JobExecutionContext
JobExecutionContext context = createMockJobExecutionContext();

// Set up the necessary objects and data
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("456");
taskEntity.setId(1L);
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

context.setResult(taskEntity);

when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
.thenReturn(Optional.empty());

// Call the method
DataArchiveDeleteJob job = new DataArchiveDeleteJob();
job.setScheduleTaskRepository(scheduleTaskRepository);
job.execute(context);

// Verify that the task was marked as failed
verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
}

@Test
public void testExecute_withNotFinishedDataArchiveTask_shouldFail() {
// Create a mock JobExecutionContext
JobExecutionContext context = createMockJobExecutionContext();

// Set up the necessary objects and data
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("789");
taskEntity.setId(1L);
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

context.setResult(taskEntity);

ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(2L);
dataArchiveTask.setStatus(TaskStatus.RUNNING);

when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
.thenReturn(Optional.of(dataArchiveTask));

// Call the method
DataArchiveDeleteJob job = new DataArchiveDeleteJob();
job.setScheduleTaskRepository(scheduleTaskRepository);
job.execute(context);

// Verify that the task was marked as failed
verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
}
}