package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.connection.ConnectionConfig;
import com.oceanbase.odc.core.connection.DatabaseEntity;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("unchecked")
public class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ConnectionService connectionService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    private OscService oscService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        oscService = new OscService();

        ReflectionTestUtils.setField(oscService, "databaseRepository", databaseRepository);
        ReflectionTestUtils.setField(oscService, "connectionService", connectionService);
        ReflectionTestUtils.setField(oscService, "scheduleTaskRepository", scheduleTaskRepository);
        ReflectionTestUtils.setField(oscService, "scheduleRepository", scheduleRepository);
    }

    @Test
    public void testGetOscDatabaseInfo_whenDatabaseIdNotPresent_thenReturnEmptyOscLockDatabaseUserInfo() {
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);

        assertNull(result.getDatabaseId());
        assertFalse(result.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfo_whenDatabaseIdPresent_thenReturnOscLockDatabaseUserInfo() {
        DatabaseEntity databaseEntity = new DatabaseEntity(1L, 1L, "test", "localhost", 3306, "root", "password",
                "utf8", "testSchema", true, true, null, null, null, null, null, null, null, null, null);

        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);

        assertEquals(1L, result.getDatabaseId());
        assertTrue(result.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable_whenScheduleTaskIdAndDatabaseIdNotPresent_thenThrowException() {
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            oscService.swapTable(1L);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertEquals("schedule task", e.getMessage());
        }
    }

    @Test
    public void testSwapTable_whenScheduleTaskIdPresentButScheduleIdNotPresent_thenThrowException() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity(1L, 1L, 1L, "{\"json\":\"data\"}"
                , "{\"json\":\"result\"}", false, false, false, false);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            oscService.swapTable(1L);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertEquals("schedule ", e.getMessage());
        }
    }

    @Test
    public void testSwapTable_whenScheduleTaskIdAndScheduleIdPresentButJobTypeNotCorrect_thenThrowException() {
        ScheduleEntity scheduleEntity = new ScheduleEntity(1L, 1L, "{\"json\":\"data\"}", JobType.BACKUP,
                null, null, null, null, null);

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity(1L, 1L, 1L, "{\"json\":\"data\"}"
                , "{\"json\":\"result\"}", false, false, false, false);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        try {
            oscService.swapTable(1L);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertEquals("Task type is not ONLINE_SCHEMA_CHANGE", e.getMessage());
        }
    }

    @Test
    public void testSwapTable_whenScheduleTaskIdAndScheduleIdPresentButSwapTableTypeNotCorrect_thenThrowException() {
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters(1L, 1L, 1L,
                "testTable", "testTableRenamed", SwapTableType.AUTO, null, null, null, null, null,
                null, null, null);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(true, true,
                false, false, null, null);

        ScheduleEntity scheduleEntity = new ScheduleEntity(1L, 1L, JsonUtils.toJson(oscParameters),
                JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, null, null, null, null, null);

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity(1L, 1L, 1L,
                JsonUtils.toJson(oscParameters), JsonUtils.toJson(result), false, false, false, false);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        try {
            oscService.swapTable(1L);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertEquals("Swap table type is not MANUAL", e.getMessage());
        }
    }

    @Test
    public void testSwapTable_whenScheduleTaskIdAndScheduleIdPresent_thenReturnOscSwapTableVO() {
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters(1L, 1L, 1L,
                "testTable", "testTableRenamed", SwapTableType.MANUAL, null, null, null, null, null,
                null, null, null);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult(false, true,
                true, false, null, null);

        ScheduleEntity scheduleEntity = new ScheduleEntity(1L, 1L, JsonUtils.toJson(oscParameters),
                JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, null, null, null, null, null);

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity(1L, 1L, 1L,
                JsonUtils.toJson(oscParameters), JsonUtils.toJson(result), false, false, false, false);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        OscSwapTableVO resultOscSwapTable = oscService.swapTable(1L);

        assertEquals(1L, resultOscSwapTable.getScheduleTaskId());
    }

    @Test
    public void testGetLockUserIsRequired_whenConnectionIdPresent_thenReturnBoolean() {
        ConnectionConfig connectionConfig = new ConnectionConfig(1L, "testDialect", "testHost", 3306,
                "testUsername", "testPassword", "testSchema", "testCharset", null, null, null,
                null, null, null, null, null, null, null, null);

        ConnectionSessionFactory connectionSessionFactory = mock(ConnectionSessionFactory.class);

        String version = "testVersion";
        ConnectionSession connectionSession = mock(ConnectionSession.class);

        when(connectionSession.isExpired()).thenReturn(false);
        when(connectionSessionFactory.generateSession()).thenReturn(connectionSession);
        when(connectionSessionFactory.getVersion(connectionSession)).thenReturn(version);

        ReflectionTestUtils.setField(oscService, "connectionSessionFactory", connectionSessionFactory);

        boolean result = (boolean) ReflectionTestUtils.invokeMethod(oscService, "getLockUserIsRequired",
                connectionConfig.getConnectionId());

        assertTrue(result);
    }
}