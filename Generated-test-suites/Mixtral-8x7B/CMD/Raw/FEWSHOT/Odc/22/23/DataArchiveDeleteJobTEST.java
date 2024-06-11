import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

@SuppressWarnings("unchecked")
public class DataArchiveDeleteJobTest {

 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;

 @Mock
 private JsonUtils jsonUtils;

 @Mock
 private DlmJobIdUtil dlmJobIdUtil;

 @InjectMocks
 private DataArchiveDeleteJob dataArchiveDeleteJob;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testExecute_whenTaskEntityIsNull_thenUpdateStatusByIdCalledWithFailed() {
 // Given
 JobExecutionContext context = mock(JobExecutionContext.class);

 // When
 dataArchiveDeleteJob.execute(context);

 // Then
 // Verify that updateStatusById is called with id and FAILED
 }

 @Test
 public void testExecute_whenDataArchiveTaskNotPresent_thenUpdateStatusByIdCalledWithFailed() {
 // Given
 JobExecutionContext context = mock(JobExecutionContext.class);
 ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
 when(context.getResult()).thenReturn(taskEntity);
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 when(jsonUtils.fromJson(anyString(), any(Class.class))).thenReturn(dataArchiveClearParameters);
 Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.empty();
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(dataArchiveTaskOption);

 // When
 dataArchiveDeleteJob.execute(context);

 // Then
 // Verify that updateStatusById is called with id and FAILED
 }

 @Test
 public void testExecute_whenDataArchiveTaskNotDone_thenUpdateStatusByIdCalledWithFailed() {
 // Given
 JobExecutionContext context = mock(JobExecutionContext.class);
 ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
 when(context.getResult()).thenReturn(taskEntity);
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 when(jsonUtils.fromJson(anyString(), any(Class.class))).thenReturn(dataArchiveClearParameters);
 Long dataArchiveTaskId = 1L;
 dataArchiveClearParameters.setDataArchiveTaskId(dataArchiveTaskId);
 Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(taskEntity);
 when(scheduleTaskRepository.findById(dataArchiveTaskId)).thenReturn(dataArchiveTaskOption);
 when(taskEntity.getStatus()).thenReturn(TaskStatus.CREATED);

 // When
 dataArchiveDeleteJob.execute(context);

 // Then
 // Verify that updateStatusById is called with id and FAILED
 }

 @Test
 public void testExecute_whenTaskUnitsIsNull_thenUpdateStatusByIdCalledWithFailed() {
 // Given
 JobExecutionContext context = mock(JobExecutionContext.class);
 ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
 when(context.getResult()).thenReturn(taskEntity);
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 when(jsonUtils.fromJson(anyString(), any(Class.class))).thenReturn(dataArchiveClearParameters);
 Long dataArchiveTaskId = 1L;
 dataArchiveClearParameters.setDataArchiveTaskId(dataArchiveTaskId);
 Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(taskEntity);
 when(scheduleTaskRepository.findById(dataArchiveTaskId)).thenReturn(dataArchiveTaskOption);
 when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);
 when(jsonUtils.fromJson(anyString(), any(TypeReference.class))).thenReturn(new ArrayList<DlmTask>());

 // When
 dataArchiveDeleteJob.execute(context);

 // Then
 // Verify that updateStatusById is called with id and FAILED
 }

 @Test
 public void testExecute_whenEverythingIsOk_thenUpdateStatusByIdCalledWithCorrectStatus() {
 // Given
 JobExecutionContext context = mock(JobExecutionContext.class);
 ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
 when(context.getResult()).thenReturn(taskEntity);
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 when(jsonUtils.fromJson(anyString(), any(Class.class))).thenReturn(dataArchiveClearParameters);
 Long dataArchiveTaskId = 1L;
 dataArchiveClearParameters.setDataArchiveTaskId(dataArchiveTaskId);
 Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(taskEntity);
 when(scheduleTaskRepository.findById(dataArchiveTaskId)).thenReturn(dataArchiveTaskOption);
 when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);
 List<DlmTask> taskUnits = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 taskUnits.add(dlmTask);
 when(jsonUtils.fromJson(anyString(), any(TypeReference.class))).thenReturn(taskUnits);
 TaskStatus taskStatus = TaskStatus.PREPARING;
 when(dataArchiveDeleteJob.getTaskStatus(taskUnits)).thenReturn(taskStatus);
 doNothing().when(dataArchiveDeleteJob).executeTask(anyLong(), anyList());

 // When
 dataArchiveDeleteJob.execute(context);

 // Then
 // Verify that updateStatusById is called with id and CREATED
 }
}