import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.impl.JobExecutionContextImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataArchiveRollbackJobTest {

private static final Logger log = LoggerFactory.getLogger(DataArchiveRollbackJobTest.class);
private JobExecutionContext context;
private DataArchiveRollbackJob job;
private ScheduleTaskEntity taskEntity;
private DataArchiveRollbackParameters rollbackParameters;
private List<DlmTask> taskUnits;

@Before
public void setup() {
context = new JobExecutionContextImpl();
job = new DataArchiveRollbackJob() {
@Override
protected Logger getLogger() {
return log;
}
};
taskEntity = mock(ScheduleTaskEntity.class);
rollbackParameters = new DataArchiveRollbackParameters();
rollbackParameters.setDataArchiveTaskId(1L);
taskUnits = Arrays.asList(mock(DlmTask.class), mock(DlmTask.class));
}

@Test
public void testExecuteWithEmptyTaskEntity() {
job.execute(context);
}

@Test
public void testExecuteWithNullRollbackParameters() {
when(taskEntity.getParametersJson()).thenReturn(null);
context.setMergedJobDataMap(job.createJobDataMap(taskEntity));
job.execute(context);
}

@Test
public void testExecuteWithNullTaskUnitList() {
when(taskEntity.getParametersJson())
.thenReturn(JsonUtils.toJson(rollbackParameters));
when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
.thenReturn(Optional.of(taskEntity));
when(taskEntity.getResultJson()).thenReturn(null);
context.setMergedJobDataMap(job.createJobDataMap(taskEntity));
job.execute(context);
}

@Test
public void testExecuteWithNonPresentDataArchiveTask() {
when(taskEntity.getParametersJson())
.thenReturn(JsonUtils.toJson(rollbackParameters));
when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
.thenReturn(Optional.empty());
context.setMergedJobDataMap(job.createJobDataMap(taskEntity));
job.execute(context);
}

@Test
public void testExecuteWithSuccess() {
when(taskEntity.getParametersJson())
.thenReturn(JsonUtils.toJson(rollbackParameters));
when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
.thenReturn(Optional.of(taskEntity));
when(taskEntity.getResultJson()).thenReturn(JsonUtils.toJson(taskUnits));
when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.DONE))
.thenReturn(true);
context.setMergedJobDataMap(job.createJobDataMap(taskEntity));
job.execute(context);
}

@Test
public void testExecuteWithFailedRollback() {
when(taskEntity.getParametersJson())
.thenReturn(JsonUtils.toJson(rollbackParameters));
when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
.thenReturn(Optional.of(taskEntity));
when(taskEntity.getResultJson()).thenReturn(JsonUtils.toJson(taskUnits));
when(scheduleTaskRepository.updateStatusById(taskEntity.getId(), TaskStatus.FAILED))
.thenReturn(true);
context.setMergedJobDataMap(job.createJobDataMap(taskEntity));
job.execute(context);
}

@Test
public void testGetTaskStatusWithAllDone() {
assertEquals(TaskStatus.DONE, job.getTaskStatus(taskUnits));
}

@Test
public void testGetTaskStatusWithAllPreparing() {
assertEquals(TaskStatus.PREPARING, job.getTaskStatus(Arrays.asList(taskUnits.get(0))));
}

private class scheduleTaskRepository {
public Optional<ScheduleTaskEntity> findById(Long id) {
return Optional.ofNullable(taskEntity);
}

public boolean updateStatusById(Long id, TaskStatus status) {
return true;
}
}
}