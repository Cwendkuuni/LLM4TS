import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.impl.JobExecutionContextImpl;

@Slf4j
public class DataArchiveRollbackJobTest {

private DataArchiveRollbackJob dataArchiveRollbackJob;
private JobExecutionContext jobExecutionContext;
private ScheduleTaskEntity scheduleTaskEntity;
private DataArchiveRollbackParameters rollbackParameters;
private List<DlmTask> taskUnits;
private ScheduleTaskEntity dataArchiveTask;
private Optional<ScheduleTaskEntity> dataArchiveTaskOption;

@Before
public void setUp() {
dataArchiveRollbackJob = new DataArchiveRollbackJob();

jobExecutionContext = new JobExecutionContextImpl();

scheduleTaskEntity = new ScheduleTaskEntity();
scheduleTaskEntity.setId(1L);
scheduleTaskEntity.setParametersJson("{}");
scheduleTaskEntity.setResultJson("[]");

rollbackParameters = new DataArchiveRollbackParameters();
rollbackParameters.setDataArchiveTaskId(2L);

taskUnits = new ArrayList<>();
taskUnits.add(new DlmTask());
taskUnits.add(new DlmTask());

dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(2L);
dataArchiveTask.setResultJson(JsonUtils.toJson(taskUnits));

dataArchiveTaskOption = Optional.of(dataArchiveTask);

}

@Test
public void testExecute() throws Exception {
JobExecutionContext context = mock(JobExecutionContext.class);
when(context.getResult()).thenReturn(scheduleTaskEntity);

dataArchiveRollbackJob.execute(context);

// Add your assertions here
}

@Test
public void testExecuteWithDataArchiveTaskNotFound() throws Exception {
JobExecutionContext context = mock(JobExecutionContext.class);
when(context.getResult()).thenReturn(scheduleTaskEntity);
when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

dataArchiveRollbackJob.execute(context);

// Add your assertions here
assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
}

@Test
public void testExecuteWithPrepareTasks() throws Exception {
JobExecutionContext context = mock(JobExecutionContext.class);
when(context.getResult()).thenReturn(scheduleTaskEntity);
when(scheduleTaskRepository.findById(anyLong())).thenReturn(dataArchiveTaskOption);

dataArchiveRollbackJob.execute(context);

// Add your assertions here
}

// Add more test methods as needed
}