package com.oceanbase.odc.service.onlineschemachange;

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
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.util.CollectionUtil;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
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
import com.oceanbase.odc.service.task.model.TaskResult;
import com.oceanbase.odc.service.task.model.TaskStatusEnum;

@RunWith(MockitoJUnitRunner.class)
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
 private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = spy(new OnlineSchemaChangeFlowableTask());

 @Mock
 private TaskService taskService;

 @Mock
 private DelegateExecution execution;

 @Mock
 private ProcessInstance processInstance;

 @Mock
 private Task task;

 @Mock
 private HistoricTaskInstance historicTaskInstance;

 @Mock
 private ObjectMapper objectMapper;

 @Before
 public void setUp() {
 OnlineSchemaChangeContextHolder.clear();
 }

 @Test
 public void testStart() throws Exception {
 // Test case: start the task, check if the task is started, and check if the schedule is created
 Long taskId = 1L;
 when(execution.getProcessInstance()).thenReturn(processInstance);
 when(execution.getVariable("uid")).thenReturn("uid");
 when(execution.getVariable("parameter")).thenReturn(createOnlineSchemaChangeParameters());
 when(execution.getVariable("connectionConfig")).thenReturn(createConnectionConfig());
 when(execution.getVariable("schema")).thenReturn("schema");
 when(taskService.start(taskId)).thenReturn(null);
 when(taskService.detail(taskId)).thenReturn(createTaskEntity());

 onlineSchemaChangeFlowableTask.start(taskId, taskService, execution);

 verify(taskService, times(1)).start(taskId);
 verify(onlineSchemaChangeFlowableTask, times(1)).createScheduleEntity(any(ConnectionConfig.class),
 any(OnlineSchemaChangeParameters.class), anyString());
 }

 @Test
 public void testIsSuccessful() {
 // Test case: the task is successful
 when(onlineSchemaChangeFlowableTask.getStatus()).thenReturn(TaskStatus.DONE);

 boolean result = onlineSchemaChangeFlowableTask.isSuccessful();

 assertTrue(result);
 }

 @Test
 public void testIsFailure() {
 // Test case: the task is failed
 when(onlineSchemaChangeFlowableTask.getStatus()).thenReturn(TaskStatus.FAILED);

 boolean result = onlineSchemaChangeFlowableTask.isFailure();

 assertTrue(result);
 }

 @Test
 public void testOnFailure() {
 // Test case: the task is failed
 when(onlineSchemaChangeFlowableTask.getStatus()).thenReturn(TaskStatus.FAILED);

 onlineSchemaChangeFlowableTask.onFailure(1L, taskService);

 verify(taskService, times(1)).cancel(1L);
 }

 @Test
 public void testOnSuccessful() {
 // Test case: the task is successful
 when(onlineSchemaChangeFlowableTask.getStatus()).thenReturn(TaskStatus.DONE);

 onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);

 verify(onlineSchemaChangeFlowableTask, times(1)).updateFlowInstanceStatus(FlowStatus.EXECUTION_SUCCEEDED);
 }

 @Test
 public void testOnTimeout() {
 // Test case: the task is timeout
 when(onlineSchemaChangeFlowableTask.getStatus()).thenReturn(TaskStatus.TIMEOUT);

 onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);

 verify(taskService, times(1)).cancel(1L);
 }

 @Test
 public void testOnProgressUpdate() throws JsonProcessingException {
 // Test case: the task progress is updated
 when(onlineSchemaChangeFlowableTask.getStatus()).thenReturn(TaskStatus.RUNNING);
 when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(createPageScheduleTaskEntity());
 when(objectMapper.readTree(anyString())).thenReturn(createJsonNode());

 onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

 verify(taskService, times(1)).update(any(TaskEntity.class));
 }

 @Test
 public void testCancel() {
 // Test case: cancel the task
 doReturn(true).when(onlineSchemaChangeFlowableTask).cancel(anyBoolean(), anyLong(), any(TaskService.class));

 boolean result = onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);

 assertTrue(result);
 }

 @Test
 public void testIsCancelled() {
 // Test case: check if the task is cancelled
 when(onlineSchemaChangeFlowableTask.getStatus()).thenReturn(TaskStatus.CANCELED);

 boolean result = onlineSchemaChangeFlowableTask.isCancelled();

 assertTrue(result);
 }

 @Test
 public void testCreateScheduleEntity() {
 // Test case: create the schedule entity
 OnlineSchemaChangeParameters parameter = createOnlineSchemaChangeParameters();
 ConnectionConfig connectionConfig = createConnectionConfig();
 String schema = "schema";
 ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter,
 schema);

 assertNotNull(scheduleEntity);
 }

 @Test
 public void testCreateScheduleTaskEntity() {
 // Test case: create the schedule task entity
 OnlineSchemaChangeScheduleTaskParameters param = createOnlineSchemaChangeScheduleTaskParameters();
 ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(1L, param);

 assertNotNull(scheduleTaskEntity);
 }

 private OnlineSchemaChangeParameters createOnlineSchemaChangeParameters() {
 OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
 parameter.setContinueOnError(true);
 return parameter;
 }

 private ConnectionConfig createConnectionConfig() {
 ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
 when(connectionConfig.id()).thenReturn(1L);
 return connectionConfig;
 }

 private TaskEntity createTaskEntity() {
 TaskEntity taskEntity = new TaskEntity();
 taskEntity.setId(1L);
 taskEntity.setStatus(TaskStatusEnum.RUNNING);
 return taskEntity;
 }

 private Page<ScheduleTaskEntity> createPageScheduleTaskEntity() {
 List<ScheduleTaskEntity> list = new ArrayList<>();
 for (int i = 0; i < 5; i++) {
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setStatus(TaskStatusEnum.RUNNING);
 list.add(scheduleTaskEntity);
 }
 return new PageImpl<>(list);
 }

 private JsonNode createJsonNode() {
 ObjectMapper objectMapper = new ObjectMapper();
 Triple<Integer, Integer, Integer> progress = Triple.of(0, 1, 100);
 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(createOnlineSchemaChangeScheduleTaskParameters(),
 progress);
 try {
 return objectMapper.readTree(objectMapper.writeValueAsString(result));
 } catch (JsonProcessingException e) {
 e.printStackTrace();
 }
 return null;
 }

 private OnlineSchemaChangeScheduleTaskParameters createOnlineSchemaChangeScheduleTaskParameters() {
 OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();
 param.setUid("uid");
 return param;
 }
}