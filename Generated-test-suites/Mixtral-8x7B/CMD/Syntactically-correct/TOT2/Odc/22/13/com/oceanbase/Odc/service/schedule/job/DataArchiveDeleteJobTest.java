import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.AbstractDlmJob;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

@Mock
private ScheduleTaskRepository scheduleTaskRepository;

@Mock
private JobExecutionContext jobExecutionContext;

@InjectMocks
private DataArchiveDeleteJob dataArchiveDeleteJob;

private static final Logger LOG = LoggerFactory.getLogger(DataArchiveDeleteJobTest.class);

private ScheduleTaskEntity scheduleTaskEntity;

private DataArchiveClearParameters dataArchiveClearParameters;

@Before
public void setup() {
dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("dataArchiveTaskId");

scheduleTaskEntity = new ScheduleTaskEntity();
scheduleTaskEntity.setId(1L);
scheduleTaskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
scheduleTaskEntity.setStatus(TaskStatus.DONE);
}

@Test
public void testExecute_whenDataArchiveTaskNotFound_thenUpdateTaskStatusToFailed() throws JobExecutionException {
// Given
scheduleTaskEntity.setStatus(TaskStatus.DONE);
doReturn(Optional.of(scheduleTaskEntity)).when(scheduleTaskRepository).findById(anyString());
doReturn(Optional.empty()).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());

// When
dataArchiveDeleteJob.execute(jobExecutionContext);

// Then
verify(scheduleTaskRepository, times(1)).findById(dataArchiveClearParameters.getDataArchiveTaskId());
verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
}

@Test
public void testExecute_whenDataArchiveTaskNotFinished_thenUpdateTaskStatusToFailed() throws JobExecutionException {
// Given
scheduleTaskEntity.setStatus(TaskStatus.RUNNING);
doReturn(Optional.of(scheduleTaskEntity)).when(scheduleTaskRepository).findById(anyString());

// When
dataArchiveDeleteJob.execute(jobExecutionContext);

// Then
verify(scheduleTaskRepository, times(1)).findById(scheduleTaskEntity.getId());
verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
}

@Test
public void testExecute_whenTaskUnitsPrepareSuccess_thenUpdateTaskStatus() throws JobExecutionException {
// Given
List<DlmTask> taskUnits = new ArrayList<>();
taskUnits.add(new DlmTask());
scheduleTaskEntity.setStatus(TaskStatus.DONE);
scheduleTaskEntity.setResultJson(JsonUtils.toJson(taskUnits));
dataArchiveClearParameters.setDataArchiveTaskId("correctDataArchiveTaskId");
doReturn(Optional.of(scheduleTaskEntity)).when(scheduleTaskRepository).findById(anyString());

// When
dataArchiveDeleteJob.execute(jobExecutionContext);

// Then
verify(scheduleTaskRepository, times(1)).findById(dataArchiveClearParameters.getDataArchiveTaskId());
verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.DONE);
}

private void mockFindById(TaskStatus status) {
doReturn(Optional.of(createScheduleTaskEntity(status)))
.when(scheduleTaskRepository)
.findById(dataArchiveClearParameters.getDataArchiveTaskId());
}

private ScheduleTaskEntity createScheduleTaskEntity(TaskStatus status) {
ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
scheduleTaskEntity.setId(1L);
scheduleTaskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
scheduleTaskEntity.setStatus(status);
return scheduleTaskEntity;
}
}