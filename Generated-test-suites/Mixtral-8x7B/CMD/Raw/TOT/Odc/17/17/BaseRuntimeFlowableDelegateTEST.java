import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.test.PluggableFlowableTestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BaseRuntimeFlowableDelegateTest extends PluggableFlowableTestCase {

    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;
    private DelegateExecution delegateExecution;
    private FlowableAdaptor flowableAdaptor;
    private EventPublisher eventPublisher;
    private FlowInstanceRepository flowInstanceRepository;

    @Before
    public void setup() {
        delegateExecution = mock(DelegateExecution.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        eventPublisher = mock(EventPublisher.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);

        baseRuntimeFlowableDelegate = new TestBaseRuntimeFlowableDelegate() {
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
    public void testExecuteWithSuccess() throws Exception {
        when(delegateExecution.getProcessDefinitionId()).thenReturn("Test Process Definition");
        when(delegateExecution.getCurrentActivityId()).thenReturn("Test Activity");

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenReturn(Optional.of(1L));

        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
                .thenReturn(Optional.of(TestFlowTaskInstance.builder().build()));

        baseRuntimeFlowableDelegate.execute(delegateExecution);

        ArgumentCaptor<ServiceTaskStartedEvent> serviceTaskStartedEventArgumentCaptor =
                ArgumentCaptor.forClass(ServiceTaskStartedEvent.class);

        verify(eventPublisher, times(1)).publishEvent(serviceTaskStartedEventArgumentCaptor.capture());
        ServiceTaskStartedEvent serviceTaskStartedEvent = serviceTaskStartedEventArgumentCaptor.getValue();
        assertEquals(baseRuntimeFlowableDelegate, serviceTaskStartedEvent.getServiceTask());

        Future<String> future = baseRuntimeFlowableDelegate;
        String result = future.get();

        assertEquals("Test Result", result);
    }

    @Test
    public void testExecuteWithException() {
        when(delegateExecution.getProcessDefinitionId()).thenReturn("Test Process Definition");
        when(delegateExecution.getCurrentActivityId()).thenReturn("Test Activity");

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenThrow(new RuntimeException("Test Exception"));

        assertThrows(ExecutionException.class, () -> baseRuntimeFlowableDelegate.execute(delegateExecution));
    }

    private static class TestBaseRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {

        TestBaseRuntimeFlowableDelegate() {
            super();
        }

        @Override
        protected Callable<String> initCallable(DelegateExecution execution) {
            return () -> "Test Result";
        }
    }

    private static class TestFlowTaskInstance {

        public Long id;
        public TaskType taskType;
        public ExecutionStrategyConfig strategyConfig;

        public TestFlowTaskInstance(Long id, TaskType taskType, ExecutionStrategyConfig strategyConfig) {
            this.id = id;
            this.taskType = taskType;
            this.strategyConfig = strategyConfig;
        }

        public static TestFlowTaskInstance.TestFlowTaskInstanceBuilder builder() {
            return new TestFlowTaskInstance.TestFlowTaskInstanceBuilder();
        }

        public static class TestFlowTaskInstanceBuilder {

            private Long id;
            private TaskType taskType;
            private ExecutionStrategyConfig strategyConfig;

            public TestFlowTaskInstance.TestFlowTaskInstanceBuilder id(Long id) {
                this.id = id;
                return this;
            }

            public TestFlowTaskInstance.TestFlowTaskInstanceBuilder taskType(TaskType taskType) {
                this.taskType = taskType;
                return this;
            }

            public TestFlowTaskInstance.TestFlowTaskInstanceBuilder strategyConfig(ExecutionStrategyConfig strategyConfig) {
                this.strategyConfig = strategyConfig;
                return this;
            }

            public TestFlowTaskInstance build() {
                return new TestFlowTaskInstance(id, taskType, strategyConfig);
            }
        }
    }
}