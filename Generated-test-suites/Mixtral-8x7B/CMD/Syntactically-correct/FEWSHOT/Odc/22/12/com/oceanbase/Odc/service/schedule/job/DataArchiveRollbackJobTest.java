import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class DataArchiveRollbackJobTest {

@Mock
private AbstractDlmJob abstractDlmJob;

@Mock
private ScheduleTaskEntity scheduleTaskEntity;

@Mock
private ScheduleTaskEntity dataArchiveTask;

@Mock
private DataArchiveRollbackParameters rollbackParameters;

@Mock
private DlmJobIdUtil dlmJobIdUtil;

@Mock
private JsonUtils jsonUtils;

@Mock
private scheduleTaskRepository scheduleTaskRepository;

private DataArchiveRollbackJob dataArchiveRollbackJob;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);
dataArchiveRollbackJob = new DataArchiveRollbackJob(abstractDlmJob, dlmJobIdUtil, jsonUtils, scheduleTaskRepository);
}

@Test
public void testExecute() {
// setup mock data
String parametersJson = "{\"dataArchiveTaskId\":123}";
dataArchiveRollbackJob = spy(dataArchiveRollbackJob);
doReturn(rollbackParameters).when(jsonUtils).fromJson(parametersJson, DataArchiveRollbackParameters.class);
doReturn(Optional.of(dataArchiveTask)).when(scheduleTaskRepository).findById(123L);
List<DlmTask> taskUnits = new ArrayList<>();
DlmTask dlmTask = new DlmTask();
dlmTask.setSourceDatabaseId(1L);
taskUnits.add(dlmTask);
doReturn(taskUnits).when(jsonUtils).fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {});

// call the method under test
dataArchiveRollbackJob.execute(null);

// verify the results
verify(scheduleTaskRepository).updateStatusById(123L, TaskStatus.FAILED);
verify(dataArchiveRollbackJob).executeTask(123L, taskUnits);
}

@Test
public void testExecuteTask() {
// setup mock data
List<DlmTask> taskUnits = new ArrayList<>();
DlmTask dlmTask = new DlmTask();
dlmTask.setId(1L);
taskUnits.add(dlmTask);

// call the method under test
dataArchiveRollbackJob.executeTask(123L, taskUnits);

// verify the results
assertEquals(1, dlmJobIdUtil.generateHistoryJobId(DataArchiveRollbackJob.class.getSimpleName(), "group", 123L, 1));
assertEquals(1L, dlmTask.getId());
assertEquals(JobType.ROLLBACK, dlmTask.getJobType());
}

@Test
public void testGetTaskStatus() {
// setup mock data
List<DlmTask> taskUnits = new ArrayList<>();
DlmTask dlmTask = new DlmTask();
dlmTask.setStatus(TaskStatus.PREPARING);
taskUnits.add(dlmTask);

// call the method under test
TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

// verify the results
assertEquals(TaskStatus.PREPARING, taskStatus);

// setup mock data
List<DlmTask> taskUnits2 = new ArrayList<>();
DlmTask dlmTask2 = new DlmTask();
dlmTask2.setStatus(TaskStatus.DONE);
taskUnits2.add(dlmTask2);

// call the method under test
TaskStatus taskStatus2 = dataArchiveRollbackJob.getTaskStatus(taskUnits2);

// verify the results
assertEquals(TaskStatus.DONE, taskStatus2);
}
}