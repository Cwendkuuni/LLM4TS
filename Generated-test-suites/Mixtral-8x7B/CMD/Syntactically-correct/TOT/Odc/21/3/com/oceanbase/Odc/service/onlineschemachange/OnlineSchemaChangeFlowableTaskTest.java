import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OnlineSchemaChangeFlowableTaskTest {

 @Autowired
 private ScheduleTaskRepository scheduleTaskRepository;

 @Autowired
 private ScheduleService scheduleService;

 @Autowired
 private ScheduleTaskService scheduleTaskService;

 @Autowired
 private QuartzJobService quartzJobService;

 @Autowired
 private OnlineSchemaChangeTaskHandler taskHandler;

 @Autowired
 private OscTaskCompleteHandler completeHandler;

 @Autowired
 private OrganizationService organizationService;

 @Autowired
 private TaskService taskService;

 private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

 @BeforeEach
 public void setUp() {
 onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
 onlineSchemaChangeFlowableTask.scheduleTaskRepository = scheduleTaskRepository;
 onlineSchemaChangeFlowableTask.scheduleService = scheduleService;
 onlineSchemaChangeFlowableTask.scheduleTaskService = scheduleTaskService;
 onlineSchemaChangeFlowableTask.quartzJobService = quartzJobService;
 onlineSchemaChangeFlowableTask.taskHandler = taskHandler;
 onlineSchemaChangeFlowableTask.completeHandler = completeHandler;
 onlineSchemaChangeFlowableTask.organizationService = organizationService;
 onlineSchemaChangeFlowableTask.taskService = taskService;
 }

 @Test
 public void testStart() {
 // Prepare mock objects
 ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
 OnlineSchemaChangeParameters parameters = Mockito.mock(OnlineSchemaChangeParameters.class);
 DelegateExecution execution = Mockito.mock(DelegateExecution.class);
 User creator = Mockito.mock(User.class);

 // Set up mock objects
 Mockito.when(execution.getVariable("connectionConfig")).thenReturn(connectionConfig);
 Mockito.when(execution.getVariable("parameters")).thenReturn(parameters);
 Mockito.when(execution.getVariable("uid")).thenReturn("testUid");
 Mockito.when(execution.getVariable("schema")).thenReturn("testSchema");
 Mockito.when(execution.getVariable("continueOnError")).thenReturn(false);
 Mockito.when(execution.getVariable("flowInstanceId")).thenReturn(1L);
 Mockito.when(execution.getVariable("flowDefinitionId")).thenReturn(1L);
 Mockito.when(organizationService.getCurrentUser()).thenReturn(creator);
 Mockito.when(creator.getId()).thenReturn(1L);
 Mockito.when(creator.getOrganizationId()).thenReturn(1L);

 // Call the method
 onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

 // Verify the results
 Mockito.verify(scheduleTaskRepository, Mockito.times(1)).saveAndFlush(Mockito.any());
 }

 @Test
 public void testIsSuccessful() {
 // Prepare mock objects
 onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;

 // Call the method
 boolean result = onlineSchemaChangeFlowableTask.isSuccessful();

 // Verify the results
 assertTrue(result);
 }

 @Test
 public void testIsFailure() {
 // Prepare mock objects
 onlineSchemaChangeFlowableTask.status = TaskStatus.FAILED;

 // Call the method
 boolean result = onlineSchemaChangeFlowableTask.isFailure();

 // Verify the results
 assertTrue(result);
 }

 @Test
 public void testOnFailure() {
 // Prepare mock objects
 Long taskId = 1L;

 // Call the method
 onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);

 // Verify the results
 Mockito.verify(taskService, Mockito.times(1)).fail(taskId);
 }

 @Test
 public void testOnSuccessful() {
 // Prepare mock objects
 Long taskId = 1L;

 // Call the method
 onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskService);

 // Verify the results
 Mockito.verify(taskService, Mockito.times(1)).done(taskId);
 }

 @Test
 public void testOnProgressUpdate() {
 // Prepare mock objects
 Page<ScheduleTaskEntity> tasks = Mockito.mock(Page.class);
 TaskEntity flowTask = Mockito.mock(TaskEntity.class);

 // Set up mock objects
 Mockito.when(tasks.getSize()).thenReturn(3);
 Mockito.when(tasks.getContent()).thenReturn(new ArrayList<ScheduleTaskEntity>(
 Arrays.asList(
 Mockito.mock(ScheduleTaskEntity.class),
 Mockito.mock(ScheduleTaskEntity.class),
 Mockito.mock(ScheduleTaskEntity.class)
 )
 ));
 Mockito.when(taskService.detail(1L)).thenReturn(flowTask);
 Mockito.when(flowTask.getStatus()).thenReturn(TaskStatus.FAILED);
 Mockito.when(flowTask.getProgressPercentage()).thenReturn(50.0);

 // Call the method
 onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

 // Verify the results
 Mockito.verify(taskService, Mockito.times(1)).update(flowTask);
 }

 @Test
 public void testCancel() {
 // Prepare mock objects
 Long taskId = 1L;

 // Call the method
 onlineSchemaChangeFlowableTask.cancel(true, taskId, taskService);

 // Verify the results
 Mockito.verify(taskService, Mockito.times(1)).cancel(taskId);
 }

 @Test
 public void testIsCancelled() {
 // Prepare mock objects
 onlineSchemaChangeFlowableTask.status = TaskStatus.CANCELED;

 // Call the method
 boolean result = onlineSchemaChangeFlowableTask.isCancelled();

 // Verify the results
 assertTrue(result);
 }
}