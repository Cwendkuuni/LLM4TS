import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class DataArchiveDeleteJobTest {

@Mock
private ScheduleTaskRepository scheduleTaskRepository;

@InjectMocks
private DataArchiveDeleteJob dataArchiveDeleteJob;

private ScheduleTaskEntity scheduleTaskEntity;
private DataArchiveClearParameters dataArchiveClearParameters;
private List<DlmTask> dlmTasks;

@BeforeEach
public void setup() {
MockitoAnnotations.openMocks(this);

scheduleTaskEntity = new ScheduleTaskEntity();
scheduleTaskEntity.setId(1L);
scheduleTaskEntity.setJobName("testJob");
scheduleTaskEntity.setJobGroup("testGroup");
scheduleTaskEntity.setStatus(TaskStatus.DONE);
scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
scheduleTaskEntity.setResultJson("[]");

dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId(2L);

dlmTasks = new ArrayList<>();
dlmTasks.add(new DlmTask());
dlmTasks.add(new DlmTask());
}

@Test
public void testExecute_whenTaskEntityIsNull_thenLogWarnAndUpdateStatus() {
// Arrange
jobThread = Thread.currentThread();
scheduleTaskEntity.setStatus(null);

when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

// Act
dataArchiveDeleteJob.execute(null);

// Assert
Assertions.assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
Assertions.assertTrue(jobThread.isInterrupted());
}

@Test
public void testExecute_whenDataArchiveTaskNotPresent_thenLogWarnAndUpdateStatus() {
// Arrange
when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));

// Act
dataArchiveDeleteJob.execute(null);

// Assert
Assertions.assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
}

@Test
public void testExecute_whenDataArchiveTaskNotDone_thenLogWarnAndUpdateStatus() {
// Arrange
scheduleTaskEntity.setStatus(TaskStatus.RUNNING);

when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));

// Act
dataArchiveDeleteJob.execute(null);

// Assert
Assertions.assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
}

@Test
public void testExecute_whenDataArchiveTaskPresentAndDone_thenExecuteTasks() throws Exception {
// Arrange
when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
when(scheduleTaskRepository.updateStatusById(anyLong(), any())).thenReturn(null);

// Act
dataArchiveDeleteJob.execute(null);

// Assert
Assertions.assertFalse(dlmTasks.isEmpty());
Assertions.assertEquals(TaskStatus.PREPARING, dlmTasks.get(0).getStatus());
}

}