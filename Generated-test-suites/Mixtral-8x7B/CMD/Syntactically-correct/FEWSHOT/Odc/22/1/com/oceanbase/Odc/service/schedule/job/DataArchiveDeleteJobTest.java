import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.QuartzJobFactory;

public class DataArchiveDeleteJobTest {

private DataArchiveDeleteJob dataArchiveDeleteJob;
private ScheduleTaskEntity scheduleTaskEntity;
private DataArchiveClearParameters dataArchiveClearParameters;
private List<DlmTask> taskUnits;
private JobExecutionContext jobExecutionContext;
private QuartzJobFactory quartzJobFactory = mock(QuartzJobFactory.class);
private QuartzJobBean quartzJobBean = mock(QuartzJobBean.class);
private ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);

@Before
public void setup() {
dataArchiveDeleteJob = new DataArchiveDeleteJob();
dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);

scheduleTaskEntity = new ScheduleTaskEntity();
scheduleTaskEntity.setId(1L);
scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
scheduleTaskEntity.setResultJson("[]");
scheduleTaskEntity.setStatus(TaskStatus.DONE);

dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId(2L);

taskUnits = new ArrayList<>();
DlmTask dlmTask1 = new DlmTask();
dlmTask1.setId(1L);
dlmTask1.setJobName("jobName");
dlmTask1.setJobGroup("jobGroup");
taskUnits.add(dlmTask1);

jobExecutionContext = mock(JobExecutionContext.class);
when(jobExecutionContext.getMergedJobDataMap().get("scheduleTaskEntity")).thenReturn(scheduleTaskEntity);
}

@Test
public void testExecute() throws Exception {
dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1))
.findById(dataArchiveClearParameters.getDataArchiveTaskId());
verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
}

@Test
public void testExecuteWhenDataArchiveTaskIsAbsent() throws Exception {
when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).findById(dataArchiveClearParameters.getDataArchiveTaskId());
verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
}

@Test
public void testExecuteWhenDataArchiveTaskIsNotDone() throws Exception {
scheduleTaskEntity.setStatus(TaskStatus.RUNNING);

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).findById(dataArchiveClearParameters.getDataArchiveTaskId());
verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
}

@Test
public void testExecuteWhenDataArchiveTaskIsDone() throws Exception {
scheduleTaskEntity.setStatus(TaskStatus.DONE);

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1))
.findById(dataArchiveClearParameters.getDataArchiveTaskId());
verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
}

@Test
public void testExecuteWhenDataArchiveTaskIsNotFinished() throws Exception {
scheduleTaskEntity.setStatus(TaskStatus.PREPARING);

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1))
.findById(dataArchiveClearParameters.getDataArchiveTaskId());
verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
}

@Test
public void testExecuteWhenDataArchiveTaskIsFinished() throws Exception {
scheduleTaskEntity.setStatus(TaskStatus.DONE);

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1))
.findById(dataArchiveClearParameters.getDataArchiveTaskId());
verify(scheduleTaskRepository, times(1)).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
}

@Test
public void testExecuteTask() throws Exception {
dataArchiveDeleteJob.executeTask(1L, taskUnits);

verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
}

@Test
public void testGetTaskStatus() throws Exception {
TaskStatus taskStatus = dataArchiveDeleteJob.getTaskStatus(taskUnits);

assertNotNull(taskStatus);
}

@Test
public void testGenerateHistoryJobId() throws Exception {
String historyJobId = DlmJobIdUtil.generateHistoryJobId("jobName", "jobGroup", 1L, 2);

assertNotNull(historyJobId);
}
}