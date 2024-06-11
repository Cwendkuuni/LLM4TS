package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.connection.ConnectionConfig;
import com.oceanbase.odc.core.connection.DatabaseEntity;
import com.oceanbase.odc.core.connection.DatabaseRepository;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

public class OscServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ConnectionService connectionService;

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private ConnectionSessionFactory connectionSessionFactory;

    @Mock
    private ConnectionSession connectionSession;

    private OscService oscService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        oscService = new OscService();
        oscService.databaseRepository = databaseRepository;
        oscService.connectionService = connectionService;
        oscService.scheduleTaskRepository = scheduleTaskRepository;
        oscService.scheduleRepository = scheduleRepository;
        oscService.connectionSessionFactory = connectionSessionFactory;
    }

    @Test
    public void testGetOscDatabaseInfo_nonexistentDatabase() {
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);

        assertNull(result.getDatabaseId());
        assertFalse(result.isLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfo_existingDatabase() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setDatabaseId(1L);
        databaseEntity.setConnectionId(2L);

        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(databaseEntity));
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong()))
                .thenReturn(new ConnectionConfig());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);

        assertEquals(databaseEntity.getDatabaseId(), result.getDatabaseId());
        assertTrue(result.isLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable_nonexistentScheduleTask() {
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

        OscSwapTableVO result = oscService.swapTable(1L);

        assertNull(result);
    }

    @Test
    public void testSwapTable_nonexistentSchedule() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("1");

        when(scheduleTaskRepository.findById(anyLong()))
                .thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.empty());

        OscSwapTableVO result = oscService.swapTable(1L);

        assertNull(result);
    }

    @Test
    public void testSwapTable_invalidJobType() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("1");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.SCHEDULE_TYPE_1);

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();

        when(scheduleTaskRepository.findById(anyLong()))
                .thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(anyLong()))
                .thenReturn(Optional.of(scheduleEntity));
        when(JsonUtils.fromJson(anyString(), OnlineSchemaChangeParameters.class))
                .thenReturn(parameters);
        when(JsonUtils.fromJson(anyString(), OnlineSchemaChangeScheduleTaskResult.class))
                .thenReturn(result);

        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);

        assertNull(oscSwapTable);
    }

    @Test
    public void testSwapTable_invalidSwapTableType() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("1");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSwapTableType(SwapTableType.AUTO);

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();

        when(scheduleTaskRepository.findById(anyLong()))
                .thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(anyLong()))
                .thenReturn(Optional.of(scheduleEntity));
        when(JsonUtils.fromJson(anyString(), OnlineSchemaChangeParameters.class))
                .thenReturn(parameters);
        when(JsonUtils.fromJson(anyString(), OnlineSchemaChangeScheduleTaskResult.class))
                .thenReturn(result);

        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);

        assertNull(oscSwapTable);
    }

    @Test
    public void testSwapTable_success() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("1");

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult initialResult =
                new OnlineSchemaChangeScheduleTaskResult();
        initialResult.setManualSwapTableEnabled(true);

        OnlineSchemaChangeScheduleTaskResult updatedResult =
                new OnlineSchemaChangeScheduleTaskResult();
        updatedResult.setManualSwapTableEnabled(false);
        updatedResult.setManualSwapTableStarted(true);

        when(scheduleTaskRepository.findById(anyLong()))
                .thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(anyLong()))
                .thenReturn(Optional.of(scheduleEntity));
        when(JsonUtils.fromJson(anyString(), OnlineSchemaChangeParameters.class))
                .thenReturn(parameters);
        when(JsonUtils.fromJson(anyString(), OnlineSchemaChangeScheduleTaskResult.class))
                .thenReturn(initialResult)
                .thenReturn(updatedResult);

        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);

        assertNotNull(oscSwapTable);
        assertEquals(1L, oscSwapTable.getScheduleTaskId());

        ArgumentCaptor<OnlineSchemaChangeScheduleTaskResult> captor =
                ArgumentCaptor.forClass(OnlineSchemaChangeScheduleTaskResult.class);
        verify(scheduleTaskRepository)
                .updateTaskResult(eq(1L), captor.capture());

        OnlineSchemaChangeScheduleTaskResult taskResult = captor.getValue();
        assertEquals(updatedResult.getManualSwapTableStarted(), taskResult.getManualSwapTableStarted());
    }

}