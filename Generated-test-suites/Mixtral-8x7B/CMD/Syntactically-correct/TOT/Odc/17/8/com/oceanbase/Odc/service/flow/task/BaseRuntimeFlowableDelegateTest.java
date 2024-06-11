import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.test.PluggableFlowableTestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BaseRuntimeFlowableDelegateTest extends PluggableFlowableTestCase {

 private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;
 private DelegateExecution delegateExecution;
 private FlowInstanceRepository flowInstanceRepository;
 private FlowableAdaptor flowableAdaptor;
 private EventPublisher eventPublisher;
 private FlowTaskInstance flowTaskInstance;

 @Before
 public void setup() {
 delegateExecution = mock(DelegateExecution.class);
 flowInstanceRepository = mock(FlowInstanceRepository.class);
 flowableAdaptor = mock(FlowableAdaptor.class);
 eventPublisher = mock(EventPublisher.class);
 flowTaskInstance = mock(FlowTaskInstance.class);

 baseRuntimeFlowableDelegate = Mockito.spy(new TestRuntimeFlowableDelegate());
 baseRuntimeFlowableDelegate.setFlowableAdaptor(flowableAdaptor);
 baseRuntimeFlowableDelegate.setEventPublisher(eventPublisher);
 baseRuntimeFlowableDelegate.setFlowInstanceRepository(flowInstanceRepository);
 }

 @Test
 public void testRunWithInitializationException() throws Exception {
 Mockito.doThrow(new Exception("Initialization failed")).when(baseRuntimeFlowableDelegate).initCallable(delegateExecution);

 ExecutionException exception = assertThrows(ExecutionException.class, () -> {
 baseRuntimeFlowableDelegate.run(delegateExecution);
 });

 assertEquals("Initialization failed", exception.getCause().getMessage());
 }

 @Test
 public void testRunWithServiceTaskException() throws Exception {
 when(baseRuntimeFlowableDelegate.initCallable(delegateExecution)).thenReturn(() -> {
 throw new RuntimeException("Service task failed");
 });

 ExecutionException exception = assertThrows(ExecutionException.class, () -> {
 baseRuntimeFlowableDelegate.run(delegateExecution);
 });

 assertEquals("Service task failed", exception.getCause().getMessage());
 }

 @Test
 public void testGetFutureWithInitializationException() throws Exception {
 Mockito.doThrow(new Exception("Initialization failed")).when(baseRuntimeFlowableDelegate).initCallable(delegateExecution);
 Future<String> future = baseRuntimeFlowableDelegate.getFuture();

 ExecutionException exception = assertThrows(ExecutionException.class, future::get);

 assertEquals("Initialization failed", exception.getCause().getMessage());
 }

 @Test
 public void testGetFutureWithServiceTaskException() throws Exception {
 when(baseRuntimeFlowableDelegate.initCallable(delegateExecution)).thenReturn(() -> {
 throw new RuntimeException("Service task failed");
 });

 Future<String> future = baseRuntimeFlowableDelegate.getFuture();

 ExecutionException exception = assertThrows(ExecutionException.class, future::get);

 assertEquals("Service task failed", exception.getCause().getMessage());
 }

 @Test
 public void testGetFutureWithTimeout() throws Exception {
 when(baseRuntimeFlowableDelegate.initCallable(delegateExecution)).thenReturn(() -> {
 Thread.sleep(1000);
 return "Result";
 });

 Future<String> future = baseRuntimeFlowableDelegate.getFuture(500, TimeUnit.MILLISECONDS);

 TimeoutException exception = assertThrows(TimeoutException.class, future::get);
 }

 @Test
 public void testGetFutureWithSuccess() throws Exception {
 when(baseRuntimeFlowableDelegate.initCallable(delegateExecution)).thenReturn(() -> "Result");

 Future<String> future = baseRuntimeFlowableDelegate.getFuture();

 assertEquals("Result", future.get());
 }

 @Test
 public void testInitTargetTaskInstanceId() {
 String processDefinitionId = "process-1";

 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId)).thenReturn(1L);
 when(flowableAdaptor.getTaskInstanceByActivityId(any(), anyLong())).thenReturn(Optional.of(flowTaskInstance));

 baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

 assertEquals(1L, baseRuntimeFlowableDelegate.getFlowInstanceId());
 assertEquals(flowTaskInstance, baseRuntimeFlowableDelegate.getTargetTaskInstance());
 assertEquals(TaskType.TEST, baseRuntimeFlowableDelegate.getTaskType());
 }

 @Test
 public void testBindToFlowTaskInstance() {
 FlowTaskInstance taskInstance = mock(FlowTaskInstance.class);

 baseRuntimeFlowableDelegate.bindToFlowTaskInstance(taskInstance);

 Mockito.verify(taskInstance).bindServiceTask(baseRuntimeFlowableDelegate);
 }

 @Test
 public void testUpdateFlowInstanceStatus() {
 baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

 Mockito.verify(flowInstanceRepository).updateStatusById(anyLong(), FlowStatus.COMPLETED);
 }

 private static class TestRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {
 @Override
 protected Callable<String> initCallable(DelegateExecution execution) {
 return () -> "Result";
 }
 }
}