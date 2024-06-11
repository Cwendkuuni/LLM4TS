package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
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
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

@Transactional
public class OscServiceTest {

    private OscService oscService;

    private DatabaseRepository databaseRepository;
    private ConnectionService connectionService;
    private ScheduleTaskRepository scheduleTaskRepository;
    private ScheduleRepository scheduleRepository;

    @Before
    public void setUp() {
        databaseRepository = mock(DatabaseRepository.class);
        connectionService = mock(ConnectionService.class);
        scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        scheduleRepository = mock(ScheduleRepository.class);

        oscService = new OscService();
        oscService.databaseRepository = databaseRepository;
        oscService.connectionService = connectionService;
        oscService.scheduleTaskRepository = scheduleTaskRepository;
        oscService.scheduleRepository = scheduleRepository;
    }

    @Test
    public void testGetOscDatabaseInfo_whenIdNotPresent_shouldReturnEmptyOscLockDatabaseUserInfo() {
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);

        assertEquals(0, oscDatabase.getDatabaseId());
        assertFalse(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfo_whenIdPresent_shouldReturnOscLockDatabaseUserInfo() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        databaseEntity.setConnectionId(2L);

        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);

        assertEquals(1, oscDatabase.getDatabaseId());
        assertEquals(2, (long) oscDatabase.getConnectionId());
        assertTrue(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable_whenScheduleTaskIdNotPresent_shouldThrowException() {
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            oscService.swapTable(1L);
            fail("Expected Exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("schedule task with id 1 not found", e.getMessage());
        }
    }

    @Test
    public void testSwapTable_whenScheduleTaskPresent_shouldDisableManualSwapTable() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setJobName("1");

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTask));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(new OnlineSchemaChangeParameters()));

        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        result.setManualSwapTableStarted(false);

        when(scheduleTaskRepository.getTaskResult(anyLong())).thenReturn(result);

        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);

        assertTrue(oscSwapTable.isSwapped());
        assertTrue(result.isManualSwapTableStarted());
        assertFalse(result.isManualSwapTableEnabled());
    }
}