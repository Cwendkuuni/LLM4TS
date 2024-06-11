import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowableElementType.FlowNodeType;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedEvent;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

public class FlowTaskInstanceTest {

	@Mock
	private ServiceTaskInstanceRepository serviceTaskRepository;

	@Mock
	private NodeInstanceEntityRepository nodeRepository;

	@Mock
	private SequenceInstanceRepository sequenceRepository;

	@Mock
	private FlowableAdaptor flowableAdaptor;

	@Mock
	private TaskService taskService;

	@Mock
	private EventPublisher eventPublisher;

	@Mock
	private RuntimeDelegateMapper delegateConvertor;

	@InjectMocks
	private FlowTaskInstance flowTaskInstance;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testConstructorWithEntity() {
		ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
		entity.setId(1L);
		entity.setOrganizationId(1L);
		entity.setStatus(FlowNodeStatus.PENDING);
		entity.setFlowInstanceId(1L);
		entity.setCreateTime(1L);
		entity.setUpdateTime(1L);
		entity.setStartEndpoint(true);
		entity.setEndEndpoint(false);
		entity.setTargetTaskId(1L);
		entity.setTaskType(TaskType.TASK_TYPE_1);
		entity.setStrategy(FlowTaskExecutionStrategy.AUTO);

		flowTaskInstance = new FlowTaskInstance(entity, delegateConvertor, flowableAdaptor, eventPublisher,
				taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

		assertEquals(1L, flowTaskInstance.getId().longValue());
		assertEquals(1L, flowTaskInstance.getOrganizationId().longValue());
		assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
		assertEquals(1L, flowTaskInstance.getFlowInstanceId().longValue());
		assertEquals(1L, flowTaskInstance.getCreateTime().longValue());
		assertEquals(1L, flowTaskInstance.getUpdateTime().longValue());
		assertTrue(flowTaskInstance.isStartEndpoint());
		assertFalse(flowTaskInstance.isEndEndPoint());
		assertEquals(1L, flowTaskInstance.getTargetTaskId().longValue());
		assertEquals(TaskType.TASK_TYPE_1, flowTaskInstance.getTaskType());
		assertEquals(FlowTaskExecutionStrategy.AUTO, flowTaskInstance.getStrategyConfig().getStrategy());
	}

	@Test
	public void testConstructorWithTaskType() {
		flowTaskInstance = new FlowTaskInstance(TaskType.TASK_TYPE_1, 1L, 1L, new ExecutionStrategyConfig(), true,
				false, delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository,
				sequenceRepository, serviceTaskRepository);

		assertEquals(0L, flowTaskInstance.getId().longValue());
		assertEquals(1L, flowTaskInstance.getOrganizationId().longValue());
		assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
		assertEquals(1L, flowTaskInstance.getFlowInstanceId().longValue());
		assertEquals(0L, flowTaskInstance.getCreateTime().longValue());
		assertEquals(0L, flowTaskInstance.getUpdateTime().longValue());
		assertTrue(flowTaskInstance.isStartEndpoint());
		assertFalse(flowTaskInstance.isEndEndPoint());
		assertNull(flowTaskInstance.getTargetTaskId());
		assertEquals(TaskType.TASK_TYPE_1, flowTaskInstance.getTaskType());
		assertEquals(FlowTaskExecutionStrategy.AUTO, flowTaskInstance.getStrategyConfig().getStrategy());
	}

	@Test
	public void testGetCoreFlowableElementType() {
		assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
	}

	@Test
	public void testBatchCreate() {
		List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
		List<ServiceTaskInstanceEntity> entities = Collections.singletonList(new ServiceTaskInstanceEntity());

		when(serviceTaskRepository.batchCreate(instances.stream().map(FlowTaskInstance::mapToTaskEntity)
				.collect(Collectors.toList()))).thenReturn(entities);

		assertEquals(entities, FlowTaskInstance.batchCreate(instances, serviceTaskRepository));
	}

	@Test
	public void testCreate() {
		ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
		entity.setId(1L);
		entity.setOrganizationId(1L);
		entity.setStatus(FlowNodeStatus.PENDING);
		entity.setFlowInstanceId(1L);
		entity.setCreateTime(1L);
		entity.setUpdateTime(1L);
		entity.setStartEndpoint(true);
		entity.setEndEndpoint(false);
		entity.setTargetTaskId(1L);
		entity.setTaskType(TaskType.TASK_TYPE_1);

		when(serviceTaskRepository.save(entity)).thenReturn(entity);

		flowTaskInstance.create();

		verify(serviceTaskRepository).save(entity);
		assertEquals(entity.getId(), flowTaskInstance.getId());
		assertEquals(entity.getCreateTime(), flowTaskInstance.getCreateTime());
		assertEquals(entity.getUpdateTime(), flowTaskInstance.getUpdateTime());
	}

	@Test
	public void testUpdate() {
		ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
		entity.setId(1L);
		entity.setStatus(FlowNodeStatus.EXECUTING);
		entity.setTargetTaskId(1L);

		when(serviceTaskRepository.update(entity)).thenReturn(1);

		flowTaskInstance.update();

		verify(serviceTaskRepository).update(entity);
	}

	@Test
	public void testDelete() {
		doNothing().when(serviceTaskRepository).deleteById(1L);

		flowTaskInstance.delete(1L);

		verify(serviceTaskRepository).deleteById(1L);
	}

	@Test
	public void testBindServiceTask() {
		BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);

		flowTaskInstance.bindServiceTask(target);

		verify(target).setTargetTaskInstanceId(flowTaskInstance.getId());
		assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
	}

	@Test
	public void testIsPresentOnThisMachine() {
		BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);

		when(target.getTargetTaskInstanceId()).thenReturn(1L);

		assertTrue(flowTaskInstance.isPresentOnThisMachine(target));

		when(target.getTargetTaskInstanceId()).thenReturn(2L);

		assertFalse(flowTaskInstance.isPresentOnThisMachine(target));
	}

	@Test
	public void testDealloc() {
		ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);

		flowTaskInstance.listener = listener;

		flowTaskInstance.dealloc();

		verify(eventPublisher).removeEventListener(listener);
		assertNull(flowTaskInstance.listener);
	}

	@Test
	public void testAlloc() {
		ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);

		flowTaskInstance.alloc();

		verify(eventPublisher).addEventListener(listener);
		assertEquals(listener, flowTaskInstance.listener);
	}

	@Test
	public void testComplete() {
		List<FlowableElement> elements = Collections.singletonList(
				new FlowableElement(1L, 1L, "task1", FlowNodeType.USER_TASK));

		when(flowableAdaptor.getFlowableElementByType(1L, FlowNodeType.SERVICE_TASK, FlowableElementType.USER_TASK))
				.thenReturn(elements);
		when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(1L)).thenReturn(Optional.of("1"));
		when(taskService.createTaskQuery().processInstanceId("1").list()).thenReturn(
				Collections.singletonList(new TaskInfo("task1", "1")));

		flowTaskInstance.complete(false);

		verify(taskService).complete(anyString(), anyMap());
	}

	@Test
	public void testMapToTaskEntity() {
		ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
		entity.setOrganizationId(1L);
		entity.setStatus(FlowNodeStatus.PENDING);
		entity.setFlowInstanceId(1L);
		entity.setStartEndpoint(true);
		entity.setEndEndpoint(false);
		entity.setStrategy(FlowTaskExecutionStrategy.AUTO);
		entity.setTaskType(TaskType.TASK_TYPE_1);

		assertEquals(entity, FlowTaskInstance.mapToTaskEntity(flowTaskInstance));
	}

	@Test
	public void testGetTargetDelegateClass() {
		when(delegateConvertor.map(TaskType.TASK_TYPE_1)).thenReturn(DummyDelegate.class);

		assertEquals(DummyDelegate.class, flowTaskInstance.getTargetDelegateClass());
	}

	private static class DummyDelegate extends BaseRuntimeFlowableDelegate<Object> {

		@Override
		public Object execute(Map<String, Object> variables) throws Exception {
			return null;
		}

	}

}