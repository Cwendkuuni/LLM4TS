import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.task.service.delegate.DelegateTask;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class BaseRuntimeFlowableDelegateTest {

 @Mock
 private DelegateExecution delegateExecution;

 @Mock
 private DelegateTask delegateTask;

 @Mock
 private FlowableAdaptor flowableAdaptor;

 @Mock
 private EventPublisher eventPublisher;

 @Mock
 private FlowInstanceRepository flowInstanceRepository;

 @Mock
 private RetryExecutor retryExecutor;

 @InjectMocks
 @Autowired
 private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

 @Before
 public void setup() {
 MockitoAnnotations.openMocks(this);
 baseRuntimeFlowableDelegate = spy(baseRuntimeFlowableDelegate);
 }

 @Test
 public void testGetters() {
 assertEquals("", baseRuntimeFlowableDelegate.getActivityId());
 assertEquals(null, baseRuntimeFlowableDelegate.getTargetTaskInstanceId());
 assertEquals(null, baseRuntimeFlowableDelegate.getTaskType());
 assertEquals(null, baseRuntimeFlowableDelegate.getFlowInstanceId());
 assertEquals(null, baseRuntimeFlowableDelegate.getStrategyConfig());
 }

 @Test
 public void testInitTargetTaskInstanceId() {
 when(delegateExecution.getProcessDefinitionId()).thenReturn("processDefinitionId");
 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
 .thenReturn(Optional.of(Optional.of(1L)));
 when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
 .thenReturn(Optional.of(new FlowTaskInstance()));

 baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

 verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(anyString());
 verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(anyString(), anyLong());
 }

 @Test
 public void testPreHandle() throws Exception {
 baseRuntimeFlowableDelegate.preHandle(delegateExecution);

 verify(delegateExecution, times(1)).getProcessDefinitionId();
 verify(delegateExecution, times(1)).getProcessInstanceId();
 }

 @Test
 public void testRun_ExceptionDuringInitialization() throws Exception {
 when(delegateExecution.getCurrentActivityId()).thenThrow(new Exception("Initialization exception"));

 baseRuntimeFlowableDelegate.run(delegateExecution);

 verify(delegateExecution, times(1)).getCurrentActivityId();
 verify(eventPublisher, never()).publishEvent(any(ServiceTaskStartedEvent.class));
 }

 @Test
 public void testRun_ExceptionDuringExecution() throws Exception {
 when(delegateExecution.getCurrentActivityId()).thenReturn("activityId");
 doReturn(new RuntimeException("Execution exception")).when(baseRuntimeFlowableDelegate)
 .initCallable(delegateExecution);

 baseRuntimeFlowableDelegate.run(delegateExecution);

 verify(delegateExecution, times(1)).getCurrentActivityId();
 verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
 }

 @Test
 public void testRun_Success() throws Exception {
 when(delegateExecution.getCurrentActivityId()).thenReturn("activityId");
 when(baseRuntimeFlowableDelegate.initCallable(delegateExecution)).thenReturn(
 () -> "returnObject");

 baseRuntimeFlowableDelegate.run(delegateExecution);

 verify(delegateExecution, times(1)).getCurrentActivityId();
 verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
 assertEquals("returnObject", baseRuntimeFlowableDelegate.get());
 }

 @Test
 public void testExecute() throws Exception {
 baseRuntimeFlowableDelegate.execute(delegateExecution);

 verify(eventPublisher, times(1)).removeEventListener(any(TaskInstanceCreatedListener.class));
 verify(eventPublisher, times(1)).removeEventListener(any(ActiveTaskStatisticsListener.class));
 }

 @Test
 public void testIsDone() {
 assertTrue(baseRuntimeFlowableDelegate.isDone());
 }

 @Test
 public void testGet_InterruptedException() throws Exception {
 baseRuntimeFlowableDelegate.latch = new CountDownLatch(1);

 try {
 baseRuntimeFlowableDelegate.get();
 } catch (InterruptedException e) {
 // expected
 }

 verify(baseRuntimeFlowableDelegate.latch, times(1)).await();
 assertNull(baseRuntimeFlowableDelegate.returnObject);
 }

 @Test
 public void testGet_ExecutionException() throws Exception {
 baseRuntimeFlowableDelegate.thrown = new Exception("Execution exception");

 try {
 baseRuntimeFlowableDelegate.get();
 } catch (ExecutionException e) {
 // expected
 assertEquals("Execution exception", e.getMessage());
 }

 verify(baseRuntimeFlowableDelegate.latch, times(1)).await();
 assertNull(baseRuntimeFlowableDelegate.returnObject);
 }

 @Test
 public void testGet_Success() throws Exception {
 baseRuntimeFlowableDelegate.returnObject = "returnObject";

 assertEquals("returnObject", baseRuntimeFlowableDelegate.get());
 }

 @Test
 public void testGet_TimeoutException() throws Exception {
 baseRuntimeFlowableDelegate.latch = new CountDownLatch(1);

 try {
 baseRuntimeFlowableDelegate.get(1, TimeUnit.MILLISECONDS);
 } catch (TimeoutException e) {
 // expected
 }

 verify(baseRuntimeFlowableDelegate.latch, times(1)).await(1, TimeUnit.MILLISECONDS);
 assertNull(baseRuntimeFlowableDelegate.returnObject);
 }

 @Test
 public void testBindToFlowTaskInstance() {
 FlowTaskInstance taskInstance = new FlowTaskInstance();
 baseRuntimeFlowableDelegate.bindToFlowTaskInstance(taskInstance);

 verify(taskInstance, times(1)).bindServiceTask(baseRuntimeFlowableDelegate);
 }

 @Test
 public void testUpdateFlowInstanceStatus() {
 when(flowInstanceRepository.updateStatusById(anyLong(), any()))
 .thenReturn(true);

 baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

 verify(flowInstanceRepository, times(1)).updateStatusById(anyLong(), any());
 }

 @Test
 public void testInitCallable() {
 when(delegateExecution.getCurrentActivityId()).thenReturn("activityId");
 Callable<String> callable = baseRuntimeFlowableDelegate.initCallable(delegateExecution);

 verify(delegateExecution, times(1)).getCurrentActivityId();
 verify(baseRuntimeFlowableDelegate, never()).preHandle(any());
 }

 @Test
 public void testInitCallable_PreHandleCalled() throws Exception {
 when(delegateExecution.getCurrentActivityId()).thenReturn("activityId");
 baseRuntimeFlowableDelegate.preHandle(delegateExecution);
 Callable<String> callable = baseRuntimeFlowableDelegate.initCallable(delegateExecution);

 verify(delegateExecution, times(1)).getCurrentActivityId();
 verify(baseRuntimeFlowableDelegate, times(1)).preHandle(any());
 }

 @Test
 public void testInitCallable_NullReturn() {
 when(delegateExecution.getCurrentActivityId()).thenReturn("activityId");
 when(baseRuntimeFlowableDelegate.preHandle(any())).thenReturn(null);
 Callable<String> callable = baseRuntimeFlowableDelegate.initCallable(delegateExecution);

 verify(delegateExecution, times(1)).getCurrentActivityId();
 verify(baseRuntimeFlowableDelegate, times(1)).preHandle(any());
 assertTrue(callable == null);
 }
}