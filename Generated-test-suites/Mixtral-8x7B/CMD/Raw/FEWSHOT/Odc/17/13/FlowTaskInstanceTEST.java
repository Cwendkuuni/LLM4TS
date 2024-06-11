import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedEvent;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.model.FlowableElement;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

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

 private FlowTaskInstance flowTaskInstance;

 @Before
 public void setup() {
 ExecutionStrategyConfig strategyConfig = ExecutionStrategyConfig.builder()
 .strategy(FlowTaskExecutionStrategy.MANUAL)
 .pendingExpireIntervalSeconds(300)
 .executionTime(10)
 .build();

 flowTaskInstance = new FlowTaskInstance(TaskType.SERIAL, 1L, 1L, strategyConfig, false, false,
 delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
 serviceTaskRepository);
 }

 @Test
 public void testFlowTaskInstance() {
 assertEquals(TaskType.SERIAL, flowTaskInstance.getTaskType());
 assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
 assertEquals(1L, flowTaskInstance.getOrganizationId().longValue());
 assertEquals(1L, flowTaskInstance.getFlowInstanceId().longValue());
 assertNull(flowTaskInstance.getTargetTaskId());
 assertNull(flowTaskInstance.getDelegate());
 assertNull(flowTaskInstance.getTargetTaskHandle());
 }

 @Test
 public void testMapToTaskEntity() {
 ServiceTaskInstanceEntity entity = FlowTaskInstance.mapToTaskEntity(flowTaskInstance);
 assertEquals(1L, entity.getOrganizationId().longValue());
 assertEquals(FlowNodeStatus.PENDING.getValue(), entity.getStatus());
 assertEquals(1L, entity.getFlowInstanceId().longValue());
 assertEquals(false, entity.isStartEndpoint());
 assertEquals(false, entity.isEndEndpoint());
 assertEquals(FlowTaskExecutionStrategy.MANUAL, entity.getStrategy());
 assertEquals(TaskType.SERIAL, entity.getTaskType());
 assertEquals(0L, entity.getTargetTaskId().longValue());
 assertEquals(300, entity.getWaitExecExpireIntervalSeconds());
 assertEquals(10, entity.getExecutionTime());
 }

 @Test
 public void testBatchCreate() throws ExecutionException, InterruptedException {
 List<FlowTaskInstance> instances = new ArrayList<>();
 instances.add(flowTaskInstance);

 when(serviceTaskRepository.batchCreate(anyList())).thenReturn(instances);

 List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
 assertEquals(1, result.size());
 assertEquals(instances, result);
 }

 @Test
 public void testCreate() {
 ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
 entity.setId(1L);
 entity.setCreateTime(1L);
 entity.setUpdateTime(2L);

 when(serviceTaskRepository.save(any())).thenReturn(entity);

 flowTaskInstance.create();

 assertEquals(1L, flowTaskInstance.getId().longValue());
 assertEquals(1L, flowTaskInstance.getCreateTime().longValue());
 assertEquals(2L, flowTaskInstance.getUpdateTime().longValue());
 }

 @Test
 public void testUpdate() {
 ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
 entity.setId(1L);
 entity.setStatus(FlowNodeStatus.EXECUTING.getValue());
 entity.setTargetTaskId(2L);

 when(serviceTaskRepository.update(any())).thenReturn(1);

 flowTaskInstance.setStatus(FlowNodeStatus.EXECUTING);
 flowTaskInstance.setTargetTaskId(2L);
 flowTaskInstance.update();

 verify(serviceTaskRepository).update(entity);
 }

 @Test
 public void testDelete() {
 flowTaskInstance.delete(1L);

 verify(serviceTaskRepository).deleteById(1L);
 }

 @Test
 public void testBindServiceTask() {
 BaseRuntimeFlowableDelegate<?> delegate = mock(BaseRuntimeFlowableDelegate.class);
 when(delegateConvertor.map(TaskType.SERIAL)).thenReturn(BaseRuntimeFlowableDelegate.class);

 flowTaskInstance.bindServiceTask(delegate);

 assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
 assertEquals(delegate, flowTaskInstance.getTargetTaskHandle());
 }

 @Test
 public void testIsPresentOnThisMachine() {
 BaseRuntimeFlowableDelegate<?> delegate = mock(BaseRuntimeFlowableDelegate.class);
 when(delegateConvertor.map(TaskType.SERIAL)).thenReturn(BaseRuntimeFlowableDelegate.class);

 flowTaskInstance.bindServiceTask(delegate);

 assertTrue(flowTaskInstance.isPresentOnThisMachine());
 }

 @Test
 public void testDealloc() {
 ServiceTaskStartedListener listener = new ServiceTaskStartedListener(flowTaskInstance);

 flowTaskInstance.alloc();

 flowTaskInstance.dealloc();

 assertNull(flowTaskInstance.getListener());
 }

 @Test
 public void testComplete() throws ExecutionException, InterruptedException, TimeoutException {
 List<FlowableElement> elements = new ArrayList<>();
 elements.add(FlowableElement.builder().name("test").build());

 when(flowableAdaptor.getFlowableElementByType(anyLong(), any(), any()))
 .thenReturn(elements);

 when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(anyLong()))
 .thenReturn(Optional.of("test"));

 when(taskService.createTaskQuery().processInstanceId(anyString()).list()).thenReturn(Collections.singletonList(mock(Task.class)));

 flowTaskInstance.complete(false);

 verify(taskService).complete(any(), anyMap());
 }
}