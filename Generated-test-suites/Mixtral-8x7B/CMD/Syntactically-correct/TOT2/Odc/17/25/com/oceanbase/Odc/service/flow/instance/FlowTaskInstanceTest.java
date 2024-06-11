import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricTaskInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Task;
import org.flowable.task.api.TaskInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowableElementTypeImpl;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowableElementTypeImplTest;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedEvent;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

public class FlowTaskInstanceTest {

	@Mock
	private ServiceTaskInstanceRepository serviceTaskRepository;

	@Mock
	private RuntimeService runtimeService;

	@Mock
	private TaskService taskService;

	@Mock
	private HistoryService historyService;

	@Mock
	private RepositoryService repositoryService;

	@Mock
	private NodeInstanceEntityRepository nodeInstanceEntityRepository;

	@Mock
	private SequenceInstanceRepository sequenceInstanceRepository;

	@Mock
	private FlowableAdaptor flowableAdaptor;

	@Mock
	private RuntimeDelegateMapper runtimeDelegateMapper;

	@Mock
	private EventPublisher eventPublisher;

	private FlowTaskInstance flowTaskInstance;

	private FlowableElementType userTaskElementType;

	private ExecutionStrategyConfig executionStrategyConfig;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		userTaskElementType = FlowableElementTypeImplTest.createUserTaskElementType();
		executionStrategyConfig = ExecutionStrategyConfig.builder()
				.strategy(FlowTaskExecutionStrategy.AUTO)
				.pendingExpireIntervalSeconds(30)
				.executionTime(Duration.ofSeconds(10))
				.build();

		flowTaskInstance = new FlowTaskInstance(1L, 2L, 3L, executionStrategyConfig, true, true,
				runtimeDelegateMapper, new FlowableAdaptorImpl(repositoryService, runtimeService, taskService,
						historyService), eventPublisher, serviceTaskRepository, nodeInstanceEntityRepository,
				sequenceInstanceRepository);
	}

	@Test
	public void testGetCoreFlowableElementType() {
		assertEquals(FlowableElementTypeImpl.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
	}

	@Test
	public void testBatchCreate() {
		List<FlowTaskInstance> instances = Arrays.asList(
				new FlowTaskInstance(1L, 2L, 3L, executionStrategyConfig, true, true, runtimeDelegateMapper,
						flowableAdaptor, eventPublisher, serviceTaskRepository, nodeInstanceEntityRepository,
						sequenceInstanceRepository),
				new FlowTaskInstance(4L, 5L, 6L, executionStrategyConfig, true, true, runtimeDelegateMapper,
						flowableAdaptor, eventPublisher, serviceTaskRepository, nodeInstanceEntityRepository,
						sequenceInstanceRepository));

		ArgumentCaptor<List<ServiceTaskInstanceEntity>> entityCaptor = ArgumentCaptor.forClass(List.class);
		when(serviceTaskRepository.batchCreate(entityCaptor.capture())).thenReturn(instances.stream()
				.map(FlowTaskInstance::mapToTaskEntity).collect(Collectors.toList()));

		List<FlowTaskInstance> createdInstances = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);

		assertEquals(instances, createdInstances);
		verify(serviceTaskRepository).batchCreate(entityCaptor.capture());
		List<ServiceTaskInstanceEntity> entities = entityCaptor.getValue();
		assertEquals(2, entities.size());
		verifyNoMoreInteractions(serviceTaskRepository);
	}

	@Test
	public void testCreate() {
		when(serviceTaskRepository.save(any(ServiceTaskInstanceEntity.class))).thenReturn(mock(ServiceTaskInstanceEntity.class));

		flowTaskInstance.create();

		verify(serviceTaskRepository).save(any(ServiceTaskInstanceEntity.class));
		verifyNoMoreInteractions(serviceTaskRepository);
	}

	@Test
	public void testUpdate() {
		ServiceTaskInstanceEntity entity = mock(ServiceTaskInstanceEntity.class);
		when(serviceTaskRepository.update(entity)).thenReturn(1);

		flowTaskInstance.update();

		verify(serviceTaskRepository).update(entity);
		verifyNoMoreInteractions(serviceTaskRepository);
	}

	@Test
	public void testDoDelete() {
		flowTaskInstance.doDelete(1L);

		verify(serviceTaskRepository).deleteById(1L);
		verifyNoMoreInteractions(serviceTaskRepository);
	}

	@Test
	public void testBindServiceTask() {
		BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
		flowTaskInstance.bindServiceTask(target);

		assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
	}

	@Test
	public void testIsPresentOnThisMachine() {
		BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
		when(target.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());

		flowTaskInstance.targetTaskHandle = target;

		assertTrue(flowTaskInstance.isPresentOnThisMachine());

		flowTaskInstance.targetTaskHandle = null;

		assertFalse(flowTaskInstance.isPresentOnThisMachine());
	}

	@Test
	public void testDealloc() {
		ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);
		flowTaskInstance.listener = listener;

		flowTaskInstance.dealloc();

		verify(eventPublisher).removeEventListener(listener);
		verifyNoMoreInteractions(eventPublisher);
	}

	@Test
	public void testAlloc() {
		ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);

		flowTaskInstance.alloc();

		verify(eventPublisher).addEventListener(listener);
		verifyNoMoreInteractions(eventPublisher);
	}

	@Test
	public void testConfirmExecute() {
		flowTaskInstance.confirmExecute();

		verify(flowTaskInstance).complete(false);
		verifyNoMoreInteractions(flowTaskInstance);
	}

	@Test
	public void testAbort() {
		flowTaskInstance.abort();

		verify(flowTaskInstance).complete(true);
		verifyNoMoreInteractions(flowTaskInstance);
	}

	@Test
	public void testCancel() throws InterruptedException, ExecutionException {
		BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
		flowTaskInstance.targetTaskHandle = target;

		when(target.isDone()).thenReturn(true);
		when(target.get()).thenReturn("Result");

		assertEquals("Result", flowTaskInstance.getResult());

		verify(target).isDone();
		verify(target).get();
		verifyNoMoreInteractions(target);
	}

	@Test(expected = ExecutionException.class)
	public void testCancelWithExecutionException() throws InterruptedException, ExecutionException {
		BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
		flowTaskInstance.targetTaskHandle = target;

		when(target.isDone()).thenReturn(true);
		when(target.get()).thenThrow(new ExecutionException("Test exception"));

		flowTaskInstance.getResult();

		verify(target).isDone();
		verify(target).get();
		verifyNoMoreInteractions(target);
	}

	@Test(expected = InterruptedException.class)
	public void testCancelWithInterruptedException() throws InterruptedException, ExecutionException {
		BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
		flowTaskInstance.targetTaskHandle = target;

		when(target.isDone()).thenReturn(false);

		flowTaskInstance.getResult();

		verify(target).isDone();
		verifyNoMoreInteractions(target);
	}

	@Test
	public void testGetTargetDelegateClass() {
		Class<? extends BaseRuntimeFlowableDelegate<?>> delegateClass = flowTaskInstance.getTargetDelegateClass();

		assertNotNull(delegateClass);
	}

	private static ServiceTaskInstanceEntity mapToTaskEntity(FlowTaskInstance instance) {
		// TODO: Implement this method
		return null;
	}

}