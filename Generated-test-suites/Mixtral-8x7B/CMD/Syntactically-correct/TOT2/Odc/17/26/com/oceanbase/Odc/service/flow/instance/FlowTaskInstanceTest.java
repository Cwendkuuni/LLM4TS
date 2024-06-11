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
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FlowTaskInstanceTest {

 @Mock
 private TaskService taskService;

 @Mock
 private NodeInstanceEntityRepository nodeRepository;

 @Mock
 private SequenceInstanceRepository sequenceRepository;

 @Mock
 private ServiceTaskInstanceRepository serviceTaskRepository;

 @Mock
 private FlowableAdaptor flowableAdaptor;

 @Mock
 private EventPublisher eventPublisher;

 @Mock
 private RuntimeDelegateMapper delegateConvertor;

 @InjectMocks
 private FlowTaskInstance flowTaskInstance;

 private ServiceTaskInstanceEntity serviceTaskInstanceEntity;

 @Before
 public void setup() {
 serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
 serviceTaskInstanceEntity.setId(1L);
 serviceTaskInstanceEntity.setOrganizationId(1L);
 serviceTaskInstanceEntity.setStatus(FlowNodeStatus.PENDING);
 serviceTaskInstanceEntity.setFlowInstanceId(1L);
 serviceTaskInstanceEntity.setCreateTime(System.currentTimeMillis());
 serviceTaskInstanceEntity.setUpdateTime(System.currentTimeMillis());
 serviceTaskInstanceEntity.setTargetTaskId(null);
 serviceTaskInstanceEntity.setTaskType(TaskType.MANUAL);
 serviceTaskInstanceEntity.setStrategy(FlowTaskExecutionStrategy.AUTO);
 }

 @Test
 public void testCreate() {
 flowTaskInstance.create();
 verify(serviceTaskRepository, times(1)).save(serviceTaskInstanceEntity);
 }

 @Test
 public void testBatchCreate() {
 List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
 when(serviceTaskRepository.batchCreate(anyList())).thenReturn(Collections.singletonList(serviceTaskInstanceEntity));
 List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
 assertEquals(1, result.size());
 }

 @Test
 public void testUpdate() {
 flowTaskInstance.update();
 verify(serviceTaskRepository, times(1)).update(serviceTaskInstanceEntity);
 }

 @Test
 public void testDelete() {
 flowTaskInstance.delete(1L);
 verify(serviceTaskRepository, times(1)).deleteById(1L);
 }

 @Test
 public void testBindServiceTask() {
 BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
 flowTaskInstance.bindServiceTask(target);
 verify(target, times(1)).setTargetTaskInstanceId(1L);
 assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
 }

 @Test
 public void testIsPresentOnThisMachine() {
 BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
 when(target.getTargetTaskInstanceId()).thenReturn(1L);
 flowTaskInstance.bindServiceTask(target);
 assertEquals(true, flowTaskInstance.isPresentOnThisMachine());
 }

 @Test
 public void testDealloc() {
 flowTaskInstance.dealloc();
 verify(eventPublisher, times(1)).removeEventListener(any(ServiceTaskStartedListener.class));
 }

 @Test
 public void testAlloc() {
 flowTaskInstance.alloc();
 verify(eventPublisher, times(1)).addEventListener(any(ServiceTaskStartedListener.class));
 }

 @Test
 public void testComplete() {
 flowTaskInstance.complete(false);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testConfirmExecute() {
 flowTaskInstance.confirmExecute();
 }

 @Test
 public void testAbort() {
 flowTaskInstance.abort();
 }

 @Test
 public void testCancel() throws InterruptedException {
 flowTaskInstance.cancel(true);
 }

 @Test
 public void testIsDone() throws InterruptedException {
 flowTaskInstance.isDone();
 }

 @Test
 public void testGetResult() throws InterruptedException, ExecutionException {
 flowTaskInstance.getResult();
 }

 @Test
 public void testGetResultWithTimeout()
 throws InterruptedException, ExecutionException, TimeoutException {
 flowTaskInstance.getResult(1, TimeUnit.SECONDS);
 }

 @Test
 public void testGetTargetDelegateClass() {
 Class<? extends BaseRuntimeFlowableDelegate<?>> delegateClass =
 flowTaskInstance.getTargetDelegateClass();
 assertNotNull(delegateClass);
 }
}