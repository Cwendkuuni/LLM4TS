import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.schedule.model.JobType;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

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
        ReflectionTestUtils.setField(oscService, "skipAuthorize", true);
    }

    @Test
    public void testGetOscDatabaseInfoWithEmptyDatabaseId() {
        Optional<DatabaseEntity> database = Optional.empty();
        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(0L);
        Assert.assertEquals(0L, oscDatabaseInfo.getDatabaseId());
        Assert.assertFalse(oscDatabaseInfo.isLockDatabaseUserRequired());
        Mockito.verify(databaseRepository, Mockito.times(1)).findById(0L);
    }

    @Test
    public void testGetOscDatabaseInfoWithNonEmptyDatabaseId() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        databaseEntity.setConnectionId(2L);
        Optional<DatabaseEntity> database = Optional.of(databaseEntity);
        Mockito.when(databaseRepository.findById(1L)).thenReturn(database);

        OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(1L);
        Assert.assertEquals(1L, oscDatabaseInfo.getDatabaseId());
        Assert.assertTrue(oscDatabaseInfo.isLockDatabaseUserRequired());
        Mockito.verify(databaseRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    public void testSwapTableWithInvalidScheduleTaskId() {
        Mockito.when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

        OscSwapTableVO oscSwapTableVO = oscService.swapTable(1L);

        Assert.assertNull(oscSwapTableVO);
        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(scheduleRepository, Mockito.times(0)).findById(anyLong());
        Mockito.verify(databaseRepository, Mockito.times(0)).findById(anyLong());
    }

    @Test
    public void testSwapTableWithValidScheduleTaskId() {
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));
        scheduleTaskEntity.setJobName(String.valueOf(scheduleEntity.getScheduleId()));

        Mockito.when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
        Mockito.when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        OscSwapTableVO oscSwapTableVO = oscService.swapTable(1L);

        Assert.assertNotNull(oscSwapTableVO);
        Assert.assertEquals(1L, oscSwapTableVO.getScheduleTaskId());
        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(scheduleRepository, Mockito.times(1)).findById(anyLong());
        Mockito.verify(databaseRepository, Mockito.times(0)).findById(anyLong());
    }

    @Test(expected = IllegalStateException.class)
    public void testSwapTableWithInvalidSwapTableType() {
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.AUTO);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(new OnlineSchemaChangeScheduleTaskResult()));
        scheduleTaskEntity.setJobName(String.valueOf(scheduleEntity.getScheduleId()));

        Mockito.when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
        Mockito.when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        oscService.swapTable(1L);
    }

    @Test(expected = IllegalStateException.class)
    public void testSwapTableWithDisabledManualSwapTable() {
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult taskResult = new OnlineSchemaChangeScheduleTaskResult();
        taskResult.setManualSwapTableEnabled(false);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(taskResult));
        scheduleTaskEntity.setJobName(String.valueOf(scheduleEntity.getScheduleId()));

        Mockito.when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
        Mockito.when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        oscService.swapTable(1L);
    }
}