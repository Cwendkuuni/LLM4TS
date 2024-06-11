import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.odc.core.shared.Verify;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowNodeStatus;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.TaskInstanceCreatedEvent;

@RunWith(MockitoJUnitRunner.class)
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
 private NodeInstanceEntityRepository nodeRepository;

 @Mock
 private SequenceInstanceRepository sequenceRepository;

 @Mock
 private FlowableAdaptor flowableAdaptor;

 @Mock
 private Verify verify;

 @InjectMocks
 private FlowTaskInstance flowTaskInstance;

 private ServiceTaskInstanceEntity serviceTaskInstanceEntity;

 @Before
 public void setUp() {
 serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
 serviceTaskInstanceEntity.setOrganizationId(1L);
 serviceTaskInstanceEntity.setStatus(FlowNodeStatus.PENDING);
 serviceTaskInstanceEntity.setFlowInstanceId(1L);
 serviceTaskInstanceEntity.setStartEndpoint(true);
 serviceTaskInstanceEntity.setEndEndpoint(false);
 serviceTaskInstanceEntity.setStrategy(ExecutionStrategyConfig.Strategy.MANUAL);
 serviceTaskInstanceEntity.setTaskType(TaskType.USER_TASK);
 serviceTaskInstanceEntity.setTargetTaskId(null);
 serviceTaskInstanceEntity.setWaitExecExpireIntervalSeconds(100);
 serviceTaskInstanceEntity.setExecutionTime(10);
 }

 @Test
 public void testFlowTaskInstanceCreation() {
 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(1L, 1L, FlowNodeStatus.PENDING, 1L, true, false,
 new FlowableAdaptorImpl(),
 new EventPublisherImpl(), taskService, nodeRepository, sequenceRepository,
 serviceTaskInstanceRepository);

 assertNotNull(flowTaskInstance);
 assertNotNull(flowTaskInstance.getId());
 assertNotNull(flowTaskInstance.getCreateTime());
 assertNotNull(flowTaskInstance.getUpdateTime());
 assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
 assertEquals(1L, flowTaskInstance.getOrganizationId());
 assertEquals(1L, flowTaskInstance.getFlowInstanceId());
 assertTrue(flowTaskInstance.isStartEndpoint());
 assertFalse(flowTaskInstance.isEndEndPoint());
 assertEquals(FlowNodeType.SERVICE_TASK, flowTaskInstance.getNodeType());
 }

 @Test
 public void testMapToTaskEntity() {
 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(serviceTaskInstanceEntity, delegateConvertor, flowableAdaptor,
 eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskInstanceRepository);

 ServiceTaskInstanceEntity mappedEntity = flowTaskInstance.mapToTaskEntity();

 assertEquals(serviceTaskInstanceEntity.getOrganizationId(), mappedEntity.getOrganizationId());
 assertEquals(serviceTaskInstanceEntity.getStatus(), mappedEntity.getStatus());
 assertEquals(serviceTaskInstanceEntity.getFlowInstanceId(), mappedEntity.getFlowInstanceId());
 assertEquals(serviceTaskInstanceEntity.isStartEndpoint(), mappedEntity.isStartEndpoint());
 assertEquals(serviceTaskInstanceEntity.isEndEndpoint(), mappedEntity.isEndEndpoint());
 assertEquals(serviceTaskInstanceEntity.getStrategy(), mappedEntity.getStrategy());
 assertEquals(serviceTaskInstanceEntity.getTaskType(), mappedEntity.getTaskType());
 assertEquals(serviceTaskInstanceEntity.getTargetTaskId(), mappedEntity.getTargetTaskId());
 assertEquals(serviceTaskInstanceEntity.getWaitExecExpireIntervalSeconds(), mappedEntity
 .getWaitExecExpireIntervalSeconds());
 assertEquals(serviceTaskInstanceEntity.getExecutionTime(), mappedEntity.getExecutionTime());
 }

 @Test
 public void testCreate() {
 when(verify.notNull(anyString(), anyString())).thenReturn(null);
 when(verify.notNull(any(), anyString())).thenReturn(null);
 when(serviceTaskRepository.save(any())).thenReturn(serviceTaskInstanceEntity);

 flowTaskInstance.create();

 verify(verify, times(1)).notNull(anyString(), anyString());
 verify(verify, times(1)).notNull(any(), anyString());
 verify(serviceTaskRepository, times(1)).save(serviceTaskInstanceEntity);
 }

 @Test
 public void testUpdate() {
 when(verify.notNull(anyString(), anyString())).thenReturn(null);
 when(serviceTaskRepository.update(any())).thenReturn(1);

 flowTaskInstance.update();

 verify(verify, times(1)).notNull(anyString(), anyString());
 verify(serviceTaskRepository, times(1)).update(serviceTaskInstanceEntity);
 }

 @Test
 public void testDealloc() {
 flowTaskInstance.dealloc();

 verify(eventPublisher, times(1)).removeEventListener(any());
 }

 @Test
 public void testAlloc() {
 flowTaskInstance.alloc();

 verify(eventPublisher, times(1)).addEventListener(any());
 }

 @Test
 public void testComplete() {
 List<FlowableElement> elements = Collections.singletonList(new FlowableElement());
 Optional<String> optional = Optional.of("processInstanceId");
 when(flowableAdaptor.getFlowableElementByType(anyLong(), any(), any()))
 .thenReturn(elements);
 when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(anyLong()))
 .thenReturn(optional);

 flowTaskInstance.complete(false);

 verify(flowableAdaptor, times(1))
 .getFlowableElementByType(anyLong(), any(), any());
 verify(flowableAdaptor, times(1))
 .getProcessInstanceIdByFlowInstanceId(anyLong());
 }
}