import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
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
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        databaseEntity.setConnectionId(1L);

        Optional<DatabaseEntity> database = Optional.of(databaseEntity);
        when(databaseRepository.findById(id)).thenReturn(database);

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);
        Assert.assertEquals(id, oscDatabase.getDatabaseId());
        Assert.assertTrue(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfoNonExistingDatabase() {
        Long id = 1L;
        when(databaseRepository.findById(id)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);
        Assert.assertNull(oscDatabase.getDatabaseId());
        Assert.assertFalse(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(scheduleTaskId);
        scheduleTaskEntity.setJobName("1");
        scheduleTaskEntity.setResultJson("{}");

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        result.setManualSwapTableStarted(false);

        Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(scheduleTaskEntity);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(scheduleTaskOptional);

        Optional<ScheduleEntity> scheduleEntityOptional = Optional.of(scheduleEntity);
        when(scheduleRepository.findById(anyLong())).thenReturn(scheduleEntityOptional);

        when(scheduleTaskRepository.updateTaskResult(anyLong(), any())).thenReturn(null);

        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);
        Assert.assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
    }

    @Test(expected = ErrorCodes.BadArgument.class)
    public void testSwapTableInvalidSwapTableType() {
        Long scheduleTaskId = 1L;

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.AUTO);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();

        Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(new ScheduleTaskEntity());
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(scheduleTaskOptional);

        Optional<ScheduleEntity> scheduleEntityOptional = Optional.of(scheduleEntity);
        when(scheduleRepository.findById(anyLong())).thenReturn(scheduleEntityOptional);

        oscService.swapTable(scheduleTaskId);
    }

    @Test(expected = ErrorCodes.BadArgument.class)
    public void testSwapTableInvalidTaskType() {
        Long scheduleTaskId = 1L;

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.MANUAL_SWAP_TABLE_DISABLE);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();

        Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(new ScheduleTaskEntity());
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(scheduleTaskOptional);

        Optional<ScheduleEntity> scheduleEntityOptional = Optional.of(scheduleEntity);
        when(scheduleRepository.findById(anyLong())).thenReturn(scheduleEntityOptional);

        oscService.swapTable(scheduleTaskId);
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("mysql");

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);

        when(connectionService.generateSession(decryptedConnConfig)).thenReturn(connectionSession);

        boolean isRequired = oscService.getLockUserIsRequired(connectionId);
        Assert.assertTrue(isRequired);
    }

    @Test
    public void testGetLockUserIsRequiredNotRequired() {
        Long connectionId = 1L;
        ConnectionConfig decryptedConnConfig = new ConnectionConfig();
        decryptedConnConfig.setDialectType("postgresql");

        when(connectionService.getForConnectionSkipPermissionCheck(connectionId)).thenReturn(decryptedConnConfig);

        when(connectionService.generateSession(decryptedConnConfig)).thenReturn(connectionSession);

        boolean isRequired = oscService.getLockUserIsRequired(connectionId);
        Assert.assertFalse(isRequired);
    }

    private ConnectionSession connectionSession = new ConnectionSession() {
        @Override
        public String getVersion() {
            return "5.7.12";
        }

        @Override
        public boolean isExpired() {
            return true;
        }

        @Override
        public void expire() {
        }

        @Override
        public String getUsername() {
            return null;
        }

        @Override
        public String getHostname() {
            return null;
        }

        @Override
        public String getPort() {
            return null;
        }

        @Override
        public String getDatabase() {
            return null;
        }

        @Override
        public boolean isClosed() {
            return false;
        }

        @Override
        public void close() {
        }

        @Override
        public void closeQuietly() {
        }
    };
}