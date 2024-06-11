
package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.core.shared.PreConditions;
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
import com.oceanbase.odc.service.onlineschemachange.rename.OscDBUserUtil;
import com.oceanbase.odc.service.schedule.model.JobType;

@RunWith(MockitoJUnitRunner.class)
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

 @Test
 public void testGetOscDatabaseInfoWithValidId() {
 long databaseId = 1L;
 OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(databaseId);
 Assert.assertNotNull(result);
 Assert.assertEquals(databaseId, result.getDatabaseId());
 Assert.assertFalse(result.isLockDatabaseUserRequired());
 }

 @Test
 public void testSwapTableWithValidIdAndLockUserRequired() {
 Long scheduleTaskId = 1L;
 OscSwapTableVO result = oscService.swapTable(scheduleTaskId);
 Assert.assertNotNull(result);
 Assert.assertEquals(scheduleTaskId, result.getScheduleTaskId());
 verify(scheduleTaskRepository, times(1)).updateTaskResult(scheduleTaskId, anyString());
 }

 @Test
 public void testGetLockUserIsRequiredWithValidConnectionId() {
 long connectionId = 1L;
 boolean result = oscService.getLockUserIsRequired(connectionId);
 Assert.assertTrue(result);
 }

 @Test(expected = PreConditions.InvalidArgumentStateException.class)
 public void testGetLockUserIsRequiredWithInvalidConnectionId() {
 long connectionId = -1L;
 oscService.getLockUserIsRequired(connectionId);
 }

}
