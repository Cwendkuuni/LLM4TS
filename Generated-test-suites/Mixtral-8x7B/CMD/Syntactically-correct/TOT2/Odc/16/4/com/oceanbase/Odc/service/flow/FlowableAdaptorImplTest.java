import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceSpecs;
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
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
 private UserTaskInstanceRepository userTaskInstanceRepository;
 @Mock
 private GateWayInstanceRepository gateWayInstanceRepository;
 @Mock
 private TaskService taskService;
 @Mock
 private FormService formService;
 @Mock
 private AuthenticationFacade authenticationFacade;
 @Mock
 private EventPublisher eventPublisher;

 @InjectMocks
 private FlowableAdaptorImpl flowableAdaptor;

 private NodeInstanceEntity nodeInstanceEntity;
 private ServiceTaskInstanceEntity serviceTaskInstanceEntity;
 private UserTaskInstanceEntity userTaskInstanceEntity;
 private GateWayInstanceEntity gateWayInstanceEntity;
 private ExecutionEntity executionEntity;
 private Specification<FlowInstanceEntity> specification;
 private BaseFlowNodeInstance flowInstance;
 private FlowTaskInstance flowTaskInstance;
 private FlowApprovalInstance flowApprovalInstance;
 private FlowGatewayInstance flowGatewayInstance;

 @Before
 public void setup() {
 nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity.setId(1L);
 nodeInstanceEntity.setActivityId("activityId");
 nodeInstanceEntity.setName("name");
 nodeInstanceEntity.setFlowableElementType(FlowableElementType.SERVICE_TASK);

 serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
 serviceTaskInstanceEntity.setId(2L);
 serviceTaskInstanceEntity.setExecutionId(3L);
 serviceTaskInstanceEntity.setInstanceType(FlowNodeType.SERVICE_TASK);

 userTaskInstanceEntity = new UserTaskInstanceEntity();
 userTaskInstanceEntity.setId(4L);
 userTaskInstanceEntity.setExecutionId(5L);
 userTaskInstanceEntity.setInstanceType(FlowNodeType.APPROVAL_TASK);

 gateWayInstanceEntity = new GateWayInstanceEntity();
 gateWayInstanceEntity.setId(6L);
 gateWayInstanceEntity.setExecutionId(7L);
 gateWayInstanceEntity.setInstanceType(FlowNodeType.GATEWAY);

 executionEntity = new ExecutionEntity();
 executionEntity.setId(8L);

 specification = FlowInstanceSpecs.processInstanceIdEquals("testProcessInstanceId");

 flowInstance = Mockito.mock(BaseFlowNodeInstance.class);

 flowTaskInstance = new FlowTaskInstance(serviceTaskInstanceEntity, new OdcRuntimeDelegateMapper(), flowableAdaptor,
 eventPublisher, taskService, nodeInstanceRepository, sequenceRepository,
 serviceTaskInstanceRepository);

 flowApprovalInstance = new FlowApprovalInstance(userTaskInstanceEntity, flowableAdaptor, taskService, formService,
 eventPublisher, authenticationFacade, nodeInstanceRepository, sequenceRepository,
 userTaskInstanceRepository);

 flowGatewayInstance = new FlowGatewayInstance(gateWayInstanceEntity, flowableAdaptor, nodeInstanceRepository,
 sequenceRepository, gateWayInstanceRepository);
 }

 @Test
 public void testSetProcessInstanceId() {
 flowableAdaptor.setProcessInstanceId(1L, "testProcessInstanceId");

 Mockito.verify(flowInstanceRepository, Mockito.times(1))
 .updateProcessInstanceIdById(1L, "testProcessInstanceId");
 }

 @Test
 public void testGetFlowInstanceIdByProcessInstanceId() {
 when(flowInstanceRepository.findAll(specification)).thenReturn(
 Collections.singletonList(new FlowInstanceEntity()));

 Optional<Long> flowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessInstanceId("testProcessInstanceId");

 assertTrue(flowInstanceId.isPresent());
 assertEquals((Long) 1L, flowInstanceId.get());
 }

 @Test
 public void testSetProcessDefinitionId() {
 flowableAdaptor.setProcessDefinitionId(1L, "testProcessDefinitionId");

 Mockito.verify(flowInstanceRepository, Mockito.times(1))
 .updateProcessDefinitionIdById(1L, "testProcessDefinitionId");
 }

 @Test
 public void testGetFlowInstanceIdByProcessDefinitionId() {
 when(flowInstanceRepository.findAll(specification)).thenReturn(
 Collections.singletonList(new FlowInstanceEntity()));

 Optional<Long> flowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(
 "testProcessDefinitionId");

 assertTrue(flowInstanceId.isPresent());
 assertEquals((Long) 1L, flowInstanceId.get());
 }

 @Test
 public void testGetProcessInstanceIdByFlowInstanceId() {
 when(flowInstanceRepository.findById(1L)).thenReturn(Optional.of(new FlowInstanceEntity()));
 when(flowInstanceRepository.findById(2L)).thenReturn(Optional.empty());

 Optional<String> processInstanceId =
 flowableAdaptor.getProcessInstanceIdByFlowInstanceId(1L);
 Optional<String> emptyProcessInstanceId =
 flowableAdaptor.getProcessInstanceIdByFlowInstanceId(2L);

 assertTrue(processInstanceId.isPresent());
 assertEquals("testProcessInstanceId", processInstanceId.get());
 assertFalse(emptyProcessInstanceId.isPresent());
 }

 @Test
 public void testGetFlowableElementByType() {
 when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
 FlowNodeType.SERVICE_TASK, FlowableElementType.SERVICE_TASK))
 .thenReturn(Collections.singletonList(nodeInstanceEntity));

 List<FlowableElement> flowableElements =
 flowableAdaptor.getFlowableElementByType(1L, FlowNodeType.SERVICE_TASK,
 FlowableElementType.SERVICE_TASK);

 assertEquals(1, flowableElements.size());
 }

 @Test
 public void testGetTaskInstanceByActivityId() {
 when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId("SERVICE_TASK", "activityId", 1L))
 .thenReturn(Optional.of(serviceTaskInstanceEntity));

 Optional<FlowTaskInstance> flowTaskInstanceOptional =
 flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L);

 assertTrue(flowTaskInstanceOptional.isPresent());
 assertEquals(serviceTaskInstanceEntity.getExecutionId(),
 flowTaskInstanceOptional.get().getExecutionId());
 }

 @Test
 public void testGetApprovalInstanceByActivityId() {
 when(userTaskInstanceRepository.findByInstanceTypeAndActivityId("APPROVAL_TASK", "activityId", 1L))
 .thenReturn(Optional.of(userTaskInstanceEntity));

 Optional<FlowApprovalInstance> flowApprovalInstanceOptional =
 flowableAdaptor.getApprovalInstanceByActivityId("activityId", 1L);

 assertTrue(flowApprovalInstanceOptional.isPresent());
 assertEquals(userTaskInstanceEntity.getExecutionId(),
 flowApprovalInstanceOptional.get().getExecutionId());
 }

 @Test
 public void testGetApprovalInstanceByName() {
 when(userTaskInstanceRepository.findByInstanceTypeAndName("APPROVAL_TASK", "name", 1L))
 .thenReturn(Optional.of(userTaskInstanceEntity));

 Optional<FlowApprovalInstance> flowApprovalInstanceOptional =
 flowableAdaptor.getApprovalInstanceByName("name", 1L);

 assertTrue(flowApprovalInstanceOptional.isPresent());
 assertEquals(userTaskInstanceEntity.getExecutionId(),
 flowApprovalInstanceOptional.get().getExecutionId());
 }

 @Test
 public void testGetGatewayInstanceByActivityId() {
 when(gateWayInstanceRepository.findByInstanceTypeAndActivityId("GATEWAY", "activityId", 1L))
 .thenReturn(Optional.of(gateWayInstanceEntity));

 Optional<FlowGatewayInstance> flowGatewayInstanceOptional =
 flowableAdaptor.getGatewayInstanceByActivityId("activityId", 1L);

 assertTrue(flowGatewayInstanceOptional.isPresent());
 assertEquals(gateWayInstanceEntity.getExecutionId(),
 flowGatewayInstanceOptional.get().getExecutionId());
 }

 @Test
 public void testSetFlowableElement() {
 when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
 FlowNodeType.SERVICE_TASK, FlowableElementType.SERVICE_TASK))
 .thenReturn(Collections.emptyList());

 flowableAdaptor.setFlowableElement(flowInstance,
 new FlowableElement("activityId", "name", FlowableElementType.SERVICE_TASK));

 Mockito.verify(nodeInstanceRepository, Mockito.times(1))
 .save(any(NodeInstanceEntity.class));
 }

 private <T> Optional<T> innerConvert(Optional<T> optional, FlowableAdaptor flowableAdaptor,
 BiFunction<T, FlowableAdaptor, T> function) {
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