import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.scheduler.JobKey;
import org.quartz.scheduler.Scheduler;
import org.quartz.scheduler.SchedulerException;
import org.quartz.scheduler.SchedulerFactory;
import org.quartz.simpl.SimpleScheduleBuilder;
import org.quartz.simpl.TriggerBuilder;
import org.quartz.simpl.TriggerKey;
import org.quartz.trigger.Trigger;

@Slf4j
public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private Scheduler scheduler;
 private JobKey jobKey;
 private TriggerKey triggerKey;
 private TriggerBuilder triggerBuilder;
 private ScheduleTaskEntity scheduleTaskEntity;
 private DataArchiveRollbackParameters rollbackParameters;
 private List<DlmTask> taskUnits;
 private SchedulerFactory schedulerFactory;

 @Before
 public void setup() throws SchedulerException {
 schedulerFactory = new org.quartz.impl.StdSchedulerFactory();
 scheduler = schedulerFactory.getScheduler();
 scheduler.start();

 jobKey = JobKey.jobKey("testJob", "testGroup");
 triggerKey = TriggerKey.triggerKey("testTrigger", "testGroup");
 triggerBuilder = TriggerBuilder.newTrigger().withIdentity(triggerKey);

 scheduleTaskEntity = new ScheduleTaskEntity();
 rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(1L);
 taskUnits = new ArrayList<>();
 }

 @Test
 public void testExecute() throws Exception {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();

 JobExecutionContext context = mock(JobExecutionContext.class);
 when(context.getResult()).thenReturn(scheduleTaskEntity);
 when(context.getMergedJobDataMap().getString("parametersJson"))
 .thenReturn(JsonUtils.toJson(rollbackParameters));

 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
 scheduleTaskEntity.setJobName("testJob");
 scheduleTaskEntity.setJobGroup("testGroup");
 scheduleTaskEntity.setStatus(TaskStatus.CREATED);

 when(scheduler.getJobDetail(jobKey))
 .thenReturn(scheduler.getListenerManager().getJobDetail(jobKey));
 when(scheduler.getTrigger(triggerKey))
 .thenReturn(triggerBuilder.startNow().build());

 when(scheduleTaskRepository.findById(anyLong()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 DlmTask dlmTask = new DlmTask();
 dlmTask.setSourceDatabaseId(1L);
 dlmTask.setTargetDatabaseId(2L);
 taskUnits.add(dlmTask);

 when(scheduleTaskRepository.updateStatusById(anyLong(), any()))
 .thenReturn(true);

 scheduleTaskEntity.setResultJson(JsonUtils.toJson(taskUnits));

 dataArchiveRollbackJob.execute(context);

 // add assertions here based on the expected behavior
 assertEquals(TaskStatus.DONE, scheduleTaskEntity.getStatus());
 }

 // Add more test cases for different scenarios
}