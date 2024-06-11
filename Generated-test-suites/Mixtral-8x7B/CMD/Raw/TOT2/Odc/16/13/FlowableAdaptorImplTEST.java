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
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
 @Mock
 private OdcRuntimeDelegateMapper odcRuntimeDelegateMapper;

 @InjectMocks
 @Slf4j
 @SkipAuthorize("odc internal usage")
 private FlowableAdaptor flowableAdaptor = new FlowableAdaptorImpl();

 private Long flowInstanceId;
 private String processInstanceId;
 private String processDefinitionId;
 private Long instanceId;
 private String activityId;
 private NodeInstanceEntity nodeInstanceEntity;
 private ServiceTaskInstanceEntity serviceTaskInstanceEntity;
 private UserTaskInstanceEntity userTaskInstanceEntity;
 private GateWayInstanceEntity gateWayInstanceEntity;
 private BaseFlowNodeInstance baseFlowNodeInstance;
 private FlowableElement flowableElement;
 private FlowTaskInstance flowTaskInstance;
 private FlowApprovalInstance flowApprovalInstance;
 private FlowGatewayInstance flowGatewayInstance;

 @Before
 public void setUp() {
 flowInstanceId = 1L;
 processInstanceId = "processInstanceId";
 processDefinitionId = "processDefinitionId";
 instanceId = 2L;
 activityId = "activityId";
 nodeInstanceEntity = mock(NodeInstanceEntity.class);
 when(nodeInstanceEntity.getId()).thenReturn(instanceId);
 when(nodeInstanceEntity.getFlowInstanceId()).thenReturn(flowInstanceId);
 serviceTaskInstanceEntity = mock(ServiceTaskInstanceEntity.class);
 when(serviceTaskInstanceEntity.getId()).thenReturn(instanceId);
 when(serviceTaskInstanceEntity.getActivityId()).thenReturn(activityId);
 userTaskInstanceEntity = mock(UserTaskInstanceEntity.class);
 when(userTaskInstanceEntity.getId()).thenReturn(instanceId);
 when(userTaskInstanceEntity.getActivityId()).thenReturn(activityId);
 when(userTaskInstanceEntity.getName()).thenReturn("name");
 gateWayInstanceEntity = mock(GateWayInstanceEntity.class);
 when(gateWayInstanceEntity.getId()).thenReturn(instanceId);
 when(gateWayInstanceEntity.getActivityId()).thenReturn(activityId);
 baseFlowNodeInstance = mock(BaseFlowNodeInstance.class);
 when(baseFlowNodeInstance.getId()).thenReturn(instanceId);
 when(baseFlowNodeInstance.getNodeType()).thenReturn(FlowNodeType.APPROVAL_TASK);
 when(baseFlowNodeInstance.getFlowInstanceId()).thenReturn(flowInstanceId);
 flowableElement = new FlowableElement(activityId, "name", FlowableElementType.ACTIVITY);
 flowTaskInstance = mock(FlowTaskInstance.class);
 when(flowTaskInstance.getInstanceId()).thenReturn(instanceId);
 flowApprovalInstance = mock(FlowApprovalInstance.class);
 when(flowApprovalInstance.getInstanceId()).thenReturn(instanceId);
 flowGatewayInstance = mock(FlowGatewayInstance.class);
 when(flowGatewayInstance.getInstanceId()).thenReturn(instanceId);
 }

 @Test
 public void testSetProcessInstanceId() {
 Specification<FlowInstanceEntity> specification = mock(Specification.class);
 List<FlowInstanceEntity> entityList = Collections.singletonList(mock(FlowInstanceEntity.class));
 when(flowInstanceRepository.findAll(specification)).thenReturn(entityList);
 doNothing().when(flowInstanceRepository).updateProcessInstanceIdById(anyLong(), anyString());

 flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

 Verify.verify(flowInstanceRepository.updateProcessInstanceIdById(flowInstanceId, processInstanceId));
 }

 @Test
 public void testGetFlowInstanceIdByProcessInstanceId() {
 Specification<FlowInstanceEntity> specification = mock(Specification.class);
 List<FlowInstanceEntity> entityList = Collections.singletonList(mock(FlowInstanceEntity.class));
 when(flowInstanceRepository.findAll(specification)).thenReturn(entityList);

 Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

 assertTrue(result.isPresent());
 assertEquals(flowInstanceId, (long) result.get());
 }

 @Test
 public void testSetProcessDefinitionId() {
 Specification<FlowInstanceEntity> specification = mock(Specification.class);
 List<FlowInstanceEntity> entityList = Collections.singletonList(mock(FlowInstanceEntity.class));
 when(flowInstanceRepository.findAll(specification)).thenReturn(entityList);
 doNothing().when(flowInstanceRepository).updateProcessDefinitionIdById(anyLong(), anyString());

 flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

 Verify.verify(flowInstanceRepository.updateProcessDefinitionIdById(flowInstanceId, processDefinitionId));
 }

 @Test
 public void testGetFlowInstanceIdByProcessDefinitionId() {
 Specification<FlowInstanceEntity> specification = mock(Specification.class);
 List<FlowInstanceEntity> entityList = Collections.singletonList(mock(FlowInstanceEntity.class));
 when(flowInstanceRepository.findAll(specification)).thenReturn(entityList);

 Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

 assertTrue(result.isPresent());
 assertEquals(flowInstanceId, (long) result.get());
 }

 @Test
 public void testGetProcessInstanceIdByFlowInstanceId() {
 Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

 assertFalse(result.isPresent());
 }

 @Test
 public void testGetFlowableElementByType() {
 when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId,
 FlowNodeType.SERVICE_TASK,
 FlowableElementType.ACTIVITY)).thenReturn(Collections.singletonList(nodeInstanceEntity));

 List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId,
 FlowNodeType.SERVICE_TASK,
 FlowableElementType.ACTIVITY);

 assertEquals(1, result.size());
 assertEquals(activityId, result.get(0).getActivityId());
 assertEquals("name", result.get(0).getName());
 assertEquals(FlowableElementType.ACTIVITY, result.get(0).getType());
 }

 @Test
 public void testGetTaskInstanceByActivityId() {
 when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK,
 activityId,
 flowInstanceId)).thenReturn(Optional.of(serviceTaskInstanceEntity));

 Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

 assertTrue(result.isPresent());
 assertNotNull(result.get());
 }

 @Test
 public void testGetApprovalInstanceByActivityId() {
 when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK,
 activityId,
 flowInstanceId)).thenReturn(Optional.of(userTaskInstanceEntity));

 Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId,
 flowInstanceId);

 assertTrue(result.isPresent());
 assertNotNull(result.get());
 }

 @Test
 public void testGetApprovalInstanceByName() {
 when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, "name",
 flowInstanceId)).thenReturn(Optional.of(userTaskInstanceEntity));

 Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName("name", flowInstanceId);

 assertTrue(result.isPresent());
 assertNotNull(result.get());
 }

 @Test
 public void testGetGatewayInstanceByActivityId() {
 when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY,
 activityId,
 flowInstanceId)).thenReturn(Optional.of(gateWayInstanceEntity));

 Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId,
 flowInstanceId);

 assertTrue(result.isPresent());
 assertNotNull(result.get());
 }

 @Test
 public void testSetFlowableElement() {
 when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId,
 FlowNodeType.APPROVAL_TASK,
 FlowableElementType.ACTIVITY)).thenReturn(Collections.emptyList());

 flowableAdaptor.setFlowableElement(baseFlowNodeInstance, flowableElement);

 Verify.verify(nodeInstanceRepository.save(any(NodeInstanceEntity.class)));
 }

 private <T, V> Optional<T> innerConvert(Optional<V> optional, FlowableAdaptor flowableAdaptor,
 BiFunction<V, FlowableAdaptor, T> function) {
 if (!optional.isPresent()) {
 return Optional.empty();
 }
 T value = function.apply(optional.get(), flowableAdaptor);
 if (value == null) {
 return Optional.empty();
 }
 return Optional.of(value);
 }
}