import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class BaseRuntimeFlowableDelegateTest {

 @Mock
 private DelegateExecution delegateExecution;

 @Mock
 private FlowableAdaptor flowableAdaptor;

 @Mock
 private EventPublisher eventPublisher;

 @Mock
 private FlowInstanceRepository flowInstanceRepository;

 @InjectMocks
 private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

 private CountDownLatch latch;

 @Before
 public void setUp() {
 latch = new CountDownLatch(1);
 MockitoAnnotations.initMocks(this);

 ReflectionTestUtils.setField(baseRuntimeFlowableDelegate, "retryExecutor",
 Mockito.mock(RetryExecutor.class));
 ReflectionTestUtils.setField(baseRuntimeFlowableDelegate, "activeTaskStatisticsListener",
 Mockito.mock(ActiveTaskStatisticsListener.class));
 ReflectionTestUtils.setField(baseRuntimeFlowableDelegate, "taskInstanceCreatedlistener",
 Mockito.mock(TaskInstanceCreatedListener.class));

 when(delegateExecution.getProcessDefinitionId()).thenReturn("processDefinitionId");
 when(delegateExecution.getCurrentActivityId()).thenReturn("activityId");
 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(Optional.of(1L));
 when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(Optional.of(Mockito.mock(FlowTaskInstance.class)));
 }

 @Test
 public void testConstructor() {
 assertNotNull(baseRuntimeFlowableDelegate.getActivityId());
 assertNotNull(baseRuntimeFlowableDelegate.getTargetTaskInstanceId());
 assertNotNull(baseRuntimeFlowableDelegate.getFlowInstanceId());
 assertNull(baseRuntimeFlowableDelegate.getReturnObject());
 assertNull(baseRuntimeFlowableDelegate.getThrown());
 assertFalse(baseRuntimeFlowableDelegate.isDone());
 }

 @Test
 public void testInitTargetTaskInstanceId() {
 baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

 verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(anyString());
 verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(anyString(), anyLong());
 }

 @Test
 public void testPreHandle() throws Exception {
 doAnswer(invocation -> {
 latch.countDown();
 return null;
 }).when(baseRuntimeFlowableDelegate).preHandle(delegateExecution);

 baseRuntimeFlowableDelegate.execute(delegateExecution);

 latch.await();

 verify(baseRuntimeFlowableDelegate, times(1)).preHandle(delegateExecution);
 }

 @Test
 public void testRunWithException() throws Exception {
 when(baseRuntimeFlowableDelegate.initCallable(delegateExecution)).thenReturn(() -> {
 throw new RuntimeException("Test Exception");
 });

 baseRuntimeFlowableDelegate.run(delegateExecution);

 verify(delegateExecution, times(1)).setVariable(anyString(), any());
 verify(eventPublisher, times(1)).publishEvent(any());
 }

 @Test(expected = ExecutionException.class)
 public void testGetWithInterruptedException() throws InterruptedException, ExecutionException, TimeoutException {
 doThrow(new InterruptedException("Test InterruptedException")).when(latch).await();

 baseRuntimeFlowableDelegate.get();
 }

 @Test
 public void testGetWithExceptionInCallable() throws InterruptedException, ExecutionException, TimeoutException {
 when(baseRuntimeFlowableDelegate.initCallable(delegateExecution)).thenReturn(() -> {
 throw new RuntimeException("Test Exception");
 });

 ExecutionException exception = assertThrows(ExecutionException.class, () -> baseRuntimeFlowableDelegate.get());

 assertEquals("Test Exception", exception.getCause().getMessage());
 }

 @Test
 public void testIsDone() throws Exception {
 baseRuntimeFlowableDelegate.run(delegateExecution);

 assertTrue(baseRuntimeFlowableDelegate.isDone());
 }

 @Test
 public void testGet() throws Exception {
 when(baseRuntimeFlowableDelegate.initCallable(delegateExecution)).thenReturn(() -> "Test Result");

 String result = baseRuntimeFlowableDelegate.get();

 assertEquals("Test Result", result);
 }

 @Test
 public void testGetWithTimeoutException() throws Exception {
 when(baseRuntimeFlowableDelegate.initCallable(delegateExecution)).thenReturn(() -> {
 throw new RuntimeException("Test Exception");
 });

 assertThrows(TimeoutException.class, () -> baseRuntimeFlowableDelegate.get(0, TimeUnit.MILLISECONDS));
 }

 @Test
 public void testGetWithExceptionInCallableAndTimeout() throws Exception {
 when(baseRuntimeFlowableDelegate.initCallable(delegateExecution)).thenReturn(() -> {
 throw new RuntimeException("Test Exception");
 });

 TimeoutException exception = assertThrows(TimeoutException.class, () -> baseRuntimeFlowableDelegate.get(1, TimeUnit.MILLISECONDS));

 assertEquals("Test Exception", exception.getCause().getMessage());
 }

 @Test
 public void testBindToFlowTaskInstance() {
 FlowTaskInstance flowTaskInstance = Mockito.mock(FlowTaskInstance.class);

 baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);

 verify(flowTaskInstance, times(1)).bindServiceTask(baseRuntimeFlowableDelegate);
 }

 @Test
 public void testUpdateFlowInstanceStatus() {
 baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

 verify(flowInstanceRepository, times(1)).updateStatusById(anyLong(), any(FlowStatus.class));
 }

 @Test
 public void testInitCallable() {
 Callable<String> callable = spy(baseRuntimeFlowableDelegate.initCallable(delegateExecution));

 baseRuntimeFlowableDelegate.execute(delegateExecution);

 verify(callable, times(1)).call();
 }

 @Test
 public void testGetReturnObject() throws Exception {
 when(baseRuntimeFlowableDelegate.initCallable(delegateExecution)).thenReturn(() -> "Test Result");

 baseRuntimeFlowableDelegate.run(delegateExecution);

 assertEquals("Test Result", baseRuntimeFlowableDelegate.getReturnObject());
 }

 @Test
 public void testGetThrown() throws Exception {
 when(baseRuntimeFlowableDelegate.initCallable(delegateExecution)).thenThrow(new RuntimeException("Test Exception"));

 baseRuntimeFlowableDelegate.run(delegateExecution);

 assertEquals("Test Exception", baseRuntimeFlowableDelegate.getThrown().getMessage());
 }

 @Test
 public void testExecute() throws Exception {
 baseRuntimeFlowableDelegate.execute(delegateExecution);

 verify(eventPublisher, times(1)).addEventListener(any(TaskInstanceCreatedListener.class));
 verify(eventPublisher, times(1)).addEventListener(any(ActiveTaskStatisticsListener.class));

 verify(eventPublisher, times(1)).removeEventListener(any(TaskInstanceCreatedListener.class));
 verify(eventPublisher, times(1)).removeEventListener(any(ActiveTaskStatisticsListener.class));
 }

 @Test
 public void testIsCancelled() throws Exception {
 baseRuntimeFlowableDelegate.run(delegateExecution);

 assertFalse(baseRuntimeFlowableDelegate.isCancelled());
 }

 @Test
 public void testCancel() throws Exception {
 baseRuntimeFlowableDelegate.run(delegateExecution);

 baseRuntimeFlowableDelegate.cancel(true);

 verify(delegateExecution, times(1)).setVariable(anyString(), any());
 }

}