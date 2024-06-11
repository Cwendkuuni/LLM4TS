import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.AbstractDlmJob;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl. matchers.GroupMatcher;
import org.quartz.impl.matchers.JobMatcher;
import org.quartz.jobs.AnnotatedJobFactory;
import org.quartz.simpl.PropertySettingJobFactory;
import org.quartz.spi.JobFactory;
import org.quartz.spi.OperableTrigger;
import org.quartz.spi.TriggerFiredBundle;
import org.quartz.spi.jobstore.JobStore;
import org.quartz.spi.jobstore.JobStoreTX;
import org.quartz.spi.operators.DatabaseOperator;
import org.quartz.spi.operators.MaprunValueSerializingOperator;
import org.quartz.spi.operator.OperatorException;
import org.quartz.spi.schedulerListener.SchedulerListenerSupport;
import org.quartz.spi.stats.StatProvider;
import org.quartz.spi.triggers.SimpleTrigger;
import org.quartz.spi.util.FileProvider;
import org.quartz.spi.util.ThreadPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DataArchiveRollbackJobTest {

 @MockBean
 private ScheduleTaskRepository scheduleTaskRepository;

 @Autowired
 private SchedulerFactoryBean schedulerFactoryBean;

 private DataArchiveRollbackJob dataArchiveRollbackJob;

 @Before
 public void setup() {
 AnnotatedJobFactory jobFactory = new AnnotatedJobFactory();
 schedulerFactoryBean.setJobFactory(jobFactory);
 schedulerFactoryBean.afterPropertiesSet();
 JobStore jobStore = new JobStoreTX();
 jobStore.setDriverDelegateClass(mock(DatabaseOperator.class));
 jobStore.setOperatorClass(MaprunValueSerializingOperator.class);
 schedulerFactoryBean.setJobStore(jobStore);
 schedulerFactoryBean.setThreadPool(mock(ThreadPoolUtil.class));
 schedulerFactoryBean.setFileProvider(mock(FileProvider.class));
 schedulerFactoryBean.setStatProviderClass(mock(StatProvider.class));
 schedulerFactoryBean.setSchedulerListeners(Collections.singletonList(mock(SchedulerListenerSupport.class)));
 schedulerFactoryBean.setPlugins(Collections.emptyList());
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
 }

 @Test
 public void testExecute_NormalCase() throws JobExecutionException {
 JobExecutionContext context = mock(JobExecutionContext.class);
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setJobName("DataArchiveRollbackJob");
 taskEntity.setJobGroup("group1");
 taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(2L);
 when(context.getResult()).thenReturn(taskEntity);
 when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(taskEntity));
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenReturn(true);

 dataArchiveRollbackJob.execute(context);

 verify(scheduleTaskRepository, times(1)).findById(2L);
 verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.FAILED);
 }

 @Test
 public void testExecute_DataArchiveTaskNotFound() throws JobExecutionException {
 JobExecutionContext context = mock(JobExecutionContext.class);
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setJobName("DataArchiveRollbackJob");
 taskEntity.setJobGroup("group1");
 taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(2L);
 when(context.getResult()).thenReturn(taskEntity);
 when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.empty());

 dataArchiveRollbackJob.execute(context);

 verify(scheduleTaskRepository, times(1)).findById(2L);
 }

 @Test
 public void testExecute_PrepareTasksForRollback() throws JobExecutionException {
 JobExecutionContext context = mock(JobExecutionContext.class);
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setJobName("DataArchiveRollbackJob");
 taskEntity.setJobGroup("group1");
 taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(2L);
 List<DlmTask> taskUnits = new ArrayList<>();
 DlmTask taskUnit1 = new DlmTask();
 taskUnit1.setSourceDatabaseId(1L);
 taskUnit1.setTargetDatabaseId(2L);
 taskUnit1.setJobType(JobType.ROLLBACK);
 taskUnit1.setStatus(TaskStatus.PREPARING);
 DlmTask taskUnit2 = new DlmTask();
 taskUnit2.setSourceDatabaseId(3L);
 taskUnit2.setTargetDatabaseId(4L);
 taskUnit2.setJobType(JobType.ROLLBACK);
 taskUnit2.setStatus(TaskStatus.DONE);
 taskUnits.add(taskUnit1);
 taskUnits.add(taskUnit2);
 when(context.getResult()).thenReturn(taskEntity);
 when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(taskEntity));
 when(scheduleTaskRepository.updateStatusById(1L, TaskStatus.FAILED)).thenReturn(true);
 when(JsonUtils.fromJson(taskEntity.getResultJson(), new TypeReference<List<DlmTask>>() {})).thenReturn(taskUnits);

 dataArchiveRollbackJob.execute(context);

 verify(scheduleTaskRepository, times(1)).findById(2L);
 }

 @Test
 public void testExecute_Exception() {
 JobExecutionContext context = mock(JobExecutionContext.class);
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId(1L);
 taskEntity.setJobName("DataArchiveRollbackJob");
 taskEntity.setJobGroup("group1");
 taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
 DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
 rollbackParameters.setDataArchiveTaskId(2L);
 when(context.getResult()).thenReturn(taskEntity);
 when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(taskEntity));
 doThrow(new RuntimeException()).when(dataArchiveRollbackJob).executeTask(1L, anyList());

 try {
 dataArchiveRollbackJob.execute(context);
 fail("Expected JobExecutionException");
 } catch (JobExecutionException e) {
 // expected
 }
 }
}