import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@SuppressWarnings("unchecked")
public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private SchedulerFactoryBean schedulerFactoryBean;
 private Scheduler scheduler;
 private JobExecutionContext jobExecutionContext;
 private ScheduleTaskEntity taskEntity;
 private DataArchiveClearParameters dataArchiveClearParameters;
 private List<DlmTask> taskUnits;

 @Before
 public void setup() throws SchedulerException {
 dataArchiveDeleteJob = new DataArchiveDeleteJob();
 schedulerFactoryBean = mock(SchedulerFactoryBean.class);
 scheduler = mock(Scheduler.class);
 when(schedulerFactoryBean.getScheduler()).thenReturn(scheduler);
 jobExecutionContext = mock(JobExecutionContext.class);
 taskEntity = mock(ScheduleTaskEntity.class);
 dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("123");
 taskUnits = new ArrayList<>();
 taskUnits.add(new DlmTask());
 taskUnits.get(0).setId("456");
 taskUnits.get(0).setJobType(JobType.DELETE);
 }

 @Test
 public void testExecute() throws Exception {
 when(jobExecutionContext.getResult()).thenReturn(taskEntity);
 when(taskEntity.getParametersJson()).thenReturn("{}");
 when(taskEntity.getId()).thenReturn(1L);
 when(taskEntity.getResultJson()).thenReturn("[]");
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(taskEntity));
 when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.FAILED))
 .thenReturn(true);

 dataArchiveDeleteJob.execute(jobExecutionContext);
 }

 @Test
 public void testExecute_whenDataArchiveTaskNotFound_thenLogWarnAndUpdateTaskStatusToFailed() throws Exception {
 when(jobExecutionContext.getResult()).thenReturn(taskEntity);
 when(taskEntity.getParametersJson()).thenReturn("{}");
 when(taskEntity.getId()).thenReturn(1L);
 when(taskEntity.getResultJson()).thenReturn("[]");
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 dataArchiveDeleteJob.execute(jobExecutionContext);
 }

 @Test
 public void testExecute_whenDataArchiveTaskNotFinished_thenLogWarnAndUpdateTaskStatusToFailed()
 throws Exception {
 when(jobExecutionContext.getResult()).thenReturn(taskEntity);
 when(taskEntity.getParametersJson()).thenReturn("{}");
 when(taskEntity.getId()).thenReturn(1L);
 when(taskEntity.getResultJson()).thenReturn("[]");
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(taskEntity));
 when(taskEntity.getStatus()).thenReturn(TaskStatus.RUNNING);

 dataArchiveDeleteJob.execute(jobExecutionContext);
 }

 @Test
 public void testExecute_whenPrepareTasksForClearSuccessfully_thenExecuteTaskAndUpdateTaskStatus()
 throws Exception {
 when(jobExecutionContext.getResult()).thenReturn(taskEntity);
 when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters));
 when(taskEntity.getId()).thenReturn(1L);
 when(taskEntity.getResultJson()).thenReturn("[]");
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(taskEntity));
 when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);
 when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.PREPARING))
 .thenReturn(true);
 when(scheduleTaskRepository.executeTask(taskEntity.getId(), taskUnits))
 .thenReturn(true);
 when(scheduleTaskRepository.getTaskStatus(taskUnits))
 .thenReturn(TaskStatus.DONE);

 dataArchiveDeleteJob.execute(jobExecutionContext);
 }

 @Test
 public void testExecute_whenTaskUnitsIsNull_thenDoNothing() throws Exception {
 when(jobExecutionContext.getResult()).thenReturn(taskEntity);
 when(taskEntity.getParametersJson()).thenReturn("{}");
 when(taskEntity.getId()).thenReturn(1L);
 when(taskEntity.getResultJson()).thenReturn("[]");
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(taskEntity));
 when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);

 dataArchiveDeleteJob.execute(jobExecutionContext);
 }

 @Test
 public void testExecute_whenExecuteTaskFails_thenUpdateTaskStatusToFailed() throws Exception {
 when(jobExecutionContext.getResult()).thenReturn(taskEntity);
 when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters));
 when(taskEntity.getId()).thenReturn(1L);
 when(taskEntity.getResultJson()).thenReturn("[]");
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(taskEntity));
 when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);
 when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.PREPARING))
 .thenReturn(true);
 when(scheduleTaskRepository.executeTask(taskEntity.getId(), taskUnits))
 .thenReturn(false);

 dataArchiveDeleteJob.execute(jobExecutionContext);
 }

 @Test
 public void testExecute_whenGetTaskStatusFails_thenUpdateTaskStatusToFailed() throws Exception {
 when(jobExecutionContext.getResult()).thenReturn(taskEntity);
 when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters));
 when(taskEntity.getId()).thenReturn(1L);
 when(taskEntity.getResultJson()).thenReturn("[]");
 when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(taskEntity));
 when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);
 when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.PREPARING))
 .thenReturn(true);
 when(scheduleTaskRepository.executeTask(taskEntity.getId(), taskUnits))
 .thenReturn(true);
 when(scheduleTaskRepository.getTaskStatus(taskUnits))
 .thenThrow(new RuntimeException());

 dataArchiveDeleteJob.execute(jobExecutionContext);
 }

 @Test
 public void testGetTaskStatus() throws Exception {
 assertEquals(TaskStatus.DONE, dataArchiveDeleteJob.getTaskStatus(taskUnits));
 }

 private Scheduler schedulerCreate() throws SchedulerException {
 Scheduler scheduler = schedulerFactoryBean.getScheduler();
 scheduler.start();

 return scheduler;
 }

 private void schedulerShutdown(Scheduler scheduler) throws SchedulerException {
 scheduler.shutdown();
 }
}