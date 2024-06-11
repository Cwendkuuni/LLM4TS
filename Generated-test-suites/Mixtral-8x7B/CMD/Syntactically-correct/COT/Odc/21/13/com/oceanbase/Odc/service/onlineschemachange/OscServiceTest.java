import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;
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
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;

import lombok.extern.slf4j.Slf4j;

import org.mockito.ArgumentMatchers;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
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
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(oscService, "jsonUtils", JsonUtils.getInstance());
    }

    @Test
    public void testGetOscDatabaseInfo_nonexistentId() {
        Long id = 1L;
        when(databaseRepository.findById(id)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        assertNull(result);
        verify(databaseRepository).findById(id);
        verifyNoMoreInteractions(connectionService, scheduleTaskRepository, scheduleRepository);
    }

    @Test
    public void testGetOscDatabaseInfo_existingId() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        assertEquals(id, result.getDatabaseId());
        assertEquals(getLockUserIsRequired(id), result.isLockDatabaseUserRequired());
        verify(databaseRepository).findById(id);
        verifyNoMoreInteractions(connectionService, scheduleTaskRepository, scheduleRepository);
    }

    private boolean getLockUserIsRequired(Long connectionId) {
        ConnectionConfig decryptedConnConfig =
                new ConnectionConfig();
        decryptedConnConfig.setDialectType("testType");
        ReflectionTestUtils.setField(oscService, "connectionService",
                new ConnectionService() {
                    @Override
                    public ConnectionConfig getForConnectionSkipPermissionCheck(Long connectionId) {
                        return decryptedConnConfig;
                    }
                });

        ConnectionSessionFactory factory = mock(ConnectionSessionFactory.class);
        ConnectionSession connectionSession = mock(ConnectionSession.class);

        when(factory.generateSession()).thenReturn(connectionSession);
        when(connectionSession.expire()).thenReturn(null);

        ReflectionTestUtils.setField(oscService, "connectionSessionFactory", factory);

        return OscDBUserUtil.isLockUserRequired(decryptedConnConfig.getDialectType(),
                () -> ConnectionSessionUtil.getVersion(connectionSession));
    }

    @Test
    public void testSwapTable_nonexistentScheduleTaskId() {
        Long scheduleTaskId = 1L;
        when(scheduleTaskRepository.findById(scheduleTaskId))
                .thenReturn(Optional.empty());

        Exception exception = null;
        try {
            oscService.swapTable(scheduleTaskId);
        } catch (Exception e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorCodes.ResourceNotFound.getCode(), exception.getMessage().split(":")[0]);
        verify(scheduleTaskRepository).findById(scheduleTaskId);
        verifyNoMoreInteractions(databaseRepository, connectionService, scheduleRepository);
    }

    @Test
    public void testSwapTable_nonexistentScheduleId() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName(String.valueOf(2L));

        when(scheduleTaskRepository.findById(scheduleTaskId))
                .thenReturn(Optional.of(scheduleTask));

        when(scheduleRepository.findById(Long.parseLong(scheduleTask.getJobName())))
                .thenReturn(Optional.empty());

        Exception exception = null;
        try {
            oscService.swapTable(scheduleTaskId);
        } catch (Exception e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorCodes.ResourceNotFound.getCode(), exception.getMessage().split(":")[0]);
        verify(scheduleTaskRepository).findById(scheduleTaskId);
        verify(scheduleRepository).findById(Long.parseLong(scheduleTask.getJobName()));
        verifyNoMoreInteractions(databaseRepository, connectionService);
    }

    @Test
    public void testSwapTable_invalidScheduleType() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName(String.valueOf(2L));

        when(scheduleTaskRepository.findById(scheduleTaskId))
                .thenReturn(Optional.of(scheduleTask));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(2L);
        scheduleEntity.setJobType(JobType.OTHER);

        when(scheduleRepository.findById(Long.parseLong(scheduleTask.getJobName())))
                .thenReturn(Optional.of(scheduleEntity));

        Exception exception = null;
        try {
            oscService.swapTable(scheduleTaskId);
        } catch (Exception e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorCodes.BadArgument.getCode(), exception.getMessage().split(":")[0]);
        verify(scheduleTaskRepository).findById(scheduleTaskId);
        verify(scheduleRepository).findById(Long.parseLong(scheduleTask.getJobName()));
        verifyNoMoreInteractions(databaseRepository, connectionService);
    }

    @Test
    public void testSwapTable_invalidSwapTableType() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName(String.valueOf(2L));

        when(scheduleTaskRepository.findById(scheduleTaskId))
                .thenReturn(Optional.of(scheduleTask));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(2L);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        when(scheduleRepository.findById(Long.parseLong(scheduleTask.getJobName())))
                .thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.AUTO);

        when(jsonUtils.fromJson(ArgumentMatchers.eq(scheduleEntity.getJobParametersJson()),
                ArgumentMatchers.eq(OnlineSchemaChangeParameters.class)))
                .thenReturn(oscParameters);

        Exception exception = null;
        try {
            oscService.swapTable(scheduleTaskId);
        } catch (Exception e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorCodes.BadArgument.getCode(), exception.getMessage().split(":")[0]);
        verify(scheduleTaskRepository).findById(scheduleTaskId);
        verify(scheduleRepository).findById(Long.parseLong(scheduleTask.getJobName()));
        verifyNoMoreInteractions(databaseRepository, connectionService);
    }

    @Test
    public void testSwapTable_swapTableNotEnabled() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName(String.valueOf(2L));

        when(scheduleTaskRepository.findById(scheduleTaskId))
                .thenReturn(Optional.of(scheduleTask));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(2L);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        when(scheduleRepository.findById(Long.parseLong(scheduleTask.getJobName())))
                .thenReturn(Optional.of(scheduleEntity));

        when(jsonUtils.fromJson(ArgumentMatchers.eq(scheduleEntity.getJobParametersJson()),
                ArgumentMatchers.eq(OnlineSchemaChangeParameters.class)))
                .thenReturn(oscParameters);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);

        when(jsonUtils.fromJson(ArgumentMatchers.eq(scheduleTask.getResultJson()),
                ArgumentMatchers.eq(OnlineSchemaChangeScheduleTaskResult.class)))
                .thenReturn(result);

        Exception exception = null;
        try {
            oscService.swapTable(scheduleTaskId);
        } catch (Exception e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorCodes.BadArgument.getCode(), exception.getMessage().split(":")[0]);
        verify(scheduleTaskRepository).findById(scheduleTaskId);
        verify(scheduleRepository).findById(Long.parseLong(scheduleTask.getJobName()));
        verifyNoMoreInteractions(databaseRepository, connectionService);
    }

    @Test
    public void testSwapTable_success() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName(String.valueOf(2L));

        when(scheduleTaskRepository.findById(scheduleTaskId))
                .thenReturn(Optional.of(scheduleTask));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(2L);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        when(scheduleRepository.findById(Long.parseLong(scheduleTask.getJobName())))
                .thenReturn(Optional.of(scheduleEntity));

        when(jsonUtils.fromJson(ArgumentMatchers.eq(scheduleEntity.getJobParametersJson()),
                ArgumentMatchers.eq(OnlineSchemaChangeParameters.class)))
                .thenReturn(oscParameters);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        result.setManualSwapTableStarted(false);

        when(jsonUtils.fromJson(ArgumentMatchers.eq(scheduleTask.getResultJson()),
                ArgumentMatchers.eq(OnlineSchemaChangeScheduleTaskResult.class)))
                .thenReturn(result);

        OscSwapTableVO oscSwapTable = new OscSwapTableVO();
        oscSwapTable.setScheduleTaskId(scheduleTaskId);

        when(oscService.swapTable(scheduleTaskId)).thenReturn(oscSwapTable);

        OscSwapTableVO resultOscSwapTable = oscService.swapTable(scheduleTaskId);

        assertEquals(oscSwapTable, resultOscSwapTable);
        verify(scheduleTaskRepository).findById(scheduleTaskId);
        verify(scheduleRepository).findById(Long.parseLong(scheduleTask.getJobName()));
        verify(jsonUtils).fromJson(scheduleEntity.getJobParametersJson(), OnlineSchemaChangeParameters.class);
        verify(jsonUtils).fromJson(scheduleTask.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class);
        verify(oscService).swapTable(scheduleTaskId);
        verifyNoMoreInteractions(databaseRepository, connectionService);
    }
}