package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

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
 @Mock
 private ConnectionSessionFactory connectionSessionFactory;
 @Mock
 private ConnectionSession connectionSession;
 @Mock
 private ConnectionSessionUtil connectionSessionUtil;
 @Mock
 private OscDBUserUtil oscDBUserUtil;
 @Mock
 private JsonUtils jsonUtils;

 @InjectMocks
 private OscService oscService;

 @Before
 public void setUp() {
 MockitoAnnotations.openMocks(this);
 ReflectionTestUtils.setField(oscService, "skipAuthorize", new SkipAuthorize());
 }

 @Test
 public void testGetOscDatabaseInfo_whenDatabaseIdIsAbsent_thenReturnsEmptyOscLockDatabaseUserInfo() {
 Optional<DatabaseEntity> databaseOptional = Optional.ofNullable(null);
 when(databaseRepository.findById(anyLong())).thenReturn(databaseOptional);
 OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);
 assertEquals(0, oscDatabase.getDatabaseId());
 assertEquals(false, oscDatabase.isLockDatabaseUserRequired());
 }

 @Test
 public void testGetOscDatabaseInfo_whenDatabaseIdIsPresent_thenReturnsOscLockDatabaseUserInfo() {
 DatabaseEntity databaseEntity = new DatabaseEntity();
 databaseEntity.setDatabaseId(1L);
 databaseEntity.setConnectionId(2L);

 Optional<DatabaseEntity> databaseOptional = Optional.of(databaseEntity);
 when(databaseRepository.findById(1L)).thenReturn(databaseOptional);

 OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);
 assertEquals(1, oscDatabase.getDatabaseId());
 assertEquals(true, oscDatabase.isLockDatabaseUserRequired());
 }

 @Test
 public void testSwapTable_whenScheduleTaskIdIsAbsent_thenThrowsException() {
 Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.ofNullable(null);
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(scheduleTaskOptional);
 assertThrows(IllegalArgumentException.class, () -> oscService.swapTable(1L));
 }

 @Test
 public void testSwapTable_whenScheduleTaskIdIsPresent_thenSwapsTable() {
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(1L);
 scheduleTask.setJobName("1");

 Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(scheduleTask);
 when(scheduleTaskRepository.findById(1L)).thenReturn(scheduleTaskOptional);

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);

 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(true);
 result.setManualSwapTableStarted(true);

 when(jsonUtils.fromJson(anyString(), eq(OnlineSchemaChangeParameters.class)))
 .thenReturn(oscParameters);
 when(jsonUtils.fromJson(anyString(), eq(OnlineSchemaChangeScheduleTaskResult.class)))
 .thenReturn(result);

 oscService.swapTable(1L);
 verify(scheduleTaskRepository, times(1)).updateTaskResult(1L, anyString());
 }

 @Test
 public void testGetLockUserIsRequired_whenConnectionIdIsAbsent_thenReturnsFalse() {
 PreConditions preConditions = new PreConditions();
 ReflectionTestUtils.setField(preConditions, "errorCodes", new ErrorCodes());
 ReflectionTestUtils.setField(preConditions, "resourceType", new ResourceType());

 when(connectionService.getForConnectionSkipPermissionCheck(1L))
 .thenReturn(new ConnectionConfig());

 when(oscDBUserUtil.isLockUserRequired(anyString(), any()))
 .thenReturn(false);

 boolean lockUserRequired = preConditions.getLockUserIsRequired(oscService, 1L);
 assertFalse(lockUserRequired);
 }

 @Test
 public void testGetLockUserIsRequired_whenConnectionIdIsPresent_thenReturnsLockUserRequired() {
 PreConditions preConditions = new PreConditions();
 ReflectionTestUtils.setField(preConditions, "errorCodes", new ErrorCodes());
 ReflectionTestUtils.setField(preConditions, "resourceType", new ResourceType());

 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType("mysql");
 when(connectionService.getForConnectionSkipPermissionCheck(1L))
 .thenReturn(connectionConfig);

 ConnectionSessionFactory factory = mock(ConnectionSessionFactory.class);
 when(connectionSessionFactory.generateSession())
 .thenReturn(connectionSession);

 when(connectionSessionUtil.getVersion(connectionSession))
 .thenReturn("5.7.34");

 when(oscDBUserUtil.isLockUserRequired(eq("mysql"), any()))
 .thenReturn(true);

 ReflectionTestUtils.setField(oscService, "connectionSessionFactory", connectionSessionFactory);
 ReflectionTestUtils.setField(oscService, "connectionSessionUtil", connectionSessionUtil);

 PreConditions preConditionsSpy = spy(preConditions);
 boolean lockUserRequired = preConditionsSpy.getLockUserIsRequired(oscService, 1L);
 assertTrue(lockUserRequired);
 }
}