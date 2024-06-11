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
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
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
 public void setup() {
 MockitoAnnotations.openMocks(this);
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
 public void testGetters() {
 // Test for getter methods
 assert(baseRuntimeFlowableDelegate.getActivityId() == null);
 assert(baseRuntimeFlowableDelegate.getTargetTaskInstanceId() == null);
 assert(baseRuntimeFlowableDelegate.getTaskType() == null);
 assert(baseRuntimeFlowableDelegate.getFlowInstanceId() == null);
 assert(baseRuntimeFlowableDelegate.getStrategyConfig() == null);
 }

 @Test
 public void testInitTargetTaskInstanceId() {
 // Test for initialization of targetTaskInstanceId, taskType, strategyConfig and flowInstanceId
 Mockito.when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(ArgumentMatchers.anyString()))
 .thenReturn(Optional.of(Optional.of(1L)));
 Mockito.when(flowableAdaptor.getTaskInstanceByActivityId(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong()))
 .thenReturn(Optional.of(new FlowTaskInstance()));
 baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);
 Mockito.verify(flowableAdaptor, Mockito.times(1))
 .getFlowInstanceIdByProcessDefinitionId(delegateExecution.getProcessDefinitionId());
 Mockito.verify(flowableAdaptor, Mockito.times(1))
 .getTaskInstanceByActivityId(delegateExecution.getCurrentActivityId(), 1L);
 }

 @Test
 public void testPreHandle() throws Exception {
 // Test for preHandle method
 baseRuntimeFlowableDelegate.preHandle(delegateExecution);
 Mockito.verify(delegateExecution, Mockito.times(1)).setVariable(ArgumentMatchers.anyString(),
 AdditionalMatchers.notNullValue());
 }

 @Test
 public void testExecute() throws Exception {
 // Test for execute method
 baseRuntimeFlowableDelegate.execute(delegateExecution);
 Mockito.verify(eventPublisher, Mockito.times(1)).removeEventListener(baseRuntimeFlowableDelegate.taskInstanceCreatedlistener);
 Mockito.verify(eventPublisher, Mockito.times(1)).removeEventListener(baseRuntimeFlowableDelegate.activeTaskStatisticsListener);
 }

 @Test
 public void testIsDone() throws Exception {
 // Test for isDone method
 assert(baseRuntimeFlowableDelegate.isDone() == false);
 }

 @Test
 public void testGet() throws Exception {
 // Test for get method
 CountDownLatch latch = new CountDownLatch(1);
 baseRuntimeFlowableDelegate.setReturnObject("Test Value");
 baseRuntimeFlowableDelegate.setLatch(latch);
 assert(baseRuntimeFlowableDelegate.get().equals("Test Value"));
 }

 @Test
 public void testGetWithException() throws Exception {
 // Test for get method with exception
 CountDownLatch latch = new CountDownLatch(1);
 baseRuntimeFlowableDelegate.setThrown(new Exception("Test Exception"));
 baseRuntimeFlowableDelegate.setLatch(latch);
 Mockito.when(latch.await(1, TimeUnit.MILLISECONDS)).thenThrow(new InterruptedException());
 assert(baseRuntimeFlowableDelegate.get(1, TimeUnit.MILLISECONDS)
 .getClass().equals(ExecutionException.class));
 }

 @Test
 public void testUpdateFlowInstanceStatus() throws Exception {
 // Test for updateFlowInstanceStatus method
 Mockito.when(flowInstanceRepository.updateStatusById(ArgumentMatchers.anyLong(), ArgumentMatchers.any(FlowStatus.class)))
 .thenReturn(true);
 baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
 Mockito.verify(flowInstanceRepository, Mockito.times(1))
 .updateStatusById(ArgumentMatchers.anyLong(), ArgumentMatchers.any(FlowStatus.class));
 }

 @Test
 public void testInitCallable() throws Exception {
 // Test for initCallable method
 Callable<String> callable = baseRuntimeFlowableDelegate.initCallable(delegateExecution);
 assert(callable != null);
 }
}