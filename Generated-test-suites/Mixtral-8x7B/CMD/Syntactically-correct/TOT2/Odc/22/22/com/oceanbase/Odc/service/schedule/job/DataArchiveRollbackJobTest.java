import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.core.task.TaskStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class DataArchiveRollbackJobTest {

private DataArchiveRollbackJob dataArchiveRollbackJob;
private JobExecutionContext jobExecutionContext;
private ScheduleTaskEntity scheduleTaskEntity;
private ScheduleTaskEntity dataArchiveTask;
private DataArchiveRollbackParameters dataArchiveRollbackParameters;
private List<DlmTask> dlmTasks;

@Before
public void setup() {
dataArchiveRollbackJob = spy(new DataArchiveRollbackJob());
jobExecutionContext = mock(JobExecutionContext.class);
scheduleTaskEntity = new ScheduleTaskEntity();
dataArchiveTask = new ScheduleTaskEntity();
dataArchiveRollbackParameters = new DataArchiveRollbackParameters();
dlmTasks = new ArrayList<>();
}

// Test for extracting and listing all the public methods including their signatures
@Test
public void testMethods() {
// List all the public methods here
}

// Test for execute method
@Test
public void testExecute() throws JobExecutionException {
dataArchiveRollbackParameters.setDataArchiveTaskId(1L);
scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\":1}");
dataArchiveTask.setId(2L);
dataArchiveTask.setResultJson("[]");
dataArchiveTask.setJobName("testJobName");
dataArchiveTask.setJobGroup("testJobGroup");

when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
doReturn(dataArchiveTask).when(dataArchiveRollbackJob).findDataArchiveTaskById(anyLong());

DlmTask dlmTask = new DlmTask();
dlmTask.setSourceDatabaseId(3L);
dlmTasks.add(dlmTask);

doReturn(dlmTasks).when(dataArchiveRollbackJob).prepareTasksForRollback(anyLong());

dataArchiveRollbackJob.execute(jobExecutionContext);

assertEquals(TaskStatus.DONE, scheduleTaskEntity.getStatus());
assertEquals("testJobName", scheduleTaskEntity.getJobName());
assertEquals("testJobGroup", scheduleTaskEntity.getJobGroup());
assertNotNull(scheduleTaskEntity.getResultJson());
assertFalse(scheduleTaskEntity.getResultJson().isEmpty());
}

// Helper method for finding data archive task by id
private ScheduleTaskEntity findDataArchiveTaskById(Long id) {
return dataArchiveRollbackJob.findDataArchiveTaskById(id);
}

// Helper method for preparing tasks for rollback
private List<DlmTask> prepareTasksForRollback(Long dataArchiveTaskId) {
return dataArchiveRollbackJob.prepareTasksForRollback(dataArchiveTaskId);
}
}