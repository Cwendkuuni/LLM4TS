
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

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

    private OnlineSchemaChangeScheduleTaskResult result;

    private DatabaseEntity databaseEntity;

    private ScheduleTaskEntity scheduleTask;

    private ScheduleEntity schedule;

    @Before
    public void setUp() {
        oscParameters = new OnlineSchemaChangeParameters();
        result = new OnlineSchemaChangeScheduleTaskResult();
        databaseEntity = new DatabaseEntity();
        schedule = new ScheduleEntity();
        scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setJobName(String.valueOf(schedule.getId()));
        databaseEntity.setConnectionId(1L);
        schedule.setJobParametersJson(JsonUtils.toJson(oscParameters));
        schedule.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        result.setManualSwapTableEnabled(true);
        result.setManualSwapTableStarted(false);
        scheduleTask.setResultJson(JsonUtils.toJson(result));
        scheduleEntity = Optional.of(schedule);
        scheduleTaskOptional = Optional.of(scheduleTask);
    }

    @Test
    public void testGetOscDatabaseInfo_ValidDatabaseId() {
        databaseEntity.setLockDatabaseUserRequired(true);
        databaseEntity.setDatabaseId(1L);
        databaseRepository.findById = (id) -> scheduleEntity;
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);
        assertEquals("1", oscDatabase.getDatabaseId());
        assertTrue(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfo_InvalidDatabaseId() {
        databaseEntity.setLockDatabaseUserRequired(true);
        databaseEntity.setDatabaseId(1L);
        databaseRepository.findById = (id) -> Optional.empty();
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);
        assertEquals("", oscDatabase.getDatabaseId());
        assertFalse(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfo_NullDatabaseId() {
        databaseEntity.setLockDatabaseUserRequired(true);
        databaseEntity.setDatabaseId(1L);
        databaseRepository.findById = (id) -> scheduleEntity;
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(null);
        assertEquals("", oscDatabase.getDatabaseId());
        assertFalse(oscDatabase.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable_ValidScheduleTaskId() {
        when(scheduleTaskRepository.findById(1L)).thenReturn(scheduleTaskOptional);
        when(scheduleRepository.findById(1L)).thenReturn(scheduleEntity);
        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);
        assertEquals(1L, oscSwapTable.getScheduleTaskId());
    }

    @Test
    public void testSwapTable_InvalidScheduleTaskId() {
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());
        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);
        assertEquals(-1L, oscSwapTable.getScheduleTaskId());
    }

    @Test
    public void testSwapTable_NullScheduleTaskId() {
        when(scheduleTaskRepository.findById(1L)).thenReturn(scheduleTaskOptional);
        when(scheduleRepository.findById(1L)).thenReturn(scheduleEntity);
        OscSwapTableVO oscSwapTable = oscService.swapTable(null);
        assertEquals(-1L, oscSwapTable.getScheduleTaskId());
    }
}
