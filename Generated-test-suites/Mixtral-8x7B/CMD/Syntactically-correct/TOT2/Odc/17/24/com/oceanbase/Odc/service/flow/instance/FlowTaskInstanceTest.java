import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowableElementTypeImpl;
import com.oceanbase.odc.service.flow.FlowNodeStatus;
import com.oceanbase.odc.service.flow.TaskInstanceCreatedEvent;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

public class FlowTaskInstanceTest {

 private static final Long ORGANIZATION_ID = 1L;
 private static final Long FLOW_INSTANCE_ID = 2L;
 private static final Long TASK_TYPE = 3L;
 private static final Long TARGET_TASK_ID = 4L;
 private static final Long ID = 5L;
 private static final Long NODE_ID = 6L;
 private static final Long CREATE_TIME = Instant.now().toEpochMilli();
 private static final Long UPDATE_TIME = Instant.now().toEpochMilli();
 private static final FlowNodeStatus STATUS = FlowNodeStatus.PENDING;
 private static final FlowNodeType NODE_TYPE = FlowNodeType.SERVICE_TASK;
 private static final FlowTaskExecutionStrategy STRATEGY = FlowTaskExecutionStrategy.AUTO;
 private static final Integer WAIT_EXEC_EXPIRE_INTERVAL_SECONDS = 10;
 private static final Integer EXECUTION_TIME = 5;
 private static final String PROCESS_INSTANCE_ID = "processInstanceId";

 private TaskService taskService;
 private NodeInstanceEntityRepository nodeRepository;
 private SequenceInstanceRepository sequenceRepository;
 private ServiceTaskInstanceRepository serviceTaskRepository;
 private FlowableAdaptor flowableAdaptor;
 private EventPublisher eventPublisher;
 private RuntimeDelegateMapper delegateConvertor;
 private FlowTaskInstance flowTaskInstance;
 private ServiceTaskInstanceEntity serviceTaskInstanceEntity;
 private ArgumentCaptor<TaskInstanceCreatedEvent> taskInstanceCreatedEventArgumentCaptor;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);

 taskService = mock(TaskService.class);
 nodeRepository = mock(NodeInstanceEntityRepository.class);
 sequenceRepository = mock(SequenceInstanceRepository.class);
 serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);
 flowableAdaptor = new FlowableAdaptorImpl();
 eventPublisher = mock(EventPublisher.class);
 delegateConvertor = mock(RuntimeDelegateMapper.class);

 serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
 serviceTaskInstanceEntity.setOrganizationId(ORGANIZATION_ID);
 serviceTaskInstanceEntity.setStatus(STATUS);
 serviceTaskInstanceEntity.setFlowInstanceId(FLOW_INSTANCE_ID);
 serviceTaskInstanceEntity.setStartEndpoint(true);
 serviceTaskInstanceEntity.setEndEndpoint(true);
 serviceTaskInstanceEntity.setStrategy(STRATEGY);
 serviceTaskInstanceEntity.setTaskType(TASK_TYPE);
 serviceTaskInstanceEntity.setTargetTaskId(TARGET_TASK_ID);
 serviceTaskInstanceEntity.setWaitExecExpireIntervalSeconds(WAIT_EXEC_EXPIRE_INTERVAL_SECONDS);
 serviceTaskInstanceEntity.setExecutionTime(EXECUTION_TIME);

 flowTaskInstance = new FlowTaskInstance(TASK_TYPE, ORGANIZATION_ID, FLOW_INSTANCE_ID,
 new ExecutionStrategyConfig(STRATEGY, WAIT_EXEC_EXPIRE_INTERVAL_SECONDS, EXECUTION_TIME),
 true, true, delegateConvertor, flowableAdaptor, eventPublisher, taskService,
 nodeRepository, sequenceRepository, serviceTaskRepository);

 taskInstanceCreatedEventArgumentCaptor = ArgumentCaptor.forClass(TaskInstanceCreatedEvent.class);
 }

 @Test
 public void testConstructorWithEntity() {
 FlowTaskInstance flowTaskInstance =
 new FlowTaskInstance(new ServiceTaskInstanceEntity(), delegateConvertor, flowableAdaptor, eventPublisher,
 taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

 verify(nodeRepository).addFlowNodeInstance(flowTaskInstance);
 }

 @Test
 public void testConstructorWithTypeAndParameters() {
 FlowTaskInstance flowTaskInstance =
 new FlowTaskInstance(TASK_TYPE, ORGANIZATION_ID, FLOW_INSTANCE_ID,
 new ExecutionStrategyConfig(STRATEGY, WAIT_EXEC_EXPIRE_INTERVAL_SECONDS, EXECUTION_TIME),
 true, true, delegateConvertor, flowableAdaptor, eventPublisher, taskService,
 nodeRepository, sequenceRepository, serviceTaskRepository);

 verify(nodeRepository).addFlowNodeInstance(flowTaskInstance);
 }

 @Test
 public void testGetCoreFlowableElementType() {
 FlowableElementType expected = new FlowableElementTypeImpl(FlowableElementType.SERVICE_TASK);
 FlowableElementType actual = flowTaskInstance.getCoreFlowableElementType();

 assertEquals(expected, actual);
 }

 @Test
 public void testBatchCreate() {
 List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);

 when(serviceTaskRepository.batchCreate(anyList())).thenReturn(Collections.singletonList(serviceTaskInstanceEntity));

 List<FlowTaskInstance> result =
 FlowTaskInstance.batchCreate(instances, serviceTaskRepository);

 assertEquals(instances, result);
 }

 @Test
 public void testCreate() {
 when(serviceTaskRepository.save(any())).thenReturn(serviceTaskInstanceEntity);

 flowTaskInstance.create();

 verify(serviceTaskRepository).save(any());
 }

 @Test
 public void testUpdate() {
 ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
 entity.setId(ID);
 entity.setStatus(STATUS);
 entity.setTargetTaskId(TARGET_TASK_ID);
 entity.setTaskType(TASK_TYPE);

 when(serviceTaskRepository.update(entity)).thenReturn(1);

 flowTaskInstance.update();

 verify(serviceTaskRepository).update(entity);
 }

 @Test
 public void testDelete() {
 doNothing().when(serviceTaskRepository).deleteById(ID);

 flowTaskInstance.delete(ID);

 verify(serviceTaskRepository).deleteById(ID);
 }

 @Test
 public void testBindServiceTask() {
 BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);

 flowTaskInstance.bindServiceTask(target);

 assertEquals(target, flowTaskInstance.getTargetTaskHandle());
 assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
 }

 @Test
 public void testIsPresentOnThisMachine() {
 BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);

 flowTaskInstance.bindServiceTask(target);

 assertTrue(flowTaskInstance.isPresentOnThisMachine());
 }

 @Test
 public void testDealloc() {
 ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);

 flowTaskInstance.listener = listener;

 flowTaskInstance.dealloc();

 verify(eventPublisher).removeEventListener(listener);
 flowTaskInstance.listener = null;
 }

 @Test
 public void testAlloc() {
 ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);

 flowTaskInstance.alloc();

 verify(eventPublisher).addEventListener(listener);
 }

 @Test
 public void testConfirmExecute() {
 flowTaskInstance.confirmExecute();

 verify(flowTaskInstance).complete(false);
 }

 @Test
 public void testAbort() {
 flowTaskInstance.abort();

 verify(flowTaskInstance).complete(true);
 }

 @Test
 public void testCancel() {
 BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);

 flowTaskInstance.bindServiceTask(target);

 doNothing().when(target).cancel(anyBoolean());

 flowTaskInstance.cancel(true);

 verify(target).cancel(true);
 }

 @Test
 public void testIsDone() {
 BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);

 flowTaskInstance.bindServiceTask(target);

 when(target.isDone()).thenReturn(true);

 assertTrue(flowTaskInstance.isDone());
 }

 @Test
 public void testGetResult() throws InterruptedException, ExecutionException {
 BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);

 flowTaskInstance.bindServiceTask(target);

 when(target.get()).thenReturn("result");

 assertEquals("result", flowTaskInstance.getResult());
 }

 @Test
 public void testGetResultWithTimeout()
 throws InterruptedException, ExecutionException, TimeoutException {
 BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);

 flowTaskInstance.bindServiceTask(target);

 when(target.get(1, TimeUnit.SECONDS)).thenReturn("result");

 assertEquals("result", flowTaskInstance.getResult(1, TimeUnit.SECONDS));
 }

 @Test
 public void testResourceType() {
 String expected = ResourceType.ODC_FLOW_TASK_INSTANCE.name();
 String actual = flowTaskInstance.resourceType();

 assertEquals(expected, actual);
 }

 @Test
 public void testGetTargetDelegateClass() {
 Class<? extends BaseRuntimeFlowableDelegate<?>> expected =
 mock(Class.class);

 when(delegateConvertor.map(TASK_TYPE)).thenReturn(expected);

 Class<? extends BaseRuntimeFlowableDelegate<?>> actual =
 flowTaskInstance.getTargetDelegateClass();

 assertEquals(expected, actual);
 }

 @Test
 public void testComplete() {
 List<FlowableElement> elements =
 Collections.singletonList(mock(FlowableElement.class));

 when(flowableAdaptor.getFlowableElementByType(anyLong(), any(), any()))
 .thenReturn(elements);

 Optional<String> optional =
 Optional.of(PROCESS_INSTANCE_ID);

 when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(anyLong()))
 .thenReturn(optional);

 List<Task> tasks = Collections.singletonList(mock(Task.class));

 when(taskService.createTaskQuery().processInstanceId(PROCESS_INSTANCE_ID).list())
 .thenReturn(tasks);

 Map<String, Object> variables = new HashMap<>();
 variables.put(ABORT_VARIABLE_NAME, true);

 flowTaskInstance.complete(true);

 verify(taskService).complete(any(), eq(variables));
 }

 @Test
 public void testMapToTaskEntity() {
 ServiceTaskInstanceEntity expected = serviceTaskInstanceEntity;
 ServiceTaskInstanceEntity actual =
 FlowTaskInstance.mapToTaskEntity(flowTaskInstance);

 assertEquals(expected, actual);
 }

 @Test
 public void testPublishEvent() {
 flowTaskInstance.publishEvent();

 verify(eventPublisher).publishEvent(taskInstanceCreatedEventArgumentCaptor.capture());

 TaskInstanceCreatedEvent event =
 taskInstanceCreatedEventArgumentCaptor.getValue();

 assertEquals(flowTaskInstance, event.getTaskInstance());
 }
}