import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.simpl.RAMJobStore;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.test.util.ReflectionTestUtils;

@Slf4j
public class DataArchiveRollbackJobTest {

private SchedulerFactoryBean schedulerFactoryBean;
private Scheduler scheduler;
private ScheduleTaskRepository scheduleTaskRepository;
private DataArchiveRollbackJob dataArchiveRollbackJob;

@Autowired
private ApplicationContext applicationContext;

@Configuration
static class ContextConfiguration {

@Bean
public SchedulerFactoryBean schedulerFactoryBean() {
RAMJobStore jobStore = new RAMJobStore();
jobStore.setClassLoadHelper(new ClassLoadHelper() {
@Override
public ClassLoadHelper newClassLoadHelper() {
return this;
}

@Override
public Class<?> loadClass(String classname) throws ClassNotFoundException {
try {
return Class.forName(classname, true, Thread.currentThread().getContextClassLoader());
} catch (ClassNotFoundException ex) {
return Class.forName(classname);
}
}
});

SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
schedulerFactoryBean.setJobFactory(springBeanJobFactory());
schedulerFactoryBean.setJobStore(jobStore);
return schedulerFactoryBean;
}

@Bean
public JobFactory springBeanJobFactory() {
return new SpringBeanJobFactory();
}

}

@Before
public void setUp() throws Exception {
schedulerFactoryBean = new SchedulerFactoryBean();
schedulerFactoryBean.setApplicationContext(applicationContext);
schedulerFactoryBean.setOverwriteExistingJobs(true);
schedulerFactoryBean.afterPropertiesSet();

scheduler = schedulerFactoryBean.getScheduler();
scheduler.start();

scheduleTaskRepository = mock(ScheduleTaskRepository.class);
dataArchiveRollbackJob = new DataArchiveRollbackJob();
ReflectionTestUtils.setField(dataArchiveRollbackJob, "scheduleTaskRepository", scheduleTaskRepository);
}

@After
public void tearDown() throws Exception {
scheduler.shutdown();
}

@Test
public void testExecute_Success() throws Exception {
// Given
JobExecutionContext context = mock(JobExecutionContext.class);
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setJobName("dataArchiveRollbackJob");
taskEntity.setJobGroup("group1");
taskEntity.setParametersJson("{\"dataArchiveTaskId\":10L}");
taskEntity.setResultJson("[]");
when(context.getResult()).thenReturn(taskEntity);

DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
rollbackParameters.setDataArchiveTaskId(10L);
when(scheduleTaskRepository.findById(10L)).thenReturn(Optional.of(taskEntity));

List<DlmTask> taskUnits = new ArrayList<>();
taskUnits.add(new DlmTask());
taskUnits.add(new DlmTask());
when(JsonUtils.fromJson(anyString(), eq(new TypeReference<List<DlmTask>>() {})))
.thenReturn(taskUnits);

// When
dataArchiveRollbackJob.execute(context);

// Then
verify(scheduleTaskRepository, times(1)).findById(10L);
verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.DONE);
}

@Test
public void testExecute_NoDataArchiveTask() throws Exception {
// Given
JobExecutionContext context = mock(JobExecutionContext.class);
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setJobName("dataArchiveRollbackJob");
taskEntity.setJobGroup("group1");
taskEntity.setParametersJson("{\"dataArchiveTaskId\":10L}");
taskEntity.setResultJson("[]");
when(context.getResult()).thenReturn(taskEntity);

when(scheduleTaskRepository.findById(10L)).thenReturn(Optional.ofNullable(null));

// When
dataArchiveRollbackJob.execute(context);

// Then
verify(scheduleTaskRepository, times(1)).findById(10L);
verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.FAILED);
}

@Test(expected = JobExecutionException.class)
public void testExecute_NullDataArchiveTask() throws Exception {
// Given
JobExecutionContext context = mock(JobExecutionContext.class);
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setJobName("dataArchiveRollbackJob");
taskEntity.setJobGroup("group1");
taskEntity.setParametersJson("{\"dataArchiveTaskId\":10L}");
taskEntity.setResultJson("[]");
when(context.getResult()).thenReturn(taskEntity);

when(scheduleTaskRepository.findById(10L)).thenReturn(Optional.of(taskEntity));

// When
dataArchiveRollbackJob.execute(context);

// Then
verify(scheduleTaskRepository, times(1)).findById(10L);
verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.FAILED);
}

@Test
public void testExecute_NoTaskUnits() throws Exception {
// Given
JobExecutionContext context = mock(JobExecutionContext.class);
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setJobName("dataArchiveRollbackJob");
taskEntity.setJobGroup("group1");
taskEntity.setParametersJson("{\"dataArchiveTaskId\":10L}");
taskEntity.setResultJson("[]");
when(context.getResult()).thenReturn(taskEntity);

when(scheduleTaskRepository.findById(10L)).thenReturn(Optional.of(taskEntity));

when(JsonUtils.fromJson(anyString(), eq(new TypeReference<List<DlmTask>>() {})))
.thenReturn(null);

// When
dataArchiveRollbackJob.execute(context);

// Then
verify(scheduleTaskRepository, times(1)).findById(10L);
verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.FAILED);
}

}