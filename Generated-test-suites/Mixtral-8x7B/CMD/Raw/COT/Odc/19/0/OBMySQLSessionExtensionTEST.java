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

    private OBMySQLSessionExtension obMySQLSessionExtension;
    private Connection connection;

    @Before
    public void setup() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        connection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        obMySQLSessionExtension.killQuery(connection, "12345");
    }

    @Test
    public void testKillQueryWithNullConnection() {
        assertThrows(NullPointerException.class, () -> obMySQLSessionExtension.killQuery(null, "12345"));
    }

    @Test
    public void testKillQueryWithNullConnectionId() {
        assertThrows(NullPointerException.class, () -> obMySQLSessionExtension.killQuery(connection, null));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(connection.getCatalog()).thenReturn("schema1");

        obMySQLSessionExtension.switchSchema(connection, "schema1");

        when(connection.getCatalog()).thenReturn("schema2");

        obMySQLSessionExtension.switchSchema(connection, "schema2");
    }

    @Test
    public void testSwitchSchemaWithNullConnection() {
        assertThrows(NullPointerException.class, () -> obMySQLSessionExtension.switchSchema(null, "schema1"));
    }

    @Test
    public void testSwitchSchemaWithNullSchemaName() {
        assertThrows(NullPointerException.class, () -> obMySQLSessionExtension.switchSchema(connection, null));
    }

    @Test
    public void testSwitchSchemaWithSameSchemaName() throws SQLException {
        when(connection.getCatalog()).thenReturn("schema1");

        obMySQLSessionExtension.switchSchema(connection, "schema1");
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class))
                .thenReturn("schema1");

        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);

        assertEquals("schema1", currentSchema);
    }

    @Test
    public void testGetCurrentSchemaWithException() {
        PowerMockito.mockStatic(JdbcOperationsUtil.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class))
                .thenReturn("12345");

        String connectionId = obMySQLSessionExtension.getConnectionId(connection);

        assertEquals("12345", connectionId);
    }

    @Test
    public void testGetConnectionIdWithException() throws SQLException {
        PowerMockito.mockStatic(JdbcOperationsUtil.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection))
                .thenThrow(new SQLException("Unable to get connection ID"));

        when(connection instanceof OceanBaseConnection).thenReturn(true);

        String connectionId = obMySQLSessionExtension.getConnectionId(connection);

        assertEquals("0", connectionId);
    }

}