import org.flowable.engine.TaskService;
import org.flowable.engine.form.FormService;
import org.flowable.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = StandaloneInMemProcessEngineConfiguration.class)
@ActiveProfiles("test")
public class FlowableAdaptorImplTest {

    @Autowired
    private FlowableAdaptor flowableAdaptor;

    private TaskService taskService;
    private FormService formService;
    private EventPublisher eventPublisher;
    private NodeInstanceEntityRepository nodeInstanceRepository;
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
    private NodeInstanceEntityRepository nodeRepository;
    private SequenceInstanceRepository sequenceRepository;
    private FlowInstanceRepository flowInstanceRepository;
    private UserTaskInstanceRepository userTaskInstanceRepository;
    private GateWayInstanceRepository gateWayInstanceRepository;
    private AuthenticationFacade authenticationFacade;

    @Before
    public void setUp() {
        taskService = mock(TaskService.class);
        formService = mock(FormService.class);
        eventPublisher = mock(EventPublisher.class);
        nodeInstanceRepository = mock(NodeInstanceEntityRepository.class);
        serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
        nodeRepository = mock(NodeInstanceEntityRepository.class);
        sequenceRepository = mock(SequenceInstanceRepository.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);
        userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
        gateWayInstanceRepository = mock(GateWayInstanceRepository.class);
        authenticationFacade = mock(AuthenticationFacade.class);

        flowableAdaptor = new FlowableAdaptorImpl();
        flowableAdaptor.setTaskService(taskService);
        flowableAdaptor.setFormService(formService);
        flowableAdaptor.setEventPublisher(eventPublisher);
        flowableAdaptor.setNodeInstanceRepository(nodeInstanceRepository);
        flowableAdaptor.setServiceTaskInstanceRepository(serviceTaskInstanceRepository);
        flowableAdaptor.setNodeRepository(nodeRepository);
        flowableAdaptor.setSequenceRepository(sequenceRepository);
        flowableAdaptor.setFlowInstanceRepository(flowInstanceRepository);
        flowableAdaptor.setUserTaskInstanceRepository(userTaskInstanceRepository);
        flowableAdaptor.setGateWayInstanceRepository(gateWayInstanceRepository);
        flowableAdaptor.setAuthenticationFacade(authenticationFacade);
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";

        when(flowInstanceRepository.findById(anyLong())).thenReturn(Optional.of(new FlowInstanceEntity()));
        when(flowInstanceRepository.updateProcessInstanceIdById(anyLong(), anyString())).thenReturn(1);

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "processInstanceId";

        when(flowInstanceRepository.findAll(any(Specification.class)))
                .thenReturn(Collections.singletonList(new FlowInstanceEntity()));

        Optional<Long> flowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);
        assertTrue(flowInstanceId.isPresent());
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "processDefinitionId";

        when(flowInstanceRepository.findById(anyLong())).thenReturn(Optional.of(new FlowInstanceEntity()));
        when(flowInstanceRepository.updateProcessDefinitionIdById(anyLong(), anyString())).thenReturn(1);

        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "processDefinitionId";

        when(flowInstanceRepository.findAll(any(Specification.class)))
                .thenReturn(Collections.singletonList(new FlowInstanceEntity()));

        Optional<Long> flowInstanceId = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        assertTrue(flowInstanceId.isPresent());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;

        when(flowInstanceRepository.findById(anyLong()))
                .thenReturn(Optional.of(new FlowInstanceEntity().setProcessInstanceId("processInstanceId")));

        Optional<String> processInstanceId =
                flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);
        assertTrue(processInstanceId.isPresent());
        assertEquals("processInstanceId", processInstanceId.get());
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                anyLong(), any(FlowNodeType.class), any(FlowableElementType.class)))
                .thenReturn(Collections.singletonList(new NodeInstanceEntity()));

        List<FlowableElement> flowableElements =
                flowableAdaptor.getFlowableElementByType(instanceId, instanceType, flowableElementType);
        assertFalse(flowableElements.isEmpty());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;

        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(any(FlowNodeType.class),
                anyString(), anyLong()))
                .thenReturn(Optional.of(new ServiceTaskInstanceEntity()));

        Optional<FlowTaskInstance> flowTaskInstance =
                flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);
        assertTrue(flowTaskInstance.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(any(FlowNodeType.class),
                anyString(), anyLong()))
                .thenReturn(Optional.of(new UserTaskInstanceEntity()));

        Optional<FlowApprovalInstance> flowApprovalInstance =
                flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);
        assertTrue(flowApprovalInstance.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;

        when(userTaskInstanceRepository.findByInstanceTypeAndName(any(FlowNodeType.class),
                anyString(), anyLong()))
                .thenReturn(Optional.of(new UserTaskInstanceEntity()));

        Optional<FlowApprovalInstance> flowApprovalInstance =
                flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);
        assertTrue(flowApprovalInstance.isPresent());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;

        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(any(FlowNodeType.class),
                anyString(), anyLong()))
                .thenReturn(Optional.of(new GateWayInstanceEntity()));

        Optional<FlowGatewayInstance> flowGatewayInstance =
                flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);
        assertTrue(flowGatewayInstance.isPresent());
    }

    @Test
    public void testSetFlowableElement() {
        BaseFlowNodeInstance nodeInstance = mock(BaseFlowNodeInstance.class);
        FlowableElement flowableElement = new FlowableElement("activityId", "name", FlowableElementType.TASK);

        flowableAdaptor.setFlowableElement(nodeInstance, flowableElement);
    }

    @Test
    public void testInnerConvertWhenOptionalIsNull() {
        Optional<Object> optional = Optional.empty();

        Optional<Object> result = flowableAdaptor.innerConvert(optional, this, (v, flowService) -> null);

        assertFalse(result.isPresent());
    }

    @Test
    public void testInnerConvertWhenFunctionIsNull() {
        Object value = new Object();

        Optional<Object> result = flowableAdaptor.innerConvert(Optional.of(value), this, null);

        assertTrue(result.isPresent());
        assertEquals(value, result.get());
    }

    @After
    public void tearDown() {
        taskService = null;
        formService = null;
        eventPublisher = null;
        nodeInstanceRepository = null;
        serviceTaskInstanceRepository = null;
        nodeRepository = null;
        sequenceRepository = null;
        flowInstanceRepository = null;
        userTaskInstanceRepository = null;
        gateWayInstanceRepository = null;
        authenticationFacade = null;
    }
}