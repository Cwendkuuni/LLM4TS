import static org.junit.Assert.*;

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
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

@RunWith(JUnit4.class)
public class OnlineSchemaChangeParametersTest {

@Before
public void setup() {
MockitoAnnotations.initMocks(this);
}

@Test
public void testValidOnlineSchemaChangeParameters() {
ConnectionConfig connectionConfig = new ConnectionConfig();
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
parameters.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 INT");
parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
parameters.setLockTableTimeOutSeconds(10);
parameters.setSwapTableNameRetryTimes(3);
parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP);
parameters.setDelimiter(";");
parameters.setLockUsers(new ArrayList<>());
parameters.setSwapTableType(SwapTableType.LOCAL);

List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, "schema");

// Assert that the result is not empty and contains the expected data
assertFalse(result.isEmpty());
assertEquals("table1", result.get(0).getOriginTableName());
assertEquals("ADD COLUMN c1 INT", result.get(0).getSqlsToBeExecuted().get(0));
}

@Test(expected = UnexpectedException.class)
public void testInvalidSqlType() {
ConnectionConfig connectionConfig = new ConnectionConfig();
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
parameters.setSqlType(null);
parameters.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 INT");
parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
parameters.setLockTableTimeOutSeconds(10);
parameters.setSwapTableNameRetryTimes(3);
parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP);
parameters.setDelimiter(";");
parameters.setLockUsers(new ArrayList<>());
parameters.setSwapTableType(SwapTableType.LOCAL);

parameters.generateSubTaskParameters(connectionConfig, "schema");
}

@Test
public void testNoSqlContent() {
ConnectionConfig connectionConfig = new ConnectionConfig();
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
parameters.setSqlContent("");
parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
parameters.setLockTableTimeOutSeconds(10);
parameters.setSwapTableNameRetryTimes(3);
parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP);
parameters.setDelimiter(";");
parameters.setLockUsers(new ArrayList<>());
parameters.setSwapTableType(SwapTableType.LOCAL);

try {
parameters.generateSubTaskParameters(connectionConfig, "schema");
fail("Expected an UnexpectedException to be thrown");
} catch (UnexpectedException e) {
// pass
}
}

@Test
public void testEmptyLockUsers() {
ConnectionConfig connectionConfig = new ConnectionConfig();
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
parameters.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 INT");
parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
parameters.setLockTableTimeOutSeconds(10);
parameters.setSwapTableNameRetryTimes(3);
parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP);
parameters.setDelimiter(";");
parameters.setLockUsers(new ArrayList<>());
parameters.setSwapTableType(SwapTableType.LOCAL);

List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, "schema");

// Assert that the result is not empty and contains the expected data
assertFalse(result.isEmpty());
assertEquals("table1", result.get(0).getOriginTableName());
assertEquals("ADD COLUMN c1 INT", result.get(0).getSqlsToBeExecuted().get(0));
}

@Test
public void testNonEmptyLockUsers() {
ConnectionConfig connectionConfig = new ConnectionConfig();
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
parameters.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 INT");
parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
parameters.setLockTableTimeOutSeconds(10);
parameters.setSwapTableNameRetryTimes(3);
parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP);
parameters.setDelimiter(";");
parameters.setLockUsers(List.of("user1", "user2"));
parameters.setSwapTableType(SwapTableType.LOCAL);

List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, "schema");

// Assert that the result is not empty and contains the expected data
assertFalse(result.isEmpty());
assertEquals("table1", result.get(0).getOriginTableName());
assertEquals("ADD COLUMN c1 INT", result.get(0).getSqlsToBeExecuted().get(0));
assertEquals("user1", result.get(0).getLockUsers().get(0));
assertEquals("user2", result.get(0).getLockUsers().get(1));
}

@Test
public void testContinueOnError() {
ConnectionConfig connectionConfig = new ConnectionConfig();
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
parameters.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 INT");
parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
parameters.setLockTableTimeOutSeconds(10);
parameters.setSwapTableNameRetryTimes(3);
parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP);
parameters.setDelimiter(";");
parameters.setLockUsers(new ArrayList<>());
parameters.setSwapTableType(SwapTableType.LOCAL);

assertTrue(parameters.isContinueOnError());
}

@Test
public void testRollbackOnError() {
ConnectionConfig connectionConfig = new ConnectionConfig();
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
parameters.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 INT");
parameters.setErrorStrategy(TaskErrorStrategy.ROLLBACK);
parameters.setLockTableTimeOutSeconds(10);
parameters.setSwapTableNameRetryTimes(3);
parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP);
parameters.setDelimiter(";");
parameters.setLockUsers(new ArrayList<>());
parameters.setSwapTableType(SwapTableType.LOCAL);

assertFalse(parameters.isContinueOnError());
}

@Test
public void testKeepOriginTable() {
ConnectionConfig connectionConfig = new ConnectionConfig();
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
parameters.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 INT");
parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
parameters.setLockTableTimeOutSeconds(10);
parameters.setSwapTableNameRetryTimes(3);
parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP);
parameters.setDelimiter(";");
parameters.setLockUsers(new ArrayList<>());
parameters.setSwapTableType(SwapTableType.LOCAL);

assertEquals(OriginTableCleanStrategy.KEEP, parameters.getOriginTableCleanStrategy());
}

@Test
public void testDropOriginTable() {
ConnectionConfig connectionConfig = new ConnectionConfig();
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
parameters.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 INT");
parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
parameters.setLockTableTimeOutSeconds(10);
parameters.setSwapTableNameRetryTimes(3);
parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP);
parameters.setDelimiter(";");
parameters.setLockUsers(new ArrayList<>());
parameters.setSwapTableType(SwapTableType.LOCAL);

assertEquals(OriginTableCleanStrategy.DROP, parameters.getOriginTableCleanStrategy());
}

@Test
public void testLocalSwapTableType() {
ConnectionConfig connectionConfig = new ConnectionConfig();
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
parameters.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 INT");
parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
parameters.setLockTableTimeOutSeconds(10);
parameters.setSwapTableNameRetryTimes(3);
parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP);
parameters.setDelimiter(";");
parameters.setLockUsers(new ArrayList<>());
parameters.setSwapTableType(SwapTableType.LOCAL);

assertEquals(SwapTableType.LOCAL, parameters.getSwapTableType());
}

@Test
public void testRemoteSwapTableType() {
ConnectionConfig connectionConfig = new ConnectionConfig();
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
parameters.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 INT");
parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
parameters.setLockTableTimeOutSeconds(10);
parameters.setSwapTableNameRetryTimes(3);
parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP);
parameters.setDelimiter(";");
parameters.setLockUsers(new ArrayList<>());
parameters.setSwapTableType(SwapTableType.REMOTE);

assertEquals(SwapTableType.REMOTE, parameters.getSwapTableType());
}
}