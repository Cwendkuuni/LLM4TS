import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataArchiveDeleteJobTest {

@Test
public void testExecute() throws JobExecutionException {
// Prepare mock objects
JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);
ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
taskEntity.setId(1L);
taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
taskEntity.setResultJson("[]");

DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId(2L);

when(jobExecutionContext.getResult()).thenReturn(taskEntity);

List<DlmTask> taskUnits = new ArrayList<>();
taskUnits.add(new DlmTask());
taskUnits.add(new DlmTask());

String taskUnitsJson = JsonUtils.toJson(taskUnits);

ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
dataArchiveTask.setId(2L);
dataArchiveTask.setStatus(TaskStatus.DONE);

Optional<ScheduleTaskEntity> dataArchiveTaskOptional = Optional.of(dataArchiveTask);

when(scheduleTaskRepository.findById(2L)).thenReturn(dataArchiveTaskOptional);

when(JsonUtils.fromJson(anyString(), any(TypeReference.class))).thenReturn(taskUnits);

DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
dataArchiveDeleteJob.scheduleTaskRepository = mock(ScheduleTaskRepository.class);
when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class))).thenReturn(true);

// Call the method under test
dataArchiveDeleteJob.execute(jobExecutionContext);

// Verify the results
/*
Add your assertions here based on the expected behavior of the method under test.
For example, you can check if the task status has been updated correctly,
if the tasks have been prepared with the correct properties, etc.
*/
}

// Add more test methods as needed to cover different scenarios and methods

}