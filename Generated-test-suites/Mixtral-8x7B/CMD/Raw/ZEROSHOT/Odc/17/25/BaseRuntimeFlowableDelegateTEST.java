import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.concurrent.Callable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution delegateExecution;

    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        baseRuntimeFlowableDelegate = spy(new TestBaseRuntimeFlowableDelegate());
        doReturn("123").when(baseRuntimeFlowableDelegate).initCallable(delegateExecution);
    }

    @Test
    public void testRun() throws Exception {
        doReturn(true).when(baseRuntimeFlowableDelegate).preHandle(delegateExecution);
        baseRuntimeFlowableDelegate.run(delegateExecution);
        verify(baseRuntimeFlowableDelegate, times(1)).preHandle(delegateExecution);
        assertEquals("123", baseRuntimeFlowableDelegate.get());
    }

    @Test
    public void testIsDone() {
        assertFalse(baseRuntimeFlowableDelegate.isDone());
        baseRuntimeFlowableDelegate.run(delegateExecution);
        assertTrue(baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet() throws Exception {
        baseRuntimeFlowableDelegate.run(delegateExecution);
        assertEquals("123", baseRuntimeFlowableDelegate.get());
    }

    @Test
    public void testGetTimeout() throws Exception {
        doReturn(true).when(baseRuntimeFlowableDelegate).preHandle(delegateExecution);
        when(delegateExecution.getCurrentActivityId()).thenReturn("testActivityId");
        baseRuntimeFlowableDelegate.run(delegateExecution);

        Callable<String> callableSpy = spy(baseRuntimeFlowableDelegate);
        doReturn("timeout").when(callableSpy).call();

        TestBaseRuntimeFlowableDelegate testBaseRuntimeFlowableDelegate = new TestBaseRuntimeFlowableDelegate();
        testBaseRuntimeFlowableDelegate.bindToFlowTaskInstance(mock(FlowTaskInstance.class));
        testBaseRuntimeFlowableDelegate.setCallable(callableSpy);

        assertThrows(TimeoutException.class, () -> testBaseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS));
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        when(delegateExecution.getProcessDefinitionId()).thenReturn("testProcessDefinitionId");
        when(baseRuntimeFlowableDelegate.flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(
                "testProcessDefinitionId")).thenReturn(123L);

        when(delegateExecution.getCurrentActivityId()).thenReturn("testActivityId");
        when(baseRuntimeFlowableDelegate.flowableAdaptor.getTaskInstanceByActivityId(
                "testActivityId", 123L)).thenReturn(Optional.of(mock(FlowTaskInstance.class)));

        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

        assertEquals(123L, baseRuntimeFlowableDelegate.getFlowInstanceId());
        assertEquals("testActivityId", baseRuntimeFlowableDelegate.getActivityId());
        assertEquals(TaskType.TEST_TASK_TYPE, baseRuntimeFlowableDelegate.getTaskType());
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        when(baseRuntimeFlowableDelegate.flowInstanceRepository.updateStatusById(123L, FlowStatus.TEST))
                .thenReturn(1);

        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.TEST);

        verify(baseRuntimeFlowableDelegate.flowInstanceRepository, times(1))
                .updateStatusById(123L, FlowStatus.TEST);
    }

    private static class TestBaseRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {

        private Callable<String> callable;

        @Override
        protected Callable<String> initCallable(DelegateExecution delegateExecution) {
            return callable;
        }

        public void setCallable(Callable<String> callable) {
            this.callable = callable;
        }

        @Override
        protected void preHandle(DelegateExecution delegateExecution) throws Exception {
            super.preHandle(delegateExecution);
            doNothing().when(this).initTargetTaskInstanceId(delegateExecution);
        }

        @Override
        public void execute(DelegateExecution delegateExecution) {
            super.execute(delegateExecution);
        }
    }
}