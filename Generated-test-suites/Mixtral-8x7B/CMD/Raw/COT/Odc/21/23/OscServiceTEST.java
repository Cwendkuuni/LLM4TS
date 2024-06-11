
package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.connection.ConnectionConfig;
import com.oceanbase.odc.core.connection.DatabaseEntity;
import com.oceanbase.odc.core.connection.DatabaseRepository;
import com.oceanbase.odc.core.connection.model.ConnectionConfigBuilder;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.connection.factory.DefaultConnectSessionFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;

@RunWith(MockitoJUnitRunner.class)
public class OscServiceTest {

    @InjectMocks
    private OscService oscService;

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

    @Before
    public void setup() {
        ReflectionTestUtils.setField(oscService, "oscDBUserUtil", mock(OscDBUserUtil.class));
        PreConditions.setInstance(mock(PreConditions.class));
        JsonUtils.setInstance(mock(JsonUtils.class));
    }

    @Test
    public void testGetOscDatabaseInfo_nonexistentDatabase() {
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);

        assertNull(oscDatabase.getDatabaseId());
        assertFalse(oscDatabase.isLockDatabaseUserRequired());

        verify(databaseRepository, times(1)).findById(anyLong());
        verify(connectionService, times(0)).getForConnectionSkipPermissionCheck(anyLong());
    }

    @Test
    public void testGetOscDatabaseInfo_existingDatabase() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setConnectionId(1L);
        databaseEntity.setDatabaseId(1L);

        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(databaseEntity));
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong()))
                .thenReturn(new ConnectionConfigBuilder().build());

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);

        assertEquals(1L, (long) oscDatabase.getDatabaseId());
        assertTrue(oscDatabase.isLockDatabaseUserRequired());

        verify(databaseRepository, times(1)).findById(anyLong());
        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(anyLong());
    }

    @Test
    public void testSwapTable_nonexistentScheduleTask() {
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);

        assertNull(oscSwapTable);

        verify(scheduleTaskRepository, times(1)).findById(anyLong());
        verify(connectionService, times(0)).getForConnectionSkipPermissionCheck(anyLong());
    }

    @Test
    public void testSwapTable_nonexistentSchedule() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setJobName("1");

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.empty());

        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);

        assertNull(oscSwapTable);

        verify(scheduleTaskRepository, times(1)).findById(anyLong());
        verify(scheduleRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testSwapTable_invalidJobType() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setJobName("1");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.DDL);

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
        when(JsonUtils.fromJson(anyString(), any()))
                .thenReturn(oscParameters, result);

        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);

        assertNull(oscSwapTable);

        verify(scheduleTaskRepository, times(1)).findById(anyLong());
        verify(scheduleRepository, times(1)).findById(anyLong());
        verify(JsonUtils, times(2)).fromJson(anyString(), any());
    }

    @Test
    public void testSwapTable_invalidSwapTableType() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setJobName("1");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.AUTOMATIC);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
        when(JsonUtils.fromJson(anyString(), any()))
                .thenReturn(oscParameters, result);

        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);

        assertNull(oscSwapTable);

        verify(scheduleTaskRepository, times(1)).findById(anyLong());
        verify(scheduleRepository, times(1)).findById(anyLong());
        verify(JsonUtils, times(2)).fromJson(anyString(), any());
    }

    @Test
    public void testSwapTable_swapTableNotEnabled() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setJobName("1");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
        when(JsonUtils.fromJson(anyString(), any()))
                .thenReturn(oscParameters, result);

        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);

        assertNull(oscSwapTable);

        verify(scheduleTaskRepository, times(1)).findById(anyLong());
        verify(scheduleRepository, times(1)).findById(anyLong());
        verify(JsonUtils, times(2)).fromJson(anyString(), any());
    }

    @Test
    public void testSwapTable_success() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setJobName("1");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        OscSwapTableVO oscSwapTable = new OscSwapTableVO();
        oscSwapTable.setScheduleTaskId(1L);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
        when(JsonUtils.fromJson(anyString(), any()))
                .thenReturn(oscParameters, result);
        when(scheduleTaskRepository.updateTaskResult(anyLong(), anyString()))
                .thenReturn(result);

        OscSwapTableVO resultOscSwapTable = oscService.swapTable(1L);

        assertEquals(oscSwapTable, resultOscSwapTable);

        verify(scheduleTaskRepository, times(1)).findById(anyLong());
        verify(scheduleRepository, times(1)).findById(anyLong());
        verify(JsonUtils, times(2)).fromJson(anyString(), any());
        verify(scheduleTaskRepository, times(1)).updateTaskResult(anyLong(), anyString());
    }

    @Test
    public void testLockUserIsRequired_exception() {
        ConnectionConfig connectionConfig = new ConnectionConfigBuilder().build();
        ConnectionSessionFactory factory = new DefaultConnectSessionFactory(connectionConfig);
        ConnectionSessionUtil.setInstance(factory);
        ConnectionSessionUtil spyConnectionSessionUtil = spy(ConnectionSessionUtil.class);
        ReflectionTestUtils.setField(oscService, "connectionSessionUtil", spyConnectionSessionUtil);

        when(spyConnectionSessionUtil.getVersion(any(ConnectionSession.class)))
                .thenThrow(new RuntimeException());

        boolean isLockUserRequired = oscService.getLockUserIsRequired(1L);

        assertFalse(isLockUserRequired);

        verify(spyConnectionSessionUtil, times(1)).getVersion(any(ConnectionSession.class));
    }

    @Test
    public void testLockUserIsRequired_success() {
        ConnectionConfig connectionConfig = new ConnectionConfigBuilder().build();
        connectionConfig.setDialectType("mysql");
        ConnectionSessionFactory factory = new DefaultConnectSessionFactory(connectionConfig);
        ConnectionSessionUtil.setInstance(factory);
        ConnectionSessionUtil spyConnectionSessionUtil = spy(ConnectionSessionUtil.class);
        ReflectionTestUtils.setField(oscService, "connectionSessionUtil", spyConnectionSessionUtil);

        when(spyConnectionSessionUtil.getVersion(any(ConnectionSession.class))).thenReturn("5.7");

        boolean isLockUserRequired = oscService.getLockUserIsRequired(1L);

        assertTrue(isLockUserRequired);

        verify(spyConnectionSessionUtil, times(1)).getVersion(any(ConnectionSession.class));
    }
}
