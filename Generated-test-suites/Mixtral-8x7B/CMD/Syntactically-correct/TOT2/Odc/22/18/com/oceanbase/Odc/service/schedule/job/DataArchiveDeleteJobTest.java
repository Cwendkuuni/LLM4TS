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
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class DataArchiveDeleteJobTest {

 private Scheduler scheduler;
 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskEntity scheduleTaskEntity;
 private DataArchiveClearParameters dataArchiveClearParameters;
 private List<DlmTask> taskUnits;

 @Before
 public void setup() {
 scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setStatus(TaskStatus.DONE);

 dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(2L);

 taskUnits = new ArrayList<>();

 dataArchiveDeleteJob = spy(new DataArchiveDeleteJob() {
 @Override
 protected SchedulerFactoryBean schedulerFactoryBean() {
 return null;
 }
 });
 }

 @TestConfiguration
 static class TestConfig {
 @Bean
 @Primary
 public SchedulerFactoryBean schedulerFactoryBean() {
 SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
 return factoryBean;
 }
 }

 @Test
 public void execute_dataArchiveTaskNotFound_shouldUpdateTaskStatusToFailed() throws SchedulerException {
 when(scheduleTaskRepository.findById(eq(dataArchiveClearParameters.getDataArchiveTaskId())))
 .thenReturn(Optional.empty());

 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

 verify(scheduleTaskRepository).updateStatusById(eq(scheduleTaskEntity.getId()), eq(TaskStatus.FAILED));
 }

 @Test
 public void execute_dataArchiveTaskNotDone_shouldUpdateTaskStatusToFailed() throws SchedulerException {
 when(scheduleTaskRepository.findById(eq(dataArchiveClearParameters.getDataArchiveTaskId())))
 .thenReturn(Optional.of(new ScheduleTaskEntity().setStatus(TaskStatus.RUNNING)));

 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

 verify(scheduleTaskRepository).updateStatusById(eq(scheduleTaskEntity.getId()), eq(TaskStatus.FAILED));
 }

 @Test
 public void execute_validDataArchiveTask_shouldPrepareTasksForClearAndUpdateTaskStatus()
 throws SchedulerException {
 when(scheduleTaskRepository.findById(eq(dataArchiveClearParameters.getDataArchiveTaskId())))
 .thenReturn(Optional.of(scheduleTaskEntity));
 when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class)))
 .thenReturn(1);

 taskUnits.add(new DlmTask());
 when(JsonUtils.fromJson(anyString(), any(TypeReference.class)))
 .thenReturn(taskUnits);

 dataArchiveDeleteJob.execute(mock(JobExecutionContext.class));

 verify(dataArchiveDeleteJob).prepareTasksForClear(eq(scheduleTaskEntity), eq(taskUnits));
 verify(scheduleTaskRepository).updateStatusById(eq(scheduleTaskEntity.getId()), any(TaskStatus.class));
 }

 private void prepareTasksForClear(ScheduleTaskEntity scheduleTaskEntity, List<DlmTask> taskUnits) {
 taskUnits.forEach(taskUnit -> {
 taskUnit.setId(DlmJobIdUtil.generateHistoryJobId(scheduleTaskEntity.getJobName(),
 scheduleTaskEntity.getJobGroup(),
 scheduleTaskEntity.getId(),
 taskUnits.size()));
 taskUnit.setJobType(JobType.DELETE);
 taskUnit.setStatus(TaskStatus.PREPARING);
 });
 }

 private TaskStatus getTaskStatus(List<DlmTask> taskUnits) {
 return TaskStatus.PREPARING;
 }

 @Test
 public void prepareTasksForClear_validDataArchiveTask() {
 prepareTasksForClear(scheduleTaskEntity, taskUnits);

 assertEquals(DlmJobIdUtil.generateHistoryJobId("jobName", "jobGroup", 1L, 1),
 taskUnits.get(0).getId());
 assertEquals(JobType.DELETE, taskUnits.get(0).getJobType());
 assertEquals(TaskStatus.PREPARING, taskUnits.get(0).getStatus());
 }

 @Test
 public void updateStatusById_success() throws SchedulerException {
 when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class)))
 .thenReturn(1);

 dataArchiveDeleteJob.updateStatusById(1L, TaskStatus.PREPARING);

 verify(scheduleTaskRepository).updateStatusById(eq(1L), eq(TaskStatus.PREPARING));
 }

 @Test(expected = SchedulerException.class)
 public void updateStatusById_failure() throws SchedulerException {
 when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class)))
 .thenThrow(new SchedulerException("test"));

 dataArchiveDeleteJob.updateStatusById(1L, TaskStatus.PREPARING);
 }

}