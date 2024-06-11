import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceSpecs;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
@Slf4j
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

 private Long flowInstanceId;
 private String processInstanceId;
 private String processDefinitionId;

 @Before
 public void setUp() {
 flowInstanceId = 1L;
 processInstanceId = "processInstanceId";
 processDefinitionId = "processDefinitionId";
 }

 @Test
 public void testSetProcessInstanceId() {
 FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
 flowInstanceEntity.setId(flowInstanceId);
 Mockito.when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));
 Mockito.when(flowInstanceRepository.updateProcessInstanceIdById(flowInstanceId, processInstanceId))
 .thenReturn(1);

 flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

 Mockito.verify(flowInstanceRepository, Mockito.times(1))
 .updateProcessInstanceIdById(flowInstanceId, processInstanceId);
 }

 @Test
 public void testGetFlowInstanceIdByProcessInstanceId() {
 Specification<FlowInstanceEntity> specification =
 FlowInstanceSpecs.processInstanceIdEquals(processInstanceId);

 Mockito.when(flowInstanceRepository.findAll(specification))
 .thenReturn(Collections.singletonList(new FlowInstanceEntity(flowInstanceId, processInstanceId)));

 Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

 Assert.assertTrue(result.isPresent());
 Assert.assertEquals(flowInstanceId, result.get());
 }

 @Test
 public void testSetProcessDefinitionId() {
 FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
 flowInstanceEntity.setId(flowInstanceId);
 Mockito.when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));
 Mockito.when(flowInstanceRepository.updateProcessDefinitionIdById(flowInstanceId, processDefinitionId))
 .thenReturn(1);

 flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

 Mockito.verify(flowInstanceRepository, Mockito.times(1))
 .updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
 }

 @Test
 public void testGetFlowInstanceIdByProcessDefinitionId() {
 Specification<FlowInstanceEntity> specification =
 FlowInstanceSpecs.processDefinitionIdEquals(processDefinitionId);

 Mockito.when(flowInstanceRepository.findAll(specification))
 .thenReturn(Collections.singletonList(new FlowInstanceEntity(flowInstanceId, processInstanceId)));

 Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

 Assert.assertTrue(result.isPresent());
 Assert.assertEquals(flowInstanceId, result.get());
 }

 @Test
 public void testGetProcessInstanceIdByFlowInstanceId() {
 FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
 flowInstanceEntity.setId(flowInstanceId);
 flowInstanceEntity.setProcessInstanceId(processInstanceId);

 Mockito.when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));

 Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

 Assert.assertTrue(result.isPresent());
 Assert.assertEquals(processInstanceId, result.get());
 }

 @Test
 public void testGetFlowableElementByType() {
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity.setInstanceType(FlowNodeType.SERVICE_TASK);
 nodeInstanceEntity.setFlowableElementType(FlowableElementType.USER_TASK);
 nodeInstanceEntity.setName("testTask");
 nodeInstanceEntity.setActivityId("testActivityId");

 Mockito.when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
 anyLong(), any(FlowNodeType.class), any(FlowableElementType.class)))
 .thenReturn(Collections.singletonList(nodeInstanceEntity));

 List<FlowableElement> result =
 flowableAdaptor.getFlowableElementByType(1L, FlowNodeType.SERVICE_TASK, FlowableElementType.USER_TASK);

 Assert.assertEquals(1, result.size());
 Assert.assertEquals("testTask", result.get(0).getName());
 Assert.assertEquals("testActivityId", result.get(0).getActivityId());
 }

 @Test
 public void testGetTaskInstanceByActivityId() {
 ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
 serviceTaskInstanceEntity.setActivityId("testActivityId");

 Mockito.when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(any(FlowNodeType.class),
 anyString(), anyLong()))
 .thenReturn(Optional.of(serviceTaskInstanceEntity));

 Optional<FlowTaskInstance> result =
 flowableAdaptor.getTaskInstanceByActivityId("testActivityId", 1L);

 Assert.assertTrue(result.isPresent());
 }

 @Test
 public void testGetApprovalInstanceByActivityId() {
 UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
 userTaskInstanceEntity.setActivityId("testActivityId");

 Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(any(FlowNodeType.class),
 anyString(), anyLong()))
 .thenReturn(Optional.of(userTaskInstanceEntity));

 Optional<FlowApprovalInstance> result =
 flowableAdaptor.getApprovalInstanceByActivityId("testActivityId", 1L);

 Assert.assertTrue(result.isPresent());
 }

 @Test
 public void testGetApprovalInstanceByName() {
 UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
 userTaskInstanceEntity.setName("testTask");

 Mockito.when(userTaskInstanceRepository.findByInstanceTypeAndName(any(FlowNodeType.class),
 anyString(), anyLong()))
 .thenReturn(Optional.of(userTaskInstanceEntity));

 Optional<FlowApprovalInstance> result =
 flowableAdaptor.getApprovalInstanceByName("testTask", 1L);

 Assert.assertTrue(result.isPresent());
 }

 @Test
 public void testGetGatewayInstanceByActivityId() {
 GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();
 gateWayInstanceEntity.setActivityId("testActivityId");

 Mockito.when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(any(FlowNodeType.class),
 anyString(), anyLong()))
 .thenReturn(Optional.of(gateWayInstanceEntity));

 Optional<FlowGatewayInstance> result =
 flowableAdaptor.getGatewayInstanceByActivityId("testActivityId", 1L);

 Assert.assertTrue(result.isPresent());
 }

 @Test
 public void testSetFlowableElement() {
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity.setInstanceType(FlowNodeType.SERVICE_TASK);
 nodeInstanceEntity.setFlowableElementType(FlowableElementType.USER_TASK);
 nodeInstanceEntity.setName("testTask");
 nodeInstanceEntity.setActivityId("testActivityId");
 nodeInstanceEntity.setInstanceId(1L);
 nodeInstanceEntity.setFlowInstanceId(1L);

 Mockito.when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
 anyLong(), any(FlowNodeType.class), any(FlowableElementType.class)))
 .thenReturn(Collections.singletonList(nodeInstanceEntity));

 flowableAdaptor.setFlowableElement(new BaseFlowNodeInstance(), new FlowableElement("testActivityId", "testTask",
 FlowableElementType.USER_TASK));

 Mockito.verify(nodeInstanceRepository, Mockito.times(1)).save(nodeInstanceEntity);
 }
}