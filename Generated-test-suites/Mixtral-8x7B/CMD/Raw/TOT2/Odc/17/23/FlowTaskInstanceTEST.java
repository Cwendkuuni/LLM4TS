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
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FlowTaskInstanceTest {

 @Mock
 private TaskService taskService;

 @Mock
 private NodeInstanceEntityRepository nodeInstanceEntityRepository;

 @Mock
 private SequenceInstanceRepository sequenceInstanceRepository;

 @Mock
 private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

 @Mock
 private EventPublisher eventPublisher;

 @Mock
 private FlowableAdaptor flowableAdaptor;

 @InjectMocks
 private FlowTaskInstance flowTaskInstance;

 @Mock
 private RuntimeDelegateMapper runtimeDelegateMapper;

 @BeforeEach
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 }

 // Test for constructor with ServiceTaskInstanceEntity
 @Test
 public void testConstructorWithEntity() {
 ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
 entity.setId(1L);
 entity.setOrganizationId(1L);
 entity.setStatus(FlowNodeStatus.PENDING);
 entity.setFlowInstanceId(1L);
 entity.setCreateTime(LocalDateTime.now());
 entity.setUpdateTime(LocalDateTime.now());
 entity.setStartEndpoint(true);
 entity.setEndEndpoint(true);
 entity.setTaskType(TaskType.SERVICE_TASK);

 flowTaskInstance = new FlowTaskInstance(entity, runtimeDelegateMapper, new FlowableAdaptorImpl(), eventPublisher,
 taskService, nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);

 Assertions.assertEquals(1L, flowTaskInstance.getId());
 Assertions.assertEquals(1L, flowTaskInstance.getOrganizationId());
 Assertions.assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
 Assertions.assertEquals(1L, flowTaskInstance.getFlowInstanceId());
 Assertions.assertNotNull(flowTaskInstance.getCreateTime());
 Assertions.assertNotNull(flowTaskInstance.getUpdateTime());
 Assertions.assertTrue(flowTaskInstance.isStartEndpoint());
 Assertions.assertTrue(flowTaskInstance.isEndEndPoint());
 Assertions.assertEquals(TaskType.SERVICE_TASK, flowTaskInstance.getTaskType());
 }

 // Test for constructor with TaskType
 @Test
 public void testConstructorWithTaskType() {
 flowTaskInstance = new FlowTaskInstance(TaskType.SERVICE_TASK, 1L, 1L, new ExecutionStrategyConfig(), true, true,
 runtimeDelegateMapper, new FlowableAdaptorImpl(), eventPublisher, taskService,
 nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);

 Assertions.assertNotNull(flowTaskInstance.getId());
 Assertions.assertEquals(1L, flowTaskInstance.getOrganizationId());
 Assertions.assertEquals(1L, flowTaskInstance.getFlowInstanceId());
 Assertions.assertEquals(TaskType.SERVICE_TASK, flowTaskInstance.getTaskType());
 Assertions.assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
 Assertions.assertTrue(flowTaskInstance.isStartEndpoint());
 Assertions.assertTrue(flowTaskInstance.isEndEndPoint());
 }

 // Test for getCoreFlowableElementType
 @Test
 public void testGetCoreFlowableElementType() {
 Assertions.assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
 }

 // Test for batchCreate
 @Test
 public void testBatchCreate() {
 }

 // Test for create
 @Test
 public void testCreate() {
 ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
 entity.setId(1L);
 entity.setOrganizationId(1L);
 entity.setStatus(FlowNodeStatus.PENDING);
 entity.setFlowInstanceId(1L);
 entity.setCreateTime(LocalDateTime.now());
 entity.setUpdateTime(LocalDateTime.now());
 entity.setStartEndpoint(true);
 entity.setEndEndpoint(true);
 entity.setTaskType(TaskType.SERVICE_TASK);

 Mockito.when(serviceTaskInstanceRepository.save(entity)).thenReturn(entity);

 flowTaskInstance.create();

 Mockito.verify(serviceTaskInstanceRepository, Mockito.times(1)).save(entity);
 }

 // Test for update
 @Test
 public void testUpdate() {
 ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
 entity.setId(1L);
 entity.setOrganizationId(1L);
 entity.setStatus(FlowNodeStatus.PENDING);
 entity.setFlowInstanceId(1L);
 entity.setCreateTime(LocalDateTime.now());
 entity.setUpdateTime(LocalDateTime.now());
 entity.setStartEndpoint(true);
 entity.setEndEndpoint(true);
 entity.setTaskType(TaskType.SERVICE_TASK);

 flowTaskInstance.setId(1L);
 flowTaskInstance.setStatus(FlowNodeStatus.EXECUTING);

 Mockito.when(serviceTaskInstanceRepository.update(entity)).thenReturn(1);

 flowTaskInstance.update();

 Mockito.verify(serviceTaskInstanceRepository, Mockito.times(1)).update(entity);
 }

 // Test for doDelete
 @Test
 public void testDoDelete() {
 flowTaskInstance.setId(1L);

 Mockito.doNothing().when(serviceTaskInstanceRepository).deleteById(1L);

 flowTaskInstance.doDelete(1L);

 Mockito.verify(serviceTaskInstanceRepository, Mockito.times(1)).deleteById(1L);
 }

 // Test for bindServiceTask
 @Test
 public void testBindServiceTask() {
 }

 // Test for isPresentOnThisMachine
 @Test
 public void testIsPresentOnThisMachine() {
 Assertions.assertFalse(flowTaskInstance.isPresentOnThisMachine());

 flowTaskInstance.targetTaskHandle = Mockito.mock(BaseRuntimeFlowableDelegate.class);

 Assertions.assertTrue(flowTaskInstance.isPresentOnThisMachine());
 }

 // Test for dealloc
 @Test
 public void testDealloc() {
 flowTaskInstance.listener = Mockito.mock(ServiceTaskStartedListener.class);

 flowTaskInstance.dealloc();

 Mockito.verify(eventPublisher, Mockito.times(1)).removeEventListener(flowTaskInstance.listener);
 }

 // Test for alloc
 @Test
 public void testAlloc() {
 flowTaskInstance.listener = Mockito.mock(ServiceTaskStartedListener.class);

 flowTaskInstance.alloc();

 Mockito.verify(eventPublisher, Mockito.times(1)).addEventListener(flowTaskInstance.listener);
 }

 // Test for complete
 @Test
 public void testComplete() {
 flowTaskInstance.strategyConfig = new ExecutionStrategyConfig();
 flowTaskInstance.strategyConfig.setStrategy(FlowTaskExecutionStrategy.MANUAL);

 Mockito.when(flowableAdaptor.getFlowableElementByType(Mockito.anyLong(), Mockito.any(), Mockito.any()))
 .thenReturn(Arrays.asList(Mockito.mock(FlowableElement.class)));

 Mockito.when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(Mockito.anyLong()))
 .thenReturn(Optional.of("processInstanceId"));

 Mockito.when(taskService.createTaskQuery().processInstanceId(Mockito.anyString()).list())
 .thenReturn(Collections.singletonList(Mockito.mock(Task.class)));

 flowTaskInstance.complete(true);

 Mockito.verify(taskService, Mockito.times(1)).complete(Mockito.anyString(), Mockito.anyMap());
 }

 // Test for confirmExecute
 @Test
 public void testConfirmExecute() {
 flowTaskInstance.complete(false);
 }

 // Test for abort
 @Test
 public void testAbort() {
 flowTaskInstance.complete(true);
 }

 // Test for cancel
 @Test
 public void testCancel() {
 flowTaskInstance.targetTaskHandle = Mockito.mock(BaseRuntimeFlowableDelegate.class);

 flowTaskInstance.cancel(true);

 Mockito.verify(flowTaskInstance.targetTaskHandle, Mockito.times(1)).cancel(true);
 }

 // Test for isDone
 @Test
 public void testIsDone() {
 flowTaskInstance.targetTaskHandle = Mockito.mock(BaseRuntimeFlowableDelegate.class);

 Mockito.when(flowTaskInstance.targetTaskHandle.isDone()).thenReturn(true);

 Assertions.assertTrue(flowTaskInstance.isDone());
 }

 // Test for getResult
 @Test
 public void testGetResult() throws InterruptedException, ExecutionException {
 flowTaskInstance.targetTaskHandle = Mockito.mock(BaseRuntimeFlowableDelegate.class);

 Mockito.when(flowTaskInstance.targetTaskHandle.get()).thenReturn("result");

 Assertions.assertEquals("result", flowTaskInstance.getResult());
 }

 // Test for getResult with timeout
 @Test
 public void testGetResultWithTimeout()
 throws InterruptedException, ExecutionException, TimeoutException {
 flowTaskInstance.targetTaskHandle = Mockito.mock(BaseRuntimeFlowableDelegate.class);

 Mockito.when(flowTaskInstance.targetTaskHandle.get(1, TimeUnit.SECONDS)).thenReturn("result");

 Assertions.assertEquals("result", flowTaskInstance.getResult(1, TimeUnit.SECONDS));
 }

 // Test for resourceType
 @Test
 public void testResourceType() {
 Assertions.assertEquals("ODC_FLOW_TASK_INSTANCE", flowTaskInstance.resourceType());
 }

 // Test for getTargetDelegateClass
 @Test
 public void testGetTargetDelegateClass() {
 }
}