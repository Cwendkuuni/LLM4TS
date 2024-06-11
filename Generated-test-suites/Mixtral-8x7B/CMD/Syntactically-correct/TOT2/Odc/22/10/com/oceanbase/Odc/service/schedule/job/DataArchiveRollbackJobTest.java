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
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Mock
    private AbstractDlmJobExecutor abstractDlmJobExecutor;

    private JobExecutionContext jobExecutionContext;

    private Scheduler scheduler;

    @Before
    public void setUp() throws SchedulerException {
        dataArchiveRollbackJob = new DataArchiveRollbackJob(new SimpleAsyncTaskExecutor(), abstractDlmJobExecutor);

        scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();

        jobExecutionContext = new JobExecutionContext(scheduler);
    }

    @Test
    public void testExecute() throws Exception {
        // Create ScheduleTaskEntity
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("DataArchiveRollbackJob");
        scheduleTaskEntity.setJobGroup("group1");
        scheduleTaskEntity.setParametersJson("{\"dataArchiveTaskId\":10L}");
        scheduleTaskEntity.setResultJson("[]");

        jobExecutionContext.setResult(scheduleTaskEntity);

        // Create DataArchiveRollbackParameters
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(10L);

        // Create DlmTask
        DlmTask dlmTask = new DlmTask();
        dlmTask.setSourceDatabaseId(1L);
        dlmTask.setTargetDatabaseId(2L);

        // Create List of DlmTask
        List<DlmTask> dlmTaskList = new ArrayList<>();
        dlmTaskList.add(dlmTask);

        // Create ScheduleTaskEntity with resultJson
        ScheduleTaskEntity scheduleTaskEntityWithResultJson = new ScheduleTaskEntity();
        scheduleTaskEntityWithResultJson.setId(11L);
        scheduleTaskEntityWithResultJson.setResultJson(JsonUtils.toJsonString(dlmTaskList));

        // Mock abstractDlmJobExecutor
        Optional<ScheduleTaskEntity> scheduleTaskEntityOptional = Optional.of(scheduleTaskEntityWithResultJson);
        JobType jobType = JobType.ROLLBACK;
        ScheduleTaskEntity resultScheduleTaskEntity = new ScheduleTaskEntity();
        resultScheduleTaskEntity.setId(11L);
        resultScheduleTaskEntity.setTaskStatus(TaskStatus.DONE);
        List<DlmTask> resultDlmTaskList = new ArrayList<>();
        resultDlmTaskList.add(dlmTask);
        String resultJson = JsonUtils.toJsonString(resultDlmTaskList);

        JobExecutionContext resultJobExecutionContext = new JobExecutionContext(scheduler);
        resultJobExecutionContext.setResult(resultScheduleTaskEntity);

        JobDetail jobDetail = newJob(DataArchiveRollbackJob.class)
                .withIdentity("DataArchiveRollbackJob", "group1")
                .build();

        Trigger trigger = newTrigger()
                .withIdentity("DataArchiveRollbackJobTrigger", "group1")
                .startNow()
                .build();

        jobExecutionContext.setJobDetail(jobDetail);

        // Mock find data archive task by id
        Optional<ScheduleTaskEntity> scheduleTaskEntityOptionalMock = Optional.of(scheduleTaskEntity);

        // Mock scheduleTaskRepository
        ScheduleTaskRepository scheduleTaskRepositoryMock = new ScheduleTaskRepository() {

            @Override
            public Optional<ScheduleTaskEntity> findById(Long id) {
                return scheduleTaskEntityOptionalMock;
            }

            @Override
            public void updateStatusById(Long id, TaskStatus taskStatus) throws SchedulerException {
                // Do nothing
            }
        };

        // Mock JsonUtils
        JsonUtils jsonUtilsMock = new JsonUtils() {

            @Override
            public String toJsonString(Object object) {
                return "";
            }

            @Override
            public <T> T fromJson(String jsonString, TypeReference<T> typeReference) {
                return (T) dlmTaskList;
            }
        };

        // Set mocks
        dataArchiveRollbackJob.setAbstractDlmJobExecutor(abstractDlmJobExecutor);
        dataArchiveRollbackJob.setJsonUtils(jsonUtilsMock);
        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepositoryMock);

        // Test execute
        dataArchiveRollbackJob.execute(jobExecutionContext);

        // Verify the task status is updated to DONE
        assertEquals(scheduleTaskEntity.getTaskStatus(), TaskStatus.DONE);
    }

    // Add more test cases for edge cases and error scenarios here

    // Do not remove or modify the following test end marker
    // 
}