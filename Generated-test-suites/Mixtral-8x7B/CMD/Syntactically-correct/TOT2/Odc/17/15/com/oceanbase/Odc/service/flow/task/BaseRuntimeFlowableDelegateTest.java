
package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.FlowableAdaptor;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowInstanceRepositoryImpl;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;

@Slf4j
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

        baseRuntimeFlowableDelegate = spy(new TestBaseRuntimeFlowableDelegate());

        when(baseRuntimeFlowableDelegate.getFlowableAdaptor()).thenReturn(flowableAdaptor);
        when(baseRuntimeFlowableDelegate.getEventPublisher()).thenReturn(eventPublisher);
        when(baseRuntimeFlowableDelegate.getFlowInstanceRepository()).thenReturn(flowInstanceRepository);

        doAnswer(invocation -> {
            FlowTaskInstance flowTaskInstance = invocation.getArgument(0);
            flowTaskInstance.bindServiceTask(baseRuntimeFlowableDelegate);
            return null;
        }).when(eventPublisher).addEventListener(any(TaskInstanceCreatedListener.class));

        doAnswer(invocation -> {
            ServiceTaskStartedEvent event = invocation.getArgument(0);
            event.run();
            return null;
        }).when(eventPublisher).addEventListener(any(TaskInstanceCreatedListener.class));

        doAnswer(invocation -> {
            ServiceTaskStartedEvent event = invocation.getArgument(0);
            event.run();
            return null;
        }).when(eventPublisher).addEventListener(any(ActiveTaskStatisticsListener.class));

        doAnswer(invocation -> {
            ServiceTaskStartedEvent event = invocation.getArgument(0);
            event.run();
            return null;
        }).when(eventPublisher).removeEventListener(any(TaskInstanceCreatedListener.class));

        doAnswer(invocation -> {
            ServiceTaskStartedEvent event = invocation.getArgument(0);
            event.run();
            return null;
        }).when(eventPublisher).removeEventListener(any(ActiveTaskStatisticsListener.class));
    }

    @Test
    public void testRun() throws Exception {
        when(delegateExecution.getCurrentActivityId()).thenReturn("activityId");

        Callable<String> callable = () -> "returnObject";

        when(baseRuntimeFlowableDelegate.initCallable(delegateExecution)).thenReturn(callable);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(baseRuntimeFlowableDelegate);
        String result = future.get();

        assertEquals("returnObject", result);
        assertTrue(baseRuntimeFlowableDelegate.isDone());
        verify(baseRuntimeFlowableDelegate, times(1)).run(delegateExecution);
        verify(baseRuntimeFlowableDelegate, times(1)).initTargetTaskInstanceId(delegateExecution);
        verify(baseRuntimeFlowableDelegate, times(1)).preHandle(delegateExecution);
        verify(callable, times(1)).call();
        verify(eventPublisher, times(1))
                .publishEvent(new ServiceTaskStartedEvent(baseRuntimeFlowableDelegate, Thread.currentThread()));
        verify(eventPublisher, times(1)).removeEventListener(baseRuntimeFlowableDelegate.getTaskInstanceCreatedlistener());
        verify(eventPublisher, times(1)).removeEventListener(baseRuntimeFlowableDelegate.getActiveTaskStatisticsListener());
    }

    @Test
    public void testRun_initializationException() throws Exception {
        when(delegateExecution.getCurrentActivityId()).thenReturn("activityId");

        when(baseRuntimeFlowableDelegate.initCallable(delegateExecution)).thenThrow(new RuntimeException("init exception"));

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(baseRuntimeFlowableDelegate);

        try {
            future.get();
            assertTrue(false);
        } catch (ExecutionException e) {
            assertEquals("init exception", e.getCause().getMessage());
        }
        assertTrue(baseRuntimeFlowableDelegate.isDone());
        verify(baseRuntimeFlowableDelegate, times(1)).run(delegateExecution);
        verify(baseRuntimeFlowableDelegate, times(1)).initTargetTaskInstanceId(delegateExecution);
        verify(eventPublisher, never())
                .publishEvent(new ServiceTaskStartedEvent(baseRuntimeFlowableDelegate, Thread.currentThread()));
        verify(eventPublisher, times(1))
                .publishEvent(new ServiceTaskStartedEvent(baseRuntimeFlowableDelegate, Thread.currentThread()));
        verify(eventPublisher, times(1)).removeEventListener(baseRuntimeFlowableDelegate.getTaskInstanceCreatedlistener());
        verify(eventPublisher, times(1)).removeEventListener(baseRuntimeFlowableDelegate.getActiveTaskStatisticsListener());
    }

    @Test
    public void testRun_serviceTaskException() throws Exception {
        when(delegateExecution.getCurrentActivityId()).thenReturn("activityId");

        Callable<String> callable = () -> {
            throw new RuntimeException("serviceTask exception");
        };

        when(baseRuntimeFlowableDelegate.initCallable(delegateExecution)).thenReturn(callable);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(baseRuntimeFlowableDelegate);

        try {
            future.get();
            assertTrue(false);
        } catch (ExecutionException e) {
            assertEquals("serviceTask exception", e.getCause().getMessage());
        }
        assertTrue(baseRuntimeFlowableDelegate.isDone());
        verify(baseRuntimeFlowableDelegate, times(1)).run(delegateExecution);
        verify(baseRuntimeFlowableDelegate, times(1)).initTargetTaskInstanceId(delegateExecution);
        verify(baseRuntimeFlowableDelegate, times(1)).preHandle(delegateExecution);
        verify(callable, times(1)).call();
        verify(eventPublisher, times(1))
                .publishEvent(new ServiceTaskStartedEvent(baseRuntimeFlowableDelegate, Thread.currentThread()));
        verify(eventPublisher, times(1)).removeEventListener(baseRuntimeFlowableDelegate.getTaskInstanceCreatedlistener());
        verify(eventPublisher, times(1)).removeEventListener(baseRuntimeFlowableDelegate.getActiveTaskStatisticsListener());
    }

    @Test
    public void testPreHandle() {
        baseRuntimeFlowableDelegate.preHandle(delegateExecution);

        verify(baseRuntimeFlowableDelegate, times(1)).preHandle(delegateExecution);
    }

    @Test
    public void testIsDone() {
        assertFalse(baseRuntimeFlowableDelegate.isDone());

        baseRuntimeFlowableDelegate.done = true;

        assertTrue(baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        when(baseRuntimeFlowableDelegate.get()).thenReturn("returnObject");

        String result = baseRuntimeFlowableDelegate.get();

        assertEquals("returnObject", result);
    }

    @Test
    public void testGet_timeout() throws InterruptedException, ExecutionException, TimeoutException {
        when(baseRuntimeFlowableDelegate.get(1, TimeUnit.MILLISECONDS))
                .thenThrow(new TimeoutException("timeout"));

        try {
            baseRuntimeFlowableDelegate.get(1, TimeUnit.MILLISECONDS);
            assertTrue(false);
        } catch (TimeoutException e) {
            assertEquals("timeout", e.getMessage());
        }
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        when(delegateExecution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        when(flowableAdaptor
                .getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(Optional.of(1L));

        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

        verify(baseRuntimeFlowableDelegate, times(1))
                .initTargetTaskInstanceId(delegateExecution);
        verify(flowableAdaptor, times(1))
                .getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
    }

    @Test
    public void testInitTargetTaskInstanceId_flowInstanceNotFound() {
        when(delegateExecution.getProcessDefinitionId()).thenReturn("processDefinitionId");
        when(flowableAdaptor
                .getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenReturn(Optional.empty());

        try {
            baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);
            assertTrue(false);
        } catch (ServiceTaskError e) {
            assertEquals("Initialization task failed", e.getMessage());
        }

        verify(flowableAdaptor, times(3))
                .getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);
        when(flowTaskInstance.getId()).thenReturn(1L);

        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);

        verify(flowTaskInstance, times(1)).bindServiceTask(baseRuntimeFlowableDelegate);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class)))
                .thenReturn(true);

        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.RUNNING);

        verify(flowInstanceRepository, times(1))
                .updateStatusById(anyLong(), any(FlowStatus.class));
    }

    @Test
    public void testUpdateFlowInstanceStatus_flowInstanceNotFound() {
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class)))
                .thenReturn(false);

        try {
            baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.RUNNING);
            assertTrue(false);
        } catch (ServiceTaskError e) {
            assertEquals("Initialization task failed", e.getMessage());
        }

        verify(flowInstanceRepository, times(1))
                .updateStatusById(anyLong(), any(FlowStatus.class));
    }

    private class TestBaseRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {

        @Autowired
        private FlowableAdaptor flowableAdaptor;

        @Autowired
        private EventPublisher eventPublisher;

        @Autowired
        private FlowInstanceRepository flowInstanceRepository;

        @Override
        protected Callable<String> initCallable(DelegateExecution execution) {
            return () -> "returnObject";
        }

        @Override
        public FlowableAdaptor getFlowableAdaptor() {
            return flowableAdaptor;
        }

        @Override
        public EventPublisher getEventPublisher() {
            return eventPublisher;
        }

        @Override
        public FlowInstanceRepository getFlowInstanceRepository() {
            return flowInstanceRepository;
        }

    }

}
