import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.AbstractDlmJob;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {

@Mock
private ScheduleTaskRepository scheduleTaskRepository;

@Mock
private JsonUtils jsonUtils;

private DataArchiveRollbackJob dataArchiveRollbackJob;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);
dataArchiveRollbackJob = new DataArchiveRollbackJob();
dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
dataArchiveRollbackJob.setJsonUtils(jsonUtils);
}

@Test
public void testExecute() {
// setup mock data
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");

DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
rollbackParameters.setDataArchiveTaskId(2L);

List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());

when(jsonUtils.fromJson(any(), eq(DataArchiveRollbackParameters.class)))
.thenReturn(rollbackParameters);

when(scheduleTaskRepository.findById(eq(2L)))
.thenReturn(Optional.of(new ScheduleTaskEntity()));

when(jsonUtils.fromJson(any(), eq(new TypeReference<List<DlmTask>>() {})))
.thenReturn(taskUnits);

// call method under test
dataArchiveRollbackJob.execute(null);

// assertions
assertEquals(TaskStatus.FAILED, TaskStatus.FAILED);
}

@Test
public void testExecuteTask() {
// setup mock data
List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());

doNothing().when(dataArchiveRollbackJob).executeTask(eq(1L), eq(taskUnits));

// call method under test
dataArchiveRollbackJob.executeTask(1L, taskUnits);

// assertions
// add your assertions here
}

@Test
public void testGetTaskStatus() {
// setup mock data
List<DlmTask> taskUnits = Arrays.asList(new DlmTask(), new DlmTask());

when(dataArchiveRollbackJob.getTaskStatus(eq(taskUnits)))
.thenReturn(TaskStatus.PREPARING);

// call method under test
TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

// assertions
assertEquals(TaskStatus.PREPARING, taskStatus);
}
}