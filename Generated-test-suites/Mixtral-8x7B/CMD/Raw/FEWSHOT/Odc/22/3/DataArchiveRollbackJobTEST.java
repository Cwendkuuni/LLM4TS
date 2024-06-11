import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.JobExecutionContextImpl;

@Slf4j
public class DataArchiveRollbackJobTest {

 @Before
 public void setup() {
 log.info("Setting up the test environment...");
 JsonUtils = spy(JsonUtils.class);
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 }

 @Test
 public void testExecute_whenTaskEntityAndRollbackParametersAreValid_thenExecuteTaskAndUpdateStatus()
 throws JobExecutionException {
 // Arrange
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");

 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(2L);

 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));

 List<DlmTask> taskUnits = new ArrayList<>();
 DlmTask task1 = new DlmTask();
 task1.setSourceDatabaseId(3L);
 DlmTask task2 = new DlmTask();
 task2.setSourceDatabaseId(4L);
 taskUnits.add(task1);
 taskUnits.add(task2);

 String taskUnitsJson = JsonUtils.toJson(taskUnits);
 doReturn(taskUnitsJson).when(JsonUtils).fromJson(anyString(), any(TypeReference.class));

 // Act
 DataArchiveRollbackJob job = new DataArchiveRollbackJob();
 JobExecutionContext context = new JobExecutionContextImpl();
 context.setResult(taskEntity);
 job.execute(context);

 // Assert
 verify(scheduleTaskRepository, times(1)).findById(rollbackParameters.getDataArchiveTaskId());
 verify(JsonUtils, times(1)).fromJson(anyString(), any(TypeReference.class));
 verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
 }

 @Test
 public void testExecute_whenDataArchiveTaskIsNull_thenLogWarningAndUpdateStatus()
 throws JobExecutionException {
 // Arrange
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");

 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(2L);

 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

 // Act
 DataArchiveRollbackJob job = new DataArchiveRollbackJob();
 JobExecutionContext context = new JobExecutionContextImpl();
 context.setResult(taskEntity);
 job.execute(context);

 // Assert
 verify(scheduleTaskRepository, times(1)).findById(rollbackParameters.getDataArchiveTaskId());
 verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }

 private ScheduleTaskRepository scheduleTaskRepository;

}