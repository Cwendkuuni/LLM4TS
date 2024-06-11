package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
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

import com.oceanbase.odc.core.session.ConnectionSessionFactory;
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
 databaseEntity.setDatabaseId(1L);
 databaseEntity.setConnectionId(2L);

 Optional<DatabaseEntity> databaseOptional = Optional.of(databaseEntity);
 when(databaseRepository.findById(anyLong())).thenReturn(databaseOptional);

 OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
 assertEquals(1L, result.getDatabaseId().longValue());
 assertTrue(result.isLockDatabaseUserRequired());
 }

 @Test
 public void testSwapTable_ScheduleTaskNotPresent() {
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));

 OscSwapTableVO result = oscService.swapTable(1L);
 assertNull(result.getScheduleTaskId());
 }

 @Test
 public void testSwapTable_ScheduleTaskPresent() {
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setId(1L);
 scheduleTaskEntity.setJobName("1");
 scheduleTaskEntity.setResultJson("{\"manualSwapTableEnabled\":true}");

 Optional<ScheduleTaskEntity> scheduleTaskOptional = Optional.of(scheduleTaskEntity);
 when(scheduleTaskRepository.findById(anyLong())).thenReturn(scheduleTaskOptional);

 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
 scheduleEntity.setJobParametersJson("{}");

 Optional<ScheduleEntity> scheduleEntityOptional = Optional.of(scheduleEntity);
 when(scheduleRepository.findById(anyLong())).thenReturn(scheduleEntityOptional);

 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);

 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(true);
 result.setManualSwapTableStarted(false);

 when(JsonUtils.fromJson(anyString(), OnlineSchemaChangeScheduleTaskResult.class))
 .thenReturn(result);

 when(JsonUtils.fromJson(anyString(), OnlineSchemaChangeParameters.class))
 .thenReturn(oscParameters);

 OscSwapTableVO oscSwapTableVO = new OscSwapTableVO();
 oscSwapTableVO.setScheduleTaskId(1L);

 OscSwapTableVO resultVO = oscService.swapTable(1L);
 assertEquals(oscSwapTableVO.getScheduleTaskId(), resultVO.getScheduleTaskId());
 }
}