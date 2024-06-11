import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.simpl.RemotableRamJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {

private static final Logger log = LoggerFactory.getLogger(DataArchiveRollbackJobTest.class);

private DataArchiveRollbackJob dataArchiveRollbackJob;
private JobExecutionContext jobExecutionContext;
private ScheduleTaskEntity scheduleTaskEntity;
private DataArchiveRollbackParameters dataArchiveRollbackParameters;
private List<DlmTask> dlmTasks;
private Optional<ScheduleTaskEntity> scheduleTaskEntityOptional;
private ScheduleTaskRepository scheduleTaskRepository;

@Before
public void setUp() {
dataArchiveRollbackJob = new DataArchiveRollbackJob();
jobExecutionContext = mock(JobExecutionContext.class);
scheduleTaskEntity = mock(ScheduleTaskEntity.class);
dataArchiveRollbackParameters = new DataArchiveRollbackParameters();
dlmTasks = new ArrayList<>();
scheduleTaskEntityOptional = Optional.of(scheduleTaskEntity);
scheduleTaskRepository = mock(ScheduleTaskRepository.class);
}

@Test
public void testExecute() {
// Setup mock data
String parametersJson = "{\"dataArchiveTaskId\": 123}";
dataArchiveRollbackParameters.setDataArchiveTaskId(123L);
scheduleTaskEntity.setParametersJson(parametersJson);
scheduleTaskEntity.setResultJson("[]");
dlmTasks.add(new DlmTask());
when(scheduleTaskRepository.findById(any())).thenReturn(scheduleTaskEntityOptional);
when(scheduleTaskRepository.updateStatusById(any(), any())).thenReturn(null);

// Execute method under test
dataArchiveRollbackJob.execute(jobExecutionContext);

// Assert results
assertEquals(TaskStatus.DONE, scheduleTaskEntity.getStatus());
assertNotNull(dlmTasks.get(0).getId());
assertNotNull(dlmTasks.get(0).getSourceDatabaseId());
assertNotNull(dlmTasks.get(0).getTargetDatabaseId());
assertEquals(JobType.ROLLBACK, dlmTasks.get(0).getJobType());
}

@Test
public void testExecuteWhenDataArchiveTaskNotFound() {
// Setup mock data
when(scheduleTaskRepository.findById(any())).thenReturn(Optional.empty());

// Execute method under test
dataArchiveRollbackJob.execute(jobExecutionContext);

// Assert results
assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
}

@Test
public void testExecuteTask() {
// Execute method under test
dataArchiveRollbackJob.executeTask(123L, dlmTasks);

// Assert results
assertEquals(123L, dlmTasks.get(0).getTaskId());
}

@Test
public void testGetTaskStatus() {
// Setup mock data
DlmTask task1 = new DlmTask();
task1.setStatus(TaskStatus.PREPARING);
DlmTask task2 = new DlmTask();
task2.setStatus(TaskStatus.DONE);
dlmTasks.add(task1);
dlmTasks.add(task2);

// Execute method under test
TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(dlmTasks);

// Assert results
assertEquals(TaskStatus.DONE, taskStatus);
}
}

class ScheduleTaskRepository {

public Optional<ScheduleTaskEntity> findById(Long id) {
return scheduleTaskEntityOptional;
}

public ScheduleTaskEntity updateStatusById(Long id, TaskStatus status) {
return scheduleTaskEntity;
}
}

class AbstractDlmJob {

public void executeTask(Long id, List<DlmTask> dlmTasks) {
// Implementation not needed for this test
}

public TaskStatus getTaskStatus(List<DlmTask> dlmTasks) {
// Implementation not needed for this test
return TaskStatus.DONE;
}
}

class RemotableRamJob extends Job {
}

class ScheduleTaskEntity extends RemotableRamJob {

private Long id;
private TaskStatus status;
private String parametersJson;
private String resultJson;

public Long getId() {
return id;
}

public void setId(Long id) {
this.id = id;
}

public TaskStatus getStatus() {
return status;
}

public void setStatus(TaskStatus status) {
this.status = status;
}

public String getParametersJson() {
return parametersJson;
}

public void setParametersJson(String parametersJson) {
this.parametersJson = parametersJson;
}

public String getResultJson() {
return resultJson;
}

public void setResultJson(String resultJson) {
this.resultJson = resultJson;
}
}

class DataArchiveRollbackParameters {

private Long dataArchiveTaskId;

public Long getDataArchiveTaskId() {
return dataArchiveTaskId;
}

public void setDataArchiveTaskId(Long dataArchiveTaskId) {
this.dataArchiveTaskId = dataArchiveTaskId;
}
}

class DlmTask extends RemotableRamJob {

private Long id;
private Long sourceDatabaseId;
private Long targetDatabaseId;
private JobType jobType;
private TaskStatus status;

public Long getId() {
return id;
}

public void setId(Long id) {
this.id = id;
}

public Long getSourceDatabaseId() {
return sourceDatabaseId;
}

public void setSourceDatabaseId(Long sourceDatabaseId) {
this.sourceDatabaseId = sourceDatabaseId;
}

public Long getTargetDatabaseId() {
return targetDatabaseId;
}

public void setTargetDatabaseId(Long targetDatabaseId) {
this.targetDatabaseId = targetDatabaseId;
}

public JobType getJobType() {
return jobType;
}

public void setJobType(JobType jobType) {
this.jobType = jobType;
}

public TaskStatus getStatus() {
return status;
}

public void setStatus(TaskStatus status) {
this.status = status;
}
}

class TaskStatus {

public static TaskStatus PREPARING = null;
public static TaskStatus DONE = null;
public static TaskStatus FAILED = null;
}

class JobType {

public static JobType ROLLBACK = null;
}

class JsonUtils {

public static String fromJson(String json, TypeReference valueTypeRef) {
return "";
}

public static <T> T fromJson(String json, Class<T> valueType) {
return null;
}
}

class ScheduleTaskRepository {

}