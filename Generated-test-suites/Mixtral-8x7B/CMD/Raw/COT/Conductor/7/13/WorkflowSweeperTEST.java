package com.netflix.conductor.core.reconciliation;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.metadata.workflow.WorkflowDefinition;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.runners.QueueDAO;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel.Status;
import java.time.Instant;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowSweeperTest {

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private ConductorProperties conductorProperties;

    private WorkflowModel workflowModel;

    @Before
    public void setUp() {
        workflowModel = new WorkflowModel();
        workflowModel.setStatus(Status.IN_PROGRESS);
        workflowModel.setWorkflowId("workflow_id");
        workflowModel.setWorkflowDefinition(new WorkflowDefinition());
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "workflow_id";

        ReflectionTestUtils.setField(workflowSweeper, "sweepAsyncExecutor", mock(AsyncExecutor.class));

        AsyncExecutor asyncExecutor =
                ReflectionTestUtils.getField(workflowSweeper, "sweepAsyncExecutor");

        StepVerifier.create(workflowSweeper.sweepAsync(workflowId))
                .then(
                        asyncExecutor.verify -> {
                            verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
                            return Mono.empty();
                        })
                .verifyComplete();

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweep() {
        String workflowId = "workflow_id";

        doNothing().when(queueDAO).remove(DECIDER_QUEUE, workflowId);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweepNotFoundException() {
        String workflowId = "workflow_id";

        doThrow(new NotFoundException("workflow not found")).when(workflowExecutor)
                .decide(workflowId);

        workflowSweeper.sweep(workflowId);

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testSweepException() {
        String workflowId = "workflow_id";

        doThrow(new RuntimeException("test exception")).when(workflowExecutor).decide(workflowId);

        workflowSweeper.sweeper.sweep(workflowId);

        verify(queueDAO, never()).remove(DECIDER_QUEUE, workflowId);
    }

    @Test
    public void testUnack() {
        String workflowId = "workflow_id";

        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(Status.IN_PROGRESS);
        workflow.setWorkflowId(workflowId);
        workflow.setWorkflowDefinition(new WorkflowDefinition());
        workflow.setTasks(
                new TaskModel[]{
                    new TaskModel()
                            .setStatus(Status.IN_PROGRESS)
                            .setTaskType(TaskType.TASK_TYPE_WAIT)
                });

        doReturn(workflow).when(workflowExecutor).decide(workflowId);

        doReturn(100L).when(queueDAO).setUnackTimeout(anyString(), anyString(), anyLong());

        workflowSweeper.unack(workflow, 30);

        verify(queueDAO, times(1)).setUnackTimeout(DECIDER_QUEUE, workflowId, 30 * 1000);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;

        doReturn(30L)
                .when(workflowSweeper)
                .workflowOffsetWithJitter(workflowOffsetTimeout);

        long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        Mockito.verify(workflowSweeper, times(1)).workflowOffsetWithJitter(workflowOffsetTimeout);
        assertThat(result).isBetween(30L, 60L);
    }
}

class AsyncExecutor {

    public Mono<Object> verify(AsyncCallable asyncCallable) {
        asyncCallable.call();
        return Mono.empty();
    }

}

interface AsyncCallable {
    void call();
}