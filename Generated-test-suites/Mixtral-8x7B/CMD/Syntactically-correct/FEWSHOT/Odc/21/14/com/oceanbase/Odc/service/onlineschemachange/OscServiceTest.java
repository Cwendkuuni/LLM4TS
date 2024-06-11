package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

@Transactional
public class OscServiceTest {

 private OscService oscService;

 @Before
 public void setup() {
 oscService = new OscService();
 }

 @Test
 public void testGetOscDatabaseInfoWithNonExistentDatabaseId() {
 OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);
 assertEquals(0, oscDatabase.getDatabaseId());
 assertEquals(false, oscDatabase.getLockDatabaseUserRequired());
 }

 @Test
 public void testGetOscDatabaseInfoWithExistentDatabaseId() {
 DatabaseRepository databaseRepository = mock(DatabaseRepository.class);
 DatabaseEntity databaseEntity = new DatabaseEntity();
 databaseEntity.setDatabaseId(1L);
 when(databaseRepository.findById(1L)).thenReturn(Optional.of(databaseEntity));
 OscService oscService = new OscService();
 oscService.setDatabaseRepository(databaseRepository);
 OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);
 assertEquals(1, oscDatabase.getDatabaseId());
 assertEquals(getLockUserIsRequired(1L), oscDatabase.getLockDatabaseUserRequired());
 }

 @Test
 public void testSwapTableWithNonExistentScheduleTaskId() {
 OscSwapTableVO oscSwapTable = oscService.swapTable(1L);
 assertEquals(1, oscSwapTable.getScheduleTaskId());
 }

 @Test
 public void testSwapTableWithExistentScheduleTaskId() {
 ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
 ScheduleEntity scheduleEntity = new ScheduleEntity();
 scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
 scheduleEntity.setJobParametersJson("{\"swapTableType\":\"MANUAL\"}");
 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);
 ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
 scheduleTaskEntity.setResultJson("{\"manualSwapTableEnabled\":true,\"manualSwapTableStarted\":false}");
 OnlineSchemaChangeScheduleTaskResult result = new OnlineSchemaChangeScheduleTaskResult();
 result.setManualSwapTableEnabled(true);
 result.setManualSwapTableStarted(false);
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTaskEntity));
 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(scheduleEntity));
 oscService.setScheduleTaskRepository(scheduleTaskRepository);
 oscService.swapTable(1L);
 }

 private boolean getLockUserIsRequired(Long connectionId) {
 return true;
 }
}