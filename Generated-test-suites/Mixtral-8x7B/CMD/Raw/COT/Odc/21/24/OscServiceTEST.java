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

package com.oceanbase.odc.service.onlineschemachange;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;

@RunWith(MockitoJUnitRunner.class)
public class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ConnectionService connectionService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private OscService oscService;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(oscService, "oscDBUserUtil", new OscDBUserUtil());

        // Initialize mocks here if needed
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setConnectionId(2L);
        databaseEntity.setDatabaseId(id);

        OscLockDatabaseUserInfo oscDatabase = new OscLockDatabaseUserInfo();
        oscDatabase.setDatabaseId(id);
        oscDatabase.setLockDatabaseUserRequired(false);

        Optional<DatabaseEntity> databaseOptional = Optional.of(databaseEntity);
        PreConditions preConditions = new PreConditions();

        ReflectionTestUtils.setField(oscService, "preConditions", preConditions);

        // Mocking databaseRepository.findById(id)
        ReflectionTestUtils.setField(oscService, "databaseRepository", databaseRepository);
        Mockito
            .when(databaseRepository.findById(id))
            .thenReturn(databaseOptional);

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        Assert.assertEquals(oscDatabase, result);
        Mockito.verify(databaseRepository).findById(id);
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName("2");
        scheduleTask.setResultJson("{}");

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        result.setManualSwapTableStarted(false);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(2L);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));

        PreConditions preConditions = new PreConditions();

        ReflectionTestUtils.setField(oscService, "preConditions", preConditions);

        // Mocking scheduleTaskRepository.findById(scheduleTaskId)
        ReflectionTestUtils.setField(oscService, "scheduleTaskRepository", scheduleTaskRepository);
        Mockito
            .when(scheduleTaskRepository.findById(scheduleTaskId))
            .thenReturn(Optional.of(scheduleTask));

        // Mocking scheduleRepository.findById(2L)
        ReflectionTestUtils.setField(oscService, "scheduleRepository", scheduleRepository);
        Mockito
            .when(scheduleRepository.findById(2L))
            .thenReturn(Optional.of(scheduleEntity));

        // Mocking JsonUtils.fromJson
        ReflectionTestUtils.setField(oscService, "jsonUtils", new JsonUtils());
        Mockito
            .when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), OnlineSchemaChangeParameters.class))
            .thenReturn(oscParameters);

        Mockito
            .when(JsonUtils.fromJson(scheduleTask.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class))
            .thenReturn(result);

        OscSwapTableVO oscSwapTable = new OscSwapTableVO();
        ReflectionTestUtils.setField(oscService, "oscSwapTable", oscSwapTable);

        oscService.swapTable(scheduleTaskId);

        Mockito.verify(scheduleTaskRepository).findById(scheduleTaskId);
        Mockito.verify(scheduleRepository).findById(2L);
        Mockito.verify(JsonUtils).fromJson(scheduleEntity.getJobParametersJson(), OnlineSchemaChangeParameters.class);
        Mockito.verify(JsonUtils).fromJson(scheduleTask.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class);
        Mockito.verify(preConditions).validExists(ResourceType.ODC_SCHEDULE_TASK, "schedule task",
                scheduleTaskId, scheduleTaskOptional -> true);
        Mockito.verify(preConditions).validExists(ResourceType.ODC_SCHEDULE, "schedule ",
                2L, scheduleEntityOptional -> true);

        // Add more verification and edge cases if needed
    }

    @Test
    public void testGetLockUserIsRequired() {
        // Add your test cases for getLockUserIsRequired here
    }
}

