import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.AbstractDlmJob;

@SuppressWarnings("unchecked")
public class DataArchiveRollbackJobTest {

private static final Logger logger = LoggerFactory.getLogger(DataArchiveRollbackJobTest.class);

private DataArchiveRollbackJob dataArchiveRollbackJob;

private ScheduleTaskRepository scheduleTaskRepository;

private JobExecutionContext jobExecutionContext;

private ScheduleTaskEntity scheduleTaskEntity;

private DataArchiveRollbackParameters rollbackParameters;

private List<DlmTask> taskUnits;

@Before
public void setUp() {
dataArchiveRollbackJob = new DataArchiveRollbackJob();

scheduleTaskRepository = mock(ScheduleTaskRepository.class);
jobExecutionContext = mock(JobExecutionContext.class);

scheduleTaskEntity = new ScheduleTaskEntity();
scheduleTaskEntity.setId(1L);
scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");

rollbackParameters = new DataArchiveRollbackParameters();
rollbackParameters.setDataArchiveTaskId(2L);

taskUnits = new ArrayList<>();
DlmTask task1 = new DlmTask();
task1.setId(1L);
task1.setSourceDatabaseId(3L);
task1.setTargetDatabaseId(4L);
task1.setStatus(TaskStatus.PREPARING);

DlmTask task2 = new DlmTask();
task2.setId(2L);
task2.setSourceDatabaseId(4L);
task2.setTargetDatabaseId(3L);
task2.setStatus(TaskStatus.DONE);

taskUnits.add(task1);
taskUnits.add(task2);
}

@Test
public void testExecute() throws JobExecutionException {
// Mock the behavior of scheduleTaskRepository.findById
when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
.thenReturn(Optional.of(scheduleTaskEntity));

// Call the method to test
dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
dataArchiveRollbackJob.execute(jobExecutionContext);

// Verify the expected behavior
verify(scheduleTaskRepository, times(1)).findById(rollbackParameters.getDataArchiveTaskId());
verify(scheduleTaskRepository, times(1))
.updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
}

@Test
public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
// Mock the behavior of scheduleTaskRepository.findById
when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
.thenReturn(Optional.empty());

// Call the method to test
dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
dataArchiveRollbackJob.execute(jobExecutionContext);

// Verify the expected behavior
verify(scheduleTaskRepository, times(1)).findById(rollbackParameters.getDataArchiveTaskId());
verify(scheduleTaskRepository, times(1))
.updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
}

@Test
public void testExecute_ExecuteTaskSuccess() throws JobExecutionException {
// Mock the behavior of scheduleTaskRepository.findById
when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
.thenReturn(Optional.of(scheduleTaskEntity));

// Call the method to test
dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
dataArchiveRollbackJob.execute(jobExecutionContext);

// Verify the expected behavior
verify(scheduleTaskRepository, times(1)).findById(rollbackParameters.getDataArchiveTaskId());
verify(scheduleTaskRepository, times(1))
.updateStatusById(scheduleTaskEntity.getId(), TaskStatus.DONE);
}

@Test
public void testSetScheduleTaskRepository() {
dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
assertEquals(scheduleTaskRepository, dataArchiveRollbackJob.getScheduleTaskRepository());
}
}