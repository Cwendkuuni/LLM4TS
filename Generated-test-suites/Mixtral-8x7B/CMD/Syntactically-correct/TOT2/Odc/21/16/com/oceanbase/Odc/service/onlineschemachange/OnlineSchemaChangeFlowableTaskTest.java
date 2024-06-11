import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

@InjectMocks
private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

@Before
public void init() {
MockitoAnnotations.initMocks(this);
}

@Test
public void testStart() {
DelegateExecution execution = createDelegateExecution();

when(scheduleTaskService.listTask(any(Pageable.class), anyLong())).thenReturn(createPage());

onlineSchemaChangeFlowableTask.start(1L, null, execution);

verify(scheduleService, times(1)).create(any(ScheduleEntity.class));
verify(scheduleTaskService, times(1)).listTask(any(Pageable.class), anyLong());
verify(taskHandler, times(1)).start(anyLong(), anyLong());
}

@Test
public void testIsSuccessful() {
assertEquals(true, onlineSchemaChangeFlowableTask.isSuccessful());
}

@Test
public void testIsFailure() {
assertEquals(false, onlineSchemaChangeFlowableTask.isFailure());
}

@Test
public void testOnFailure() {
onlineSchemaChangeFlowableTask.onFailure(1L, null);

verify(scheduleTaskService, times(1)).cancel(anyLong());
}

@Test
public void testOnSuccessful() {
onlineSchemaChangeFlowableTask.onSuccessful(1L, null);

verify(scheduleInstanceService, times(1)).updateStatus(anyLong(), anyString());
}

@Test
public void testOnTimeout() {
onlineSchemaChangeFlowableTask.onTimeout(1L, null);

verify(scheduleTaskService, times(1)).cancel(anyLong());
}

@Test
public void testOnProgressUpdate() {
onlineSchemaChangeFlowableTask.onProgressUpdate(1L, null);

verify(scheduleTaskService, times(1)).listTask(any(Pageable.class), anyLong());
}

@Test
public void testCancel() {
onlineSchemaChangeFlowableTask.cancel(false, 1L, null);

verify(taskHandler, times(1)).terminate(anyLong(), anyLong());
verify(scheduleTaskService, times(1)).cancel(anyLong());
}

private DelegateExecution createDelegateExecution() {
DelegateExecution execution = mock(DelegateExecution.class);
when(execution.getId()).thenReturn(1L);
when(execution.getVariable("uid")).thenReturn("uid");
when(execution.getVariable("parameter")).thenReturn(createOnlineSchemaChangeParameters());
when(execution.getVariable("connectionConfig")).thenReturn(createConnectionConfig());
when(execution.getVariable("schema")).thenReturn("schema");
return execution;
}

private Page<ScheduleTaskEntity> createPage() {
Pageable pageable = mock(Pageable.class);
List<ScheduleTaskEntity> scheduleTaskEntities = createScheduleTaskEntities();
Page<ScheduleTaskEntity> page = mock(Page.class);
when(page.getContent()).thenReturn(scheduleTaskEntities);
when(page.getSize()).thenReturn(scheduleTaskEntities.size());
when(scheduleTaskRepository.findAll(pageable)).thenReturn(page);
return page;
}

private List<ScheduleTaskEntity> createScheduleTaskEntities() {
return List.of(new ScheduleTaskEntity());
}

private OnlineSchemaChangeParameters createOnlineSchemaChangeParameters() {
return new OnlineSchemaChangeParameters();
}

private ConnectionConfig createConnectionConfig() {
return new ConnectionConfig();
}
}