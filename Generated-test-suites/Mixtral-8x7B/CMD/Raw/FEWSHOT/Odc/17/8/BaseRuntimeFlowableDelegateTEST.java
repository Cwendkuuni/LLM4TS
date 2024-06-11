import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.task.api.Task;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class BaseRuntimeFlowableDelegateTest {

@Autowired
private ApplicationEventPublisher eventPublisher;

@Autowired
private FlowableAdaptor flowableAdaptor;

@Autowired
private FlowInstanceRepository flowInstanceRepository;

@Autowired
private RetryExecutor retryExecutor;

private BaseRuntimeFlowableDelegate baseRuntimeFlowableDelegate;

private DelegateExecution delegateExecution;

private Task task;

@Before
public void setup() {
delegateExecution = Mockito.mock(DelegateExecution.class);
task = Mockito.mock(Task.class);

baseRuntimeFlowableDelegate = Mockito.spy(new BaseRuntimeFlowableDelegate() {
@Override
protected Callable<Object> initCallable(DelegateExecution execution) {
return () -> null;
}
});

eventPublisher = Mockito.mock(ApplicationEventPublisher.class);
flowableAdaptor = Mockito.mock(FlowableAdaptor.class);
flowInstanceRepository = Mockito.mock(FlowInstanceRepository.class);
retryExecutor = Mockito.mock(RetryExecutor.class);

baseRuntimeFlowableDelegate.setEventPublisher(eventPublisher);
baseRuntimeFlowableDelegate.setFlowableAdaptor(flowableAdaptor);
baseRuntimeFlowableDelegate.setFlowInstanceRepository(flowInstanceRepository);
baseRuntimeFlowableDelegate.setRetryExecutor(retryExecutor);
}

@Test
public void testInitTargetTaskInstanceId() throws Exception {
Mockito.when(delegateExecution.getProcessDefinitionId()).thenReturn("processDefinitionId");

Optional<Optional<Long>> retryOptional = Optional.of(Optional.of(1L));
Mockito.when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(ArgumentMatchers.anyString()))
.thenReturn(retryOptional);

Mockito.when(flowableAdaptor.getTaskInstanceByActivityId(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong()))
.thenReturn(Optional.of(task));

baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

Mockito.verify(flowableAdaptor, Mockito.times(1))
.getFlowInstanceIdByProcessDefinitionId(ArgumentMatchers.anyString());
Mockito.verify(flowableAdaptor, Mockito.times(1))
.getTaskInstanceByActivityId(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong());

Assert.assertEquals(1L, baseRuntimeFlowableDelegate.getFlowInstanceId());
Assert.assertEquals(task, baseRuntimeFlowableDelegate.getTargetTaskInstance());
Assert.assertEquals(TaskType.SERVICE, baseRuntimeFlowableDelegate.getTaskType());
}

@Test
public void testPreHandle() throws Exception {
baseRuntimeFlowableDelegate.preHandle(delegateExecution);

Mockito.verify(delegateExecution, Mockito.times(1)).setVariable(ArgumentMatchers.anyString(), ArgumentMatchers.any());
}

@Test
public void testExecute() throws Exception {
baseRuntimeFlowableDelegate.execute(delegateExecution);

Mockito.verify(eventPublisher, Mockito.times(1)).removeEventListener(ArgumentMatchers.any());
}

@Test
public void testIsDone() throws Exception {
baseRuntimeFlowableDelegate.setDone(true);

Assert.assertTrue(baseRuntimeFlowableDelegate.isDone());
}

@Test
public void testGet() throws Exception {
baseRuntimeFlowableDelegate.setReturnObject("returnObject");

Assert.assertEquals("returnObject", baseRuntimeFlowableDelegate.get());
}

@Test(expected = ExecutionException.class)
public void testGetWithException() throws Exception {
baseRuntimeFlowableDelegate.setThrown(new RuntimeException("testException"));

baseRuntimeFlowableDelegate.get();
}

@Test(expected = TimeoutException.class)
public void testGetWithTimeout() throws Exception {
baseRuntimeFlowableDelegate.setDone(false);

baseRuntimeFlowableDelegate.get(1, TimeUnit.MILLISECONDS);
}

@Test
public void testBindToFlowTaskInstance() throws Exception {
FlowTaskInstance flowTaskInstance = Mockito.mock(FlowTaskInstance.class);

baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);

Mockito.verify(flowTaskInstance, Mockito.times(1)).bindServiceTask(baseRuntimeFlowableDelegate);
}

@Test
public void testUpdateFlowInstanceStatus() throws Exception {
baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.FINISHED);

Mockito.verify(retryExecutor, Mockito.times(1))
.run(() -> flowInstanceRepository.updateStatusById(ArgumentMatchers.anyLong(), ArgumentMatchers.any()));
}

}