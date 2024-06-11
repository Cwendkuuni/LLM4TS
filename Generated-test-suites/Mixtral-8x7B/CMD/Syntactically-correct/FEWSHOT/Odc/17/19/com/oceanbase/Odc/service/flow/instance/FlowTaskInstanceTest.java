import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorTest;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedEvent;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

@RunWith(MockitoJUnitRunner.class)
public class FlowTaskInstanceTest {

 @Mock
 private TaskService taskService;

 @Mock
 private ServiceTaskInstanceRepository serviceTaskRepository;

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

 @InjectMocks
 private FlowTaskInstance flowTaskInstance;

 private TaskInfo taskInfo;

 @Before
 public void setup() {
 taskInfo = mock(TaskInfo.class);
 when(taskInfo.getName()).thenReturn("Test Task");
 }

 @Test
 public void testBatchCreate_emptyList() {
 List<FlowTaskInstance> instances = Collections.emptyList();
 List<ServiceTaskInstanceEntity> entities = FlowTaskInstance.batchCreate(instances,
 serviceTaskRepository);

 verify(serviceTaskRepository, times(0)).batchCreate(any());
 }

 @Test
 public void testBatchCreate_nonEmptyList() {
 List<FlowTaskInstance> instances = Collections.singletonList(createFlowTaskInstance());
 List<ServiceTaskInstanceEntity> entities = FlowTaskInstance.batchCreate(instances,
 serviceTaskRepository);

 verify(serviceTaskRepository, times(1)).batchCreate(any());
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

 verify(serviceTaskRepository, times(1)).deleteById(anyLong());
 }

 private FlowTaskInstance createFlowTaskInstance() {
 return new FlowTaskInstance(1L, 1L, ExecutionStrategyConfig.builder().build(), true, true,
 flowableAdaptor, nodeRepository, sequenceRepository, serviceTaskRepository);
 }
}