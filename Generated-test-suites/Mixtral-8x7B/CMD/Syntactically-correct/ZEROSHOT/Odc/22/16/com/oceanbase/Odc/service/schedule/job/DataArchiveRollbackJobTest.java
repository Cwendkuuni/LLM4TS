package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
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
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.extern.slf4j.Slf4j;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private JobExecutionContext context;

    @InjectMocks
    private DataArchiveRollbackJob dataArchiveRollbackJob;

    private static final Logger LOG = LoggerFactory.getLogger(DataArchiveRollbackJobTest.class);

    @Before
    public void setup() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
    }

    @Test
    public void testExecuteWhenDataArchiveTaskNotFound() throws JobExecutionException {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        when(context.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        dataArchiveRollbackJob.execute(context);

        assertEquals(TaskStatus.FAILED, scheduleTaskEntity.getStatus());
    }

    @Test
    public void testExecuteWhenDataArchiveTaskFound() throws JobExecutionException {
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));

        DlmTask dlmTask1 = new DlmTask();
        dlmTask1.setId(1L);
        dlmTask1.setSourceDatabaseId(1L);
        dlmTask1.setTargetDatabaseId(2L);
        dlmTask1.setJobType(JobType.MIGRATE);

        DlmTask dlmTask2 = new DlmTask();
        dlmTask2.setId(2L);
        dlmTask2.setSourceDatabaseId(2L);
        dlmTask2.setTargetDatabaseId(1L);
        dlmTask2.setJobType(JobType.MIGRATE);

        List<DlmTask> taskUnits = Arrays.asList(dlmTask1, dlmTask2);
        String taskUnitsJson = JsonUtils.toJson(taskUnits);

        ScheduleTaskEntity dataArchiveTaskEntity = new ScheduleTaskEntity();
        dataArchiveTaskEntity.setId(2L);
        dataArchiveTaskEntity.setResultJson(taskUnitsJson);

        when(context.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTaskEntity));

        dataArchiveRollbackJob.execute(context);

        // Check if the status of the scheduleTaskEntity is updated correctly
        ScheduleTaskEntity updatedScheduleTaskEntity = scheduleTaskRepository.findById(1L).get();
        assertEquals(TaskStatus.DONE, updatedScheduleTaskEntity.getStatus());
    }
}