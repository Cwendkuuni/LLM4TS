import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest {

    private static final Long ORGANIZATION_ID = 1L;
    private static final Long FLOW_INSTANCE_ID = 2L;
    private static final Long TASK_ID = 3L;
    private static final Long TARGET_TASK_ID = 4L;
    private static final FlowNodeStatus STATUS = FlowNodeStatus.PENDING;
    private static final FlowableElementType ELEMENT_TYPE = FlowableElementType.SERVICE_TASK;
    private static final FlowNodeType NODE_TYPE = FlowNodeType.SERVICE_TASK;
    private static final FlowTaskExecutionStrategy STRATEGY = FlowTaskExecutionStrategy.AUTO;
    private static final TaskType TASK_TYPE = TaskType.ODC_EXECUTE_TASK;
    private static final String PROCESS_INSTANCE_ID = "processInstanceId";

    private NodeInstanceEntityRepository nodeRepository;
    private SequenceInstanceRepository sequenceRepository;
    private ServiceTaskInstanceRepository serviceTaskRepository;
    private TaskService taskService;
    private EventPublisher eventPublisher;
    private FlowableAdaptor flowableAdaptor;
    private RuntimeDelegateMapper delegateConvertor;

    @Before
    public void setUp() {
        nodeRepository = mock(NodeInstanceEntityRepository.class);
        sequenceRepository = mock(SequenceInstanceRepository.class);
        serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);
        taskService = mock(TaskService.class);
        eventPublisher = mock(EventPublisher.class);
        flowableAdaptor = new FlowableAdaptorImpl();
        delegateConvertor = mock(RuntimeDelegateMapper.class);
    }

    @Test
    public void testFlowTaskInstanceCreation() {
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setId(TASK_ID);
        entity.setOrganizationId(ORGANIZATION_ID);
        entity.setStatus(STATUS);
        entity.setFlowInstanceId(FLOW_INSTANCE_ID);
        entity.setCreateTime(System.currentTimeMillis());
        entity.setUpdateTime(System.currentTimeMillis());
        entity.setStartEndpoint(true);
        entity.setEndEndpoint(false);
        entity.setTargetTaskId(TARGET_TASK_ID);
        entity.setTaskType(TASK_TYPE);
        entity.setStrategy(STRATEGY);

        FlowTaskExecutionStrategyConfig strategyConfig = new FlowTaskExecutionStrategyConfig();
        strategyConfig.setStrategy(STRATEGY);
        strategyConfig.setExecutionTime(Duration.ofSeconds(10));
        strategyConfig.setPendingExpireIntervalSeconds(30);

        when(serviceTaskRepository.save(any(ServiceTaskInstanceEntity.class)))
                .thenReturn(entity);

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(entity, delegateConvertor, flowableAdaptor,
                eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

        assertEquals(TASK_ID, flowTaskInstance.getId());
        assertEquals(ORGANIZATION_ID, flowTaskInstance.getOrganizationId());
        assertEquals(STATUS, flowTaskInstance.getStatus());
        assertEquals(FLOW_INSTANCE_ID, flowTaskInstance.getFlowInstanceId());
        assertEquals(FlowNodeType.SERVICE_TASK, flowTaskInstance.getNodeType());
        assertEquals(ELEMENT_TYPE, flowTaskInstance.getElementType());
        assertEquals(STRATEGY, flowTaskInstance.getStrategyConfig().getStrategy());
        assertEquals(TASK_TYPE, flowTaskInstance.getTaskType());

        verify(serviceTaskRepository, times(1)).save(entity);
    }

    @Test
    public void testBatchCreate() {
        ServiceTaskInstanceEntity entity1 = new ServiceTaskInstanceEntity();
        entity1.setId(TASK_ID);
        entity1.setOrganizationId(ORGANIZATION_ID);
        entity1.setStatus(STATUS);
        entity1.setFlowInstanceId(FLOW_INSTANCE_ID);
        entity1.setCreateTime(System.currentTimeMillis());
        entity1.setUpdateTime(System.currentTimeMillis());
        entity1.setStartEndpoint(true);
        entity1.setEndEndpoint(false);
        entity1.setTargetTaskId(TARGET_TASK_ID);
        entity1.setTaskType(TASK_TYPE);
        entity1.setStrategy(STRATEGY);

        ServiceTaskInstanceEntity entity2 = new ServiceTaskInstanceEntity();
        entity2.setId(TASK_ID + 1);
        entity2.setOrganizationId(ORGANIZATION_ID);
        entity2.setStatus(STATUS);
        entity2.setFlowInstanceId(FLOW_INSTANCE_ID);
        entity2.setCreateTime(System.currentTimeMillis());
        entity2.setUpdateTime(System.currentTimeMillis());
        entity2.setStartEndpoint(true);
        entity2.setEndEndpoint(false);
        entity2.setTargetTaskId(TARGET_TASK_ID);
        entity2.setTaskType(TASK_TYPE);
        entity2.setStrategy(STRATEGY);

        List<FlowTaskInstance> instances = Arrays.asList(
                new FlowTaskInstance(entity1, delegateConvertor, flowableAdaptor, eventPublisher, taskService,
                        nodeRepository, sequenceRepository, serviceTaskRepository),
                new FlowTaskInstance(entity2, delegateConvertor, flowableAdaptor, eventPublisher, taskService,
                        nodeRepository, sequenceRepository, serviceTaskRepository)
        );

        List<ServiceTaskInstanceEntity> entities = instances.stream()
                .map(FlowTaskInstance::mapToTaskEntity)
                .collect(Collectors.toList());

        when(serviceTaskRepository.batchCreate(entities))
                .thenReturn(Arrays.asList(entity1, entity2));

        List<FlowTaskInstance> createdInstances = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);

        assertEquals(instances, createdInstances);
        verify(serviceTaskRepository, times(1)).batchCreate(entities);
    }

    // Add more test methods for other methods in the FlowTaskInstance class

}