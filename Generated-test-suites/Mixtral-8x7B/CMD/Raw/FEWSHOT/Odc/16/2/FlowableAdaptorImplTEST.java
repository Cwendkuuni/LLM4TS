package com.oceanbase.odc.service.flow;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.persistence.entity.TaskEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.core.shared.PreConditions;
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
import com.oceanbase.odc.service.flow.dto.FlowableElement;
import com.oceanbase.odc.service.flow.dto.FlowNodeType;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;

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
 private PreConditions preConditions;

 @Mock
 private Verify verify;

 @Mock
 private OdcRuntimeDelegateMapper odcRuntimeDelegateMapper;

 @InjectMocks
 private FlowableAdaptorImpl flowableAdaptorImpl;

 @Before
 public void init() {
 MockitoAnnotations.openMocks(this);
 }

 @Test
 public void testSetProcessInstanceId() {
 Long flowInstanceId = 1L;
 String processInstanceId = "processInstanceId";
 when(flowInstanceRepository.updateProcessInstanceIdById(flowInstanceId, processInstanceId))
 .thenReturn(1);

 flowableAdaptorImpl.setProcessInstanceId(flowInstanceId, processInstanceId);

 verify(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
 }

 @Test
 public void testGetFlowInstanceIdByProcessInstanceId_empty() {
 String processInstanceId = "processInstanceId";
 when(flowInstanceRepository.findAll(any(Specification.class)))
 .thenReturn(Collections.emptyList());

 Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(processInstanceId);

 assertTrue(result.isEmpty());
 }

 @Test
 public void testGetFlowInstanceIdByProcessInstanceId_notEmpty() {
 String processInstanceId = "processInstanceId";
 List<FlowInstanceEntity> flowInstanceEntities = new ArrayList<>();
 flowInstanceEntities.add(new FlowInstanceEntity());
 when(flowInstanceRepository.findAll(any(Specification.class)))
 .thenReturn(flowInstanceEntities);

 Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessInstanceId(processInstanceId);

 assertTrue(result.isPresent());
 }

 @Test
 public void testSetProcessDefinitionId() {
 Long flowInstanceId = 1L;
 String processDefinitionId = "processDefinitionId";
 when(flowInstanceRepository.updateProcessDefinitionIdById(flowInstanceId, processDefinitionId))
 .thenReturn(1);

 flowableAdaptorImpl.setProcessDefinitionId(flowInstanceId, processDefinitionId);

 verify(flowInstanceRepository).updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
 }

 @Test
 public void testGetFlowInstanceIdByProcessDefinitionId_empty() {
 String processDefinitionId = "processDefinitionId";
 when(flowInstanceRepository.findAll(any(Specification.class)))
 .thenReturn(Collections.emptyList());

 Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

 assertTrue(result.isEmpty());
 }

 @Test
 public void testGetFlowInstanceIdByProcessDefinitionId_notEmpty() {
 String processDefinitionId = "processDefinitionId";
 List<FlowInstanceEntity> flowInstanceEntities = new ArrayList<>();
 flowInstanceEntities.add(new FlowInstanceEntity());
 when(flowInstanceRepository.findAll(any(Specification.class)))
 .thenReturn(flowInstanceEntities);

 Optional<Long> result = flowableAdaptorImpl.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

 assertTrue(result.isPresent());
 }

 @Test
 public void testGetProcessInstanceIdByFlowInstanceId_empty() {
 Long flowInstanceId = 1L;
 when(flowInstanceRepository.findById(flowInstanceId))
 .thenReturn(Optional.empty());

 Optional<String> result = flowableAdaptorImpl.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

 assertTrue(result.isEmpty());
 }

 @Test
 public void testGetProcessInstanceIdByFlowInstanceId_notEmpty() {
 Long flowInstanceId = 1L;
 FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
 flowInstanceEntity.setProcessInstanceId("processInstanceId");
 when(flowInstanceRepository.findById(flowInstanceId))
 .thenReturn(Optional.of(flowInstanceEntity));

 Optional<String> result = flowableAdaptorImpl.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

 assertTrue(result.isPresent());
 assertEquals("processInstanceId", result.get());
 }

 @Test
 public void testGetFlowableElementByType_empty() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.NODE;
 FlowableElementType flowableElementType = FlowableElementType.GATEWAY;

 List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
 when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
 instanceId, instanceType, flowableElementType))
 .thenReturn(nodeInstanceEntities);

 List<FlowableElement> result = flowableAdaptorImpl.getFlowableElementByType(instanceId, instanceType,
 flowableElementType);

 assertTrue(result.isEmpty());
 }

 @Test
 public void testGetFlowableElementByType_notEmpty() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.NODE;
 FlowableElementType flowableElementType = FlowableElementType.GATEWAY;

 List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
 nodeInstanceEntities.add(new NodeInstanceEntity());
 when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
 instanceId, instanceType, flowableElementType))
 .thenReturn(nodeInstanceEntities);

 List<FlowableElement> result = flowableAdaptorImpl.getFlowableElementByType(instanceId, instanceType,
 flowableElementType);

 assertFalse(result.isEmpty());
 }

 @Test
 public void testGetTaskInstanceByActivityId_empty() {
 String activityId = "activityId";
 Long flowInstanceId = 1L;

 Optional<ServiceTaskInstanceEntity> serviceTaskInstanceEntity = Optional.empty();
 when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
 flowInstanceId))
 .thenReturn(serviceTaskInstanceEntity);

 Optional<FlowTaskInstance> result = flowableAdaptorImpl.getTaskInstanceByActivityId(activityId,
 flowInstanceId);

 assertTrue(result.isEmpty());
 }

 @Test
 public void testGetTaskInstanceByActivityId_notEmpty() {
 String activityId = "activityId";
 Long flowInstanceId = 1L;

 ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
 serviceTaskInstanceEntity.setFlowInstanceId(flowInstanceId);
 serviceTaskInstanceEntity.setActivityId(activityId);

 Optional<ServiceTaskInstanceEntity> serviceTaskInstanceEntityOptional = Optional.of(serviceTaskInstanceEntity);
 when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
 flowInstanceId))
 .thenReturn(serviceTaskInstanceEntityOptional);

 Optional<FlowTaskInstance> result = flowableAdaptorImpl.getTaskInstanceByActivityId(activityId,
 flowInstanceId);

 assertTrue(result.isPresent());
 }

 @Test
 public void testGetApprovalInstanceByActivityId_empty() {
 String activityId = "activityId";
 Long flowInstanceId = 1L;

 Optional<UserTaskInstanceEntity> userTaskInstanceEntity = Optional.empty();
 when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
 flowInstanceId))
 .thenReturn(userTaskInstanceEntity);

 Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByActivityId(activityId,
 flowInstanceId);

 assertTrue(result.isEmpty());
 }

 @Test
 public void testGetApprovalInstanceByActivityId_notEmpty() {
 String activityId = "activityId";
 Long flowInstanceId = 1L;

 UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
 userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);
 userTaskInstanceEntity.setActivityId(activityId);

 Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = Optional.of(userTaskInstanceEntity);
 when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
 flowInstanceId))
 .thenReturn(userTaskInstanceEntityOptional);

 Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByActivityId(activityId,
 flowInstanceId);

 assertTrue(result.isPresent());
 }

 @Test
 public void testGetApprovalInstanceByName_empty() {
 String name = "name";
 Long flowInstanceId = 1L;

 Optional<UserTaskInstanceEntity> userTaskInstanceEntity = Optional.empty();
 when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name,
 flowInstanceId))
 .thenReturn(userTaskInstanceEntity);

 Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByName(name,
 flowInstanceId);

 assertTrue(result.isEmpty());
 }

 @Test
 public void testGetApprovalInstanceByName_notEmpty() {
 String name = "name";
 Long flowInstanceId = 1L;

 UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
 userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);
 userTaskInstanceEntity.setName(name);

 Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = Optional.of(userTaskInstanceEntity);
 when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name,
 flowInstanceId))
 .thenReturn(userTaskInstanceEntityOptional);

 Optional<FlowApprovalInstance> result = flowableAdaptorImpl.getApprovalInstanceByName(name,
 flowInstanceId);

 assertTrue(result.isPresent());
 }

 @Test
 public void testGetGatewayInstanceByActivityId_empty() {
 String activityId = "activityId";
 Long flowInstanceId = 1L;

 Optional<GateWayInstanceEntity> gateWayInstanceEntity = Optional.empty();
 when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
 flowInstanceId))
 .thenReturn(gateWayInstanceEntity);

 Optional<FlowGatewayInstance> result = flowableAdaptorImpl.getGatewayInstanceByActivityId(activityId,
 flowInstanceId);

 assertTrue(result.isEmpty());
 }

 @Test
 public void testGetGatewayInstanceByActivityId_notEmpty() {
 String activityId = "activityId";
 Long flowInstanceId = 1L;

 GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();
 gateWayInstanceEntity.setFlowInstanceId(flowInstanceId);
 gateWayInstanceEntity.setActivityId(activityId);

 Optional<GateWayInstanceEntity> gateWayInstanceEntityOptional = Optional.of(gateWayInstanceEntity);
 when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
 flowInstanceId))
 .thenReturn(gateWayInstanceEntityOptional);

 Optional<FlowGatewayInstance> result = flowableAdaptorImpl.getGatewayInstanceByActivityId(activityId,
 flowInstanceId);

 assertTrue(result.isPresent());
 }

 @Test
 public void testSetFlowableElement() {
 BaseFlowNodeInstance nodeInstance = mock(BaseFlowNodeInstance.class);
 FlowableElement flowableElement = new FlowableElement("activityId", "name", FlowableElementType.GATEWAY);

 List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity.setActivityId("activityId");
 nodeInstanceEntity.setName("name");
 nodeInstanceEntity.setFlowableElementType(FlowableElementType.GATEWAY);
 nodeInstanceEntities.add(nodeInstanceEntity);
 when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
 nodeInstance.getId(), nodeInstance.getNodeType(), flowableElement.getType()))
 .thenReturn(nodeInstanceEntities);

 flowableAdaptorImpl.setFlowableElement(nodeInstance, flowableElement);

 verify(nodeInstanceRepository).save(nodeInstanceEntity);
 }

}