package com.oceanbase.odc.service.onlineschemachange;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

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
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOscDatabaseInfo() {
        Long id = 1L;
        Optional<DatabaseEntity> database = Optional.of(new DatabaseEntity());
        when(databaseRepository.findById(id)).thenReturn(database);

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        assertEquals(id, result.getDatabaseId());
        assertTrue(result.isLockDatabaseUserRequired());

        verify(databaseRepository, times(1)).findById(id);
    }

    @Test
    public void testGetOscDatabaseInfoWithNonExistentDatabase() {
        Long id = 1L;
        when(databaseRepository.findById(id)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(id);

        assertNull(result.getDatabaseId());
        assertFalse(result.isLockDatabaseUserRequired());

        verify(databaseRepository, times(1)).findById(id);
    }

    @Test
    @Transactional(rollbackFor = Exception.class)
    public void testSwapTable() {
        Long scheduleTaskId = 1L;
        ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
        scheduleTask.setId(scheduleTaskId);
        scheduleTask.setJobName("1");
        OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);
        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(new ScheduleEntity()));
        when(JsonUtils.fromJson(anyString(), any())).thenReturn(oscParameters).thenReturn(result);

        OscSwapTableVO swapTableVO = oscService.swapTable(scheduleTaskId);

        assertEquals(scheduleTaskId, swapTableVO.getScheduleTaskId());

        verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
        verify(scheduleRepository, times(1)).findById(anyLong());
        verify(JsonUtils, times(2)).fromJson(anyString(), any());
        verify(scheduleTaskRepository, times(1)).updateTaskResult(scheduleTaskId, anyString());
    }

    @Test
    public void testGetLockUserIsRequired() {
        Long connectionId = 1L;
        ConnectionConfig connectionConfig = new ConnectionConfig();
        ConnectionSessionFactory factory = mock(ConnectionSessionFactory.class);
        ConnectionSession session = mock(ConnectionSession.class);
        when(factory.generateSession()).thenReturn(session);
        ConnectionSessionUtil.setVersion(session, "1.0");

        PreConditions preConditions = mock(PreConditions.class);
        when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
                .thenReturn(connectionConfig);
        when(preConditions.validArgumentState(anyBoolean(), anyInt(), any(), anyString()))
                .thenReturn(true);
        when(preConditions.validExists(any(), any(), any(), any()))
                .thenReturn(true);

        oscService.getLockUserIsRequired(connectionId);

        verify(connectionService, times(1)).getForConnectionSkipPermissionCheck(connectionId);
        verify(preConditions, times(1)).validArgumentState(anyBoolean(), anyInt(), any(), anyString());
        verify(preConditions, times(1)).validExists(any(), any(), any(), any());
    }
}