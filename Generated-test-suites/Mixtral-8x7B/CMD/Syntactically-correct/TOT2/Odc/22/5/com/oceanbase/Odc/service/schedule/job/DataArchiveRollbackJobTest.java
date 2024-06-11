
package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private JobExecutionContext jobExecutionContext;
    private ScheduleTaskEntity scheduleTaskEntity;
    private ScheduleTaskEntity dataArchiveTask;
    private List<DlmTask> taskUnits;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = spy(new DataArchiveRollbackJob());
        jobExecutionContext = mock(JobExecutionContext.class);
        scheduleTaskEntity = mock(ScheduleTaskEntity.class);
        dataArchiveTask = mock(ScheduleTaskEntity.class);
        taskUnits = new ArrayList<>();
    }

    @Test
    public void testExecuteWithNullTaskEntity() throws JobExecutionException {
        doNothing().when(dataArchiveRollbackJob).executeTask(anyLong(), any());

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(dataArchiveRollbackJob, times(1)).executeTask(anyLong(), any());
        verify(dataArchiveRollbackJob, times(1)).updateStatusById(anyLong(), any());
    }

    @Test
    public void testExecuteWithNullRollbackParameters() throws JobExecutionException {
        String parametersJson = "{}";
        when(scheduleTaskEntity.getParametersJson()).thenReturn(parametersJson);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(dataArchiveRollbackJob, times(0)).executeTask(anyLong(), any());
        verify(dataArchiveRollbackJob, times(1)).updateStatusById(anyLong(), any());
    }

    @Test
    public void testExecuteWithNullDataArchiveTask() throws JobExecutionException {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        String parametersJson = JsonUtils.toJson(rollbackParameters);
        when(scheduleTaskEntity.getParametersJson()).thenReturn(parametersJson);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(dataArchiveRollbackJob, times(0)).executeTask(anyLong(), any());
        verify(dataArchiveRollbackJob, times(1)).updateStatusById(anyLong(), any());
    }

    @Test
    public void testExecuteWithNullResultJson() throws JobExecutionException {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        String parametersJson = JsonUtils.toJson(rollbackParameters);
        when(scheduleTaskEntity.getParametersJson()).thenReturn(parametersJson);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getResultJson()).thenReturn(null);

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(dataArchiveRollbackJob, times(0)).executeTask(anyLong(), any());
        verify(dataArchiveRollbackJob, times(1)).updateStatusById(anyLong(), any());
    }

    @Test
    public void testExecuteWithEmptyTaskUnits() throws JobExecutionException {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        String parametersJson = JsonUtils.toJson(rollbackParameters);
        when(scheduleTaskEntity.getParametersJson()).thenReturn(parametersJson);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getResultJson()).thenReturn("[]");

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(dataArchiveRollbackJob, times(0)).executeTask(anyLong(), any());
        verify(dataArchiveRollbackJob, times(1)).updateStatusById(anyLong(), any());
    }

    @Test
    public void testExecuteWithNonEmptyTaskUnits() throws JobExecutionException {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        String parametersJson = JsonUtils.toJson(rollbackParameters);
        when(scheduleTaskEntity.getParametersJson()).thenReturn(parametersJson);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getResultJson()).thenReturn("[" +
                "{\"sourceDatabaseId\":1,\"targetDatabaseId\":2,\"jobType\":\"ROLLBACK\",\"status\":\"DONE\"," +
                "\"id\":1,\"jobName\":\"testJob\",\"jobGroup\":\"testGroup\",\"parametersJson\":\"{}\"}" +
                "]");

        doNothing().when(dataArchiveRollbackJob).executeTask(anyLong(), any());

        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(dataArchiveRollbackJob, times(1)).executeTask(anyLong(), any());
        verify(dataArchiveRollbackJob, times(1)).updateStatusById(anyLong(), any());
    }
}
