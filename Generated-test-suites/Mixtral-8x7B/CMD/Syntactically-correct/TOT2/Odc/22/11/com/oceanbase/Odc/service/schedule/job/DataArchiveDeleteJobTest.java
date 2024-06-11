import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

@Mock
private ScheduleTaskRepository scheduleTaskRepository;

@Mock
private JobExecutionContext jobExecutionContext;

private DataArchiveDeleteJob dataArchiveDeleteJob;

private static final Logger logger = LoggerFactory.getLogger(DataArchiveDeleteJobTest.class);

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);
dataArchiveDeleteJob = new DataArchiveDeleteJob(scheduleTaskRepository);
}

@Test
public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
// Arrange
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson("{\"dataArchiveTaskId\": 10}");

when(scheduleTaskRepository.findById(10L)).thenReturn(Optional.empty());

// Act and Assert
dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).findById(10L);
verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.FAILED);
verifyNoMoreInteractions(scheduleTaskRepository);
}

@Test
public void testExecute_DataArchiveTaskNotDone() throws JobExecutionException {
// Arrange
ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(10L);
dataArchiveTask.setStatus(TaskStatus.RUNNING);

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson("{\"dataArchiveTaskId\": 10}");

when(scheduleTaskRepository.findById(10L)).thenReturn(Optional.of(dataArchiveTask));

// Act and Assert
dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).findById(10L);
verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.FAILED);
verifyNoMoreInteractions(scheduleTaskRepository);
}

@Test
public void testExecute_PrepareTasksForClear() throws JobExecutionException {
// Arrange
ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(10L);
dataArchiveTask.setStatus(TaskStatus.DONE);

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson("{\"dataArchiveTaskId\": 10}");

List<DlmTask> taskUnits = new ArrayList<>();
taskUnits.add(new DlmTask());
taskUnits.add(new DlmTask());

String dataArchiveTaskResultJson = JsonUtils.toJson(taskUnits);
dataArchiveTask.setResultJson(dataArchiveTaskResultJson);

when(scheduleTaskRepository.findById(10L)).thenReturn(Optional.of(dataArchiveTask));

// Act and Assert
dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).findById(10L);
verify(scheduleTaskRepository, times(1)).updateStatusById(1L, any());
verifyNoMoreInteractions(scheduleTaskRepository);
}
}