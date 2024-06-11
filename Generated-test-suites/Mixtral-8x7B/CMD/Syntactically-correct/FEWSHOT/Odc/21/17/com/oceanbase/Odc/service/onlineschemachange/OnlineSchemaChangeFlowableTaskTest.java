package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.Expression;
import org.flowable.engine.delegate.VariableScope;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.flow.util.FlowTaskUtil;
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.iam.model.User;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.subtask.OscTaskCompleteHandler;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.quartz.model.MisfireStrategy;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskService;
import com.oceanbase.odc.service.task.model.TaskResult;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private ScheduleTaskService scheduleTaskService;

    @Mock
    private QuartzJobService quartzJobService;

    @Mock
    private OnlineSchemaChangeTaskHandler taskHandler;

    @Mock
    private OscTaskCompleteHandler completeHandler;

    @Mock
    private OrganizationService organizationService;

    @Mock
    private Expression creatorIdExpression;

    @Mock
    private Expression organizationIdExpression;

    @Mock
    private Expression continueOnErrorExpression;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private VariableScope variableScope;

    @Mock
    private DelegateExecution delegateExecution;

    @Mock
    private TaskService taskService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setup() {
        onlineSchemaChangeFlowableTask = spy(onlineSchemaChangeFlowableTask);
        doReturn(1L).when(creatorIdExpression).getValue(delegateExecution);
        doReturn(2L).when(organizationIdExpression).getValue(delegateExecution);
        doReturn(true).when(continueOnErrorExpression).getValue(delegateExecution);
    }

    @Test
    public void testStart() throws Exception {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "schemaName";
        ScheduleEntity schedule = new ScheduleEntity();
        schedule.setId(1L);

        when(delegateExecution.getVariable(OnlineSchemaChangeFlowableTask.ONLINE_SCHEMA_CHANGE_PARAMETER)).thenReturn(parameters);
        when(delegateExecution.getVariable(ConnectionConfig.CONNECTION_CONFIG_NAME)).thenReturn(connectionConfig);
        when(delegateExecution.getVariable(FlowTaskUtil.SCHEMA_NAME)).thenReturn(schema);
        when(scheduleService.create(any(ScheduleEntity.class))).thenReturn(schedule);

        onlineSchemaChangeFlowableTask.start(1L, taskService, delegateExecution);

        verify(scheduleTaskRepository, times(1)).saveAndFlush(any(ScheduleTaskEntity.class));
        verify(taskHandler, times(1)).start(1L, 1L);
        verify(onlineSchemaChangeFlowableTask, times(1)).updateFlowInstanceStatus(FlowStatus.EXECUTION_SUCCEEDED);
    }

    @Test
    public void testIsSuccessful() {
        onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());

        onlineSchemaChangeFlowableTask.status = TaskStatus.FAILED;
        assertFalse(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;
        assertFalse(onlineSchemaChangeFlowableTask.isFailure());

        onlineSchemaChangeFlowableTask.status = TaskStatus.FAILED;
        assertTrue(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure() {
        onlineSchemaChangeFlowableTask.onFailure(1L, taskService);

        verify(taskService, times(1)).cancel(1L);
    }

    @Test
    public void testOnSuccessful() {
        onlineSchemaChangeFlowableTask.updateFlowInstanceStatus(FlowStatus.EXECUTION_SUCCEEDED);

        verify(taskService, times(1)).complete(1L, "{}", true);
    }

    @Test
    public void testOnTimeout() {
        onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);

        verify(taskService, times(1)).cancel(1L);
    }

    @Test
    public void testOnProgressUpdate() throws Exception {
        List<ScheduleTaskEntity> tasks = new ArrayList<>();
        ScheduleTaskEntity task1 = new ScheduleTaskEntity();
        task1.setId(1L);
        task1.setStatus(TaskStatus.RUNNING);
        task1.setProgressPercentage(50.0);
        tasks.add(task1);

        ScheduleTaskEntity task2 = new ScheduleTaskEntity();
        task2.setId(2L);
        task2.setStatus(TaskStatus.DONE);
        task2.setProgressPercentage(100.0);
        tasks.add(task2);

        Page<ScheduleTaskEntity> taskPage = new PageImpl<>(tasks);

        when(scheduleTaskService.listTask(Pageable.unpaged(), 1L)).thenReturn(taskPage);

        onlineSchemaChangeFlowableTask.percentage = 0.0;
        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

        ArgumentCaptor<TaskResult> arg = ArgumentCaptor.forClass(TaskResult.class);
        verify(taskService, times(1)).update(arg.capture());
        TaskResult taskResult = arg.getValue();
        assertEquals(100.0, taskResult.getProgress(), 0.001);
        assertEquals(FlowStatus.EXECUTION_SUCCEEDED, taskResult.getFlowStatus());

        onlineSchemaChangeFlowableTask.percentage = 50.0;
        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

        arg = ArgumentCaptor.forClass(TaskResult.class);
        verify(taskService, times(2)).update(arg.capture());
        taskResult = arg.getValue();
        assertEquals(75.0, taskResult.getProgress(), 0.001);
        assertEquals(FlowStatus.EXECUTION_SUCCEEDED, taskResult.getFlowStatus());
    }

    @Test
    public void testCancel() {
        when(scheduleTaskService.listTask(Pageable.unpaged(), 1L))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        assertTrue(onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService));
        verify(taskHandler, times(1)).terminate(1L, 1L);
        verify(taskService, times(1)).cancel(1L);
    }

    @Test
    public void testIsCancelled() {
        onlineSchemaChangeFlowableTask.status = TaskStatus.CANCELED;
        assertTrue(onlineSchemaChangeFlowableTask.isCancelled());

        onlineSchemaChangeFlowableTask.status = TaskStatus.FAILED;
        assertFalse(onlineSchemaChangeFlowableTask.isCancelled());
    }

    @Test
    public void testCreateScheduleEntity() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "schemaName";

        ScheduleEntity schedule = onlineSchemaChangeFlowableTask.createScheduleEntity(connectionConfig, parameters, schema);

        assertNotNull(schedule);
        assertEquals(connectionConfig.id(), schedule.getConnectionId());
        assertEquals(schema, schedule.getDatabaseName());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, schedule.getJobType());
        assertEquals(ScheduleStatus.ENABLED, schedule.getStatus());
        assertEquals(1L, schedule.getCreatorId());
        assertEquals(2L, schedule.getOrganizationId());
        assertEquals(1L, schedule.getProjectId());
        assertEquals(1L, schedule.getDatabaseId());
        assertEquals(TriggerStrategy.CRON, schedule.getTriggerConfigJson());
        assertEquals(checkTaskCronExpression,
                StringUtils.substringBetween(schedule.getTriggerConfigJson(), "{ \"cronExpression\": \""));
        assertEquals(MisfireStrategy.MISFIRE_INSTRUCTION_DO_NOTHING,
                StringUtils.substringBetween(schedule.getTriggerConfigJson(), ",\"misfireStrategy\": \""));
        assertEquals(JsonUtils.toJson(parameters),
                StringUtils.substringBetween(schedule.getJobParametersJson(), "{ \"parameters\": "));
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        OnlineSchemaChangeScheduleTaskParameters parameters = new OnlineSchemaChangeScheduleTaskParameters();

        ScheduleTaskEntity scheduleTask = onlineSchemaChangeFlowableTask.createScheduleTaskEntity(1L, parameters);

        assertNotNull(scheduleTask);
        assertEquals(1L, scheduleTask.getJobId());
        assertEquals(TaskStatus.PREPARING, scheduleTask.getStatus());
        assertEquals(1L, scheduleTask.getTaskId());
        assertEquals(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE.name(), scheduleTask.getJobGroup());
        assertEquals(0.0, scheduleTask.getProgressPercentage(), 0.001);
        assertNull(scheduleTask.getResultJson());
        assertNotNull(scheduleTask.getFireTime());

        String json = StringUtils.substringBetween(scheduleTask.getParametersJson(), "{ \"parameters\": ");
        assertEquals(JsonUtils.toJson(parameters), json + "}");
    }

    @Test
    public void testProgressStatusUpdate() {
        List<ScheduleTaskEntity> tasks = new ArrayList<>();
        tasks.add(new ScheduleTaskEntity());
        tasks.add(new ScheduleTaskEntity());
        tasks.add(new ScheduleTaskEntity());

        OnlineSchemaChangeFlowableTask.progressStatusUpdate(tasks);

        assertEquals(TaskStatus.DONE, tasks.get(0).getStatus());
        assertEquals(TaskStatus.DONE, tasks.get(1).getStatus());
        assertEquals(TaskStatus.DONE, tasks.get(2).getStatus());
    }

    @Test
    public void testSingleTaskPercentage() {
        ScheduleTaskEntity task1 = new ScheduleTaskEntity();
        task1.setStatus(TaskStatus.PREPARING);
        ScheduleTaskEntity task2 = new ScheduleTaskEntity();
        task2.setStatus(TaskStatus.RUNNING);
        task2.setProgressPercentage(50.0);
        ScheduleTaskEntity task3 = new ScheduleTaskEntity();
        task3.setStatus(TaskStatus.DONE);
        task3.setProgressPercentage(100.0);

        assertEquals(0.0, onlineSchemaChangeFlowableTask.singleTaskPercentage(task1), 0.001);
        assertEquals(0.5, onlineSchemaChangeFlowableTask.singleTaskPercentage(task2), 0.001);
        assertEquals(1.0, onlineSchemaChangeFlowableTask.singleTaskPercentage(task3), 0.001);
    }
}