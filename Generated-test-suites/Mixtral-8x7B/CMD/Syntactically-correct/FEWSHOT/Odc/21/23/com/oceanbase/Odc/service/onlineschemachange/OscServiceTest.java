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
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;

@Transactional
@SkipAuthorize("internal authenticated")
class OscServiceTest {

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
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo_whenDatabaseNotPresent_returnsEmptyOscLockDatabaseUserInfo() {
        Long id = 1L;
        when(databaseRepository.findById(id)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        assertEquals(0, result.getDatabaseId());
        assertFalse(result.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfo_whenDatabasePresent_returnsOscLockDatabaseUserInfo() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        databaseEntity.setConnectionId(2L);

        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));
        when(oscService.getLockUserIsRequired(anyLong())).thenReturn(true);

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        assertEquals(id, result.getDatabaseId());
        assertTrue(result.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable_whenScheduleTaskNotPresent_throwsIllegalArgumentException() {
        Long scheduleTaskId = 1L;
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.empty());

        try {
            oscService.swapTable(scheduleTaskId);
        } catch (IllegalArgumentException e) {
            assertEquals(ErrorCodes.BadArgument, e.getMessage());
        }
    }

    @Test
    public void testSwapTable_whenScheduleTaskPresent_performsSwapTableOperation() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName("1");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson("{\"swapTableType\":\"MANUAL\"}");

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        ScheduleTaskEntity.Result result = new ScheduleTaskEntity.Result();
        result.setManualSwapTableEnabled(true);
        result.setManualSwapTableStarted(false);

        OnlineSchemaChangeScheduleTaskResult scheduleTaskResult = new OnlineSchemaChangeScheduleTaskResult();
        scheduleTaskResult.setManualSwapTableEnabled(false);
        scheduleTaskResult.setManualSwapTableStarted(true);

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
        when(JsonUtils.fromJson(anyString(), OnlineSchemaChangeParameters.class))
                .thenReturn(oscParameters);
        when(JsonUtils.fromJson(anyString(), OnlineSchemaChangeScheduleTaskResult.class))
                .thenReturn(scheduleTaskResult);

        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);

        assertEquals(scheduleTaskId, result.getScheduleTaskId());
    }

    @Test
    public void testGetLockUserIsRequired_whenConnectionServiceThrowsException_returnsFalse() {
        Long connectionId = 1L;
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenThrow(new RuntimeException());

        boolean result = oscService.getLockUserIsRequired(connectionId);

        assertFalse(result);
    }
}