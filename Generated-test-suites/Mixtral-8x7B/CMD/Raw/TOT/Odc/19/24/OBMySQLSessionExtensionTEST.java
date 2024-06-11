import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.pf4j.ExtensionPoint;
import org.pf4j.PluginManager;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

@Mock private PluginManager pluginManager;
@Mock private Connection connection;
@Mock private OceanBaseConnection oceanBaseConnection;

private OBMySQLSessionExtension obMySQLSessionExtension;

@Before
public void setup() {
initMocks(this);
obMySQLSessionExtension = new OBMySQLSessionExtension();
obMySQLSessionExtension.setPluginManager(pluginManager);
when(pluginManager.getExtensionPoint(SessionExtensionPoint.class)).thenReturn(obMySQLSessionExtension);
}

@Test
public void testKillQuery() throws SQLException {
String connectionId = "1234";
obMySQLSessionExtension.killQuery(connection, connectionId);
}

@Test
public void testSwitchSchema() throws SQLException {
String schemaName = "testSchema";
when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn("defaultSchema");
obMySQLSessionExtension.switchSchema(connection, schemaName);
}

@Test
public void testSwitchSchema_sameSchema() throws SQLException {
String schemaName = "testSchema";
when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn(schemaName);
obMySQLSessionExtension.switchSchema(connection, schemaName);
}

@Test
public void testSwitchSchema_exception() throws SQLException {
String schemaName = "testSchema";
when(obMySQLSessionExtension.getCurrentSchema(connection)).thenThrow(new SQLException("Test exception"));
obMySQLSessionExtension.switchSchema(connection, schemaName);
}

@Test
public void testGetConnectionId() {
when(connection.getClass().getName()).thenReturn("java.sql.Connection");
when(pluginManager.getExtensionPoint(SessionExtensionPoint.class)).thenReturn(obMySQLSessionExtension);
when(obMySQLSessionExtension.getConnectionId(connection)).thenReturn("1234");
assertEquals("1234", obMySQLSessionExtension.getConnectionId(connection));
}

@Test
public void testGetConnectionId_oceanBaseConnection() {
when(connection.getClass().getName()).thenReturn(OceanBaseConnection.class.getName());
when(pluginManager.getExtensionPoint(SessionExtensionPoint.class)).thenReturn(obMySQLSessionExtension);
when(oceanBaseConnection.getServerThreadId()).thenReturn(1234);
when(connection.unwrap(OceanBaseConnection.class)).thenReturn(oceanBaseConnection);
assertEquals("1234", obMySQLSessionExtension.getConnectionId(connection));
}
}