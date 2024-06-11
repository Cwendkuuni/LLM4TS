import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class DataArchiveDeleteJobTest {

@MockBean
private ScheduleTaskRepository scheduleTaskRepository;

@Autowired
private DataArchiveDeleteJob dataArchiveDeleteJob;

private ScheduleTaskEntity taskEntity;
private ScheduleTaskEntity dataArchiveTask;
private DataArchiveClearParameters dataArchiveClearParameters;
private List<DlmTask> taskUnits;
private JobExecutionContext jobExecutionContext;

@Before
public void setup() {
taskEntity = new ScheduleTaskEntity();
dataArchiveTask = new ScheduleTaskEntity();
dataArchiveClearParameters = new DataArchiveClearParameters();
taskUnits = new ArrayList<>();
jobExecutionContext = new JobExecutionContext(null);
}

@Test
public void testExecute_DataArchiveTaskNotFound_ReturnsFailed() throws JobExecutionException {
dataArchiveClearParameters.setDataArchiveTaskId("123");
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
when(scheduleTaskRepository.findById(eq("123"))).thenReturn(Optional.empty());

dataArchiveDeleteJob.execute(jobExecutionContext);

assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
}

@Test
public void testExecute_DataArchiveTaskNotDone_ReturnsFailed() throws JobExecutionException {
dataArchiveClearParameters.setDataArchiveTaskId("123");
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
dataArchiveTask.setId("123");
dataArchiveTask.setStatus(TaskStatus.RUNNING);
when(scheduleTaskRepository.findById(eq("123"))).thenReturn(Optional.of(dataArchiveTask));

dataArchiveDeleteJob.execute(jobExecutionContext);

assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
}

@Test
public void testExecute_Success() throws JobExecutionException {
dataArchiveClearParameters.setDataArchiveTaskId("123");
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
dataArchiveTask.setId("123");
dataArchiveTask.setStatus(TaskStatus.DONE);
taskUnits.add(new DlmTask());
when(scheduleTaskRepository.findById(eq("123"))).thenReturn(Optional.of(dataArchiveTask));

dataArchiveDeleteJob.execute(jobExecutionContext);

assertEquals(TaskStatus.PREPARING, taskUnits.get(0).getStatus());
assertEquals(TaskStatus.PREPARING, taskEntity.getStatus());
}
}