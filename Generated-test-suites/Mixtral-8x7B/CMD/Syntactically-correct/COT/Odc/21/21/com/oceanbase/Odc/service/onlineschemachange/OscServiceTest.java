import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.model.OscSwapTableVO;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

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

@InjectMocks
private OscService oscService;

private DatabaseEntity databaseEntity;
private ScheduleEntity scheduleEntity;
private ScheduleTaskEntity scheduleTaskEntity;
private OnlineSchemaChangeParameters oscParameters;
private OnlineSchemaChangeScheduleTaskResult oscResult;

@Before
public void setup() {
databaseEntity = new DatabaseEntity();
databaseEntity.setConnectionId(1L);
databaseEntity.setDatabaseId(2L);

scheduleEntity = new ScheduleEntity();
scheduleEntity.setJobName("1");
scheduleEntity.setJobParametersJson("{\"key\": \"value\"}");
scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);

scheduleTaskEntity = new ScheduleTaskEntity();
scheduleTaskEntity.setResultJson("{\"key\": \"value\"}");
scheduleTaskEntity.setScheduleId(1L);
scheduleTaskEntity.setTaskType(TaskType.ONLINE_SCHEMA_CHANGE);

oscParameters = new OnlineSchemaChangeParameters();
oscParameters.setSwapTableType(SwapTableType.MANUAL);

oscResult = new OnlineSchemaChangeScheduleTaskResult();
oscResult.setManualSwapTableEnabled(true);
oscResult.setManualSwapTableStarted(true);
}

@Test
public void testGetOscDatabaseInfo_DatabaseNotPresent() {
Optional<DatabaseEntity> emptyDatabase = Optional.empty();
when(databaseRepository.findById(anyLong())).thenReturn(emptyDatabase);

OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
Assert.assertNotNull(result);
Assert.assertEquals(0, result.getDatabaseId());
Assert.assertFalse(result.isLockDatabaseUserRequired());
}

@Test
public void testGetOscDatabaseInfo_DatabasePresent() {
when(databaseRepository.findById(anyLong())).thenReturn(Optional.of(databaseEntity));

OscLockDatabaseUserInfo result = oscService.getOscDatabaseInfo(1L);
Assert.assertNotNull(result);
Assert.assertEquals(databaseEntity.getDatabaseId(), result.getDatabaseId());
Assert.assertEquals(getLockUserIsRequired(databaseEntity.getConnectionId()),
result.isLockDatabaseUserRequired());
}

private boolean getLockUserIsRequired(Long connectionId) {
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

@Test
public void testSwapTable_ScheduleTaskNotPresent() {
when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());

Assert.assertThrows(IllegalArgumentException.class, () -> {
oscService.swapTable(1L);
});
}

@Test
public void testSwapTable_ScheduleNotPresent() {
when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
when(scheduleRepository.findById(anyLong())).thenReturn(Optional.empty());

Assert.assertThrows(IllegalArgumentException.class, () -> {
oscService.swapTable(1L);
});
}

@Test
public void testSwapTable_BadArgument() {
when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_NOT_COMPLETE);

Assert.assertThrows(IllegalArgumentException.class, () -> {
oscService.swapTable(1L);
});
}

@Test
public void testSwapTable_BadSwapTableType() {
when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
oscParameters.setSwapTableType(SwapTableType.AUTO);

Assert.assertThrows(IllegalArgumentException.class, () -> {
oscService.swapTable(1L);
});
}

@Test
public void testSwapTable_SwapTableNotEnabled() {
when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
oscParameters.setSwapTableType(SwapTableType.MANUAL);
oscResult.setManualSwapTableEnabled(false);

when(scheduleTaskRepository.getTaskResult(anyLong())).thenReturn(oscResult);

Assert.assertThrows(IllegalArgumentException.class, () -> {
oscService.swapTable(1L);
});
}

@Test
public void testSwapTable_Success() {
when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(scheduleTaskEntity));
when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleEntity));

scheduleEntity.setJobType(JobType.ONLINE_SCHEMA_CHANGE_COMPLETE);
oscParameters.setSwapTableType(SwapTableType.MANUAL);
oscResult.setManualSwapTableEnabled(true);

when(scheduleTaskRepository.getTaskResult(anyLong())).thenReturn(oscResult);

OscSwapTableVO result = oscService.swapTable(1L);
Assert.assertNotNull(result);
Assert.assertEquals(scheduleTaskEntity.getScheduleId(), result.getScheduleTaskId());
}
}