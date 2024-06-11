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
import org.quartz.core. scheduler.Scheduler;
import org.quartz.core.scheduler.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.simpl.RMISchedulerFactory;
import org.quartz.spi.OperableTrigger;
import org.quartz.spi.TriggerFiredBundle;
import org.quartz.spi.jobstore.JobStore;
import org.quartz.spi.operable.OperableJob;
import org.quartz.spi.operator.Operator;
import org.quartz.spi.scheduler.SchedulerPlugin;
import org.quartz.spi.scheduler.SchedulerResource;
import org.quartz.spi.trigger.Trigger;
import org.quartz.spi.validators.TriggerValidator;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

private Scheduler scheduler;
private JobStore jobStore;
private DataArchiveDeleteJob dataArchiveDeleteJob;
private ScheduleTaskEntity scheduleTaskEntity;
private DataArchiveClearParameters dataArchiveClearParameters;
private List<DlmTask> taskUnits;

@Before
public void setUp() throws SchedulerException {
// Initialize the mocked scheduler
scheduler = mock(Scheduler.class);
jobStore = mock(JobStore.class);
when(scheduler.getJobStore()).thenReturn(jobStore);

// Initialize the system under test
dataArchiveDeleteJob = new DataArchiveDeleteJob();

// Initialize test data
scheduleTaskEntity = new ScheduleTaskEntity();
dataArchiveClearParameters = new DataArchiveClearParameters();
taskUnits = JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {});
dataArchiveClearParameters.setDataArchiveTaskId("123");
scheduleTaskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
scheduleTaskEntity.setResultJson(JsonUtils.toJson(taskUnits));
scheduleTaskEntity.setStatus(TaskStatus.DONE);
}

@Test
public void testExecute() throws Exception {
// Mock the scheduleTaskRepository.findById method to return the scheduleTaskEntity
when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
.thenReturn(Optional.of(scheduleTaskEntity));

// Call the method under test
dataArchiveDeleteJob.execute(null);

// Verify that the updateStatusById method was called with the correct arguments
verify(scheduleTaskRepository).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
}

@Test
public void testExecuteWithNotFoundDataArchiveTask() throws Exception {
// Mock the scheduleTaskRepository.findById method to not return the scheduleTaskEntity
when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
.thenReturn(Optional.empty());

// Call the method under test
dataArchiveDeleteJob.execute(null);

// Verify that the updateStatusById method was called with the correct arguments
verify(scheduleTaskRepository).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
}

@Test
public void testExecuteWithNotFinishedDataArchiveTask() throws Exception {
// Mock the scheduleTaskRepository.findById method to return the scheduleTaskEntity with status not DONE
scheduleTaskEntity.setStatus(TaskStatus.RUNNING);
when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
.thenReturn(Optional.of(scheduleTaskEntity));

// Call the method under test
dataArchiveDeleteJob.execute(null);

// Verify that the updateStatusById method was called with the correct arguments
verify(scheduleTaskRepository).updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
}

@Test
public void testExecuteTask() throws Exception {
// Call the method under test
dataArchiveDeleteJob.executeTask(scheduleTaskEntity.getId(), taskUnits);

// Verify that the method was called with the correct arguments
verify(dataArchiveDeleteJob).executeTask(scheduleTaskEntity.getId(), taskUnits);
}

@Test
public void testGetTaskStatus() throws Exception {
// Call the method under test
TaskStatus taskStatus = dataArchiveDeleteJob.getTaskStatus(taskUnits);

// Verify that the method was called with the correct arguments
verify(dataArchiveDeleteJob).getTaskStatus(taskUnits);

// Verify the returned value
assertEquals(TaskStatus.PREPARING, taskStatus);
}
}