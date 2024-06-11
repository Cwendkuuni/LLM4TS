package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
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
import com.oceanbase.odc.service.connection.model.ConnectionConfig.DialectType;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;

@Transactional
public class OscServiceTest {

 private OscService oscService;

 @Before
 public void setUp() {
 DatabaseRepository databaseRepository = mock(DatabaseRepository.class);
 ConnectionService connectionService = mock(ConnectionService.class);
 ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 ScheduleRepository scheduleRepository = mock(ScheduleRepository.class);

 ReflectionTestUtils.setField(oscService, "databaseRepository", databaseRepository);
 ReflectionTestUtils.setField(oscService, "connectionService", connectionService);
 ReflectionTestUtils.setField(oscService, "scheduleTaskRepository", scheduleTaskRepository);
 ReflectionTestUtils.setField(oscService, "scheduleRepository", scheduleRepository);
 }

 @Test
 public void testGetOscDatabaseInfo_databaseNotPresent_returnsEmptyOscDatabase() {
 DatabaseEntity databaseEntity = null;
 Optional<DatabaseEntity> databaseOptional = Optional.ofNullable(databaseEntity);

 when(databaseRepository.findById(anyLong())).thenReturn(databaseOptional);

 OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);

 assertNull(oscDatabase.getDatabaseId());
 assertFalse(oscDatabase.isLockDatabaseUserRequired());
 }

 @Test
 public void testGetOscDatabaseInfo_databasePresent_returnsOscDatabase() {
 DatabaseEntity databaseEntity = new DatabaseEntity();
 databaseEntity.setDatabaseId(1L);
 databaseEntity.setConnectionId(2L);
 Optional<DatabaseEntity> databaseOptional = Optional.of(databaseEntity);

 when(databaseRepository.findById(anyLong())).thenReturn(databaseOptional);

 OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);

 assertEquals(1L, oscDatabase.getDatabaseId());
 assertTrue(oscDatabase.isLockDatabaseUserRequired());
 }

 @Test
 public void testSwapTable_scheduleTaskNotPresent_throwsException() {
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

 try {
 oscService.swapTable(1L);
 } catch (IllegalArgumentException ex) {
 assertEquals(ErrorCodes.BadArgument, ex.getErrorCode());
 }
 }

 @Test
 public void testSwapTable_scheduleTaskPresent_updatesTaskResult() {
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setScheduleId(1L);
 scheduleTaskEntity.setResultJson("{\"manualSwapTableEnabled\":true}");

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));

 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
 when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
 when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(new DatabaseEntity()));

 doNothing().when(scheduleTaskRepository).updateTaskResult(anyLong(), anyString());

 oscService.swapTable(1L);

 verify(scheduleTaskRepository, times(1)).updateTaskResult(1L, anyString());
 }

 private ConnectionConfig createConnectionConfig(DialectType dialectType) {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(dialectType);
 return connectionConfig;
 }

 private ConnectionSession createConnectionSession(ConnectionConfig connectionConfig) {
 ConnectionSessionFactory factory = mock(ConnectionSessionFactory.class);
 ConnectionSession connectionSession = mock(ConnectionSession.class);

 when(factory.generateSession()).thenReturn(connectionSession);
 when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(connectionConfig);

 ReflectionTestUtils.setField(oscService, "connectionService", connectionService);
 ReflectionTestUtils.setField(oscService, "connectionSessionFactory", factory);

 return connectionSession;
 }

 @Test
 public void testGetLockUserIsRequired_connectionNotPresent_returnsFalse() {
 when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(null);

 boolean isLockUserRequired = oscService.getLockUserIsRequired(1L);

 assertFalse(isLockUserRequired);
 }

 @Test
 public void testGetLockUserIsRequired_connectionPresent_returnsTrue() {
 ConnectionConfig connectionConfig = createConnectionConfig(DialectType.MYSQL);
 ConnectionSession connectionSession = createConnectionSession(connectionConfig);

 when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(connectionConfig);

 boolean isLockUserRequired = oscService.getLockUserIsRequired(1L);

 assertTrue(isLockUserRequired);
 }
}