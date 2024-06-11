import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BaseRuntimeFlowableDelegateTest {

 @Mock
 private DelegateExecution delegateExecution;
 @Mock
 private FlowableAdaptor flowableAdaptor;
 @Mock
 private FlowInstanceRepository flowInstanceRepository;
 @Mock
 private EventPublisher eventPublisher;
 @Mock
 private ActiveTaskStatisticsListener activeTaskStatisticsListener;
 @Mock
 private TaskInstanceCreatedListener taskInstanceCreatedlistener;
 @Mock
 private RetryExecutor retryExecutor;
 private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;
 private Callable<String> callable;
 private CountDownLatch latch;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 callable = mock(Callable.class);
 latch = new CountDownLatch(1);

 baseRuntimeFlowableDelegate = spy(new BaseRuntimeFlowableDelegate<String>() {
 @Override
 protected Callable<String> initCallable(DelegateExecution execution) {
 return callable;
 }
 });

 doNothing().when(baseRuntimeFlowableDelegate).preHandle(delegateExecution);
 doAnswer(AdditionalAnswers.rewrite(() -> latch.countDown())).when(callable).call();
 when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenReturn(null);
 when(baseRuntimeFlowableDelegate.flowableAdaptor).thenReturn(flowableAdaptor);
 when(baseRuntimeFlowableDelegate.eventPublisher).thenReturn(eventPublisher);
 when(baseRuntimeFlowableDelegate.flowInstanceRepository).thenReturn(flowInstanceRepository);
 when(baseRuntimeFlowableDelegate.retryExecutor).thenReturn(retryExecutor);
 }

 @Test
 public void testRun_initializationException() throws Exception {
 doThrow(new ServiceTaskError(new RuntimeException("Initialization error")))
 .when(baseRuntimeFlowableDelegate)
 .initTargetTaskInstanceId(delegateExecution);

 baseRuntimeFlowableDelegate.run(delegateExecution);

 verify(delegateExecution, times(1)).setVariable(eq("exception"), any(RuntimeException.class));
 assertFalse(baseRuntimeFlowableDelegate.isDone());
 }

 @Test
 public void testRun_executionException() throws Exception {
 doThrow(new ServiceTaskError(new RuntimeException("Execution error")))
 .when(callable)
 .call();

 baseRuntimeFlowableDelegate.run(delegateExecution);

 verify(delegateExecution, times(1)).setVariable(eq("exception"), any(RuntimeException.class));
 assertFalse(baseRuntimeFlowableDelegate.isDone());
 }

 @Test
 public void testRun() throws Exception {
 String expectedResult = "Test Result";
 when(callable.call()).thenReturn(expectedResult);

 baseRuntimeFlowableDelegate.run(delegateExecution);

 verify(delegateExecution, never()).setVariable(eq("exception"), any(RuntimeException.class));
 assertEquals(expectedResult, baseRuntimeFlowableDelegate.get());
 }

 @Test
 public void testExecute() throws Exception {
 baseRuntimeFlowableDelegate.execute(delegateExecution);

 verify(eventPublisher, times(1)).removeEventListener(taskInstanceCreatedlistener);
 verify(eventPublisher, times(1)).removeEventListener(activeTaskStatisticsListener);
 }

 @Test
 public void testIsDone() throws Exception {
 assertFalse(baseRuntimeFlowableDelegate.isDone());

 doAnswer(AdditionalAnswers.rewrite(() -> latch.countDown())).when(callable).call();
 baseRuntimeFlowableDelegate.run(delegateExecution);

 assertTrue(baseRuntimeFlowableDelegate.isDone());
 }

 @Test
 public void testGet_interruptedException() throws Exception {
 when(latch.await()).thenThrow(new InterruptedException());

 assertThrows(InterruptedException.class, () -> baseRuntimeFlowableDelegate.get());
 }

 @Test
 public void testGet_executionException() throws Exception {
 doThrow(new ServiceTaskError(new RuntimeException("Execution error"))).when(callable).call();

 assertThrows(ExecutionException.class, () -> baseRuntimeFlowableDelegate.get());
 }

 @Test
 public void testGet() throws Exception {
 String expectedResult = "Test Result";
 when(callable.call()).thenReturn(expectedResult);

 assertEquals(expectedResult, baseRuntimeFlowableDelegate.get());
 }

 @Test
 public void testGet_timeoutException() throws Exception {
 when(latch.await(anyLong(), any(TimeUnit.class))).thenReturn(false);

 assertThrows(TimeoutException.class, () -> baseRuntimeFlowableDelegate.get(1L, TimeUnit.SECONDS));
 }

 @Test
 public void testInitTargetTaskInstanceId_exception() throws Exception {
 doThrow(new ServiceTaskError(new RuntimeException("Exception in initTargetTaskInstanceId")))
 .when(baseRuntimeFlowableDelegate)
 .initTargetTaskInstanceId(delegateExecution);

 baseRuntimeFlowableDelegate.execute(delegateExecution);

 verify(delegateExecution, times(1)).setVariable(eq("exception"), any(RuntimeException.class));
 }

 @Test
 public void testUpdateFlowInstanceStatus() throws Exception {
 String flowStatus = "TEST_STATUS";

 doNothing().when(flowInstanceRepository).updateStatusById(anyLong(), eq(flowStatus));

 baseRuntimeFlowableDelegate.updateFlowInstanceStatus(flowStatus);

 verify(flowInstanceRepository, times(1)).updateStatusById(anyLong(), eq(flowStatus));
 }

 @Test
 public void testBindToFlowTaskInstance() throws Exception {
 FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);

 baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);

 verify(flowTaskInstance, times(1)).bindServiceTask(baseRuntimeFlowableDelegate);
 }
}