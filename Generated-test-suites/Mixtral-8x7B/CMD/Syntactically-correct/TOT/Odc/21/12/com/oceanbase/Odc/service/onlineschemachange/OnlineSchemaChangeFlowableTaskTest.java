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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.test.util.ReflectionTestUtils;

import com.alibaba.fastjson.JSON;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.OnlineSchemaChangeTaskHandler;
import com.oceanbase.odc.service.flow.task.OscTaskCompleteHandler;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;

public class OnlineSchemaChangeFlowableTaskTest {

private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
private TaskService taskService;
private Task task;
private DelegateExecution delegateExecution;
private ConnectionConfig connectionConfig;
private OnlineSchemaChangeParameters onlineSchemaChangeParameters;
private ScheduleTaskRepository scheduleTaskRepository;
private ScheduleEntity scheduleEntity;
private ScheduleTaskEntity scheduleTaskEntity;
private OnlineSchemaChangeTaskHandler onlineSchemaChangeTaskHandler;
private OscTaskCompleteHandler oscTaskCompleteHandler;
private OrganizationService organizationService;
private QuartzJobService quartzJobService;
private ScheduleService scheduleService;
private ScheduleTaskService scheduleTaskService;

@Before
public void setUp() throws Exception {
taskService = mock(TaskService.class);
task = mock(Task.class);
delegateExecution = mock(DelegateExecution.class);
connectionConfig = mock(ConnectionConfig.class);
onlineSchemaChangeParameters = mock(OnlineSchemaChangeParameters.class);
scheduleTaskRepository = mock(ScheduleTaskRepository.class);
scheduleEntity = mock(ScheduleEntity.class);
scheduleTaskEntity = mock(ScheduleTaskEntity.class);
onlineSchemaChangeTaskHandler = mock(OnlineSchemaChangeTaskHandler.class);
oscTaskCompleteHandler = mock(OscTaskCompleteHandler.class);
organizationService = mock(OrganizationService.class);
quartzJobService = mock(QuartzJobService.class);
scheduleService = mock(ScheduleService.class);
scheduleTaskService = mock(ScheduleTaskService.class);

onlineSchemaChangeFlowableTask = spy(new OnlineSchemaChangeFlowableTask());
ReflectionTestUtils.setField(onlineSchemaChangeFlowableTask, "taskService", taskService);
ReflectionTestUtils.setField(onlineSchemaChangeFlowableTask, "scheduleTaskRepository",
scheduleTaskRepository);
ReflectionTestUtils.setField(onlineSchemaChangeFlowableTask, "onlineSchemaChangeTaskHandler",
onlineSchemaChangeTaskHandler);
ReflectionTestUtils.setField(onlineSchemaChangeFlowableTask, "oscTaskCompleteHandler", oscTaskCompleteHandler);
ReflectionTestUtils.setField(onlineSchemaChangeFlowableTask, "organizationService", organizationService);
ReflectionTestUtils.setField(onlineSchemaChangeFlowableTask, "quartzJobService", quartzJobService);
ReflectionTestUtils.setField(onlineSchemaChangeFlowableTask, "scheduleService", scheduleService);
ReflectionTestUtils.setField(onlineSchemaChangeFlowableTask, "scheduleTaskService", scheduleTaskService);

when(delegateExecution.getVariable(OnlineSchemaChangeFlowableTask.ONLINE_SCHEMA_CHANGE_PARAMETERS))
.thenReturn(onlineSchemaChangeParameters);
when(delegateExecution.getVariable(OnlineSchemaChangeFlowableTask.TASK_ID))
.thenReturn(1L);
when(delegateExecution.getVariable(OnlineSchemaChangeFlowableTask.CONNECTION_CONFIG))
.thenReturn(connectionConfig);

}

@Test
public void testStart() throws Exception {

when(taskService.start(anyLong())).thenReturn(null);
when(onlineSchemaChangeTaskHandler.start(anyLong(), anyLong()))
.thenReturn(OnlineSchemaChangeTaskHandler.START_TASK_RESULT_SUCCESS);

onlineSchemaChangeFlowableTask.start(1L, taskService, delegateExecution);

verify(taskService, times(1)).start(anyLong());
verify(onlineSchemaChangeTaskHandler, times(1)).start(anyLong(), anyLong());

}

@Test
public void testIsSuccessful() {
assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
}

@Test
public void testIsFailure() {
assertFalse(onlineSchemaChangeFlowableTask.isFailure());
}

@Test
public void testOnFailure() throws Exception {

String errorMessage = "Online schema change task failed";
onlineSchemaChangeFlowableTask.onFailure(1L, taskService);

verify(taskService, times(1)).delete(anyLong());

}

@Test
public void testOnSuccessful() throws Exception {

onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);

verify(taskService, times(1)).complete(any(Task.class));

}

@Test
public void testOnTimeout() throws Exception {

onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);

verify(taskService, times(1)).delete(anyLong());

}

@Test
public void testOnProgressUpdate() throws Exception {

Pageable pageable = PageRequest.of(0, 10, new Order(Direction.DESC, "id"));
List<ScheduleTaskEntity> scheduleTaskEntities = new ArrayList<>();
scheduleTaskEntities.add(scheduleTaskEntity);
Page<ScheduleTaskEntity> page = new PageImpl<>(scheduleTaskEntities, pageable, 1);

when(scheduleTaskService.listTask(pageable, anyLong()))
.thenReturn(page);
when(onlineSchemaChangeFlowableTask.singleTaskPercentage(any(ScheduleTaskEntity.class)))
.thenReturn(0D);

ReflectionTestUtils.setField(onlineSchemaChangeFlowableTask, "scheduleTaskService", scheduleTaskService);

onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

verify(taskService, times(1)).update(any(TaskEntity.class));

}

@Test
public void testCancel() throws Exception {

doNothing().when(onlineSchemaChangeTaskHandler).terminate(anyLong(), anyLong());
when(taskService.delete(anyLong())).thenReturn(true);

ReflectionTestUtils.setField(onlineSchemaChangeFlowableTask, "scheduleTaskService", scheduleTaskService);

assertTrue(onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService));
verify(taskService, times(1)).delete(anyLong());
verify(onlineSchemaChangeTaskHandler, times(1)).terminate(anyLong(), anyLong());

}

@Test
public void testIsCancelled() {
assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
}

private TaskEntity createTaskEntity() {
TaskEntity taskEntity = new TaskEntity();
taskEntity.setId(1L);
taskEntity.setStatus(TaskStatus.RUNNING);
taskEntity.setResultJson("{}");
taskEntity.setProgressPercentage(0.0);
return taskEntity;
}

private ScheduleEntity createScheduleEntity() throws ParseException {
ScheduleEntity scheduleEntity = new ScheduleEntity();
scheduleEntity.setId(1L);
scheduleEntity.setConnectionId(1L);
scheduleEntity.setDatabaseName("testdb");
scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
scheduleEntity.setStatus(ScheduleStatus.ENABLED);
scheduleEntity.setAllowConcurrent(false);
scheduleEntity.setCreatorId(1L);
scheduleEntity.setOrganizationId(1L);
scheduleEntity.setProjectId(1L);
scheduleEntity.setDatabaseId(1L);
scheduleEntity.setModifierId(1L);

TriggerConfig triggerConfig = new TriggerConfig();
triggerConfig.setTriggerStrategy(TriggerStrategy.CRON);
triggerConfig.setCronExpression("0/10 * * * * ?");
scheduleEntity.setTriggerConfigJson(JsonUtils.toJson(triggerConfig));

scheduleEntity.setMisfireStrategy(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING);
scheduleEntity.setJobParametersJson("{}");
return scheduleEntity;
}

private ScheduleTaskEntity createScheduleTaskEntity() {
ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
scheduleTaskEntity.setId(1L);
scheduleTaskEntity.setStatus(TaskStatus.PREPARING);
scheduleTaskEntity.setJobName("test");
scheduleTaskEntity.setJobGroup(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name());
scheduleTaskEntity.setProgressPercentage(0.0);
scheduleTaskEntity.setParametersJson("{}");
scheduleTaskEntity.setResultJson("{}");
scheduleTaskEntity.setFireTime(new Date());
return scheduleTaskEntity;
}

}