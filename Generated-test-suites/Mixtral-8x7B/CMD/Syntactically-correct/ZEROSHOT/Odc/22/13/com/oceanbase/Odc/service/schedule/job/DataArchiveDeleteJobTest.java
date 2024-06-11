import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.common.json.JsonUtils;
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
import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

@Mock
private ScheduleTaskRepository scheduleTaskRepository;

@Mock
private AbstractDlmJob abstractDlmJob;

@InjectMocks
private DataArchiveDeleteJob dataArchiveDeleteJob;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);
}

@Test
public void testExecute() {
// Arrange
JobExecutionContext context = mock(JobExecutionContext.class);
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("12345");
String parametersJson = JsonUtils.toJson(dataArchiveClearParameters);
taskEntity.setParametersJson(parametersJson);

when(context.getResult()).thenReturn(taskEntity);

Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(new ScheduleTaskEntity());
when(scheduleTaskRepository.findById(any())).thenReturn(dataArchiveTaskOption);

List<DlmTask> taskUnits = JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {});
when(scheduleTaskRepository.updateStatusById(any(), any())).thenReturn(null);

// Act
dataArchiveDeleteJob.execute(context);

// Assert
verify(scheduleTaskRepository, times(1)).findById(any());
verify(scheduleTaskRepository, times(1)).updateStatusById(any(), any());
}

@Test
public void testGetTaskStatus() {
// Arrange
List<DlmTask> taskUnits = JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {});

// Act
TaskStatus result = dataArchiveDeleteJob.getTaskStatus(taskUnits);

// Assert
assertEquals(TaskStatus.PREPARING, result);
}
}