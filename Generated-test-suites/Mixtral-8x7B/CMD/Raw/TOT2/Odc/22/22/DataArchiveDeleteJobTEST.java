import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class DataArchiveDeleteJobTest {

@Mock
private ScheduleTaskRepository scheduleTaskRepository;

private DataArchiveDeleteJob dataArchiveDeleteJob;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);
dataArchiveDeleteJob = spy(new DataArchiveDeleteJob() {
@Override
public ScheduleTaskRepository getScheduleTaskRepository() {
return scheduleTaskRepository;
}
});
}

@Test
public void testExecute() {
// Prepare test data
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
String parametersJson = "{}";
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("123");
taskEntity.setParametersJson(parametersJson);

ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId("456");
dataArchiveTask.setStatus(TaskStatus.DONE);

List<DlmTask> taskUnits = List.of(new DlmTask());
String resultJson = JsonUtils.toJson(taskUnits);
dataArchiveTask.setResultJson(resultJson);

doReturn(Optional.of(dataArchiveTask)).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());

// Execute job
dataArchiveDeleteJob.execute(null);

// Assert state after execution
// ...
}

// Add more test cases for each public method and edge cases

}