package com.netflix.conductor.core.execution;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/*
 * Copyright 2022 Netflix, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

public class AsyncSystemTaskExecutorTest {

    @Mock
    private ExecutionDAOFacade executionDAOFacade;

    @Mock
    private QueueDAO queueDAO;

    @Mock
    private MetadataDAO metadataDAO;

    @Mock
    private WorkflowExecutor workflowExecutor;

    @Mock
    private ConductorProperties conductorProperties;

    @InjectMocks
    private AsyncSystemTaskExecutor asyncSystemTaskExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(asyncSystemTaskExecutor, "systemTaskCallbackTime", 1000L);
        ReflectionTestUtils.setField(asyncSystemTaskExecutor, "queueTaskMessagePostponeSecs", 1000L);
        when(conductorProperties.getSystemTaskWorkerCallbackDuration().getSeconds())
                .thenReturn(1000L);
        when(conductorProperties.getTaskExecutionPostponeDuration().getSeconds())
                .thenReturn(1000L);
    }

    @Test
    public void testExecute_NullTaskAndTaskId() {
        asyncSystemTaskExecutor.execute(null, null);

        verify(executionDAOFacade, never()).getTaskModel(anyString());
        verify(queueDAO, never()).remove(anyString(), anyString());
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testExecute_TaskNotFound() {
        String taskId = "123";
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade).getTaskModel(taskId);
        verify(queueDAO, never()).remove(anyString(), anyString());
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(systemTask).isAsyncComplete(any(TaskModel.class));
        verify(systemTask, never()).start(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(systemTask, never()).execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
    }

    @Test
    public void testExecute_TaskIsTerminal() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.FAILED);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade).getTaskModel(taskId);
        verify(queueDAO).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_TaskStatusIsScheduledAndLimitsExceeded() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskDefName("testTaskDef");
        task.setRateLimitPerFrequency(1);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(true);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(new TaskModel());

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade).exceedsInProgressLimit(task);
        verify(executionDAOFacade).exceedsRateLimitPerFrequency(
                task, metadataDAO.getTaskDef(task.getTaskDefName()));
        verify(queueDAO).postpone(anyString(), anyString(), anyInt(), anyLong());
    }

    @Test
    public void testExecute_TaskStatusIsScheduledAndLimitsNotExceeded() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.SCHEDULED);
        task.setTaskDefName("testTaskDef");
        task.setRateLimitPerFrequency(1);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.exceedsInProgressLimit(task)).thenReturn(false);
        when(executionDAOFacade.exceedsRateLimitPerFrequency(
                task, metadataDAO.getTaskDef(task.getTaskDefName())))
                .thenReturn(false);
        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);
        when(metadataDAO.getTaskDef(task.getTaskDefName())).thenReturn(new TaskModel());

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(executionDAOFacade).exceedsInProgressLimit(task);
        verify(executionDAOFacade).exceedsRateLimitPerFrequency(
                task, metadataDAO.getTaskDef(task.getTaskDefName()));
        verify(queueDAO, never()).postpone(anyString(), anyString(), anyInt(), anyLong());
        verify(systemTask).isAsyncComplete(any(TaskModel.class));
        verify(systemTask).start(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
    }

    @Test
    public void testExecute_TaskStatusIsInProgress() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.IN_PROGRESS);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(systemTask).isAsyncComplete(any(TaskModel.class));
        verify(systemTask).execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
    }

    @Test
    public void testExecute_TaskStatusIsTerminalAndWorkflowIsNotTerminal() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.FAILED);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.WorkflowStatus.RUNNING);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getWorkflowModel(workflow.getWorkflowId(), true)).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(systemTask).isAsyncComplete(any(TaskModel.class));
        verify(systemTask, never()).start(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(systemTask, never()).execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(executionDAOFacade).getWorkflowModel(workflow.getWorkflowId(), true);
        verify(queueDAO).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_TaskStatusIsTerminalAndWorkflowIsTerminal() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.FAILED);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.WorkflowStatus.COMPLETED);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getWorkflowModel(workflow.getWorkflowId(), true)).thenReturn(workflow);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(systemTask).isAsyncComplete(any(TaskModel.class));
        verify(systemTask, never()).start(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(systemTask, never()).execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(executionDAOFacade).getWorkflowModel(workflow.getWorkflowId(), true);
        verify(queueDAO).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_ExceptionDuringExecution() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.FAILED);
        WorkflowModel workflow = new WorkflowModel();
        workflow.setStatus(WorkflowModel.WorkflowStatus.RUNNING);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(executionDAOFacade.getWorkflowModel(workflow.getWorkflowId(), true)).thenReturn(workflow);

        doThrow(RuntimeException.class).when(systemTask).execute(
                any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(systemTask).isAsyncComplete(any(TaskModel.class));
        verify(systemTask).execute(any(WorkflowModel.class), any(TaskModel.class), any(WorkflowExecutor.class));
        verify(executionDAOFacade).getWorkflowModel(workflow.getWorkflowId(), true);
        verify(queueDAO, never()).remove(anyString(), anyString());
    }

    @Test
    public void testExecute_TaskIsAsyncComplete() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);
        task.setStatus(TaskModel.Status.FAILED);
        WorkflowSystemTask systemTask = mock(WorkflowSystemTask.class);

        when(systemTask.isAsyncComplete(any(TaskModel.class))).thenReturn(true);

        asyncSystemTaskExecutor.execute(systemTask, taskId);

        verify(systemTask).isAsyncComplete(any(TaskModel.class));
        verify(queueDAO).remove(anyString(), anyString());
    }

    @Test
    public void testLoadTaskQuietly_TaskFound() {
        String taskId = "123";
        TaskModel task = new TaskModel();
        task.setTaskId(taskId);

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(task);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNotNull(result);
        assertEquals(task, result);
    }

    @Test
    public void testLoadTaskQuietly_TaskNotFound() {
        String taskId = "123";

        when(executionDAOFacade.getTaskModel(taskId)).thenReturn(null);

        TaskModel result = asyncSystemTaskExecutor.loadTaskQuietly(taskId);

        assertNull(result);
    }

    @Test
    public void testPostponeQuietly() {
        String queueName = "testQueue";
        String taskId = "123";

        asyncSystemTaskExecutor.postponeQuietly(queueName, new TaskModel());

        verify(queueDAO).postpone(queueName, taskId, anyInt(), anyLong());
    }
}