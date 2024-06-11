import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@Extension
class OBMySQLSessionExtensionTest {

private Connection connection;

@Before
public void setup() {
connection = JdbcOperationsUtil.getJdbcOperations().createConnection();
}

@Test
public void testKillQuery() throws Exception {
String connectionId = "1";
OBMySQLSessionExtension obj = new OBMySQLSessionExtension();
obj.killQuery(connection, connectionId);
}

@Test
public void testSwitchSchema() throws Exception {
String schemaName = "testSchema";
JdbcOperationsUtil.getJdbcOperations(connection).execute("DROP DATABASE IF EXISTS " + schemaName);
JdbcOperationsUtil.getJdbcOperations(connection).execute("CREATE DATABASE " + schemaName);
OBMySQLSessionExtension obj = new OBMySQLSessionExtension();
obj.switchSchema(connection, schemaName);
assertEquals(schemaName, obj.getCurrentSchema(connection));
}

@Test
public void testGetCurrentSchema() throws Exception {
String schemaName = "testSchema";
JdbcOperationsUtil.getJdbcOperations(connection).execute("DROP DATABASE IF EXISTS " + schemaName);
JdbcOperationsUtil.getJdbcOperations(connection).execute("CREATE DATABASE " + schemaName);
OBMySQLSessionExtension obj = new OBMySQLSessionExtension();
assertEquals(schemaName, obj.getCurrentSchema(connection));
}

@Test
public void testGetConnectionId() throws Exception {
OBMySQLSessionExtension obj = new OBMySQLSessionExtension();
String connectionId = obj.getConnectionId(connection);
assertEquals(connectionId, obj.getConnectionId(connection));
}

@Test
public void testKillQueryWithNullConnection() {
String connectionId = "1";
OBMySQLSessionExtension obj = new OBMySQLSessionExtension();
try {
obj.killQuery(null, connectionId);
fail("Expected NullPointerException");
} catch (NullPointerException e) {
// expected
}
}

@Test
public void testSwitchSchemaWithNullConnection() {
String schemaName = "testSchema";
OBMySQLSessionExtension obj = new OBMySQLSessionExtension();
try {
obj.switchSchema(null, schemaName);
fail("Expected NullPointerException");
} catch (NullPointerException e) {
// expected
}
}

@Test
public void testSwitchSchemaWithNullSchemaName() {
try {
OBMySQLSessionExtension obj = new OBMySQLSessionExtension();
obj.switchSchema(connection, null);
fail("Expected NullPointerException");
} catch (NullPointerException e) {
// expected
}
}

@Test
public void testGetConnectionIdWithNullConnection() {
OBMySQLSessionExtension obj = new OBMySQLSessionExtension();
try {
obj.getConnectionId(null);
fail("Expected NullPointerException");
} catch (NullPointerException e) {
// expected
}
}

@Test
public void testKillQueryWithEmptyConnectionId() throws Exception {
OBMySQLSessionExtension obj = new OBMySQLSessionExtension();
try {
obj.killQuery(connection, "");
fail("Expected SQLException");
} catch (SQLException e) {
// expected
}
}

@Test
public void testSwitchSchemaWithEmptySchemaName() throws Exception {
OBMySQLSessionExtension obj = new OBMySQLSessionExtension();
try {
obj.switchSchema(connection, "");
fail("Expected SQLException");
} catch (SQLException e) {
// expected
}
}

@Test
public void testGetCurrentSchemaWithNoSchema() throws Exception {
JdbcOperationsUtil.getJdbcOperations(connection).execute("DROP DATABASE IF EXISTS testSchema");
OBMySQLSessionExtension obj = new OBMySQLSessionExtension();
assertNotEquals("testSchema", obj.getCurrentSchema(connection));
}

@Test
public void testGetConnectionIdWithNoConnectionId() throws Exception {
OceanBaseConnection oceanBaseConnection = new OceanBaseConnection(JdbcOperationsUtil.getJdbcOperations().getUrl(), "testSchema", "", true);
connection = oceanBaseConnection;
OBMySQLSessionExtension obj = new OBMySQLSessionExtension();
assertEquals("", obj.getConnectionId(connection));
}

}