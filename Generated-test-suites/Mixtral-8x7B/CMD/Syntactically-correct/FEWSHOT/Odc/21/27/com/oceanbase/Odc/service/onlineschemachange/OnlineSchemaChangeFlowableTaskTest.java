package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.Expression;
import org.flowable.engine.delegate.VariableScope;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.constant.FlowStatus;
import com.oceanbase.odc.common.constant.TaskStatus;
import com.oceanbase.odc.core.shared.constant.FlowVariableKey;
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

public class OnlineSchemaChangeFlowableTaskTest extends BaseODCFlowTaskDelegateTest {

private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
private ScheduleTaskRepository scheduleTaskRepository;
private ScheduleService scheduleService;
private ScheduleTaskService scheduleTaskService;
private QuartzJobService quartzJobService;
private OnlineSchemaChangeTaskHandler taskHandler;
private OscTaskCompleteHandler completeHandler;
private OrganizationService organizationService;

@Before
public void setup() {
onlineSchemaChangeFlowableTask = spy(new OnlineSchemaChangeFlowableTask());
scheduleTaskRepository = mock(ScheduleTaskRepository.class);
scheduleService = mock(ScheduleService.class);
scheduleTaskService = mock(ScheduleTaskService.class);
quartzJobService = mock(QuartzJobService.class);
taskHandler = mock(OnlineSchemaChangeTaskHandler.class);
completeHandler = mock(OscTaskCompleteHandler.class);
organizationService = mock(OrganizationService.class);

onlineSchemaChangeFlowableTask.scheduleTaskRepository = scheduleTaskRepository;
onlineSchemaChangeFlowableTask.scheduleService = scheduleService;
onlineSchemaChangeFlowableTask.scheduleTaskService = scheduleTaskService;
onlineSchemaChangeFlowableTask.quartzJobService = quartzJobService;
onlineSchemaChangeFlowableTask.taskHandler = taskHandler;
onlineSchemaChangeFlowableTask.completeHandler = completeHandler;
onlineSchemaChangeFlowableTask.organizationService = organizationService;
}

// Test start method
@Test
public void testStart() throws Exception {
// Prepare mock data
DelegateExecution execution = createMockDelegateExecution();
TaskService taskService = createMockTaskService();
Long taskId = 1L;
ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
OnlineSchemaChangeParameters parameter = mock(OnlineSchemaChangeParameters.class);
String schema = "test_schema";
doReturn(1L).when(connectionConfig).id();
doReturn(parameter).when(onlineSchemaChangeFlowableTask)
.getOnlineSchemaChangeParameter(execution);
doReturn(connectionConfig).when(onlineSchemaChangeFlowableTask)
.getConnectionConfig(execution);
doReturn(schema).when(onlineSchemaChangeFlowableTask)
.getSchemaName(execution);

// Call method under test
onlineSchemaChangeFlowableTask.start(taskId, taskService, execution);

// Verify the result
verify(taskService).start(taskId);
verify(onlineSchemaChangeFlowableTask).createScheduleEntity(connectionConfig, parameter, schema);
verify(onlineSchemaChangeFlowableTask).createScheduleTaskEntity(anyLong(), any(OnlineSchemaChangeScheduleTaskParameters.class));
}

// Test isSuccessful method
@Test
public void testIsSuccessful() {
onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;
assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());

onlineSchemaChangeFlowableTask.status = TaskStatus.RUNNING;
assertFalse(onlineSchemaChangeFlowableTask.isSuccessful());
}

// Test isFailure method
@Test
public void testIsFailure() {
onlineSchemaChangeFlowableTask.status = TaskStatus.FAILED;
assertTrue(onlineSchemaChangeFlowableTask.isFailure());

onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;
assertFalse(onlineSchemaChangeFlowableTask.isFailure());
}

// Test onFailure method
@Test
public void testOnFailure() {
Long taskId = 1L;
TaskService taskService = createMockTaskService();

onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);

verify(taskService).fail(taskId);
}

// Test onSuccessful method
@Test
public void testOnSuccessful() throws JsonProcessingException {
Long taskId = 1L;
TaskService taskService = createMockTaskService();
FlowStatus flowStatus = FlowStatus.EXECUTION_SUCCEEDED;

onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskService);

verify(taskService).complete(taskId);
verify(onlineSchemaChangeFlowableTask).updateFlowInstanceStatus(flowStatus);
}

// Test onTimeout method
@Test
public void testOnTimeout() {
Long taskId = 1L;
TaskService taskService = createMockTaskService();

onlineSchemaChangeFlowableTask.onTimeout(taskId, taskService);

verify(taskService).timeout(taskId);
}

// Test onProgressUpdate method
@Test
public void testOnProgressUpdate() throws JsonProcessingException {
Long taskId = 1L;
TaskService taskService = createMockTaskService();
Expression expression = mock(Expression.class);
TaskEntity taskEntity = new TaskEntity();
taskEntity.setStatus(TaskStatus.RUNNING);
VariableScope variableScope = mock(VariableScope.class);
when(variableScope.getExpression(FlowVariableKey.TASK.getValue())).thenReturn(expression);
when(expression.getValue(variableScope)).thenReturn(taskEntity);

onlineSchemaChangeFlowableTask.onProgressUpdate(taskId, taskService);

verify(taskService, times(1)).update(taskEntity);
}

// Test progressStatusUpdate method
@Test
public void testProgressStatusUpdate() {
List<ScheduleTaskEntity> scheduleTaskEntities = new ArrayList<>();
ScheduleTaskEntity scheduleTaskEntity1 = new ScheduleTaskEntity();
scheduleTaskEntity1.setStatus(TaskStatus.DONE);
scheduleTaskEntity1.setId(1L);
scheduleTaskEntities.add(scheduleTaskEntity1);
ScheduleTaskEntity scheduleTaskEntity2 = new ScheduleTaskEntity();
scheduleTaskEntity2.setStatus(TaskStatus.FAILED);
scheduleTaskEntity2.setId(2L);
scheduleTaskEntities.add(scheduleTaskEntity2);
ScheduleTaskEntity scheduleTaskEntity3 = new ScheduleTaskEntity();
scheduleTaskEntity3.setStatus(TaskStatus.CANCELED);
scheduleTaskEntity3.setId(3L);
scheduleTaskEntities.add(scheduleTaskEntity3);

onlineSchemaChangeFlowableTask.progressStatusUpdate(scheduleTaskEntities);

assertTrue(onlineSchemaChangeFlowableTask.status == TaskStatus.CANCELED);
}

// Test singleTaskPercentage method
@Test
public void testSingleTaskPercentage() {
ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
scheduleTaskEntity.setStatus(TaskStatus.PREPARING);
assertEquals(0D, onlineSchemaChangeFlowableTask.singleTaskPercentage(scheduleTaskEntity), 0.01);

scheduleTaskEntity.setStatus(TaskStatus.RUNNING);
scheduleTaskEntity.setProgressPercentage(50);
assertEquals(0.5D, onlineSchemaChangeFlowableTask.singleTaskPercentage(scheduleTaskEntity), 0.01);

scheduleTaskEntity.setStatus(TaskStatus.COMPLETED);
scheduleTaskEntity.setProgressPercentage(100);
assertEquals(1D, onlineSchemaChangeFlowableTask.singleTaskPercentage(scheduleTaskEntity), 0.01);
}

// Test createScheduleEntity method
@Test
public void testCreateScheduleEntity() throws JsonProcessingException {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setId(1L);
OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
String schema = "test_schema";
ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig,
onlineSchemaChangeParameters, schema);

verify(scheduleEntity).setConnectionId(1L);
verify(scheduleEntity).setDatabaseName(schema);
verify(scheduleEntity).setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
verify(scheduleEntity).setStatus(ScheduleStatus.ENABLED);
verify(scheduleEntity).setAllowConcurrent(false);
verify(scheduleEntity).setCreatorId(1L);
verify(scheduleEntity).setOrganizationId(1L);
verify(scheduleEntity).setProjectId(1L);
verify(scheduleEntity).setDatabaseId(1L);
verify(scheduleEntity).setModifierId(1L);

ObjectMapper objectMapper = new ObjectMapper();
JsonNode expectedTriggerConfigJson = objectMapper.readTree("{\"triggerStrategy\":\"CRON\", \"cronExpression\":\"0/10 * * * * ?\"}");
assertEquals(expectedTriggerConfigJson, scheduleEntity.getTriggerConfigJson());
}

// Test createScheduleTaskEntity method
@Test
public void testCreateScheduleTaskEntity() {
OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters = new OnlineSchemaChangeScheduleTaskParameters();
ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(1L,
onlineSchemaChangeScheduleTaskParameters);

verify(scheduleTaskEntity).setStatus(TaskStatus.PREPARING);
verify(scheduleTaskEntity).setJobName("1");
verify(scheduleTaskEntity).setJobGroup(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name());
verify(scheduleTaskEntity).setProgressPercentage(0.0);
verify(scheduleTaskEntity).setFireTime(any(Date.class));

ObjectMapper objectMapper = new ObjectMapper();
JsonNode expectedParametersJson = objectMapper.readTree("{\"parameter\":\"parameter\"}");
assertEquals(expectedParametersJson, scheduleTaskEntity.getParametersJson());
}

// Test cancel method
@Test
public void testCancel() {
Long taskId = 1L;
TaskService taskService = createMockTaskService();
doNothing().when(taskHandler).terminate(anyLong(), anyLong());

onlineSchemaChangeFlowableTask.cancel(true, taskId, taskService);

verify(taskService).cancel(taskId);
verify(taskHandler).terminate(anyLong(), anyLong());
}

// Test isCancelled method
@Test
public void testIsCancelled() {
onlineSchemaChangeFlowableTask.status = TaskStatus.CANCELED;
assertTrue(onlineSchemaChangeFlowableTask.isCancelled());

onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;
assertFalse(onlineSchemaChangeFlowableTask.isCancelled());
}
}