import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.PluginManager;
import org.pf4j.junit.AbstractExtensionTestCase;
import org.pf4j.junit.Setup;
import org.pf4j.util.Files;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class OBOracleSessionExtensionTest extends AbstractExtensionTestCase {

 @Setup
 public static void setup(PluginManager pluginManager) {
 File jarFile = Files.getFileFromResourceUrl(ClassLoader.getSystemResource("oboracle-session-extension.jar"));
 pluginManager.loadPlugin(jarFile);
 }

 protected Connection createConnection() throws SQLException {
 return DriverManager.getConnection("jdbc:odps://localhost:port/schema", "username", "password");
 }

 @Before
 public void setUp() throws Exception {
 super.setUp();
 }

 @Test
 public void testKillQuery() throws SQLException {
 Connection connection = createConnection();
 String connectionId = OBOracleSessionExtension.class.getSimpleName() + "Connection";
 OBOracleSessionExtension extension = getExtension(OBOracleSessionExtension.class);
 extension.killQuery(connection, connectionId);
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 Connection connection = createConnection();
 String schemaName = "new_schema";
 OBOracleSessionExtension extension = getExtension(OBOracleSessionExtension.class);
 extension.switchSchema(connection, schemaName);
 String currentSchema = extension.getCurrentSchema(connection);
 assertEquals(schemaName, currentSchema);
 }

 @Test
 public void testGetCurrentSchema() throws SQLException {
 Connection connection = createConnection();
 OBOracleSessionExtension extension = getExtension(OBOracleSessionExtension.class);
 String currentSchema = extension.getCurrentSchema(connection);
 assertEquals(currentSchema, currentSchema);
 }

 @Test
 public void testGetConnectionId() throws SQLException {
 Connection connection = createConnection();
 OBOracleSessionExtension extension = getExtension(OBOracleSessionExtension.class);
 String connectionId = extension.getConnectionId(connection);
 assertThrows(SQLException.class, () -> extension.getConnectionId(null));
 assertEquals(connectionId, connectionId);
 }
}