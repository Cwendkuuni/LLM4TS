import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskService;

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

 @Mock
 private DelegateExecution delegateExecution;

 @Mock
 private ConnectionConfig connectionConfig;

 @Mock
 private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

 @Mock
 private TaskService taskService;

 @Mock
 private OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters;

 @Mock
 private ScheduleEntity scheduleEntity;

 @Mock
 private TaskEntity taskEntity;

 @Mock
 private ScheduleTaskEntity scheduleTaskEntity;

 @BeforeEach
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testStart() throws Exception {
 // Setup mock objects and method calls
 when(delegateExecution.getVariable("uid")).thenReturn("testUid");
 when(delegateExecution.getVariable("parameters")).thenReturn(onlineSchemaChangeParameters);
 when(delegateExecution.getVariable("connectionConfig")).thenReturn(connectionConfig);
 when(delegateExecution.getVariable("schema")).thenReturn("testSchema");
 when(scheduleService.create(scheduleEntity)).thenReturn(scheduleEntity);
 when(scheduleTaskRepository.saveAndFlush(scheduleTaskEntity)).thenReturn(scheduleTaskEntity);
 when(taskHandler.start(scheduleEntity.getId(), scheduleTaskEntity.getId())).thenReturn(true);

 // Call the method under test
 onlineSchemaChangeFlowableTask.start(1L, taskService, delegateExecution);

 // Verify the expected method calls
 verify(delegateExecution).getVariable("uid");
 verify(delegateExecution).getVariable("parameters");
 verify(delegateExecution).getVariable("connectionConfig");
 verify(delegateExecution).getVariable("schema");
 verify(scheduleService).create(scheduleEntity);
 verify(scheduleTaskRepository).saveAndFlush(scheduleTaskEntity);
 verify(taskHandler).start(scheduleEntity.getId(), scheduleTaskEntity.getId());
 }

 @Test
 public void testIsSuccessful() {
 // Call the method under test
 boolean result = onlineSchemaChangeFlowableTask.isSuccessful();

 // Verify the result
 assertFalse(result);
 }

 @Test
 public void testIsFailure() {
 // Call the method under test
 boolean result = onlineSchemaChangeFlowableTask.isFailure();

 // Verify the result
 assertFalse(result);
 }

 @Test
 public void testOnFailure() {
 // Call the method under test
 onlineSchemaChangeFlowableTask.onFailure(1L, taskService);

 // Verify that the taskService.cancel(1L) method was called
 verify(taskService).cancel(1L);
 }

 @Test
 public void testOnSuccessful() {
 // Call the method under test
 onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);

 // Verify that the updateFlowInstanceStatus(FlowStatus.EXECUTION_SUCCEEDED) method was called
 verify(onlineSchemaChangeFlowableTask).updateFlowInstanceStatus(FlowStatus.EXECUTION_SUCCEEDED);
 }

 @Test
 public void testOnProgressUpdate() {
 // Call the method under test
 onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

 // Verify that no method calls were made
 verifyNoMoreInteractions(taskService);
 }

 @Test
 public void testCancel() {
 // Call the method under test
 boolean result = onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);

 // Verify the result
 assertTrue(result);
 }

 @Test
 public void testIsCancelled() {
 // Call the method under test
 boolean result = onlineSchemaChangeFlowableTask.isCancelled();

 // Verify the result
 assertFalse(result);
 }
}