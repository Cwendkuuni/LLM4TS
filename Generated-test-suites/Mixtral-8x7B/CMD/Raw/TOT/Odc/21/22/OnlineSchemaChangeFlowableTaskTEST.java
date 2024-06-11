import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.Expression;
import org.flowable.engine.delegate.VariableScope;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
 private Expression expression;

 @InjectMocks
 private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

 private ObjectMapper objectMapper = new ObjectMapper();

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testStart() throws Exception {
 // Prepare mocks
 Long taskId = 1L;
 TaskService taskService = Mockito.mock(TaskService.class);
 DelegateExecution execution = Mockito.mock(DelegateExecution.class);
 ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
 OnlineSchemaChangeParameters parameter = Mockito.mock(OnlineSchemaChangeParameters.class);
 User creator = Mockito.mock(User.class);

 Mockito.when(execution.getVariable(FlowTaskUtil.TASK_CREATOR_KEY)).thenReturn(creator);
 Mockito.when(execution.getVariable(FlowTaskUtil.CONNECTION_CONFIG_KEY)).thenReturn(connectionConfig);
 Mockito.when(execution.getVariable(FlowTaskUtil.ONLINE_SCHEMA_CHANGE_PARAMETERS_KEY))
 .thenReturn(parameter);
 Mockito.when(expression.evaluate(execution)).thenReturn("123456");

 // Call method under test
 onlineSchemaChangeFlowableTask.start(taskId, taskService, execution);

 // Verify the interactions with mocks
 Mockito.verify(taskService).start(taskId);
 Mockito.verify(creator).getId();
 Mockito.verify(execution).getVariable(FlowTaskUtil.TASK_CREATOR_KEY);
 Mockito.verify(connectionConfig).id();
 Mockito.verify(execution).getVariable(FlowTaskUtil.CONNECTION_CONFIG_KEY);
 Mockito.verify(parameter).generateSubTaskParameters(connectionConfig, "schema");
 Mockito.verify(scheduleTaskRepository).saveAndFlush(Mockito.any(ScheduleTaskEntity.class));
 Mockito.verify(taskHandler).start(Mockito.anyLong(), Mockito.anyLong());
 }

 @Test
 public void testIsSuccessful() {
 // Call method under test
 boolean result = onlineSchemaChangeFlowableTask.isSuccessful();

 // Verify the result
 assertFalse(result);
 }

 @Test
 public void testIsFailure() {
 // Call method under test
 boolean result = onlineSchemaChangeFlowableTask.isFailure();

 // Verify the result
 assertFalse(result);
 }

 @Test
 public void testOnFailure() {
 // Prepare mocks
 Long taskId = 1L;
 TaskService taskService = Mockito.mock(TaskService.class);

 // Call method under test
 onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);

 // Verify the interactions with mocks
 Mockito.verify(taskService).cancel(taskId);
 }

 @Test
 public void testOnSuccessful() {
 // Prepare mocks
 Long taskId = 1L;
 TaskService taskService = Mockito.mock(TaskService.class);

 // Call method under test
 onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskService);

 // Verify the interactions with mocks
 Mockito.verify(taskService).complete(taskId);
 }

 @Test
 public void testOnTimeout() {
 // Prepare mocks
 Long taskId = 1L;
 TaskService taskService = Mockito.mock(TaskService.class);

 // Call method under test
 onlineSchemaChangeFlowableTask.onTimeout(taskId, taskService);

 // Verify the interactions with mocks
 Mockito.verify(taskService).cancel(taskId);
 }

 @Test
 public void testOnProgressUpdate() {
 // Prepare mocks
 Long taskId = 1L;
 TaskService taskService = Mockito.mock(TaskService.class);
 List<ScheduleTaskEntity> tasks = new ArrayList<>();
 tasks.add(createScheduleTaskEntity());
 Page<ScheduleTaskEntity> page = new PageImpl<>(tasks);

 // Call method under test
 onlineSchemaChangeFlowableTask.onProgressUpdate(taskId, taskService);

 // Verify the interactions with mocks
 Mockito.verify(scheduleTaskService).listTask(Mockito.any(Pageable.class), Mockito.anyLong());
 Mockito.verify(taskService).update(Mockito.any(TaskEntity.class));
 }

 @Test
 public void testCancel() {
 // Prepare mocks
 Long taskId = 1L;
 TaskService taskService = Mockito.mock(TaskService.class);

 // Call method under test
 boolean result = onlineSchemaChangeFlowableTask.cancel(true, taskId, taskService);

 // Verify the interactions with mocks
 Mockito.verify(scheduleTaskService).listTask(Mockito.any(Pageable.class), Mockito.anyLong());
 Mockito.verify(taskHandler).terminate(Mockito.anyLong(), Mockito.anyLong());
 Mockito.verify(taskService).cancel(taskId);
 }

 @Test
 public void testIsCancelled() {
 // Call method under test
 boolean result = onlineSchemaChangeFlowableTask.isCancelled();

 // Verify the result
 assertFalse(result);
 }

 private ScheduleTaskEntity createScheduleTaskEntity() {
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setStatus(TaskStatus.PREPARING);
 return scheduleTaskEntity;
 }
}