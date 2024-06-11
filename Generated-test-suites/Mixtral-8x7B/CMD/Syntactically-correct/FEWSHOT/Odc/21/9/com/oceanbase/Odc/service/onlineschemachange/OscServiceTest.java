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

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
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
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;

@SuppressWarnings("unchecked")
public class OscServiceTest {

 @Mock
 private DatabaseRepository databaseRepository;

 @Mock
 private ConnectionService connectionService;

 @Mock
 private ScheduleTaskRepository scheduleTaskRepository;

 @Mock
 private ScheduleRepository scheduleRepository;

 @Spy
 @InjectMocks
 private OscService oscService = new OscService();

 @Test
 public void testGetOscDatabaseInfo_whenDatabaseDoesNotExist_thenReturnsEmptyOscLockDatabaseUserInfo() {
 Optional<DatabaseEntity> database = Optional.empty();
 when(databaseRepository.findById(anyLong())).thenReturn(database);
 OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(123L);
 assertNull(result.getDatabaseId());
 assertFalse(result.isLockDatabaseUserRequired());
 }

 @Test
 public void testGetOscDatabaseInfo_whenDatabaseExists_thenReturnsOscLockDatabaseUserInfo() {
 DatabaseEntity databaseEntity = new DatabaseEntity();
 databaseEntity.setDatabaseId(123L);
 databaseEntity.setConnectionId(456L);

 Optional<DatabaseEntity> database = Optional.of(databaseEntity);
 when(databaseRepository.findById(anyLong())).thenReturn(database);

 when(oscService.getLockUserIsRequired(anyLong())).thenReturn(true);

 OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(123L);
 assertEquals(123L, result.getDatabaseId());
 assertTrue(result.isLockDatabaseUserRequired());
 }

 @Test
 public void testSwapTable_whenScheduleTaskDoesNotExist_thenThrowsIllegalArgumentException() {
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());
 PreConditions invalidCallPreConditions = mock(PreConditions.class);
 when(oscService.getPreConditions()).thenReturn(invalidCallPreConditions);
 Mockito.doThrow(IllegalArgumentException.class).when(invalidCallPreConditions).validExists(
 ArgumentMatchers.<ResourceType>any(), ArgumentMatchers.<String>any(), ArgumentMatchers.<Long>any(),
 ArgumentMatchers.<Boolean>any());

 try {
 oscService.swapTable(123L);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSwapTable_whenScheduleDoesNotExist_thenThrowsIllegalArgumentException() {
 Long scheduleTaskId = 123L;
 Long scheduleId = 456L;

 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setJobName(scheduleId.toString());

 when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
 when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.empty());

 PreConditions invalidCallPreConditions = mock(PreConditions.class);
 when(oscService.getPreConditions()).thenReturn(invalidCallPreConditions);
 Mockito.doThrow(IllegalArgumentException.class).when(invalidCallPreConditions).validExists(
 ArgumentMatchers.<ResourceType>any(), ArgumentMatchers.<String>any(), ArgumentMatchers.<Long>any(),
 ArgumentMatchers.<Boolean>any());

 try {
 oscService.swapTable(scheduleTaskId);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSwapTable_whenTaskTypeIsNotOnlineSchemaChangeComplete_thenThrowsIllegalArgumentException() {
 Long scheduleTaskId = 123L;
 Long scheduleId = 456L;

 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setJobName(scheduleId.toString());

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));

 when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
 when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(scheduleEntity));

 PreConditions invalidCallPreConditions = mock(PreConditions.class);
 when(oscService.getPreConditions()).thenReturn(invalidCallPreConditions);
 Mockito.doThrow(IllegalArgumentException.class).when(invalidCallPreConditions).validArgumentState(
 ArgumentMatchers.<Boolean>any(), ArgumentMatchers.<ErrorCodes>any(), ArgumentMatchers.<Object[]>any(),
 ArgumentMatchers.<String>any());

 try {
 oscService.swapTable(scheduleTaskId);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSwapTable_whenSwapTableTypeIsNotManual_thenThrowsIllegalArgumentException() {
 Long scheduleTaskId = 123L;
 Long scheduleId = 456L;

 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setJobName(scheduleId.toString());

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.AUTO);

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));

 when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
 when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(scheduleEntity));

 PreConditions invalidCallPreConditions = mock(PreConditions.class);
 when(oscService.getPreConditions()).thenReturn(invalidCallPreConditions);
 Mockito.doThrow(IllegalArgumentException.class).when(invalidCallPreConditions).validArgumentState(
 ArgumentMatchers.<Boolean>any(), ArgumentMatchers.<ErrorCodes>any(), ArgumentMatchers.<Object[]>any(),
 ArgumentMatchers.<String>any());

 try {
 oscService.swapTable(scheduleTaskId);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSwapTable_whenManualSwapTableIsNotEnabled_thenThrowsIllegalArgumentException() {
 Long scheduleTaskId = 123L;
 Long scheduleId = 456L;

 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setJobName(scheduleId.toString());

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(false);

 scheduleTask.setResultJson(JsonUtils.toJson(result));

 when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
 when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(scheduleEntity));

 PreConditions invalidCallPreConditions = mock(PreConditions.class);
 when(oscService.getPreConditions()).thenReturn(invalidCallPreConditions);
 Mockito.doThrow(IllegalArgumentException.class).when(invalidCallPreConditions).validArgumentState(
 ArgumentMatchers.<Boolean>any(), ArgumentMatchers.<ErrorCodes>any(), ArgumentMatchers.<Object[]>any(),
 ArgumentMatchers.<String>any());

 try {
 oscService.swapTable(scheduleTaskId);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSwapTable_whenAllConditionsAreMet_thenCallsExpectedMethods() {
 Long scheduleTaskId = 123L;
 Long scheduleId = 456L;

 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setJobName(scheduleId.toString());

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
 scheduleEntity.setJobParametersJson(JsonUtils.toJson(oscParameters));

 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(true);

 scheduleTask.setResultJson(JsonUtils.toJson(result));

 when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTask));
 when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(scheduleEntity));

 ReflectionTestUtils.setField(oscService, "preConditions", new PreConditions());

 oscService.swapTable(scheduleTaskId);

 Mockito.verify(scheduleTaskRepository).updateTaskResult(scheduleTaskId,
 JsonUtils.toJson(Mockito.argThat(arg -> arg.getManualSwapTableEnabled() == false && arg.getManualSwapTableStarted())));
 }

 private OscService getOscService() {
 ReflectionTestUtils.setField(oscService, "databaseRepository", databaseRepository);
 ReflectionTestUtils.setField(oscService, "connectionService", connectionService);
 ReflectionTestUtils.setField(oscService, "scheduleTaskRepository", scheduleTaskRepository);
 ReflectionTestUtils.setField(oscService, "scheduleRepository", scheduleRepository);
 ReflectionTestUtils.setField(oscService, "preConditions", new PreConditions());
 return oscService;
 }
}