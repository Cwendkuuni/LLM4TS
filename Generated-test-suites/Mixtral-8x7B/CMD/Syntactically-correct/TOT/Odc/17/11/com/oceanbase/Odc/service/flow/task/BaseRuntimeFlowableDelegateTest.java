import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.test.PluggableFlowableTestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BaseRuntimeFlowableDelegateTest extends PluggableFlowableTestCase {

    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;
    private DelegateExecution delegateExecution;
    private EventPublisher eventPublisher;
    private FlowableAdaptor flowableAdaptor;
    private FlowInstanceRepository flowInstanceRepository;
    private RetryExecutor retryExecutor;

    @Before
    public void setUp() {
        delegateExecution = mock(DelegateExecution.class);
        eventPublisher = mock(EventPublisher.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);
        retryExecutor = mock(RetryExecutor.class);

        baseRuntimeFlowableDelegate = new TestBaseRuntimeFlowableDelegate() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test Result";
            }
        };

        baseRuntimeFlowableDelegate.setEventPublisher(eventPublisher);
        baseRuntimeFlowableDelegate.setFlowableAdaptor(flowableAdaptor);
        baseRuntimeFlowableDelegate.setFlowInstanceRepository(flowInstanceRepository);
        baseRuntimeFlowableDelegate.setRetryExecutor(retryExecutor);
    }

    @Test
    public void testPreHandleWithNullExecution() throws Exception {
        baseRuntimeFlowableDelegate.preHandle(null);

        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    public void testPreHandleWithNonNullExecution() throws Exception {
        baseRuntimeFlowableDelegate.preHandle(delegateExecution);

        verify(eventPublisher).publishEvent(any());
    }

    @Test
    public void testGetWhenDone() throws Exception {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(baseRuntimeFlowableDelegate);
        executorService.shutdown();

        baseRuntimeFlowableDelegate.done = true;
        baseRuntimeFlowableDelegate.returnObject = "Test Result";

        String result = future.get();

        assertEquals(baseRuntimeFlowableDelegate.returnObject, result);
    }

    @Test(expected = ExecutionException.class)
    public void testGetWhenNotDoneWithException() throws Exception {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(baseRuntimeFlowableDelegate);
        executorService.shutdown();

        baseRuntimeFlowableDelegate.thrown = new RuntimeException("Test Exception");

        future.get();
    }

    @Test(expected = InterruptedException.class)
    public void testGetWhenNotDoneWithInterrupted() throws Exception {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(baseRuntimeFlowableDelegate);
        executorService.shutdown();

        Thread.currentThread().interrupt();

        future.get();
    }

    @Test
    public void testGetWithTimeout() throws Exception {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(baseRuntimeFlowableDelegate);
        executorService.shutdown();

        baseRuntimeFlowableDelegate.latch = new CountDownLatch(0);

        future.get(1, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testIsDoneTrue() throws Exception {
        baseRuntimeFlowableDelegate.done = true;

        assertTrue(baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testIsDoneFalse() throws Exception {
        baseRuntimeFlowableDelegate.done = false;

        assertFalse(baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        String processDefinitionId = "Test Process Definition";
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId))
                .thenReturn(Optional.of(1L));

        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

        ArgumentCaptor<String> processDefinitionIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> flowInstanceIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(flowableAdaptor).getTaskInstanceByActivityId(processDefinitionIdCaptor.capture(),
                flowInstanceIdCaptor.capture());
        assertEquals(processDefinitionId, processDefinitionIdCaptor.getValue());
        assertEquals(1L, flowInstanceIdCaptor.getValue().longValue());
    }

    @Test
    public void testBindToFlowTaskInstanceWithNullTaskInstance() {
        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(null);

        assertNull(baseRuntimeFlowableDelegate.targetTaskInstanceId);
    }

    @Test
    public void testBindToFlowTaskInstanceWithNotNullTaskInstance() {
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);
        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);

        verify(flowTaskInstance).bindServiceTask(baseRuntimeFlowableDelegate);
    }

    @Test
    public void testUpdateFlowInstanceStatusWithNullFlowInstanceId() {
        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        verify(flowInstanceRepository, never()).updateStatusById(eq(null), any());
    }

    @Test
    public void testUpdateFlowInstanceStatusWithNotNullFlowInstanceId() {
        long flowInstanceId = 1L;
        doAnswer(invocation -> {
            FlowInstanceRepository mockFlowInstanceRepository = invocation.getArgument(0);
            mockFlowInstanceRepository.updateStatusById(flowInstanceId, FlowStatus.COMPLETED);
            return null;
        }).when(retryExecutor).run(any(Runnable.class), any());

        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        verify(flowInstanceRepository).updateStatusById(flowInstanceId, FlowStatus.COMPLETED);
    }

    private abstract class TestBaseRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {

        @Override
        protected Callable<String> initCallable(DelegateExecution execution) {
            return super.initCallable(execution);
        }
    }
}