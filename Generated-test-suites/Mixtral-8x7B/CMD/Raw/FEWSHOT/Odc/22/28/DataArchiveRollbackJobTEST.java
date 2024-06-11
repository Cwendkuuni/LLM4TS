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
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.scheduler.GroupedTrigger;
import org.quartz.scheduler.Scheduler;
import org.quartz.scheduler.SchedulerException;
import org.quartz.scheduler.SchedulerFactory;
import org.quartz.simpl.SimpleSchedulerFactory;

public class DataArchiveRollbackJobTest {

private static final String TASK_ENTITY_ID = "123456";
private static final String TASK_ENTITY_RESULT_JSON = "{\"key\":\"value\"}";
private static final String DATA_ARCHIVE_TASK_ID = "789012";
private static final String DATA_ARCHIVE_TASK_RESULT_JSON = "[{\"key\":\"value\"}]";
private static final Long SOURCE_DATABASE_ID = 345678L;
private static final Long TARGET_DATABASE_ID = 901234L;
private static final TypeReference<List<DlmTask>> LIST_TYPE_REFERENCE = new TypeReference<List<DlmTask>>() {
};
private static final JobType JOB_TYPE = JobType.ROLLBACK;
private static final TaskStatus TASK_STATUS_PREPARING = TaskStatus.PREPARING;
private static final TaskStatus TASK_STATUS_DONE = TaskStatus.DONE;
private static final TaskStatus TASK_STATUS_FAILED = TaskStatus.FAILED;

@Test
public void testExecute() throws SchedulerException, InterruptedException {
SchedulerFactory schedulerFactory = new SimpleSchedulerFactory();
Scheduler scheduler = schedulerFactory.getScheduler();

DataArchiveRollbackJob dataArchiveRollbackJob = new DataArchiveRollbackJob();

// Initialize dependencies
JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);
ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
when(jobExecutionContext.getResult()).thenReturn(taskEntity);
when(taskEntity.getParametersJson()).thenReturn("{}");
when(taskEntity.getId()).thenReturn(TASK_ENTITY_ID);
when(taskEntity.getResultJson()).thenReturn(TASK_ENTITY_RESULT_JSON);

DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
rollbackParameters.setDataArchiveTaskId(DATA_ARCHIVE_TASK_ID);

// Mock scheduleTaskRepository.findById
ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
when(dataArchiveTask.getResultJson()).thenReturn(DATA_ARCHIVE_TASK_RESULT_JSON);
Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(dataArchiveTask);
when(scheduleTaskRepository.findById(DATA_ARCHIVE_TASK_ID)).thenReturn(dataArchiveTaskOption);

// Mock scheduleTaskRepository.updateStatusById
scheduleTaskRepository = mock(ScheduleTaskRepository.class);

// Mock JsonUtils.fromJson
List<DlmTask> taskUnits = mock(List.class);
when(JsonUtils.fromJson(DATA_ARCHIVE_TASK_RESULT_JSON, LIST_TYPE_REFERENCE))
.thenReturn(taskUnits);

// Mock DlmJobIdUtil.generateHistoryJobId
DlmJobIdUtil dlmJobIdUtil = mock(DlmJobIdUtil.class);
when(DlmJobIdUtil.generateHistoryJobId(
taskEntity.getJobName(),
taskEntity.getJobGroup(),
taskEntity.getId(),
taskUnits.size()))
.thenReturn(1234567890L);

// Set dependencies
dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
dataArchiveRollbackJob.setJsonUtils(JsonUtils.getInstance());
dataArchiveRollbackJob.setDlmJobIdUtil(dlmJobIdUtil);

// Test method
dataArchiveRollbackJob.execute(jobExecutionContext);

// Verify the result
verify(scheduleTaskRepository).updateStatusById(eq(TASK_ENTITY_ID), any(TaskStatus.class));

// Cleanup
scheduler.shutdown();
}

// Add more test cases for other methods if needed
}