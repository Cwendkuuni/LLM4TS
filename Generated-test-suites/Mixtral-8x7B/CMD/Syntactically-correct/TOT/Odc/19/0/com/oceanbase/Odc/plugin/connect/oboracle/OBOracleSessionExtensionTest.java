import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtensionTestHelper;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.PluginManager;
import org.pf4j.test.AbstractExtensionTest;
import java.sql.Connection;
import java.sql.SQLException;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class OBOracleSessionExtensionTest extends AbstractExtensionTest {

    private OBOracleSessionExtension extension;
    private Connection connection;

    @Before
    public void setUp() throws SQLException {
        PluginManager pluginManager = getPluginManager();
        pluginManager.loadPlugin("com.oceanbase.odc.plugin.connect.oboracle");
        extension = pluginManager.getExtension(OBOracleSessionExtension.class);
        connection = OBOracleSessionExtensionTestHelper.getConnection();
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = extension.getConnectionId(connection);
        assertNotNull(connectionId);
        extension.killQuery(connection, connectionId);
        // Add more logic to test if the query is killed successfully
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "schema1";
        extension.switchSchema(connection, schemaName);
        String currentSchema = extension.getCurrentSchema(connection);
        assertEquals(schemaName, currentSchema);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = extension.getCurrentSchema(connection);
        // Add more logic to test if the current schema is retrieved correctly
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = extension.getConnectionId(connection);
        assertNotNull(connectionId);
        // Add more logic to test if the connection id is retrieved correctly when the query fails
        connection.close();
        connection = OBOracleSessionExtensionTestHelper.getConnection();
        connectionId = extension.getConnectionId(connection);
        assertNotNull(connectionId);
    }
}