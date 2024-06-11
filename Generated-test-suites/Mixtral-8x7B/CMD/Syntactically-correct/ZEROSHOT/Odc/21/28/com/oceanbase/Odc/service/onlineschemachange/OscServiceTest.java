/**
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
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.session.ConnectionSession;
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
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;

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

    @Mock
    private ConnectionSessionFactory connectionSessionFactory;

    @Mock
    private ConnectionSession connectionSession;

    @InjectMocks
    private OscService oscService = spy(new OscService());

    private DatabaseEntity databaseEntity;

    private ScheduleEntity scheduleEntity;

    private ScheduleTaskEntity scheduleTaskEntity;

    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        databaseEntity.setConnectionId(1L);

        scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName(UUID.randomUUID().toString());
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setScheduleId(1L);
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));
        scheduleTaskEntity.setScheduleTaskId(1L);

        connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MYSQL");

        ReflectionTestUtils.setField(oscService, "oscDBUserUtil", mock(OscDBUserUtil.class));

        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(databaseEntity));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong()))
                .thenReturn(connectionConfig);
        when(connectionSessionFactory.generateSession()).thenReturn(connectionSession);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        OscLockDatabaseUserInfo oscLockDatabaseUserInfo = oscService.getOscDatabaseInfo(1L);
        assertEquals(1L, oscLockDatabaseUserInfo.getDatabaseId());
        assertTrue(oscLockDatabaseUserInfo.isLockDatabaseUserRequired());

        verify(databaseRepository, times(1)).findById(1L);
        verify(oscDBUserUtil, times(1)).isLockUserRequired(any(), any());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSwapTable() {
        doNothing().when(oscService).updateTaskResult(anyLong(), any());

        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);
        assertEquals(1L, oscSwapTable.getScheduleTaskId());

        ArgumentCaptor<OnlineSchemaChangeScheduleTaskResult> resultCaptor =
                ArgumentCaptor.forClass(OnlineSchemaChangeScheduleTaskResult.class);

        verify(scheduleTaskRepository, times(1)).findById(1L);
        verify(scheduleRepository, times(1)).findById(anyLong());
        verify(oscDBUserUtil, times(1)).isLockUserRequired(any(), any());
        verify(oscService, times(1)).updateTaskResult(1L, resultCaptor.capture());
        OnlineSchemaChangeScheduleTaskResult result = resultCaptor.getValue();
        assertTrue(result.isManualSwapTableStarted());
        assertFalse(result.isManualSwapTableEnabled());

        oscSwapTable = oscService.swapTable(1L);
        assertEquals(1L, oscSwapTable.getScheduleTaskId());

        verify(scheduleTaskRepository, times(2)).findById(1L);
        verify(scheduleRepository, times(2)).findById(anyLong());
        verify(oscDBUserUtil, times(2)).isLockUserRequired(any(), any());
        verify(oscService, times(2)).updateTaskResult(1L, resultCaptor.capture());
        result = resultCaptor.getValue();
        assertFalse(result.isManualSwapTableStarted());
        assertFalse(result.isManualSwapTableEnabled());

        oscSwapTable = oscService.swapTable(2L);
        assertNull(oscSwapTable);

        verify(scheduleTaskRepository, times(3)).findById(anyLong());
        verify(scheduleRepository, times(3)).findById(anyLong());
        verify(oscDBUserUtil, times(3)).isLockUserRequired(any(), any());
        verify(oscService, times(3)).updateTaskResult(anyLong(), any());
    }

    @Test
    public void testGetLockUserIsRequired() {
        when(connectionSession.executeQuery(any())).thenReturn("5.7");
        assertTrue(oscService.getLockUserIsRequired(1L));

        when(connectionSession.executeQuery(any())).thenReturn(null);
        assertTrue(oscService.getLockUserIsRequired(1L));

        when(connectionSession.executeQuery(any())).thenReturn("8.0");
        assertFalse(oscService.getLockUserIsRequired(1L));

        verify(connectionSessionFactory, times(3)).generateSession();
        verify(connectionSession, times(1)).expire();
        verify(connectionSession, times(3)).executeQuery(any());
    }
}