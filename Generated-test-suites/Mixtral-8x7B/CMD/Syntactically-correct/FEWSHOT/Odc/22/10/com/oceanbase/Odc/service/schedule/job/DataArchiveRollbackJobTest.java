import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.utils.AbstractDlmJob;
import com.oceanbase.tools.migrator.common.enums.JobType;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.Result;
import org.quartz.TriggerResult;

public class DataArchiveRollbackJobTest {

private DataArchiveRollbackJob dataArchiveRollbackJob;
private ScheduleTaskRepository scheduleTaskRepository;
private JobExecutionContext jobExecutionContext;
private ScheduleTaskEntity scheduleTaskEntity;
private DataArchiveRollbackParameters rollbackParameters;
private List<DlmTask> taskUnits;

@Before
public void setup() {
scheduleTaskRepository = mock(ScheduleTaskRepository.class);
dataArchiveRollbackJob = spy(new DataArchiveRollbackJob() {
@Override
public void execute(JobExecutionContext context) {
super.execute(context);
jobThread = Thread.currentThread();
}
});
doReturn(scheduleTaskRepository).when(dataArchiveRollbackJob).getScheduleTaskRepository();

jobExecutionContext = mock(JobExecutionContext.class);
scheduleTaskEntity = mock(ScheduleTaskEntity.class);
rollbackParameters = mock(DataArchiveRollbackParameters.class);
taskUnits = Arrays.asList(mock(DlmTask.class), mock(DlmTask.class));

when(scheduleTaskEntity.getParametersJson()).thenReturn("{\"dataArchiveTaskId\": 1}");
when(rollbackParameters.getDataArchiveTaskId()).thenReturn(1L);
when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);

when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));

String json = JsonUtils.toJson(taskUnits);
when(scheduleTaskEntity.getResultJson()).thenReturn(json);
TypeReference<List<DlmTask>> typeReference = new TypeReference<List<DlmTask>>() {};
when(JsonUtils.fromJson(json, typeReference)).thenReturn(taskUnits);

DlmTask dlmTask = taskUnits.get(0);
when(dlmTask.getSourceDatabaseId()).thenReturn(1L);
when(dlmTask.getTargetDatabaseId()).thenReturn(2L);
when(dlmTask.getId()).thenReturn(3L);
when(dlmTask.getJobType()).thenReturn(JobType.MIGRATE);
when(dlmTask.getStatus()).thenReturn(TaskStatus.PREPARING);

dlmTask = taskUnits.get(1);
when(dlmTask.getSourceDatabaseId()).thenReturn(3L);
when(dlmTask.getTargetDatabaseId()).thenReturn(1L);
when(dlmTask.getId()).thenReturn(4L);
when(dlmTask.getJobType()).thenReturn(JobType.FINALIZE);
when(dlmTask.getStatus()).thenReturn(TaskStatus.PREPARING);
}

@Test
public void testExecute() throws Exception {
dataArchiveRollbackJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).findById(anyLong());
verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any(TaskStatus.class));
}

@Test
public void testExecuteTask() throws Exception {
doNothing().when(dataArchiveRollbackJob).executeTask(anyLong(), any());

dataArchiveRollbackJob.executeTask(1L, taskUnits);
verify(dataArchiveRollbackJob, times(1)).executeTask(anyLong(), any());
}

@Test
public void testGetTaskStatus() {
TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);
assertNotNull(taskStatus);
}
}