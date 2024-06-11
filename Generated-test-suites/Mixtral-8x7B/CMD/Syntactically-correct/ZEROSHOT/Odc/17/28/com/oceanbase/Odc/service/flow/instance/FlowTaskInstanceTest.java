import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableElement;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.TaskInstanceCreatedEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest {

private FlowTaskInstance flowTaskInstance;

@Mock
private ServiceTaskInstanceRepository serviceTaskRepository;
@Mock
private TaskService taskService;
@Mock
private EventPublisher eventPublisher;
@Mock
private RuntimeDelegateMapper delegateConvertor;
@Mock
private FlowableAdaptor flowableAdaptor;
@Mock
private NodeInstanceEntityRepository nodeRepository;
@Mock
private SequenceInstanceRepository sequenceRepository;
@Mock
private BaseRuntimeFlowableDelegate<?> targetTaskHandle;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);
flowTaskInstance = new FlowTaskInstance(TaskType.SERVICE_TASK, 1L, 2L, new ExecutionStrategyConfig(), true, true,
delegateConvertor,
flowableAdaptor,
eventPublisher,
taskService,
nodeRepository,
sequenceRepository,
serviceTaskRepository);
}

@Test
public void testCreate() {
flowTaskInstance.create();
verify(serviceTaskRepository, times(1)).save(any());
}

@Test
public void testUpdate() {
flowTaskInstance.update();
verify(serviceTaskRepository, times(1)).update(any());
}

@Test
public void testDelete() {
flowTaskInstance.delete(1L);
verify(serviceTaskRepository, times(1)).deleteById(1L);
}

@Test
public void testBatchCreate() {
List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
List<ServiceTaskInstanceEntity> entities =
flowTaskInstance.batchCreate(instances, serviceTaskRepository);
assertEquals(1, entities.size());
}

@Test
public void testBindServiceTask() {
BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
when(target.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
flowTaskInstance.bindServiceTask(target);
assertEquals(target, flowTaskInstance.getTargetTaskHandle());
}

@Test
public void testIsPresentOnThisMachine() {
assertTrue(flowTaskInstance.isPresentOnThisMachine());
}

@Test
public void testDealloc() {
flowTaskInstance.dealloc();
verify(eventPublisher, times(1)).removeEventListener(flowTaskInstance.getListener());
}

@Test
public void testAlloc() {
flowTaskInstance.alloc();
verify(eventPublisher, times(1)).addEventListener(flowTaskInstance.getListener());
}

@Test
public void testConfirmExecute() {
flowTaskInstance.confirmExecute();
verify(flowTaskInstance.getTargetTaskHandle(), times(1)).complete(any());
}

@Test
public void testAbort() {
flowTaskInstance.abort();
verify(flowTaskInstance.getTargetTaskHandle(), times(1)).cancel(anyBoolean());
}

@Test
public void testCancel() {
flowTaskInstance.cancel(true);
verify(flowTaskInstance.getTargetTaskHandle(), times(1)).cancel(true);
}

@Test
public void testIsDone() {
when(flowTaskInstance.getTargetTaskHandle().isDone()).thenReturn(true);
assertTrue(flowTaskInstance.isDone());
}

@Test
public void testGetResult() throws InterruptedException, ExecutionException {
when(flowTaskInstance.getTargetTaskHandle().get()).thenReturn("Result");
assertEquals("Result", flowTaskInstance.getResult());
}

@Test
public void testGetResultWithTimeout()
throws InterruptedException, ExecutionException, TimeoutException {
when(flowTaskInstance.getTargetTaskHandle().get(1, TimeUnit.SECONDS)).thenReturn("Result");
assertEquals("Result", flowTaskInstance.getResult(1, TimeUnit.SECONDS));
}

@Test
public void testResourceType() {
assertEquals(ResourceType.ODC_FLOW_TASK_INSTANCE.name(), flowTaskInstance.resourceType());
}

@Test
public void testGetTargetDelegateClass() {
Class<? extends BaseRuntimeFlowableDelegate<?>> delegateClass =
flowTaskInstance.getTargetDelegateClass();
assertNotNull(delegateClass);
}

}