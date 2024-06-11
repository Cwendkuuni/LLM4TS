import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.test.PluggableFlowableTestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BaseRuntimeFlowableDelegateTest extends PluggableFlowableTestCase {

    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;
    private DelegateExecution delegateExecution;
    private FlowInstanceRepository flowInstanceRepository;
    private FlowableAdaptor flowableAdaptor;
    private EventPublisher eventPublisher;

    @Before
    public void setUp() {
        baseRuntimeFlowableDelegate = mock(BaseRuntimeFlowableDelegate.class);
        delegateExecution = mock(DelegateExecution.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        eventPublisher = mock(EventPublisher.class);

        when(baseRuntimeFlowableDelegate.getEventPublisher()).thenReturn(eventPublisher);
        when(baseRuntimeFlowableDelegate.getFlowInstanceRepository()).thenReturn(flowInstanceRepository);
        when(baseRuntimeFlowableDelegate.getFlowableAdaptor()).thenReturn(flowableAdaptor);
    }

    @Test
    public void testInitTargetTaskInstanceId() throws ExecutionException {
        String processDefinitionId = "process1";
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId))
                .thenReturn(42L);

        String activityId = "activity1";
        when(flowableAdaptor.getTaskInstanceByActivityId(activityId, 42L))
                .thenReturn(Optional.of(new FlowTaskInstance(1L, activityId, TaskType.SERVICE,
                new ExecutionStrategyConfig())));

        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

        ArgumentCaptor<String> activityCaptor = ArgumentCaptor.forClass(String.class);
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(activityCaptor.capture(), any());
        assertEquals(activityId, activityCaptor.getValue());

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(idCaptor.capture());
        assertEquals(processDefinitionId, idCaptor.getValue().toString());
    }

    @Test
    public void testPreHandle() throws Exception {
        baseRuntimeFlowableDelegate.preHandle(delegateExecution);

        verify(baseRuntimeFlowableDelegate, times(1)).preHandle(delegateExecution);
    }

    @Test
    public void testIsDone() {
        baseRuntimeFlowableDelegate.setDone(true);
        assertTrue(baseRuntimeFlowableDelegate.isDone());

        baseRuntimeFlowableDelegate.setDone(false);
        assertFalse(baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        Future<String> future = baseRuntimeFlowableDelegate;
        when(baseRuntimeFlowableDelegate.get()).thenReturn("result");

        String result = future.get();

        assertEquals("result", result);
    }

    @Test
    public void testGetTimeoutException() throws InterruptedException, ExecutionException {
        Future<String> future = baseRuntimeFlowableDelegate;
        when(future.get(1, TimeUnit.SECONDS)).thenThrow(new TimeoutException());

        try {
            future.get(1, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            // Expected
        }
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = new FlowTaskInstance(1L, "activity1", TaskType.SERVICE,
                new ExecutionStrategyConfig());

        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(taskInstance);

        assertTrue(taskInstance.isBound());
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        FlowInstanceRepository repository = mock(FlowInstanceRepository.class);
        baseRuntimeFlowableDelegate.setFlowInstanceRepository(repository);

        Long flowInstanceId = 1L;
        FlowStatus flowStatus = FlowStatus.COMPLETED;

        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(flowStatus);

        verify(repository, times(1)).updateStatusById(flowInstanceId, flowStatus);
    }

    @Test
    public void testInitCallable() {
        DelegateExecution delegateExecution = mock(DelegateExecution.class);

        Callable<String> callable = baseRuntimeFlowableDelegate.initCallable(delegateExecution);

        assertNotNull(callable);
    }

    @Test
    public void testGetActivityId() {
        String activityId = "activity1";
        baseRuntimeFlowableDelegate.setActivityId(activityId);

        assertEquals(activityId, baseRuntimeFlowableDelegate.getActivityId());
    }

    @Test
    public void testGetTargetTaskInstanceId() {
        Long targetTaskInstanceId = 1L;
        baseRuntimeFlowableDelegate.setTargetTaskInstanceId(targetTaskInstanceId);

        assertEquals(targetTaskInstanceId, baseRuntimeFlowableDelegate.getTargetTaskInstanceId());
    }

    @Test
    public void testGetTaskType() {
        TaskType taskType = TaskType.SERVICE;
        baseRuntimeFlowableDelegate.setTaskType(taskType);

        assertEquals(taskType, baseRuntimeFlowableDelegate.getTaskType());
    }

    @Test
    public void testGetFlowInstanceId() {
        Long flowInstanceId = 1L;
        baseRuntimeFlowableDelegate.setFlowInstanceId(flowInstanceId);

        assertEquals(flowInstanceId, baseRuntimeFlowableDelegate.getFlowInstanceId());
    }

    @Test
    public void testGetStrategyConfig() {
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig();
        baseRuntimeFlowableDelegate.setStrategyConfig(strategyConfig);

        assertEquals(strategyConfig, baseRuntimeFlowableDelegate.getStrategyConfig());
    }

    @Test
    public void testSetDone() {
        baseRuntimeFlowableDelegate.setDone(true);

        assertTrue(baseRuntimeFlowableDelegate.isDone());

        baseRuntimeFlowableDelegate.setDone(false);

        assertFalse(baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testSetReturnObject() {
        String returnObject = "result";
        baseRuntimeFlowableDelegate.setReturnObject(returnObject);

        assertEquals(returnObject, baseRuntimeFlowableDelegate.get());
    }

    @Test
    public void testSetThrown() {
        Exception thrown = new RuntimeException("Test exception");
        baseRuntimeFlowableDelegate.setThrown(thrown);

        assertNull(baseRuntimeFlowableDelegate.get());
        assertTrue(baseRuntimeFlowableDelegate.isFailed());

        try {
            baseRuntimeFlowableDelegate.get();
        } catch (ExecutionException e) {
            assertEquals(thrown, e.getCause());
        }
    }

    @Test
    public void testIsFailed() {
        Exception thrown = new RuntimeException("Test exception");
        baseRuntimeFlowableDelegate.setThrown(thrown);

        assertTrue(baseRuntimeFlowableDelegate.isFailed());

        baseRuntimeFlowableDelegate.setThrown(null);

        assertFalse(baseRuntimeFlowableDelegate.isFailed());
    }

    @Test
    public void testSetEventPublisher() {
        EventPublisher eventPublisher = mock(EventPublisher.class);
        baseRuntimeFlowableDelegate.setEventPublisher(eventPublisher);

        assertEquals(eventPublisher, baseRuntimeFlowableDelegate.getEventPublisher());
    }

    @Test
    public void testSetFlowInstanceRepository() {
        FlowInstanceRepository flowInstanceRepository = mock(FlowInstanceRepository.class);
        baseRuntimeFlowableDelegate.setFlowInstanceRepository(flowInstanceRepository);

        assertEquals(flowInstanceRepository, baseRuntimeFlowableDelegate.getFlowInstanceRepository());
    }

    @Test
    public void testSetFlowableAdaptor() {
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
        baseRuntimeFlowableDelegate.setFlowableAdaptor(flowableAdaptor);

        assertEquals(flowableAdaptor, baseRuntimeFlowableDelegate.getFlowableAdaptor());
    }
}