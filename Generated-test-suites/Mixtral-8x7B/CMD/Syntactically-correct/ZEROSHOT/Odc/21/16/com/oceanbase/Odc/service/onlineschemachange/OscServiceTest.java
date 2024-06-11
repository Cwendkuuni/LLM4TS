import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.schedule.model.JobType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

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
    public void setup() {
        ReflectionTestUtils.setField(oscService, "odcDbUserUtil", new OscDBUserUtil());
    }

    @Test
    public void testGetOscDatabaseInfo_nonexistentDatabase_returnsEmptyOscDatabaseInfo() {
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);

        Assert.assertEquals(0, oscDatabase.getDatabaseId());
        Assert.assertFalse(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfo_existingDatabase_returnsOscDatabaseInfo() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        databaseEntity.setConnectionId(2L);

        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(databaseEntity));
        when(oscService.getLockUserIsRequired(anyLong())).thenReturn(true);

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);

        Assert.assertEquals(1, oscDatabase.getDatabaseId());
        Assert.assertTrue(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test(expected = RuntimeException.class)
    public void testSwapTable_nonexistentScheduleTask_throwsRuntimeException() {
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        oscService.swapTable(1L);
    }

    @Test
    public void testSwapTable_nonexistentSchedule_returnsOscSwapTableVO() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName("1000");

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.empty());

        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);

        Assert.assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
    }

    @Test
    public void testSwapTable_existingSchedule_returnsOscSwapTableVO() {
        Long scheduleTaskId = 1L;
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        OnlineSchemaChangeScheduleTaskResult result =
                new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName("1000");
        scheduleTask.setResultJson(JsonUtils.toJson(result));

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);

        Assert.assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
    }

    @Test(expected = RuntimeException.class)
    public void testGetLockUserIsRequired_nonexistentConnection_throwsRuntimeException() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong()))
                .thenReturn(connectionConfig);

        oscService.getLockUserIsRequired(1L);
    }

    @Test
    public void testGetLockUserIsRequired_existingConnection_returnsTrue() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");

        when(connectionService.getForConnectionSkipPermissionCheck(anyLong()))
                .thenReturn(connectionConfig);

        when(ConnectionSessionUtil.getVersion(any(ConnectionSession.class)))
                .thenReturn("5.7.33");

        boolean lockUserRequired = oscService.getLockUserIsRequired(1L);

        Assert.assertTrue(lockUserRequired);
    }

    @Test
    public void testGetLockUserIsRequired_existingConnectionWithUnsupportedVersion_returnsFalse() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");

        when(connectionService.getForConnectionSkipPermissionCheck(anyLong()))
                .thenReturn(connectionConfig);

        when(ConnectionSessionUtil.getVersion(any(ConnectionSession.class)))
                .thenReturn("10.4.21-MariaDB");

        boolean lockUserRequired = oscService.getLockUserIsRequired(1L);

        Assert.assertFalse(lockUserRequired);
    }

    @Test
    public void testGetLockUserIsRequired_existingConnectionWithNoVersion_returnsTrue() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");

        when(connectionService.getForConnectionSkipPermissionCheck(anyLong()))
                .thenReturn(connectionConfig);

        when(ConnectionSessionUtil.getVersion(any(ConnectionSession.class)))
                .thenThrow(new RuntimeException("Get connection version failed"));

        boolean lockUserRequired = oscService.getLockUserIsRequired(1L);

        Assert.assertTrue(lockUserRequired);
    }
}