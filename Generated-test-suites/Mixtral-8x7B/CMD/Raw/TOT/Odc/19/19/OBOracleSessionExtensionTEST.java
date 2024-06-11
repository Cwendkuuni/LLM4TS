
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.PluginManager;
import org.pf4j.testing.TestPluginManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OBOracleSessionExtensionTest {

    private PluginManager pluginManager;
    private OBOracleSessionExtension sessionExtension;
    private DataSource dataSource;
    private Connection connection;

    @Before
    public void setUp() throws Exception {
        pluginManager = new TestPluginManager();
        pluginManager.loadPlugin("com.oceanbase.odc.plugin.connect.oboracle");
        sessionExtension = pluginManager.getExtensionPoint(OBOracleSessionExtension.class).get();
        dataSource = new SingleConnectionDataSource(createConnection(), true);
        connection = dataSource.getConnection();
    }

    @Test
    public void testKillQuery() throws SQLException {
        // setup
        String connectionId = sessionExtension.getConnectionId(connection);
        // exercise
        sessionExtension.killQuery(connection, connectionId);
        // verify
        // ... (check if query is killed)
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        // setup
        String schemaName = "testSchema";
        String currentSchema = sessionExtension.getCurrentSchema(connection);
        // exercise
        sessionExtension.switchSchema(connection, schemaName);
        // verify
        String newSchema = sessionExtension.getCurrentSchema(connection);
        assertEquals(schemaName, newSchema);
        // reset
        sessionExtension.switchSchema(connection, currentSchema);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        // setup
        String schemaName = "testSchema";
        sessionExtension.switchSchema(connection, schemaName);
        // exercise
        String currentSchema = sessionExtension.getCurrentSchema(connection);
        // verify
        assertEquals(schemaName, currentSchema);
        // reset
        sessionExtension.switchSchema(connection, "DEFAULT");
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        // setup
        String connectionId = sessionExtension.getConnectionId(connection);
        // exercise
        String newConnectionId = sessionExtension.getConnectionId(connection);
        // verify
        assertEquals(connectionId, newConnectionId);
        // reset
        newConnectionId = null;
        assertNull(sessionExtension.getConnectionId(connection));
    }

    private Connection createConnection() throws SQLException {
        // ... (create a connection)
    }
}
