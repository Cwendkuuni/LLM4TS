import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.GateWayInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.repository.FlowInstanceRepository;
import com.oceanbase.odc.metadb.flow.repository.GateWayInstanceRepository;
import com.oceanbase.odc.metadb.flow.repository.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.repository.ServiceTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.repository.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.specs.FlowInstanceSpecs;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableElement;
import com.oceanbase.odc.service.flow.FlowNodeType;
import com.oceanbase.odc.service.flow.FlowableAdaptor.FlowTaskInstance;
import com.oceanbase.odc.service.flow.FlowableAdaptor.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.FlowableAdaptor.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;

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
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testSetProcessInstanceId() {
 Long flowInstanceId = 1L;
 String processInstanceId = "processInstanceId";

 flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

 verify(flowInstanceRepository).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
 }

 @Test
 public void testGetFlowInstanceIdByProcessInstanceId() {
 String processInstanceId = "processInstanceId";
 Long flowInstanceId = 1L;

 Specification<FlowInstanceEntity> specification =
 Specification.where(FlowInstanceSpecs.processInstanceIdEquals(processInstanceId));

 List<FlowInstanceEntity> entityList = new ArrayList<>();
 entityList.add(new FlowInstanceEntity(flowInstanceId));

 when(flowInstanceRepository.findAll(specification)).thenReturn(entityList);

 Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

 assertTrue(result.isPresent());
 assertEquals(result.get().longValue(), flowInstanceId);
 }

 @Test
 public void testSetProcessDefinitionId() {
 Long flowInstanceId = 1L;
 String processDefinitionId = "processDefinitionId";

 flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

 verify(flowInstanceRepository).updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
 }

 @Test
 public void testGetFlowInstanceIdByProcessDefinitionId() {
 String processDefinitionId = "processDefinitionId";
 Long flowInstanceId = 1L;

 Specification<FlowInstanceEntity> specification =
 Specification.where(FlowInstanceSpecs.processDefinitionIdEquals(processDefinitionId));

 List<FlowInstanceEntity> entityList = new ArrayList<>();
 entityList.add(new FlowInstanceEntity(flowInstanceId));

 when(flowInstanceRepository.findAll(specification)).thenReturn(entityList);

 Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

 assertTrue(result.isPresent());
 assertEquals(result.get().longValue(), flowInstanceId);
 }

 @Test
 public void testGetProcessInstanceIdByFlowInstanceId() {
 Long flowInstanceId = 1L;
 String processInstanceId = "processInstanceId";

 FlowInstanceEntity entity = new FlowInstanceEntity(flowInstanceId);
 entity.setProcessInstanceId(processInstanceId);

 Optional<FlowInstanceEntity> optional = Optional.of(entity);

 when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(optional);

 Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

 assertTrue(result.isPresent());
 assertEquals(result.get(), processInstanceId);
 }

 @Test
 public void testGetFlowableElementByType() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.SERVICE_TASK;
 FlowableElementType flowableElementType = FlowableElementType.USER_TASK;

 List<NodeInstanceEntity> optional = new ArrayList<>();

 when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
 flowableElementType)).thenReturn(optional);

 List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, instanceType,
 flowableElementType);

 assertEquals(result.size(), 0);
 }

 @Test
 public void testGetTaskInstanceByActivityId() {
 String activityId = "activityId";
 Long flowInstanceId = 1L;

 ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
 entity.setActivityId(activityId);
 entity.setInstanceType(FlowNodeType.SERVICE_TASK);
 entity.setFlowInstanceId(flowInstanceId);

 Optional<ServiceTaskInstanceEntity> optional = Optional.of(entity);

 when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
 flowInstanceId)).thenReturn(optional);

 Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

 assertTrue(result.isPresent());
 }

 @Test
 public void testGetApprovalInstanceByActivityId() {
 String activityId = "activityId";
 Long flowInstanceId = 1L;

 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setActivityId(activityId);
 entity.setInstanceType(FlowNodeType.APPROVAL_TASK);
 entity.setFlowInstanceId(flowInstanceId);

 Optional<UserTaskInstanceEntity> optional = Optional.of(entity);

 when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
 flowInstanceId)).thenReturn(optional);

 Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);

 assertTrue(result.isPresent());
 }

 @Test
 public void testGetApprovalInstanceByName() {
 String name = "name";
 Long flowInstanceId = 1L;

 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setInstanceType(FlowNodeType.APPROVAL_TASK);
 entity.setName(name);
 entity.setFlowInstanceId(flowInstanceId);

 Optional<UserTaskInstanceEntity> optional = Optional.of(entity);

 when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
 .thenReturn(optional);

 Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);

 assertTrue(result.isPresent());
 }

 @Test
 public void testGetGatewayInstanceByActivityId() {
 String activityId = "activityId";
 Long flowInstanceId = 1L;

 GateWayInstanceEntity entity = new GateWayInstanceEntity();
 entity.setActivityId(activityId);
 entity.setInstanceType(FlowNodeType.GATEWAY);
 entity.setFlowInstanceId(flowInstanceId);

 Optional<GateWayInstanceEntity> optional = Optional.of(entity);

 when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
 flowInstanceId)).thenReturn(optional);

 Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);

 assertTrue(result.isPresent());
 }

 @Test
 public void testSetFlowableElement() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.SERVICE_TASK;
 FlowableElementType flowableElementType = FlowableElementType.USER_TASK;

 FlowableElement flowableElement = new FlowableElement("activityId", "name", flowableElementType);

 List<NodeInstanceEntity> entities = new ArrayList<>();

 when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
 flowableElementType)).thenReturn(entities);

 flowableAdaptor.setFlowableElement(null, flowableElement);

 verify(nodeInstanceRepository, times(0)).save(any(NodeInstanceEntity.class));
 }

 @Test
 public void testInnerConvert() {
 ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
 entity.setActivityId("activityId");
 entity.setInstanceType(FlowNodeType.SERVICE_TASK);
 entity.setFlowInstanceId(1L);

 Optional<ServiceTaskInstanceEntity> optional = Optional.of(entity);

 when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, "activityId", 1L))
 .thenReturn(optional);

 Optional<FlowTaskInstance> result = flowableAdaptor.innerConvert(optional, this,
 (e, f) -> new FlowTaskInstance(e, new OdcRuntimeDelegateMapper(), f, eventPublisher, taskService, nodeRepository,
 sequenceRepository, serviceTaskInstanceRepository));

 assertTrue(result.isPresent());
 }
}