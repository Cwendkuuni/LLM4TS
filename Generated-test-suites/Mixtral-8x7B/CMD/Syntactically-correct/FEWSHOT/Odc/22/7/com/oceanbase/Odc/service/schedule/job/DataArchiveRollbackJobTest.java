import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.QuartzJobMessageListener;

public class DataArchiveRollbackJobTest {

private DataArchiveRollbackJob dataArchiveRollbackJob;
private JobExecutionContext jobExecutionContext;
private ScheduleTaskEntity taskEntity;
private DataArchiveRollbackParameters rollbackParameters;
private List<DlmTask> taskUnits;
private ScheduleTaskEntity dataArchiveTask;
private QuartzJobMessageListener listener;

@Before
public void setup() {
taskEntity = new ScheduleTaskEntity();
rollbackParameters = new DataArchiveRollbackParameters();
taskUnits = new ArrayList<>();
dataArchiveTask = new ScheduleTaskEntity();

jobExecutionContext = mock(JobExecutionContext.class);
doReturn(taskEntity).when(jobExecutionContext).getResult();

dataArchiveRollbackJob = spy(new DataArchiveRollbackJob());
dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
}

@Test
public void testExecute() throws JobExecutionException {
dataArchiveRollbackJob.execute(jobExecutionContext);
verify(dataArchiveRollbackJob, times(1)).executeTask(anyLong(), any());
}

@Test
public void testFindDataArchiveTaskById_notPresent() {
doReturn(Optional.empty()).when(scheduleTaskRepository).findById(anyLong());

dataArchiveRollbackJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
}

@Test
public void testFindDataArchiveTaskById_present() {
doReturn(Optional.of(dataArchiveTask)).when(scheduleTaskRepository).findById(anyLong());

dataArchiveRollbackJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any());
}

@Test
public void testExecuteTask() {
dataArchiveRollbackJob.executeTask(1L, taskUnits);

taskUnits.forEach(task -> {
assertEquals(1L, task.getSourceDatabaseId());
assertEquals(DlmJobIdUtil.generateHistoryJobId("jobName", "jobGroup", 1L, 1), task.getId());
assertEquals(DlmJobIdUtil.generateHistoryJobId("jobName", "jobGroup", 1L, 1), task.getTargetDatabaseId());
assertEquals(JobType.ROLLBACK, task.getJobType());
});
}

@Test
public void testGetTaskStatus() {
List<DlmTask> taskUnits = new ArrayList<>();
DlmTask task1 = new DlmTask();
task1.setStatus(TaskStatus.PREPARING);
DlmTask task2 = new DlmTask();
task2.setStatus(TaskStatus.DONE);
taskUnits.add(task1);
taskUnits.add(task2);

assertEquals(TaskStatus.DONE, dataArchiveRollbackJob.getTaskStatus(taskUnits));
}

@Test
public void testGetTaskStatus_allPreparing() {
List<DlmTask> taskUnits = new ArrayList<>();
DlmTask task1 = new DlmTask();
task1.setStatus(TaskStatus.PREPARING);
DlmTask task2 = new DlmTask();
task2.setStatus(TaskStatus.PREPARING);
taskUnits.add(task1);
taskUnits.add(task2);

assertEquals(TaskStatus.PREPARING, dataArchiveRollbackJob.getTaskStatus(taskUnits));
}

}

class AbstractDlmJobSpy extends AbstractDlmJob {
@Override
public void executeTask(Long taskId, List<DlmTask> taskUnits) {
super.executeTask(taskId, taskUnits);
}

@Override
public TaskStatus getTaskStatus(List<DlmTask> taskUnits) {
return super.getTaskStatus(taskUnits);
}
}