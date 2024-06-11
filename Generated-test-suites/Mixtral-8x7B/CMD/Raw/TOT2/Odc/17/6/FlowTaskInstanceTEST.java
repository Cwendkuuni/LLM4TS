import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowableElementTypeImpl;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.flowable.FlowableElementImpl;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FlowTaskInstanceTest {

 private FlowableAdaptor flowableAdaptor;
 private NodeInstanceEntityRepository nodeInstanceEntityRepository;
 private SequenceInstanceRepository sequenceInstanceRepository;
 private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
 private TaskService taskService;
 private EventPublisher eventPublisher;
 private RuntimeDelegateMapper runtimeDelegateMapper;
 private FlowTaskInstance flowTaskInstance;
 private BaseRuntimeFlowableDelegate baseRuntimeFlowableDelegate;

 @BeforeEach
 public void setup() {
 flowableAdaptor = Mockito.mock(FlowableAdaptor.class);
 nodeInstanceEntityRepository = Mockito.mock(NodeInstanceEntityRepository.class);
 sequenceInstanceRepository = Mockito.mock(SequenceInstanceRepository.class);
 serviceTaskInstanceRepository = Mockito.mock(ServiceTaskInstanceRepository.class);
 taskService = Mockito.mock(TaskService.class);
 eventPublisher = Mockito.mock(EventPublisher.class);
 runtimeDelegateMapper = Mockito.mock(RuntimeDelegateMapper.class);

 flowTaskInstance = new FlowTaskInstance(1L, 2L, FlowableElementTypeImpl.SERVICE_TASK, true, true,
 runtimeDelegateMapper, flowableAdaptor, eventPublisher, taskService,
 nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);

 baseRuntimeFlowableDelegate = Mockito.mock(BaseRuntimeFlowableDelegate.class);
 }

 @Test
 public void testCreate() {
 // Test typical use case
 flowTaskInstance.create();
 Mockito.verify(serviceTaskInstanceRepository, Mockito.times(1)).save(Mockito.any(ServiceTaskInstanceEntity.class));

 // Test edge case: When creating, the entity should be saved with a non-null id
 ServiceTaskInstanceEntity savedEntity = new ServiceTaskInstanceEntity();
 savedEntity.setId(1L);
 Mockito.when(serviceTaskInstanceRepository.save(Mockito.any(ServiceTaskInstanceEntity.class)))
 .thenReturn(savedEntity);
 flowTaskInstance.create();
 Assertions.assertEquals(1L, flowTaskInstance.getId());
 }

 @Test
 public void testUpdate() {
 // Test typical use case
 flowTaskInstance.update();
 Mockito.verify(serviceTaskInstanceRepository, Mockito.times(1)).update(Mockito.any(ServiceTaskInstanceEntity.class));

 // Test edge case: When updating, the entity should be saved with a non-null id
 ServiceTaskInstanceEntity savedEntity = new ServiceTaskInstanceEntity();
 savedEntity.setId(1L);
 Mockito.when(serviceTaskInstanceRepository.update(Mockito.any(ServiceTaskInstanceEntity.class)))
 .thenReturn(savedEntity);
 flowTaskInstance.update();
 Assertions.assertEquals(1L, flowTaskInstance.getId());
 }

 @Test
 public void testDelete() {
 // Test typical use case
 flowTaskInstance.delete(1L);
 Mockito.verify(serviceTaskInstanceRepository, Mockito.times(1)).deleteById(1L);
 }

 @Test
 public void testBindServiceTask() {
 // Test typical use case
 flowTaskInstance.bindServiceTask(baseRuntimeFlowableDelegate);
 Assertions.assertEquals(baseRuntimeFlowableDelegate, flowTaskInstance.getTargetTaskHandle());
 }

 @Test
 public void testConfirmExecute() {
 // Test typical use case
 flowTaskInstance.confirmExecute();

 // Verify the expected behavior for the complete method
 Mockito.verify(flowableAdaptor, Mockito.times(1)).getFlowableElementByType(
 Mockito.anyLong(), Mockito.eq(FlowableElementTypeImpl.SERVICE_TASK),
 Mockito.eq(FlowableElementTypeImpl.USER_TASK));

 Mockito.verify(taskService, Mockito.times(1)).createTaskQuery()
 .processInstanceId(Mockito.anyString());
 Mockito.verify(taskService, Mockito.times(1)).complete(Mockito.anyString(), Mockito.anyMap());
 }

 @Test
 public void testAbort() {
 // Test typical use case
 flowTaskInstance.abort();

 // Verify the expected behavior for the complete method
 Mockito.verify(flowableAdaptor, Mockito.times(1)).getFlowableElementByType(
 Mockito.anyLong(), Mockito.eq(FlowableElementTypeImpl.SERVICE_TASK),
 Mockito.eq(FlowableElementTypeImpl.USER_TASK));

 Mockito.verify(taskService, Mockito.times(1)).createTaskQuery()
 .processInstanceId(Mockito.anyString());

 Map<String, Object> variables = new HashMap<>();
 variables.put(FlowTaskInstance.ABORT_VARIABLE_NAME, true);
 Mockito.verify(taskService, Mockito.times(1)).complete(Mockito.anyString(), Mockito.eq(variables));
 }

 @Test
 public void testGetTargetDelegateClass() {
 // Test typical use case
 Mockito.when(runtimeDelegateMapper.map(TaskType.SERVICE_TASK))
 .thenReturn(BaseRuntimeFlowableDelegate.class);
 Assertions.assertEquals(BaseRuntimeFlowableDelegate.class,
 flowTaskInstance.getTargetDelegateClass());
 }

}