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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FlowTaskInstanceTest {

 @Mock
 private TaskService taskService;

 @Mock
 private EventPublisher eventPublisher;

 @Mock
 private ServiceTaskInstanceRepository serviceTaskRepository;

 @Mock
 private NodeInstanceEntityRepository nodeInstanceEntityRepository;

 @Mock
 private SequenceInstanceRepository sequenceInstanceRepository;

 @Mock
 private FlowableAdaptor flowableAdaptor;

 @Mock
 private RuntimeDelegateMapper delegateConvertor;

 @InjectMocks
 private FlowTaskInstance flowTaskInstance;

 @Before
 public void setup() {
 flowTaskInstance = new FlowTaskInstance(TaskType.SERVICE_TASK, 1L, 2L, new ExecutionStrategyConfig(), true, true,
 delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeInstanceEntityRepository,
 sequenceInstanceRepository, serviceTaskRepository);
 }

 @Test
 public void testConstructor() {
 assertNotNull(flowTaskInstance);
 }

 @Test
 public void testGetCoreFlowableElementType() {
 assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
 }

 @Test
 public void testBatchCreate() {
 List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
 List<ServiceTaskInstanceEntity> entities = Collections.singletonList(new ServiceTaskInstanceEntity());
 when(serviceTaskRepository.batchCreate(instances.stream().map(FlowTaskInstance::mapToTaskEntity)
 .collect(Collectors.toList())))
 .thenReturn(entities);
 List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
 assertEquals(instances, result);
 }

 @Test
 public void testCreate() {
 flowTaskInstance.create();
 // Add assertions for successful creation
 }

 @Test(expected = IllegalStateException.class)
 public void testCreateWhenExists() {
 flowTaskInstance.setStatus(FlowNodeStatus.EXECUTING);
 flowTaskInstance.create();
 }

 @Test
 public void testUpdate() {
 flowTaskInstance.update();
 // Add assertions for successful update
 }

 @Test(expected = IllegalStateException.class)
 public void testUpdateWhenNotExists() {
 flowTaskInstance.setId(null);
 flowTaskInstance.update();
 }

 @Test
 public void testDelete() {
 flowTaskInstance.delete(1L);
 // Add assertions for successful deletion
 }

 @Test(expected = IllegalStateException.class)
 public void testDeleteWhenNotExists() {
 flowTaskInstance.setId(null);
 flowTaskInstance.delete(1L);
 }

 @Test
 public void testBindServiceTask() {
 // Create a mock delegate
 BaseRuntimeFlowableDelegate mockDelegate = mock(BaseRuntimeFlowableDelegate.class);
 when(delegateConvertor.map(TaskType.SERVICE_TASK))
 .thenReturn(mockDelegate.getClass());

 // Call the method
 flowTaskInstance.bindServiceTask(mockDelegate);

 // Add assertions
 assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
 verify(mockDelegate).setTargetTaskInstanceId(flowTaskInstance.getId());
 }

 @Test
 public void testIsPresentOnThisMachine() {
 // Test when the delegate is present
 flowTaskInstance.targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
 assertTrue(flowTaskInstance.isPresentOnThisMachine());

 // Test when the delegate is not present
 flowTaskInstance.targetTaskHandle = null;
 assertFalse(flowTaskInstance.isPresentOnThisMachine());
 }

 @Test
 public void testDealloc() {
 flowTaskInstance.dealloc();
 // Add assertions for successful deallocation
 }

 @Test
 public void testAlloc() {
 flowTaskInstance.alloc();
 // Add assertions for successful allocation
 }

 @Test
 public void testConfirmExecute() {
 flowTaskInstance.confirmExecute();
 // Add assertions for successful completion
 }

 @Test
 public void testAbort() {
 flowTaskInstance.abort();
 // Add assertions for successful abortion
 }

 @Test
 public void testCancel() {
 // Create a mock delegate
 BaseRuntimeFlowableDelegate mockDelegate = mock(BaseRuntimeFlowableDelegate.class);
 when(delegateConvertor.map(TaskType.SERVICE_TASK))
 .thenReturn(mockDelegate.getClass());

 // Call the method
 flowTaskInstance.bindServiceTask(mockDelegate);
 flowTaskInstance.cancel(true);

 // Add assertions
 verify(mockDelegate).cancel(true);
 }

 @Test
 public void testIsDone() {
 // Create a mock delegate
 BaseRuntimeFlowableDelegate mockDelegate = mock(BaseRuntimeFlowableDelegate.class);
 when(delegateConvertor.map(TaskType.SERVICE_TASK))
 .thenReturn(mockDelegate.getClass());

 // Call the method
 flowTaskInstance.bindServiceTask(mockDelegate);
 when(mockDelegate.isDone()).thenReturn(true);

 // Test the method
 assertTrue(flowTaskInstance.isDone());
 }

 @Test
 public void testGetResult() throws InterruptedException, ExecutionException {
 // Create a mock delegate
 BaseRuntimeFlowableDelegate mockDelegate = mock(BaseRuntimeFlowableDelegate.class);
 when(delegateConvertor.map(TaskType.SERVICE_TASK))
 .thenReturn(mockDelegate.getClass());

 // Call the method
 flowTaskInstance.bindServiceTask(mockDelegate);
 when(mockDelegate.get()).thenReturn("Result");

 // Test the method
 assertEquals("Result", flowTaskInstance.getResult());
 }

 @Test
 public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
 // Create a mock delegate
 BaseRuntimeFlowableDelegate mockDelegate = mock(BaseRuntimeFlowableDelegate.class);
 when(delegateConvertor.map(TaskType.SERVICE_TASK))
 .thenReturn(mockDelegate.getClass());

 // Call the method
 flowTaskInstance.bindServiceTask(mockDelegate);
 when(mockDelegate.get(1, TimeUnit.SECONDS)).thenReturn("Result");

 // Test the method
 assertEquals("Result", flowTaskInstance.getResult(1, TimeUnit.SECONDS));
 }

 @Test
 public void testGetTargetDelegateClass() {
 Class<? extends BaseRuntimeFlowableDelegate<?>> delegateClass =
 flowTaskInstance.getTargetDelegateClass();
 assertNotNull(delegateClass);
 }

 @Test
 public void testMapToTaskEntity() {
 ServiceTaskInstanceEntity entity =
 flowTaskInstance.mapToTaskEntity(flowTaskInstance);
 assertNotNull(entity);
 }
}