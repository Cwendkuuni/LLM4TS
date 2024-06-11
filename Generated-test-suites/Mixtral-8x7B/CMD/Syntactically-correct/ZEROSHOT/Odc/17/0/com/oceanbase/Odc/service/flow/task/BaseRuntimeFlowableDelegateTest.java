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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import java.util.concurrent.Callable;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class BaseRuntimeFlowableDelegateTest {

@Mock
private DelegateExecution delegateExecution;

@Mock
private FlowableAdaptor flowableAdaptor;

@Mock
private FlowInstanceRepository flowInstanceRepository;

@Mock
private EventPublisher eventPublisher;

private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);
baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
@Override
protected Callable<String> initCallable(DelegateExecution execution) {
return () -> "Test Result";
}
};
baseRuntimeFlowableDelegate.setFlowableAdaptor(flowableAdaptor);
baseRuntimeFlowableDelegate.setEventPublisher(eventPublisher);
baseRuntimeFlowableDelegate.setFlowInstanceRepository(flowInstanceRepository);
}

@Test
public void testGet_NoInterruption() throws Exception {
String result = baseRuntimeFlowableDelegate.get();
assertEquals("Test Result", result);
}

@Test(expected = ExecutionException.class)
public void testGet_InterruptedException() throws Exception {
doThrow(new InterruptedException()).when(baseRuntimeFlowableDelegate).latch$await();
baseRuntimeFlowableDelegate.get();
}

@Test(expected = ExecutionException.class)
public void testGet_ExecutionException() throws Exception {
doThrow(new ExecutionException("Test Exception")).when(baseRuntimeFlowableDelegate).initCallable(any(DelegateExecution.class));
baseRuntimeFlowableDelegate.get();
}

@Test
public void testGet_TimeoutException() throws Exception {
doReturn(false).when(baseRuntimeFlowableDelegate).latch$await(anyLong(), any(TimeUnit.class));
baseRuntimeFlowableDelegate.get(1000L, TimeUnit.MILLISECONDS);
}

@Test
public void testIsDone() throws Exception {
assertEquals(false, baseRuntimeFlowableDelegate.isDone());
baseRuntimeFlowableDelegate.get();
assertEquals(true, baseRuntimeFlowableDelegate.isDone());
}

@Test
public void testPreHandle() throws Exception {
baseRuntimeFlowableDelegate.preHandle(delegateExecution);
verify(delegateExecution, never()).setVariable(anyString(), any());
}

@Test
public void testInitTargetTaskInstanceId()
throws Exception {
String processDefinitionId = "TestProcessDefinitionId";
FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
flowTaskInstance.setId(1L);
flowTaskInstance.setTaskType(TaskType.TEST_TASK_TYPE);
flowTaskInstance.setStrategyConfig("TestStrategyConfig");
doReturn(Optional.of(Optional.of(processDefinitionId)))
.doReturn(Optional.of(flowTaskInstance))
.when(flowableAdaptor)
.getFlowInstanceIdByProcessDefinitionId(processDefinitionId)
.getTaskInstanceByActivityId(anyString(), anyLong());
baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);
assertEquals(processDefinitionId, baseRuntimeFlowableDelegate.getFlowInstanceId());
assertEquals(1L, baseRuntimeFlowableDelegate.getTargetTaskInstanceId());
assertEquals(TaskType.TEST_TASK_TYPE, baseRuntimeFlowableDelegate.getTaskType());
assertEquals("TestStrategyConfig", baseRuntimeFlowableDelegate.getStrategyConfig().toString());
verify(flowTaskInstance, times(1)).dealloc();
}

@Test
public void testBindToFlowTaskInstance() {
FlowTaskInstance taskInstance = new FlowTaskInstance();
taskInstance.setId(1L);
baseRuntimeFlowableDelegate.bindToFlowTaskInstance(taskInstance);
verify(taskInstance, times(1)).bindServiceTask(baseRuntimeFlowableDelegate);
}

@Test
public void testUpdateFlowInstanceStatus() {
FlowStatus flowStatus = FlowStatus.TEST_FLOW_STATUS;
doReturn(true).when(flowInstanceRepository).updateStatusById(anyLong(), any(FlowStatus.class));
baseRuntimeFlowableDelegate.updateFlowInstanceStatus(flowStatus);
verify(flowInstanceRepository, times(1)).updateStatusById(anyLong(), any(FlowStatus.class));
}

@Test
public void testExecute() {
doNothing().when(baseRuntimeFlowableDelegate).execute(delegateExecution);
baseRuntimeFlowableDelegate.execute(delegateExecution);
verify(eventPublisher, times(1)).removeEventListener(any(TaskInstanceCreatedListener.class));
verify(eventPublisher, times(1)).removeEventListener(any(ActiveTaskStatisticsListener.class));
}

@Test
public void testInit() {
baseRuntimeFlowableDelegate.init(eventPublisher);
verify(eventPublisher, times(1)).addEventListener(any(ActiveTaskStatisticsListener.class));
verify(eventPublisher, times(1)).addEventListener(any(TaskInstanceCreatedListener.class));
}

@Test
public void testRun() throws Exception {
String activityId = "TestActivityId";
doReturn(activityId).when(delegateExecution).getCurrentActivityId();
FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
flowTaskInstance.setId(1L);
doReturn(Optional.of(flowTaskInstance)).when(flowableAdaptor).getTaskInstanceByActivityId(activityId, 1L);
doNothing().when(baseRuntimeFlowableDelegate).preHandle(delegateExecution);
Callable<String> callable = baseRuntimeFlowableDelegate.initCallable(delegateExecution);
assertNotNull(callable);
eventPublisher.publishEvent(new ServiceTaskStartedEvent(baseRuntimeFlowableDelegate, Thread.currentThread()));
String result = callable.call();
assertEquals("Test Result", result);
verify(delegateExecution, times(1)).setVariable(anyString(), any());
}

@Test
public void testRun_InitializationTaskFailed() throws Exception {
doThrow(new Exception("Test Exception")).when(baseRuntimeFlowableDelegate)
.initCallable(any(DelegateExecution.class));
baseRuntimeFlowableDelegate.run(delegateExecution);
verify(delegateExecution, never()).setVariable(anyString(), any());
}

@Test
public void testRun_ServiceTaskExecutionFailed() throws Exception {
Callable<String> callable = () -> { throw new RuntimeException("Test Exception"); };
doReturn(callable).when(baseRuntimeFlowableDelegate).initCallable(any(DelegateExecution.class));
baseRuntimeFlowableDelegate.run(delegateExecution);
verify(delegateExecution, never()).setVariable(anyString(), any());
}

}