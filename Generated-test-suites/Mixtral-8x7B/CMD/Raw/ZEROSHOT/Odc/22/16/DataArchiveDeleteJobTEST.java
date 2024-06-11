import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import org.junit.Before;
import org.junit.Test;
import org.quartz.*;
import org.quartz.core.JobDetail;
import org.quartz.core.JobExecutor;
import org.quartz.core.QuartzScheduler;
import org.quartz.core. scheduler.ClassLoadHelper;
import org.quartz.core.scheduler.SchedulerFactoryImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.simpl.RAMJobStore;
import org.quartz.simpl.SimpleThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

private static final Logger log = LoggerFactory.getLogger(DataArchiveDeleteJobTest.class);
private QuartzScheduler scheduler;
private ScheduleTaskEntity taskEntity;
private DataArchiveClearParameters dataArchiveClearParameters;
private DataArchiveDeleteJob dataArchiveDeleteJob;
private ScheduleTaskRepository scheduleTaskRepository;

@Before
public void setUp() throws SchedulerException {
scheduler = new StdSchedulerFactory().getScheduler();
scheduler.start();

scheduleTaskRepository = mock(ScheduleTaskRepository.class);

dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("123");

taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
taskEntity.setStatus(TaskStatus.DONE);

dataArchiveDeleteJob = new DataArchiveDeleteJob();
dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);

}

@Test
public void testExecute() throws SchedulerException, InterruptedException {
// mock data archive task
List<DlmTask> dataArchiveTasks = new ArrayList<>();
dataArchiveTasks.add(new DlmTask());
dataArchiveTasks.add(new DlmTask());

when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(taskEntity));
when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class))).thenReturn(true);

JobDetail jobDetail = JobBuilder.newJob(DataArchiveDeleteJob.class)
.withIdentity("testJob", "testGroup")
.build();

jobDetail.getJobDataMap().put("scheduleTaskRepository", scheduleTaskRepository);

scheduler.addJob(jobDetail, true);

// trigger job
JobKey jobKey = new JobKey("testJob", "testGroup");
JobExecutionContext jobExecutionContext = scheduler.getContext().getScheduler().getContext().getJobExecutionContext(jobKey);
jobExecutionContext.setMergedJobDataMap(jobDetail.getJobDataMap());
dataArchiveDeleteJob.execute(jobExecutionContext);

// verify if task status is updated
verify(scheduleTaskRepository, times(1)).updateStatusById(eq(taskEntity.getId()), any(TaskStatus.class));
}

}