package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.Expression;
import org.flowable.engine.delegate.VariableScope;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.junit.Before;
import org.junit.Test;
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
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.metadb.task.TaskRepository;
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

@org.junit.runner.RunWith(org.mockito.junit.MockitoJUnitRunner.Silent.class)
public class OnlineSchemaChangeFlowableTaskTest {

 @Mock
 private ScheduleRepository scheduleRepository;

 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;

 @Mock
 private TaskRepository taskRepository;

 @Mock
 private QuartzJobService quartzJobService;

 @Mock
 private ScheduleService scheduleService;

 @Mock
 private ScheduleTaskService scheduleTaskService;

 @Mock
 private OrganizationService organizationService;

 @Mock
 private ConnectionConfig connectionConfig;

 @Mock
 private OnlineSchemaChangeParameters parameters;

 @Mock
 private OnlineSchemaChangeScheduleTaskParameters subTaskParameters;

 @Mock
 private TaskEntity taskEntity;

 @Mock
 private ScheduleEntity scheduleEntity;

 @Mock
 private ScheduleTaskEntity scheduleTaskEntity;

 @Mock
 private DelegateExecution execution;

 @Mock
 private Expression expression;

 @Mock
 private VariableScope variableScope;

 @InjectMocks
 private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

 private ObjectMapper objectMapper = new ObjectMapper();

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testStart() throws Exception {
 // Arrange
 when(execution.getVariable("uid")).thenReturn(1L);
 when(execution.getVariable("parameters")).thenReturn(parameters);
 when(execution.getVariable("connectionConfig")).thenReturn(connectionConfig);
 when(execution.getVariable("schema")).thenReturn("test_schema");
 when(parameters.isContinueOnError()).thenReturn(true);
 when(scheduleRepository.save(any(ScheduleEntity.class))).thenReturn(scheduleEntity);
 when(scheduleTaskRepository.saveAll(anyList())).thenReturn(new ArrayList<>());
 when(subTaskParameters.generateSubTaskParameters(connectionConfig, "test_schema"))
 .thenReturn(List.of(subTaskParameters));
 when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(new PageImpl<>(List.of(scheduleTaskEntity)));
 doReturn(taskEntity).when(taskRepository).saveAndFlush(any(TaskEntity.class));
 doNothing().when(taskHandler).start(anyLong(), anyLong());

 // Act
 onlineSchemaChangeFlowableTask.start(1L, taskRepository, execution);

 // Assert
 verify(execution, times(1)).setVariable("scheduleId", scheduleEntity.getId());
 verify(execution, times(1)).setVariable("organizationId", 1L);
 verify(taskRepository, times(1)).save(taskEntity);
 verify(taskHandler, times(1)).start(scheduleEntity.getId(), scheduleTaskEntity.getId());
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
 onlineSchemaChangeFlowableTask.onFailure(1L, taskRepository);

 // Assert
 verify(taskRepository, times(1)).fail(1L);
 }

 @Test
 public void testOnSuccessful() {
 // Arrange
 when(execution.getVariable("status")).thenReturn(TaskStatus.DONE.name());

 // Act
 onlineSchemaChangeFlowableTask.onSuccessful(1L, taskRepository);

 // Assert
 verify(taskRepository, times(1)).complete(1L);
 }

 @Test
 public void testOnTimeout() {
 // Act
 onlineSchemaChangeFlowableTask.onTimeout(1L, taskRepository);

 // Assert
 verify(taskRepository, times(1)).timeout(1L);
 }

 @Test
 public void testOnProgressUpdate() throws JsonProcessingException {
 // Arrange
 when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(
 new PageImpl<>(List.of(scheduleTaskEntity)));
 when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.RUNNING);
 when(scheduleTaskEntity.getProgressPercentage()).thenReturn(50.0);
 when(objectMapper.writeValueAsString(any(OnlineSchemaChangeTaskResult.class))).thenReturn("{}");
 when(taskRepository.detail(1L)).thenReturn(taskEntity);

 // Act
 onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskRepository);

 // Assert
 verify(taskEntity, times(1)).setProgressPercentage(50);
 }

 @Test
 public void testCancel() {
 // Arrange
 when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(
 new PageImpl<>(List.of(scheduleTaskEntity)));
 doReturn(scheduleEntity).when(scheduleRepository).findById(1L);
 doNothing().when(taskHandler).terminate(1L, 1L);
 doNothing().when(taskRepository).cancel(1L);

 // Act
 onlineSchemaChangeFlowableTask.cancel(false, 1L, taskRepository);

 // Assert
 verify(scheduleTaskEntity, times(1)).setStatus(TaskStatus.CANCELED);
 verify(scheduleRepository, times(1)).save(scheduleEntity);
 verify(taskRepository, times(1)).cancel(1L);
 }

 @Test
 public void testIsCancelled() {
 // Act
 boolean result = onlineSchemaChangeFlowableTask.isCancelled();

 // Assert
 assertFalse(result);
 }

 private ScheduleEntity setupScheduleEntity() {
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 scheduleEntity.setConnectionId(1L);
 scheduleEntity.setDatabaseName("test_schema");
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
 scheduleEntity.setStatus(ScheduleStatus.ENABLED);
 scheduleEntity.setAllowConcurrent(false);
 scheduleEntity.setCreatorId(1L);
 scheduleEntity.setOrganizationId(1L);
 scheduleEntity.setProjectId(1L);
 scheduleEntity.setDatabaseId(1L);
 scheduleEntity.setModifierId(1L);
 scheduleEntity.setTriggerConfigJson(
 "{\"triggerStrategy\":\"CRON\", \"cronExpression\":\"0/10 * * * * ?\", \"misfireStrategy\":\"MISFIRE_INSTRUCTION_DO_NOTHING\"}");
 scheduleEntity
 .setJobParametersJson("{\"parameters\":{\"param1\":\"value1\", \"param2\":\"value2\"}}");
 return scheduleEntity;
 }

 private ScheduleTaskEntity setupScheduleTaskEntity() {
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setStatus(TaskStatus.PREPARING);
 scheduleTaskEntity.setJobName("1");
 scheduleTaskEntity.setJobGroup(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name());
 scheduleTaskEntity.setProgressPercentage(0.0);
 scheduleTaskEntity.setParametersJson(
 "{\"parameters\":{\"param1\":\"value1\", \"param2\":\"value2\"}}");
 scheduleTaskEntity.setResultJson("{\"result\":{\"resultKey1\":\"resultValue1\", \"resultKey2\":\"resultValue2\"}}");
 scheduleTaskEntity.setFireTime(new Date());
 return scheduleTaskEntity;
 }

 private TaskEntity setupTaskEntity() {
 TaskEntity taskEntity = new TaskEntity();
 taskEntity.setId(1L);
 taskEntity.setStatus(TaskStatus.RUNNING);
 taskEntity.setResultJson("{}");
 return taskEntity;
 }
}