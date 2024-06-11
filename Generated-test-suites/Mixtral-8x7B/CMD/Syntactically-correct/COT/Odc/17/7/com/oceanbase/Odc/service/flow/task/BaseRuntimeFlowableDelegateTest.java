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
import com.oceanbase.odc.service.flow.event.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate baseRuntimeFlowableDelegate;

    @Mock
    private DelegateExecution delegateExecution;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private RetryExecutor retryExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        baseRuntimeFlowableDelegate = spy(new TestBaseRuntimeFlowableDelegate());
        baseRuntimeFlowableDelegate.flowableAdaptor = flowableAdaptor;
        baseRuntimeFlowableDelegate.eventPublisher = eventPublisher;
        baseRuntimeFlowableDelegate.flowInstanceRepository = flowInstanceRepository;
        baseRuntimeFlowableDelegate.retryExecutor = retryExecutor;
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        String processDefinitionId = "processDefinitionId";
        String activityId = "activityId";

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId)).thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId(activityId, 1L)).thenReturn(Optional.of(FlowTaskInstance.builder().id(1L).build()));

        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);

        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(activityId, 1L);

        assertEquals(1L, baseRuntimeFlowableDelegate.getTargetTaskInstanceId());
        assertEquals(ResourceType.ODC_FLOW_TASK_INSTANCE, baseRuntimeFlowableDelegate.getResourceType());
    }

    @Test
    public void testInitTargetTaskInstanceIdException() {
        String processDefinitionId = "processDefinitionId";
        String activityId = "activityId";

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(processDefinitionId)).thenReturn(Optional.empty());

        assertThrows(ServiceTaskError.class, () -> baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution));

        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(processDefinitionId);
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = FlowTaskInstance.builder().id(1L).build();
        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(taskInstance);

        verify(taskInstance, times(1)).bindServiceTask(baseRuntimeFlowableDelegate);
    }

    @Test
    public void testUpdateFlowInstanceStatus() throws ExecutionException {
        when(flowInstanceRepository.updateStatusById(1L, FlowStatus.COMPLETED)).thenReturn(true);

        baseRuntimeFlowableDelegate.returnObject = (T) "result";
        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        verify(flowInstanceRepository, times(1)).updateStatusById(1L, FlowStatus.COMPLETED);
    }

    @Test
    public void testUpdateFlowInstanceStatusException() throws ExecutionException {
        when(flowInstanceRepository.updateStatusById(1L, FlowStatus.COMPLETED)).thenReturn(false);

        assertThrows(ExecutionException.class, () -> baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMPLETED));

        verify(flowInstanceRepository, times(1)).updateStatusById(1L, FlowStatus.COMPLETED);
    }

    private class TestBaseRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {

        @Override
        protected Callable<String> initCallable(DelegateExecution execution) {
            return () -> "result";
        }

        @Override
        protected void preHandle(DelegateExecution execution) {
            // Do nothing
        }

        @Override
        public ResourceType getResourceType() {
            return ResourceType.ODC_FLOW_TASK_INSTANCE;
        }
    }
}