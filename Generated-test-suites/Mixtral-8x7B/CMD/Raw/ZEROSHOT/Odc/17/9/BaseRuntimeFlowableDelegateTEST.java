import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.EventPublisher;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

    @Mock
    private DelegateExecution delegateExecution;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        baseRuntimeFlowableDelegate = spy(new TestBaseRuntimeFlowableDelegate());
        doReturn(flowableAdaptor).when(baseRuntimeFlowableDelegate).getFlowableAdaptor();
        doReturn(eventPublisher).when(baseRuntimeFlowableDelegate).getEventPublisher();
        doReturn(flowInstanceRepository).when(baseRuntimeFlowableDelegate).getFlowInstanceRepository();
    }

    @Test
    public void testRun() throws Exception {
        Callable<String> callable = () -> "Test Result";

        doReturn(callable).when(baseRuntimeFlowableDelegate).initCallable(delegateExecution);

        baseRuntimeFlowableDelegate.run(delegateExecution);

        verify(baseRuntimeFlowableDelegate).initTargetTaskInstanceId(delegateExecution);
        verify(baseRuntimeFlowableDelegate).preHandle(delegateExecution);
        verify(callable).call();
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
    }

    @Test
    public void testRunException() {
        Callable<String> callable = () -> {
            throw new RuntimeException("Test Exception");
        };

        doReturn(callable).when(baseRuntimeFlowableDelegate).initCallable(delegateExecution);

        assertThrows(ServiceTaskError.class, () -> baseRuntimeFlowableDelegate.run(delegateExecution));

        verify(baseRuntimeFlowableDelegate).initTargetTaskInstanceId(delegateExecution);
        verify(baseRuntimeFlowableDelegate).preHandle(delegateExecution);
        verify(callable).call();
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
    }

    @Test
    public void testExecute() {
        baseRuntimeFlowableDelegate.execute(delegateExecution);

        verify(eventPublisher).removeEventListener(any(TaskInstanceCreatedListener.class));
        verify(eventPublisher).removeEventListener(any(ActiveTaskStatisticsListener.class));
    }

    @Test
    public void testIsDone() {
        assertEquals(false, baseRuntimeFlowableDelegate.isDone());

        baseRuntimeFlowableDelegate.setDone(true);

        assertEquals(true, baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        CountDownLatch latch = new CountDownLatch(1);
        baseRuntimeFlowableDelegate = new TestBaseRuntimeFlowableDelegate(latch);

        latch.await();

        assertEquals("Test Result", baseRuntimeFlowableDelegate.get());
    }

    @Test
    public void testGetTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        CountDownLatch latch = new CountDownLatch(1);
        baseRuntimeFlowableDelegate = new TestBaseRuntimeFlowableDelegate(latch);

        assertThrows(TimeoutException.class, () -> baseRuntimeFlowableDelegate.get(1, TimeUnit.MILLISECONDS));
    }

    private static class TestBaseRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {

        private final CountDownLatch latch;

        TestBaseRuntimeFlowableDelegate() {
            this.latch = new CountDownLatch(1);
        }

        TestBaseRuntimeFlowableDelegate(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        protected Callable<String> initCallable(DelegateExecution delegateExecution) {
            return () -> "Test Result";
        }

        @Override
        public FlowableAdaptor getFlowableAdaptor() {
            return super.getFlowableAdaptor();
        }

        @Override
        public EventPublisher getEventPublisher() {
            return super.getEventPublisher();
        }

        @Override
        public FlowInstanceRepository getFlowInstanceRepository() {
            return super.getFlowInstanceRepository();
        }

        @Override
        public void setDone(boolean done) {
            super.setDone(done);
        }

        @Override
        protected void preHandle(DelegateExecution execution) throws Exception {
            // Do nothing
        }

        @Override
        public String get() throws InterruptedException, ExecutionException {
            latch.await();
            return super.get();
        }

        @Override
        public String get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            latch.await();
            return super.get(timeout, unit);
        }
    }
}