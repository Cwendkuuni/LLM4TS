import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.PluginWrapper;
import org.pf4j.test.PluginTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class OBMySQLSessionExtensionTest extends PluginTest<OBMySQLSessionExtension> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OBMySQLSessionExtensionTest.class);

    private Connection connection;

    @Before
    public void setup() throws SQLException {
        PluginWrapper wrapper = new PluginWrapper() {
            @Override
            public String getPluginId() {
                return "test-obmysql-plugin";
            }
        };
        plugin = new OBMySQLSessionExtension();
        plugin.setPluginWrapper(wrapper);
        startPlugin();

        String url = "jdbc:mysql://localhost:3306/testdb?user=testuser&password=testpassword";
        connection = DriverManager.getConnection(url);
    }

    @Test
    public void testKillQuery() throws SQLException {
        assertFalse(connection.isClosed());

        // Perform a query that takes some time to execute
        String query = "SELECT SLEEP(5)";
        connection.createStatement().executeQuery(query);

        // Get the connection ID
        OBMySQLSessionExtension extension = getPlugin().getPluginObject();
        String connectionId = extension.getConnectionId(connection);
        assertNotNull(connectionId);

        // Kill the query
        extension.killQuery(connection, connectionId);

        // The previous statement should be cancelled
        SQLException sqlException = null;
        try {
            connection.getStatement().getResultSet();
        } catch (SQLException e) {
            sqlException = e;
        }
        assertNotNull(sqlException);
        assertEquals("Statement cancelled", sqlException.getMessage());
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        assertFalse(connection.isClosed());

        // Check the current schema
        OBMySQLSessionExtension extension = getPlugin().getPluginObject();
        String currentSchema = extension.getCurrentSchema(connection);
        assertEquals("testdb", currentSchema);

        // Switch to another schema
        extension.switchSchema(connection, "otherdb");

        // Check if the schema has been changed
        currentSchema = extension.getCurrentSchema(connection);
        assertEquals("otherdb", currentSchema);

        // Switch back to the original schema
        extension.switchSchema(connection, "testdb");

        // Check if the schema has been changed back
        currentSchema = extension.getCurrentSchema(connection);
        assertEquals("testdb", currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        assertFalse(connection.isClosed());

        OBMySQLSessionExtension extension = getPlugin().getPluginObject();

        // Get the connection ID using the JDBC API
        String connectionId = extension.getConnectionId(connection);
        assertNotNull(connectionId);

        // Get the connection ID using the OceanBaseConnection API
        if (connection instanceof OceanBaseConnection) {
            connectionId = ((OceanBaseConnection) connection).getServerThreadId() + "";
            assertEquals(connectionId, extension.getConnectionId(connection));
        } else {
            assertNull(extension.getConnectionId(connection));
        }
    }

    @Test
    public void testGetPluginObject() {
        assertNotNull(getPlugin().getPluginObject());
    }

    @Test
    public void testGetPluginId() {
        assertEquals("test-obmysql-plugin", getPlugin().getPluginId());
    }

    @Test
    public void testExtensionAnnotation() {
        Extension extensionAnnotation = getPlugin().getClass().getAnnotation(Extension.class);
        assertNotNull(extensionAnnotation);
        assertEquals("com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint", extensionAnnotation.value().getName());
    }

    @Test
    public void testSlf4jAnnotation() {
        Slf4j slf4jAnnotation = getPlugin().getClass().getAnnotation(Slf4j.class);
        assertNotNull(slf4jAnnotation);
    }
}