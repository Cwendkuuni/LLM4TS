import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.impl.JobExecutionException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@Slf4j
public class DataArchiveDeleteJobTest {

private DataArchiveDeleteJob dataArchiveDeleteJob;
private JobExecutionContext jobExecutionContext;
private ScheduleTaskEntity scheduleTaskEntity;
private DataArchiveClearParameters dataArchiveClearParameters;
private List<DlmTask> taskUnits;
private MongoTemplate mongoTemplate;
private ScheduleTaskRepository scheduleTaskRepository;

@Before
public void setup() {
dataArchiveDeleteJob = spy(new DataArchiveDeleteJob());
jobExecutionContext = mock(JobExecutionContext.class);
scheduleTaskEntity = mock(ScheduleTaskEntity.class);
dataArchiveClearParameters = new DataArchiveClearParameters();
dataArchiveClearParameters.setDataArchiveTaskId("123");

taskUnits = new ArrayList<>();
taskUnits.add(new DlmTask());

mongoTemplate = mock(MongoTemplate.class);
scheduleTaskRepository = mock(ScheduleTaskRepository.class);

dataArchiveDeleteJob.mongoTemplate = mongoTemplate;
dataArchiveDeleteJob.scheduleTaskRepository = scheduleTaskRepository;
}

@Test
public void testExecute_whenTaskEntityIsNull_thenUpdateStatusByIdWithFailed() throws JobExecutionException {
doReturn(null).when(jobExecutionContext).getResult();

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), eq(TaskStatus.FAILED));
}

@Test
public void testExecute_whenDataArchiveTaskNotFound_thenUpdateStatusByIdWithFailed() throws JobExecutionException {
doReturn(scheduleTaskEntity).when(jobExecutionContext).getResult();
when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.empty());

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), eq(TaskStatus.FAILED));
}

@Test
public void testExecute_whenDataArchiveTaskNotDone_thenUpdateStatusByIdWithFailed() throws JobExecutionException {
doReturn(scheduleTaskEntity).when(jobExecutionContext).getResult();
when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(new ScheduleTaskEntity()));
when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.RUNNING);

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), eq(TaskStatus.FAILED));
}

@Test
public void testExecute_whenDeserializeDataArchiveClearParametersFailed_thenUpdateStatusByIdWithFailed()
throws JobExecutionException {
doReturn(scheduleTaskEntity).when(jobExecutionContext).getResult();
when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(new ScheduleTaskEntity()));
when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.DONE);
when(scheduleTaskEntity.getParametersJson()).thenReturn("invalid json");

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), eq(TaskStatus.FAILED));
}

@Test
public void testExecute_whenDeserializeTaskUnitsFailed_thenUpdateStatusByIdWithFailed() throws JobExecutionException {
doReturn(scheduleTaskEntity).when(jobExecutionContext).getResult();
when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(new ScheduleTaskEntity()));
when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.DONE);
String invalidJson = "{\"dataArchiveTaskId\":\"123\",\"resultJson\":\"invalid json\"}";
when(scheduleTaskEntity.getParametersJson()).thenReturn(invalidJson);

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), eq(TaskStatus.FAILED));
}

@Test
public void testExecute_whenEverythingOk_thenUpdateStatusByIdWithPreparing() throws JobExecutionException {
doReturn(scheduleTaskEntity).when(jobExecutionContext).getResult();
when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(new ScheduleTaskEntity()));
when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.DONE);
String validJson = "{\"dataArchiveTaskId\":\"123\",\"resultJson\":[]}";
when(scheduleTaskEntity.getParametersJson()).thenReturn(validJson);

dataArchiveDeleteJob.execute(jobExecutionContext);

verify(scheduleTaskRepository, times(1)).updateStatusById(anyString(), eq(TaskStatus.PREPARING));
}
}