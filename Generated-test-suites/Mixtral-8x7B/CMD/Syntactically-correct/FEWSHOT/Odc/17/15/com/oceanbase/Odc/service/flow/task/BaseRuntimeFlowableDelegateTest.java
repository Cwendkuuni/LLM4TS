import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class BaseRuntimeFlowableDelegateTest {

@Mock
private DelegateExecution delegateExecution;

@Mock
private FlowableAdaptor flowableAdaptor;

@Mock
private EventPublisher eventPublisher;

@Mock
private FlowInstanceRepository flowInstanceRepository;

private BaseRuntimeFlowableDelegate baseRuntimeFlowableDelegate;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);

baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate() {
@Override
protected Callable<Object> initCallable(DelegateExecution execution) {
return () -> "return object";
}
};

baseRuntimeFlowableDelegate.setFlowableAdaptor(flowableAdaptor);
baseRuntimeFlowableDelegate.setEventPublisher(eventPublisher);
baseRuntimeFlowableDelegate.setFlowInstanceRepository(flowInstanceRepository);
}

@Test
public void testIsDone_whenDoneIsTrue_thenReturnTrue() throws ExecutionException, InterruptedException {
baseRuntimeFlowableDelegate.setDone(true);
Assert.assertTrue(baseRuntimeFlowableDelegate.isDone());
}

@Test
public void testIsDone_whenDoneIsFalse_thenReturnFalse() {
Assert.assertFalse(baseRuntimeFlowableDelegate.isDone());
}

@Test
public void testGet_whenCalled_thenReturnTheResult() throws ExecutionException, InterruptedException, TimeoutException {
CountDownLatch latch = new CountDownLatch(1);
baseRuntimeFlowableDelegate.setReturnObject("test result");
baseRuntimeFlowableDelegate.setDone(true);
baseRuntimeFlowableDelegate.latch = latch;

Future<String> future = baseRuntimeFlowableDelegate;
String result = future.get(1, TimeUnit.SECONDS);

Assert.assertEquals("test result", result);
}

@Test(expected = ExecutionException.class)
public void testGet_whenExceptionIsThrown_thenWrapTheException() throws ExecutionException, InterruptedException, TimeoutException {
CountDownLatch latch = new CountDownLatch(1);
baseRuntimeFlowableDelegate.setThrown(new RuntimeException("test exception"));
baseRuntimeFlowableDelegate.setDone(true);
baseRuntimeFlowableDelegate.latch = latch;

Future<String> future = baseRuntimeFlowableDelegate;
future.get(1, TimeUnit.SECONDS);
}

@Test(expected = TimeoutException.class)
public void testGet_whenTimeoutOccurs_thenThrowTimeoutException() throws ExecutionException, InterruptedException, TimeoutException {
CountDownLatch latch = new CountDownLatch(1);
baseRuntimeFlowableDelegate.setDone(false);
baseRuntimeFlowableDelegate.latch = latch;

Future<String> future = baseRuntimeFlowableDelegate;
future.get(1, TimeUnit.SECONDS);
}

@Test
public void testInitTargetTaskInstanceId_whenProcessDefinitionIdIsNotNull_thenInitTheTask() {
baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

Mockito.verify(flowableAdaptor, Mockito.times(1))
.getFlowInstanceIdByProcessDefinitionId(ArgumentMatchers.anyString());
}

@Test(expected = ServiceTaskError.class)
public void testRun_whenInitializationTaskFails_thenThrowServiceTaskError() throws Exception {
Mockito.when(delegateExecution.getCurrentActivityId())
.thenReturn("test activity id");

Mockito.when(flowableAdaptor.getTaskInstanceByActivityId(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong()))
.thenReturn(Optional.of(new FlowTaskInstance()));

baseRuntimeFlowableDelegate.run(delegateExecution);
}

@Test
public void testRun_whenTaskIsCompleted_thenReturnTheResult() throws Exception {
CountDownLatch latch = new CountDownLatch(1);
Mockito.when(delegateExecution.getCurrentActivityId())
.thenReturn("test activity id");

Mockito.when(flowableAdaptor.getTaskInstanceByActivityId(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong()))
.thenReturn(Optional.of(new FlowTaskInstance()));

baseRuntimeFlowableDelegate.run(delegateExecution);

Mockito.verify(eventPublisher, Mockito.times(1))
.publishEvent(ArgumentMatchers.any(ServiceTaskStartedEvent.class));
}
}