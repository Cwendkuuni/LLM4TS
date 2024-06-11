import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DataArchiveDeleteJobTest {

@MockBean
private ScheduleTaskRepository scheduleTaskRepository;

@Autowired
private DataArchiveDeleteJob dataArchiveDeleteJob;

@Test
public void testExecute_whenTaskEntityIsNull_thenLogWarningAndUpdateStatusByIdToFailed() throws JobExecutionException {
JobExecutionContext context = mock(JobExecutionContext.class);
dataArchiveDeleteJob.execute(context);
verify(scheduleTaskRepository, times(1)).updateStatusById(any(Long.class), eq(TaskStatus.FAILED));
}

@Test
public void testExecute_whenDataArchiveTaskNotFound_thenLogWarnAndUpdateStatusByIdToFailed() throws JobExecutionException {
JobExecutionContext context = mock(JobExecutionContext.class);
ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
doReturn(Optional.empty()).when(scheduleTaskRepository).findById(any(Long.class));
dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
dataArchiveDeleteJob.execute(context);
verify(scheduleTaskRepository, times(1)).updateStatusById(any(Long.class), eq(TaskStatus.FAILED));
}

@Test
public void testExecute_whenDataArchiveTaskNotDone_thenLogWarnAndUpdateStatusByIdToFailed() throws JobExecutionException {
JobExecutionContext context = mock(JobExecutionContext.class);
ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
doReturn(Optional.of(dataArchiveTask)).when(scheduleTaskRepository).findById(any(Long.class));
doReturn(TaskStatus.RUNNING).when(dataArchiveTask).getStatus();
dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
dataArchiveDeleteJob.execute(context);
verify(scheduleTaskRepository, times(1)).updateStatusById(any(Long.class), eq(TaskStatus.FAILED));
}

@Test
public void testExecute_whenDataArchiveTaskFoundAndDone_thenExecuteTaskAndUpdateStatusById() throws JobExecutionException {
JobExecutionContext context = mock(JobExecutionContext.class);
ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId(1L);
String parametersJson = JsonUtils.toJson(dataArchiveClearParameters);
doReturn(Optional.of(dataArchiveTask)).when(scheduleTaskRepository).findById(any(Long.class));
doReturn(TaskStatus.DONE).when(dataArchiveTask).getStatus();
doReturn(parametersJson).when(taskEntity).getParametersJson();
DlmTask dlmTask1 = new DlmTask();
DlmTask dlmTask2 = new DlmTask();
List<DlmTask> taskUnits = new ArrayList<>();
taskUnits.add(dlmTask1);
taskUnits.add(dlmTask2);
doReturn(taskUnits).when(JsonUtils.class).fromJson(anyString(), eq(new TypeReference<List<DlmTask>>() {}));
DataArchiveDeleteJob spyDataArchiveDeleteJob = spy(dataArchiveDeleteJob);
spyDataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);
spyDataArchiveDeleteJob.execute(context);
verify(spyDataArchiveDeleteJob, times(1)).executeTask(anyLong(), anyList());
verify(scheduleTaskRepository, times(1)).updateStatusById(any(Long.class), any(TaskStatus.class));
}
}