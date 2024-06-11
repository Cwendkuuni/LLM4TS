package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
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
import com.oceanbase.odc.service.schedule.model.JobType;

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

    @InjectMocks
    private OscService oscService;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(oscService, "jsonUtils", new JsonUtils());
    }

    @Test
    public void testGetOscDatabaseInfo_whenDatabaseDoesNotExist_returnEmptyOscLockDatabaseUserInfo() {
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);

        assertEquals(0, oscDatabase.getDatabaseId());
        assertFalse(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfo_whenDatabaseExists_returnOscLockDatabaseUserInfo() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        databaseEntity.setConnectionId(1L);

        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(databaseEntity));
        when(oscService.getLockUserIsRequired(anyLong())).thenReturn(true);

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);

        assertEquals(1, oscDatabase.getDatabaseId());
        assertTrue(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable_whenScheduleTaskDoesNotExist_throwIllegalArgumentException() {
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            oscService.swapTable(1L);
        } catch (IllegalArgumentException e) {
            assertEquals("Schedule task [1] does not exist.", e.getMessage());
            return;
        }

        assertTrue("Expected IllegalArgumentException to be thrown.", false);
    }

    @Test
    public void testSwapTable_whenScheduleTaskExists_updateTaskResult() {
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("1");
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(result));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        oscService.swapTable(1L);

        ScheduleTaskEntity updatedScheduleTask = scheduleTaskRepository.findById(1L).get();
        OnlineSchemaChangeScheduleTaskResult updatedResult = JsonUtils.fromJson(updatedScheduleTask.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class);

        assertFalse(updatedResult.isManualSwapTableEnabled());
        assertTrue(updatedResult.isManualSwapTableStarted());
    }

    @Test
    public void testGetLockUserIsRequired_whenConnectionDoesNotExist_returnFalse() {
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(null);

        boolean lockUserRequired = oscService.getLockUserIsRequired(1L);

        assertFalse(lockUserRequired);
    }

    @Test
    public void testGetLockUserIsRequired_whenConnectionExists_returnTrue() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");

        ConnectionSessionFactory factory = mock(ConnectionSessionFactory.class);
        ConnectionSession connSession = mock(ConnectionSession.class);
        when(factory.generateSession()).thenReturn(connSession);
        when(connSession.expire()).thenReturn(null);

        ReflectionTestUtils.setField(oscService, "connectionSessionFactory", factory);

        boolean lockUserRequired = oscService.getLockUserIsRequired(1L);

        assertTrue(lockUserRequired);
    }

    @Test
    public void testGetLockUserIsRequired_whenConnectionExistsWithOtherDialect_returnFalse() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("PostgreSQL");

        ConnectionSessionFactory factory = mock(ConnectionSessionFactory.class);
        ConnectionSession connSession = mock(ConnectionSession.class);
        when(factory.generateSession()).thenReturn(connSession);
        when(connSession.expire()).thenReturn(null);

        ReflectionTestUtils.setField(oscService, "connectionSessionFactory", factory);

        boolean lockUserRequired = oscService.getLockUserIsRequired(1L);

        assertFalse(lockUserRequired);
    }

    @Test
    public void testGetLockUserIsRequired_whenConnectionExistsWithException_returnFalse() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");

        ConnectionSessionFactory factory = mock(ConnectionSessionFactory.class);
        ConnectionSession connSession = mock(ConnectionSession.class);
        when(factory.generateSession()).thenReturn(connSession);
        when(connSession.expire()).thenThrow(new RuntimeException("Test exception"));

        ReflectionTestUtils.setField(oscService, "connectionSessionFactory", factory);

        boolean lockUserRequired = oscService.getLockUserIsRequired(1L);

        assertFalse(lockUserRequired);
    }

}