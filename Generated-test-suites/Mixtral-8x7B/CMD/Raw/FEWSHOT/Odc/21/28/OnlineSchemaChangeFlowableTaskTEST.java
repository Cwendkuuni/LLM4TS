package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.test.PluggableFlowableTestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.constant.FlowStatus;
import com.oceanbase.odc.common.constant.TaskStatus;
import com.oceanbase.odc.core.shared.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.core.shared.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.core.shared.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.core.shared.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.metadb.task.TaskService;
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

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = OnlineSchemaChangeFlowableTask.class)
public class OnlineSchemaChangeFlowableTaskTest extends PluggableFlowableTestCase {

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

 private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

 private ObjectMapper objectMapper = new ObjectMapper();

 @Before
 public void setUp() {
 onlineSchemaChangeFlowableTask = spy(new OnlineSchemaChangeFlowableTask());
 doReturn(scheduleTaskRepository).when(onlineSchemaChangeFlowableTask).scheduleTaskRepository;
 doReturn(scheduleService).when(onlineSchemaChangeFlowableTask).scheduleService;
 doReturn(scheduleTaskService).when(onlineSchemaChangeFlowableTask).scheduleTaskService;
 doReturn(quartzJobService).when(onlineSchemaChangeFlowableTask).quartzJobService;
 doReturn(taskHandler).when(onlineSchemaChangeFlowableTask).taskHandler;
 doReturn(completeHandler).when(onlineSchemaChangeFlowableTask).completeHandler;
 doReturn(organizationService).when(onlineSchemaChangeFlowableTask).organizationService;
 doReturn(taskService).when(onlineSchemaChangeFlowableTask).taskService;
 }

 @Test
 public void testStart() throws Exception {
 // Prepare mock data
 ConnectionConfig connectionConfig = new ConnectionConfig();
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 OnlineSchemaChangeScheduleTaskParameters subTaskParameters = new OnlineSchemaChangeScheduleTaskParameters();
 ScheduleEntity schedule = new ScheduleEntity();
 schedule.setId(1L);
 schedule.setConnectionId(connectionConfig.id());
 schedule.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
 schedule.setStatus(ScheduleStatus.ENABLED);
 schedule.setAllowConcurrent(false);
 schedule.setCreatorId(1L);
 schedule.setOrganizationId(1L);
 schedule.setProjectId(1L);
 schedule.setDatabaseId(1L);
 schedule.setModifierId(schedule.getCreatorId());
 TriggerConfig triggerConfig = new TriggerConfig();
 triggerConfig.setTriggerStrategy(TriggerStrategy.CRON);
 triggerConfig.setCronExpression("0/10 * * * * ?");
 schedule.setTriggerConfigJson(objectMapper.writeValueAsString(triggerConfig));
 schedule.setMisfireStrategy(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING);
 schedule.setJobParametersJson("{}");

 DelegateExecution execution = new DelegateExecution();
 execution.setVariable("uid", "test_uid");
 execution.setVariable("parameters", parameters);
 execution.setVariable("connectionConfig", connectionConfig);
 execution.setVariable("schema", "test_schema");

 // Call the method to test
 onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

 // Verify the results
 verify(scheduleTaskRepository, times(1)).saveAndFlush(any(ScheduleTaskEntity.class));
 verify(taskHandler, times(1)).start(anyLong(), anyLong());
 }

 @Test
 public void testIsSuccessful() {
 onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);
 assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());

 onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);
 assertFalse(onlineSchemaChangeFlowableTask.isSuccessful());

 onlineSchemaChangeFlowableTask.setStatus(TaskStatus.RUNNING);
 assertFalse(onlineSchemaChangeFlowableTask.isSuccessful());
 }

 @Test
 public void testIsFailure() {
 onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);
 assertFalse(onlineSchemaChangeFlowableTask.isFailure());

 onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);
 assertTrue(onlineSchemaChangeFlowableTask.isFailure());

 onlineSchemaChangeFlowableTask.setStatus(TaskStatus.RUNNING);
 assertFalse(onlineSchemaChangeFlowableTask.isFailure());
 }

 @Test
 public void testOnFailure() {
 onlineSchemaChangeFlowableTask.onFailure(1L, taskService);
 verify(taskService, times(1)).cancel(1L);
 }

 @Test
 public void testOnSuccessful() throws JsonProcessingException {
 onlineSchemaChangeFlowableTask.updateFlowInstanceStatus = (status) -> {
 };

 // Prepare mock data
 OnlineSchemaChangeTaskResult result = new OnlineSchemaChangeTaskResult();
 TaskEntity task = new TaskEntity();
 task.setId(1L);
 task.setStatus(TaskStatus.DONE);
 task.setProgressPercentage(100.0);
 task.setResultJson(objectMapper.writeValueAsString(result));

 // Call the method to test
 onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);

 // Verify the results
 verify(onlineSchemaChangeFlowableTask, times(1)).updateFlowInstanceStatus(FlowStatus.EXECUTION_SUCCEEDED);
 }

 @Test
 public void testOnTimeout() {
 onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);
 verify(taskService, times(0)).cancel(1L);
 }

 @Test
 public void testOnProgressUpdate() throws JsonProcessingException {
 onlineSchemaChangeFlowableTask.updateFlowInstanceStatus = (status) -> {
 };

 // Prepare mock data
 List<ScheduleTaskEntity> tasks = new ArrayList<>();
 tasks.add(new ScheduleTaskEntity());
 tasks.add(new ScheduleTaskEntity());
 Page<ScheduleTaskEntity> page = new PageImpl<>(tasks);
 OnlineSchemaChangeTaskResult result1 = new OnlineSchemaChangeTaskResult();
 OnlineSchemaChangeTaskResult result2 = new OnlineSchemaChangeTaskResult();
 result1.setTaskResult(true);
 result2.setTaskResult(false);
 OnlineSchemaChangeScheduleTaskResult taskResult1 = new OnlineSchemaChangeScheduleTaskResult();
 OnlineSchemaChangeScheduleTaskResult taskResult2 = new OnlineSchemaChangeScheduleTaskResult();
 taskResult1.setParameter(new OnlineSchemaChangeScheduleTaskParameters());
 taskResult2.setParameter(new OnlineSchemaChangeScheduleTaskParameters());
 taskResult1.setResult(result1);
 taskResult2.setResult(result2);
 OnlineSchemaChangeTaskResult overallResult = new OnlineSchemaChangeTaskResult();
 overallResult.getTasks().add(taskResult1);
 overallResult.getTasks().add(taskResult2);

 doReturn(page).when(scheduleTaskService).listTask(any(Pageable.class), anyLong());
 doReturn(SerializationUtils.clone(overallResult)).when(onlineSchemaChangeFlowableTask)
 .singleTaskPercentage(tasks.get(0));
 doReturn(SerializationUtils.clone(overallResult)).when(onlineSchemaChangeFlowableTask)
 .singleTaskPercentage(tasks.get(1));

 // Call the method to test
 onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

 // Verify the results
 verify(taskService, times(1)).detail(1L);
 verify(taskService, times(1)).update(any(TaskEntity.class));
 }

 @Test
 public void testCancel() {
 // Prepare mock data
 ScheduleTaskEntity runningEntity = new ScheduleTaskEntity();
 runningEntity.setStatus(TaskStatus.RUNNING);

 doReturn(Optional.of(runningEntity)).when(scheduleTaskService).listTask(any(Pageable.class), anyLong())
 .stream().filter(task -> task.getStatus() == TaskStatus.RUNNING)
 .findFirst();

 // Call the method to test
 onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);

 // Verify the results
 verify(scheduleTaskRepository, times(1)).delete(runningEntity);
 verify(taskService, times(1)).cancel(1L);
 }
}