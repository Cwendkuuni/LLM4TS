import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.QuartzJobBeanJobFactory;
import org.springframework.test.util.ReflectionTestUtils;

@SuppressWarnings("unchecked")
public class DataArchiveDeleteJobTest {

private DataArchiveDeleteJob dataArchiveDeleteJob;
private JobExecutionContext jobExecutionContext;
private ScheduleTaskEntity scheduleTaskEntity;
private ScheduleTaskEntity dataArchiveTask;
private DataArchiveClearParameters dataArchiveClearParameters;
private List<DlmTask> taskUnits;

@Before
public void setup() {
dataArchiveDeleteJob = new DataArchiveDeleteJob();

// Autowired dependencies cannot be used in tests, so we need to create mock objects
JobExecutionContext mockJobExecutionContext = mock(JobExecutionContext.class);
ReflectionTestUtils.setField(dataArchiveDeleteJob, "scheduleTaskRepository", mockScheduleTaskRepository());

dataArchiveDeleteJob.execute(mockJobExecutionContext);

scheduleTaskEntity = new ScheduleTaskEntity();
dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveTask = new ScheduleTaskEntity();
taskUnits = new ArrayList<>();
}

@Test
public void testExecute_DataArchiveTaskNotFound_UpdatesTaskStatusToFailed() {
dataArchiveClearParameters.setDataArchiveTaskId(1L);
ReflectionTestUtils.setField(dataArchiveDeleteJob, "jsonUtils", mockJsonUtils());

when(mockScheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(mockScheduleTaskRepository).updateStatusById(1L, TaskStatus.FAILED);
}

@Test
public void testExecute_DataArchiveTaskNotDone_UpdatesTaskStatusToFailed() {
dataArchiveClearParameters.setDataArchiveTaskId(1L);
ReflectionTestUtils.setField(dataArchiveDeleteJob, "jsonUtils", mockJsonUtils());

dataArchiveTask.setId(1L);
dataArchiveTask.setStatus(TaskStatus.RUNNING);

when(mockScheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(mockScheduleTaskRepository).updateStatusById(1L, TaskStatus.FAILED);
}

@Test
public void testExecute_PrepareTasksForClear() {
dataArchiveClearParameters.setDataArchiveTaskId(1L);
ReflectionTestUtils.setField(dataArchiveDeleteJob, "jsonUtils", mockJsonUtils());

dataArchiveTask.setId(1L);
dataArchiveTask.setStatus(TaskStatus.DONE);
taskUnits.add(new DlmTask());

when(mockScheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));
when(mockJsonUtils.fromJson(anyString(), any(TypeReference.class))).thenReturn(taskUnits);

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(dataArchiveDeleteJob).executeTask(1L, taskUnits);
}

private ScheduleTaskRepository mockScheduleTaskRepository() {
return mock(ScheduleTaskRepository.class);
}

private JsonUtils mockJsonUtils() {
return mock(JsonUtils.class);
}
}

class ScheduleTaskRepository {

public Optional<ScheduleTaskEntity> findById(Long id) {
return Optional.empty();
}

public void updateStatusById(Long id, TaskStatus taskStatus) {
// Do nothing
}
}

class JsonUtils {

public <T> T fromJson(String json, TypeReference<T> typeReference) {
return null;
}
}

class QuartzJobBeanWithFactory extends QuartzJobBean {

public static QuartzJobBeanJobFactory jobFactory;

@Override
protected void executeInternal(JobExecutionContext jobExecutionContext) {
jobFactory.createJob(this.getClass(), jobDataMap, jobExecutionContext);
}
}