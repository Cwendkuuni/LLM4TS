package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

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
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;

public class BaseRuntimeFlowableDelegateTest {

 @Mock
 private FlowableAdaptor flowableAdaptor;

 @Mock
 private EventPublisher eventPublisher;

 @Mock
 private FlowInstanceRepository flowInstanceRepository;

 @Mock
 private DelegateExecution execution;

 @Mock
 private Task task;

 @Mock
 private HistoricTaskInstance historicTaskInstance;

 @InjectMocks
 private BaseRuntimeFlowableDelegate<String> delegate;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testInitTargetTaskInstanceId() {
 ArgumentCaptor<String> processDefinitionIdCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<Optional<Optional<Long>>> retryOptionalCaptor = ArgumentCaptor.forClass(Optional.class);

 delegate.initTargetTaskInstanceId(execution);

 verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId(processDefinitionIdCaptor.capture());
 verify(flowableAdaptor).getTaskInstanceByActivityId(anyString(), anyLong());

 PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "processDefinitionId",
 processDefinitionIdCaptor.getValue(), retryOptionalCaptor.getValue()::isPresent);
 }

 @Test
 public void testPreHandle() throws Exception {
 delegate.preHandle(execution);
 verify(execution, times(1)).getProcessDefinitionId();
 }

 @Test
 public void testExecute() {
 delegate.execute(execution);
 verify(execution, times(1)).getId();
 }

 @Test
 public void testIsDone() {
 assertTrue(delegate.isDone());
 }

 @Test
 public void testGet() throws InterruptedException, ExecutionException {
 delegate.get();
 verify(delegate.latch, times(1)).await();
 }

 @Test
 public void testGetWithTimeout()
 throws InterruptedException, ExecutionException, TimeoutException {
 delegate.get(1, TimeUnit.SECONDS);
 verify(delegate.latch, times(1)).await(1, TimeUnit.SECONDS);
 }

 @Test(expected = ExecutionException.class)
 public void testGetWithThrownException()
 throws InterruptedException, ExecutionException, TimeoutException {
 when(execution.getCurrentActivityId()).thenThrow(new RuntimeException());
 delegate.get();
 }

 @Test
 public void testBindToFlowTaskInstance() {
 FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);
 delegate.bindToFlowTaskInstance(flowTaskInstance);
 verify(flowTaskInstance, times(1)).bindServiceTask(delegate);
 }

 @Test
 public void testUpdateFlowInstanceStatus() {
 ArgumentCaptor<Long> flowInstanceIdCaptor = ArgumentCaptor.forClass(Long.class);
 ArgumentCaptor<FlowStatus> flowStatusCaptor = ArgumentCaptor.forClass(FlowStatus.class);

 delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

 verify(flowInstanceRepository).updateStatusById(flowInstanceIdCaptor.capture(),
 flowStatusCaptor.capture());
 }

 @Test(expected = ServiceTaskError.class)
 public void testInitCallableWithException() throws Exception {
 when(execution.getCurrentActivityId()).thenThrow(new RuntimeException());

 delegate.initCallable(execution);
 }
}