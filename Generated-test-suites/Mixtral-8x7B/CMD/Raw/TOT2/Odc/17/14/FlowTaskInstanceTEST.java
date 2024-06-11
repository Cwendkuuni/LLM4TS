import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowNodeType;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.model.TaskInstanceCreatedEvent;
import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FlowTaskInstanceTest {

 private static final Long TEST_ORGANIZATION_ID = 1L;
 private static final Long TEST_FLOW_INSTANCE_ID = 2L;
 private static final String TEST_PROCESS_INSTANCE_ID = "testProcessInstanceId";
 private static final String TEST_PI_ID = "testPiId";
 private static final FlowableElementType TEST_NODE_TYPE = FlowableElementType.SERVICE_TASK;
 private static final FlowTaskExecutionStrategy TEST_STRATEGY = FlowTaskExecutionStrategy.MANUAL;
 private static final Integer TEST_WAIT_EXEC_EXPIRE_INTERVAL_SECONDS = 30;
 private static final Integer TEST_EXECUTION_TIME = 10;
 private static final Long TEST_TARGET_TASK_ID = 3L;
 private static final TaskType TEST_TASK_TYPE = TaskType.COMPUTE;
 private static final ServiceTaskStartedListener TEST_LISTENER = new ServiceTaskStartedListener(null);
 private static final EventPublisher TEST_EVENT_PUBLISHER = Mockito.mock(EventPublisher.class);
 private static final NodeInstanceEntityRepository TEST_NODE_REPOSITORY = Mockito.mock(NodeInstanceEntityRepository.class);
 private static final SequenceInstanceRepository TEST_SEQUENCE_REPOSITORY = Mockito.mock(SequenceInstanceRepository.class);
 private static final ServiceTaskInstanceRepository TEST_SERVICE_TASK_REPOSITORY = Mockito.mock(ServiceTaskInstanceRepository.class);
 private static final TaskService TEST_TASK_SERVICE = Mockito.mock(TaskService.class);
 private static final FlowableAdaptor TEST_FLOWABLE_ADAPTOR = new FlowableAdaptorImpl(TEST_TASK_SERVICE, TEST_NODE_REPOSITORY,
 TEST_SEQUENCE_REPOSITORY);

 private FlowTaskInstance flowTaskInstance;

 @Before
 public void setUp() {
 ExecutionStrategyConfig config = new ExecutionStrategyConfig();
 config.setStrategy(TEST_STRATEGY);
 config.setPendingExpireIntervalSeconds(TEST_WAIT_EXEC_EXPIRE_INTERVAL_SECONDS);
 config.setExecutionTime(TEST_EXECUTION_TIME);

 this.flowTaskInstance = new FlowTaskInstance(TEST_TASK_TYPE, TEST_ORGANIZATION_ID, TEST_FLOW_INSTANCE_ID, config,
 false, false, Mockito.mock(RuntimeDelegateMapper.class), TEST_FLOWABLE_ADAPTOR, TEST_EVENT_PUBLISHER,
 TEST_TASK_SERVICE, TEST_NODE_REPOSITORY, TEST_SEQUENCE_REPOSITORY, TEST_SERVICE_TASK_REPOSITORY);
 }

 @Test
 public void testCreate() {
 Instant beforeCreate = Instant.now();
 this.flowTaskInstance.create();
 Instant afterCreate = Instant.now();

 Assert.assertNotNull(this.flowTaskInstance.getId());
 Assert.assertTrue(Duration.between(beforeCreate, afterCreate).toMillis() > 0);

 Mockito.verify(TEST_SERVICE_TASK_REPOSITORY).save(Mockito.any(ServiceTaskInstanceEntity.class));
 }

 @Test
 public void testUpdate() {
 this.flowTaskInstance.setStatus(FlowNodeStatus.EXECUTING);

 Instant beforeUpdate = Instant.now();
 this.flowTaskInstance.update();
 Instant afterUpdate = Instant.now();

 Assert.assertEquals(FlowNodeStatus.EXECUTING, this.flowTaskInstance.getStatus());
 Assert.assertTrue(Duration.between(beforeUpdate, afterUpdate).toMillis() > 0);

 Mockito.verify(TEST_SERVICE_TASK_REPOSITORY).update(Mockito.any(ServiceTaskInstanceEntity.class));
 }

 @Test
 public void testDelete() {
 Long id = 1L;
 this.flowTaskInstance.setId(id);

 this.flowTaskInstance.delete(id);

 Mockito.verify(TEST_SERVICE_TASK_REPOSITORY).deleteById(id);
 }

 @Test
 public void testBatchCreate() {
 List<FlowTaskInstance> instances = Collections.singletonList(this.flowTaskInstance);

 List<ServiceTaskInstanceEntity> entities =
 FlowTaskInstance.batchCreate(instances, TEST_SERVICE_TASK_REPOSITORY);

 Assert.assertEquals(1, entities.size());
 Assert.assertEquals(this.flowTaskInstance.getId(), entities.get(0).getId());
 }

 @Test
 public void testBindServiceTask() {
 BaseRuntimeFlowableDelegate mockTask = Mockito.mock(BaseRuntimeFlowableDelegate.class);
 this.flowTaskInstance.bindServiceTask(mockTask);

 Assert.assertSame(mockTask, this.flowTaskInstance.getTargetTaskHandle());
 Assert.assertEquals(FlowNodeStatus.EXECUTING, this.flowTaskInstance.getStatus());
 }

 @Test
 public void testIsPresentOnThisMachine() {
 Assert.assertFalse(this.flowTaskInstance.isPresentOnThisMachine());

 this.flowTaskInstance.bindServiceTask(Mockito.mock(BaseRuntimeFlowableDelegate.class));
 Assert.assertTrue(this.flowTaskInstance.isPresentOnThisMachine());
 }

 @Test
 public void testDealloc() {
 this.flowTaskInstance.bindServiceTask(Mockito.mock(BaseRuntimeFlowableDelegate.class));
 this.flowTaskInstance.dealloc();

 Assert.assertNull(this.flowTaskInstance.getTargetTaskHandle());
 Assert.assertNull(this.flowTaskInstance.getListener());
 }

 @Test
 public void testAlloc() {
 this.flowTaskInstance.alloc();

 Assert.assertSame(TEST_LISTENER, this.flowTaskInstance.getListener());
 }

 @Test
 public void testComplete() {
 this.flowTaskInstance.bindServiceTask(Mockito.mock(BaseRuntimeFlowableDelegate.class));

 this.flowTaskInstance.complete(false);

 Mockito.verify(TEST_TASK_SERVICE).complete(Mockito.anyString(), Mockito.anyMap());
 }

 @Test
 public void testAbort() {
 this.flowTaskInstance.bindServiceTask(Mockito.mock(BaseRuntimeFlowableDelegate.class));

 this.flowTaskInstance.abort();

 Mockito.verify(TEST_TASK_SERVICE).complete(Mockito.anyString(), Mockito.anyMap());
 }

 @Test
 public void testMapToTaskEntity() {
 ServiceTaskInstanceEntity entity = FlowTaskInstance.mapToTaskEntity(this.flowTaskInstance);

 Assert.assertEquals( TEST_ORGANIZATION_ID, entity.getOrganizationId());
 Assert.assertEquals( FlowNodeStatus.PENDING, entity.getStatus());
 Assert.assertEquals( TEST_FLOW_INSTANCE_ID, entity.getFlowInstanceId());
 Assert.assertFalse( entity.isStartEndpoint());
 Assert.assertFalse( entity.isEndEndpoint());
 Assert.assertEquals( TEST_STRATEGY, entity.getStrategy());
 Assert.assertEquals( TEST_TASK_TYPE, entity.getTaskType());
 Assert.assertEquals( TEST_TARGET_TASK_ID, entity.getTargetTaskId());
 Assert.assertEquals( TEST_WAIT_EXEC_EXPIRE_INTERVAL_SECONDS, entity.getWaitExecExpireIntervalSeconds());
 Assert.assertEquals( TEST_EXECUTION_TIME, entity.getExecutionTime());
 }

 @Test
 public void testEventPublish() {
 Mockito.verify(TEST_EVENT_PUBLISHER).publishEvent(Mockito.any(TaskInstanceCreatedEvent.class));
 }

 @Test
 public void testGetTargetDelegateClass() {
 Class<? extends BaseRuntimeFlowableDelegate<?>> clazz =
 this.flowTaskInstance.getTargetDelegateClass();

 Assert.assertNotNull(clazz);
 Assert.assertTrue(BaseRuntimeFlowableDelegate.class.isAssignableFrom(clazz));
 }
}