import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

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
import com.oceanbase.odc.service.schedule.model.JobType;

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

    private DatabaseEntity databaseEntity;

    private ScheduleEntity scheduleEntity;

    private ScheduleTaskEntity scheduleTaskEntity;

    private OnlineSchemaChangeParameters oscParameters;

    private OnlineSchemaChangeScheduleTaskResult oscResult;

    @Before
    public void setUp() {
        databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        databaseEntity.setConnectionId(2L);

        scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson("{}");

        scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(3L);
        scheduleTaskEntity.setJobName("1");
        scheduleTaskEntity.setResultJson("{}");

        oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        oscResult = new OnlineSchemaChangeScheduleTaskResult();
        oscResult.setManualSwapTableEnabled(true);
        oscResult.setManualSwapTableStarted(true);

        ReflectionTestUtils.setField(oscService, "odcVersion", "1.0.0");
    }

    @Test
    public void testGetOscDatabaseInfoWithoutDatabase() {
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo info = oscService.getOscDatabaseInfo(1L);

        assertEquals(0, info.getDatabaseId());
        assertFalse(info.isLockDatabaseUserRequired());

        verify(databaseRepository, times(1)).findById(1L);
        verify(connectionService, never()).getForConnectionSkipPermissionCheck(anyLong());
    }

    @Test
    public void testGetOscDatabaseInfoWithDatabase() {
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(databaseEntity));
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong()))
                .thenReturn(new ConnectionConfig());

        OscLockDatabaseUserInfo info = oscService.getOscDatabaseInfo(1L);

        assertEquals(1, info.getDatabaseId());
        assertTrue(info.isLockDatabaseUserRequired());

        verify(databaseRepository, times(1)).findById(1L);
        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(2L);
    }

    @Test
    public void testSwapTableWithInvalidScheduleTaskId() {
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        OscSwapTableVO vo = oscService.swapTable(1L);

        assertNull(vo);

        verify(scheduleTaskRepository, times(1)).findById(1L);
        verify(scheduleRepository, never()).findById(anyLong());
        verify(databaseRepository, never()).findById(anyLong());
    }

    @Test
    public void testSwapTableWithInvalidSchedule() {
        when(scheduleTaskRepository.findById(anyLong()))
                .thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.empty());

        OscSwapTableVO vo = oscService.swapTable(1L);

        assertNull(vo);

        verify(scheduleTaskRepository, times(1)).findById(1L);
        verify(scheduleRepository, times(1)).findById(1L);
        verify(databaseRepository, never()).findById(anyLong());
    }

    @Test
    public void testSwapTableWithInvalidOscParameters() {
        when(scheduleTaskRepository.findById(anyLong()))
                .thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(databaseEntity));

        oscParameters.setSwapTableType(null);

        OscSwapTableVO vo = oscService.swapTable(1L);

        assertNull(vo);

        verify(scheduleTaskRepository, times(1)).findById(1L);
        verify(scheduleRepository, times(1)).findById(1L);
        verify(databaseRepository, times(1)).findById(1L);
    }

    @Test
    public void testSwapTableWithInvalidOscResult() {
        when(scheduleTaskRepository.findById(anyLong()))
                .thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(databaseEntity));

        oscResult.setManualSwapTableEnabled(false);

        OscSwapTableVO vo = oscService.swapTable(1L);

        assertNull(vo);

        verify(scheduleTaskRepository, times(1)).findById(1L);
        verify(scheduleRepository, times(1)).findById(1L);
        verify(databaseRepository, times(1)).findById(1L);
    }

    @Test
    public void testSwapTableWithSuccess() {
        when(scheduleTaskRepository.findById(anyLong()))
                .thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(databaseEntity));

        oscResult.setManualSwapTableEnabled(true);

        OscSwapTableVO vo = oscService.swapTable(1L);

        assertEquals(1L, vo.getScheduleTaskId());

        verify(scheduleTaskRepository, times(1)).findById(1L);
        verify(scheduleRepository, times(1)).findById(1L);
        verify(databaseRepository, times(1)).findById(1L);
        verify(scheduleTaskRepository, times(1)).updateTaskResult(1L, "{\"manualSwapTableEnabled\":false,\"manualSwapTableStarted\":true}");
    }

    @Test
    public void testGetLockUserIsRequiredWithInvalidConnectionId() {
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong()))
                .thenReturn(null);

        boolean result = oscService.getLockUserIsRequired(1L);

        assertFalse(result);

        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(1L);
    }

    @Test
    public void testGetLockUserIsRequiredWithSuccess() {
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong()))
                .thenReturn(new ConnectionConfig());

        boolean result = oscService.getLockUserIsRequired(1L);

        assertTrue(result);

        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(1L);
    }
}