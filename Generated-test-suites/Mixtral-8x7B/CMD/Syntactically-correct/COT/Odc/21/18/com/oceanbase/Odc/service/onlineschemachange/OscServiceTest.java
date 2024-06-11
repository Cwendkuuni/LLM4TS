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
import static org.mockito.ArgumentMatchers.any;
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

    private OnlineSchemaChangeParameters oscParameters;
    private OnlineSchemaChangeScheduleTaskResult oscResult;
    private ScheduleTaskEntity scheduleTaskEntity;
    private DatabaseEntity databaseEntity;
    private ScheduleEntity scheduleEntity;

    @Before
    public void setUp() {
        oscParameters = new OnlineSchemaChangeParameters();
        oscResult = new OnlineSchemaChangeScheduleTaskResult();
        scheduleTaskEntity = new ScheduleTaskEntity();
        databaseEntity = new DatabaseEntity();
        scheduleEntity = new ScheduleEntity();

        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        oscResult.setManualSwapTableEnabled(true);
        oscResult.setManualSwapTableStarted(false);

        databaseEntity.setConnectionId(1L);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson("{}");
        scheduleTaskEntity.setResultJson("{}");

        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(databaseEntity));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong()))
                .thenReturn(new ConnectionConfig());
    }

    @Test
    public void testGetOscDatabaseInfo_emptyDatabaseId_returnsEmptyOscDatabase() {
        databaseEntity.setDatabaseId(null);

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);

        verify(databaseRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(databaseRepository);
        verifyZeroInteractions(connectionService, scheduleTaskRepository, scheduleRepository);
        // Assert that the returned oscDatabase is empty
        assert oscDatabase.getDatabaseId() == null;
    }

    @Test
    public void testGetOscDatabaseInfo_nonEmptyDatabaseId_returnsOscDatabase() {
        databaseEntity.setDatabaseId(8L);

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);

        verify(databaseRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(databaseRepository);
        verifyZeroInteractions(connectionService, scheduleTaskRepository, scheduleRepository);
        // Assert that the returned oscDatabase has the correct databaseId
        assert oscDatabase.getDatabaseId() == 8L;
    }

    @Test
    public void testSwapTable_validScheduleTaskId_returnsOscSwapTableVO() throws Exception {
        scheduleTaskEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(oscResult));

        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);

        verify(databaseRepository, times(1)).findById(1L);
        verify(scheduleRepository, times(1)).findById(anyLong());
        verify(scheduleTaskRepository, times(1)).updateTaskResult(anyLong(), anyString());
        verify(connectionService, times(1))
                .getForConnectionSkipPermissionCheck(databaseEntity.getConnectionId());
        verifyNoMoreInteractions(databaseRepository, scheduleTaskRepository, scheduleRepository);
        verifyZeroInteractions(connectionService);
        // Assert that the returned oscSwapTable has the correct scheduleTaskId
        assert oscSwapTable.getScheduleTaskId() == 1L;
    }

    @Test(expected = Exception.class)
    public void testSwapTable_emptyScheduleTaskId_throwsException() {
        scheduleTaskEntity.setJobParametersJson(null);

        oscService.swapTable(-1L);

        verifyZeroInteractions(databaseRepository, connectionService, scheduleTaskRepository);
    }

    @Test(expected = Exception.class)
    public void testSwapTable_nullScheduleTaskId_throwsException() {
        scheduleTaskEntity.setJobParametersJson(null);

        oscService.swapTable(null);

        verifyZeroInteractions(databaseRepository, connectionService, scheduleTaskRepository);
    }
}