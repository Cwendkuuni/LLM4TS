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

@org.powermock.modules.junit4.PowerMockRunner
public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.killQuery(connection, connectionId);

        PowerMockito.verifyStatic(JdbcOperationsUtil.class);
        JdbcOperationsUtil.getJdbcOperations(connection).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";

        String currentSchema = "original_schema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class))
                .thenReturn(currentSchema);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.switchSchema(connection, schemaName);

        PowerMockito.verifyStatic(Connection.class);
        connection.setCatalog(schemaName);
    }

    @Test
    public void testSwitchSchema_sameSchema() throws SQLException {
        Connection connection = mock(Connection.class);

        String currentSchema = "test_schema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class))
                .thenReturn(currentSchema);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.switchSchema(connection, currentSchema);

        PowerMockito.verifyStatic(Connection.class, PowerMockito.times(0));
        connection.setCatalog(currentSchema);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);

        String expectedSchema = "test_schema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class))
                .thenReturn(expectedSchema);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String actualSchema = extension.getCurrentSchema(connection);

        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId_connectionIdFromQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        String expectedConnectionId = "12345";

        PowerMockito.mockStatic(JdbcOperationsUtil.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class))
                .thenReturn(expectedConnectionId);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String actualConnectionId = extension.getConnectionId(connection);

        assertEquals(expectedConnectionId, actualConnectionId);
    }

    @Test
    public void testGetConnectionId_connectionIdFromOceanBaseConnection() throws SQLException {
        Connection connection = mock(OceanBaseConnection.class);
        String expectedConnectionId = "67890";

        PowerMockito.when(((OceanBaseConnection) connection).getServerThreadId())
                .thenReturn(Integer.parseInt(expectedConnectionId));

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String actualConnectionId = extension.getConnectionId(connection);

        assertEquals(expectedConnectionId, actualConnectionId);
    }

    @Test
    public void testGetConnectionId_nullConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);

        PowerMockito.mockStatic(JdbcOperationsUtil.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class))
                .thenThrow(new SQLException());

        PowerMockito.when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(null);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String actualConnectionId = extension.getConnectionId(connection);

        assertNull(actualConnectionId);
    }
}