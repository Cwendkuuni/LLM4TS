import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstanceImpl;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalMatchers;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution delegateExecution;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

    private FlowTaskInstanceImpl flowTaskInstance;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        flowTaskInstance = new FlowTaskInstanceImpl();
        flowTaskInstance.setId(1L);
        flowTaskInstance.setTaskType(TaskType.MANUAL_TASK);
        flowTaskInstance.setStrategyConfig(null);
        when(delegateExecution.getCurrentActivityId()).thenReturn("activityId");
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
                .thenReturn(Optional.of(flowTaskInstance));
        when(flowInstanceRepository.updateStatusById(anyLong(), any()))
                .thenReturn(1);
    }

    @Test
    public void testBaseRuntimeFlowableDelegate() {
        assertNotNull(baseRuntimeFlowableDelegate);
        assertEquals("activityId", baseRuntimeFlowableDelegate.getActivityId());
        assertEquals(1L, baseRuntimeFlowableDelegate.getTargetTaskInstanceId());
        assertEquals(TaskType.MANUAL_TASK, baseRuntimeFlowableDelegate.getTaskType());
        assertEquals(null, baseRuntimeFlowableDelegate.getStrategyConfig());
    }

    @Test
    public void testRun() throws Exception {
        Callable<String> callable = () -> "return object";
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenReturn(1L);

        baseRuntimeFlowableDelegate.run(delegateExecution);

        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(delegateExecution, times(1)).setVariable(eq("odc_service_task_status"),
                eq(FlowStatus.SERVICE_TASK_COMPLETED));
        verifyNoMoreInteractions(eventPublisher, delegateExecution, flowInstanceRepository);
    }

    @Test
    public void testRunException() throws Exception {
        Callable<String> callable = () -> {
            throw new RuntimeException("test exception");
        };
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenReturn(1L);

        Exception exception = assertThrows(RuntimeException.class, () -> baseRuntimeFlowableDelegate.run(delegateExecution));
        assertEquals("test exception", exception.getMessage());

        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(delegateExecution, times(1)).setVariable(eq("odc_service_task_status"),
                eq(FlowStatus.SERVICE_TASK_FAILED));
        verifyNoMoreInteractions(eventPublisher, delegateExecution, flowInstanceRepository);
    }

    @Test
    public void testExecute() {
        baseRuntimeFlowableDelegate.execute(delegateExecution);

        verify(eventPublisher, times(1)).removeEventListener(any(TaskInstanceCreatedListener.class));
        verify(eventPublisher, times(1)).removeEventListener(any(ActiveTaskStatisticsListener.class));
        verifyNoMoreInteractions(eventPublisher);
    }

    @Test
    public void testIsDone() {
        assertFalse(baseRuntimeFlowableDelegate.isDone());
        baseRuntimeFlowableDelegate.run(delegateExecution);
        assertTrue(baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        Callable<String> callable = () -> "return object";
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenReturn(1L);

        baseRuntimeFlowableDelegate.run(delegateExecution);

        String result = baseRuntimeFlowableDelegate.get();
        assertEquals("return object", result);

        result = baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS);
        assertEquals("return object", result);
    }

    @Test
    public void testGetInterruptedException() throws InterruptedException, ExecutionException {
        Callable<String> callable = () -> {
            Thread.currentThread().interrupt();
            return "return object";
        };
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenReturn(1L);

        baseRuntimeFlowableDelegate.run(delegateExecution);

        assertThrows(InterruptedException.class, baseRuntimeFlowableDelegate::get);
    }

    @Test
    public void testGetExecutionException() throws InterruptedException, ExecutionException, TimeoutException {
        Callable<String> callable = () -> {
            throw new RuntimeException("test exception");
        };
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenReturn(1L);

        baseRuntimeFlowableDelegate.run(delegateExecution);

        assertThrows(ExecutionException.class, baseRuntimeFlowableDelegate::get);
    }

    @Test
    public void testGetTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        Callable<String> callable = () -> "return object";
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenReturn(1L);

        baseRuntimeFlowableDelegate.run(delegateExecution);

        assertThrows(TimeoutException.class, () -> baseRuntimeFlowableDelegate.get(0, TimeUnit.SECONDS));
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenReturn(1L);

        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(anyString());
        verifyNoMoreInteractions(flowableAdaptor);
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstanceImpl taskInstance = new FlowTaskInstanceImpl();
        taskInstance.setId(1L);
        taskInstance.setTaskType(TaskType.MANUAL_TASK);
        taskInstance.setStrategyConfig(null);

        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(taskInstance);

        verify(flowTaskInstance, times(1)).bindServiceTask(ArgumentMatchers.any());
        verifyNoMoreInteractions(flowTaskInstance);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.MANUAL_TASK_COMPLETED);

        verify(flowInstanceRepository, times(1))
                .updateStatusById(eq(1L), eq(FlowStatus.MANUAL_TASK_COMPLETED));
        verifyNoMoreInteractions(flowInstanceRepository);
    }

    @Test
    public void testInitCallable() {
        Callable<String> callable = baseRuntimeFlowableDelegate.initCallable(delegateExecution);

        assertNotNull(callable);
    }
}