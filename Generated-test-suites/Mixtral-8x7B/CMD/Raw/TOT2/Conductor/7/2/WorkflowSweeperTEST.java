import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.reconciliation.events.WorkflowRepairService;
import com.netflix.conductor.core.utils.TestingUtils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import com.netflix.conductor.model.WorkflowStatus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.AsyncResult;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WorkflowSweeperTest {

    private static final String WORKFLOW_ID = "workflow-1";
    private static final String APP_ID = "test-app";
    private static final String TASK_DEF_NAME = "test-task-def";
    private static final String TASK_DEF_VERSION = "1";
    private static final String WAIT_TASK_TYPE = TaskType.TASK_TYPE_WAIT.name();
    private static final String HUMAN_TASK_TYPE = TaskType.TASK_TYPE_HUMAN.name();
    private static final String WORKFLOW_NAME = "test-workflow";
    private static final int WORKFLOW_DEFINITION_TIMEOUT_SECONDS = 60;
    private static final int WORKFLOW_OFFSET_TIMEOUT_SECONDS = 45;
    private static final int WAIT_TIMEOUT = 30;
    private static final int RESPONSE_TIMEOUT_SECONDS = 20;
    private static final int POLL_TIMEOUT_SECONDS = 10;
    private static final int JITTER = 15;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    private WorkflowSweeper workflowSweeper;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        ConductorProperties properties = new ConductorProperties();
        properties.setAppId(APP_ID);
        properties.setWorkflowOffsetTimeout(Duration.ofSeconds(WORKFLOW_OFFSET_TIMEOUT_SECONDS));
        workflowSweeper =
                new WorkflowSweeper(
                        workflowExecutor,
                        Optional.of(workflowRepairService),
                        properties,
                        queueDAO);
        TestingUtils.setField(workflowSweeper, "deciderQueue", DECIDER_QUEUE);
    }

    @Test
    public void testSweepAsync() throws Exception {
        WorkflowModel workflowModel = createWorkflowModel();
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(Mono.just(workflowModel));

        CompletableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);

        StepVerifier.create(future)
                .expectComplete()
                .verify(Duration.ofSeconds(WORKFLOW_OFFSET_TIMEOUT_SECONDS + JITTER));

        ArgumentCaptor<String> workflowIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(queueDAO, times(1))
                .remove(DECIDER_QUEUE, workflowIdCaptor.capture());
        assertEquals(WORKFLOW_ID, workflowIdCaptor.getValue());
    }

    @Test
    public void testSweepAsyncNotFoundException() throws Exception {
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(Mono.error(new NotFoundException("")));

        CompletableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);

        StepVerifier.create(future)
                .expectComplete()
                .verify(Duration.ofSeconds(WORKFLOW_OFFSET_TIMEOUT_SECONDS + JITTER));

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweepAsyncException() throws Exception {
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(Mono.error(new RuntimeException("")));

        CompletableFuture<Void> future = workflowSweeper.sweepAsync(WORKFLOW_ID);

        StepVerifier.create(future)
                .expectComplete()
                .verify(Duration.ofSeconds(WORKFLOW_OFFSET_TIMEOUT_SECONDS + JITTER));

        verify(queueDAO, never()).remove(DECIDER_QUEUE, WORKFLOW_ID);
    }

    @Test
    public void testSweep() throws Exception {
        WorkflowModel workflowModel = createWorkflowModel();
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(Mono.just(workflowModel));

        workflowSweeper.sweep(WORKFLOW_ID);

        ArgumentCaptor<String> workflowIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(queueDAO, times(1))
                .remove(DECIDER_QUEUE, workflowIdCaptor.capture());
        assertEquals(WORKFLOW_ID, workflowIdCaptor.getValue());
    }

    @Test
    public void testSweepNotFoundException() throws Exception {
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(Mono.error(new NotFoundException("")));

        workflowSweeper.sweep(WORKFLOW_ID);

        ArgumentCaptor<String> workflowIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(queueDAO, times(1))
                .remove(DECIDER_QUEUE, workflowIdCaptor.capture());
        assertEquals(WORKFLOW_ID, workflowIdCaptor.getValue());
    }

    @Test
    public void testSweepException() throws Exception {
        when(workflowExecutor.decide(WORKFLOW_ID)).thenReturn(Mono.error(new RuntimeException("")));

        workflowSweeper.sweep(WORKFLOW_ID);

        ArgumentCaptor<String> workflowIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(queueDAO, never()).remove(DECIDER_QUEUE, workflowIdCaptor.capture());
    }

    @Test
    public void testUnackWithWorkflowModel() throws Exception {
        WorkflowModel workflowModel = createWorkflowModel();
        TaskModel inProgressTask = createTaskModel(TaskType.TASK_TYPE_IN_PROGRESS);
        TaskModel scheduledTask = createTaskModel(TaskType.TASK_TYPE_SCHEDULED);
        workflowModel.getTasks().add(inProgressTask);
        workflowModel.getTasks().add(scheduledTask);

        ArgumentCaptor<String> workflowIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> timeoutCaptor = ArgumentCaptor.forClass(Long.class);

        doAnswer(
                invocation -> {
                    String id = workflowIdCaptor.getValue();
                    Long timeout = timeoutCaptor.getValue();
                    queueDAO.setUnackTimeout(DECIDER_QUEUE, id, timeout * 1000);
                    return Mono.empty();
                })
                .when(queueDAO)
                .setUnackTimeout(DECIDER_QUEUE, workflowIdCaptor.capture(), timeoutCaptor.capture());

        workflowSweeper.unack(workflowModel, WORKFLOW_OFFSET_TIMEOUT_SECONDS);

        verify(queueDAO, times(1))
                .setUnackTimeout(DECIDER_QUEUE, workflowIdCaptor.capture(), timeoutCaptor.capture());
        assertEquals(WORKFLOW_ID, workflowIdCaptor.getValue());
        assertEquals(
                WAIT_TIMEOUT + 1,
                (long) timeoutCaptor.getValue(),
                "Unexpected unack timeout for in-progress task.");

        verify(queueDAO, times(1))
                .setUnackTimeout(
                        DECIDER_QUEUE, workflowIdCaptor.capture(), timeoutCaptor.capture());
        assertEquals(WORKFLOW_ID, workflowIdCaptor.getValue());
        assertEquals(
                POLL_TIMEOUT_SECONDS + 1,
                (long) timeoutCaptor.getValue(),
                "Unexpected unack timeout for scheduled task.");
    }

    @Test
    public void testUnackWithoutWorkflowModel() throws Exception {
        ArgumentCaptor<String> workflowIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> timeoutCaptor = ArgumentCaptor.forClass(Long.class);

        doAnswer(
                invocation -> {
                    String id = workflowIdCaptor.getValue();
                    Long timeout = timeoutCaptor.getValue();
                    queueDAO.setUnackTimeout(DECIDER_QUEUE, id, timeout * 1000);
                    return Mono.empty();
                })
                .when(queueDAO)
                .setUnackTimeout(DECIDER_QUEUE, workflowIdCaptor.capture(), timeoutCaptor.capture());

        workflowSweeper.unack(null, WORKFLOW_OFFSET_TIMEOUT_SECONDS);

        verify(queueDAO, times(1))
                .setUnackTimeout(DECIDER_QUEUE, workflowIdCaptor.capture(), timeoutCaptor.capture());
        assertEquals(WORKFLOW_ID, workflowIdCaptor.getValue());
        assertEquals(
                WORKFLOW_DEFINITION_TIMEOUT_SECONDS + 1,
                (long) timeoutCaptor.getValue(),
                "Unexpected unack timeout for workflow without model.");
    }

    @Test
    public void testWorkflowOffsetWithJitter() throws Exception {
        long jitteredValue = workflowSweeper.workflowOffsetWithJitter(WORKFLOW_OFFSET_TIMEOUT_SECONDS);
        assertTrue(
                jitteredValue >= WORKFLOW_OFFSET_TIMEOUT_SECONDS - JITTER
                        && jitteredValue <= WORKFLOW_OFFSET_TIMEOUT_SECONDS + JITTER);
    }

    private WorkflowModel createWorkflowModel() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setAppId(APP_ID);
        workflowModel.setStatus(WorkflowStatus.STARTED);
        workflowModel.setWorkflowId(WORKFLOW_ID);
        workflowModel.setWorkflowName(WORKFLOW_NAME);
        workflowModel.setTimeoutSeconds(WORKFLOW_DEFINITION_TIMEOUT_SECONDS);
        return workflowModel;
    }

    private TaskModel createTaskModel(TaskType taskType) {
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(Status.IN_PROGRESS);
        taskModel.setTaskType(taskType.name());
        taskModel.setTaskDefName(TASK_DEF_NAME);
        taskModel.setTaskDefVersion(TASK_DEF_VERSION);
        return taskModel;
    }
}