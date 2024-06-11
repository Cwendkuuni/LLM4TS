import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.runners.SpringApplicationRunner;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.events.EventService;
import com.netflix.conductor.core.execution.WorkflowExecutorImpl;
import com.netflix.conductor.core.utils.TestUtils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.dao.TaskDAO;
import com.netflix.conductor.dao.WorkflowDAO;
import com.netflix.conductor.metrics.Monitors;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = {WorkflowSweeper.class, SpringApplicationRunner.class})
@ActiveProfiles("test")
public class WorkflowSweeperTest {

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private WorkflowRepairService workflowRepairService;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private EventService eventService;

    @Mock
    private WorkflowDAO workflowDAO;

    @Mock
    private TaskDAO taskDAO;

    @Mock
    private Monitors monitors;

    @InjectMocks
    private WorkflowSweeper workflowSweeper;

    @Captor
    private ArgumentCaptor<WorkflowModel> workflowModelArgumentCaptor;

    private Executor executor;

    @Before
    public void setup() {
        executor = Executors.newSingleThreadExecutor();
        when(conductorProperties.getAppId()).thenReturn("test-app");
        when(conductorProperties.getWorkflowOffsetTimeout()).thenReturn(TestUtils.seconds(45));
        when(workflowExecutor.decide(anyString())).thenReturn(createWorkflowModel());
    }

    @After
    public void teardown() {
        executor.shutdownNow();
    }

    @Test
    public void testSweepAsync() throws ExecutionException, InterruptedException, TimeoutException {
        when(workflowExecutor.decide(anyString()))
                .thenThrow(new NotFoundException("workflow not found"))
                .thenReturn(createWorkflowModel());

        CompletableFuture<Void> future = workflowSweeper.sweepAsync("workflow-1");
        future.get(1, TimeUnit.SECONDS);

        verify(queueDAO, times(2)).remove(DECIDER_QUEUE, "workflow-1");
        verify(queueDAO, times(1)).setUnackTimeout(
                DECIDER_QUEUE, "workflow-1", 45 * 1000);
    }

    @Test
    public void testSweepInProgress() throws InterruptedException, TimeoutException {
        WorkflowModel workflowModel = createWorkflowModel();
        workflowModel.setStatus(WorkflowModel.Status.IN_PROGRESS);

        when(workflowExecutor.decide(anyString())).thenReturn(workflowModel);

        workflowSweeper.sweep("workflow-1");

        verify(queueDAO, never()).remove(DECIDER_QUEUE, "workflow-1");
        verify(queueDAO, never()).setUnackTimeout(anyString(), anyLong(), anyLong());
    }

    @Test
    public void testSweepTerminal() throws InterruptedException, TimeoutException {
        WorkflowModel workflowModel = createWorkflowModel();
        workflowModel.setStatus(WorkflowModel.Status.COMPLETED);

        when(workflowExecutor.decide(anyString())).thenReturn(workflowModel);

        workflowSweeper.sweep("workflow-1");

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, "workflow-1");
        verify(queueDAO, never()).setUnackTimeout(anyString(), anyLong(), anyLong());
    }

    @Test
    public void testSweepNotFound() throws InterruptedException, TimeoutException {
        when(workflowExecutor.decide(anyString()))
                .thenThrow(new NotFoundException("workflow not found"));

        workflowSweeper.sweep("workflow-1");

        verify(queueDAO, times(1)).remove(DECIDER_QUEUE, "workflow-1");
        verify(queueDAO, never()).setUnackTimeout(anyString(), anyLong(), anyLong());
    }

    @Test
    public void testUnack() {
        WorkflowModel workflowModel = createWorkflowModel();

        workflowSweeper.unack(workflowModel, 30);

        verify(queueDAO, times(1)).setUnackTimeout(
                DECIDER_QUEUE, "workflow-1", 30 * 1000);
    }

    @Test
    public void testWorkflowOffsetWithJitter() {
        long jitter = workflowSweeper.workflowOffsetWithJitter(30);

        assertTrue(jitter >= 20 && jitter <= 40);
    }

    private WorkflowModel createWorkflowModel() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setApp("test-app");
        workflowModel.setWorkflowId("workflow-1");
        workflowModel.setStatus(WorkflowModel.Status.STARTED);
        workflowModel.setName("test-workflow");
        workflowModel.setVersion(1);
        workflowModel.setTimeoutSeconds(60);
        workflowModel.setWorkflowDefinition(TestUtils.createWorkflowDefinition());
        return workflowModel;
    }
}