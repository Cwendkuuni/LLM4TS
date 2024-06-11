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
        when(JdbcOperationsUtil.getJdbcOperations(connection).execute( "KILL QUERY " + connectionId))
                .thenReturn(true);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class))
                .thenReturn("current_schema");
        when(connection.setCatalog(schemaName)).thenReturn(true);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class))
                .thenReturn("test_schema");

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String currentSchema = extension.getCurrentSchema(connection);

        assertEquals(currentSchema, "test_schema");
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        when(connection.getClass().getName()).thenReturn("com.oceanbase.jdbc.OceanBaseConnection");
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class))
                .thenThrow(new SQLException("Exception"));
        when(connection.getClass().getDeclaredMethod("getServerThreadId")).thenReturn(12345);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String connectionId = extension.getConnectionId(connection);

        assertEquals(connectionId, "12345");
    }

    @Test
    public void testGetConnectionIdNormalCase() throws SQLException {
        Connection connection = mock(Connection.class);
        when(connection.getClass().getName()).thenReturn("com.oceanbase.jdbc.OceanBaseConnection");
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class))
                .thenReturn("12345");

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String connectionId = extension.getConnectionId(connection);

        assertEquals(connectionId, "12345");
    }

    @Test
    public void testGetConnectionIdNotOceanBaseConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        when(connection.getClass().getName()).thenReturn("java.sql.Connection");

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String connectionId = extension.getConnectionId(connection);

        assertNull(connectionId);
    }
}