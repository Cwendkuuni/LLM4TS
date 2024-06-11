import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {

@Mock
private AbstractDlmJob.ScheduleTaskRepository scheduleTaskRepository;

private DataArchiveRollbackJob dataArchiveRollbackJob;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);
dataArchiveRollbackJob = new DataArchiveRollbackJob();
dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
}

@Test
public void testExecute() {
// Prepare test data
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setJobName("testJobName");
taskEntity.setJobGroup("testJobGroup");
taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
taskEntity.setResultJson("[]");

DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
rollbackParameters.setDataArchiveTaskId(2L);

List<DlmTask> taskUnits = JsonUtils.fromJson("[[{\"id\": 3, \"sourceDatabaseId\": 4, \"targetDatabaseId\": 5}]]",
new TypeReference<List<DlmTask>>() {});

when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));

// Call the method under test
dataArchiveRollbackJob.execute(null);

// Verify the results
verify(scheduleTaskRepository, times(1)).findById(anyLong());
verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
verifyNoMoreInteractions(scheduleTaskRepository);

assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
assertNotNull(taskEntity.getResultJson());
assertEquals(1, taskUnits.size());
assertEquals(JobType.ROLLBACK, taskUnits.get(0).getJobType());
}
}