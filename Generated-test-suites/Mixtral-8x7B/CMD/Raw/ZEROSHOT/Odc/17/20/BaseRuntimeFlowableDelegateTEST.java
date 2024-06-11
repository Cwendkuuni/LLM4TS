import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.event.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.flowable.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.flowable.FlowInstanceRepositoryImpl;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.impl.test.PluggableFlowableTestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class BaseRuntimeFlowableDelegateTest extends PluggableFlowableTestCase {

 @Mock
 private FlowableAdaptor flowableAdaptor;
 @Mock
 private FlowInstanceRepository flowInstanceRepository;
 @Mock
 private ApplicationEventPublisher eventPublisher;
 @Mock
 private DelegateExecution execution;
 @Mock
 private FlowTaskInstance flowTaskInstance;
 private BaseRuntimeFlowableDelegate<String> delegate;
 @Captor
 private ArgumentCaptor<TaskInstanceCreatedListener> taskInstanceCreatedListenerCaptor;
 @Captor
 private ArgumentCaptor<ActiveTaskStatisticsListener> activeTaskStatisticsListenerCaptor;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 delegate = new TestBaseRuntimeFlowableDelegate();
 delegate.setFlowableAdaptor(flowableAdaptor);
 delegate.setEventPublisher(eventPublisher);
 delegate.setFlowInstanceRepository(flowInstanceRepository);
 }

 @Test
 public void testRun() throws Exception {
 // Given
 DelegateExecution execution = Mockito.mock(DelegateExecution.class);
 Callable<String> callable = Mockito.mock(Callable.class);
 Mockito.when(delegate.initCallable(execution)).thenReturn(callable);

 // When
 delegate.run(execution);

 // Then
 Mockito.verify(execution).setVariable(Mockito.eq("returnObject"), Mockito.anyString());
 Mockito.verifyNoMoreInteractions(execution);
 }

 @Test
 public void testIsDone() throws Exception {
 Assert.assertFalse(delegate.isDone());
 delegate.run(execution);
 Assert.assertTrue(delegate.isDone());
 }

 @Test
 public void testGet() throws Exception {
 // Given
 DelegateExecution execution = Mockito.mock(DelegateExecution.class);
 Callable<String> callable = Mockito.mock(Callable.class);
 Mockito.when(delegate.initCallable(execution)).thenReturn(callable);
 Mockito.when(callable.call()).thenReturn("test");

 // When
 String result = delegate.get();

 // Then
 Assert.assertEquals("test", result);
 }

 @Test(expected = ExecutionException.class)
 public void testGetException() throws Exception {
 // Given
 DelegateExecution execution = Mockito.mock(DelegateExecution.class);
 Callable<String> callable = Mockito.mock(Callable.class);
 Mockito.when(delegate.initCallable(execution)).thenReturn(callable);
 Mockito.when(callable.call()).thenThrow(new RuntimeException("test"));

 // When
 delegate.get();
 }

 @Test(expected = InterruptedException.class)
 public void testGetInterrupted() throws Exception {
 // Given
 DelegateExecution execution = Mockito.mock(DelegateExecution.class);
 Callable<String> callable = Mockito.mock(Callable.class);
 Mockito.when(delegate.initCallable(execution)).thenReturn(callable);
 Thread.currentThread().interrupt();

 // When
 delegate.get();
 }

 @Test
 public void testGetTimeout() throws Exception {
 // Given
 DelegateExecution execution = Mockito.mock(DelegateExecution.class);
 Callable<String> callable = Mockito.mock(Callable.class);
 Mockito.when(delegate.initCallable(execution)).thenReturn(callable);
 Mockito.when(callable.call()).thenReturn("test");

 // When
 Assert.assertEquals("test", delegate.get(1, TimeUnit.MILLISECONDS));
 }

 @Test(expected = TimeoutException.class)
 public void testGetTimeoutException() throws Exception {
 // Given
 DelegateExecution execution = Mockito.mock(DelegateExecution.class);
 Callable<String> callable = Mockito.mock(Callable.class);
 Mockito.when(delegate.initCallable(execution)).thenReturn(callable);
 Mockito.when(callable.call()).thenReturn("test");

 // When
 delegate.get(2, TimeUnit.MILLISECONDS);
 }

 @Test
 public void testInitTargetTaskInstanceId() {
 // Given
 Mockito.when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(Mockito.anyString()))
 .thenReturn(Optional.of(1L));
 Mockito.when(flowableAdaptor.getTaskInstanceByActivityId(Mockito.anyString(), Mockito.anyLong()))
 .thenReturn(Optional.of(flowTaskInstance));

 // When
 delegate.initTargetTaskInstanceId(execution);

 // Then
 Mockito.verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId(Mockito.anyString());
 Mockito.verify(flowableAdaptor).getTaskInstanceByActivityId(Mockito.anyString(), Mockito.anyLong());
 Mockito.verifyNoMoreInteractions(flowableAdaptor);
 }

 @Test
 public void testBindToFlowTaskInstance() {
 // Given
 Mockito.when(execution.getCurrentActivityId()).thenReturn("test");
 Mockito.when(flowableAdaptor.getTaskInstanceByActivityId("test", 1L))
 .thenReturn(Optional.of(flowTaskInstance));

 // When
 delegate.bindToFlowTaskInstance(flowTaskInstance);

 // Then
 Mockito.verify(flowTaskInstance).bindServiceTask(delegate);
 Mockito.verifyNoMoreInteractions(flowTaskInstance);
 }

 @Test
 public void testUpdateFlowInstanceStatus() {
 // Given
 Long flowInstanceId = 1L;

 // When
 delegate.updateFlowInstanceStatus(FlowStatus.TEST);

 // Then
 Mockito.verify(flowInstanceRepository).updateStatusById(flowInstanceId, FlowStatus.TEST);
 Mockito.verifyNoMoreInteractions(flowInstanceRepository);
 }

 private class TestBaseRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {

 @Override
 protected Callable<String> initCallable(DelegateExecution execution) {
 return () -> "test";
 }

 @Override
 protected void preHandle(DelegateExecution execution) {
 execution.setVariable("returnObject", "test");
 }
 }

}