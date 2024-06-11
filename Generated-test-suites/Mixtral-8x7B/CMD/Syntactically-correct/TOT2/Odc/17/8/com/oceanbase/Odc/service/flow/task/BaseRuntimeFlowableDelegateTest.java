import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.test.PluggableFlowableTestCase;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class BaseRuntimeFlowableDelegateTest extends PluggableFlowableTestCase {

@Mock
private FlowableAdaptor flowableAdaptor;

@Mock
private EventPublisher eventPublisher;

@Mock
private FlowInstanceRepository flowInstanceRepository;

@Mock
private DelegateExecution delegateExecution;

@Mock
private Task task;

@InjectMocks
private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);
}

@Test
public void testIsDone() throws ExecutionException, InterruptedException {
baseRuntimeFlowableDelegate.setReturnObject("test");
baseRuntimeFlowableDelegate.setDone(true);
assertEquals("test", baseRuntimeFlowableDelegate.get());
assertTrue(baseRuntimeFlowableDelegate.isDone());
}

@Test
public void testGetTimeout() throws ExecutionException, InterruptedException, TimeoutException {
baseRuntimeFlowableDelegate.setReturnObject("test");
baseRuntimeFlowableDelegate.setDone(true);
assertEquals("test", baseRuntimeFlowableDelegate.get(1000, TimeUnit.MILLISECONDS));
}

@Test
public void testGetTimeoutException() throws ExecutionException, InterruptedException {
baseRuntimeFlowableDelegate.setDone(false);
assertThrows(TimeoutException.class, () -> baseRuntimeFlowableDelegate.get(100, TimeUnit.MILLISECONDS));
}

@Test
public void testInitTargetTaskInstanceId() {
when(delegateExecution.getProcessDefinitionId()).thenReturn("processDefinitionId");
when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any())).thenReturn(Optional.of(1L));

baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

verify(delegateExecution, times(1)).getProcessDefinitionId();
verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(eq("processDefinitionId"));
verify(delegateExecution, times(1)).setVariable(eq("FLOW_INSTANCE_ID"), eq(1L));
}

@Test
public void testInitTargetTaskInstanceIdException() {
when(delegateExecution.getProcessDefinitionId()).thenReturn("processDefinitionId");
when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any()))
.thenReturn(Optional.ofNullable(null));

baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

verify(delegateExecution, times(1)).getProcessDefinitionId();
verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(eq("processDefinitionId"));
verify(delegateExecution, never()).setVariable(any(), any());
}

@Test
public void testInitCallable() {
when(delegateExecution.getProcessDefinitionId()).thenReturn("processDefinitionId");
when(delegateExecution.getId()).thenReturn("executionId");
when(delegateExecution.getVariable("varName")).thenReturn("varValue");

Callable<String> callable = baseRuntimeFlowableDelegate.initCallable(delegateExecution);

verify(delegateExecution, times(1)).getProcessDefinitionId();
verify(delegateExecution, times(1)).getId();
verify(delegateExecution, times(1)).getVariable("varName");
verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(any(), anyLong());
}

@Test
public void testPreHandle() throws Exception {
baseRuntimeFlowableDelegate.preHandle(delegateExecution);

verify(delegateExecution, times(1)).getProcessDefinitionId();
}

@Test
public void testExecute() throws Exception {
baseRuntimeFlowableDelegate.execute(delegateExecution);

verify(delegateExecution, times(1)).getProcessDefinitionId();
verify(eventPublisher, times(1)).removeEventListener(any());
verify(eventPublisher, times(1)).removeEventListener(any());
}

@Test
public void testBindingToFlowTaskInstance() {
FlowTaskInstance flowTaskInstance = Mockito.mock(FlowTaskInstance.class);
when(flowTaskInstance.getId()).thenReturn(1L);
when(flowTaskInstance.getTaskType()).thenReturn(TaskType.SERVICE_TASK);

baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);

verify(flowTaskInstance, times(1)).bindServiceTask(baseRuntimeFlowableDelegate);
}

@Test
public void testUpdateFlowInstanceStatus() {
baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

verify(flowInstanceRepository, times(1)).updateStatusById(anyLong(), eq(FlowStatus.COMPLETED));
}

@Test
public void testInitCallableException() {
when(delegateExecution.getProcessDefinitionId()).thenReturn("processDefinitionId");
when(delegateExecution.getId()).thenReturn("executionId");
when(delegateExecution.getVariable("varName")).thenReturn("varValue");
when(flowableAdaptor.getTaskInstanceByActivityId(any(), anyLong()))
.thenThrow(new RuntimeException("test exception"));

assertThrows(RuntimeException.class, () -> baseRuntimeFlowableDelegate.initCallable(delegateExecution));
}
}