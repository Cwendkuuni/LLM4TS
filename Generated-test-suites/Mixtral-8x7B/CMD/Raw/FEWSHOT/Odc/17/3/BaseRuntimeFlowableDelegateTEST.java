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
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.delegate.Expression;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.flowable.engine.impl.context.Context;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.test.PluggableFlowableTestCase;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.task.Task;
import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.variable.api.event.VariableScopeEventListener;
import org.flowable.variable.api.event.VariableScopeEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

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
import com.oceanbase.odc.service.flow.config.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {

@Mock
private DelegateExecution delegateExecution;

@Mock
private FlowableAdaptor flowableAdaptor;

@Mock
private EventPublisher eventPublisher;

@Mock
private FlowInstanceRepository flowInstanceRepository;

@Mock
private RetryExecutor retryExecutor;

@Mock
private TaskInstanceCreatedListener taskInstanceCreatedlistener;

@Mock
private ActiveTaskStatisticsListener activeTaskStatisticsListener;

private BaseRuntimeFlowableDelegate baseRuntimeFlowableDelegate;

@Before
public void setUp() {
baseRuntimeFlowableDelegate = spy(new TestBaseRuntimeFlowableDelegate());

doReturn(flowableAdaptor).when(baseRuntimeFlowableDelegate).getFlowableAdaptor();
doReturn(eventPublisher).when(baseRuntimeFlowableDelegate).getEventPublisher();
doReturn(retryExecutor).when(baseRuntimeFlowableDelegate).getRetryExecutor();

}

@Test
public void testIsDone() throws ExecutionException, InterruptedException {
Future<String> future = baseRuntimeFlowableDelegate.getFuture();

assertFalse(future.isDone());

baseRuntimeFlowableDelegate.get();

assertTrue(future.isDone());
}

@Test
public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
Future<String> future = baseRuntimeFlowableDelegate.getFuture();

String result = future.get(1, TimeUnit.SECONDS);

assertEquals("Test Result", result);
}

@Test(expected = ExecutionException.class)
public void testGetException() throws InterruptedException, ExecutionException, TimeoutException {
Future<String> future = baseRuntimeFlowableDelegate.getFuture();

doThrow(new RuntimeException("Test Exception")).when(baseRuntimeFlowableDelegate).get();

future.get();
}

@Test
public void testPreHandle() throws Exception {
baseRuntimeFlowableDelegate.preHandle(delegateExecution);

verify(delegateExecution, times(1)).getProcessDefinitionId();
verify(delegateExecution, times(1)).getCurrentActivityId();
}

@Test
public void testInitTargetTaskInstanceId() {
doReturn("Test Process Definition Id").when(delegateExecution).getProcessDefinitionId();

TaskInstanceCreatedListener taskInstanceCreatedlistener = mock(TaskInstanceCreatedListener.class);
ActiveTaskStatisticsListener activeTaskStatisticsListener = mock(ActiveTaskStatisticsListener.class);

baseRuntimeFlowableDelegate = new TestBaseRuntimeFlowableDelegate(flowableAdaptor, eventPublisher,
flowInstanceRepository, retryExecutor, taskInstanceCreatedlistener, activeTaskStatisticsListener);

ArgumentCaptor<ExecutionListener> executionListenerArgumentCaptor = ArgumentCaptor.forClass(ExecutionListener.class);

baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

verify(delegateExecution, times(1)).addExecutionListener(executionListenerArgumentCaptor.capture());

ExecutionListener executionListener = executionListenerArgumentCaptor.getValue();

assertEquals(TaskInstanceCreatedListener.EVENT_NAME, executionListener.getEventName());
verify(taskInstanceCreatedlistener, times(1)).onEvent(any());

assertEquals(ActiveTaskStatisticsListener.EVENT_NAME, executionListener.getEventName());
verify(activeTaskStatisticsListener, times(1)).onEvent(any());
}

@Test
public void testBindToFlowTaskInstance() {
FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);

doReturn(1L).when(flowTaskInstance).getId();
doNothing().when(flowTaskInstance).bindServiceTask(baseRuntimeFlowableDelegate);

baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);

verify(flowTaskInstance, times(1)).bindServiceTask(baseRuntimeFlowableDelegate);
}

@Test
public void testUpdateFlowInstanceStatus() {
FlowInstanceRepository flowInstanceRepository = mock(FlowInstanceRepository.class);

baseRuntimeFlowableDelegate = new TestBaseRuntimeFlowableDelegate(flowableAdaptor, eventPublisher,
flowInstanceRepository, retryExecutor, taskInstanceCreatedlistener, activeTaskStatisticsListener);

baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

verify(flowInstanceRepository, times(1)).updateStatusById(anyLong(), any());
}

@Test
public void testInitCallable() throws Exception {
Expression expression = mock(Expression.class);

doReturn("Test Callable").when(expression).getValue(delegateExecution);

baseRuntimeFlowableDelegate = new TestBaseRuntimeFlowableDelegate(flowableAdaptor, eventPublisher,
flowInstanceRepository, retryExecutor, taskInstanceCreatedlistener, activeTaskStatisticsListener) {
@Override
protected Callable<String> initCallable(DelegateExecution execution) {
return () -> expression.getValue(execution).toString();
}
};

String result = baseRuntimeFlowableDelegate.call();

assertEquals("Test Callable", result);
}

private static class TestBaseRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {

public TestBaseRuntimeFlowableDelegate() {
super();
}

public TestBaseRuntimeFlowableDelegate(FlowableAdaptor flowableAdaptor, EventPublisher eventPublisher,
FlowInstanceRepository flowInstanceRepository, RetryExecutor retryExecutor,
TaskInstanceCreatedListener taskInstanceCreatedlistener,
ActiveTaskStatisticsListener activeTaskStatisticsListener) {
super(flowableAdaptor, eventPublisher, flowInstanceRepository, retryExecutor,
taskInstanceCreatedlistener, activeTaskStatisticsListener);
}

@Override
protected Callable<String> initCallable(DelegateExecution execution) {
return () -> "Test Result";
}

@Override
public String call() throws Exception {
return super.call();
}
}

}