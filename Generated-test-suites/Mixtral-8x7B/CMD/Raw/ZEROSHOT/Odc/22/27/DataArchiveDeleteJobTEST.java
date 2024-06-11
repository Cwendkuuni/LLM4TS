import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

@SuppressWarnings("unchecked")
public class DataArchiveDeleteJobTest {

private static final Logger logger = LoggerFactory.getLogger(DataArchiveDeleteJobTest.class);

private DataArchiveDeleteJob dataArchiveDeleteJob;

private JobExecutionContext jobExecutionContext;

private ScheduleTaskEntity scheduleTaskEntity;

private ScheduleTaskRepository scheduleTaskRepository;

@Before
public void setup() {
dataArchiveDeleteJob = new DataArchiveDeleteJob();

scheduleTaskRepository = mock(ScheduleTaskRepository.class);

ReflectionTestUtils.setField(dataArchiveDeleteJob, "scheduleTaskRepository", scheduleTaskRepository);

jobExecutionContext = mock(JobExecutionContext.class);

scheduleTaskEntity = new ScheduleTaskEntity();
scheduleTaskEntity.setId(1L);
scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
scheduleTaskEntity.setJobName("testJobName");
scheduleTaskEntity.setJobGroup("testJobGroup");
scheduleTaskEntity.setResultJson("[]");
scheduleTaskEntity.setStatus(TaskStatus.DONE);
}

@Test
public void testExecute_whenDataArchiveTaskNotFound_thenRollbackTaskFastFailed() throws JobExecutionException {
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId(2L);

Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.ofNullable(null);

when(scheduleTaskRepository.findById(2L)).thenReturn(dataArchiveTaskOption);

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.FAILED);
}

@Test
public void testExecute_whenDataArchiveTaskNotFinished_thenRollbackTaskFastFailed() throws JobExecutionException {
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId(2L);

ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(2L);
dataArchiveTask.setStatus(TaskStatus.RUNNING);

Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(dataArchiveTask);

when(scheduleTaskRepository.findById(2L)).thenReturn(dataArchiveTaskOption);

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.FAILED);
}

@Test
public void testExecute_whenPrepareTasksForClearSuccess_thenUpdateTaskStatus() throws JobExecutionException {
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId(2L);

ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(2L);
dataArchiveTask.setStatus(TaskStatus.DONE);

List<DlmTask> taskUnits = JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {});

Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(dataArchiveTask);

when(scheduleTaskRepository.findById(2L)).thenReturn(dataArchiveTaskOption);
when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.PREPARING)).thenReturn(1);

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.PREPARING);
}
}