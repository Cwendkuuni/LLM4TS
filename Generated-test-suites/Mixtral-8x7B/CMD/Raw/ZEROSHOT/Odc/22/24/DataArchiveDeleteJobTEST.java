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
import org.quartz.*;
import org.quartz.core.JobDetail;
import org.quartz.core.JobExecutor;
import org.quartz.core. scheduler.SchedulerContext;
import org.quartz.core.scheduler.SchedulerFactory;
import org.quartz.core.scheduler.TriggerFiredBundle;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.simpl.SimpleScheduleBuilder;
import org.quartz.simpl.SimpleTrigger;
import org.quartz.simpl.SimpleTriggerImpl;
import org.quartz.spi.OperableTrigger;
import org.quartz.spi.TriggerFiredResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

private SchedulerFactory schedulerFactory;
private Scheduler scheduler;
private JobDetail jobDetail;
private JobDataMap jobDataMap;
private DataArchiveDeleteJob dataArchiveDeleteJob;
private ScheduleTaskEntity scheduleTaskEntity;
private DataArchiveClearParameters dataArchiveClearParameters;
private List<DlmTask> taskUnits;

@Before
public void setUp() throws SchedulerException {
schedulerFactory = new StdSchedulerFactory();
scheduler = schedulerFactory.getScheduler();
jobDetail = JobBuilder.newJob(DataArchiveDeleteJob.class)
.withIdentity("testJob", Scheduler.DEFAULT_GROUP)
.build();
jobDataMap = new JobDataMap();
dataArchiveDeleteJob = new DataArchiveDeleteJob();
scheduleTaskEntity = new ScheduleTaskEntity();
dataArchiveClearParameters = new DataArchiveClearParameters();
taskUnits = new ArrayList<>();
}

@Test
public void testExecute() throws SchedulerException, JobExecutionException {
// set up mock objects
SchedulerContext schedulerContext = mock(SchedulerContext.class);
scheduleTaskRepository = mock(ScheduleTaskRepository.class);
when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(scheduleTaskEntity));
when(scheduleTaskRepository.updateStatusById(anyString(), any(TaskStatus.class))).thenReturn(true);
jobDataMap.put("scheduleTaskRepository", scheduleTaskRepository);
dataArchiveClearParameters.setDataArchiveTaskId("testDataArchiveTaskId");
taskUnits.add(new DlmTask());
String parametersJson = JsonUtils.toJson(dataArchiveClearParameters);
scheduleTaskEntity.setParametersJson(parametersJson);
String resultJson = JsonUtils.toJson(taskUnits);
scheduleTaskEntity.setResultJson(resultJson);
jobDataMap.put("scheduleTaskEntity", scheduleTaskEntity);

// set up job detail with job data map
jobDetail.setJobDataMap(jobDataMap);

// start scheduler
scheduler.start();

// set up trigger
SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
.withIntervalInSeconds(1)
.repeatForever();
SimpleTrigger simpleTrigger = TriggerBuilder.newTrigger()
.withIdentity("testTrigger", Scheduler.DEFAULT_GROUP)
.startNow()
.withSchedule(simpleScheduleBuilder)
.build();

// schedule job
scheduler.scheduleJob(jobDetail, simpleTrigger);

// execute job
JobExecutionContext jobExecutionContext = new JobExecutionContext(scheduler, jobDetail, null, null);
dataArchiveDeleteJob.execute(jobExecutionContext);

// verify that task status is updated to DONE
verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.DONE);
}
}