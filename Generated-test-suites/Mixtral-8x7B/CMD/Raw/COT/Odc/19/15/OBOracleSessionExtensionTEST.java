import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.PluginManager;
import org.pf4j.test.AbstractExtensionTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class OBOracleSessionExtensionTest extends AbstractExtensionTest {

private static final Logger LOGGER = LoggerFactory.getLogger(OBOracleSessionExtensionTest.class);
private Connection connection;
private OBOracleSessionExtension obOracleSessionExtension;

@Before
public void setUp() throws SQLException {
pluginManager = new PluginManager();
pluginManager.loadPlugins();
obOracleSessionExtension = pluginManager.getExtensionPoint(OBOracleSessionExtension.class).get(0);
connection = getConnection();
}

@After
public void tearDown() throws SQLException {
if (connection != null) {
connection.close();
}
}

@Test
public void testKillQuery() throws SQLException {
String connectionId = obOracleSessionExtension.getConnectionId(connection);
obOracleSessionExtension.killQuery(connection, connectionId);
}

@Test
public void testSwitchSchema() throws SQLException {
String schemaName = "test_schema";
String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
obOracleSessionExtension.switchSchema(connection, schemaName);
String newSchema = obOracleSessionExtension.getCurrentSchema(connection);
assertEquals(schemaName, newSchema);
obOracleSessionExtension.switchSchema(connection, currentSchema);
}

@Test
public void testGetCurrentSchema() throws SQLException {
String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
assertNotNull(currentSchema);
}

@Test
public void testGetConnectionId() throws SQLException {
String connectionId = obOracleSessionExtension.getConnectionId(connection);
assertNotNull(connectionId);
}

@Test
public void testGetConnectionIdException() {
assertThrows(SQLException.class, () -> {
obOracleSessionExtension.getConnectionId(null);
});
}

}