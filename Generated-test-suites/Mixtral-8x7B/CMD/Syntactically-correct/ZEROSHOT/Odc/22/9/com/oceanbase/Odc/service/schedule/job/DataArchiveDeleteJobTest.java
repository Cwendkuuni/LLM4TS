import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.quartz.*;
import org.quartz.core.JobDetail;
import org.quartz.core.JobExecutor;
import org.quartz.core. scheduler.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.simpl.RAMJobStore;
import org.quartz.simpl.SimpleThreadPool;
import org.quartz.simpl.SchedulerSignalerImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

private Scheduler scheduler;
private DataArchiveDeleteJob dataArchiveDeleteJob;
private ScheduleTaskEntity taskEntity;
private DataArchiveClearParameters dataArchiveClearParameters;
private ScheduleTaskEntity dataArchiveTask;
private List<DlmTask> taskUnits;

@Before
public void setUp() throws SchedulerException {
SchedulerFactory schedulerFactory = new StdSchedulerFactory();
scheduler = schedulerFactory.getScheduler();
scheduler.setJobStore(new RAMJobStore());
scheduler.start();

dataArchiveDeleteJob = spy(new DataArchiveDeleteJob());

taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setJobName("DataArchiveDeleteJob");
taskEntity.setJobGroup("DEFAULT");
taskEntity.setStatus(TaskStatus.DONE);
taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");

dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId(2L);

dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(2L);
dataArchiveTask.setStatus(TaskStatus.DONE);
dataArchiveTask.setResultJson("[]");

taskUnits = new ArrayList<>();
taskUnits.add(new DlmTask());
taskUnits.get(0).setId(3L);
taskUnits.get(0).setJobType(JobType.DELETE);
taskUnits.get(0).setStatus(TaskStatus.PREPARING);
}

@Test
public void testExecute_DataArchiveTaskNotFound() {
doReturn(Optional.of(dataArchiveTask)).when(dataArchiveDeleteJob).findDataArchiveTaskById(anyLong());
doReturn(Optional.empty()).when(dataArchiveDeleteJob).findDataArchiveTaskById(dataArchiveClearParameters.getDataArchiveTaskId());

dataArchiveDeleteJob.execute(null);

verify(dataArchiveDeleteJob).findDataArchiveTaskById(dataArchiveClearParameters.getDataArchiveTaskId());
verify(scheduleTaskRepository).updateStatusById(eq(taskEntity.getId()), eq(TaskStatus.FAILED));
}

@Test
public void testExecute_DataArchiveTaskNotFinished() {
doReturn(Optional.of(dataArchiveTask)).when(dataArchiveDeleteJob).findDataArchiveTaskById(anyLong());

dataArchiveTask.setStatus(TaskStatus.RUNNING);

dataArchiveDeleteJob.execute(null);

verify(dataArchiveDeleteJob).findDataArchiveTaskById(dataArchiveClearParameters.getDataArchiveTaskId());
verify(scheduleTaskRepository).updateStatusById(eq(taskEntity.getId()), eq(TaskStatus.FAILED));
}

@Test
public void testExecute_ClearTasksSuccess() throws SchedulerException {
doReturn(Optional.of(dataArchiveTask)).when(dataArchiveDeleteJob).findDataArchiveTaskById(anyLong());

dataArchiveTask.setStatus(TaskStatus.DONE);
dataArchiveTask.setResultJson(JsonUtils.toJson(taskUnits));

when(dataArchiveDeleteJob.executeTask(anyLong(), anyList())).thenReturn(true);

dataArchiveDeleteJob.execute(null);

verify(dataArchiveDeleteJob).findDataArchiveTaskById(dataArchiveClearParameters.getDataArchiveTaskId());
verify(dataArchiveDeleteJob).executeTask(taskEntity.getId(), taskUnits);
verify(scheduleTaskRepository).updateStatusById(eq(taskEntity.getId()), any(TaskStatus.class));
}

@Test
public void testExecute_ClearTasksFailed() throws SchedulerException {
doReturn(Optional.of(dataArchiveTask)).when(dataArchiveDeleteJob).findDataArchiveTaskById(anyLong());

dataArchiveTask.setStatus(TaskStatus.DONE);
dataArchiveTask.setResultJson(JsonUtils.toJson(taskUnits));

when(dataArchiveDeleteJob.executeTask(anyLong(), anyList())).thenReturn(false);

dataArchiveDeleteJob.execute(null);

verify(dataArchiveDeleteJob).findDataArchiveTaskById(dataArchiveClearParameters.getDataArchiveTaskId());
verify(dataArchiveDeleteJob).executeTask(taskEntity.getId(), taskUnits);
verify(scheduleTaskRepository).updateStatusById(eq(taskEntity.getId()), eq(TaskStatus.FAILED));
}

private Optional<ScheduleTaskEntity> findDataArchiveTaskById(Long id) {
// Implement the findDataArchiveTaskById method here
return Optional.empty();
}

private boolean executeTask(Long taskId, List<DlmTask> taskUnits) throws SchedulerException {
// Implement the executeTask method here
return false;
}

private TaskStatus getTaskStatus(List<DlmTask> taskUnits) {
// Implement the getTaskStatus method here
return null;
}

}