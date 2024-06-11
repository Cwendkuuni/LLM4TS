import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.concurrent.Callable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class BaseRuntimeFlowableDelegateTest {

 private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

 @Mock
 private DelegateExecution delegateExecution;

 @Mock
 private FlowableAdaptor flowableAdaptor;

 @Mock
 private EventPublisher eventPublisher;

 @Mock
 private FlowInstanceRepository flowInstanceRepository;

 @Before
 public void setup() {
 MockitoAnnotations.openMocks(this);

 baseRuntimeFlowableDelegate = new MyConcreteRuntimeFlowableDelegate() {
 @Override
 protected Callable<String> initCallable(DelegateExecution execution) {
 return () -> "Test Result";
 }
 };

 baseRuntimeFlowableDelegate.flowableAdaptor = flowableAdaptor;
 baseRuntimeFlowableDelegate.eventPublisher = eventPublisher;
 baseRuntimeFlowableDelegate.flowInstanceRepository = flowInstanceRepository;
 }

 @Test
 public void testGet_TypicalUseCase() throws Exception {
 String result = baseRuntimeFlowableDelegate.get();
 assertEquals("Test Result", result);
 }

 @Test
 public void testGet_InterruptedException() throws Exception {
 when(delegateExecution.getCurrentActivityId()).thenThrow(new InterruptedException("Test InterruptedException"));

 assertThrows(ExecutionException.class, () -> baseRuntimeFlowableDelegate.get());
 }

 @Test
 public void testGet_ExecutionException() throws Exception {
 when(delegateExecution.getCurrentActivityId()).thenThrow(new RuntimeException("Test ExecutionException"));

 assertThrows(ExecutionException.class, () -> baseRuntimeFlowableDelegate.get());
 }

 @Test
 public void testIsDone_True() throws Exception {
 baseRuntimeFlowableDelegate.run(delegateExecution);
 assertTrue(baseRuntimeFlowableDelegate.isDone());
 }

 @Test
 public void testIsDone_False() throws Exception {
 assertFalse(baseRuntimeFlowableDelegate.isDone());
 }

 @Test
 public void testInitTargetTaskInstanceId() {
 String processDefinitionId = "TestProcessDefinitionId";

 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId))
 .thenReturn(Optional.of(Optional.of(1L)));

 baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

 assertEquals(Long.valueOf(1), baseRuntimeFlowableDelegate.getFlowInstanceId());
 }

 @Test
 public void testInitTargetTaskInstanceId_Exception() {
 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
 .thenReturn(Optional.empty());

 assertThrows(ServiceTaskError.class, () -> baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution));
 }

 @Test
 public void testBindToFlowTaskInstance() {
 FlowTaskInstance flowTaskInstance = Mockito.mock(FlowTaskInstance.class);

 baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);

 Mockito.verify(flowTaskInstance, Mockito.times(1)).bindServiceTask(baseRuntimeFlowableDelegate);
 }

 @Test
 public void testUpdateFlowInstanceStatus() {
 when(flowInstanceRepository.updateStatusById(anyLong(), ArgumentMatchers.<FlowStatus>any()))
 .thenReturn(true);

 baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

 Mockito.verify(flowInstanceRepository, Mockito.times(1))
 .updateStatusById(baseRuntimeFlowableDelegate.getFlowInstanceId(), FlowStatus.COMPLETED);
 }

}

class MyConcreteRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {

 @Override
 protected Callable<String> initCallable(DelegateExecution execution) {
 return () -> "Test Result";
 }

}