package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.test.PluggableFlowableTestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

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
import com.oceanbase.odc.service.flow.execution.ExecutionStrategyConfig;

public class BaseRuntimeFlowableDelegateTest extends PluggableFlowableTestCase implements ApplicationEventPublisherAware {

@Mock
private DelegateExecution delegateExecution;

@Mock
private FlowableAdaptor flowableAdaptor;

@Mock
private FlowInstanceRepository flowInstanceRepository;

@Mock
private RetryExecutor retryExecutor;

@Mock
private ApplicationEventPublisher applicationEventPublisher;

private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);
baseRuntimeFlowableDelegate = spy(new BaseRuntimeFlowableDelegateTestDelegate());
baseRuntimeFlowableDelegate.flowableAdaptor = flowableAdaptor;
baseRuntimeFlowableDelegate.retryExecutor = retryExecutor;
baseRuntimeFlowableDelegate.flowInstanceRepository = flowInstanceRepository;
baseRuntimeFlowableDelegate.eventPublisher = applicationEventPublisher;
doNothing().when(baseRuntimeFlowableDelegate).preHandle(delegateExecution);
}

@Test
public void testRun() throws Exception {
ArgumentCaptor<ServiceTaskStartedEvent> eventCaptor = ArgumentCaptor.forClass(ServiceTaskStartedEvent.class);

baseRuntimeFlowableDelegate.run(delegateExecution);

verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(delegateExecution.getProcessDefinitionId());
verify(flowableAdaptor, times(1))
.getTaskInstanceByActivityId(delegateExecution.getCurrentActivityId(), baseRuntimeFlowableDelegate.getFlowInstanceId());
verify(delegateExecution, times(1)).setVariable(eq("targetTaskInstanceId"), anyLong());
verify(delegateExecution, times(1)).setVariable(eq("taskType"), any());
verify(delegateExecution, times(1)).setVariable(eq("strategyConfig"), any());
verify(delegateExecution, times(1)).setVariable(eq("status"), eq(FlowStatus.RUNNING.name()));
verify(delegateExecution, times(1)).setVariable(eq("processInstanceId"), anyLong());
verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());
assertEquals(Thread.currentThread(), eventCaptor.getValue().getThread());
}

@Test
public void testIsDone() throws Exception {
assertFalse(baseRuntimeFlowableDelegate.isDone());
baseRuntimeFlowableDelegate.run(delegateExecution);
assertTrue(baseRuntimeFlowableDelegate.isDone());
}

@Test
public void testGet() throws Exception {
assertNull(baseRuntimeFlowableDelegate.get());
baseRuntimeFlowableDelegate.run(delegateExecution);
assertEquals("", baseRuntimeFlowableDelegate.get());
}

@Test(expected = InterruptedException.class)
public void testGetInterruptedException() throws Exception {
baseRuntimeFlowableDelegate.get(0, TimeUnit.MICROSECONDS);
}

@Test(expected = TimeoutException.class)
public void testGetTimeoutException() throws Exception {
Thread.sleep(100);
assertNull(baseRuntimeFlowableDelegate.get(100, TimeUnit.MILLISECONDS));
}

@Test
public void testInitTargetTaskInstanceId() throws Exception {
baseRuntimeFlowableDelegate.flowInstanceId = null;
baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);
verify(flowableAdaptor, times(1))
.getTaskInstanceByActivityId(delegateExecution.getCurrentActivityId(), baseRuntimeFlowableDelegate.getFlowInstanceId());
}

private static class BaseRuntimeFlowableDelegateTestDelegate extends BaseRuntimeFlowableDelegate<String> {

@Override
protected Callable<String> initCallable(DelegateExecution execution) {
return () -> "";
}

}

@Override
public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
this.applicationEventPublisher = applicationEventPublisher;
}
}