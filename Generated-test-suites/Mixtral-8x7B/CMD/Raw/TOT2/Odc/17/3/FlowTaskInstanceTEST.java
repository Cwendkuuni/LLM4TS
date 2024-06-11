import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowableEventPublisher;
import com.oceanbase.odc.service.flow.FlowableEventPublisherImpl;
import com.oceanbase.odc.service.flow.FlowNodeInstance;
import com.oceanbase.odc.service.flow.FlowNodeInstanceFactory;
import com.oceanbase.odc.service.flow.FlowNodeInstanceFactoryImpl;
import com.oceanbase.odc.service.flow.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedEvent;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

public class FlowTaskInstanceTest {

    @Mock
    private TaskService taskService;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepository;

    @Mock
    private NodeInstanceEntityRepository nodeRepository;

    @Mock
    private SequenceInstanceRepository sequenceRepository;

    @Mock
    private RuntimeDelegateMapper delegateConvertor;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    @InjectMocks
    private FlowNodeInstanceFactory flowNodeInstanceFactory;

    @InjectMocks
    private FlowableEventPublisher flowableEventPublisher;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize mocks
        when(delegateConvertor.map(TaskType.SERVICE_TASK)).thenReturn(DummyRuntimeDelegate.class);
        when(flowableAdaptor.getFlowableElementByType(anyLong(), any(FlowNodeType.class), any(FlowableElementType.class)))
                .thenReturn(Collections.emptyList());
        when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(anyLong()))
                .thenReturn(Optional.of("processInstanceId"));
    }

    @Test
    public void testCreate() {
        // Test creating a new FlowTaskInstance
        flowTaskInstance.create();

        // Verify that the ServiceTaskInstanceEntity was saved
        ArgumentCaptor<ServiceTaskInstanceEntity> entityCaptor = ArgumentCaptor.forClass(ServiceTaskInstanceEntity.class);
        verify(serviceTaskRepository).save(entityCaptor.capture());
        ServiceTaskInstanceEntity savedEntity = entityCaptor.getValue();
        assertEquals(flowTaskInstance.getId(), savedEntity.getId());
        assertEquals(flowTaskInstance.getOrganizationId(), savedEntity.getOrganizationId());
        assertEquals(flowTaskInstance.getStatus(), savedEntity.getStatus());
        assertEquals(flowTaskInstance.getFlowInstanceId(), savedEntity.getFlowInstanceId());
        assertTrue(savedEntity.getCreateTime() != null);
        assertTrue(savedEntity.getUpdateTime() != null);
    }

    @Test
    public void testUpdate() {
        // Test updating an existing FlowTaskInstance
        flowTaskInstance.update();

        // Verify that the ServiceTaskInstanceEntity was updated
        ArgumentCaptor<ServiceTaskInstanceEntity> entityCaptor = ArgumentCaptor.forClass(ServiceTaskInstanceEntity.class);
        verify(serviceTaskRepository).update(entityCaptor.capture());
        ServiceTaskInstanceEntity updatedEntity = entityCaptor.getValue();
        assertEquals(flowTaskInstance.getId(), updatedEntity.getId());
        assertEquals(flowTaskInstance.getStatus(), updatedEntity.getStatus());
        assertEquals(flowTaskInstance.getTargetTaskId(), updatedEntity.getTargetTaskId());
        assertEquals(flowTaskInstance.getTaskType(), updatedEntity.getTaskType());
    }

    @Test
    public void testDelete() {
        // Test deleting a FlowTaskInstance
        Long instanceId = 1L;
        flowTaskInstance.setId(instanceId);
        flowTaskInstance.delete(instanceId);

        // Verify that the ServiceTaskInstanceEntity was deleted
        verify(serviceTaskRepository).deleteById(instanceId);
    }

    @Test
    public void testBatchCreate() {
        // Test batch creating FlowTaskInstances
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
        List<ServiceTaskInstanceEntity> entities = flowTaskInstance.batchCreate(instances, serviceTaskRepository);

        // Verify that the ServiceTaskInstanceEntities were saved
        assertEquals(1, entities.size());
        ServiceTaskInstanceEntity savedEntity = entities.get(0);
        assertEquals(flowTaskInstance.getId(), savedEntity.getId());
        assertEquals(flowTaskInstance.getOrganizationId(), savedEntity.getOrganizationId());
        assertEquals(flowTaskInstance.getStatus(), savedEntity.getStatus());
        assertEquals(flowTaskInstance.getFlowInstanceId(), savedEntity.getFlowInstanceId());
        assertTrue(savedEntity.getCreateTime() != null);
        assertTrue(savedEntity.getUpdateTime() != null);
    }

    @Test
    public void testBindServiceTask() {
        // Test binding a ServiceTask to a FlowTaskInstance
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        // Verify that the targetTaskHandle was set
        assertEquals(target, flowTaskInstance.getTargetTaskHandle());
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testIsPresentOnThisMachine() {
        // Test checking if a ServiceTask is present on this machine
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
        flowTaskInstance.setTargetTaskHandle(target);
        assertTrue(flowTaskInstance.isPresentOnThisMachine());

        // Test when the targetTaskHandle is null
        flowTaskInstance.setTargetTaskHandle(null);
        assertFalse(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testDealloc() {
        // Test deallocating a listener
        flowTaskInstance.dealloc();

        // Verify that the listener was removed
        verify(eventPublisher).removeEventListener(flowTaskInstance.getListener());
        assertNull(flowTaskInstance.getListener());
    }

    @Test
    public void testAlloc() {
        // Test allocating a listener
        flowTaskInstance.alloc();

        // Verify that the listener was added
        verify(eventPublisher).addEventListener(flowTaskInstance.getListener());
        assertNotNull(flowTaskInstance.getListener());
    }

    @Test
    public void testComplete() {
        // Test completing a FlowTaskInstance
        flowTaskInstance.complete(false);

        // Verify that the complete method of the targetTaskHandle was called
        verify(flowTaskInstance.getTargetTaskHandle()).complete(false);
    }

    @Test
    public void testAbort() {
        // Test aborting a FlowTaskInstance
        flowTaskInstance.abort();

        // Verify that the complete method of the targetTaskHandle was called with aborted=true
        verify(flowTaskInstance.getTargetTaskHandle()).complete(true);
    }

    @Test
    public void testCancel() {
        // Test canceling a ServiceTask
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
        flowTaskInstance.setTargetTaskHandle(target);
        flowTaskInstance.cancel(true);

        // Verify that the cancel method of the targetTaskHandle was called
        verify(target).cancel(true);
    }

    @Test
    public void testIsDone() {
        // Test checking if a ServiceTask is done
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
        flowTaskInstance.setTargetTaskHandle(target);
        when(target.isDone()).thenReturn(true);
        assertTrue(flowTaskInstance.isDone());

        // Test when the targetTaskHandle is null
        flowTaskInstance.setTargetTaskHandle(null);
        assertFalse(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        // Test getting the result of a ServiceTask
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
        flowTaskInstance.setTargetTaskHandle(target);
        when(target.get()).thenReturn("Result");
        assertEquals("Result", flowTaskInstance.getResult());

        // Test when the targetTaskHandle is null
        flowTaskInstance.setTargetTaskHandle(null);
        assertThrows(UnsupportedOperationException.class, () -> flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout()
            throws InterruptedException, ExecutionException, TimeoutException {
        // Test getting the result of a ServiceTask with a timeout
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
        flowTaskInstance.setTargetTaskHandle(target);
        when(target.get(5, TimeUnit.SECONDS)).thenReturn("Result");
        assertEquals("Result", flowTaskInstance.getResult(5, TimeUnit.SECONDS));

        // Test when the targetTaskHandle is null
        flowTaskInstance.setTargetTaskHandle(null);
        assertThrows(UnsupportedOperationException.class, () -> flowTaskInstance.getResult(5, TimeUnit.SECONDS));
    }

    public static class DummyRuntimeDelegate extends BaseRuntimeFlowableDelegate<Object> {

        public DummyRuntimeDelegate() {
            super(null);
        }

        @Override
        public Object execute(Map<String, Object> variables) {
            return null;
        }

        @Override
        public void cancel(boolean mayInterruptIfRunning) {
            // Do nothing
        }

        @Override
        public boolean isDone() {
            return true;
        }

        @Override
        public Object get() throws InterruptedException, ExecutionException {
            return null;
        }

        @Override
        public Object get(long timeout, TimeUnit unit)
                throws InterruptedException, ExecutionException, TimeoutException {
            return null;
        }
    }

}