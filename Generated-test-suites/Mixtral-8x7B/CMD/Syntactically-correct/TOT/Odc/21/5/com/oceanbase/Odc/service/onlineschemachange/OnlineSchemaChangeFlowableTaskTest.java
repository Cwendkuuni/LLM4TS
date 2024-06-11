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
import org.springframework.data.domain.PageImpl;
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
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskService;

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

 @InjectMocks
 private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

 private DelegateExecution delegateExecution;

 @BeforeEach
 void setUp() {
 MockitoAnnotations.openMocks(this);

 delegateExecution = Mockito.mock(DelegateExecution.class);

 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setContinueOnError(true);

 FlowTaskUtil.setOnlineSchemaChangeParameter(delegateExecution, parameters);

 ConnectionConfig connectionConfig = new ConnectionConfig();
 FlowTaskUtil.setConnectionConfig(delegateExecution, connectionConfig);

 String schema = "test_schema";
 FlowTaskUtil.setSchemaName(delegateExecution, schema);
 }

 @Test
 void testStartSuccess() throws Exception {
 // Arrange
 Page<ScheduleTaskEntity> tasks = new PageImpl<>(new ArrayList<>());
 Mockito.when(scheduleTaskService.listTask(Pageable.unpaged(), Mockito.anyLong()))
 .thenReturn(tasks);

 // Act
 onlineSchemaChangeFlowableTask.start(1L, Mockito.mock(TaskService.class), delegateExecution);

 // Assert
 Mockito.verify(scheduleTaskRepository, Mockito.times(1)).saveAndFlush(Mockito.any(ScheduleTaskEntity.class));
 Mockito.verify(taskHandler, Mockito.times(1)).start(Mockito.anyLong(), Mockito.anyLong());
 }

 @Test
 void testProgressStatusUpdate() {
 // Arrange
 List<ScheduleTaskEntity> tasks = new ArrayList<>();
 tasks.add(createScheduleTaskEntity(TaskStatus.DONE));
 tasks.add(createScheduleTaskEntity(TaskStatus.FAILED));
 tasks.add(createScheduleTaskEntity(TaskStatus.RUNNING));

 Page<ScheduleTaskEntity> taskPage = new PageImpl<>(tasks);

 // Act
 onlineSchemaChangeFlowableTask.progressStatusUpdate(taskPage);

 // Assert
 assertEquals(TaskStatus.FAILED, onlineSchemaChangeFlowableTask.status);
 }

 @Test
 void testSingleTaskPercentage() {
 // Arrange
 ScheduleTaskEntity taskEntity = createScheduleTaskEntity(TaskStatus.RUNNING);
 taskEntity.setProgressPercentage(50);

 // Act
 double percentage = onlineSchemaChangeFlowableTask.singleTaskPercentage(taskEntity);

 // Assert
 assertEquals(0.5, percentage);
 }

 @Test
 void testCreateScheduleEntity() {
 // Arrange
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setContinueOnError(false);

 FlowTaskUtil.setOnlineSchemaChangeParameter(delegateExecution, parameters);

 // Act
 ScheduleEntity scheduleEntity = onlineSchemaChangeFlowableTask.createScheduleEntity(
 Mockito.mock(ConnectionConfig.class), Mockito.mock(OnlineSchemaChangeParameters.class), "test_schema");

 // Assert
 assertNotNull(scheduleEntity);
 }

 @Test
 void testCreateScheduleTaskEntity() {
 // Arrange
 OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();
 param.setUid("test_uid");

 // Act
 ScheduleTaskEntity scheduleTaskEntity = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(1L, param);

 // Assert
 assertNotNull(scheduleTaskEntity);
 }

 private ScheduleTaskEntity createScheduleTaskEntity(TaskStatus taskStatus) {
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setStatus(taskStatus);
 return scheduleTaskEntity;
 }
}