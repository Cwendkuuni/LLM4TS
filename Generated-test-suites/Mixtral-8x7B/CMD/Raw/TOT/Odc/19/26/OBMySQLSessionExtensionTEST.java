import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.PluginManager;
import org.pf4j.testing.AutoPluginManager;
import org.pf4j.testing.ConfigurationBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OBMySQLSessionExtensionTest {

 private PluginManager pluginManager;
 private OBMySQLSessionExtension obMySQLSessionExtension;
 private Connection connection;

 @Before
 public void setUp() throws Exception {
 // Initialize plugin manager
 ConfigurationBuilder builder = new ConfigurationBuilder();
 builder.put("pluginArtifact", "target/classes/oceanbase-plugin-connect-obmysql-4.2.0.jar");
 pluginManager = new AutoPluginManager(builder.build());
 pluginManager.startPlugins();

 // Get the extension
 obMySQLSessionExtension = pluginManager.getExtensionPoint(OBMySQLSessionExtension.class).getUniqueExtension();

 // Create a MySQL connection
 Class.forName("com.mysql.cj.jdbc.Driver");
 connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "testuser", "testpassword");
 }

 @Test
 public void testKillQuery() throws SQLException {
 // Create a query that takes some time
 String query = "SELECT SLEEP(5)";
 JdbcTemplate jdbcTemplate = new JdbcTemplate(new SimpleDriverDataSource(connection.getMetaData().getDriver(), connection));
 jdbcTemplate.execute(query);

 // Get connection id
 String connectionId = obMySQLSessionExtension.getConnectionId(connection);

 // Kill the query
 obMySQLSessionExtension.killQuery(connection, connectionId);

 // Verify that the query has been killed
 assertEquals("Kill Query should return 0 if successful", 0, jdbcTemplate.queryForObject("SELECT ROW_COUNT()", Integer.class));
 }

 @Test
 public void testSwitchSchema() throws SQLException {
 // Check the current schema
 String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
 assertEquals("Current schema should be 'testdb'", "testdb", currentSchema);

 // Switch to another schema
 obMySQLSessionExtension.switchSchema(connection, "anotherschema");

 // Check if the schema has been switched
 currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
 assertEquals("Current schema should be 'anotherschema'", "anotherschema", currentSchema);

 // Switch back to the original schema
 obMySQLSessionExtension.switchSchema(connection, "testdb");

 // Check if the schema has been switched back
 currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
 assertEquals("Current schema should be 'testdb'", "testdb", currentSchema);
 }

 @Test
 public void testGetCurrentSchema() throws SQLException {
 // Check the current schema
 String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
 assertEquals("Current schema should be 'testdb'", "testdb", currentSchema);
 }

 @Test
 public void testGetConnectionId() {
 // Test on a MySQL connection
 String connectionId = obMySQLSessionExtension.getConnectionId(connection);
 assertNull("Connection id should be null for MySQL connections", connectionId);

 // Test on an OceanBaseConnection
 // (Note: This requires an OceanBaseConnection instance, which is not provided in the context)
 }

}