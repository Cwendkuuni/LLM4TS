import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class BaseRuntimeFlowableDelegateTest {

private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

@Before
public void setUp() {
baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
@Override
protected Callable<String> initCallable(DelegateExecution execution) {
return () -> "Test Result";
}
};

ReflectionTestUtils.setField(baseRuntimeFlowableDelegate, "flowableAdaptor", new FlowableAdaptor() {
@Override
public Optional<Optional<Long>> getFlowInstanceIdByProcessDefinitionId(String processDefinitionId) {
return Optional.of(Optional.of(1L));
}

@Override
public Optional<FlowTaskInstance> getTaskInstanceByActivityId(String activityId, Long flowInstanceId) {
return Optional.of(new FlowTaskInstance());
}
});

ReflectionTestUtils.setField(baseRuntimeFlowableDelegate, "eventPublisher", new EventPublisher() {
@Override
public void publishEvent(Object event) {
// Do nothing
}

@Override
public void addEventListener(Object listener) {
// Do nothing
}

@Override
public void removeEventListener(Object listener) {
// Do nothing
}
});

ReflectionTestUtils.setField(baseRuntimeFlowableDelegate, "flowInstanceRepository", new FlowInstanceRepository() {
@Override
public Optional<FlowInstance> getFlowInstanceById(Long id) {
return Optional.of(new FlowInstance());
}

@Override
public int updateStatusById(Long id, FlowStatus status) {
return 1;
}
});
}

@Test
public void testRun() throws Exception {
DelegateExecution execution = Mockito.mock(DelegateExecution.class);
Mockito.when(execution.getCurrentActivityId()).thenReturn("Test Activity");

baseRuntimeFlowableDelegate.run(execution);

Assert.assertEquals("Test Result", baseRuntimeFlowableDelegate.get().get());
}

@Test(expected = ExecutionException.class)
public void testRunWithException() throws Exception {
DelegateExecution execution = Mockito.mock(DelegateExecution.class);
Mockito.when(execution.getCurrentActivityId()).thenReturn("Test Activity");

ReflectionTestUtils.setField(baseRuntimeFlowableDelegate, "throwException", true);

baseRuntimeFlowableDelegate.run(execution);

baseRuntimeFlowableDelegate.get();
}

@Test
public void testExecute() throws Exception {
DelegateExecution execution = Mockito.mock(DelegateExecution.class);
Mockito.when(execution.getCurrentActivityId()).thenReturn("Test Activity");

baseRuntimeFlowableDelegate.execute(execution);

Mockito.verify(execution, Mockito.times(1)).setVariable(Mockito.anyString(), Mockito.any());
}

@Test
public void testIsDone() throws Exception {
DelegateExecution execution = Mockito.mock(DelegateExecution.class);
Mockito.when(execution.getCurrentActivityId()).thenReturn("Test Activity");

baseRuntimeFlowableDelegate.run(execution);

Assert.assertTrue(baseRuntimeFlowableDelegate.isDone());
}

@Test
public void testGet() throws Exception {
DelegateExecution execution = Mockito.mock(DelegateExecution.class);
Mockito.when(execution.getCurrentActivityId()).thenReturn("Test Activity");

baseRuntimeFlowableDelegate.run(execution);

Assert.assertEquals("Test Result", baseRuntimeFlowableDelegate.get().get());
}

@Test(expected = ExecutionException.class)
public void testGetWithException() throws Exception {
DelegateExecution execution = Mockito.mock(DelegateExecution.class);
Mockito.when(execution.getCurrentActivityId()).thenReturn("Test Activity");

ReflectionTestUtils.setField(baseRuntimeFlowableDelegate, "throwException", true);

baseRuntimeFlowableDelegate.run(execution);

baseRuntimeFlowableDelegate.get();
}

@Test(expected = InterruptedException.class)
public void testGetWithInterruptedException() throws Exception {
DelegateExecution execution = Mockito.mock(DelegateExecution.class);
Mockito.when(execution.getCurrentActivityId()).thenReturn("Test Activity");

baseRuntimeFlowableDelegate.run(execution);

Thread.currentThread().interrupt();

baseRuntimeFlowableDelegate.get();
}

@Test(expected = TimeoutException.class)
public void testGetWithTimeoutException() throws Exception {
DelegateExecution execution = Mockito.mock(DelegateExecution.class);
Mockito.when(execution.getCurrentActivityId()).thenReturn("Test Activity");

baseRuntimeFlowableDelegate.run(execution);

baseRuntimeFlowableDelegate.get(0, TimeUnit.MILLISECONDS);
}

@Test
public void testGetWithTimeout() throws Exception {
DelegateExecution execution = Mockito.mock(DelegateExecution.class);
Mockito.when(execution.getCurrentActivityId()).thenReturn("Test Activity");

baseRuntimeFlowableDelegate.run(execution);

Assert.assertEquals("Test Result", baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS).get());
}

@Test
public void testInitTargetTaskInstanceId() throws Exception {
DelegateExecution execution = Mockito.mock(DelegateExecution.class);
Mockito.when(execution.getProcessDefinitionId()).thenReturn("Test Process");

ReflectionTestUtils.setField(baseRuntimeFlowableDelegate, "flowableAdaptor", new FlowableAdaptor() {
@Override
public Optional<Optional<Long>> getFlowInstanceIdByProcessDefinitionId(String processDefinitionId) {
return Optional.of(Optional.of(1L));
}

@Override
public Optional<FlowTaskInstance> getTaskInstanceByActivityId(String activityId, Long flowInstanceId) {
return Optional.of(new FlowTaskInstance());
}
});

baseRuntimeFlowableDelegate.initTargetTaskInstanceId(execution);

Assert.assertNotNull(baseRuntimeFlowableDelegate.getTargetTaskInstanceId());
Assert.assertNotNull(baseRuntimeFlowableDelegate.getTaskType());
Assert.assertNotNull(baseRuntimeFlowableDelegate.getStrategyConfig());
Assert.assertNotNull(baseRuntimeFlowableDelegate.getFlowInstanceId());
}

@Test
public void testBindToFlowTaskInstance() throws Exception {
FlowTaskInstance taskInstance = new FlowTaskInstance();

baseRuntimeFlowableDelegate.bindToFlowTaskInstance(taskInstance);

Assert.assertNotNull(taskInstance.getServiceTask());
}

@Test
public void testUpdateFlowInstanceStatus() throws Exception {
ReflectionTestUtils.setField(baseRuntimeFlowableDelegate, "flowInstanceRepository", new FlowInstanceRepository() {
@Override
public Optional<FlowInstance> getFlowInstanceById(Long id) {
return Optional.of(new FlowInstance());
}

@Override
public int updateStatusById(Long id, FlowStatus status) {
return 1;
}
});

baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

Mockito.verify(baseRuntimeFlowableDelegate.getFlowInstanceRepository(), Mockito.times(1))
.updateStatusById(Mockito.anyLong(), Mockito.any(FlowStatus.class));
}
}