import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowNodeType;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.model.TaskInstanceCreatedEvent;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class FlowTaskInstanceTest {

private FlowableAdaptor flowableAdaptor;
private TaskService taskService;
private NodeInstanceEntityRepository nodeInstanceEntityRepository;
private SequenceInstanceRepository sequenceInstanceRepository;
private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
private EventPublisher eventPublisher;
private RuntimeDelegateMapper runtimeDelegateMapper;
private ScheduledExecutorService executorService;
private FlowTaskInstance flowTaskInstance;

@BeforeEach
void setUp() {
taskService = mock(TaskService.class);
flowableAdaptor = new FlowableAdaptorImpl(taskService);
nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);
sequenceInstanceRepository = mock(SequenceInstanceRepository.class);
serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
eventPublisher = mock(EventPublisher.class);
runtimeDelegateMapper = mock(RuntimeDelegateMapper.class);
executorService = Executors.newSingleThreadScheduledExecutor();

when(runtimeDelegateMapper.map(any())).thenReturn(mock(BaseRuntimeFlowableDelegate.class));
}

@Test
void testBatchCreate() {
List<FlowTaskInstance> instances = Collections.singletonList(new FlowTaskInstance(
FlowNodeType.SERVICE_TASK, 1L, 1L,
new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, Duration.ofSeconds(30), 10),
true, true,
runtimeDelegateMapper,
flowableAdaptor,
eventPublisher,
serviceTaskInstanceRepository));

List<ServiceTaskInstanceEntity> entities = Collections.singletonList(
new ServiceTaskInstanceEntity(1L, 1L, 1L, FlowNodeStatus.PENDING,
1L, 1L, 1L, FlowNodeType.SERVICE_TASK,
1L, FlowTaskExecutionStrategy.MANUAL, 30, 10, null, null));

ServiceTaskInstanceRepository serviceTaskInstanceRepositorySpy = spy(serviceTaskInstanceRepository);
doReturn(entities).when(serviceTaskInstanceRepositorySpy).batchCreate(anyList());

flowTaskInstance = new FlowTaskInstance(
FlowNodeType.SERVICE_TASK, 1L, 1L,
new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, Duration.ofSeconds(30), 10),
true, true,
runtimeDelegateMapper,
flowableAdaptor,
eventPublisher,
serviceTaskInstanceRepositorySpy,
nodeInstanceEntityRepository,
sequenceInstanceRepository,
executorService);

List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskInstanceRepositorySpy);

assertEquals(1, result.size());
assertEquals(1L, result.get(0).getId());
assertEquals(1L, result.get(0).getFlowInstanceId());
assertEquals(FlowNodeStatus.PENDING, result.get(0).getStatus());
}

@Test
void testCreate() throws Exception {
FlowTaskInstance flowTaskInstance = createFlowTaskInstance();

flowTaskInstance.create();

verify(serviceTaskInstanceRepository).save(any(ServiceTaskInstanceEntity.class));
assertEquals(1L, flowTaskInstance.getId());
assertEquals(1L, flowTaskInstance.getFlowInstanceId());
assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
}

@Test
void testUpdate() throws Exception {
FlowTaskInstance flowTaskInstance = createFlowTaskInstance();
flowTaskInstance.create();

flowTaskInstance.setStatus(FlowNodeStatus.EXECUTING);
flowTaskInstance.update();

verify(serviceTaskInstanceRepository).update(any(ServiceTaskInstanceEntity.class));
assertEquals(1L, flowTaskInstance.getId());
assertEquals(1L, flowTaskInstance.getFlowInstanceId());
assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
}

@Test
void testDelete() {
FlowTaskInstance flowTaskInstance = createFlowTaskInstance();
flowTaskInstance.create();

flowTaskInstance.delete();

verify(serviceTaskInstanceRepository).deleteById(1L);
assertEquals(0, flowTaskInstance.getId());
assertEquals(0, flowTaskInstance.getFlowInstanceId());
assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
}

@Test
void testBindServiceTask() {
FlowTaskInstance flowTaskInstance = createFlowTaskInstance();
flowTaskInstance.create();

BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
when(target.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
flowTaskInstance.bindServiceTask(target);

assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
}

@Test
void testDealloc() {
FlowTaskInstance flowTaskInstance = createFlowTaskInstance();

flowTaskInstance.dealloc();

verify(eventPublisher).removeEventListener(any(ServiceTaskStartedListener.class));
}

@Test
void testAlloc() {
FlowTaskInstance flowTaskInstance = createFlowTaskInstance();

flowTaskInstance.alloc();

verify(eventPublisher).addEventListener(any(ServiceTaskStartedListener.class));
}

@Test
void testConfirmExecute() throws Exception {
FlowTaskInstance flowTaskInstance = createFlowTaskInstance();
flowTaskInstance.create();

flowTaskInstance.confirmExecute();

verify(serviceTaskInstanceRepository).deleteById(1L);
assertEquals(0, flowTaskInstance.getId());
assertEquals(0, flowTaskInstance.getFlowInstanceId());
assertEquals(FlowNodeStatus.EXECUTED, flowTaskInstance.getStatus());
}

@Test
void testAbort() throws Exception {
FlowTaskInstance flowTaskInstance = createFlowTaskInstance();
flowTaskInstance.create();

flowTaskInstance.abort();

verify(serviceTaskInstanceRepository).deleteById(1L);
assertEquals(0, flowTaskInstance.getId());
assertEquals(0, flowTaskInstance.getFlowInstanceId());
assertEquals(FlowNodeStatus.ABORTED, flowTaskInstance.getStatus());
}

@Test
void testCancel() throws Exception {
FlowTaskInstance flowTaskInstance = createFlowTaskInstance();
flowTaskInstance.create();

BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
when(target.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
flowTaskInstance.bindServiceTask(target);

flowTaskInstance.cancel(true);

verify(target).cancel(true);
assertEquals(FlowNodeStatus.CANCELED, flowTaskInstance.getStatus());
}

@Test
void testIsDone() throws Exception {
FlowTaskInstance flowTaskInstance = createFlowTaskInstance();
flowTaskInstance.create();

BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
when(target.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
flowTaskInstance.bindServiceTask(target);

when(target.isDone()).thenReturn(true);
assertTrue(flowTaskInstance.isDone());

when(target.isDone()).thenReturn(false);
assertFalse(flowTaskInstance.isDone());
}

@Test
void testGetResult() throws Exception {
FlowTaskInstance flowTaskInstance = createFlowTaskInstance();
flowTaskInstance.create();

BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
when(target.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
flowTaskInstance.bindServiceTask(target);

when(target.get()).thenReturn("Result");
assertEquals("Result", flowTaskInstance.getResult());

when(target.get()).thenThrow(new InterruptedException());
assertThrows(InterruptedException.class, () -> flowTaskInstance.getResult());

when(target.get()).thenThrow(new ExecutionException(new Exception()));
assertThrows(ExecutionException.class, () -> flowTaskInstance.getResult());
}

@Test
void testGetResultTimeout() throws Exception {
FlowTaskInstance flowTaskInstance = createFlowTaskInstance();
flowTaskInstance.create();

BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
when(target.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
flowTaskInstance.bindServiceTask(target);

when(target.get(1, TimeUnit.SECONDS)).thenReturn("Result");
assertEquals("Result", flowTaskInstance.getResult(1, TimeUnit.SECONDS));

when(target.get(1, TimeUnit.SECONDS)).thenThrow(new ExecutionException(new Exception()));
assertThrows(TimeoutException.class, () -> flowTaskInstance.getResult(1, TimeUnit.SECONDS));

when(target.get(1, TimeUnit.SECONDS)).thenThrow(new InterruptedException());
assertThrows(InterruptedException.class, () -> flowTaskInstance.getResult(1, TimeUnit.SECONDS));
}

private FlowTaskInstance createFlowTaskInstance() {
return new FlowTaskInstance(
FlowNodeType.SERVICE_TASK, 1L, 1L,
new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, Duration.ofSeconds(30), 10),
true, true,
runtimeDelegateMapper,
flowableAdaptor,
eventPublisher,
serviceTaskInstanceRepository,
nodeInstanceEntityRepository,
sequenceInstanceRepository,
executorService);
}
}