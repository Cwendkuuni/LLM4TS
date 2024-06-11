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
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;

@SuppressWarnings("unused")
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
 MockitoAnnotations.openMocks(this);
 ReflectionTestUtils.setField(oscService, "jsonUtils", new JsonUtils());
 }

 @Test
 public void testGetOscDatabaseInfo_nonexistentDatabase() {
 Optional<DatabaseEntity> databaseOptional = Optional.empty();
 when(databaseRepository.findById(anyLong())).thenReturn(databaseOptional);

 OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(1L);

 assertNull(oscDatabaseInfo.getDatabaseId());
 assertFalse(oscDatabaseInfo.isLockDatabaseUserRequired());
 }

 @Test
 public void testGetOscDatabaseInfo_existentDatabase() {
 DatabaseEntity databaseEntity = new DatabaseEntity();
 databaseEntity.setConnectionId(1L);

 Optional<DatabaseEntity> databaseOptional = Optional.of(databaseEntity);
 when(databaseRepository.findById(anyLong())).thenReturn(databaseOptional);

 when(oscService.getLockUserIsRequired(anyLong())).thenReturn(true);

 OscLockDatabaseUserInfo oscDatabaseInfo = oscService.getOscDatabaseInfo(1L);

 assertEquals(databaseEntity.getDatabaseId(), oscDatabaseInfo.getDatabaseId());
 assertTrue(oscDatabaseInfo.isLockDatabaseUserRequired());
 }

 @Test
 public void testSwapTable_nonexistentScheduleTask() {

 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

 PreConditions preConditions = mock(PreConditions.class);
 when(preConditions.validExists(any(), any(), anyLong(), any())).thenReturn(null);

 ReflectionTestUtils.setField(oscService, "preConditions", preConditions);

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);

 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(true);

 oscService.swapTable(1L);

 }

 @Test
 public void testSwapTable_incompatibleTaskType() {
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setJobName("1");
 scheduleTaskEntity.setResultJson("{}");

 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));

 PreConditions preConditions = mock(PreConditions.class);
 when(preConditions.validExists(any(), any(), anyLong(), any())).thenReturn(null);

 ReflectionTestUtils.setField(oscService, "preConditions", preConditions);

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);

 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(true);

 when(oscService.getScheduleEntity(anyLong())).thenReturn(new ScheduleEntity());
 when(oscService.getSwapTableTaskResult(anyLong())).thenReturn(result);

 oscService.swapTable(1L);

 }

 @Test
 public void testSwapTable_disabledManualSwapTable() {
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setJobName("1");
 scheduleTaskEntity.setResultJson("{}");

 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));

 PreConditions preConditions = mock(PreConditions.class);
 when(preConditions.validExists(any(), any(), anyLong(), any())).thenReturn(null);

 ReflectionTestUtils.setField(oscService, "preConditions", preConditions);

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);

 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(false);

 when(oscService.getScheduleEntity(anyLong())).thenReturn(new ScheduleEntity());
 when(oscService.getSwapTableTaskResult(anyLong())).thenReturn(result);

 oscService.swapTable(1L);

 }

 @Test
 public void testSwapTable_enabledManualSwapTable() {
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setJobName("1");
 scheduleTaskEntity.setResultJson("{}");

 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));

 PreConditions preConditions = mock(PreConditions.class);
 when(preConditions.validExists(any(), any(), anyLong(), any())).thenReturn(null);

 ReflectionTestUtils.setField(oscService, "preConditions", preConditions);

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);

 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(true);
 result.setManualSwapTableStarted(false);

 when(oscService.getScheduleEntity(anyLong())).thenReturn(new ScheduleEntity());
 when(oscService.getSwapTableTaskResult(anyLong())).thenReturn(result);

 when(scheduleTaskRepository.updateTaskResult(anyLong(), any())).thenReturn(true);

 oscService.swapTable(1L);

 }

 private ScheduleEntity getScheduleEntity(Long scheduleId) {
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
 scheduleEntity.setJobParametersJson("{}");
 scheduleEntity.setJobName(Long.toString(scheduleId));
 return scheduleEntity;
 }

 private OnlineSchemaChangeScheduleTaskResult getSwapTableTaskResult(Long scheduleTaskId) {
 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(true);
 return result;
 }

 private boolean getLockUserIsRequired(Long connectionId) {
 ConnectionConfig decryptedConnConfig = new ConnectionConfig();

 ConnectionSessionFactory factory = mock(ConnectionSessionFactory.class);
 ConnectionSession connectionSession = mock(ConnectionSession.class);
 when(factory.generateSession()).thenReturn(connectionSession);

 ReflectionTestUtils.setField(decryptedConnConfig, "dialectType", "MySQL");
 ReflectionTestUtils.setField(decryptedConnConfig, "connectionSessionFactory", factory);

 when(connectionService.getForConnectionSkipPermissionCheck(anyLong())).thenReturn(decryptedConnConfig);

 return OscDBUserUtil.isLockUserRequired(decryptedConnConfig.getDialectType(), () -> "8.0");
 }
}