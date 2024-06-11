import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.Expression;
import org.flowable.engine.delegate.VariableScope;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.quartz.CronExpression;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.quartz.model.ScheduleStatus;
import com.oceanbase.odc.service.quartz.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskService;

public class OnlineSchemaChangeFlowableTaskTest {

 private static final String CONNECTION_CONFIG_ID = "123";
 private static final String SCHEDULE_ID = "456";
 private static final String TASK_ID = "789";
 private static final String ORGANIZATION_ID = "101";
 private static final String USER_ID = "202";

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
 private VariableScope variableScope;

 @Mock
 private DelegateExecution delegateExecution;

 @Mock
 private Expression expression;

 @Mock
 private Scheduler scheduler;

 @InjectMocks
 private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

 private User user;

 private ConnectionConfig connectionConfig;

 private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

 private OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters;

 private ScheduleEntity scheduleEntity;

 private ScheduleTaskEntity scheduleTaskEntity;

 private MockedStatic<JsonUtils> jsonUtilsMock;

 private final ObjectMapper objectMapper = new ObjectMapper();

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);

 user = new User();
 user.setId(USER_ID);
 user.setOrganizationId(ORGANIZATION_ID);

 connectionConfig = new ConnectionConfig();
 connectionConfig.setId(CONNECTION_CONFIG_ID);

 onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 onlineSchemaChangeScheduleTaskParameters = new OnlineSchemaChangeScheduleTaskParameters();

 scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(Long.parseLong(SCHEDULE_ID));

 scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(Long.parseLong(TASK_ID));

 jsonUtilsMock = Mockito.mockStatic(JsonUtils.class);
 jsonUtilsMock.when(() -> JsonUtils.toJson(any())).thenAnswer(invocation -> {
 try {
 return objectMapper.writeValueAsString(invocation.getArgument(0));
 } catch (JsonProcessingException e) {
 throw new RuntimeException(e);
 }
 });
 jsonUtilsMock.when(() -> JsonUtils.fromJson(anyString(), any())).thenAnswer(invocation -> {
 try {
 return objectMapper.readValue(invocation.getArgument(0), (Class) invocation.getArgument(1));
 } catch (JsonProcessingException e) {
 throw new RuntimeException(e);
 }
 });
 }

 @Test
 public void testStart_success() throws Exception {
 when(delegateExecution.getVariable(OnlineSchemaChangeFlowableTask.CLOUD_MAIN_ACCOUNT_ID)).thenReturn(USER_ID);
 when(delegateExecution.getVariable(OnlineSchemaChangeFlowableTask.CONNECTION_CONFIG)).thenReturn(connectionConfig);
 when(delegateExecution.getVariable(OnlineSchemaChangeFlowableTask.ONLINE_SCHEMA_CHANGE_PARAMETER))
 .thenReturn(onlineSchemaChangeParameters);
 when(delegateExecution.getVariable(OnlineSchemaChangeFlowableTask.SCHEMA)).thenReturn("test_schema");
 when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenReturn(scheduleTaskEntity);
 when(scheduleService.create(any(ScheduleEntity.class))).thenReturn(scheduleEntity);
 when(taskHandler.start(anyLong(), anyLong())).thenReturn(true);

 onlineSchemaChangeFlowableTask.start(TASK_ID, taskService, delegateExecution);

 verify(delegateExecution, times(1)).setVariable(OnlineSchemaChangeFlowableTask.SCHEDULE_ID, SCHEDULE_ID);
 verify(delegateExecution, times(1)).setVariable(OnlineSchemaChangeFlowableTask.SCHEDULE_TASK_ID, TASK_ID);
 }

 @Test
 public void testStart_taskHandlerStartFails() throws Exception {
 when(delegateExecution.getVariable(OnlineSchemaChangeFlowableTask.CLOUD_MAIN_ACCOUNT_ID)).thenReturn(USER_ID);
 when(delegateExecution.getVariable(OnlineSchemaChangeFlowableTask.CONNECTION_CONFIG)).thenReturn(connectionConfig);
 when(delegateExecution.getVariable(OnlineSchemaChangeFlowableTask.ONLINE_SCHEMA_CHANGE_PARAMETER))
 .thenReturn(onlineSchemaChangeParameters);
 when(delegateExecution.getVariable(OnlineSchemaChangeFlowableTask.SCHEMA)).thenReturn("test_schema");
 when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenReturn(scheduleTaskEntity);
 when(scheduleService.create(any(ScheduleEntity.class))).thenReturn(scheduleEntity);
 when(taskHandler.start(anyLong(), anyLong())).thenReturn(false);

 onlineSchemaChangeFlowableTask.start(TASK_ID, taskService, delegateExecution);

 verify(delegateExecution, times(1)).setVariable(OnlineSchemaChangeFlowableTask.SCHEDULE_ID, SCHEDULE_ID);
 verify(delegateExecution, times(1)).setVariable(OnlineSchemaChangeFlowableTask.SCHEDULE_TASK_ID, TASK_ID);
 }

 @Test
 public void testIsSuccessful_success() {
 scheduleTaskEntity.setStatus(TaskStatus.DONE);

 assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());
 }

 @Test
 public void testIsSuccessful_failure() {
 scheduleTaskEntity.setStatus(TaskStatus.FAILED);

 assertFalse(onlineSchemaChangeFlowableTask.isSuccessful());
 }

 @Test
 public void testIsFailure_success() {
 scheduleTaskEntity.setStatus(TaskStatus.FAILED);

 assertTrue(onlineSchemaChangeFlowableTask.isFailure());
 }

 @Test
 public void testIsFailure_failure() {
 scheduleTaskEntity.setStatus(TaskStatus.DONE);

 assertFalse(onlineSchemaChangeFlowableTask.isFailure());
 }

 @Test
 public void testOnFailure_called() {
 onlineSchemaChangeFlowableTask.onFailure(TASK_ID, taskService);

 verify(taskService, times(1)).failure(TASK_ID);
 }

 @Test
 public void testOnSuccessful_called() {
 onlineSchemaChangeFlowableTask.onSuccessful(TASK_ID, taskService);

 verify(taskService, times(1)).success(TASK_ID);
 }

 @Test
 public void testOnTimeout_called() {
 onlineSchemaChangeFlowableTask.onTimeout(TASK_ID, taskService);

 verify(taskService, times(1)).timeout(TASK_ID);
 }

 @Test
 public void testOnProgressUpdate_noTasks() {
 Page<ScheduleTaskEntity> emptyPage = new PageImpl<ScheduleTaskEntity>(new ArrayList<ScheduleTaskEntity>());

 onlineSchemaChangeFlowableTask.onProgressUpdate(TASK_ID, taskService);

 verify(taskService, never()).update(any(TaskEntity.class));
 }

 @Test
 public void testOnProgressUpdate_tasksUpdated() throws Exception {
 List<ScheduleTaskEntity> tasks = Arrays.asList(
 new ScheduleTaskEntity().setId(1L).setStatus(TaskStatus.PREPARING),
 new ScheduleTaskEntity().setId(2L).setStatus(TaskStatus.DONE),
 new ScheduleTaskEntity().setId(3L).setStatus(TaskStatus.FAILED)
 );

 Page<ScheduleTaskEntity> tasksPage = new PageImpl<>(tasks);
 when(scheduleTaskService.listTask(Pageable.unpaged(), Long.parseLong(SCHEDULE_ID)))
 .thenReturn(tasksPage);

 onlineSchemaChangeFlowableTask.onProgressUpdate(TASK_ID, taskService);

 verify(taskService, times(1)).update(any(TaskEntity.class));
 }

 @Test
 public void testCancel_called() {
 doNothing().when(taskHandler).terminate(anyLong(), anyLong());

 onlineSchemaChangeFlowableTask.cancel(true, TASK_ID, taskService);

 verify(taskHandler, times(1)).terminate(Long.parseLong(SCHEDULE_ID), Long.parseLong(TASK_ID));
 verify(taskService, times(1)).cancel(TASK_ID);
 }

 @Test
 public void testIsCancelled_cancelled() {
 scheduleTaskEntity.setStatus(TaskStatus.CANCELED);

 assertTrue(onlineSchemaChangeFlowableTask.isCancelled());
 }

 @Test
 public void testIsCancelled_notCancelled() {
 scheduleTaskEntity.setStatus(TaskStatus.DONE);

 assertFalse(onlineSchemaChangeFlowableTask.isCancelled());
 }

 private ScheduleEntity createSampleScheduleEntity() {
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 scheduleEntity.setConnectionId(CONNECTION_CONFIG_ID);
 scheduleEntity.setDatabaseName("test_db");
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
 scheduleEntity.setStatus(ScheduleStatus.ENABLED);
 scheduleEntity.setAllowConcurrent(false);
 scheduleEntity.setCreatorId(USER_ID);
 scheduleEntity.setOrganizationId(ORGANIZATION_ID);
 scheduleEntity.setProjectId(1L);
 scheduleEntity.setDatabaseId(1L);
 scheduleEntity.setModifierId(scheduleEntity.getCreatorId());

 TriggerConfig triggerConfig = new TriggerConfig();
 triggerConfig.setTriggerStrategy(TriggerStrategy.CRON);
 triggerConfig.setCronExpression("0 0/1 * * * ?");
 scheduleEntity.setTriggerConfigJson(JsonUtils.toJson(triggerConfig));

 scheduleEntity.setMisfireStrategy(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING);
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(onlineSchemaChangeParameters));

 return scheduleEntity;
 }

 private TaskEntity createSampleTaskEntity() {
 TaskEntity taskEntity = new TaskEntity();
 taskEntity.setId(TASK_ID);
 taskEntity.setProcessInstanceId("12345");
 taskEntity.setTaskDefinitionKey("testTask");
 taskEntity.setAssignee(USER_ID);
 taskEntity.setStatus(TaskStatus.CREATED);
 taskEntity.setCreateTime(new Date());

 return taskEntity;
 }

}