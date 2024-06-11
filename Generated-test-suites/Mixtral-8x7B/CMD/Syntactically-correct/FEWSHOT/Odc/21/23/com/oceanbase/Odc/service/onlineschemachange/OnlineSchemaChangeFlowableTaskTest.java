package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.TaskResult;

@SuppressWarnings("unchecked")
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

 @Mock
 private TaskService taskService;

 @Mock
 private DelegateExecution execution;

 @Mock
 private FlowTaskUtil flowTaskUtil;

 @Mock
 private ConnectionConfig connectionConfig;

 @Mock
 private OnlineSchemaChangeParameters parameter;

 @Mock
 private ScheduleEntity schedule;

 @Mock
 private ScheduleTaskEntity scheduleTask;

 @Mock
 private OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters;

 @Mock
 private Pageable pageable;

 @Mock
 private Page<ScheduleTaskEntity> page;

 @Mock
 private TaskEntity task;

 @Mock
 private OnlineSchemaChangeTaskResult onlineSchemaChangeTaskResult;

 @Mock
 private OnlineSchemaChangeScheduleTaskResult onlineSchemaChangeScheduleTaskResult;

 @InjectMocks
 private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

 private ObjectMapper objectMapper = new ObjectMapper();

 @Before
 public void setup() {
 MockitoAnnotations.openMocks(this);
 }

 @Test
 public void testStart() throws Exception {
 // Arrange
 when(execution.getVariable(eq("uid"))).thenReturn("uid");
 when(execution.getVariable(eq("parameter"))).thenReturn(parameter);
 when(execution.getVariable(eq("connectionConfig"))).thenReturn(connectionConfig);
 when(execution.getVariable(eq("schema"))).thenReturn("schema");
 when(flowTaskUtil.getCloudMainAccountId(execution)).thenReturn("uid");
 when(parameter.generateSubTaskParameters(connectionConfig, "schema"))
 .thenReturn(List.of(onlineSchemaChangeScheduleTaskParameters));
 when(scheduleService.create(any(ScheduleEntity.class))).thenReturn(schedule);
 when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenReturn(scheduleTask);
 doNothing().when(taskHandler).start(eq(schedule.getId()), eq(scheduleTask.getId()));

 // Act
 onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

 // Assert
 verify(execution, times(1)).setVariable(eq("scheduleId"), eq(schedule.getId()));
 verify(execution, times(1)).setVariable(eq("organizationId"), eq(1L));
 verify(execution, times(1)).setVariable(eq("status"), eq(TaskStatus.RUNNING));
 verify(taskService, times(1)).start(eq(1L));
 verify(scheduleTaskRepository, times(1)).saveAndFlush(any(ScheduleTaskEntity.class));
 verify(taskHandler, times(1)).start(eq(schedule.getId()), eq(scheduleTask.getId()));
 }

 @Test
 public void testIsSuccessful() {
 // Act
 boolean result = onlineSchemaChangeFlowableTask.isSuccessful();

 // Assert
 assertFalse(result);
 }

 @Test
 public void testIsFailure() {
 // Act
 boolean result = onlineSchemaChangeFlowableTask.isFailure();

 // Assert
 assertFalse(result);
 }

 @Test
 public void testOnFailure() {
 // Act
 onlineSchemaChangeFlowableTask.onFailure(1L, taskService);

 // Assert
 verify(taskService, times(1)).cancel(eq(1L));
 }

 @Test
 public void testOnSuccessful() {
 // Act
 onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);

 // Assert
 verify(taskService, never()).cancel(anyLong());
 }

 @Test
 public void testOnTimeout() {
 // Act
 onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);

 // Assert
 verify(taskService, times(1)).cancel(eq(1L));
 }

 @Test
 public void testOnProgressUpdate() throws JsonProcessingException {
 // Arrange
 when(scheduleTaskService.listTask(pageable, schedule.getId()))
 .thenReturn(new PageImpl<>(List.of(scheduleTask)));
 when(scheduleTask.getStatus()).thenReturn(TaskStatus.RUNNING);
 when(scheduleTask.getProgressPercentage()).thenReturn(50.0);
 when(onlineSchemaChangeTaskResult.getTasks()).thenReturn(List.of(onlineSchemaChangeScheduleTaskResult));
 when(onlineSchemaChangeScheduleTaskResult.getParameters())
 .thenReturn(objectMapper.convertValue(onlineSchemaChangeScheduleTaskParameters, JsonNode.class));
 when(taskService.detail(eq(1L))).thenReturn(task);
 when(task.getStatus()).thenReturn(TaskStatus.RUNNING);
 when(task.getProgressPercentage()).thenReturn(0.0);
 when(task.getResultJson()).thenReturn(objectMapper.writeValueAsString(new OnlineSchemaChangeTaskResult(List
.of(onlineSchemaChangeScheduleTaskResult))));

 ArgumentCaptor<TaskEntity> taskEntityArgumentCaptor = ArgumentCaptor.forClass(TaskEntity.class);

 // Act
 onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

 // Assert
 verify(taskService, times(1)).update(taskEntityArgumentCaptor.capture());
 TaskEntity taskEntity = taskEntityArgumentCaptor.getValue();
 assertEquals(50.0, taskEntity.getProgressPercentage(), 0.0);
 assertEquals(onlineSchemaChangeTaskResult, objectMapper.readValue(taskEntity.getResultJson(),
OnlineSchemaChangeTaskResult.class));
 }

 @Test
 public void testProgressStatusUpdateSuccess() {
 // Arrange
 List<ScheduleTaskEntity> tasks = List.of(
 new ScheduleTaskEntity().setStatus(TaskStatus.DONE),
 new ScheduleTaskEntity().setStatus(TaskStatus.FAILED)
 );

 // Act
 double result = onlineSchemaChangeFlowableTask.progressStatusUpdate(tasks);

 // Assert
 assertEquals(1.0, result, 0.0);
 }

 @Test
 public void testProgressStatusUpdateFailure() {
 // Arrange
 List<ScheduleTaskEntity> tasks = List.of(
 new ScheduleTaskEntity().setStatus(TaskStatus.PREPARING),
 new ScheduleTaskEntity().setStatus(TaskStatus.RUNNING)
 );

 // Act
 double result = onlineSchemaChangeFlowableTask.progressStatusUpdate(tasks);

 // Assert
 assertEquals(0.0, result, 0.0);
 }

 @Test
 public void testSingleTaskPercentagePreparing() {
 // Act
 double result = onlineSchemaChangeFlowableTask.singleTaskPercentage(
 new ScheduleTaskEntity().setStatus(TaskStatus.PREPARING)
 );

 // Assert
 assertEquals(0.0, result, 0.0);
 }

 @Test
 public void testSingleTaskPercentageRunning() {
 // Act
 double result = onlineSchemaChangeFlowableTask.singleTaskPercentage(
 new ScheduleTaskEntity().setStatus(TaskStatus.RUNNING).setProgressPercentage(50.0)
 );

 // Assert
 assertEquals(0.5, result, 0.0);
 }

 @Test
 public void testSingleTaskPercentageDone() {
 // Act
 double result = onlineSchemaChangeFlowableTask.singleTaskPercentage(
 new ScheduleTaskEntity().setStatus(TaskStatus.DONE)
 );

 // Assert
 assertEquals(1.0, result, 0.0);
 }

 @Test
 public void testCancel() {
 // Arrange
 when(scheduleTaskService.listTask(pageable, schedule.getId()))
 .thenReturn(new PageImpl<>(List.of(
 new ScheduleTaskEntity().setStatus(TaskStatus.RUNNING)
 )));
 doNothing().when(taskHandler).terminate(eq(schedule.getId()), eq(1L));

 // Act
 boolean result = onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);

 // Assert
 assertTrue(result);
 verify(taskService, times(1)).cancel(eq(1L));
 }

 @Test
 public void testIsCancelled() {
 // Arrange
 onlineSchemaChangeFlowableTask.status = TaskStatus.CANCELED;

 // Act
 boolean result = onlineSchemaChangeFlowableTask.isCancelled();

 // Assert
 assertTrue(result);
 }

 private ScheduleEntity createScheduleEntity() {
 return new ScheduleEntity();
 }

 private ScheduleTaskEntity createScheduleTaskEntity() {
 return new ScheduleTaskEntity();
 }
}