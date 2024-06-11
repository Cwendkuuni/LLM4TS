package com.oceanbase.odc.service.onlineschemachange;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ConnectionService connectionService;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @InjectMocks
    private OscService oscService;

    private static final Long DATABASE_ID = 1L;
    private static final Long CONNECTION_ID = 2L;
    private static final Long SCHEDULE_TASK_ID = 3L;
    private static final Long SCHEDULE_ID = 4L;

    private static final String JOB_NAME = "jobName";
    private static final String JOB_PARAMETERS_JSON = "{\"key\": \"value\"}";

    private static final OnlineSchemaChangeParameters ONLINE_SCHEMA_CHANGE_PARAMETERS = new OnlineSchemaChangeParameters();
    private static final OnlineSchemaChangeScheduleTaskResult ONLINE_SCHEMA_CHANGE_SCHEDULE_TASK_RESULT = new OnlineSchemaChangeScheduleTaskResult();
    private static final OscLockDatabaseUserInfo OSC_LOCK_DATABASE_USER_INFO = new OscLockDatabaseUserInfo();
    private static final OscSwapTableVO OSC_SWAP_TABLE_VO = new OscSwapTableVO();

    private static final DatabaseEntity DATABASE_ENTITY = new DatabaseEntity();
    private static final ScheduleEntity SCHEDULE_ENTITY = new ScheduleEntity();
    private static final ScheduleTaskEntity SCHEDULE_TASK_ENTITY = new ScheduleTaskEntity();

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(oscService, "objectMapper", new ObjectMapper());

        DATABASE_ENTITY.setDatabaseId(DATABASE_ID);
        DATABASE_ENTITY.setConnectionId(CONNECTION_ID);

        SCHEDULE_ENTITY.setJobName(JOB_NAME);
        SCHEDULE_ENTITY.setJobParametersJson(JOB_PARAMETERS_JSON);
        SCHEDULE_ENTITY.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        SCHEDULE_TASK_ENTITY.setId(SCHEDULE_TASK_ID);
        SCHEDULE_TASK_ENTITY.setJobName(SCHEDULE_ID.toString());
        SCHEDULE_TASK_ENTITY.setResultJson("{}");

        ONLINE_SCHEMA_CHANGE_PARAMETERS.setSwapTableType(SwapTableType.MANUAL);

        ONLINE_SCHEMA_CHANGE_SCHEDULE_TASK_RESULT.setManualSwapTableEnabled(true);
        ONLINE_SCHEMA_CHANGE_SCHEDULE_TASK_RESULT.setManualSwapTableStarted(false);

        OSC_LOCK_DATABASE_USER_INFO.setDatabaseId(DATABASE_ID);
        OSC_LOCK_DATABASE_USER_INFO.setLockDatabaseUserRequired(false);

        OSC_SWAP_TABLE_VO.setScheduleTaskId(SCHEDULE_TASK_ID);

        when(databaseRepository.findById(DATABASE_ID)).thenReturn(Optional.of(DATABASE_ENTITY));
        when(scheduleRepository.findById(SCHEDULE_ID)).thenReturn(Optional.of(SCHEDULE_ENTITY));
        when(scheduleTaskRepository.findById(SCHEDULE_TASK_ID)).thenReturn(Optional.of(SCHEDULE_TASK_ENTITY));
        when(connectionService.getForConnectionSkipPermissionCheck(CONNECTION_ID))
                .thenReturn(mock(ConnectionConfig.class));
        doNothing().when(scheduleTaskRepository).updateTaskResult(anyLong(), any());
    }

    @Test
    public void testGetOscDatabaseInfo() {
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(DATABASE_ID);
        assertEquals(DATABASE_ID, oscDatabase.getDatabaseId());
        assertFalse(oscDatabase.isLockDatabaseUserRequired());

        when(databaseRepository.findById(DATABASE_ID)).thenReturn(Optional.empty());
        oscDatabase = oscService.getOscDatabaseInfo(DATABASE_ID);
        assertNull(oscDatabase);
    }

    @Test
    public void testSwapTable() {
        OscSwapTableVO oscSwapTable = oscService.swapTable(SCHEDULE_TASK_ID);
        assertEquals(SCHEDULE_TASK_ID, oscSwapTable.getScheduleTaskId());

        when(scheduleTaskRepository.findById(SCHEDULE_TASK_ID)).thenReturn(Optional.empty());
        oscSwapTable = oscService.swapTable(SCHEDULE_TASK_ID);
        assertNull(oscSwapTable);

        when(scheduleEntity.getJobType()).thenReturn(JobType.ONLINE_SCHEMA_CHANGE_IN_PROGRESS);
        oscSwapTable = oscService.swapTable(SCHEDULE_TASK_ID);
        assertNull(oscSwapTable);

        when(ONLINE_SCHEMA_CHANGE_SCHEDULE_TASK_RESULT.isManualSwapTableEnabled()).thenReturn(false);
        when(scheduleTaskRepository.findById(SCHEDULE_TASK_ID))
                .thenReturn(Optional.of(SCHEDULE_TASK_ENTITY));
        oscSwapTable = oscService.swapTable(SCHEDULE_TASK_ID);
        assertNull(oscSwapTable);

        when(ONLINE_SCHEMA_CHANGE_PARAMETERS.getSwapTableType()).thenReturn(SwapTableType.AUTO);
        when(scheduleTaskRepository.findById(SCHEDULE_TASK_ID))
                .thenReturn(Optional.of(SCHEDULE_TASK_ENTITY));
        oscSwapTable = oscService.swapTable(SCHEDULE_TASK_ID);
        assertNull(oscSwapTable);

        when(ONLINE_SCHEMA_CHANGE_SCHEDULE_TASK_RESULT.isManualSwapTableEnabled()).thenReturn(true);
        when(scheduleTaskRepository.findById(SCHEDULE_TASK_ID))
                .thenReturn(Optional.of(SCHEDULE_TASK_ENTITY));
        oscSwapTable = oscService.swapTable(SCHEDULE_TASK_ID);
        assertEquals(SCHEDULE_TASK_ID, oscSwapTable.getScheduleTaskId());
    }

    @Test(expected = Exception.class)
    public void testSwapTable_transactionalRollback() {
        when(ONLINE_SCHEMA_CHANGE_SCHEDULE_TASK_RESULT.isManualSwapTableEnabled()).thenReturn(true);
        when(scheduleTaskRepository.findById(SCHEDULE_TASK_ID))
                .thenReturn(Optional.of(SCHEDULE_TASK_ENTITY));

        oscService.swapTable(SCHEDULE_TASK_ID);
    }

    @Test
    public void testGetLockUserIsRequired() {
        when(connectionService.getForConnectionSkipPermissionCheck(CONNECTION_ID))
                .thenReturn(mock(ConnectionConfig.class));

        boolean result = ((OscService) ReflectionTestUtils.setField(oscService, "skipAuthorize", new SkipAuthorize() {
            @Override
            public boolean isSkip() {
                return false;
            }
        })).getLockUserIsRequired(CONNECTION_ID);
        assertFalse(result);

        when(connectionService.getForConnectionSkipPermissionCheck(CONNECTION_ID))
                .thenThrow(new RuntimeException());

        result = ((OscService) ReflectionTestUtils.setField(oscService, "skipAuthorize", new SkipAuthorize() {
            @Override
            public boolean isSkip() {
                return false;
            }
        })).getLockUserIsRequired(CONNECTION_ID);
        assertFalse(result);
    }
}