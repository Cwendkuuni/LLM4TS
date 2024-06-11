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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class DataArchiveDeleteJobTest {

@Mock
private ScheduleTaskRepository scheduleTaskRepository;

private DataArchiveDeleteJob dataArchiveDeleteJob;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);
dataArchiveDeleteJob = spy(new DataArchiveDeleteJob() {
@Override
public void executeTask(long taskId, List<DlmTask> taskUnits) {
// do nothing in the test
}
});
doReturn(scheduleTaskRepository).when(dataArchiveDeleteJob).getScheduleTaskRepository();
}

@Test
public void testExecute_dataArchiveTaskNotFound() {
// arrange
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId(1L);
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

doReturn(Optional.of(new ScheduleTaskEntity()))
.when(scheduleTaskRepository)
.findById(dataArchiveClearParameters.getDataArchiveTaskId());

// act
dataArchiveDeleteJob.execute(null);

// assert
verify(scheduleTaskRepository).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
}

@Test
public void testExecute_dataArchiveTaskNotFinished() {
// arrange
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId(1L);
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(1L);
dataArchiveTask.setStatus(TaskStatus.RUNNING);
doReturn(Optional.of(dataArchiveTask))
.when(scheduleTaskRepository)
.findById(dataArchiveClearParameters.getDataArchiveTaskId());

// act
dataArchiveDeleteJob.execute(null);

// assert
verify(scheduleTaskRepository).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
}

@Test
public void testExecute_prepareTasksForClear() {
// arrange
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId(1L);
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(1L);
dataArchiveTask.setStatus(TaskStatus.DONE);
List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());
doReturn(Optional.of(dataArchiveTask))
.when(scheduleTaskRepository)
.findById(dataArchiveClearParameters.getDataArchiveTaskId());
doReturn(taskUnits)
.when(dataArchiveDeleteJob)
.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {});

// act
dataArchiveDeleteJob.execute(null);

// assert
verify(dataArchiveDeleteJob).executeTask(anyLong(), any());
}

private class TestDataArchiveDeleteJob extends DataArchiveDeleteJob {
@Override
public void executeTask(long taskId, List<DlmTask> taskUnits) {
// do nothing in the test
}
}

private void fromJson(String json, TypeReference<List<DlmTask>> type) {
// implementation not needed for the test
}
}