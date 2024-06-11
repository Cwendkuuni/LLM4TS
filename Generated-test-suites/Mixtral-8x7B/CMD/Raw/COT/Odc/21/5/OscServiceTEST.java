import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.SwapTableType;
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
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(oscService, "jsonUtils", mock(JsonUtils.class));
    }

    @Test
    public void testGetOscDatabaseInfo() {

        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setConnectionId(2L);
        databaseEntity.setDatabaseId(id);

        given(databaseRepository.findById(id)).willReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);

        assertEquals(id, oscDatabase.getDatabaseId());
        assertTrue(oscDatabase.isLockDatabaseUserRequired());

        // Test for non-existing database
        given(databaseRepository.findById(id)).willReturn(Optional.empty());
        assertNull(oscService.getOscDatabaseInfo(id));
    }

    @Test
    public void testSwapTable() throws Exception {

        Long scheduleTaskId = 1L;
        Long scheduleId = 2L;
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName(String.valueOf(scheduleId));
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        result.setManualSwapTableStarted(true);

        ScheduleTaskEntity updatedScheduleTask = new ScheduleTaskEntity();
        updatedScheduleTask.setResultJson(JsonUtils.toJson(result));

        given(scheduleTaskRepository.findById(scheduleTaskId)).willReturn(Optional.of(scheduleTask));
        given(scheduleRepository.findById(scheduleId)).willReturn(Optional.of(scheduleEntity));
        given(scheduleTaskRepository.updateTaskResult(scheduleTaskId, any())).willReturn(updatedScheduleTask);

        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);

        assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());

        // Test for non-existing schedule task
        given(scheduleTaskRepository.findById(scheduleTaskId)).willReturn(Optional.empty());
        assertNull(oscService.swapTable(scheduleTaskId));

        // Test for invalid job type
        scheduleEntity.setJobType(JobType.SCHEMA_COMPARE);
        assertThrows(IllegalArgumentException.class, () -> oscService.swapTable(scheduleTaskId));

        // Test for manual swap table disabled
        result.setManualSwapTableEnabled(false);
        assertThrows(IllegalStateException.class, () -> oscService.swapTable(scheduleTaskId));

    }

    @Test
    public void testGetLockUserIsRequired() {

        Long connectionId = 1L;
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");

        ConnectionSessionFactory factory = mock(ConnectionSessionFactory.class);
        ConnectionSession session = mock(ConnectionSession.class);

        given(connectionService.getForConnectionSkipPermissionCheck(connectionId)).willReturn(connectionConfig);
        given(factory.generateSession()).willReturn(session);
        given(connectionService.getConnectionSessionFactory(connectionConfig)).willReturn(factory);

        boolean isLockUserRequired = oscService.getLockUserIsRequired(connectionId);

        assertTrue(isLockUserRequired);

        // Test for exception during connection
        given(session.expire()).willThrow(new RuntimeException("test exception"));
        assertFalse(oscService.getLockUserIsRequired(connectionId));
        verify(session, times(1)).expire();

        // Test for non-supported dialect
        connectionConfig.setDialectType("non-supported");
        isLockUserRequired = oscService.getLockUserIsRequired(connectionId);
        assertFalse(isLockUserRequired);
        verify(connectionService, never()).getConnectionSessionFactory(connectionConfig);
    }
}