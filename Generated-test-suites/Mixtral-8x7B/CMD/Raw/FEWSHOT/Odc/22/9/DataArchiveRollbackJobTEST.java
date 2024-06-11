import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;

@org.junit.runner.RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

 @Autowired
 private ScheduleTaskRepository scheduleTaskRepository;

 private DataArchiveRollbackJob dataArchiveRollbackJob;

 private SchedulerFactoryBean schedulerFactoryBean;

 @Before
 public void setup() {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();

 // Initialize the mocks
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 schedulerFactoryBean = mock(SchedulerFactoryBean.class);

 // Inject the mocks into the DataArchiveRollbackJob
 dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
 }

 @Test
 public void testExecute() throws SchedulerException, JobExecutionException {
 // Prepare the input parameters
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 String parametersJson = "{\"dataArchiveTaskId\": 123}";
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(123L);
 taskEntity.setParametersJson(parametersJson);

 // Prepare the mocked data
 List<DlmTask> taskUnits = new ArrayList<>();
 DlmTask taskUnit = new DlmTask();
 taskUnit.setSourceDatabaseId(123L);
 taskUnit.setTargetDatabaseId(456L);
 taskUnits.add(taskUnit);

 // Set up the mocked ScheduleTaskRepository behavior
 when(scheduleTaskRepository.findById(123L)).thenReturn(Optional.of(taskEntity));
 when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.FAILED))
 .thenReturn(taskEntity);

 // Prepare the mocked SchedulerFactoryBean behavior
 JobKey jobKey = new JobKey("testJob", "testGroup");
 JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);
 when(jobExecutionContext.getResult()).thenReturn(taskEntity);
 when(jobExecutionContext.getMergedJobDataMap().getString("TASK_ID"))
 .thenReturn(String.valueOf(taskEntity.getId()));

 // Set up the mocked Scheduler behavior
 Trigger trigger = TriggerBuilder.newTrigger().withIdentity("testTrigger", "testGroup")
 .startNow().build();
 when(schedulerFactoryBean.getScheduler().getTrigger(jobKey, GroupMatcher.groupEquals("testGroup")))
 .thenReturn(trigger);

 // Execute the test
 dataArchiveRollbackJob.setSchedulerFactoryBean(schedulerFactoryBean);
 dataArchiveRollbackJob.execute(jobExecutionContext);

 // Assertions
 // Add your assertions here based on the expected behavior of your test
 }
}