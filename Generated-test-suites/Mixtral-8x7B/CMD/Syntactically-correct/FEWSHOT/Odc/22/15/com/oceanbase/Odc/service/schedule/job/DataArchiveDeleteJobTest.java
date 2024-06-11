import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Slf4j
public class DataArchiveDeleteJobTest {

private DataArchiveDeleteJob dataArchiveDeleteJob;
private JobExecutionContext jobExecutionContext;
private ScheduleTaskEntity scheduleTaskEntity;
private DataArchiveClearParameters dataArchiveClearParameters;
private Optional<ScheduleTaskEntity> dataArchiveTaskOption;
private ScheduleTaskEntity dataArchiveTask;
private List<DlmTask> taskUnits;
private TypeReference<List<DlmTask>> typeReference;

@Before
public void setUp() {
dataArchiveDeleteJob = new DataArchiveDeleteJob();
jobExecutionContext = mock(JobExecutionContext.class);
scheduleTaskEntity = mock(ScheduleTaskEntity.class);
dataArchiveClearParameters = mock(DataArchiveClearParameters.class);
dataArchiveTaskOption = Optional.of(scheduleTaskEntity);
dataArchiveTask = mock(ScheduleTaskEntity.class);
taskUnits = mock(List.class);
typeReference = new TypeReference<List<DlmTask>>() {
};
}

@Test
public void testExecute() throws JobExecutionException {
when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
when(scheduleTaskEntity.getParametersJson()).thenReturn("parametersJson");
when(JsonUtils.fromJson(anyString(), any(Class.class)))
.thenReturn(dataArchiveClearParameters);
when(scheduleTaskRepository.findById(anyString())).thenReturn(dataArchiveTaskOption);
when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);
when(dataArchiveTask.getResultJson()).thenReturn("resultJson");
when(JsonUtils.fromJson(anyString(), any(TypeReference.class)))
.thenReturn(taskUnits);
when(DlmJobIdUtil.generateHistoryJobId(anyString(), anyString(), anyString(), anyInt()))
.thenReturn("historyJobId");
dataArchiveDeleteJob.execute(jobExecutionContext);
}

@Test
public void testExecuteFailedNoTaskFound()
throws JobExecutionException {
when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
when(scheduleTaskEntity.getParametersJson()).thenReturn("parametersJson");
when(JsonUtils.fromJson(anyString(), any(Class.class)))
.thenReturn(dataArchiveClearParameters);
when(scheduleTaskRepository.findById(anyString()))
.thenReturn(Optional.ofNullable(null));
dataArchiveDeleteJob.execute(jobExecutionContext);
}

@Test
public void testExecuteFailedTaskNotDone()
throws JobExecutionException {
when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
when(scheduleTaskEntity.getParametersJson()).thenReturn("parametersJson");
when(JsonUtils.fromJson(anyString(), any(Class.class)))
.thenReturn(dataArchiveClearParameters);
when(scheduleTaskRepository.findById(anyString()))
.thenReturn(dataArchiveTaskOption);
when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.RUNNING);
dataArchiveDeleteJob.execute(jobExecutionContext);
}

}