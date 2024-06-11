import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

public class OBMySQLSessionExtensionTest {

private Connection connection;

@Before
public void setUp() throws Exception {
String url = "jdbc:obmysql://localhost:2883/testdb";
String user = "user";
String password = "password";
connection = DriverManager.getConnection(url, user, password);
}

@After
public void tearDown() throws Exception {
if (connection != null) {
connection.close();
}
}

@Test
public void testKillQuery() {
assertNotNull(connection);
String connectionId = null;
try {
connectionId = OBMySQLSessionExtension.getConnectionId(connection);
} catch (SQLException e) {
e.printStackTrace();
}
assertNotNull(connectionId);
try {
OBMySQLSessionExtension.killQuery(connection, connectionId);
} catch (SQLException e) {
e.printStackTrace();
fail("killQuery method should not throw an exception");
}
}

@Test
public void testSwitchSchema() {
assertNotNull(connection);
String schemaName = "newSchema";
String currentSchema = null;
try {
currentSchema = OBMySQLSessionExtension.getCurrentSchema(connection);
} catch (SQLException e) {
e.printStackTrace();
}
assertNotNull(currentSchema);
assertFalse(schemaName.equals(currentSchema));
try {
OBMySQLSessionExtension.switchSchema(connection, schemaName);
} catch (SQLException e) {
e.printStackTrace();
fail("switchSchema method should not throw an exception");
}
try {
assertEquals(schemaName, OBMySQLSessionExtension.getCurrentSchema(connection));
} catch (SQLException e) {
e.printStackTrace();
fail("getCurrentSchema method should not throw an exception");
}
}

@Test
public void testKillQueryWithNullConnection() {
try {
OBMySQLSessionExtension.killQuery(null, "connectionId");
fail("killQuery method should throw an exception with null connection");
} catch (SQLException e) {
// pass
}
}

@Test
public void testSwitchSchemaWithNullConnection() {
try {
OBMySQLSessionExtension.switchSchema(null, "schemaName");
fail("switchSchema method should throw an exception with null connection");
} catch (SQLException e) {
// pass
}
}

@Test
public void testSwitchSchemaWithNullSchemaName() {
assertNotNull(connection);
try {
OBMySQLSessionExtension.switchSchema(connection, null);
fail("switchSchema method should throw an exception with null schemaName");
} catch (SQLException e) {
// pass
}
}

@Test
public void testGetCurrentSchemaWithNullConnection() {
try {
OBMySQLSessionExtension.getCurrentSchema(null);
fail("getCurrentSchema method should throw an exception with null connection");
} catch (SQLException e) {
// pass
}
}

@Test
public void testGetConnectionIdWithNullConnection() {
try {
OBMySQLSessionExtension.getConnectionId(null);
fail("getConnectionId method should throw an exception with null connection");
} catch (SQLException e) {
// pass
}
}

@Test
public void testGetConnectionIdWithNonOceanBaseConnection() {
Connection connection = null;
try {
connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "user", "password");
} catch (SQLException e) {
e.printStackTrace();
}
assertNotNull(connection);
try {
assertEquals(null, OBMySQLSessionExtension.getConnectionId(connection));
} catch (Exception e) {
// pass
}
}

}