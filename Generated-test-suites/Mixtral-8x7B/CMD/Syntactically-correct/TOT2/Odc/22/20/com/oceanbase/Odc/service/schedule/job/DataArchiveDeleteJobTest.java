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
import org.quartz.SimpleScheduleBuilder;
import org.quartz.TriggerBuilder;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
public class DataArchiveDeleteJobTest {

@MockBean
private ScheduleTaskRepository scheduleTaskRepository;

private DataArchiveDeleteJob dataArchiveDeleteJob;

private JobExecutionContext jobExecutionContext;

@Before
public void setUp() {
dataArchiveDeleteJob = spy(new DataArchiveDeleteJob());

JobExecutionContext.JobExecutionNotificationListener listener = (context, notificationType) -> {};
jobExecutionContext = mock(JobExecutionContext.class);
doReturn("testId").when(jobExecutionContext).getResult();
doReturn(listener).when(jobExecutionContext).getMergedJobDataMap();
}

@Test
public void testExecuteWithNonExistentDataArchiveTask() {
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("nonExistentId");

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId("testId");
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

doReturn(Optional.of(taskEntity)).when(scheduleTaskRepository).findById("testId");

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository).updateStatusById("testId", TaskStatus.FAILED);
}

@Test
public void testExecuteWithUnfinishedDataArchiveTask() {
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("finishedTaskId");

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId("testId");
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId("finishedTaskId");
dataArchiveTask.setStatus(TaskStatus.RUNNING);

doReturn(Optional.of(taskEntity)).when(scheduleTaskRepository).findById("testId");
doReturn(Optional.of(dataArchiveTask)).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository).updateStatusById("testId", TaskStatus.FAILED);
}

@Test
public void testExecuteWithTaskUnits() {
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("finishedTaskId");

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId("testId");
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId("finishedTaskId");
dataArchiveTask.setStatus(TaskStatus.DONE);
dataArchiveTask.setResultJson("[{\"id\":\"testId\",\"jobName\":\"testJob\",\"jobGroup\":\"testGroup\"," +
"\"" +
"jobType\":\"DELETE\",\"status\":\"PREPARING\",\"parameters\":{},\"result\":null,\"startTime\":0,\"endTime\":0}]");

List<DlmTask> taskUnits = new ArrayList<>();
DlmTask taskUnit = new DlmTask();
taskUnit.setId("testId");
taskUnit.setJobName("testJob");
taskUnit.setJobGroup("testGroup");
taskUnit.setJobType(JobType.DELETE);
taskUnit.setStatus(TaskStatus.PREPARING);
taskUnits.add(taskUnit);

doReturn(Optional.of(taskEntity)).when(scheduleTaskRepository).findById("testId");
doReturn(Optional.of(dataArchiveTask)).when(scheduleTaskRepository).findById(dataArchiveClearParameters.getDataArchiveTaskId());
doReturn(taskUnits).when(dataArchiveDeleteJob).prepareTasksForClear(any(), any());

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository).updateStatusById("testId", TaskStatus.PREPARING);
}
}