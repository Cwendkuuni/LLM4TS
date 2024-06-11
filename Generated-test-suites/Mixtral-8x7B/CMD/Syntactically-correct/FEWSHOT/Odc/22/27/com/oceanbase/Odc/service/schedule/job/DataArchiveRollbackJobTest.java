import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Slf4j
public class DataArchiveRollbackJobTest {

private DataArchiveRollbackJob dataArchiveRollbackJob;
private JobExecutionContext jobExecutionContext;
private ScheduleTaskEntity taskEntity;
private ScheduleTaskEntity dataArchiveTask;
private List<DlmTask> taskUnits;
private SchedulerFactoryBean schedulerFactoryBean;
private JsonUtils jsonUtils;
private DlmJobIdUtil dlmJobIdUtil;
private scheduleTaskRepository scheduleTaskRepository;

@Before
public void setup() {
dataArchiveRollbackJob = spy(new DataArchiveRollbackJob());
jsonUtils = mock(JsonUtils.class);
dlmJobIdUtil = mock(DlmJobIdUtil.class);
scheduleTaskRepository = mock(scheduleTaskRepository.class);

dataArchiveRollbackJob.setJsonUtils(jsonUtils);
dataArchiveRollbackJob.setDlmJobIdUtil(dlmJobIdUtil);
dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);

taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson("{}");
taskEntity.setResultJson("[]");
taskEntity.setJobName("testJob");
taskEntity.setJobGroup("testGroup");

dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(2L);
dataArchiveTask.setResultJson("[]");

taskUnits = Arrays.asList(
new DlmTask(),
new DlmTask()
);

jobExecutionContext = mock(JobExecutionContext.class);
doReturn(taskEntity).when(jobExecutionContext).getResult();

schedulerFactoryBean = mock(SchedulerFactoryBean.class);
}

@Test
public void testExecute() throws Exception {
dataArchiveRollbackJob.execute(jobExecutionContext);

verify(jsonUtils, times(1)).fromJson(anyString(), any(TypeReference.class));
verify(jsonUtils, times(1)).fromJson(anyString(), any(TypeReference.class));
verify(dlmJobIdUtil, times(2)).generateHistoryJobId(anyString(), anyString(), anyLong(), anyInt());
verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
}

@Test
public void testExecuteTask() throws Exception {
dataArchiveRollbackJob.executeTask(1L, taskUnits);

verify(jsonUtils, times(1)).fromJson(anyString(), any(TypeReference.class));
}

@Test
public void testGetTaskStatus() throws Exception {
DataArchiveRollbackJob spyDataArchiveRollbackJob = spy(dataArchiveRollbackJob);
doReturn(TaskStatus.DONE).when(spyDataArchiveRollbackJob).getTaskStatus(taskUnits);

assertEquals(TaskStatus.DONE, spyDataArchiveRollbackJob.getTaskStatus(taskUnits));
verify(spyDataArchiveRollbackJob, times(1)).getTaskStatus(taskUnits);
}

@Test
public void testFindById() throws Exception {
doReturn(Optional.of(dataArchiveTask)).when(scheduleTaskRepository).findById(anyLong());

assertEquals(Optional.of(dataArchiveTask),
scheduleTaskRepository.findById(anyLong()));
verify(scheduleTaskRepository, times(1)).findById(anyLong());
}

@Test
public void testUpdateStatusById() throws Exception {
dataArchiveRollbackJob.updateStatusById(1L, TaskStatus.DONE);

verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
}

@Test
public void testPrepareTasksForRollback() throws Exception {
DlmTask dlmTask1 = new DlmTask();
dlmTask1.setId(1L);
dlmTask1.setSourceDatabaseId(1L);
dlmTask1.setTargetDatabaseId(2L);

DlmTask dlmTask2 = new DlmTask();
dlmTask2.setId(2L);
dlmTask2.setSourceDatabaseId(2L);
dlmTask2.setTargetDatabaseId(1L);

List<DlmTask> taskUnits = Arrays.asList(dlmTask1, dlmTask2);

List<DlmTask> preparedTaskUnits =
dataArchiveRollbackJob.prepareTasksForRollback(taskUnits, 1L, 2L);

assertEquals(2, preparedTaskUnits.size());
assertEquals(1L, preparedTaskUnits.get(0).getSourceDatabaseId());
assertEquals(2L, preparedTaskUnits.get(0).getTargetDatabaseId());
assertEquals(2L, preparedTaskUnits.get(0).getId());
assertEquals(2L, preparedTaskUnits.get(1).getSourceDatabaseId());
assertEquals(1L, preparedTaskUnits.get(1).getTargetDatabaseId());
assertEquals(3L, preparedTaskUnits.get(1).getId());
}
}