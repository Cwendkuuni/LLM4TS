import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class BaseRuntimeFlowableDelegateTest {

 @Mock
 private DelegateExecution delegateExecution;

 @Mock
 private FlowableAdaptor flowableAdaptor;

 @Mock
 private EventPublisher eventPublisher;

 @Mock
 private FlowInstanceRepository flowInstanceRepository;

 @Mock
 private RetryExecutor retryExecutor;

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
 baseRuntimeFlowableDelegate.flowableAdaptor = flowableAdaptor;
 baseRuntimeFlowableDelegate.eventPublisher = eventPublisher;
 baseRuntimeFlowableDelegate.flowInstanceRepository = flowInstanceRepository;
 baseRuntimeFlowableDelegate.retryExecutor = retryExecutor;
 }

 @Test
 public void testRun() throws Exception {
 // Mock method calls
 Mockito.when(delegateExecution.getCurrentActivityId()).thenReturn("testActivityId");
 Mockito.when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(ArgumentMatchers.anyString()))
 .thenReturn(1L);
 Mockito.when(flowableAdaptor.getTaskInstanceByActivityId(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong()))
 .thenReturn(Optional.of(new FlowTaskInstance()));

 // Call run method
 baseRuntimeFlowableDelegate.run(delegateExecution);

 // Assert method calls
 Mockito.verify(eventPublisher).addEventListener(baseRuntimeFlowableDelegate.activeTaskStatisticsListener);
 Mockito.verify(eventPublisher).addEventListener(baseRuntimeFlowableDelegate.taskInstanceCreatedlistener);
 Mockito.verify(eventPublisher).publishEvent(ArgumentMatchers.any(ServiceTaskStartedEvent.class));
 Mockito.verify(eventPublisher).removeEventListener(baseRuntimeFlowableDelegate.taskInstanceCreatedlistener);
 Mockito.verify(eventPublisher).removeEventListener(baseRuntimeFlowableDelegate.activeTaskStatisticsListener);
 }

 @Test
 public void testExecute() throws Exception {
 // Mock method calls
 baseRuntimeFlowableDelegate.execute(delegateExecution);

 // Assert method calls
 Mockito.verify(eventPublisher).removeEventListener(baseRuntimeFlowableDelegate.taskInstanceCreatedlistener);
 Mockito.verify(eventPublisher).removeEventListener(baseRuntimeFlowableDelegate.activeTaskStatisticsListener);
 }

 @Test
 public void testIsDone() {
 Assert.assertFalse(baseRuntimeFlowableDelegate.isDone());
 }

 @Test
 public void testGet() throws InterruptedException, ExecutionException {
 // Set done to true
 baseRuntimeFlowableDelegate.done = true;

 // Call get method
 String result = baseRuntimeFlowableDelegate.get();

 // Assert result
 Assert.assertEquals("test", result);
 }

 @Test(expected = ExecutionException.class)
 public void testGetWithException() throws InterruptedException, ExecutionException {
 // Set thrown to an Exception
 baseRuntimeFlowableDelegate.thrown = new RuntimeException();

 // Call get method
 baseRuntimeFlowableDelegate.get();
 }

 @Test(expected = TimeoutException.class)
 public void testGetWithTimeout()
 throws InterruptedException, ExecutionException, TimeoutException {
 // Set latch count to 0
 baseRuntimeFlowableDelegate.latch.countDown();

 // Call get method with timeout
 baseRuntimeFlowableDelegate.get(100, TimeUnit.MILLISECONDS);
 }

 @Test
 public void testInitTargetTaskInstanceId() {
 // Mock method calls
 Mockito.when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(ArgumentMatchers.anyString()))
 .thenReturn(1L);
 Mockito.when(flowableAdaptor.getTaskInstanceByActivityId(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong()))
 .thenReturn(Optional.of(new FlowTaskInstance()));

 // Call initTargetTaskInstanceId method
 baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

 // Assert properties
 Assert.assertNotNull(baseRuntimeFlowableDelegate.getFlowInstanceId());
 Assert.assertNotNull(baseRuntimeFlowableDelegate.getTargetTaskInstanceId());
 Assert.assertNotNull(baseRuntimeFlowableDelegate.getTaskType());
 Assert.assertNotNull(baseRuntimeFlowableDelegate.getStrategyConfig());
 }

 @Test
 public void testBindToFlowTaskInstance() {
 FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
 baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);

 // Assert properties
 Assert.assertEquals(baseRuntimeFlowableDelegate, flowTaskInstance.getServiceTask());
 }

 @Test
 public void testUpdateFlowInstanceStatus() {
 // Mock method calls
 Mockito.when(flowInstanceRepository.updateStatusById(ArgumentMatchers.anyLong(), ArgumentMatchers.any(FlowStatus.class)))
 .thenReturn(true);

 // Call updateFlowInstanceStatus method
 boolean result = baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.TEST);

 // Assert result
 Assert.assertTrue(result);
 }

 @Test
 public void testInitCallable() {
 // Call initCallable method
 Callable<String> callable = baseRuntimeFlowableDelegate.initCallable(delegateExecution);

 // Assert callable
 Assert.assertNotNull(callable);
 }
}