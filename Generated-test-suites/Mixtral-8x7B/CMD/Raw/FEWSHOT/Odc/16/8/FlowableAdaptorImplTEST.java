package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.flowable.engine.impl.persistence.entity.ProcessInstanceEntity;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.metadb.flow.GateWayInstanceEntity;
import com.oceanbase.odc.metadb.flow.GateWayInstanceRepository;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;

@SkipAuthorize("odc internal usage")
public class FlowableAdaptorImplTest {

 @Mock
 private NodeInstanceEntityRepository nodeInstanceRepository;

 @Mock
 private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

 @Mock
 private EventPublisher eventPublisher;

 @Mock
 private NodeInstanceEntityRepository nodeRepository;

 @Mock
 private SequenceInstanceRepository sequenceRepository;

 @Mock
 private FlowInstanceRepository flowInstanceRepository;

 @Mock
 private UserTaskInstanceRepository userTaskInstanceRepository;

 @Mock
 private TaskService taskService;

 @Mock
 private FormService formService;

 @Mock
 private AuthenticationFacade authenticationFacade;

 @Mock
 private GateWayInstanceRepository gateWayInstanceRepository;

 @Mock
 private OdcRuntimeDelegateMapper odcRuntimeDelegateMapper;

 @Mock
 private PreConditions preConditions;

 @Mock
 private Verify verify;

 @Mock
 private Specification<FlowInstanceEntity> specification;

 @Mock
 private ExecutionEntity executionEntity;

 @Mock
 private ProcessDefinitionEntity processDefinitionEntity;

 @Mock
 private ProcessInstanceEntity processInstanceEntity;

 @Mock
 private Task task;

 @InjectMocks
 private FlowableAdaptorImpl flowableAdaptor;

 @Before
 public void init() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testSetProcessInstanceId() {
 String processInstanceId = "processInstanceId";
 Long flowInstanceId = 1L;

 when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));

 flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

 ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
 ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

 verify(flowInstanceRepository, times(1)).updateProcessInstanceIdById(longArgumentCaptor.capture(),
 stringArgumentCaptor.capture());

 assertEquals(flowInstanceId, longArgumentCaptor.getValue());
 assertEquals(processInstanceId, stringArgumentCaptor.getValue());
 }

 @Test
 public void testSetProcessInstanceId_whenFlowInstanceIdDoesNotExist_thenDoNothing() {
 Long flowInstanceId = 1L;

 when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.empty());

 flowableAdaptor.setProcessInstanceId(flowInstanceId, "processInstanceId");

 verify(flowInstanceRepository, never()).updateProcessInstanceIdById(anyLong(), anyString());
 }

 @Test
 public void testGetFlowInstanceIdByProcessInstanceId_whenProcessInstanceIdExists_thenReturnFlowInstanceId() {
 String processInstanceId = "processInstanceId";
 Long flowInstanceId = 1L;

 when(flowInstanceRepository.findAll(specification)).thenReturn(
 Collections.singletonList(new FlowInstanceEntity(flowInstanceId, processInstanceId)));

 Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

 assertTrue(result.isPresent());
 assertEquals(result.get().longValue(), flowInstanceId);
 }

 @Test
 public void testGetFlowInstanceIdByProcessInstanceId_whenProcessInstanceIdDoesNotExist_thenReturnEmpty() {
 String processInstanceId = "processInstanceId";

 when(flowInstanceRepository.findAll(specification)).thenReturn(Collections.emptyList());

 Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

 assertFalse(result.isPresent());
 }

 @Test
 public void testGetFlowableElementByType_whenInstanceIdAndInstanceTypeAndFlowableElementTypeAreValid_thenReturnListOfFlowableElements() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.APPROVAL_TASK;
 FlowableElementType flowableElementType = FlowableElementType.ACTIVITY;

 List<NodeInstanceEntity> nodeInstanceEntities = new java.util.ArrayList<>();
 nodeInstanceEntities.add(new NodeInstanceEntity(instanceId, instanceType, flowableElementType, "activityId",
 "name"));

 when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
 flowableElementType)).thenReturn(nodeInstanceEntities);

 List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, instanceType,
 flowableElementType);

 assertEquals(nodeInstanceEntities.size(), result.size());
 for (int i = 0; i < nodeInstanceEntities.size(); i++) {
 assertEquals(nodeInstanceEntities.get(i).getActivityId(), result.get(i).getActivityId());
 assertEquals(nodeInstanceEntities.get(i).getName(), result.get(i).getName());
 assertEquals(nodeInstanceEntities.get(i).getFlowableElementType(), result.get(i).getType());
 }
 }

 @Test
 public void testGetFlowableElementByType_whenInstanceIdAndInstanceTypeAndFlowableElementTypeAreInvalid_thenReturnEmptyList() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.APPROVAL_TASK;
 FlowableElementType flowableElementType = FlowableElementType.ACTIVITY;

 when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
 flowableElementType)).thenReturn(Collections.emptyList());

 List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, instanceType,
 flowableElementType);

 assertTrue(result.isEmpty());
 }

 @Test
 public void testGetTaskInstanceByActivityId_whenActivityIdAndFlowInstanceIdAreValid_thenReturnFlowTaskInstance() {
 String activityId = "activityId";
 Long flowInstanceId = 1L;

 ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity(activityId,
 flowInstanceId);

 when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
 flowInstanceId)).thenReturn(Optional.of(serviceTaskInstanceEntity));

 Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

 assertTrue(result.isPresent());
 }

 @Test
 public void testGetTaskInstanceByActivityId_whenActivityIdOrFlowInstanceIdAreInvalid_thenReturnEmpty() {
 String activityId = "activityId";
 Long flowInstanceId = 1L;

 when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
 flowInstanceId)).thenReturn(Optional.empty());

 Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

 assertFalse(result.isPresent());
 }

 @Test
 public void testGetApprovalInstanceByActivityId_whenActivityIdAndFlowInstanceIdAreValid_thenReturnFlowApprovalInstance() {
 String activityId = "activityId";
 Long flowInstanceId = 1L;

 UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity(activityId, flowInstanceId);

 when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
 flowInstanceId)).thenReturn(Optional.of(userTaskInstanceEntity));

 Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);

 assertTrue(result.isPresent());
 }

 @Test
 public void testGetApprovalInstanceByActivityId_whenActivityIdOrFlowInstanceIdAreInvalid_thenReturnEmpty() {
 String activityId = "activityId";
 Long flowInstanceId = 1L;

 when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
 flowInstanceId)).thenReturn(Optional.empty());

 Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);

 assertFalse(result.isPresent());
 }

 @Test
 public void testGetApprovalInstanceByName_whenNameAndFlowInstanceIdAreValid_thenReturnFlowApprovalInstance() {
 String name = "name";
 Long flowInstanceId = 1L;

 UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity(name, flowInstanceId);

 when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
 .thenReturn(Optional.of(userTaskInstanceEntity));

 Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);

 assertTrue(result.isPresent());
 }

 @Test
 public void testGetApprovalInstanceByName_whenNameOrFlowInstanceIdAreInvalid_thenReturnEmpty() {
 String name = "name";
 Long flowInstanceId = 1L;

 when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
 .thenReturn(Optional.empty());

 Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);

 assertFalse(result.isPresent());
 }

 @Test
 public void testGetGatewayInstanceByActivityId_whenActivityIdAndFlowInstanceIdAreValid_thenReturnFlowGatewayInstance() {
 String activityId = "activityId";
 Long flowInstanceId = 1L;

 GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity(activityId, flowInstanceId);

 when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
 flowInstanceId)).thenReturn(Optional.of(gateWayInstanceEntity));

 Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);

 assertTrue(result.isPresent());
 }

 @Test
 public void testGetGatewayInstanceByActivityId_whenActivityIdOrFlowInstanceIdAreInvalid_thenReturnEmpty() {
 String activityId = "activityId";
 Long flowInstanceId = 1L;

 when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
 flowInstanceId)).thenReturn(Optional.empty());

 Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);

 assertFalse(result.isPresent());
 }

 @Test
 public void testSetFlowableElement_whenNodeInstanceAndFlowableElementAreValid_thenSaveNodeInstanceEntity() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.APPROVAL_TASK;
 FlowableElement flowableElement = new FlowableElement("activityId", "name", FlowableElementType.ACTIVITY);

 BaseFlowNodeInstance nodeInstance = mock(BaseFlowNodeInstance.class);
 when(nodeInstance.getId()).thenReturn(instanceId);
 when(nodeInstance.getNodeType()).thenReturn(instanceType);
 when(nodeInstance.getFlowInstanceId()).thenReturn(instanceId);

 doNothing().when(nodeInstanceRepository).save(any(NodeInstanceEntity.class));

 flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);

 verify(nodeInstanceRepository, times(1)).save(any(NodeInstanceEntity.class));
 }

 @Test
 public void testSetFlowableElement_whenNodeInstanceAndFlowableElementAreInvalid_thenDoNothing() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.APPROVAL_TASK;
 FlowableElement flowableElement = new FlowableElement("activityId", "name", FlowableElementType.ACTIVITY);

 BaseFlowNodeInstance nodeInstance = mock(BaseFlowNodeInstance.class);
 when(nodeInstance.getId()).thenReturn(instanceId);
 when(nodeInstance.getNodeType()).thenReturn(instanceType);
 when(nodeInstance.getFlowInstanceId()).thenReturn(instanceId);

 doNothing().when(nodeInstanceRepository).save(any(NodeInstanceEntity.class));

 when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
 FlowableElementType.ACTIVITY)).thenReturn(Collections.singletonList(new NodeInstanceEntity()));

 flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);

 verify(nodeInstanceRepository, never()).save(any(NodeInstanceEntity.class));
 }

 private <T, V> Optional<T> innerConvert(Optional<V> optional, FlowableAdaptor flowableAdaptor,
 BiFunction<V, FlowableAdaptor, T> function) {
 return null;
 }
}