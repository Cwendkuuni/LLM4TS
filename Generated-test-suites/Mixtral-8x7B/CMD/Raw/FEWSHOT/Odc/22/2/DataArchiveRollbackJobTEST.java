import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.model.DlmTask.DlmTaskBuilder;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters.DataArchiveRollbackParametersBuilder;
import com.oceanbase.tools.migrator.common.enums.JobType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.QuartzJobBean.ExecutionContextAdapter;

public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private JpaRepository<ScheduleTaskEntity, Long> scheduleTaskRepository;
 private JobExecutionContext jobExecutionContext;
 private ScheduleTaskEntity scheduleTaskEntity;
 private DataArchiveRollbackParameters rollbackParameters;
 private List<DlmTask> taskUnits;

 @Before
 public void setUp() {
 scheduleTaskRepository = mock(JpaRepository.class);
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);

 jobExecutionContext = mock(JobExecutionContext.class);
 scheduleTaskEntity = mock(ScheduleTaskEntity.class);
 rollbackParameters = getRollbackParameters();
 taskUnits = getTaskUnits();

 doNothing().when(scheduleTaskRepository).updateStatusById(anyLong(), any(TaskStatus.class));
 when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
 when(scheduleTaskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(rollbackParameters));
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
 }

 @Test
 public void testExecute_whenDataArchiveTaskNotFound_setTaskStatusToFailed() {
 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.empty());

 dataArchiveRollbackJob.execute(jobExecutionContext);

 verify(scheduleTaskRepository, times(1))
 .updateStatusById(scheduleTaskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_whenDataArchiveTaskFound_prepareTasksForRollback() {
 when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId()))
 .thenReturn(Optional.of(scheduleTaskEntity));

 when(scheduleTaskEntity.getResultJson())
 .thenReturn(JsonUtils.toJson(taskUnits));

 ExecutionContextAdapter context = new ExecutionContextAdapter(jobExecutionContext);
 dataArchiveRollbackJob.execute(context);

 verify(scheduleTaskRepository, times(1))
 .updateStatusById(scheduleTaskEntity.getId(), TaskStatus.COMPLETED);
 }

 private DataArchiveRollbackParameters getRollbackParameters() {
 return new DataArchiveRollbackParametersBuilder().dataArchiveTaskId(1L).build();
 }

 private List<DlmTask> getTaskUnits() {
 return new ArrayList<DlmTask>() {{
 add(getDlmTask());
 }};
 }

 private DlmTask getDlmTask() {
 return new DlmTaskBuilder().jobType(JobType.DATA_ARCHIVE)
 .sourceDatabaseId(1L)
 .targetDatabaseId(2L)
 .jobId("jobId")
 .status(TaskStatus.PREPARING)
 .build();
 }
}