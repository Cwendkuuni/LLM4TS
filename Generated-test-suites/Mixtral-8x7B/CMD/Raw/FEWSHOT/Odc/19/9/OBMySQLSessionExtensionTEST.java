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

        when(extension.getCurrentSchema(connection)).thenReturn("old_schema");

        when(connection.setCatalog(schemaName)).thenReturn(true);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(
                "SELECT DATABASE()", String.class)).thenReturn(schemaName);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String result = extension.getCurrentSchema(connection);
        assertEquals(schemaName, result);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(connection.getClass()).thenReturn(OceanBaseConnection.class);
        when(connection.getClass().getConstructor(Connection.class))
                .thenReturn(oceanBaseConnection.getClass().getConstructor(Connection.class));
        when(oceanBaseConnection.getServerThreadId()).thenReturn(12345);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String result = extension.getConnectionId(connection);
        assertEquals("12345", result);
    }

    @Test
    public void testGetConnectionIdWithException() {
        Connection connection = mock(Connection.class);
        when(connection.getClass()).thenReturn(Connection.class);
        when(connection.getClass().getConstructor(Connection.class))
                .thenThrow(Exception.class);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String result = extension.getConnectionId(connection);
        assertNull(result);
    }
}

class ExtensionTest {

    @Extension
    static class TestExtensionPoint implements SessionExtensionPoint {

        @Override
        public void killQuery(Connection connection, String connectionId) {
        }

        @Override
        public void switchSchema(Connection connection, String schemaName) throws SQLException {
        }

        @Override
        public String getCurrentSchema(Connection connection) {
            return null;
        }

        @Override
        public String getConnectionId(Connection connection) {
            return null;
        }
    }
}