import org.flowable.engine.CommonEngineConfiguration;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.Expression;
import org.flowable.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BaseRuntimeFlowableDelegateTest {

private StandaloneInMemProcessEngineConfiguration processEngineConfiguration;
private CommonEngineConfiguration engineConfiguration;
private FlowableAdaptor flowableAdaptor;
private EventPublisher eventPublisher;
private FlowInstanceRepository flowInstanceRepository;
private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

@Before
public void setUp() {
processEngineConfiguration = new StandaloneInMemProcessEngineConfiguration();
processEngineConfiguration.setDatabaseSchemaUpdate(StandaloneInMemProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
engineConfiguration = processEngineConfiguration.buildEngineConfiguration();
engineConfiguration.setActivityFontName("Arial");
engineConfiguration.setLabelFontName("Arial");
engineConfiguration.setAnnotationFontName("Arial");
engineConfiguration.setHistoryLevel(ProcessEngineConfiguration.HISTORY_FULL);
engineConfiguration.setJobExecutorActivate(false);

flowableAdaptor = mock(FlowableAdaptor.class);
eventPublisher = mock(EventPublisher.class);
flowInstanceRepository = mock(FlowInstanceRepository.class);

baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
@Override
protected Callable<String> initCallable(DelegateExecution execution) {
return () -> "Test Result";
}

@Override
protected void preHandle(DelegateExecution execution) {
// Do nothing
}

@Override
public void bindToFlowTaskInstance(@NonNull FlowTaskInstance taskInstance) {
super.bindToFlowTaskInstance(taskInstance);
}
};

baseRuntimeFlowableDelegate.setFlowableAdaptor(flowableAdaptor);
baseRuntimeFlowableDelegate.setEventPublisher(eventPublisher);
baseRuntimeFlowableDelegate.setFlowInstanceRepository(flowInstanceRepository);
}

@After
public void tearDown() {
processEngineConfiguration.close();
}

@Test
public void testPreHandleMethod() throws Exception {
DelegateExecution delegateExecution = mock(DelegateExecution.class);
when(delegateExecution.getProcessDefinitionId()).thenReturn("testProcessDefinitionId");
when(delegateExecution.getCurrentActivityId()).thenReturn("testActivityId");

Expression expression = mock(Expression.class);
when(expression.getValue(delegateExecution)).thenReturn("testTargetTaskInstanceId");
baseRuntimeFlowableDelegate.setTargetTaskInstanceIdExpression(expression);

baseRuntimeFlowableDelegate.preHandle(delegateExecution);

ArgumentCaptor<TaskInstanceCreatedListener> taskInstanceCreatedListenerArgumentCaptor = ArgumentCaptor.forClass(TaskInstanceCreatedListener.class);
verify(eventPublisher, times(1)).addEventListener(taskInstanceCreatedListenerArgumentCaptor.capture());
TaskInstanceCreatedListener taskInstanceCreatedListener = taskInstanceCreatedListenerArgumentCaptor.getValue();
assertEquals(baseRuntimeFlowableDelegate, taskInstanceCreatedListener.getBaseRuntimeFlowableDelegate());
assertEquals("testTargetTaskInstanceId", taskInstanceCreatedListener.getTargetTaskInstanceId());
assertEquals("testActivityId", taskInstanceCreatedListener.getActivityId());
assertEquals("testProcessDefinitionId", taskInstanceCreatedListener.getProcessDefinitionId());
}

@Test
public void testRunMethod() throws Exception {
DelegateExecution delegateExecution = mock(DelegateExecution.class);
when(delegateExecution.getProcessDefinitionId()).thenReturn("testProcessDefinitionId");
when(delegateExecution.getCurrentActivityId()).thenReturn("testActivityId");

Expression expression = mock(Expression.class);
when(expression.getValue(delegateExecution)).thenReturn("testTargetTaskInstanceId");
baseRuntimeFlowableDelegate.setTargetTaskInstanceIdExpression(expression);

Execution execution = mock(Execution.class);
when(execution.getId()).thenReturn("testExecutionId");
when(delegateExecution.getExecution()).thenReturn(execution);

ProcessInstance processInstance = mock(ProcessInstance.class);
when(processInstance.getId()).thenReturn("testProcessInstanceId");
when(execution.getProcessInstance()).thenReturn(processInstance);

Task task = mock(Task.class);
when(task.getId()).thenReturn("testTaskId");
when(task.getProcessInstanceId()).thenReturn("testProcessInstanceId");
when(task.getExecutionId()).thenReturn("testExecutionId");
when(task.getActivityId()).thenReturn("testActivityId");
when(flowableAdaptor.getTaskInstanceByActivityId(any(), anyLong())).thenReturn(Optional.of(task));

baseRuntimeFlowableDelegate.run(delegateExecution);

verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
verify(taskInstanceCreatedlistener, times(1)).onTaskInstanceCreated(any(FlowTaskInstance.class));
verify(activeTaskStatisticsListener, times(1)).onTaskInstanceActive(any(FlowTaskInstance.class));
}

@Test
public void testGetMethod() throws InterruptedException, ExecutionException {
Future<String> future = baseRuntimeFlowableDelegate.get();
assertEquals("Test Result", future.get());
}

@Test
public void testGetMethodWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
Future<String> future = baseRuntimeFlowableDelegate.get(100, TimeUnit.MILLISECONDS);
assertEquals("Test Result", future.get());
}

@Test
public void testIsDoneMethod() throws Exception {
DelegateExecution delegateExecution = mock(DelegateExecution.class);
when(delegateExecution.getProcessDefinitionId()).thenReturn("testProcessDefinitionId");
when(delegateExecution.getCurrentActivityId()).thenReturn("testActivityId");

Expression expression = mock(Expression.class);
when(expression.getValue(delegateExecution)).thenReturn("testTargetTaskInstanceId");
baseRuntimeFlowableDelegate.setTargetTaskInstanceIdExpression(expression);

baseRuntimeFlowableDelegate.run(delegateExecution);

Future<String> future = baseRuntimeFlowableDelegate.get();
assertTrue(future.isDone());
}

@Test
public void testInitTargetTaskInstanceIdMethod() throws Exception {
DelegateExecution delegateExecution = mock(DelegateExecution.class);
when(delegateExecution.getProcessDefinitionId()).thenReturn("testProcessDefinitionId");
when(delegateExecution.getCurrentActivityId()).thenReturn("testActivityId");

Expression expression = mock(Expression.class);
when(expression.getValue(delegateExecution)).thenReturn("testTargetTaskInstanceId");
baseRuntimeFlowableDelegate.setTargetTaskInstanceIdExpression(expression);

when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any())).thenReturn(Optional.of(1L));

baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(any());
verify(flowableAdaptor, never()).getTaskInstanceByActivityId(any(), anyLong());

assertEquals("testTargetTaskInstanceId", baseRuntimeFlowableDelegate.getTargetTaskInstanceId());
assertEquals("testActivityId", baseRuntimeFlowableDelegate.getActivityId());
assertEquals(FlowStatus.RUNNING, baseRuntimeFlowableDelegate.getFlowInstance().getStatus());
}

@Test
public void testBindToFlowTaskInstanceMethod() throws Exception {
FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);
when(flowTaskInstance.getId()).thenReturn("testTaskInstanceId");
when(flowTaskInstance.getTaskType()).thenReturn(TaskType.SERVICE);
when(flowTaskInstance.getStrategyConfig()).thenReturn(new ExecutionStrategyConfig());

baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);

verify(flowTaskInstance, times(1)).bindServiceTask(baseRuntimeFlowableDelegate);
}

@Test
public void testUpdateFlowInstanceStatusMethod() throws Exception {
baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

verify(flowInstanceRepository, times(1)).updateStatusById(baseRuntimeFlowableDelegate.getFlowInstanceId(), FlowStatus.COMPLETED);
}

@Test
public void testInitCallableMethod() throws Exception {
DelegateExecution delegateExecution = mock(DelegateExecution.class);
when(delegateExecution.getProcessDefinitionId()).thenReturn("testProcessDefinitionId");
when(delegateExecution.getCurrentActivityId()).thenReturn("testActivityId");

Expression expression = mock(Expression.class);
when(expression.getValue(delegateExecution)).thenReturn("testTargetTaskInstanceId");
baseRuntimeFlowableDelegate.setTargetTaskInstanceIdExpression(expression);

Callable<String> callable = baseRuntimeFlowableDelegate.initCallable(delegateExecution);

Map<String, Object> variables = new HashMap<>();
variables.put("testKey", "testValue");

callable.call();

verify(delegateExecution, times(1)).setVariables(variables);
}

@Test
public void testExecuteMethod() throws Exception {
DelegateExecution delegateExecution = mock(DelegateExecution.class);
when(delegateExecution.getProcessDefinitionId()).thenReturn("testProcessDefinitionId");
when(delegateExecution.getCurrentActivityId()).thenReturn("testActivityId");

Expression expression = mock(Expression.class);
when(expression.getValue(delegateExecution)).thenReturn("testTargetTaskInstanceId");
baseRuntimeFlowableDelegate.setTargetTaskInstanceIdExpression(expression);

baseRuntimeFlowableDelegate.execute(delegateExecution);

verify(eventPublisher, times(1)).removeEventListener(any(TaskInstanceCreatedListener.class));
verify(eventPublisher, times(1)).removeEventListener(any(ActiveTaskStatisticsListener.class));
}

@Test
public void testInitMethod() throws Exception {
baseRuntimeFlowableDelegate.init(eventPublisher);

verify(eventPublisher, times(1)).addEventListener(any(TaskInstanceCreatedListener.class));
verify(eventPublisher, times(1)).addEventListener(any(ActiveTaskStatisticsListener.class));
}
}