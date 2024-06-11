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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.connection.ConnectionConfig;
import com.oceanbase.odc.core.connection.ConnectionService;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;

@Transactional(rollbackFor = Exception.class)
public class OscServiceTest {

    private OscService oscService;

    @Before
    public void setup() {
        DatabaseRepository databaseRepository = mock(DatabaseRepository.class);
        ConnectionService connectionService = mock(ConnectionService.class);
        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        ScheduleRepository scheduleRepository = mock(ScheduleRepository.class);

        oscService = new OscService();
        ReflectionTestUtils.setField(oscService, "databaseRepository", databaseRepository);
        ReflectionTestUtils.setField(oscService, "connectionService", connectionService);
        ReflectionTestUtils.setField(oscService, "scheduleTaskRepository", scheduleTaskRepository);
        ReflectionTestUtils.setField(oscService, "scheduleRepository", scheduleRepository);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        DatabaseRepository databaseRepository = (DatabaseRepository) ReflectionTestUtils.getField(oscService,
                "databaseRepository");
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(new DatabaseEntity()));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(123L);

        assertEquals(Long.class, result.getDatabaseId().getClass());
        assertNull(result.getLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfo_DatabaseNotExists() {
        DatabaseRepository databaseRepository = (DatabaseRepository) ReflectionTestUtils.getField(oscService,
                "databaseRepository");
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(123L);

        assertEquals(null, result.getDatabaseId());
        assertEquals(null, result.getLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable() {
        DatabaseRepository databaseRepository = (DatabaseRepository) ReflectionTestUtils.getField(oscService,
                "databaseRepository");
        ConnectionService connectionService = (ConnectionService) ReflectionTestUtils.getField(oscService,
                "connectionService");
        ScheduleTaskRepository scheduleTaskRepository = (ScheduleTaskRepository) ReflectionTestUtils.getField(oscService,
                "scheduleTaskRepository");
        ScheduleRepository scheduleRepository = (ScheduleRepository) ReflectionTestUtils.getField(oscService,
                "scheduleRepository");

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(123L);
        scheduleTask.setJobName(String.valueOf(456L));
        scheduleTask.setResultJson(JsonUtils.toJson(result));

        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(new DatabaseEntity()));
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(new ConnectionConfig());
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(new ScheduleEntity()));

        PreConditions preConditions = mock(PreConditions.class);
        ReflectionTestUtils.setField(oscService, "preConditions", preConditions);
        when(preConditions.validExists(any(ResourceType.class), anyString(), anyLong(), any(Optional.class)))
                .thenReturn(null);

        OscSwapTableVO oscSwapTable = oscService.swapTable(123L);

        assertEquals(Long.class, oscSwapTable.getScheduleTaskId().getClass());
    }

    @Test
    public void testSwapTable_DatabaseNotExists() {
        DatabaseRepository databaseRepository = (DatabaseRepository) ReflectionTestUtils.getField(oscService,
                "databaseRepository");
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.empty());

        OscSwapTableVO oscSwapTable = oscService.swapTable(123L);

        assertEquals(null, oscSwapTable);
    }

    @Test
    public void testSwapTable_ScheduleTaskNotExists() {
        DatabaseRepository databaseRepository = (DatabaseRepository) ReflectionTestUtils.getField(oscService,
                "databaseRepository");
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(new DatabaseEntity()));
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        OscSwapTableVO oscSwapTable = oscService.swapTable(123L);

        assertEquals(null, oscSwapTable);
    }

    @Test
    public void testGetLockUserIsRequired() {
        ConnectionService connectionService = (ConnectionService) ReflectionTestUtils.getField(oscService,
                "connectionService");
        ConnectionSessionFactory factory = mock(ConnectionSessionFactory.class);
        ConnectionSessionUtil util = mock(ConnectionSessionUtil.class);
        ConnectionSession session = mock(ConnectionSession.class);

        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(new ConnectionConfig());
        when(connectionService.getConnectionSessionFactory(any(ConnectionConfig.class))).thenReturn(factory);
        when(factory.generateSession()).thenReturn(session);
        when(util.getVersion(any(ConnectionSession.class))).thenReturn("123");
        ReflectionTestUtils.setField(oscService, "connectionSessionUtil", util);

        boolean result = (boolean) ReflectionTestUtils.invokeMethod(oscService, "getLockUserIsRequired", 123L);

        assertTrue(result);
    }

    @Test
    public void testGetLockUserIsRequired_Exception() {
        ConnectionService connectionService = (ConnectionService) ReflectionTestUtils.getField(oscService,
                "connectionService");
        ConnectionSessionFactory factory = mock(ConnectionSessionFactory.class);
        ConnectionSessionUtil util = mock(ConnectionSessionUtil.class);

        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(new ConnectionConfig());
        when(connectionService.getConnectionSessionFactory(any(ConnectionConfig.class))).thenReturn(factory);
        when(util.getVersion(any(ConnectionSession.class))).thenThrow(new RuntimeException());
        ReflectionTestUtils.setField(oscService, "connectionSessionUtil", util);

        boolean result = (boolean) ReflectionTestUtils.invokeMethod(oscService, "getLockUserIsRequired", 123L);

        assertFalse(result);
    }

}