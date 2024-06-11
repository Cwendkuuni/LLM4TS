package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
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
import com.oceanbase.odc.service.connection.ConnectionService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
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
 @Mock
 private ConnectionSessionFactory connectionSessionFactory;
 @Mock
 private ConnectionSession connectionSession;
 @Mock
 private ConnectionSessionUtil connectionSessionUtil;
 @Mock
 private JsonUtils jsonUtils;
 @Mock
 private PreConditions preConditions;
 @Mock
 private SkipAuthorize skipAuthorize;
 @Mock
 private OscDBUserUtil oscDBUserUtil;

 @InjectMocks
 private OscService oscService;

 @Before
 public void init() {
 MockitoAnnotations.openMocks(this);
 }

 @Test
 public void testGetOscDatabaseInfo_whenDatabaseIdIsNull_thenReturnEmptyOscLockDatabaseUserInfo() {
 OscLockDatabaseUserInfo oscLockDatabaseUserInfo = oscService.getOscDatabaseInfo(null);
 assertEquals(0, oscLockDatabaseUserInfo.getDatabaseId());
 assertFalse(oscLockDatabaseUserInfo.isLockDatabaseUserRequired());
 }

 @Test
 public void testGetOscDatabaseInfo_whenDatabaseDoesNotExist_thenReturnEmptyOscLockDatabaseUserInfo() {
 Long databaseId = 1L;
 Optional<DatabaseEntity> databaseEntityOptional = Optional.empty();
 when(databaseRepository.findById(databaseId)).thenReturn(databaseEntityOptional);
 OscLockDatabaseUserInfo oscLockDatabaseUserInfo = oscService.getOscDatabaseInfo(databaseId);
 assertEquals(0, oscLockDatabaseUserInfo.getDatabaseId());
 assertFalse(oscLockDatabaseUserInfo.isLockDatabaseUserRequired());
 }

 @Test
 public void testGetOscDatabaseInfo_whenDatabaseExists_thenReturnOscLockDatabaseUserInfo() {
 Long databaseId = 1L;
 DatabaseEntity databaseEntity = new DatabaseEntity();
 databaseEntity.setDatabaseId(databaseId);
 databaseEntity.setConnectionId(1L);
 Optional<DatabaseEntity> databaseEntityOptional = Optional.of(databaseEntity);
 when(databaseRepository.findById(databaseId)).thenReturn(databaseEntityOptional);
 when(oscService.getLockUserIsRequired(1L)).thenReturn(true);
 OscLockDatabaseUserInfo oscLockDatabaseUserInfo = oscService.getOscDatabaseInfo(databaseId);
 assertEquals(databaseId, oscLockDatabaseUserInfo.getDatabaseId());
 assertTrue(oscLockDatabaseUserInfo.isLockDatabaseUserRequired());
 }

 @Test
 public void testSwapTable_whenScheduleTaskDoesNotExist_thenThrowException() {
 Long scheduleTaskId = 1L;
 when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.empty());
 try {
 oscService.swapTable(scheduleTaskId);
 } catch (IllegalArgumentException e) {
 assertEquals("Schedule task not found", e.getMessage());
 }
 }

 @Test
 public void testSwapTable_whenScheduleTaskExists_thenSwapTable() {
 Long scheduleTaskId = 1L;
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(scheduleTaskId);
 scheduleTaskEntity.setJobName("1");
 when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobParametersJson(jsonUtils.toJson(oscParameters));
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(true);
 when(jsonUtils.fromJson(any(), any())).thenReturn(result);

 doNothing().when(scheduleTaskRepository).updateTaskResult(anyLong(), any());

 oscService.swapTable(scheduleTaskId);

 verify(scheduleTaskRepository, times(1)).findById(scheduleTaskId);
 verify(jsonUtils, times(1)).fromJson(any(), any());
 verify(scheduleTaskRepository, times(1)).updateTaskResult(anyLong(), any());
 }

 @Test
 public void testSwapTable_whenLockUserIsNotRequired_thenReturnOscSwapTableVO() {
 Long scheduleTaskId = 1L;
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(scheduleTaskId);
 scheduleTaskEntity.setJobName("1");
 when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobParametersJson(jsonUtils.toJson(oscParameters));
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(false);
 when(jsonUtils.fromJson(any(), any())).thenReturn(result);

 OscSwapTableVO oscSwapTable = new OscSwapTableVO();
 oscSwapTable.setScheduleTaskId(scheduleTaskId);
 when(oscService.swapTableInternal(anyLong())).thenReturn(oscSwapTable);

 assertEquals(oscSwapTable, oscService.swapTable(scheduleTaskId));
 }

 @Test
 public void testSwapTable_whenLockUserIsRequired_thenThrowException() {
 Long scheduleTaskId = 1L;
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(scheduleTaskId);
 scheduleTaskEntity.setJobName("1");
 when(scheduleTaskRepository.findById(scheduleTaskId)).thenReturn(Optional.of(scheduleTaskEntity));

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobParametersJson(jsonUtils.toJson(oscParameters));
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(true);
 when(jsonUtils.fromJson(any(), any())).thenReturn(result);

 when(oscService.getLockUserIsRequired(anyLong())).thenReturn(true);

 try {
 oscService.swapTable(scheduleTaskId);
 } catch (IllegalArgumentException e) {
 assertEquals("Lock user is required for this operation", e.getMessage());
 }
 }

 private OnlineSchemaChangeScheduleTaskResult swapTableInternal(Long scheduleTaskId) {
 return new OnlineSchemaChangeScheduleTaskResult();
 }

 private boolean getLockUserIsRequired(Long connectionId) {
 return false;
 }
}