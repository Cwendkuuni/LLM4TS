import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.AbstractDlmJob;
import com.oceanbase.odc.utils.JsonUtils;
import com.oceanbase.tools.migrator.common.enums.JobType;

@SpringBootTest
@ActiveProfiles("test")
public class DataArchiveRollbackJobTest {

 @MockBean
 private ScheduleTaskRepository scheduleTaskRepository;

 @Autowired
 private AbstractDlmJob dataArchiveRollbackJob;

 private JobExecutionContext jobExecutionContext;

 @Before
 public void setup() {
 jobExecutionContext = mock(JobExecutionContext.class);
 }

 @Test
 public void testExecute_whenDataArchiveTaskNotFound_thenShouldUpdateTaskStatusToFailed()
 throws JobExecutionException {
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters(1L);
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 dataArchiveRollbackJob.execute(jobExecutionContext);

 verify(scheduleTaskRepository, times(1)).updateStatusById(eq(1L), eq(TaskStatus.FAILED));
 }

 @Test
 public void testExecute_whenDataArchiveTaskFound_thenShouldPrepareTasksForRollbackAndUpdateTaskStatus()
 throws JobExecutionException {
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters(1L);
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(2L);
 dataArchiveTask.setResultJson("[{\"sourceDatabaseId\": 1,\"targetDatabaseId\": 2}]");
 DlmTask dlmTask = new DlmTask();
 dlmTask.setSourceDatabaseId(1L);
 dlmTask.setTargetDatabaseId(2L);
 List<DlmTask> taskUnits = new ArrayList<>();
 taskUnits.add(dlmTask);

 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));
 when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class)))
 .thenReturn(true);
 when(JsonUtils.fromJson(dataArchiveTask.getResultJson(),
 new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);

 dataArchiveRollbackJob.execute(jobExecutionContext);

 verify(scheduleTaskRepository, times(1)).updateStatusById(eq(1L), any(TaskStatus.class));
 }
}