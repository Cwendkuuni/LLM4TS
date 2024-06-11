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
import org.apache.commons.lang3.tuple.Triple;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.constant.FlowStatus;
import com.oceanbase.odc.common.constant.TaskStatus;
import com.oceanbase.odc.core.shared.constant.OnlineSchemaChangeTaskResultStatus;
import com.oceanbase.odc.core.shared.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.core.shared.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.core.shared.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.core.shared.model.OnlineSchemaChangeTaskResultStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.quartz.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.TaskResult;

@SuppressWarnings("unchecked")
public class OnlineSchemaChangeFlowableTaskTest {

 private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

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
 private DelegateExecution delegateExecution;

 private ObjectMapper objectMapper = new ObjectMapper();

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 onlineSchemaChangeFlowableTask = spy(new OnlineSchemaChangeFlowableTask());
 onlineSchemaChangeFlowableTask.setScheduleTaskRepository(scheduleTaskRepository);
 onlineSchemaChangeFlowableTask.setScheduleService(scheduleService);
 onlineSchemaChangeFlowableTask.setScheduleTaskService(scheduleTaskService);
 onlineSchemaChangeFlowableTask.setQuartzJobService(quartzJobService);
 onlineSchemaChangeFlowableTask.setTaskHandler(taskHandler);
 onlineSchemaChangeFlowableTask.setCompleteHandler(completeHandler);
 onlineSchemaChangeFlowableTask.setOrganizationService(organizationService);
 onlineSchemaChangeFlowableTask.setTaskService(taskService);
 }

 @Test
 public void testStart() throws Exception {
 Long taskId = 1L;
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setUid("uid");
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "schema";
 when(delegateExecution.getVariable(OnlineSchemaChangeFlowableTask.ONLINE_SCHEMA_CHANGE_PARAMETER))
 .thenReturn(parameters);
 when(delegateExecution.getVariable(OnlineSchemaChangeFlowableTask.CONNECTION_CONFIG))
 .thenReturn(connectionConfig);
 when(delegateExecution.getVariable(OnlineSchemaChangeFlowableTask.SCHEMA))
 .thenReturn(schema);
 when(delegateExecution.getVariable(BaseODCFlowTaskDelegate.TASK_ID))
 .thenReturn(taskId);
 doReturn(1L).when(onlineSchemaChangeFlowableTask).createScheduleEntity(connectionConfig, parameters, schema);

 onlineSchemaChangeFlowableTask.start(taskId, taskService, delegateExecution);

 verify(scheduleTaskRepository, times(1)).saveAndFlush(any(ScheduleTaskEntity.class));
 verify(taskHandler, times(1)).start(anyLong(), anyLong());
 }

 @Test
 public void testIsSuccessful() {
 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTaskSpy = spy(onlineSchemaChangeFlowableTask);
 onlineSchemaChangeFlowableTaskSpy.setStatus(TaskStatus.DONE);

 assertTrue(onlineSchemaChangeFlowableTaskSpy.isSuccessful());

 onlineSchemaChangeFlowableTaskSpy.setStatus(TaskStatus.FAILED);

 assertFalse(onlineSchemaChangeFlowableTaskSpy.isSuccessful());
 }

 @Test
 public void testIsFailure() {
 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTaskSpy = spy(onlineSchemaChangeFlowableTask);
 onlineSchemaChangeFlowableTaskSpy.setStatus(TaskStatus.DONE);

 assertFalse(onlineSchemaChangeFlowableTaskSpy.isFailure());

 onlineSchemaChangeFlowableTaskSpy.setStatus(TaskStatus.FAILED);

 assertTrue(onlineSchemaChangeFlowableTaskSpy.isFailure());
 }

 @Test
 public void testOnFailure() {
 Long taskId = 1L;
 onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);

 verify(taskService, times(1)).cancel(taskId);
 }

 @Test
 public void testOnSuccessful() throws JsonProcessingException {
 Long taskId = 1L;
 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTaskSpy = spy(onlineSchemaChangeFlowableTask);
 OnlineSchemaChangeTaskResult taskResult = new OnlineSchemaChangeTaskResult();
 String taskResultJson = objectMapper.writeValueAsString(taskResult);
 TaskEntity taskEntity = new TaskEntity();
 taskEntity.setId(taskId);
 taskEntity.setResultJson(taskResultJson);
 Page<TaskEntity> taskEntityPage = new PageImpl<>(List.of(taskEntity));
 when(taskService.list(any(Pageable.class)))
 .thenReturn(taskEntityPage);
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setUid("uid");
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "schema";
 when(delegateExecution.getVariable(OnlineSchemaChangeFlowableTask.ONLINE_SCHEMA_CHANGE_PARAMETER))
 .thenReturn(parameters);
 when(delegateExecution.getVariable(OnlineSchemaChangeFlowableTask.CONNECTION_CONFIG))
 .thenReturn(connectionConfig);
 when(delegateExecution.getVariable(OnlineSchemaChangeFlowableTask.SCHEMA))
 .thenReturn(schema);
 when(delegateExecution.getVariable(BaseODCFlowTaskDelegate.TASK_ID))
 .thenReturn(taskId);

 onlineSchemaChangeFlowableTaskSpy.onSuccessful(taskId, taskService);

 verify(scheduleService, times(1)).updateStatus(anyLong(), any(ScheduleStatus.class));
 }

 @Test
 public void testOnTimeout() {
 Long taskId = 1L;
 onlineSchemaChangeFlowableTask.onTimeout(taskId, taskService);

 verify(taskService, times(1)).cancel(taskId);
 }

 @Test
 public void testOnProgressUpdate() throws JsonProcessingException {
 Long taskId = 1L;
 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTaskSpy = spy(onlineSchemaChangeFlowableTask);
 OnlineSchemaChangeTaskResult taskResult = new OnlineSchemaChangeTaskResult();
 OnlineSchemaChangeTaskResultStatus taskResultStatus = new OnlineSchemaChangeTaskResultStatus();
 taskResultStatus.setStatus(OnlineSchemaChangeTaskResultStatus.Status.COMPLETED);
 taskResult.setStatus(taskResultStatus);
 String taskResultJson = objectMapper.writeValueAsString(taskResult);
 TaskEntity taskEntity = new TaskEntity();
 taskEntity.setId(taskId);
 taskEntity.setResultJson(taskResultJson);
 Page<TaskEntity> taskEntityPage = new PageImpl<>(List.of(taskEntity));
 when(taskService.list(any(Pageable.class)))
 .thenReturn(taskEntityPage);
 when(delegateExecution.getVariable(OnlineSchemaChangeFlowableTask.ONLINE_SCHEMA_CHANGE_PARAMETER))
 .thenReturn(new OnlineSchemaChangeParameters());
 when(delegateExecution.getVariable(OnlineSchemaChangeFlowableTask.CONNECTION_CONFIG))
 .thenReturn(new ConnectionConfig());
 when(delegateExecution.getVariable(OnlineSchemaChangeFlowableTask.SCHEMA))
 .thenReturn("schema");
 when(delegateExecution.getVariable(BaseODCFlowTaskDelegate.TASK_ID))
 .thenReturn(taskId);

 onlineSchemaChangeFlowableTaskSpy.onProgressUpdate(taskId, taskService);

 verify(taskService, times(1)).updateProgressPercentage(taskId, 100);
 }

 @Test
 public void testCancel() {
 Long taskId = 1L;
 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTaskSpy = spy(onlineSchemaChangeFlowableTask);
 doNothing().when(onlineSchemaChangeFlowableTaskSpy)
 .terminateScheduleTask(anyLong(), anyLong());

 assertTrue(onlineSchemaChangeFlowableTaskSpy.cancel(false, taskId, taskService));

 verify(scheduleTaskRepository, times(1)).deleteById(anyLong());
 verify(taskService, times(1)).cancel(taskId);
 }

 @Test
 public void testCreateScheduleEntity() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 String schema = "schema";

 ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameters,
 schema);

 assertEquals(ScheduleStatus.ENABLED, scheduleEntity.getStatus());
 assertEquals(1L, scheduleEntity.getProjectId());
 assertEquals(1L, scheduleEntity.getDatabaseId());
 assertEquals(TriggerStrategy.CRON, scheduleEntity.getTriggerConfig().getTriggerStrategy());
 }

 @Test
 public void testCreateScheduleTaskEntity() {
 OnlineSchemaChangeScheduleTaskParameters parameters = new OnlineSchemaChangeScheduleTaskParameters();

 ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(1L, parameters);

 assertEquals(TaskStatus.PREPARING, scheduleTaskEntity.getStatus());
 }

 @Test
 public void testTerminateScheduleTask() {
 String jobName = "jobName";
 Long taskId = 1L;

 onlineSchemaChangeFlowableTask.terminateScheduleTask(1L, taskId);

 verify(quartzJobService, times(1)).deleteJob(jobName);
 }
}