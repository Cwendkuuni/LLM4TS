import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskService;
import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.task.api.Task;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class OnlineSchemaChangeFlowableTaskTest {

@InjectMocks
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

private DelegateExecution delegateExecution;
private ConnectionConfig connectionConfig;
private OnlineSchemaChangeParameters parameters;
private ScheduleEntity scheduleEntity;
private Task task;
private List<ScheduleTaskEntity> scheduleTaskEntities;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);

delegateExecution = Mockito.mock(DelegateExecution.class);
connectionConfig = Mockito.mock(ConnectionConfig.class);
parameters = Mockito.mock(OnlineSchemaChangeParameters.class);
scheduleEntity = Mockito.mock(ScheduleEntity.class);
task = Mockito.mock(Task.class);
scheduleTaskEntities = new ArrayList<>();

when(delegateExecution.getVariable(BaseODCFlowTaskDelegate.TASK))
.thenReturn(task);

when(delegateExecution.getVariable(OnlineSchemaChangeFlowableTask.ONLINE_SCHEMA_CHANGE_PARAMETER))
.thenReturn(parameters);

when(delegateExecution.getVariable(OnlineSchemaChangeFlowableTask.CONNECTION_CONFIG))
.thenReturn(connectionConfig);

scheduleTaskEntities.add(Mockito.mock(ScheduleTaskEntity.class));

when(scheduleTaskService.listTask(any(Pageable.class), any(Long.class)))
.thenReturn(Mockito.mock(Page.class));

when(taskService.detail(any(Long.class)))
.thenReturn(Mockito.mock(TaskEntity.class));
}

@Test
public void testStart() throws Exception {
onlineSchemaChangeFlowableTask.start(1L, taskService, delegateExecution);

Mockito.verify(taskService, Mockito.times(1)).start(1L);
Mockito.verify(scheduleTaskRepository, Mockito.times(1)).saveAndFlush(any(ScheduleTaskEntity.class));
Mockito.verify(taskHandler, Mockito.times(1)).start(any(Long.class), any(Long.class));
}

@Test
public void testIsSuccessful() {
onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);
Assert.assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
}

@Test
public void testIsFailure() {
onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);
Assert.assertTrue(onlineSchemaChangeFlowableTask.isFailure());
}

@Test
public void testOnFailure() {
onlineSchemaChangeFlowableTask.onFailure(1L, taskService);

Mockito.verify(taskService, Mockito.times(1)).cancel(1L);
}

@Test
public void testOnSuccessful() {
onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);

Mockito.verify(taskService, Mockito.times(1)).cancel(1L);
}

@Test
public void testOnTimeout() {
onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);

Mockito.verify(taskService, Mockito.times(1)).cancel(1L);
}

@Test
public void testCancel() {
onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);

Mockito.verify(taskService, Mockito.times(1)).cancel(1L);
}

@Test
public void testIsCancelled() {
onlineSchemaChangeFlowableTask.setStatus(TaskStatus.CANCELED);
Assert.assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
}

private void setupScheduleEntity() {
when(scheduleEntity.getId()).thenReturn(1L);
when(scheduleEntity.getConnectionId()).thenReturn(1L);
when(scheduleEntity.getDatabaseName()).thenReturn("test_db");
when(scheduleEntity.getJobType()).thenReturn(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
when(scheduleEntity.getStatus()).thenReturn(ScheduleStatus.ENABLED);
when(scheduleEntity.getAllowConcurrent()).thenReturn(false);
when(scheduleEntity.getCreatorId()).thenReturn(1L);
when(scheduleEntity.getOrganizationId()).thenReturn(1L);
when(scheduleEntity.getProjectId()).thenReturn(1L);
when(scheduleEntity.getDatabaseId()).thenReturn(1L);
when(scheduleEntity.getModifierId()).thenReturn(1L);
}

private void setupScheduleTaskEntities() {
scheduleTaskEntities.clear();

for (int i = 0; i < 5; i++) {
ScheduleTaskEntity scheduleTaskEntity = Mockito.mock(ScheduleTaskEntity.class);

when(scheduleTaskEntity.getId()).thenReturn((long) i);
when(scheduleTaskEntity.getJobName()).thenReturn("1");
when(scheduleTaskEntity.getJobGroup()).thenReturn(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name());
when(scheduleTaskEntity.getProgressPercentage()).thenReturn(0.0);
when(scheduleTaskEntity.getParametersJson()).thenReturn("{}");
when(scheduleTaskEntity.getResultJson()).thenReturn("{}");
when(scheduleTaskEntity.getFireTime()).thenReturn(new Date());

scheduleTaskEntities.add(scheduleTaskEntity);
}
}

@Test
public void testProgressStatusUpdate() {
setupScheduleTaskEntities();

int successfulTask = 3;
int failedTask = 1;
boolean canceled = false;

onlineSchemaChangeFlowableTask.progressStatusUpdate(scheduleTaskEntities);

if (canceled) {
Assert.assertEquals(TaskStatus.CANCELED, onlineSchemaChangeFlowableTask.getStatus());
} else if (successfulTask + failedTask == scheduleTaskEntities.size()) {
if (failedTask == 0) {
Assert.assertEquals(TaskStatus.DONE, onlineSchemaChangeFlowableTask.getStatus());
} else {
Assert.assertEquals(TaskStatus.FAILED, onlineSchemaChangeFlowableTask.getStatus());
}
} else if (failedTask > 0 && !onlineSchemaChangeFlowableTask.isContinueOnError()) {
Assert.assertEquals(TaskStatus.FAILED, onlineSchemaChangeFlowableTask.getStatus());
}
}

@Test
public void testSingleTaskPercentage() {
Assert.assertEquals(0.0, onlineSchemaChangeFlowableTask.singleTaskPercentage(null), 0);

ScheduleTaskEntity scheduleTaskEntity = Mockito.mock(ScheduleTaskEntity.class);

when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.PREPARING);
Assert.assertEquals(0.0, onlineSchemaChangeFlowableTask.singleTaskPercentage(scheduleTaskEntity), 0);

when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.RUNNING);
when(scheduleTaskEntity.getProgressPercentage()).thenReturn(50.0);
Assert.assertEquals(0.5, onlineSchemaChangeFlowableTask.singleTaskPercentage(scheduleTaskEntity), 0);

when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.DONE);
Assert.assertEquals(1.0, onlineSchemaChangeFlowableTask.singleTaskPercentage(scheduleTaskEntity), 0);
}
}