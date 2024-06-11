import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Result;
import org.quartz.spi.JobExecutionInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

@Mock private ScheduleTaskRepository scheduleTaskRepository;

private static final Logger log = LoggerFactory.getLogger(DataArchiveRollbackJobTest.class);

private JobExecutionContext createJobExecutionContext() {
JobExecutionContext context = new JobExecutionContext();
context.setResult(new ScheduleTaskEntity());
return context;
}

@Test
public void testExecute() throws Exception {
// Arrange
JobExecutionContext context = createJobExecutionContext();
DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
rollbackParameters.setDataArchiveTaskId(1L);
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
context.setResult(taskEntity);

ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(2L);
dataArchiveTask.setResultJson("[{\"id\":1,\"sourceDatabaseId\":1,\"targetDatabaseId\":2,\"jobType\":\"ROLLBACK\"}]");
when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));

DataArchiveRollbackJob job = new DataArchiveRollbackJob();
job.setScheduleTaskRepository(scheduleTaskRepository);

// Act
job.execute(context);

// Assert
verify(scheduleTaskRepository, times(1)).findById(rollbackParameters.getDataArchiveTaskId());
verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
}

@Test
public void testExecuteEdgeCase() throws Exception {
// Arrange
JobExecutionContext context = createJobExecutionContext();
DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
rollbackParameters.setDataArchiveTaskId(null);
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
context.setResult(taskEntity);

DataArchiveRollbackJob job = new DataArchiveRollbackJob();

// Act
job.execute(context);

// Assert
verify(scheduleTaskRepository, times(0)).findById(rollbackParameters.getDataArchiveTaskId());
verify(scheduleTaskRepository, times(0)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
}

@Test
public void testExecuteException() throws Exception {
// Arrange
JobExecutionContext context = createJobExecutionContext();
DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
rollbackParameters.setDataArchiveTaskId(1L);
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
context.setResult(taskEntity);

ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenThrow(new RuntimeException());

DataArchiveRollbackJob job = new DataArchiveRollbackJob();
job.setScheduleTaskRepository(scheduleTaskRepository);

// Assert
assertThrows(RuntimeException.class, () -> job.execute(context));
verify(scheduleTaskRepository, times(0)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
}

}