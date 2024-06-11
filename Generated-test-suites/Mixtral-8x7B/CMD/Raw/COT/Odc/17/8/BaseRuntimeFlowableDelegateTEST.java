import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
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
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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

 @Mock
 private RetryExecutor retryExecutor;

 private BaseRuntimeFlowableDelegate<Object> baseRuntimeFlowableDelegate;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 baseRuntimeFlowableDelegate = Mockito.spy(new TestBaseRuntimeFlowableDelegate());
 }

 @Test
 public void testRunMethod() throws Exception {
 baseRuntimeFlowableDelegate.run(delegateExecution);
 Mockito.verify(delegateExecution, Mockito.times(1)).getCurrentActivityId();
 Mockito.verify(delegateExecution, Mockito.times(1)).getProcessDefinitionId();
 }

 @Test
 public void testIsDoneMethod() {
 Assert.assertFalse(baseRuntimeFlowableDelegate.isDone());
 }

 @Test
 public void testGetMethodWithNoInterruptedException() throws ExecutionException, InterruptedException {
 CountDownLatch latch = new CountDownLatch(1);
 baseRuntimeFlowableDelegate.latch = latch;
 latch.countDown();
 Assert.assertEquals("Test Value", baseRuntimeFlowableDelegate.get());
 }

 @Test(expected = ExecutionException.class)
 public void testGetMethodWithInterruptedException() throws ExecutionException, InterruptedException {
 CountDownLatch latch = new CountDownLatch(1);
 baseRuntimeFlowableDelegate.latch = latch;
 baseRuntimeFlowableDelegate.thrown = new InterruptedException();
 latch.countDown();
 baseRuntimeFlowableDelegate.get();
 }

 @Test(expected = TimeoutException.class)
 public void testGetMethodWithTimeoutException() throws ExecutionException, InterruptedException, TimeoutException {
 CountDownLatch latch = new CountDownLatch(1);
 baseRuntimeFlowableDelegate.latch = latch;
 baseRuntimeFlowableDelegate.done = false;
 latch.countDown();
 baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS);
 }

 @Test
 public void testPreHandleMethod() throws Exception {
 baseRuntimeFlowableDelegate.preHandle(delegateExecution);
 Mockito.verify(delegateExecution, Mockito.times(1)).getProcessDefinitionId();
 }

 @Test
 public void testInitTargetTaskInstanceIdMethod() throws Exception {
 Mockito.when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(ArgumentMatchers.anyString()))
 .thenReturn(Optional.of(Optional.of(1L)));
 Mockito.when(flowableAdaptor.getTaskInstanceByActivityId(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong()))
 .thenReturn(Optional.of(new FlowTaskInstance()));
 baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);
 Mockito.verify(flowableAdaptor, Mockito.times(1))
 .getFlowInstanceIdByProcessDefinitionId(ArgumentMatchers.anyString());
 Mockito.verify(flowableAdaptor, Mockito.times(1))
 .getTaskInstanceByActivityId(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong());
 }

 @Test
 public void testBindToFlowTaskInstanceMethod() throws Exception {
 FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
 baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);
 Mockito.verify(flowTaskInstance, Mockito.times(1)).bindServiceTask(baseRuntimeFlowableDelegate);
 }

 @Test
 public void testUpdateFlowInstanceStatusMethod() throws Exception {
 Mockito.when(flowInstanceRepository.updateStatusById(ArgumentMatchers.anyLong(), ArgumentMatchers.any(FlowStatus.class)))
 .thenReturn(true);
 baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.TEST);
 Mockito.verify(flowInstanceRepository, Mockito.times(1))
 .updateStatusById(ArgumentMatchers.anyLong(), ArgumentMatchers.any(FlowStatus.class));
 }

 private class TestBaseRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<Object> {

 @Override
 protected Callable<Object> initCallable(DelegateExecution execution) {
 return AdditionalAnswers.returns("Test Value", Callable.class);
 }
 }
}