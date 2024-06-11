import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.event.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
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
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

public class BaseRuntimeFlowableDelegateTest {

 @Mock
 private DelegateExecution execution;
 @Mock
 private FlowableAdaptor flowableAdaptor;
 @Mock
 private EventPublisher eventPublisher;
 @Mock
 private FlowInstanceRepository flowInstanceRepository;
 @Mock
 private RetryExecutor retryExecutor;
 private BaseRuntimeFlowableDelegate<String> delegate;
 private Callable<String> callable;
 private FlowTaskInstance flowTaskInstance;

 @Before
 public void setUp() {
 MockitoAnnotations.openMocks(this);
 callable = mock(Callable.class);
 flowTaskInstance = mock(FlowTaskInstance.class);
 delegate = spy(new TestRuntimeFlowableDelegate() {
 @Override
 protected Callable<String> initCallable(DelegateExecution execution) {
 return callable;
 }
 });
 delegate.flowableAdaptor = flowableAdaptor;
 delegate.eventPublisher = eventPublisher;
 delegate.flowInstanceRepository = flowInstanceRepository;
 delegate.retryExecutor = retryExecutor;
 }

 @Test
 public void testRun_initializationTaskFailed() throws Exception {
 doThrow(new ServiceTaskError(new RuntimeException("Initialization task failed")))
 .when(delegate).initTargetTaskInstanceId(execution);
 delegate.run(execution);
 verify(delegate).updateFlowInstanceStatus(FlowStatus.FAILED);
 }

 @Test
 public void testRun_serviceTaskExecutionFailed() throws Exception {
 doThrow(new RuntimeException("Service task execution failed"))
 .when(delegate).initTargetTaskInstanceId(execution);
 assertThrows(ServiceTaskError.class, () -> delegate.run(execution));
 verify(delegate).updateFlowInstanceStatus(FlowStatus.FAILED);
 }

 @Test
 public void testRun_executionSuccess() throws Exception {
 when(callable.call()).thenReturn("Result");
 delegate.run(execution);
 verify(delegate).updateFlowInstanceStatus(FlowStatus.COMPLETED);
 }

 @Test
 public void testGet_interruptedException() throws Exception {
 CountDownLatch latch = new CountDownLatch(1);
 Future<String> future = new BaseRuntimeFlowableDelegate<String>() {
 @Override
 protected Callable<String> initCallable(DelegateExecution execution) {
 return callable;
 }
 }.getWrappedFuture(future -> latch);
 doThrow(new InterruptedException()).when(future).get();
 assertThrows(InterruptedException.class, future::get);
 latch.await();
 }

 @Test
 public void testGet_executionException() throws Exception {
 CountDownLatch latch = new CountDownLatch(1);
 Future<String> future = new BaseRuntimeFlowableDelegate<String>() {
 @Override
 protected Callable<String> initCallable(DelegateExecution execution) {
 return callable;
 }
 }.getWrappedFuture(future -> latch);
 doThrow(new ExecutionException(new RuntimeException("Execution exception"))).when(future).get();
 assertThrows(ExecutionException.class, future::get);
 latch.await();
 }

 @Test
 public void testGet_timeoutException() throws Exception {
 CountDownLatch latch = new CountDownLatch(1);
 Future<String> future = new BaseRuntimeFlowableDelegate<String>() {
 @Override
 protected Callable<String> initCallable(DelegateExecution execution) {
 return callable;
 }
 }.getWrappedFuture(future -> latch);
 doThrow(new TimeoutException()).when(future).get(1, TimeUnit.SECONDS);
 assertThrows(TimeoutException.class, () -> future.get(1, TimeUnit.SECONDS));
 latch.await();
 }

 @Test
 public void testInitTargetTaskInstanceId_processDefinitionIdNotFound() {
 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
 .thenReturn(Optional.empty());
 PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "processDefinitionId", "123", () -> true);
 assertThrows(IllegalArgumentException.class, () -> delegate.initTargetTaskInstanceId(execution));
 }

 @Test
 public void testInitTargetTaskInstanceId_callableIsNull() {
 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
 .thenReturn(Optional.of(1L));
 when(callable).thenReturn(null);
 assertThrows(NullPointerException.class, () -> delegate.initTargetTaskInstanceId(execution));
 }

 @Test
 public void testBindToFlowTaskInstance_taskInstancesAreTheSame() {
 FlowTaskInstance taskInstance = mock(FlowTaskInstance.class);
 when(taskInstance.getId()).thenReturn(1L);
 when(flowTaskInstance.getId()).thenReturn(1L);
 delegate.bindToFlowTaskInstance(taskInstance);
 verify(taskInstance).bindServiceTask(delegate);
 }

 @Test
 public void testBindToFlowTaskInstance_taskInstancesAreNotTheSame() {
 FlowTaskInstance taskInstance = mock(FlowTaskInstance.class);
 when(taskInstance.getId()).thenReturn(2L);
 when(flowTaskInstance.getId()).thenReturn(1L);
 delegate.bindToFlowTaskInstance(taskInstance);
 verify(taskInstance, never()).bindServiceTask(delegate);
 }

 @Test
 public void testUpdateFlowInstanceStatus() {
 when(flowInstanceRepository.updateStatusById(1L, FlowStatus.COMPLETED)).thenReturn(1);
 delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
 verify(flowInstanceRepository).updateStatusById(1L, FlowStatus.COMPLETED);
 }

 static class TestRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {

 @Override
 protected Callable<String> initCallable(DelegateExecution execution) {
 return callable;
 }
 }
}