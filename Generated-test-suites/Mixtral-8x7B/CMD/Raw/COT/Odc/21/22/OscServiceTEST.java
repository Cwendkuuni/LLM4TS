package com.oceanbase.odc.service.onlineschemachange;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

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

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(oscService, "objectMapper", new ObjectMapper());
    }

    @Test
    public void testGetOscDatabaseInfo_NoDatabaseFound() {
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.empty());
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);
        assertNull(oscDatabase);
    }

    @Test
    public void testGetOscDatabaseInfo_LockUserNotRequired() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setConnectionId(1L);

        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(databaseEntity));
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);
        assertFalse(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfo_LockUserRequired() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setConnectionId(1L);

        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(databaseEntity));
        when(oscService.getLockUserIsRequired(1L)).thenReturn(true);
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);
        assertTrue(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable_ScheduleTaskNotFound() {
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());
        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);
        assertNull(oscSwapTable);
    }

    @Test
    public void testSwapTable_InvalidScheduleTask() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setJobName("1");

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTask));
        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);
        assertNull(oscSwapTable);
    }

    @Test
    public void testSwapTable_InvalidSchedule() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setJobName("1");
        scheduleTask.setResultJson("{}");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_IN_PROGRESS);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);
        assertNull(oscSwapTable);
    }

    @Test
    public void testSwapTable_SwapTableNotEnabled() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setJobName("1");
        scheduleTask.setResultJson("{}");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
        when(oscService.getOnlineSchemaChangeScheduleTaskResult(scheduleTask)).thenReturn(result);

        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);
        assertNull(oscSwapTable);
    }

    @Test
    public void testSwapTable_Success() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setJobName("1");
        scheduleTask.setResultJson("{}");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
        when(oscService.getOnlineSchemaChangeParameters(scheduleEntity)).thenReturn(oscParameters);
        when(oscService.getOnlineSchemaChangeScheduleTaskResult(scheduleTask)).thenReturn(result);

        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);
        assertNotNull(oscSwapTable);
        assertEquals(1L, oscSwapTable.getScheduleTaskId());
    }

    private OnlineSchemaChangeParameters getOnlineSchemaChangeParameters(ScheduleEntity scheduleEntity) {
        return new OnlineSchemaChangeParameters();
    }

    private OnlineSchemaChangeScheduleTaskResult getOnlineSchemaChangeScheduleTaskResult(ScheduleTaskEntity scheduleTask) {
        return new OnlineSchemaChangeScheduleTaskResult();
    }

    private boolean getLockUserIsRequired(Long connectionId) {
        return false;
    }
}