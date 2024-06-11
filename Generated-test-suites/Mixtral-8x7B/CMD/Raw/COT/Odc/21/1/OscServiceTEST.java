import com.oceanbase.odc.core.shared.constant.ErrorCodes;
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

import static org.mockito.ArgumentMatchers.any;
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
        ReflectionTestUtils.setField(oscService, "taskType", TaskType.ONLINE_SCHEMA_CHANGE);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        Optional<DatabaseEntity> databaseEntityOptional = Optional.of(new DatabaseEntity());
        when(databaseRepository.findById(id)).thenReturn(databaseEntityOptional);

        OscLockDatabaseUserInfo oscLockDatabaseUserInfo = oscService.getOscDatabaseInfo(id);

        Assert.assertEquals(1L, oscLockDatabaseUserInfo.getDatabaseId());
    }

    @Test
    public void testGetOscDatabaseInfoEmptyDatabaseEntity() {
        Long id = 1L;
        Optional<DatabaseEntity> databaseEntityOptional = Optional.empty();
        when(databaseRepository.findById(id)).thenReturn(databaseEntityOptional);

        OscLockDatabaseUserInfo oscLockDatabaseUserInfo = oscService.getOscDatabaseInfo(id);

        Assert.assertEquals(0L, oscLockDatabaseUserInfo.getDatabaseId());
    }

    @Test
    public void testSwapTable() throws Exception {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        Optional<ScheduleEntity> scheduleEntityOptional = Optional.of(new ScheduleEntity());
        when(scheduleRepository.findById(any())).thenReturn(scheduleEntityOptional);

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        String oscParametersJson = JsonUtils.toJson(oscParameters);
        when(scheduleEntityOptional.get().getJobParametersJson()).thenReturn(oscParametersJson);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        String resultJson = JsonUtils.toJson(result);
        when(scheduleTaskEntity.getResultJson()).thenReturn(resultJson);

        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);

        Assert.assertEquals(scheduleTaskId, oscSwapTable.getScheduleTaskId());
    }

    @Test
    public void testSwapTableNonManualSwapTableType() throws Exception {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        Optional<ScheduleEntity> scheduleEntityOptional = Optional.of(new ScheduleEntity());
        when(scheduleRepository.findById(any())).thenReturn(scheduleEntityOptional);

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.AUTO);
        String oscParametersJson = JsonUtils.toJson(oscParameters);
        when(scheduleEntityOptional.get().getJobParametersJson()).thenReturn(oscParametersJson);

        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);

        Mockito.verify(scheduleTaskRepository, Mockito.times(0)).updateTaskResult(any(), any());
    }

    @Test
    public void testSwapTableManualSwapTableNotEnabled() throws Exception {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setJobName("1");
        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

        Optional<ScheduleEntity> scheduleEntityOptional = Optional.of(new ScheduleEntity());
        when(scheduleRepository.findById(any())).thenReturn(scheduleEntityOptional);

        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        String oscParametersJson = JsonUtils.toJson(oscParameters);
        when(scheduleEntityOptional.get().getJobParametersJson()).thenReturn(oscParametersJson);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(false);
        String resultJson = JsonUtils.toJson(result);
        when(scheduleTaskEntity.getResultJson()).thenReturn(resultJson);

        OscSwapTableVO oscSwapTable = oscService.swapTable(scheduleTaskId);

        Mockito.verify(scheduleTaskRepository, Mockito.times(0)).updateTaskResult(any(), any());
    }
}