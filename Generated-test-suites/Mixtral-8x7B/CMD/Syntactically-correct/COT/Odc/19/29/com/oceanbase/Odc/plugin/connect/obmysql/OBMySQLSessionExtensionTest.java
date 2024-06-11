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

@SuppressWarnings("unchecked")
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
        when(connection.getMetaData().getURL()).thenReturn("jdbc:mysql://localhost:3306/testSchema");
        obMySQLSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testSwitchSchemaWithNullConnection() {
        assertThrows(NullPointerException.class, () -> obMySQLSessionExtension.switchSchema(null, "testSchema"));
    }

    @Test
    public void testSwitchSchemaWithNullSchemaName() {
        assertThrows(NullPointerException.class, () -> obMySQLSessionExtension.switchSchema(connection, null));
    }

    @Test
    public void testSwitchSchemaWithSameSchema() throws SQLException {
        String schemaName = "testSchema";
        when(connection.getMetaData().getURL()).thenReturn("jdbc:mysql://localhost:3306/" + schemaName);
        when(connection.getCatalog()).thenReturn(schemaName);
        obMySQLSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String schemaName = "testSchema";
        when(connection.getMetaData().getURL()).thenReturn("jdbc:mysql://localhost:3306/" + schemaName);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class))
                .thenReturn(schemaName);
        assertEquals(schemaName, obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetCurrentSchemaWithNullConnection() {
        assertThrows(NullPointerException.class, () -> obMySQLSessionExtension.getCurrentSchema(null));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class))
                .thenReturn("1");
        assertEquals("1", obMySQLSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdWithException() throws SQLException {
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        PowerMockito.mockStatic(JdbcOperationsUtil.class);
        PowerMockito.doThrow(new SQLException()).when(JdbcOperationsUtil.class);
        PowerMockito.queryForObject("select connection_id()", String.class, connection);
        assertEquals("4221", obMySQLSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdWithNullConnection() {
        assertThrows(NullPointerException.class, () -> obMySQLSessionExtension.getConnectionId(null));
    }
}