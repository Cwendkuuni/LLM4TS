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

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.FlowableAdaptor;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptorTest;
import com.oceanbase.odc.service.flow.FlowInstanceRepositoryTest;
import com.oceanbase.odc.service.flow.FlowTaskInstanceTest;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseRuntimeFlowableDelegateTest implements ApplicationEventPublisherAware {

 private BaseRuntimeFlowableDelegate<String> spyBaseRuntimeFlowableDelegate;
 private EventPublisher eventPublisher;
 private FlowableAdaptor flowableAdaptor;
 private FlowInstanceRepository flowInstanceRepository;
 private DelegateExecution delegateExecution;
 private Callable<String> callable;
 private ExecutionStrategyConfig executionStrategyConfig;
 private TaskInstanceCreatedListener taskInstanceCreatedListener;
 private RetryExecutor retryExecutor;
 private ApplicationEventPublisher applicationEventPublisher;

 @Before
 public void setup() {
 eventPublisher = mock(EventPublisher.class);
 flowableAdaptor = mock(FlowableAdaptor.class);
 flowInstanceRepository = mock(FlowInstanceRepository.class);
 delegateExecution = mock(DelegateExecution.class);
 callable = mock(Callable.class);
 executionStrategyConfig = new ExecutionStrategyConfig();
 taskInstanceCreatedListener = new TaskInstanceCreatedListener(spyBaseRuntimeFlowableDelegate);
 retryExecutor = mock(RetryExecutor.class);

 spyBaseRuntimeFlowableDelegate = spy(new BaseRuntimeFlowableDelegate<String>() {
 @Override
 protected Callable<String> initCallable(DelegateExecution execution) {
 return callable;
 }
 });

 doReturn(eventPublisher).when(spyBaseRuntimeFlowableDelegate).getEventPublisher();
 doReturn(flowableAdaptor).when(spyBaseRuntimeFlowableDelegate).getFlowableAdaptor();
 doReturn(flowInstanceRepository).when(spyBaseRuntimeFlowableDelegate).getFlowInstanceRepository();
 doReturn(executionStrategyConfig).when(spyBaseRuntimeFlowableDelegate).getStrategyConfig();
 doReturn(retryExecutor).when(spyBaseRuntimeFlowableDelegate).getRetryExecutor();
 doReturn(delegateExecution).when(spyBaseRuntimeFlowableDelegate).getDelegateExecution();
 doReturn(taskInstanceCreatedListener).when(spyBaseRuntimeFlowableDelegate).getTaskInstanceCreatedlistener();

 applicationEventPublisher = mock(ApplicationEventPublisher.class);
 spyBaseRuntimeFlowableDelegate.setApplicationEventPublisher(applicationEventPublisher);
 }

 @Test
 public void testInitialization() {
 // test initialization without exception
 doReturn("testFlowInstanceId").when(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId(anyString());
 doReturn(Optional.of(Optional.of(FlowTaskInstanceTest.createFlowTaskInstance()))).when(flowableAdaptor)
 .getTaskInstanceByActivityId(anyString(), anyLong());

 try {
 spyBaseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);
 } catch (ServiceTaskError e) {
 fail("Unexpected ServiceTaskError: " + e.getMessage());
 }

 verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId(anyString());
 verify(flowableAdaptor).getTaskInstanceByActivityId(anyString(), anyLong());
 }

 @Test(expected = ServiceTaskError.class)
 public void testInitializationWithException() {
 // test initialization with exception
 doThrow(new RuntimeException("test exception")).when(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId(anyString());

 try {
 spyBaseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);
 fail("Expected ServiceTaskError, but no exception was thrown");
 } catch (ServiceTaskError e) {
 verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId(anyString());
 }
 }

 @Test
 public void testPreHandle() throws Exception {
 doNothing().when(spyBaseRuntimeFlowableDelegate).preHandle(any(DelegateExecution.class));

 spyBaseRuntimeFlowableDelegate.preHandle(delegateExecution);

 verify(spyBaseRuntimeFlowableDelegate).preHandle(delegateExecution);
 }

 @Test
 public void testRunWithInitializationException() throws Exception {
 // test run with initialization exception
 doThrow(new ServiceTaskError("test initialization error")).when(spyBaseRuntimeFlowableDelegate)
 .initTargetTaskInstanceId(delegateExecution);

 try {
 spyBaseRuntimeFlowableDelegate.run(delegateExecution);
 fail("Expected ServiceTaskError, but no exception was thrown");
 } catch (ServiceTaskError e) {
 verify(spyBaseRuntimeFlowableDelegate).initTargetTaskInstanceId(delegateExecution);
 }
 }

 @Test
 public void testRunWithExecutionException() throws Exception {
 // test run with execution exception
 doThrow(new RuntimeException("test execution error")).when(callable).call();

 try {
 spyBaseRuntimeFlowableDelegate.run(delegateExecution);
 fail("Expected ExecutionException, but no exception was thrown");
 } catch (ExecutionException e) {
 verify(spyBaseRuntimeFlowableDelegate).call();
 }
 }

 @Test
 public void testRun() throws Exception {
 // test run without exception
 doReturn("testReturnObject").when(callable).call();

 spyBaseRuntimeFlowableDelegate.run(delegateExecution);

 verify(spyBaseRuntimeFlowableDelegate).initTargetTaskInstanceId(delegateExecution);
 verify(spyBaseRuntimeFlowableDelegate).call();
 }

 @Test
 public void testExecute() {
 // test execute
 doNothing().when(spyBaseRuntimeFlowableDelegate).execute(any(DelegateExecution.class));

 spyBaseRuntimeFlowableDelegate.execute(delegateExecution);

 verify(spyBaseRuntimeFlowableDelegate).execute(delegateExecution);
 }

 @Test
 public void testIsDone() {
 // test isDone
 doReturn(true).when(spyBaseRuntimeFlowableDelegate).isDone();
 assertTrue(spyBaseRuntimeFlowableDelegate.isDone());

 doReturn(false).when(spyBaseRuntimeFlowableDelegate).isDone();
 assertFalse(spyBaseRuntimeFlowableDelegate.isDone());
 }

 @Test
 public void testGet() throws Exception {
 // test get without exception
 doReturn("testReturnObject").when(spyBaseRuntimeFlowableDelegate).get();

 assertEquals("testReturnObject", spyBaseRuntimeFlowableDelegate.get());
 verify(spyBaseRuntimeFlowableDelegate).get();
 }

 @Test(expected = ExecutionException.class)
 public void testGetWithExecutionException() throws Exception {
 // test get with execution exception
 doThrow(new RuntimeException("test execution error")).when(spyBaseRuntimeFlowableDelegate).get();

 spyBaseRuntimeFlowableDelegate.get();
 fail("Expected ExecutionException, but no exception was thrown");
 }

 @Test
 public void testGetWithInterruptedException() throws Exception {
 // test get with interrupted exception
 doThrow(new InterruptedException("test interrupted exception")).when(spyBaseRuntimeFlowableDelegate).get();

 spyBaseRuntimeFlowableDelegate.get();
 }

 @Test
 public void testGetWithTimeoutException() throws Exception {
 // test get with timeout exception
 doThrow(new TimeoutException("test timeout exception")).when(spyBaseRuntimeFlowableDelegate).get(anyLong(), any());

 spyBaseRuntimeFlowableDelegate.get(1000L, TimeUnit.MILLISECONDS);
 }

 @Test
 public void testBindToFlowTaskInstance() {
 // test bindToFlowTaskInstance
 FlowTaskInstance flowTaskInstance = FlowTaskInstanceTest.createFlowTaskInstance();
 doNothing().when(flowTaskInstance).bindServiceTask(spyBaseRuntimeFlowableDelegate);

 spyBaseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);

 verify(flowTaskInstance).bindServiceTask(spyBaseRuntimeFlowableDelegate);
 }

 @Test
 public void testUpdateFlowInstanceStatus() {
 // test updateFlowInstanceStatus
 doNothing().when(flowInstanceRepository).updateStatusById(anyLong(), any());

 spyBaseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.TEST_STATUS);

 verify(flowInstanceRepository).updateStatusById(anyLong(), any());
 }

 @Override
 public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
 this.applicationEventPublisher = applicationEventPublisher;
 }

}