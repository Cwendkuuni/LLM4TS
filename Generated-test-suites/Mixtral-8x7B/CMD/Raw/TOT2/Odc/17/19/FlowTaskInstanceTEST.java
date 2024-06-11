import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FlowTaskInstanceTest {

 @Mock
 private TaskService taskService;

 @Mock
 private ServiceTaskInstanceRepository serviceTaskRepository;

 @Mock
 private EventPublisher eventPublisher;

 @Mock
 private RuntimeDelegateMapper delegateConvertor;

 @Mock
 private FlowableAdaptor flowableAdaptor;

 @Mock
 private NodeInstanceEntityRepository nodeRepository;

 @Mock
 private SequenceInstanceRepository sequenceRepository;

 @InjectMocks
 private FlowTaskInstance flowTaskInstance;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testCreate() {
 FlowTaskInstance instance = new FlowTaskInstance(TaskType.SERVICE_TASK, 1L, 1L,
 ExecutionStrategyConfig.builder().build(), true, true, delegateConvertor, flowableAdaptor, eventPublisher,
 taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

 instance.create();

 Verify.notNull(instance.getId(), "id");
 Verify.notNull(instance.getCreateTime(), "CreateTime");
 Verify.notNull(instance.getUpdateTime(), "UpdateTime");
 }

 @Test
 public void testUpdate() {
 FlowTaskInstance instance = new FlowTaskInstance(TaskType.SERVICE_TASK, 1L, 1L,
 ExecutionStrategyConfig.builder().build(), true, true, delegateConvertor, flowableAdaptor, eventPublisher,
 taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

 instance.setId(1L);
 instance.setStatus(FlowNodeStatus.EXECUTING);

 instance.update();

 verify(serviceTaskRepository).update(instance.mapToTaskEntity(instance));
 }

 @Test
 public void testDelete() {
 FlowTaskInstance instance = new FlowTaskInstance(TaskType.SERVICE_TASK, 1L, 1L,
 ExecutionStrategyConfig.builder().build(), true, true, delegateConvertor, flowableAdaptor, eventPublisher,
 taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

 instance.setId(1L);

 instance.delete(1L);

 verify(serviceTaskRepository).deleteById(1L);
 }

 @Test
 public void testBatchCreate() {
 List<FlowTaskInstance> instances = new ArrayList<>();

 instances.add(new FlowTaskInstance(TaskType.SERVICE_TASK, 1L, 1L,
 ExecutionStrategyConfig.builder().build(), true, true, delegateConvertor, flowableAdaptor, eventPublisher,
 taskService, nodeRepository, sequenceRepository, serviceTaskRepository));

 List<ServiceTaskInstanceEntity> entities =
 flowTaskInstance.batchCreate(instances, serviceTaskRepository);

 assertEquals(1, entities.size());
 }

 @Test
 public void testBindServiceTask() {
 FlowTaskInstance instance = new FlowTaskInstance(TaskType.SERVICE_TASK, 1L, 1L,
 ExecutionStrategyConfig.builder().build(), true, true, delegateConvertor, flowableAdaptor, eventPublisher,
 taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

 instance.setId(1L);

 BaseRuntimeFlowableDelegate mockDelegate = mock(BaseRuntimeFlowableDelegate.class);

 instance.bindServiceTask(mockDelegate);

 assertEquals(FlowNodeStatus.EXECUTING, instance.getStatus());
 }

 @Test
 public void testIsPresentOnThisMachine() {
 FlowTaskInstance instance = new FlowTaskInstance(TaskType.SERVICE_TASK, 1L, 1L,
 ExecutionStrategyConfig.builder().build(), true, true, delegateConvertor, flowableAdaptor, eventPublisher,
 taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

 instance.targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);

 assertTrue(instance.isPresentOnThisMachine());

 instance.targetTaskHandle = null;

 assertFalse(instance.isPresentOnThisMachine());
 }

 @Test
 public void testDealloc() {
 FlowTaskInstance instance = new FlowTaskInstance(TaskType.SERVICE_TASK, 1L, 1L,
 ExecutionStrategyConfig.builder().build(), true, true, delegateConvertor, flowableAdaptor, eventPublisher,
 taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

 instance.listener = new ServiceTaskStartedListener(instance);

 instance.dealloc();

 assertNull(instance.listener);
 }

 @Test
 public void testAlloc() {
 FlowTaskInstance instance = new FlowTaskInstance(TaskType.SERVICE_TASK, 1L, 1L,
 ExecutionStrategyConfig.builder().build(), true, true, delegateConvertor, flowableAdaptor, eventPublisher,
 taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

 instance.listener = null;

 instance.alloc();

 assertNotNull(instance.listener);
 }

 @Test
 public void testConfirmExecute() {
 FlowTaskInstance instance = new FlowTaskInstance(TaskType.SERVICE_TASK, 1L, 1L,
 ExecutionStrategyConfig.builder().build(), true, true, delegateConvertor, flowableAdaptor, eventPublisher,
 taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

 instance.setStatus(FlowNodeStatus.PENDING);

 instance.confirmExecute();

 assertEquals(FlowNodeStatus.COMPLETED, instance.getStatus());
 }

 @Test
 public void testAbort() {
 FlowTaskInstance instance = new FlowTaskInstance(TaskType.SERVICE_TASK, 1L, 1L,
 ExecutionStrategyConfig.builder().build(), true, true, delegateConvertor, flowableAdaptor, eventPublisher,
 taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

 instance.setStatus(FlowNodeStatus.PENDING);

 instance.abort();

 assertEquals(FlowNodeStatus.ABORTED, instance.getStatus());
 }

 @Test
 public void testCancel() {
 FlowTaskInstance instance = new FlowTaskInstance(TaskType.SERVICE_TASK, 1L, 1L,
 ExecutionStrategyConfig.builder().build(), true, true, delegateConvertor, flowableAdaptor, eventPublisher,
 taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

 instance.targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);

 instance.cancel(true);

 verify(instance.targetTaskHandle).cancel(true);
 }

 @Test
 public void testIsDone() {
 FlowTaskInstance instance = new FlowTaskInstance(TaskType.SERVICE_TASK, 1L, 1L,
 ExecutionStrategyConfig.builder().build(), true, true, delegateConvertor, flowableAdaptor, eventPublisher,
 taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

 instance.targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);

 when(instance.targetTaskHandle.isDone()).thenReturn(true);

 assertTrue(instance.isDone());
 }

 @Test
 public void testGetResult() throws InterruptedException, ExecutionException {
 FlowTaskInstance instance = new FlowTaskInstance(TaskType.SERVICE_TASK, 1L, 1L,
 ExecutionStrategyConfig.builder().build(), true, true, delegateConvertor, flowableAdaptor, eventPublisher,
 taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

 instance.targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);

 when(instance.targetTaskHandle.get()).thenReturn("result");

 assertEquals("result", instance.getResult());
 }

 @Test
 public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
 FlowTaskInstance instance = new FlowTaskInstance(TaskType.SERVICE_TASK, 1L, 1L,
 ExecutionStrategyConfig.builder().build(), true, true, delegateConvertor, flowableAdaptor, eventPublisher,
 taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

 instance.targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);

 when(instance.targetTaskHandle.get(1, TimeUnit.SECONDS)).thenReturn("result");

 assertEquals("result", instance.getResult(1, TimeUnit.SECONDS));
 }

 @Test
 public void testMapToTaskEntity() {
 FlowTaskInstance instance = new FlowTaskInstance(TaskType.SERVICE_TASK, 1L, 1L,
 ExecutionStrategyConfig.builder().build(), true, true, delegateConvertor, flowableAdaptor, eventPublisher,
 taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

 ServiceTaskInstanceEntity entity = instance.mapToTaskEntity(instance);

 assertEquals(1L, entity.getOrganizationId());
 assertEquals(FlowNodeStatus.PENDING, entity.getStatus());
 assertEquals(1L, entity.getFlowInstanceId());
 assertTrue(entity.isStartEndpoint());
 assertTrue(entity.isEndEndpoint());
 assertEquals(FlowTaskExecutionStrategy.DEFAULT, entity.getStrategy());
 assertEquals(TaskType.SERVICE_TASK, entity.getTaskType());
 assertNull(entity.getTargetTaskId());
 assertEquals(0, entity.getWaitExecExpireIntervalSeconds());
 assertEquals(0, entity.getExecutionTime());
 }

 @Test
 public void testGetTargetDelegateClass() {
 FlowTaskInstance instance = new FlowTaskInstance(TaskType.SERVICE_TASK, 1L, 1L,
 ExecutionStrategyConfig.builder().build(), true, true, delegateConvertor, flowableAdaptor, eventPublisher,
 taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

 Class<? extends BaseRuntimeFlowableDelegate<?>> delegateClass =
 instance.getTargetDelegateClass();

 assertNotNull(delegateClass);
 }
}