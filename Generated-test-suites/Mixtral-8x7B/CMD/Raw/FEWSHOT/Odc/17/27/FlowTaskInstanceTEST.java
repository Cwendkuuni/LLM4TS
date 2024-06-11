import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
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
import org.springframework.test.util.ReflectionTestUtils;

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
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;

@RunWith(MockitoJUnitRunner.class)
public class FlowTaskInstanceTest {

 @Mock
 private TaskService taskService;

 @Mock
 private EventPublisher eventPublisher;

 @Mock
 private NodeInstanceEntityRepository nodeInstanceEntityRepository;

 @Mock
 private SequenceInstanceRepository sequenceInstanceRepository;

 @Mock
 private ServiceTaskInstanceRepository serviceTaskInstanceRepository;

 @Mock
 private FlowableAdaptor flowableAdaptor;

 @Mock
 private RuntimeDelegateMapper delegateConvertor;

 @InjectMocks
 private FlowTaskInstance flowTaskInstance;

 @Before
 public void setup() {
 ReflectionTestUtils.setField(flowTaskInstance, "id", 1L);
 ReflectionTestUtils.setField(flowTaskInstance, "createTime", 1500000000L);
 ReflectionTestUtils.setField(flowTaskInstance, "updateTime", 1500000000L);
 ReflectionTestUtils.setField(flowTaskInstance, "flowInstanceId", 100L);
 ReflectionTestUtils.setField(flowTaskInstance, "organizationId", 10L);
 ReflectionTestUtils.setField(flowTaskInstance, "status", FlowNodeStatus.PENDING);
 ReflectionTestUtils.setField(flowTaskInstance, "targetTaskId", null);
 ReflectionTestUtils.setField(flowTaskInstance, "strategyConfig",
 new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 60, 10));
 ReflectionTestUtils.setField(flowTaskInstance, "taskType", TaskType.COMPENSATE);
 }

 @Test
 public void testBatchCreate() {
 List<FlowTaskInstance> instances = new ArrayList<>();
 instances.add(flowTaskInstance);
 List<ServiceTaskInstanceEntity> entities =
 Collections.singletonList(new ServiceTaskInstanceEntity());
 when(serviceTaskInstanceRepository.batchCreate(anyList())).thenReturn(entities);
 FlowTaskInstance.batchCreate(instances, serviceTaskInstanceRepository);
 verify(serviceTaskInstanceRepository, times(1)).batchCreate(instances);
 }

 @Test
 public void testCreate() {
 flowTaskInstance.create();
 verify(serviceTaskInstanceRepository, times(1)).save(any(ServiceTaskInstanceEntity.class));
 }

 @Test
 public void testUpdate() {
 flowTaskInstance.update();
 verify(serviceTaskInstanceRepository, times(1)).update(any(ServiceTaskInstanceEntity.class));
 }

 @Test
 public void testDelete() {
 flowTaskInstance.delete(1L);
 verify(serviceTaskInstanceRepository, times(1)).deleteById(1L);
 }

 @Test
 public void testBindServiceTask() {
 BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
 flowTaskInstance.bindServiceTask(target);
 verify(target, times(1)).setTargetTaskInstanceId(1L);
 }

 @Test
 public void testIsPresentOnThisMachine() {
 assertTrue(flowTaskInstance.isPresentOnThisMachine());
 }

 @Test
 public void testDealloc() {
 flowTaskInstance.dealloc();
 verify(eventPublisher, times(1))
 .removeEventListener(any(ServiceTaskStartedListener.class));
 }

 @Test
 public void testAlloc() {
 flowTaskInstance.alloc();
 verify(eventPublisher, times(1)).addEventListener(any(ServiceTaskStartedListener.class));
 }

 @Test
 public void testComplete() {
 flowTaskInstance.complete(false);
 verify(flowableAdaptor, times(1)).getFlowableElementByType(
 anyLong(), any(FlowNodeType.class), any(FlowableElementType.class));
 verify(flowableAdaptor, times(1)).getProcessInstanceIdByFlowInstanceId(anyLong());
 verify(taskService, times(1)).createTaskQuery()
 .processInstanceId(anyString()).list();
 }

 @Test
 public void testMapToTaskEntity() {
 ServiceTaskInstanceEntity entity =
 FlowTaskInstance.mapToTaskEntity(flowTaskInstance);
 assertNotNull(entity);
 }
}