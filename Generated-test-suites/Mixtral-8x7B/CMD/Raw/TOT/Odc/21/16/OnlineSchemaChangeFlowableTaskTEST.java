import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
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

 @InjectMocks
 private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

 private DelegateExecution delegateExecution;

 private TaskService taskService;

 @BeforeEach
 public void setup() {
 MockitoAnnotations.initMocks(this);
 delegateExecution = Mockito.mock(DelegateExecution.class);
 taskService = Mockito.mock(TaskService.class);
 }

 @Test
 public void testStart() throws Exception {
 // Prepare inputs
 Long taskId = 1L;
 User creator = new User();
 creator.setId(1L);
 creator.setOrganizationId(1L);
 ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
 OnlineSchemaChangeParameters parameter = Mockito.mock(OnlineSchemaChangeParameters.class);
 String schema = "testSchema";

 // Mock methods
 Mockito.when(delegateExecution.getVariable(FlowTaskUtil.TASK_CREATOR_VAR))
 .thenReturn(creator);
 Mockito.when(delegateExecution.getVariable(FlowTaskUtil.ONLINE_SCHEMA_CHANGE_PARAMETERS_VAR))
 .thenReturn(parameter);
 Mockito.when(delegateExecution.getVariable(FlowTaskUtil.CONNECTION_CONFIG_VAR))
 .thenReturn(connectionConfig);
 Mockito.when(delegateExecution.getVariable(FlowTaskUtil.SCHEMA_NAME_VAR))
 .thenReturn(schema);
 Mockito.when(delegateExecution.getVariable(FlowTaskUtil.CLOUD_MAIN_ACCOUNT_ID_VAR))
 .thenReturn("testUid");

 // Call method to test
 onlineSchemaChangeFlowableTask.start(taskId, taskService, delegateExecution);

 // Verify method calls
 Mockito.verify(taskService).start(taskId);
 Mockito.verify(delegateExecution).setVariable(FlowTaskUtil.SCHEDULE_ID_VAR,
 onlineSchemaChangeFlowableTask.scheduleId);
 Mockito.verify(delegateExecution).setVariable(FlowTaskUtil.ORGANIZATION_ID_VAR,
 creator.getOrganizationId());
 Mockito.verify(delegateExecution).setVariable(FlowTaskUtil.CONTINUE_ON_ERROR_VAR,
 parameter.isContinueOnError());
 }

 @Test
 public void testIsSuccessful() {
 onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;
 assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());

 onlineSchemaChangeFlowableTask.status = TaskStatus.RUNNING;
 assertFalse(onlineSchemaChangeFlowableTask.isSuccessful());
 }

 @Test
 public void testIsFailure() {
 onlineSchemaChangeFlowableTask.status = TaskStatus.FAILED;
 assertTrue(onlineSchemaChangeFlowableTask.isFailure());

 onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;
 assertFalse(onlineSchemaChangeFlowableTask.isFailure());
 }

 @Test
 public void testOnFailure() {
 Long taskId = 1L;
 onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);

 // Verify method calls
 Mockito.verify(taskService).update(taskId, TaskStatus.FAILED, null);
 }

 @Test
 public void testOnSuccessful() {
 Long taskId = 1L;
 onlineSchemaChangeFlowableTask.updateFlowInstanceStatus = Mockito.mock(BaseODCFlowTaskDelegate
 .UpdateFlowInstanceStatus.class);

 onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskService);

 // Verify method calls
 Mockito.verify(onlineSchemaChangeFlowableTask.updateFlowInstanceStatus)
 .update(taskId, FlowStatus.EXECUTION_SUCCEEDED);
 }

 @Test
 public void testOnTimeout() {
 Long taskId = 1L;
 onlineSchemaChangeFlowableTask.onTimeout(taskId, taskService);

 // Verify method calls
 Mockito.verify(taskService).update(taskId, TaskStatus.TIMED_OUT, null);
 }

 @Test
 public void testOnProgressUpdate() {
 Long taskId = 1L;
 Page<ScheduleTaskEntity> tasks = Mockito.mock(Page.class);
 OnlineSchemaChangeTaskResult result = new OnlineSchemaChangeTaskResult();
 result.setTasks(new ArrayList<>());

 TaskEntity taskEntity = new TaskEntity();
 taskEntity.setId(taskId);
 taskEntity.setStatus(TaskStatus.RUNNING);
 taskEntity.setProgressPercentage(50);

 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setTask(taskEntity);
 result.getTasks().add(scheduleTaskEntity);

 Mockito.when(scheduleTaskService.listTask(Pageable.unpaged(), 1L))
 .thenReturn(tasks);
 Mockito.when(tasks.getContent())
 .thenReturn(result.getTasks());
 Mockito.when(JsonUtils.toJson(result))
 .thenReturn("{}");

 onlineSchemaChangeFlowableTask.onProgressUpdate(taskId, taskService);

 // Verify method calls
 Mockito.verify(delegateExecution).setVariable(BaseODCFlowTaskDelegate.TASK_RESULT_VAR, "{}");
 Mockito.verify(delegateExecution).setVariable(BaseODCFlowTaskDelegate.TASK_STATUS_VAR,
 TaskStatus.RUNNING);
 Mockito.verify(delegateExecution).setVariable(BaseODCFlowTaskDelegate.PROGRESS_PERCENTAGE_VAR, 50.0);
 }

}