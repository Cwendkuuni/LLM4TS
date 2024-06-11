import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.PluginManager;
import org.pf4j.testing.AutoRegisterPluginManager;
import org.pf4j.testing.TestingExtensionPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class OBOracleSessionExtensionTest {

    private static final Logger logger = LoggerFactory.getLogger(OBOracleSessionExtensionTest.class);

    private PluginManager pluginManager;

    @Before
    public void setup() {
        pluginManager = new AutoRegisterPluginManager();
        pluginManager.loadPluginsFrom("./target/test-classes/plugins");
        pluginManager.startPlugins();
    }

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/orcl", "user", "password");
        String connectionId = new OBOracleSessionExtension().getConnectionId(connection);
        new OBOracleSessionExtension().killQuery(connection, connectionId);
        pluginManager.close();
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/orcl", "user", "password");
        String schemaName = "test_schema";
        new OBOracleSessionExtension().switchSchema(connection, schemaName);
        String currentSchema = new OBOracleSessionExtension().getCurrentSchema(connection);
        assertEquals(schemaName, currentSchema);
        pluginManager.close();
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/orcl", "user", "password");
        String currentSchema = new OBOracleSessionExtension().getCurrentSchema(connection);
        Assert.assertNotNull(currentSchema);
        pluginManager.close();
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/orcl", "user", "password");
        String connectionId = new OBOracleSessionExtension().getConnectionId(connection);
        Assert.assertNotNull(connectionId);
        pluginManager.close();
    }
}