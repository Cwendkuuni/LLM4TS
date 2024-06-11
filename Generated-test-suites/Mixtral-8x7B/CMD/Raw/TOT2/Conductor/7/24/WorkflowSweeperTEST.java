import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.tasks.TaskType;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.execution.WorkflowExecutor;
import com.netflix.conductor.core.metadata.tasks.TaskModel;
import com.netflix.conductor.core.metadata.tasks.WorkflowModel;
import com.netflix.conductor.core.reconciliation.WorkflowRepairService;
import com.netflix.conductor.core.utils.Utils;
import com.netflix.conductor.dao.QueueDAO;
import com.netflix.conductor.model.WorkflowMetric;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.scheduling.annotation.AsyncResult;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class WorkflowSweeperTest {

private static final String WORKFLOW_ID = "workflow-123";
private static final String WORKFLOW_NAME = "test-workflow";
private static final String TASK_DEF_NAME = "test-task-def";
private static final String TASK_TYPE = TaskType.TASK_TYPE_SCHEDULED.name();
private static final String DECIDER_QUEUE = Utils.DECIDER_QUEUE;
private static final String APP_ID = "test-app";
private static final String TASK_ID = "task-123";
private static final String WORKFLOW_DEFINITION = "{\"name\":\"" + WORKFLOW_NAME + "\", \"timeoutSeconds\": 60}";
private static final String WAIT_TASK_TYPE = TaskType.TASK_TYPE_WAIT.name();
private static final String HUMAN_TASK_TYPE = TaskType.TASK_TYPE_HUMAN.name();
private static final String IN_PROGRESS = TaskModel.Status.IN_PROGRESS.name();
private static final String SCHEDULED = TaskModel.Status.SCHEDULED.name();
private static final String ERROR = "error";
private static final String INFO = "info";

private WorkflowSweeper workflowSweeper;
private WorkflowExecutor workflowExecutor;
private QueueDAO queueDAO;
private WorkflowRepairService workflowRepairService;
private ConductorProperties properties;

@BeforeEach
public void setup() {
workflowExecutor = Mockito.mock(WorkflowExecutor.class);
queueDAO = Mockito.mock(QueueDAO.class);
workflowRepairService = Mockito.mock(WorkflowRepairService.class);
properties = Mockito.mock(ConductorProperties.class);

Mockito.when(properties.getAppId()).thenReturn(APP_ID);

workflowSweeper = new WorkflowSweeper(workflowExecutor, Optional.of(workflowRepairService), properties, queueDAO);
}

@Test
public void sweepAsyncTest() throws ExecutionException, InterruptedException {
CompletableFuture<Void> completableFuture = workflowSweeper.sweepAsync(WORKFLOW_ID);
completableFuture.get();

Mockito.verify(workflowExecutor, Mockito.times(1)).decide(WORKFLOW_ID);
}

@Test
public void sweepTest() {
Mockito.when(workflowExecutor.decide(WORKFLOW_ID))
.thenReturn(getTestWorkflowModel());

workflowSweeper.sweep(WORKFLOW_ID);

Mockito.verify(queueDAO, Mockito.times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
}

@Test
public void sweepNotFoundExceptionTest() {
Mockito.when(workflowExecutor.decide(WORKFLOW_ID))
.thenThrow(new NotFoundException("Workflow not found"));

workflowSweeper.sweep(WORKFLOW_ID);

Mockito.verify(queueDAO, Mockito.times(1)).remove(DECIDER_QUEUE, WORKFLOW_ID);
}

@Test
public void unackTest() {
long workflowOffsetTimeout = 45;
WorkflowModel workflowModel = getTestWorkflowModel();

workflowSweeper.unack(workflowModel, workflowOffsetTimeout);

Mockito.verify(queueDAO, Mockito.times(1))
.setUnackTimeout(DECIDER_QUEUE, WORKFLOW_ID, (workflowOffsetTimeout + 1) * 1000);
}

@Test
public void workflowOffsetWithJitterTest() {
long workflowOffsetTimeout = 45;
long result = workflowSweeper.workflowOffsetWithJitter(workflowOffsetTimeout);

Assertions.assertTrue(result >= (workflowOffsetTimeout - workflowOffsetTimeout / 3)
&& result <= (workflowOffsetTimeout + workflowOffsetTimeout / 3));
}

private WorkflowModel getTestWorkflowModel() {
return WorkflowModel.builder()
.appId(APP_ID)
.correlationId(WORKFLOW_ID)
.name(WORKFLOW_NAME)
.status(TaskModel.Status.IN_PROGRESS)
.tasks(getTestTasks())
.build();
}

private TaskModel getTestTaskModel(String taskId, String taskType, String status) {
return TaskModel.builder()
.id(taskId)
.name(taskId)
.taskType(TaskType.builder().name(taskType).build())
.status(Status.valueOf(status))
.build();
}

private TaskModel getTestWorkflowTaskModel(String taskId, String taskType, String status) {
return TaskModel.builder()
.id(taskId)
.name(taskId)
.taskType(TaskDef.builder().name(taskType).build())
.status(Status.valueOf(status))
.build();
}

private TaskModel getTestScheduledTaskModel(String taskId, String taskType) {
return TaskModel.builder()
.id(taskId)
.name(taskId)
.taskType(TaskDef.builder().name(taskType).build())
.status(Status.SCHEDULED)
.build();
}

private TaskModel getTestWaitTaskModel(String taskId, String taskType) {
return TaskModel.builder()
.id(taskId)
.name(taskId)
.taskType(TaskDef.builder().name(taskType).build())
.waitTimeout(60)
.status(Status.IN_PROGRESS)
.build();
}

private TaskModel getTestHumanTaskModel(String taskId, String taskType) {
return TaskModel.builder()
.id(taskId)
.name(taskId)
.taskType(TaskDef.builder().name(taskType).build())
.responseTimeoutSeconds(60)
.status(Status.IN_PROGRESS)
.build();
}

private TaskModel getTestTaskModelWithPollTimeoutSeconds(String taskId, String taskType, long pollTimeoutSeconds) {
return TaskModel.builder()
.id(taskId)
.name(taskId)
.taskType(TaskDef.builder().name(taskType).build())
.pollTimeoutSeconds(pollTimeoutSeconds)
.status(Status.SCHEDULED)
.build();
}

private TaskModel getTestTaskModelWithTimeoutSeconds(String taskId, String taskType, int timeoutSeconds) {
return TaskModel.builder()
.id(taskId)
.name(taskId)
.taskType(TaskDef.builder().name(taskType).build())
.timeoutSeconds(timeoutSeconds)
.status(Status.SCHEDULED)
.build();
}

private Optional<TaskDef> getTestTaskDef() {
return Optional.of(
TaskDef.builder()
.name(TASK_DEF_NAME)
.taskType(TASK_TYPE)
.build());
}

private Mono<WorkflowModel> getTestWorkflowModelMono(WorkflowModel workflowModel) {
return Mono.just(workflowModel);
}

private Mono<WorkflowMetric> getTestWorkflowMetricMono(String workflowId, String message) {
return Mono.just(
WorkflowMetric.builder()
.workflowId(workflowId)
.message(message)
.build());
}

private Mono<WorkflowModel> getTestWorkflowModelWithInProgressTasksMono() {
WorkflowModel workflowModel = WorkflowModel.builder()
.appId(APP_ID)
.correlationId(WORKFLOW_ID)
.name(WORKFLOW_NAME)
.status(TaskModel.Status.IN_PROGRESS)
.tasks(getTestTasksWithInProgress())
.build();

return Mono.just(workflowModel);
}

private Mono<WorkflowModel> getTestWorkflowModelWithScheduledTasksMono() {
WorkflowModel workflowModel = WorkflowModel.builder()
.appId(APP_ID)
.correlationId(WORKFLOW_ID)
.name(WORKFLOW_NAME)
.status(TaskModel.Status.IN_PROGRESS)
.tasks(getTestScheduledTasks())
.build();

return Mono.just(workflowModel);
}

private Mono<WorkflowModel> getTestWorkflowModelWithWaitTaskMono() {
WorkflowModel workflowModel = WorkflowModel.builder()
.appId(APP_ID)
.correlationId(WORKFLOW_ID)
.name(WORKFLOW_NAME)
.status(TaskModel.Status.IN_PROGRESS)
.tasks(getTestWaitTasks())
.build();

return Mono.just(workflowModel);
}

private Mono<WorkflowModel> getTestWorkflowModelWithHumanTaskMono() {
WorkflowModel workflowModel = WorkflowModel.builder()
.appId(APP_ID)
.correlationId(WORKFLOW_ID)
.name(WORKFLOW_NAME)
.status(TaskModel.Status.IN_PROGRESS)
.tasks(getTestHumanTasks())
.build();

return Mono.just(workflowModel);
}

private Mono<WorkflowModel> getTestWorkflowModelWithPollTimeoutSecondsTaskMono() {
WorkflowModel workflowModel = WorkflowModel.builder()
.appId(APP_ID)
.correlationId(WORKFLOW_ID)
.name(WORKFLOW_NAME)
.status(TaskModel.Status.SCHEDULED)
.tasks(getTestTasksWithPollTimeoutSeconds())
.build();

return Mono.just(workflowModel);
}

private Mono<WorkflowModel> getTestWorkflowModelWithTimeoutSecondsTaskMono() {
WorkflowModel workflowModel = WorkflowModel.builder()
.appId(APP_ID)
.correlationId(WORKFLOW_ID)
.name(WORKFLOW_NAME)
.status(TaskModel.Status.SCHEDULED)
.tasks(getTestTasksWithTimeoutSeconds())
.build();

return Mono.just(workflowModel);
}

private TaskModel.Status getRandomTaskStatus() {
return TaskModel.Status.values()[new Random().nextInt(TaskModel.Status.values().length)];
}

private String getRandomTaskType() {
return TASK_TYPES[new Random().nextInt(TASK_TYPES.length)];
}

private String[] TASK_TYPES = {TASK_TYPE, WAIT_TASK_TYPE, HUMAN_TASK_TYPE};

private TaskModel[] getTestTasks() {
return new TaskModel[]{
getTestTaskModel(TASK_ID, TASK_TYPE, IN_PROGRESS)
};
}

private TaskModel[] getTestTasksWithInProgress() {
return new TaskModel[]{
getTestTaskModel(TASK_ID, TASK_TYPE, IN_PROGRESS),
getTestWorkflowTaskModel(TASK_ID + "-2", getRandomTaskType(), getRandomTaskStatus().name())
};
}

private TaskModel[] getTestTasksWithScheduled() {
return new TaskModel[]{
getTestScheduledTaskModel(TASK_ID, TASK_TYPE),
getTestTaskModelWithTimeoutSeconds(TASK_ID + "-2", getRandomTaskType(), 60)
};
}

private TaskModel[] getTestTasksWithPollTimeoutSeconds() {
return new TaskModel[]{
getTestTaskModelWithPollTimeoutSeconds(TASK_ID, TASK_TYPE, 60),
getTestWorkflowTaskModel(TASK_ID + "-2", getRandomTaskType(), getRandomTaskStatus().name())
};
}

private TaskModel[] getTestTasksWithTimeoutSeconds() {
return new TaskModel[]{
getTestTaskModelWithTimeoutSeconds(TASK_ID, TASK_TYPE, 60),
getTestWorkflowTaskModel(TASK_ID + "-2", getRandomTaskType(), getRandomTaskStatus().name())
};
}

private TaskModel[] getTestScheduledTasks() {
return new TaskModel[]{
getTestScheduledTaskModel(TASK_ID, TASK_TYPE),
getTestScheduledTaskModel(TASK_ID + "-2", getRandomTaskType())
};
}

private TaskModel[] getTestWaitTasks() {
return new TaskModel[]{
getTestWaitTaskModel(TASK_ID, WAIT_TASK_TYPE)
};
}

private TaskModel[] getTestHumanTasks() {
return new TaskModel[]{
getTestHumanTaskModel(TASK_ID, HUMAN_TASK_TYPE)
};
}
}