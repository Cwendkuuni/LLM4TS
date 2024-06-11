import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

import lombok.extern.slf4j.Slf4j;

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
    @Slf4j
    private OscService oscService;

    private ConnectionSessionFactory connectionSessionFactory;

    @Before
    public void setUp() {
        connectionSessionFactory = Mockito.mock(ConnectionSessionFactory.class);
        ReflectionTestUtils.setField(oscService, "connectionSessionFactory", connectionSessionFactory);
    }

    @Test
    public void testGetOscDatabaseInfo_nonexistentDatabase() {
        Long id = 1L;
        Mockito.when(databaseRepository.findById(id)).thenReturn(Optional.empty());
        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(id);
        assertEquals(0, oscDatabaseInfo.getDatabaseId());
        assertFalse(oscDatabaseInfo.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfo_existingDatabase() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        databaseEntity.setConnectionId(2L);
        Mockito.when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));
        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(id);
        assertEquals(id, oscDatabaseInfo.getDatabaseId());
        Mockito.verify(connectionService, Mockito.times(1)).getForConnectionSkipPermissionCheck(2L);
    }

    @Test
    public void testSwapTable_nonexistentScheduleTask() {
        Long scheduleTaskId = 1L;
        Mockito.when(
                scheduleTaskRepository.findById(scheduleTaskId))
                        .thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> oscService.swapTable(scheduleTaskId));
        Mockito.verifyNoMoreInteractions(connectionService);
    }

    @Test
    public void testSwapTable_wrongScheduleTaskType() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask =
                new ScheduleTaskEntity();
        scheduleTask.setJobName(String.valueOf(2L));
        Mockito.when(
                scheduleTaskRepository.findById(scheduleTaskId))
                        .thenReturn(Optional.of(scheduleTask));

        Mockito.when(
                scheduleRepository.findById(2L))
                        .thenReturn(Optional.of(
                new ScheduleEntity()
                        .setJobType(JobType.MANUAL_SCHEDULE)));
        assertThrows(IllegalArgumentException.class, () -> oscService.swapTable(scheduleTaskId));
        Mockito.verifyNoMoreInteractions(connectionService);
    }

    @Test
    public void testSwapTable_wrongSwapTableType() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask =
                new ScheduleTaskEntity();
        scheduleTask.setJobName(String.valueOf(2L));
        OnlineSchemaChangeParameters oscParameters =
                new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.AUTO);
        OnlineSchemaChangeScheduleTaskResult result =
                new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        Mockito.when(
                scheduleTaskRepository.findById(scheduleTaskId))
                        .thenReturn(Optional.of(scheduleTask));

        Mockito.when(
                scheduleRepository.findById(2L))
                        .thenReturn(Optional.of(
                new ScheduleEntity()
                        .setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE)
                        .setJobParametersJson(JsonUtils.toJson(oscParameters))));

        Mockito.when(
                JsonUtils.fromJson(scheduleTask.getResultJson(),
                        OnlineSchemaChangeScheduleTaskResult.class))
                        .thenReturn(result);
        assertThrows(IllegalArgumentException.class, () -> oscService.swapTable(scheduleTaskId));
        Mockito.verifyNoMoreInteractions(connectionService);
    }

    @Test
    public void testSwapTable_success() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask =
                new ScheduleTaskEntity();
        scheduleTask.setJobName(String.valueOf(2L));
        OnlineSchemaChangeParameters oscParameters =
                new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        OnlineSchemaChangeScheduleTaskResult result =
                new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        Mockito.when(
                scheduleTaskRepository.findById(scheduleTaskId))
                        .thenReturn(Optional.of(scheduleTask));

        Mockito.when(
                scheduleRepository.findById(2L))
                        .thenReturn(Optional.of(
                new ScheduleEntity()
                        .setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE)
                        .setJobParametersJson(JsonUtils.toJson(oscParameters))));

        Mockito.when(
                JsonUtils.fromJson(scheduleTask.getResultJson(),
                        OnlineSchemaChangeScheduleTaskResult.class))
                        .thenReturn(result);
        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);
        assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
        assertTrue(result.isManualSwapTableStarted());
        Mockito.verify(connectionService, Mockito.times(1))
                .getForConnectionSkipPermissionCheck(2L);
    }

}