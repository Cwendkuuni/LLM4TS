import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.*;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("test")
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

    private DatabaseEntity database;

    private ScheduleEntity schedule;

    private ScheduleTaskEntity scheduleTask;

    private OnlineSchemaChangeParameters oscParameters;

    private OnlineSchemaChangeScheduleTaskResult result;

    @Before
    public void setUp() {
        database = new DatabaseEntity();
        database.setDatabaseId(1L);
        database.setConnectionId(2L);

        schedule = new ScheduleEntity();
        schedule.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        schedule.setJobParametersJson("{\"swapTableType\": \"MANUAL\"}");

        scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(3L);
        scheduleTask.setJobName(String.valueOf(schedule.getId()));
        scheduleTask.setResultJson("{\"manualSwapTableEnabled\": true}");

        oscParameters = JsonUtils.fromJson(schedule.getJobParametersJson(), OnlineSchemaChangeParameters.class);
        result = JsonUtils.fromJson(scheduleTask.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class);
    }

    @Test
    public void testGetOscDatabaseInfoWithNonExistentDatabase() {
        when(databaseRepository.findById(1L)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);

        Assert.assertNotNull(oscDatabase);
        Assert.assertEquals(0, oscDatabase.getDatabaseId());
        Assert.assertFalse(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfoWithExistentDatabase() {
        when(databaseRepository.findById(1L)).thenReturn(Optional.of(database));
        when(getLockUserIsRequired(2L)).thenReturn(true);

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);

        Assert.assertNotNull(oscDatabase);
        Assert.assertEquals(1, oscDatabase.getDatabaseId());
        Assert.assertTrue(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTableWithNonExistentScheduleTask() {
        when(scheduleTaskRepository.findById(3L)).thenReturn(Optional.empty());

        try {
            oscService.swapTable(3L);
            Assert.fail("Expected an exception to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(ErrorCodes.BadArgument, e.getErrorCode());
        }
    }

    @Test
    public void testSwapTableWithNonExistentSchedule() {
        when(scheduleTaskRepository.findById(3L)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(Long.parseLong(scheduleTask.getJobName()))).thenReturn(Optional.empty());

        try {
            oscService.swapTable(3L);
            Assert.fail("Expected an exception to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(ErrorCodes.BadArgument, e.getErrorCode());
        }
    }

    @Test
    public void testSwapTableWithNonOnlineSchemaChangeCompleteTask() {
        when(scheduleTaskRepository.findById(3L)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(Long.parseLong(scheduleTask.getJobName()))).thenReturn(Optional.of(schedule));
        when(scheduleEntity.getJobType()).thenReturn(JobType.SCHEDULE_COMPLETE);

        try {
            oscService.swapTable(3L);
            Assert.fail("Expected an exception to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(ErrorCodes.BadArgument, e.getErrorCode());
        }
    }

    @Test
    public void testSwapTableWithSwapTableTypeNotManual() {
        when(scheduleTaskRepository.findById(3L)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(Long.parseLong(scheduleTask.getJobName()))).thenReturn(Optional.of(schedule));
        when(scheduleEntity.getJobType()).thenReturn(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        when(oscParameters.getSwapTableType()).thenReturn(SwapTableType.AUTO);

        try {
            oscService.swapTable(3L);
            Assert.fail("Expected an exception to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(ErrorCodes.BadArgument, e.getErrorCode());
        }
    }

    @Test
    public void testSwapTableWithManualSwapTableNotEnabled() {
        when(scheduleTaskRepository.findById(3L)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(Long.parseLong(scheduleTask.getJobName()))).thenReturn(Optional.of(schedule));
        when(scheduleEntity.getJobType()).thenReturn(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        when(oscParameters.getSwapTableType()).thenReturn(SwapTableType.MANUAL);
        when(result.isManualSwapTableEnabled()).thenReturn(false);

        try {
            oscService.swapTable(3L);
            Assert.fail("Expected an exception to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(ErrorCodes.BadArgument, e.getErrorCode());
        }
    }

    @Test
    public void testSwapTableWithValidParameters() {
        when(scheduleTaskRepository.findById(3L)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(Long.parseLong(scheduleTask.getJobName()))).thenReturn(Optional.of(schedule));
        when(scheduleEntity.getJobType()).thenReturn(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        when(oscParameters.getSwapTableType()).thenReturn(SwapTableType.MANUAL);
        when(result.isManualSwapTableEnabled()).thenReturn(true);

        OscSwapTableVO oscSwapTable = oscService.swapTable(3L);

        Assert.assertNotNull(oscSwapTable);
        Assert.assertEquals(3L, oscSwapTable.getScheduleTaskId());

        verify(scheduleTaskRepository).updateTaskResult(3L, JsonUtils.toJson(result));
    }

    @Transactional
    private boolean getLockUserIsRequired(Long connectionId) {
        ConnectionConfig decryptedConnConfig =
                connectionService.getForConnectionSkipPermissionCheck(connectionId);
        return OscDBUserUtil.isLockUserRequired(decryptedConnConfig.getDialectType(),
                () -> {
                    ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
                    String version = null;
                    ConnectionSession connSession = null;
                    try {
                        connSession = factory.generateSession();
                        version = ConnectionSessionUtil.getVersion(connSession);
                    } catch (Exception ex) {
                        log.info("Get connection occur error", ex);
                    } finally {
                        if (connSession != null) {
                            connSession.expire();
                        }
                    }
                    return version;
                });
    }
}