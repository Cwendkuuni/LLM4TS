import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.Expression;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskService;

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

 @Mock
 private DelegateExecution execution;

 @Mock
 private Expression expression;

 @Mock
 private Task task;

 @Mock
 private Pageable pageable;

 @InjectMocks
 private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask = spy(new OnlineSchemaChangeFlowableTask());

 private ObjectMapper objectMapper = new ObjectMapper();

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testStart() throws Exception {
 Expression connectionConfigExpression = expression;
 Expression parameterExpression = expression;
 Expression schemaExpression = expression;
 Expression uidExpression = expression;
 Expression continueOnErrorExpression = expression;

 doReturn("connectionConfig").when(connectionConfigExpression).getValue(any());
 doReturn("parameter").when(parameterExpression).getValue(any());
 doReturn("schema").when(schemaExpression).getValue(any());
 doReturn("uid").when(uidExpression).getValue(any());
 doReturn(true).when(continueOnErrorExpression).getValue(any());

 User user = new User();
 user.setId(1L);
 user.setOrganizationId(2L);

 doReturn(user).when(execution).getVariable("creator");

 OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 onlineSchemaChangeParameters.setContinueOnError(true);

 doReturn(onlineSchemaChangeParameters).when(onlineSchemaChangeFlowableTask)
 .getOnlineSchemaChangeParameter(execution);

 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setId(1L);

 doReturn(connectionConfig).when(onlineSchemaChangeFlowableTask)
 .getConnectionConfig(execution);

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);

 doReturn(scheduleEntity).when(onlineSchemaChangeFlowableTask)
 .createScheduleEntity(connectionConfig, onlineSchemaChangeParameters, "schema");

 OnlineSchemaChangeContextHolder.trace(1L, 1L, 2L);

 onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

 verify(onlineSchemaChangeFlowableTask, times(1)).createScheduleEntity(connectionConfig,
 onlineSchemaChangeParameters, "schema");
 verify(onlineSchemaChangeFlowableTask, times(1)).start(eq(1L), anyLong());
 OnlineSchemaChangeContextHolder.clear();
 }

 @Test
 public void testIsSuccessful() {
 onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);
 assertEquals(true, onlineSchemaChangeFlowableTask.isSuccessful());

 onlineSchemaChangeFlowableTask.setStatus(TaskStatus.RUNNING);
 assertEquals(false, onlineSchemaChangeFlowableTask.isSuccessful());
 }

 @Test
 public void testIsFailure() {
 onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);
 assertEquals(false, onlineSchemaChangeFlowableTask.isFailure());

 onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);
 assertEquals(true, onlineSchemaChangeFlowableTask.isFailure());
 }

 @Test
 public void testOnFailure() {
 onlineSchemaChangeFlowableTask.onFailure(1L, taskService);
 }

 @Test
 public void testOnSuccessful() {
 onlineSchemaChangeFlowableTask.updateFlowInstanceStatus(FlowStatus.EXECUTION_SUCCEEDED);
 onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);
 }

 @Test
 public void testOnTimeout() {
 onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);
 }

 @Test
 public void testOnProgressUpdate() {
 Page<ScheduleTaskEntity> tasks = new PageImpl<>(new ArrayList<>());

 doReturn(tasks).when(scheduleTaskService).listTask(any(Pageable.class), anyLong());

 onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);
 }

 @Test
 public void testCancel() throws Exception {
 doNothing().when(taskHandler).terminate(anyLong(), anyLong());

 onlineSchemaChangeFlowableTask.cancel(false, 1L, taskService);

 verify(taskHandler, times(1)).terminate(anyLong(), anyLong());
 }

 @Test
 public void testIsCancelled() {
 onlineSchemaChangeFlowableTask.setStatus(TaskStatus.CANCELED);
 assertEquals(true, onlineSchemaChangeFlowableTask.isCancelled());

 onlineSchemaChangeFlowableTask.setStatus(TaskStatus.RUNNING);
 assertEquals(false, onlineSchemaChangeFlowableTask.isCancelled());
 }

 @Test
 public void testCreateScheduleEntity() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setId(1L);

 OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 scheduleEntity.setConnectionId(1L);
 scheduleEntity.setDatabaseName("schema");
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
 scheduleEntity.setStatus(ScheduleStatus.ENABLED);
 scheduleEntity.setAllowConcurrent(false);
 scheduleEntity.setCreatorId(1L);
 scheduleEntity.setOrganizationId(2L);
 scheduleEntity.setProjectId(1L);
 scheduleEntity.setDatabaseId(1L);
 scheduleEntity.setModifierId(1L);

 TriggerConfig triggerConfig = new TriggerConfig();
 triggerConfig.setTriggerStrategy(TriggerStrategy.CRON);
 triggerConfig.setCronExpression("checkTaskCronExpression");

 scheduleEntity.setTriggerConfigJson(JsonUtils.toJson(triggerConfig));
 scheduleEntity.setMisfireStrategy("MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING");
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(onlineSchemaChangeParameters));

 assertEquals(scheduleEntity,
 onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, onlineSchemaChangeParameters, "schema"));
 }

 @Test
 public void testCreateScheduleTaskEntity() {
 OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters = new OnlineSchemaChangeScheduleTaskParameters();

 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setStatus(TaskStatus.PREPARING);
 scheduleTaskEntity.setJobName("1");
 scheduleTaskEntity.setJobGroup(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name());
 scheduleTaskEntity.setProgressPercentage(0.0);
 scheduleTaskEntity.setParametersJson(JsonUtils.toJson(onlineSchemaChangeScheduleTaskParameters));
 scheduleTaskEntity.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult(
 onlineSchemaChangeScheduleTaskParameters)));
 scheduleTaskEntity.setFireTime(new Date());

 assertEquals(scheduleTaskEntity,
 onlineSchemaChangeFlowableTask.createScheduleTaskEntity(1L, onlineSchemaChangeScheduleTaskParameters));
 }

 private ScheduleEntity getScheduleEntity() {
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 return scheduleEntity;
 }

 private OnlineSchemaChangeParameters getOnlineSchemaChangeParameters() throws Exception {
 OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 onlineSchemaChangeParameters.setContinueOnError(true);

 JsonNode jsonNode = objectMapper.readTree("{\"key\":\"value\"}");
 doReturn(jsonNode).when(onlineSchemaChangeFlowableTask).getOnlineSchemaChangeParameter(execution);

 return onlineSchemaChangeParameters;
 }

 private ConnectionConfig getConnectionConfig() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setId(1L);
 return connectionConfig;
 }
}