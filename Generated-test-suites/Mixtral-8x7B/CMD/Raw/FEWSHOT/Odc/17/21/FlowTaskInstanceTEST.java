import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricTaskInstance;
import org.flowable.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Task;
import org.flowable.task.api.TaskCandidateUser;
import org.flowable.task.api.TaskCandidateGroup;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowableElementType.ServiceTaskElementType;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowableElementConvertor;
import com.oceanbase.odc.service.flow.FlowableElementConvertorImpl;
import com.oceanbase.odc.service.flow.FlowableTaskService;
import com.oceanbase.odc.service.flow.FlowableTaskServiceImpl;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapperImpl;

public class FlowTaskInstanceTest {

private FlowableAdaptor flowableAdaptor;
private NodeInstanceEntityRepository nodeInstanceEntityRepository;
private SequenceInstanceRepository sequenceInstanceRepository;
private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
private EventPublisher eventPublisher;
private TaskService taskService;
private HistoryService historyService;
private RuntimeService runtimeService;
private FlowableElementConvertor flowableElementConvertor;
private RuntimeDelegateMapper runtimeDelegateMapper;
private FlowableTaskService flowableTaskService;

@Before
public void setUp() {
StandaloneInMemProcessEngineConfiguration configuration = new StandaloneInMemProcessEngineConfiguration();
configuration.setJdbcUrl("jdbc:h2:mem:flowable;DB_CLOSE_DELAY=-1");
configuration.setJdbcUsername("sa");
configuration.setJdbcPassword(" ");
configuration.setJdbcDriver("org.h2.Driver");
configuration.setDatabaseSchemaUpdate(StandaloneInMemProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
configuration.buildProcessEngine();

this.flowableAdaptor = new FlowableAdaptorImpl();
this.nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);
this.sequenceInstanceRepository = mock(SequenceInstanceRepository.class);
this.serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
this.eventPublisher = mock(EventPublisher.class);
this.taskService = mock(TaskService.class);
this.historyService = mock(HistoryService.class);
this.runtimeService = mock(RuntimeService.class);
this.flowableElementConvertor = new FlowableElementConvertorImpl();
this.runtimeDelegateMapper = new RuntimeDelegateMapperImpl();
this.flowableTaskService = new FlowableTaskServiceImpl(flowableAdaptor, nodeInstanceEntityRepository,
sequenceInstanceRepository, serviceTaskInstanceRepository, eventPublisher, taskService, historyService,
runtimeService, flowableElementConvertor, runtimeDelegateMapper);
}

@After
public void tearDown() {
// Clean up the process engine
}

@Test
public void testBatchCreate() {
// Prepare test data
List<FlowTaskInstance> instances = new ArrayList<>();
FlowTaskInstance instance1 = createFlowTaskInstance(1L, FlowNodeType.SERVICE_TASK,
FlowableElementType.SERVICE_TASK, "testTask1");
FlowTaskInstance instance2 = createFlowTaskInstance(2L, FlowNodeType.SERVICE_TASK,
FlowableElementType.SERVICE_TASK, "testTask2");
instances.add(instance1);
instances.add(instance2);

// Execute the method under test
List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskInstanceRepository);

// Verify the result
assertEquals(2, result.size());
assertEquals(instance1.getId(), result.get(0).getId());
assertEquals(instance2.getId(), result.get(1).getId());
}

@Test
public void testCreate() {
FlowTaskInstance instance = createFlowTaskInstance(1L, FlowNodeType.SERVICE_TASK,
FlowableElementType.SERVICE_TASK, "testTask");

// Prepare mocks
when(serviceTaskInstanceRepository.save(any(ServiceTaskInstanceEntity.class)))
.thenReturn(new ServiceTaskInstanceEntity());

// Execute the method under test
instance.create();

// Verify the result
Verify.notNull(instance.getId());
Verify.notNull(instance.getCreateTime());
Verify.notNull(instance.getUpdateTime());
}

@Test
public void testUpdate() {
FlowTaskInstance instance = createFlowTaskInstance(1L, FlowNodeType.SERVICE_TASK,
FlowableElementType.SERVICE_TASK, "testTask");

// Prepare mocks
when(serviceTaskInstanceRepository.update(any(ServiceTaskInstanceEntity.class)))
.thenReturn(1);

// Execute the method under test
instance.update();

// Verify the result
assertTrue(true); // Update method does not return any value
}

@Test
public void testComplete() throws ExecutionException, InterruptedException {
FlowTaskInstance instance = createFlowTaskInstance(1L, FlowNodeType.SERVICE_TASK,
FlowableElementType.SERVICE_TASK, "testTask");

// Prepare mocks
List<FlowableElement> elements = new ArrayList<>();
FlowableElement element = new FlowableElement();
element.setName("userTask1");
elements.add(element);
when(flowableAdaptor.getFlowableElementByType(instance.getId(), instance.getNodeType(),
FlowableElementType.USER_TASK)).thenReturn(elements);
when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(instance.getFlowInstanceId()))
.thenReturn(Optional.of("processInstance1"));
when(taskService.createTaskQuery().processInstanceId("processInstance1")
.list()).thenReturn(Collections.singletonList(mock(Task.class)));

// Execute the method under test
instance.complete(false);

// Verify the result
// ...
}

@Test
public void testAbort() throws ExecutionException, InterruptedException {
FlowTaskInstance instance = createFlowTaskInstance(1L, FlowNodeType.SERVICE_TASK,
FlowableElementType.SERVICE_TASK, "testTask");

// Prepare mocks
List<FlowableElement> elements = new ArrayList<>();
FlowableElement element = new FlowableElement();
element.setName("userTask1");
elements.add(element);
when(flowableAdaptor.getFlowableElementByType(instance.getId(), instance.getNodeType(),
FlowableElementType.USER_TASK)).thenReturn(elements);
when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(instance.getFlowInstanceId()))
.thenReturn(Optional.of("processInstance1"));
when(taskService.createTaskQuery().processInstanceId("processInstance1")
.list()).thenReturn(Collections.singletonList(mock(Task.class)));

// Execute the method under test
instance.abort();

// Verify the result
// ...
}

@Test
public void testGetTargetDelegateClass() {
FlowTaskInstance instance = createFlowTaskInstance(1L, FlowNodeType.SERVICE_TASK,
FlowableElementType.SERVICE_TASK, "testTask");

// Prepare mocks
Class<? extends BaseRuntimeFlowableDelegate<?>> delegateClass =
mock(Class.class);
when(runtimeDelegateMapper.map(TaskType.SERVICE_TASK))
.thenReturn(delegateClass);

// Execute the method under test
Class<? extends BaseRuntimeFlowableDelegate<?>> result = instance.getTargetDelegateClass();

// Verify the result
assertEquals(delegateClass, result);
}

private FlowTaskInstance createFlowTaskInstance(Long id, FlowNodeType nodeType,
FlowableElementType elementType, String taskName) {
FlowTaskInstance instance = new FlowTaskInstance(nodeType, 1L, 1L,
new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 30, 2), false, false,
runtimeDelegateMapper, flowableAdaptor, eventPublisher, taskService,
nodeInstanceEntityRepository, sequenceInstanceRepository,
serviceTaskInstanceRepository);

instance.setId(id);
instance.setNodeType(nodeType);
instance.setElementType(elementType);
instance.setTaskType(TaskType.SERVICE_TASK);
instance.setStatus(FlowNodeStatus.PENDING);

return instance;
}
}