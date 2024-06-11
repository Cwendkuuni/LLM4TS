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
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedEvent;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.TaskType;
import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FlowTaskInstanceTest {

private FlowableAdaptor flowableAdaptor;
private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
private TaskService taskService;
private NodeInstanceEntityRepository nodeInstanceEntityRepository;
private SequenceInstanceRepository sequenceInstanceRepository;
private EventPublisher eventPublisher;
private RuntimeDelegateMapper runtimeDelegateMapper;
private FlowTaskInstance flowTaskInstance;

@BeforeEach
public void setup() {
taskService = mock(TaskService.class);
flowableAdaptor = new FlowableAdaptorImpl(taskService);
serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);
sequenceInstanceRepository = mock(SequenceInstanceRepository.class);
eventPublisher = mock(EventPublisher.class);
runtimeDelegateMapper = mock(RuntimeDelegateMapper.class);

when(runtimeDelegateMapper.map(TaskType.SERVICE_TASK))
.thenReturn(ServiceTaskStartedListener.class);

flowTaskInstance = new FlowTaskInstance(
TaskType.SERVICE_TASK,
1L,
1L,
new ExecutionStrategyConfig(FlowTaskExecutionStrategy.AUTO, 10, 20),
true,
false,
runtimeDelegateMapper,
flowableAdaptor,
eventPublisher,
taskService,
nodeInstanceEntityRepository,
sequenceInstanceRepository,
serviceTaskInstanceRepository
);
}

@Test
public void testCreate() {
flowTaskInstance.create();

Verify.verify(serviceTaskInstanceRepository.save(Mockito.any()))
.isNotNull();
}

@Test
public void testUpdate() {
flowTaskInstance.setStatus(FlowNodeStatus.EXECUTING);
flowTaskInstance.update();

Verify.verify(serviceTaskInstanceRepository.update(Mockito.any()))
.isNotNull();
}

@Test
public void testDelete() {
flowTaskInstance.setId(1L);
flowTaskInstance.doDelete(1L);

Verify.verify(serviceTaskInstanceRepository.deleteById(1L))
.isNotNull();
}

@Test
public void testBatchCreate() {
List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);

serviceTaskInstanceRepository = Mockito.mock(ServiceTaskInstanceRepository.class);

when(serviceTaskInstanceRepository.batchCreate(Mockito.anyList()))
.thenReturn(Collections.singletonList(
new ServiceTaskInstanceEntity(
1L,
1L,
1L,
FlowNodeStatus.PENDING,
LocalDateTime.now(),
LocalDateTime.now(),
true,
true,
TaskType.SERVICE_TASK,
new ExecutionStrategyConfig(FlowTaskExecutionStrategy.AUTO, 10, 20),
null
)
));

List<ServiceTaskInstanceEntity> result =
FlowTaskInstance.batchCreate(instances, serviceTaskInstanceRepository);

assertEquals(1, result.size());
}

@Test
public void testMapToTaskEntity() {
ServiceTaskInstanceEntity entity =
FlowTaskInstance.mapToTaskEntity(flowTaskInstance);

assertEquals(1, entity.getOrganizationId());
assertEquals(1, entity.getFlowInstanceId());
assertEquals(FlowNodeStatus.PENDING, entity.getStatus());
assertEquals(TaskType.SERVICE_TASK, entity.getTaskType());
assertEquals(
new ExecutionStrategyConfig(FlowTaskExecutionStrategy.AUTO, 10, 20),
entity.getStrategy()
);
}

@Test
public void testComplete() {
flowTaskInstance.setId(1L);
flowTaskInstance.complete(true);

Mockito.verify(flowableAdaptor)
.getFlowableElementByType(1L, FlowNodeType.SERVICE_TASK, FlowableElementType.USER_TASK);

Mockito.verify(taskService)
.createTaskQuery()
.processInstanceId(Mockito.anyString())
.list();

Mockito.verify(taskService)
.complete(Mockito.anyString(), Mockito.anyMap());
}

@Test
public void testGetTargetDelegateClass() {
assertEquals(ServiceTaskStartedListener.class,
flowTaskInstance.getTargetDelegateClass());
}

@Test
public void testGetResult_interruptedException() throws InterruptedException {
assertThrows(InterruptedException.class, () -> {
flowTaskInstance.setTargetTaskHandle(mock(BaseRuntimeFlowableDelegate.class));
flowTaskInstance.getResult();
});
}

@Test
public void testGetResult_executionException() throws ExecutionException, InterruptedException {
assertThrows(ExecutionException.class, () -> {
flowTaskInstance.setTargetTaskHandle(mock(BaseRuntimeFlowableDelegate.class));
when(flowTaskInstance.getTargetTaskHandle().get())
.thenThrow(new ExecutionException("test"));
flowTaskInstance.getResult();
});
}

@Test
public void testGetResult_timeoutException() throws InterruptedException, ExecutionException, TimeoutException {
assertThrows(TimeoutException.class, () -> {
flowTaskInstance.setTargetTaskHandle(mock(BaseRuntimeFlowableDelegate.class));
when(flowTaskInstance.getTargetTaskHandle().get(10, TimeUnit.MILLISECONDS))
.thenThrow(new TimeoutException("test"));
flowTaskInstance.getResult(10, TimeUnit.MILLISECONDS);
});
}

@Test
public void testIsPresentOnThisMachine() {
assertThrows(UnsupportedOperationException.class, () -> {
flowTaskInstance.isPresentOnThisMachine();
});
}

@Test
public void testBindServiceTask() {
BaseRuntimeFlowableDelegate mockDelegate = mock(BaseRuntimeFlowableDelegate.class);

flowTaskInstance.setTargetTaskHandle(mockDelegate);
flowTaskInstance.bindServiceTask(mockDelegate);

assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
}

@Test
public void testDealloc() {
flowTaskInstance.setListener(mock(ServiceTaskStartedListener.class));
flowTaskInstance.dealloc();

Mockito.verify(eventPublisher).removeEventListener(flowTaskInstance.getListener());
}

@Test
public void testAlloc() {
flowTaskInstance.alloc();

assertNotNull(flowTaskInstance.getListener());
}

@Test
public void testIsDone() {
assertThrows(UnsupportedOperationException.class, () -> {
flowTaskInstance.isDone();
});
}

@Test
public void testGetResult_exception()
throws InterruptedException, ExecutionException {
BaseRuntimeFlowableDelegate mockDelegate = mock(BaseRuntimeFlowableDelegate.class);

when(mockDelegate.get())
.thenThrow(new ExecutionException("test"));

flowTaskInstance.setTargetTaskHandle(mockDelegate);

assertThrows(ExecutionException.class, () -> {
flowTaskInstance.getResult();
});
}

@Test
public void testConfirmExecute() {
flowTaskInstance.setId(1L);
flowTaskInstance.confirmExecute();

Mockito.verify(flowableAdaptor)
.getFlowableElementByType(1L, FlowNodeType.SERVICE_TASK, FlowableElementType.USER_TASK);

Mockito.verify(taskService)
.createTaskQuery()
.processInstanceId(Mockito.anyString())
.list();

Mockito.verify(taskService)
.complete(Mockito.anyString(), Mockito.anyMap());
}

@Test
public void testAbort() {
flowTaskInstance.setId(1L);
flowTaskInstance.abort();

Mockito.verify(flowableAdaptor)
.getFlowableElementByType(1L, FlowNodeType.SERVICE_TASK, FlowableElementType.USER_TASK);

Mockito.verify(taskService)
.createTaskQuery()
.processInstanceId(Mockito.anyString())
.list();

Mockito.verify(taskService)
.complete(Mockito.anyString(), Mockito.anyMap());
}

@Test
public void testCancel() {
BaseRuntimeFlowableDelegate mockDelegate = mock(BaseRuntimeFlowableDelegate.class);

flowTaskInstance.setTargetTaskHandle(mockDelegate);

assertThrows(UnsupportedOperationException.class, () -> {
flowTaskInstance.cancel(true);
});
}

@Test
public void testGetResourceType() {
assertEquals(ResourceType.ODC_FLOW_TASK_INSTANCE.toString(),
flowTaskInstance.resourceType());
}
}