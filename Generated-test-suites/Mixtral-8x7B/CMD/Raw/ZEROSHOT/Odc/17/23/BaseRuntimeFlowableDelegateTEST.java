import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.concurrent.Callable;

public class BaseRuntimeFlowableDelegateTest {

@Mock
private FlowableAdaptor flowableAdaptor;

@Mock
private EventPublisher eventPublisher;

@Mock
private FlowInstanceRepository flowInstanceRepository;

@Mock
private DelegateExecution delegateExecution;

private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);
baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
@Override
protected Callable<String> initCallable(DelegateExecution execution) {
return () -> "test";
}
};
baseRuntimeFlowableDelegate.flowableAdaptor = flowableAdaptor;
baseRuntimeFlowableDelegate.eventPublisher = eventPublisher;
baseRuntimeFlowableDelegate.flowInstanceRepository = flowInstanceRepository;
}

@Test
public void testRun() throws Exception {
baseRuntimeFlowableDelegate.run(delegateExecution);
Mockito.verify(eventPublisher, Mockito.times(1))
.publishEvent(Mockito.argThat(event -> event instanceof ServiceTaskStartedEvent));
Mockito.verify(flowableAdaptor, Mockito.times(1))
.getFlowInstanceIdByProcessDefinitionId(delegateExecution.getProcessDefinitionId());
Mockito.verify(flowInstanceRepository, Mockito.times(1))
.updateStatusById(baseRuntimeFlowableDelegate.getFlowInstanceId(), FlowStatus.FINISHED);
}

@Test
public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
baseRuntimeFlowableDelegate.run(delegateExecution);
String result = baseRuntimeFlowableDelegate.get();
Assert.assertEquals("test", result);
}

@Test(expected = TimeoutException.class)
public void testGetTimeout() throws InterruptedException, ExecutionException, TimeoutException {
baseRuntimeFlowableDelegate.run(delegateExecution);
baseRuntimeFlowableDelegate.get(1, TimeUnit.MILLISECONDS);
}

@Test
public void testIsDone() throws InterruptedException, ExecutionException {
baseRuntimeFlowableDelegate.run(delegateExecution);
Assert.assertTrue(baseRuntimeFlowableDelegate.isDone());
}

@Test
public void testInitTargetTaskInstanceId() {
baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);
Mockito.verify(flowableAdaptor, Mockito.times(1))
.getTaskInstanceByActivityId(delegateExecution.getCurrentActivityId(), baseRuntimeFlowableDelegate.getFlowInstanceId());
}

@Test
public void testBindToFlowTaskInstance() {
FlowTaskInstance taskInstance = Mockito.mock(FlowTaskInstance.class);
baseRuntimeFlowableDelegate.bindToFlowTaskInstance(taskInstance);
Mockito.verify(taskInstance, Mockito.times(1)).bindServiceTask(baseRuntimeFlowableDelegate);
}

@Test
public void testUpdateFlowInstanceStatus() {
baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.FINISHED);
Mockito.verify(flowInstanceRepository, Mockito.times(1))
.updateStatusById(baseRuntimeFlowableDelegate.getFlowInstanceId(), FlowStatus.FINISHED);
}

@Test
public void testPreHandle() throws Exception {
baseRuntimeFlowableDelegate.preHandle(delegateExecution);
Mockito.verify(delegateExecution, Mockito.times(1)).setVariable("returnObject", "test");
}

@Test(expected = ServiceTaskError.class)
public void testRunWithException() throws Exception {
Mockito.doThrow(new RuntimeException("test exception"))
.when(baseRuntimeFlowableDelegate)
.initTargetTaskInstanceId(delegateExecution);
baseRuntimeFlowableDelegate.run(delegateExecution);
}
}