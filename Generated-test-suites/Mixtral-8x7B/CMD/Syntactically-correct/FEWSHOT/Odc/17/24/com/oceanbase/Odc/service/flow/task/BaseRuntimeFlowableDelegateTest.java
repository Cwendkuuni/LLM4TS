package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
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
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.delegate.Expression;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.flowable.engine.impl.context.Context;
import org.flowable.engine.impl.interceptor.CommandContext;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.test.PluggableFlowableTestCase;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.task.Task;
import org.flowable.job.service.impl.asyncexecutor.AsyncExecutor;
import org.flowable.task.service.delegate.DelegateTask;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

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
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.FlowableEngineConfig;
import com.oceanbase.odc.service.flow.FlowEngine;
import com.oceanbase.odc.service.flow.FlowEngineImpl;
import com.oceanbase.odc.service.flow.Flows;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest extends PluggableFlowableTestCase {

@Mock
private DelegateExecution execution;

@Mock
private EventPublisher eventPublisher;

@Mock
private FlowInstanceRepository flowInstanceRepository;

@Mock
private FlowableAdaptor flowableAdaptor;

@Mock
private AsyncExecutor asyncExecutor;

@Mock
private CommandContext commandContext;

@Mock
private ProcessEngineConfigurationImpl processEngineConfiguration;

@Captor
private ArgumentCaptor<ExecutionListener> executionListenerArgumentCaptor;

private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

@Before
public void setup() {
// Initialize the baseRuntimeFlowableDelegate with mocked objects
baseRuntimeFlowableDelegate = spy(new BaseRuntimeFlowableDelegate<String>() {
@Override
protected Callable<String> initCallable(DelegateExecution execution) {
return () -> "Test Result";
}
});

// Set up mocked objects
when(baseRuntimeFlowableDelegate.getEventPublisher()).thenReturn(eventPublisher);
when(baseRuntimeFlowableDelegate.getFlowInstanceRepository()).thenReturn(flowInstanceRepository);
when(baseRuntimeFlowableDelegate.getFlowableAdaptor()).thenReturn(flowableAdaptor);
when(baseRuntimeFlowableDelegate.getAsyncExecutor()).thenReturn(asyncExecutor);
when(baseRuntimeFlowableDelegate.getCommandContext()).thenReturn(commandContext);
when(baseRuntimeFlowableDelegate.getProcessEngineConfiguration()).thenReturn(processEngineConfiguration);

// Initialize the mocked execution object
ExecutionEntity executionEntity = new ExecutionEntity();
executionEntity.setId("1");
when(execution.getId()).thenReturn(executionEntity.getId());
when(execution.getProcessDefinitionId()).thenReturn("TestProcessDefinitionId");
when(execution.getProcessEngineServices()).thenReturn(runtimeService, taskService, managementService);
}

@Test
public void testIsDone() throws InterruptedException, ExecutionException, TimeoutException {
// Test when the task is done
baseRuntimeFlowableDelegate.setDone(true);
assertTrue(baseRuntimeFlowableDelegate.isDone());

// Test when the task is not done
baseRuntimeFlowableDelegate.setDone(false);
assertFalse(baseRuntimeFlowableDelegate.isDone());
}

@Test
public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
// Test when the task is done
baseRuntimeFlowableDelegate.setDone(true);
baseRuntimeFlowableDelegate.setReturnObject("Test Result");
assertEquals("Test Result", baseRuntimeFlowableDelegate.get());

// Test when the task is not done
baseRuntimeFlowableDelegate.setDone(false);
try {
baseRuntimeFlowableDelegate.get();
} catch (ExecutionException e) {
assertTrue(e.getCause() instanceof InterruptedException);
}
}

@Test
public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
// Test when the task is done
baseRuntimeFlowableDelegate.setDone(true);
baseRuntimeFlowableDelegate.setReturnObject("Test Result");
assertEquals("Test Result", baseRuntimeFlowableDelegate.get(10, TimeUnit.SECONDS));

// Test when the task is not done within the timeout
baseRuntimeFlowableDelegate.setDone(false);
try {
baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS);
} catch (TimeoutException e) {
// Expected behavior
}
}

@Test
public void testPreHandle() throws Exception {
// Call the preHandle method
baseRuntimeFlowableDelegate.preHandle(execution);

// Verify that no exceptions were thrown
verify(execution, never()).setVariable(anyString(), any());
verify(execution, never()).setErrorMessage(anyString());
verify(execution, never()).setErrorMessage(anyString(), any(Exception.class));
}

@Test
public void testPreHandleWithException() throws Exception {
doThrow(new Exception("Test Exception")).when(baseRuntimeFlowableDelegate).preHandle(execution);

// Call the preHandle method
try {
baseRuntimeFlowableDelegate.preHandle(execution);
} catch (Exception e) {
verify(execution).setErrorMessage(eq("Test Exception"), eq(e));
}
}

@Test
public void testInitTargetTaskInstanceId() throws Exception {
// Set up mocked objects
when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
.thenReturn(Optional.of(1L))
.thenReturn(Optional.empty());

FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
flowTaskInstance.setId(1L);
flowTaskInstance.setTaskType(TaskType.SERVICE_TASK);
when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
.thenReturn(Optional.of(flowTaskInstance))
.thenReturn(Optional.empty());

// Call the initTargetTaskInstanceId method
baseRuntimeFlowableDelegate.initTargetTaskInstanceId(execution);

// Verify the results
verify(execution).setVariable("flowInstanceId", 1L);
verify(execution).setVariable("targetTaskInstanceId", 1L);
verify(execution).setVariable("taskType", TaskType.SERVICE_TASK);
verify(execution).setVariable("strategyConfig", null);
}

@Test(expected = ServiceTaskError.class)
public void testInitTargetTaskInstanceIdWhenProcessDefinitionIdNotFound() throws Exception {
// Set up mocked objects
when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
.thenReturn(Optional.empty());

// Call the initTargetTaskInstanceId method
baseRuntimeFlowableDelegate.initTargetTaskInstanceId(execution);
}

@Test
public void testInitTargetTaskInstanceIdWhenActivityIdNotFound() throws Exception {
// Set up mocked objects
when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
.thenReturn(Optional.of(1L));

when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
.thenReturn(Optional.empty());

// Call the initTargetTaskInstanceId method
try {
baseRuntimeFlowableDelegate.initTargetTaskInstanceId(execution);
} catch (ServiceTaskError e) {
verify(execution).setErrorMessage(eq("Task instance not found for activityId: " + execution.getCurrentActivityId()),
eq(e));
}
}

@Test
public void testBindToFlowTaskInstance() throws Exception {
FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
flowTaskInstance.setId(1L);
flowTaskInstance.setTaskType(TaskType.SERVICE_TASK);
flowTaskInstance.bindServiceTask(baseRuntimeFlowableDelegate);

// Verify the results
verify(flowTaskInstance).bindServiceTask(baseRuntimeFlowableDelegate);
verify(execution).setVariable("targetTaskInstanceId", 1L);
verify(execution).setVariable("taskType", TaskType.SERVICE_TASK);
verify(execution).setVariable("strategyConfig", null);
}

@Test
public void testUpdateFlowInstanceStatus() throws Exception {
// Set up mocked objects
CommandContext commandContext = mock(CommandContext.class);
ProcessEngineConfigurationImpl processEngineConfiguration = mock(ProcessEngineConfigurationImpl.class);

when(processEngineConfiguration.getCommandContext())
.thenReturn(commandContext);

Context.setProcessEngineConfiguration(processEngineConfiguration);

// Call the updateFlowInstanceStatus method
baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

// Verify the results
verify(commandContext).updateFlowInstanceStatusById(anyLong(), eq(FlowStatus.COMPLETED));
}

@Test
public void testInitCallable() throws Exception {
// Call the initCallable method
Callable<String> callable = baseRuntimeFlowableDelegate.initCallable(execution);

// Verify the results
assertNotNull(callable);
verify(execution).getProcessDefinitionId();
verify(execution).getCurrentActivityId();
}
}