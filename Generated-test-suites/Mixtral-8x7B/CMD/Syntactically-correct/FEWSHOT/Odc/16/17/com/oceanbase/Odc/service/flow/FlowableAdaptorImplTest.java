import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.flowable.engine.common.api.delegate.Expression;
import org.flowable.engine.common.api.delegate.variable.VariableScope;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.flowable.engine.impl.task.TaskDefinition;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.task.Task;
import org.flowable.task.api.TaskInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.ResourceType;
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
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.task.mapper.OdcRuntimeDelegateMapper;

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

 @InjectMocks
 private FlowableAdaptorImpl flowableAdaptor;

 private NodeInstanceEntity nodeInstanceEntity;
 private ServiceTaskInstanceEntity serviceTaskInstanceEntity;
 private UserTaskInstanceEntity userTaskInstanceEntity;
 private GateWayInstanceEntity gateWayInstanceEntity;
 private FlowInstanceEntity flowInstanceEntity;
 private ExecutionEntity executionEntity;
 private ProcessDefinitionEntity processDefinitionEntity;
 private TaskDefinition taskDefinition;
 private Task task;
 private TaskInfo taskInfo;
 private Expression expression;
 private VariableScope variableScope;
 private List<NodeInstanceEntity> nodeInstanceList;
 private List<FlowableElement> flowableElementList;
 private FlowableElement flowableElement;
 private FlowTaskInstance flowTaskInstance;
 private FlowApprovalInstance flowApprovalInstance;
 private FlowGatewayInstance flowGatewayInstance;
 private BiFunction<UserTaskInstanceEntity, FlowableAdaptor, FlowApprovalInstance> flowApprovalInstanceConverter;
 private BiFunction<ServiceTaskInstanceEntity, FlowableAdaptor, FlowTaskInstance> flowTaskInstanceConverter;
 private BiFunction<GateWayInstanceEntity, FlowableAdaptor, FlowGatewayInstance> flowGatewayInstanceConverter;

 @Before
 public void setUp() {
 nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity.setId(1L);
 nodeInstanceEntity.setActivityId("ActivityId");
 nodeInstanceEntity.setName("Name");
 nodeInstanceEntity.setFlowableElementType(FlowableElementType.TASK);
 nodeInstanceEntity.setInstanceId(2L);
 nodeInstanceEntity.setInstanceType(FlowNodeType.SERVICE_TASK);
 nodeInstanceEntity.setFlowInstanceId(3L);

 serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
 serviceTaskInstanceEntity.setId(4L);
 serviceTaskInstanceEntity.setInstanceType(FlowNodeType.SERVICE_TASK);
 serviceTaskInstanceEntity.setActivityId("ActivityId");
 serviceTaskInstanceEntity.setFlowInstanceId(3L);

 userTaskInstanceEntity = new UserTaskInstanceEntity();
 userTaskInstanceEntity.setId(5L);
 userTaskInstanceEntity.setInstanceType(FlowNodeType.APPROVAL_TASK);
 userTaskInstanceEntity.setActivityId("ActivityId");
 userTaskInstanceEntity.setFlowInstanceId(3L);

 gateWayInstanceEntity = new GateWayInstanceEntity();
 gateWayInstanceEntity.setId(6L);
 gateWayInstanceEntity.setInstanceType(FlowNodeType.GATEWAY);
 gateWayInstanceEntity.setActivityId("ActivityId");
 gateWayInstanceEntity.setFlowInstanceId(3L);

 flowInstanceEntity = new FlowInstanceEntity();
 flowInstanceEntity.setId(3L);
 flowInstanceEntity.setProcessInstanceId("ProcessInstanceId");
 flowInstanceEntity.setProcessDefinitionId("ProcessDefinitionId");

 executionEntity = new ExecutionEntity();
 executionEntity.setId(7L);
 executionEntity.setProcessDefinition(processDefinitionEntity);
 executionEntity.setActivityId("ActivityId");

 processDefinitionEntity = new ProcessDefinitionEntity();
 processDefinitionEntity.setKey("ProcessDefinitionKey");

 taskDefinition = mock(TaskDefinition.class);

 task = mock(Task.class);

 taskInfo = mock(TaskInfo.class);

 expression = mock(Expression.class);

 variableScope = mock(VariableScope.class);

 nodeInstanceList = new ArrayList<>();
 nodeInstanceList.add(nodeInstanceEntity);

 flowableElementList = new ArrayList<>();
 flowableElement = new FlowableElement("ActivityId", "Name", FlowableElementType.TASK);
 flowableElementList.add(flowableElement);

 flowTaskInstance = mock(FlowTaskInstance.class);

 flowApprovalInstance = mock(FlowApprovalInstance.class);

 flowGatewayInstance = mock(FlowGatewayInstance.class);

 flowApprovalInstanceConverter = (entity, flowService) -> flowApprovalInstance;
 flowTaskInstanceConverter = (entity, flowService) -> flowTaskInstance;
 flowGatewayInstanceConverter = (entity, flowService) -> flowGatewayInstance;
 }

 @Test
 public void testSetProcessInstanceId() {
 flowableAdaptor.setProcessInstanceId(3L, "ProcessInstanceId");
 verify(flowInstanceRepository).updateProcessInstanceIdById(3L, "ProcessInstanceId");
 }

 @Test
 public void testGetFlowInstanceIdByProcessInstanceId() {
 Specification<FlowInstanceEntity> specification = FlowInstanceSpecs.processInstanceIdEquals("ProcessInstanceId");
 when(flowInstanceRepository.findAll(specification)).thenReturn(Collections.singletonList(flowInstanceEntity));
 assertEquals(Optional.of(3L), flowableAdaptor.getFlowInstanceIdByProcessInstanceId("ProcessInstanceId"));
 }

 @Test
 public void testSetProcessDefinitionId() {
 flowableAdaptor.setProcessDefinitionId(3L, "ProcessDefinitionId");
 verify(flowInstanceRepository).updateProcessDefinitionIdById(3L, "ProcessDefinitionId");
 }

 @Test
 public void testGetFlowInstanceIdByProcessDefinitionId() {
 Specification<FlowInstanceEntity> specification = FlowInstanceSpecs.processDefinitionIdEquals("ProcessDefinitionId");
 when(flowInstanceRepository.findAll(specification)).thenReturn(Collections.singletonList(flowInstanceEntity));
 assertEquals(Optional.of(3L), flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("ProcessDefinitionId"));
 }

 @Test
 public void testGetProcessInstanceIdByFlowInstanceId() {
 when(flowInstanceEntity.getProcessInstanceId()).thenReturn("ProcessInstanceId");
 assertEquals(Optional.of("ProcessInstanceId"), flowableAdaptor.getProcessInstanceIdByFlowInstanceId(3L));
 }

 @Test
 public void testGetFlowableElementByType() {
 when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(2L, FlowNodeType.SERVICE_TASK,
 FlowableElementType.TASK)).thenReturn(nodeInstanceList);
 assertEquals(flowableElementList, flowableAdaptor.getFlowableElementByType(2L, FlowNodeType.SERVICE_TASK,
 FlowableElementType.TASK));
 }

 @Test
 public void testGetTaskInstanceByActivityId() {
 when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, "ActivityId", 3L))
 .thenReturn(Optional.of(serviceTaskInstanceEntity));
 assertEquals(Optional.of(flowTaskInstance), flowableAdaptor.getTaskInstanceByActivityId("ActivityId", 3L));
 }

 @Test
 public void testGetApprovalInstanceByActivityId() {
 when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, "ActivityId", 3L))
 .thenReturn(Optional.of(userTaskInstanceEntity));
 assertEquals(Optional.of(flowApprovalInstance),
 flowableAdaptor.getApprovalInstanceByActivityId("ActivityId", 3L));
 }

 @Test
 public void testGetApprovalInstanceByName() {
 when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, "Name", 3L))
 .thenReturn(Optional.of(userTaskInstanceEntity));
 assertEquals(Optional.of(flowApprovalInstance),
 flowableAdaptor.getApprovalInstanceByName("Name", 3L));
 }

 @Test
 public void testGetGatewayInstanceByActivityId() {
 when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, "ActivityId", 3L))
 .thenReturn(Optional.of(gateWayInstanceEntity));
 assertEquals(Optional.of(flowGatewayInstance),
 flowableAdaptor.getGatewayInstanceByActivityId("ActivityId", 3L));
 }

 @Test
 public void testSetFlowableElement() {
 flowableAdaptor.setFlowableElement(mock(BaseFlowNodeInstance.class), flowableElement);
 verify(nodeInstanceRepository).save(nodeInstanceEntity);
 }

 @Test
 public void testInnerConvert() {
 Optional<UserTaskInstanceEntity> userTaskInstanceEntityOptional = Optional.of(userTaskInstanceEntity);
 assertEquals(Optional.of(flowApprovalInstance),
 flowableAdaptor.innerConvert(userTaskInstanceEntityOptional, this, flowApprovalInstanceConverter));
 }
}