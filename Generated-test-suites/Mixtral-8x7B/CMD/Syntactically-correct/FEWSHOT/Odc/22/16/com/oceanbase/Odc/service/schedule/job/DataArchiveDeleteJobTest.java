import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.scheduler.GroupedScheduler;
import org.quartz.scheduler.Scheduler;
import org.quartz.scheduler.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class DataArchiveDeleteJobTest implements ApplicationContextAware {

@MockBean
private SchedulerFactoryBean schedulerFactoryBean;

@MockBean
private JobFactory jobFactory;

@MockBean
private scheduleTaskRepository scheduleTaskRepository;

@Autowired
private ApplicationContext applicationContext;

private DataArchiveDeleteJob dataArchiveDeleteJob;

private Scheduler scheduler;

private JobExecutionContext jobExecutionContext;

@Before
public void setup() throws SchedulerException {
dataArchiveDeleteJob = spy(new DataArchiveDeleteJob());

scheduler = mock(Scheduler.class);
when(schedulerFactoryBean.getScheduler()).thenReturn(scheduler);

jobExecutionContext = mock(JobExecutionContext.class);
when(jobExecutionContext.getMergedJobDataMap().getString("scheduleTaskId")).thenReturn("123");
when(jobExecutionContext.getScheduler()).thenReturn(scheduler);
when(scheduler.getJobDetail(anyString(), anyString())).thenReturn(null);
when(scheduler.getTrigger(anyString(), anyString())).thenReturn(null);
when(scheduler.getJobGroupNames()).thenReturn(new String[]{"group1", "group2"});
when(scheduler.getTriggerGroupNames("group1")).thenReturn(new String[]{"triggerGroup1", "triggerGroup2"});

applicationContext.setBean("schedulerFactoryBean", schedulerFactoryBean);
applicationContext.setBean("jobFactory", jobFactory);
}

@Test
public void testExecute_whenTaskNotFound_thenUpdateTaskStatusToFailed() throws SchedulerException {
DataArchiveClearParameters parameters = new DataArchiveClearParameters();
parameters.setDataArchiveTaskId("456");

String parametersJson = JsonUtils.toJson(parameters);
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId("123");
taskEntity.setParametersJson(parametersJson);

doReturn(taskEntity).when(dataArchiveDeleteJob).getScheduleTaskEntity(jobExecutionContext);

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
}

@Test
public void testExecute_whenTaskNotDone_thenUpdateTaskStatusToFailed() throws SchedulerException {
DataArchiveClearParameters parameters = new DataArchiveClearParameters();
parameters.setDataArchiveTaskId("456");

String parametersJson = JsonUtils.toJson(parameters);
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId("123");
taskEntity.setParametersJson(parametersJson);
taskEntity.setStatus(TaskStatus.RUNNING);

doReturn(taskEntity).when(dataArchiveDeleteJob).getScheduleTaskEntity(jobExecutionContext);

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
}

@Test
public void testExecute_whenTaskFoundAndDone_thenExecuteTask() throws SchedulerException {
DataArchiveClearParameters parameters = new DataArchiveClearParameters();
parameters.setDataArchiveTaskId("456");

String parametersJson = JsonUtils.toJson(parameters);
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId("123");
taskEntity.setParametersJson(parametersJson);
taskEntity.setStatus(TaskStatus.DONE);

DlmTask dlmTask1 = new DlmTask();
dlmTask1.setId(1L);

DlmTask dlmTask2 = new DlmTask();
dlmTask2.setId(2L);

List<DlmTask> taskUnits = List.of(dlmTask1, dlmTask2);
String taskUnitsJson = JsonUtils.toJson(taskUnits);

doReturn(Optional.of(taskEntity)).when(dataArchiveDeleteJob).getScheduleTaskEntity(jobExecutionContext);
when(scheduleTaskRepository.findById(taskEntity.getId())).thenReturn(Optional.of(taskEntity));
when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.FAILED)).thenReturn(null);
when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.RUNNING)).thenReturn(null);
when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.PREPARING)).thenReturn(null);
when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.SUCCESS)).thenReturn(null);
when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.FAILED)).thenReturn(null);
when( JsonUtils.fromJson(dataArchiveTask.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.PREPARING);
verify(dataArchiveDeleteJob).executeTask(taskEntity.getId(), taskUnits);
}

@Override
public void setApplicationContext(ApplicationContext applicationContext) {
this.applicationContext = applicationContext;
}
}