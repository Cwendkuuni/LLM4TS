import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;
    private ServiceTaskInstanceEntity serviceTaskInstanceEntity;
    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
    private TaskService taskService;
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;
    private SequenceInstanceRepository sequenceInstanceRepository;
    private FlowableAdaptor flowableAdaptor;
    private RuntimeDelegateMapper runtimeDelegateMapper;
    private EventPublisher eventPublisher;

    @BeforeEach
    public void setup() {
        serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
        serviceTaskInstanceEntity.setId(1L);
        serviceTaskInstanceEntity.setOrganizationId(1L);
        serviceTaskInstanceEntity.setStatus(FlowNodeStatus.PENDING);
        serviceTaskInstanceEntity.setFlowInstanceId(1L);
        serviceTaskInstanceEntity.setStartEndpoint(true);
        serviceTaskInstanceEntity.setEndEndpoint(false);
        serviceTaskInstanceEntity.setStrategy(FlowTaskExecutionStrategy.AUTO);
        serviceTaskInstanceEntity.setTaskType(TaskType.GENERAL);
        serviceTaskInstanceEntity.setTargetTaskId(null);
        serviceTaskInstanceEntity.setWaitExecExpireIntervalSeconds(300);
        serviceTaskInstanceEntity.setExecutionTime(LocalDateTime.now());

        serviceTaskInstanceRepository = Mockito.mock(ServiceTaskInstanceRepository.class);
        Mockito.when(serviceTaskInstanceRepository.save(Mockito.any())).thenAnswers(invocationOnMock -> {
            ServiceTaskInstanceEntity entity = invocationOnMock.getArgument(0);
            entity.setId(2L);
            entity.setCreateTime(LocalDateTime.now());
            entity.setUpdateTime(LocalDateTime.now());
            return entity;
        });
        Mockito.when(serviceTaskInstanceRepository.batchCreate(Mockito.anyList())).thenAnswers(invocationOnMock -> {
            List<ServiceTaskInstanceEntity> entities = invocationOnMock.getArgument(0);
            for (int i = 0; i < entities.size(); i++) {
                entities.get(i).setId(2L + i);
                entities.get(i).setCreateTime(LocalDateTime.now());
                entities.get(i).setUpdateTime(LocalDateTime.now());
            }
            return entities;
        });

        taskService = Mockito.mock(TaskService.class);

        nodeInstanceEntityRepository = Mockito.mock(NodeInstanceEntityRepository.class);

        sequenceInstanceRepository = Mockito.mock(SequenceInstanceRepository.class);

        flowableAdaptor = Mockito.mock(FlowableAdaptor.class);
        Mockito.when(flowableAdaptor.getFlowableElementByType(
                Mockito.anyLong(), Mockito.eq(FlowNodeType.SERVICE_TASK), Mockito.eq(FlowableElementType.USER_TASK)
        )).thenReturn(Collections.emptyList());
        Mockito.when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(Mockito.anyLong()))
                .thenReturn(Optional.of("processInstanceId"));

        runtimeDelegateMapper = Mockito.mock(RuntimeDelegateMapper.class);

        eventPublisher = Mockito.mock(EventPublisher.class);

        flowTaskInstance = new FlowTaskInstance(
                serviceTaskInstanceEntity, runtimeDelegateMapper, flowableAdaptor, eventPublisher, taskService,
                nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository
        );
    }

    @Test
    public void testExtractPublicMethods() {
        // Extract and list all the public methods including their signatures
        Class<FlowTaskInstance> clazz = FlowTaskInstance.class;
        for (var method : clazz.getDeclaredMethods()) {
            System.out.println(method.getName() + " " + method.getParameterTypes().length);
        }
    }

    @Test
    public void testCreate() {
        // Test create method with valid inputs
        flowTaskInstance.create();

        // Verify the expected behavior
        Mockito.verify(serviceTaskInstanceRepository).save(Mockito.any());
    }

    @Test
    public void testUpdate() {
        // Test update method with valid inputs
        flowTaskInstance.update();

        // Verify the expected behavior
        Mockito.verify(serviceTaskInstanceRepository).update(Mockito.any());
    }

    @Test
    public void testDelete() {
        // Test delete method with valid inputs
        flowTaskInstance.delete(3L);

        // Verify the expected behavior
        Mockito.verify(serviceTaskInstanceRepository).deleteById(3L);
    }

    @Test
    public void testBatchCreate() {
        // Test batchCreate method with valid inputs
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
        List<ServiceTaskInstanceEntity> entities = flowTaskInstance.batchCreate(instances);

        // Verify the expected behavior
        Assertions.assertEquals(2, entities.get(0).getId());
    }

    @Test
    public void testBindServiceTask() {
        // Test bindServiceTask method with valid inputs
        BaseRuntimeFlowableDelegate<?> delegate = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(delegate);

        // Verify the expected behavior
        Mockito.verify(flowTaskInstance).setStatus(FlowNodeStatus.EXECUTING);
    }

    @Test
    public void testDealloc() {
        // Test dealloc method with valid inputs
        flowTaskInstance.dealloc();

        // Verify the expected behavior
        Mockito.verify(eventPublisher).removeEventListener(Mockito.any());
    }

    @Test
    public void testAlloc() {
        // Test alloc method with valid inputs
        flowTaskInstance.alloc();

        // Verify the expected behavior
        Mockito.verify(eventPublisher).addEventListener(Mockito.any());
    }

    @Test
    public void testComplete() {
        // Test complete method with valid inputs
        flowTaskInstance.complete(false);

        // Verify the expected behavior
        Mockito.verify(flowableAdaptor).getFlowableElementByType(
                Mockito.anyLong(), Mockito.eq(FlowNodeType.SERVICE_TASK), Mockito.eq(FlowableElementType.USER_TASK)
        );
        Mockito.verify(flowableAdaptor).getProcessInstanceIdByFlowInstanceId(Mockito.anyLong());
        Mockito.verify(taskService).createTaskQuery().processInstanceId(Mockito.anyString()).list();
    }

    @Test
    public void testConfirmExecute() {
        // Test confirmExecute method with valid inputs
        flowTaskInstance.confirmExecute();

        // Verify the expected behavior
        Mockito.verify(flowTaskInstance).complete(false);
    }

    @Test
    public void testAbort() {
        // Test abort method with valid inputs
        flowTaskInstance.abort();

        // Verify the expected behavior
        Mockito.verify(flowTaskInstance).complete(true);
    }

    @Test
    public void testCancel() {
        // Test cancel method with valid inputs
        BaseRuntimeFlowableDelegate<?> delegate = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(delegate);
        flowTaskInstance.cancel(true);

        // Verify the expected behavior
        Mockito.verify(delegate).cancel(true);
    }

    @Test
    public void testIsDone() {
        // Test isDone method with valid inputs
        BaseRuntimeFlowableDelegate<?> delegate = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(delegate);
        Mockito.when(delegate.isDone()).thenReturn(true);

        // Test the method
        boolean result = flowTaskInstance.isDone();

        // Verify the expected behavior
        Assertions.assertTrue(result);
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        // Test getResult method with valid inputs
        BaseRuntimeFlowableDelegate<?> delegate = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(delegate);
        Mockito.when(delegate.get()).thenReturn("result");

        // Test the method
        String result = flowTaskInstance.getResult();

        // Verify the expected behavior
        Assertions.assertEquals("result", result);
    }

    @Test
    public void testGetResultWithTimeout()
            throws InterruptedException, ExecutionException, TimeoutException {
        // Test getResultWithTimeout method with valid inputs
        BaseRuntimeFlowableDelegate<?> delegate = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(delegate);
        Mockito.when(delegate.get(100, TimeUnit.MILLISECONDS)).thenReturn("result");

        // Test the method
        String result = flowTaskInstance.getResult(100, TimeUnit.MILLISECONDS);

        // Verify the expected behavior
        Assertions.assertEquals("result", result);
    }

    @Test
    public void testResourceType() {
        // Test resourceType method with valid inputs
        String result = flowTaskInstance.resourceType();

        // Verify the expected behavior
        Assertions.assertEquals(ResourceType.ODC_FLOW_TASK_INSTANCE.name(), result);
    }

    @Test
    public void testGetTargetDelegateClass() {
        // Test getTargetDelegateClass method with valid inputs
        Class<? extends BaseRuntimeFlowableDelegate<?>> result = flowTaskInstance.getTargetDelegateClass();

        // Verify the expected behavior
        Mockito.verifyNoInteractions(runtimeDelegateMapper);
    }

}