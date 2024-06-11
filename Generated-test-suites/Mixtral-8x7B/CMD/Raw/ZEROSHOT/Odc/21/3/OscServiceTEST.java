import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;

@SuppressWarnings("unused")
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
        MockitoAnnotations.initMocks(this);

        ReflectionTestUtils.setField(oscService, "lockUserRequiredVersion", "10.0.0");
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        Optional<DatabaseEntity> databaseOptional = Optional.of(new DatabaseEntity());
        when(databaseRepository.findById(id)).thenReturn(databaseOptional);

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        assertEquals(id, result.getDatabaseId());
        assertTrue(result.isLockDatabaseUserRequired());

        verify(databaseRepository, times(1)).findById(id);
    }

    @Test
    public void testGetOscDatabaseInfo_databaseNotFound() {
        Long id = 1L;
        when(databaseRepository.findById(id)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        assertNull(result.getDatabaseId());
        assertFalse(result.isLockDatabaseUserRequired());

        verify(databaseRepository, times(1)).findById(id);
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName("1");
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        result.setManualSwapTableStarted(false);
        String resultJson = JsonUtils.toJson(result);
        scheduleTask.setResultJson(resultJson);

        Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(scheduleTask);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(scheduleTaskOptional);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        Optional<ScheduleEntity> scheduleEntityOptional = Optional.of(scheduleEntity);
        when(scheduleRepository.findById(anyLong())).thenReturn(scheduleEntityOptional);

        when(scheduleTaskRepository.updateTaskResult(scheduleTaskId, any())).thenReturn(null);

        OscSwapTableVO resultVO = oscService.swapTable(scheduleTaskId);

        assertEquals(scheduleTaskId, resultVO.getScheduleTaskId());

        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
        verify(scheduleRepository, times(1)).findById(anyLong());
        verify(scheduleTaskRepository, times(1)).updateTaskResult(scheduleTaskId, any());
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
                .thenReturn(connectionConfig);

        String version = "8.0.0";
        when(ConnectionSessionUtil.getVersion(any())).thenReturn(version);

        boolean result =
                ((OscService) ReflectionTestUtils.getField(oscService, "oscService"))
                        .getLockUserIsRequired(connectionId);

        assertFalse(result);

        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(connectionId);
        verify(ConnectionSessionUtil, times(1)).getVersion(any());
    }

    @Test
    public void testGetLockUserIsRequired_lockUserRequired() {
        Long connectionId = 1L;
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("Oracle");

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
                .thenReturn(connectionConfig);

        String version = "11.0.0";
        when(ConnectionSessionUtil.getVersion(any())).thenReturn(version);

        boolean result =
                ((OscService) ReflectionTestUtils.getField(oscService, "oscService"))
                        .getLockUserIsRequired(connectionId);

        assertTrue(result);

        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(connectionId);
        verify(ConnectionSessionUtil, times(1)).getVersion(any());
    }

}