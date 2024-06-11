import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.GateWayInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.TaskService;
import org.flowable.engine.form.FormService;
import org.flowable.engine.impl.persistence.entity.TaskEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@Slf4j
public class FlowableAdaptorImplTest {

 @Mock
 private NodeInstanceEntityRepository nodeInstanceRepository;
 @Mock
 private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
 @Mock
 private SequenceInstanceRepository sequenceRepository;
 @Mock
 private EventPublisher eventPublisher;
 @Mock
 private NodeInstanceEntityRepository nodeRepository;
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

 private static final Long FLOW_INSTANCE_ID = 1L;
 private static final String PROCESS_INSTANCE_ID = "processInstanceId";
 private static final String PROCESS_DEFINITION_ID = "processDefinitionId";
 private static final String ACTIVITY_ID = "activityId";
 private static final String NAME = "name";
 private static final FlowNodeType FLOW_NODE_TYPE = FlowNodeType.SERVICE_TASK;
 private static final FlowableElementType FLOWABLE_ELEMENT_TYPE = FlowableElementType.SERVICE_TASK;
 private static final Long INSTANCE_ID = 2L;

 @Before
 public void setUp() {
 ReflectionTestUtils.setField(flowableAdaptor, "flowableAdaptor", flowableAdaptor);
 }

 @Test
 public void testSetProcessInstanceId() {
 flowableAdaptor.setProcessInstanceId(FLOW_INSTANCE_ID, PROCESS_INSTANCE_ID);
 }

 @Test
 public void testGetFlowInstanceIdByProcessInstanceId() {
 Specification<FlowInstanceEntity> specification =
 Mockito.mock(Specification.class);
 List<FlowInstanceEntity> flowInstanceEntities =
 Mockito.mock(List.class);
 when(flowInstanceRepository.findAll(specification))
 .thenReturn(flowInstanceEntities);
 Mockito.when(flowInstanceEntities.isEmpty())
 .thenReturn(true);
 flowableAdaptor.getFlowInstanceIdByProcessInstanceId(PROCESS_INSTANCE_ID);
 Mockito.when(flowInstanceEntities.isEmpty())
 .thenReturn(false);
 Mockito.when(flowInstanceEntities.size())
 .thenReturn(1);
 FlowInstanceEntity flowInstanceEntity =
 Mockito.mock(FlowInstanceEntity.class);
 Mockito.when(flowInstanceEntities.get(0))
 .thenReturn(flowInstanceEntity);
 Mockito.when(flowInstanceEntity.getId())
 .thenReturn(FLOW_INSTANCE_ID);
 flowableAdaptor.getFlowInstanceIdByProcessInstanceId(PROCESS_INSTANCE_ID);
 }

 @Test
 public void testSetProcessDefinitionId() {
 flowableAdaptor.setProcessDefinitionId(FLOW_INSTANCE_ID, PROCESS_DEFINITION_ID);
 }

 @Test
 public void testGetFlowInstanceIdByProcessDefinitionId() {
 Specification<FlowInstanceEntity> specification =
 Mockito.mock(Specification.class);
 List<FlowInstanceEntity> flowInstanceEntities =
 Mockito.mock(List.class);
 when(flowInstanceRepository.findAll(specification))
 .thenReturn(flowInstanceEntities);
 Mockito.when(flowInstanceEntities.isEmpty())
 .thenReturn(true);
 flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(PROCESS_DEFINITION_ID);
 Mockito.when(flowInstanceEntities.isEmpty())
 .thenReturn(false);
 Mockito.when(flowInstanceEntities.size())
 .thenReturn(1);
 FlowInstanceEntity flowInstanceEntity =
 Mockito.mock(FlowInstanceEntity.class);
 Mockito.when(flowInstanceEntities.get(0))
 .thenReturn(flowInstanceEntity);
 Mockito.when(flowInstanceEntity.getId())
 .thenReturn(FLOW_INSTANCE_ID);
 flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(PROCESS_DEFINITION_ID);
 }

 @Test
 public void testGetProcessInstanceIdByFlowInstanceId() {
 Optional<String> processInstanceId =
 flowableAdaptor.getProcessInstanceIdByFlowInstanceId(FLOW_INSTANCE_ID);
 Assert.assertTrue(processInstanceId.isEmpty());
 FlowInstanceEntity flowInstanceEntity =
 Mockito.mock(FlowInstanceEntity.class);
 Mockito.when(flowInstanceEntity.getProcessInstanceId())
 .thenReturn(PROCESS_INSTANCE_ID);
 Mockito.when(flowInstanceRepository.findById(FLOW_INSTANCE_ID))
 .thenReturn(Optional.of(flowInstanceEntity));
 processInstanceId = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(FLOW_INSTANCE_ID);
 Assert.assertEquals(PROCESS_INSTANCE_ID, processInstanceId.get());
 }

 @Test
 public void testGetFlowableElementByType() {
 List<FlowableElement> flowableElements =
 flowableAdaptor.getFlowableElementByType(INSTANCE_ID, FLOW_NODE_TYPE, FLOWABLE_ELEMENT_TYPE);
 Assert.assertEquals(0, flowableElements.size());
 NodeInstanceEntity nodeInstanceEntity =
 Mockito.mock(NodeInstanceEntity.class);
 Mockito.when(nodeInstanceEntity.getActivityId())
 .thenReturn(ACTIVITY_ID);
 Mockito.when(nodeInstanceEntity.getName())
 .thenReturn(NAME);
 Mockito.when(nodeInstanceEntity.getFlowableElementType())
 .thenReturn(FLOWABLE_ELEMENT_TYPE);
 Mockito.when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(INSTANCE_ID,
 FLOW_NODE_TYPE, FLOWABLE_ELEMENT_TYPE))
 .thenReturn(Collections.singletonList(nodeInstanceEntity));
 flowableElements = flowableAdaptor.getFlowableElementByType(INSTANCE_ID, FLOW_NODE_TYPE, FLOWABLE_ELEMENT_TYPE);
 Assert.assertEquals(1, flowableElements.size());
 Assert.assertEquals(ACTIVITY_ID,
 flowableElements.get(0).getActivityId());
 Assert.assertEquals(NAME, flowableElements.get(0).getName());
 Assert.assertEquals(FLOWABLE_ELEMENT_TYPE,
 flowableElements.get(0).getType());
 }

 @Test
 public void testGetTaskInstanceByActivityId() {
 ServiceTaskInstanceEntity serviceTaskInstanceEntity =
 Mockito.mock(ServiceTaskInstanceEntity.class);
 Optional<ServiceTaskInstanceEntity> optional =
 Optional.of(serviceTaskInstanceEntity);
 FlowTaskInstance flowTaskInstance =
 Mockito.mock(FlowTaskInstance.class);
 when(flowableAdaptor.innerConvert(
 optional, flowableAdaptor,
 any()))
 .thenReturn(flowTaskInstance);
 Optional<FlowTaskInstance> flowTaskInstanceOptional =
 flowableAdaptor.getTaskInstanceByActivityId(ACTIVITY_ID, INSTANCE_ID);
 Assert.assertEquals(flowTaskInstance,
 flowTaskInstanceOptional.get());
 }

 @Test
 public void testGetApprovalInstanceByActivityId() {
 UserTaskInstanceEntity userTaskInstanceEntity =
 Mockito.mock(UserTaskInstanceEntity.class);
 Optional<UserTaskInstanceEntity> optional =
 Optional.of(userTaskInstanceEntity);
 FlowApprovalInstance flowApprovalInstance =
 Mockito.mock(FlowApprovalInstance.class);
 when(flowableAdaptor.innerConvert(
 optional, flowableAdaptor,
 any()))
 .thenReturn(flowApprovalInstance);
 Optional<FlowApprovalInstance> flowApprovalInstanceOptional =
 flowableAdaptor.getApprovalInstanceByActivityId(ACTIVITY_ID, INSTANCE_ID);
 Assert.assertEquals(flowApprovalInstance,
 flowApprovalInstanceOptional.get());
 }

 @Test
 public void testGetApprovalInstanceByName() {
 UserTaskInstanceEntity userTaskInstanceEntity =
 Mockito.mock(UserTaskInstanceEntity.class);
 Optional<UserTaskInstanceEntity> optional =
 Optional.of(userTaskInstanceEntity);
 FlowApprovalInstance flowApprovalInstance =
 Mockito.mock(FlowApprovalInstance.class);
 when(flowableAdaptor.innerConvert(
 optional, flowableAdaptor,
 any()))
 .thenReturn(flowApprovalInstance);
 Optional<FlowApprovalInstance> flowApprovalInstanceOptional =
 flowableAdaptor.getApprovalInstanceByName(NAME, INSTANCE_ID);
 Assert.assertEquals(flowApprovalInstance,
 flowApprovalInstanceOptional.get());
 }

 @Test
 public void testGetGatewayInstanceByActivityId() {
 GateWayInstanceEntity gateWayInstanceEntity =
 Mockito.mock(GateWayInstanceEntity.class);
 Optional<GateWayInstanceEntity> optional =
 Optional.of(gateWayInstanceEntity);
 FlowGatewayInstance flowGatewayInstance =
 Mockito.mock(FlowGatewayInstance.class);
 when(flowableAdaptor.innerConvert(
 optional, flowableAdaptor,
 any()))
 .thenReturn(flowGatewayInstance);
 Optional<FlowGatewayInstance> flowGatewayInstanceOptional =
 flowableAdaptor.getGatewayInstanceByActivityId(ACTIVITY_ID, INSTANCE_ID);
 Assert.assertEquals(flowGatewayInstance,
 flowGatewayInstanceOptional.get());
 }

 @Test
 public void testSetFlowableElement() {
 BaseFlowNodeInstance baseFlowNodeInstance =
 Mockito.mock(BaseFlowNodeInstance.class);
 FlowableElement flowableElement =
 Mockito.mock(FlowableElement.class);
 flowableAdaptor.setFlowableElement(baseFlowNodeInstance, flowableElement);
 }

 private <T, V> Optional<T> innerConvert(
 Optional<V> optional,
 FlowableAdaptor flowService,
 BiFunction<V, FlowableAdaptor, T> function) {
 if (!optional.isPresent()) {
 return Optional.empty();
 }
 T value = function.apply(optional.get(), flowService);
 if (value == null) {
 return Optional.empty();
 }
 return Optional.of(value);
 }
}