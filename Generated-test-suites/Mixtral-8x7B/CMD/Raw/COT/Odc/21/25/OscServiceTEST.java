import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

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
        databaseEntity.setConnectionId(1L);
        databaseEntity.setDatabaseId(2L);

        scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobName("1");
        scheduleEntity.setJobParametersJson("{\"swapTableType\":\"MANUAL\"}");

        scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("1");
        scheduleTaskEntity.setResultJson("{\"manualSwapTableEnabled\":true}");

        oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        oscResult = new OnlineSchemaChangeScheduleTaskResult();
        oscResult.setManualSwapTableEnabled(true);

        ReflectionTestUtils.setField(oscService, "lockUserRequiredMap",
                Map.<String, Boolean>of("dialectType1", true, "dialectType2", false));
    }

    @Test
    public void testGetOscDatabaseInfoForNonExistentDatabase() {
        Mockito.when(databaseRepository.findById(2L)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(2L);

        Assert.assertNotNull(result);
        Assert.assertEquals(0L, result.getDatabaseId());
        Assert.assertFalse(result.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfoForLockUserRequired() {
        databaseEntity.setConnectionId(1L);
        Mockito.when(databaseRepository.findById(2L)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(2L);

        Assert.assertNotNull(result);
        Assert.assertEquals(2L, result.getDatabaseId());
        Assert.assertTrue(result.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfoForLockUserNotRequired() {
        databaseEntity.setConnectionId(2L);
        Mockito.when(databaseRepository.findById(2L)).thenReturn(Optional.of(databaseEntity));

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(2L);

        Assert.assertNotNull(result);
        Assert.assertEquals(2L, result.getDatabaseId());
        Assert.assertFalse(result.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTableForNonExistentScheduleTask() {
        Mockito.when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            oscService.swapTable(1L);
        });
    }

    @Test
    public void testSwapTableForNonExistentSchedule() {
        scheduleTaskEntity.setJobName("2");
        Mockito.when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
        Mockito.when(scheduleRepository.findById(2L)).thenReturn(Optional.empty());

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            oscService.swapTable(1L);
        });
    }

    @Test
    public void testSwapTableForNonManualSwapTableType() {
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_FAILED);
        Mockito.when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
        Mockito.when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            oscService.swapTable(1L);
        });
    }

    @Test
    public void testSwapTableForNotEnabledManualSwapTable() {
        oscParameters.setSwapTableType(SwapTableType.AUTOMATIC);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        Mockito.when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
        Mockito.when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            oscService.swapTable(1L);
        });
    }

    @Test
    public void testSwapTableForValidManualSwapTable() {
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        oscResult.setManualSwapTableEnabled(true);
        oscResult.setManualSwapTableStarted(false);
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(oscResult));
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        Mockito.when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
        Mockito.when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

        OscSwapTableVO result = oscService.swapTable(1L);

        Assert.assertNotNull(result);
        Assert.assertEquals(1L, result.getScheduleTaskId());
    }
}