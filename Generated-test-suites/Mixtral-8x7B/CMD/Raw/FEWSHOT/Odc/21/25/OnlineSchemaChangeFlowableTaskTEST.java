package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import org.apache.commons.lang3.tuple.Triple;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.util.CollectionUtil;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
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
import com.oceanbase.odc.service.task.TaskStatusEnum;

@SuppressWarnings("unchecked")
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
    private DelegateExecution execution;

    @Mock
    private TaskService taskService;

    @Mock
    private User user;

    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private OnlineSchemaChangeParameters parameter;

    @Mock
    private OnlineSchemaChangeScheduleTaskParameters subTaskParameter;

    @Mock
    private ScheduleEntity scheduleEntity;

    @Mock
    private ScheduleTaskEntity scheduleTaskEntity;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        onlineSchemaChangeFlowableTask = spy(onlineSchemaChangeFlowableTask);
    }

    @Test
    public void testStart() throws JsonProcessingException {
        // Prepare mocks
        Long taskId = 1L;
        String uid = "testUid";
        String schema = "testSchema";
        OnlineSchemaChangeContextHolder.trace(1L, taskId, 1L);
        when(FlowTaskUtil.getCloudMainAccountId(execution)).thenReturn(uid);
        when(FlowTaskUtil.getOnlineSchemaChangeParameter(execution)).thenReturn(parameter);
        when(FlowTaskUtil.getConnectionConfig(execution)).thenReturn(connectionConfig);
        when(FlowTaskUtil.getSchemaName(execution)).thenReturn(schema);
        when(parameter.generateSubTaskParameters(connectionConfig, schema)).thenReturn(List.of(subTaskParameter));
        when(createScheduleEntity(connectionConfig, parameter, schema)).thenReturn(scheduleEntity);
        when(createScheduleTaskEntity(scheduleEntity.getId(), subTaskParameter)).thenReturn(scheduleTaskEntity);

        // Call method under test
        onlineSchemaChangeFlowableTask.start(taskId, taskService, execution);

        // Verify results
        verify(taskService).start(taskId);
        verify(onlineSchemaChangeFlowableTask).createScheduleEntity(connectionConfig, parameter, schema);
        verify(onlineSchemaChangeFlowableTask).createScheduleTaskEntity(scheduleEntity.getId(), subTaskParameter);
        verify(taskHandler).start(scheduleEntity.getId(), scheduleTaskEntity.getId());
    }

    @Test
    public void testIsSuccessful() {
        // Prepare mocks
        onlineSchemaChangeFlowableTask.status = TaskStatus.DONE;

        // Call method under test
        boolean result = onlineSchemaChangeFlowableTask.isSuccessful();

        // Verify results
        assertTrue(result);
    }

    @Test
    public void testIsFailure() {
        // Prepare mocks
        onlineSchemaChangeFlowableTask.status = TaskStatus.FAILED;

        // Call method under test
        boolean result = onlineSchemaChangeFlowableTask.isFailure();

        // Verify results
        assertTrue(result);
    }

    @Test
    public void testOnFailure() {
        // Prepare mocks
        Long taskId = 1L;

        // Call method under test
        onlineSchemaChangeFlowableTask.onFailure(taskId, taskService);

        // Verify results
        verify(taskService).cancel(taskId);
    }

    @Test
    public void testOnSuccessful() {
        // Prepare mocks
        Long taskId = 1L;

        // Call method under test
        onlineSchemaChangeFlowableTask.onSuccessful(taskId, taskService);

        // Verify results
        verify(onlineSchemaChangeFlowableTask).updateFlowInstanceStatus(FlowStatus.EXECUTION_SUCCEEDED);
    }

    @Test
    public void testOnProgressUpdate() {
        // Prepare mocks
        List<ScheduleTaskEntity> tasks = new ArrayList<>();
        tasks.add(scheduleTaskEntity);
        Page<ScheduleTaskEntity> page = new PageImpl<>(tasks, Pageable.unpaged(), 1);
        when(scheduleTaskService.listTask(Pageable.unpaged(), anyLong())).thenReturn(page);

        // Call method under test
        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);

        // Verify results
        verify(onlineSchemaChangeFlowableTask).singleTaskPercentage(scheduleTaskEntity);
    }

    @Test
    public void testSingleTaskPercentage() {
        // Prepare mocks
        when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.PREPARING);
        when(scheduleTaskEntity.getProgressPercentage()).thenReturn(50.0);

        // Call method under test
        double result = onlineSchemaChangeFlowableTask.singleTaskPercentage(scheduleTaskEntity);

        // Verify results
        assertEquals(0.0, result, 0.001);
    }

    @Test
    public void testCancel() {
        // Prepare mocks
        Long taskId = 1L;
        Optional<ScheduleTaskEntity> runningEntity = Optional.of(scheduleTaskEntity);

        doNothing().when(taskHandler).terminate(anyLong(), anyLong());
        when(scheduleTaskService.listTask(Pageable.unpaged(), anyLong())).thenReturn(runningEntity.isPresent()
                ? new PageImpl<>(List.of(scheduleTaskEntity), Pageable.unpaged(), 1) : new PageImpl<>(new ArrayList<>(),
                        Pageable.unpaged(), 0));

        // Call method under test
        onlineSchemaChangeFlowableTask.cancel(true, taskId, taskService);

        // Verify results
        verify(taskService).cancel(taskId);
        verify(taskHandler).terminate(anyLong(), anyLong());
    }

    @Test
    public void testIsCancelled() {
        // Prepare mocks
        onlineSchemaChangeFlowableTask.status = TaskStatus.CANCELED;

        // Call method under test
        boolean result = onlineSchemaChangeFlowableTask.isCancelled();

        // Verify results
        assertTrue(result);
    }

    @Test
    public void testCreateScheduleEntity() {
        // Prepare mocks
        String json = "{}";
        ObjectNode node = objectMapper.readValue(json, ObjectNode.class);
        when(parameter.toJson()).thenReturn(json);
        when(scheduleEntity.getJobParametersJson()).thenReturn(json);

        // Call method under test
        OnlineSchemaChangeFlowableTask spy = spy(onlineSchemaChangeFlowableTask);
        ScheduleEntity result = spy.createScheduleEntity(connectionConfig, parameter, "schema");

        // Verify results
        verify(spy).addPropertiesToScheduleEntity(result, parameter);
    }

    @Test
    public void testCreateScheduleEntityWithNullParameters() {
        // Prepare mocks
        OnlineSchemaChangeFlowableTask spy = spy(onlineSchemaChangeFlowableTask);

        // Call method under test
        ScheduleEntity result = spy.createScheduleEntity(connectionConfig, null, "schema");

        // Verify results
        verify(spy, times(0)).addPropertiesToScheduleEntity(any(ScheduleEntity.class), any(OnlineSchemaChangeParameters.class));
    }

    @Test
    public void testAddPropertiesToScheduleEntity() {
        // Prepare mocks
        String json = "{}";
        ObjectNode node = objectMapper.readValue(json, ObjectNode.class);
        node.put("key", "value");
        when(parameter.toJson()).thenReturn(json);

        // Call method under test
        ScheduleEntity result = new ScheduleEntity();
        onlineSchemaChangeFlowableTask.addPropertiesToScheduleEntity(result, parameter);

        // Verify results
        assertNotNull(result.getJobParametersJson());
        JsonNode resultNode = objectMapper.readTree(result.getJobParametersJson());
        assertEquals("value", resultNode.get("key").asText());
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        // Prepare mocks
        OnlineSchemaChangeFlowableTask spy = spy(onlineSchemaChangeFlowableTask);
        when(scheduleTaskRepository.saveAndFlush(any(ScheduleTaskEntity.class))).thenReturn(scheduleTaskEntity);

        // Call method under test
        ScheduleTaskEntity result = spy.createScheduleTaskEntity(1L, subTaskParameter);

        // Verify results
        verify(scheduleTaskRepository).saveAndFlush(result);
    }

    @Test
    public void testGetSubTaskParameters() {
        // Prepare mocks
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        List<Triple<String, String, String>> list = new ArrayList<>();
        list.add(Triple.of("key1", "value1", ""));
        list.add(Triple.of("key2", "", "value2"));
        list.add(Triple.of("key3", "value3", "value4"));
        params.setAdditionalProperties(list);

        // Call method under test
        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeFlowableTask
                .getSubTaskParameters(params);

        // Verify results
        assertEquals(3, result.size());
        OnlineSchemaChangeScheduleTaskParameters param1 = result.get(0);
        OnlineSchemaChangeScheduleTaskParameters param2 = result.get(1);
        OnlineSchemaChangeScheduleTaskParameters param3 = result.get(2);

        assertEquals("value1", param1.getValue("key1"));
        assertTrue(param1.getValue("key2").isEmpty());

        assertTrue(param2.getValue("key1").isEmpty());
        assertEquals("value2", param2.getValue("key3"));

        assertEquals("value3", param3.getValue("key1"));
        assertEquals("value4", param3.getValue("key2"));
    }

    @Test
    public void testGetSubTaskParametersWithNullParams() {
        // Call method under test
        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeFlowableTask
                .getSubTaskParameters(null);

        // Verify results
        assertEquals(0, result.size());
    }

    @Test
    public void testGetSubTaskParametersWithEmptyParams() {
        // Prepare mocks
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();

        // Call method under test
        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeFlowableTask
                .getSubTaskParameters(params);

        // Verify results
        assertEquals(0, result.size());
    }

    @Test
    public void testGetSubTaskParametersWithEmptyAdditionalProperties() {
        // Prepare mocks
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setAdditionalProperties(new ArrayList<>());

        // Call method under test
        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeFlowableTask
                .getSubTaskParameters(params);

        // Verify results
        assertEquals(0, result.size());
    }
}