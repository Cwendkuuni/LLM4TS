package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.task.service.delegate.DelegateTask;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowConfig;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;

public class BaseRuntimeFlowableDelegateTest {

 @Mock
 private DelegateExecution execution;

 @Mock
 private DelegateTask delegateTask;

 @Mock
 private FlowableAdaptor flowableAdaptor;

 @Mock
 private FlowInstanceRepository flowInstanceRepository;

 @Mock
 private ApplicationEventPublisher eventPublisher;

 @Mock
 private RetryExecutor retryExecutor;

 @Mock
 private PreConditions preConditions;

 @Mock
 private Verify verify;

 @Mock
 private FlowConfig flowConfig;

 @InjectMocks
 private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 ReflectionTestUtils.setField(baseRuntimeFlowableDelegate, "processEngineConfiguration",
 mock(ProcessEngineConfigurationImpl.class));
 }

 @Test
 public void testRun() throws Exception {
 ArgumentCaptor<ActiveTaskStatisticsListener> activeTaskStatisticsListenerArgumentCaptor = ArgumentCaptor
 .forClass(ActiveTaskStatisticsListener.class);
 ArgumentCaptor<TaskInstanceCreatedListener> taskInstanceCreatedlistenerArgumentCaptor = ArgumentCaptor
 .forClass(TaskInstanceCreatedListener.class);

 // Setup mocks
 when(execution.getCurrentActivityId()).thenReturn("activityId");
 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
 .thenReturn(Optional.of(Optional.of(1L)));
 when(flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L)).thenReturn(Optional.of(mock(FlowTaskInstance.class)));

 // Call method under test
 baseRuntimeFlowableDelegate.run(execution);

 // Verify method calls
 verify(eventPublisher).addEventListener(activeTaskStatisticsListenerArgumentCaptor.capture());
 verify(eventPublisher).addEventListener(taskInstanceCreatedlistenerArgumentCaptor.capture());
 verify(execution, never()).setVariable(anyString(), any());

 // Test that the activeTaskStatisticsListener and taskInstanceCreatedlistener are not null
 ActiveTaskStatisticsListener activeTaskStatisticsListener = activeTaskStatisticsListenerArgumentCaptor
 .getValue();
 TaskInstanceCreatedListener taskInstanceCreatedlistener = taskInstanceCreatedlistenerArgumentCaptor.getValue();
 assertNotNull(activeTaskStatisticsListener);
 assertNotNull(taskInstanceCreatedlistener);

 // Test that the activeTaskStatisticsListener and taskInstanceCreatedlistener are registered with the eventPublisher
 assertEquals(eventPublisher, activeTaskStatisticsListener.getEventPublisher());
 assertEquals(eventPublisher, taskInstanceCreatedlistener.getEventPublisher());

 // Test that the activeTaskStatisticsListener and taskInstanceCreatedlistener are unregistered when the run method exits
 verify(eventPublisher).removeEventListener(activeTaskStatisticsListener);
 verify(eventPublisher).removeEventListener(taskInstanceCreatedlistener);
 }

 @Test
 public void testExecute() throws Exception {
 // Setup mocks
 when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");
 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
 .thenReturn(Optional.of(Optional.of(1L)));
 when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(Optional.of(mock(FlowTaskInstance.class)));

 // Call method under test
 baseRuntimeFlowableDelegate.execute(execution);

 // Verify method calls
 verify(eventPublisher).removeEventListener(any(ActiveTaskStatisticsListener.class));
 verify(eventPublisher).removeEventListener(any(TaskInstanceCreatedListener.class));
 }

 @Test
 public void testIsDone() {
 assertTrue(baseRuntimeFlowableDelegate.isDone());
 }

 @Test
 public void testGet() throws InterruptedException, ExecutionException {
 T result = baseRuntimeFlowableDelegate.get();
 assertNull(result);
 }

 @Test(expected = TimeoutException.class)
 public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
 baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS);
 }

 @Test
 public void testInitTargetTaskInstanceId() {
 // Setup mocks
 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
 .thenReturn(Optional.of(Optional.of(1L)));
 when(flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L)).thenReturn(Optional.of(mock(FlowTaskInstance.class)));

 // Call method under test
 baseRuntimeFlowableDelegate.initTargetTaskInstanceId(execution);

 // Verify method calls
 verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
 verify(flowableAdaptor).getTaskInstanceByActivityId("activityId", 1L);

 // Verify method arguments
 verify(preConditions).validExists(ResourceType.ODC_FLOW_INSTANCE, "processDefinitionId", "processDefinitionId",
 Optional::isPresent);

 verify(verify).notNull("callable", baseRuntimeFlowableDelegate.getCallable());
 }

 @Test
 public void testBindToFlowTaskInstance() {
 // Setup mocks
 FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);
 when(flowTaskInstance.getId()).thenReturn(1L);
 when(flowTaskInstance.getTaskType()).thenReturn(TaskType.SERVICE);

 // Call method under test
 baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);

 // Verify method calls
 verify(flowTaskInstance).bindServiceTask(baseRuntimeFlowableDelegate);
 }

 @Test
 public void testUpdateFlowInstanceStatus() {
 // Setup mocks
 when(flowInstanceRepository.updateStatusById(1L, FlowStatus.RUNNING)).thenReturn(true);

 // Call method under test
 baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.RUNNING);

 // Verify method calls
 verify(flowInstanceRepository).updateStatusById(1L, FlowStatus.RUNNING);
 }

 @Test
 public void testInitCallable() {
 // Test that the initCallable method returns a Callable instance
 assertNotNull(baseRuntimeFlowableDelegate.initCallable(execution));
 }
}