package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.reflect.FieldSetter;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedEvent;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

public class FlowTaskInstanceTest {

 private FlowTaskInstance flowTaskInstance;
 private ServiceTaskInstanceEntity serviceTaskInstanceEntity;
 private TaskService taskService;
 private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
 private EventPublisher eventPublisher;
 private NodeInstanceEntityRepository nodeInstanceEntityRepository;
 private SequenceInstanceRepository sequenceInstanceRepository;
 private FlowableAdaptor flowableAdaptor;
 private RuntimeDelegateMapper runtimeDelegateMapper;
 private ExecutionStrategyConfig executionStrategyConfig;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);

 serviceTaskInstanceEntity = new ServiceTaskInstanceEntity();
 serviceTaskInstanceEntity.setId(1L);
 serviceTaskInstanceEntity.setOrganizationId(1L);
 serviceTaskInstanceEntity.setStatus(FlowNodeStatus.PENDING);
 serviceTaskInstanceEntity.setFlowInstanceId(1L);
 serviceTaskInstanceEntity.setStartEndpoint(false);
 serviceTaskInstanceEntity.setEndEndpoint(false);
 serviceTaskInstanceEntity.setStrategy(FlowTaskExecutionStrategy.MANUAL);
 serviceTaskInstanceEntity.setTaskType(TaskType.ODC_SERVICE_TASK);
 serviceTaskInstanceEntity.setTargetTaskId(null);

 taskService = mock(TaskService.class);
 serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
 eventPublisher = mock(EventPublisher.class);
 nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);
 sequenceInstanceRepository = mock(SequenceInstanceRepository.class);
 flowableAdaptor = mock(FlowableAdaptor.class);
 runtimeDelegateMapper = mock(RuntimeDelegateMapper.class);

 executionStrategyConfig = ExecutionStrategyConfig.builder()
 .strategy(FlowTaskExecutionStrategy.MANUAL)
 .pendingExpireIntervalSeconds(10)
 .executionTime(5)
 .build();

 FlowableElement flowableElement = new FlowableElement();
 flowableElement.setName("test");
 flowableElement.setType(FlowableElementType.USER_TASK);

 when(flowableAdaptor.getFlowableElementByType(1L, FlowNodeType.SERVICE_TASK, FlowableElementType.USER_TASK))
 .thenReturn(Collections.singletonList(flowableElement));

 FieldSetter.setField(verify, "flowableAdaptor", flowableAdaptor);
 FieldSetter.setField(verify, "serviceTaskInstanceRepository", serviceTaskInstanceRepository);
 FieldSetter.setField(verify, "eventPublisher", eventPublisher);
 FieldSetter.setField(verify, "nodeInstanceEntityRepository", nodeInstanceEntityRepository);
 FieldSetter.setField(verify, "sequenceInstanceRepository", sequenceInstanceRepository);
 FieldSetter.setField(verify, "executionStrategyConfig", executionStrategyConfig);
 FieldSetter.setField(verify, "runtimeDelegateMapper", runtimeDelegateMapper);

 flowTaskInstance = new FlowTaskInstance(serviceTaskInstanceEntity, runtimeDelegateMapper, flowableAdaptor,
 eventPublisher, taskService, nodeInstanceEntityRepository, sequenceInstanceRepository,
 serviceTaskInstanceRepository);
 }

 @Test
 public void testFlowTaskInstanceCreation() {
 assertEquals(1L, flowTaskInstance.getId().longValue());
 assertEquals(1L, flowTaskInstance.getOrganizationId().longValue());
 assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
 assertEquals(1L, flowTaskInstance.getFlowInstanceId().longValue());
 assertEquals(false, flowTaskInstance.isStartEndpoint());
 assertEquals(false, flowTaskInstance.isEndEndPoint());
 assertEquals(FlowTaskExecutionStrategy.MANUAL, flowTaskInstance.getStrategyConfig().getStrategy());
 assertEquals(TaskType.ODC_SERVICE_TASK, flowTaskInstance.getTaskType());
 assertEquals(null, flowTaskInstance.getTargetTaskId());
 }

 @Test
 public void testBatchCreate() {
 List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);

 when(serviceTaskInstanceRepository.batchCreate(Collections.singletonList(serviceTaskInstanceEntity)))
 .thenReturn(Collections.singletonList(serviceTaskInstanceEntity));

 List<FlowTaskInstance> result =
 FlowTaskInstance.batchCreate(instances, serviceTaskInstanceRepository);

 assertEquals(instances, result);
 }

 @Test
 public void testCreate() {
 when(serviceTaskInstanceRepository.save(serviceTaskInstanceEntity)).thenReturn(serviceTaskInstanceEntity);

 flowTaskInstance.create();

 assertEquals(1L, flowTaskInstance.getId().longValue());
 Verify.notNull(flowTaskInstance.getCreateTime(), "CreateTime");
 Verify.notNull(flowTaskInstance.getUpdateTime(), "UpdateTime");
 }

 @Test
 public void testUpdate() {
 ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
 entity.setId(1L);
 entity.setStatus(FlowNodeStatus.EXECUTING);
 entity.setTargetTaskId(2L);
 entity.setTaskType(TaskType.ODC_SERVICE_TASK);

 when(serviceTaskInstanceRepository.update(entity)).thenReturn(1);

 flowTaskInstance.setStatus(FlowNodeStatus.EXECUTING);
 flowTaskInstance.setTargetTaskId(2L);
 flowTaskInstance.update();

 assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
 assertEquals(2L, flowTaskInstance.getTargetTaskId().longValue());
 }

 @Test
 public void testDelete() {
 serviceTaskInstanceRepository.deleteById(1L);

 flowTaskInstance.delete(1L);

 assertEquals(1L, flowTaskInstance.getId().longValue());
 }

 @Test
 public void testBindServiceTask() {
 BaseRuntimeFlowableDelegate<?> targetTask = mock(BaseRuntimeFlowableDelegate.class);

 flowTaskInstance.bindServiceTask(targetTask);

 assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
 assertEquals(targetTask, flowTaskInstance.getTargetTaskHandle());
 }

 @Test
 public void testDealloc() {
 flowTaskInstance.dealloc();

 assertNull(flowTaskInstance.getListener());
 }

 @Test
 public void testAlloc() {
 flowTaskInstance.alloc();

 assertNotNull(flowTaskInstance.getListener());
 }

 @Test
 public void testComplete() {
 flowTaskInstance.complete(false);

 // Add assertions based on your logic after completing the task
 }

 @Test
 public void testAbort() {
 flowTaskInstance.abort();

 // Add assertions based on your logic after aborting the task
 }

 @Test
 public void testCancel() {
 flowTaskInstance.cancel(true);

 // Add assertions based on your logic after cancelling the task
 }

 @Test
 public void testIsDone() {
 BaseRuntimeFlowableDelegate<?> targetTask = mock(BaseRuntimeFlowableDelegate.class);

 when(targetTask.isDone()).thenReturn(true);

 flowTaskInstance.setTargetTaskHandle(targetTask);

 assertTrue(flowTaskInstance.isDone());
 }

 @Test
 public void testGetResult() throws InterruptedException, ExecutionException {
 BaseRuntimeFlowableDelegate<?> targetTask = mock(BaseRuntimeFlowableDelegate.class);

 when(targetTask.get()).thenReturn("Result");

 flowTaskInstance.setTargetTaskHandle(targetTask);

 assertEquals("Result", flowTaskInstance.getResult());
 }

 @Test
 public void testGetResultWithTimeout()
 throws InterruptedException, ExecutionException, TimeoutException {
 BaseRuntimeFlowableDelegate<?> targetTask = mock(BaseRuntimeFlowableDelegate.class);

 when(targetTask.get(1, TimeUnit.SECONDS)).thenReturn("Result");

 flowTaskInstance.setTargetTaskHandle(targetTask);

 assertEquals("Result", flowTaskInstance.getResult(1, TimeUnit.SECONDS));
 }
}