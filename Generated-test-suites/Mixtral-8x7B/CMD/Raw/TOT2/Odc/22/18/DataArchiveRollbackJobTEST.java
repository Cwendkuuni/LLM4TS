import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

@Slf4j
public class DataArchiveRollbackJobTest {

 private static final String JOB_NAME = "dataArchiveRollbackJob";
 private static final String JOB_GROUP = "dataArchiveRollbackGroup";
 private static final String DATA_ARCHIVE_TASK_ID = "12345";
 private static final String TASK_ENTITY_JSON = "{\"id\":\"123\", \"name\":\"test\", \"parametersJson\":\"{\\\"dataArchiveTaskId\\\":\\\"12345\\\"}\", \"resultJson\":\"[]\", \"status\":\"PREPARING\", \"type\":\"DATA_ARCHIVE_ROLLBACK\", \"createdAt\": 1687702800000, \"updatedAt\": 1687702800000}";
 private static final String DATA_ARCHIVE_ROLLBACK_PARAMETERS_JSON = "{\"dataArchiveTaskId\":\"12345\"}";
 private static final String DATA_ARCHIVE_TASK_JSON = "[{\"id\":\"678\", \"sourceDatabaseId\":1, \"targetDatabaseId\":2, \"jobType\":\"MIGRATION\", \"status\":\"PREPARING\", \"createdAt\": 1687702800000, \"updatedAt\": 1687702800000}]";

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private JobExecutionContext jobExecutionContext;
 private ScheduleTaskEntity scheduleTaskEntity;
 private List<DlmTask> taskUnits;

 @Before
 public void setUp() throws Exception {
 dataArchiveRollbackJob = new DataArchiveRollbackJob();

 // Initialize jobExecutionContext
 ScheduleTaskEntity taskEntity = JsonUtils.fromJson(TASK_ENTITY_JSON, ScheduleTaskEntity.class);
 DataArchiveRollbackParameters rollbackParameters = JsonUtils.fromJson(DATA_ARCHIVE_ROLLBACK_PARAMETERS_JSON,
 DataArchiveRollbackParameters.class);

 scheduleTaskEntity = taskEntity;

 taskUnits = JsonUtils.fromJson(DATA_ARCHIVE_TASK_JSON, new TypeReference<List<DlmTask>>() {
 });

 JobExecutionContext context = newJob(DataArchiveRollbackJob.class)
 .withIdentity(JobKey.jobKey(JOB_NAME, JOB_GROUP))
 .build();

 context.setMergedJobDataMap(taskEntity.toJobDataMap());

 jobExecutionContext = context;
 }

 @Test
 public void testExecuteHappyPath() throws Exception {
 // Given
 jobExecutionContext.setResult(scheduleTaskEntity);

 // When
 dataArchiveRollbackJob.execute(jobExecutionContext);

 // Then
 assertEquals(TaskStatus.DONE, scheduleTaskEntity.getStatus());
 }

 @Test
 public void testExecuteDataArchiveTaskNotFound() throws Exception {
 // Given
 rollbackParameters.setDataArchiveTaskId("notFound");
 ScheduleTaskEntity notFoundTaskEntity = JsonUtils.fromJson(TASK_ENTITY_JSON, ScheduleTaskEntity.class);
 notFoundTaskEntity.setId("notFoundId");
 notFoundTaskEntity.setParametersJson("{\"dataArchiveTaskId\":\"notFound\"}");

 jobExecutionContext.setResult(notFoundTaskEntity);

 // When
 dataArchiveRollbackJob.execute(jobExecutionContext);

 // Then
 assertEquals(TaskStatus.FAILED, notFoundTaskEntity.getStatus());
 }

 @Test
 public void testExecuteNoTaskUnits() throws Exception {
 // Given
 List<DlmTask> emptyTaskUnits = new ArrayList<>();
 taskUnits = emptyTaskUnits;

 ScheduleTaskEntity emptyTaskEntity = JsonUtils.fromJson(TASK_ENTITY_JSON, ScheduleTaskEntity.class);
 emptyTaskEntity.setResultJson("[]");

 jobExecutionContext.setResult(emptyTaskEntity);

 // When
 dataArchiveRollbackJob.execute(jobExecutionContext);

 // Then
 assertEquals(TaskStatus.FAILED, emptyTaskEntity.getStatus());
 }

 @Test
 public void testExecuteExceptionInExecuteTask() throws Exception {
 // Given
 taskUnits = null;

 ScheduleTaskEntity exceptionTaskEntity = JsonUtils.fromJson(TASK_ENTITY_JSON, ScheduleTaskEntity.class);
 exceptionTaskEntity.setId("exceptionId");

 jobExecutionContext.setResult(exceptionTaskEntity);

 // When
 dataArchiveRollbackJob.execute(jobExecutionContext);

 // Then
 assertEquals(TaskStatus.FAILED, exceptionTaskEntity.getStatus());
 }

 @Test
 public void testExecuteNoScheduler() throws Exception {
 // When
 dataArchiveRollbackJob.execute(null);

 // Then
 // No exception should be thrown
 }

 @Test(expected = JobExecutionException.class)
 public void testExecuteEmptyJobKey() throws Exception {
 // Given
 JobExecutionContext context = newJob(DataArchiveRollbackJob.class)
 .withIdentity(JobKey.jobKey("", ""))
 .build();

 // When
 dataArchiveRollbackJob.execute(context);

 // Then
 // A JobExecutionException should be thrown
 }

 @Test
 public void testGetTaskStatus() throws Exception {
 // Given
 TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

 // Then
 assertEquals(TaskStatus.DONE, taskStatus);
 }

 @Test
 public void testGetTaskStatusNoTaskUnits() throws Exception {
 // Given
 List<DlmTask> emptyTaskUnits = new ArrayList<>();
 taskUnits = emptyTaskUnits;

 // When
 TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

 // Then
 assertEquals(TaskStatus.FAILED, taskStatus);
 }

 @Test
 public void testGetTaskStatusException() throws Exception {
 // Given
 taskUnits = null;

 // When
 TaskStatus taskStatus = dataArchiveRollbackJob.getTaskStatus(taskUnits);

 // Then
 assertEquals(TaskStatus.FAILED, taskStatus);
 }

}