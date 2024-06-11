import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> delegate;

    @Mock
    private DelegateExecution execution;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private RetryExecutor retryExecutor;

    @Before
    public void setUp() {
        delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return null;
            }
        };
        delegate.flowableAdaptor = flowableAdaptor;
        delegate.eventPublisher = eventPublisher;
        delegate.flowInstanceRepository = flowInstanceRepository;
        delegate.retryExecutor = retryExecutor;
    }

    @Test
    public void testGetters() {
        assertEquals("activityId is not initialized correctly", null, delegate.getActivityId());
        assertEquals("targetTaskInstanceId is not initialized correctly", null, delegate.getTargetTaskInstanceId());
        assertEquals("taskType is not initialized correctly", null, delegate.getTaskType());
        assertEquals("flowInstanceId is not initialized correctly", null, delegate.getFlowInstanceId());
        assertEquals("strategyConfig is not initialized correctly", null, delegate.getStrategyConfig());
    }

    @Test
    public void testIsDone() {
        assertFalse("Delegate should not be done initially", delegate.isDone());

        delegate.done = true;
        assertTrue("Delegate should be done", delegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        delegate.returnObject = "test";

        String result = delegate.get();

        assertEquals("Delegate get method returned incorrect result", "test", result);
    }

    @Test
    public void testGetTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.returnObject = "test";

        String result = delegate.get(1, TimeUnit.SECONDS);

        assertEquals("Delegate get method returned incorrect result", "test", result);
    }

    @Test
    public void testGetInterrupted() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Thread thread = new Thread(() -> {
            try {
                delegate.get();
            } catch (InterruptedException e) {
                latch.countDown();
            }
        });
        thread.start();

        thread.interrupt();
        thread.join();

        assertTrue("Thread should be interrupted", thread.isInterrupted());
        assertTrue("CountDownLatch should be decremented", latch.await(1, TimeUnit.SECONDS));
    }

    @Test
    public void testGetException() throws InterruptedException {
        when(execution.getCurrentActivityId()).thenReturn("testActivity");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("testProcess"))
                .thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId("testActivity", 1L))
                .thenReturn(Optional.of(mock(FlowTaskInstance.class)));

        delegate.execution = execution;

        assertThrows("Delegate get method should throw ExecutionException",
                ExecutionException.class, delegate::get);
    }

    @Test
    public void testRun() throws Exception {
        Callable<String> callable = () -> "test";
        when(initCallable(execution)).thenReturn(callable);
        when(execution.getCurrentActivityId()).thenReturn("testActivity");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("testProcess"))
                .thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId("testActivity", 1L))
                .thenReturn(Optional.of(mock(FlowTaskInstance.class)));

        delegate.execution = execution;

        delegate.run(execution);

        verify(execution, times(1)).getCurrentActivityId();
        verify(flowableAdaptor, times(1))
                .getFlowInstanceIdByProcessDefinitionId("testProcess");
        verify(flowableAdaptor, times(1))
                .getTaskInstanceByActivityId("testActivity", 1L);
        verify(execution, never()).setVariable(anyString(), any());
        verify(eventPublisher, times(1))
                .publishEvent(any(ServiceTaskStartedEvent.class));
    }

    @Test
    public void testPreHandle() throws Exception {
        delegate.preHandle(execution);

        verify(execution, never()).setVariable(anyString(), any());
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        when(execution.getCurrentActivityId()).thenReturn("testActivity");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("testProcess"))
                .thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId("testActivity", 1L))
                .thenReturn(Optional.of(mock(FlowTaskInstance.class)));

        delegate.execution = execution;

        delegate.initTargetTaskInstanceId(execution);

        assertEquals("activityId is not initialized correctly", "testActivity", delegate.getActivityId());
        assertEquals("targetTaskInstanceId is not initialized correctly", 1L, delegate.getTargetTaskInstanceId());
        assertEquals("taskType is not initialized correctly", TaskType.SERVICE, delegate.getTaskType());
        assertEquals("flowInstanceId is not initialized correctly", 1L, delegate.getFlowInstanceId());
        assertEquals("strategyConfig is not initialized correctly", null, delegate.getStrategyConfig());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = mock(FlowTaskInstance.class);
        delegate.bindToFlowTaskInstance(taskInstance);

        verify(taskInstance, times(1)).bindServiceTask(delegate);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        delegate.updateFlowInstanceStatus(FlowStatus.RUNNING);

        verify(flowInstanceRepository, times(1))
                .updateStatusById(1L, FlowStatus.RUNNING);
    }
}