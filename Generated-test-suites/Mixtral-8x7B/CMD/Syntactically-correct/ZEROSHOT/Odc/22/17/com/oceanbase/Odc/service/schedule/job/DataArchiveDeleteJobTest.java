import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class DataArchiveDeleteJobTest {

 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;

 @Mock
 private AbstractDlmJob abstractDlmJob;

 @InjectMocks
 private DataArchiveDeleteJob dataArchiveDeleteJob;

 @Before
 public void init() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testExecute() {
 // setup input data
 String parametersJson = "{\"dataArchiveTaskId\": \"123\"}";
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setId("456");
 taskEntity.setParametersJson(parametersJson);
 taskEntity.setStatus(TaskStatus.DONE);

 // setup dependencies
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId("123");

 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId("123");
 dataArchiveTask.setStatus(TaskStatus.DONE);

 List<DlmTask> taskUnits = JsonUtils.fromJson(
 "[{\"id\": \"1\", \"jobType\": \"DELETE\", \"status\": \"PREPARING\"}]",
 new TypeReference<List<DlmTask>>() {
 });

 when(JsonUtils.fromJson(anyString(), any(Class.class))).thenReturn(dataArchiveClearParameters);
 when(scheduleTaskRepository.findById(anyString())).thenReturn(Optional.of(dataArchiveTask));
 when(JsonUtils.fromJson(anyString(), any(TypeReference.class))).thenReturn(taskUnits);
 doNothing().when(dataArchiveDeleteJob).executeTask(anyString(), any());

 // execute method under test
 dataArchiveDeleteJob.execute(null);

 // assert results
 assertEquals(TaskStatus.PREPARING, taskEntity.getStatus());
 }
}