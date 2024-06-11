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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testInitTargetTaskInstanceId() {
 when(delegateExecution.getCurrentActivityId()).thenReturn("activityId");
 when(delegateExecution.getProcessDefinitionId()).thenReturn("processDefinitionId");
 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(Optional.of(Optional.of(1L)));
 when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(Optional.of(task));

 baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

 assertEquals(Long.valueOf(1), baseRuntimeFlowableDelegate.getTargetTaskInstanceId());
 assertEquals(TaskType.SERVICE_TASK, baseRuntimeFlowableDelegate.getTaskType());
 }

 @Test
 public void testPreHandle() throws Exception {
 when(delegateExecution.getVariable(anyString())).thenReturn("variableValue");
 Callable<String> callable = Mockito.mock(Callable.class);
 when(callable.call()).thenReturn("returnValue");

 baseRuntimeFlowableDelegate.preHandle(delegateExecution);
 baseRuntimeFlowableDelegate.initCallable(delegateExecution);

 String result = baseRuntimeFlowableDelegate.run(delegateExecution);

 assertEquals("returnValue", result);
 }

 @Test
 public void testGet() throws Exception {
 when(delegateExecution.getVariable(anyString())).thenReturn("variableValue");
 Callable<String> callable = Mockito.mock(Callable.class);
 when(callable.call()).thenReturn("returnValue");

 baseRuntimeFlowableDelegate.preHandle(delegateExecution);
 baseRuntimeFlowableDelegate.initCallable(delegateExecution);

 String result = baseRuntimeFlowableDelegate.get();

 assertEquals("returnValue", result);
 }

 @Test
 public void testGetWithException() throws Exception {
 when(delegateExecution.getVariable(anyString())).thenReturn("variableValue");
 Callable<String> callable = Mockito.mock(Callable.class);
 when(callable.call()).thenThrow(new RuntimeException("Exception"));

 baseRuntimeFlowableDelegate.preHandle(delegateExecution);
 baseRuntimeFlowableDelegate.initCallable(delegateExecution);

 try {
 baseRuntimeFlowableDelegate.get();
 } catch (ExecutionException e) {
 assertEquals("Exception", e.getMessage());
 }
 }

 @Test
 public void testGetWithTimeout() throws Exception {
 when(delegateExecution.getVariable(anyString())).thenReturn("variableValue");
 Callable<String> callable = Mockito.mock(Callable.class);
 when(callable.call()).thenReturn("returnValue");

 baseRuntimeFlowableDelegate.preHandle(delegateExecution);
 baseRuntimeFlowableDelegate.initCallable(delegateExecution);

 String result = baseRuntimeFlowableDelegate.get(1, TimeUnit.MILLISECONDS);

 assertEquals("returnValue", result);
 }

 @Test
 public void testBindToFlowTaskInstance() {
 FlowTaskInstance flowTaskInstance = Mockito.mock(FlowTaskInstance.class);
 when(flowTaskInstance.getId()).thenReturn(1L);
 when(flowTaskInstance.getTaskType()).thenReturn(TaskType.SERVICE_TASK);
 when(flowTaskInstance.getStrategyConfig()).thenReturn(null);

 baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);

 Mockito.verify(flowTaskInstance, Mockito.times(1)).bindServiceTask(baseRuntimeFlowableDelegate);
 }

 @Test
 public void testUpdateFlowInstanceStatus() {
 when(flowInstanceRepository.updateStatusById(anyLong(), any())).thenReturn(true);

 baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.STARTED);

 Mockito.verify(flowInstanceRepository, Mockito.times(1)).updateStatusById(anyLong(), ArgumentMatchers.eq(FlowStatus.STARTED));
 }

 @Test
 public void testInitCallable() {
 Callable<String> callable = baseRuntimeFlowableDelegate.initCallable(delegateExecution);

 assertNotNull(callable);
 }
}