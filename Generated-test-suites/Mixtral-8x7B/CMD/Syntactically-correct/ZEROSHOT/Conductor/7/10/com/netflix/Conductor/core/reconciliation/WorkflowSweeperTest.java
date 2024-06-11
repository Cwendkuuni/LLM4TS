package com.netflix.conductor.core.reconciliation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskModel;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAO;
import java.time.Instant;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableAsync
@EnableScheduling
@Component
public class WorkflowSweeperTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowSweeperTest.class);

    private WorkflowSweeper workflowSweeper;
    private QueueDAO queueDAO;
    private WorkflowExecutor workflowExecutor;
    private ConductorProperties properties;
    private WorkflowRepairService workflowRepairService;

    @Before
    public void setUp() {
        queueDAO = mock(QueueDAO.class);
        workflowExecutor = mock(WorkflowExecutor.class);
        properties = mock(ConductorProperties.class);
        workflowRepairService = mock(WorkflowRepairService.class);

        workflowSweeper =
                new WorkflowSweeper(
                        workflowExecutor,
                        Optional.of(workflowRepairService),
                        properties,
                        queueDAO);

        // Spy on the WorkflowSweeper to test protected methods
        workflowSweeper = spy(workflowSweeper);
    }

    @Test
    public void testSweepAsync() {
        String workflowId = "test_workflow_id";

        // Verify that the sweep method is called
        doNothing().when(workflowSweeper).sweep(workflowId);

        @SuppressWarnings("unused")
        CompletableFuture<Void> future = workflowSweeper.sweepAsync(workflowId);

        verify(workflowSweeper).sweep(workflowId);
    }

    @Test
    public void testSweep() {
        String workflowId = "test_workflow_id";

        // Create a mocked WorkflowModel
        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowId(workflowId);
        workflow.setWorkflowDefinition(new WorkflowDef());

        // Create a mocked TaskModel
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId("test_task_id");
        taskModel.setStatus(TaskModel.Status.IN_PROGRESS);

        // Add the task to the workflow
        workflow.getTasks().add(taskModel);

        // Set the mocked WorkflowModel to be returned by decide() method
        doReturn(workflow).when(workflowExecutor).decide(workflowId);

        workflowSweeper.sweep(workflowId);

        // Verify that unack() method is called
        verify(workflowSweeper, times(1)).unack(workflow, anyLong());
    }

    @Test
    public void testUnack() {
        String workflowId = "test_workflow_id";

        // Create a mocked WorkflowModel
        WorkflowModel workflow = new WorkflowModel();
        workflow.setWorkflowId(workflowId);
        workflow.setWorkflowDefinition(new WorkflowDef());

        // Set the mocked WorkflowModel to be returned by decide() method
        doReturn(workflow).when(workflowExecutor).decide(workflowId);

        long postponeDurationSeconds = 45;
        workflowSweeper.unack(workflow, postponeDurationSeconds);

        // Verify that queueDAO.setUnackTimeout() method is called with the correct arguments
        verify(queueDAO)
                .setUnackTimeout(DECIDER_QUEUE, workflowId, postponeDurationSeconds * 1000);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long workflowOffsetTimeout = 45;

        long jitter = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

        // The jitter should be between -(1/3) and +(2/3) of the workflowOffsetTimeout
        long minJitter = -(workflowOffsetTimeout / 3);
        long maxJitter = (workflowOffsetTimeout * 2) / 3;

        assertTrue(jitter >= minJitter && jitter <= maxJitter);
    }

    @VisibleForTesting
    void setWorkflowExecutor(WorkflowExecutor workflowExecutor) {
        this.workflowExecutor = workflowExecutor;
    }

    @VisibleForTesting
    void setProperties(ConductorProperties properties) {
        this.properties = properties;
    }

    @VisibleForTesting
    void setQueueDAO(QueueDAO queueDAO) {
        this.queueDAO = queueDAO;
    }

    @VisibleForTesting
    void setWorkflowRepairService(WorkflowRepairService workflowRepairService) {
        this.workflowRepairService = workflowRepairService;
    }

    private static void assertTrue(boolean condition) {
        if (!condition) {
            throw new RuntimeException("Assertion failed");
        }
    }

    @SuppressWarnings("unused")
    private static class WorkflowRepairService {

        @VisibleForTesting
        void verifyAndRepairWorkflowTasks(String workflowId) {}
    }

    @SuppressWarnings("unused")
    private static class ConductorProperties {

        @VisibleForTesting
        String getAppId() {
            return "test_app_id";
        }

        @VisibleForTesting
        SchedulerConfiguration getSchedulerConfiguration() {
            return new SchedulerConfiguration();
        }

        @VisibleForTesting
        WorkflowConfiguration getWorkflowConfiguration() {
            return new WorkflowConfiguration();
        }

        @VisibleForTesting
        class SchedulerConfiguration {

            @VisibleForTesting
            String SWEEPER_EXECUTOR_NAME = "test_sweeper_executor_name";
        }

        @VisibleForTesting
        class WorkflowConfiguration {

            @VisibleForTesting
            Long WORKFLOW_OFFSET_TIMEOUT = 45L;
        }
    }

    @SuppressWarnings("unused")
    private static class QueueDAO {

        @VisibleForTesting
        void remove(String queue, String messageId) {}

        @VisibleForTesting
        void setUnackTimeout(
                String queue, String messageId, long timeoutInMilliseconds) {}
    }
}