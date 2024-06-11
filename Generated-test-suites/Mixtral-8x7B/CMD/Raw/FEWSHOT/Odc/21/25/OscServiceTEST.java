package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
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
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

@SuppressWarnings("unchecked")
public class OscServiceTest {

 private OscService oscService;
 private DatabaseRepository databaseRepository;
 private ConnectionService connectionService;
 private ScheduleTaskRepository scheduleTaskRepository;
 private ScheduleRepository scheduleRepository;

 @Before
 public void setUp() {
 databaseRepository = mock(DatabaseRepository.class);
 connectionService = mock(ConnectionService.class);
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 scheduleRepository = mock(ScheduleRepository.class);

 oscService = new OscService();
 oscService.databaseRepository = databaseRepository;
 oscService.connectionService = connectionService;
 oscService.scheduleTaskRepository = scheduleTaskRepository;
 oscService.scheduleRepository = scheduleRepository;
 }

 @Test
 public void testGetOscDatabaseInfo_NonExistentDatabaseId() {
 OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);
 assertNull(oscDatabase.getDatabaseId());
 assertFalse(oscDatabase.isLockDatabaseUserRequired());
 }

 @Test
 public void testGetOscDatabaseInfo_ExistentDatabaseId() {
 DatabaseEntity databaseEntity = new DatabaseEntity();
 databaseEntity.setDatabaseId(2L);
 databaseEntity.setConnectionId(3L);

 Optional<DatabaseEntity> optionalDatabaseEntity = Optional.of(databaseEntity);
 when(databaseRepository.findById(anyLong())).thenReturn(optionalDatabaseEntity);

 OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(2L);
 assertEquals(2L, (long) oscDatabase.getDatabaseId());
 assertTrue(oscDatabase.isLockDatabaseUserRequired());
 }

 @Test(expected = AssertionError.class)
 @Transactional
 @SkipAuthorize("internal authenticated")
 public void testSwapTable_NonExistentScheduleTaskId_AssertionError() {
 oscService.swapTable(1L);
 }

 @Test
 @Transactional
 @SkipAuthorize("internal authenticated")
 public void testSwapTable_ExistentScheduleTaskId() {
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("1");

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));

 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(true);
 result.setManualSwapTableStarted(false);

 Optional<ScheduleTaskEntity> optionalScheduleTaskEntity = Optional.of(scheduleTaskEntity);
 Optional<ScheduleEntity> optionalScheduleEntity = Optional.of(scheduleEntity);

 when(scheduleTaskRepository.findById(anyLong())).thenReturn(optionalScheduleTaskEntity);
 when(scheduleRepository.findById(anyLong())).thenReturn(optionalScheduleEntity);
 when(JsonUtils.fromJson(anyString(), eq(OnlineSchemaChangeParameters.class)))
 .thenReturn(oscParameters);
 when(JsonUtils.fromJson(anyString(), eq(OnlineSchemaChangeScheduleTaskResult.class)))
 .thenReturn(result);

 OscSwapTableVO oscSwapTable = oscService.swapTable(1L);
 assertEquals(1L, (long) oscSwapTable.getScheduleTaskId());
 }

 @Test
 public void testGetLockUserIsRequired_True() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType("MySQL");

 ConnectionSessionFactory factory = mock(ConnectionSessionFactory.class);
 ConnectionSession session = mock(ConnectionSession.class);
 String version = "5.7.34";
 when(factory.generateSession()).thenReturn(session);
 when(ConnectionSessionUtil.getVersion(session)).thenReturn(version);

 when(connectionService.getForConnectionSkipPermissionCheck(anyLong()))
 .thenReturn(connectionConfig);
 when(connectionService.generateSession(connectionConfig)).thenReturn(factory);

 assertTrue(oscService.getLockUserIsRequired(1L));
 }

 @Test
 public void testGetLockUserIsRequired_False() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType("PostgreSQL");

 ConnectionSessionFactory factory = mock(ConnectionSessionFactory.class);
 ConnectionSession session = mock(ConnectionSession.class);
 String version = "12.5";
 when(factory.generateSession()).thenReturn(session);
 when(ConnectionSessionUtil.getVersion(session)).thenReturn(version);

 when(connectionService.getForConnectionSkipPermissionCheck(anyLong()))
 .thenReturn(connectionConfig);
 when(connectionService.generateSession(connectionConfig)).thenReturn(factory);

 assertFalse(oscService.getLockUserIsRequired(1L));
 }
}