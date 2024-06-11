import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveDeleteJobTest {

@Mock
private ScheduleTaskRepository scheduleTaskRepository;

@Mock
private AbstractDlmJob abstractDlmJob;

@InjectMocks
private DataArchiveDeleteJob dataArchiveDeleteJob;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);
}

@Test
public void testExecute() {
// Prepare mock ScheduleTaskEntity
ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
when(taskEntity.getParametersJson()).thenReturn("{}");
when(taskEntity.getResultJson()).thenReturn("[]");
when(taskEntity.getId()).thenReturn(1L);
when(taskEntity.getJobName()).thenReturn("testJobName");
when(taskEntity.getJobGroup()).thenReturn("testJobGroup");
when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));
when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenReturn(true);

// Prepare mock DataArchiveClearParameters
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId(2L);

// Prepare mock taskUnits
List<DlmTask> taskUnits = mock(List.class);
DlmTask taskUnit = new DlmTask();
taskUnit.setId(3L);
taskUnit.setJobType(JobType.DELETE);
taskUnit.setStatus(TaskStatus.PREPARING);
when(taskUnits.size()).thenReturn(1);
when(taskUnits.get(0)).thenReturn(taskUnit);
when(JsonUtils.fromJson(anyString(), any(TypeReference.class))).thenReturn(taskUnits);

// Execute the method
dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

// Verify the result
assertEquals(TaskStatus.DONE, taskEntity.getStatus());
}

@Test
public void testExecute_whenDataArchiveTaskNotPresent_shouldReturnFailed() {
// Prepare mock ScheduleTaskEntity
when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

// Execute the method
dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

// Verify the result
assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
}

@Test
public void testExecute_whenDataArchiveTaskNotFinished_shouldReturnFailed() {
// Prepare mock ScheduleTaskEntity
ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
when(taskEntity.getParametersJson()).thenReturn("{}");
when(taskEntity.getResultJson()).thenReturn("[]");
when(taskEntity.getId()).thenReturn(1L);
when(taskEntity.getJobName()).thenReturn("testJobName");
when(taskEntity.getJobGroup()).thenReturn("testJobGroup");
when(taskEntity.getStatus()).thenReturn(TaskStatus.RUNNING);
when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));

// Execute the method
dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

// Verify the result
assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
}

@Test
public void testExecute_whenExecuteTaskFailed_shouldReturnFailed() {
// Prepare mock ScheduleTaskEntity
ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
when(taskEntity.getParametersJson()).thenReturn("{}");
when(taskEntity.getResultJson()).thenReturn("[]");
when(taskEntity.getId()).thenReturn(1L);
when(taskEntity.getJobName()).thenReturn("testJobName");
when(taskEntity.getJobGroup()).thenReturn("testJobGroup");
when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);
when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));
when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenReturn(true);

// Prepare mock taskUnits
List<DlmTask> taskUnits = mock(List.class);
when(JsonUtils.fromJson(anyString(), any(TypeReference.class))).thenReturn(taskUnits);
when(taskUnits.size()).thenReturn(1);
when(taskUnits.get(0)).thenReturn(mock(DlmTask.class));
when(dataArchiveDeleteJob.executeTask(1L, taskUnits)).thenReturn(false);

// Execute the method
dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

// Verify the result
assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
}
}