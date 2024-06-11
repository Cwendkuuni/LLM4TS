import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.FlowInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.metadb.flow.FlowInstanceSpecs;
import com.oceanbase.odc.metadb.flow.GateWayInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.instance.BaseFlowNodeInstance;
import com.oceanbase.odc.service.flow.instance.FlowApprovalInstance;
import com.oceanbase.odc.service.flow.instance.FlowGatewayInstance;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import org.flowable.engine.TaskService;
import org.flowable.engine.form.FormService;
import org.flowable.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FlowableAdaptorImplTest {

    private FlowableAdaptorImpl flowableAdaptor;
    private NodeInstanceEntityRepository nodeInstanceRepository;
    private SequenceInstanceRepository sequenceInstanceRepository;
    private FlowInstanceRepository flowInstanceRepository;
    private UserTaskInstanceRepository userTaskInstanceRepository;
    private TaskService taskService;
    private FormService formService;
    private EventPublisher eventPublisher;
    private GateWayInstanceRepository gateWayInstanceRepository;

    @Before
    public void setUp() {
        StandaloneInMemProcessEngineConfiguration configuration = new StandaloneInMemProcessEngineConfiguration();
        configuration.setJdbcUrl("jdbc:h2:mem:test");
        configuration.setDatabaseSchemaUpdate(StandaloneInMemProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        configuration.buildProcessEngine();

        nodeInstanceRepository = mock(NodeInstanceEntityRepository.class);
        sequenceInstanceRepository = mock(SequenceInstanceRepository.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);
        userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
        taskService = mock(TaskService.class);
        formService = mock(FormService.class);
        eventPublisher = mock(EventPublisher.class);
        gateWayInstanceRepository = mock(GateWayInstanceRepository.class);

        flowableAdaptor = new FlowableAdaptorImpl();
        flowableAdaptor.nodeInstanceRepository = nodeInstanceRepository;
        flowableAdaptor.sequenceInstanceRepository = sequenceInstanceRepository;
        flowableAdaptor.flowInstanceRepository = flowInstanceRepository;
        flowableAdaptor.userTaskInstanceRepository = userTaskInstanceRepository;
        flowableAdaptor.taskService = taskService;
        flowableAdaptor.formService = formService;
        flowableAdaptor.eventPublisher = eventPublisher;
        flowableAdaptor.gateWayInstanceRepository = gateWayInstanceRepository;
    }

    @After
    public void tearDown() {
        flowableAdaptor = null;
    }

    @Test
    public void testSetProcessInstanceId() {
        Long flowInstanceId = 1L;
        String processInstanceId = "processInstanceId";

        flowableAdaptor.setProcessInstanceId(flowInstanceId, processInstanceId);

        verify(flowInstanceRepository, times(1))
                .updateProcessInstanceIdById(flowInstanceId, processInstanceId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        String processInstanceId = "processInstanceId";

        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        flowInstanceEntity.setProcessInstanceId(processInstanceId);

        when(flowInstanceRepository.findAll(any(Specification.class)))
                .thenReturn(Collections.singletonList(flowInstanceEntity));

        Optional<Long> flowInstanceIdOptional = flowableAdaptor.getFlowInstanceIdByProcessInstanceId(processInstanceId);

        assertTrue(flowInstanceIdOptional.isPresent());
        assertEquals(flowInstanceIdOptional.get().longValue(), 1L);
    }

    @Test
    public void testSetProcessDefinitionId() {
        Long flowInstanceId = 1L;
        String processDefinitionId = "processDefinitionId";

        flowableAdaptor.setProcessDefinitionId(flowInstanceId, processDefinitionId);

        verify(flowInstanceRepository, times(1))
                .updateProcessDefinitionIdById(flowInstanceId, processDefinitionId);
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        String processDefinitionId = "processDefinitionId";

        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(1L);
        flowInstanceEntity.setProcessDefinitionId(processDefinitionId);

        when(flowInstanceRepository.findAll(any(Specification.class)))
                .thenReturn(Collections.singletonList(flowInstanceEntity));

        Optional<Long> flowInstanceIdOptional = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

        assertTrue(flowInstanceIdOptional.isPresent());
        assertEquals(flowInstanceIdOptional.get().longValue(), 1L);
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Long flowInstanceId = 1L;

        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setId(flowInstanceId);
        flowInstanceEntity.setProcessInstanceId("processInstanceId");

        when(flowInstanceRepository.findById(flowInstanceId))
                .thenReturn(Optional.of(flowInstanceEntity));

        Optional<String> processInstanceIdOptional =
                flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowInstanceId);

        assertTrue(processInstanceIdOptional.isPresent());
        assertEquals(processInstanceIdOptional.get(), "processInstanceId");
    }

    @Test
    public void testGetFlowableElementByType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.APPROVAL_TASK;
        FlowableElementType flowableElementType = FlowableElementType.USER_TASK;

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(instanceId);
        nodeInstanceEntity.setInstanceType(instanceType);
        nodeInstanceEntity.setFlowableElementType(flowableElementType);

        when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType,
                flowableElementType))
                .thenReturn(Collections.singletonList(nodeInstanceEntity));

        List<FlowableElement> flowableElements =
                flowableAdaptor.getFlowableElementByType(instanceId, instanceType, flowableElementType);

        assertEquals(flowableElements.size(), 1);
        assertEquals(flowableElements.get(0).getType(), FlowableElementType.USER_TASK);
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;

        ServiceTaskInstanceEntity serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
        serviceTaskInstanceEntity.setId(1L);
        serviceTaskInstanceEntity.setActivityId(activityId);

        when(serviceTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, activityId,
                flowInstanceId))
                .thenReturn(Optional.of(serviceTaskInstanceEntity));

        Optional<FlowTaskInstance> flowTaskInstanceOptional =
                flowableAdaptor.getTaskInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(flowTaskInstanceOptional.isPresent());
        assertEquals(flowTaskInstanceOptional.get().getId().longValue(), 1L);
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;

        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setId(1L);
        userTaskInstanceEntity.setActivityId(activityId);

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL_TASK, activityId,
                flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> flowApprovalInstanceOptional =
                flowableAdaptor.getApprovalInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(flowApprovalInstanceOptional.isPresent());
        assertEquals(flowApprovalInstanceOptional.get().getId().longValue(), 1L);
    }

    @Test
    public void testGetApprovalInstanceByName() {
        String name = "name";
        Long flowInstanceId = 1L;

        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setId(1L);
        userTaskInstanceEntity.setName(name);

        when(userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL_TASK, name,
                flowInstanceId))
                .thenReturn(Optional.of(userTaskInstanceEntity));

        Optional<FlowApprovalInstance> flowApprovalInstanceOptional =
                flowableAdaptor.getApprovalInstanceByName(name, flowInstanceId);

        assertTrue(flowApprovalInstanceOptional.isPresent());
        assertEquals(flowApprovalInstanceOptional.get().getId().longValue(), 1L);
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        String activityId = "activityId";
        Long flowInstanceId = 1L;

        GateWayInstanceEntity gateWayInstanceEntity = new GateWayInstanceEntity();
        gateWayInstanceEntity.setId(1L);
        gateWayInstanceEntity.setActivityId(activityId);

        when(gateWayInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.GATEWAY, activityId,
                flowInstanceId))
                .thenReturn(Optional.of(gateWayInstanceEntity));

        Optional<FlowGatewayInstance> flowGatewayInstanceOptional =
                flowableAdaptor.getGatewayInstanceByActivityId(activityId, flowInstanceId);

        assertTrue(flowGatewayInstanceOptional.isPresent());
        assertEquals(flowGatewayInstanceOptional.get().getId().longValue(), 1L);
    }

    @Test
    public void testSetFlowableElement() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setId(1L);
        nodeInstanceEntity.setInstanceId(2L);
        nodeInstanceEntity.setInstanceType(FlowNodeType.APPROVAL_TASK);

        FlowableElement flowableElement = new FlowableElement();
        flowableElement.setActivityId("activityId");
        flowableElement.setName("name");
        flowableElement.setType(FlowableElementType.USER_TASK);

        flowableAdaptor.setFlowableElement(new FlowApprovalInstance(nodeInstanceEntity, flowableAdaptor,
                new OdcRuntimeDelegateMapper(), eventPublisher, taskService, nodeInstanceRepository,
                sequenceInstanceRepository, userTaskInstanceRepository), flowableElement);

        verify(nodeInstanceRepository, times(1)).save(nodeInstanceEntity);
    }
}