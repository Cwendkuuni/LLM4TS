package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;

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
import com.oceanbase.odc.service.session.factory.DefaultConnectSessionFactory;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = {})
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
        JsonUtils.setMapper(new CustomMapper());
    }

    @Test
    public void testGetOscDatabaseInfo_idNotPresent() {
        when(databaseRepository.findById(1L)).thenReturn(Optional.empty());
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
        assertNull(result);
    }

    @Test
    public void testGetOscDatabaseInfo_lockDatabaseUserRequired() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setConnectionId(2L);
        when(databaseRepository.findById(3L)).thenReturn(Optional.of(databaseEntity));
        when(connectionService.getForConnectionSkipPermissionCheck(2L))
                .thenReturn(new ConnectionConfig("dialectType"));
        when(OscDBUserUtil.isLockUserRequired("dialectType",
                () -> DefaultConnectSessionFactory.generateSession()))
                        .thenReturn(true);
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(3L);
        assertTrue(result.getLockDatabaseUserRequired());
    }

    @Test
    public void testGetOscDatabaseInfo_lockDatabaseUserNotRequired() {
        DatabaseEntity databaseEntity = new DatabaseEntity();
        databaseEntity.setConnectionId(4L);
        when(databaseRepository.findById(5L)).thenReturn(Optional.of(databaseEntity));
        when(connectionService.getForConnectionSkipPermissionCheck(4L))
                .thenReturn(new ConnectionConfig("dialectType"));
        when(OscDBUserUtil.isLockUserRequired("dialectType",
                () -> DefaultConnectSessionFactory.generateSession()))
                        .thenReturn(false);
        OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(5L);
        assertFalse(result.getLockDatabaseUserRequired());
    }

    @Test
    public void testSwapTable_scheduleTaskIdNotPresent() {
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());
        OnlineSchemaChangeScheduleTaskResult result = oscService.swapTable(1L);
        assertNull(result);
    }

    @Test
    public void testSwapTable_scheduleTaskComplete() {
        OnlineSchemaChangeParameters oscParameters =
                new OnlineSchemaChangeParameters();
        oscParameters.setSwapTableType(SwapTableType.MANUAL);

        OnlineSchemaChangeScheduleTaskResult result =
                new OnlineSchemaChangeScheduleTaskResult();
        result.setManualSwapTableEnabled(true);

        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setResultJson(JsonUtils.toJson(result));
        scheduleTaskEntity.setJobName("6");

        when(scheduleTaskRepository.findById(7L)).thenReturn(Optional.of(scheduleTaskEntity));
        when(scheduleRepository.findById(6L)).thenReturn(Optional.of(new ScheduleEntity()));

        PreConditions
                .validArgumentState(
                        scheduleEntity.get().getJobType() == JobType.ONLINE_SCHEMA_CHANGE_COMPLETE,
                        ErrorCodes.BadArgument, new Object[] {scheduleEntity.get().getJobType()},
                        "Task type is not " + TaskType.ONLINE_SCHEMA_CHANGE.name());

        SwapTableType swapTableType = oscParameters.getSwapTableType();
        PreConditions.validArgumentState(swapTableType == SwapTableType.MANUAL,
                ErrorCodes.BadArgument, new Object[] {oscParameters.getSwapTableType()},
                "Swap table type is not " + SwapTableType.MANUAL.name());

        PreConditions.validArgumentState(result.isManualSwapTableEnabled(),
                ErrorCodes.BadArgument, new Object[] {result.isManualSwapTableEnabled()},
                "Manual Swap table type is not enable ");

        OscSwapTableVO resultVO = oscService.swapTable(7L);
        assertNull(resultVO);
    }

    // Add more test cases for edge cases and exception handling as needed

    // 
}