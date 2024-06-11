import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
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
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import org.flowable.engine.TaskService;
import org.flowable.engine.form.FormService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FlowableAdaptorImplTest {

    @InjectMocks
    private FlowableAdaptorImpl flowableAdaptor;

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

    @Before
    public void setup() {
        // Initialize mocks here if needed
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        when(flowInstanceRepository.updateProcessInstanceIdById(flowInstanceId, processInstanceId))
                .thenReturn(1);

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        // Add assertions here
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "processInstanceId";

        when(flowInstanceRepository.findAll(Specification.where(FlowInstanceSpecs.processInstanceIdEquals(processInstanceId))))
                .thenReturn(Collections.singletonList(new FlowInstanceEntity()));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertTrue(result.isPresent());
        assertEquals(Long.class, result.get().getClass());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "processDefinitionId";

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(new FlowInstanceEntity()));
        when(flowInstanceRepository.updateProcessDefinitionIdById(flowInstanceId, processDefinitionId))
                .thenReturn(1);

        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        // Add assertions here
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "processDefinitionId";

        when(flowInstanceRepository.findAll(Specification.where(FlowInstanceSpecs.processDefinitionIdEquals(processDefinitionId))))
                .thenReturn(Collections.singletonList(new FlowInstanceEntity()));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        assertTrue(result.isPresent());
        assertEquals(Long.class, result.get().getClass());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;

        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setProcessInstanceId("processInstanceId");

        when(flowInstanceRepository.findById(flowInstanceId)).thenReturn(Optional.of(flowInstanceEntity));

        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(String.class, result.get().getClass());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.SERVICE_TASK;
        FlowableElementType flowableElementType = FlowableElementType.SERVICE_TASK;

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
                flowableElementType)).thenReturn(Collections.singletonList(new NodeInstanceEntity()));

        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(instanceId, instanceType,
                flowableElementType);

        assertFalse(result.isEmpty());
        assertEquals(FlowableElement.class, result.get(0).getClass());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;

        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
        serviceTaskInstanceEntity.setActivityId(activityId);
        serviceTaskInstanceEntity.setFlowInstanceId(flowInstanceId);

        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(serviceTaskInstanceEntity));

        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(FlowTaskInstance.class, result.get().getClass());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;

        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setActivityId(activityId);
        userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId)).thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId(activityId,
                flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(FlowApprovalInstance.class, result.get().getClass());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;

        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setName(name);
        userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);

        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name,
                flowInstanceId)).thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(FlowApprovalInstance.class, result.get().getClass());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;

        GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();
        gateWayInstanceEntity.setActivityId(activityId);
        gateWayInstanceEntity.setFlowInstanceId(flowInstanceId);

        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId)).thenReturn(Optional.of(gateWayInstanceEntity));

        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId(activityId,
                flowInstanceId);

        assertTrue(result.isPresent());
        assertEquals(FlowGatewayInstance.class, result.get().getClass());
    }

    @Test
    public void testSetFlowableElement() {
        BaseFlowNodeInstance nodeInstance = new BaseFlowNodeInstance() {};
        FlowableElement flowableElement = new FlowableElement();

        NodeInstanceEntity entity = new NodeInstanceEntity();

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeInstance.getId(),
                nodeInstance.getNodeType(), flowableElement.getType()))
                .thenReturn(Collections.singletonList(entity));

        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);

        // Add assertions here
    }
}