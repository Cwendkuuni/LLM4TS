import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.TaskType;
import com.oceanbase.odc.service.flow.event.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import java.util.concurrent.Callable;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
MockitoAnnotations.openMocks(this);
baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
@Override
protected Callable<String> initCallable(DelegateExecution execution) {
return () -> "test";
}
};
baseRuntimeFlowableDelegate.setFlowableAdaptor(flowableAdaptor);
baseRuntimeFlowableDelegate.setEventPublisher(eventPublisher);
baseRuntimeFlowableDelegate.setFlowInstanceRepository(flowInstanceRepository);
}

@Test
public void testRun() throws Exception {
baseRuntimeFlowableDelegate.run(delegateExecution);
verify(delegateExecution, times(1)).getCurrentActivityId();
verify(flowableAdaptor, times(1))
.getFlowInstanceIdByProcessDefinitionId(delegateExecution.getProcessDefinitionId());
verify(flowableAdaptor, times(1))
.getTaskInstanceByActivityId(delegateExecution.getCurrentActivityId(), baseRuntimeFlowableDelegate.getFlowInstanceId());
verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
assertEquals("test", baseRuntimeFlowableDelegate.getReturnObject());
}

@Test
public void testExecute() throws Exception {
baseRuntimeFlowableDelegate.execute(delegateExecution);
verify(eventPublisher, times(1)).removeEventListener(baseRuntimeFlowableDelegate.getTaskInstanceCreatedlistener());
verify(eventPublisher, times(1)).removeEventListener(baseRuntimeFlowableDelegate.getActiveTaskStatisticsListener());
}

@Test
public void testGet() throws Exception {
baseRuntimeFlowableDelegate.run(delegateExecution);
assertEquals("test", baseRuntimeFlowableDelegate.get());
}

@Test
public void testGetTimeout() throws Exception {
baseRuntimeFlowableDelegate.run(delegateExecution);
when(delegateExecution.getProcessDefinitionId()).thenReturn("testProcessDefinitionId");
when(delegateExecution.getCurrentActivityId()).thenReturn("testActivityId");
BaseRuntimeFlowableDelegate<String> testBaseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
@Override
protected Callable<String> initCallable(DelegateExecution execution) {
return () -> {
try {
Thread.sleep(5000);
} catch (InterruptedException e) {
throw new RuntimeException(e);
}
return "test";
};
}
};
testBaseRuntimeFlowableDelegate.setFlowableAdaptor(flowableAdaptor);
testBaseRuntimeFlowableDelegate.setEventPublisher(eventPublisher);
testBaseRuntimeFlowableDelegate.setFlowInstanceRepository(flowInstanceRepository);
testBaseRuntimeFlowableDelegate.execute(delegateExecution);
testBaseRuntimeFlowableDelegate.get(3, TimeUnit.SECONDS);
verify(delegateExecution, times(1)).getProcessDefinitionId();
verify(delegateExecution, times(1)).getCurrentActivityId();
verify(flowableAdaptor, times(1))
.getFlowInstanceIdByProcessDefinitionId(delegateExecution.getProcessDefinitionId());
verify(flowableAdaptor, times(1))
.getTaskInstanceByActivityId(delegateExecution.getCurrentActivityId(), testBaseRuntimeFlowableDelegate.getFlowInstanceId());
verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
}

@Test
public void testInitTargetTaskInstanceId() {
baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);
verify(flowableAdaptor, times(1))
.getFlowInstanceIdByProcessDefinitionId(delegateExecution.getProcessDefinitionId());
verify(flowableAdaptor, times(1))
.getTaskInstanceByActivityId(delegateExecution.getCurrentActivityId(), baseRuntimeFlowableDelegate.getFlowInstanceId());
}

@Test
public void testBindToFlowTaskInstance() {
FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);
verify(flowTaskInstance, times(1)).bindServiceTask(baseRuntimeFlowableDelegate);
}

@Test
public void testUpdateFlowInstanceStatus() {
baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.TEST);
verify(flowInstanceRepository, times(1))
.updateStatusById(baseRuntimeFlowableDelegate.getFlowInstanceId(), FlowStatus.TEST);
}

@Test
public void testIsDone() {
baseRuntimeFlowableDelegate.run(delegateExecution);
assertEquals(true, baseRuntimeFlowableDelegate.isDone());
}

@Test
public void testInitCallable() {
Callable<String> callable = baseRuntimeFlowableDelegate.initCallable(delegateExecution);
verify(delegateExecution, times(1)).getCurrentActivityId();
assertEquals("test", callable.call());
}

@Test
public void testGetWithException() throws Exception {
when(delegateExecution.getProcessDefinitionId()).thenReturn("testProcessDefinitionId");
when(delegateExecution.getCurrentActivityId()).thenReturn("testActivityId");
BaseRuntimeFlowableDelegate<String> testBaseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
@Override
protected Callable<String> initCallable(DelegateExecution execution) {
return () -> {
throw new RuntimeException("test");
};
}
};
testBaseRuntimeFlowableDelegate.setFlowableAdaptor(flowableAdaptor);
testBaseRuntimeFlowableDelegate.setEventPublisher(eventPublisher);
testBaseRuntimeFlowableDelegate.setFlowInstanceRepository(flowInstanceRepository);
testBaseRuntimeFlowableDelegate.execute(delegateExecution);
verify(delegateExecution, times(1)).getProcessDefinitionId();
verify(delegateExecution, times(1)).getCurrentActivityId();
verify(flowableAdaptor, never())
.getFlowInstanceIdByProcessDefinitionId(delegateExecution.getProcessDefinitionId());
verify(flowableAdaptor, never())
.getTaskInstanceByActivityId(delegateExecution.getCurrentActivityId(), testBaseRuntimeFlowableDelegate.getFlowInstanceId());
verify(eventPublisher, never()).publishEvent(any(ServiceTaskStartedEvent.class));
try {
testBaseRuntimeFlowableDelegate.get();
} catch (Exception e) {
assertEquals("test", e.getCause().getMessage());
}
}

@Test
public void testGetWithExceptionTimeout() throws Exception {
when(delegateExecution.getProcessDefinitionId()).thenReturn("testProcessDefinitionId");
when(delegateExecution.getCurrentActivityId()).thenReturn("testActivityId");
BaseRuntimeFlowableDelegate<String> testBaseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
@Override
protected Callable<String> initCallable(DelegateExecution execution) {
return () -> {
try {
Thread.sleep(5000);
} catch (InterruptedException e) {
throw new RuntimeException(e);
}
throw new RuntimeException("test");
};
}
};
testBaseRuntimeFlowableDelegate.setFlowableAdaptor(flowableAdaptor);
testBaseRuntimeFlowableDelegate.setEventPublisher(eventPublisher);
testBaseRuntimeFlowableDelegate.setFlowInstanceRepository(flowInstanceRepository);
testBaseRuntimeFlowableDelegate.execute(delegateExecution);
try {
testBaseRuntimeFlowableDelegate.get(3, TimeUnit.SECONDS);
} catch (Exception e) {
verify(delegateExecution, times(1)).getProcessDefinitionId();
verify(delegateExecution, times(1)).getCurrentActivityId();
verify(flowableAdaptor, times(1))
.getFlowInstanceIdByProcessDefinitionId(delegateExecution.getProcessDefinitionId());
verify(flowableAdaptor, times(1))
.getTaskInstanceByActivityId(delegateExecution.getCurrentActivityId(), testBaseRuntimeFlowableDelegate.getFlowInstanceId());
verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
assertEquals("test", e.getCause().getMessage());
}
}
}