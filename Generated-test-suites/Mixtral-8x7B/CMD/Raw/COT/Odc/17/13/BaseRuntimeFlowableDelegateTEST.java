import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.event.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalMatchers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
 private Callable<String> callable;
 private CountDownLatch latch;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 callable = Mockito.mock(Callable.class);
 latch = new CountDownLatch(1);
 baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
 @Override
 protected Callable<String> initCallable(DelegateExecution execution) {
 return callable;
 }
 };
 baseRuntimeFlowableDelegate.setFlowableAdaptor(flowableAdaptor);
 baseRuntimeFlowableDelegate.setEventPublisher(eventPublisher);
 baseRuntimeFlowableDelegate.setFlowInstanceRepository(flowInstanceRepository);
 baseRuntimeFlowableDelegate.setRetryExecutor(retryExecutor);
 }

 @Test
 public void testRun_InitializationTaskFailed() throws Exception {
 doThrow(new ServiceTaskError(new RuntimeException("Initialization task failed")))
 .when(callable)
 .call();

 baseRuntimeFlowableDelegate.run(delegateExecution);

 verify(eventPublisher, times(1))
 .publishEvent(ArgumentMatchers.argThat(event -> event instanceof ServiceTaskStartedEvent));
 verify(delegateExecution, times(1))
 .setVariable(BaseFlowableDelegate.VAR_FLOWABLE_DELEGATE_EXCEPTION,
 ArgumentMatchers.argThat(exception -> exception.getMessage()
 .equals("Initialization task failed")));
 }

 @Test
 public void testRun_ServiceTaskExecutionFailed() throws Exception {
 doThrow(new RuntimeException("Service task execution failed"))
 .when(callable)
 .call();

 baseRuntimeFlowableDelegate.run(delegateExecution);

 verify(eventPublisher, times(1))
 .publishEvent(ArgumentMatchers.argThat(event -> event instanceof ServiceTaskStartedEvent));
 verify(delegateExecution, times(1))
 .setVariable(BaseFlowableDelegate.VAR_FLOWABLE_DELEGATE_EXCEPTION,
 ArgumentMatchers.argThat(exception -> exception.getMessage()
 .equals("Service task execution failed")));
 }

 @Test
 public void testRun_Success() throws Exception {
 when(callable.call()).thenReturn("Test Result");

 baseRuntimeFlowableDelegate.run(delegateExecution);

 verify(eventPublisher, times(1))
 .publishEvent(ArgumentMatchers.argThat(event -> event instanceof ServiceTaskStartedEvent));
 verify(delegateExecution, never())
 .setVariable(BaseFlowableDelegate.VAR_FLOWABLE_DELEGATE_EXCEPTION, any());
 }

 @Test
 public void testExecute_Success() throws Exception {
 baseRuntimeFlowableDelegate.execute(delegateExecution);

 verify(eventPublisher, times(1))
 .removeEventListener(ArgumentMatchers.argThat(listener -> listener instanceof TaskInstanceCreatedListener));
 verify(eventPublisher, times(1))
 .removeEventListener(ArgumentMatchers.argThat(listener -> listener instanceof ActiveTaskStatisticsListener));
 }

 @Test
 public void testIsDone_ReturnsTrueWhenGetIsCalled() throws Exception {
 baseRuntimeFlowableDelegate.get();

 assertTrue(baseRuntimeFlowableDelegate.isDone());
 }

 @Test
 public void testGet_Success() throws Exception {
 when(callable.call()).thenReturn("Test Result");

 String result = baseRuntimeFlowableDelegate.get();

 assertEquals("Test Result", result);
 }

 @Test
 public void testGet_ThrowsInterruptedException() throws Exception {
 doThrow(new InterruptedException())
 .when(latch)
 .await();

 try {
 baseRuntimeFlowableDelegate.get();
 fail("Expected InterruptedException");
 } catch (InterruptedException e) {
 // Expected
 }
 }

 @Test
 public void testGet_ThrowsExecutionException() throws Exception {
 doThrow(new ExecutionException(new RuntimeException("Execution Exception")))
 .when(callable)
 .call();

 try {
 baseRuntimeFlowableDelegate.get();
 fail("Expected ExecutionException");
 } catch (ExecutionException e) {
 assertEquals("Execution Exception", e.getMessage());
 }
 }

 @Test
 public void testGet_ThrowsTimeoutException() throws Exception {
 doThrow(new TimeoutException())
 .when(latch)
 .await(AnyLong.ANY, any(TimeUnit.class));

 try {
 baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS);
 fail("Expected TimeoutException");
 } catch (TimeoutException e) {
 // Expected
 }
 }

 @Test
 public void testInitTargetTaskInstanceId_Success() {
 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
 .thenReturn(Optional.of(Optional.of(1L)));
 when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
 .thenReturn(Optional.of(Mockito.mock(FlowTaskInstance.class)));

 baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

 verify(flowableAdaptor, times(1))
 .getFlowInstanceIdByProcessDefinitionId(ArgumentMatchers.anyString());
 verify(flowableAdaptor, times(1))
 .getTaskInstanceByActivityId(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong());
 }

 @Test
 public void testInitTargetTaskInstanceId_ProcessDefinitionIdNotFound() {
 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
 .thenReturn(Optional.of(Optional.empty()));

 try {
 baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);
 fail("Expected PreConditions.validExists");
 } catch (PreConditions.PreConditionException e) {
 // Expected
 }
 }

 @Test
 public void testInitTargetTaskInstanceId_TaskInstanceNotFound() {
 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
 .thenReturn(Optional.of(Optional.of(1L)));
 when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
 .thenReturn(Optional.empty());

 try {
 baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);
 fail("Expected PreConditions.validExists");
 } catch (PreConditions.PreConditionException e) {
 // Expected
 }
 }

 @Test
 public void testBindToFlowTaskInstance_Success() {
 FlowTaskInstance taskInstance = Mockito.mock(FlowTaskInstance.class);
 when(taskInstance.getId()).thenReturn(1L);
 baseRuntimeFlowableDelegate.bindToFlowTaskInstance(taskInstance);

 verify(taskInstance, times(1)).bindServiceTask(baseRuntimeFlowableDelegate);
 }

 @Test
 public void testUpdateFlowInstanceStatus_Success() {
 when(flowInstanceRepository.updateStatusById(anyLong(), any()))
 .thenReturn(true);

 baseRuntimeFlowableDelegate.updateFlowInstanceStatus(Mockito.mock(FlowStatus.class));

 verify(flowInstanceRepository, times(1))
 .updateStatusById(ArgumentMatchers.anyLong(), ArgumentMatchers.any());
 }

 @Test
 public void testUpdateFlowInstanceStatus_Failure() {
 when(flowInstanceRepository.updateStatusById(anyLong(), any()))
 .thenReturn(false);

 try {
 baseRuntimeFlowableDelegate.updateFlowInstanceStatus(Mockito.mock(FlowStatus.class));
 fail("Expected PreConditions.validExists");
 } catch (PreConditions.PreConditionException e) {
 // Expected
 }
 }
}

class AnyLong {
 public static Long ANY = AdditionalMatchers.anyLong();
}