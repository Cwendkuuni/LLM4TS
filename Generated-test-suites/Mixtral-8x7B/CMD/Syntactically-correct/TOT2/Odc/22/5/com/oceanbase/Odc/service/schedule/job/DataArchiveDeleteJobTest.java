import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.matchers.JobMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class DataArchiveDeleteJobTest {

@MockBean
private JobFactory jobFactory;

@MockBean
private MongoTemplate mongoTemplate;

@MockBean
private ScheduleTaskRepository scheduleTaskRepository;

@Autowired
private DataArchiveDeleteJob dataArchiveDeleteJob;

private JobExecutionContext jobExecutionContext;

@Before
public void setup() {
JobExecutionContext jobExecutionContext = Mockito.mock(JobExecutionContext.class);
when(jobExecutionContext.getMergedJobDataMap()).thenReturn(Mockito.mock(JobDataMap.class));
when(jobExecutionContext.getResult()).thenReturn(Mockito.mock(ScheduleTaskEntity.class));
this.jobExecutionContext = jobExecutionContext;
}

@Test
public void testExecute_whenDataArchiveTaskIsNotFound_thenUpdateStatusToFailed() throws JobExecutionException {
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("task123");
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId("task123");
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.ofNullable(null));

dataArchiveDeleteJob.execute(jobExecutionContext);

assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
 assertEquals("FAILED", taskEntity.getStatus());
}

@Test
public void testExecute_whenDataArchiveTaskIsNotDone_thenUpdateStatusToFailed() throws JobExecutionException {
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("task123");
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId("task123");
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId("dataArchiveTask123");
dataArchiveTask.setStatus(TaskStatus.RUNNING);

when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(dataArchiveTask));

dataArchiveDeleteJob.execute(jobExecutionContext);

assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
}

@Test
public void testExecute_whenPrepareTasksForClear_thenExecuteTask() throws JobExecutionException {
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("task123");
List<DlmTask> dlmTasks = new ArrayList<>();
dlmTasks.add(new DlmTask());
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId("task123");
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId("dataArchiveTask123");
dataArchiveTask.setStatus(TaskStatus.DONE);
dataArchiveTask.setResultJson(JsonUtils.toJson(dlmTasks));

when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(dataArchiveTask));

dataArchiveDeleteJob.execute(jobExecutionContext);

Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(anyString(), any(TaskStatus.class));
}

}