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

    private static final Long ID = 1L;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(oscService, "lockUserRequiredVersion", "10.2.1");
    }

    @Test
    public void testGetOscDatabaseInfo() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setConnectionId(ID);
        databaseEntity.setDatabaseId(ID);

        Optional<DatabaseEntity> optionalDatabaseEntity = Optional.of(databaseEntity);

        when(databaseRepository.findById(anyLong())).thenReturn(optionalDatabaseEntity);

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(ID);

        Assert.assertEquals(ID, result.getDatabaseId());
        Assert.assertEquals(true, result.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfoWithoutDatabase() {
        Optional<DatabaseEntity> optionalDatabaseEntity = Optional.empty();

        when(databaseRepository.findById(anyLong())).thenReturn(optionalDatabaseEntity);

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(ID);

        Assert.assertEquals(0L, result.getDatabaseId());
        Assert.assertEquals(false, result.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSwapTableType(SwapTableType.MANUAL);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(parameters));

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName(scheduleEntity.getScheduleId().toString());
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));

        Optional<ScheduleTaskEntity> optionalScheduleTaskEntity = Optional.of(scheduleTaskEntity);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(optionalScheduleTaskEntity);
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        OscSwapTableVO result = oscService.swapTable(ID);

        Assert.assertEquals(ID, result.getScheduleTaskId());
    }

    @Test
    public void testSwapTableWithInvalidScheduleTaskId() {
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        OscSwapTableVO result = oscService.swapTable(ID);

        Assert.assertNull(result);
    }

    @Test
    public void testSwapTableWithInvalidSchedule() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSwapTableType(SwapTableType.AUTOMATIC);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(parameters));

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName(scheduleEntity.getScheduleId().toString());
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));

        Optional<ScheduleTaskEntity> optionalScheduleTaskEntity = Optional.of(scheduleTaskEntity);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(optionalScheduleTaskEntity);
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        OscSwapTableVO result = oscService.swapTable(ID);

        Assert.assertNull(result);
    }

    @Test
    public void testSwapTableWithDisabledManualSwapTable() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSwapTableType(SwapTableType.MANUAL);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(parameters));

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName(scheduleEntity.getScheduleId().toString());
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(result));

        Optional<ScheduleTaskEntity> optionalScheduleTaskEntity = Optional.of(scheduleTaskEntity);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(optionalScheduleTaskEntity);
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        OscSwapTableVO oscSwapTable = oscService.swapTable(ID);

        Assert.assertNull(oscSwapTable);
    }

    @Test
    public void testGetLockUserIsRequired() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");
        connectionConfig.setVersion("8.0.21");

        when(connectionService.getForConnectionSkipPermissionCheck(anyLong()))
                .thenReturn(connectionConfig);

        boolean result = oscService.getLockUserIsRequired(ID);

        Assert.assertEquals(false, result);
    }

    @Test
    public void testGetLockUserIsRequiredWithLockUserRequiredVersion() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");
        connectionConfig.setVersion("10.2.1");

        when(connectionService.getForConnectionSkipPermissionCheck(anyLong()))
                .thenReturn(connectionConfig);

        boolean result = oscService.getLockUserIsRequired(ID);

        Assert.assertEquals(true, result);
    }

    @Test
    public void testGetLockUserIsRequiredWithNullVersion() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");
        connectionConfig.setVersion(null);

        when(connectionService.getForConnectionSkipPermissionCheck(anyLong()))
                .thenReturn(connectionConfig);

        boolean result = oscService.getLockUserIsRequired(ID);

        Assert.assertEquals(false, result);
    }

    @Test
    public void testGetLockUserIsRequiredWithDifferentDialectType() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("PostgreSQL");
        connectionConfig.setVersion("10.2.1");

        when(connectionService.getForConnectionSkipPermissionCheck(anyLong()))
                .thenReturn(connectionConfig);

        boolean result = oscService.getLockUserIsRequired(ID);

        Assert.assertEquals(false, result);
    }

    @Test(expected = NullPointerException.class)
    public void testGetLockUserIsRequiredWithNullConnectionConfig() {
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong()))
                .thenReturn(null);

        oscService.getLockUserIsRequired(ID);
    }
}