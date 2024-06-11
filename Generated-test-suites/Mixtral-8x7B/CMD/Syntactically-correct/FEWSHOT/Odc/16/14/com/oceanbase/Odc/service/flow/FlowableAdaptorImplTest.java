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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.persistence.entity.TaskEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.metadb.flow.FlowInstanceSpecs;
import com.oceanbase.odc.metadb.flow.GateWayInstanceEntity;
import com.oceanbase.odc.metadb.flow.GateWayInstanceRepository;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowNodeType;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

import lombok.extern.slf4j.Slf4j;

@RunWith(MockitoJUnitRunner.class)
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

 @Captor
 private ArgumentCaptor<String> processInstanceIdCaptor;

 @Captor
 private ArgumentCaptor<Long> flowInstanceIdCaptor;

 @Captor
 private ArgumentCaptor<String> processDefinitionIdCaptor;

 @Captor
 private ArgumentCaptor<Long> instanceIdCaptor;

 @Captor
 private ArgumentCaptor<FlowNodeType> instanceTypeCaptor;

 @Captor
 private ArgumentCaptor<FlowableElementType> flowableElementTypeCaptor;

 @InjectMocks
 @Service
 @SkipAuthorize("odc internal usage")
 private FlowableAdaptorImpl flowableAdaptor;

 @Before
 public void setup() {
 }

 @Test
 public void testSetProcessInstanceId() {
 long flowInstanceId = 1L;
 String processInstanceId = "processInstanceId";

 doNothing().when(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);

 flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

 verify(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
 }

 @Test
 public void testGetFlowInstanceIdByProcessInstanceId() {
 String processInstanceId = "processInstanceId";
 FlowInstanceEntity flowInstanceEntity = mock(FlowInstanceEntity.class);
 when(flowInstanceEntity.getId()).thenReturn(1L);
 when(flowInstanceRepository.findByProcessInstanceId(processInstanceId)).thenReturn(flowInstanceEntity);

 Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

 assertTrue(result.isPresent());
 assertEquals(Long.valueOf(1), result.get());
 }

 @Test
 public void testSetProcessDefinitionId() {
 long flowInstanceId = 1L;
 String processDefinitionId = "processDefinitionId";

 doNothing().when(flowInstanceRepository).updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);

 flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

 verify(flowInstanceRepository).updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
 }

 @Test
 public void testGetFlowInstanceIdByProcessDefinitionId() {
 String processDefinitionId = "processDefinitionId";
 FlowInstanceEntity flowInstanceEntity = mock(FlowInstanceEntity.class);
 when(flowInstanceEntity.getId()).thenReturn(1L);
 when(flowInstanceRepository.findByProcessDefinitionId(processDefinitionId)).thenReturn(flowInstanceEntity);

 Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

 assertTrue(result.isPresent());
 assertEquals(Long.valueOf(1), result.get());
 }

 @Test
 public void testGetProcessInstanceIdByFlowInstanceId() {
 Long flowInstanceId = 1L;
 FlowInstanceEntity flowInstanceEntity = mock(FlowInstanceEntity.class);
 when(flowInstanceEntity.getProcessInstanceId()).thenReturn("processInstanceId");
 when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));

 Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

 assertTrue(result.isPresent());
 assertEquals("processInstanceId", result.get());
 }

 @Test
 public void testGetFlowableElementByTypeEmptyResult() {
 long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.APPROVAL_TASK;
 FlowableElementType flowableElementType = FlowableElementType.USER_TASK;

 List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, instanceType,
 flowableElementType);

 assertTrue(result.isEmpty());
 }

 @Test
 public void testGetTaskInstanceByActivityId() {
 String activityId = "activityId";
 Long flowInstanceId = 1L;
 ServiceTaskInstanceEntity serviceTaskInstanceEntity = mock(ServiceTaskInstanceEntity.class);
 when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
 flowInstanceId)).thenReturn(Optional.of(serviceTaskInstanceEntity));

 OdcRuntimeDelegateMapper odcRuntimeDelegateMapper = mock(OdcRuntimeDelegateMapper.class);
 when(odcRuntimeDelegateMapper.apply(any(), any())).thenReturn(mock(FlowTaskInstance.class));

 Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

 assertTrue(result.isPresent());
 }

 @Test
 public void testGetTaskInstanceByActivityIdEmptyResult() {
 String activityId = "activityId";
 Long flowInstanceId = 1L;

 when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
 flowInstanceId)).thenReturn(Optional.empty());

 Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

 assertFalse(result.isPresent());
 }

 @Test
 public void testGetApprovalInstanceByActivityId() {
 String activityId = "activityId";
 Long flowInstanceId = 1L;
 UserTaskInstanceEntity userTaskInstanceEntity = mock(UserTaskInstanceEntity.class);
 when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
 flowInstanceId)).thenReturn(Optional.of(userTaskInstanceEntity));

 FlowApprovalInstance flowApprovalInstance = mock(FlowApprovalInstance.class);
 when(flowApprovalInstance.apply(any(), any(), any(), any(), any(), any(), any(), any(), any()))
 .thenReturn(userTaskInstanceEntity);

 Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);

 assertTrue(result.isPresent());
 }

 @Test
 public void testGetApprovalInstanceByActivityIdEmptyResult() {
 String activityId = "activityId";
 Long flowInstanceId = 1L;

 when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
 flowInstanceId)).thenReturn(Optional.empty());

 Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);

 assertFalse(result.isPresent());
 }

 @Test
 public void testGetApprovalInstanceByName() {
 String name = "name";
 Long flowInstanceId = 1L;
 UserTaskInstanceEntity userTaskInstanceEntity = mock(UserTaskInstanceEntity.class);
 when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
 .thenReturn(Optional.of(userTaskInstanceEntity));

 FlowApprovalInstance flowApprovalInstance = mock(FlowApprovalInstance.class);
 when(flowApprovalInstance.apply(any(), any(), any(), any(), any(), any(), any(), any(), any()))
 .thenReturn(userTaskInstanceEntity);

 Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);

 assertTrue(result.isPresent());
 }

 @Test
 public void testGetApprovalInstanceByNameEmptyResult() {
 String name = "name";
 Long flowInstanceId = 1L;

 when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
 .thenReturn(Optional.empty());

 Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);

 assertFalse(result.isPresent());
 }

 @Test
 public void testGetGatewayInstanceByActivityId() {
 String activityId = "activityId";
 Long flowInstanceId = 1L;
 GateWayInstanceEntity gateWayInstanceEntity = mock(GateWayInstanceEntity.class);
 when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
 flowInstanceId)).thenReturn(Optional.of(gateWayInstanceEntity));

 FlowGatewayInstance flowGatewayInstance = mock(FlowGatewayInstance.class);
 when(flowGatewayInstance.apply(any(), any(), any(), any())).thenReturn(gateWayInstanceEntity);

 Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);

 assertTrue(result.isPresent());
 }

 @Test
 public void testGetGatewayInstanceByActivityIdEmptyResult() {
 String activityId = "activityId";
 Long flowInstanceId = 1L;

 when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
 flowInstanceId)).thenReturn(Optional.empty());

 Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);

 assertFalse(result.isPresent());
 }

 @Test
 public void testSetFlowableElement() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.APPROVAL_TASK;
 FlowableElementType flowableElementType = FlowableElementType.USER_TASK;
 FlowableElement flowableElement = new FlowableElement();

 doNothing().when(nodeInstanceRepository).save(any(NodeInstanceEntity.class));

 flowableAdaptor.setFlowableElement(mock(BaseFlowNodeInstance.class), flowableElement);

 verify(nodeInstanceRepository).save(any(NodeInstanceEntity.class));
 }

 @Test
 public void testInnerConvert() {
 String activityId = "activityId";
 Long flowInstanceId = 1L;
 ServiceTaskInstanceEntity serviceTaskInstanceEntity = mock(ServiceTaskInstanceEntity.class);
 when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
 flowInstanceId)).thenReturn(Optional.of(serviceTaskInstanceEntity));

 OdcRuntimeDelegateMapper odcRuntimeDelegateMapper = mock(OdcRuntimeDelegateMapper.class);
 when(odcRuntimeDelegateMapper.apply(any(), any())).thenReturn(mock(FlowTaskInstance.class));

 Optional<FlowTaskInstance> result = flowableAdaptor.innerConvert(Optional.of(serviceTaskInstanceEntity),
 flowableAdaptor, (entity, flowService) -> new FlowTaskInstance(entity, odcRuntimeDelegateMapper, flowService,
 eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskInstanceRepository));

 assertTrue(result.isPresent());
 }

 @Test
 public void testInnerConvertEmpty() {
 String activityId = "activityId";
 Long flowInstanceId = 1L;

 when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
 flowInstanceId)).thenReturn(Optional.empty());

 Optional<FlowTaskInstance> result = flowableAdaptor.innerConvert(Optional.empty(), flowableAdaptor, (entity, flowService) -> new FlowTaskInstance(entity, odcRuntimeDelegateMapper, flowService, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskInstanceRepository));

 assertFalse(result.isPresent());
 }

 @Test
 public void testPreConditions() {
 PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "Id", 1L,
 () -> true);

 verify(nodeInstanceRepository, never()).findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(), any());
 }

 @Test(expected = IllegalStateException.class)
 public void testPreConditionsIllegalStateException() {
 PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "Id", 1L,
 () -> false);

 verify(nodeInstanceRepository, never()).findByInstanceIdAndInstanceTypeAndFlowableElementType(anyLong(), any(), any());
 }

 @Test
 public void testVerify() {
 Verify.verify(1 == 1, "1 has to be equal to one");
 }
}