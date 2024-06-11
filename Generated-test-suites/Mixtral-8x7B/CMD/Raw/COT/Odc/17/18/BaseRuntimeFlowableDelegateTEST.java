import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class BaseRuntimeFlowableDelegateTest {

 private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;
 private DelegateExecution delegateExecution;
 private FlowableAdaptor flowableAdaptor;
 private EventPublisher eventPublisher;
 private FlowInstanceRepository flowInstanceRepository;
 private Callable<String> callable;
 private CountDownLatch latch;
 private RetryExecutor retryExecutor;
 private TaskInstanceCreatedListener taskInstanceCreatedlistener;
 private ActiveTaskStatisticsListener activeTaskStatisticsListener;

 @Before
 public void setup() {
 flowableAdaptor = Mockito.mock(FlowableAdaptor.class);
 eventPublisher = Mockito.mock(EventPublisher.class);
 flowInstanceRepository = Mockito.mock(FlowInstanceRepository.class);

 baseRuntimeFlowableDelegate = Mockito.spy(new TestRuntimeFlowableDelegate());
 baseRuntimeFlowableDelegate.flowableAdaptor = flowableAdaptor;
 baseRuntimeFlowableDelegate.eventPublisher = eventPublisher;
 baseRuntimeFlowableDelegate.flowInstanceRepository = flowInstanceRepository;

 delegateExecution = Mockito.mock(DelegateExecution.class);
 callable = Mockito.mock(Callable.class);
 latch = new CountDownLatch(1);
 retryExecutor = Mockito.mock(RetryExecutor.class);
 taskInstanceCreatedlistener = Mockito.mock(TaskInstanceCreatedListener.class);
 activeTaskStatisticsListener = Mockito.mock(ActiveTaskStatisticsListener.class);
 }

 @Test
 public void testInitTargetTaskInstanceId() {
 String processDefinitionId = "testProcessDefinitionId";

 // Verify getFlowInstanceIdByProcessDefinitionId is called
 Mockito.when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId))
 .thenReturn(Optional.of(1L));

 baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

 Mockito.verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
 }

 @Test
 public void testPreHandle() throws Exception {
 // Verify preHandle is called when run is called
 Mockito.doNothing().when(baseRuntimeFlowableDelegate).preHandle(delegateExecution);

 baseRuntimeFlowableDelegate.run(delegateExecution);

 Mockito.verify(baseRuntimeFlowableDelegate).preHandle(delegateExecution);
 }

 @Test
 public void testIsDone() {
 // Verify isDone returns false before latch.countDown() is called
 Assert.assertFalse(baseRuntimeFlowableDelegate.isDone());

 baseRuntimeFlowableDelegate.latch.countDown();

 // Verify isDone returns true after latch.countDown() is called
 Assert.assertTrue(baseRuntimeFlowableDelegate.isDone());
 }

 @Test(expected = ExecutionException.class)
 public void testGet_InterruptedException() throws InterruptedException, ExecutionException {
 // Verify get throws ExecutionException when InterruptedException is thrown
 baseRuntimeFlowableDelegate.latch.await();

 Mockito.when(baseRuntimeFlowableDelegate.callable.call())
 .thenThrow(new InterruptedException());

 baseRuntimeFlowableDelegate.get();
 }

 @Test(expected = ExecutionException.class)
 public void testGet_ExecutionException() throws InterruptedException, ExecutionException {
 // Verify get throws ExecutionException when ExecutionException is thrown
 baseRuntimeFlowableDelegate.latch.await();

 Mockito.when(baseRuntimeFlowableDelegate.callable.call())
 .thenThrow(new ExecutionException(new Exception()));

 baseRuntimeFlowableDelegate.get();
 }

 @Test(expected = TimeoutException.class)
 public void testGet_TimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
 // Verify get throws TimeoutException when isDone() returns false after timeout
 Instant start = Instant.now();

 baseRuntimeFlowableDelegate.get(1, TimeUnit.MILLISECONDS);

 Assert.assertTrue(Duration.between(start, Instant.now()).toMillis() >= 1);
 }

 @Test
 public void testBindToFlowTaskInstance() {
 FlowTaskInstance flowTaskInstance = Mockito.mock(FlowTaskInstance.class);

 baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);

 Mockito.verify(flowTaskInstance).bindServiceTask(baseRuntimeFlowableDelegate);
 }

 @Test
 public void testUpdateFlowInstanceStatus() {
 Long flowInstanceId = 1L;

 // Verify updateStatusById is called with correct parameters
 baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.TEST);

 Mockito.verify(flowInstanceRepository)
 .updateStatusById(flowInstanceId, FlowStatus.TEST);
 }

 static class TestRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {
 @Override
 protected Callable<String> initCallable(DelegateExecution execution) {
 return callable;
 }

 @Override
 protected void preHandle(DelegateExecution execution) {}
 }
}