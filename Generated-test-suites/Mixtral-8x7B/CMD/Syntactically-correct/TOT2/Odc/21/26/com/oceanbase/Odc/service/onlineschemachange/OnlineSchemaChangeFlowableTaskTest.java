import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
public class OnlineSchemaChangeFlowableTaskTest {

 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;

 @Mock
 private ScheduleService scheduleService;

 @Mock
 private ScheduleTaskService scheduleTaskService;

 @Mock
 private QuartzJobService quartzJobService;

 @Mock
 private OnlineSchemaChangeTaskHandler taskHandler;

 @Mock
 private OscTaskCompleteHandler completeHandler;

 @Mock
 private OrganizationService organizationService;

 @InjectMocks
 private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

 private DelegateExecution delegateExecution;

 private ProcessInstance processInstance;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);

 delegateExecution = mock(DelegateExecution.class);
 processInstance = mock(ProcessInstance.class);

 when(delegateExecution.getProcessInstance()).thenReturn(processInstance);
 when(processInstance.getId()).thenReturn("123");
 }

 @Test
 public void testStart() throws Exception {
 OnlineSchemaChangeParameters parameters = OnlineSchemaChangeParameters.builder().build();
 ConnectionConfig connectionConfig = ConnectionConfig.builder().id(1L).build();

 onlineSchemaChangeFlowableTask.start(1L, null, delegateExecution);

 verify(scheduleTaskRepository, times(1)).saveAndFlush(any(ScheduleTaskEntity.class));
 verify(taskHandler, times(1)).start(anyLong(), anyLong());
 }

 @Test
 public void testIsSuccessful() {
 onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);

 assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
 }

 @Test
 public void testIsFailure() {
 onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);

 assertTrue(onlineSchemaChangeFlowableTask.isFailure());
 }

 @Test
 public void testOnFailure() {
 onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);

 onlineSchemaChangeFlowableTask.onFailure(1L, null);

 verify(delegateExecution, times(1)).setVariable("failure", true);
 }

 @Test
 public void testOnSuccessful() {
 onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);

 onlineSchemaChangeFlowableTask.onSuccessful(1L, null);

 verify(delegateExecution, times(1)).setVariable("success", true);
 }

 @Test
 public void testOnTimeout() {
 onlineSchemaChangeFlowableTask.onTimeout(1L, null);

 verify(delegateExecution, times(1)).setVariable("timeout", true);
 }

 @Test
 public void testOnProgressUpdate() {
 OnlineSchemaChangeTaskResult result = new OnlineSchemaChangeTaskResult();
 result.setTasks(Arrays.asList(
 new OnlineSchemaChangeScheduleTaskResult(),
 new OnlineSchemaChangeScheduleTaskResult()
 ));

 TaskEntity taskEntity = new TaskEntity();
 taskEntity.setResultJson(JsonUtils.toJson(result));
 taskEntity.setProgressPercentage(50);
 taskEntity.setStatus(TaskStatus.RUNNING);

 when(scheduleTaskService.listTask(Pageable.unpaged(), 1L)).thenReturn(mock(Page.class));
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));

 onlineSchemaChangeFlowableTask.onProgressUpdate(1L, null);

 verify(delegateExecution, times(1)).setVariable("progress", result);
 verify(delegateExecution, times(1)).setVariable("progress_percentage", 50);
 }

 @Test
 public void testCancel() {
 onlineSchemaChangeFlowableTask.setStatus(TaskStatus.RUNNING);

 doNothing().when(taskHandler).terminate(anyLong(), anyLong());

 assertTrue(onlineSchemaChangeFlowableTask.cancel(false, 1L, null));
 }

 @Test
 public void testIsCancelled() {
 onlineSchemaChangeFlowableTask.setStatus(TaskStatus.CANCELED);

 assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
 }

 @Test
 public void testCreateScheduleEntity() {
 OnlineSchemaChangeParameters parameters = OnlineSchemaChangeParameters.builder().build();
 ConnectionConfig connectionConfig = ConnectionConfig.builder().id(1L).build();

 ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameters, "schema");

 assertNotNull(scheduleEntity.getId());
 }

 @Test
 public void testCreateScheduleTaskEntity() {
 OnlineSchemaChangeScheduleTaskParameters parameters = OnlineSchemaChangeScheduleTaskParameters.builder().build();

 ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(1L, parameters);

 assertNotNull(scheduleTaskEntity.getId());
 }
}