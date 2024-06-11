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
import com.oceanbase.odc.service.flow.FlowServiceImpl;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
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
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
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

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);
baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
@Override
protected Callable<String> initCallable(DelegateExecution execution) {
return null;
}
};
baseRuntimeFlowableDelegate.flowableAdaptor = flowableAdaptor;
baseRuntimeFlowableDelegate.eventPublisher = eventPublisher;
baseRuntimeFlowableDelegate.flowInstanceRepository = flowInstanceRepository;
baseRuntimeFlowableDelegate.retryExecutor = retryExecutor;
}

@Test
public void testRun() throws Exception {
// Test basic functionality
baseRuntimeFlowableDelegate.run(delegateExecution);
verify(delegateExecution, times(1)).getCurrentActivityId();
verify(flowableAdaptor, times(1))
.getFlowInstanceIdByProcessDefinitionId(delegateExecution.getProcessDefinitionId());
}

@Test(expected = ServiceTaskError.class)
public void testRunException() throws Exception {
// Test exception handling
doThrow(new Exception("Test exception")).when(flowableAdaptor)
.getFlowInstanceIdByProcessDefinitionId(delegateExecution.getProcessDefinitionId());
baseRuntimeFlowableDelegate.run(delegateExecution);
}

@Test
public void testPreHandle() throws Exception {
// Test preHandle() method
baseRuntimeFlowableDelegate.preHandle(delegateExecution);
verify(delegateExecution, times(1)).getProcessDefinitionId();
}

@Test
public void testIsDone() {
// Test isDone() method
baseRuntimeFlowableDelegate.setDone(true);
assertTrue(baseRuntimeFlowableDelegate.isDone());
}

@Test
public void testGet() throws InterruptedException, ExecutionException {
// Test get() method
baseRuntimeFlowableDelegate.setReturnObject("Test return object");
baseRuntimeFlowableDelegate.setDone(true);
assertEquals("Test return object", baseRuntimeFlowableDelegate.get());
}

@Test(expected = ExecutionException.class)
public void testGetInterruptedException()
throws InterruptedException, ExecutionException, TimeoutException {
// Test get() method with InterruptedException
baseRuntimeFlowableDelegate.latch.await(1, TimeUnit.SECONDS);
baseRuntimeFlowableDelegate.setDone(true);
baseRuntimeFlowableDelegate.get(5, TimeUnit.SECONDS);
}

@Test(expected = TimeoutException.class)
public void testGetTimeoutException()
throws InterruptedException, ExecutionException, TimeoutException {
// Test get() method with TimeoutException
baseRuntimeFlowableDelegate.setDone(true);
baseRuntimeFlowableDelegate.get(1, TimeUnit.MILLISECONDS);
}

@Test
public void testInitTargetTaskInstanceId() {
// Test initTargetTaskInstanceId() method
when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any())).thenReturn(Optional.of(1L));
when(flowableAdaptor.getTaskInstanceByActivityId(any(), anyLong()))
.thenReturn(Optional.of(new FlowTaskInstance()));
baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);
verify(delegateExecution, times(1)).getCurrentActivityId();
}

@Test
public void testBindToFlowTaskInstance() {
// Test bindToFlowTaskInstance() method
FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);
verify(flowTaskInstance, times(1)).bindServiceTask(baseRuntimeFlowableDelegate);
}

@Test
public void testUpdateFlowInstanceStatus() {
// Test updateFlowInstanceStatus() method
baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
verify(flowInstanceRepository, times(1))
.updateStatusById(anyLong(), eq(FlowStatus.COMPLETED));
}

private class TestCallable implements Callable<String> {
@Override
public String call() throws Exception {
return "Test callable result";
}
}

}