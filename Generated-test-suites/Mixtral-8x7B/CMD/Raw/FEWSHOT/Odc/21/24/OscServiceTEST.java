package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleRepository;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskRepository;
import com.oceanbase.odc.service.connection.ConnectionService;
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
 private PreConditions preConditions;
 @Mock
 private JsonUtils jsonUtils;
 @Mock
 private OscDBUserUtil oscDBUserUtil;
 @Mock
 private ConnectionSessionFactory connectionSessionFactory;
 @Mock
 private ConnectionSession connectionSession;

 @InjectMocks
 private OscService oscService;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 ReflectionTestUtils.setField(oscService, "preConditions", preConditions);
 ReflectionTestUtils.setField(oscService, "jsonUtils", jsonUtils);
 ReflectionTestUtils.setField(oscService, "oscDBUserUtil", oscDBUserUtil);
 ReflectionTestUtils.setField(oscService, "connectionSessionFactory", connectionSessionFactory);
 }

 @Test
 public void testGetOscDatabaseInfo() {
 Optional<DatabaseEntity> database = Optional.of(new DatabaseEntity());
 when(databaseRepository.findById(1L)).thenReturn(database);

 OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);
 assertEquals(1L, oscDatabase.getDatabaseId());
 }

 @Test
 public void testGetOscDatabaseInfo_whenDatabaseNotPresent() {
 when(databaseRepository.findById(1L)).thenReturn(Optional.empty());

 OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);
 assertNull(oscDatabase);
 }

 @Test
 public void testSwapTable() {
 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();

 Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(scheduleTask);
 when(scheduleTaskRepository.findById(1L)).thenReturn(scheduleTaskOptional);

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));

 when(jsonUtils.fromJson(scheduleEntity.getJobParametersJson(), OnlineSchemaChangeParameters.class))
 .thenReturn(oscParameters);

 when(jsonUtils.fromJson(scheduleTask.getResultJson(), OnlineSchemaChangeScheduleTaskResult.class))
 .thenReturn(result);

 when(preConditions.validExists(ResourceType.ODC_SCHEDULE_TASK, "schedule task", 1L,
 scheduleTaskOptional::isPresent)).thenReturn(null);

 when(preConditions.validExists(ResourceType.ODC_SCHEDULE, "schedule ", 1L,
 scheduleEntity::isPresent)).thenReturn(null);

 oscService.swapTable(1L);
 }

 @Test
 public void testSwapTable_whenScheduleTaskNotPresent() {
 Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.empty();
 when(scheduleTaskRepository.findById(1L)).thenReturn(scheduleTaskOptional);

 oscService.swapTable(1L);
 PreConditions.validExists(ResourceType.ODC_SCHEDULE_TASK, "schedule task", 1L,
 scheduleTaskOptional::isPresent);
 }

 @Test
 public void testSwapTable_whenScheduleNotPresent() {
 Optional<ScheduleEntity> scheduleEntity = Optional.empty();
 when(scheduleRepository.findById(1L)).thenReturn(scheduleEntity);

 oscService.swapTable(1L);
 PreConditions.validExists(ResourceType.ODC_SCHEDULE, "schedule ", 1L,
 scheduleEntity::isPresent);
 }

 @Test
 public void testGetLockUserIsRequired() {
 ConnectionConfig decryptedConnConfig = new ConnectionConfig();
 ConnectionSessionFactory factory = mock(ConnectionSessionFactory.class);
 ConnectionSession connSession = mock(ConnectionSession.class);
 String version = "1";

 when(connectionService.getForConnectionSkipPermissionCheck(1L)).thenReturn(decryptedConnConfig);
 when(connectionSessionFactory.generateSession()).thenReturn(connSession);

 when(connSession.getVersion()).thenReturn(version);

 ReflectionTestUtils.setField(oscService, "connectionSession", connSession);

 boolean lockUserRequired = oscService.getLockUserIsRequired(1L);
 assertEquals(true, lockUserRequired);
 }
}