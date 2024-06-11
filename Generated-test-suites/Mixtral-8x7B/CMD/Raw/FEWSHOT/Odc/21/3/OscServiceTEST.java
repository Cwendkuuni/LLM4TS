package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.connection.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.schedule.model.JobType;

@Transactional
public class OscServiceTest {

 private OscService oscService;

 @Before
 public void setup() {
 DatabaseRepository databaseRepository = mock(DatabaseRepository.class);
 ConnectionService connectionService = mock(ConnectionService.class);
 ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 ScheduleRepository scheduleRepository = mock(ScheduleRepository.class);

 oscService = new OscService();
 oscService.databaseRepository = databaseRepository;
 oscService.connectionService = connectionService;
 oscService.scheduleTaskRepository = scheduleTaskRepository;
 oscService.scheduleRepository = scheduleRepository;
 }

 @Test
 public void testGetOscDatabaseInfo_nonexistentDatabase_returnsEmptyOscLockDatabaseUserInfo() {
 Optional<DatabaseEntity> database = Optional.empty();
 when(databaseRepository.findById(1L)).thenReturn(database);

 OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);

 assertNotNull(result);
 assertEquals(0, result.getDatabaseId());
 assertFalse(result.isLockDatabaseUserRequired());
 }

 @Test
 public void testGetOscDatabaseInfo_existingDatabase_returnsOscLockDatabaseUserInfo() {
 DatabaseEntity database = new DatabaseEntity();
 database.setDatabaseId(1L);
 database.setConnectionId(2L);

 Optional<DatabaseEntity> databaseOptional = Optional.of(database);
 when(databaseRepository.findById(1L)).thenReturn(databaseOptional);

 OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);

 assertNotNull(result);
 assertEquals(1, result.getDatabaseId());
 assertTrue(result.isLockDatabaseUserRequired());
 }

 @Test
 public void testSwapTable_nonexistentScheduleTask_throwsIllegalArgumentException() {
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.empty());

 try {
 oscService.swapTable(1L);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 assertEquals(ErrorCodes.BadArgument, e.getErrorCode());
 }
 }

 @Test
 public void testSwapTable_nonexistentSchedule_throwsIllegalArgumentException() {
 when(scheduleRepository.findById(1L)).thenReturn(Optional.empty());

 try {
 oscService.swapTable(1L);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 assertEquals(ErrorCodes.BadArgument, e.getErrorCode());
 }
 }

 @Test
 public void testSwapTable_incorrectJobType_throwsIllegalArgumentException() {
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobType(JobType.BACKUP);

 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

 try {
 oscService.swapTable(1L);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 assertEquals(ErrorCodes.BadArgument, e.getErrorCode());
 }
 }

 @Test
 public void testSwapTable_incorrectSwapTableType_throwsIllegalArgumentException() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSwapTableType(SwapTableType.AUTO);

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(true);

 when(scheduleRepository.findById(1L))
 .thenReturn(Optional.of(scheduleEntity));
 when(scheduleTaskRepository.findById(1L))
 .thenReturn(Optional.of(scheduleTask));
 when(scheduleTask.getResultJson())
 .thenReturn(JsonUtils.toJson(result));

 try {
 oscService.swapTable(1L);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 assertEquals(ErrorCodes.BadArgument, e.getErrorCode());
 }
 }

 @Test
 public void testSwapTable_disabledManualSwapTable_throwsIllegalArgumentException() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSwapTableType(SwapTableType.MANUAL);

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(false);

 when(scheduleRepository.findById(1L))
 .thenReturn(Optional.of(scheduleEntity));
 when(scheduleTaskRepository.findById(1L))
 .thenReturn(Optional.of(scheduleTask));
 when(scheduleTask.getResultJson())
 .thenReturn(JsonUtils.toJson(result));

 try {
 oscService.swapTable(1L);
 fail("Expected IllegalArgumentException to be thrown");
 } catch (IllegalArgumentException e) {
 assertEquals(ErrorCodes.BadArgument, e.getErrorCode());
 }
 }

 @Test
 public void testSwapTable_enabledManualSwapTable_returnsOscSwapTableVO() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSwapTableType(SwapTableType.MANUAL);

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(true);
 result.setManualSwapTableStarted(true);

 when(scheduleRepository.findById(1L))
 .thenReturn(Optional.of(scheduleEntity));
 when(scheduleTaskRepository.findById(1L))
 .thenReturn(Optional.of(scheduleTask));
 when(scheduleTask.getResultJson())
 .thenReturn(JsonUtils.toJson(result));

 OscSwapTableVO resultVO = oscService.swapTable(1L);

 assertNotNull(resultVO);
 assertEquals(1, resultVO.getScheduleTaskId());
 }
}