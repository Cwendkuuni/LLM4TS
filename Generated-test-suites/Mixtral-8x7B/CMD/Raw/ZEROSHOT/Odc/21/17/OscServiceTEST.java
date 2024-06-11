package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
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

    @InjectMocks
    private OscService oscService;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(oscService, "jsonUtils", new JsonUtils());
    }

    @Test
    public void testGetOscDatabaseInfo_nonExistentDatabase_returnsEmptyOscLockDatabaseUserInfo() {
        Long id = 1L;
        when(databaseRepository.findById(id)).thenReturn(Optional.empty());
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);
        assertEquals(0, oscDatabase.getDatabaseId());
        assertFalse(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfo_existentDatabaseWithLockUserRequired_returnsTrue() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        databaseEntity.setConnectionId(2L);
        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));
        when(oscService.getLockUserIsRequired(2L)).thenReturn(true);
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);
        assertEquals(id, oscDatabase.getDatabaseId());
        assertTrue(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfo_existentDatabaseWithoutLockUserRequired_returnsFalse() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        databaseEntity.setConnectionId(2L);
        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));
        when(oscService.getLockUserIsRequired(2L)).thenReturn(false);
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);
        assertEquals(id, oscDatabase.getDatabaseId());
        assertFalse(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable_nonExistentScheduleTask_throwsIllegalArgumentException() {
        Long scheduleTaskId = 1L;
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.empty());
        try {
            oscService.swapTable(scheduleTaskId);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertEquals(ErrorCodes.BadArgument, e.getErrorCode());
        }
    }

    @Test
    public void testSwapTable_nonExistentSchedule_throwsIllegalArgumentException() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setScheduleId(2L);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(2L)).thenReturn(Optional.empty());
        try {
            oscService.swapTable(scheduleTaskId);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertEquals(ErrorCodes.BadArgument, e.getErrorCode());
        }
    }

    @Test
    public void testSwapTable_existentScheduleWithInvalidJobType_throwsIllegalArgumentException() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setScheduleId(2L);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.UNKNOWN);
        when(scheduleRepository.findById(2L)).thenReturn(Optional.of(scheduleEntity));
        try {
            oscService.swapTable(scheduleTaskId);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertEquals(ErrorCodes.BadArgument, e.getErrorCode());
        }
    }

    @Test
    public void testSwapTable_existentScheduleWithInvalidSwapTableType_throwsIllegalArgumentException() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setScheduleId(2L);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.AUTOMATIC);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));
        when(scheduleRepository.findById(2L)).thenReturn(Optional.of(scheduleEntity));
        try {
            oscService.swapTable(scheduleTaskId);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertEquals(ErrorCodes.BadArgument, e.getErrorCode());
        }
    }

    @Test
    public void testSwapTable_existentScheduleWithDisabledManualSwapTable_throwsIllegalArgumentException() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setScheduleId(2L);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        oscParameters.setManualSwapTableEnabled(false);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));
        when(scheduleRepository.findById(2L)).thenReturn(Optional.of(scheduleEntity));
        try {
            oscService.swapTable(scheduleTaskId);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertEquals(ErrorCodes.BadArgument, e.getErrorCode());
        }
    }

    @Test
    public void testSwapTable_success() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setScheduleId(2L);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        oscParameters.setManualSwapTableEnabled(true);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        result.setManualSwapTableStarted(false);
        scheduleTask.setResultJson(JsonUtils.toJson(result));
        when(scheduleRepository.findById(2L)).thenReturn(Optional.of(scheduleEntity));
        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);
        assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
        assertNull(oscSwapTable.getTableName());
        assertNull(oscSwapTable.getSwapTableType());
    }

    @Test
    public void testGetLockUserIsRequired_nonExistentConnection_returnsFalse() {
        Long connectionId = 1L;
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
                .thenReturn(null);
        assertFalse(oscService.getLockUserIsRequired(connectionId));
    }

}

