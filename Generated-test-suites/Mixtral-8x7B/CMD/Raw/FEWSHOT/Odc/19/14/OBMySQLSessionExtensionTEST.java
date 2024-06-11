import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.pf4j.Extension;

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
        when(JdbcOperationsUtil.getJdbcOperations(connection).execute(connectionId)).thenReturn(null);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class))
                .thenReturn(connectionId);

        new OBMySQLSessionExtension().killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class))
                .thenReturn(schemaName);
        new OBMySQLSessionExtension().switchSchema(connection, schemaName);

        schemaName = "otherSchema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class))
                .thenReturn(schemaName);
        new OBMySQLSessionExtension().switchSchema(connection, "testSchema");
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class))
                .thenReturn(schemaName);
        assertEquals(schemaName, new OBMySQLSessionExtension().getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()",
                String.class)).thenReturn(connectionId);
        assertEquals(connectionId, new OBMySQLSessionExtension().getConnectionId(connection));

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()",
                String.class)).thenThrow(new RuntimeException());
        when(connection instanceof OceanBaseConnection)
                .thenReturn(true)
                .thenReturn(false);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(Long.parseLong(connectionId));

        assertEquals(connectionId, new OBMySQLSessionExtension().getConnectionId(connection));
        assertNull(new OBMySQLSessionExtension().getConnectionId(connection));
    }
}