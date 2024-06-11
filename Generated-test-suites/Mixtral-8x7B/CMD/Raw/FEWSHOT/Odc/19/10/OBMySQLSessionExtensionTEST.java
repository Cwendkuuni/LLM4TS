import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.pf4j.Extension;
import org.powermock.api.mockito.PowerMockito;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@SuppressWarnings("rawtypes")
public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";
        when(JdbcOperationsUtil.getJdbcOperations(connection).execute(PowerMockito.anyString())).thenReturn(null);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(PowerMockito.eq("SELECT DATABASE()"),
                PowerMockito.any())).thenReturn("test_schema");
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(PowerMockito.eq("select connection_id()"),
                PowerMockito.any())).thenReturn(connectionId);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(PowerMockito.eq("SELECT DATABASE()"),
                PowerMockito.any())).thenReturn("test_schema");
        when(connection.setCatalog(PowerMockito.anyString())).thenReturn(true);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.switchSchema(connection, "test_schema");
    }

    @Test
    public void testSwitchSchemaWithSameSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(PowerMockito.eq("SELECT DATABASE()"),
                PowerMockito.any())).thenReturn("test_schema");

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.switchSchema(connection, "test_schema");
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(PowerMockito.eq("SELECT DATABASE()"),
                PowerMockito.any())).thenReturn("test_schema");

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String schema = extension.getCurrentSchema(connection);

        assertEquals("test_schema", schema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(PowerMockito.eq("select connection_id()"),
                PowerMockito.any())).thenThrow(new RuntimeException());
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(12345);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String connectionId = extension.getConnectionId(connection);

        assertEquals("12345", connectionId);
    }

    @Test
    public void testGetConnectionIdWithQueryForObject() throws SQLException {
        Connection connection = mock(Connection.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(PowerMockito.eq("select connection_id()"),
                PowerMockito.any())).thenReturn("12345");

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String connectionId = extension.getConnectionId(connection);

        assertEquals("12345", connectionId);
    }

    @Test
    public void testGetConnectionIdWithNullConnection() {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String connectionId = extension.getConnectionId(null);

        assertNull(connectionId);
    }
}