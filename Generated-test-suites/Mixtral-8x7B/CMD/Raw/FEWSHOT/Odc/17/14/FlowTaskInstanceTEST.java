import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
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
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableDelegateImpl;
import com.oceanbase.odc.service.flow.FlowNodeStatus;
import com.oceanbase.odc.service.flow.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowTaskInstance;
import com.oceanbase.odc.service.flow.model.TaskType;
import com.oceanbase.odc.service.flow.repository.NodeInstanceEntityRepositoryImpl;
import com.oceanbase.odc.service.flow.repository.SequenceInstanceRepositoryImpl;
import com.oceanbase.odc.service.flow.repository.ServiceTaskInstanceRepositoryImpl;

@RunWith(MockitoJUnitRunner.class)
public class FlowTaskInstanceTest {

 @Mock
 private TaskService taskService;

 @Mock
 private ServiceTaskInstanceRepository serviceTaskRepository;

 @Mock
 private NodeInstanceEntityRepository nodeInstanceEntityRepository;

 @Mock
 private SequenceInstanceRepository sequenceInstanceRepository;

 @Mock
 private FlowableAdaptor flowableAdaptor;

 @Mock
 private EventPublisher eventPublisher;

 @Mock
 private RuntimeDelegateMapper delegateConvertor;

 @InjectMocks
 private FlowTaskInstance flowTaskInstance;

 @Before
 public void setup() {
 Verify.setStaticInstance(nodeInstanceEntityRepository, new NodeInstanceEntityRepositoryImpl());
 Verify.setStaticInstance(sequenceInstanceRepository, new SequenceInstanceRepositoryImpl());
 Verify.setStaticInstance(serviceTaskInstanceRepository, new ServiceTaskInstanceRepositoryImpl());
 }

 // Test create() method
 @Test
 public void testCreate() {
 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(TaskType.SERVICE_TASK, 1L, 2L,
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 10, 20), false, false,
 delegateConvertor, new FlowableAdaptorImpl(), eventPublisher, taskService,
 nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);

 flowTaskInstance.create();

 verify(serviceTaskInstanceRepository, times(1)).save(any(ServiceTaskInstanceEntity.class));
 }

 // Test update() method
 @Test
 public void testUpdate() {
 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(TaskType.SERVICE_TASK, 1L, 2L,
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 10, 20), false, false,
 delegateConvertor, new FlowableAdaptorImpl(), eventPublisher, taskService,
 nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);

 flowTaskInstance.setStatus(FlowNodeStatus.EXECUTING);
 flowTaskInstance.update();

 verify(serviceTaskInstanceRepository, times(1)).update(any(ServiceTaskInstanceEntity.class));
 }

 // Test delete() method
 @Test
 public void testDelete() {
 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(TaskType.SERVICE_TASK, 1L, 2L,
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 10, 20), false, false,
 delegateConvertor, new FlowableAdaptorImpl(), eventPublisher, taskService,
 nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);

 flowTaskInstance.delete(1L);

 verify(serviceTaskInstanceRepository, times(1)).deleteById(1L);
 }

 // Test batchCreate() method
 @Test
 public void testBatchCreate() {
 List<FlowTaskInstance> flowTaskInstances = Arrays.asList(
 new FlowTaskInstance(TaskType.SERVICE_TASK, 1L, 2L,
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 10, 20), false, false,
 delegateConvertor, new FlowableAdaptorImpl(), eventPublisher, taskService,
 nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository),
 new FlowTaskInstance(TaskType.SERVICE_TASK, 1L, 2L,
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 10, 20), false, false,
 delegateConvertor, new FlowableAdaptorImpl(), eventPublisher, taskService,
 nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository));

 List<ServiceTaskInstanceEntity> entities = FlowTaskInstance.batchCreate(flowTaskInstances,
 serviceTaskInstanceRepository);

 verify(serviceTaskInstanceRepository, times(1)).batchCreate(anyList());
 }

 // Test bindServiceTask() method
 @Test
 public void testBindServiceTask() {
 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(TaskType.SERVICE_TASK, 1L, 2L,
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 10, 20), false, false,
 delegateConvertor, new FlowableAdaptorImpl(), eventPublisher, taskService,
 nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);

 FlowableDelegate flowableDelegate = new FlowableDelegateImpl();
 flowTaskInstance.bindServiceTask(flowableDelegate);

 assertTrue(flowTaskInstance.isPresentOnThisMachine());
 }

 // Test isDone() method
 @Test
 public void testIsDone() {
 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(TaskType.SERVICE_TASK, 1L, 2L,
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 10, 20), false, false,
 delegateConvertor, new FlowableAdaptorImpl() {
 @Override
 public Optional<String> getProcessInstanceIdByFlowInstanceId(Long flowInstanceId) {
 return Optional.of("1");
 }
 }, eventPublisher, taskService,
 nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);

 FlowableDelegate flowableDelegate = new FlowableDelegateImpl();
 flowTaskInstance.bindServiceTask(flowableDelegate);

 assertTrue(flowTaskInstance.isDone());
 }

 // Test getResult() method
 @Test
 public void testGetResult() throws InterruptedException, ExecutionException {
 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(TaskType.SERVICE_TASK, 1L, 2L,
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 10, 20), false, false,
 delegateConvertor, new FlowableAdaptorImpl() {
 @Override
 public Optional<String> getProcessInstanceIdByFlowInstanceId(Long flowInstanceId) {
 return Optional.of("1");
 }
 }, eventPublisher, taskService,
 nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);

 FlowableDelegate flowableDelegate = new FlowableDelegateImpl() {
 @Override
 public Object get() throws InterruptedException, ExecutionException {
 return "result";
 }
 };
 flowTaskInstance.bindServiceTask(flowableDelegate);

 assertEquals("result", flowTaskInstance.getResult());
 }

 // Test getResult(long timeout, TimeUnit unit) method
 @Test
 public void testGetResultWithTimeout()
 throws InterruptedException, ExecutionException, TimeoutException {
 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(TaskType.SERVICE_TASK, 1L, 2L,
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 10, 20), false, false,
 delegateConvertor, new FlowableAdaptorImpl() {
 @Override
 public Optional<String> getProcessInstanceIdByFlowInstanceId(Long flowInstanceId) {
 return Optional.of("1");
 }
 }, eventPublisher, taskService,
 nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);

 FlowableDelegate flowableDelegate = new FlowableDelegateImpl() {
 @Override
 public Object get() throws InterruptedException, ExecutionException {
 return "result";
 }
 };
 flowTaskInstance.bindServiceTask(flowableDelegate);

 assertEquals("result", flowTaskInstance.getResult(10, TimeUnit.SECONDS));
 }

 // Test complete(boolean aborted) method
 @Test
 public void testComplete() {
 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(TaskType.SERVICE_TASK, 1L, 2L,
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 10, 20), false, false,
 delegateConvertor, new FlowableAdaptorImpl() {
 @Override
 public List<FlowableElement> getFlowableElementByType(Long id, FlowNodeType nodeType,
 FlowableElementType elementType) {
 return Collections.singletonList(new FlowableElement("name", elementType));
 }

 @Override
 public Optional<String> getProcessInstanceIdByFlowInstanceId(Long flowInstanceId) {
 return Optional.of("1");
 }
 }, eventPublisher, taskService,
 nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);

 flowTaskInstance.complete(true);

 verify(taskService, times(1)).complete(anyString(), anyMap());
 }

 // Test mapToTaskEntity() method
 @Test
 public void testMapToTaskEntity() {
 FlowTaskInstance flowTaskInstance = new FlowTaskInstance(TaskType.SERVICE_TASK, 1L, 2L,
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 10, 20), false, false,
 delegateConvertor, new FlowableAdaptorImpl(), eventPublisher, taskService,
 nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);

 ServiceTaskInstanceEntity entity = FlowTaskInstance.mapToTaskEntity(flowTaskInstance);

 assertEquals(1L, entity.getOrganizationId().longValue());
 assertEquals(2L, entity.getFlowInstanceId().longValue());
 assertEquals(FlowNodeStatus.PENDING, entity.getStatus());
 assertEquals(FlowTaskExecutionStrategy.MANUAL, entity.getStrategy());
 assertEquals(TaskType.SERVICE_TASK, entity.getTaskType());
 }
}