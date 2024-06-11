import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.AbstractDlmJob;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import java.util.List;
import java.util.Optional;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class DataArchiveRollbackJobTest {

@Mock
private ScheduleTaskRepository scheduleTaskRepository;

@InjectMocks
private DataArchiveRollbackJob dataArchiveRollbackJob;

private JobExecutionContext jobExecutionContext;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);
jobExecutionContext = mock(JobExecutionContext.class);
}

@Test
public void testExecute_whenDataArchiveTaskNotFound_shouldUpdateStatusToFailed() {
DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
rollbackParameters.setDataArchiveTaskId(1L);

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(2L);
taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

doReturn(Optional.of(new ScheduleTaskEntity())).when(scheduleTaskRepository).findById(1L);

dataArchiveRollbackJob.execute(jobExecutionContext);

verify(scheduleTaskRepository).updateStatusById(2L, TaskStatus.FAILED);
}

@Test
public void testExecute_whenDataArchiveTaskFound_shouldExecuteTaskAndUpdateStatus() {
DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
rollbackParameters.setDataArchiveTaskId(1L);

ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(2L);
taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

DlmTask dlmTask = new DlmTask();
List<DlmTask> taskUnits = java.util.Collections.singletonList(dlmTask);

doReturn(Optional.of(taskEntity)).when(scheduleTaskRepository).findById(1L);

JobExecutionContext context = spy(jobExecutionContext);
doReturn(taskEntity).when(context).getResult();

dataArchiveRollbackJob.execute(context);

verify(dataArchiveRollbackJob).executeTask(2L, taskUnits);
verify(scheduleTaskRepository).updateStatusById(2L, TaskStatus.FAILED);
}

private class TestAbstractDlmJob extends AbstractDlmJob {
@Override
public void executeTask(Long taskId, List<DlmTask> taskUnits) {
// do nothing
}

@Override
public TaskStatus getTaskStatus(List<DlmTask> taskUnits) {
return TaskStatus.FAILED;
}
}

private String toJson(Object obj) {
return JsonUtils.toJson(obj);
}

private <T> T fromJson(String json, Class<T> clazz) {
return JsonUtils.fromJson(json, clazz);
}

private <T> T fromJson(String json, TypeReference<T> typeReference) {
return JsonUtils.fromJson(json, typeReference);
}
}