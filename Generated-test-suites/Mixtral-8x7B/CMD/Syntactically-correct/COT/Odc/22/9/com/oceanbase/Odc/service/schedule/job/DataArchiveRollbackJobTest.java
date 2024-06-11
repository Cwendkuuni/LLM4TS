
package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.SimpleTriggerImpl;

@Slf4j
public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private JobExecutionContext jobExecutionContext;
    private ScheduleTaskEntity taskEntity;
    private ScheduleTaskEntity dataArchiveTask;
    private List<DlmTask> taskUnits;
    private TypeReference<List<DlmTask>> typeReference;
    private JsonUtils jsonUtils;
    private DlmJobIdUtil dlmJobIdUtil;
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setup() {
        jsonUtils = mock(JsonUtils.class);
        dlmJobIdUtil = mock(DlmJobIdUtil.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        dataArchiveRollbackJob = new DataArchiveRollbackJob(jsonUtils, dlmJobIdUtil, scheduleTaskRepository);
        taskEntity = mock(ScheduleTaskEntity.class);
        dataArchiveTask = mock(ScheduleTaskEntity.class);
        taskUnits = Collections.singletonList(mock(DlmTask.class));
        typeReference = new TypeReference<List<DlmTask>>() {
        };
    }

    @Test
    public void testExecute_whenDataArchiveTaskNotFound_thenUpdateTaskStatusToFailed() throws SchedulerException {
        when(taskEntity.getParametersJson()).thenReturn("{\"dataArchiveTaskId\":1}");
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(scheduleTaskRepository.updateStatusById(anyLong(), any())).thenReturn(true);

        jobExecutionContext = createJobExecutionContext(taskEntity);
        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).findById(eq(1L));
        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(1L), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_whenDataArchiveTaskFound_thenPrepareTasksForRollback() throws SchedulerException {
        String parametersJson = "{\"dataArchiveTaskId\":1}";
        String resultJson = "{\"taskUnits\":[{\"sourceDatabaseId\":1,\"targetDatabaseId\":2}]}";
        when(taskEntity.getParametersJson()).thenReturn(parametersJson);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getResultJson()).thenReturn(resultJson);
        when(jsonUtils.fromJson(resultJson, typeReference)).thenReturn(taskUnits);

        doNothing().when(taskUnits.get(0)).setId(anyLong());
        doNothing().when(taskUnits.get(0)).setSourceDatabaseId(anyLong());
        doNothing().when(taskUnits.get(0)).setTargetDatabaseId(anyLong());
        doNothing().when(taskUnits.get(0)).setJobType(any());
        doNothing().when(taskUnits.get(0)).setStatus(any());

        jobExecutionContext = createJobExecutionContext(taskEntity);
        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).findById(eq(1L));
        verify(jsonUtils, times(1)).fromJson(eq(resultJson), eq(typeReference));
        verify(taskUnits.get(0), times(1)).setId(anyLong());
        verify(taskUnits.get(0), times(1)).setSourceDatabaseId(anyLong());
        verify(taskUnits.get(0), times(1)).setTargetDatabaseId(anyLong());
        verify(taskUnits.get(0), times(1)).setJobType(any());
        verify(taskUnits.get(0), times(1)).setStatus(any());
    }

    @Test
    public void testExecute_whenTasksPrepared_thenExecuteTasksAndUpdateTaskStatus() throws SchedulerException {
        String parametersJson = "{\"dataArchiveTaskId\":1}";
        String resultJson = "{\"taskUnits\":[{\"sourceDatabaseId\":1,\"targetDatabaseId\":2}]}";
        when(taskEntity.getParametersJson()).thenReturn(parametersJson);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getResultJson()).thenReturn(resultJson);
        when(jsonUtils.fromJson(resultJson, typeReference)).thenReturn(taskUnits);

        doNothing().when(taskUnits.get(0)).setId(anyLong());
        doNothing().when(taskUnits.get(0)).setSourceDatabaseId(anyLong());
        doNothing().when(taskUnits.get(0)).setTargetDatabaseId(anyLong());
        doNothing().when(taskUnits.get(0)).setJobType(any());
        doNothing().when(taskUnits.get(0)).setStatus(any());

        when(scheduleTaskRepository.updateStatusById(anyLong(), any())).thenReturn(true);

        jobExecutionContext = createJobExecutionContext(taskEntity);
        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).findById(eq(1L));
        verify(jsonUtils, times(1)).fromJson(eq(resultJson), eq(typeReference));
        verify(taskUnits.get(0), times(1)).setId(anyLong());
        verify(taskUnits.get(0), times(1)).setSourceDatabaseId(anyLong());
        verify(taskUnits.get(0), times(1)).setTargetDatabaseId(anyLong());
        verify(taskUnits.get(0), times(1)).setJobType(any());
        verify(taskUnits.get(0), times(1)).setStatus(any());
        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(1L), any());
    }

    @Test
    public void testExecute_whenSchedulerExceptionOccurs_thenLogErrorAndUpdateTaskStatusToError()
            throws SchedulerException {
        String parametersJson = "{\"dataArchiveTaskId\":1}";
        when(taskEntity.getParametersJson()).thenReturn(parametersJson);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        doThrow(new SchedulerException("Test exception")).when(dataArchiveRollbackJob).executeTask(anyLong(),
                any());
        when(scheduleTaskRepository.updateStatusById(anyLong(), any())).thenReturn(true);

        jobExecutionContext = createJobExecutionContext(taskEntity);
        dataArchiveRollbackJob.execute(jobExecutionContext);

        verify(scheduleTaskRepository, times(1)).findById(eq(1L));
        verify(dataArchiveRollbackJob, times(1)).executeTask(anyLong(), any());
        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(1L), eq(TaskStatus.ERROR));
    }

    private JobExecutionContext createJobExecutionContext(ScheduleTaskEntity taskEntity) throws SchedulerException {
        JobExecutionContext context = mock(JobExecutionContext.class);
        Trigger trigger =
                TriggerBuilder.newTrigger().withIdentity(taskEntity.getJobName(), taskEntity.getJobGroup())
                        .startNow().build();
        when(context.getResult()).thenReturn(taskEntity);
        when(context.getTrigger()).thenReturn(trigger);
        return context;
    }
}
