import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskService;

public class OnlineSchemaChangeFlowableTaskTest {

private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

@BeforeEach
public void setUp() {
onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
}

@Test
public void testStart() {
// Mocking dependencies
TaskService taskService = mock(TaskService.class);
DelegateExecution execution = mock(DelegateExecution.class);
ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
OnlineSchemaChangeParameters parameter = mock(OnlineSchemaChangeParameters.class);
ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);
OnlineSchemaChangeTaskHandler taskHandler = mock(OnlineSchemaChangeTaskHandler.class);
OscTaskCompleteHandler completeHandler = mock(OscTaskCompleteHandler.class);
OrganizationService organizationService = mock(OrganizationService.class);
ScheduleService scheduleService = mock(ScheduleService.class);
ScheduleTaskService scheduleTaskService = mock(ScheduleTaskService.class);
QuartzJobService quartzJobService = mock(QuartzJobService.class);

// Setting up mocks
when(execution.getVariable(BaseODCFlowTaskDelegate.TASK_ID)).thenReturn(1L);
when(execution.getVariable(BaseODCFlowTaskDelegate.CLOUD_MAIN_ACCOUNT_ID)).thenReturn("uid");
when(execution.getVariable(BaseODCFlowTaskDelegate.ONLINE_SCHEMA_CHANGE_PARAMETERS))
.thenReturn(parameter);
when(execution.getVariable(BaseODCFlowTaskDelegate.CONNECTION_CONFIG)).thenReturn(connectionConfig);
when(execution.getVariable(BaseODCFlowTaskDelegate.SCHEMA_NAME)).thenReturn("schema");
when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(mock(User.class));
when(parameter.generateSubTaskParameters(connectionConfig, "schema"))
.thenReturn(new ArrayList<>());
when(scheduleEntity.getId()).thenReturn(1L);
when(scheduleService.create(scheduleEntity)).thenReturn(scheduleEntity);
when(scheduleTaskRepository.saveAndFlush(scheduleTaskEntity)).thenReturn(scheduleTaskEntity);

// Calling method
onlineSchemaChangeFlowableTask.start(1L, taskService, execution);
}

@Test
public void testIsSuccessful() {
OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
}

@Test
public void testIsFailure() {
OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
assertFalse(onlineSchemaChangeFlowableTask.isFailure());
}

@Test
public void testOnFailure() {
TaskService taskService = mock(TaskService.class);
onlineSchemaChangeFlowableTask.onFailure(1L, taskService);
}

@Test
public void testOnSuccessful() {
TaskService taskService = mock(TaskService.class);
onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);
}

@Test
public void testOnTimeout() {
TaskService taskService = mock(TaskService.class);
onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);
}

@Test
public void testOnProgressUpdate() {
TaskService taskService = mock(TaskService.class);
Page<ScheduleTaskEntity> tasks = new PageImpl<>(new ArrayList<>());
when(scheduleTaskService.listTask(any(Pageable.class), anyLong()))
.thenReturn(tasks);
onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);
}

@Test
public void testCancel() {
onlineSchemaChangeFlowableTask.cancel(true, 1L, mock(TaskService.class));
}

@Test
public void testIsCancelled() {
OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
assertFalse(onlineSchemaChangeFlowableTask.isCancelled());
}

private ScheduleTaskRepository mockScheduleTaskRepository() {
ScheduleTaskRepository repository = mock(ScheduleTaskRepository.class);
ScheduleTaskEntity entity = new ScheduleTaskEntity();
entity.setId(1L);
when(repository.saveAndFlush(any())).thenReturn(entity);
return repository;
}

private TaskService mockTaskService() {
TaskService service = mock(TaskService.class);
TaskEntity entity = new TaskEntity();
entity.setId(1L);
entity.setStatus(TaskStatus.RUNNING);
when(service.start(any())).thenReturn(entity);
when(service.detail(any())).thenReturn(entity);
when(service.cancel(any())).thenReturn(true);
return service;
}

private ScheduleEntity createMockScheduleEntity() {
ScheduleEntity entity = new ScheduleEntity();
entity.setId(1L);
entity.setConnectionId(1L);
entity.setDatabaseName("databaseName");
entity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
entity.setStatus(ScheduleStatus.ENABLED);
entity.setAllowConcurrent(false);
entity.setCreatorId(1L);
entity.setOrganizationId(1L);
entity.setProjectId(1L);
entity.setDatabaseId(1L);
entity.setModifierId(1L);
entity.setTriggerConfigJson(JsonUtils.toJson(new TriggerConfig()));
entity.setMisfireStrategy(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING);
entity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
return entity;
}

private ScheduleTaskEntity createMockScheduleTaskEntity() {
ScheduleTaskEntity entity = new ScheduleTaskEntity();
entity.setId(1L);
entity.setStatus(TaskStatus.PREPARING);
entity.setJobName("jobName");
entity.setJobGroup(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name());
entity.setProgressPercentage(0.0);
entity.setParametersJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskParameters()));
entity.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));
entity.setFireTime(new Date());
return entity;
}
}