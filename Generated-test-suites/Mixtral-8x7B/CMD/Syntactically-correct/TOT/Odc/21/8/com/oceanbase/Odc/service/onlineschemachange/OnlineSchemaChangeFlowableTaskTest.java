import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.connection.model.ConnectionConfig;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.schedule.model.TriggerStrategy;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.metadb.task.TaskService;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;

class OnlineSchemaChangeFlowableTaskTest {

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

@InjectMocks
private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

private ObjectMapper objectMapper;

@BeforeEach
public void setUp() {
MockitoAnnotations.openMocks(this);
objectMapper = new ObjectMapper();
}

@Test
public void testStart() throws Exception {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setId(1L);

OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

Mockito.verify(execution).setVariable(OnlineSchemaChangeFlowableTask.SCHEDULE_ID, 1L);
Mockito.verify(execution).setVariable(OnlineSchemaChangeFlowableTask.FLOW_STATUS, FlowStatus.EXECUTION_RUNNING);
}

@Test
public void testIsSuccessful() {
onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);
assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());

onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);
assertFalse(onlineSchemaChangeFlowableTask.isSuccessful());
}

@Test
public void testIsFailure() {
onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);
assertFalse(onlineSchemaChangeFlowableTask.isFailure());

onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);
assertTrue(onlineSchemaChangeFlowableTask.isFailure());
}

@Test
public void testOnFailure() {
onlineSchemaChangeFlowableTask.onFailure(1L, taskService);

Mockito.verify(taskService).update(Mockito.any(TaskEntity.class));
}

@Test
public void testOnSuccessful() {
onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);

Mockito.verify(taskService).update(Mockito.any(TaskEntity.class));
}

@Test
public void testOnProgressUpdate() {
OnlineSchemaChangeScheduleTaskParameters taskParam1 = new OnlineSchemaChangeScheduleTaskParameters();
OnlineSchemaChangeScheduleTaskParameters taskParam2 = new OnlineSchemaChangeScheduleTaskParameters();
List<ScheduleTaskEntity> tasks = new ArrayList<>();
tasks.add(createScheduleTaskEntity(taskParam1));
tasks.add(createScheduleTaskEntity(taskParam2));

Page<ScheduleTaskEntity> taskPage = new PageImpl<>(tasks);

onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

Mockito.verify(taskService).update(Mockito.any(TaskEntity.class));
}

@Test
public void testCreateScheduleEntity() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setId(1L);

OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameters, "schema");

assertEquals(connectionConfig.getId(), scheduleEntity.getConnectionId());
assertEquals(TriggerStrategy.CRON, scheduleEntity.getTriggerConfigJson().getTriggerStrategy());
assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, scheduleEntity.getJobType());
assertEquals(ScheduleStatus.ENABLED, scheduleEntity.getStatus());
}

@Test
public void testCreateScheduleTaskEntity() throws JsonMappingException, JsonProcessingException {
OnlineSchemaChangeScheduleTaskParameters taskParam = new OnlineSchemaChangeScheduleTaskParameters();

ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(1L, taskParam);

assertEquals(1L, scheduleTaskEntity.getJobId());
assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name(), scheduleTaskEntity.getJobName());
assertEquals(0.0, scheduleTaskEntity.getProgressPercentage());
assertEquals(objectMapper.writeValueAsString(taskParam), scheduleTaskEntity.getParametersJson());
assertEquals(objectMapper.writeValueAsString(new OnlineSchemaChangeScheduleTaskResult(taskParam)),
scheduleTaskEntity.getResultJson());
}

private ScheduleTaskEntity createScheduleTaskEntity(OnlineSchemaChangeScheduleTaskParameters taskParam) {
ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
scheduleTaskEntity.setStatus(TaskStatus.PREPARING);
scheduleTaskEntity.setJobName(1L + "");
scheduleTaskEntity.setJobGroup(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name());
scheduleTaskEntity.setProgressPercentage(0.0);
scheduleTaskEntity.setParametersJson(objectMapper.writeValueAsString(taskParam));
scheduleTaskEntity.setResultJson(objectMapper.writeValueAsString(new OnlineSchemaChangeScheduleTaskResult(taskParam)));
scheduleTaskEntity.setFireTime(new Date());
return scheduleTaskEntity;
}
}