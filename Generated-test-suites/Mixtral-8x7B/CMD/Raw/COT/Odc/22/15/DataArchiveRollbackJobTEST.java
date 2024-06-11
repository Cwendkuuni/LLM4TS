import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public class DataArchiveRollbackJobTest {

private static final Logger log = LoggerFactory.getLogger(DataArchiveRollbackJobTest.class);

private DataArchiveRollbackJob dataArchiveRollbackJob;

private ScheduleTaskRepository scheduleTaskRepository;

private JobExecutionContext jobExecutionContext;

private ScheduleTaskEntity taskEntity;

private DataArchiveRollbackParameters rollbackParameters;

private List<DlmTask> taskUnits;

@Before
public void setup() {
taskUnits = new ArrayList<>();
taskEntity = new ScheduleTaskEntity();
rollbackParameters = new DataArchiveRollbackParameters();
scheduleTaskRepository = mock(ScheduleTaskRepository.class);
jobExecutionContext = mock(JobExecutionContext.class);
dataArchiveRollbackJob = new DataArchiveRollbackJob() {
@Override
protected ScheduleTaskRepository scheduleTaskRepository() {
return scheduleTaskRepository;
}

@Override
protected Logger log() {
return log;
}
};
}

@Test(expected = JobExecutionException.class)
public void testExecute_dataArchiveTaskNotFound() throws Exception {
rollbackParameters.setDataArchiveTaskId(1L);

when(jobExecutionContext.getResult()).thenReturn(taskEntity);
when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

dataArchiveRollbackJob.execute(jobExecutionContext);
}

@Test
public void testExecute_dataArchiveTaskFound() throws Exception {
String parametersJson = "{\"dataArchiveTaskId\": 1}";
rollbackParameters.setDataArchiveTaskId(1L);
taskEntity.setParametersJson(parametersJson);

String resultJson = "[\n" +
" {\n" +
" \"databaseName\": \"db1\",\n" +
" \"sql\": \"CREATE TABLE t1 (c1 INT)\",\n" +
" \"sourceDatabaseId\": 1,\n" +
" \"targetDatabaseId\": 2,\n" +
" \"jobType\": \"MIGRATE\",\n" +
" \"status\": \"PREPARING\",\n" +
" \"id\": 1,\n" +
" \"startTime\": 0,\n" +
" \"endTime\": 0\n" +
" }\n" +
"]";
taskEntity.setResultJson(resultJson);

DlmTask taskUnit = new DlmTask();
taskUnit.setId(1L);
taskUnit.setDatabaseName("db1");
taskUnit.setSql("CREATE TABLE t1 (c1 INT)");
taskUnit.setSourceDatabaseId(1L);
taskUnit.setTargetDatabaseId(2L);
taskUnit.setJobType(JobType.MIGRATE);
taskUnit.setStatus(TaskStatus.PREPARING);
taskUnits.add(taskUnit);

when(jobExecutionContext.getResult()).thenReturn(taskEntity);
when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));

dataArchiveRollbackJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
}

@Test
public void testExecute_taskUnitsPrepared() throws Exception {
String parametersJson = "{\"dataArchiveTaskId\": 1}";
rollbackParameters.setDataArchiveTaskId(1L);
taskEntity.setParametersJson(parametersJson);

String resultJson = "[\n" +
" {\n" +
" \"databaseName\": \"db1\",\n" +
" \"sql\": \"CREATE TABLE t1 (c1 INT)\",\n" +
" \"sourceDatabaseId\": 1,\n" +
" \"targetDatabaseId\": 2,\n" +
" \"jobType\": \"MIGRATE\",\n" +
" \"status\": \"DONE\",\n" +
" \"id\": 1,\n" +
" \"startTime\": 0,\n" +
" \"endTime\": 0\n" +
" }\n" +
"]";
taskEntity.setResultJson(resultJson);

DlmTask taskUnit = new DlmTask();
taskUnit.setId(DlmJobIdUtil.generateHistoryJobId("testJobName", "testJobGroup", 1L, 1));
taskUnit.setDatabaseName("db1");
taskUnit.setSql("CREATE TABLE t1 (c1 INT)");
taskUnit.setSourceDatabaseId(2L);
taskUnit.setTargetDatabaseId(1L);
taskUnit.setJobType(JobType.ROLLBACK);
taskUnit.setStatus(TaskStatus.DONE);
taskUnits.add(taskUnit);

when(jobExecutionContext.getResult()).thenReturn(taskEntity);
when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));

dataArchiveRollbackJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).updateStatusById(taskEntity.getId(), TaskStatus.DONE);
}
}