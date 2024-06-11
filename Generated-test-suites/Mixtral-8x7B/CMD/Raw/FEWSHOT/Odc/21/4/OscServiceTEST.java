package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;

@Transactional(rollbackFor = Exception.class)
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
 public void testGetOscDatabaseInfo_whenDatabaseNotFound_thenReturnEmptyOscDatabase() {
 Optional<DatabaseEntity> databaseOptional = Optional.empty();
 when(oscService.databaseRepository.findById(any())).thenReturn(databaseOptional);

 OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);

 assertEquals(1L, oscDatabase.getDatabaseId());
 assertNull(oscDatabase.getLockDatabaseUserRequired());
 }

 @Test
 public void testGetOscDatabaseInfo_whenDatabaseFound_thenReturnOscDatabaseWithLockDatabaseUserRequired() {
 DatabaseEntity databaseEntity = new DatabaseEntity();
 databaseEntity.setConnectionId(1L);
 Optional<DatabaseEntity> databaseOptional = Optional.of(databaseEntity);
 when(oscService.databaseRepository.findById(any())).thenReturn(databaseOptional);

 when(oscService.getLockUserIsRequired(any())).thenReturn(true);

 OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);

 assertEquals(1L, oscDatabase.getDatabaseId());
 assertEquals(true, oscDatabase.getLockDatabaseUserRequired());
 }

 @Test
 public void testSwapTable_whenScheduleTaskNotFound_thenThrowException() {
 when(oscService.scheduleTaskRepository.findById(any())).thenReturn(Optional.empty());

 try {
 oscService.swapTable(1L);
 } catch (IllegalArgumentException ex) {
 assertEquals(ErrorCodes.BadArgument, ex.getErrorCode());
 return;
 }

 assertEquals(false, true);
 }

 @Test
 public void testSwapTable_whenScheduleTaskFound_thenUpdateTaskResultAndReturnOscSwapTableVO() {
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setJobName("1");
 Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(scheduleTaskEntity);
 when(oscService.scheduleTaskRepository.findById(any())).thenReturn(scheduleTaskOptional);

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
 when(oscService.scheduleRepository.findById(any())).thenReturn(Optional.of(scheduleEntity));

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);
 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(true);

 when(JsonUtils.fromJson(any(), any())).thenReturn(oscParameters).thenReturn(result);

 OscSwapTableVO oscSwapTable = oscService.swapTable(1L);

 assertEquals(1L, oscSwapTable.getScheduleTaskId());
 }

 private boolean getLockUserIsRequired(Long connectionId) {
 // Mock implementation
 return false;
 }
}