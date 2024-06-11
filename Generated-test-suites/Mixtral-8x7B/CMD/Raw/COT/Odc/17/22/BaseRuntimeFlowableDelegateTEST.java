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
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
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

    @Getter
    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

    private Callable<String> callable;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        callable = mock(Callable.class);

        baseRuntimeFlowableDelegate = spy(new TestRuntimeFlowableDelegate(flowableAdaptor, eventPublisher,
                flowInstanceRepository));

        doNothing().when(baseRuntimeFlowableDelegate).preHandle(delegateExecution);
        doAnswer(invocation -> callable).when(baseRuntimeFlowableDelegate).initCallable(delegateExecution);
    }

    @Test
    public void testRun_InitializationTaskFailed() throws Exception {
        when(callable.call()).thenThrow(new Exception("Initialization task failed"));

        Exception exception = assertThrows(ServiceTaskError.class, () -> baseRuntimeFlowableDelegate.run(delegateExecution));
        assertEquals("Initialization task failed", exception.getCause().getMessage());
    }

    @Test
    public void testRun_ServiceTaskExecutionFailed() throws Exception {
        when(callable.call()).thenThrow(new RuntimeException("Service task execution failed"));

        Exception exception = assertThrows(RuntimeException.class, () -> baseRuntimeFlowableDelegate.run(delegateExecution));
        assertEquals("Service task execution failed", exception.getMessage());
    }

    @Test
    public void testRun_Success() throws Exception {
        when(callable.call()).thenReturn("Result");

        baseRuntimeFlowableDelegate.run(delegateExecution);

        assertEquals("Result", baseRuntimeFlowableDelegate.get());
    }

    @Test
    public void testIsDone_BeforeRun() {
        assertFalse(baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testIsDone_AfterRun() throws Exception {
        baseRuntimeFlowableDelegate.run(delegateExecution);

        assertTrue(baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet_BeforeRun() throws InterruptedException, ExecutionException {
        assertThrows(InterruptedException.class, () -> baseRuntimeFlowableDelegate.get());
    }

    @Test
    public void testGet_AfterRun_Success() throws Exception {
        when(callable.call()).thenReturn("Result");
        baseRuntimeFlowableDelegate.run(delegateExecution);

        assertEquals("Result", baseRuntimeFlowableDelegate.get());
    }

    @Test
    public void testGet_AfterRun_Exception() throws Exception {
        when(callable.call()).thenThrow(new RuntimeException("Service task execution failed"));
        baseRuntimeFlowableDelegate.run(delegateExecution);

        assertThrows(ExecutionException.class, () -> baseRuntimeFlowableDelegate.get());
    }

    @Test
    public void testGet_AfterRun_Timeout() throws Exception {
        when(callable.call()).thenReturn("Result");
        doThrow(new RuntimeException("Timeout")).when(baseRuntimeFlowableDelegate).get();

        assertThrows(TimeoutException.class, () -> baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS));
    }

    @Test
    public void testInitTargetTaskInstanceId_ProcessDefinitionIdNotFound() {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(PreConditions.PreException.class, () -> baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution));
    }

    @Test
    public void testInitTargetTaskInstanceId_TaskInstanceNotFound() {
        String processDefinitionId = "processDefinitionId";
        String activityId = "activityId";

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId))
                .thenReturn(Optional.of(Optional.of(1L)));
        when(flowableAdaptor.getTaskInstanceByActivityId(activityId, 1L))
                .thenReturn(Optional.empty());

        assertThrows(PreConditions.PreException.class, () -> baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution));
    }

    @Test
    public void testInitTargetTaskInstanceId_Success() {
        String processDefinitionId = "processDefinitionId";
        String activityId = "activityId";

        when(delegateExecution.getCurrentActivityId()).thenReturn(activityId);
        when(delegateExecution.getProcessDefinitionId()).thenReturn(processDefinitionId);

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId))
                .thenReturn(Optional.of(Optional.of(1L)));

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        flowTaskInstance.setId(2L);
        flowTaskInstance.setTaskType(TaskType.TEST);
        flowTaskInstance.setStrategyConfig("{}");

        when(flowableAdaptor.getTaskInstanceByActivityId(activityId, 1L)).thenReturn(Optional.of(flowTaskInstance));

        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

        assertEquals(2L, baseRuntimeFlowableDelegate.getTargetTaskInstanceId().longValue());
        assertEquals(TaskType.TEST, baseRuntimeFlowableDelegate.getTaskType());
        assertEquals("{}", baseRuntimeFlowableDelegate.getStrategyConfig().getStrategyConfig());
    }

    @Test
    public void testBindToFlowTaskInstance_TaskInstanceIdMismatch() {
        FlowTaskInstance taskInstance = new FlowTaskInstance();
        taskInstance.setId(2L);

        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(taskInstance);

        verify(taskInstance, never()).bindServiceTask(baseRuntimeFlowableDelegate);
    }

    @Test
    public void testBindToFlowTaskInstance_Success() {
        FlowTaskInstance taskInstance = new FlowTaskInstance();
        taskInstance.setId(baseRuntimeFlowableDelegate.getTargetTaskInstanceId());

        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(taskInstance);

        verify(taskInstance, times(1)).bindServiceTask(baseRuntimeFlowableDelegate);
    }

    @Test
    public void testUpdateFlowInstanceStatus_FlowInstanceIdNull() {
        assertThrows(IllegalArgumentException.class,
                () -> baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.TEST));
    }

    @Test
    public void testUpdateFlowInstanceStatus_Success() {
        when(flowInstanceRepository.updateStatusById(anyLong(), any())).thenReturn(true);

        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.TEST);

        verify(flowInstanceRepository, times(1)).updateStatusById(baseRuntimeFlowableDelegate.getFlowInstanceId(),
                FlowStatus.TEST);
    }

    @Test
    public void testUpdateFlowInstanceStatus_Failure() {
        when(flowInstanceRepository.updateStatusById(anyLong(), any())).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.TEST));
    }

    private static class TestRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {

        public TestRuntimeFlowableDelegate(FlowableAdaptor flowableAdaptor, EventPublisher eventPublisher,
                FlowInstanceRepository flowInstanceRepository) {
            super(flowableAdaptor, eventPublisher, flowInstanceRepository);
        }

        @Override
        protected Callable<String> initCallable(DelegateExecution delegateExecution) {
            return callable;
        }

        @Override
        public void execute(DelegateExecution execution) {
            super.execute(execution);
        }

        @Override
        protected void preHandle(DelegateExecution execution) {
            super.preHandle(execution);
        }

        @Override
        public String get() throws InterruptedException, ExecutionException {
            return super.get();
        }

        @Override
        public String get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return super.get(timeout, unit);
        }
    }
}