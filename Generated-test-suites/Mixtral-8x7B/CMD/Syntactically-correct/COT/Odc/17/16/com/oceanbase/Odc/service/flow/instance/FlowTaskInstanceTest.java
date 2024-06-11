import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
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
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowableElementImpl;
import com.oceanbase.odc.service.flow.FlowNodeStatus;
import com.oceanbase.odc.service.flow.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

public class FlowTaskInstanceTest {

	@Mock
	private ServiceTaskInstanceRepository serviceTaskRepository;

	@Mock
	private TaskService taskService;

	@Mock
	private EventPublisher eventPublisher;

	@Mock
	private NodeInstanceEntityRepository nodeRepository;

	@Mock
	private SequenceInstanceRepository sequenceRepository;

	@Mock
	private FlowableAdaptor flowableAdaptor;

	@Mock
	private BaseRuntimeFlowableDelegate<?> targetTaskHandle;

	@Mock
	private RuntimeDelegateMapper delegateConvertor;

	@Captor
	private ArgumentCaptor<ServiceTaskInstanceEntity> entityCaptor;

	private FlowTaskInstance flowTaskInstance;

	private ExecutionStrategyConfig strategyConfig;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		strategyConfig = ExecutionStrategyConfig.builder()
				.strategy(FlowTaskExecutionStrategy.AUTO)
				.pendingExpireIntervalSeconds(30)
				.executionTime(Duration.ofMinutes(1))
				.build();
		flowTaskInstance = new FlowTaskInstance(1L, 2L, 3L, strategyConfig, true, true, delegateConvertor,
				flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
				serviceTaskRepository);
	}

	@Test
	public void testGettersAndSetters() {
		assertEquals(1L, flowTaskInstance.getId().longValue());
		assertEquals(2L, flowTaskInstance.getOrganizationId().longValue());
		assertEquals(3L, flowTaskInstance.getFlowInstanceId().longValue());
		assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
		assertEquals(FlowNodeType.SERVICE_TASK, flowTaskInstance.getNodeType());
		assertEquals(strategyConfig, flowTaskInstance.getStrategyConfig());
		assertNull(flowTaskInstance.getTargetTaskId());
		assertEquals(TaskType.GENERIC, flowTaskInstance.getTaskType());
	}

	@Test
	public void testCreate() {
		ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
		entity.setId(4L);
		entity.setCreateTime(1000L);
		entity.setUpdateTime(2000L);

		when(serviceTaskRepository.save(any(ServiceTaskInstanceEntity.class))).thenReturn(entity);

		flowTaskInstance.create();

		verify(serviceTaskRepository, times(1)).save(entityCaptor.capture());
		ServiceTaskInstanceEntity savedEntity = entityCaptor.getValue();
		assertEquals(4L, savedEntity.getId().longValue());
		assertEquals(1000L, savedEntity.getCreateTime().longValue());
		assertEquals(2000L, savedEntity.getUpdateTime().longValue());
	}

	@Test
	public void testUpdate() {
		ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
		entity.setId(4L);
		entity.setStatus(FlowNodeStatus.EXECUTING);

		when(serviceTaskRepository.update(any(ServiceTaskInstanceEntity.class))).thenReturn(1);

		flowTaskInstance.setStatus(FlowNodeStatus.EXECUTING);
		flowTaskInstance.update();

		verify(serviceTaskRepository, times(1)).update(entityCaptor.capture());
		ServiceTaskInstanceEntity updatedEntity = entityCaptor.getValue();
		assertEquals(4L, updatedEntity.getId().longValue());
		assertEquals(FlowNodeStatus.EXECUTING, updatedEntity.getStatus());
	}

	@Test
	public void testDelete() {
		flowTaskInstance.delete(4L);
		verify(serviceTaskRepository, times(1)).deleteById(4L);
	}

	@Test
	public void testMapToTaskEntity() {
		ServiceTaskInstanceEntity entity = FlowTaskInstance.mapToTaskEntity(flowTaskInstance);
		assertEquals(2L, entity.getOrganizationId().longValue());
		assertEquals(FlowNodeStatus.PENDING, entity.getStatus());
		assertEquals(3L, entity.getFlowInstanceId().longValue());
		assertEquals(true, entity.isStartEndpoint());
		assertEquals(true, entity.isEndEndpoint());
		assertEquals(strategyConfig.getStrategy(), entity.getStrategy());
		assertEquals(TaskType.GENERIC, entity.getTaskType());
		assertEquals(0, entity.getTargetTaskId().longValue());
		assertEquals(30, entity.getWaitExecExpireIntervalSeconds().intValue());
		assertEquals(1, entity.getExecutionTime().getSeconds());
	}

	@Test
	public void testBatchCreate() {
		List<FlowTaskInstance> instances = Arrays
				.asList(new FlowTaskInstance(1L, 2L, 3L, strategyConfig, true, true, delegateConvertor,
						flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
						serviceTaskRepository),
						new FlowTaskInstance(4L, 5L, 6L, strategyConfig, true, true, delegateConvertor,
								flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository,
								serviceTaskRepository));
		List<ServiceTaskInstanceEntity> entities =
				FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
		assertEquals(2, entities.size());
		assertEquals(1L, entities.get(0).getOrganizationId().longValue());
		assertEquals(FlowNodeStatus.PENDING, entities.get(0).getStatus());
		assertEquals(3L, entities.get(0).getFlowInstanceId().longValue());
		assertEquals(true, entities.get(0).isStartEndpoint());
		assertEquals(true, entities.get(0).isEndEndpoint());
		assertEquals(strategyConfig.getStrategy(), entities.get(0).getStrategy());
		assertEquals(TaskType.GENERIC, entities.get(0).getTaskType());
		assertEquals(0, entities.get(0).getTargetTaskId().longValue());
		assertEquals(30, entities.get(0).getWaitExecExpireIntervalSeconds().intValue());
		assertEquals(1, entities.get(0).getExecutionTime().getSeconds());
	}

	@Test
	public void testBindServiceTask() {
		BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
		when(target.getTargetTaskInstanceId()).thenReturn(1L);
		flowTaskInstance.bindServiceTask(target);
		verify(target, times(1)).setTargetTaskInstanceId(1L);
		assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
	}

	@Test
	public void testIsPresentOnThisMachine() {
		assertFalse(flowTaskInstance.isPresentOnThisMachine());
		flowTaskInstance.bindServiceTask(targetTaskHandle);
		assertTrue(flowTaskInstance.isPresentOnThisMachine());
	}

	@Test
	public void testDealloc() {
		ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);
		flowTaskInstance.setListener(listener);
		flowTaskInstance.dealloc();
		verify(eventPublisher, times(1)).removeEventListener(listener);
		assertNull(flowTaskInstance.getListener());
	}

	@Test
	public void testAlloc() {
		ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);
		flowTaskInstance.alloc();
		verify(eventPublisher, times(1)).addEventListener(listener);
		assertEquals(listener, flowTaskInstance.getListener());
	}

	@Test
	public void testConfirmExecute() {
		flowTaskInstance.confirmExecute();
		verify(taskService, times(1)).complete(anyLong(), any());
	}

	@Test
	public void testAbort() {
		flowTaskInstance.abort();
		verify(taskService, times(1)).complete(anyLong(), any());
	}

	@Test
	public void testCancel() throws InterruptedException, ExecutionException {
		when(targetTaskHandle.isDone()).thenReturn(true);
		when(targetTaskHandle.get()).thenReturn("Result");
		flowTaskInstance.bindServiceTask(targetTaskHandle);
		assertEquals("Result", flowTaskInstance.getResult());
		verify(targetTaskHandle, never()).cancel(anyBoolean());
		flowTaskInstance.cancel(true);
		verify(targetTaskHandle, times(1)).cancel(true);
	}

	@Test
	public void testIsDone() throws InterruptedException, ExecutionException {
		when(targetTaskHandle.isDone()).thenReturn(true);
		flowTaskInstance.bindServiceTask(targetTaskHandle);
		assertTrue(flowTaskInstance.isDone());
		verify(targetTaskHandle, times(1)).isDone();
	}

	@Test
	public void testGetResult() throws InterruptedException, ExecutionException, TimeoutException {
		when(targetTaskHandle.get()).thenReturn("Result");
		flowTaskInstance.bindServiceTask(targetTaskHandle);
		assertEquals("Result", flowTaskInstance.getResult());
		verify(targetTaskHandle, times(1)).get();
	}

	@Test
	public void testGetResultWithTimeout()
			throws InterruptedException, ExecutionException, TimeoutException {
		when(targetTaskHandle.get(30, TimeUnit.SECONDS)).thenReturn("Result");
		flowTaskInstance.bindServiceTask(targetTaskHandle);
		assertEquals("Result", flowTaskInstance.getResult(30, TimeUnit.SECONDS));
		verify(targetTaskHandle, times(1)).get(30, TimeUnit.SECONDS);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetResultWithException()
			throws InterruptedException, ExecutionException, TimeoutException {
		doThrow(new ExecutionException("Test")).when(targetTaskHandle).get();
		flowTaskInstance.bindServiceTask(targetTaskHandle);
		flowTaskInstance.getResult();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetResultWithTimeoutException()
			throws InterruptedException, ExecutionException, TimeoutException {
		doThrow(new TimeoutException("Test")).when(targetTaskHandle).get(30, TimeUnit.SECONDS);
		flowTaskInstance.bindServiceTask(targetTaskHandle);
		flowTaskInstance.getResult(30, TimeUnit.SECONDS);
	}

	@Test
	public void testGetTargetDelegateClass() {
		Class<? extends BaseRuntimeFlowableDelegate<?>> delegateClass =
				flowTaskInstance.getTargetDelegateClass();
		assertNotNull(delegateClass);
	}

	@Test
	public void testComplete() {
		FlowableElement userTask1 = new FlowableElementImpl(1L, 2L, 3L, "UserTask1",
				FlowableElementType.USER_TASK, FlowNodeType.USER_TASK);
		FlowableElement userTask2 = new FlowableElementImpl(4L, 5L, 6L, "UserTask2",
				FlowableElementType.USER_TASK, FlowNodeType.USER_TASK);
		List<FlowableElement> userTasks = Arrays.asList(userTask1, userTask2);
		Optional<String> processInstanceId = Optional.of("ProcessInstance1");

		when(flowableAdaptor.getFlowableElementByType(anyLong(), any(FlowNodeType.class),
				any(FlowableElementType.class))).thenReturn(userTasks);
		when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(anyLong())).thenReturn(processInstanceId);
		when(taskService.createTaskQuery().processInstanceId(anyString()).list())
				.thenReturn(Arrays.asList(mock(Task.class), mock(Task.class)));
		when(taskService.complete(anyLong(), anyMap())).thenReturn(null);

		flowTaskInstance.complete(false);

		verify(flowableAdaptor, times(1))
				.getFlowableElementByType(anyLong(), any(FlowNodeType.class), any(FlowableElementType.class));
		verify(flowableAdaptor, times(1))
				.getProcessInstanceIdByFlowInstanceId(anyLong());
		verify(taskService, times(1)).createTaskQuery().processInstanceId(anyString()).list();
		verify(taskService, times(2)).complete(anyLong(), anyMap());
	}

}