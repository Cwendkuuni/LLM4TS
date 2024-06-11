import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FlowTaskInstanceTest {

 @Mock
 private TaskService taskService;

 @Mock
 private ServiceTaskInstanceRepository serviceTaskRepository;

 @Mock
 private NodeInstanceEntityRepository nodeRepository;

 @Mock
 private SequenceInstanceRepository sequenceRepository;

 @Mock
 private FlowableAdaptor flowableAdaptor;

 @Mock
 private EventPublisher eventPublisher;

 @Mock
 private RuntimeDelegateMapper delegateConvertor;

 @InjectMocks
 private FlowTaskInstance flowTaskInstance;

 @Before
 public void setup() {
 executionStrategyConfig = ExecutionStrategyConfig.builder().strategy(FlowTaskExecutionStrategy.MANUAL)
 .pendingExpireIntervalSeconds(30)
 .executionTime(10)
 .build();
 }

 // Test for constructor with ServiceTaskInstanceEntity, RuntimeDelegateMapper, FlowableAdaptor, EventPublisher,
 // TaskService, NodeInstanceEntityRepository, SequenceInstanceRepository, and ServiceTaskInstanceRepository
 @Test
 public void testFlowTaskInstanceConstructionWithEntityAndDependencies() {
 ServiceTaskInstanceEntity entity = ServiceTaskInstanceEntity.builder()
 .id(1L)
 .organizationId(2L)
 .status(FlowNodeStatus.PENDING)
 .flowInstanceId(3L)
 .createTime(4L)
 .updateTime(5L)
 .startEndpoint(true)
 .endEndpoint(false)
 .strategy(FlowTaskExecutionStrategy.MANUAL)
 .waitExecExpireIntervalSeconds(10)
 .executionTime(5)
 .build();

 FlowTaskInstance taskInstance = new FlowTaskInstance(entity, delegateConvertor, flowableAdaptor, eventPublisher,
 taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

 assertEquals(entity.getId(), taskInstance.getId());
 assertEquals(entity.getOrganizationId(), taskInstance.getOrganizationId());
 assertEquals(entity.getStatus(), taskInstance.getStatus());
 assertEquals(entity.getFlowInstanceId(), taskInstance.getFlowInstanceId());
 assertEquals(entity.getCreateTime(), taskInstance.getCreateTime());
 assertEquals(entity.getUpdateTime(), taskInstance.getUpdateTime());
 assertEquals(entity.isStartEndpoint(), taskInstance.isStartEndpoint());
 assertEquals(entity.isEndEndpoint(), taskInstance.isEndEndPoint());
 assertEquals(FlowTaskExecutionStrategy.MANUAL, taskInstance.getStrategyConfig().getStrategy());
 assertEquals(10, taskInstance.getStrategyConfig().getPendingExpireIntervalSeconds());
 assertEquals(5, taskInstance.getStrategyConfig().getExecutionTime());
 }

 // Test for constructor with TaskType, Long, Long, ExecutionStrategyConfig, boolean, boolean,
 // RuntimeDelegateMapper, FlowableAdaptor, EventPublisher, TaskService, NodeInstanceEntityRepository,
 // SequenceInstanceRepository, and ServiceTaskInstanceRepository
 @Test
 public void testFlowTaskInstanceConstructionWithTaskTypeAndDependencies() {
 FlowTaskInstance taskInstance = new
 FlowTaskInstance(TaskType.TASK_TYPE_1,
 2L,
 3L,
 executionStrategyConfig,
 true,
 false,
 delegateConvertor,
 flowableAdaptor,
 eventPublisher,
 taskService,
 nodeRepository,
 sequenceRepository,
 serviceTaskRepository);

 assertNotNull(taskInstance.getId());
 assertEquals(FlowNodeStatus.PENDING, taskInstance.getStatus());
 assertEquals(2L, taskInstance.getOrganizationId());
 assertEquals(3L, taskInstance.getFlowInstanceId());
 assertEquals(true, taskInstance.isStartEndpoint());
 assertEquals(false, taskInstance.isEndEndPoint());
 assertEquals(FlowTaskExecutionStrategy.MANUAL, taskInstance.getStrategyConfig().getStrategy());
 assertEquals(10, taskInstance.getStrategyConfig().getPendingExpireIntervalSeconds());
 assertEquals(5, taskInstance.getStrategyConfig().getExecutionTime());
 }

 // Test for getCoreFlowableElementType method
 @Test
 public void testGetCoreFlowableElementType() {
 assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
 }

 // Test for batchCreate method with empty list
 @Test
 public void testBatchCreateWithEmptyList() {
 List<FlowTaskInstance> instances = Collections.emptyList();
 List<ServiceTaskInstanceEntity> entities =
 FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
 assertTrue(entities.isEmpty());
 }

 // Test for batchCreate method with non-empty list
 @Test
 public void testBatchCreateWithNonEmptyList() {
 List<FlowTaskInstance> instances = createFlowTaskInstanceList();
 List<ServiceTaskInstanceEntity> entities =
 FlowTaskInstance.batchCreate(instances, serviceTaskRepository);

 assertEquals(2, entities.size());

 for (int i = 0; i < instances.size(); i++) {
 ServiceTaskInstanceEntity entity = entities.get(i);
 FlowTaskInstance instance = instances.get(i);

 assertEquals(instance.getId(), entity.getId());
 assertEquals(instance.getOrganizationId(), entity.getOrganizationId());
 assertEquals(instance.getStatus(), entity.getStatus());
 assertEquals(instance.getFlowInstanceId(), entity.getFlowInstanceId());
 assertEquals(instance.getCreateTime(), entity.getCreateTime());
 assertEquals(instance.getUpdateTime(), entity.getUpdateTime());
 }
 }

 // Test for create method
 @Test
 public void testCreate() {
 when(serviceTaskRepository.save(any(ServiceTaskInstanceEntity.class)))
 .thenReturn(createServiceTaskInstanceEntity());

 flowTaskInstance.create();

 verify(serviceTaskRepository, times(1)).save(any(ServiceTaskInstanceEntity.class));
 }

 // Test for update method
 @Test
 public void testUpdate() {
 when(serviceTaskRepository.update(any(ServiceTaskInstanceEntity.class)))
 .thenReturn(1);

 flowTaskInstance.update();

 verify(serviceTaskRepository, times(1)).update(any(ServiceTaskInstanceEntity.class));
 }

 // Test for delete method
 @Test
 public void testDelete() {
 flowTaskInstance.delete(1L);

 verify(serviceTaskRepository, times(1)).deleteById(1L);
 }

 // Test for bindServiceTask method
 @Test
 public void testBindServiceTask() {
 BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
 flowTaskInstance.bindServiceTask(target);

 assertEquals(target, flowTaskInstance.targetTaskHandle);
 assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
 }

 // Test for isPresentOnThisMachine method
 @Test
 public void testIsPresentOnThisMachine() {
 flowTaskInstance.targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);

 assertTrue(flowTaskInstance.isPresentOnThisMachine());
 }

 // Test for isPresentOnThisMachine method with null targetTaskHandle
 @Test
 public void testIsPresentOnThisMachineWithNull() {
 assertFalse(flowTaskInstance.isPresentOnThisMachine());
 }

 // Test for dealloc method
 @Test
 public void testDealloc() {
 flowTaskInstance.listener = mock(ServiceTaskStartedListener.class);

 flowTaskInstance.dealloc();

 verify(eventPublisher, times(1)).removeEventListener(any(ServiceTaskStartedListener.class));
 }

 // Test for alloc method
 @Test
 public void testAlloc() {
 flowTaskInstance.listener = null;

 flowTaskInstance.alloc();

 assertNotNull(flowTaskInstance.listener);
 }

 // Test for complete method with aborted as false
 @Test
 public void testCompleteWithAbortedFalse() {
 List<FlowableElement> elements = createFlowableElementList();
 when(flowableAdaptor.getFlowableElementByType(anyLong(), any(FlowNodeType.class), any(FlowableElementType.class)))
 .thenReturn(elements);

 when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(anyLong()))
 .thenReturn(Optional.of("processInstanceId"));

 when(taskService.createTaskQuery().processInstanceId(anyString()).list())
 .thenReturn(createTaskList());

 flowTaskInstance.complete(false);

 verify(taskService, times(1)).complete(anyLong(), any(Map.class));
 }

 // Test for complete method with aborted as true
 @Test
 public void testCompleteWithAbortedTrue() {
 List<FlowableElement> elements = createFlowableElementList();
 when(flowableAdaptor.getFlowableElementByType(anyLong(), any(FlowNodeType.class), any(FlowableElementType.class)))
 .thenReturn(elements);

 when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(anyLong()))
 .thenReturn(Optional.of("processInstanceId"));

 when(taskService.createTaskQuery().processInstanceId(anyString()).list())
 .thenReturn(createTaskList());

 flowTaskInstance.complete(true);

 verify(taskService, times(1)).complete(anyLong(), any(Map.class));
 }

 // Test for confirmExecute method
 @Test
 public void testConfirmExecute() {
 flowTaskInstance.confirmExecute();

 verify(taskService, times(1)).complete(anyLong(), any(Map.class));
 }

 // Test for abort method
 @Test
 public void testAbort() {
 flowTaskInstance.abort();

 verify(taskService, times(1)).complete(anyLong(), any(Map.class));
 }

 // Test for cancel method
 @Test
 public void testCancel() throws InterruptedException, ExecutionException {
 flowTaskInstance.targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
 flowTaskInstance.cancel(true);

 verify(flowTaskInstance.targetTaskHandle, times(1)).cancel(true);
 }

 // Test for isDone method
 @Test
 public void testIsDone() throws InterruptedException, ExecutionException {
 flowTaskInstance.targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
 when(flowTaskInstance.targetTaskHandle.isDone()).thenReturn(true);

 assertTrue(flowTaskInstance.isDone());
 }

 // Test for getResult method
 @Test
 public void testGetResult() throws InterruptedException, ExecutionException {
 flowTaskInstance.targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
 when(flowTaskInstance.targetTaskHandle.get()).thenReturn("result");

 assertEquals("result", flowTaskInstance.getResult());
 }

 // Test for getResult method with timeout
 @Test
 public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
 flowTaskInstance.targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
 when(flowTaskInstance.targetTaskHandle.get(1, TimeUnit.SECONDS)).thenThrow(TimeoutException.class);

 try {
 flowTaskInstance.getResult(1, TimeUnit.SECONDS);
 } catch (TimeoutException e) {
 // Expected
 }
 }

 // Test for resourceType method
 @Test
 public void testResourceType() {
 assertEquals(ResourceType.ODC_FLOW_TASK_INSTANCE.name(), flowTaskInstance.resourceType());
 }

 // Test for getTargetDelegateClass method
 @Test
 public void testGetTargetDelegateClass() {
 Class<? extends BaseRuntimeFlowableDelegate<?>> delegateClass = flowTaskInstance.getTargetDelegateClass();

 // The returned class should be a subclass of BaseRuntimeFlowableDelegate<?>
 assertTrue(BaseRuntimeFlowableDelegate.class.isAssignableFrom(delegateClass));
 }

 private List<FlowableElement> createFlowableElementList() {
 return Collections.singletonList(
 FlowableElement.builder()
 .name("taskName")
 .build()
 );
 }

 private List<Task> createTaskList() {
 return Collections.singletonList(
 Task.builder()
 .id(1L)
 .name("taskName")
 .build()
 );
 }

 private ServiceTaskInstanceEntity createServiceTaskInstanceEntity() {
 return ServiceTaskInstanceEntity.builder()
 .id(1L)
 .organizationId(2L)
 .status(FlowNodeStatus.PENDING)
 .flowInstanceId(3L)
 .createTime(4L)
 .updateTime(5L)
 .startEndpoint(true)
 .endEndpoint(false)
 .strategy(FlowTaskExecutionStrategy.MANUAL)
 .waitExecExpireIntervalSeconds(10)
 .executionTime(5)
 .build();
 }

 private List<FlowTaskInstance> createFlowTaskInstanceList() {
 return Collections.singletonList(
 FlowTaskInstance.builder()
 .id(1L)
 .organizationId(2L)
 .status(FlowNodeStatus.PENDING)
 .flowInstanceId(3L)
 .createTime(4L)
 .updateTime(5L)
 .startEndpoint(true)
 .endEndPoint(false)
 .strategyConfig(executionStrategyConfig)
 .build()
 );
 }
}