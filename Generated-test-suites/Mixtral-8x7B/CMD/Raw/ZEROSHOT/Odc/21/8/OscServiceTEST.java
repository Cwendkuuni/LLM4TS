/**
 * Test file for the OscService class.
 */

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
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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
        ReflectionTestUtils.setField(oscService, "skipAuthorizeAnnotationClass", SkipAuthorize.class);
    }

    @Test
    public void testGetOscDatabaseInfoEmptyDatabase() {
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);

        assertEquals(0, result.getDatabaseId());
        assertEquals(false, result.isLockDatabaseUserRequired());

        verify(databaseRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(databaseRepository);
    }

    @Test
    public void testGetOscDatabaseInfoWithDatabase() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        databaseEntity.setConnectionId(2L);

        when(databaseRepository.findById(1L)).thenReturn(Optional.of(databaseEntity));
        when(oscService.getLockUserIsRequired(2L)).thenReturn(true);

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);

        assertEquals(1, result.getDatabaseId());
        assertEquals(true, result.isLockDatabaseUserRequired());

        verify(databaseRepository, times(1)).findById(1L);
        verify(oscService, times(1)).getLockUserIsRequired(2L);
        verifyNoMoreInteractions(databaseRepository, oscService);
    }

    @Test
    @Transactional
    public void testSwapTable() {
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));

        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setJobName(scheduleEntity.getScheduleId().toString());
        scheduleTask.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));

        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(scheduleEntity.getScheduleId())).thenReturn(Optional.of(scheduleEntity));
        doNothing().when(oscService).closeManualSwapTable(Mockito.any());
        doNothing().when(oscService).openStartManualSwapTable(Mockito.any());

        OscSwapTableVO result = oscService.swapTable(1L);

        assertEquals(1, result.getScheduleTaskId());

        verify(scheduleTaskRepository, times(1)).findById(1L);
        verify(scheduleRepository, times(1)).findById(scheduleEntity.getScheduleId());
        verify(oscService, times(1)).closeManualSwapTable(Mockito.any());
        verify(oscService, times(1)).openStartManualSwapTable(Mockito.any());
        verifyNoMoreInteractions(scheduleTaskRepository, scheduleRepository, oscService);
    }

    @Test
    public void testGetLockUserIsRequired() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        ConnectionSessionFactory connectionSessionFactory = new DefaultConnectSessionFactory(connectionConfig);
        ConnectionSession connectionSession = mock(ConnectionSession.class);

        when(oscService.getConnectionSessionFactory()).thenReturn(connectionSessionFactory);
        when(connectionSessionFactory.generateSession()).thenReturn(connectionSession);
        when(connectionSession.getVersion()).thenReturn("1.0");

        boolean result = oscService.getLockUserIsRequired(1L);

        assertEquals(false, result);

        verify(oscService, times(1)).getConnectionSessionFactory();
        verify(connectionSessionFactory, times(1)).generateSession();
        verify(connectionSession, times(1)).getVersion();
        verifyNoMoreInteractions(oscService, connectionSessionFactory, connectionSession);
    }

    @Test
    public void testCloseManualSwapTable() {
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        oscService.closeManualSwapTable(result);

        assertEquals(false, result.isManualSwapTableEnabled());
    }

    @Test
    public void testOpenStartManualSwapTable() {
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();

        oscService.openStartManualSwapTable(result);

        assertEquals(true, result.isManualSwapTableStarted());
    }
}