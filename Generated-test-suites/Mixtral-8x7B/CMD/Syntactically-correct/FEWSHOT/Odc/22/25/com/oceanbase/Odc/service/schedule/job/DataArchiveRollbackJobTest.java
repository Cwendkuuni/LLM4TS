import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;
import com.oceanbase.tools.migrator.common.enums.JobType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.impl.JobExecutionContextImpl;
import org.quartz.simpl.RAMJobStore;
import org.quartz.simpl.RAMJobStoreFactory;

@SuppressWarnings("unchecked")
public class DataArchiveRollbackJobTest {

 private DataArchiveRollbackJob dataArchiveRollbackJob;
 private ScheduleTaskRepository scheduleTaskRepository;
 private JobExecutionContext jobExecutionContext;

 @Before
 public void setup() {
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 dataArchiveRollbackJob = new DataArchiveRollbackJob();
 dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);

 // Initialize a mock JobDataMap
 RAMJobStoreFactory factory = new RAMJobStoreFactory();
 RAMJobStore store = (RAMJobStore) factory.createJobStore();
 store.init();

 // Create a mock JobExecutionContext with a populated result
 jobExecutionContext = new JobExecutionContextImpl();
 JobDataMap data = jobExecutionContext.getMergedJobDataMap();
 data.put("scheduleTaskEntity", mock(ScheduleTaskEntity.class));

 // Set the job store for the JobExecutionContext to use the mock JobStore
 jobExecutionContext.setJobStore(store);
 }

 @Test
 public void testExecuteWhenDataArchiveTaskNotFound() throws Exception {
 // Given
 ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
 when(taskEntity.getId()).thenReturn(1L);
 when(taskEntity.getParametersJson()).thenReturn("{}");
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));

 // When
 dataArchiveRollbackJob.execute(jobExecutionContext);

 // Then
 assertEquals(TaskStatus.FAILED, getTaskStatus(taskEntity.getId()));
 }

 @Test
 public void testExecuteWhenDataArchiveTaskFound() throws Exception {
 // Given
 ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
 when(taskEntity.getId()).thenReturn(1L);
 when(taskEntity.getParametersJson())
 .thenReturn("{\"dataArchiveTaskId\":2,\"rollbackParameters\":{}}");
 
 Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(taskEntity);
 when(scheduleTaskRepository.findById(anyLong()))
 .thenReturn(dataArchiveTaskOption);
 
 List<DlmTask> taskUnits = new ArrayList<>();
 DlmTask dlmTask = new DlmTask();
 dlmTask.setSourceDatabaseId(1L);
 taskUnits.add(dlmTask);
 when(scheduleTaskRepository.findById(anyLong()))
 .thenReturn(dataArchiveTaskOption);
 when(JsonUtils.fromJson(anyString(), any(TypeReference.class)))
 .thenReturn(taskUnits);
 
 // When
 dataArchiveRollbackJob.execute(jobExecutionContext);

 // Then
 assertEquals(TaskStatus.DONE, getTaskStatus(taskEntity.getId()));
 }

 private TaskStatus getTaskStatus(Long taskId) {
 ScheduleTaskEntity taskEntity = (ScheduleTaskEntity) jobExecutionContext.getMergedJobDataMap().get("scheduleTaskEntity");
 return taskEntity.getStatus();
 }
}