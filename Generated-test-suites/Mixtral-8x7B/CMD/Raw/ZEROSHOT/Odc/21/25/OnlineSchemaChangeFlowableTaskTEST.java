import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.flowable.engine.delegate.DelegateExecution;
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
 private TaskService taskService;

 @Mock
 private DelegateExecution execution;

 @Mock
 private ConnectionConfig connectionConfig;

 @Mock
 private OnlineSchemaChangeParameters parameter;

 @Mock
 private ScheduleEntity scheduleEntity;

 @Mock
 private ScheduleTaskEntity scheduleTaskEntity;

 @Mock
 private OnlineSchemaChangeScheduleTaskParameters onlineSchemaChangeScheduleTaskParameters;

 @Mock
 private TaskEntity taskEntity;

 @Mock
 private Page<ScheduleTaskEntity> tasks;

 @InjectMocks
 private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testStart() throws Exception {
 onlineSchemaChangeFlowableTask.start(1L, taskService, execution);
 verify(taskService, times(1)).start(1L);
 verify(execution, times(1)).setVariable(eq("uid"), anyString());
 verify(execution, times(1)).setVariable(eq("parameters"), any());
 verify(execution, times(1)).setVariable(eq("connectionConfig"), any());
 verify(execution, times(1)).setVariable(eq("schema"), anyString());
 verify(scheduleTaskRepository, times(1)).saveAndFlush(any(ScheduleTaskEntity.class));
 }

 @Test
 public void testIsSuccessful() {
 onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;
 assertEquals(true, onlineSchemaChangeFlowableTask.isSuccessful());
 }

 @Test
 public void testIsFailure() {
 onlineSchemaChangeFlowableTask.status = TaskStatus.FAILED;
 assertEquals(true, onlineSchemaChangeFlowableTask.isFailure());
 }

 @Test
 public void testOnFailure() {
 onlineSchemaChangeFlowableTask.onFailure(1L, taskService);
 verify(taskService, times(1)).cancel(1L);
 }

 @Test
 public void testOnSuccessful() {
 onlineSchemaChangeFlowableTask.updateFlowInstanceStatus(FlowStatus.EXECUTION_SUCCEEDED);
 onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);
 verify(taskService, times(1)).update(any(TaskEntity.class));
 }

 @Test
 public void testOnProgressUpdate() {
 onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);
 verify(taskService, times(1)).update(any(TaskEntity.class));
 }

 @Test
 public void testCancel() {
 onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService);
 verify(scheduleTaskService, times(1)).listTask(any(Pageable.class), anyLong());
 verify(taskHandler, times(1)).terminate(anyLong(), anyLong());
 verify(taskService, times(1)).cancel(1L);
 }

 @Test
 public void testIsCancelled() {
 onlineSchemaChangeFlowableTask.status = TaskStatus.CANCELED;
 assertEquals(true, onlineSchemaChangeFlowableTask.isCancelled());
 }

 // Add more test methods for other methods if needed
}