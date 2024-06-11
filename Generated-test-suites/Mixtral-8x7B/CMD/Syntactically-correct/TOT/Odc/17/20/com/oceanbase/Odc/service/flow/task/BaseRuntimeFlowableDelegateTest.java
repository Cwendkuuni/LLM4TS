import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.test.PluggableFlowableTestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.concurrent.Callable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BaseRuntimeFlowableDelegateTest extends PluggableFlowableTestCase {

    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;
    private DelegateExecution delegateExecution;
    private FlowableAdaptor flowableAdaptor;
    private EventPublisher eventPublisher;
    private FlowInstanceRepository flowInstanceRepository;

    @Before
    public void setUp() {
        delegateExecution = mock(DelegateExecution.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        eventPublisher = mock(EventPublisher.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);

        baseRuntimeFlowableDelegate = new TestRuntimeFlowableDelegate() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test Result";
            }
        };

        baseRuntimeFlowableDelegate.setFlowableAdaptor(flowableAdaptor);
        baseRuntimeFlowableDelegate.setEventPublisher(eventPublisher);
        baseRuntimeFlowableDelegate.setFlowInstanceRepository(flowInstanceRepository);
    }

    @Test
    public void testPreHandle() throws Exception {
        baseRuntimeFlowableDelegate.preHandle(delegateExecution);

        verify(delegateExecution, times(1)).getProcessDefinitionId();
        verify(delegateExecution, times(1)).getCurrentActivityId();
    }

    @Test
    public void testInitTargetTaskInstanceId_Success() {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any())).thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId(any(), anyLong())).thenReturn(Optional.of(mock(FlowTaskInstance.class)));

        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(any());
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(any(), anyLong());

        assertEquals(1L, baseRuntimeFlowableDelegate.getFlowInstanceId());
        assertEquals("Test Task", baseRuntimeFlowableDelegate.getTaskType());
        assertNull(baseRuntimeFlowableDelegate.getReturnObject());
        assertFalse(baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testInitTargetTaskInstanceId_Exception() {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any())).thenReturn(Optional.empty());

        assertThrows(ServiceTaskError.class, () -> baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution));

        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(any());
        verify(flowableAdaptor, never()).getTaskInstanceByActivityId(any(), anyLong());
    }

    @Test
    public void testGet_Success() throws Exception {
        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);
        baseRuntimeFlowableDelegate.execute(delegateExecution);

        assertEquals("Test Result", baseRuntimeFlowableDelegate.get());
    }

    @Test
    public void testGet_InterruptedException() throws Exception {
        when(delegateExecution.getCurrentActivityId()).thenReturn("MockActivity");

        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);
        Mockito.doThrow(new InterruptedException()).when(delegateExecution).waitForFlowObject(any());
        baseRuntimeFlowableDelegate.execute(delegateExecution);

        assertThrows(InterruptedException.class, () -> baseRuntimeFlowableDelegate.get());
    }

    @Test
    public void testGet_ExecutionException() throws Exception {
        when(delegateExecution.getCurrentActivityId()).thenReturn("MockActivity");

        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);
        Mockito.doThrow(new RuntimeException("Test Exception")).when(delegateExecution).waitForFlowObject(any());
        baseRuntimeFlowableDelegate.execute(delegateExecution);

        assertThrows(ExecutionException.class, () -> baseRuntimeFlowableDelegate.get());
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        when(flowInstanceRepository.updateStatusById(anyLong(), any())).thenReturn(true);

        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        verify(flowInstanceRepository, times(1)).updateStatusById(anyLong(), any());
    }

    @Test
    public void testBindToFlowTaskInstance_Bind() {
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);
        when(flowTaskInstance.getId()).thenReturn(1L);

        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);

        verify(flowTaskInstance, times(1)).bindServiceTask(baseRuntimeFlowableDelegate);
    }

    @Test
    public void testBindToFlowTaskInstance_NotEqual() {
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);
        when(flowTaskInstance.getId()).thenReturn(2L);

        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);

        verify(flowTaskInstance, never()).bindServiceTask(baseRuntimeFlowableDelegate);
    }

    private abstract class TestRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {

        @Override
        protected Callable<String> initCallable(DelegateExecution execution) {
            return () -> "Test Result";
        }
    }
}