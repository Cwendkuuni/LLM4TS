import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.flowable.engine.impl.persistence.entity.ProcessInstanceEntity;
import org.flowable.engine.task.Task;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
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

 @InjectMocks
 @SkipAuthorize("odc internal usage")
 private FlowableAdaptorImpl flowableAdaptor;

 @Before
 public void init() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testSetProcessInstanceId() {
 Long flowInstanceId = 1L;
 String processInstanceId = "processInstanceId1";

 flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

 verify(flowInstanceRepository, times(1)).updateProcessInstanceIdById(flowInstanceId, processInstanceId);
 }

 @Test
 public void testGetFlowInstanceIdByProcessInstanceId() {
 String processInstanceId = "processInstanceId1";

 Specification<FlowInstanceEntity> specification =
 Specification.where(FlowInstanceSpecs.processInstanceIdEquals(processInstanceId));

 List<FlowInstanceEntity> entityList = new ArrayList<>();
 entityList.add(new FlowInstanceEntity());

 when(flowInstanceRepository.findAll(specification)).thenReturn(entityList);

 Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

 assertTrue(result.isPresent());
 assertEquals((Long) 1L, result.get());
 }

 @Test
 public void testSetProcessDefinitionId() {
 Long flowInstanceId = 1L;
 String processDefinitionId = "processDefinitionId1";

 flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

 verify(flowInstanceRepository, times(1)).updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
 }

 @Test
 public void testGetFlowInstanceIdByProcessDefinitionId() {
 String processDefinitionId = "processDefinitionId1";

 Specification<FlowInstanceEntity> specification =
 Specification.where(FlowInstanceSpecs.processDefinitionIdEquals(processDefinitionId));

 List<FlowInstanceEntity> entityList = new ArrayList<>();
 entityList.add(new FlowInstanceEntity());

 when(flowInstanceRepository.findAll(specification)).thenReturn(entityList);

 Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

 assertTrue(result.isPresent());
 assertEquals((Long) 1L, result.get());
 }

 @Test
 public void testGetProcessInstanceIdByFlowInstanceId() {
 Long flowInstanceId = 1L;

 Optional<FlowInstanceEntity> optional = Optional.of(new FlowInstanceEntity());
 optional.get().setProcessInstanceId("processInstanceId1");

 when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(optional);

 Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

 assertTrue(result.isPresent());
 assertEquals("processInstanceId1", result.get());
 }

 @Test
 public void testGetFlowableElementByType() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.TASK;
 FlowableElementType flowableElementType = FlowableElementType.SERVICE_TASK;

 List<NodeInstanceEntity> optional = new ArrayList<>();
 optional.add(new NodeInstanceEntity());

 when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
 flowableElementType)).thenReturn(optional);

 List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, instanceType,
 flowableElementType);

 assertEquals(1, result.size());
 }

 @Test
 public void testGetTaskInstanceByActivityId() {
 String activityId = "activityId1";
 Long flowInstanceId = 1L;

 ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
 serviceTaskInstanceEntity.setActivityId(activityId);
 serviceTaskInstanceEntity.setInstanceType(FlowNodeType.SERVICE_TASK);

 Optional<ServiceTaskInstanceEntity> optional = Optional.of(serviceTaskInstanceEntity);

 when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
 flowInstanceId)).thenReturn(optional);

 Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

 assertTrue(result.isPresent());
 }

 @Test
 public void testGetApprovalInstanceByActivityId() {
 String activityId = "activityId1";
 Long flowInstanceId = 1L;

 UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
 userTaskInstanceEntity.setActivityId(activityId);
 userTaskInstanceEntity.setInstanceType(FlowNodeType.APPROVAL_TASK);

 Optional<UserTaskInstanceEntity> optional = Optional.of(userTaskInstanceEntity);

 when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
 flowInstanceId)).thenReturn(optional);

 Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);

 assertTrue(result.isPresent());
 }

 @Test
 public void testGetApprovalInstanceByName() {
 String name = "name1";
 Long flowInstanceId = 1L;

 UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
 userTaskInstanceEntity.setName(name);
 userTaskInstanceEntity.setInstanceType(FlowNodeType.APPROVAL_TASK);

 Optional<UserTaskInstanceEntity> optional = Optional.of(userTaskInstanceEntity);

 when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name, flowInstanceId))
 .thenReturn(optional);

 Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);

 assertTrue(result.isPresent());
 }

 @Test
 public void testGetGatewayInstanceByActivityId() {
 String activityId = "activityId1";
 Long flowInstanceId = 1L;

 GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();
 gateWayInstanceEntity.setActivityId(activityId);
 gateWayInstanceEntity.setInstanceType(FlowNodeType.GATEWAY);

 Optional<GateWayInstanceEntity> optional = Optional.of(gateWayInstanceEntity);

 when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
 flowInstanceId)).thenReturn(optional);

 Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);

 assertTrue(result.isPresent());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testSetFlowableElement() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.TASK;
 FlowableElement flowableElement = new FlowableElement("activityId1", "name1", FlowableElementType.SERVICE_TASK);

 NodeInstanceEntity entity1 = new NodeInstanceEntity();
 entity1.setActivityId("activityId1");
 entity1.setName("name1");
 entity1.setFlowableElementType(FlowableElementType.SERVICE_TASK);
 entity1.setInstanceType(instanceType);
 entity1.setInstanceId(instanceId);
 entity1.setFlowInstanceId(instanceId);

 NodeInstanceEntity entity2 = new NodeInstanceEntity();
 entity2.setActivityId("activityId2");
 entity2.setName("name2");
 entity2.setFlowableElementType(FlowableElementType.USER_TASK);
 entity2.setInstanceType(instanceType);
 entity2.setInstanceId(instanceId);
 entity2.setFlowInstanceId(instanceId);

 List<NodeInstanceEntity> entities = new ArrayList<>();
 entities.add(entity1);
 entities.add(entity2);

 when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
 FlowableElementType.SERVICE_TASK)).thenReturn(entities);

 flowableAdaptor.setFlowableElement(new FlowTaskInstance(null, null, null, null, null, null, null,
 null, null, null), flowableElement);

 verify(nodeInstanceRepository, times(1)).save(entity1);
 verify(nodeInstanceRepository, never()).save(entity2);
 }

 @Test
 public void testInnerConvert() {
 }
}