import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowableElementUtils;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowNodeStatus;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedEvent;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FlowTaskInstanceTest {

	@Mock
	private TaskService taskService;

	@Mock
	private ServiceTaskInstanceRepository serviceTaskRepository;

	@Mock
	private EventPublisher eventPublisher;

	@Mock
	private NodeInstanceEntityRepository nodeRepository;

	@Mock
	private SequenceInstanceRepository sequenceRepository;

	@Mock
	private FlowableAdaptor flowableAdaptor;

	@Mock
	private RuntimeDelegateMapper delegateConvertor;

	@InjectMocks
	@Getter
	private FlowTaskInstance flowTaskInstance;

	@Before
	public void init() {
		MockitoAnnotations.openMocks(this);
		flowTaskInstance = new FlowTaskInstance(TaskType.AUTH_CHECK, 1L, 1L,
				new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 10, 1), false, false,
				delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
				serviceTaskRepository);
	}

	@Test
	public void testConstructor() {
		assertNotNull(flowTaskInstance);
		assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
	}

	@Test
	public void testGetCoreFlowableElementType() {
		assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
	}

	@Test
	public void testBatchCreate() {
		List<FlowTaskInstance> instances = new ArrayList<>();
		instances.add(flowTaskInstance);
		List<ServiceTaskInstanceEntity> entities = new ArrayList<>();
		entities.add(flowTaskInstance.mapToTaskEntity(flowTaskInstance));
		when(serviceTaskRepository.batchCreate(entities)).thenReturn(entities);

		List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
		assertEquals(instances, result);
		verify(serviceTaskRepository).batchCreate(entities);
	}

	@Test
	public void testCreate() {
		ServiceTaskInstanceEntity entity = flowTaskInstance.mapToTaskEntity(flowTaskInstance);
		when(serviceTaskRepository.save(entity)).thenReturn(entity);

		flowTaskInstance.create();

		verify(serviceTaskRepository).save(entity);
		assertEquals(entity.getId(), flowTaskInstance.getId());
		assertEquals(entity.getCreateTime(), flowTaskInstance.getCreateTime());
		assertEquals(entity.getUpdateTime(), flowTaskInstance.getUpdateTime());
	}

	@Test
	public void testUpdate() {
		ServiceTaskInstanceEntity entity = flowTaskInstance.mapToTaskEntity(flowTaskInstance);
		entity.setStatus(FlowNodeStatus.EXECUTING);
		int affectRows = 1;
		when(serviceTaskRepository.update(entity)).thenReturn(affectRows);

		flowTaskInstance.update();

		verify(serviceTaskRepository).update(entity);
	}

	@Test
	public void testDelete() {
		Long instanceId = 1L;
		flowTaskInstance.setId(instanceId);
		flowTaskInstance.delete(instanceId);

		verify(serviceTaskRepository).deleteById(instanceId);
	}

	@Test
	public void testBindServiceTask() {
		BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
		flowTaskInstance.bindServiceTask(target);

		assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
		assertEquals(target, flowTaskInstance.getTargetTaskHandle());
	}

	@Test
	public void testIsPresentOnThisMachine() {
		assertFalse(flowTaskInstance.isPresentOnThisMachine());
		flowTaskInstance.setTargetTaskHandle(mock(BaseRuntimeFlowableDelegate.class));
		assertTrue(flowTaskInstance.isPresentOnThisMachine());
	}

	@Test
	public void testDealloc() {
		ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);
		flowTaskInstance.setListener(listener);
		flowTaskInstance.dealloc();

		verify(eventPublisher).removeEventListener(listener);
		assertNull(flowTaskInstance.getListener());
	}

	@Test
	public void testAlloc() {
		ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);
		flowTaskInstance.setListener(null);
		flowTaskInstance.alloc();

		verify(eventPublisher).addEventListener(listener);
		assertEquals(listener, flowTaskInstance.getListener());
	}

	@Test
	public void testConfirmExecute() {
		flowTaskInstance.confirmExecute();

		verify(flowTaskInstance.getTargetTaskHandle()).complete(false);
	}

	@Test
	public void testAbort() {
		flowTaskInstance.abort();

		verify(flowTaskInstance.getTargetTaskHandle()).complete(true);
	}

	@Test
	public void testCancel() throws ExecutionException, InterruptedException {
		BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
		when(target.isDone()).thenReturn(false);
		flowTaskInstance.setTargetTaskHandle(target);

		flowTaskInstance.cancel(true);

		verify(target).cancel(true);
	}

	@Test
	public void testIsDone() throws ExecutionException, InterruptedException {
		BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
		when(target.isDone()).thenReturn(true);
		flowTaskInstance.setTargetTaskHandle(target);

		assertTrue(flowTaskInstance.isDone());
	}

	@Test
	public void testGetResult() throws ExecutionException, InterruptedException {
		BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
		when(target.get()).thenReturn("result");
		flowTaskInstance.setTargetTaskHandle(target);

		assertEquals("result", flowTaskInstance.getResult());
	}

	@Test
	public void testGetResultWithTimeout()
			throws ExecutionException, InterruptedException, TimeoutException {
		BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
		when(target.get(1, TimeUnit.SECONDS)).thenReturn("result");
		flowTaskInstance.setTargetTaskHandle(target);

		assertEquals("result", flowTaskInstance.getResult(1, TimeUnit.SECONDS));
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

	@Test
	public void testComplete() {
		flowTaskInstance.setStatus(FlowNodeStatus.PENDING);
		List<FlowableElement> elements = FlowableElementUtils.createFlowableElements(
				Arrays.asList("task1", "task2"), FlowableElementType.USER_TASK);
		flowTaskInstance.setElements(elements);
		Optional<String> optional = Optional.of("processInstanceId");
		flowTaskInstance.setFlowableAdaptor(flowableAdaptor);
		when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(1L)).thenReturn(optional);
		List<Task> tasks = Arrays.asList(mock(Task.class), mock(Task.class));
		when(taskService.createTaskQuery().processInstanceId(optional.get()).list()).thenReturn(tasks);

		flowTaskInstance.complete(false);

		verify(taskService).complete(tasks.get(0).getId(), new HashMap<>());
		verify(taskService).complete(tasks.get(1).getId(), new HashMap<>());
	}

	@Test
	public void testMapToTaskEntity() {
		ServiceTaskInstanceEntity entity = flowTaskInstance.mapToTaskEntity(flowTaskInstance);
		assertNotNull(entity);
		assertEquals(flowTaskInstance.getOrganizationId(), entity.getOrganizationId());
		assertEquals(flowTaskInstance.getStatus(), entity.getStatus());
		assertEquals(flowTaskInstance.getFlowInstanceId(), entity.getFlowInstanceId());
		assertEquals(flowTaskInstance.isStartEndpoint(), entity.isStartEndpoint());
		assertEquals(flowTaskInstance.isEndEndpoint(), entity.isEndEndpoint());
		assertEquals(flowTaskInstance.strategyConfig.getStrategy(), entity.getStrategy());
		assertEquals(flowTaskInstance.getTaskType(), entity.getTaskType());
		assertEquals(flowTaskInstance.getTargetTaskId(), entity.getTargetTaskId());
		assertEquals(flowTaskInstance.strategyConfig.getPendingExpireIntervalSeconds(),
				entity.getWaitExecExpireIntervalSeconds());
		assertEquals(flowTaskInstance.strategyConfig.getExecutionTime(), entity.getExecutionTime());
	}

	@Test
	public void testValidNotExists() {
		FlowTaskInstance flowTaskInstanceVerify =
				new FlowTaskInstance(1L, 1L, FlowNodeStatus.PENDING, 1L, Instant.now(), Instant.now(),
						true, true, flowableAdaptor, nodeRepository, sequenceRepository, serviceTaskRepository);
		ArgumentCaptor<Verify.VerifyException> exceptionCaptor = ArgumentCaptor.forClass(Verify.VerifyException.class);
		doThrow(exceptionCaptor.capture()).when(serviceTaskRepository).deleteById(1L);

		try {
			flowTaskInstanceVerify.validNotExists();
		} catch (Verify.VerifyException e) {
			assertEquals("Instance not found", exceptionCaptor.getValue().getMessage());
		}
	}

	@Test
	public void testValidExists() {
		FlowTaskInstance flowTaskInstanceVerify =
				new FlowTaskInstance(1L, 1L, FlowNodeStatus.PENDING, 1L, Instant.now(), Instant.now(),
						true, true, flowableAdaptor, nodeRepository, sequenceRepository, serviceTaskRepository);
		ArgumentCaptor<Verify.VerifyException> exceptionCaptor = ArgumentCaptor.forClass(Verify.VerifyException.class);
		doThrow(exceptionCaptor.capture()).when(serviceTaskRepository).update(any());

		try {
			flowTaskInstanceVerify.validExists();
		} catch (Verify.VerifyException e) {
			assertEquals("Instance already exists", exceptionCaptor.getValue().getMessage());
		}
	}

}