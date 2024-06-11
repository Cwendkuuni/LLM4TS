import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
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
 private DelegateExecution execution;

 @Mock
 private TaskService taskService;

 @Mock
 private FlowTaskUtil flowTaskUtil;

 @Mock
 private JsonUtils jsonUtils;

 @InjectMocks
 private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

 @Before
 public void init() {
 MockitoAnnotations.initMocks(this);
 }

 @After
 public void tearDown() {
 onlineSchemaChangeFlowableTask = null;
 }

 @Test
 public void testStart() throws Exception {
 // Prepare mock input
 Long taskId = 1L;
 User creator = new User();
 OnlineSchemaChangeParameters parameter = new OnlineSchemaChangeParameters();
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "testSchema";
 boolean continueOnError = true;

 // Set up expectations
 when(execution.getVariable(OnlineSchemaChangeFlowableTask.TASK_CREATOR_VAR))
 .thenReturn(creator);
 when(execution.getVariable(OnlineSchemaChangeFlowableTask.ONLINE_SCHEMA_CHANGE_PARAMETERS_VAR))
 .thenReturn(parameter);
 when(execution.getVariable(OnlineSchemaChangeFlowableTask.CONNECTION_CONFIG_VAR))
 .thenReturn(connectionConfig);
 when(execution.getVariable(OnlineSchemaChangeFlowableTask.SCHEMA_NAME_VAR))
 .thenReturn(schema);
 when(flowTaskUtil.getCloudMainAccountId(execution)).thenReturn("testUid");
 when(flowTaskUtil.isPublicCloud()).thenReturn(true);
 when(flowTaskUtil.getTaskCreator(execution)).thenReturn(creator);
 when(jsonUtils.toJson(any())).thenReturn("{}");

 // Call method under test
 onlineSchemaChangeFlowableTask.start(taskId, taskService, execution);

 // Verify the interactions
 verify(scheduleTaskRepository, times(1)).saveAndFlush(any(ScheduleTaskEntity.class));
 verify(taskHandler, times(1)).start(anyLong(), anyLong());
 }

 @Test
 public void testIsSuccessful() {
 // Prepare mock input
 onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;

 // Call method under test
 boolean result = onlineSchemaChangeFlowableTask.isSuccessful();

 // Verify the result
 assertTrue(result);
 }

 @Test
 public void testIsFailure() {
 // Prepare mock input
 onlineSchemaChangeFlowableTask.status = TaskStatus.FAILED;

 // Call method under test
 boolean result = onlineSchemaChangeFlowableTask.isFailure();

 // Verify the result
 assertTrue(result);
 }

 @Test
 public void testOnFailure() {
 // Prepare mock input
 Long taskId = 1L;

 // Call method under test
 onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);

 // Verify the interactions
 verify(taskService, times(1)).update(any(TaskEntity.class));
 }

 @Test
 public void testOnSuccessful() {
 // Prepare mock input
 Long taskId = 1L;

 // Call method under test
 onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskService);

 // Verify the interactions
 verify(flowTaskUtil, times(1)).updateFlowInstanceStatus(anyString());
 }

 @Test
 public void testOnTimeout() {
 // Prepare mock input
 Long taskId = 1L;

 // Call method under test
 onlineSchemaChangeFlowableTask.onTimeout(taskId, taskService);

 // Verify the interactions
 // No interaction expected
 }

 @Test
 public void testOnProgressUpdate() {
 // Prepare mock input
 Long taskId = 1L;
 Page<ScheduleTaskEntity> tasks = new Page<ScheduleTaskEntity>() {
 @Override
 public int getTotalPages() {
 return 1;
 }

 @Override
 public long getTotalElements() {
 return 1;
 }

 @Override
 public <T> List<T> collect(Collector<ScheduleTaskEntity, ?, List<T>> collector) {
 return null;
 }

 @Override
 public int getNumber() {
 return 0;
 }

 @Override
 public int getSize() {
 return 1;
 }

 @Override
 public boolean hasContent() {
 return true;
 }

 @Override
 public List<ScheduleTaskEntity> getContent() {
 List<ScheduleTaskEntity> list = new ArrayList<>();
 list.add(new ScheduleTaskEntity());
 return list;
 }

 @Override
 public boolean isFirst() {
 return false;
 }

 @Override
 public boolean isLast() {
 return false;
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
 public Pageable nextPageable() {
 return null;
 }

 @Override
 public Page<ScheduleTaskEntity> previousPage(Pageable pageable) {
 return null;
 }

 @Override
 public <U> Page<U> map(Function<ScheduleTaskEntity, U> converter) {
 return null;
 }
 };

 // Call method under test
 onlineSchemaChangeFlowableTask.onProgressUpdate(taskId, taskService);

 // Verify the interactions
 // No interaction expected
 }

 @Test
 public void testCancel() {
 // Prepare mock input
 Long taskId = 1L;
 Optional<ScheduleTaskEntity> runningEntity = Optional.of(new ScheduleTaskEntity());

 // Set up expectations
 when(scheduleTaskService.listTask(any(Pageable.class), anyLong()))
 .thenReturn(runningEntity);

 // Call method under test
 onlineSchemaChangeFlowableTask.cancel(true, taskId, taskService);

 // Verify the interactions
 verify(scheduleTaskService, times(1)).listTask(any(Pageable.class), anyLong());
 verify(taskHandler, times(1)).terminate(anyLong(), anyLong());
 verify(taskService, times(1)).cancel(taskId);
 }

 @Test
 public void testIsCancelled() {
 // Prepare mock input
 onlineSchemaChangeFlowableTask.status = TaskStatus.CANCELED;

 // Call method under test
 boolean result = onlineSchemaChangeFlowableTask.isCancelled();

 // Verify the result
 assertTrue(result);
 }
}