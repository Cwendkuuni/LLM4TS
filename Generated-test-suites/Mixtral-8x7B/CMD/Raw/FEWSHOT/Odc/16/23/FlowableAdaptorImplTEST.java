import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;

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

 private FlowableAdaptor flowableAdaptor;

 @Before
 public void setUp() {
 flowableAdaptor = new FlowableAdaptorImpl(nodeInstanceRepository, serviceTaskInstanceRepository, eventPublisher,
 nodeRepository, sequenceRepository, flowInstanceRepository, userTaskInstanceRepository, taskService, formService,
 authenticationFacade, gateWayInstanceRepository);
 }

 @After
 public void tearDown() {
 MockitoAnnotations.openMocks(this).close();
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
 StandaloneInMemProcessEngineConfiguration configuration = new StandaloneInMemProcessEngineConfiguration();
 ProcessEngine processEngine = configuration.buildProcessEngine();
 Deployment deployment = processEngine.getRepositoryService().createDeployment().name("test").addClasspathResource("test.bpmn20.xml").deploy();
 ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery()
 .deploymentId(deployment.getId()).singleResult();
 ProcessInstance processInstance = processEngine.getRuntimeService()
 .startProcessInstanceById(processDefinition.getId());
 FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
 flowInstanceEntity.setId(1L);
 flowInstanceEntity.setProcessInstanceId(processInstance.getId());
 when(flowInstanceRepository.findById(1L)).thenReturn(Optional.of(flowInstanceEntity));
 assertEquals(1L, flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstance.getId()).get().longValue());
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
 StandaloneInMemProcessEngineConfiguration configuration = new StandaloneInMemProcessEngineConfiguration();
 ProcessEngine processEngine = configuration.buildProcessEngine();
 Deployment deployment = processEngine.getRepositoryService().createDeployment().name("test").addClasspathResource("test.bpmn20.xml").deploy();
 ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery()
 .deploymentId(deployment.getId()).singleResult();
 FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
 flowInstanceEntity.setId(1L);
 flowInstanceEntity.setProcessDefinitionId(processDefinition.getId());
 when(flowInstanceRepository.findById(1L)).thenReturn(Optional.of(flowInstanceEntity));
 assertEquals(1L, flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinition.getId()).get().longValue());
 }

 @Test
 public void testGetProcessInstanceIdByFlowInstanceId() {
 FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
 flowInstanceEntity.setId(1L);
 flowInstanceEntity.setProcessInstanceId("processInstanceId");
 when(flowInstanceRepository.findById(1L)).thenReturn(Optional.of(flowInstanceEntity));
 assertEquals("processInstanceId", flowableAdaptor.getProcessInstanceIdByFlowInstanceId(1L).get());
 }

 @Test
 public void testGetFlowableElementByType() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.SERVICE_TASK;
 FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity.setInstanceId(instanceId);
 nodeInstanceEntity.setInstanceType(instanceType.getValue());
 nodeInstanceEntity.setFlowableElementType(flowableElementType.getValue());
 nodeInstanceEntity.setActivityId("activityId");
 nodeInstanceEntity.setName("name");
 when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType.getValue(),
 flowableElementType.getValue())).thenReturn(Arrays.asList(nodeInstanceEntity));
 List<FlowableElement> expectedFlowableElements = Arrays.asList(new FlowableElement("activityId", "name",
 flowableElementType.getValue()));
 assertEquals(expectedFlowableElements, flowableAdaptor.getFlowableElementByType(instanceId, instanceType,
 flowableElementType));
 }

 @Test
 public void testGetTaskInstanceByActivityId() {
 String activityId = "activityId";
 Long flowInstanceId = 1L;
 ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
 serviceTaskInstanceEntity.setInstanceType(FlowNodeType.SERVICE_TASK.getValue());
 serviceTaskInstanceEntity.setActivityId(activityId);
 serviceTaskInstanceEntity.setFlowInstanceId(flowInstanceId);
 when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK.getValue(), activityId,
 flowInstanceId)).thenReturn(Optional.of(serviceTaskInstanceEntity));
 BaseFlowNodeInstance baseFlowNodeInstance = mock(BaseFlowNodeInstance.class);
 when(baseFlowNodeInstance.getNodeType()).thenReturn(FlowNodeType.SERVICE_TASK);
 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(serviceTaskInstanceEntity, new OdcRuntimeDelegateMapper(),
 flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskInstanceRepository);
 assertEquals(flowTaskInstance, flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId).get());
 }

 @Test
 public void testGetApprovalInstanceByActivityId() {
 String activityId = "activityId";
 Long flowInstanceId = 1L;
 UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
 userTaskInstanceEntity.setInstanceType(FlowNodeType.APPROVAL_TASK.getValue());
 userTaskInstanceEntity.setActivityId(activityId);
 userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);
 when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK.getValue(), activityId,
 flowInstanceId)).thenReturn(Optional.of(userTaskInstanceEntity));
 BaseFlowNodeInstance baseFlowNodeInstance = mock(BaseFlowNodeInstance.class);
 when(baseFlowNodeInstance.getNodeType()).thenReturn(FlowNodeType.APPROVAL_TASK);
 FlowApprovalInstance flowApprovalInstance = new FlowApprovalInstance(userTaskInstanceEntity, flowableAdaptor,
 taskService, formService, eventPublisher, authenticationFacade, nodeRepository, sequenceRepository,
 userTaskInstanceRepository);
 assertEquals(flowApprovalInstance, flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId).get());
 }

 @Test
 public void testGetApprovalInstanceByName() {
 String name = "name";
 Long flowInstanceId = 1L;
 UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
 userTaskInstanceEntity.setInstanceType(FlowNodeType.APPROVAL_TASK.getValue());
 userTaskInstanceEntity.setName(name);
 userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);
 when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK.getValue(), name,
 flowInstanceId)).thenReturn(Optional.of(userTaskInstanceEntity));
 BaseFlowNodeInstance baseFlowNodeInstance = mock(BaseFlowNodeInstance.class);
 when(baseFlowNodeInstance.getNodeType()).thenReturn(FlowNodeType.APPROVAL_TASK);
 FlowApprovalInstance flowApprovalInstance = new FlowApprovalInstance(userTaskInstanceEntity, flowableAdaptor,
 taskService, formService, eventPublisher, authenticationFacade, nodeRepository, sequenceRepository,
 userTaskInstanceRepository);
 assertEquals(flowApprovalInstance, flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId).get());
 }

 @Test
 public void testGetGatewayInstanceByActivityId() {
 String activityId = "activityId";
 Long flowInstanceId = 1L;
 GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();
 gateWayInstanceEntity.setInstanceType(FlowNodeType.GATEWAY.getValue());
 gateWayInstanceEntity.setActivityId(activityId);
 gateWayInstanceEntity.setFlowInstanceId(flowInstanceId);
 when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY.getValue(), activityId,
 flowInstanceId)).thenReturn(Optional.of(gateWayInstanceEntity));
 BaseFlowNodeInstance baseFlowNodeInstance = mock(BaseFlowNodeInstance.class);
 when(baseFlowNodeInstance.getNodeType()).thenReturn(FlowNodeType.GATEWAY);
 FlowGatewayInstance flowGatewayInstance = new FlowGatewayInstance(gateWayInstanceEntity, flowableAdaptor,
 nodeRepository, sequenceRepository, gateWayInstanceRepository);
 assertEquals(flowGatewayInstance, flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId).get());
 }

 @Test
 public void testSetFlowableElement() {
 BaseFlowNodeInstance baseFlowNodeInstance = mock(BaseFlowNodeInstance.class);
 when(baseFlowNodeInstance.getId()).thenReturn(1L);
 when(baseFlowNodeInstance.getNodeType()).thenReturn(FlowNodeType.APPROVAL_TASK);
 FlowableElement flowableElement = new FlowableElement("activityId", "name",
 FlowableElementType.START_EVENT.getValue());
 flowableAdaptor.setFlowableElement(baseFlowNodeInstance, flowableElement);
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity.setInstanceId(1L);
 nodeInstanceEntity.setInstanceType(FlowNodeType.APPROVAL_TASK.getValue());
 nodeInstanceEntity.setFlowableElementType(FlowableElementType.START_EVENT.getValue());
 nodeInstanceEntity.setActivityId("activityId");
 nodeInstanceEntity.setName("name");
 nodeInstanceEntity.setFlowInstanceId(baseFlowNodeInstance.getFlowInstanceId());
 verify(nodeInstanceRepository).save(nodeInstanceEntity);
 }
}