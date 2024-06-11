import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.AbstractDlmJob;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {

@Mock
private ScheduleTaskRepository scheduleTaskRepository;

@InjectMocks
private DataArchiveRollbackJob dataArchiveRollbackJob;

private ScheduleTaskEntity scheduleTaskEntity;

private DataArchiveRollbackParameters dataArchiveRollbackParameters;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);
initObjects();
}

private void initObjects() {
scheduleTaskEntity = new ScheduleTaskEntity();
scheduleTaskEntity.setId(1L);
scheduleTaskEntity.setJobName("test");
scheduleTaskEntity.setJobGroup("test");
scheduleTaskEntity.setParametersJson("{}");
scheduleTaskEntity.setResultJson("[]");

dataArchiveRollbackParameters = new DataArchiveRollbackParameters();
dataArchiveRollbackParameters.setDataArchiveTaskId(1L);
dataArchiveRollbackParameters.setParametersJson("{}");
}

@Test
public void testExecute_WithValidParams_ShouldCompleteWithoutException() {
List<DlmTask> taskUnits = new ArrayList<>();
taskUnits.add(new DlmTask());

scheduleTaskEntity.setParametersJson(JsonUtils.toJson(dataArchiveRollbackParameters));
scheduleTaskEntity.setResultJson(JsonUtils.toJson(taskUnits));

when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));

dataArchiveRollbackJob.execute(null);

verify(scheduleTaskRepository).updateStatusById(1L, TaskStatus.DONE);
}

@Test
public void testExecute_WithInvalidDataArchiveTaskId_ShouldLogWarningAndUpdateTaskStatusToFailed() {
scheduleTaskEntity.setParametersJson("{}");
scheduleTaskEntity.setResultJson("[]");

when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));

dataArchiveRollbackJob.execute(null);

verify(scheduleTaskRepository).updateStatusById(1L, TaskStatus.FAILED);
}

@Test
public void testExecute_WithEmptyTaskUnitsList_ShouldCompleteWithoutException() {
scheduleTaskEntity.setParametersJson(JsonUtils.toJson(dataArchiveRollbackParameters));
scheduleTaskEntity.setResultJson("[]");

when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));

dataArchiveRollbackJob.execute(null);

verify(scheduleTaskRepository).updateStatusById(1L, TaskStatus.FAILED);
}

@Test
public void testExecute_WithNullScheduleTaskEntity_ShouldLogErrorAndCompleteWithoutException() {
dataArchiveRollbackJob.execute(null);
}

@Test
public void testExecute_WithNullDataArchiveTaskId_ShouldLogWarningAndUpdateTaskStatusToFailed() {
dataArchiveRollbackParameters.setDataArchiveTaskId(null);
scheduleTaskEntity.setParametersJson(JsonUtils.toJson(dataArchiveRollbackParameters));
scheduleTaskEntity.setResultJson("[]");

when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));

dataArchiveRollbackJob.execute(null);

verify(scheduleTaskRepository).updateStatusById(1L, TaskStatus.FAILED);
}
}