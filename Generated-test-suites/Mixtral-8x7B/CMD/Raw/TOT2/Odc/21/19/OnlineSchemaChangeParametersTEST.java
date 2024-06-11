import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

@Test
public void testOnlineSchemaChangeParameters() {
OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
assertNull(onlineSchemaChangeParameters.getSqlContent());
assertEquals(TaskErrorStrategy.TERMINATE, onlineSchemaChangeParameters.getErrorStrategy());
assertEquals(0, onlineSchemaChangeParameters.getLockTableTimeOutSeconds().intValue());
assertEquals(0, onlineSchemaChangeParameters.getSwapTableNameRetryTimes().intValue());
assertEquals(OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP,
onlineSchemaChangeParameters.getOriginTableCleanStrategy());
assertEquals(";", onlineSchemaChangeParameters.getDelimiter());
assertNull(onlineSchemaChangeParameters.getLockUsers());
assertEquals(OnlineSchemaChangeParameters.SwapTableType.SWAP_TABLE,
onlineSchemaChangeParameters.getSwapTableType());
}

@Test
public void testSetAndGetSqlType() {
OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ADD_COLUMN);
assertEquals(OnlineSchemaChangeSqlType.ADD_COLUMN, onlineSchemaChangeParameters.getSqlType());
}

@Test
public void testSetAndGetSqlContent() {
OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
onlineSchemaChangeParameters.setSqlContent("ALTER TABLE users ADD COLUMN age INT;");
assertEquals("ALTER TABLE users ADD COLUMN age INT;", onlineSchemaChangeParameters.getSqlContent());
}

@Test
public void testSetAndGetErrorStrategy() {
OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
assertEquals(TaskErrorStrategy.CONTINUE, onlineSchemaChangeParameters.getErrorStrategy());
}

@Test
public void testSetAndGetLockTableTimeOutSeconds() {
OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
onlineSchemaChangeParameters.setLockTableTimeOutSeconds(60);
assertEquals(Integer.valueOf(60), onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
}

@Test
public void testSetAndGetSwapTableNameRetryTimes() {
OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
onlineSchemaChangeParameters.setSwapTableNameRetryTimes(3);
assertEquals(Integer.valueOf(3), onlineSchemaChangeParameters.getSwapTableNameRetryTimes());
}

@Test
public void testSetAndGetOriginTableCleanStrategy() {
OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
onlineSchemaChangeParameters
.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.REMOVE);
assertEquals(OnlineSchemaChangeParameters.OriginTableCleanStrategy.REMOVE,
onlineSchemaChangeParameters.getOriginTableCleanStrategy());
}

@Test
public void testSetAndGetDelimiter() {
OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
onlineSchemaChangeParameters.setDelimiter(",");
assertEquals(",", onlineSchemaChangeParameters.getDelimiter());
}

@Test
public void testSetAndGetLockUsers() {
OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
List<String> lockUsers = new ArrayList<>();
lockUsers.add("testUser1");
lockUsers.add("testUser2");
onlineSchemaChangeParameters.setLockUsers(lockUsers);
assertEquals(lockUsers, onlineSchemaChangeParameters.getLockUsers());
}

@Test
public void testSetAndGetSwapTableType() {
OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
onlineSchemaChangeParameters.setSwapTableType(OnlineSchemaChangeParameters.

SwapTableType.NO_SWAP_TABLE);
assertEquals(OnlineSchemaChangeParameters.SwapTableType.NO_SWAP_TABLE,
onlineSchemaChangeParameters.getSwapTableType());

}

@Test
public void testIsContinueOnError() {
OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
assertTrue(onlineSchemaChangeParameters.isContinueOnError());

onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.TERMINATE);
assertFalse(onlineSchemaChangeParameters.isContinueOnError());
}

@Test
public void testGenerateSubTaskParameters() throws UnexpectedException {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "testSchema";
OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
onlineSchemaChangeParameters.setSqlContent("ALTER TABLE users ADD COLUMN age INT; ALTER TABLE users ADD COLUMN gender VARCHAR(20);");
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ADD_COLUMN);

List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
assertEquals(2, subTaskParameters.size());
assertEquals(schema, subTaskParameters.get(0).getDatabaseName());
assertEquals("users", subTaskParameters.get(0).getOriginTableName());
assertEquals("age", subTaskParameters.get(0).getColumnName());
assertEquals("INT", subTaskParameters.get(0).getColumnType());
assertTrue(subTaskParameters.get(0).getSqlsToBeExecuted().contains("ALTER TABLE users ADD COLUMN age INT;"));

assertEquals(schema, subTaskParameters.get(1).getDatabaseName());
assertEquals("users", subTaskParameters.get(1).getOriginTableName());
assertEquals("gender", subTaskParameters.get(1).getColumnName());
assertEquals("VARCHAR(20)", subTaskParameters.get(1).getColumnType());
assertTrue(subTaskParameters.get(1).getSqlsToBeExecuted().contains("ALTER TABLE users ADD COLUMN gender VARCHAR(20);"));
}

@Test(expected = UnexpectedException.class)
public void testGenerateSubTaskParametersException() throws UnexpectedException {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "testSchema";
OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
onlineSchemaChangeParameters.setSqlContent("INCORRECT SQL;");
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ADD_COLUMN);

onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
}
}