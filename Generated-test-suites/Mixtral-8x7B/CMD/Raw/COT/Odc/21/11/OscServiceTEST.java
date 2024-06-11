import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.connection.DatabaseType;
import com.oceanbase.odc.core.connection.model.ConnectionConfig;
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
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;
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

    private ObjectMapper objectMapper;

    @Before
    public void init() {
        objectMapper = new ObjectMapper();
        ReflectionTestUtils.setField(oscService, "lockUserRequiredVersion",
                new String[]{"8.0.20", "8.0.21", "10.1.11", "10.2.1"});
    }

    // Test getOscDatabaseInfo()
    @Test
    public void testGetOscDatabaseInfo() {
        DatabaseEntity database = new DatabaseEntity();
        database.setDatabaseId(1L);
        database.setConnectionId(2L);

        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(database));

        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(1L);
        Assert.assertNotNull(oscDatabaseInfo);
        Assert.assertEquals(1L, oscDatabaseInfo.getDatabaseId());
    }

    @Test
    public void testGetOscDatabaseInfo_notExists() {
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(1L);
        Assert.assertNotNull(oscDatabaseInfo);
        Assert.assertEquals(-1L, oscDatabaseInfo.getDatabaseId());
    }

    // Test swapTable()
    @Test
    public void testSwapTable() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setJobName("1");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
        when(JsonUtils.fromJson(any(String.class), any(TypeReference.class)))
                .thenReturn(oscParameters, result);

        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);
        Assert.assertNotNull(oscSwapTable);
        Assert.assertEquals(1L, oscSwapTable.getScheduleTaskId());
    }

    @Test
    public void testSwapTable_notExists() {
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);
        Assert.assertNull(oscSwapTable);
    }

    @Test
    public void testSwapTable_jobTypeNotMatch() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setJobName("1");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobType(JobType.MANUAL);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);
        Assert.assertNull(oscSwapTable);
    }

    @Test
    public void testSwapTable_swapTableTypeNotMatch() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setJobName("1");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.AUTOMATIC);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
        when(JsonUtils.fromJson(any(String.class), any(TypeReference.class)))
                .thenReturn(oscParameters, result);

        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);
        Assert.assertNull(oscSwapTable);
    }

    @Test
    public void testSwapTable_manualSwapTableNotEnabled() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setJobName("1");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(anyLong()))
                .thenReturn(Optional.of(scheduleEntity), Optional.of(scheduleEntity));
        when(JsonUtils.fromJson(any(String.class), any(TypeReference.class)))
                .thenReturn(oscParameters, result);

        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);
        Assert.assertNull(oscSwapTable);
    }

    @Test
    public void testSwapTable_getLockUserIsRequired_true() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setJobName("1");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DatabaseType.MYSQL);
        ReflectionTestUtils.setField(oscService, "lockUserRequiredVersion",
                new String[]{"8.0.20", "8.0.21", "10.1.11"});

        ConnectionSessionFactory factory = Mockito.mock(ConnectionSessionFactory.class);
        when(factory.generateSession()).thenReturn(Mockito.mock(ConnectionSession.class));

        when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(connectionConfig);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
        when(JsonUtils.fromJson(any(String.class), any(TypeReference.class)))
                .thenReturn(oscParameters, result);

        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);
        Assert.assertNotNull(oscSwapTable);
        Assert.assertEquals(1L, oscSwapTable.getScheduleTaskId());
    }

    @Test
    public void testSwapTable_getLockUserIsRequired_false() {
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setJobName("1");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DatabaseType.MYSQL);
        ReflectionTestUtils.setField(oscService, "lockUserRequiredVersion", new String[0]);

        ConnectionSessionFactory factory = Mockito.mock(ConnectionSessionFactory.class);
        when(factory.generateSession()).thenReturn(Mockito.mock(ConnectionSession.class));

        when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(connectionConfig);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(anyLong()))
                .thenReturn(Optional.of(scheduleEntity), Optional.of(scheduleEntity));
        when(JsonUtils.fromJson(any(String.class), any(TypeReference.class)))
                .thenReturn(oscParameters, result);

        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);
        Assert.assertNull(oscSwapTable);
    }

    // Helper method to create DatabaseEntity
    private DatabaseEntity createDatabaseEntity(long id, long connectionId) {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        databaseEntity.setConnectionId(connectionId);
        return databaseEntity;
    }

}