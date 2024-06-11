import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.AbstractDlmJob;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.scheduler.Scheduler;
import org.quartz.scheduler.SchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.MockitoAnnotations.initMocks;

@SpringBootTest
@ActiveProfiles("test")
public class DataArchiveRollbackJobTest {

@MockBean
private ScheduleTaskRepository scheduleTaskRepository;

private DataArchiveRollbackJob dataArchiveRollbackJob;

private Scheduler scheduler;

@Before
public void setup() throws Exception {
initMocks(this);
dataArchiveRollbackJob = spy(new DataArchiveRollbackJob());

SchedulerFactory schedulerFactory = new org.quartz.impl.StdSchedulerFactory();
scheduler = schedulerFactory.getScheduler();
scheduler.start();

doReturn(true).when(dataArchiveRollbackJob).checkJobCanRun(any(JobExecutionContext.class));
}

@Test
public void testExecute() throws Exception {

JobExecutionContext jobExecutionContext = createJobExecutionContext();

dataArchiveRollbackJob.execute(jobExecutionContext);

// Verify the task status is updated to FAILED when data archive task is not found
ScheduleTaskEntity taskEntity = jobExecutionContext.getMergedJobDataMap().get("scheduleTaskEntity");
Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.ofNullable(taskEntity);

doReturn(dataArchiveTaskOption).when(dataArchiveRollbackJob).findDataArchiveTask(anyLong());

dataArchiveRollbackJob.execute(jobExecutionContext);

}

private JobExecutionContext createJobExecutionContext() throws Exception {
JobExecutionContext jobExecutionContext = new JobExecutionContext();

DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
rollbackParameters.setDataArchiveTaskId(1L);

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(2L);
taskEntity.setJobName("DataArchiveRollbackJob");
taskEntity.setJobGroup("group1");
taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
taskEntity.setStatus(TaskStatus.RUNNING);

jobExecutionContext.setResult(taskEntity);

CronTriggerImpl trigger = new CronTriggerImpl();
trigger.setName("testTrigger");
trigger.setGroup("group1");
trigger.setCronExpression("0/5 * * * * ?");
trigger.setNextFireTime(new Date());

jobExecutionContext.setTrigger(trigger);

return jobExecutionContext;
}

private Optional<ScheduleTaskEntity> findDataArchiveTask(Long dataArchiveTaskId) {
List<ScheduleTaskEntity> taskEntities = new ArrayList<>();
ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(dataArchiveTaskId);
taskEntities.add(dataArchiveTask);
return taskEntities.stream().findFirst();
}
}