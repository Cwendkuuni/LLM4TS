import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OBMySQLSessionExtensionTest {

private OBMySQLSessionExtension obMySQLSessionExtension;
private Connection connection;

@Before
public void setUp() {
obMySQLSessionExtension = new OBMySQLSessionExtension();
connection = mock(Connection.class);
}

@Test
public void testKillQuery() throws SQLException {
obMySQLSessionExtension.killQuery(connection, "12345");
}

@Test
public void testSwitchSchema() throws SQLException {
String schemaName = "testSchema";
obMySQLSessionExtension.switchSchema(connection, schemaName);
}

@Test
public void testSwitchSchemaWhenCurrentSchemaIsTargetSchema() throws SQLException {
String schemaName = "testSchema";
when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn(schemaName);
obMySQLSessionExtension.switchSchema(connection, schemaName);
}

@Test
public void testGetCurrentSchema() throws SQLException {
String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
assertEquals(currentSchema, "testSchema");
}

@Test
public void testGetConnectionId() {
String connectionId = obMySQLSessionExtension.getConnectionId(connection);
assertNull(connectionId);
}

@Test
public void testGetConnectionIdWithOceanBaseConnection() {
long threadId = 1234;
obMySQLSessionExtension = new OBMySQLSessionExtension();
Connection oceanBaseConnection = mock(OceanBaseConnection.class);
when(oceanBaseConnection.getServerThreadId()).thenReturn(threadId);
String connectionId = obMySQLSessionExtension.getConnectionId(oceanBaseConnection);
assertEquals(connectionId, Long.toString(threadId));
}
}