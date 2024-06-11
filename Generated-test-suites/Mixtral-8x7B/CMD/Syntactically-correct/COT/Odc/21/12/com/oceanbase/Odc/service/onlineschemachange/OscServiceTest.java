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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.*;
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

    private DatabaseEntity databaseEntity;

    private OnlineSchemaChangeParameters oscParameters;

    private ScheduleTaskEntity scheduleTask;

    private OnlineSchemaChangeScheduleTaskResult result;

    @Before
    public void setup() {
        databaseEntity = new DatabaseEntity();
        databaseEntity.setConnectionId(1L);
        databaseEntity.setDatabaseId(1L);

        oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(1L);
        scheduleTask.setJobName("1");
        scheduleTask.setResultJson("{}");

        result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        result.setManualSwapTableStarted(true);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);

        assertThat(oscDatabase, notNullValue());
        verify(databaseRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetOscDatabaseInfoNonExistentDatabase() {
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);

        assertThat(oscDatabase, notNullValue());
        verify(databaseRepository, times(1)).findById(1L);
    }

    @Test
    public void testSwapTable() throws Exception {
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(new ScheduleEntity()));
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(databaseEntity));
        when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(new ConnectionConfig());

        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);

        assertThat(oscSwapTable, notNullValue());
        verify(scheduleTaskRepository, times(1)).findById(1L);
        verify(scheduleRepository, times(1)).findById(1L);
        verify(databaseRepository, times(1)).findById(anyLong());
        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(1L);
    }

    @Test
    public void testSwapTableNonExistentScheduleTask() {
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = spyException(new Exception("Task not found."));
        when(exception.getMessage()).thenReturn("Task not found.");

        try {
            oscService.swapTable(1L);
        } catch (Exception e) {
            assertThat(e, notNullValue());
            verify(scheduleTaskRepository, times(1)).findById(1L);
            assertThat(e.getMessage(), equalTo("Task not found."));
        }
    }

    @Test
    public void testSwapTableNonExistentSchedule() {
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = spyException(new Exception("Schedule not found."));
        when(exception.getMessage()).thenReturn("Schedule not found.");

        try {
            oscService.swapTable(1L);
        } catch (Exception e) {
            assertThat(e, notNullValue());
            verify(scheduleTaskRepository, times(1)).findById(1L);
            verify(scheduleRepository, times(1)).findById(1L);
            assertThat(e.getMessage(), equalTo("Schedule not found."));
        }
    }

    private RuntimeException spyException(Throwable throwable) {
        RuntimeException runtimeException = new RuntimeException(throwable);
        Mockito.spy(runtimeException);
        return runtimeException;
    }
}