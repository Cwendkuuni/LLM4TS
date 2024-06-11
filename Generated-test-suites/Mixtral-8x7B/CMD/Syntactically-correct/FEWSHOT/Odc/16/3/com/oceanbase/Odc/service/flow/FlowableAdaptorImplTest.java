import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.Arrays;
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
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.GateWayInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

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

 @InjectMocks
 private FlowableAdaptorImpl flowableAdaptor;

 @Before
 public void init() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testSetProcessInstanceId() {
 FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
 flowInstanceEntity.setId(1L);
 flowInstanceEntity.setProcessInstanceId("oldProcessInstanceId");

 when(flowInstanceRepository.findById(1L)).thenReturn(Optional.of(flowInstanceEntity));

 flowableAdaptor.setProcessInstanceId(1L, "newProcessInstanceId");

 verify(flowInstanceRepository).updateProcessInstanceIdById(1L, "newProcessInstanceId");
 }

 @Test
 public void testGetFlowInstanceIdByProcessInstanceId() {
 when(flowInstanceRepository.findAll(any(Specification.class)))
 .thenReturn(Arrays.asList(new FlowInstanceEntity(1L, "processInstanceId1")));

 Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId("processInstanceId1");

 assertTrue(result.isPresent());
 assertEquals(new Long(1), result.get());
 }

 @Test
 public void testSetProcessDefinitionId() {
 FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
 flowInstanceEntity.setId(1L);
 flowInstanceEntity.setProcessDefinitionId("oldProcessDefinitionId");

 when(flowInstanceRepository.findById(1L)).thenReturn(Optional.of(flowInstanceEntity));

 flowableAdaptor.setProcessDefinitionId(1L, "newProcessDefinitionId");

 verify(flowInstanceRepository).updateProcessDefinitionIdById(1L, "newProcessDefinitionId");
 }

 @Test
 public void testGetFlowInstanceIdByProcessDefinitionId() {
 when(flowInstanceRepository.findAll(any(Specification.class)))
 .thenReturn(Arrays.asList(new FlowInstanceEntity(1L, "processDefinitionId1")));

 Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId1");

 assertTrue(result.isPresent());
 assertEquals(new Long(1), result.get());
 }

 @Test
 public void testGetProcessInstanceIdByFlowInstanceId() {
 FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
 flowInstanceEntity.setId(1L);
 flowInstanceEntity.setProcessInstanceId("processInstanceId1");

 when(flowInstanceRepository.findById(1L)).thenReturn(Optional.of(flowInstanceEntity));

 Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(1L);

 assertTrue(result.isPresent());
 assertEquals("processInstanceId1", result.get());
 }

 @Test
 public void testGetFlowableElementByType() {
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity.setInstanceId(1L);
 nodeInstanceEntity.setInstanceType(FlowNodeType.SERVICE_TASK);
 nodeInstanceEntity.setFlowableElementType(FlowableElementType.SEQUENCE_FLOW);

 when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.SERVICE_TASK,
 FlowableElementType.SEQUENCE_FLOW))
 .thenReturn(Arrays.asList(nodeInstanceEntity));

 List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(1L, FlowNodeType.SERVICE_TASK,
 FlowableElementType.SEQUENCE_FLOW);

 assertEquals(1, result.size());
 }

 @Test
 public void testGetTaskInstanceByActivityId() {
 ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
 serviceTaskInstanceEntity.setId(1L);
 serviceTaskInstanceEntity.setActivityId("activityId1");
 serviceTaskInstanceEntity.setInstanceType(FlowNodeType.SERVICE_TASK);

 when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, "activityId1", 1L))
 .thenReturn(Optional.of(serviceTaskInstanceEntity));

 Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId("activityId1", 1L);

 assertTrue(result.isPresent());
 }

 @Test
 public void testGetApprovalInstanceByActivityId() {
 UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
 userTaskInstanceEntity.setId(1L);
 userTaskInstanceEntity.setActivityId("activityId1");
 userTaskInstanceEntity.setInstanceType(FlowNodeType.APPROVAL_TASK);

 when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, "activityId1", 1L))
 .thenReturn(Optional.of(userTaskInstanceEntity));

 Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId("activityId1", 1L);

 assertTrue(result.isPresent());
 }

 @Test
 public void testGetApprovalInstanceByName() {
 UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
 userTaskInstanceEntity.setId(1L);
 userTaskInstanceEntity.setName("name1");
 userTaskInstanceEntity.setInstanceType(FlowNodeType.APPROVAL_TASK);

 when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, "name1", 1L))
 .thenReturn(Optional.of(userTaskInstanceEntity));

 Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName("name1", 1L);

 assertTrue(result.isPresent());
 }

 @Test
 public void testGetGatewayInstanceByActivityId() {
 GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();
 gateWayInstanceEntity.setId(1L);
 gateWayInstanceEntity.setActivityId("activityId1");
 gateWayInstanceEntity.setInstanceType(FlowNodeType.GATEWAY);

 when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, "activityId1", 1L))
 .thenReturn(Optional.of(gateWayInstanceEntity));

 Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId("activityId1", 1L);

 assertTrue(result.isPresent());
 }

 @Test
 public void testSetFlowableElement() {
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity.setId(1L);
 nodeInstanceEntity.setInstanceId(1L);
 nodeInstanceEntity.setInstanceType(FlowNodeType.APPROVAL_TASK);
 nodeInstanceEntity.setFlowableElementType(FlowableElementType.SEQUENCE_FLOW);

 when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.APPROVAL_TASK,
 FlowableElementType.SEQUENCE_FLOW))
 .thenReturn(Arrays.asList(nodeInstanceEntity));

 flowableAdaptor.setFlowableElement(new FlowApprovalInstance(),
 new FlowableElement("activityId1", "name1", FlowableElementType.SEQUENCE_FLOW));

 verify(nodeInstanceRepository).save(nodeInstanceEntity);
 }

 private <T, V> Optional<T> innerConvert(Optional<V> optional, FlowableAdaptor flowableAdaptor,
 BiFunction<V, FlowableAdaptor, T> function) {
 if (!optional.isPresent()) {
 return Optional.empty();
 }
 T value = function.apply(optional.get(), flowableAdaptor);
 return Optional.of(value);
 }
}