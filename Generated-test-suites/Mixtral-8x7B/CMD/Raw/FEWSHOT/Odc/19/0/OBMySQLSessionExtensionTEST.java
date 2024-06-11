import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.PluginManager;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@SuppressWarnings("unchecked")
public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";
        when(JdbcOperationsUtil.getJdbcOperations(connection).execute(
                "KILL QUERY " + connectionId)).thenReturn(null);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        String currentSchema = "other_schema";

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()",
                String.class)).thenReturn(currentSchema);

        when(connection.setCatalog(schemaName)).thenReturn(true);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.switchSchema(connection, schemaName);

        assertEquals(schemaName, connection.getCatalog());
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String expectedSchema = "test_schema";

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()",
                String.class)).thenReturn(expectedSchema);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String actualSchema = extension.getCurrentSchema(connection);

        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        OceanBaseConnection connection = mock(OceanBaseConnection.class);
        String expectedConnectionId = "12345";

        when(connection.getServerThreadId()).thenReturn(Integer.parseInt(expectedConnectionId));

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String actualConnectionId = extension.getConnectionId(connection);

        assertEquals(expectedConnectionId, actualConnectionId);
    }

    @Test
    public void testGetConnectionId_WithException() throws SQLException {
        Connection connection = mock(Connection.class);
        String expectedConnectionId = null;

        when(connection instanceof OceanBaseConnection).thenReturn(false);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String actualConnectionId = extension.getConnectionId(connection);

        assertEquals(expectedConnectionId, actualConnectionId);
    }
}