package com.oceanbase.odc.service.flow.task;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
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

 @Mock
 private ApplicationEventPublisher applicationEventPublisher;

 private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

 @Before
 public void setUp() {
 MockitoAnnotations.openMocks(this);
 baseRuntimeFlowableDelegate = spy(new TestRuntimeFlowableDelegate());
 baseRuntimeFlowableDelegate.flowableAdaptor = flowableAdaptor;
 baseRuntimeFlowableDelegate.eventPublisher = eventPublisher;
 baseRuntimeFlowableDelegate.flowInstanceRepository = flowInstanceRepository;
 doNothing().when(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId(ArgumentMatchers.anyString());
 doNothing().when(flowableAdaptor).getTaskInstanceByActivityId(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong());
 }

 @Test
 public void testGet_WhenCalled_ReturnsExpectedResult() throws InterruptedException, ExecutionException, TimeoutException {
 CountDownLatch latch = new CountDownLatch(1);
 baseRuntimeFlowableDelegate.setLatch(latch);
 when(baseRuntimeFlowableDelegate.initCallable(delegateExecution)).thenReturn(() -> "Test Result");
 baseRuntimeFlowableDelegate.run(delegateExecution);
 baseRuntimeFlowableDelegate.get();
 assertTrue(latch.await(5, TimeUnit.SECONDS));
 assertEquals("Test Result", baseRuntimeFlowableDelegate.getReturnObject());
 }

 @Test
 public void testGet_WithTimeout_ThrowsTimeoutException()
 throws InterruptedException, ExecutionException, TimeoutException {
 CountDownLatch latch = new CountDownLater(1);
 baseRuntimeFlowableDelegate.setLatch(latch);
 when(baseRuntimeFlowableDelegate.initCallable(delegateExecution)).thenReturn(mock(Callable.class));
 assertThrows(TimeoutException.class, () -> baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS));
 assertFalse(latch.await(5, TimeUnit.SECONDS));
 }

 @Test
 public void testIsDone_AfterRunning_ReturnsTrue() throws Exception {
 when(baseRuntimeFlowableDelegate.initCallable(delegateExecution)).thenReturn(mock(Callable.class));
 baseRuntimeFlowableDelegate.run(delegateExecution);
 assertTrue(baseRuntimeFlowableDelegate.isDone());
 }

 @Test
 public void testPreHandle_WhenCalled_DoesNotThrowException() throws Exception {
 doNothing().when(baseRuntimeFlowableDelegate).preHandle(delegateExecution);
 baseRuntimeFlowableDelegate.preHandle(delegateExecution);
 }

 @Test
 public void testInitTargetTaskInstanceId_WhenCalled_InitializesTaskInstanceId() {
 doAnswer(returnsFirstArg()).when(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId(ArgumentMatchers.anyString());
 doAnswer(returnsFirstArg()).when(flowableAdaptor).getTaskInstanceByActivityId(ArgumentMatchers.anyString(),
 ArgumentMatchers.anyLong());
 baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);
 verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(ArgumentMatchers.anyString());
 verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong());
 }

 @Test
 public void testBindToFlowTaskInstance_WhenCalledWithValidTaskInstance_BindsTask() {
 FlowTaskInstance taskInstance = mock(FlowTaskInstance.class);
 when(taskInstance.getId()).thenReturn(1L);
 when(taskInstance.getTaskType()).thenReturn(TaskType.SERVICE);
 when(taskInstance.getStrategyConfig()).thenReturn(mock(ExecutionStrategyConfig.class));
 baseRuntimeFlowableDelegate.targetTaskInstanceId = 1L;
 baseRuntimeFlowableDelegate.bindToFlowTaskInstance(taskInstance);
 verify(taskInstance, times(1)).bindServiceTask(baseRuntimeFlowableDelegate);
 }

 @Test
 public void testUpdateFlowInstanceStatus_WhenCalledWithValidStatus_UpdatesStatus() {
 doAnswer(AdditionalAnswers.returnsFirstArg()).when(flowInstanceRepository).updateStatusById(ArgumentMatchers.anyLong(),
 ArgumentMatchers.any(FlowStatus.class));
 baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
 verify(flowInstanceRepository, times(1)).updateStatusById(ArgumentMatchers.anyLong(), ArgumentMatchers.any(FlowStatus.class));
 }

 @Test
 public void testInitCallable_WhenCalled_ReturnsCallable() {
 Callable<String> callable = baseRuntimeFlowableDelegate.initCallable(delegateExecution);
 assertFalse(callable.call().isEmpty());
 }

 @Test
 public void testInitCallable_WithException_ThrowsServiceTaskError() {
 when(baseRuntimeFlowableDelegate.initCallable(delegateExecution)).thenThrow(new RuntimeException("Test Exception"));
 assertThrows(ServiceTaskError.class, () -> baseRuntimeFlowableDelegate.run(delegateExecution));
 }

}

class TestRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {

 @Override
 protected Callable<String> initCallable(DelegateExecution delegateExecution) {
 return () -> "Test Result";
 }

 public void setLatch(CountDownLatch latch) {
 this.latch = latch;
 }
}