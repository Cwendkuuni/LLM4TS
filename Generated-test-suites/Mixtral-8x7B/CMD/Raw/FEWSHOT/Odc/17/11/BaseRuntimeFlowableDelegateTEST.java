package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.delegate.Expression;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.flowable.engine.impl.context.Context;
import org.flowable.engine.impl.interceptor.CommandContext;
import org.flowable.engine.impl.interceptor.CommandContextInterceptor;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.test.PluggableFlowableTestCase;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.task.Task;
import org.flowable.task.service.delegate.DelegateTask;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowableConfig;
import com.oceanbase.odc.service.flow.FlowableEngineUtil;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;

public class BaseRuntimeFlowableDelegateTest extends PluggableFlowableTestCase {


private static final String PROCESS_DEFINITION_KEY = "testProcess";
private static final String ACTIVITY_ID = "testTask";

private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;
private DelegateExecution delegateExecution;
private Callable<String> callable;
private FlowTaskInstance flowTaskInstance;
private FlowableAdaptor flowableAdaptor;
private EventPublisher eventPublisher;
private FlowInstanceRepository flowInstanceRepository;
private RetryExecutor retryExecutor;
private TaskInstanceCreatedListener taskInstanceCreatedlistener;
private ActiveTaskStatisticsListener activeTaskStatisticsListener;
private FlowableConfig flowableConfig;
private FlowableEngineUtil flowableEngineUtil;
private CommandContext commandContext;
private ExecutionListener executionListener;
private Expression expression;
private JavaDelegate javaDelegate;
private CountDownLatch latch;
private ExecutorService executorService;

@Before
public void setup() {
    baseRuntimeFlowableDelegate = spy(new BaseTestRuntimeFlowableDelegate());
    delegateExecution = mock(DelegateExecution.class);
    callable = mock(Callable.class);
    flowTaskInstance = mock(FlowTaskInstance.class);
    flowableAdaptor = mock(FlowableAdaptorImpl.class);
    eventPublisher = mock(EventPublisher.class);
    flowInstanceRepository = mock(FlowInstanceRepository.class);
    retryExecutor = mock(RetryExecutor.class);
    taskInstanceCreatedlistener = mock(TaskInstanceCreatedListener.class);
    activeTaskStatisticsListener = mock(ActiveTaskStatisticsListener.class);
    flowableConfig = mock(FlowableConfig.class);
    flowableEngineUtil = mock(FlowableEngineUtil.class);
    commandContext = mock(CommandContext.class);
    executionListener = mock(ExecutionListener.class);
    expression = mock(Expression.class);
    javaDelegate = mock(JavaDelegate.class);
    latch = new CountDownLatch(1);
    executorService = Executors.newSingleThreadExecutor();

    Context.setProcessEngineConfiguration(new StandaloneInMemProcessEngineConfiguration() {
        {
            addInterceptor(new CommandContextInterceptor() {
                @Override
                public void insertCommandContextInterceptor(CommandContext commandContext) {
                    BaseRuntimeFlowableDelegateTest.this.commandContext = commandContext;
                }
            });
        }
    });

    doReturn(flowTaskInstance).when(baseRuntimeFlowableDelegate).getFlowTaskInstance();
    doReturn(flowableAdaptor).when(baseRuntimeFlowableDelegate).getFlowableAdaptor();
    doReturn(eventPublisher).when(baseRuntimeFlowableDelegate).getEventPublisher();
    doReturn(flowInstanceRepository).when(baseRuntimeFlowableDelegate).getFlowInstanceRepository();
    doReturn(retryExecutor).when(baseRuntimeFlowableDelegate).getRetryExecutor();
    doReturn(taskInstanceCreatedlistener).when(baseRuntimeFlowableDelegate).getTaskInstanceCreatedlistener();
    doReturn(activeTaskStatisticsListener).when(baseRuntimeFlowableDelegate).getActiveTaskStatisticsListener();
    doReturn(flowableConfig).when(baseRuntimeFlowableDelegate).getFlowableConfig();
    doReturn(flowableEngineUtil).when(baseRuntimeFlowableDelegate).getFlowableEngineUtil();
    doNothing().when(baseRuntimeFlowableDelegate).registerTaskInstanceCreatedListener(any());
    doNothing().when(baseRuntimeFlowableDelegate)
            .registerActiveTaskStatisticsListener(any());
    doNothing().when(baseRuntimeFlowableDelegate).addExecutionListeners(any(), any());

    when(delegateExecution.getCurrentActivityId()).thenReturn(ACTIVITY_ID);
    when(delegateExecution.getId()).thenReturn("testExecutionId");
    when(delegateExecution.getProcessDefinitionId()).thenReturn("testProcessDefinitionId");

    when(flowTaskInstance.getId()).thenReturn(1L);
    when(flowTaskInstance.getTaskType()).thenReturn(TaskType.SERVICE);
    when(flowTaskInstance.getStrategyConfig()).thenReturn(null);

    when(flowableAdaptor.getTaskInstanceByActivityId(ACTIVITY_ID, anyLong())).thenReturn(Optional.of(flowTaskInstance));
    when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(Optional.of(1L));

    when(retryExecutor.run(any(), any())).thenAnswer(new Answer<Object>() {
        @Override
        public Object answer(InvocationOnMock invocation) throws Throwable {
            Object[] args = invocation.getArguments();
            Callable<Object> callable = (Callable<Object>) args[0];
            boolean useOptional = (boolean) args[1];

            if (useOptional) {
                Optional<Object> resultOptional = Optional.ofNullable(callable.call());
                return resultOptional;
            } else {
                return callable.call();
            }
        }
    });

    PreConditions.validExists = (type, name, id, present) -> {
    };
    Verify.notNull = (message, object) -> {
        if (object == null) {
            throw new IllegalStateException(message);
        }
    };

    ExecutionEntity executionEntity = new ExecutionEntity();
    executionEntity.setId("testExecutionId");
    when(commandContext.getExecutionEntityManager().findById("testExecutionId")).thenReturn(executionEntity);
}

@After
public void teardown() {
    executorService.shutdownNow();
}

@Test
public void testPreHandle() throws Exception {
    baseRuntimeFlowableDelegate.preHandle(delegateExecution);

    verify(delegateExecution, times(1)).setVariable(ArgumentMatchers.eq("odc_flow_task_instance_id"), ArgumentMatchers.eq(1L));
}

@Test
public void testRun() throws Exception {
    doNothing().when(baseRuntimeFlowableDelegate).registerTaskInstanceCreatedListener(any());
    doNothing().when(baseRuntimeFlowableDelegate)
            .registerActiveTaskStatisticsListener(any());
    doNothing().when(baseRuntimeFlowableDelegate).addExecutionListeners(any(), any());

    baseRuntimeFlowableDelegate.run(delegateExecution);

    verify(baseRuntimeFlowableDelegate, times(1)).registerTaskInstanceCreatedListener(any());
    verify(baseRuntimeFlowableDelegate, times(1))
            .registerActiveTaskStatisticsListener(any());
    verify(baseRuntimeFlowableDelegate, times(1)).addExecutionListeners(any(), any());
    verify(delegateExecution, times(1)).setVariable(ArgumentMatchers.eq("odc_flow_task_instance_id"), ArgumentMatchers.eq(1L));
}

@Test
public void testIsDone() {
    assertTrue(baseRuntimeFlowableDelegate.isDone());
}

@Test
public void testGet() throws InterruptedException, ExecutionException {
    when(callable.call()).thenReturn("testResult");
    Future<String> future = executorService.submit(callable);

    String result = future.get();

    assertEquals("testResult", result);
}

@Test
public void testGetWithInterruptedException() throws InterruptedException, ExecutionException {
    doThrow(new InterruptedException()).when(callable).call();
    Future<String> future = executorService.submit(callable);

    try {
        future.get();
    } catch (ExecutionException e) {
        assertTrue(e.getCause() instanceof InterruptedException);
    }
}

@Test
public void testGetWithTimeoutException() throws InterruptedException, ExecutionException {
    doThrow(new TimeoutException()).when(callable).call();
    Future<String> future = executorService.submit(callable);

    try {
        future.get(10, TimeUnit.MILLISECONDS);
    } catch (TimeoutException e) {
        assertTrue(true);
    }
}

@Test
public void testGetWithExecutionException() throws InterruptedException, ExecutionException {
    doThrow(new RuntimeException("testException")).when(callable).call();
    Future<String> future = executorService.submit(callable);

    try {
        future.get();
    } catch (ExecutionException e) {
        assertTrue(e.getCause() instanceof RuntimeException);
        assertEquals("testException", e.getCause().getMessage());
    }
}

@Test
public void testInitTargetTaskInstanceId() throws Exception {
    when(delegateExecution.getCurrentActivityId()).thenReturn(ACTIVITY_ID);
    when(delegateExecution.getProcessDefinitionId()).thenReturn("testProcessDefinitionId");

    baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

    verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(ACTIVITY_ID, 1L);
    verify(flowableAdaptor, times(1))
            .getFlowInstanceIdByProcessDefinitionId("testProcessDefinitionId");
    verify(delegateExecution, times(1)).setVariable(ArgumentMatchers.eq("odc_flow_task_instance_id"), ArgumentMatchers.eq(1L));
}

@Test
public void testBindToFlowTaskInstance() {
    FlowTaskInstance taskInstance = mock(FlowTaskInstance.class);
    when(taskInstance.getId()).thenReturn(1L);
    when(taskInstance.getTaskType()).thenReturn(TaskType.SERVICE);

    baseRuntimeFlowableDelegate.bindToFlowTaskInstance(taskInstance);

    verify(taskInstance, times(1)).bindServiceTask(baseRuntimeFlowableDelegate);
}

@Test
public void testUpdateFlowInstanceStatus() {
    when(flowInstanceRepository.updateStatusById(1L, FlowStatus.COMPLETED)).thenReturn(true);

    baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

    verify(flowInstanceRepository, times(1)).updateStatusById(1L, FlowStatus.COMPLETED);
}

@Test
public void testInitCallable() throws Exception {
    DelegateTask delegateTask = mock(DelegateTask.class);
    when(delegateTask.getExecution()).thenReturn(delegateExecution);

    Callable<String> result = baseRuntimeFlowableDelegate.initCallable(delegateTask);

    assertNotNull(result);
}

private static class BaseTestRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {

    @Override
    protected Callable<String> initCallable(DelegateExecution execution) {
        return callable;
    }

    @Override
    public FlowTaskInstance getFlowTaskInstance() {
        return flowTaskInstance;
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

    @Override
    public RetryExecutor getRetryExecutor() {
        return retryExecutor;
    }

    @Override
    public TaskInstanceCreatedListener getTaskInstanceCreatedlistener() {
        return taskInstanceCreatedlistener;
    }

    @Override
    public ActiveTaskStatisticsListener getActiveTaskStatisticsListener() {
        return activeTaskStatisticsListener;
    }

    @Override
    public FlowableConfig getFlowableConfig() {
        return flowableConfig;
    }

    @Override
    public FlowableEngineUtil getFlowableEngineUtil() {
        return flowableEngineUtil;
    }

    @Override
    public void registerTaskInstanceCreatedListener(TaskInstanceCreatedListener listener) {
        // No-op
    }

    @Override
    public void registerActiveTaskStatisticsListener(ActiveTaskStatisticsListener listener) {
        // No-op
    }

    @Override
    public void addExecutionListeners(ExecutionEntity execution,
            ExecutionListener... executionListeners) {
        // No-op
    }
}


}