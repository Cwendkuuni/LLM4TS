package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.core.shared.PreConditions;
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

@Transactional
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
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testGetOscDatabaseInfoShouldReturnEmptyOscDatabaseWhenDatabaseDoesNotExist() {
 Optional<DatabaseEntity> databaseOptional = Optional.empty();
 when(databaseRepository.findById(anyLong())).thenReturn(databaseOptional);

 OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);

 assertEquals(0, oscDatabase.getDatabaseId());
 assertFalse(oscDatabase.isLockDatabaseUserRequired());
 }

 @Test
 public void testGetOscDatabaseInfoShouldReturnOscDatabaseWithRequiredLockDatabaseUserWhenDatabaseExists() {
 Long connectionId = 1L;
 Long databaseId = 2L;
 boolean isLockDatabaseUserRequired = true;

 DatabaseEntity databaseEntity = new DatabaseEntity();
 databaseEntity.setConnectionId(connectionId);
 databaseEntity.setDatabaseId(databaseId);

 Optional<DatabaseEntity> databaseOptional = Optional.of(databaseEntity);
 when(databaseRepository.findById(anyLong())).thenReturn(databaseOptional);

 when(oscService.getLockUserIsRequired(connectionId)).thenReturn(isLockDatabaseUserRequired);

 OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);

 assertEquals(databaseId, oscDatabase.getDatabaseId());
 assertTrue(oscDatabase.isLockDatabaseUserRequired());
 }

 @Test
 public void testSwapTableShouldThrowExceptionWhenScheduleTaskDoesNotExist() {
 Long scheduleTaskId = 1L;

 Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.empty();
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(scheduleTaskOptional);

 try {
 oscService.swapTable(scheduleTaskId);
 } catch (IllegalArgumentException e) {
 return;
 }

 assertTrue(false);
 }

 @Test
 public void testSwapTableShouldThrowExceptionWhenScheduleDoesNotExist() {
 Long scheduleTaskId = 1L;
 Long scheduleId = 2L;

 Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(new ScheduleTaskEntity());
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(scheduleTaskOptional);
 when(scheduleTaskOptional.get().getJobName()).thenReturn(String.valueOf(scheduleId));

 Optional<ScheduleEntity> scheduleEntityOptional = Optional.empty();
 when(scheduleRepository.findById(anyLong())).thenReturn(scheduleEntityOptional);

 try {
 oscService.swapTable(scheduleTaskId);
 } catch (IllegalArgumentException e) {
 return;
 }

 assertTrue(false);
 }

 @Test
 public void testSwapTableShouldThrowExceptionWhenJobTypeIsNotOnlineSchemaChangeComplete() {
 Long scheduleTaskId = 1L;
 Long scheduleId = 2L;
 JobType jobType = JobType.SCHEDULE;

 Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(new ScheduleTaskEntity());
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(scheduleTaskOptional);
 when(scheduleTaskOptional.get().getJobName()).thenReturn(String.valueOf(scheduleId));

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobType(jobType);
 Optional<ScheduleEntity> scheduleEntityOptional = Optional.of(scheduleEntity);
 when(scheduleRepository.findById(anyLong())).thenReturn(scheduleEntityOptional);

 try {
 oscService.swapTable(scheduleTaskId);
 } catch (IllegalArgumentException e) {
 return;
 }

 assertTrue(false);
 }

 @Test
 public void testSwapTableShouldThrowExceptionWhenSwapTableTypeIsNotManual() {
 Long scheduleTaskId = 1L;
 Long scheduleId = 2L;
 JobType jobType = JobType.ONLINE_SCHEMA_CHANGE_COMPLETE;
 SwapTableType swapTableType = SwapTableType.AUTO;

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(swapTableType);

 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();

 Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(new ScheduleTaskEntity());
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(scheduleTaskOptional);
 when(scheduleTaskOptional.get().getJobName()).thenReturn(String.valueOf(scheduleId));

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobType(jobType);
 Optional<ScheduleEntity> scheduleEntityOptional = Optional.of(scheduleEntity);
 when(scheduleRepository.findById(anyLong())).thenReturn(scheduleEntityOptional);

 when(JsonUtils.fromJson(anyString(), OnlineSchemaChangeParameters.class))
 .thenReturn(oscParameters);
 when(JsonUtils.fromJson(anyString(), OnlineSchemaChangeScheduleTaskResult.class))
 .thenReturn(result);

 try {
 oscService.swapTable(scheduleTaskId);
 } catch (IllegalArgumentException e) {
 return;
 }

 assertTrue(false);
 }

 @Test
 public void testSwapTableShouldThrowExceptionWhenManualSwapTableIsNotEnabled() {
 Long scheduleTaskId = 1L;
 Long scheduleId = 2L;
 JobType jobType = JobType.ONLINE_SCHEMA_CHANGE_COMPLETE;
 SwapTableType swapTableType = SwapTableType.MANUAL;

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(swapTableType);

 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(false);

 Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(new ScheduleTaskEntity());
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(scheduleTaskOptional);
 when(scheduleTaskOptional.get().getJobName()).thenReturn(String.valueOf(scheduleId));

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobType(jobType);
 Optional<ScheduleEntity> scheduleEntityOptional = Optional.of(scheduleEntity);
 when(scheduleRepository.findById(anyLong())).thenReturn(scheduleEntityOptional);

 when(JsonUtils.fromJson(anyString(), OnlineSchemaChangeParameters.class))
 .thenReturn(oscParameters);
 when(JsonUtils.fromJson(anyString(), OnlineSchemaChangeScheduleTaskResult.class))
 .thenReturn(result);

 try {
 oscService.swapTable(scheduleTaskId);
 } catch (IllegalArgumentException e) {
 return;
 }

 assertTrue(false);
 }

 @Test
 public void testSwapTableShouldDisableManualSwapTableAndEnableStartManualSwapTable() {
 Long scheduleTaskId = 1L;
 Long scheduleId = 2L;
 JobType jobType = JobType.ONLINE_SCHEMA_CHANGE_COMPLETE;
 SwapTableType swapTableType = SwapTableType.MANUAL;

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(swapTableType);

 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(true);

 Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(new ScheduleTaskEntity());
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(scheduleTaskOptional);
 when(scheduleTaskOptional.get().getJobName()).thenReturn(String.valueOf(scheduleId));

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobType(jobType);
 Optional<ScheduleEntity> scheduleEntityOptional = Optional.of(scheduleEntity);
 when(scheduleRepository.findById(anyLong())).thenReturn(scheduleEntityOptional);

 when(JsonUtils.fromJson(anyString(), OnlineSchemaChangeParameters.class))
 .thenReturn(oscParameters);
 when(JsonUtils.fromJson(anyString(), OnlineSchemaChangeScheduleTaskResult.class))
 .thenReturn(result);

 when(scheduleTaskRepository.updateTaskResult(anyLong(), anyString()))
 .thenReturn(null);

 oscService.swapTable(scheduleTaskId);

 verify(scheduleTaskRepository, times(1))
 .updateTaskResult(anyLong(), anyString());
 }

 @Test
 public void testGetLockUserIsRequiredShouldReturnTrueWhenLockUserIsRequired() {
 Long connectionId = 1L;

 ConnectionConfig connectionConfig = new ConnectionConfig();

 ConnectionSessionFactory factory = mock(ConnectionSessionFactory.class);
 ConnectionSession connectionSession = mock(ConnectionSession.class);
 when(factory.generateSession()).thenReturn(connectionSession);

 when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
 .thenReturn(connectionConfig);

 when(connectionConfig.getDialectType()).thenReturn("MySQL");

 doNothing().when(connectionSession).expire();
 when(ConnectionSessionUtil.getVersion(connectionSession)).thenReturn("5.7");

 boolean isLockUserRequired = oscService.getLockUserIsRequired(connectionId);

 assertTrue(isLockUserRequired);
 }

 @Test
 public void testGetLockUserIsRequiredShouldReturnFalseWhenLockUserIsNotRequired() {
 Long connectionId = 1L;

 ConnectionConfig connectionConfig = new ConnectionConfig();

 ConnectionSessionFactory factory = mock(ConnectionSessionFactory.class);
 ConnectionSession connectionSession = mock(ConnectionSession.class);
 when(factory.generateSession()).thenReturn(connectionSession);

 when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
 .thenReturn(connectionConfig);

 when(connectionConfig.getDialectType()).thenReturn("MySQL");

 doNothing().when(connectionSession).expire();
 when(ConnectionSessionUtil.getVersion(connectionSession)).thenReturn("8.0");

 boolean isLockUserRequired = oscService.getLockUserIsRequired(connectionId);

 assertFalse(isLockUserRequired);
 }

 @Test
 public void testGetLockUserIsRequiredShouldReturnFalseWhenDialectTypeIsNotMySQL() {
 Long connectionId = 1L;

 ConnectionConfig connectionConfig = new ConnectionConfig();

 ConnectionSessionFactory factory = mock(ConnectionSessionFactory.class);
 ConnectionSession connectionSession = mock(ConnectionSession.class);
 when(factory.generateSession()).thenReturn(connectionSession);

 when(connectionService.getForConnectionSkipPermissionCheck(connectionId))
 .thenReturn(connectionConfig);

 when(connectionConfig.getDialectType()).thenReturn("PostgreSQL");

 doNothing().when(connectionSession).expire();
 when(ConnectionSessionUtil.getVersion(connectionSession)).thenReturn("9.5");

 boolean isLockUserRequired = oscService.getLockUserIsRequired(connectionId);

 assertFalse(isLockUserRequired);
 }

 @Test
 public void testGetLockUserIsRequiredShouldReturnFalseWhenConnectionIsNull() {
 Long connectionId = null;

 boolean isLockUserRequired = oscService.getLockUserIsRequired(connectionId);

 assertFalse(isLockUserRequired);
 }
}