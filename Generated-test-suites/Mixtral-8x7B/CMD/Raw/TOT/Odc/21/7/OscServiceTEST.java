
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;

@RunWith(MockitoJUnitRunner.class)
public class OscServiceTest {

    @InjectMocks
    private OscService oscService;

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private ConnectionService connectionService;

    @Mock
    private JsonUtils jsonUtils;

    @Before
    public void setup() {
    }

    @After
    public void teardown() {
    }

    @Test
    public void testGetOscDatabaseInfo() {
        // Test case for expert 2
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        databaseEntity.setConnectionId(2L);
        when(databaseRepository.findById(id)).thenReturn(Optional.of(databaseEntity));
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);
        verify(databaseRepository, times(1)).findById(id);
        assertEquals(id, result.getDatabaseId());
        assertTrue(result.isLockDatabaseUserRequired());

        // Test case for expert 3
        when(databaseRepository.findById(id)).thenReturn(Optional.empty());
        OscLockDatabaseUserInfo resultEmpty = oscService.getOscDatabaseInfo(id);
        verify(databaseRepository, times(1)).findById(id);
        assertNull(resultEmpty);
    }

    @Test
    @Transactional
    public void testSwapTable() {
        // Test case for expert 2
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName(String.valueOf(scheduleTaskId));
        when(scheduleRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleEntity));
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        when(jsonUtils.fromJson(anyString(), eq(OnlineSchemaChangeParameters.class)))
            .thenReturn(oscParameters);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        when(jsonUtils.fromJson(anyString(), eq(OnlineSchemaChangeScheduleTaskResult.class)))
            .thenReturn(result);
        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);
        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
        verify(scheduleRepository, times(1)).findById(scheduleTaskId);
        verify(jsonUtils, times(1)).fromJson(anyString(), eq(OnlineSchemaChangeParameters.class));
        verify(jsonUtils, times(1))
            .fromJson(anyString(), eq(OnlineSchemaChangeScheduleTaskResult.class));
        assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
        assertFalse(result.isManualSwapTableEnabled());
        assertTrue(result.isManualSwapTableStarted());
    }

    @Test(expected = PreConditions.InvalidStateException.class)
    @Transactional
    public void testSwapTablePreConditions() {
        // Test case for expert 3
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName(String.valueOf(scheduleTaskId));
        when(scheduleRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleEntity));
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.IMMEDIATE);
        when(jsonUtils.fromJson(anyString(), eq(OnlineSchemaChangeParameters.class)))
            .thenReturn(oscParameters);
        oscService.swapTable(scheduleTaskId);
        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
        verify(scheduleRepository, times(1)).findById(scheduleTaskId);
        verify(jsonUtils, never()).fromJson(anyString(), eq(OnlineSchemaChangeParameters.class));
    }
}
