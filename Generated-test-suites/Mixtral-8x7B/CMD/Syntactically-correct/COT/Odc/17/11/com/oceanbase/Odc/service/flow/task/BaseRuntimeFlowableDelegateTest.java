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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution delegateExecution;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        baseRuntimeFlowableDelegate = spy(new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "test";
            }
        });
        baseRuntimeFlowableDelegate.flowableAdaptor = flowableAdaptor;
        baseRuntimeFlowableDelegate.eventPublisher = eventPublisher;
        baseRuntimeFlowableDelegate.flowInstanceRepository = flowInstanceRepository;
    }

    @Test
    public void testRun() throws Exception {
        when(delegateExecution.getCurrentActivityId()).thenReturn("testActivity");
        when(flowableAdaptor.getTaskInstanceByActivityId(any(), anyLong()))
                .thenReturn(Optional.of(new FlowTaskInstance()));

        baseRuntimeFlowableDelegate.run(delegateExecution);

        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
    }

    @Test
    public void testExecute() {
        baseRuntimeFlowableDelegate.execute(delegateExecution);

        verify(eventPublisher, times(1)).removeEventListener(any());
        verify(eventPublisher, times(1)).removeEventListener(any());
    }

    @Test
    public void testIsDone() {
        // Testing isDone() before the task is executed
        assertEquals(false, baseRuntimeFlowableDelegate.isDone());

        // Testing isDone() after the task is executed
        baseRuntimeFlowableDelegate.execute(delegateExecution);
        assertEquals(true, baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGetWithInterruptedException() throws Exception {
        when(delegateExecution.getCurrentActivityId()).thenReturn("testActivity");
        when(flowableAdaptor.getTaskInstanceByActivityId(any(), anyLong()))
                .thenReturn(Optional.of(new FlowTaskInstance()));

        baseRuntimeFlowableDelegate.run(delegateExecution);

        CountDownLatch latch = new CountDownLatch(1);
        Thread thread = new Thread(() -> {
            try {
                baseRuntimeFlowableDelegate.get(1, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                latch.countDown();
            }
        });
        thread.start();

        // Interrupt the thread waiting for get()
        thread.interrupt();

        latch.await();
        verify(delegateExecution, times(1)).getActivityId();
    }

    @Test
    public void testGetWithExecutionException() throws Exception {
        when(delegateExecution.getCurrentActivityId()).thenReturn("testActivity");
        when(flowableAdaptor.getTaskInstanceByActivityId(any(), anyLong()))
                .thenReturn(Optional.of(new FlowTaskInstance()));
        doThrow(new ExecutionException(new RuntimeException())).when(baseRuntimeFlowableDelegate).run(delegateExecution);

        assertThrows(ExecutionException.class, () -> baseRuntimeFlowableDelegate.get());
    }

    @Test
    public void testGetWithTimeoutException() throws Exception {
        when(delegateExecution.getCurrentActivityId()).thenReturn("testActivity");
        when(flowableAdaptor.getTaskInstanceByActivityId(any(), anyLong()))
                .thenReturn(Optional.of(new FlowTaskInstance()));

        baseRuntimeFlowableDelegate.run(delegateExecution);

        CountDownLatch latch = new CountDownLatch(1);
        Thread thread = new Thread(() -> {
            try {
                baseRuntimeFlowableDelegate.get(1, TimeUnit.MILLISECONDS);
            } catch (TimeoutException e) {
                latch.countDown();
            }
        });
        thread.start();

        latch.await();
        verify(delegateExecution, times(1)).getActivityId();
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        when(delegateExecution.getProcessDefinitionId()).thenReturn("testProcess");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any()))
                .thenReturn(Optional.of(1L));

        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

        verify(delegateExecution, times(1)).getProcessDefinitionId();
        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(any());
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        FlowStatus flowStatus = FlowStatus.TEST;
        when(flowInstanceRepository.updateStatusById(anyLong(), any()))
                .thenReturn(true);

        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(flowStatus);

        verify(flowInstanceRepository, times(1)).updateStatusById(anyLong(), any());
    }

    @Test
    public void testInitCallable() {
        Callable<String> callable = baseRuntimeFlowableDelegate.initCallable(delegateExecution);

        assertEquals("test", callable.call());
    }

    @Test(expected = ServiceTaskError.class)
    public void testRunWithInitializationException() throws Exception {
        when(delegateExecution.getCurrentActivityId()).thenThrow(new Exception());

        baseRuntimeFlowableDelegate.run(delegateExecution);
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = new FlowTaskInstance();
        taskInstance.setId(1L);
        taskInstance.setTaskType(TaskType.TEST);

        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(taskInstance);

        assertEquals(taskInstance.getId(), baseRuntimeFlowableDelegate.getTargetTaskInstanceId());
        assertEquals(taskInstance.getTaskType(), baseRuntimeFlowableDelegate.getTaskType());
    }
}