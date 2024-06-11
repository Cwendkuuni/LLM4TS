import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.TaskType;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class BaseRuntimeFlowableDelegateTest {

    private static final String PROCESS_DEFINITION_ID = "processDefinitionId";
    private static final String ACTIVITY_ID = "activityId";
    private static final Long FLOW_INSTANCE_ID = 1L;
    private static final Long TASK_INSTANCE_ID = 2L;
    private static final TaskType TASK_TYPE = TaskType.ASYNC;

    @Mock
    private DelegateExecution delegateExecution;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        baseRuntimeFlowableDelegate = spy(new TestRuntimeFlowableDelegate());

        doReturn(flowableAdaptor).when(baseRuntimeFlowableDelegate).getFlowableAdaptor();
        doReturn(eventPublisher).when(baseRuntimeFlowableDelegate).getEventPublisher();
        doReturn(flowInstanceRepository).when(baseRuntimeFlowableDelegate).getFlowInstanceRepository();
    }

    @Test
    public void testPreHandle() throws Exception {
        doNothing().when(baseRuntimeFlowableDelegate).preHandle(delegateExecution);

        baseRuntimeFlowableDelegate.preHandle(delegateExecution);

        verify(baseRuntimeFlowableDelegate).preHandle(delegateExecution);
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        Mockito.when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(PROCESS_DEFINITION_ID))
                .thenReturn(Optional.of(FLOW_INSTANCE_ID));
        Mockito.when(flowableAdaptor.getTaskInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID))
                .thenReturn(Optional.of(createTaskInstance()));

        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

        verify(flowableAdaptor, times(1))
                .getFlowInstanceIdByProcessDefinitionId(PROCESS_DEFINITION_ID);
        verify(flowableAdaptor, times(1))
                .getTaskInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID);

        assertEquals(FLOW_INSTANCE_ID, baseRuntimeFlowableDelegate.getFlowInstanceId());
        assertEquals(TASK_INSTANCE_ID, baseRuntimeFlowableDelegate.getTargetTaskInstanceId());
        assertEquals(TASK_TYPE, baseRuntimeFlowableDelegate.getTaskType());
    }

    @Test
    public void testInitTargetTaskInstanceId_getFlowInstanceIdByProcessDefinitionId_exception() {
        Mockito.when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(PROCESS_DEFINITION_ID))
                .thenReturn(Optional.empty());

        assertThrows(PreConditions.ServiceException.class,
                () -> baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution));

        verify(flowableAdaptor, times(1))
                .getFlowInstanceIdByProcessDefinitionId(PROCESS_DEFINITION_ID);
        verify(flowableAdaptor, never())
                .getTaskInstanceByActivityId(any(), anyLong());
    }

    @Test
    public void testInitTargetTaskInstanceId_getTaskInstanceByActivityId_exception() {
        Mockito.when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(PROCESS_DEFINITION_ID))
                .thenReturn(Optional.of(FLOW_INSTANCE_ID));
        Mockito.when(flowableAdaptor.getTaskInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID))
                .thenReturn(Optional.empty());

        assertThrows(PreConditions.ServiceException.class,
                () -> baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution));

        verify(flowableAdaptor, times(1))
                .getFlowInstanceIdByProcessDefinitionId(PROCESS_DEFINITION_ID);
        verify(flowableAdaptor, times(1))
                .getTaskInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID);
    }

    @Test
    public void testRun() throws Exception {
        Callable<String> callable = Mockito.mock(Callable.class);
        when(callable.call()).thenReturn("test-result");

        baseRuntimeFlowableDelegate.run(delegateExecution);

        verify(baseRuntimeFlowableDelegate).preHandle(delegateExecution);
        verify(callable).call();
    }

    @Test
    public void testRun_initException() throws Exception {
        doThrow(new Exception("test-init-exception")).when(baseRuntimeFlowableDelegate)
                .initTargetTaskInstanceId(delegateExecution);

        assertThrows(Exception.class, () -> baseRuntimeFlowableDelegate.run(delegateExecution));

        verify(baseRuntimeFlowableDelegate, never()).preHandle(delegateExecution);
        verify(baseRuntimeFlowableDelegate).initTargetTaskInstanceId(delegateExecution);
    }

    @Test
    public void testRun_callException() throws Exception {
        Callable<String> callable = Mockito.mock(Callable.class);
        doThrow(new Exception("test-call-exception")).when(callable).call();

        baseRuntimeFlowableDelegate.run(delegateExecution);

        verify(baseRuntimeFlowableDelegate).preHandle(delegateExecution);
        verify(callable).call();
    }

    @Test
    public void testExecute() throws Exception {
        baseRuntimeFlowableDelegate.execute(delegateExecution);

        verify(eventPublisher, times(1)).removeEventListener(any());
        verify(eventPublisher, times(1)).removeEventListener(any());
    }

    @Test
    public void testIsDone() throws Exception {
        baseRuntimeFlowableDelegate.done = true;
        assertTrue(baseRuntimeFlowableDelegate.isDone());

        baseRuntimeFlowableDelegate.done = false;
        assertFalse(baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet() throws Exception {
        doReturn("test-result").when(baseRuntimeFlowableDelegate).get();

        String result = baseRuntimeFlowableDelegate.get();

        assertEquals("test-result", result);
    }

    @Test
    public void testGet_exception() throws Exception {
        doThrow(new ExecutionException("test-execution-exception")).when(baseRuntimeFlowableDelegate).get();

        assertThrows(ExecutionException.class, () -> baseRuntimeFlowableDelegate.get());
    }

    @Test
    public void testGet_timeout() throws Exception {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(baseRuntimeFlowableDelegate);

        assertThrows(TimeoutException.class, () -> future.get(1, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = createTaskInstance();
        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(taskInstance);

        verify(taskInstance, times(1)).bindServiceTask(baseRuntimeFlowableDelegate);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        when(flowInstanceRepository.updateStatusById(anyLong(), ArgumentMatchers.any()))
                .thenReturn(true);

        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(null);

        verify(flowInstanceRepository, times(1))
                .updateStatusById(anyLong(), ArgumentMatchers.any());
    }

    @Test
    public void testUpdateFlowInstanceStatus_exception() {
        doThrow(new RuntimeException("test-update-status-exception"))
                .when(flowInstanceRepository).updateStatusById(anyLong(), ArgumentMatchers.any());

        assertThrows(RuntimeException.class,
                () -> baseRuntimeFlowableDelegate.updateFlowInstanceStatus(null));

        verify(flowInstanceRepository, times(1))
                .updateStatusById(anyLong(), ArgumentMatchers.any());
    }

    private FlowTaskInstance createTaskInstance() {
        FlowTaskInstance flowTaskInstance = Mockito.mock(FlowTaskInstance.class);
        when(flowTaskInstance.getId()).thenReturn(TASK_INSTANCE_ID);
        when(flowTaskInstance.getTaskType()).thenReturn(TASK_TYPE);
        return flowTaskInstance;
    }

    private class TestRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {

        @Override
        protected Callable<String> initCallable(DelegateExecution execution) {
            return callable;
        }

        @Override
        protected FlowableAdaptor getFlowableAdaptor() {
            return flowableAdaptor;
        }

        @Override
        protected EventPublisher getEventPublisher() {
            return eventPublisher;
        }

        @Override
        protected FlowInstanceRepository getFlowInstanceRepository() {
            return flowInstanceRepository;
        }

        @Override
        public void preHandle(DelegateExecution execution) {
            // No-op
        }
    }

    private Callable<String> callable = Mockito.mock(Callable.class, AdditionalAnswers.returnsFirstArg());
}