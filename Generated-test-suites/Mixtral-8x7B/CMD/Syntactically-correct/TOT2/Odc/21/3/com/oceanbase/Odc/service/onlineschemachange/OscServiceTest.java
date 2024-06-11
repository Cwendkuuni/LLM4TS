import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

@RunWith(SpringJUnit4ClassRunner.class)
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
MockitoAnnotations.initMocks(this);
}

@Test
public void testGetOscDatabaseInfo() {
Optional<DatabaseEntity> database = databaseRepository.findById(1L);
OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(1L);
assertEquals(oscDatabase.getDatabaseId(), database.get().getDatabaseId());
}

@Test(expected = EntityNotFoundException.class)
public void testGetOscDatabaseInfoWithNonExistentId() {
oscService.getOscDatabaseInfo(999L);
}

@Test
public void testSwapTable() {
ScheduleTaskEntity scheduleTask = new ScheduleTaskEntity();
scheduleTask.setId(1L);
scheduleTask.setJobName("1");
scheduleRepository.save(new ScheduleEntity(1L, JobType.ONLINE_SCHEMA_CHANGE_COMPLETE, null));
scheduleTaskRepository.save(scheduleTask);
OscSwapTableVO oscSwapTable = oscService.swapTable(1L);
assertEquals(oscSwapTable.getScheduleTaskId(), 1L);
}

@Test(expected = EntityNotFoundException.class)
public void testSwapTableWithNonExistentScheduleTaskId() {
oscService.swapTable(999L);
}

@Test
public void testGetLockUserIsRequired() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType("MySQL");
ConnectionSessionFactory factory = new DefaultConnectSessionFactory(connectionConfig);
ConnectionSession connSession = factory.generateSession();
boolean result = oscService.getLockUserIsRequired(connSession.getConnectionId());
assertTrue(result);
}

@Test
public void testGetLockUserIsRequiredWithInvalidConnectionId() {
assertFalse(oscService.getLockUserIsRequired(-1L));
}

}