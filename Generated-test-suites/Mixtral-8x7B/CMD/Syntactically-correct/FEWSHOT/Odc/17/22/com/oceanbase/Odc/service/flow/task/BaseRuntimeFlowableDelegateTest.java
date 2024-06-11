package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.Expression;
import org.flowable.engine.delegate.VariableScope;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.util.ExpressionUtil;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.FlowableAdaptor;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptorSpy;
import com.oceanbase.odc.service.flow.FlowInstanceRepositorySpy;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfigSpy;

import lombok.extern.slf4j.Slf4j;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {

    private static final String PROCESS_DEFINITION_ID = "processDefinitionId";
    private static final String ACTIVITY_ID = "activityId";
    private static final Long FLOW_INSTANCE_ID = 1L;
    private static final Long TASK_INSTANCE_ID = 2L;
    private static final TaskType TASK_TYPE = TaskType.SERVICE_TASK;
    private static final ExecutionStrategyConfig STRATEGY_CONFIG = new ExecutionStrategyConfig();
    private static final String EXCEPTION_MESSAGE = "Exception message";

    @Mock
    private VariableScope variableScope;

    @Mock
    private DelegateExecution delegateExecution;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private TaskEntity taskEntity;

    @Mock
    private Expression expression;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

    private FlowableAdaptorSpy flowableAdaptorSpy;

    private FlowInstanceRepositorySpy flowInstanceRepositorySpy;

    private ExecutionStrategyConfigSpy strategyConfigSpy;

    @Before
    public void setUp() {
        flowableAdaptorSpy = spy(flowableAdaptor);
        ReflectionTestUtils.setField(baseRuntimeFlowableDelegate, "flowableAdaptor", flowableAdaptorSpy);

        flowInstanceRepositorySpy = spy(flowInstanceRepository);
        ReflectionTestUtils.setField(baseRuntimeFlowableDelegate, "flowInstanceRepository",
                flowInstanceRepositorySpy);

        strategyConfigSpy = spy(STRATEGY_CONFIG);
        ReflectionTestUtils.setField(baseRuntimeFlowableDelegate, "strategyConfig", strategyConfigSpy);

        doReturn(taskEntity).when(delegateExecution).getTask();
        doReturn(expression).when(variableScope).getExpression(anyString());
        doReturn(delegateExecution).when(variableScope).getExecution();

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(PROCESS_DEFINITION_ID))
                .thenReturn(Optional.of(FLOW_INSTANCE_ID));

        when(flowableAdaptor.getTaskInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID))
                .thenReturn(Optional.of(taskEntity));

        taskEntity.setId(TASK_INSTANCE_ID);
        taskEntity.setTaskType(TASK_TYPE);
        taskEntity.setStrategyConfig(STRATEGY_CONFIG);

        doNothing().when(eventPublisher).addEventListener(any(TaskInstanceCreatedListener.class));
        doNothing().when(eventPublisher).addEventListener(any(ActiveTaskStatisticsListener.class));
        doNothing().when(eventPublisher).removeEventListener(any(TaskInstanceCreatedListener.class));
        doNothing().when(eventPublisher).removeEventListener(any(ActiveTaskStatisticsListener.class));
        doReturn(eventPublisher).when(applicationEventPublisher).getEventPublisher();
    }

    @Test
    public void testRun() throws Exception {
        baseRuntimeFlowableDelegate.run(delegateExecution);

        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(PROCESS_DEFINITION_ID);
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID);
        verify(eventPublisher, times(1)).removeEventListener(taskInstanceCreatedlistener);
        verify(eventPublisher, times(1)).removeEventListener(activeTaskStatisticsListener);
    }

    @Test
    public void testRunWithException() throws Exception {
        doThrow(new ServiceTaskError(EXCEPTION_MESSAGE)).when(baseRuntimeFlowableDelegate).execute(delegateExecution);

        baseRuntimeFlowableDelegate.run(delegateExecution);

        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(PROCESS_DEFINITION_ID);
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID);
        verify(eventPublisher, times(1)).removeEventListener(taskInstanceCreatedlistener);
        verify(eventPublisher, times(1)).removeEventListener(activeTaskStatisticsListener);
    }

    @Test
    public void testInitTargetTaskInstanceId() throws Exception {
        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(PROCESS_DEFINITION_ID);
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID);

        assertEquals(TASK_INSTANCE_ID, baseRuntimeFlowableDelegate.getTargetTaskInstanceId());
        assertEquals(TASK_TYPE, baseRuntimeFlowableDelegate.getTaskType());
        assertEquals(STRATEGY_CONFIG, baseRuntimeFlowableDelegate.getStrategyConfig());
        assertEquals(FLOW_INSTANCE_ID, baseRuntimeFlowableDelegate.getFlowInstanceId());
    }

    @Test
    public void testIsDone() throws Exception {
        assertTrue(baseRuntimeFlowableDelegate.isDone());

        baseRuntimeFlowableDelegate.setDone(false);

        assertFalse(baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet() throws Exception {
        when(flowableAdaptor.getTaskInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID)).thenReturn(Optional.empty());

        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

        assertNull(baseRuntimeFlowableDelegate.get());
    }

    @Test
    public void testGetWithException() throws Exception {
        ExecutionException exception = new ExecutionException(EXCEPTION_MESSAGE);

        doThrow(exception).when(baseRuntimeFlowableDelegate).get();

        try {
            baseRuntimeFlowableDelegate.get();
            assertTrue(false);
        } catch (ExecutionException e) {
            assertEquals(EXCEPTION_MESSAGE, e.getMessage());
        }
    }

    @Test
    public void testGetWithTimeout() throws Exception {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        AtomicReference<CountDownLatch> countDownLatchRef = new AtomicReference<>(new CountDownLatch(1));
        AtomicBoolean doneRef = new AtomicBoolean(false);

        doAnswer(invocation -> {
            CountDownLatch countDownLatch = countDownLatchRef.get();
            doneRef.set(true);
            countDownLatch.countDown();
            return null;
        }).when(baseRuntimeFlowableDelegate).setDone(anyBoolean());

        doAnswer(invocation -> {
            CountDownLatch countDownLatch = countDownLatchRef.get();
            countDownLatch.await();
            return null;
        }).when(baseRuntimeFlowableDelegate).getLatch().await();

        Future<String> future = spy(new BaseRuntimeFlowableDelegateFuture<>(baseRuntimeFlowableDelegate,
                scheduledExecutorService, Duration.ofSeconds(1)));

        future.get(1, TimeUnit.SECONDS);

        assertTrue(doneRef.get());
    }

    @Test
    public void testGetWithTimeoutException() throws Exception {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        Future<String> future = spy(new BaseRuntimeFlowableDelegateFuture<>(baseRuntimeFlowableDelegate,
                scheduledExecutorService, Duration.ofSeconds(1)));

        try {
            future.get(0, TimeUnit.SECONDS);
            assertTrue(false);
        } catch (TimeoutException e) {
            // expected
        }
    }

    @Test
    public void testBindToFlowTaskInstance() throws Exception {
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);

        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);

        verify(flowTaskInstance, times(1)).bindServiceTask(baseRuntimeFlowableDelegate);
    }

    @Test
    public void testUpdateFlowInstanceStatus() throws Exception {
        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.FINISHED);

        verify(flowInstanceRepositorySpy, times(1)).updateStatusById(FLOW_INSTANCE_ID, FlowStatus.FINISHED);
    }

    private class BaseRuntimeFlowableDelegateFuture<V> implements Future<V> {

        private BaseRuntimeFlowableDelegate<V> delegate;

        private ScheduledExecutorService scheduledExecutorService;

        private Duration timeout;

        public BaseRuntimeFlowableDelegateFuture(BaseRuntimeFlowableDelegate<V> delegate,
                ScheduledExecutorService scheduledExecutorService, Duration timeout) {
            this.delegate = delegate;
            this.scheduledExecutorService = scheduledExecutorService;
            this.timeout = timeout;
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return false;
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public boolean isDone() {
            return delegate.isDone();
        }

        @Override
        public V get() throws InterruptedException, ExecutionException {
            CountDownLatch latch = delegate.getLatch();
            latch.await();
            if (delegate.isCancelled()) {
                throw new CancellationException();
            }
            if (delegate.getException() != null) {
                throw new ExecutionException(delegate.getException());
            }
            return delegate.getReturnObject();
        }

        @Override
        public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            if (!scheduledExecutorService.schedule(
                    () -> latch.countDown(), timeout, TimeUnit.MILLISECONDS).isDone()) {
                throw new TimeoutException();
            }
            return get();
        }

    }

}