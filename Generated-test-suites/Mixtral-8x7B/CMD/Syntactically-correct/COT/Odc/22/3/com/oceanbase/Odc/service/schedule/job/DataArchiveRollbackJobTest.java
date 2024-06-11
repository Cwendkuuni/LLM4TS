/*
 * Copyright (c) 2023 OceanBase.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private JobExecutionContext jobExecutionContext;
    private ScheduleTaskRepository scheduleTaskRepository;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        jobExecutionContext = mock(JobExecutionContext.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository);
    }

    @Test
    public void testExecute_whenTaskEntityIsNull() {
        when(jobExecutionContext.getResult()).thenReturn(null);
        dataArchiveRollbackJob.execute(jobExecutionContext);
        // Assert that the task status is updated to FAILED
        assertEquals(TaskStatus.FAILED, dataArchiveRollbackJob.getTaskStatus(new ArrayList<>()));
    }

    @Test
    public void testExecute_whenDataArchiveTaskIsNotFound() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());
        dataArchiveRollbackJob.execute(jobExecutionContext);
        // Assert that the task status is updated to FAILED
        assertEquals(TaskStatus.FAILED, dataArchiveRollbackJob.getTaskStatus(new ArrayList<>()));
    }

    @Test
    public void testExecute_whenTaskUnitsIsEmpty() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        List<DlmTask> taskUnits = new ArrayList<>();
        dataArchiveTask.setResultJson(JsonUtils.toJson(taskUnits));
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));
        dataArchiveRollbackJob.execute(jobExecutionContext);
        // Assert that the task status is updated to FAILED
        assertEquals(TaskStatus.FAILED, dataArchiveRollbackJob.getTaskStatus(new ArrayList<>()));
    }

    @Test
    public void testExecute_whenTaskUnitsIsNotEmpty() {
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(rollbackParameters));
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setSourceDatabaseId(1L);
        taskUnits.add(dlmTask);
        dataArchiveTask.setResultJson(JsonUtils.toJson(taskUnits));
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(rollbackParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));
        when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class))).thenReturn(true);
        dataArchiveRollbackJob.execute(jobExecutionContext);
        // Assert that the task status is updated to DONE
        assertEquals(TaskStatus.DONE, dataArchiveRollbackJob.getTaskStatus(new ArrayList<>()));
    }

    @Test
    public void testGetTaskStatus_whenTaskUnitsIsEmpty() {
        List<DlmTask> taskUnits = new ArrayList<>();
        assertEquals(TaskStatus.FAILED, dataArchiveRollbackJob.getTaskStatus(taskUnits));
    }

    @Test
    public void testGetTaskStatus_whenAllTaskUnitsAreDone() {
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setStatus(TaskStatus.DONE);
        taskUnits.add(dlmTask);
        assertEquals(TaskStatus.DONE, dataArchiveRollbackJob.getTaskStatus(taskUnits));
    }

    @Test
    public void testGetTaskStatus_whenAllTaskUnitsArePreparing() {
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setStatus(TaskStatus.PREPARING);
        taskUnits.add(dlmTask);
        assertEquals(TaskStatus.DONE, dataArchiveRollbackJob.getTaskStatus(taskUnits));
    }

    @Test
    public void testGetTaskStatus_whenSomeTaskUnitsAreFailed() {
        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask dlmTask = new DlmTask();
        dlmTask.setStatus(TaskStatus.FAILED);
        taskUnits.add(dlmTask);
        dlmTask = new DlmTask();
        dlmTask.setStatus(TaskStatus.DONE);
        taskUnits.add(dlmTask);
        assertEquals(TaskStatus.FAILED, dataArchiveRollbackJob.getTaskStatus(taskUnits));
    }
}