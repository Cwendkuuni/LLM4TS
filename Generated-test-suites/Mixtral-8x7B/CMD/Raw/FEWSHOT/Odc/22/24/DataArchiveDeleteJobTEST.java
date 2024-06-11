import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.odc.service.schedule.service.DlmJobService;
import com.oceanbase.tools.migrator.common.enums.JobType;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.matchers.GroupMatcher;

@Slf4j
public class DataArchiveDeleteJobTest {

 private DataArchiveDeleteJob dataArchiveDeleteJob;
 private ScheduleTaskRepository scheduleTaskRepository;
 private DlmJobService dlmJobService;
 private JobExecutionContext jobExecutionContext;

 @Before
 public void setup() {
 dataArchiveDeleteJob = spy(new DataArchiveDeleteJob());
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 dlmJobService = mock(DlmJobService.class);
 jobExecutionContext = mock(JobExecutionContext.class);

 doReturn(scheduleTaskRepository).when(dataArchiveDeleteJob).getScheduleTaskRepository();
 doReturn(dlmJobService).when(dataArchiveDeleteJob).getDlmJobService();
 }

 @Test
 public void testExecute_noDataArchiveTask() throws JobExecutionException {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

 when(jobExecutionContext.getResult()).thenReturn(taskEntity);

 dataArchiveDeleteJob.execute(jobExecutionContext);

 verify(scheduleTaskRepository, times(1))
 .updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_dataArchiveTaskNotDone() throws JobExecutionException {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);

 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(2L);
 dataArchiveTask.setStatus(TaskStatus.RUNNING);

 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
 taskEntity.setResultJson("[]");

 when(jobExecutionContext.getResult()).thenReturn(taskEntity);
 doReturn(Optional.of(dataArchiveTask))
 .when(scheduleTaskRepository)
 .findById(dataArchiveClearParameters.getDataArchiveTaskId());

 dataArchiveDeleteJob.execute(jobExecutionContext);

 verify(scheduleTaskRepository, times(1))
 .updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
 }

 @Test
 public void testExecute_success() throws JobExecutionException {
 DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
 dataArchiveClearParameters.setDataArchiveTaskId(1L);
 dataArchiveClearParameters.setArchiveTenantId("tid");
 dataArchiveClearParameters.setArchiveDatabaseName("db");
 dataArchiveClearParameters.setArchiveTableName("tbl");
 dataArchiveClearParameters.setArchiveSchemaName("sch");

 ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
 dataArchiveTask.setId(2L);
 dataArchiveTask.setStatus(TaskStatus.DONE);
 dataArchiveTask.setResultJson(JsonUtils.toJson(Arrays.asList("task1", "task2")));

 ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
 taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));
 taskEntity.setId(3L);

 DlmTask dlmTask1 = new DlmTask();
 dlmTask1.setArchiveTenantId(dataArchiveClearParameters.getArchiveTenantId());
 dlmTask1.setArchiveDatabaseName(dataArchiveClearParameters.getArchiveDatabaseName());
 dlmTask1.setArchiveTableName(dataArchiveClearParameters.getArchiveTableName());
 dlmTask1.setArchiveSchemaName(dataArchiveClearParameters.getArchiveSchemaName());
 dlmTask1.setId("task1");
 dlmTask1.setJobType(JobType.DELETE);
 dlmTask1.setStatus(TaskStatus.PREPARING);

 DlmTask dlmTask2 = new DlmTask();
 dlmTask2.setArchiveTenantId(dataArchiveClearParameters.getArchiveTenantId());
 dlmTask2.setArchiveDatabaseName(dataArchiveClearParameters.getArchiveDatabaseName());
 dlmTask2.setArchiveTableName(dataArchiveClearParameters.getArchiveTableName());
 dlmTask2.setArchiveSchemaName(dataArchiveClearParameters.getArchiveSchemaName());
 dlmTask2.setId("task2");
 dlmTask2.setJobType(JobType.DELETE);
 dlmTask2.setStatus(TaskStatus.PREPARING);

 when(jobExecutionContext.getResult()).thenReturn(taskEntity);
 doReturn(Optional.of(dataArchiveTask))
 .when(scheduleTaskRepository)
 .findById(dataArchiveClearParameters.getDataArchiveTaskId());

 List<DlmTask> taskUnits = Arrays.asList(dlmTask1, dlmTask2);
 doReturn(taskUnits)
 .when(dataArchiveDeleteJob)
 .prepareTasksForClear(anyLong(), any(DataArchiveClearParameters.class));

 dataArchiveDeleteJob.execute(jobExecutionContext);

 verify(scheduleTaskRepository, times(1))
 .updateStatusById(taskEntity.getId(), TaskStatus.SUCCESS);
 verify(dlmJobService, times(1))
 .executeTasks(anyString(), anyListOf(DlmTask.class));
 }
}