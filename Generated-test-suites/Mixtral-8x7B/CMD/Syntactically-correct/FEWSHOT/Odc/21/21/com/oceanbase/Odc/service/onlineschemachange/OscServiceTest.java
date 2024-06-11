package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import com.oceanbase.odc.service.schedule.model.JobType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OscServiceTest {

 private DatabaseRepository databaseRepository;
 private ConnectionService connectionService;
 private ScheduleTaskRepository scheduleTaskRepository;
 private ScheduleRepository scheduleRepository;
 private OscService oscService;

 @Before
 public void setUp() {
 databaseRepository = mock(DatabaseRepository.class);
 connectionService = mock(ConnectionService.class);
 scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 scheduleRepository = mock(ScheduleRepository.class);
 oscService = new OscService();
 oscService.setDatabaseRepository(databaseRepository);
 oscService.setConnectionService(connectionService);
 oscService.setScheduleTaskRepository(scheduleTaskRepository);
 oscService.setScheduleRepository(scheduleRepository);
 }

 @Test
 public void testGetOscDatabaseInfo_emptyDatabaseId() {
 OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(null);
 assertNotNull(result);
 assertEquals(0, result.getDatabaseId());
 assertEquals(false, result.isLockDatabaseUserRequired());
 }

 @Test
 public void testGetOscDatabaseInfo_emptyDatabase() {
 when(databaseRepository.findById(anyLong())).thenReturn(Optional.empty());
 OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
 assertNotNull(result);
 assertEquals(0, result.getDatabaseId());
 assertEquals(false, result.isLockDatabaseUserRequired());
 }

 @Test
 public void testGetOscDatabaseInfo_validDatabase() {
 DatabaseEntity databaseEntity = new DatabaseEntity();
 databaseEntity.setDatabaseId(1L);
 databaseEntity.setConnectionId(1L);
 when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(databaseEntity));
 when(getLockUserIsRequired(anyLong())).thenReturn(true);
 OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
 assertNotNull(result);
 assertEquals(1, result.getDatabaseId());
 assertEquals(true, result.isLockDatabaseUserRequired());
 }

 @Test(expected = Transactional.class)
 public void testSwapTable_exception() {
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());
 oscService.swapTable(1L);
 }

 @Test
 public void testSwapTable_closeManualSwapTable() {
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
 scheduleTask.setId(1L);
 scheduleTask.setJobName("1");

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setId(1L);
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
 scheduleEntity.setJobParametersJson("{}");

 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTask));
 when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);

 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(true);
 result.setManualSwapTableStarted(false);

 when(JsonUtils.fromJson(anyString(), any())).thenReturn(oscParameters);
 when(JsonUtils.fromJson(anyString(), any())).thenReturn(result);

 oscService.swapTable(1L);

 verify(scheduleTaskRepository).updateTaskResult(anyLong(), anyString());
 }

 @Transactional
 public boolean getLockUserIsRequired(Long connectionId) {
 ConnectionConfig decryptedConnConfig =
 connectionService.getForConnectionSkipPermissionCheck(connectionId);
 return OscDBUserUtil.isLockUserRequired(decryptedConnConfig.getDialectType(),
 () -> {
 ConnectionSessionFactory factory = new DefaultConnectSessionFactory(decryptedConnConfig);
 String version = null;
 ConnectionSession connSession = null;
 try {
 connSession = factory.generateSession();
 version = ConnectionSessionUtil.getVersion(connSession);
 } catch (Exception ex) {
 log.info("Get connection occur error", ex);
 } finally {
 if (connSession != null) {
 connSession.expire();
 }
 }
 return version;
 });
 }
}