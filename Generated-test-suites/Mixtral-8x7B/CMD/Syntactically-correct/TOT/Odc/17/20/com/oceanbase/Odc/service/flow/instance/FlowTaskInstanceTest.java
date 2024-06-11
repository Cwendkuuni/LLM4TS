import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedEvent;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.model.TaskType;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@Slf4j
public class FlowTaskInstanceTest {

 private NodeInstanceEntityRepository nodeRepository;
 private SequenceInstanceRepository sequenceRepository;
 private ServiceTaskInstanceRepository serviceTaskRepository;
 private EventPublisher eventPublisher;
 private TaskService taskService;
 private FlowableAdaptor flowableAdaptor;
 private FlowTaskInstance flowTaskInstance;

 @BeforeEach
 public void setup() {
 nodeRepository = mock(NodeInstanceEntityRepository.class);
 sequenceRepository = mock(SequenceInstanceRepository.class);
 serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);
 eventPublisher = mock(EventPublisher.class);
 taskService = mock(TaskService.class);
 flowableAdaptor = mock(FlowableAdaptor.class);

 ExecutionStrategyConfig strategyConfig = ExecutionStrategyConfig.builder()
 .strategy(FlowTaskExecutionStrategy.MANUAL)
 .pendingExpireIntervalSeconds(10)
 .executionTime(1)
 .build();

 flowTaskInstance = new FlowTaskInstance(TaskType.FLOW_NODE_TASK, 1L, 1L, strategyConfig, true, true,
 mock(RuntimeDelegateMapper.class), flowableAdaptor, eventPublisher, taskService, nodeRepository,
 sequenceRepository, serviceTaskRepository);
 }

 @AfterEach
 public void tearDown() {
 flowTaskInstance = null;
 }

 @Test
 public void testCreate() {
 flowTaskInstance.create();

 verify(serviceTaskRepository, times(1)).save(ArgumentMatchers.any());
 }

 @Test
 public void testUpdate() {
 flowTaskInstance.setStatus(FlowNodeStatus.EXECUTING);

 flowTaskInstance.update();

 verify(serviceTaskRepository, times(1)).update(ArgumentMatchers.any());
 }

 @Test
 public void testDelete() {
 Long instanceId = 1L;

 doNothing().when(serviceTaskRepository).deleteById(instanceId);

 flowTaskInstance.delete(instanceId);

 verify(serviceTaskRepository, times(1)).deleteById(instanceId);
 }

 @Test
 public void testBindServiceTask() {
 BaseRuntimeFlowableDelegate<?> targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
 flowTaskInstance.bindServiceTask(targetTaskHandle);

 flowTaskInstance.setStatus(FlowNodeStatus.EXECUTING);

 verify(serviceTaskRepository, times(1)).update(ArgumentMatchers.any());
 }

 @Test
 public void testDealloc() {
 flowTaskInstance.dealloc();

 verify(eventPublisher, times(1)).removeEventListener(ArgumentMatchers.any());
 }

 @Test
 public void testAlloc() {
 flowTaskInstance.alloc();

 verify(eventPublisher, times(1)).addEventListener(ArgumentMatchers.any());
 }

 @Test
 public void testConfirmExecute() {
 flowTaskInstance.confirmExecute();

 verify(serviceTaskRepository, times(1)).update(ArgumentMatchers.any());
 }

 @Test
 public void testAbort() {
 flowTaskInstance.abort();

 verify(serviceTaskRepository, times(1)).update(ArgumentMatchers.any());
 }

 @Test
 public void testCancel() throws InterruptedException {
 flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));

 doNothing().when(flowTaskInstance.targetTaskHandle).cancel(true);

 flowTaskInstance.cancel(true);

 verify(flowTaskInstance.targetTaskHandle, times(1)).cancel(true);
 }

 @Test
 public void testIsDone() throws InterruptedException, ExecutionException {
 flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));

 when(flowTaskInstance.targetTaskHandle.isDone()).thenReturn(true);

 assertEquals(true, flowTaskInstance.isDone());
 }

 @Test
 public void testGetResult() throws InterruptedException, ExecutionException {
 flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));

 when(flowTaskInstance.targetTaskHandle.get()).thenReturn("test result");

 assertEquals("test result", flowTaskInstance.getResult());
 }

 @Test
 public void testGetResultTimeout() throws InterruptedException, ExecutionException, TimeoutException {
 flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));

 when(flowTaskInstance.targetTaskHandle.get(1, TimeUnit.SECONDS)).thenReturn("test result");

 assertEquals("test result", flowTaskInstance.getResult(1, TimeUnit.SECONDS));
 }

 @Test
 public void testBatchCreate() {
 List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);

 List<ServiceTaskInstanceEntity> entities = Collections.singletonList(mock(ServiceTaskInstanceEntity.class));

 when(serviceTaskRepository.batchCreate(ArgumentMatchers.anyList())).thenReturn(entities);

 List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);

 assertEquals(result.size(), 1);
 assertEquals(result.get(0).getId(), entities.get(0).getId());
 }

 @Test
 public void testComplete() {
 flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));

 doNothing().when(flowableAdaptor).getProcessInstanceIdByFlowInstanceId(ArgumentMatchers.anyLong());

 List<FlowableElement> elements = Collections.singletonList(mock(FlowableElement.class));

 when(flowableAdaptor.getFlowableElementByType(ArgumentMatchers.anyLong(), ArgumentMatchers.any(),
 ArgumentMatchers.any()))
 .thenReturn(elements);

 when(taskService.createTaskQuery().processInstanceId(ArgumentMatchers.anyString()).list())
 .thenReturn(Collections.singletonList(mock(Task.class)));

 flowTaskInstance.complete(true);

 verify(taskService, times(1)).complete(ArgumentMatchers.anyString(), ArgumentMatchers.any());
 }

 @Test
 public void testMapToTaskEntity() {
 ExecutionStrategyConfig strategyConfig = ExecutionStrategyConfig.builder()
 .strategy(FlowTaskExecutionStrategy.MANUAL)
 .pendingExpireIntervalSeconds(10)
 .executionTime(1)
 .build();

 ServiceTaskInstanceEntity entity = FlowTaskInstance.mapToTaskEntity(flowTaskInstance);

 assertEquals(flowTaskInstance.getOrganizationId(), entity.getOrganizationId());
 assertEquals(flowTaskInstance.getStatus(), entity.getStatus());
 assertEquals(flowTaskInstance.getFlowInstanceId(), entity.getFlowInstanceId());
 assertEquals(flowTaskInstance.isStartEndpoint(), entity.isStartEndpoint());
 assertEquals(flowTaskInstance.isEndEndpoint(), entity.isEndEndpoint());
 assertEquals(strategyConfig.getStrategy(), entity.getStrategy());
 assertEquals(flowTaskInstance.getTaskType(), entity.getTaskType());
 assertEquals(flowTaskInstance.getTargetTaskId(), entity.getTargetTaskId());
 assertEquals(strategyConfig.getPendingExpireIntervalSeconds(), entity.getWaitExecExpireIntervalSeconds());
 assertEquals(strategyConfig.getExecutionTime(), entity.getExecutionTime());
 }
}