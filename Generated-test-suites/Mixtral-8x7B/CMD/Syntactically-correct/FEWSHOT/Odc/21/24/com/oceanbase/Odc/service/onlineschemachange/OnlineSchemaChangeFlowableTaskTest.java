package com.oceanbase.odc.service.onlineschemachange;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
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
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.Expression;
import org.flowable.engine.delegate.VariableScope;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskServiceImpl;

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

 @Mock
 private TaskRepository taskRepository;

 @Mock
 private DelegateExecution execution;

 @Mock
 private TaskService taskService;

 @Mock
 private ConnectionConfig connectionConfig;

 @Mock
 private OnlineSchemaChangeParameters parameter;

 @Mock
 private ScheduleEntity scheduleEntity;

 @Mock
 private ScheduleTaskEntity scheduleTaskEntity;

 @Mock
 private TaskEntity taskEntity;

 @Mock
 private User user;

 @Mock
 private Expression expression;

 @InjectMocks
 private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = spy(new OnlineSchemaChangeFlowableTask());

 @Before
 public void setUp() {
 when(execution.getVariable(OnlineSchemaChangeFlowableTask.TASK_ID)).thenReturn(1L);
 when(execution.getVariable(OnlineSchemaChangeFlowableTask.UID)).thenReturn("uid");
 when(execution.getVariable(OnlineSchemaChangeFlowableTask.CONNECTION_CONFIG)).thenReturn(connectionConfig);
 when(execution.getVariable(OnlineSchemaChangeFlowableTask.ONLINE_SCHEMA_CHANGE_PARAMETERS))
 .thenReturn(parameter);
 when(execution.getVariable(OnlineSchemaChangeFlowableTask.SCHEMA)).thenReturn("schema");
 when(execution.getVariable(OnlineSchemaChangeFlowableTask.FLOW_STATUS)).thenReturn(expression);
 when(execution.getVariable(OnlineSchemaChangeFlowableTask.TASK_STATUS)).thenReturn(expression);
 when(execution.getVariable(OnlineSchemaChangeFlowableTask.PROGRESS_PERCENTAGE)).thenReturn(expression);
 when(onlineSchemaChangeFlowableTask.getTaskService()).thenReturn(taskService);
 onlineSchemaChangeFlowableTask.setScheduleTaskRepository(scheduleTaskRepository);
 onlineSchemaChangeFlowableTask.setScheduleService(scheduleService);
 onlineSchemaChangeFlowableTask.setScheduleTaskService(scheduleTaskService);
 onlineSchemaChangeFlowableTask.setQuartzJobService(quartzJobService);
 onlineSchemaChangeFlowableTask.setTaskHandler(taskHandler);
 onlineSchemaChangeFlowableTask.setCompleteHandler(completeHandler);
 onlineSchemaChangeFlowableTask.setOrganizationService(organizationService);
 onlineSchemaChangeFlowableTask.setTaskRepository(taskRepository);
 }

 @Test
 public void testStart() throws Exception {
 doNothing().when(taskService).start((Long) any());
 when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(user);
 when(parameter.generateSubTaskParameters(connectionConfig, "schema")).thenReturn(new ArrayList<>());
 when(onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, "schema"))
 .thenReturn(scheduleEntity);
 when(scheduleTaskRepository.saveAndFlush(any())).thenReturn(scheduleTaskEntity);

 onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

 verify(taskService, times(1)).start((Long) any());
 verify(FlowTaskUtil, times(1)).getTaskCreator(execution);
 verify(parameter, times(1)).generateSubTaskParameters(connectionConfig, "schema");
 verify(onlineSchemaChangeFlowableTask, times(1)).createScheduleEntity(connectionConfig, parameter, "schema");
 verify(scheduleTaskRepository, times(1)).saveAndFlush(any());
 }

 @Test
 public void testIsSuccessful() {
 onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);

 boolean result = onlineSchemaChangeFlowableTask.isSuccessful();

 assertTrue(result);
 }

 @Test
 public void testIsFailure() {
 onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);

 boolean result = onlineSchemaChangeFlowableTask.isFailure();

 assertTrue(result);
 }

 @Test
 public void testOnFailure() {
 onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);

 onlineSchemaChangeFlowableTask.onFailure(1L, taskService);

 verify(taskService, times(1)).cancel(1L);
 }

 @Test
 public void testOnSuccessful() {
 onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);

 onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);

 verify(onlineSchemaChangeFlowableTask, times(1)).updateFlowInstanceStatus(FlowStatus.EXECUTION_SUCCEEDED);
 verify(taskService, times(1)).complete(1L);
 }

 @Test
 public void testOnTimeout() {
 onlineSchemaChangeFlowableTask.setStatus(TaskStatus.TIMED_OUT);

 onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);

 verify(onlineSchemaChangeFlowableTask, times(1)).updateFlowInstanceStatus(FlowStatus.EXECUTION_TIMEOUT);
 }

 @Test
 public void testOnProgressUpdate() {
 ArgumentCaptor<Double> progressPercentageCaptor = ArgumentCaptor.forClass(Double.class);
 ArgumentCaptor<String> resultJsonCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<TaskStatus> statusCaptor = ArgumentCaptor.forClass(TaskStatus.class);

 Page<ScheduleTaskEntity> tasks = mock(Page.class);
 when(tasks.getContent()).thenReturn(new ArrayList<>());
 when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(tasks);

 onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

 verify(taskService, times(1)).update(any(TaskEntity.class));
 verify(tasks, times(1)).getContent();
 verify(onlineSchemaChangeFlowableTask, times(1))
 .progressStatusUpdate(any(List.class));

 onlineSchemaChangeFlowableTask.setStatus(TaskStatus.RUNNING);
 onlineSchemaChangeFlowableTask.setPercentage(50.0);

 OnlineSchemaChangeTaskResult onlineSchemaChangeTaskResult = new OnlineSchemaChangeTaskResult();
 onlineSchemaChangeTaskResult.setTasks(new ArrayList<>());
 String resultJson = JsonUtils.toJson(onlineSchemaChangeTaskResult);
 when(taskEntity.getResultJson()).thenReturn(resultJson);
 when(taskRepository.detail(1L)).thenReturn(taskEntity);

 onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

 verify(taskService, times(2)).update(any(TaskEntity.class));
 verify(taskEntity, times(1)).getResultJson();
 verify(taskRepository, times(1)).detail(1L);

 onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);

 onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

 verify(taskService, times(3)).update(any(TaskEntity.class));
 }

 @Test
 public void testCancel() {
 when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(mock(Page.class));

 onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);

 verify(taskService, times(1)).cancel(1L);
 }

 @Test
 public void testIsCancelled() {
 onlineSchemaChangeFlowableTask.setStatus(TaskStatus.CANCELED);

 boolean result = onlineSchemaChangeFlowableTask.isCancelled();

 assertTrue(result);
 }

 @Test
 public void testCreateScheduleEntity() {
 ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter, "schema");

 verify(scheduleEntity, times(1)).setConnectionId(anyLong());
 verify(scheduleEntity, times(1)).setDatabaseName(anyString());
 verify(scheduleEntity, times(1)).setJobType(any(JobType.class));
 verify(scheduleEntity, times(1)).setStatus(any(ScheduleStatus.class));
 verify(scheduleEntity, times(1)).setAllowConcurrent(anyBoolean());
 verify(scheduleEntity, times(1)).setCreatorId(anyLong());
 verify(scheduleEntity, times(1)).setOrganizationId(anyLong());
 verify(scheduleEntity, times(1)).setProjectId(anyLong());
 verify(scheduleEntity, times(1)).setDatabaseId(anyLong());
 verify(scheduleEntity, times(1)).setModifierId(anyLong());
 verify(scheduleEntity, times(1)).setJobParametersJson(anyString());
 verify(scheduleService, times(1)).create(scheduleEntity);
 }

 @Test
 public void testCreateScheduleTaskEntity() {
 ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(1L,
 new OnlineSchemaChangeScheduleTaskParameters());

 verify(scheduleTaskEntity, times(1)).setStatus(any(TaskStatus.class));
 verify(scheduleTaskEntity, times(1)).setJobName(anyString());
 verify(scheduleTaskEntity, times(1)).setJobGroup(anyString());
 verify(scheduleTaskEntity, times(1)).setProgressPercentage(anyDouble());
 verify(scheduleTaskEntity, times(1)).setParametersJson(anyString());
 verify(scheduleTaskEntity, times(1)).setResultJson(anyString());
 verify(scheduleTaskEntity, times(1)).setFireTime(any(Date.class));
 verify(scheduleTaskRepository, times(1)).saveAndFlush(any());
 }

 private OnlineSchemaChangeParameters getOnlineSchemaChangeParameters() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 return parameters;
 }

 private ScheduleEntity getScheduleEntity() {
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 return scheduleEntity;
 }

 private ScheduleTaskEntity getScheduleTaskEntity() {
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 return scheduleTaskEntity;
 }

 private TaskEntity getTaskEntity() {
 TaskEntity taskEntity = new TaskEntity();
 return taskEntity;
 }

 private User getUser() {
 User user = new User();
 return user;
 }

}