
package com.oceanbase.odc.service.flow.task;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.FlowableAdaptor;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private FlowTaskInstance flowTaskInstance;

    @Mock
    private DelegateExecution execution;

    private RetryExecutor retryExecutor;

    private BaseRuntimeFlowableDelegate<String> delegate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        retryExecutor = RetryExecutor.builder().retryIntervalMillis(1000).retryTimes(3).build();
        delegate = spy(new TestRuntimeFlowableDelegate());
        delegate.setEventPublisher(eventPublisher);
        delegate.setFlowableAdaptor(flowableAdaptor);
        delegate.setFlowInstanceRepository(flowInstanceRepository);
        delegate.setRetryExecutor(retryExecutor);
    }

    @After
    public void tearDown() {
        delegate.cleanup();
    }

    @Test
    public void testInitTargetTaskInstanceId_ValidProcessDefinitionId_Success() throws Exception {
        when(execution.getProcessDefinitionId()).thenReturn("validProcessDefinitionId");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
                .thenReturn(Optional.of(flowTaskInstance));
        delegate.initTargetTaskInstanceId(execution);
        verify(delegate).setFlowInstanceId(1L);
        verify(delegate).setTargetTaskInstanceId(1L);
        verify(delegate).setTaskType(TaskType.TEST);
        verify(delegate).setStrategyConfig(any());
    }

    @Test
    public void testInitTargetTaskInstanceId_InvalidProcessDefinitionId_ExceptionThrown() throws Exception {
        when(execution.getProcessDefinitionId()).thenReturn("invalidProcessDefinitionId");
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenReturn(Optional.empty());
        try {
            delegate.initTargetTaskInstanceId(execution);
            fail();
        } catch (ServiceTaskError e) {
            verify(delegate).setThrown(any());
            verify(delegate).setDone(true);
            verify(delegate).latchCountDown();
        }
    }

    @Test
    public void testExecute_ValidExecution_TaskBoundToFlowTaskInstance() throws Exception {
        when(execution.getCurrentActivityId()).thenReturn("testActivityId");
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
                .thenReturn(Optional.of(flowTaskInstance));
        delegate.bindToFlowTaskInstance(flowTaskInstance);
        delegate.execute(execution);
        verify(flowTaskInstance).bindServiceTask(delegate);
    }

    @Test
    public void testExecute_InvalidExecution_ExceptionThrown() throws Exception {
        when(execution.getCurrentActivityId()).thenReturn("invalidActivityId");
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
                .thenReturn(Optional.empty());
        try {
            delegate.execute(execution);
            fail();
        } catch (ServiceTaskError e) {
            verify(delegate).setThrown(any());
            verify(delegate).setDone(true);
            verify(delegate).latchCountDown();
        }
    }

    @Test
    public void testGet_NotDone_InterruptedExceptionThrown() throws Exception {
        delegate.setDone(false);
        try {
            delegate.get();
            fail();
        } catch (InterruptedException e) {
            // expected
        }
    }

    @Test
    public void testGet_DoneWithResult_ResultReturned() throws Exception {
        delegate.setDone(true);
        delegate.setReturnObject("testResult");
        assertEquals("testResult", delegate.get());
    }

    @Test
    public void testGet_DoneWithoutResult_ExecutionExceptionThrown() throws Exception {
        delegate.setDone(true);
        delegate.setThrown(new RuntimeException("testException"));
        try {
            delegate.get();
            fail();
        } catch (ExecutionException e) {
            assertEquals("testException", e.getCause().getMessage());
        }
    }

    private class TestRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {

        @Override
        protected Callable<String> initCallable(DelegateExecution execution) {
            return () -> "testResult";
        }

    }

}
