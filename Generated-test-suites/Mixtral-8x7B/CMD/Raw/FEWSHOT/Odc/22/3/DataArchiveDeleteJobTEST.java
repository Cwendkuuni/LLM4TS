import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerBuilder;
import org.quartz.impl.matchers.GroupMatcher;

@Slf4j
public class DataArchiveDeleteJobTest {

private DataArchiveDeleteJob dataArchiveDeleteJob;
private JobExecutionContext jobExecutionContext;
private ScheduleTaskEntity taskEntity;
private ScheduleTaskEntity dataArchiveTask;
private DataArchiveClearParameters dataArchiveClearParameters;
private List<DlmTask> taskUnits;

@Before
public void setUp() {
dataArchiveDeleteJob = new DataArchiveDeleteJob();

// Create mock objects
jobExecutionContext = mock(JobExecutionContext.class);
taskEntity = mock(ScheduleTaskEntity.class);
dataArchiveTask = mock(ScheduleTaskEntity.class);
dataArchiveClearParameters = mock(DataArchiveClearParameters.class);
taskUnits = Arrays.asList(mock(DlmTask.class), mock(DlmTask.class));

// Set up mock objects
when(jobExecutionContext.getResult()).thenReturn(taskEntity);
when(taskEntity.getParametersJson()).thenReturn("{}");
when(taskEntity.getResultJson()).thenReturn("[]");
when(taskEntity.getId()).thenReturn(1L);
when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn(2L);
when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(dataArchiveTask));
when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
when(dataArchiveTask.getResultJson()).thenReturn(JsonUtils.toJson(taskUnits));
when(dataArchiveTask.getId()).thenReturn(2L);
when(jobExecutionContext.getMergedJobDataMap()).thenReturn(new org.quartz.JobDataMap());

JsonUtils.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
}

@Test
public void testExecute_TaskNotFound() {
// Given
when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.empty());

// When
dataArchiveDeleteJob.execute(jobExecutionContext);

// Then
assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
}

@Test
public void testExecute_TaskNotDone() {
// Given
when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.PREPARING);

// When
dataArchiveDeleteJob.execute(jobExecutionContext);

// Then
assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
}

@Test
public void testExecute_Success() throws Exception {
// Given
when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);

// When
dataArchiveDeleteJob.execute(jobExecutionContext);

// Then
assertEquals(TaskStatus.PREPARING, taskEntity.getStatus());
}

private void executeTask(Long id, List<DlmTask> taskUnits) {
// Implementation not needed for the purpose of this test
}

private TaskStatus getTaskStatus(List<DlmTask> taskUnits) {
// Implementation not needed for the purpose of this test
return null;
}

private JobExecutionContext createJobExecutionContext(ScheduleTaskEntity taskEntity) {
Trigger trigger = TriggerBuilder.newTrigger()
.withIdentity("triggerName", "triggerGroup")
.startNow()
.build();

return new JobExecutionContext(dataArchiveDeleteJob, trigger, null, null, null, null, null, null,
taskEntity, new org.quartz.JobDataMap());
}

private static class ScheduleTaskRepository {
public Optional<ScheduleTaskEntity> findById(Long id) {
// Implementation not needed for the purpose of this test
return Optional.empty();
}

public void updateStatusById(Long id, TaskStatus status) {
// Implementation not needed for the purpose of this test
}
}

private static class JsonUtils {
public static <T> T fromJson(String json, Class<T> clazz) {
// Implementation not needed for the purpose of this test
return null;
}

public static String toJson(Object object) {
// Implementation not needed for the purpose of this test
return "";
}

public static void registerModule(Object module) {
// Implementation not needed for the purpose of this test
}
}

private static class DlmJobIdUtil {
public static String generateHistoryJobId(String jobName, String jobGroup, Long taskEntityId, int taskUnitsSize) {
// Implementation not needed for the purpose of this test
return "";
}
}
}