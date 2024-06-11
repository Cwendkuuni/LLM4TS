import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

@RunWith(JUnit4.class)
public class OnlineSchemaChangeParametersTest {

 private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);

 onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD column1 int");
 onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 onlineSchemaChangeParameters.setLockTableTimeOutSeconds(10);
 onlineSchemaChangeParameters.setSwapTableNameRetryTimes(3);
 onlineSchemaChangeParameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.BACKUP_AND_DELETE);
 onlineSchemaChangeParameters.setDelimiter(";");

 List<String> lockUsers = new ArrayList<>();
 lockUsers.add("user1");
 lockUsers.add("user2");
 onlineSchemaChangeParameters.setLockUsers(lockUsers);
 onlineSchemaChangeParameters.setSwapTableType(SwapTableType.TEMPORARY);
 }

 @Test
 public void testGettersAndSetters() {
 assertEquals(OnlineSchemaChangeSqlType.ALTER, onlineSchemaChangeParameters.getSqlType());
 assertEquals("ALTER TABLE table1 ADD column1 int", onlineSchemaChangeParameters.getSqlContent());
 assertEquals(TaskErrorStrategy.CONTINUE, onlineSchemaChangeParameters.getErrorStrategy());
 assertEquals(Integer.valueOf(10), onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
 assertEquals(Integer.valueOf(3), onlineSchemaChangeParameters.getSwapTableNameRetryTimes());
 assertEquals(OriginTableCleanStrategy.BACKUP_AND_DELETE,
 onlineSchemaChangeParameters.getOriginTableCleanStrategy());
 assertEquals(";", onlineSchemaChangeParameters.getDelimiter());
 assertEquals(lockUsers, onlineSchemaChangeParameters.getLockUsers());
 assertEquals(SwapTableType.TEMPORARY, onlineSchemaChangeParameters.getSwapTableType());
 }

 @Test
 public void testIsContinueOnError() {
 assertTrue(onlineSchemaChangeParameters.isContinueOnError());
 }

 @Test
 public void testGenerateSubTaskParametersDefault() throws UnexpectedException {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType("mysql");
 String schema = "schema1";
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters =
 onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
 assertEquals(1, subTaskParameters.size());
 assertEquals("ALTER TABLE table1 ADD column1 int",
 subTaskParameters.get(0).getSqlsToBeExecuted().get(0));
 }

 @Test
 public void testGenerateSubTaskParametersWithSwapTable() throws UnexpectedException {
 onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD column1 int, RENAME TO table2");
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType("mysql");
 String schema = "schema1";
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters =
 onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
 assertEquals(2, subTaskParameters.size());
 assertEquals("ALTER TABLE table1 ADD column1 int",
 subTaskParameters.get(0).getSqlsToBeExecuted().get(0));
 assertEquals("ALTER TABLE table1 RENAME TO table2",
 subTaskParameters.get(1).getSqlsToBeExecuted().get(0));
 }

 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParametersException() throws UnexpectedException {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType("oracle");
 String schema = "schema1";
 onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
 }
}