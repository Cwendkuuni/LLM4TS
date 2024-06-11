import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.scheduler.GroupedScheduler;
import org.quartz.scheduler.Scheduler;
import org.quartz.scheduler.SchedulerException;
import org.quartz.scheduler.SchedulerFactory;
import org.quartz.simpl.SimpleScheduleBuilder;
import org.quartz.simpl.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataArchiveRollbackJobTest {
 private static final Logger LOGGER = LoggerFactory.getLogger(DataArchiveRollbackJobTest.class);
 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private JobExecutionContext jobExecutionContext;
 private ScheduleTaskEntity scheduleTaskEntity;
 private DataArchiveRollbackParameters rollbackParameters;
 private List<DlmTask> taskUnits;
 private Scheduler scheduler;

 @Before
 public void setUp() throws SchedulerException {
 SchedulerFactory schedulerFactory = new StdSchedulerFactory();
 scheduler = schedulerFactory.getScheduler();
 scheduler.start();

 dataArchiveRollbackJob = new DataArchiveRollbackJob();

 scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("testJob");
 scheduleTaskEntity.setJobGroup("testGroup");
 scheduleTaskEntity.setStatus(TaskStatus.PREPARING);

 rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(2L);

 taskUnits = Arrays.asList(new DlmTask(), new DlmTask(), new DlmTask());
 }

 @Test
 public void testExecute() throws JobExecutionException, SchedulerException {
 // Mock the JsonUtils.fromJson method
 when(JsonUtils.fromJson(anyString(), eq(DataArchiveRollbackParameters.class)))
 .thenReturn(rollbackParameters);
 when(JsonUtils.fromJson(anyString(), eq(new TypeReference<List<DlmTask>>() {})))
 .thenReturn(taskUnits);

 // Add a mock ScheduleTaskEntity to the job context
 jobExecutionContext = mock(JobExecutionContext.class);
 when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);

 // Mock the scheduleTaskRepository.findById method
 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(2L);
 dataArchiveTask.setResultJson("[]");
 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(dataArchiveTask));

 // Execute the DataArchiveRollbackJob
 dataArchiveRollbackJob.execute(jobExecutionContext);

 // Check that the task status has been updated
 assertEquals(scheduleTaskEntity.getStatus(), TaskStatus.FAILED);

 // Verify that the scheduleTaskRepository.updateStatusById method was called with the correct arguments
 verify(scheduleTaskRepository).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
 }

 // Add more test methods to cover all the edge cases in the DataArchiveRollbackJob.execute method

}