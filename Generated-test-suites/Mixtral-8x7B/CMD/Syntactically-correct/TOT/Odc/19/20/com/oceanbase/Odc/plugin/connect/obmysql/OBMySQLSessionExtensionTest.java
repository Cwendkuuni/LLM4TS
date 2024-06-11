import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.pf4j.PluginManager;
import org.pf4j.SimplePluginManager;
import org.pf4j.junit.PluginTest;
import org.pf4j.junit.SetPluginManager;
import java.sql.Connection;
import java.sql.SQLException;
import static org.mockito.Mockito.*;

@PluginTest
public class OBMySQLSessionExtensionTest {

private PluginManager pluginManager;
private OBMySQLSessionExtension obMySQLSessionExtension;
private Connection connection;

@Before
@SetPluginManager
public void setup() {
MockitoAnnotations.initMocks(this);
pluginManager = new SimplePluginManager();
pluginManager.loadPlugins();
obMySQLSessionExtension = pluginManager.getExtensionPoint(OBMySQLSessionExtension.class).get();
connection = mock(Connection.class);
}

@Test
public void testKillQuery() throws SQLException {
obMySQLSessionExtension.killQuery(connection, "123456");
verify(connection).createStatement();
verify(connection).createStatement().execute("KILL QUERY 123456");
}

@Test
public void testSwitchSchema() throws SQLException {
String schemaName = "testSchema";
obMySQLSessionExtension.switchSchema(connection, schemaName);
verify(connection).setCatalog(schemaName);
}

@Test
public void testSwitchSchemaWithSameSchema() throws SQLException {
String schemaName = "testSchema";
when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn(schemaName);
obMySQLSessionExtension.switchSchema(connection, schemaName);
verify(connection, never()).setCatalog(anyString());
}

@Test
public void testGetConnectionId() throws SQLException {
when(connection.createStatement()).thenThrow(new SQLException("Mocked SQLException"));
when(connection instanceof OceanBaseConnection).thenReturn(true);
when(connection.createStatement().execute(anyString())).thenReturn(true);
String connectionId = obMySQLSessionExtension.getConnectionId(connection);
assertNotNull(connectionId);
}

@Test
public void testGetConnectionIdWithOceanBaseConnection() throws SQLException {
String connectionId = obMySQLSessionExtension.getConnectionId(connection);
assertNotNull(connectionId);
}
}