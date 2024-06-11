
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.PluginManager;
import org.pf4j.SimplePluginManager;
import org.pf4j.junit.PluginTest;
import org.pf4j.junit.SetPluginClasspath;
import org.pf4j.plugin.ManifestPlugin;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@ManifestPlugin("obmysql-plugin.jar")
@PluginTest("com.oceanbase.odc.plugin.connect.obmysql.OBMySQLPlugin")
@SetPluginClasspath("obmysql-plugin.jar")
public class OBMySQLSessionExtensionTest {

    private SessionExtensionPoint obMySQLSessionExtension;

    @Before
    public void setUp() {
        PluginManager pluginManager = new SimplePluginManager();
        pluginManager.loadPlugins();
        obMySQLSessionExtension = pluginManager.getExtensionPoint(SessionExtensionPoint.class).get(0);
    }

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:obmysql://localhost:2883/testdb", "user", "password");
        String connectionId = getConnectionId(connection);
        String query = "SELECT SLEEP(10)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.executeQuery();
        obMySQLSessionExtension.killQuery(connection, connectionId);
        // Verify that the query was killed by checking for an exception
        assertThrows(SQLException.class, statement::executeQuery);
    }

    @Test
    public void testKillQueryNullConnectionId() {
        Connection connection = null;
        assertDoesNotThrow(() -> obMySQLSessionExtension.killQuery(connection, null));
    }

    @Test
    public void testKillQueryClosedConnection() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:obmysql://localhost:2883/testdb", "user", "password");
        connection.close();
        assertDoesNotThrow(() -> obMySQLSessionExtension.killQuery(connection, "123"));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:obmysql://localhost:2883/testdb", "user", "password");
        String originalSchema = getCurrentSchema(connection);
        String newSchema = "new_schema";
        obMySQLSessionExtension.switchSchema(connection, newSchema);
        String currentSchema = getCurrentSchema(connection);
        Assert.assertEquals(newSchema, currentSchema);
        obMySQLSessionExtension.switchSchema(connection, originalSchema);
    }

    @Test
    public void testSwitchSchemaToSameSchema() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:obmysql://localhost:2883/testdb", "user", "password");
        String schemaName = getCurrentSchema(connection);
        obMySQLSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testSwitchSchemaNullSchemaName() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:obmysql://localhost:2883/testdb", "user", "password");
        obMySQLSessionExtension.switchSchema(connection, null);
        String currentSchema = getCurrentSchema(connection);
        Assert.assertEquals("testdb", currentSchema);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:obmysql://localhost:2883/testdb", "user", "password");
        String schemaName = getCurrentSchema(connection);
        Assert.assertEquals("testdb", schemaName);
    }

    @Test
    public void testGetCurrentSchemaNullConnection() {
        Connection connection = null;
        String schemaName = obMySQLSessionExtension.getCurrentSchema(connection);
        Assert.assertEquals("", schemaName);
    }

    @Test
    public void testGetCurrentSchemaDefaultSchema() {
        Connection connection = null;
        String schemaName = obMySQLSessionExtension.getCurrentSchema(connection);
        Assert.assertEquals("", schemaName);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:obmysql://localhost:2883/testdb", "user", "password");
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        Assert.assertNotNull(connectionId);
    }

    @Test
    public void testGetConnectionIdNullConnection() {
        Connection connection = null;
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        Assert.assertNull(connectionId);
    }

    @Test
    public void testGetConnectionIdClosedConnection() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:obmysql://localhost:2883/testdb", "user", "password");
        connection.close();
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        Assert.assertNull(connectionId);
    }

}
