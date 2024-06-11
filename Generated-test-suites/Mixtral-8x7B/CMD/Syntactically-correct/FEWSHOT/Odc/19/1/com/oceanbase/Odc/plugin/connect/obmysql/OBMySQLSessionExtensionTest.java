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
        when(JdbcOperationsUtil.getJdbcOperations(connection).execute(connectionId)).thenReturn(true);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String currentSchema = "test";
        String newSchema = "new_test";

        when(connection.getCatalog()).thenReturn(currentSchema);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class))
                .thenReturn(currentSchema);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.switchSchema(connection, newSchema);

        assertEquals(newSchema, connection.getCatalog());
    }

    @Test
    public void testSwitchSchemaSameSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schema = "test";

        when(connection.getCatalog()).thenReturn(schema);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class))
                .thenReturn(schema);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.switchSchema(connection, schema);

        assertEquals(schema, connection.getCatalog());
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schema = "test";

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class))
                .thenReturn(schema);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String result = extension.getCurrentSchema(connection);

        assertEquals(schema, result);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";

        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(Integer.parseInt(connectionId));

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String result = extension.getConnectionId(connection);

        assertEquals(connectionId, result);
    }

    @Test
    public void testGetConnectionIdNull() throws SQLException {
        Connection connection = mock(Connection.class);

        when(connection instanceof OceanBaseConnection).thenReturn(false);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String result = extension.getConnectionId(connection);

        assertNull(result);
    }
}