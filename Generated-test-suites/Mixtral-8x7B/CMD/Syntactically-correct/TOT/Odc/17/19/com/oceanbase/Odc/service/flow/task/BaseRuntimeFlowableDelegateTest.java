import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.test.PluggableFlowableTestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.concurrent.Callable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BaseRuntimeFlowableDelegateTest extends PluggableFlowableTestCase {

 private BaseRuntimeFlowableDelegate<String> delegate;
 private DelegateExecution execution;
 private FlowInstanceRepository flowInstanceRepository;
 private FlowableAdaptor flowableAdaptor;
 private EventPublisher eventPublisher;
 private TaskInstanceCreatedListener taskInstanceCreatedlistener;
 private ActiveTaskStatisticsListener activeTaskStatisticsListener;
 private Callable<String> callable;
 private RetryExecutor retryExecutor;

 @Before
 public void setup() {
 flowInstanceRepository = Mockito.mock(FlowInstanceRepository.class);
 flowableAdaptor = Mockito.mock(FlowableAdaptor.class);
 eventPublisher = Mockito.mock(EventPublisher.class);
 retryExecutor = Mockito.mock(RetryExecutor.class);
 taskInstanceCreatedlistener = Mockito.mock(TaskInstanceCreatedListener.class);
 activeTaskStatisticsListener = Mockito.mock(ActiveTaskStatisticsListener.class);

 delegate = Mockito.spy(new TestRuntimeFlowableDelegate());
 delegate.setFlowInstanceRepository(flowInstanceRepository);
 delegate.setFlowableAdaptor(flowableAdaptor);
 delegate.setEventPublisher(eventPublisher);
 delegate.setRetryExecutor(retryExecutor);
 delegate.setTaskInstanceCreatedlistener(taskInstanceCreatedlistener);
 delegate.setActiveTaskStatisticsListener(activeTaskStatisticsListener);

 execution = Mockito.mock(DelegateExecution.class);
 when(execution.getCurrentActivityId()).thenReturn("testActivityId");
 when(execution.getProcessDefinitionId()).thenReturn("testProcessDefinitionId");

 callable = Mockito.mock(Callable.class);
 when(callable.call()).thenReturn("testResult");
 }

 @Test
 public void testRun() throws Exception {
 // Test typical use case
 delegate.run(execution);

 ArgumentCaptor<ServiceTaskStartedEvent> argument = ArgumentCaptor.forClass(ServiceTaskStartedEvent.class);
 verify(eventPublisher, times(1)).publishEvent(argument.capture());
 assertEquals("testActivityId", argument.getValue().getTaskInstance().getActivityId());

 verify(flowInstanceRepository, times(1))
 .updateStatusById(anyLong(), any(FlowStatus.class));

 verify(callable, times(1)).call();

 assertEquals("testResult", delegate.get());
 assertEquals(true, delegate.isDone());
 }

 @Test
 public void testInitTargetTaskInstanceId() {
 // Test typical use case
 delegate.initTargetTaskInstanceId(execution);

 verify(flowableAdaptor, times(1))
 .getFlowInstanceIdByProcessDefinitionId(anyString());

 verify(flowableAdaptor, times(1))
 .getTaskInstanceByActivityId(anyString(), anyLong());

 assertEquals("testProcessDefinitionId", delegate.getFlowInstanceId().toString());
 assertEquals("testActivityId", delegate.getActivityId());
 assertEquals(TaskType.TEST, delegate.getTaskType());
 }

 @Test(expected = ServiceTaskError.class)
 public void testInitTargetTaskInstanceId_Exception() {
 // Test error scenario
 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
 .thenReturn(Optional.empty());

 delegate.initTargetTaskInstanceId(execution);
 }

 @Test
 public void testInitCallable() throws Exception {
 // Test typical use case
 Callable<String> callable = delegate.initCallable(execution);

 verify(flowableAdaptor, times(1))
 .getTaskInstanceByActivityId(anyString(), anyLong());

 assertNotNull(callable);
 }

 @Test
 public void testBindToFlowTaskInstance() {
 // Test typical use case
 FlowTaskInstance taskInstance = Mockito.mock(FlowTaskInstance.class);
 when(taskInstance.getId()).thenReturn(1L);
 when(taskInstance.getActivityId()).thenReturn("testActivityId");

 delegate.bindToFlowTaskInstance(taskInstance);

 verify(taskInstance, times(1)).bindServiceTask(delegate);
 }

 @Test
 public void testUpdateFlowInstanceStatus() {
 // Test typical use case
 delegate.updateFlowInstanceStatus(FlowStatus.TEST);

 verify(flowInstanceRepository, times(1))
 .updateStatusById(anyLong(), any(FlowStatus.class));
 }
}

class TestRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {

 @Override
 protected Callable<String> initCallable(DelegateExecution execution) {
 return callable;
 }

 @Override
 protected void preHandle(DelegateExecution execution) {}

 @Override
 public void bindToFlowTaskInstance(@NonNull FlowTaskInstance taskInstance) {
 super.bindToFlowTaskInstance(taskInstance);
 }
}