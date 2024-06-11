import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.PluginManager;
import org.pf4j.junit.AbstractJUnit49PluginTest;
import org.pf4j.junit.CreatePluginManagerRule;
import org.pf4j.junit.CreatePluginManagerRule.StartMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OBMySQLSessionExtensionTest extends AbstractJUnit49PluginTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(OBMySQLSessionExtensionTest.class);

    @CreatePluginManagerRule(startMode = StartMode.EAGER_NO_CLEANUP)
    public static CreatePluginManagerRule pluginManagerRule = new CreatePluginManagerRule();

    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setUp() {
        PluginManager pluginManager = pluginManagerRule.getPluginManager();
        obMySQLSessionExtension = pluginManager.getExtensionPoint(SessionExtensionPoint.class).getExtensions().stream()
                .filter(extension -> extension instanceof OBMySQLSessionExtension)
                .map(extension -> (OBMySQLSessionExtension) extension)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("OBMySQLSessionExtension not found"));
    }

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = null;
        try {
            connection = createDummyConnection();
            obMySQLSessionExtension.killQuery(connection, "12345");
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = createDummyConnection();
        try {
            obMySQLSessionExtension.switchSchema(connection, "testSchema");
            String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
            assertEquals("testSchema", currentSchema);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = createDummyConnection();
        try {
            String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
            assertEquals("testSchema", currentSchema);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = createDummyConnection();
        try {
            String connectionId = obMySQLSessionExtension.getConnectionId(connection);
            assertFalse(connectionId.isEmpty());
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private Connection createDummyConnection() throws SQLException {
        // Create a dummy connection implementation here
        // or use a mocking framework like Mockito to create a mock connection
        // For the sake of example, we'll return a null connection
        return null;
    }
}