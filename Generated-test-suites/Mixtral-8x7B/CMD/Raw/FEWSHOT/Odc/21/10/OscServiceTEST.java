package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
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
 public void testGetOscDatabaseInfo_DatabaseNotPresent() {
 Optional<DatabaseEntity> databaseOptional = Optional.ofNullable(null);
 when(databaseRepository.findById(anyLong())).thenReturn(databaseOptional);
 OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
 assertNull(result.getDatabaseId());
 assertFalse(result.isLockDatabaseUserRequired());
 }

 @Test
 public void testGetOscDatabaseInfo_DatabasePresent() {
 DatabaseEntity databaseEntity = new DatabaseEntity();
 databaseEntity.setConnectionId(1L);
 databaseEntity.setDatabaseId(2L);
 Optional<DatabaseEntity> databaseOptional = Optional.of(databaseEntity);
 when(databaseRepository.findById(anyLong())).thenReturn(databaseOptional);
 when(oscService.getLockUserIsRequired(anyLong())).thenReturn(true);
 OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
 assertEquals(2L, (long) result.getDatabaseId());
 assertTrue(result.isLockDatabaseUserRequired());
 }

 @Test(expected = IllegalStateException.class)
 public void testSwapTable_ScheduleTaskNotPresent() {
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
 oscService.swapTable(1L);
 }

 @Test(expected = IllegalStateException.class)
 public void testSwapTable_ScheduleNotPresent() {
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setJobName("1");
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTask));
 when(scheduleRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
 oscService.swapTable(1L);
 }

 @Test
 public void testSwapTable_ValidParameters() {
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setJobName("1");
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTask));

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
 when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);
 String jsonParameters = JsonUtils.toJson(oscParameters);
 when(scheduleEntity.getJobParametersJson()).thenReturn(jsonParameters);

 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(true);
 String jsonResult = JsonUtils.toJson(result);
 scheduleTask.setResultJson(jsonResult);

 when(scheduleTaskRepository.updateTaskResult(anyLong(), any())).thenReturn(null);

 OscSwapTableVO oscSwapTable = oscService.swapTable(1L);
 assertEquals(1L, (long) oscSwapTable.getScheduleTaskId());
 }

 @Test(expected = IllegalStateException.class)
 public void testSwapTable_InvalidSwapTableType() {
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setJobName("1");
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTask));

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
 when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.AUTO);
 String jsonParameters = JsonUtils.toJson(oscParameters);
 when(scheduleEntity.getJobParametersJson()).thenReturn(jsonParameters);

 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(true);
 String jsonResult = JsonUtils.toJson(result);
 scheduleTask.setResultJson(jsonResult);

 when(scheduleTaskRepository.updateTaskResult(anyLong(), any())).thenReturn(null);

 oscService.swapTable(1L);
 }

 @Test
 public void testSwapTable_ManualSwapTableDisabled() {
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setJobName("1");
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTask));

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
 when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);
 String jsonParameters = JsonUtils.toJson(oscParameters);
 when(scheduleEntity.getJobParametersJson()).thenReturn(jsonParameters);

 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(false);
 String jsonResult = JsonUtils.toJson(result);
 scheduleTask.setResultJson(jsonResult);

 when(scheduleTaskRepository.updateTaskResult(anyLong(), any())).thenReturn(null);

 try {
 oscService.swapTable(1L);
 } catch (IllegalStateException e) {
 assertEquals(ErrorCodes.BadArgument, e.getErrorCode());
 }
 }

 @Test
 public void testGetLockUserIsRequired_ValidVersion_ReturnsTrue() {
 when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(new ConnectionConfig());
 when(connectionService.getVersion(any())).thenReturn("10.2.3-GA");
 assertTrue(oscService.getLockUserIsRequired(1L));
 }

 @Test
 public void testGetLockUserIsRequired_InvalidVersion_ReturnsFalse() {
 when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(new ConnectionConfig());
 when(connectionService.getVersion(any())).thenReturn("1.2.3-GA");
 assertFalse(oscService.getLockUserIsRequired(1L));
 }

 @Test
 public void testGetLockUserIsRequired_NullVersion_ReturnsFalse() {
 when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(new ConnectionConfig());
 when(connectionService.getVersion(any())).thenReturn(null);
 assertFalse(oscService.getLockUserIsRequired(1L));
 }
}