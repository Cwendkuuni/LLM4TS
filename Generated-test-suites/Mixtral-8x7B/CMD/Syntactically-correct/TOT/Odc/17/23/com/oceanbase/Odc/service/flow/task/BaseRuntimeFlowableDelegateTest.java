import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.test.PluggableFlowableTestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.concurrent.Callable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BaseRuntimeFlowableDelegateTest extends PluggableFlowableTestCase {

@Mock
private FlowableAdaptor flowableAdaptor;

@Mock
private EventPublisher eventPublisher;

@Mock
private FlowInstanceRepository flowInstanceRepository;

@InjectMocks
private BaseRuntimeFlowableDelegate<String> delegate;

@Before
public void setUp() {
MockitoAnnotations.openMocks(this);
ReflectionTestUtils.setField(delegate, "retryExecutor", Mockito.mock(RetryExecutor.class));
}

@Test
public void testPreHandle() throws Exception {
DelegateExecution execution = Mockito.mock(DelegateExecution.class);

delegate.preHandle(execution);

verify(execution, times(1)).getProcessDefinitionId();
verify(execution, times(1)).getCurrentActivityId();
}

@Test
public void testInitTargetTaskInstanceIdSuccess() {
when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any())).thenReturn(Optional.of(1L));
when(flowableAdaptor.getTaskInstanceByActivityId(any(), anyLong()))
.thenReturn(Optional.of(Mockito.mock(FlowTaskInstance.class)));

delegate.initTargetTaskInstanceId(Mockito.mock(DelegateExecution.class));

verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(any());
verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(any(), anyLong());
assertEquals(Long.valueOf(1), delegate.getFlowInstanceId());
assertNotNull(delegate.getTargetTaskInstanceId());
assertNotNull(delegate.getTaskType());
assertNotNull(delegate.getStrategyConfig());
}

@Test
public void testInitTargetTaskInstanceIdFlowInstanceIdNotFound() {
when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any())).thenReturn(Optional.empty());

delegate.initTargetTaskInstanceId(Mockito.mock(DelegateExecution.class));

verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(any());
verify(flowableAdaptor, never()).getTaskInstanceByActivityId(any(), anyLong());
assertNull(delegate.getFlowInstanceId());
assertNull(delegate.getTargetTaskInstanceId());
assertNull(delegate.getTaskType());
assertNull(delegate.getStrategyConfig());
}

@Test
public void testInitCallableSuccess() throws Exception {
DelegateExecution execution = Mockito.mock(DelegateExecution.class);
Callable<String> callable = Mockito.mock(Callable.class);
when(delegate.initCallable(execution)).thenReturn(callable);

assertEquals(callable, delegate.initCallable(execution));
}

@Test
public void testRunSuccess() throws Exception {
DelegateExecution execution = Mockito.mock(DelegateExecution.class);
Callable<String> callable = Mockito.mock(Callable.class);
when(delegate.initCallable(execution)).thenReturn(callable);

delegate.run(execution);

verify(execution, times(1)).getProcessDefinitionId();
verify(execution, times(1)).getCurrentActivityId();
verify(execution, times(1)).setVariable(any(), any());
verify(callable, times(1)).call();
assertNotNull(delegate.getReturnObject());
assertNull(delegate.getThrown());
assertTrue(delegate.isDone());
verify(delegate.getLatch(), times(1)).await();
}

@Test
public void testRunFailure() throws Exception {
DelegateExecution execution = Mockito.mock(DelegateExecution.class);
Callable<String> callable = Mockito.mock(Callable.class);
when(delegate.initCallable(execution)).thenReturn(callable);
when(callable.call()).thenThrow(new RuntimeException("test exception"));

delegate.run(execution);

verify(execution, times(1)).getProcessDefinitionId();
verify(execution, times(1)).getCurrentActivityId();
verify(execution, never()).setVariable(any(), any());
verify(callable, times(1)).call();
assertNotNull(delegate.getThrown());
assertNull(delegate.getReturnObject());
assertTrue(delegate.isDone());
verify(delegate.getLatch(), times(1)).await();
}

@Test
public void testGet() throws Exception {
DelegateExecution execution = Mockito.mock(DelegateExecution.class);
Callable<String> callable = Mockito.mock(Callable.class);
when(delegate.initCallable(execution)).thenReturn(callable);
when(callable.call()).thenReturn("test value");

assertEquals("test value", delegate.get());
assertEquals("test value", delegate.get(1, TimeUnit.MINUTES));
}

@Test(expected = InterruptedException.class)
public void testGetInterrupted() throws Exception {
DelegateExecution execution = Mockito.mock(DelegateExecution.class);
Callable<String> callable = Mockito.mock(Callable.class);
when(delegate.initCallable(execution)).thenReturn(callable);
when(callable.call()).thenThrow(new InterruptedException("test exception"));

delegate.get();
}

@Test(expected = ExecutionException.class)
public void testGetExecutionException() throws Exception {
DelegateExecution execution = Mockito.mock(DelegateExecution.class);
Callable<String> callable = Mockito.mock(Callable.class);
when(delegate.initCallable(execution)).thenReturn(callable);
when(callable.call()).thenThrow(new RuntimeException("test exception"));

delegate.get();
}

@Test(expected = TimeoutException.class)
public void testGetTimeoutException() throws Exception {
DelegateExecution execution = Mockito.mock(DelegateExecution.class);
Callable<String> callable = Mockito.mock(Callable.class);
when(delegate.initCallable(execution)).thenReturn(callable);
when(callable.call()).thenReturn("test value");

delegate.get(0, TimeUnit.MINUTES);
}

@Test
public void testIsDone() {
assertFalse(delegate.isDone());
delegate.run(Mockito.mock(DelegateExecution.class));
assertTrue(delegate.isDone());
}

@Test
public void testBindToFlowTaskInstance() {
FlowTaskInstance flowTaskInstance = Mockito.mock(FlowTaskInstance.class);
delegate.bindToFlowTaskInstance(flowTaskInstance);
verify(flowTaskInstance, times(1)).bindServiceTask(delegate);
verify(flowTaskInstance, times(1)).getTaskType();
verify(flowTaskInstance, times(1)).getId();
verify(flowTaskInstance, times(1)).getActivityId();
}

@Test
public void testUpdateFlowInstanceStatusSuccess() {
when(flowInstanceRepository.updateStatusById(anyLong(), any()))
.thenReturn(Optional.of(1L));
delegate.updateFlowInstanceStatus(Mockito.mock(FlowStatus.class));
verify(flowInstanceRepository, times(1))
.updateStatusById(anyLong(), any());
}

@Test
public void testUpdateFlowInstanceStatusFailure() {
when(flowInstanceRepository.updateStatusById(anyLong(), any()))
.thenReturn(Optional.empty());
delegate.updateFlowInstanceStatus(Mockito.mock(FlowStatus.class));
verify(flowInstanceRepository, times(1))
.updateStatusById(anyLong(), any());
}

@Test
public void testInit() {
assertNotNull(delegate.getLatch());
assertNotNull(delegate.getRetryExecutor());
assertNotNull(delegate.getTaskInstanceCreatedlistener());
assertNotNull(delegate.getActiveTaskStatisticsListener());
}

@Test
public void testInitPostConstruct() {
delegate.init(eventPublisher);
verify(eventPublisher, times(1)).addEventListener(any());
verify(eventPublisher, times(1)).addEventListener(any());
}
}