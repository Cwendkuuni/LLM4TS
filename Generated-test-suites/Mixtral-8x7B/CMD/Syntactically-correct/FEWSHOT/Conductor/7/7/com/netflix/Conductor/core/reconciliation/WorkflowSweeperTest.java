import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.util.AsyncTestUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SettableListenableFuture;

import com.netflix.conductor.annotations.VisibleForTesting;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.events.queue.QueueDAO;
import com.netflix.conductor.core.model.Task;
import com.netflix.conductor.core.model.Workflow;
import com.netflix.conductor.core.model.WorkflowDefinition;
import com.netflix.conductor.core.model.workflow.WorkflowTask;
import com.netflix.conductor.core.utils.Utils;

@EnableAsync
public class WorkflowSweeperTest {

 @Mock
 private WorkflowExecutor workflowExecutor;

 @Mock
 private QueueDAO queueDAO;

 @Mock
 private ConductorProperties properties;

 @InjectMocks
 private WorkflowSweeper workflowSweeper;

 @Before
 public void init() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void sweepAsyncTest() {
 AsyncTestUtils.assertEventuallyAsync(
 () -> {
 workflowSweeper.sweepAsync("workflowId");
 return null;
 },
 (result) -> {
 return true;
 },
 5000L);
 }

 @Test
 public void sweepTest() {
 Workflow workflow = new Workflow();
 workflow.setStatus(Workflow.Status.COMPLETED);

 WorkflowContext workflowContext = new WorkflowContext("appId");
 WorkflowContext.set(workflowContext);

 when(properties.getAppId()).thenReturn("appId");
 when(workflowExecutor.decide(eq("workflowId"))).thenReturn(workflow);

 workflowSweeper.sweep("workflowId");

 verify(queueDAO, times(1)).remove(Utils.DECIDER_QUEUE, "workflowId");
 }

 @Test
 public void sweepTestWithException() {
 Workflow workflow = new Workflow();
 workflow.setStatus(Workflow.Status.COMPLETED);

 WorkflowContext workflowContext = new WorkflowContext("appId");
 WorkflowContext.set(workflowContext);

 when(properties.getAppId()).thenReturn("appId");
 when(workflowExecutor.decide(eq("workflowId"))).thenThrow(new RuntimeException("Test Exception"));

 workflowSweeper.sweep("workflowId");

 verify(queueDAO, times(1)).remove(Utils.DECIDER_QUEUE, "workflowId");
 }

 @Test
 public void unackTest() {
 Workflow workflow = new Workflow();
 workflow.setWorkflowId("workflowId");
 WorkflowDefinition workflowDefinition = new WorkflowDefinition();
 workflowDefinition.setTimeoutSeconds(60);
 workflow.setWorkflowDefinition(workflowDefinition);
 WorkflowTask workflowTask = new WorkflowTask();
 workflowTask.setStatus(WorkflowTask.Status.IN_PROGRESS);
 workflow.addTask(workflowTask);

 when(queueDAO.setUnackTimeout(eq(Utils.DECIDER_QUEUE), eq("workflowId"), anyLong()))
 .thenReturn(null);

 workflowSweeper.unack(workflow, 60);

 verify(queueDAO, times(1))
 .setUnackTimeout(eq(Utils.DECIDER_QUEUE), eq("workflowId"), eq(61000L));
 }

 @Test
 public void workflowOffsetWithJitterTest() {
 long result = workflowSweeper.workflowOffsetWithJitter(45);
 Assert.assertTrue(result >= 30 && result <= 60);
 }
}