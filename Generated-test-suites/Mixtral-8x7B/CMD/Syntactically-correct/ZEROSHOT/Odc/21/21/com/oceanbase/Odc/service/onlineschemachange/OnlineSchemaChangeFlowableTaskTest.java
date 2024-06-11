import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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

@InjectMocks
private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);
}

@Test
public void testStart() {
DelegateExecution execution = mock(DelegateExecution.class);
when(execution.getId()).thenReturn(1L);
when(execution.getVariable(anyString())).thenReturn("test");
when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(new User());
when(FlowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(new OnlineSchemaChangeParameters());
when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(new ConnectionConfig());
when(FlowTaskUtil.getSchemaName(execution)).thenReturn("test_schema");

onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

verify(taskService, times(1)).start(1L);
verify(scheduleTaskRepository, times(1)).saveAndFlush(any(ScheduleTaskEntity.class));
}

@Test
public void testIsSuccessful() {
onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;
assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());

onlineSchemaChangeFlowableTask.status = TaskStatus.FAILED;
assertFalse(onlineSchemaChangeFlowableTask.isSuccessful());
}

@Test
public void testIsFailure() {
onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;
assertFalse(onlineSchemaChangeFlowableTask.isFailure());

onlineSchemaChangeFlowableTask.status = TaskStatus.FAILED;
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
verify(taskService, times(1)).update(any(TaskEntity.class));
}

@Test
public void testOnProgressUpdate() {
TaskEntity task = new TaskEntity();
task.setId(1L);
List<ScheduleTaskEntity> tasks =
mock(Page.class).getContent();
when(scheduleTaskService.listTask(Pageable.unpaged(), anyLong())).thenReturn(tasks);

onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

verify(scheduleTaskService, times(1)).listTask(Pageable.unpaged(), anyLong());
verify(taskService, times(1)).update(any(TaskEntity.class));
}

@Test
public void testCancel() {
onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);
verify(taskService, times(1)).cancel(1L);
verify(taskHandler, times(1)).terminate(anyLong(), anyLong());
}

@Test
public void testIsCancelled() {
onlineSchemaChangeFlowableTask.status = TaskStatus.CANCELED;
assertTrue(onlineSchemaChangeFlowableTask.isCancelled());

onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;
assertFalse(onlineSchemaChangeFlowableTask.isCancelled());
}

private Page<ScheduleTaskEntity> mockTasks() {
return new Page<ScheduleTaskEntity>() {
@Override
public int getTotalElements() {
return 10;
}

@Override
public int getNumber() {
return 0;
}

@Override
public int getSize() {
return 10;
}

@Override
public int getTotalPages() {
return 1;
}

@Override
public boolean hasContent() {
return true;
}

@Override
public List<ScheduleTaskEntity> getContent() {
List<ScheduleTaskEntity> list = new ArrayList<>();
for (int i = 0; i < 10; i++) {
ScheduleTaskEntity entity = new ScheduleTaskEntity();
entity.setStatus(TaskStatus.values()[i % 3]);
list.add(entity);
}
return list;
}

@Override
public boolean isFirst() {
return true;
}

@Override
public boolean isLast() {
return true;
}

@Override
public boolean hasNext() {
return false;
}

@Override
public boolean hasPrevious() {
return false;
}

@Override
public Pageable getPageable() {
return null;
}

@Override
public <U> Page<U> map(Function<? super ScheduleTaskEntity, ? extends U> function) {
return null;
}
};
}
}