import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.test.PluggableFlowableTestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class BaseRuntimeFlowableDelegateTest extends PluggableFlowableTestCase {

 @Autowired
 private FlowableAdaptor flowableAdaptor;

 @Autowired
 private EventPublisher eventPublisher;

 @Autowired
 private FlowInstanceRepository flowInstanceRepository;

 private BaseRuntimeFlowableDelegate<String> delegate;

 @Before
 public void setUp() {
 flowableAdaptor = mock(FlowableAdaptor.class);
 eventPublisher = mock(EventPublisher.class);
 flowInstanceRepository = mock(FlowInstanceRepository.class);
 delegate = spy(new TestRuntimeFlowableDelegate());
 delegate.setFlowableAdaptor(flowableAdaptor);
 delegate.setEventPublisher(eventPublisher);
 delegate.setFlowInstanceRepository(flowInstanceRepository);
 }

 @Test
 public void testInitTargetTaskInstanceId() {
 String processDefinitionId = "processDefinitionId";
 DelegateExecution execution = mock(DelegateExecution.class);
 doReturn(Optional.of(Optional.of(1L))).when(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId(processDefinitionId);

 delegate.initTargetTaskInstanceId(execution);

 verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
 verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
 }

 @Test
 public void testRunWithExceptionInInitialization() throws Exception {
 DelegateExecution execution = mock(DelegateExecution.class);
 doReturn(null).when(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId(anyString());

 try {
 delegate.run(execution);
 fail("Exception should be thrown");
 } catch (ServiceTaskError e) {
 // expected
 }
 }

 @Test
 public void testRunWithExceptionInCallable() throws Exception {
 DelegateExecution execution = mock(DelegateExecution.class);
 doReturn(Optional.of(Optional.of(1L))).when(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId(anyString());

 try {
 delegate.run(execution);
 fail("Exception should be thrown");
 } catch (Exception e) {
 // expected
 }
 }

 @Test
 public void testIsDone() {
 assertTrue(delegate.isDone());
 }

 @Test
 public void testGet() throws InterruptedException, ExecutionException {
 delegate.get();
 }

 @Test(expected = ExecutionException.class)
 public void testGetWithException() throws InterruptedException, ExecutionException {
 delegate.get(1, TimeUnit.MILLISECONDS);
 }

 @Test(expected = TimeoutException.class)
 public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
 delegate.get(1, TimeUnit.SECONDS);
 }

 @Test
 public void testUpdateFlowInstanceStatus() {
 delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
 }

}

class TestRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {

 @Override
 protected Callable<String> initCallable(DelegateExecution execution) {
 return () -> {
 throw new RuntimeException("Something went wrong");
 };
 }

}