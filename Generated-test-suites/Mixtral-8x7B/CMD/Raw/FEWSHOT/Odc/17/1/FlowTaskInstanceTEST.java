import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowNodeInstance;
import com.oceanbase.odc.service.flow.FlowNodeInstanceTest;
import com.oceanbase.odc.service.flow.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.executionstrategy.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.executionstrategy.ExecutionStrategyConfigTest;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegateTest;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapperTest;

public class FlowTaskInstanceTest {

 @Mock
 private TaskService taskService;

 @Mock
 private ServiceTaskInstanceRepository serviceTaskRepository;

 @Mock
 private EventPublisher eventPublisher;

 @Mock
 private RuntimeDelegateMapper delegateConvertor;

 private FlowableAdaptor flowableAdaptor;

 private NodeInstanceEntityRepository nodeRepository;

 private SequenceInstanceRepository sequenceRepository;

 private FlowTaskInstance flowTaskInstance;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);

 flowableAdaptor = new FlowableAdaptorImpl();
 nodeRepository = new NodeInstanceEntityRepository();
 sequenceRepository = new SequenceInstanceRepository();

 when(delegateConvertor.map(TaskType.TASK_TYPE_1)).thenReturn(Mockito.mock(
 BaseRuntimeFlowableDelegate.class));

 flowTaskInstance = new FlowTaskInstance(TaskType.TASK_TYPE_1, 1L, 2L,
 ExecutionStrategyConfigTest.getExecutionStrategyConfig(), false, false, delegateConvertor, flowableAdaptor,
 eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
 }

 @Test
 public void testFlowTaskInstance() {
 assertEquals(TaskType.TASK_TYPE_1, flowTaskInstance.getTaskType());
 assertEquals(1L, flowTaskInstance.getOrganizationId());
 assertEquals(2L, flowTaskInstance.getFlowInstanceId());
 assertEquals(FlowNodeType.SERVICE_TASK, flowTaskInstance.getNodeType());
 }

 @Test
 public void testBatchCreate() {
 List<FlowTaskInstance> flowTaskInstances = Arrays.asList(
 new FlowTaskInstance(TaskType.TASK_TYPE_1, 1L, 2L,
 ExecutionStrategyConfigTest.getExecutionStrategyConfig(), false, false, delegateConvertor, flowableAdaptor,
 eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository),
 new FlowTaskInstance(TaskType.TASK_TYPE_2, 1L, 2L,
 ExecutionStrategyConfigTest.getExecutionStrategyConfig(), false, false, delegateConvertor, flowableAdaptor,
 eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository));

 List<ServiceTaskInstanceEntity> entities =
 FlowTaskInstance.batchCreate(flowTaskInstances, serviceTaskRepository);

 assertEquals(2, entities.size());

 for (int i = 0; i < entities.size(); i++) {
 ServiceTaskInstanceEntity entity = entities.get(i);
 FlowTaskInstance flowTaskInstance = flowTaskInstances.get(i);

 assertEquals(flowTaskInstance.getId(), entity.getId());
 assertEquals(flowTaskInstance.getFlowInstanceId(), entity.getFlowInstanceId());
 assertEquals(flowTaskInstance.getOrganizationId(), entity.getOrganizationId());
 assertEquals(flowTaskInstance.getStatus(), entity.getStatus());
 assertEquals(flowTaskInstance.getTargetTaskId(), entity.getTargetTaskId());
 assertEquals(flowTaskInstance.getTaskType(), entity.getTaskType());
 }
 }

 @Test
 public void testCreate() {
 ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();

 when(serviceTaskRepository.save(entity)).thenReturn(entity);

 flowTaskInstance.create();

 verify(serviceTaskRepository).save(entity);
 }

 @Test
 public void testUpdate() {
 ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();

 when(serviceTaskRepository.update(entity)).thenReturn(1);

 flowTaskInstance.update();

 verify(serviceTaskRepository).update(entity);
 }

 @Test
 public void testDelete() {
 flowTaskInstance.doDelete(1L);

 verify(serviceTaskRepository).deleteById(1L);
 }

 @Test
 public void testBindServiceTask() {
 BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);

 flowTaskInstance.bindServiceTask(target);

 assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
 assertEquals(target, flowTaskInstance.getTargetTaskHandle());
 }

 @Test
 public void testIsPresentOnThisMachine() {
 assertFalse(flowTaskInstance.isPresentOnThisMachine());

 flowTaskInstance.setTargetTaskHandle(mock(BaseRuntimeFlowableDelegate.class));

 assertTrue(flowTaskInstance.isPresentOnThisMachine());
 }

 @Test
 public void testDealloc() {
 flowTaskInstance.dealloc();

 verify(eventPublisher).removeEventListener(any(ServiceTaskStartedListener.class));
 }

 @Test
 public void testAlloc() {
 flowTaskInstance.alloc();

 verify(eventPublisher).addEventListener(any(ServiceTaskStartedListener.class));
 }

 @Test
 public void testComplete() throws Exception {
 ArgumentCaptor<Map<String, Object>> variablesCaptor = ArgumentCaptor.forClass(Map.class);

 flowTaskInstance.setStrategyConfig(ExecutionStrategyConfigTest.getExecutionStrategyConfig());

 flowTaskInstance.complete(false);

 verify(taskService).complete(anyString(), variablesCaptor.capture());

 Map<String, Object> variables = variablesCaptor.getValue();

 assertTrue(variables.containsKey(FlowTaskInstance.ABORT_VARIABLE_NAME));
 }

 @Test
 public void testAbort() throws Exception {
 ArgumentCaptor<Map<String, Object>> variablesCaptor = ArgumentCaptor.forClass(Map.class);

 flowTaskInstance.setStrategyConfig(ExecutionStrategyConfigTest.getExecutionStrategyConfig());

 flowTaskInstance.abort();

 verify(taskService).complete(anyString(), variablesCaptor.capture());

 Map<String, Object> variables = variablesCaptor.getValue();

 assertTrue(variables.containsKey(FlowTaskInstance.ABORT_VARIABLE_NAME));
 }

 @Test
 public void testCancel() throws Exception {
 BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);

 flowTaskInstance.setTargetTaskHandle(target);

 flowTaskInstance.cancel(true);

 verify(target).cancel(true);
 }

 @Test
 public void testIsDone() throws Exception {
 BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);

 flowTaskInstance.setTargetTaskHandle(target);

 when(target.isDone()).thenReturn(true);

 assertTrue(flowTaskInstance.isDone());
 }

 @Test
 public void testGetResult() throws Exception {
 BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);

 flowTaskInstance.setTargetTaskHandle(target);

 when(target.get()).thenReturn("result");

 assertEquals("result", flowTaskInstance.getResult());
 }

 @Test
 public void testGetResultWithTimeout() throws Exception {
 BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);

 flowTaskInstance.setTargetTaskHandle(target);

 when(target.get(5, TimeUnit.SECONDS)).thenReturn("result");

 assertEquals("result", flowTaskInstance.getResult(5, TimeUnit.SECONDS));
 }

 @Test
 public void testResourceType() {
 assertEquals(ResourceType.ODC_FLOW_TASK_INSTANCE.name(), flowTaskInstance.resourceType());
 }

 @Test
 public void testGetTargetDelegateClass() {
 assertEquals(BaseRuntimeFlowableDelegate.class,
 flowTaskInstance.getTargetDelegateClass());
 }

}