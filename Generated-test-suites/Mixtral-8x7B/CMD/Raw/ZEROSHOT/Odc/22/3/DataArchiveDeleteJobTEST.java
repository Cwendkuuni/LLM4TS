import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.AbstractDlmJob;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class DataArchiveDeleteJobTest {

 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;

 @Mock
 private AbstractDlmJob abstractDlmJob;

 @InjectMocks
 private DataArchiveDeleteJob dataArchiveDeleteJob;

 private ScheduleTaskEntity scheduleTaskEntity;

 private DataArchiveClearParameters dataArchiveClearParameters;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);

 scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
 scheduleTaskEntity.setStatus(TaskStatus.DONE);
 scheduleTaskEntity.setResultJson("[]");

 dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(2L);
 }

 @Test
 public void testExecute() {
 List<DlmTask> taskUnits = JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {});

 when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(scheduleTaskEntity));
 when(abstractDlmJob.executeTask(any(), any())).thenReturn(taskUnits);

 dataArchiveDeleteJob.execute(null);

 assertEquals(TaskStatus.PREPARING, scheduleTaskRepository.getTaskStatus(1L));
 }

 @Test
 public void testExecute_whenDataArchiveTaskNotFound() {
 when(scheduleTaskRepository.findById(any())).thenReturn(Optional.empty());

 dataArchiveDeleteJob.execute(null);

 assertEquals(TaskStatus.FAILED, scheduleTaskRepository.getTaskStatus(1L));
 }

 @Test
 public void testExecute_whenDataArchiveTaskNotDone() {
 scheduleTaskEntity.setStatus(TaskStatus.PREPARING);

 dataArchiveDeleteJob.execute(null);

 assertEquals(TaskStatus.FAILED, scheduleTaskRepository.getTaskStatus(1L));
 }
}