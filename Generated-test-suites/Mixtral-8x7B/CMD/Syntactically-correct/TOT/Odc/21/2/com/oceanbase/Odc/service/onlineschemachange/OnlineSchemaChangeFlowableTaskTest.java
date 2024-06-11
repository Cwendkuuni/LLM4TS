import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskServiceImpl;

public class OnlineSchemaChangeFlowableTaskTest {

@Mock
private TaskService taskService;

@Mock
private ConnectionConfig connectionConfig;

@Mock
private OnlineSchemaChangeParameters parameters;

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
private DelegateExecution delegateExecution;

@Mock
private TaskEntity taskEntity;

@Mock
private User user;

@InjectMocks
private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

@Before
public void setup() {
MockitoAnnotations.openMocks(this);
}

@Test
public void testStart() throws JsonProcessingException {
OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
params.setContinueOnError(true);

when(delegateExecution.getVariable(FlowTaskUtil.TASK_CREATOR)).thenReturn(user);
when(delegateExecution.getVariable(FlowTaskUtil.CONNECTION_CONFIG)).thenReturn(connectionConfig);
when(delegateExecution.getVariable(FlowTaskUtil.ONLINE_SCHEMA_CHANGE_PARAMETER))
.thenReturn(params);
when(delegateExecution.getVariable(FlowTaskUtil.SCHEMA_NAME))
.thenReturn("testSchema");
when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class)))
.thenAnswer(invocation -> invocation.getArguments()[0]);
when(scheduleService.create(any(ScheduleEntity.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

onlineSchemaChangeFlowableTask.start(1L, taskService, delegateExecution);

verify(delegateExecution, times(1)).getVariable(FlowTaskUtil.TASK_CREATOR);
verify(delegateExecution, times(1)).getVariable(FlowTaskUtil.CONNECTION_CONFIG);
verify(delegateExecution, times(1)).getVariable(FlowTaskUtil.ONLINE_SCHEMA_CHANGE_PARAMETER);
verify(delegateExecution, times(1)).getVariable(FlowTaskUtil.SCHEMA_NAME);
verify(scheduleTaskRepository, times(1)).saveAndFlush(any(ScheduleTaskEntity.class));
verify(scheduleService, times(1)).create(any(ScheduleEntity.class));
}

@Test
public void testIsSuccessful() {
TaskEntity taskEntity = new TaskEntity();
taskEntity.setStatus(TaskStatus.DONE);
when(taskService.detail(1L)).thenReturn(taskEntity);

assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
}

@Test
public void testIsFailure() {
TaskEntity taskEntity = new TaskEntity();
taskEntity.setStatus(TaskStatus.FAILED);
when(taskService.detail(1L)).thenReturn(taskEntity);

assertTrue(onlineSchemaChangeFlowableTask.isFailure());
}

@Test
public void testOnFailure() {
onlineSchemaChangeFlowableTask.onFailure(1L, taskService);
verify(taskService, times(1)).cancel(1L);
}

@Test
public void testOnSuccessful() {
onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);
verify(taskService, times(1)).update(taskEntity);
}

@Test
public void testOnTimeout() {
onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);
verify(taskService, times(1)).cancel(1L);
}

@Test
public void testOnProgressUpdate() {
Page<ScheduleTaskEntity> tasks = new PageImpl<>(Arrays.asList(new ScheduleTaskEntity(), new ScheduleTaskEntity()));
when(scheduleTaskService.listTask(Pageable.unpaged(), 1L)).thenReturn(tasks);

onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

verify(scheduleTaskService, times(1)).listTask(Pageable.unpaged(), 1L);
}

@Test
public void testCancel() {
doNothing().when(taskHandler).terminate(1L, 1L);
when(scheduleTaskService.listTask(Pageable.unpaged(), 1L)).thenReturn(new PageImpl<>(Arrays.asList(new ScheduleTaskEntity())));

onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);

verify(taskHandler, times(1)).terminate(1L, 1L);
verify(scheduleTaskService, times(1)).listTask(Pageable.unpaged(), 1L);
}

@Test
public void testIsCancelled() {
TaskEntity taskEntity = new TaskEntity();
taskEntity.setStatus(TaskStatus.CANCELED);
when(taskService.detail(1L)).thenReturn(taskEntity);

assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
}
}