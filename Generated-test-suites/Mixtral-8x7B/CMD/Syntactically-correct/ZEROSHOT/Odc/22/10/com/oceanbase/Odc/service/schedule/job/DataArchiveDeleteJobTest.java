import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
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
 private List<DlmTask> dlmTaskList;
 private Optional<ScheduleTaskEntity> scheduleTaskEntityOptional;
 private DlmTask dlmTask;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);

 scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\": 1}");
 scheduleTaskEntity.setStatus(TaskStatus.DONE);

 dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 dlmTaskList = JsonUtils.fromJson("[]", new TypeReference<List<DlmTask>>() {});

 scheduleTaskEntityOptional = Optional.of(scheduleTaskEntity);

 dlmTask = new DlmTask();
 dlmTask.setId(1L);
 dlmTask.setJobType(JobType.DELETE);
 dlmTask.setStatus(TaskStatus.PREPARING);
 }

 @Test
 public void testExecute() throws Exception {
 dataArchiveDeleteJob.execute(null);
 }

 @Test
 public void testPrepareTasksForClear() throws Exception {
 when(scheduleTaskRepository.findById(1L)).thenReturn(scheduleTaskEntityOptional);

 dataArchiveDeleteJob.prepareTasksForClear(1L, dlmTaskList);

 assertEquals(TaskStatus.PREPARING, dlmTaskList.get(0).getStatus());
 }

 @Test
 public void testExecuteTask() throws Exception {
 dataArchiveDeleteJob.executeTask(1L, dlmTaskList);
 }

 @Test
 public void testGetTaskStatus() throws Exception {
 when(scheduleTaskRepository.findById(1L)).thenReturn(scheduleTaskEntityOptional);

 TaskStatus taskStatus = dataArchiveDeleteJob.getTaskStatus(dlmTaskList);

 assertEquals(taskStatus, TaskStatus.PREPARING);
 }
}