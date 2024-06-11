import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.powermock.api.mockito.PowerMockito;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

public class OBMySQLSessionExtensionTest {

    private Connection connection;
    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setup() {
        connection = mock(Connection.class);
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "1";
        obMySQLSessionExtension.killQuery(connection, connectionId);
    }

    @Test
    public void testKillQueryWithNullConnection() {
        assertThrows(NullPointerException.class, () -> obMySQLSessionExtension.killQuery(null, "1"));
    }

    @Test
    public void testKillQueryWithNullConnectionId() {
        assertThrows(NullPointerException.class, () -> obMySQLSessionExtension.killQuery(connection, null));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        String currentSchema = "currentSchema";
        PowerMockito.mockStatic(JdbcOperationsUtil.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(JdbcOperationsUtil.class));
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class))
                .thenReturn(currentSchema);

        obMySQLSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testSwitchSchemaWithNullConnection() {
        assertThrows(NullPointerException.class, () -> obMySQLSessionExtension.switchSchema(null, "1"));
    }

    @Test
    public void testSwitchSchemaWithNullSchemaName() {
        assertThrows(NullPointerException.class, () -> obMySQLSessionExtension.switchSchema(connection, null));
    }

    @Test
    public void testSwitchSchemaWithSameSchema() throws SQLException {
        String schemaName = "testSchema";
        String currentSchema = schemaName;
        PowerMockito.mockStatic(JdbcOperationsUtil.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(JdbcOperationsUtil.class));
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class))
                .thenReturn(currentSchema);

        obMySQLSessionExtension.switchSchema(connection, schemaName);

        // No exception should be thrown
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = "testSchema";
        PowerMockito.mockStatic(JdbcOperationsUtil.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(JdbcOperationsUtil.class));
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class))
                .thenReturn(currentSchema);

        String result = obMySQLSessionExtension.getCurrentSchema(connection);

        assertEquals(currentSchema, result);
    }

    @Test
    public void testGetCurrentSchemaWithNullConnection() {
        assertThrows(NullPointerException.class, () -> obMySQLSessionExtension.getCurrentSchema(null));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = "1";
        PowerMockito.mockStatic(JdbcOperationsUtil.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(JdbcOperationsUtil.class));
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class))
                .thenReturn(connectionId);

        String result = obMySQLSessionExtension.getConnectionId(connection);

        assertEquals(connectionId, result);
    }

    @Test
    public void testGetConnectionIdWithNullConnection() {
        assertThrows(NullPointerException.class, () -> obMySQLSessionExtension.getConnectionId(null));
    }

    @Test
    public void testGetConnectionIdWithException() throws SQLException {
        PowerMockito.mockStatic(JdbcOperationsUtil.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(JdbcOperationsUtil.class));
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class))
                .thenThrow(new SQLException());

        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(1);

        when(connection.getClass().getName())
                .thenReturn(OceanBaseConnection.class.getName());
        when(connection.getClass().getConstructor(Connection.class)).thenReturn(
                oceanBaseConnection.getClass().getConstructor(Connection.class));
        when(connection.getClass().getConstructor(Connection.class).newInstance(connection)).thenReturn(
                oceanBaseConnection);

        String result = obMySQLSessionExtension.getConnectionId(connection);

        assertEquals("1", result);
    }
}