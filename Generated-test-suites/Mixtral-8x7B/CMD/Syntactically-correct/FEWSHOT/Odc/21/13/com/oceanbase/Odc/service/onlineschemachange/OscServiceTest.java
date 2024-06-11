package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
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
import org.springframework.test.util.ReflectionTestUtils;

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

 @InjectMocks
 private OscService oscService;

 @Before
 public void init() {
 MockitoAnnotations.initMocks(this);
 ReflectionTestUtils.setField(oscService, "jsonUtils", new JsonUtils());
 }

 @Test
 public void testGetOscDatabaseInfo_databaseIdNotPresent() {
 OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
 assertNotNull(result);
 assertEquals(0L, result.getDatabaseId());
 assertEquals(false, result.isLockDatabaseUserRequired());
 }

 @Test
 public void testGetOscDatabaseInfo_databaseIdPresent() {
 DatabaseEntity databaseEntity = new DatabaseEntity();
 databaseEntity.setDatabaseId(1L);
 databaseEntity.setConnectionId(2L);

 Optional<DatabaseEntity> databaseOptional = Optional.of(databaseEntity);
 when(databaseRepository.findById(1L)).thenReturn(databaseOptional);

 OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
 assertNotNull(result);
 assertEquals(1L, result.getDatabaseId());
 assertTrue(result.isLockDatabaseUserRequired());
 }

 @Test
 public void testSwapTable_scheduleTaskIdNotPresent() {
 assertThrows(IllegalArgumentException.class, () -> oscService.swapTable(1L));
 }

 @Test
 public void testSwapTable_scheduleTaskIdPresent() {
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("1");

 Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(scheduleTaskEntity);
 when(scheduleTaskRepository.findById(1L)).thenReturn(scheduleTaskOptional);

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

 Optional<ScheduleEntity> scheduleEntityOptional = Optional.of(scheduleEntity);
 when(scheduleRepository.findById(1L)).thenReturn(scheduleEntityOptional);

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);

 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(true);

 when(JsonUtils.fromJson(any(), OnlineSchemaChangeParameters.class))
 .thenReturn(oscParameters);

 when(JsonUtils.fromJson(any(), OnlineSchemaChangeScheduleTaskResult.class))
 .thenReturn(result);

 oscService.swapTable(1L);

 verify(scheduleTaskRepository, times(1)).updateTaskResult(anyLong(), any());
 }

 @Test
 public void testGetLockUserIsRequired() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType("mysql");

 ConnectionSessionFactory factory = mock(ConnectionSessionFactory.class);
 ConnectionSession session = mock(ConnectionSession.class);
 when(factory.generateSession()).thenReturn(session);
 when(session.getVersion()).thenReturn("5.7.34");

 ReflectionTestUtils.setField(oscService, "connectionSessionFactory", factory);

 boolean result = (boolean) ReflectionTestUtils.invokeMethod(oscService, "getLockUserIsRequired", 1L);
 assertTrue(result);
 }

 @Test
 public void testGetLockUserIsRequired_sessionException() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType("mysql");

 ConnectionSessionFactory factory = mock(ConnectionSessionFactory.class);
 ConnectionSession session = mock(ConnectionSession.class);
 when(factory.generateSession()).thenThrow(new RuntimeException());

 ReflectionTestUtils.setField(oscService, "connectionSessionFactory", factory);

 boolean result = (boolean) ReflectionTestUtils.invokeMethod(oscService, "getLockUserIsRequired", 1L);
 assertFalse(result);
 }

 @Test
 public void testPreConditions_validExists() {
 PreConditions.validExists(ResourceType.ODC_SCHEDULE_TASK, "schedule task", 1L, () -> true);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPreConditions_validExists_exception() {
 PreConditions.validExists(ResourceType.ODC_SCHEDULE_TASK, "schedule task", 1L, () -> false);
 }
}