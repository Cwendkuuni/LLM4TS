import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.*;
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
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

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(oscService, "oscDBUserUtil", new OscDBUserUtil());
    }

    @Test
    public void test_getOscDatabaseInfo_nonexistentDatabaseId() {
        Optional<DatabaseEntity> databaseOptional = Optional.empty();
        when(databaseRepository.findById(anyLong())).thenReturn(databaseOptional);

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(123L);

        assertEquals(0, result.getDatabaseId());
        assertFalse(result.isLockDatabaseUserRequired());
    }

    @Test
    public void test_getOscDatabaseInfo_existingDatabaseId() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(123L);
        databaseEntity.setConnectionId(456L);

        Optional<DatabaseEntity> databaseOptional = Optional.of(databaseEntity);
        when(databaseRepository.findById(anyLong())).thenReturn(databaseOptional);

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(123L);

        assertEquals(123L, result.getDatabaseId());
        assertTrue(result.isLockDatabaseUserRequired());
    }

    @Test
    public void test_swapTable_nonexistentScheduleTaskId() {
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        OscSwapTableVO result = oscService.swapTable(123L);

        assertNull(result);
    }

    @Test
    public void test_swapTable_success() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(123L);
        scheduleTaskEntity.setJobName("100");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(100L);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(new ConnectionConfig());

        doReturn(oscParameters).when(oscService).getOscParameters(scheduleEntity);
        doReturn(result).when(oscService).executeSwapTable(anyLong(), anyLong(), anyBoolean());

        OscSwapTableVO oscSwapTable = oscService.swapTable(123L);

        assertEquals(123L, oscSwapTable.getScheduleTaskId());
    }

    private OnlineSchemaChangeParameters getOscParameters(ScheduleEntity scheduleEntity) {
        // Implement this method if needed
        return null;
    }

    private OnlineSchemaChangeScheduleTaskResult executeSwapTable(Long connectionId, Long scheduleTaskId,
                                                                   boolean isDryRun) {
        // Implement this method if needed
        return null;
    }
}