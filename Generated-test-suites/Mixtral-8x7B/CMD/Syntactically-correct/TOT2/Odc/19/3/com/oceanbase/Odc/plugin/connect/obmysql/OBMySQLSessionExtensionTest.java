import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@SuppressWarnings("deprecation")
public class OBMySQLSessionExtensionTest {

private Connection createMockConnection(String schema) throws SQLException {
Connection connection = Mockito.mock(Connection.class);
Mockito.when(connection.getCatalog()).thenReturn(schema);
return connection;
}

private OceanBaseConnection createMockOceanBaseConnection() {
OceanBaseConnection oceanBaseConnection = Mockito.mock(OceanBaseConnection.class);
Mockito.when(oceanBaseConnection.getServerThreadId()).thenReturn(12345);
return oceanBaseConnection;
}

@Test
public void testKillQuery() {
Connection connection = createMockConnection("testSchema");
String connectionId = "12345";
OBMySQLSessionExtension obmysqlSessionExtension = new OBMySQLSessionExtension();
obmysqlSessionExtension.killQuery(connection, connectionId);
}

@Test
public void testKillQueryWithNullConnection() {
OBMySQLSessionExtension obmysqlSessionExtension = new OBMySQLSessionExtension();
assertThrows(NullPointerException.class, () -> obmysqlSessionExtension.killQuery(null, "12345"));
}

@Test
public void testKillQueryWithNullConnectionId() {
Connection connection = createMockConnection("testSchema");
OBMySQLSessionExtension obmysqlSessionExtension = new OBMySQLSessionExtension();
assertThrows(NullPointerException.class, () -> obmysqlSessionExtension.killQuery(connection, null));
}

@Test
public void testKillQueryWithEmptyConnectionId() {
Connection connection = createMockConnection("testSchema");
OBMySQLSessionExtension obmysqlSessionExtension = new OBMySQLSessionExtension();
assertThrows(IllegalArgumentException.class, () -> obmysqlSessionExtension.killQuery(connection, ""));
}

@Test
public void testSwitchSchema() {
Connection connection = createMockConnection("testSchema");
String schemaName = "testSchema2";
OBMySQLSessionExtension obmysqlSessionExtension = new OBMySQLSessionExtension();
obmysqlSessionExtension.switchSchema(connection, schemaName);
}

@Test
public void testSwitchSchemaWithNullConnection() {
OBMySQLSessionExtension obmysqlSessionExtension = new OBMySQLSessionExtension();
assertThrows(NullPointerException.class, () -> obmysqlSessionExtension.switchSchema(null, "testSchema2"));
}

@Test
public void testSwitchSchemaWithNullSchemaName() {
Connection connection = createMockConnection("testSchema");
OBMySQLSessionExtension obmysqlSessionExtension = new OBMySQLSessionExtension();
assertThrows(NullPointerException.class, () -> obmysqlSessionExtension.switchSchema(connection, null));
}

@Test
public void testSwitchSchemaWithEmptySchemaName() {
Connection connection = createMockConnection("testSchema");
OBMySQLSessionExtension obmysqlSessionExtension = new OBMySQLSessionExtension();
assertThrows(IllegalArgumentException.class, () -> obmysqlSessionExtension.switchSchema(connection, ""));
}

@Test
public void testGetCurrentSchema() {
Connection connection = createMockConnection("testSchema");
OBMySQLSessionExtension obmysqlSessionExtension = new OBMySQLSessionExtension();
assertEquals("testSchema", obmysqlSessionExtension.getCurrentSchema(connection));
}

@Test
public void testGetCurrentSchemaWithNullConnection() {
OBMySQLSessionExtension obmysqlSessionExtension = new OBMySQLSessionExtension();
assertThrows(NullPointerException.class, () -> obmysqlSessionExtension.getCurrentSchema(null));
}

@Test
public void testGetCurrentSchemaWithEmptySchema() {
Connection connection = createMockConnection("");
OBMySQLSessionExtension obmysqlSessionExtension = new OBMySQLSessionExtension();
assertEquals("", obmysqlSessionExtension.getCurrentSchema(connection));
}

@Test
public void testGetConnectionId() {
Connection connection = createMockConnection("testSchema");
OBMySQLSessionExtension obmysqlSessionExtension = new OBMySQLSessionExtension();
assertEquals("12345", obmysqlSessionExtension.getConnectionId(connection));
}

@Test
public void testGetConnectionIdWithNullConnection() {
OBMySQLSessionExtension obmysqlSessionExtension = new OBMySQLSessionExtension();
assertThrows(NullPointerException.class, () -> obmysqlSessionExtension.getConnectionId(null));
}

@Test
public void testGetConnectionIdWithOceanBaseConnection() {
OceanBaseConnection oceanBaseConnection = createMockOceanBaseConnection();
OBMySQLSessionExtension obmysqlSessionExtension = new OBMySQLSessionExtension();
assertEquals("12345", obmysqlSessionExtension.getConnectionId(oceanBaseConnection));
}

}