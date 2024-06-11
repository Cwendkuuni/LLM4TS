import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
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
    public void setUp() {
        // Initialize mocks and call the constructor of the class under test
    }

    @Test
    public void testGetOscDatabaseInfoWithNonexistentDatabaseId() {
        Long id = 1L;
        when(databaseRepository.findById(eq(id))).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        Assert.assertNotNull(result);
        Assert.assertEquals(0L, result.getDatabaseId());
        Assert.assertFalse(result.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfoWithExistentDatabaseId() {
        Long id = 1L;
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(id);
        databaseEntity.setConnectionId(2L);

        when(databaseRepository.findById(eq(id))).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        Assert.assertNotNull(result);
        Assert.assertEquals(id, result.getDatabaseId());
        Assert.assertTrue(result.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTableWithNonexistentScheduleTaskId() {
        Long scheduleTaskId = 1L;
        when(scheduleTaskRepository.findById(eq(scheduleTaskId))).thenReturn(Optional.empty());

        OscSwapTableVO result = oscService.swapTable(scheduleTaskId);

        Assert.assertNotNull(result);
        Assert.assertEquals(scheduleTaskId, result.getScheduleTaskId());
    }

    @Test
    public void testSwapTableWithExistentScheduleTaskId() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");

        when(scheduleTaskRepository.findById(eq(scheduleTaskId))).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(eq(1L))).thenReturn(Optional.of(new ScheduleEntity()));

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        when(JsonUtils.fromJson(any(), eq(OnlineSchemaChangeParameters.class)))
                .thenReturn(oscParameters);
        when(JsonUtils.fromJson(any(), eq(OnlineSchemaChangeScheduleTaskResult.class)))
                .thenReturn(result);

        OscSwapTableVO swapTableVO = oscService.swapTable(scheduleTaskId);

        Assert.assertNotNull(swapTableVO);
        Assert.assertEquals(scheduleTaskId, swapTableVO.getScheduleTaskId());
    }

    private ScheduleTaskEntity createScheduleTaskEntity(Long scheduleTaskId) {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        scheduleTaskEntity.setId(scheduleTaskId);
        return scheduleTaskEntity;
    }

    private ScheduleEntity createScheduleEntity(Long scheduleId) {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        return scheduleEntity;
    }
}