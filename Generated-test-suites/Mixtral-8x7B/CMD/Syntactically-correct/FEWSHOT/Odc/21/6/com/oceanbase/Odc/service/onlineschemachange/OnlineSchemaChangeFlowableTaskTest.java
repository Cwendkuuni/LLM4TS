package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

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
import com.oceanbase.odc.service.iam.OrganizationService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.quartz.QuartzJobService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.schedule.model.ScheduleStatus;
import com.oceanbase.odc.service.schedule.model.TriggerConfig;
import com.oceanbase.odc.service.schedule.model.TriggerStrategy;
import com.oceanbase.odc.service.task.TaskService;

@SuppressWarnings("unchecked")
public class OnlineSchemaChangeFlowableTaskTest {

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
 private TaskService taskService;

 @Mock
 private DelegateExecution execution;

 @InjectMocks
 private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 ReflectionTestUtils.setField(onlineSchemaChangeFlowableTask, "percentage", 0.0);
 }

 @Test
 public void testStart() throws Exception {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";

 when(execution.getVariable(OnlineSchemaChangeFlowableTask.TASK_ID))
 .thenReturn(1L);
 when(execution.getVariable(OnlineSchemaChangeFlowableTask.FLOW_EXECUTION_UID))
 .thenReturn("test_uid");
 when(execution.getVariable(OnlineSchemaChangeFlowableTask.ONLINE_SCHEMA_CHANGE_PARAMETERS))
 .thenReturn(parameters);
 when(execution.getVariable(OnlineSchemaChangeFlowableTask.CONNECTION_CONFIG))
 .thenReturn(connectionConfig);
 when(execution.getVariable(OnlineSchemaChangeFlowableTask.SCHEMA))
 .thenReturn(schema);

 onlineSchemaChangeFlowableTask.start(1L, taskService, execution);

 verify(taskService, times(1)).start(1L);
 verify(organizationService, times(1)).getById(anyLong());
 verify(scheduleTaskRepository, times(1)).saveAndFlush(any(ScheduleTaskEntity.class));
 verify(quartzJobService, times(1)).createJob(anyString(), any(Class.class), anyMap());
 verify(taskHandler, times(1)).start(anyLong(), anyLong());
 }

 @Test
 public void testIsSuccessful() {
 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTaskSpy = spy(onlineSchemaChangeFlowableTask);
 ReflectionTestUtils.setField(onlineSchemaChangeFlowableTaskSpy, "status", TaskStatus.DONE);

 assertTrue(onlineSchemaChangeFlowableTaskSpy.isSuccessful());

 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTaskSpy2 = spy(onlineSchemaChangeFlowableTask);
 ReflectionTestUtils.setField(onlineSchemaChangeFlowableTaskSpy2, "status", TaskStatus.FAILED);

 assertFalse(onlineSchemaChangeFlowableTaskSpy2.isSuccessful());
 }

 @Test
 public void testIsFailure() {
 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTaskSpy = spy(onlineSchemaChangeFlowableTask);
 ReflectionTestUtils.setField(onlineSchemaChangeFlowableTaskSpy, "status", TaskStatus.FAILED);

 assertTrue(onlineSchemaChangeFlowableTaskSpy.isFailure());

 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTaskSpy2 = spy(onlineSchemaChangeFlowableTask);
 ReflectionTestUtils.setField(onlineSchemaChangeFlowableTaskSpy2, "status", TaskStatus.DONE);

 assertFalse(onlineSchemaChangeFlowableTaskSpy2.isFailure());
 }

 @Test
 public void testOnFailure() {
 onlineSchemaChangeFlowableTask.onFailure(1L, taskService);

 verify(taskService, times(1)).cancel(1L);
 }

 @Test
 public void testOnSuccessful() {
 onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);

 verify(taskService, times(1)).update(any(TaskEntity.class));
 }

 @Test
 public void testOnProgressUpdate() {
 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTaskSpy =
 spy(onlineSchemaChangeFlowableTask);

 Page<ScheduleTaskEntity> tasks = mock(Page.class);
 when(scheduleTaskService.listTask(Pageable.unpaged(), anyLong()))
 .thenReturn(tasks);

 onlineSchemaChangeFlowableTaskSpy.onProgressUpdate(1L, taskService);

 verify(tasks, times(1)).stream();
 }

 @Test
 public void testCancel() {
 OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTaskSpy = spy(onlineSchemaChangeFlowableTask);
 when(scheduleTaskService.listTask(Pageable.unpaged(), anyLong()))
 .thenReturn(mock(Page.class));

 onlineSchemaChangeFlowableTaskSpy.cancel(true, 1L, taskService);

 verify(taskService, times(1)).cancel(1L);
 }
}