
package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

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
import com.oceanbase.odc.service.onlineschemachange.exception.OscServiceException;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

@SuppressWarnings("unchecked")
public class OscServiceTest {

    private OscService oscService;

    private DatabaseRepository databaseRepository;
    private ConnectionService connectionService;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ScheduleRepository scheduleRepository;

    @Before
    public void setup() {
        databaseRepository = mock(DatabaseRepository.class);
        connectionService = mock(ConnectionService.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        scheduleRepository = mock(ScheduleRepository.class);

        ReflectionTestUtils.setField(oscService, "databaseRepository", databaseRepository);
        ReflectionTestUtils.setField(oscService, "connectionService", connectionService);
        ReflectionTestUtils.setField(oscService, "scheduleTaskRepository", scheduleTaskRepository);
        ReflectionTestUtils.setField(oscService, "scheduleRepository", scheduleRepository);

        oscService = new OscService();
        ReflectionTestUtils.setField(oscService, "getLockUserIsRequired",
                oscService.getLockUserIsRequired(anyLong()));
    }

    @Test
    public void testGetOscDatabaseInfo_whenDatabaseNotFound() {
        given(databaseRepository.findById(anyLong())).willReturn(Optional.empty());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);

        assertEquals(0, result.getDatabaseId());
        assertFalse(result.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfo_whenDatabaseFound() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        databaseEntity.setConnectionId(2L);

        given(databaseRepository.findById(anyLong())).willReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);

        assertEquals(1, result.getDatabaseId());
        assertEquals(2, result.getConnectionId());
        assertTrue(result.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable_whenScheduleTaskIdNotFound() {
        given(scheduleTaskRepository.findById(anyLong())).willReturn(Optional.empty());

        Throwable exception = null;
        try {
            oscService.swapTable(1L);
        } catch (Throwable e) {
            exception = e;
        }
        verify(scheduleTaskRepository, never()).findById(anyLong());
        assertEquals(OscServiceException.class, exception.getClass());
        assertEquals(ErrorCodes.BadArgument, ((OscServiceException) exception).getErrorCode());
        assertEquals("schedule task", ((OscServiceException) exception).getArgs()[0]);
    }

    @Test
    public void testSwapTable_whenScheduleTaskIdFound() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("1");
        scheduleTaskEntity.setResultJson("{}");

        given(scheduleTaskRepository.findById(1L)).willReturn(Optional.of(scheduleTaskEntity));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(2L);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        given(scheduleRepository.findById(1L)).willReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        result.setManualSwapTableStarted(false);

        given(JsonUtils.fromJson(scheduleTaskEntity.getResultJson(),
                OnlineSchemaChangeScheduleTaskResult.class)).willReturn(result);

        oscService.swapTable(1L);

        verify(scheduleTaskRepository, times(1)).updateTaskResult(1L, "{\"manualSwapTableEnabled\":false," +
                "\"manualSwapTableStarted\":true}");
    }

    @Test
    @Transactional(rollbackFor = Exception.class)
    public void testGetLockUserIsRequired_whenConnectionNotFound() {
        given(connectionService.getForConnectionSkipPermissionCheck(2L)).willReturn(null);

        boolean result = oscService.getLockUserIsRequired(2L);

        assertFalse(result);
    }

    @Test
    public void testGetLockUserIsRequired_whenConnectionFound() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        ConnectionSessionFactory factory = mock(ConnectionSessionFactory.class);
        ConnectionSession session = mock(ConnectionSession.class);
        when(factory.generateSession()).thenReturn(session);
        ReflectionTestUtils.setField(connectionConfig, "dialectType", "mysql");
        ReflectionTestUtils.setField(connectionConfig, "connectionSessionFactory", factory);

        given(connectionService.getForConnectionSkipPermissionCheck(2L)).willReturn(connectionConfig);

        boolean result = oscService.getLockUserIsRequired(2L);

        assertTrue(result);
    }
}
