import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class OBMySQLSessionExtensionTest {

private Connection connection;

@Test
public void testKillQuery() {
// create a mock Connection object
OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
obMySQLSessionExtension.killQuery(connection, "connectionId");
}

@Test
public void testKillQueryWithNullConnection() {
OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
assertThrows(IllegalArgumentException.class, () -> obMySQLSessionExtension.killQuery(null, "connectionId"));
}

@Test
public void testKillQueryWithNullConnectionId() {
OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
assertThrows(IllegalArgumentException.class, () -> obMySQLSessionExtension.killQuery(connection, null));
}

@Test
public void testSwitchSchema() {
// create a mock Connection object
OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
obMySQLSessionExtension.switchSchema(connection, "schemaName");
}

@Test
public void testSwitchSchemaWithNullConnection() {
OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
assertThrows(IllegalArgumentException.class, () -> obMySQLSessionExtension.switchSchema(null, "schemaName"));
}

@Test
public void testSwitchSchemaWithNullSchemaName() {
OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
assertThrows(IllegalArgumentException.class, () -> obMySQLSessionExtension.switchSchema(connection, null));
}

@Test
public void testGetCurrentSchema() {
// create a mock Connection object
OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
obMySQLSessionExtension.getCurrentSchema(connection);
}

@Test
public void testGetCurrentSchemaWithNullConnection() {
OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
assertThrows(IllegalArgumentException.class, () -> obMySQLSessionExtension.getCurrentSchema(null));
}

@Test
public void testGetConnectionId() {
// create a mock Connection object
OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
obMySQLSessionExtension.getConnectionId(connection);
}

@Test
public void testGetConnectionIdWithNullConnection() {
OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
assertThrows(IllegalArgumentException.class, () -> obMySQLSessionExtension.getConnectionId(null));
}

@Test
public void testKillQueryWithEmptyConnectionId() {
OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
assertThrows(IllegalArgumentException.class, () -> obMySQLSessionExtension.killQuery(connection, ""));
}

@Test
public void testSwitchSchemaWithEmptySchemaName() {
OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
assertThrows(IllegalArgumentException.class, () -> obMySQLSessionExtension.switchSchema(connection, ""));
}

@Test
public void testGetConnectionIdWithEmptyConnectionId() {
OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
String connectionId = obMySQLSessionExtension.getConnectionId(connection);
assertEquals(connectionId, ""); // assume that an empty connectionId is returned when the connection doesn't support it
}

@Test
public void testSwitchSchemaWithSameSchemaName() throws SQLException {
// create a mock Connection object
String schemaName = "testSchema";
connection.setCatalog(schemaName);
OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
obMySQLSessionExtension.switchSchema(connection, schemaName);
}

@Test
public void testGetCurrentSchemaWithSameSchemaName() {
// create a mock Connection object
String schemaName = "testSchema";
connection.setCatalog(schemaName);
OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
assertEquals(currentSchema, schemaName);
}

@Test
public void testGetConnectionIdWithConnectionId() {
// create a mock Connection object that supports getting the connection id
String connectionId = "1234";
connection.setClientInfo("connection_id", connectionId);
OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
String actualConnectionId = obMySQLSessionExtension.getConnectionId(connection);
assertEquals(actualConnectionId, connectionId);
}

}