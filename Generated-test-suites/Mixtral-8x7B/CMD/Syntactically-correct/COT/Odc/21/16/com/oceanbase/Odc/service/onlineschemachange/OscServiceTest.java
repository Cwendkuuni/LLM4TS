import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Optional;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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

    private OnlineSchemaChangeScheduleTaskResult result;

    @Before
    public void setup() {
        databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        databaseEntity.setConnectionId(1L);

        scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson("{}");
        scheduleEntity.setJobId(1L);
        scheduleEntity.setJobName("1");

        scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setResultJson("{}");
        scheduleTaskEntity.setTaskId(1L);
        scheduleTaskEntity.setJobName("1");

        oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        result.setManualSwapTableStarted(false);
    }

    @Test
    public void testGetOscDatabaseInfo_whenDatabaseDoesNotExist_returnsEmptyOscLockDatabaseUserInfo() {
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);

        assertNull(oscDatabase);
    }

    @Test
    public void testGetOscDatabaseInfo_whenDatabaseExists_returnsOscLockDatabaseUserInfo() {
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);

        assertEquals(databaseEntity.getDatabaseId(), oscDatabase.getDatabaseId());
        assertEquals(getLockUserIsRequired(databaseEntity.getConnectionId()), oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable_whenScheduleTaskDoesNotExist_throwsException() {
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            oscService.swapTable(1L);
        } catch (IllegalArgumentException e) {
            assertEquals(ErrorCodes.BadArgument, e.getErrorCode());
            assertEquals("schedule task", e.getErrorArgs()[0]);
            assertEquals(1L, e.getErrorArgs()[1]);
            return;
        }

        assertFalse(true);
    }

    @Test
    public void testSwapTable_whenScheduleTaskExistsButScheduleDoesNotExist_throwsException() {
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            oscService.swapTable(1L);
        } catch (IllegalArgumentException e) {
            assertEquals(ErrorCodes.BadArgument, e.getErrorCode());
            assertEquals("schedule ", e.getErrorArgs()[0]);
            assertEquals(1L, e.getErrorArgs()[1]);
            return;
        }

        assertFalse(true);
    }

    @Test
    public void testSwapTable_whenScheduleTaskAndScheduleExistButJobTypeIsNotOnlineSchemaChangeComplete_throwsException() {
        scheduleEntity.setJobType(JobType.OTHER);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        try {
            oscService.swapTable(1L);
        } catch (IllegalArgumentException e) {
            assertEquals(ErrorCodes.BadArgument, e.getErrorCode());
            assertEquals("Task type is not " + TaskType.ONLINE_SCHEMA_CHANGE.name(), e.getErrorArgs()[0]);
            return;
        }

        assertFalse(true);
    }

    @Test
    public void testSwapTable_whenScheduleTaskExistsAndJobTypeIsOnlineSchemaChangeCompleteAndSwapTableTypeIsNotManual_throwsException() {
        oscParameters.setSwapTableType(SwapTableType.AUTOMATIC);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        try {
            oscService.swapTable(1L);
        } catch (IllegalArgumentException e) {
            assertEquals(ErrorCodes.BadArgument, e.getErrorCode());
            assertEquals("Swap table type is not " + SwapTableType.MANUAL.name(), e.getErrorArgs()[0]);
            return;
        }

        assertFalse(true);
    }

    @Test
    public void testSwapTable_whenScheduleTaskExistsAndJobTypeIsOnlineSchemaChangeCompleteAndSwapTableTypeIsManual_disablesManualSwapTableAndEnablesStartManualSwapTable() {
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        result.setManualSwapTableEnabled(true);
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(result));
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);

        assertNotNull(oscSwapTable);
        assertEquals(1L, oscSwapTable.getScheduleTaskId());
        verify(scheduleTaskRepository).updateTaskResult(1L, "{\"manualSwapTableEnabled\":false,\"manualSwapTableStarted\":true}");
        verify(scheduleTaskRepository, never()).delete(any());
    }

    @Test
    public void testSwapTable_whenScheduleTaskExistsAndJobTypeIsOnlineSchemaChangeCompleteAndSwapTableTypeIsManualAndManualSwapTableIsNotEnabled_throwsException() {
        result.setManualSwapTableEnabled(false);
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(result));
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        try {
            oscService.swapTable(1L);
        } catch (IllegalArgumentException e) {
            assertEquals(ErrorCodes.BadArgument, e.getErrorCode());
            assertEquals("Manual Swap table type is not enable ", e.getErrorArgs()[0]);
            return;
        }

        assertFalse(true);
    }

    @Test
    public void testSwapTable_whenScheduleTaskExistsAndJobTypeIsOnlineSchemaChangeCompleteAndSwapTableTypeIsManualAndManualSwapTableIsEnabled_disablesManualSwapTableAndEnablesStartManualSwapTable() {
        result.setManualSwapTableEnabled(true);
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(result));
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);

        assertNotNull(oscSwapTable);
        assertEquals(1L, oscSwapTable.getScheduleTaskId());
        verify(scheduleTaskRepository).updateTaskResult(1L, "{\"manualSwapTableEnabled\":false,\"manualSwapTableStarted\":true}");
        verify(scheduleTaskRepository, never()).delete(any());
    }

    private boolean getLockUserIsRequired(Long connectionId) {
        // Mock the behavior of this method for testing
        return true;
    }
}