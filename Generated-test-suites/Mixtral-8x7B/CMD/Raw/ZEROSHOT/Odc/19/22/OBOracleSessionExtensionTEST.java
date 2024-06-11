import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.PluginManager;
import org.pf4j.junit.AbstractExtensionTest;
import org.pf4j.junit.Setup;
import org.pf4j.util.Files;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OBOracleSessionExtensionTest extends AbstractExtensionTest {

@Setup
public static void setup(PluginManager pluginManager) {
Files.copyRecursively(new File("target/test-classes/extensions"), pluginManager.getPluginPath("test-plugin"));
pluginManager.loadPlugins();
pluginManager.startPlugins();
}

@Before
public void before() throws SQLException {
String url = "jdbc:oracle:thin:@localhost:1521:xe";
String user = "system";
String password = "oracle";
Connection connection = DriverManager.getConnection(url, user, password);
setExtensionContextValue("connection", connection);
}

@Test
public void testKillQuery() throws SQLException {
Connection connection = (Connection) getExtensionContextValue("connection");
String connectionId = new OBOracleSessionExtension().getConnectionId(connection);
Assert.assertNotNull(connectionId);
new OBOracleSessionExtension().killQuery(connection, connectionId);
}

@Test
public void testSwitchSchema() throws SQLException {
Connection connection = (Connection) getExtensionContextValue("connection");
new OBOracleSessionExtension().switchSchema(connection, "test");
String currentSchema = new OBOracleSessionExtension().getCurrentSchema(connection);
Assert.assertEquals("test", currentSchema);
}

@Test
public void testGetCurrentSchema() throws SQLException {
Connection connection = (Connection) getExtensionContextValue("connection");
String currentSchema = new OBOracleSessionExtension().getCurrentSchema(connection);
Assert.assertTrue(StringUtils.isNotBlank(currentSchema));
}

@Test
public void testGetConnectionId() throws SQLException {
Connection connection = (Connection) getExtensionContextValue("connection");
String connectionId = new OBOracleSessionExtension().getConnectionId(connection);
Assert.assertNotNull(connectionId);
}
}