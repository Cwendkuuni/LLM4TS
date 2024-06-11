import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.GateWayInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import org.flowable.engine.TaskService;
import org.flowable.engine.form.FormService;
import org.flowable.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class FlowableAdaptorImplTest {

private FlowableAdaptor flowableAdaptor;
private TaskService taskService;
private FormService formService;
private StandaloneInMemProcessEngineConfiguration processEngineConfiguration;
private ProcessEngine processEngine;
private ProcessDefinition processDefinition;
private Deployment deployment;
private ProcessInstance processInstance;
private FlowableElement flowableElement;
private NodeInstanceEntity nodeInstanceEntity;
private ServiceTaskInstanceEntity serviceTaskInstanceEntity;
private UserTaskInstanceEntity userTaskInstanceEntity;
private GateWayInstanceEntity gateWayInstanceEntity;
private Long flowInstanceId;
private String processInstanceId;
private String processDefinitionId;

@Before
public void setup() {
processEngineConfiguration = new StandaloneInMemProcessEngineConfiguration();
processEngineConfiguration.setDatabaseSchemaUpdate(StandaloneInMemProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
processEngine = processEngineConfiguration.buildProcessEngine();
taskService = processEngine.getTaskService();
formService = processEngine.getFormService();
FlowableAdaptorImpl flowableAdaptorImpl = new FlowableAdaptorImpl();
flowableAdaptorImpl.setTaskService(taskService);
flowableAdaptorImpl.setFormService(formService);
flowableAdaptorImpl.setNodeInstanceRepository(mock(NodeInstanceEntityRepository.class));
flowableAdaptorImpl.setServiceTaskInstanceRepository(mock(ServiceTaskInstanceRepository.class));
flowableAdaptorImpl.setEventPublisher(mock(EventPublisher.class));
flowableAdaptorImpl.setNodeRepository(mock(NodeInstanceEntityRepository.class));
flowableAdaptorImpl.setSequenceRepository(mock(SequenceInstanceRepository.class));
flowableAdaptorImpl.setFlowInstanceRepository(mock(FlowInstanceRepository.class));
flowableAdaptorImpl.setUserTaskInstanceRepository(mock(UserTaskInstanceRepository.class));
flowableAdaptorImpl.setGateWayInstanceRepository(mock(GateWayInstanceRepository.class));
flowableAdaptor = flowableAdaptorImpl;
// Deploy a process definition
deployment = processEngine.getRepositoryService()
.createDeployment()
.name("test-process")
.addClasspathResource("processes/test-process.bpmn20.xml")
.deploy();
processDefinition = processEngine.getRepositoryService()
.createProcessDefinitionQuery()
.deploymentId(deployment.getId())
.singleResult();
// Start a process instance
processInstance = processEngine.getRuntimeService()
.startProcessInstanceById(processDefinition.getId());
processInstanceId = processInstance.getId();
flowInstanceId = Long.parseLong(processInstanceId.split(":")[1]);
}

@Test
public void testSetProcessInstanceId() {
// Test with valid parameters
flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
Optional<Long> optional = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
assertTrue(optional.isPresent());
assertEquals(flowInstanceId, (long) optional.get());
// Test with empty processInstanceId
Optional<Long> emptyOptional = flowableAdaptor.getFlowInstanceIdByProcessInstanceId("");
assertFalse(emptyOptional.isPresent());
}

@Test
public void testGetFlowInstanceIdByProcessInstanceId() {
// Test with valid parameters
Optional<Long> optional = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
assertTrue(optional.isPresent());
assertEquals(flowInstanceId, (long) optional.get());
// Test with empty processInstanceId
Optional<Long> emptyOptional = flowableAdaptor.getFlowInstanceIdByProcessInstanceId("");
assertFalse(emptyOptional.isPresent());
}

@Test
public void testSetProcessDefinitionId() {
// Test with valid parameters
flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);
Optional<Long> optional = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
assertTrue(optional.isPresent());
assertEquals(flowInstanceId, (long) optional.get());
// Test with empty processDefinitionId
Optional<Long> emptyOptional = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("");
assertFalse(emptyOptional.isPresent());
}

@Test
public void testGetFlowInstanceIdByProcessDefinitionId() {
// Test with valid parameters
Optional<Long> optional = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
assertTrue(optional.isPresent());
assertEquals(flowInstanceId, (long) optional.get());
// Test with empty processDefinitionId
Optional<Long> emptyOptional = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("");
assertFalse(emptyOptional.isPresent());
}

@Test
public void testGetProcessInstanceIdByFlowInstanceId() {
// Test with valid parameters
Optional<String> optional = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
assertTrue(optional.isPresent());
assertEquals(processInstanceId, optional.get());
// Test with empty flowInstanceId
Optional<String> emptyOptional = flowableAdaptor.getProcessInstanceIdByFlowInstanceId((long) -1);
assertFalse(emptyOptional.isPresent());
}

@Test
public void testGetFlowableElementByType() {
List<FlowableElement> flowableElements = flowableAdaptor.getFlowableElementByType(flowInstanceId,
FlowNodeType.SERVICE_TASK, FlowableElementType.ACTIVITY);
assertEquals(1, flowableElements.size());
assertEquals("serviceTask1", flowableElements.get(0).getActivityId());
}

@Test
public void testGetTaskInstanceByActivityId() {
Optional<FlowTaskInstance> optional = flowableAdaptor.getTaskInstanceByActivityId("serviceTask1", flowInstanceId);
assertTrue(optional.isPresent());
FlowTaskInstance flowTaskInstance = optional.get();
assertEquals("serviceTask1", flowTaskInstance.getActivityId());
assertEquals("Service Task 1", flowTaskInstance.getName());
}

@Test
public void testGetApprovalInstanceByActivityId() {
Optional<FlowApprovalInstance> optional = flowableAdaptor.getApprovalInstanceByActivityId("userTask1", flowInstanceId);
assertTrue(optional.isPresent());
FlowApprovalInstance flowApprovalInstance = optional.get();
assertEquals("userTask1", flowApprovalInstance.getActivityId());
assertEquals("User Task 1", flowApprovalInstance.getName());
}

@Test
public void testGetApprovalInstanceByName() {
Optional<FlowApprovalInstance> optional = flowableAdaptor.getApprovalInstanceByName("User Task 1", flowInstanceId);
assertTrue(optional.isPresent());
FlowApprovalInstance flowApprovalInstance = optional.get();
assertEquals("userTask1", flowApprovalInstance.getActivityId());
assertEquals("User Task 1", flowApprovalInstance.getName());
}

@Test
public void testGetGatewayInstanceByActivityId() {
Optional<FlowGatewayInstance> optional =
flowableAdaptor.getGatewayInstanceByActivityId("exclusiveGateway1", flowInstanceId);
assertTrue(optional.isPresent());
FlowGatewayInstance flowGatewayInstance = optional.get();
assertEquals("exclusiveGateway1", flowGatewayInstance.getActivityId());
assertEquals("Exclusive Gateway 1", flowGatewayInstance.getName());
}

@Test
public void testSetFlowableElement() {
flowableAdaptor.setFlowableElement(new BaseFlowNodeInstance() {
}, new FlowableElement("newActivityId", "New Activity", FlowableElementType.ACTIVITY));
verify(flowableAdaptor.getNodeInstanceRepository(), times(1))
.save(any(NodeInstanceEntity.class));
}

@After
public void teardown() {
// Clean up deployed process definition
processEngine.getRepositoryService().deleteDeployment(deployment.getId(), true);
// Close process engine
processEngine.close();
}
}