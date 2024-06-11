import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.PluginManager;
import org.pf4j.test.AbstractExtensionTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class OBOracleSessionExtensionTest extends AbstractExtensionTest {

    private static final Logger logger = LoggerFactory.getLogger(OBOracleSessionExtensionTest.class);
    private Connection connection;

    @Before
    public void setUp() throws Exception {
        String url = "jdbc:oracle:thin:@//localhost:1521/orcl";
        String user = "scott";
        String password = "tiger";
        connection = DriverManager.getConnection(url, user, password);
        PluginManager pluginManager = getPluginManager();
        pluginManager.loadPlugin("com.oceanbase.odc.plugin.connect.oboracle");
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = new OBOracleSessionExtension().getConnectionId(connection);
        Assert.assertNotNull(connectionId);

        Statement stmt = connection.createStatement();
        stmt.executeQuery("SELECT SLEEP(5) FROM dual");
        new OBOracleSessionExtension().killQuery(connection, connectionId);

        // The query should be killed, so we don't wait for the result.
        stmt.getResultSet();
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "other_schema";
        new OBOracleSessionExtension().switchSchema(connection, schemaName);

        String currentSchema = new OBOracleSessionExtension().getCurrentSchema(connection);
        Assert.assertEquals(schemaName, currentSchema);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = new OBOracleSessionExtension().getCurrentSchema(connection);
        Assert.assertNotNull(currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = new OBOracleSessionExtension().getConnectionId(connection);
        Assert.assertNotNull(connectionId);
    }
}