import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.constant.ResourceType;
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
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import org.flowable.engine.FormService;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.flowable.engine.impl.interceptor.CommandContext;
import org.flowable.engine.impl.interceptor.CommandContextInterceptor;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FlowableAdaptorImplTest {

    private FlowableAdaptor flowableAdaptor;
    private TaskService taskService;
    private FormService formService;
    private AuthenticationFacade authenticationFacade;
    private SequenceInstanceRepository sequenceInstanceRepository;
    private NodeInstanceEntityRepository nodeInstanceRepository;
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
    private UserTaskInstanceRepository userTaskInstanceRepository;
    private GateWayInstanceRepository gateWayInstanceRepository;

    @BeforeEach
    public void setup() {
        StandaloneInMemProcessEngineConfiguration configuration = new StandaloneInMemProcessEngineConfiguration();
        configuration.setActivityFontName("Arial");
        configuration.setAnnotationFontName("Arial");
        configuration.setLabelFontName("Arial");
        configuration.setHistory("full");
        configuration.setJobExecutorActivate(false);
        configuration.setDatabaseSchemaUpdate(StandaloneInMemProcessEngineConfiguration.DB_SCHEMA_UPDATE_CREATE_DROP);
        configuration.setProcessEngineName("FlowableAdaptorImplTest");
        configuration.addInterceptor(new CommandContextInterceptor() {
            @Override
            public void afterCommandContextCreate(CommandContext commandContext) {
                TaskServiceImpl taskServiceImpl = Mockito.spy(new TaskServiceImpl(commandContext));
                taskService = taskServiceImpl;
                formService = Mockito.spy(new FormService() {
                });
                authenticationFacade = Mockito.mock(AuthenticationFacade.class);
                sequenceInstanceRepository = Mockito.mock(SequenceInstanceRepository.class);
                nodeInstanceRepository = Mockito.mock(NodeInstanceEntityRepository.class);
                serviceTaskInstanceRepository = Mockito.mock(ServiceTaskInstanceRepository.class);
                userTaskInstanceRepository = Mockito.mock(UserTaskInstanceRepository.class);
                gateWayInstanceRepository = Mockito.mock(GateWayInstanceRepository.class);
                flowableAdaptor = new FlowableAdaptorImpl(nodeInstanceRepository, serviceTaskInstanceRepository,
                        Mockito.mock(EventPublisher.class), nodeInstanceRepository, sequenceInstanceRepository,
                        Mockito.mock(FlowInstanceRepository.class), userTaskInstanceRepository, taskService,
                        formService, authenticationFacade, gateWayInstanceRepository);
            }
        });

        Deployment deployment = configuration.buildProcessEngine().getRepositoryService()
                .createDeployment()
                .addClasspathResource("processes/testProcess.bpmn")
                .deploy();

        ProcessDefinition processDefinition = configuration.getRepositoryService().createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();

        ProcessInstance processInstance = configuration.getRuntimeService().startProcessInstanceById(processDefinition.getId());

        Mockito.when(nodeInstanceRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(processInstance.getId(),
                FlowNodeType.SERVICE_TASK, FlowableElementType.ACTIVITY))
                .thenReturn(Collections.singletonList(new NodeInstanceEntity()));
    }

    @Test
    public void testSetProcessInstanceId() {
        flowableAdaptor.setProcessInstanceId(1L, "processInstanceId");
        Mockito.verify(flowableAdaptor.getNodeInstanceRepository())
                .updateProcessInstanceIdById(1L, "processInstanceId");
    }

    @Test
    public void testGetFlowInstanceIdByProcessInstanceId() {
        Mockito.when(flowableAdaptor.getNodeInstanceRepository()
                .findByProcessInstanceId(Mockito.anyString()))
                .thenReturn(Collections.singletonList(new NodeInstanceEntity()));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessInstanceId("processInstanceId");

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(1L, result.get());
    }

    @Test
    public void testSetProcessDefinitionId() {
        flowableAdaptor.setProcessDefinitionId(1L, "processDefinitionId");
        Mockito.verify(flowableAdaptor.getNodeInstanceRepository())
                .updateProcessDefinitionIdById(1L, "processDefinitionId");
    }

    @Test
    public void testGetFlowInstanceIdByProcessDefinitionId() {
        Mockito.when(flowableAdaptor.getNodeInstanceRepository()
                .findByProcessDefinitionId(Mockito.anyString()))
                .thenReturn(Collections.singletonList(new NodeInstanceEntity()));

        Optional<Long> result = flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId");

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(1L, result.get());
    }

    @Test
    public void testGetProcessInstanceIdByFlowInstanceId() {
        Mockito.when(flowableAdaptor.getNodeInstanceRepository()
                .findById(Mockito.anyLong()))
                .thenReturn(Optional.of(new NodeInstanceEntity()));

        Optional<String> result = flowableAdaptor.getProcessInstanceIdByFlowInstanceId(1L);

        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void testGetFlowableElementByType() {
        List<FlowableElement> result = flowableAdaptor.getFlowableElementByType(1L, FlowNodeType.SERVICE_TASK,
                FlowableElementType.ACTIVITY);

        Assertions.assertEquals(1, result.size());
    }

    @Test
    public void testGetTaskInstanceByActivityId() {
        Optional<FlowTaskInstance> result = flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L);

        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByActivityId() {
        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByActivityId("activityId", 1L);

        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void testGetApprovalInstanceByName() {
        Optional<FlowApprovalInstance> result = flowableAdaptor.getApprovalInstanceByName("name", 1L);

        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void testGetGatewayInstanceByActivityId() {
        Optional<FlowGatewayInstance> result = flowableAdaptor.getGatewayInstanceByActivityId("activityId", 1L);

        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void testSetFlowableElement() {
        BaseFlowNodeInstance baseFlowNodeInstance = Mockito.mock(BaseFlowNodeInstance.class);
        FlowableElement flowableElement = new FlowableElement("activityId", "name", FlowableElementType.ACTIVITY);

        flowableAdaptor.setFlowableElement(baseFlowNodeInstance, flowableElement);

        Mockito.verify(flowableAdaptor.getNodeInstanceRepository())
                .save(Mockito.argThat(arg -> arg.getActivityId().equals("activityId") &&
                        arg.getFlowableElementType().equals(FlowableElementType.ACTIVITY)));
    }
}