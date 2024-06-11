import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BaseRuntimeFlowableDelegateTest {

 @Mock
 private FlowableAdaptor flowableAdaptor;
 @Mock
 private EventPublisher eventPublisher;
 @Mock
 private FlowInstanceRepository flowInstanceRepository;
 @Mock
 private DelegateExecution delegateExecution;
 private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
 @Override
 protected Callable<String> initCallable(DelegateExecution execution) {
 return () -> "test result";
 }
 };
 ReflectionTestUtils.setField(baseRuntimeFlowableDelegate, "flowableAdaptor", flowableAdaptor);
 ReflectionTestUtils.setField(baseRuntimeFlowableDelegate, "eventPublisher", eventPublisher);
 ReflectionTestUtils.setField(baseRuntimeFlowableDelegate, "flowInstanceRepository", flowInstanceRepository);
 }

 @Test
 public void testRun_InitializationFails() throws Exception {
 when(delegateExecution.getCurrentActivityId()).thenReturn("testActivityId");
 doAnswer(AdditionalAnswers.rewrite(() -> {
 throw new ServiceTaskError(new RuntimeException("test exception"));
 })).when(baseRuntimeFlowableDelegate).initTargetTaskInstanceId(delegateExecution);

 baseRuntimeFlowableDelegate.run(delegateExecution);

 verify(delegateExecution, times(1)).setVariable(ArgumentMatchers.eq("exception"),
 ArgumentMatchers.any(RuntimeException.class));
 verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
 }

 @Test
 public void testRun_ExecutionFails() throws Exception {
 when(delegateExecution.getCurrentActivityId()).thenReturn("testActivityId");
 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any())).thenReturn(1L);
 doAnswer(AdditionalAnswers.rewrite(() -> {
 throw new RuntimeException("test exception");
 })).when(baseRuntimeFlowableDelegate).initCallable(delegateExecution);

 baseRuntimeFlowableDelegate.run(delegateExecution);

 verify(delegateExecution, times(1)).setVariable(ArgumentMatchers.eq("exception"),
 ArgumentMatchers.any(RuntimeException.class));
 verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
 }

 @Test
 public void testRun_Success() throws Exception {
 when(delegateExecution.getCurrentActivityId()).thenReturn("testActivityId");
 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any())).thenReturn(1L);
 when(baseRuntimeFlowableDelegate.initCallable(delegateExecution)).thenReturn(() -> "test result");

 baseRuntimeFlowableDelegate.run(delegateExecution);

 verify(delegateExecution, never()).setVariable(ArgumentMatchers.anyString(), ArgumentMatchers.any());
 verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
 assertEquals("test result", baseRuntimeFlowableDelegate.get());
 }

 @Test
 public void testIsDone_AfterRun() throws Exception {
 when(delegateExecution.getCurrentActivityId()).thenReturn("testActivityId");
 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any())).thenReturn(1L);
 when(baseRuntimeFlowableDelegate.initCallable(delegateExecution)).thenReturn(() -> "test result");

 baseRuntimeFlowableDelegate.run(delegateExecution);

 verify(delegateExecution, never()).setVariable(ArgumentMatchers.anyString(), ArgumentMatchers.any());
 verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
 assertTrue(baseRuntimeFlowableDelegate.isDone());
 }

 @Test
 public void testGet_AfterRun() throws Exception {
 when(delegateExecution.getCurrentActivityId()).thenReturn("testActivityId");
 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any())).thenReturn(1L);
 when(baseRuntimeFlowableDelegate.initCallable(delegateExecution)).thenReturn(() -> "test result");

 baseRuntimeFlowableDelegate.run(delegateExecution);

 verify(delegateExecution, never()).setVariable(ArgumentMatchers.anyString(), ArgumentMatchers.any());
 verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
 assertEquals("test result", baseRuntimeFlowableDelegate.get());
 }

 @Test
 public void testGet_WithTimeout() throws Exception {
 when(delegateExecution.getCurrentActivityId()).thenReturn("testActivityId");
 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any())).thenReturn(1L);
 when(baseRuntimeFlowableDelegate.initCallable(delegateExecution)).thenReturn(() -> "test result");

 baseRuntimeFlowableDelegate.run(delegateExecution);

 verify(delegateExecution, never()).setVariable(ArgumentMatchers.anyString(), ArgumentMatchers.any());
 verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
 assertEquals("test result", baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS));
 }

 @Test
 public void testGet_WithInterruptedException() throws Exception {
 when(delegateExecution.getCurrentActivityId()).thenReturn("testActivityId");
 when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any())).thenReturn(1L);
 when(baseRuntimeFlowableDelegate.initCallable(delegateExecution)).thenReturn(() -> {
 throw new InterruptedException();
 });

 assertThrows(ExecutionException.class, () -> baseRuntimeFlowableDelegate.get());
 }

 @Test
 public void testUpdateFlowInstanceStatus() throws Exception {
 baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

 verify(flowInstanceRepository, times(1)).updateStatusById(1L, FlowStatus.COMPLETED);
 }

 @Test
 public void testBindToFlowTaskInstance() throws Exception {
 FlowTaskInstance flowTaskInstance = Mockito.mock(FlowTaskInstance.class);
 when(flowTaskInstance.getId()).thenReturn(1L);
 when(flowTaskInstance.getTaskType()).thenReturn(TaskType.TEST);
 when(flowTaskInstance.getStrategyConfig()).thenReturn(null);

 baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);

 verify(flowTaskInstance, times(1)).bindServiceTask(baseRuntimeFlowableDelegate);
 }
}