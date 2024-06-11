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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DataArchiveDeleteJobTest {

@Mock
private ScheduleTaskRepository scheduleTaskRepository;

@InjectMocks
private DataArchiveDeleteJob dataArchiveDeleteJob = spy(new DataArchiveDeleteJob());

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);
}

@Test
public void testExecute() {
// Prepare test data
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setStatus(TaskStatus.DONE);

DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId(2L);

String parametersJson = JsonUtils.toJson(dataArchiveClearParameters);
taskEntity.setParametersJson(parametersJson);

ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(2L);
dataArchiveTask.setStatus(TaskStatus.DONE);

List<DlmTask> taskUnits = JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {});

// Define mock behavior
doReturn(Optional.of(dataArchiveTask)).when(scheduleTaskRepository).findById(anyString());
doReturn(taskUnits).when(dataArchiveDeleteJob).executeTask(anyLong(), any());

// Execute test method
dataArchiveDeleteJob.execute(null);

// Verify expected behavior
verify(dataArchiveDeleteJob, times(1)).executeTask(anyLong(), any());
verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
}
}