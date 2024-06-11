import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowableElementImpl;
import com.oceanbase.odc.service.flow.FlowNodeStatus;
import com.oceanbase.odc.service.flow.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedEvent;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowInstance;
import com.oceanbase.odc.service.flow.model.FlowTaskInstance;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest {

 private FlowableAdaptor flowableAdaptor;
 private NodeInstanceEntityRepository nodeInstanceEntityRepository;
 private SequenceInstanceRepository sequenceInstanceRepository;
 private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
 private TaskService taskService;
 private EventPublisher eventPublisher;
 private RuntimeDelegateMapper delegateConvertor;
 private FlowTaskInstance flowTaskInstance;
 private BaseRuntimeFlowableDelegate<?> delegate;

 @Before
 public void setup() {
 flowableAdaptor = mock(FlowableAdaptor.class);
 nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);
 sequenceInstanceRepository = mock(SequenceInstanceRepository.class);
 serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
 taskService = mock(TaskService.class);
 eventPublisher = mock(EventPublisher.class);
 delegateConvertor = mock(RuntimeDelegateMapper.class);

 flowTaskInstance = new FlowTaskInstance(
 1L,
 2L,
 3L,
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, Duration.ofSeconds(10), 1),
 false,
 false,
 delegateConvertor,
 flowableAdaptor,
 eventPublisher,
 taskService,
 nodeInstanceEntityRepository,
 sequenceInstanceRepository,
 serviceTaskInstanceRepository
 );

 delegate = mock(BaseRuntimeFlowableDelegate.class);
 when(delegateConvertor.map(TaskType.SERVICE_TASK)).thenReturn(delegate.getClass());
 }

 @Test
 public void testGetCoreFlowableElementType() {
 assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
 }

 @Test
 public void testBatchCreate() {
 List<FlowTaskInstance> instances = Arrays.asList(
 flowTaskInstance,
 new FlowTaskInstance(
 1L,
 2L,
 3L,
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, Duration.ofSeconds(5), 1),
 false,
 false,
 delegateConvertor,
 flowableAdaptor,
 eventPublisher,
 taskService,
 nodeInstanceEntityRepository,
 sequenceInstanceRepository,
 serviceTaskInstanceRepository
 )
 );

 List<ServiceTaskInstanceEntity> entities = instances.stream()
 .map(FlowTaskInstance::mapToTaskEntity)
 .collect(Collectors.toList());

 when(serviceTaskInstanceRepository.batchCreate(entities))
 .thenReturn(entities);

 List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskInstanceRepository);

 assertEquals(instances, result);

 verify(serviceTaskInstanceRepository).batchCreate(entities);
 }

 @Test
 public void testCreate() {
 flowTaskInstance.create();

 verify(serviceTaskInstanceRepository).save(flowTaskInstance.mapToTaskEntity(flowTaskInstance));
 }

 @Test
 public void testUpdate() {
 flowTaskInstance.update();

 verify(serviceTaskInstanceRepository).update(flowTaskInstance.mapToTaskEntity(flowTaskInstance));
 }

 @Test
 public void testDelete() {
 Long instanceId = 1L;
 flowTaskInstance.delete(instanceId);

 verify(serviceTaskInstanceRepository).deleteById(instanceId);
 }

 @Test
 public void testBindServiceTask() {
 when(delegate.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());

 flowTaskInstance.bindServiceTask(delegate);

 assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
 assertEquals(delegate, flowTaskInstance.getTargetTaskHandle());
 }

 @Test
 public void testAlloc() {
 flowTaskInstance.alloc();

 verify(eventPublisher).addEventListener(flowTaskInstance.getListener());
 }

 @Test
 public void testDealloc() {
 flowTaskInstance.dealloc();

 verify(eventPublisher).removeEventListener(flowTaskInstance.getListener());
 }

 @Test
 public void testComplete() {
 flowTaskInstance.complete();

 verify(flowableAdaptor).getFlowableElementByType(flowTaskInstance.getId(), FlowNodeType.SERVICE_TASK,
 FlowableElementType.USER_TASK);
 verify(taskService).createTaskQuery().processInstanceId(anyString()).list();
 verify(taskService).complete(anyString(), anyMap());
 }

 @Test
 public void testConfirmExecute() {
 flowTaskInstance.confirmExecute();

 verify(flowableAdaptor).getFlowableElementByType(flowTaskInstance.getId(), FlowNodeType.SERVICE_TASK,
 FlowableElementType.USER_TASK);
 verify(taskService).createTaskQuery().processInstanceId(anyString()).list();
 verify(taskService).complete(anyString(), anyMap());
 }

 @Test
 public void testAbort() {
 flowTaskInstance.abort();

 verify(flowableAdaptor).getFlowableElementByType(flowTaskInstance.getId(), FlowNodeType.SERVICE_TASK,
 FlowableElementType.USER_TASK);
 verify(taskService).createTaskQuery().processInstanceId(anyString()).list();
 verify(taskService).complete(anyString(), anyMap());
 }

 @Test
 public void testGetResult() throws InterruptedException, ExecutionException {
 when(delegate.get()).thenReturn("result");

 Object result = flowTaskInstance.getResult();

 assertEquals("result", result);
 }

 @Test
 public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
 when(delegate.get(1, TimeUnit.SECONDS)).thenThrow(TimeoutException.class);

 try {
 flowTaskInstance.getResult(1, TimeUnit.SECONDS);
 } catch (TimeoutException e) {
 // expected
 }

 verify(delegate).get(1, TimeUnit.SECONDS);
 }

 @Test
 public void testIsDone() throws InterruptedException, ExecutionException {
 when(delegate.isDone()).thenReturn(true);

 boolean done = flowTaskInstance.isDone();

 assertTrue(done);
 }

 @Test
 public void testCancel() throws InterruptedException, ExecutionException {
 when(delegate.cancel(false)).thenReturn(true);

 boolean cancelled = flowTaskInstance.cancel(false);

 assertTrue(cancelled);
 }

 private static class TestFlowableAdaptor extends FlowableAdaptorImpl {

 @Override
 public Optional<String> getProcessInstanceIdByFlowInstanceId(Long flowInstanceId) {
 Optional<String> optional = Optional.of("test-process-instance-id");
 return optional;
 }

 @Override
 public List<FlowableElement> getFlowableElementByType(Long instanceId, FlowNodeType nodeType,
 FlowableElementType elementType) {
 List<FlowableElement> elements = Collections.singletonList(
 new FlowableElementImpl("test-name", FlowNodeType.SERVICE_TASK, FlowableElementType.USER_TASK,
 "test-resource-id", 1L, "test-description", null, null)
 );
 return elements;
 }
 }

}