import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.PluginManager;
import org.pf4j.testing.AutoConfigurePluginManager;
import org.pf4j.testing.CleanPluginManager;
import org.pf4j.testing.PluginTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@PluginTest
@AutoConfigurePluginManager
@CleanPluginManager
public class OBOracleSessionExtensionTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(OBOracleSessionExtensionTest.class);

    private PluginManager pluginManager;

    @Before
    public void setUp() {
        pluginManager = PluginTest.getPluginManager();
    }

    @Test
    public void testKillQuery() throws SQLException {
        OBOracleSessionExtension extension = pluginManager.getExtensionPoint(OBOracleSessionExtension.class).get();
        Connection connection = null;
        try {
            connection = JdbcOperationsUtil.getConnection();
            extension.killQuery(connection, "123456");
        } finally {
            JdbcOperationsUtil.closeConnection(connection);
        }
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        OBOracleSessionExtension extension = pluginManager.getExtensionPoint(OBOracleSessionExtension.class).get();
        Connection connection = null;
        String schemaName = "testSchema";
        try {
            connection = JdbcOperationsUtil.getConnection();
            extension.switchSchema(connection, schemaName);
            String currentSchema = extension.getCurrentSchema(connection);
            assertEquals(schemaName, currentSchema);
        } finally {
            extension.switchSchema(connection, "");
            JdbcOperationsUtil.closeConnection(connection);
        }
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        OBOracleSessionExtension extension = pluginManager.getExtensionPoint(OBOracleSessionExtension.class).get();
        Connection connection = null;
        String schemaName = "testSchema";
        try {
            connection = JdbcOperationsUtil.getConnection();
            extension.switchSchema(connection, schemaName);
            String currentSchema = extension.getCurrentSchema(connection);
            assertEquals(schemaName, currentSchema);
        } finally {
            extension.switchSchema(connection, "");
            JdbcOperationsUtil.closeConnection(connection);
        }
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        OBOracleSessionExtension extension = pluginManager.getExtensionPoint(OBOracleSessionExtension.class).get();
        Connection connection = JdbcOperationsUtil.getConnection();
        String connectionId = extension.getConnectionId(connection);
        assertNotNull(connectionId);
        JdbcOperationsUtil.closeConnection(connection);
    }
}