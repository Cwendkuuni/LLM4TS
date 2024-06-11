import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DataArchiveDeleteJobTest {

@MockBean
private ScheduleTaskRepository scheduleTaskRepository;

@Autowired
private DataArchiveDeleteJob dataArchiveDeleteJob;

private JobExecutionContext jobExecutionContext;

@Before
public void setup() {
jobExecutionContext = mock(JobExecutionContext.class);
}

@Test
public void testExecute_whenDataArchiveTaskNotFound_shouldUpdateTaskStatusToFailed() throws JobExecutionException {
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("task_id_1");

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
.thenReturn(Optional.empty());

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
}

@Test
public void testExecute_whenDataArchiveTaskNotFinished_shouldUpdateTaskStatusToFailed() throws JobExecutionException {
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("task_id_2");

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(2L);
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(3L);
dataArchiveTask.setStatus(TaskStatus.RUNNING);

when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
.thenReturn(Optional.of(dataArchiveTask));

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
}

@Test
public void testExecute_whenTasksPreparedAndExecuted_shouldUpdateTaskStatusBasedOnTaskUnitsStatus() throws JobExecutionException {
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("task_id_3");

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(3L);
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(4L);
dataArchiveTask.setStatus(TaskStatus.DONE);

List<DlmTask> taskUnits = new ArrayList<>();
DlmTask taskUnit1 = new DlmTask();
taskUnit1.setId("id_1");
taskUnit1.setJobType(JobType.DELETE);
taskUnit1.setStatus(TaskStatus.PREPARING);

DlmTask taskUnit2 = new DlmTask();
taskUnit2.setId("id_2");
taskUnit2.setJobType(JobType.DELETE);
taskUnit2.setStatus(TaskStatus.FAILED);

taskUnits.add(taskUnit1);
taskUnits.add(taskUnit2);

when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
.thenReturn(Optional.of(dataArchiveTask));

when(JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {}))
.thenReturn(taskUnits);

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
}
}