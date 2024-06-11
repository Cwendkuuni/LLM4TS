import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Result;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class DataArchiveDeleteJobTest {

@MockBean
private ScheduleTaskRepository scheduleTaskRepository;

@MockBean
private JsonUtils jsonUtils;

@MockBean
private DlmJobIdUtil dlmJobIdUtil;

@Autowired
private SchedulerFactoryBean schedulerFactoryBean;

private DataArchiveDeleteJob dataArchiveDeleteJob;

@Before
public void setUp() {
JobFactory jobFactory = schedulerFactoryBean.getJobFactory();
AutowireCapableBeanFactory autowireCapableBeanFactory = jobFactory.getObject().getScheduler().getBeanFactory();
autowireCapableBeanFactory.autowireBean(dataArchiveDeleteJob = new DataArchiveDeleteJob());
}

@Test
public void testExecute_scheduleTaskRepositoryFindByIdReturnsEmptyOptional_updatesTaskStatusToFailed()
throws Exception {
JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);
ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);
DataArchiveClearParameters dataArchiveClearParameters = mock(DataArchiveClearParameters.class);
when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
when(scheduleTaskEntity.getParametersJson()).thenReturn("{}");
when(jsonUtils.fromJson(anyString(), eq(DataArchiveClearParameters.class)))
.thenReturn(dataArchiveClearParameters);
when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());
dataArchiveDeleteJob.execute(jobExecutionContext);
verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
}

@Test
public void testExecute_scheduleTaskRepositoryFindByIdReturnsPresentOptionalButStatusIsNotDone_updatesTaskStatusToFailed()
throws Exception {
JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);
ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);
DataArchiveClearParameters dataArchiveClearParameters = mock(DataArchiveClearParameters.class);
when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
when(scheduleTaskEntity.getParametersJson()).thenReturn("{}");
when(jsonUtils.fromJson(anyString(), eq(DataArchiveClearParameters.class)))
.thenReturn(dataArchiveClearParameters);
when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.RUNNING);
dataArchiveDeleteJob.execute(jobExecutionContext);
verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
}

@Test
public void testExecute_scheduleTaskRepositoryFindByIdReturnsPresentOptionalAndStatusIsDone_setsTasksForClearAndUpdatesTaskStatus()
throws Exception {
JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);
ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);
DataArchiveClearParameters dataArchiveClearParameters = mock(DataArchiveClearParameters.class);
List<DlmTask> dlmTasks = mock(List.class);
when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
when(scheduleTaskEntity.getParametersJson()).thenReturn("{}");
when(jsonUtils.fromJson(anyString(), eq(DataArchiveClearParameters.class)))
.thenReturn(dataArchiveClearParameters);
when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.DONE);
when(jsonUtils.fromJson(anyString(), new TypeReference<List<DlmTask>>() {})).thenReturn(dlmTasks);
when(dlmJobIdUtil.generateHistoryJobId(anyString(), anyString(), anyLong(), anyInt()))
.thenReturn("historyJobId");
doNothing().when(dataArchiveDeleteJob).executeTask(anyLong(), anyList());
when(dataArchiveDeleteJob.getTaskStatus(dlmTasks)).thenReturn(TaskStatus.SUCCESS);
dataArchiveDeleteJob.execute(jobExecutionContext);
verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.SUCCESS));
verify(dataArchiveDeleteJob, times(1)).executeTask(anyLong(), anyList());
verify(dataArchiveDeleteJob, times(1)).getTaskStatus(dlmTasks);
}

@Test(expected = JobExecutionException.class)
public void testExecute_jsonUtilsFromJsonThrowsException_throwsJobExecutionException()
throws Exception {
JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);
ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);
DataArchiveClearParameters dataArchiveClearParameters = mock(DataArchiveClearParameters.class);
when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
when(scheduleTaskEntity.getParametersJson()).thenReturn("{}");
when(jsonUtils.fromJson(anyString(), eq(DataArchiveClearParameters.class)))
.thenThrow(new RuntimeException());
dataArchiveDeleteJob.execute(jobExecutionContext);
}

}