import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.test.util.ReflectionTestUtils;

@Slf4j
public class DataArchiveDeleteJobTest {

 @Autowired
 private ScheduleTaskRepository scheduleTaskRepository;

 private DataArchiveDeleteJob dataArchiveDeleteJob;

 @Before
 public void setup() {
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 ReflectionTestUtils.setField(dataArchiveDeleteJob, "scheduleTaskRepository", scheduleTaskRepository);
 }

 @Test
 public void testExecute() throws Exception {
 // Setup mocks
 JobExecutionContext context = mock(JobExecutionContext.class);
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
 taskEntity.setResultJson("[]");
 taskEntity.setStatus(TaskStatus.DONE);
 when(context.getResult()).thenReturn(taskEntity);

 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(2L);

 Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(new ScheduleTaskEntity());
 when(scheduleTaskRepository.findById(anyString())).thenReturn(dataArchiveTaskOption);
 when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class))).thenReturn(true);

 List<DlmTask> taskUnits = JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {});
 when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(new ScheduleTaskEntity()));
 when(scheduleTaskRepository.findByTaskIdAndJobType(anyLong(), any(JobType.class), any(Pageable.class)))
 .thenReturn(new PageImpl<>(taskUnits));

 // Call method under test
 dataArchiveDeleteJob.execute(context);

 // Assertions
 }

 @Test
 public void testExecute_dataArchiveTaskNotFound() throws Exception {
 // Setup mocks
 JobExecutionContext context = mock(JobExecutionContext.class);
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
 taskEntity.setResultJson("[]");
 taskEntity.setStatus(TaskStatus.DONE);
 when(context.getResult()).thenReturn(taskEntity);

 Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.ofNullable(null);
 when(scheduleTaskRepository.findById(anyString())).thenReturn(dataArchiveTaskOption);
 when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class))).thenReturn(true);

 // Call method under test
 dataArchiveDeleteJob.execute(context);

 // Assertions
 }

 @Test
 public void testExecute_dataArchiveTaskNotFinished() throws Exception {
 // Setup mocks
 JobExecutionContext context = mock(JobExecutionContext.class);
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
 taskEntity.setResultJson("[]");
 taskEntity.setStatus(TaskStatus.DONE);
 when(context.getResult()).thenReturn(taskEntity);

 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(2L);
 dataArchiveTask.setStatus(TaskStatus.RUNNING);

 Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(dataArchiveTask);
 when(scheduleTaskRepository.findById(anyString())).thenReturn(dataArchiveTaskOption);
 when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class))).thenReturn(true);

 // Call method under test
 dataArchiveDeleteJob.execute(context);

 // Assertions
 }

 @Test
 public void testExecute_prepareTasksForClear() throws Exception {
 // Setup mocks
 JobExecutionContext context = mock(JobExecutionContext.class);
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
 taskEntity.setResultJson("[]");
 taskEntity.setStatus(TaskStatus.DONE);
 when(context.getResult()).thenReturn(taskEntity);

 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(2L);
 dataArchiveTask.setStatus(TaskStatus.DONE);

 Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(dataArchiveTask);
 when(scheduleTaskRepository.findById(anyString())).thenReturn(dataArchiveTaskOption);

 List<DlmTask> taskUnits = JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {});
 when(scheduleTaskRepository.findByTaskIdAndJobType(anyLong(), any(JobType.class), any(Pageable.class)))
 .thenReturn(new PageImpl<>(taskUnits));

 when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class))).thenReturn(true);

 // Call method under test
 dataArchiveDeleteJob.execute(context);

 // Assertions
 }

 @Test
 public void testExecute_executeTask() throws Exception {
 // Setup mocks
 JobExecutionContext context = mock(JobExecutionContext.class);
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
 taskEntity.setResultJson("[]");
 taskEntity.setStatus(TaskStatus.DONE);
 when(context.getResult()).thenReturn(taskEntity);

 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(2L);
 dataArchiveTask.setStatus(TaskStatus.DONE);

 Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(dataArchiveTask);
 when(scheduleTaskRepository.findById(anyString())).thenReturn(dataArchiveTaskOption);

 List<DlmTask> taskUnits = JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {});
 when(scheduleTaskRepository.findByTaskIdAndJobType(anyLong(), any(JobType.class), any(Pageable.class)))
 .thenReturn(new PageImpl<>(taskUnits));

 when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class))).thenReturn(true);

 // Call method under test
 dataArchiveDeleteJob.execute(context);

 // Assertions
 }

 @Test
 public void testExecute_getTaskStatus() throws Exception {
 // Setup mocks
 JobExecutionContext context = mock(JobExecutionContext.class);
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
 taskEntity.setResultJson("[]");
 taskEntity.setStatus(TaskStatus.DONE);
 when(context.getResult()).thenReturn(taskEntity);

 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(2L);
 dataArchiveTask.setStatus(TaskStatus.DONE);

 Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(dataArchiveTask);
 when(scheduleTaskRepository.findById(anyString())).thenReturn(dataArchiveTaskOption);

 List<DlmTask> taskUnits = JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {});
 when(scheduleTaskRepository.findByTaskIdAndJobType(anyLong(), any(JobType.class), any(Pageable.class)))
 .thenReturn(new PageImpl<>(taskUnits));

 when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class))).thenReturn(true);

 // Call method under test
 dataArchiveDeleteJob.execute(context);

 // Assertions
 }

 @Test
 public void testGetTaskStatus() throws Exception {
 // Setup
 List<DlmTask> taskUnits = JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {});

 // Call method under test
 TaskStatus result = DataArchiveDeleteJob.getTaskStatus(taskUnits);

 // Assertions
 }
}