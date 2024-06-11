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
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;

@Transactional(rollbackFor = Exception.class)
public class OscServiceTest {

 private OscService oscService;
 private DatabaseRepository databaseRepository;
 private ConnectionService connectionService;
 private ScheduleTaskRepository scheduleTaskRepository;
 private ScheduleRepository scheduleRepository;
 private ConnectionSessionFactory connectionSessionFactory;
 private ConnectionSession connectionSession;
 private ConnectionConfig connectionConfig;

 @Before
 public void setup() {
 databaseRepository = mock(DatabaseRepository.class);
 connectionService = mock(ConnectionService.class);
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 scheduleRepository = mock(ScheduleRepository.class);
 connectionSessionFactory = mock(ConnectionSessionFactory.class);
 connectionSession = mock(ConnectionSession.class);
 connectionConfig = mock(ConnectionConfig.class);

 oscService = new OscService();
 oscService.databaseRepository = databaseRepository;
 oscService.connectionService = connectionService;
 oscService.scheduleTaskRepository = scheduleTaskRepository;
 oscService.scheduleRepository = scheduleRepository;
 oscService.connectionSessionFactory = connectionSessionFactory;

 doNothing().when(connectionSessionFactory).expire();
 when(connectionSessionFactory.generateSession()).thenReturn(connectionSession);
 when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(connectionConfig);
 }

 @Test
 public void testGetOscDatabaseInfo_whenDatabaseNotFound_returnsEmptyOscLockDatabaseUserInfo() {
 PreConditions validExists = mock(PreConditions.class);
 when(databaseRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
 oscService.databaseRepository = databaseRepository;
 oscService.preConditions = validExists;

 OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
 assertEquals(0, result.getDatabaseId());
 assertFalse(result.isLockDatabaseUserRequired());
 }

 @Test
 public void testGetOscDatabaseInfo_whenDatabaseFound_returnsOscLockDatabaseUserInfo() {
 PreConditions validExists = mock(PreConditions.class);
 DatabaseEntity databaseEntity = new DatabaseEntity();
 databaseEntity.setConnectionId(1L);
 when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(databaseEntity));
 oscService.databaseRepository = databaseRepository;
 oscService.preConditions = validExists;

 OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
 assertEquals(1, result.getDatabaseId());
 assertTrue(result.isLockDatabaseUserRequired());
 }

 @Test
 public void testSwapTable_whenScheduleTaskNotFound_throwsException() {
 PreConditions validExists = mock(PreConditions.class);
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
 oscService.scheduleTaskRepository = scheduleTaskRepository;
 oscService.preConditions = validExists;

 try {
 oscService.swapTable(1L);
 } catch (IllegalArgumentException e) {
 assertEquals("schedule task not found", e.getMessage());
 }
 }

 @Test
 public void testSwapTable_whenScheduleNotFound_throwsException() {
 PreConditions validExists = mock(PreConditions.class);
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setJobName("1");
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTask));
 when(scheduleRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
 oscService.scheduleRepository = scheduleRepository;
 oscService.scheduleTaskRepository = scheduleTaskRepository;
 oscService.preConditions = validExists;

 try {
 oscService.swapTable(1L);
 } catch (IllegalArgumentException e) {
 assertEquals("schedule not found", e.getMessage());
 }
 }

 @Test
 public void testSwapTable_whenJobTypeNotONLINE_SCHEMA_CHANGE_COMPLETE_throwsException() {
 PreConditions validExists = mock(PreConditions.class);
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_START);
 when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
 oscService.scheduleRepository = scheduleRepository;
 oscService.preConditions = validExists;

 try {
 oscService.swapTable(1L);
 } catch (IllegalArgumentException e) {
 assertEquals("Task type is not ONLINE_SCHEMA_CHANGE", e.getMessage());
 }
 }

 @Test
 public void testSwapTable_whenSwapTableTypeNotMANUAL_throwsException() {
 PreConditions validExists = mock(PreConditions.class);
 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.AUTO);
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setResultJson("{}");
 scheduleTask.setJobName(String.valueOf(scheduleEntity.getId()));
 when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTask));
 when(JsonUtils.fromJson(anyString(), any())).thenReturn(oscParameters);
 oscService.scheduleRepository = scheduleRepository;
 oscService.scheduleTaskRepository = scheduleTaskRepository;
 oscService.preConditions = validExists;

 try {
 oscService.swapTable(1L);
 } catch (IllegalArgumentException e) {
 assertEquals("Swap table type is not MANUAL", e.getMessage());
 }
 }

 @Test
 public void testSwapTable_whenManualSwapTableNotEnabled_throwsException() {
 PreConditions validExists = mock(PreConditions.class);
 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setResultJson("{\"manualSwapTableEnabled\":false}");
 scheduleTask.setJobName(String.valueOf(scheduleEntity.getId()));
 when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTask));
 when(JsonUtils.fromJson(anyString(), any())).thenReturn(oscParameters);
 oscService.scheduleRepository = scheduleRepository;
 oscService.scheduleTaskRepository = scheduleTaskRepository;
 oscService.preConditions = validExists;

 try {
 oscService.swapTable(1L);
 } catch (IllegalArgumentException e) {
 assertEquals("Manual Swap table type is not enable ", e.getMessage());
 }
 }

 @Test
 public void testSwapTable_whenEverythingIsFine_returnsOscSwapTableVO() {
 PreConditions validExists = mock(PreConditions.class);
 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setResultJson("{\"manualSwapTableEnabled\":true,\"manualSwapTableStarted\":false}");
 scheduleTask.setJobName(String.valueOf(scheduleEntity.getId()));
 when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTask));
 when(JsonUtils.fromJson(anyString(), any())).thenReturn(oscParameters);
 when(JsonUtils.fromJson(anyString(), any())).thenReturn(new OnlineSchemaChangeScheduleTaskResult());
 oscService.scheduleRepository = scheduleRepository;
 oscService.scheduleTaskRepository = scheduleTaskRepository;
 oscService.preConditions = validExists;

 OscSwapTableVO result = oscService.swapTable(1L);
 assertEquals(1L, result.getScheduleTaskId().longValue());
 }

 @Test
 public void testGetLockUserIsRequired_whenConnectionNotFound_returnsFalse() {
 PreConditions validExists = mock(PreConditions.class);
 when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(null);
 oscService.connectionService = connectionService;
 oscService.preConditions = validExists;

 boolean result = oscService.getLockUserIsRequired(1L);
 assertFalse(result);
 }

 @Test
 public void testGetLockUserIsRequired_whenConnectionFound_returnsTrue() {
 PreConditions validExists = mock(PreConditions.class);
 when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(connectionConfig);
 when(ConnectionSessionUtil.getVersion(connectionSession)).thenReturn("1.0.0");
 oscService.connectionService = connectionService;
 oscService.connectionSessionFactory = connectionSessionFactory;
 oscService.connectionSession = connectionSession;
 oscService.preConditions = validExists;

 boolean result = oscService.getLockUserIsRequired(1L);
 assertTrue(result);
 }
}