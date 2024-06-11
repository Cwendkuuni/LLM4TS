import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.concurrent.Callable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private DelegateExecution delegateExecution;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInitTargetTaskInstanceId() throws Exception {
        String processDefinitionId = "processId";
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId)).thenReturn(Optional.of(1L));
        String activityId = "activityId";
        Optional<FlowTaskInstance> optional = Optional.of(new FlowTaskInstance(1L, 1L, activityId, "taskType",
                new ExecutionStrategyConfig()));
        when(flowableAdaptor.getTaskInstanceByActivityId(activityId, 1L)).thenReturn(optional);

        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

        assertEquals(new Long(1), baseRuntimeFlowableDelegate.getTargetTaskInstanceId());
        assertEquals("activityId", baseRuntimeFlowableDelegate.getActivityId());
        assertEquals(FlowStatus.RUNNING, baseRuntimeFlowableDelegate.getStrategyConfig().getFlowStatus());
    }

    @Test
    public void testPreHandle() {
        baseRuntimeFlowableDelegate.preHandle(delegateExecution);

        verify(delegateExecution, times(1)).setVariable("serviceTaskId", baseRuntimeFlowableDelegate.getActivityId());
    }

    @Test
    public void testRun() throws Exception {
        String processDefinitionId = "processId";
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId))
                .thenReturn(Optional.of(1L));
        String activityId = "activityId";
        Optional<FlowTaskInstance> optional = Optional.of(new FlowTaskInstance(1L, 1L, activityId, "taskType",
                new ExecutionStrategyConfig()));
        when(flowableAdaptor.getTaskInstanceByActivityId(activityId, 1L)).thenReturn(optional);

        Callable<String> callable = Mockito.mock(Callable.class);
        when(callable.call()).thenReturn("testString");

        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);
        baseRuntimeFlowableDelegate.run(delegateExecution);

        verify(callable, times(1)).call();
        assertEquals("testString", baseRuntimeFlowableDelegate.getReturnObject());
    }

    @Test
    public void testRun_Exception() throws Exception {
        String processDefinitionId = "processId";
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId))
                .thenReturn(Optional.of(1L));
        String activityId = "activityId";
        Optional<FlowTaskInstance> optional = Optional.of(new FlowTaskInstance(1L, 1L, activityId, "taskType",
                new ExecutionStrategyConfig()));
        when(flowableAdaptor.getTaskInstanceByActivityId(activityId, 1L)).thenReturn(optional);

        Callable<String> callable = Mockito.mock(Callable.class);
        when(callable.call()).thenThrow(new RuntimeException("testException"));

        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);
        assertThrows(RuntimeException.class, () -> baseRuntimeFlowableDelegate.run(delegateExecution));
    }

    @Test
    public void testGet() throws Exception {
        String processDefinitionId = "processId";
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId))
                .thenReturn(Optional.of(1L));
        String activityId = "activityId";
        Optional<FlowTaskInstance> optional = Optional.of(new FlowTaskInstance(1L, 1L, activityId, "taskType",
                new ExecutionStrategyConfig()));
        when(flowableAdaptor.getTaskInstanceByActivityId(activityId, 1L)).thenReturn(optional);

        Callable<String> callable = Mockito.mock(Callable.class);
        when(callable.call()).thenReturn("testString");

        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);
        baseRuntimeFlowableDelegate.run(delegateExecution);

        String result = baseRuntimeFlowableDelegate.get();

        assertEquals("testString", result);
    }

    @Test
    public void testGet_Exception() throws Exception {
        String processDefinitionId = "processId";
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId))
                .thenReturn(Optional.of(1L));
        String activityId = "activityId";
        Optional<FlowTaskInstance> optional = Optional.of(new FlowTaskInstance(1L, 1L, activityId, "taskType",
                new ExecutionStrategyConfig()));
        when(flowableAdaptor.getTaskInstanceByActivityId(activityId, 1L)).thenReturn(optional);

        Callable<String> callable = Mockito.mock(Callable.class);
        when(callable.call()).thenThrow(new RuntimeException("testException"));

        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);
        assertThrows(ExecutionException.class, () -> baseRuntimeFlowableDelegate.get());
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        verify(flowInstanceRepository, times(1))
                .updateStatusById(anyLong(), ArgumentMatchers.eq(FlowStatus.COMPLETED));
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(1L, 1L, "activityId", "taskType",
                new ExecutionStrategyConfig());
        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);

        assertTrue(flowTaskInstance.isBound());
    }

    @Test
    public void testBindToFlowTaskInstance_NoMatch() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(2L, 1L, "activityId", "taskType",
                new ExecutionStrategyConfig());
        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);

        assertFalse(flowTaskInstance.isBound());
    }

    @Test
    public void testInitCallable() throws Exception {
        when(delegateExecution.getProcessDefinitionId()).thenReturn("processId");
        when(delegateExecution.getCurrentActivityId()).thenReturn("activityId");
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
                .thenReturn(Optional.of(new FlowTaskInstance(1L, 1L, "activityId", "taskType",
                        new ExecutionStrategyConfig())));

        Callable<String> callable = baseRuntimeFlowableDelegate.initCallable(delegateExecution);

        assertNotNull(callable);
    }

    @Test
    public void testInitCallable_NoTaskInstanceFound() throws Exception {
        when(delegateExecution.getProcessDefinitionId()).thenReturn("processId");
        when(delegateExecution.getCurrentActivityId()).thenReturn("activityId");
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(ServiceTaskError.class, () -> baseRuntimeFlowableDelegate.initCallable(delegateExecution));

        verify(delegateExecution, never()).setVariable("serviceTaskId", anyString());
    }
}