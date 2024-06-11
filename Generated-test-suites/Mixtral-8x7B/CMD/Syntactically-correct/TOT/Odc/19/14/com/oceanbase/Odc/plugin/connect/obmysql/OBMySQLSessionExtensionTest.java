import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.PluginManager;
import org.pf4j.junit.AbstractJUnit49PluginRunner;
import org.pf4j.junit.CreatePluginManagerRule;
import org.pf4j.junit.CreatePluginManagerRule.PluginManagerFactory;
import org.pf4j.plugin.PluginWrapper;
import org.pf4j.util.Files;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.Assert.*;

@SuppressWarnings("resource")
public class OBMySQLSessionExtensionTest {

private final PluginManager pluginManager =
new CreatePluginManagerRule(
new PluginManagerFactory() {
@Override
public PluginManager create() {
return new PluginManager();
}
}, "src/test/resources/plugins").create();

@Before
public void setUp() {
pluginManager.startPlugin(new File("../odc-plugin-connect-obmysql/target/odc-plugin-connect-obmysql-4.2.0.jar"));
}

@After
public void tearDown() {
pluginManager.stopPlugin("obmysql");
}

@Test
public void testKillQuery() throws SQLException {
Connection connection =
DriverManager.getConnection("jdbc:odc:obmysql://localhost:4000/testdb?plugin=obmysql", new Properties());
String connectionId = new OBMySQLSessionExtension().getConnectionId(connection);
new OBMySQLSessionExtension().killQuery(connection, connectionId);
}

@Test
public void testSwitchSchema() throws SQLException {
Connection connection =
DriverManager.getConnection("jdbc:odc:obmysql://localhost:4000/testdb?plugin=obmysql", new Properties());
new OBMySQLSessionExtension().switchSchema(connection, "testdb");
String currentSchema = new OBMySQLSessionExtension().getCurrentSchema(connection);
assertEquals("testdb", currentSchema);
}

@Test
public void testGetCurrentSchema() throws SQLException {
Connection connection =
DriverManager.getConnection("jdbc:odc:obmysql://localhost:4000/testdb?plugin=obmysql", new Properties());
String currentSchema = new OBMySQLSessionExtension().getCurrentSchema(connection);
assertEquals("testdb", currentSchema);
}

@Test
public void testGetConnectionId() throws SQLException {
Connection connection =
DriverManager.getConnection("jdbc:odc:obmysql://localhost:4000/testdb?plugin=obmysql", new Properties());
String connectionId = new OBMySQLSessionExtension().getConnectionId(connection);
assertNotNull(connectionId);
}
}