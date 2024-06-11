import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.pf4j.ExtensionPoint;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OBMySQLSessionExtensionTest {

private OBMySQLSessionExtension obMySQLSessionExtension;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);
obMySQLSessionExtension = new OBMySQLSessionExtension();
}

@Test
public void testKillQuery() throws SQLException {
Connection connection = mock(Connection.class);
String connectionId = "12345";
obMySQLSessionExtension.killQuery(connection, connectionId);
}

@Test
public void testSwitchSchema() throws SQLException {
Connection connection = mock(Connection.class);
String schemaName = "testSchema";
obMySQLSessionExtension.switchSchema(connection, schemaName);
}

@Test
public void testGetCurrentSchema() throws SQLException {
Connection connection = mock(Connection.class);
String expectedSchema = "testSchema";
when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class))
.thenReturn(expectedSchema);
String actualSchema = obMySQLSessionExtension.getCurrentSchema(connection);
assertEquals(expectedSchema, actualSchema);
}

@Test
public void testGetConnectionId() throws SQLException {
Connection connection = mock(Connection.class);
String expectedConnectionId = "12345";
when(connection.getClass().getName().equals("com.oceanbase.jdbc.OceanBaseConnection"))
.thenReturn(true);
when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(Integer.parseInt(expectedConnectionId));
String actualConnectionId = obMySQLSessionExtension.getConnectionId(connection);
assertEquals(expectedConnectionId, actualConnectionId);
}
}