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
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
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

    private OnlineSchemaChangeScheduleTaskResult oscResult;

    @Before
    public void setUp() {
        databaseEntity = mock(DatabaseEntity.class);
        scheduleEntity = mock(ScheduleEntity.class);
        scheduleTaskEntity = mock(ScheduleTaskEntity.class);
        oscParameters = mock(OnlineSchemaChangeParameters.class);
        oscResult = mock(OnlineSchemaChangeScheduleTaskResult.class);

        ReflectionTestUtils.setField(oscService, "databaseRepository", databaseRepository);
        ReflectionTestUtils.setField(oscService, "connectionService", connectionService);
        ReflectionTestUtils.setField(oscService, "scheduleTaskRepository", scheduleTaskRepository);
        ReflectionTestUtils.setField(oscService, "scheduleRepository", scheduleRepository);

        oscService = spy(oscService);
    }

    @Test
    public void testGetOscDatabaseInfo_whenDatabaseNotPresent_thenReturnEmptyOscDatabase() {
        doReturn(Optional.empty()).when(databaseRepository).findById(anyLong());

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);

        Assert.assertEquals(1L, oscDatabase.getDatabaseId());
        Assert.assertFalse(oscDatabase.isLockDatabaseUserRequired());

        verify(databaseRepository, times(1)).findById(anyLong());
        verify(oscService, times(0)).getLockUserIsRequired(anyLong());
    }

    @Test
    public void testGetOscDatabaseInfo_whenDatabasePresent_thenReturnOscDatabase() {
        doReturn(Optional.of(databaseEntity)).when(databaseRepository).findById(anyLong());
        doReturn(true).when(oscService).getLockUserIsRequired(anyLong());

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);

        Assert.assertEquals(1L, oscDatabase.getDatabaseId());
        Assert.assertTrue(oscDatabase.isLockDatabaseUserRequired());

        verify(databaseRepository, times(1)).findById(anyLong());
        verify(oscService, times(1)).getLockUserIsRequired(anyLong());
    }

    @Test
    public void testSwapTable_whenScheduleTaskNotFound_thenThrowException() {
        doReturn(Optional.empty()).when(scheduleTaskRepository).findById(anyLong());

        try {
            oscService.swapTable(1L);
            Assert.fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Schedule task not found", e.getMessage());
        }

        verify(scheduleTaskRepository, times(1)).findById(anyLong());
        verify(scheduleRepository, times(0)).findById(anyLong());
        verify(oscService, times(0)).getLockUserIsRequired(anyLong());
    }

    @Test
    public void testSwapTable_whenScheduleNotFound_thenThrowException() {
        doReturn(Optional.of(scheduleTaskEntity)).when(scheduleTaskRepository).findById(anyLong());
        doReturn(Optional.empty()).when(scheduleRepository).findById(anyLong());

        try {
            oscService.swapTable(1L);
            Assert.fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Schedule not found", e.getMessage());
        }

        verify(scheduleTaskRepository, times(1)).findById(anyLong());
        verify(scheduleRepository, times(1)).findById(anyLong());
        verify(oscService, times(0)).getLockUserIsRequired(anyLong());
    }

    @Test
    public void testSwapTable_whenParametersAndResultNotValid_thenThrowException() {
        doReturn(Optional.of(scheduleTaskEntity)).when(scheduleTaskRepository).findById(anyLong());
        doReturn(Optional.of(scheduleEntity)).when(scheduleRepository).findById(anyLong());

        OnlineSchemaChangeParameters invalidOscParameters = mock(OnlineSchemaChangeParameters.class);
        OnlineSchemaChangeScheduleTaskResult invalidOscResult = mock(OnlineSchemaChangeScheduleTaskResult.class);

        doReturn(invalidOscParameters).when(scheduleEntity).getOnlineSchemaChangeParameters();
        doReturn(invalidOscResult).when(scheduleTaskEntity).getOnlineSchemaChangeScheduleTaskResult();

        try {
            oscService.swapTable(1L);
            Assert.fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Parameters or result not valid", e.getMessage());
        }

        verify(scheduleTaskRepository, times(1)).findById(anyLong());
        verify(scheduleRepository, times(1)).findById(anyLong());
        verify(oscService, times(0)).getLockUserIsRequired(anyLong());
    }

    @Test
    public void testSwapTable_whenSwapTableTypeNotManual_thenThrowException() {
        doReturn(Optional.of(scheduleTaskEntity)).when(scheduleTaskRepository).findById(anyLong());
        doReturn(Optional.of(scheduleEntity)).when(scheduleRepository).findById(anyLong());

        OnlineSchemaChangeParameters validOscParameters = mock(OnlineSchemaChangeParameters.class);
        OnlineSchemaChangeScheduleTaskResult validOscResult = mock(OnlineSchemaChangeScheduleTaskResult.class);

        SwapTableType swapTableType = SwapTableType.AUTOMATIC;
        doReturn(swapTableType).when(validOscParameters).getSwapTableType();

        doReturn(validOscParameters).when(scheduleEntity).getOnlineSchemaChangeParameters();
        doReturn(validOscResult).when(scheduleTaskEntity).getOnlineSchemaChangeScheduleTaskResult();

        try {
            oscService.swapTable(1L);
            Assert.fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Swap table type is not MANUAL", e.getMessage());
        }

        verify(scheduleTaskRepository, times(1)).findById(anyLong());
        verify(scheduleRepository, times(1)).findById(anyLong());
        verify(oscService, times(0)).getLockUserIsRequired(anyLong());
    }

    @Test
    public void testSwapTable_whenManualSwapTableNotEnabled_thenThrowException() {
        doReturn(Optional.of(scheduleTaskEntity)).when(scheduleTaskRepository).findById(anyLong());
        doReturn(Optional.of(scheduleEntity)).when(scheduleRepository).findById(anyLong());

        OnlineSchemaChangeParameters validOscParameters = mock(OnlineSchemaChangeParameters.class);
        OnlineSchemaChangeScheduleTaskResult validOscResult = mock(OnlineSchemaChangeScheduleTaskResult.class);

        SwapTableType swapTableType = SwapTableType.MANUAL;
        doReturn(swapTableType).when(validOscParameters).getSwapTableType();
        doReturn(false).when(validOscResult).isManualSwapTableEnabled();

        doReturn(validOscParameters).when(scheduleEntity).getOnlineSchemaChangeParameters();
        doReturn(validOscResult).when(scheduleTaskEntity).getOnlineSchemaChangeScheduleTaskResult();

        try {
            oscService.swapTable(1L);
            Assert.fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Manual Swap table type is not enable ", e.getMessage());
        }

        verify(scheduleTaskRepository, times(1)).findById(anyLong());
        verify(scheduleRepository, times(1)).findById(anyLong());
        verify(oscService, times(0)).getLockUserIsRequired(anyLong());
    }

    @Test
    public void testSwapTable_whenParametersAndResultValid_thenSwapTable() {
        doReturn(Optional.of(scheduleTaskEntity)).when(scheduleTaskRepository).findById(anyLong());
        doReturn(Optional.of(scheduleEntity)).when(scheduleRepository).findById(anyLong());

        OnlineSchemaChangeParameters validOscParameters = mock(OnlineSchemaChangeParameters.class);
        OnlineSchemaChangeScheduleTaskResult validOscResult = mock(OnlineSchemaChangeScheduleTaskResult.class);

        SwapTableType swapTableType = SwapTableType.MANUAL;
        doReturn(swapTableType).when(validOscParameters).getSwapTableType();
        doReturn(true).when(validOscResult).isManualSwapTableEnabled();

        doReturn(validOscParameters).when(scheduleEntity).getOnlineSchemaChangeParameters();
        doReturn(validOscResult).when(scheduleTaskEntity).getOnlineSchemaChangeScheduleTaskResult();

        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);

        Assert.assertNotNull(oscSwapTable);
        Assert.assertEquals(1L, oscSwapTable.getScheduleTaskId());

        verify(scheduleTaskRepository, times(1)).findById(anyLong());
        verify(scheduleRepository, times(1)).findById(anyLong());
        verify(oscService, times(1)).getLockUserIsRequired(anyLong());
    }
}