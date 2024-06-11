package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.matchers.GroupMatcher;

@Slf4j
public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private JobExecutionContext jobExecutionContext;
    private ScheduleTaskEntity scheduleTaskEntity;
    private ScheduleTaskEntity dataArchiveTask;
    private List<DlmTask> taskUnits;

    @Before
    public void setup() {
        dataArchiveRollbackJob = spy(new DataArchiveRollbackJob());
        jobExecutionContext = mock(JobExecutionContext.class);
        scheduleTaskEntity = mock(ScheduleTaskEntity.class);
        dataArchiveTask = mock(ScheduleTaskEntity.class);
        taskUnits = mock(List.class);

        doReturn(scheduleTaskEntity).when(dataArchiveRollbackJob).getScheduleTaskEntity(anyLong());

        when(jobExecutionContext.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        when(JsonUtils.fromJson(anyString(), any(Class.class))).thenReturn(new DataArchiveRollbackParameters());
        when(JsonUtils.fromJson(anyString(), any(TypeReference.class))).thenReturn(taskUnits);
        when(DlmJobIdUtil.generateHistoryJobId(anyString(), anyString(), anyLong(), anyInt()))
                .thenReturn(1L);
    }

    @Test
    public void testExecute() throws JobExecutionException {
        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(dataArchiveRollbackJob, times(1)).getScheduleTaskEntity(anyLong());
        verify(dataArchiveRollbackJob, times(1)).executeTask(anyLong(), any());
        verify(scheduleTaskRepository, times(1))
                .updateStatusById(anyLong(), any(TaskStatus.class));
    }

    @Test
    public void testGetScheduleTaskEntity() {
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));

        ScheduleTaskEntity result = dataArchiveRollbackJob.getScheduleTaskEntity(1L);

        assertEquals(scheduleTaskEntity, result);
    }

    @Test
    public void testExecuteTask() {
        dataArchiveRollbackJob.executeTask(1L, taskUnits);

        verify(dataArchiveRollbackJob, times(1)).updateTaskUnits(anyLong(), any());
        verify(dataArchiveRollbackJob, times(1)).executeDlmJob(any(), any());
    }

    @Test
    public void testUpdateTaskUnits() {
        dataArchiveRollbackJob.updateTaskUnits(1L, taskUnits);

        verify(scheduleTaskRepository, times(1)).updateTasksUnitsById(1L, taskUnits);
    }

    @Test
    public void testExecuteDlmJob() throws Exception {
        DataArchiveRollbackParameters parameters = new DataArchiveRollbackParameters();
        parameters.setDataArchiveTaskId(1L);
        String parametersJson = JsonUtils.toJson(parameters);
        when(scheduleTaskEntity.getParametersJson()).thenReturn(parametersJson);

        dataArchiveRollbackJob.executeDlmJob(scheduleTaskEntity, taskUnits);

        verify(dataArchiveRollbackJob, times(1)).findDataArchiveTaskById(anyLong());
        verify(dataArchiveRollbackJob, times(1)).prepareRollbackTasks(any(), any());
        verify(dataArchiveRollbackJob, times(1)).executeSubTasks(any());
        verify(dataArchiveRollbackJob, times(1)).getTaskStatus(any());
    }

    @Test
    public void testFindDataArchiveTaskById() {
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));

        ScheduleTaskEntity result = dataArchiveRollbackJob.findDataArchiveTaskById(1L);

        assertEquals(dataArchiveTask, result);
    }

    @Test
    public void testPrepareRollbackTasks() throws Exception {
        when(dataArchiveTask.getResultJson()).thenReturn("[]");

        dataArchiveRollbackJob.prepareRollbackTasks(dataArchiveTask, taskUnits);

        verify(dataArchiveRollbackJob, times(0)).updateTaskUnitStatus(any(), any());
    }

    @Test
    public void testExecuteSubTasks() throws Exception {
        dataArchiveRollbackJob.executeSubTasks(taskUnits);

        verify(dataArchiveRollbackJob, times(1)).executeDlmJob(any(), any());
    }

    @Test
    public void testGetTaskStatus() {
        when(taskUnits.stream()).thenReturn(taskUnits.stream());

        TaskStatus result = dataArchiveRollbackJob.getTaskStatus(taskUnits);

        assertEquals(TaskStatus.SUCCEEDED, result);
    }

    @Test
    public void testGetTaskStatus_AllFailed() {
        when(taskUnits.stream())
                .thenReturn(
                        taskUnits.stream()
                                .map(
                                        task -> {
                                            task.setStatus(TaskStatus.FAILED);
                                            return task;
                                        }));

        TaskStatus result = dataArchiveRollbackJob.getTaskStatus(taskUnits);

        assertEquals(TaskStatus.FAILED, result);
    }

    @Test
    public void testGetTaskStatus_AllPending() {
        when(taskUnits.stream())
                .thenReturn(
                        taskUnits.stream()
                                .map(
                                        task -> {
                                            task.setStatus(TaskStatus.PENDING);
                                            return task;
                                        }));

        TaskStatus result = dataArchiveRollbackJob.getTaskStatus(taskUnits);

        assertEquals(TaskStatus.PENDING, result);
    }

    @Test
    public void testGetTaskStatus_OneFailed() {
        when(taskUnits.stream())
                .thenReturn(
                        taskUnits.stream()
                                .map(
                                        task -> {
                                            task.setStatus(TaskStatus.FAILED);
                                            return task;
                                        })
                                .limit(taskUnits.size() - 1));

        when(taskUnits.limit(1).findFirst().get().getStatus())
                .thenReturn(TaskStatus.SUCCEEDED);

        TaskStatus result = dataArchiveRollbackJob.getTaskStatus(taskUnits);

        assertEquals(TaskStatus.FAILED, result);
    }

    @Test
    public void testUpdateTaskUnitStatus() {
        DlmTask dlmTask = new DlmTask();
        dlmTask.setStatus(TaskStatus.PENDING);

        dataArchiveRollbackJob.updateTaskUnitStatus(dlmTask, TaskStatus.SUCCEEDED);

        assertEquals(TaskStatus.SUCCEEDED, dlmTask.getStatus());
    }

    @Test
    public void testExecuteDlmJob_Exception() throws Exception {
        doThrow(new Exception("test exception")).when(dataArchiveRollbackJob).executeSubTasks(any());

        dataArchiveRollbackJob.executeDlmJob(scheduleTaskEntity, taskUnits);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), TaskStatus.FAILED);
    }

    @Test
    public void testExecuteDlmJob_ExceptionNoRollbackTask() throws Exception {
        DataArchiveRollbackParameters parameters = new DataArchiveRollbackParameters();
        parameters.setDataArchiveTaskId(null);
        String parametersJson = JsonUtils.toJson(parameters);
        when(scheduleTaskEntity.getParametersJson()).thenReturn(parametersJson);

        doThrow(new Exception("test exception")).when(dataArchiveRollbackJob).executeSubTasks(any());

        dataArchiveRollbackJob.executeDlmJob(scheduleTaskEntity, taskUnits);

        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), TaskStatus.FAILED);
    }
}