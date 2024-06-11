
package com.oceanbase.odc.service.onlineschemachange;

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
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import static org.assertj.core.api.Assertions.assertThat;
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
        ReflectionTestUtils.setField(oscService, "lockUserRequired", false);
    }

    @Test
    public void testGetOscDatabaseInfo_idDoesNotExist_returnsEmptyOscLockDatabaseUserInfo() {
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.empty());
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);
        assertThat(oscDatabase.getDatabaseId()).isEqualTo(0);
        assertThat(oscDatabase.isLockDatabaseUserRequired()).isFalse();
    }

    @Test
    public void testGetOscDatabaseInfo_validId_returnsOscLockDatabaseUserInfo() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setConnectionId(1L);
        databaseEntity.setDatabaseId(2L);
        when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(databaseEntity));
        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);
        assertThat(oscDatabase.getDatabaseId()).isEqualTo(2L);
        assertThat(oscDatabase.isLockDatabaseUserRequired()).isEqualTo(false);
    }

    @Test
    public void testGetLockUserIsRequired_returnsTrue() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong()))
                .thenReturn(connectionConfig);
        when(ConnectionSessionUtil.getVersion(any(ConnectionSession.class)))
                .thenReturn("8.0");
        boolean lockUserRequired = oscService.getLockUserIsRequired(1L);
        assertThat(lockUserRequired).isTrue();
    }

    @Test
    public void testGetLockUserIsRequired_returnsFalse() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("PostgreSQL");
        when(connectionService.getForConnectionSkipPermissionCheck(anyLong()))
                .thenReturn(connectionConfig);
        when(ConnectionSessionUtil.getVersion(any(ConnectionSession.class)))
                .thenReturn("13.0");
        boolean lockUserRequired = oscService.getLockUserIsRequired(1L);
        assertThat(lockUserRequired).isFalse();
    }

    @Test
    public void testSwapTable_scheduleTaskDoesNotExist_throwsException() {
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());
        PreConditions.validExists(any(), any(), anyLong(), any(PreConditions.Predicate.class));
        assertThatThrownBy(() -> oscService.swapTable(1L)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testSwapTable_scheduleTaskExists() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("1");
        when(scheduleTaskRepository.findById(anyLong()))
                .thenReturn(Optional.of(scheduleTaskEntity));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson(
                JsonUtils.toJson(new OnlineSchemaChangeParameters()));
        when(scheduleRepository.findById(anyLong()))
                .thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeScheduleTaskResult result =
                new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        when(JsonUtils.fromJson(anyString(), any()))
                .thenReturn(result);

        OscSwapTableVO oscSwapTable = oscService.swapTable(1L);
        assertThat(oscSwapTable.getScheduleTaskId()).isEqualTo(1L);
    }

    @Test
    public void testSwapTable_taskTypeIsNotComplete_throwsException() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("1");
        when(scheduleTaskRepository.findById(anyLong()))
                .thenReturn(Optional.of(scheduleTaskEntity));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_START);
        scheduleEntity.setJobParametersJson(
                JsonUtils.toJson(new OnlineSchemaChangeParameters()));
        when(scheduleRepository.findById(anyLong()))
                .thenReturn(Optional.of(scheduleEntity));

        PreConditions.validArgumentState(
                any(), any(), any(), any(PreConditions.Predicate.class));
        assertThatThrownBy(() -> oscService.swapTable(1L)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testSwapTable_swapTableTypeIsNotManual_throwsException() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("1");
        when(scheduleTaskRepository.findById(anyLong()))
                .thenReturn(Optional.of(scheduleTaskEntity));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson(
                JsonUtils.toJson(new OnlineSchemaChangeParameters()));
        when(scheduleRepository.findById(anyLong()))
                .thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeScheduleTaskResult result =
                new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);
        when(JsonUtils.fromJson(anyString(), any()))
                .thenReturn(result);

        PreConditions.validArgumentState(
                any(), any(), any(), any(PreConditions.Predicate.class));
        assertThatThrownBy(() -> oscService.swapTable(1L)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testSwapTable_manualSwapTableIsNotEnabled_throwsException() {
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setJobName("1");
        when(scheduleTaskRepository.findById(anyLong()))
                .thenReturn(Optional.of(scheduleTaskEntity));

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(1L);
        scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
        scheduleEntity.setJobParametersJson(
                JsonUtils.toJson(new OnlineSchemaChangeParameters()));
        when(scheduleRepository.findById(anyLong()))
                .thenReturn(Optional.of(scheduleEntity));

        OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
        when(JsonUtils.fromJson(anyString(), any()))
                .thenReturn(result);

        PreConditions.validArgumentState(
                any(), any(), any(), any(PreConditions.Predicate.class));
        assertThatThrownBy(() -> oscService.swapTable(1L)).isInstanceOf(IllegalArgumentException.class);
    }
}
