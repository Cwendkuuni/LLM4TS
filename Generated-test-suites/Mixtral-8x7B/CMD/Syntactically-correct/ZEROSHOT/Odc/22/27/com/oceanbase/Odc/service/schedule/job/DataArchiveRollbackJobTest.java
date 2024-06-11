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
import org.quartz.*;
import org.quartz.core.JobDetail;
import org.quartz.core.JobExecutor;
import org.quartz.core. scheduler.ClassLoadHelper;
import org.quartz.core.scheduler.SchedulerException;
import org.quartz.simpl.RamJobStore;
import org.quartz.simpl.SimpleThreadPool;
import org.quartz.spi.OperableTrigger;
import org.quartz.spi.TriggerFiredBundle;
import org.quartz.spi.jobstore.JobStore;
import org.quartz.spi.jobstore.JobStoreTX;
import org.quartz.spi. operating.JobOperator;
import org.quartz.spi.operators.SchedulerSignalerImpl;
import org.quartz.spi.trigger.MySimpleTrigger;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {

private JobOperator jobOperator;
private Scheduler scheduler;
private JobDetail jobDetail;
private JobDataMap jobDataMap;
private DataArchiveRollbackParameters rollbackParameters;
private ScheduleTaskEntity taskEntity;
private List<DlmTask> taskUnits;

@Before
public void setUp() throws SchedulerException {
// Initialize mock objects
jobOperator = mock(JobOperator.class);
scheduler = mock(Scheduler.class);
jobDetail = mock(JobDetail.class);
jobDataMap = new JobDataMap();
rollbackParameters = new DataArchiveRollbackParameters();
taskEntity = new ScheduleTaskEntity();
taskUnits = mock(List.class);

// Set up mock scheduler
when(scheduler.getJobDetail(anyLong())).thenReturn(jobDetail);
when(scheduler.getJobOperator()).thenReturn(jobOperator);

// Set up mock job data map
jobDataMap.put("scheduleTaskEntity", taskEntity);

// Set up mock rollback parameters
rollbackParameters.setDataArchiveTaskId(1L);

// Set up mock task entity
taskEntity.setId(1L);
taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

// Set up mock task units
when(taskUnits.size()).thenReturn(1);
DlmTask dlmTask = new DlmTask();
dlmTask.setSourceDatabaseId(2L);
dlmTask.setTargetDatabaseId(3L);
dlmTask.setJobType(JobType.ROLLBACK);
when(taskUnits.get(0)).thenReturn(dlmTask);

// Set up mock job detail
when(jobDetail.getJobDataMap()).thenReturn(jobDataMap);
}

@Test
public void testExecute() throws Exception {
DataArchiveRollbackJob job = new DataArchiveRollbackJob();

// Call the execute method
job.execute(mock(JobExecutionContext.class));

// Verify that the scheduleTaskRepository updateStatusById method was called with the correct arguments
verify(jobOperator).updateJob(eq(taskEntity.getId()), eq(TaskStatus.FAILED.getValue()), anyString());
}

@Test
public void testFindDataArchiveTaskById() throws Exception {
DataArchiveRollbackJob job = new DataArchiveRollbackJob();

// Set up mock scheduleTaskRepository findById method
when(job.scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(taskEntity));

// Call the findDataArchiveTaskById method
ScheduleTaskEntity result = job.findDataArchiveTaskById(1L, rollbackParameters);

// Verify that the result is the expected task entity
assertEquals(taskEntity, result);
}

@Test
public void testPrepareTasksForRollback() throws Exception {
DataArchiveRollbackJob job = new DataArchiveRollbackJob();

// Call the prepareTasksForRollback method
job.prepareTasksForRollback(taskEntity, taskUnits);

// Verify that the task units were modified correctly
assertEquals(2L, taskUnits.get(0).getSourceDatabaseId());
assertEquals(3L, taskUnits.get(0).getTargetDatabaseId());
assertEquals(JobType.ROLLBACK, taskUnits.get(0).getJobType());
}

@Test
public void testExecuteTask() throws Exception {
DataArchiveRollbackJob job = new DataArchiveRollbackJob();

// Call the executeTask method
job.executeTask(1L, taskUnits);

// Verify that the scheduleTaskRepository updateStatusById method was called with the correct arguments
verify(jobOperator).updateJob(eq(1L), eq(TaskStatus.DONE.getValue()), anyString());
}

@Test
public void testGetTaskStatus() throws Exception {
DataArchiveRollbackJob job = new DataArchiveRollbackJob();

// Set up mock task units to return different statuses
when(taskUnits.get(0).getStatus()).thenReturn(TaskStatus.PREPARING);

// Call the getTaskStatus method
TaskStatus result = job.getTaskStatus(taskUnits);

// Verify that the result is the expected status
assertEquals(TaskStatus.PREPARING, result);
}
}