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

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskService;

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
 private TaskService taskService;

 @Mock
 private DelegateExecution execution;

 @Mock
 private ObjectMapper objectMapper;

 @Mock
 private JsonUtils jsonUtils;

 @Mock
 private FlowTaskUtil flowTaskUtil;

 @InjectMocks
 private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = spy(new OnlineSchemaChangeFlowableTask());

 private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

 private ConnectionConfig connectionConfig;

 @Before
 public void setup() {
 onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 connectionConfig = new ConnectionConfig();
 connectionConfig.setId(1L);
 }

 // Test start method
 @Test
 public void testStart() throws Exception {
 // Prepare the input parameters
 Long taskId = 1L;
 String uid = "test-uid";
 OnlineSchemaChangeParameters parameter = getOnlineSchemaChangeParameters();
 ConnectionConfig connectionConfig = getConnectionConfig();
 String schema = "test-schema";

 // Set up the mocks
 when(execution.getVariable(OnlineSchemaChangeFlowableTask.TASK_ID)).thenReturn(taskId);
 when(execution.getVariable("uid")).thenReturn(uid);
 when(execution.getVariable("parameters")).thenReturn(parameter);
 when(execution.getVariable("connection")).thenReturn(connectionConfig);
 when(execution.getVariable("schema")).thenReturn(schema);
 when(flowTaskUtil.getCloudMainAccountId(execution)).thenReturn(uid);
 when(jsonUtils.toJson(any())).thenReturn("{}");

 // Call the method under test
 onlineSchemaChangeFlowableTask.start(taskId, taskService, execution);

 // Verify the interactions with mocks
 verify(taskService).start(taskId);
 verify(onlineSchemaChangeFlowableTask).createScheduleEntity(connectionConfig, parameter, schema);
 verify(taskHandler).start(anyLong(), anyLong());
 }

 // Test isSuccessful method
 @Test
 public void testIsSuccessful() {
 OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
 task.setStatus(TaskStatus.DONE);
 assertTrue(task.isSuccessful());

 task.setStatus(TaskStatus.FAILED);
 assertFalse(task.isSuccessful());

 task.setStatus(TaskStatus.RUNNING);
 assertFalse(task.isSuccessful());
 }

 // Test isFailure method
 @Test
 public void testIsFailure() {
 OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
 task.setStatus(TaskStatus.DONE);
 assertFalse(task.isFailure());

 task.setStatus(TaskStatus.FAILED);
 assertTrue(task.isFailure());

 task.setStatus(TaskStatus.RUNNING);
 assertFalse(task.isFailure());
 }

 // Test onFailure method
 @Test
 public void testOnFailure() {
 Long taskId = 1L;
 when(taskService.detail(taskId)).thenReturn(new TaskEntity());

 onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);

 verify(taskService).detail(taskId);
 verify(taskService).cancel(taskId);
 }

 // Test onSuccessful method
 @Test
 public void testOnSuccessful() {
 Long taskId = 1L;
 when(taskService.detail(taskId)).thenReturn(new TaskEntity());

 onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskService);

 verify(taskService).detail(taskId);
 verify(onlineSchemaChangeFlowableTask).updateFlowInstanceStatus(FlowStatus.EXECUTION_SUCCEEDED);
 }

 // Test onTimeout method
 @Test
 public void testOnTimeout() {
 Long taskId = 1L;
 when(taskService.detail(taskId)).thenReturn(new TaskEntity());

 onlineSchemaChangeFlowableTask.onTimeout(taskId, taskService);

 verify(taskService).detail(taskId);
 }

 // Test onProgressUpdate method
 @Test
 public void testOnProgressUpdate() throws JsonProcessingException {
 Long taskId = 1L;
 ScheduleTaskEntity scheduleTask1 = new ScheduleTaskEntity();
 scheduleTask1.setStatus(TaskStatus.PREPARING);
 scheduleTask1.setParametersJson("{}");

 ScheduleTaskEntity scheduleTask2 = new ScheduleTaskEntity();
 scheduleTask2.setStatus(TaskStatus.RUNNING);
 scheduleTask2.setProgressPercentage(50.0);

 ScheduleTaskEntity scheduleTask3 = new ScheduleTaskEntity();
 scheduleTask3.setStatus(TaskStatus.DONE);
 scheduleTask3.setProgressPercentage(100.0);

 Page<ScheduleTaskEntity> tasks = new PageImpl<>(Arrays.asList(scheduleTask1, scheduleTask2, scheduleTask3),
 PageRequest.of(0, 10), 3);

 when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(tasks);
 when(jsonUtils.toJson(any())).thenReturn("{}");
 when(taskService.detail(taskId)).thenReturn(new TaskEntity());

 onlineSchemaChangeFlowableTask.onProgressUpdate(taskId, taskService);

 verify(scheduleTaskService).listTask(any(Pageable.class), anyLong());
 verify(taskService).detail(taskId);
 }

 // Test cancel method
 @Test
 public void testCancel() {
 Long taskId = 1L;
 when(taskService.detail(taskId)).thenReturn(new TaskEntity());

 doNothing().when(taskHandler).terminate(anyLong(), anyLong());

 onlineSchemaChangeFlowableTask.cancel(true, taskId, taskService);

 verify(taskService).detail(taskId);
 verify(taskHandler).terminate(anyLong(), anyLong());
 verify(taskService).cancel(taskId);
 }

 // Test isCancelled method
 @Test
 public void testIsCancelled() {
 OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
 task.setStatus(TaskStatus.DONE);
 assertFalse(task.isCancelled());

 task.setStatus(TaskStatus.CANCELED);
 assertTrue(task.isCancelled());
 }

 // Test createScheduleEntity method
 @Test
 public void testCreateScheduleEntity() throws JsonProcessingException {
 OnlineSchemaChangeParameters parameter = getOnlineSchemaChangeParameters();
 String parameterJson = jsonUtils.toJson(parameter);

 ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameter,
 "test-schema");

 assertEquals(connectionConfig.getId(), scheduleEntity.getConnectionId());
 assertEquals("test-schema", scheduleEntity.getDatabaseName());
 assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());
 assertEquals(ScheduleStatus.ENABLED, scheduleEntity.getStatus());
 assertEquals(false, scheduleEntity.getAllowConcurrent());
 assertEquals(1L, scheduleEntity.getCreatorId());
 assertEquals(1L, scheduleEntity.getOrganizationId());
 assertEquals(1L, scheduleEntity.getProjectId());
 assertEquals(1L, scheduleEntity.getDatabaseId());
 assertEquals(1L, scheduleEntity.getModifierId());

 TriggerConfig triggerConfig = new TriggerConfig();
 triggerConfig.setTriggerStrategy(TriggerStrategy.CRON);
 triggerConfig.setCronExpression("0/10 * * * * ?");
 triggerConfig.setMisfireStrategy("MISFIRE_INSTRUCTION_DO_NOTHING");
 JsonNode triggerConfigJson = objectMapper.readTree(triggerConfig.toString());

 assertEquals(triggerConfigJson, new ObjectMapper().readTree(scheduleEntity.getTriggerConfigJson()));

 OnlineSchemaChangeParameters deserializedParameter = objectMapper.readValue(parameterJson,
 OnlineSchemaChangeParameters.class);

 assertEquals(parameter, deserializedParameter);
 }

 // Test createScheduleTaskEntity method
 @Test
 public void testCreateScheduleTaskEntity() throws JsonProcessingException {
 OnlineSchemaChangeScheduleTaskParameters parameters = new OnlineSchemaChangeScheduleTaskParameters();
 parameters.setUid("test-uid");

 ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(1L, parameters);

 assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
 assertEquals("1", scheduleTaskEntity.getJobName());
 assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name(), scheduleTaskEntity.getJobGroup());
 assertEquals(0.0, scheduleTaskEntity.getProgressPercentage(), 0.001);
 assertEquals(JsonUtils.toJson(parameters), scheduleTaskEntity.getParametersJson());
 assertEquals(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(parameters)),
 scheduleTaskEntity.getResultJson());
 assertEquals(new Date(), scheduleTaskEntity.getFireTime());
 }

 private OnlineSchemaChangeParameters getOnlineSchemaChangeParameters() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 return parameters;
 }

 private ConnectionConfig getConnectionConfig() {
 return connectionConfig;
 }
}