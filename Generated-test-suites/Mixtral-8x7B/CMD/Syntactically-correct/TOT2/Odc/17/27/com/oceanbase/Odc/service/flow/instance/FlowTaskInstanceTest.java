import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.shared.Verify;
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
import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest {

 private FlowableAdaptor flowableAdaptor;
 private EventPublisher eventPublisher;
 private TaskService taskService;
 private NodeInstanceEntityRepository nodeInstanceEntityRepository;
 private SequenceInstanceRepository sequenceInstanceRepository;
 private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
 private RuntimeDelegateMapper runtimeDelegateMapper;
 private FlowTaskInstance flowTaskInstance;

 @BeforeEach
 public void setup() {
 flowableAdaptor = mock(FlowableAdaptorImpl.class);
 eventPublisher = mock(EventPublisher.class);
 taskService = mock(TaskService.class);
 nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);
 sequenceInstanceRepository = mock(SequenceInstanceRepository.class);
 serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
 runtimeDelegateMapper = mock(RuntimeDelegateMapper.class);

 flowTaskInstance = new FlowTaskInstance(
 TaskType.SERVICE_TASK,
 1L,
 1L,
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL),
 false,
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
 public void testBatchCreate() {
 // Arrange
 List<FlowTaskInstance> flowTaskInstances = Arrays.asList(
 new FlowTaskInstance(
 TaskType.SERVICE_TASK,
 1L,
 1L,
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL),
 false,
 false,
 runtimeDelegateMapper,
 flowableAdaptor,
 eventPublisher,
 taskService,
 nodeInstanceEntityRepository,
 sequenceInstanceRepository,
 serviceTaskInstanceRepository
 ),
 new FlowTaskInstance(
 TaskType.SERVICE_TASK,
 1L,
 1L,
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL),
 false,
 false,
 runtimeDelegateMapper,
 flowableAdaptor,
 eventPublisher,
 taskService,
 nodeInstanceEntityRepository,
 sequenceInstanceRepository,
 serviceTaskInstanceRepository
 )
 );

 // Act
 List<ServiceTaskInstanceEntity> serviceTaskInstanceEntities = flowTaskInstance.batchCreate(
 flowTaskInstances,
 serviceTaskInstanceRepository
 );

 // Assert
 Assertions.assertEquals(2, serviceTaskInstanceEntities.size());
 }

 @Test
 public void testCreate() {
 // Arrange
 when(serviceTaskInstanceRepository.save(any(ServiceTaskInstanceEntity.class)))
 .thenReturn(
 ServiceTaskInstanceEntity.builder()
 .id(1L)
 .createTime(LocalDateTime.now())
 .updateTime(LocalDateTime.now())
 .build()
 );

 // Act
 flowTaskInstance.create();

 // Assert
 Verify.verify(serviceTaskInstanceRepository.save(any(ServiceTaskInstanceEntity.class)));
 }

 @Test
 public void testUpdate() {
 // Arrange
 ServiceTaskInstanceEntity serviceTaskInstanceEntity = ServiceTaskInstanceEntity.builder()
 .id(1L)
 .createTime(LocalDateTime.now())
 .updateTime(LocalDateTime.now())
 .build();

 when(serviceTaskInstanceRepository.update(serviceTaskInstanceEntity))
 .thenReturn(1);

 // Act
 flowTaskInstance.update();

 // Assert
 Verify.verify(serviceTaskInstanceRepository.update(serviceTaskInstanceEntity));
 }

 @Test
 public void testDelete() {
 // Act
 flowTaskInstance.doDelete(1L);

 // Assert
 Verify.verify(serviceTaskInstanceRepository.deleteById(1L));
 }

 @Test
 public void testBindServiceTask() {
 // Arrange
 BaseRuntimeFlowableDelegate<?> baseRuntimeFlowableDelegate = mock(BaseRuntimeFlowableDelegate.class);
 flowTaskInstance.setTargetTaskId(1L);

 // Act
 flowTaskInstance.bindServiceTask(baseRuntimeFlowableDelegate);

 // Assert
 Verify.verifyEquals(flowTaskInstance.getStatus(), FlowNodeStatus.EXECUTING);
 }

 @Test
 public void testIsPresentOnThisMachine() {
 // Arrange
 BaseRuntimeFlowableDelegate<?> baseRuntimeFlowableDelegate = mock(BaseRuntimeFlowableDelegate.class);
 flowTaskInstance.setTargetTaskId(1L);
 flowTaskInstance.setTargetTaskHandle(baseRuntimeFlowableDelegate);

 // Act
 boolean result = flowTaskInstance.isPresentOnThisMachine();

 // Assert
 Verify.verifyEquals(result, true);
 }

 @Test
 public void testDealloc() {
 // Act
 flowTaskInstance.dealloc();

 // Assert
 Verify.verify(eventPublisher.removeEventListener(any(ServiceTaskStartedListener.class)));
 }

 @Test
 public void testAlloc() {
 // Act
 flowTaskInstance.alloc();

 // Assert
 Verify.verify(eventPublisher.addEventListener(any(ServiceTaskStartedListener.class)));
 }

 @Test
 public void testConfirmExecute() {
 // Act
 flowTaskInstance.confirmExecute();

 // Assert
 Verify.verify(flowTaskInstance.complete(false));
 }

 @Test
 public void testAbort() {
 // Act
 flowTaskInstance.abort();

 // Assert
 Verify.verify(flowTaskInstance.complete(true));
 }

 @Test
 public void testCancel() {
 // Arrange
 BaseRuntimeFlowableDelegate<?> baseRuntimeFlowableDelegate = mock(BaseRuntimeFlowableDelegate.class);
 flowTaskInstance.setTargetTaskId(1L);
 flowTaskInstance.setTargetTaskHandle(baseRuntimeFlowableDelegate);

 // Act
 flowTaskInstance.cancel(true);

 // Assert
 Verify.verify(baseRuntimeFlowableDelegate.cancel(true));
 }

 @Test
 public void testIsDone() {
 // Arrange
 BaseRuntimeFlowableDelegate<?> baseRuntimeFlowableDelegate = mock(BaseRuntimeFlowableDelegate.class);
 flowTaskInstance.setTargetTaskId(1L);
 flowTaskInstance.setTargetTaskHandle(baseRuntimeFlowableDelegate);

 when(baseRuntimeFlowableDelegate.isDone()).thenReturn(true);

 // Act
 boolean result = flowTaskInstance.isDone();

 // Assert
 Verify.verifyEquals(result, true);
 }

 @Test
 public void testGetResult() throws InterruptedException, ExecutionException {
 // Arrange
 BaseRuntimeFlowableDelegate<?> baseRuntimeFlowableDelegate = mock(BaseRuntimeFlowableDelegate.class);
 flowTaskInstance.setTargetTaskId(1L);
 flowTaskInstance.setTargetTaskHandle(baseRuntimeFlowableDelegate);

 when(baseRuntimeFlowableDelegate.get()).thenReturn("Result");

 // Act
 Object result = flowTaskInstance.getResult();

 // Assert
 Verify.verifyEquals(result, "Result");
 }

 @Test
 public void testGetResultWithTimeout()
 throws InterruptedException, ExecutionException, TimeoutException {
 // Arrange
 BaseRuntimeFlowableDelegate<?> baseRuntimeFlowableDelegate = mock(BaseRuntimeFlowableDelegate.class);
 flowTaskInstance.setTargetTaskId(1L);
 flowTaskInstance.setTargetTaskHandle(baseRuntimeFlowableDelegate);

 when(baseRuntimeFlowableDelegate.get(5, TimeUnit.SECONDS)).thenReturn("Result");

 // Act
 Object result = flowTaskInstance.getResult(5, TimeUnit.SECONDS);

 // Assert
 Verify.verifyEquals(result, "Result");
 }

 @Test
 public void testResourceType() {
 // Act
 String result = flowTaskInstance.resourceType();

 // Assert
 Verify.verifyEquals(result, "ODC_FLOW_TASK_INSTANCE");
 }

 @Test
 public void testGetTargetDelegateClass() {
 // Act
 Class<? extends BaseRuntimeFlowableDelegate<?>> result = flowTaskInstance.getTargetDelegateClass();

 // Assert
 Verify.verifyNotNull(result);
 }

 @Test
 public void testComplete() {
 // Arrange
 List<FlowableElement> flowableElements = Collections.singletonList(
 FlowableElement.builder()
 .name("UserTask1")
 .build()
 );

 when(flowableAdaptor.getFlowableElementByType(
 flowTaskInstance.getId(),
 FlowNodeType.SERVICE_TASK,
 FlowableElementType.USER_TASK
 )).thenReturn(flowableElements);

 Optional<String> processInstanceIdByFlowInstanceId = Optional.of("ProcessInstance1");
 when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowTaskInstance.getFlowInstanceId()))
 .thenReturn(processInstanceIdByFlowInstanceId);

 List<Task> tasks = Collections.singletonList(
 Task.builder()
 .id("1")
 .name("UserTask1")
 .build()
 );

 when(taskService.createTaskQuery()
 .processInstanceId(processInstanceIdByFlowInstanceId.get())
 .list())
 .thenReturn(tasks);

 // Act
 flowTaskInstance.complete(false);

 // Assert
 Verify.verify(
 flowableAdaptor.getFlowableElementByType(
 flowTaskInstance.getId(),
 FlowNodeType.SERVICE_TASK,
 FlowableElementType.USER_TASK
 )
 );
 Verify.verify(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(flowTaskInstance.getFlowInstanceId()));
 Verify.verify(taskService.createTaskQuery().processInstanceId(processInstanceIdByFlowInstanceId.get()).list());
 Verify.verify(taskService.complete(tasks.get(0).getId(), Collections.singletonMap("aborted", false)));
 }
}