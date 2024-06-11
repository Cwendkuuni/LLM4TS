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
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstanceRepository;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {

    @InjectMocks
    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

    @Mock
    private DelegateExecution delegateExecution;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private RetryExecutor retryExecutor;

    @Mock
    private FlowTaskInstanceRepository flowTaskInstanceRepository;

    @Before
    public void setUp() {
        baseRuntimeFlowableDelegate = new TestRuntimeFlowableDelegate();
    }

    @Test
    public void testRun() throws Exception {
        baseRuntimeFlowableDelegate.run(delegateExecution);

        verify(delegateExecution, times(1)).getCurrentActivityId();
        verify(delegateExecution, times(1)).getProcessDefinitionId();
        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(retryExecutor, times(1)).run(any(Callable.class), any(Optional::isPresent));
        verify(flowInstanceRepository, times(1)).updateStatusById(anyLong(), any(FlowStatus.class));
    }

    @Test
    public void testIsDone() throws Exception {
        baseRuntimeFlowableDelegate.setDone(true);

        assertEquals(true, baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet() throws Exception {
        baseRuntimeFlowableDelegate.setReturnObject("test");

        assertEquals("test", baseRuntimeFlowableDelegate.get());
    }

    @Test
    public void testGetException() throws Exception {
        Exception exception = new Exception("test");
        baseRuntimeFlowableDelegate.setThrown(exception);

        assertThrows(ExecutionException.class, () -> baseRuntimeFlowableDelegate.get());
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
                .thenReturn(Optional.of(TestFlowTaskInstance.createInstance()));

        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

        verify(flowableAdaptor, times(1))
                .getFlowInstanceIdByProcessDefinitionId(anyString());
        verify(flowableAdaptor, times(1))
                .getTaskInstanceByActivityId(anyString(), anyLong());
        assertEquals(1L, baseRuntimeFlowableDelegate.getTargetTaskInstanceId());
        assertEquals(FlowTaskInstance.TaskType.TEST, baseRuntimeFlowableDelegate.getTaskType());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance flowTaskInstance = TestFlowTaskInstance.createInstance();
        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);

        verify(flowTaskInstance, times(1)).bindServiceTask(baseRuntimeFlowableDelegate);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        FlowStatus flowStatus = FlowStatus.TEST;
        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(flowStatus);

        verify(retryExecutor, times(1))
                .run(() -> flowInstanceRepository.updateStatusById(anyLong(), eq(flowStatus)), any());
    }

    @Test
    public void testInitCallable() throws Exception {
        when(delegateExecution.getVariable(anyString()))
                .thenReturn("test");

        Callable<String> callable = baseRuntimeFlowableDelegate.initCallable(delegateExecution);

        assertEquals("test", callable.call());
    }

    private static class TestRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {

        private boolean done;
        private String returnObject;
        private Exception thrown;

        @Override
        public Callable<String> initCallable(DelegateExecution delegateExecution) {
            return () -> (String) delegateExecution.getVariable("test");
        }

        @Override
        public void setDone(boolean done) {
            this.done = done;
        }

        @Override
        public void setReturnObject(String returnObject) {
            this.returnObject = returnObject;
        }

        @Override
        public void setThrown(Exception thrown) {
            this.thrown = thrown;
        }
    }

    private static class TestFlowTaskInstance implements FlowTaskInstance {

        public static FlowTaskInstance createInstance() {
            TestFlowTaskInstance testFlowTaskInstance = new TestFlowTaskInstance();
            testFlowTaskInstance.setId(1L);
            testFlowTaskInstance.setTaskType(TestFlowTaskInstance.TaskType.TEST);
            testFlowTaskInstance.setStrategyConfig(null);
            return testFlowTaskInstance;
        }

        private Long id;
        private TaskType taskType;
        private ExecutionStrategyConfig strategyConfig;

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public void setId(Long id) {
            this.id = id;
        }

        @Override
        public TaskType getTaskType() {
            return taskType;
        }

        @Override
        public void setTaskType(TaskType taskType) {
            this.taskType = taskType;
        }

        @Override
        public ExecutionStrategyConfig getStrategyConfig() {
            return strategyConfig;
        }

        @Override
        public void setStrategyConfig(ExecutionStrategyConfig strategyConfig) {
            this.strategyConfig = strategyConfig;
        }

        @Override
        public void dealloc() {
            // no-op
        }

        @Override
        public void bindServiceTask(BaseRuntimeFlowableDelegate baseRuntimeFlowableDelegate) {
            // no-op
        }

        private enum TaskType implements com.oceanbase.odc.core.shared.constant.TaskType {
            TEST
        }
    }
}