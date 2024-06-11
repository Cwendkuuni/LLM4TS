import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.RetryExecutor;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BaseRuntimeFlowableDelegateTest {

 private BaseRuntimeFlowableDelegate<Object> delegate;
 private DelegateExecution execution;
 private FlowableAdaptor flowableAdaptor;
 private FlowInstanceRepository flowInstanceRepository;
 private EventPublisher eventPublisher;
 private RetryExecutor retryExecutor;
 private TaskInstanceCreatedListener taskInstanceCreatedlistener;
 private Callable<Object> callable;
 private FlowTaskInstance flowTaskInstance;
 private CountDownLatch latch;

 @Before
 public void setup() {
 flowableAdaptor = Mockito.mock(FlowableAdaptor.class);
 flowInstanceRepository = Mockito.mock(FlowInstanceRepository.class);
 eventPublisher = Mockito.mock(EventPublisher.class);
 retryExecutor = Mockito.mock(RetryExecutor.class);
 taskInstanceCreatedlistener = Mockito.mock(TaskInstanceCreatedListener.class);
 callable = Mockito.mock(Callable.class);
 latch = new CountDownLatch(1);

 execution = Mockito.mock(DelegateExecution.class);

 delegate = Mockito.spy(new BaseRuntimeFlowableDelegate() {
 @Override
 protected Callable<Object> initCallable(DelegateExecution execution) {
 return callable;
 }
 });

 delegate.flowableAdaptor = flowableAdaptor;
 delegate.flowInstanceRepository = flowInstanceRepository;
 delegate.eventPublisher = eventPublisher;
 delegate.retryExecutor = retryExecutor;
 delegate.taskInstanceCreatedlistener = taskInstanceCreatedlistener;
 }

 @Test
 public void testInitTargetTaskInstanceId() throws Exception {
 String processDefinitionId = "processDefinitionId";
 Optional<Optional<Long>> retryOptional = Optional.of(Optional.of(1L));

 Mockito.when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId)).thenReturn(retryOptional);

 delegate.initTargetTaskInstanceId(execution);

 assertEquals(1L, delegate.getFlowInstanceId());
 assertEquals(2L, delegate.getTargetTaskInstanceId());
 assertEquals(TaskType.SERVICE, delegate.getTaskType());
 }

 @Test
 public void testInitTargetTaskInstanceIdException() throws Exception {
 String processDefinitionId = "processDefinitionId";

 Mockito.when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId)).thenReturn(Optional.ofNullable(null));

 assertThrows(ServiceTaskError.class, () -> delegate.initTargetTaskInstanceId(execution));
 }

 @Test
 public void testRun() throws Exception {
 Object result = new Object();

 Mockito.when(callable.call()).thenReturn(result);
 Mockito.when(retryExecutor.run(Mockito.any(), Mockito.any())).thenReturn(true);

 delegate.run(execution);

 Mockito.verify(callable).call();
 assertEquals(result, delegate.getReturnObject());
 assertEquals(null, delegate.getThrown());
 }

 @Test
 public void testRunException() throws Exception {
 Exception ex = new Exception();

 Mockito.when(callable.call()).thenThrow(ex);

 assertThrows(Exception.class, () -> delegate.run(execution));
 }

 @Test
 public void testGet() throws Exception {
 Object result = new Object();

 Mockito.when(callable.call()).thenReturn(result);
 delegate.run(execution);

 assertEquals(result, delegate.get());
 }

 @Test
 public void testGetTimeoutException() throws Exception {

 delegate.run(execution);

 assertThrows(TimeoutException.class, () -> delegate.get(1, TimeUnit.MILLISECONDS));
 }

}